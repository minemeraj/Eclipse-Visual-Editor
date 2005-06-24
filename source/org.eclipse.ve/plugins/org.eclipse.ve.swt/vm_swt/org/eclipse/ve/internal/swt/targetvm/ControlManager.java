/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * $RCSfile: ControlManager.java,v $ $Revision: 1.19 $ $Date: 2005-06-24 16:45:11 $
 */
package org.eclipse.ve.internal.swt.targetvm;



import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

import org.eclipse.jem.internal.proxy.common.*;

import org.eclipse.ve.internal.swt.common.Common;

/**
 * This is the ControlManager. It it is a listener for the control changes and it provides helper routines to quickly access information.
 * <p>
 * It also handles the freeform settings for loc and bounds since those need to be saved and returned as default values when set by freeform override.
 * <p>
 * This is not meant to be subclassed. Users should use ControlManagerExtension for that.
 * @see ControlManager.ControlManagerExtension for extensions.
 * @since 1.1.0
 */
public class ControlManager {

	/**
	 * The purpose of this key is a little complicated. It is possible that the component has an invalid layout data for the current 
	 * layout manager. This key is used to store with the component the original layout data by default when created, or the
	 * one that was explicitly set. Then whenever the layout or some layout data is changed then {@link CompositeManagerExtension#aboutToValidate()}
	 * will be called. This will change all of the children layoutdata back to this key and run verifications. If it fails the verification,
	 * a message will be sent back to the host to know that it is invalid for that layout, and then it will set the layout data to null.
	 * This will allow the layout to at least attempt a layout.
	 */
	protected static final String LAYOUT_DATA_KEY = "org.swt.layoutdata";	//$NON-NLS-1$
	
	protected Control fControl;

	protected ControlManagerFeedbackController feedbackController;

	protected ControlManagerExtension[] extensions;
	
	/**
	 * Dispose the widget. Used by the client to dispose the widget. Normally would call dispose on
	 * the widget directly, but sometimes the widget has already been disposed and the client
	 * doesn't know it, so this tests for this and handles it without error.
	 * <p>
	 * <b>Note:</b> Must be called on UI thread.
	 * @param widget
	 * 
	 * @since 1.1.0
	 */
	public static void disposeWidget(Widget widget) {
		if (!widget.isDisposed())
			widget.dispose();
	}

	/**
	 * All control manager extensions must be a subclass of this. They will be told of significant events and can contribute to them.
	 * 
	 * @since 1.1.0
	 */
	public abstract static class ControlManagerExtension {

		private ControlManager controlManager;

		void setControlManager(ControlManager componentManager) {
			ControlManager old = this.controlManager;
			this.controlManager = componentManager;
			controlManagerSet(old);
		}

		/**
		 * Called when this extension is added or removed from a ControlManager.
		 * 
		 * @param oldManager
		 *            old manager or <code>null</code> if not previously in a manager.
		 * 
		 * @since 1.1.0
		 */
		protected void controlManagerSet(ControlManager oldManager) {

		}

		/**
		 * Get the control manager this is associated with.
		 * 
		 * @return
		 * 
		 * @since 1.1.0
		 */
		public final ControlManager getControlManager() {
			return controlManager;
		}

		/**
		 * Get the control being managed.
		 * 
		 * @return the control being managed or <code>null</code> if not managing a control at this time.
		 * 
		 * @since 1.1.0
		 */
		public final Control getControl() {
			return controlManager != null ? controlManager.getControl() : null;
		}

		/**
		 * The control has moved, or one of its ancestors has moved.
		 * 
		 * 
		 * @since 1.1.0
		 */
		protected void controlMoved() {
		}

		/**
		 * Control was resized.
		 * 
		 * 
		 * @since 1.1.0
		 */
		protected void controlResized() {
		}

		/**
		 * A refresh has been queued up.
		 * 
		 * 
		 * @since 1.1.0
		 */
		protected void refreshQueued() {

		}

		/**
		 * Notification that the control is invalid and is about to be validated.
		 * <p>
		 * This is called by the Feedback controller just before it has re-layedout all of the queued up windows. This will only be called for invalidations
		 * done through the client call to {@link ControlManager#invalidate() invalidate} and to any parents of those controls. It will not be called
		 * for any control that was invalidated through some other means.
		 * <p>
		 * The default is to do nothing. Subclasses may do other things. But be aware that immediately after this call the control will be layed out.
		 * 
		 * @since 1.1.0
		 */
		protected void aboutToValidate() {

		}

