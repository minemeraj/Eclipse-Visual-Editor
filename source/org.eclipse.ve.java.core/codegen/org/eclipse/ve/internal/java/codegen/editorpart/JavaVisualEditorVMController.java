/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JavaVisualEditorVMController.java,v $
 *  $Revision: 1.18 $  $Date: 2005-08-24 23:30:47 $ 
 */
package org.eclipse.ve.internal.java.codegen.editorpart;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.*;
import org.eclipse.ui.*;

import org.eclipse.jem.internal.adapters.jdom.JavaModelListener;
import org.eclipse.jem.internal.beaninfo.adapters.BeaninfoNature;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;
import org.eclipse.jem.internal.proxy.initParser.tree.NoExpressionValueException;
import org.eclipse.jem.internal.proxy.remote.ProxyRemoteUtil;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * This class controls the Remote VM's needed for Java Visual Editors. Basically as long as an java ve is open for a project, there will be a spare vm
 * open for that project. Then when it needs a new one (due to a class has changed shape) it uses the spare, and a new spare will be created. This
 * will allow the ve to not have to wait for a new vm. This will save around .5 to 1 second on these kinds of requests.
 * <p>
 * Whenever a spare has been used, a new spare will be created for the next request.
 * <p>
 * However, the spare will need to be recreated if the classpath of the project has changed. This means there may be a delay for an active editor that
 * notices this too and so needs a new vm. The spare may not be ready yet.
 * <p>
 * To save have too many extra vm's hanging around a garbage collect type algorithm will be used. It is a simple one.
 * <ol>
 * <li>Whenever a ve is closed, the close time will be marked for that project.
 * <li>Every five minutes a check will be made of all open editors. For any project that has an open ve editor the spare will be left alone. For any
 * project that has no open editors, the last closed time for that project will be checked. If the time is less than five minutes, that project's
 * spare will left alone. If greater than five minutes then the spare will be closed out. The assumption here is that if it is less than five minutes
 * since the last closed editor for a project then that project may still be active. If it is more than five minutes then that project is considered
 * inactive. So the next usage in that project will cause a wait while the vm is created.
 * </ol>
 * <p>
 * If in debug mode (i.e. ask to start a remote vm in debug mode), then there will be no spares, no five minute cleanup, etc. It will always create a
 * new vm as needed.
 * <p>
 * Note to anyone changing this class in the future.
 * This class uses synchronized often. So to prevent deadlocks make sure that the following order of sync are maintained. 
 * Do not do any sync's out this order:
 * <ol>
 * <li>perProject: The perProject static map. 
 * <li>PerProject instance: A PerProject class instance (from the perProject map values()).
 * <li>PerProject.createJob: The createJob job in a PerProject instance.
 * <li>createJob.join: Join on create job is mutually exclusive with sync(createJob). A deadlock can occur
 * if sync(createJob) then join.
 * </ol>
 * <p>
 * In other words you can do perProject then PerProject instance, or PerProject instance then PerProject.createJob,
 * but you can't do PerProject.createJob then perProject. Even then be careful. For example waiting for a job
 * to complete entails lock on PerProject instance, then join on createJob. createJob will then lock itself to
 * update the spare. If you sync(createJob) then join, the createJob can't sync on itself to update spare, so it
 * won't terminate and the join will never return.
 * 
 * 
 * @since 1.0.0
 */
public class JavaVisualEditorVMController {
	
	/**
	 * This is the result of getRegistry. It contains the registry and 
	 * configuration info (which tells you what containers, plugins, etc. are
	 * available in the classpath).
	 * 
	 * @since 1.0.0
	 */
	public static class RegistryResult {
		public final ProxyFactoryRegistry registry;
		public final IConfigurationContributionInfo configInfo;
		
		public RegistryResult(ProxyFactoryRegistry registry, IConfigurationContributionInfo configInfo) {
			this.registry = registry;
			this.configInfo = configInfo;
			
			try {
				// Try to prime the communication pump. This sends about 36,000 bytes of data.
				// Doing it here means later when needed the communications costs are drastically reduced.
				IExpression exp = registry.getBeanProxyFactory().createExpression();
				((Expression) exp).setTrace(false);	// Never want trace on the startup.
				int i =1000;
				while (i-->0) {
					exp.createArrayCreation(ForExpression.ROOTEXPRESSION, registry.getBeanTypeProxyFactory()
							.getBeanTypeProxy(exp, "java.lang.Object", 1), 0); //$NON-NLS-1$
					exp.createArrayInitializer(0);
				}
				exp.invokeExpression();
			} catch (ThrowableProxy e) {
			} catch (IllegalStateException e) {
			} catch (NoExpressionValueException e) {
			}
			
		}
	}

	/**
	 * Job to create a registry.
	 * 
	 * @since 1.0.0
	 */
	protected abstract static class CreateRegistry extends Job {

		protected String vmName;
		protected int threadPriority = Thread.NORM_PRIORITY;

		protected CreateRegistry(String name, String vmName) {
			super(name);
			this.vmName = vmName;
		}

		protected final IStatus run(IProgressMonitor monitor) {
			final IConfigurationContributionInfo[] contributeInfo = new IConfigurationContributionInfo[1];
			
			if (monitor.isCanceled())
				return Status.CANCEL_STATUS;
			
			// If this is the job for starting the spare VM, reduce the thread priority.
			Thread jobThread = getThread();
			int originalPriorty = jobThread.getPriority();
			if (threadPriority != Thread.NORM_PRIORITY)
				jobThread.setPriority(Thread.MIN_PRIORITY);

			ConfigurationContributorAdapter jcmCont = new ConfigurationContributorAdapter() {

				

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.jem.internal.proxy.core.ConfigurationContributorAdapter#initialize(org.eclipse.jem.internal.proxy.core.IConfigurationContributionInfo)
				 */
				public void initialize(IConfigurationContributionInfo info) {
					contributeInfo[0] = info;
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.jem.internal.proxy.core.ConfigurationContributorAdapter#contributeClasspaths(org.eclipse.jem.internal.proxy.core.IConfigurationContributionController)
				 */
				public void contributeClasspaths(IConfigurationContributionController controller) throws CoreException {
					// Add in the remote vm jar and any nls jars that is required for JBCF itself.
					controller.contributeClasspath(JavaVEPlugin.getPlugin().getBundle(),
							"vm/javaremotevm.jar", IConfigurationContributionController.APPEND_USER_CLASSPATH, true); //$NON-NLS-1$
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.jem.internal.proxy.core.ConfigurationContributorAdapter#contributeToRegistry(org.eclipse.jem.internal.proxy.core.ProxyFactoryRegistry)
				 */
				public void contributeToRegistry(ProxyFactoryRegistry registry) {
					// Call the setup method in the target VM to initialize statics
					// and other environment variables.
					IBeanTypeProxy aSetupBeanTypeProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy(
							"org.eclipse.ve.internal.java.remotevm.Setup"); //$NON-NLS-1$
					IInvokable setupInvokable = aSetupBeanTypeProxy.getInvokable("setup"); //$NON-NLS-1$
					setupInvokable.invokeCatchThrowableExceptions(aSetupBeanTypeProxy);
				}
			};

			try {
				IConfigurationContributor[] contribs = new IConfigurationContributor[] {
						BeaninfoNature.getRuntime(getProject()).getConfigurationContributor(), jcmCont};

				ProxyFactoryRegistry registry = ProxyLaunchSupport.startImplementation(getProject(), vmName, contribs, monitor);
//				ProxyFactoryRegistry registry = IDERegistration.startAnImplementation(contribs, true, getProject(), vmName, null, monitor);
				if (registry == null)
					monitor.setCanceled(true);	// We didn't get a registry. Cancel the job.
				if (monitor.isCanceled()) {
					if (registry != null)
						registry.terminateRegistry();	// Since we were canceled, don't allow this registry to stay alive.
					return Status.CANCEL_STATUS;
				}																				   // types have been added.
				
				registry.getBeanTypeProxyFactory().setMaintainNotFoundTypes(true); // Want to maintain list of not found types so we know when those
				if (monitor.isCanceled()) {
					registry.terminateRegistry();	// Since we were canceled, don't allow this registry to stay alive.
					return Status.CANCEL_STATUS;
				}																				   // types have been added.
				processNewRegistry(registry, contributeInfo[0]);
			} catch (CoreException e) {
				// Wrapper the exception in a status so that when we join we can get the original exception back.
				return new Status(IStatus.ERROR, JavaVEPlugin.getPlugin().getBundle().getSymbolicName(), 16, e.getStatus().getMessage(), e);
			}
			// Restore thread priority to original priority if previously set because of spare vm
			if (threadPriority != Thread.NORM_PRIORITY)
				getThread().setPriority(originalPriorty);
			return Status.OK_STATUS;

		}

		/**
		 * Used by subclasses to further process the new registry.
		 * 
		 * @param registry
		 * 
		 * @since 1.0.0
		 */
		protected abstract void processNewRegistry(ProxyFactoryRegistry registry, IConfigurationContributionInfo info);

		/**
		 * @return Returns the project.
		 */
		protected abstract IProject getProject();
	}

	/**
	 * Per project info. Will be stored in the perProject map. Any access to the class needs to be sync on the perProject member so that it is
	 * thread-safe.
	 * 
	 * @since 1.0.0
	 */
	protected static class PerProject {

		/**
		 * Inactive count at time of last spare activity. Used to determine if it has been more than two 
		 * inactive checks since the last spare was created or since the last editor was 
		 * disposed. Won't be checked except if no editors for a project are open on the inactive check.
		 * <p>
		 * This allows the spare to remain active if and editor was created or disposed of within the last
		 * activity check interval. (Only if no editors were active at the time of the inactive check).
		 * 
		 */
		public long lastActivity;

		/**
		 * The create job, if any, for this project. This will also be used as a semaphore to access the spare member so that both the job and the
		 * PerProject will not access it at the same time.
		 * <p>
		 * Access to createJob member itself needs to be with sync on the PerProject instance so that the job is not manipulated by more than one
		 * thread at a time.
		 */
		public CreateSpareRegistry createJob;

		/**
		 * The project this info is for.
		 */
		public IProject project;

		/**
		 * The spare registry for this project.
		 * <p>
		 * Access to this must be done through sync(createJob).
		 */
		public RegistryResult spare;
		
		/**
		 * Create with a project
		 * 
		 * @param project
		 * 
		 * @since 1.0.0
		 */
		public PerProject(IProject project) {
			this.project = project;
		}
		
		private static final long START_JOB_DELAY = 10000l;

		/**
		 * Get the spare. If there is none, then either wait for the job, or start a new one and wait for it.
		 * 
		 * @return @throws
		 *         CoreException
		 * 
		 * @since 1.0.0
		 */
		public synchronized RegistryResult getSpare() throws CoreException {
			// synced on this so that only one can get a spare at a time.
			// This prevents race access to the job.
			RegistryResult result = null;
			while (true) {
				IStatus status = waitForJob(); // Wait for any outstanding job.
				if (!status.isOK() && !status.equals(Status.CANCEL_STATUS))
					throw new CoreException(status); // Couldn't create it for some reason.
				if (createJob != null) {
					synchronized (createJob) {
						if (spare != null) {
							// We have a spare, return it, but start a new job.
							result = spare;
							spare = null;
							startJob(START_JOB_DELAY); // We want it to wait before starting the next job so that we don't immediately interfere with user
											 // that is getting this registry.
							break;
						}
					}
				}

				// We don't have a spare waiting. We need to start/schedule a job. Only do this if job doesn't exist or is not running.
				// If sleeping, running, or waiting then waitForJob at top of loop will take care of it.
				if (createJob == null || createJob.getState() == Job.NONE)
					startJob(0L); // Start it immediately
			}
			return result;
		}

		private synchronized IStatus waitForJob() {
			// Join the job, if there is one, so that we can get the spare.
			if (createJob == null)
				return Status.OK_STATUS; // No job is ok.

			int state = createJob.getState();
			if (state == Job.WAITING || state == Job.SLEEPING) {
				// We need to force immediate evaluation. We want it now.
				// No direct way to reschedule a job except to put it to sleep and then wake it up.
				if (createJob.sleep()) {
					// We could put it to sleep, so wake it up.
					createJob.wakeUp(); // Immediately wake up and process.
				}
			}

			while (true) {
				try {
					createJob.join();
					IStatus status = createJob.getResult();
					if (status == null)
						return Status.OK_STATUS;	// Job never ran treat as ok.
					if (!status.isOK()) {
						synchronized (createJob) {
							createJob = null; // Clear it out so that next request will try again. We don't want to permanently lock out spare creations.
							if (spare != null) {
								// There shouldn't be a spare, but to be on safe side, get rid of it.
								spare.registry.terminateRegistry();
								spare = null;
							}
						}
					}
					return status;
				} catch (InterruptedException e) {
				}
			}
		}

		private void startJob(long delay) {
			// Start the job. It is assumed the job is already ended. Should not be called otherwise.
			if (createJob == null) {
				createJob = new CreateSpareRegistry(CodegenEditorPartMessages.JavaVisualEditorPart_CreateRemoteVMForJVE); 
				createJob.setSystem(true);	// Don't want to interrupt user with these being generated. They happen all of the time.
			}
			// If the delay is for the spare remote vm, set the job thread priority low (see CreateRegistry.run(IProgressMonitor))
			if (delay == START_JOB_DELAY)
				createJob.threadPriority = Thread.MIN_PRIORITY;
			else
				createJob.threadPriority = Thread.NORM_PRIORITY;
			createJob.schedule(delay);
		}
		
		/*
		 * Restart the spare (i.e. terminate old one and start new one)
		 * 
		 * @since 1.0.0
		 */
		void restartSpare() {
			if (createJob != null) {
				synchronized (createJob) {
					if (spare != null) {
						spare.registry.terminateRegistry();
						spare = null;
					}
				}
			}
			startJob(START_JOB_DELAY);
		}
		
		/**
		 * Get the existing spare but
		 * don't start a new one, and don't clear the spare out.
		 * <p>
		 * If cancel is true, then cancel any job that is starting a vm
		 * and don't wait for it to finish. If there is one available, return
		 * the spare. 
		 * 
		 * @param cancel waiting. Cancel any job that is waiting to start a vm.
		 * @since 1.0.0
		 */
		public RegistryResult getExistingSpare(boolean cancel) {
			if (cancel && createJob != null)
				createJob.cancel();
			else {
				IStatus status = waitForJob(); // Wait for any outstanding job.
				// If bad status but not cancel, then no spare. If cancel, slight possibility of a spare 
				// depending on when it was canceled.
				if (!status.isOK() && status.getSeverity() != IStatus.CANCEL)
					return null;
			}
			if (createJob != null && createJob.getState() == Job.NONE) {
				synchronized (createJob) {
					if (spare != null)
						return spare;
				}
			}
			
			return null;
		}
		
		public void terminateSpare() {
			
		}

		/**
		 * Job for creating the spare registry.
		 * 
		 * @since 1.0.0
		 */
		protected class CreateSpareRegistry extends CreateRegistry {

			public CreateSpareRegistry(String name) {
				super(name, "VM for " + PerProject.this.project.getName()); //$NON-NLS-1$
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.ve.internal.java.codegen.editorpart.JavaVisualEditorVMController.CreateRegistry#getProject()
			 */
			protected IProject getProject() {
				return PerProject.this.project;
			}

			/*
			 *  (non-Javadoc)
			 * @see org.eclipse.ve.internal.java.codegen.editorpart.JavaVisualEditorVMController.CreateRegistry#processNewRegistry(org.eclipse.jem.internal.proxy.core.ProxyFactoryRegistry, org.eclipse.jem.internal.proxy.core.IConfigurationContributionInfo)
			 */
			protected void processNewRegistry(ProxyFactoryRegistry registry, IConfigurationContributionInfo info) {
				synchronized (this) {
					spare = new RegistryResult(registry, info);
					// Set it to the current inactive count so we know how long ago it was.
					lastActivity = INACTIVE_COUNT;	
				}
			}
		}

	}

	/*
	 * Inactive check period.
	 */
	private static final long INACTIVE_PERIOD = 5 * 60 * 1000L;
	
	/**
	 * Count of inactive checks performed. Every <code>INACTIVE_PERIOD</code> a check will be made for open Java VE editors.
	 * If none are open for a particular project (one that we have a PerProject instance for), we will check the 
	 * lastCreate flag and see if it is more than two less than the current <code>INACTIVE_COUNT</code>. If so then
	 * this PerProject will be terminated because that project hasn't been used in over INACTIVE_PERIOD time. (Use
	 * two less than the count so we can be sure it wasn't within the last period, but the one before that).
	 */
	protected static int INACTIVE_COUNT;
	
	/**
	 * The perProject map. key->IProject, value->PerProject
	 * <p>
	 * Access and updates to this map needs to be synced on the map.
	 */
	protected static Map PER_PROJECT;

	protected final static boolean IN_DEBUG_MODE;

	static {
		IN_DEBUG_MODE = "true".equalsIgnoreCase(Platform.getDebugOption(ProxyPlugin.getPlugin().getBundle().getSymbolicName() + ProxyRemoteUtil.DEBUG_VM)); //$NON-NLS-1$
		if (!IN_DEBUG_MODE)
			PER_PROJECT = new HashMap();
	}


	/**
	 * Job for creating the debug registry.
	 * 
	 * @since 1.0.0
	 */
	protected static class CreateDebugRegistry extends CreateRegistry {

		protected IProject project;
		
		public RegistryResult result;

		public CreateDebugRegistry(String name, IFile file) {
			super(name, "VM for " + file.getFullPath().lastSegment()); //$NON-NLS-1$
			project = file.getProject();
		}

		/*
		 *  (non-Javadoc)
		 * @see org.eclipse.ve.internal.java.codegen.editorpart.JavaVisualEditorVMController.CreateRegistry#processNewRegistry(org.eclipse.jem.internal.proxy.core.ProxyFactoryRegistry, org.eclipse.jem.internal.proxy.core.IConfigurationContributionInfo)
		 */
		protected void processNewRegistry(ProxyFactoryRegistry registry, IConfigurationContributionInfo info) {
			result = new RegistryResult(registry, info);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ve.internal.java.codegen.editorpart.JavaVisualEditorVMController.CreateRegistry#getProject()
		 */
		protected IProject getProject() {
			return project;
		}
	}

	/**
	 * The java model and resource change listener. It listens for changes to the classpaths of any project of interest.
	 * If there are no projects of interest, the listener will be removed and thrown away. It is used
	 * to determine whether to dispose and rebuild a spare or whether to close a spare and get rid of
	 * a project info.
	 * <p>
	 * The resource change listener part listens for project pre-closing and pre-delete so
	 * that the vm's for that project can be terminated. Need to be done separately from
	 * the java model listener because the model listener receives the notification after
	 * the delete has already been done, but the delete will fail because the remote vm
	 * is holding onto things.
	 * <p>
	 * This must be accessed under sync(perProject) so that we don't cause collisions.
	 */
	protected static ChangeListener JAVA_MODEL_LISTENER;
	
	/**
	 * Called by JavaVisualEditorPart to get the next registry that it needs.
	 * <p>
	 * <package-protected> because only the JavaVisualEditorPart should call it.
	 * 
	 * @param file
	 * @return
	 * @throws CoreException
	 * 
	 * @since 1.0.0
	 */
	static RegistryResult getRegistry(IFile file) throws CoreException {
stopDeadlock(file);		
		if (IN_DEBUG_MODE)
			return createRegistryForDebug(file);
	
		PerProject perProjectEntry = null;
		IProject project = file.getProject();
	
		synchronized (PER_PROJECT) {
			if (PER_PROJECT.isEmpty()) {
				// It is empty and we are about to add a project. So we want a java model listener to know if
				// any projects of interest have changed their classpaths. And we want inactive job to check
				// for inactive projects.
				if (JAVA_MODEL_LISTENER == null)
					addJavaModelListener();
				if (INACTIVE_JOB == null)
					addInactiveJob();
				JavaVEPlugin.getPlugin().setJavaVMControllerDisposer(new Runnable() {

					public void run() {
						dispose();
					}
				});	// We are now active
			}
			
			perProjectEntry = (PerProject) PER_PROJECT.get(project);
			if (perProjectEntry == null)
				PER_PROJECT.put(project, perProjectEntry = new PerProject(project));
		}
	
		return perProjectEntry.getSpare();
	}
	
	
	
	private static void stopDeadlock(IFile file) {
		// This is just a temp kludge to try to stop a deadlock. Get all of the classpath containers
		// for this project initialized before locking anything.
		IProject project = file.getProject();
		IJavaProject jproject = JavaCore.create(project);
		try {
			jproject.getResolvedClasspath(true);
		} catch (JavaModelException e) {
		}
		
	}

	
	
	/**
	 * This is called by the JavaVisualEditorPart to tell us that it is being disposed, and this is
	 * the file that it was working on. This is used to update lastActivity counter for the project. That is 
	 * so that we know that within this inactivity check period there was activity.
	 * <p>
	 * <package-protected> because only the JavaVisualEditorPart should call it.
	 * 
	 * @param file
	 * 
	 * @since 1.0.0
	 */
	static void disposeEditor(IFile file) {
		if (IN_DEBUG_MODE)
			return; 
		
		synchronized (PER_PROJECT) {
			IProject project = file.getProject();
			
			PerProject perProjectEntry = (PerProject) PER_PROJECT.get(project);
			if (perProjectEntry != null)
				perProjectEntry.lastActivity = INACTIVE_COUNT;
		}
	}
	
	/**
	 * We are going down so get rid of everything.
	 * This is called by the JavaVEPlugin when it is being stopped. It is public only so that
	 * the JavaVEPlugin can call it. It should not be called by anyone else.
	 * 
	 * @since 1.0.0
	 */
	public static void dispose() {
		deactivate();
	}
	
	/**
	 * Deactivate the controller.
	 * 
	 * 
	 * @since 1.0.0
	 */
	protected static void deactivate() {
		synchronized(PER_PROJECT) {
			if (INACTIVE_JOB != null) {
				Job inactive = INACTIVE_JOB;
				INACTIVE_JOB = null;
				inactive.cancel();
			}
			
			if (JAVA_MODEL_LISTENER != null) {
				JavaCore.removeElementChangedListener(JAVA_MODEL_LISTENER);
				ResourcesPlugin.getWorkspace().removeResourceChangeListener(JAVA_MODEL_LISTENER);
				JAVA_MODEL_LISTENER = null;
			}
			
			if (!PER_PROJECT.isEmpty()) {
				for (Iterator iter = PER_PROJECT.values().iterator(); iter.hasNext();) {
					PerProject pp = (PerProject) iter.next();
					synchronized (pp) {
						try {
							// Sync on pp so no other changes occur to it while we check for spare and recreate if needed.
							RegistryResult spare = pp.getExistingSpare(true);
							if (spare != null)
								spare.registry.terminateRegistry();
						} catch (RuntimeException e) {
							// Just keep going. We want to shut the rest down.
						}
					}
				}
				PER_PROJECT.clear();
			}
		}
		JavaVEPlugin.getPlugin().setJavaVMControllerDisposer(null);
	}
	
	/*
	 * Listener for both JavaModel and Resource Change.
	 * [74714]
	 * 
	 * @since 1.0.2
	 */
	private static class ChangeListener extends JavaModelListener implements IResourceChangeListener {
				
		public ChangeListener() {
			super(ElementChangedEvent.POST_CHANGE);
		}
		
		protected IJavaProject getJavaProject() {
			return null;	// We never use this here since we are handling all projects.
		}

		/*
		 *  (non-Javadoc)
		 * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
		 */
		public void resourceChanged(IResourceChangeEvent e) {
			// About to close or delete the project and it is ours, so we need to cleanup.
			IProject project = (IProject) e.getResource();
			processRemovedProject(project, true);	// Need to wait because the project is going away and we can't have the registry hold onto files.
			processChangedReferencedProject(project);
		}

		
		/* (non-Javadoc)
		 * @see org.eclipse.jem.internal.adapters.jdom.JavaModelListener#processJavaElementChanged(org.eclipse.jdt.core.IJavaProject, org.eclipse.jdt.core.IJavaElementDelta)
		 */
		protected void processJavaElementChanged(IJavaProject element, IJavaElementDelta delta) {
			IProject project = element.getProject();
			switch (delta.getKind()) {
				case IJavaElementDelta.REMOVED:
					processRemovedProject(project, true);	// Need to wait because the project is going away and we can't have the registry hold onto files.
					processChangedReferencedProject(project);
					break;
				case IJavaElementDelta.ADDED:
					processChangedReferencedProject(project);
					break;
				case IJavaElementDelta.CHANGED:
					if ((delta.getFlags() & IJavaElementDelta.F_CLOSED) != 0) {
						// Treat as a project removed.
						processRemovedProject(project, false);
						processChangedReferencedProject(project);
					} else if (isClasspathResourceChange(delta)) {
						// This means this project's .classpath file was changed. This is a major
						// change (though it could be simply attach source), so to be on the safe
						// side process any referenced project like an add or remove. Plus the project
						// itself.
						processChangedProject(project);
						processChangedReferencedProject(project);	
					} else {
						processChildren(element, delta);
					}
					break;
			}
		}
		
		protected void processJavaElementChanged(IPackageFragmentRoot element, IJavaElementDelta delta) {
			if (isClassPathChange(delta)) {
				// This means this project's classpath (a container changed some element of the container) was changed. This is a major
				// change (though it could be simply attach source), so to be on the safe
				// side process any referenced project like an add or remove. Plus the project
				// itself.
				IProject project = element.getJavaProject().getProject();
				processChangedProject(project);
				processChangedReferencedProject(project);	
			}
		}
		
		/**
		 * Process the changed referenced project. This will see if any existing perProject info
		 * references this project, and if it does, it will restart the spare for it. The change
		 * could be because the project was added(or opened), removed(or closed), or its classpath
		 * was changed. In those cases any projects that reference this project must have their
		 * spares restarted (if they have one).
		 *  
		 * @param project
		 * @return
		 * 
		 * @since 1.0.0
		 */
		protected void processChangedReferencedProject(IProject project) {
			IPath projectPath = project.getFullPath();
			synchronized(PER_PROJECT) {
				if (!PER_PROJECT.isEmpty()) {
					for (Iterator iter = PER_PROJECT.values().iterator(); iter.hasNext();) {
						PerProject pp = (PerProject) iter.next();
						// If this perproject is for this project, then don't bother because it is not a referenced change.
						if (!pp.project.equals(project)) {
							// However, if we are creating one, cancel it. It may not need to be canceled, but we can't
							// tell because we don't have the necessary info for it until it completes. But we can't wait
							// for it to truly complete because we could get into a deadlock situation with the builder.
							RegistryResult spare = pp.getExistingSpare(true);
							if (spare == null || (spare.configInfo != null && spare.configInfo.getProjectPaths().containsKey(projectPath)))
								pp.restartSpare();
						}
					} 
				} 
			}
		}	
		
		/**
		 * This processes the project itself. If a project is changed (classpath has been touched),
		 * then we want to terminate the spare and restart it.
		 * 
		 * @param project
		 * 
		 * @since 1.0.0
		 */
		protected void processChangedProject(IProject project) {
			synchronized (PER_PROJECT) {
				PerProject pp = (PerProject) PER_PROJECT.get(project);	
				if (pp != null) {
					// Cancel any being built too because we are going to restart it.
					pp.getExistingSpare(true);
					pp.restartSpare();
				}
			}
		}
		
		/**
		 * The project itself has been removed. We want to remove the perProject info for it and we
		 * want to terminate the spare for it.
		 * 
		 * @param project
		 * 
		 * @since 1.0.0
		 */
		protected void processRemovedProject(IProject project, boolean wait) {
			synchronized (PER_PROJECT) {
				PerProject pp = (PerProject) PER_PROJECT.remove(project);	// Get rid of it.
				if (pp != null) {
					RegistryResult spare = pp.getExistingSpare(true);
					if (spare != null)
						spare.registry.terminateRegistry(wait);
				}
				if (PER_PROJECT.isEmpty())
					deactivate();	// We have no more projects, so let's deactivate and not waste resources.					
			}
		}			
		
	}
	
	/**
	 * Add the java model listener to listen for project changes.
	 * <p>
	 * This must be called under sync(PER_PROJECT)
	 * @since 1.0.0
	 */
	protected static void addJavaModelListener() {
		// It is assumed that we are sync(PER_PROJECT) at this point.
		// Creating it actually adds it to listening.
		JAVA_MODEL_LISTENER = new ChangeListener();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(JAVA_MODEL_LISTENER, IResourceChangeEvent.PRE_CLOSE | IResourceChangeEvent.PRE_DELETE);
	}
	
	/**
	 * Inactive checker job.
	 */
	protected static Job INACTIVE_JOB;
	
	/**
	 * Add the inactive job to check for inactive projects.
	 * 
	 * 
	 * @since 1.0.0
	 */
	protected static void addInactiveJob() {
		// It is assumed that we are sync(perProject) at this point.
		INACTIVE_JOB = new Job(CodegenEditorPartMessages.JavaVisualEditorVMController_InactiveVMCheckJob_Text) { 

			protected IStatus run(IProgressMonitor monitor) {
				try {
					INACTIVE_COUNT++;
					// Has three phases:
					// Phase 1 will gather projects active
					// Phase 2 will get rid of inactive ones.
					// Phase 3 will deactivate the controller if no projects are left.
					monitor.beginTask("", 300); //$NON-NLS-1$
					// Accumulate list of projects that we have in perProjects that have an active ve.
					// Those not listed are the ones that may need to be cleaned up.
					Set activeProjects = new HashSet();
					IWorkbenchWindow[] ww = PlatformUI.getWorkbench().getWorkbenchWindows();
					IProgressMonitor subm = new SubProgressMonitor(monitor, 100);
					subm.beginTask("", ww.length * 100); //$NON-NLS-1$
					for (int i = 0; i < ww.length; i++) {
						if (subm.isCanceled())
							return Status.CANCEL_STATUS;
						IWorkbenchPage[] wwpages = ww[i].getPages();
						for (int j = 0; j < wwpages.length; j++) {
							IEditorReference[] ers = wwpages[j].getEditorReferences();
							for (int k = 0; k < ers.length; k++) {
								IEditorPart ep = ers[k].getEditor(false);
								if (ep instanceof JavaVisualEditorPart) {
									// We have an active Java VE.
									IFile jf = ((IFileEditorInput) ep.getEditorInput()).getFile();
									if (!activeProjects.contains(jf.getProject())) {
										// A new active project
										activeProjects.add(jf.getProject());
									}
								}
							}
						}
						subm.worked(100);
					}

					if (subm.isCanceled())
						return Status.CANCEL_STATUS;
					subm.done();

					// Phase 2: Now we have all active projects, go through the project infos and remove those that have been inactive for more than one period.
					synchronized (PER_PROJECT) {
						if (monitor.isCanceled())
							return Status.CANCEL_STATUS;	// We can skip phase 3 because we haven't processed any projects yet.
						int clearCount = INACTIVE_COUNT - 2;
						subm = new SubProgressMonitor(monitor, 100);
						subm.beginTask("", PER_PROJECT.size() * 100); //$NON-NLS-1$
						for (Iterator iter = PER_PROJECT.values().iterator(); iter.hasNext();) {
							if (subm.isCanceled())
								break;	// Don't go any further here, but we still need to check if empty so that we can deactive, even though canceling.
							PerProject pp = (PerProject) iter.next();
							if (!activeProjects.contains(pp.project)) {
								// No active editors on this project, see if inactive for a long time.
								boolean removeIt = false;
								synchronized (pp) {
									if (pp.createJob == null && pp.lastActivity <= clearCount) {
										removeIt = true;
									} else {
										RegistryResult rr = pp.getExistingSpare(true); 									
										if (removeIt = pp.lastActivity <= clearCount) {
											if (rr != null) {
												// Terminate the registry now
												rr.registry.terminateRegistry();
											}
										}
									}
									if (removeIt)
										iter.remove();
								}
							}
							subm.worked(100);
						}
						subm.done();

						// Phase 3: deactivate if nothing left.
						if (PER_PROJECT.isEmpty()) {
							INACTIVE_JOB = null; // So that we don't cancel ourselves.
							deactivate();
							monitor.worked(100);
							return Status.OK_STATUS; // Return now so that we don't reschedule ourselves.
						}
						monitor.worked(100);
						monitor.done();
					}

					return !monitor.isCanceled() ? Status.OK_STATUS : Status.CANCEL_STATUS;
				} finally {
					if (INACTIVE_JOB != null)
						this.schedule(INACTIVE_PERIOD); // Reschedule ourselves
				}
			}
		};
		
		INACTIVE_JOB.setSystem(true);	// Don't want to bother users with this.
		INACTIVE_JOB.schedule(INACTIVE_PERIOD);
	}
	
	protected static RegistryResult createRegistryForDebug(IFile file) throws CoreException {
		
		CreateDebugRegistry job = new CreateDebugRegistry(CodegenEditorPartMessages.JavaVisualEditorPart_CreateRemoteVMForJVE, file); 
		job.schedule(); // Start it up
		while (true) {
			try {
				job.join();
				break;
			} catch (InterruptedException e) {
			}
		}
	
		if (job.getResult().isOK())
			return job.result;
		else
			throw new CoreException(job.getResult());
	}

}