		/**
		 * Called when everything is all set up and ready for extensions to add any listeners that they want. These listeners must be cleaned up in
		 * the {@link ControlManager.ControlManagerExtension#controlSet(Component, Component)}.
		 * 
		 * 
		 * @since 1.1.0
		 */
		protected void startComponentListening() {

		}

		/**
		 * Called when a new control is set into the manager.
		 * 
		 * @param oldControl
		 *            old component or <code>null</code> if no old control.
		 * @param newControl
		 *            new component or <code>null</code> if no new control.
		 * 
		 * @since 1.1.0
		 */
		protected void controlSet(Control oldControl, Control newControl) {

		}
	}

	/**
	 * This class manages the feedback from the control managers. It tries to batch them all together so that it can be sent back as one transaction
	 * There is only one per display.
	 * <p>
	 * This is an abstract class. There is a version for SWT 3.0.x and for SWT 3.1 or later. They are in a separate files because we can't load the
	 * 3.1 version if we are in 3.0.x.
	 * 
	 * @since 1.1.0
	 */
	public abstract static class ControlManagerFeedbackController implements ICallback, Runnable {

		/**
		 * Called from the IDE side to create the feedback controller.
		 * 
		 * @param display
		 * @return
		 * @throws ClassNotFoundException
		 * @throws NoSuchMethodException
		 * @throws SecurityException
		 * @throws InvocationTargetException
		 * @throws IllegalAccessException
		 * @throws InstantiationException
		 * @throws IllegalArgumentException
		 * 
		 * @since 1.1.0
		 */
		public static ControlManagerFeedbackController createFeedbackController(Display display) throws ClassNotFoundException, SecurityException,
				NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
			Class feedbackControllerClass;
			if (SWT.getVersion() >= 3100) {
				// It is 3.1 or greater. We can use the new one.
				feedbackControllerClass = Class.forName("org.eclipse.ve.internal.swt.targetvm.ControlManagerFeedbackController_GreaterThan_30");
			} else {
				// It is 3.0.x. Need to use the old one.
				feedbackControllerClass = Class.forName("org.eclipse.ve.internal.swt.targetvm.ControlManagerFeedbackController_30");
			}
			Environment environment = Environment.getEnvironment(display);
			Constructor ctor = feedbackControllerClass.getConstructor(new Class[] { Environment.class});
			return (ControlManagerFeedbackController) ctor.newInstance(new Object[] {environment});
		}

		protected IVMServer fServer;

		protected int fCallbackID;

		protected Environment environment; 

		private boolean changesHeld; // Changes are being queued.

		private List transactions = new ArrayList(); // List of transactions to send.

		private Map uniquesMap = new HashMap(); // Map of unique transactions (ControlManager,ID, index in transactions array). (key and value the
												// same object). Used to keep only the last unique one in list.

		/*
		 * The Unique set object for the uniquesSet. The hash key for it will be for (Notifier,ID). It will also contain the index, which will be
		 * changed if a new unique comes along.
		 */
		private static class UniqueEntry {

			protected final Object notifier;

			protected final int ID;

			private int index;

			/**
			 * Construct with cm and ID.
			 * 
			 * @param notifier
			 * @param ID
			 * 
			 * @since 1.1.0
			 */
			public UniqueEntry(Object notifier, int ID) {
				this.notifier = notifier;
				this.ID = ID;
			}

			public int hashCode() {
				return 31 * (31 + notifier.hashCode()) + ID;
			}

			public boolean equals(Object obj) {
				if (obj == this)
					return true;
				else {
					try {
						UniqueEntry ue = (UniqueEntry) obj;
						return (this.notifier == ue.notifier && this.ID == ue.ID);
					} catch (ClassCastException e) {
						// We should never get this because this set will only have UniqueEntry as entries.
						return false;
					}
				}
			}

			/**
			 * @param index
			 *            The index to set.
			 * 
			 * @since 1.1.0
			 */
			public void setIndex(int index) {
				this.index = index;
			}

			/**
			 * @return Returns the index.
			 * 
			 * @since 1.1.0
			 */
			public int getIndex() {
				return index;
			}
		}

		private List queuedRefreshRequests;

		protected Set queuedInvalidControls; // The controls that have been invalidated will be stored in here. 

		private Map controlToControlManagers = new HashMap(); // Map of controls to control managers.

		/**
		 * Construct with environment this controller is associated with.
		 * 
		 * @param environment
		 * 
		 * @since 1.1.0
		 */
		public ControlManagerFeedbackController(Environment environment) {
			this.environment = environment;
		}

		/**
		 * Get the display for the controller
		 * 
		 * @return
		 * 
		 * @since 1.1.0
		 */
		public Display getDisplay() {
			return environment.getDisplay();
		}
		
		/**
		 * Return the environment.
		 * @return
		 * 
		 * @since 1.1.0
		 */
		public Environment getEnvironment() {
			return environment;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jem.internal.proxy.common.ICallback#initializeCallback(org.eclipse.jem.internal.proxy.common.IVMServer, int)
		 */
		public void initializeCallback(IVMServer server, int callbackID) {
			fServer = server;
			fCallbackID = callbackID;
		}

		/**
		 * Register that we have a control to manager relationship.
		 * 
		 * @param component
		 * @param manager
		 * 
		 * @since 1.1.0
		 */
		public void registerComponentManager(Control component, ControlManager manager) {
			controlToControlManagers.put(component, manager);
		}

		/**
		 * Deregister the component to manager relationship.
		 * 
		 * @param component
		 * 
		 * @since 1.1.0
		 */
		public void deregisterComponentManager(Control component) {
			controlToControlManagers.remove(component);
		}

		/**
		 * Queue up for this control as being invalid. The client will eventually call and have all queued up sent back. This will handle both
		 * invalidating images of the control and all of the parents, and it will control the layout so that all who need relayout will be re-layed
		 * out.
		 * 
		 * @param control
		 * 
		 * @since 1.1.0
		 */
		public void invalidateControl(Control control) {
			if (queuedInvalidControls == null) {
				queuedInvalidControls = new HashSet();
			}
			queuedInvalidControls.add(control);
		}

		/**
		 * Validate the controls. This will be called from the UI thread when it is time to validate the controls (layout what is required for the
		 * invalid controls). 
		 * <p>
		 * <b>Note:</b> It is required that the implementation must call {@link ControlManagerFeedbackController#sendAboutToValidateToManager(Control)}
		 * to let the manager know that it is about to be validated.
		 * <p>
		 * <b>Note:</b> Also, because of probably exceptions being thrown during a layout, the implementation should surround each shell validate
		 * with a try/catch(RuntimeException) so that if an error does occur it won't prevent the other shells from being validated.
		 * 
		 * @param invalidControls
		 *            The map of controls that are invalid. Key is shell it is in, value is Collection of controls on that shell. The controls will be valid.
		 * @return collection of controls that have been invalidated. This should include each invalid control, and each parent up to and including
		 *         the shell. Each control/parent/shell should be in the list only once.
		 * @since 1.1.0
		 */
		protected abstract Collection validateControls(Map invalidControls);
		
		/**
		 * Sends the about to validate message to the manager for the control.
		 * <p>
		 * <b>Note:</b> It is the required that {@link ControlManagerFeedbackController#validateControls(Map)} implementers must
		 * call this before it validates the control. It must do this for all invalid controls.
		 *  
		 * @param control
		 * 
		 * @since 1.1.0
		 */
		protected void sendAboutToValidateToManager(Control control) {
			ControlManager cmanager = getControlManager(control);
			if (cmanager != null)
				cmanager.aboutToValidate();
		}

		/**
		 * Return the control manager, if any, for the given control.
		 * 
		 * @param control
		 * @return the control manager or <code>null</code> if there is no one managing that control.
		 * 
		 * @since 1.1.0
		 */
		protected ControlManager getControlManager(Control control) {
			return (ControlManager) controlToControlManagers.get(control);
		}

		private Runnable validateImages = new Runnable() {

			public void run() {
				// This will be on UI thread.
				// Make sure we still have image to go.
				if (queuedInvalidControls != null && !queuedInvalidControls.isEmpty()) {
					// Need to build up collection of shells and their invalid controls:
					// Map: shell->Set(invalidControls)
					Map invalidShells = new HashMap();
					for (Iterator invItr = queuedInvalidControls.iterator(); invItr.hasNext();) {
						Control control = (Control) invItr.next();
						if (control.isDisposed())
							continue;
						Shell shell = control.getShell();
						Set invalids = (Set) invalidShells.get(shell);
						if (invalids == null)
							invalidShells.put(shell, invalids = new HashSet(2));
						invalids.add(control);						
					}

					Collection invalidControls = validateControls(invalidShells);

					// Now send the client component manager invalid images transactions.
					synchronized (transactions) {
						for (Iterator itr = invalidControls.iterator(); itr.hasNext();) {
							Control component = (Control) itr.next();
							ControlManager cmanager = getControlManager(component);
							if (cmanager != null) {
								appendTransaction(cmanager, Common.CL_IMAGEINVALID, null, true);
							}
						}
					}
					queuedInvalidControls.clear();
					ControlManagerFeedbackController.this.run();
				}

			}
		};

		/**
		 * Called from the client to post back all of the invalid images.
		 * 
		 * 
		 * @since 1.1.0
		 */
		public void postInvalidImages() {
			if (queuedInvalidControls != null && !queuedInvalidControls.isEmpty()) {
				// We have something to do, but queue it off to the SWT UI thread so that the validates are done on the UI thread.
				getDisplay().asyncExec(validateImages);
			}
		}

		/**
		 * Add the transaction to the feedback controller and queue it up. These are to be called from ControlManager and ControlManager
		 * subclasses only.
		 * 
		 * @param notifier
		 *            notifier that this transaction is from. This notifier must be registered on the IDE side or else the transaction will be
		 *            ignored.
		 * @param callbackID
		 *            the if of the callback.
		 * @param parms
		 *            array of parms for the callback. <code>null</code> if there are no parms.
		 * @param unique
		 *            <code>true</code> if this is a unique transaction and should replace previous one of same manager/id.
		 * @since 1.1.0
		 */
		public void addTransaction(Object notifier, int callbackID, Object[] parms, boolean unique) {
			appendTransaction(notifier, callbackID, parms, unique);
			boolean queueNow;
			synchronized (this) {
				queueNow = !changesHeld;
			}
			if (queueNow)
				getDisplay().asyncExec(this); // Queue up for later execution.
		}

		/*
		 * Append the transaction to the transaction list.
		 * 
		 * @since 1.1.0
		 */
		private void appendTransaction(Object notifier, int callbackID, Object[] parms, boolean unique) {
			synchronized (transactions) {
				if (unique) {
					UniqueEntry newUE = new UniqueEntry(notifier, callbackID);
					UniqueEntry ue = (UniqueEntry) uniquesMap.get(newUE);
					if (ue != null) {
						// Clear out the old transaction.
						int index = ue.getIndex();
						transactions.set(index++, null);
						transactions.set(index++, null);
						transactions.set(index++, null);
					} else {
						ue = newUE;
						uniquesMap.put(ue, ue);
					}
					ue.setIndex(transactions.size());
				}
				transactions.add(notifier);
				transactions.add(new Integer(callbackID));
				transactions.add(parms != null ? new ICallbackHandler.TransmitableArray(parms) : (Object) parms);
			}
		}

		/**
		 * Queue up the initial refresh request for a manager. The client will call postInitialRefresh when all of the controls are initialized. That
		 * will then take these queued requests and send them.
		 * 
		 * @param manager
		 * 
		 * @since 1.1.0
		 */
		void queueInitialRefresh(ControlManager manager) {
			if (queuedRefreshRequests == null)
				queuedRefreshRequests = new ArrayList();
			queuedRefreshRequests.add(manager);
		}

		/**
		 * This is called by the client. It will take the queued up initial refresh requests and post them to be done.
		 * 
		 * @since 1.1.0
		 */
		public void postInitialRefresh() {
			if (queuedRefreshRequests != null && !queuedRefreshRequests.isEmpty()) {
				final List queued = queuedRefreshRequests;
				getDisplay().asyncExec(new Runnable() {

					public void run() {
						synchronized (transactions) {
							for (int i = 0; i < queued.size(); i++) {
								ControlManager manager = (ControlManager) queued.get(i);
								if (manager.fControl != null && !manager.fControl.isDisposed()) {
									appendTransaction(manager, Common.CL_REFRESHED, manager.getRefreshParms(), true);
									manager.startComponentListening(); // Start the component listening now.
								}
							}
						}

						ControlManagerFeedbackController.this.run(); // Now send all of these back at one time.
					}
				});
			}
			queuedRefreshRequests = null;
		}

		/**
		 * Starting changes. When called by client, it will set the addTransaction request to not do an invokeLater but instead just queue the
		 * transaction. Eventually a postChanges() request will be made and at that time any queued up transaction will be sent. This allows the
		 * client to tell us that there may be multiple changes coming other than new components or invalid images. Such as a size change. This will
		 * allow all size changes to be queued up until the end.
		 * 
		 * @since 1.1.0
		 */
		public synchronized void startingChanges() {
			changesHeld = true;
		}

		/**
		 * Stopping changes. Called by client to indicate all changes held should now be sent.
		 * 
		 * @since 1.1.0
		 */
		public void postChanges() {
			synchronized (this) {
				changesHeld = false;
			}
			getDisplay().asyncExec(this);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			// This should only be called through the event queue. It should not be called directly.
			final Object[] trans;
			synchronized (transactions) {
				if (transactions.isEmpty())
					return; // Nothing to do.
				trans = transactions.toArray();
				transactions.clear();
				uniquesMap.clear();
			}
			try {
				fServer.doCallback(new ICallbackRunnable() {

					public Object run(ICallbackHandler handler) throws CommandException {
						return handler.callbackWithParms(fCallbackID, Common.CL_TRANSACTIONS, trans);
					}
				});
			} catch (CommandException exp) {
				exp.printStackTrace();
			}
		}

		private Set controlListening = new HashSet(); // Set of controls that we are listening to through the control listener.

		private ControlListener controlListener = new ControlListener() {

			public void controlResized(ControlEvent e) {
				ControlManager cm = getControlManager((Control) e.getSource());
				if (cm != null)
					cm.controlResized();
			}

			public void controlMoved(ControlEvent e) {
				// A control has moved, tell all of the children (plus the control itself) that have control managers that they have moved.
				notifyMoved((Control) e.getSource());
			}

			private void notifyMoved(Control control) {
				// Notify this control first so that it gets its new absolute
				// location to the host before the children do. If the other way
				// slight possibility that children will be made relative to
				// the wrong parent offset.
				ControlManager cm = getControlManager(control);
				if (cm != null)
					cm.controlMoved();

				if (control instanceof Composite) {
					Control[] children = ((Composite) control).getChildren();
					for (int i = 0; i < children.length; i++) {
						notifyMoved(children[i]);
					}
				}
			}

		};

		private DisposeListener disposeListener = new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				controlListening.remove(e.getSource());
			}

		};

		/**
		 * Start the component listening for move. It means we have a control in a manager and it needs to know if it or any parent, up to but not
		 * including the shell has moved.
		 * 
		 * @param control
		 * 
		 * @since 1.1.0
		 */
		public void startComponentListening(Control control) {
			Shell ffHost = getEnvironment().getFreeFormHost();
			while (control != null && control != ffHost) {
				if (controlListening.contains(control))
					break; // This control has already started listening, no need to go higher.
				control.addControlListener(controlListener);
				control.addDisposeListener(disposeListener);
				controlListening.add(control);
				control = control.getParent();
			}
		}
	}

	/**
	 * Add the extension. No affect if already in list.
	 * 
	 * @param extension
	 * 
	 * @since 1.1.0
	 */
	public void addExtension(ControlManagerExtension extension) {
		if (extensions == null) {
			extensions = new ControlManagerExtension[] { extension};
			extension.setControlManager(this);
		} else {
			for (int i = 0; i < extensions.length; i++) {
				if (extensions[i] == extension)
					return;
			}
			ControlManagerExtension[] newExtensions = new ControlManagerExtension[extensions.length + 1];
			System.arraycopy(extensions, 0, newExtensions, 0, extensions.length);
			newExtensions[newExtensions.length - 1] = extension;
			extensions = newExtensions;
			extension.setControlManager(this);
		}
	}

	/**
	 * Remove the extension. No affect if not in list.
	 * 
	 * @param extension
	 * 
	 * @since 1.1.0
	 */
	public void removeExtension(ControlManagerExtension extension) {
		if (extensions != null) {
			for (int i = 0; i < extensions.length; i++) {
				if (extensions[i] == extension) {
					if (extensions.length > 1) {
						ControlManagerExtension[] newExtensions = new ControlManagerExtension[extensions.length - 1];
						System.arraycopy(extensions, 0, newExtensions, 0, i);
						int left = newExtensions.length - i;
						if (left > 0)
							System.arraycopy(extensions, i + 1, newExtensions, i, left);
						extensions = newExtensions;
					} else
						extensions = null;
					extension.setControlManager(null);
					return;
				}
			}
		}
	}

	/**
	 * Set the component that this manager is managing.
	 * <p>
	 * <b>Note:</b> This must be called on the UI thread.
	 * @param aControl
	 * 
	 * @since 1.1.0
	 */
	public void setControl(Control aControl, ControlManagerFeedbackController feedbackController) {
		this.feedbackController = feedbackController;
		if (fControl != null) {
			feedbackController.deregisterComponentManager(fControl);
			locOverridden = false; // Since changing controls we should reset to not overridden.
			fControl.setData(LAYOUT_DATA_KEY, null);
		}
		Control oldControl = fControl;
		fControl = aControl;
		if (isValidControl(fControl)) {
			feedbackController.registerComponentManager(fControl, this);
			feedbackController.queueInitialRefresh(this); // Queue up the initial refresh request.			
			fControl.setData(LAYOUT_DATA_KEY, fControl.getLayoutData());	// Save original value.
		}

		if (extensions != null) {
			ControlManagerExtension[] lcl = extensions; // In case it changes to a new array while we are walking it.
			for (int i = 0; i < lcl.length; i++) {
				lcl[i].controlSet(oldControl, fControl);
			}
		}
	}

	/**
	 * Get the control.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected Control getControl() {
		return fControl;
	}

	/**
	 * Return the feedback controller. Used by extensions so they can add transactions.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public ControlManagerFeedbackController getFeedbackController() {
		return feedbackController;
	}

	/**
	 * Called from the client side to invalidate the component. It will also tell the feedback controller about this and the controller will remember
	 * this for it and its parents for marking as an invalid image. The client side will then eventually ask for all invalid images at once.
	 * 
	 * @since 1.1.0
	 */
	public void invalidate() {
		feedbackController.invalidateControl(fControl);
	}

	/**
	 * Notification that the component is invalid.
	 * <p>
	 * This is called by the Feedback controller after it validated all of the queued up windows. This will only be called for invalidations
	 * done through the client call to {@link ControlManager#invalidate() invalidate} and to any parents of those components. It will not be called
	 * for any component that was invalidated through some other means.
	 * <p>
	 * The extensions will be notified.
	 * 
	 * @since 1.1.0
	 */
	protected void aboutToValidate() {
		if (extensions != null) {
			ControlManagerExtension[] lcl = extensions; // In case it changes to a new array while we are walking it.
			for (int i = 0; i < lcl.length; i++) {
				lcl[i].aboutToValidate();
			}
		}
	}

	/**
	 * Called by Feedback controller. This says we've received the initial refresh request, so we can now turn on listening so that we don't miss
	 * anything. This way until this point in time we don't send out extra notifications. Notifications between the setting of the component and the
	 * initial refresh are extraneous and should not be sent since the initial refresh groups them all together.
	 * 
	 * @since 1.1.0
	 */
	protected void startComponentListening() {
		feedbackController.startComponentListening(fControl);
		if (extensions != null) {
			ControlManagerExtension[] lcl = extensions; // In case it changes to a new array while we are walking it.
			for (int i = 0; i < lcl.length; i++) {
				lcl[i].startComponentListening();
			}
		}
	}

	private static final Object[] SHELL_LOCATION = new Object[] { new Integer(0), new Integer(0)};
	
	/**
	 * This will return the location relative to "swt.Shell" parent (This is the "absolute" location for components). If this is a swt.Shell, then the
	 * absolute location will be (0,0).
	 * <p>
	 * This is necessary because sometimes the parent that we add to in the model is not the the real SWT parent. We expect the location to be
	 * relative to that model parent and not the real SWT parent, which we don't know about. So instead we are reporting location relative to the
	 * constant of the top parent. The other side will then use that and the appropriate parent windows location relative to top parent to place this
	 * child window relative to it.
	 * <p>
	 * These coordinates can be compared only if they are from the root window. They don't make sense relative to each other otherwise.
	 * 
	 * @since 1.0.0
	 */
	public Object[] getLocation() {
		if (isValidControl(fControl)) {
			// We may not be in UI thread, but we must.
			if (!(fControl instanceof Shell)) {
				final Object[] result = new Object[2];
				feedbackController.getDisplay().syncExec(new Runnable() {

					public void run() {
						// Map this controls upper left to coordinate relative to upper left corner of shell. Can't
						// use coordinates because of client area of shell and/or control. It returns (0,0) when mapping something at
						// the upper-left of the client area, not the true offset from the upper corner.
						Point s = fControl.getShell().getLocation();
						// The control location in parent is true upper-left, not the control's (0,0) coordinate, because of client area. 
						Point c = fControl.getParent().toDisplay(fControl.getLocation());
						result[0] = new Integer(c.x - s.x);
						result[1] = new Integer(c.y - s.y);
					}
				});
				return result;
			} else 
				return SHELL_LOCATION;
		} else
			return null;
	}

	/**
	 * This will return the bounds relative to the relative parent. The result is a four element array that is broken down into x,y,width and height.
	 * <p>
	 * The location has the same definition of (x,y) as getLocation().
	 * 
	 * @see ControlManager#getLocation()
	 * @since 1.0.0
	 */
	public Object[] getBounds() {

		final Object[] result = new Object[4];
		if (isValidControl(fControl)) {
			feedbackController.getDisplay().syncExec(new Runnable() {

				public void run() {
					Object[] location = getLocation();
					Point size = fControl.getSize();
					result[0] = location[0];
					result[1] = location[1];
					result[2] = new Integer(size.x);
					result[3] = new Integer(size.y);
				}

			});
		}
		return result; 
	}

	/*
	 * Called by Feedback controller when control is resized.
	 * 
	 * 
	 * @since 1.1.0
	 */
	void controlResized() {
		Point s = fControl.getSize();
		feedbackController.addTransaction(this, Common.CL_RESIZED, new Object[] { new Integer(s.x), new Integer(s.y)}, true);
		if (extensions != null) {
			ControlManagerExtension[] lcl = extensions; // In case it changes to a new array while we are walking it.
			for (int i = 0; i < lcl.length; i++) {
				lcl[i].controlResized();
			}
		}
	}

	/*
	 * Called be Feedback controller when control or one of its parent (other than the shell) have moved.
	 * 
	 * 
	 * @since 1.1.0
	 */
	void controlMoved() {
		fireMoved();
	}

	/**
	 * Fire component moved.
	 * 
	 * 
	 * @since 1.1.0
	 */
	protected void fireMoved() {
		feedbackController.addTransaction(this, Common.CL_MOVED, getLocation(), true);
		if (extensions != null) {
			ControlManagerExtension[] lcl = extensions; // In case it changes to a new array while we are walking it.
			for (int i = 0; i < lcl.length; i++) {
				lcl[i].controlMoved();
			}
		}
	}

	/**
	 * Get the refresh parms. This is used by both ContainerManager and ContainerManagerFeedbackController to get the refresh parms. This is because
	 * either one can request a refresh but the manager wants to do them all at once.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	private Object[] getRefreshParms() {
		return getBounds();
	}

	/**
	 * Queue up onto the SWT event queue a refresh request. This gives the control a chance to layout before the refresh request is sent back. It can
	 * be initiated from the client side and queued up here.
	 * 
	 * @since 1.1.0
	 */
	public void queueRefresh() {
		feedbackController.addTransaction(this, Common.CL_REFRESHED, getRefreshParms(), true);
		// We are queueing up the request BEFORE queueing it up again so that if there are any layout's pending
		// they will be processed before the transaction can be queued up and get the parms at that time
		// instead of now.
		feedbackController.getDisplay().asyncExec(new Runnable() {

			public void run() {
				feedbackController.addTransaction(ControlManager.this, Common.CL_REFRESHED, getRefreshParms(), true);
			}
		});

		if (extensions != null) {
			ControlManagerExtension[] lcl = extensions; // In case it changes to a new array while we are walking it.
			for (int i = 0; i < lcl.length; i++) {
				lcl[i].refreshQueued();
			}
		}
	}
	
	/**
	 * Determine if the control is a valid control, i.e. not null and not disposed.
	 * @param control
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public boolean isValidControl(Control control) {
		return control != null && !control.isDisposed();
	}

	// ---------------- Part that handles the default loc when on freeform
	protected Point overPoint; // The last applied location when we are in override mode.

	protected boolean locOverridden;

	
	/**
	 * Apply the layout data and return the old data.
	 * <p>
	 * This is used in conjunction with CompositeManagerExtension so that it can
	 * know what the real data is when it went and wiped it out.
	 * 
	 * @param newData
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public Object applyLayoutData(Object newData) {
		Control control = getControl();
		control.setData(LAYOUT_DATA_KEY, newData);
		Object old = control.getLayoutData();
		control.setLayoutData(newData);
		return old;
	}
	
	/**
	 * Apply bounds. If the loc was overridden, this will not apply bounds, just the size part of it. It will fill the oldRect, if not null, with the
	 * before set value. If it was overridden, then the loc will be the previous override loc.
	 * <p>
	 * <b>Note:</b> This must be called on the UI thread.
	 * 
	 * @param rect
	 * @param oldRect
	 *            if not null it will be set with the previous bounds.
	 * 
	 * @since 1.1.0
	 */
	public void applyBounds(Rectangle rect, Rectangle oldRect) {
		if (isValidControl(fControl)) {
			if (!locOverridden) {
				if (oldRect != null) {
					Rectangle o = fControl.getBounds();
					oldRect.x = o.x;
					oldRect.y = o.y;
					oldRect.width = o.width;
					oldRect.height = o.height;
				}
				fControl.setBounds(rect);
			} else {
				if (oldRect != null) {
					oldRect.x = overPoint.x;
					oldRect.y = overPoint.y;
					Point s = fControl.getSize();
					oldRect.width = s.x;
					oldRect.height = s.y;
				}
				fControl.setSize(rect.width, rect.height); // Overriding, but we still need to set the size part.
				// We are overriding, so save the rect's location so that it will look like this spot when queried.
				overPoint.x = rect.x;
				overPoint.y = rect.y;
			}
		}
	}

	/**
	 * Apply location, and return previous value. If loc was overridden, this will not apply loc. It will fill the oldPoint, if not null, with the
	 * before set value. If it was overridden, then the loc will be the previous override loc.
	 * <p>
	 * <b>Note:</b> This must be called on the UI thread.
	 * 
	 * @param point
	 * @param oldPoint
	 *            if not null it will be set with the previous loc.
	 * 
	 * @since 1.1.0
	 */
	public void applyLocation(Point point, Point oldPoint) {
		if (isValidControl(fControl)) {
			if (!locOverridden) {
				if (oldPoint != null) {
					Point l = fControl.getLocation();
					oldPoint.x = l.x;
					oldPoint.y = l.y;
				}
				fControl.setLocation(point);
			} else {
				if (oldPoint != null) {
					oldPoint.x = overPoint.x;
					oldPoint.y = overPoint.y;
				}
				// We are overriding, so save the point's location so that it will look like this spot when queried.
				overPoint.x = point.x;
				overPoint.y = point.y;
			}
		}
	}

	/**
	 * Force an override of loc so that it will go to specified loc but it will get the current loc and store it as the overridden value for later
	 * retrieval.
	 * 
	 * @param x
	 * @param y
	 * 
	 * @since 1.1.0
	 */
	public void overrideLoc(final int x, final int y) {
		if (isValidControl(fControl)) {
			feedbackController.getDisplay().syncExec(new Runnable() {

				public void run() {
					if (!locOverridden) {
						overPoint = fControl.getLocation();
						locOverridden = true;
					}
					fControl.setLocation(x, y);
				}
			});
		}
	}

	/**
	 * Remove the override of the loc.
	 * 
	 * @since 1.1.0
	 */
	public void removeOverrideLoc() {
		if (isValidControl(fControl)) {
			feedbackController.getDisplay().syncExec(new Runnable() {

				public void run() {
					if (locOverridden) {
						locOverridden = false;
						if (overPoint != null)
							fControl.setLocation(overPoint);
					}
				}
			});
		}
	}

	/**
	 * Query the default loc. Which is the current loc if not override, or the last set loc if override applied.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public Point getDefaultLocation() {
		if (locOverridden)
			return overPoint;
		else {
			final Point[] l = new Point[1];
			if (isValidControl(fControl)) {
				feedbackController.getDisplay().syncExec(new Runnable() {

					public void run() {
						l[0] = fControl.getLocation();
					}

				});
			}
			return l[0];
		}
	}

	/**
	 * Query the default bounds. Which is the current bounds if not override, or the last set loc with the current size if override applied.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public Rectangle getDefaultBounds() {
		final Rectangle result = new Rectangle(0, 0, 0, 0);
		if (isValidControl(fControl)) {
			feedbackController.getDisplay().syncExec(new Runnable() {

				public void run() {
					Point s = fControl.getSize();
					if (!locOverridden) {
						Point l = fControl.getLocation();
						result.x = l.x;
						result.y = l.y;
					} else {
						result.x = overPoint.x;
						result.y = overPoint.y;
					}
					result.width = s.x;
					result.height = s.y;
				}
			});
		}
		return result;
	}

	

	/**
	 * Get the offset between the upper-left corner of the control and the origin (0,0) of the control.
	 * For most controls this is (0,0). But for Shell it is not, because (0,0) on the shell actually puts
	 * you down and to the right. Need to know this offset to make appropriate coordinate calculations on
	 * the GraphViewer.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public Point getOriginOffset(){
		final Point[] originOffset = new Point[1];
		if (isValidControl(fControl)) {
			feedbackController.getDisplay().syncExec(new Runnable() {
				public void run() {
					Point controlOrigin = fControl.toDisplay(0,0);	// Display coor of where the control's (0,0) is.
					Composite parent = fControl.getParent();
					Point controlCorner = parent != null ? parent.toDisplay(fControl.getLocation()) : fControl.getLocation();	// Display coor of control's upper-left.
					controlOrigin.x-=controlCorner.x;
					controlOrigin.y-=controlCorner.y;
					originOffset[0] = controlOrigin;
				}
			});
		}
		return originOffset[0];
	}

}