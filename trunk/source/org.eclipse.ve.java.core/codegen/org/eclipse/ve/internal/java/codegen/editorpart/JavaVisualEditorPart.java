/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.codegen.editorpart;
/*
 *  $RCSfile: JavaVisualEditorPart.java,v $
 *  $Revision: 1.93 $  $Date: 2005-03-28 22:16:29 $ 
 */

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.ILock;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.*;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.gef.ui.actions.*;
import org.eclipse.gef.ui.palette.*;
import org.eclipse.gef.ui.parts.*;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.gef.ui.views.palette.PaletteViewerPage;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.ui.IContextMenuConstants;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.text.Assert;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.commands.ICommand;
import org.eclipse.ui.commands.ICommandManager;
import org.eclipse.ui.texteditor.IStatusField;
import org.eclipse.ui.texteditor.RetargetTextEditorAction;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.*;

import org.eclipse.jem.internal.adapters.jdom.JavaClassJDOMAdaptor;
import org.eclipse.jem.internal.adapters.jdom.JavaMethodJDOMAdaptor;
import org.eclipse.jem.internal.beaninfo.adapters.BeaninfoClassAdapter;
import org.eclipse.jem.internal.beaninfo.adapters.BeaninfoNature;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.ProxyFactoryRegistry;
import org.eclipse.jem.util.PerformanceMonitorUtil;
import org.eclipse.jem.util.TimerTests;
import org.eclipse.jem.util.emf.workbench.JavaProjectUtilities;
import org.eclipse.jem.util.plugin.JEMUtilPlugin;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.CDEUtilities.EditPartNamePath;
import org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator;
import org.eclipse.ve.internal.cde.emf.*;
import org.eclipse.ve.internal.cde.palette.*;
import org.eclipse.ve.internal.cde.properties.*;
import org.eclipse.ve.internal.cdm.Diagram;
import org.eclipse.ve.internal.cdm.DiagramData;
import org.eclipse.ve.internal.java.codegen.core.IDiagramModelBuilder;
import org.eclipse.ve.internal.java.codegen.core.JavaSourceTranslator;
import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.vce.*;
import org.eclipse.ve.internal.java.vce.rules.JVEStyleRegistry;
import org.eclipse.ve.internal.jcm.AbstractEventInvocation;
import org.eclipse.ve.internal.jcm.BeanSubclassComposition;
import org.eclipse.ve.internal.propertysheet.EToolsPropertySheetPage;
import org.eclipse.ve.internal.propertysheet.IDescriptorPropertySheetEntry;



/**
 * @author richkulp
 *
 * Java Visual Editor
 */
public class JavaVisualEditorPart extends CompilationUnitEditor implements DirectSelectionInput {
	
	public final static int SELECT_JVE = 1;
	public final static int SELECT_PROPERTY = 2;
	public final static int SELECT_PROPERTY_CHANGE = 3;
	
	public final static int PARSE_ERROR_STATE = 3;	// Model controller update state to indicate parse error.
	
	public final static String PI_CLASS = "class"; //$NON-NLS-1$
	public final static String PI_PALETTE = "palette"; //$NON-NLS-1$
	public final static String PI_CONTRIBUTOR = "contributor"; //$NON-NLS-1$
	public final static String PI_LOC = "loc"; //$NON-NLS-1$
	public final static String PI_CATEGORIES = "categories"; //$NON-NLS-1$
	public final static String PI_LAST = "last"; //$NON-NLS-1$
	
	protected boolean initialized = false;
	protected EditDomain editDomain;

	protected SelectionSynchronizer selectionSynchronizer;
	protected JavaSelectionProvider mainSelectionProvider;

	protected GraphicalViewer primaryViewer;
	protected XMLTextPage xmlTextPage;
	private CustomPalettePage palettePage;	// Palette page for the palette viewer	

	protected JaveVisualEditorLoadingFigureController loadingFigureController;

	protected IDiagramModelBuilder modelBuilder;
	
	// Is the model ready. This is needed because sometimes the viewers come up before the model has been
	// instantiated but it is in the modelBuilder. This causes NPE's. So only when we are about
	// to set in the model to the root will it be available.
	protected boolean modelReady;	

	protected ProxyFactoryRegistry proxyFactoryRegistry;
	protected JavaModelSynchronizer modelSynchronizer;
	protected BeanProxyAdapterFactory beanProxyAdapterFactory;
	protected boolean rebuildPalette = true;
	
	protected JavaVisualEditorPropertySheetPage propertySheetPage;
	
	protected JavaVisualEditorOutlinePage beansListPage;
	
	protected String currentStatusMessage = ""; //$NON-NLS-1$
	
	protected  boolean statusMsgSet = false;  // do we have error/info on the status bar
	
	/*
	 *  Registry of just the graphical ones (i.e. not in common with text editor) that need to be accessed by action contributor. 
	 */
	protected ActionRegistry graphicalActionRegistry = new ActionRegistry();
	
	// Registry of actions that are in common with the graph viewer and the java text editor that need to be accessed by action contributor.
	// What is different is that these actions are all RetargetTextEditorActions. The focus listener will make sure that whichever
	// viewer/editor is in focus, the appropriate action from either the text/graph viewer is set into the retarget action.
	// This allows the action bar/menu of the editor contributor to have the correct action depending on which is in focus. 
	protected ActionRegistry commonActionRegistry = new ActionRegistry();
	
	protected OpenActionGroup openActionGroup;
	
	protected SelectionServiceListener selectionServiceListener = new SelectionServiceListener();
	
	// allow threads to wait until JVE has loaded
	protected Object loadCompleteSync = new Object();
	protected volatile boolean isLoadPending = true;
	
	private static final String JVE_STEP = "JVE";  //$NON-NLS-1$
	public static final String SETUP_STEP = "Setup JVE"; //$NON-NLS-1$
	public final static boolean DO_TIMER_TESTS = Boolean.valueOf(Platform.getDebugOption(JavaVEPlugin.getPlugin().getBundle().getSymbolicName() + "/debug/vetimetrace")).booleanValue(); //$NON-NLS-1$;
	// This is a workaround for the fact that background jobs compete for CPU during bring up
	public final static int BRING_UP_PRIORITY_BUMP = 1;
	
	public JavaVisualEditorPart() {	    
		bumpUIPriority(true, Thread.currentThread());
		PerformanceMonitorUtil.getMonitor().snapshot(100);	// Start snapshot.
		if (DO_TIMER_TESTS) {
			System.out.println(""); //$NON-NLS-1$
			TimerTests.basicTest.testState(true);
			TimerTests.basicTest.startStep(JVE_STEP);
		}
	}
	private static int uiThreadPriority = -1;
	private static Thread uiThread = null;
	private void bumpUIPriority(boolean up, Thread ui) {
		// First time around this method must be called from the ui thread		
		if (uiThread==null) {
		  	  if (ui==null)
		  	  	 return;
		  	  uiThread = ui;
			  uiThreadPriority=ui.getPriority();
		}			
		if (up)
			uiThread.setPriority(uiThreadPriority+BRING_UP_PRIORITY_BUMP);
		else
			uiThread.setPriority(uiThreadPriority);
			
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (input instanceof IFileEditorInput) {
			IProject proj = ((IFileEditorInput) input).getFile().getProject();
			try {
				if (!proj.hasNature(JavaCore.NATURE_ID))
					throw new PartInitException(
						MessageFormat.format(
							CodegenEditorPartMessages.getString("JavaVisualEditor.notJavaProject_EXC_"), //$NON-NLS-1$
							new Object[] { proj.getName(), input.getName()}));
			} catch (CoreException e) {
				throw new PartInitException(e.getStatus());
			}
		} else
			throw new PartInitException(
				MessageFormat.format(CDEMessages.getString("NOT_FILE_INPUT_ERROR_"), new Object[] { input.getName()})); //$NON-NLS-1$

		if (DO_TIMER_TESTS)
			System.out.println("------------ Measuring class \"" + input.getName() + "\" ------------"); //$NON-NLS-1$ //$NON-NLS-2$

		// We need the following now because both the thread that will be spawned off in doSetInput and 
		// in the createPartControl need the loadingFigureControler and EditDomain
		// to exist at that time. 
		//
		// Actually the createPartControl will probably occur before the thread runs to do the rest of
		// the initialization.	
		loadingFigureController = new JaveVisualEditorLoadingFigureController();

		// Do any initializations that are needed for both the viewers and the codegen parser when they are created.
		// Need to be done here because there would be race condition between the two threads otherwise.
		editDomain = new EditDomain(this);
		ClassDescriptorDecoratorPolicy policy = new ClassDescriptorDecoratorPolicy();
		// Get the default decorator and put a JavaBean label provider onto it
		ClassDescriptorDecorator defaultClassDescriptorDecorator = (ClassDescriptorDecorator) policy.getDefaultDecorator(ClassDescriptorDecorator.class);
		defaultClassDescriptorDecorator.setLabelProviderClassname(DefaultLabelProviderWithName.DECORATOR_CLASSNAME_VALUE);
		ClassDescriptorDecoratorPolicy.setClassDescriptorDecorator(editDomain, policy);	
		
		modelBuilder = new JavaSourceTranslator(editDomain);

		modelChangeController = new JavaVisualEditorModelChangeController(this, modelBuilder, site.getShell().getDisplay());
		editDomain.setData(IDiagramModelBuilder.MODEL_BUILDER_KEY, modelBuilder);
		editDomain.setCommandStack(new JavaVisualEditorCommandStack(modelChangeController));
		editDomain.setData(ModelChangeController.MODEL_CHANGE_CONTROLLER_KEY, modelChangeController);	
		
		// Create the common actions
		ISharedImages images = PlatformUI.getWorkbench().getSharedImages();
		RetargetTextEditorAction deleteAction = new RetargetTextEditorAction(CodegenEditorPartMessages.RESOURCE_BUNDLE, "Action.Delete."); //$NON-NLS-1$
		deleteAction.setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		deleteAction.setId(ActionFactory.DELETE.getId());
		commonActionRegistry.registerAction(deleteAction);
		
		// Create the toolbar palette actions that are needed right away.
		PaletteToolEntryAction paction = new PaletteToolEntryAction(editDomain);
		paction.setId(JavaVisualEditorActionContributor.PALETTE_SELECTION_ACTION_ID);
		graphicalActionRegistry.registerAction(paction);
		paction = new PaletteToolEntryAction(editDomain);
		paction.setId(JavaVisualEditorActionContributor.PALETTE_MARQUEE_SELECTION_ACTION_ID);
		graphicalActionRegistry.registerAction(paction);
		paction = new PaletteToolbarDropDownAction(editDomain);
		paction.setId(JavaVisualEditorActionContributor.PALETTE_DROPDOWN_ACTION_ID);
		graphicalActionRegistry.registerAction(paction);		
		
		// Create the customize action since needed right away.
		graphicalActionRegistry.registerAction(new CustomizeJavaBeanAction(this, editDomain));
		
		// Create the pause/reload action.
		
		ReloadAction.IReloadCallback reloadCallback = new ReloadAction.IReloadCallback() {
			public void pause() {
				modelChangeController.setHoldState(ModelChangeController.NO_UPDATE_STATE, null);	// So no updates while paused.
				modelBuilder.pause();
			}
			public void reload() {
				loadModel(true);
			}
		};
		
		graphicalActionRegistry.registerAction(new ReloadAction(reloadCallback));
		graphicalActionRegistry.registerAction(new ReloadNowAction(reloadCallback));
		
		setRootInProgressLock = Platform.getJobManager().newLock();
			
		super.init(site, input);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#doSetInput(org.eclipse.ui.IEditorInput)
	 */
	protected void doSetInput(IEditorInput input) throws CoreException {
		// The input has changed.
		super.doSetInput(input);
		loadModel(true);
	}
	
	protected Job setupJob = null; 
	protected void loadModel(boolean resetVMRecycleCounter) {
		if(!((IFileEditorInput) getEditorInput()).getFile().isAccessible())
			return;	// File not there for some reason. Can't load. Leave alone. The next time the editor gets focus the text editor portion is smart enough to close down in this case.
		if (resetVMRecycleCounter)
			recycleCntr = 0;	// Reset the counter because we are a new load and ask for it to be reset.
		setReloadEnablement(false);	// While reloading, don't want button to be pushed.
		
		modelChangeController.setHoldState(ModelChangeController.NO_UPDATE_STATE, null);	// Don't allow updates..
		
		setRootModel(null); // Clear it out so we don't see all of the changes that are about to happen.
		loadingFigureController.showLoadingFigure(true);	// Start the loading figure.	
		// Kick off the setup thread. Doing so that system stays responsive.
		if (setupJob == null) {
			setupJob = new Setup(CodegenEditorPartMessages.getString("JavaVisualEditorPart.SetupJVE")); //$NON-NLS-1$
			setupJob.setPriority(Job.SHORT); // Make it slightly slower so that ui thread still active
		}
		setupJob.schedule();	// Start asap.
	}

	private void setReloadEnablement(boolean enabled) {
		ReloadAction rla = (ReloadAction) graphicalActionRegistry.getAction(ReloadAction.RELOAD_ACTION_ID);
		rla.setEnabled(enabled);
		ReloadNowAction rlna = (ReloadNowAction) graphicalActionRegistry.getAction(ReloadNowAction.RELOADNOW_ACTION_ID);
		rlna.setEnabled(enabled);
	}

	private BeanSubclassComposition currentSetRoot;
	private static final Object SELECTED_EDITPARTS_KEY = new Object();
	// A lock to indicate a set non-null root is in progress. This is necessary because if a set root is in progress
	// we shouldn't try to load a new model while an old model is still loading. It doesn't like being ripped out from
	// underneath itself.
	private ILock setRootInProgressLock = null;	
	/*
	 * Set a new model into the root editparts. This can happen because the setup and the initial creation
	 * of the viewers can be in a race condition with each other.
	 * NOTE: Must run in display thread.
	 */
	protected void setRootModel(final BeanSubclassComposition root) {
		Assert.isTrue(Display.getCurrent() != null);
		boolean acquiredLock = false;
		try {
			EditDomain dom = null;
			// Need to get it under sync because it may go null before getting here since this method was called asymc.
			// Then check if null and if so exit.
			synchronized(this) {
				dom = editDomain;	
			}
			if (dom == null)
				return;
			
			if (root == null) {
				modelReady = false;
				// We are going away. Try to build the path to the selected editparts so they can be restored later.
				// Have to gather them all first because individually set the roots of the viewers to null will
				// destroy the selection for the following viewers. The viewers may have slightly different selections
				// if one viewer had an editpart that other didn't. So we need to get each individually.
				Iterator itr = editDomain.getViewers().iterator();
				while (itr.hasNext()) {
					EditPartViewer viewer = (EditPartViewer) itr.next();
					List selected = viewer.getSelectedEditParts();
					if (selected.isEmpty())
						editDomain.removeViewerData(viewer, SELECTED_EDITPARTS_KEY); // None selected
					else {
						List paths = new ArrayList(selected.size());
						for (int i = 0; i < selected.size(); i++) {
							EditPartNamePath editPartNamePath = CDEUtilities.getEditPartNamePath((EditPart) selected.get(i), editDomain);
							if (editPartNamePath == null)
								continue; // If the root is selected, then treat as not selected.
							paths.add(editPartNamePath);
						}
						if (paths.isEmpty())
							editDomain.removeViewerData(viewer, SELECTED_EDITPARTS_KEY); // None selected
						else
							editDomain.setViewerData(viewer, SELECTED_EDITPARTS_KEY, paths);
					}
				}
			} else {
				setRootInProgressLock.acquire();	// Get access to prevent a load while we are doing this.
				acquiredLock = true;
				if (root.eResource() == null || root.eResource().getResourceSet() == null) 
					return; // This root has already been released. Means probably another reload in progress.
				modelReady = true;
			}

			Iterator itr = editDomain.getViewers().iterator();
			while (itr.hasNext()) {
				EditPartViewer viewer = (EditPartViewer) itr.next();
				EditPart rootEP = viewer.getContents();
				rootEP.deactivate();
				rootEP.setModel(root);
				rootEP.refresh();
				rootEP.activate();
				rootEP.refresh();
			}

			if (root != null) {
				// We have something, see if anything needs to be selected.
				Iterator itr1 = editDomain.getViewers().iterator();
				while (itr1.hasNext()) {
					EditPartViewer viewer = (EditPartViewer) itr1.next();
					List paths = (List) editDomain.getViewerData(viewer, SELECTED_EDITPARTS_KEY);
					if (paths != null) {
						editDomain.removeViewerData(viewer, SELECTED_EDITPARTS_KEY); // So that doesn't hang around.
						for (int i = 0; i < paths.size(); i++) {
							EditPart selected = CDEUtilities.findEditpartFromNamePath((EditPartNamePath) paths.get(i), viewer, editDomain);
							if (selected != null)
								viewer.appendSelection(selected);
						}
					}
				}
			}

			try {
				if (currentSetRoot != root && currentSetRoot != null)
					currentSetRoot.eNotify(new ENotificationImpl((InternalEObject) currentSetRoot, CompositionProxyAdapter.RELEASE_PROXIES, null,
							null, null, false));
			} finally {
				currentSetRoot = root;
			}
			if (propertySheetPage != null) {
				// At this point in time the property sheet may be in focus, so it is not listening to selection changes
				// from us. Because of this it may have an active entries which are now pointing to the obsolete
				// model. This model won't have a resource set, etc. So the entries need to be sent away.
				propertySheetPage.selectionChanged(this, primaryViewer.getSelection());
			}
		} finally {
			if (acquiredLock)
				setRootInProgressLock.release();
		}
	}

	/*
	 * Open the VCE Viewers if required to.
	 */
	protected void openVCEViewersIfRequired(IEditorSite site) throws PartInitException {

		// Open the properties and Java beans viewer if we are in the Java or Java Browsing perspective
		IWorkbenchPage page = site.getWorkbenchWindow().getActivePage();
		if (page == null)
			return;

		// Calling showView will open the editor if it isn't already opened, and bring it to the front
		// if it already is ( for example in the J2EE perspective the Properties viewer may already be open
		// so showView brings it to the front )
		// Only open the views if the preferences say they should be.  This allows users to switch the option off
		Preferences store = VCEPreferences.getPlugin().getPluginPreferences();
		if (store.getBoolean(VCEPreferences.OPEN_PROPERTIES_VIEW)) {
			site.getPage().showView("org.eclipse.ui.views.PropertySheet", null, IWorkbenchPage.VIEW_VISIBLE); //$NON-NLS-1$	
		}
		if (store.getBoolean(VCEPreferences.OPEN_JAVABEANS_VIEW)) {
			site.getPage().showView("org.eclipse.ve.internal.java.codegen.editorpart.BeansList", null, IWorkbenchPage.VIEW_VISIBLE); //$NON-NLS-1$
		}

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {	
		Preferences store = VCEPreferences.getPlugin().getPluginPreferences();
				
		boolean isNotebook = store.getBoolean(VCEPreferences.NOTEBOOK_PAGE);
		if (isNotebook) {
			createNotebookEditor(parent, store);
		} else {
			createSplitpaneEditor(parent, store);
		}

		// Now we can add the focus listener.
		primaryViewer.getControl().addFocusListener(focusListener);
		getSourceViewer().getTextWidget().addFocusListener(focusListener);		
		
		// Main provider handles firing out the appropriate selections depending on whether the grapical editor or the text editor is in focus.
		mainSelectionProvider = new JavaSelectionProvider();
		getSite().setSelectionProvider(mainSelectionProvider);
		
		getSite().getPage().addSelectionListener(selectionServiceListener);
				
		parent.getDisplay().asyncExec(new Runnable() {
			public void run() {
				try {
					if (!isDisposed()) {
						// So that we can get the editor up and displaying as soon as possible we will push this off to
						// the next async cycle.	
						openVCEViewersIfRequired(getEditorSite());
					}			
				} catch (PartInitException e) {
					JavaVEPlugin.log(e);
				}
			}
		});
	}
	

	private PaletteViewerProvider provider;
	protected PaletteViewerProvider getPaletteViewerProvider() {
		if (provider == null)
			provider = createPaletteViewerProvider();
		return provider;
	}
	
	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new PaletteViewerProvider(editDomain) {
			
			/* (non-Javadoc)
			 * @see org.eclipse.gef.ui.palette.PaletteViewerProvider#configurePaletteViewer(org.eclipse.gef.ui.palette.PaletteViewer)
			 */
			protected void configurePaletteViewer(PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);
				if (rebuildPalette)
					rebuildPalette();
			}
		};
	}	

	private FlyoutPaletteComposite paletteSplitter;
	
	/*
	 * Create the editor as a split pane editor with the graphical editor on top and the text editor on the bottom.
	 * The palette will be on the left if no viewer.
	 */
	protected void createSplitpaneEditor(Composite parent, Preferences store) {
		paletteSplitter = new FlyoutPaletteComposite(parent, SWT.NONE, getSite().getPage(),	getPaletteViewerProvider(), getPalettePreferences());
			
		// JVE/Text editor split on the right under editorComposite
		CustomSashForm editorParent = new CustomSashForm(paletteSplitter, SWT.VERTICAL);
		createPrimaryViewer(editorParent);		

		// Let the super java text editor fill it in.			
		super.createPartControl(editorParent);
		editorParent.setSashBorders(new boolean[] { true, true });
		paletteSplitter.setGraphicalControl(editorParent);
		if (palettePage != null) {
			paletteSplitter.setExternalViewer(palettePage.getPaletteViewer());
			palettePage = null;
		}
	}
	
	protected FlyoutPaletteComposite.FlyoutPreferences getPalettePreferences() {
		return new FlyoutPaletteComposite.FlyoutPreferences() {
			
			private Preferences store = VCEPreferences.getPlugin().getPluginPreferences();

			// All of the values are set from the palette itself and stored. We can't set starting values, those
			// will come from the preferences. If not set, palette does something default.
			private static final String DOCK_LOCATION = "DOCK_LOCATION"; //$NON-NLS-1$
			private static final String PALETTE_STATE = "PALETTE_STATE"; //$NON-NLS-1$
			private static final String PALETTE_WIDTH = "PALETTE_WIDTH"; //$NON-NLS-1$
			

			public int getDockLocation() {
				return store.getInt(DOCK_LOCATION);
			}

			public int getPaletteState() {
				return store.getInt(PALETTE_STATE);
			}

			public int getPaletteWidth() {
				return store.getInt(PALETTE_WIDTH);
			}

			public void setDockLocation(int location) {
				store.setValue(DOCK_LOCATION, location);
			}

			public void setPaletteState(int state) {
				store.setValue(PALETTE_STATE, state);
			}

			public void setPaletteWidth(int width) {
				store.setValue(PALETTE_WIDTH, width);
			}
		};
	}
	
	
	/*
	 * Create the editor as a note book with one tab for the design and the other for the text editor.
	 */
	protected void createNotebookEditor(Composite parent, Preferences store) {
		// Create a notebook folder			
		final CTabFolder folder = new CTabFolder(parent, SWT.BOTTOM);
		Composite editorParent = new Composite(folder, SWT.NONE);
		editorParent.setLayout(new FillLayout());

		CTabItem jveTab = new CTabItem(folder, SWT.NONE);
		jveTab.setControl(editorParent);
		jveTab.setText(CodegenEditorPartMessages.getString("JavaVisualEditorPart.DesignPart")); //$NON-NLS-1$

		paletteSplitter = new FlyoutPaletteComposite(editorParent, SWT.NONE, getSite().getPage(), getPaletteViewerProvider(), getPalettePreferences());
		paletteSplitter.setGraphicalControl(createPrimaryViewer(paletteSplitter));
		if (palettePage != null) {
			paletteSplitter.setExternalViewer(palettePage.getPaletteViewer());
			palettePage = null;
		}

		// Create the parent (new tab) for the java text editor.
		Composite javaParent = new Composite(folder, SWT.NONE);
		javaParent.setLayout(new FillLayout());

		CTabItem javaTab = new CTabItem(folder, SWT.NONE);
		javaTab.setControl(javaParent);
		javaTab.setText(CodegenEditorPartMessages.getString("JavaVisualEditorPart.SourcePart")); //$NON-NLS-1$
		// Let the super java text editor fill it in.
		super.createPartControl(javaParent);

		// Selection must be on the first page otherwise it does not activate correctly
		folder.setSelection(jveTab);
	}

	private List paletteCategories = new ArrayList(5);
	/*
	 * Rebuild the palette.
	 * NOTE: This must be run in the display thread.
	 */
	protected void rebuildPalette() {
		if(editDomain.getPaletteViewer() == null) return;
		ResourceSet rset = EMFEditDomainHelper.getResourceSet(editDomain);
		if(rset == null) return;
		rebuildPalette = false;
		PaletteRoot paletteRoot = editDomain.getPaletteRoot();

		List newChildren = null; // New list of children. We will build entire list and then set into palette root to speed it up.
		// What happens is first time through we will build the palette up before applying into the palette viewer,
		// that way it gets it all at once. For later times, since palette root can't be replaced, we will instead
		// get the old children list, copy just the control group over to a new child list, build the rest into
		// the new list and apply the new list back all at once into the palette root.
		
		// TODO This whole area needs to be rethinked for customization and for caching and other things.

		if (paletteRoot == null) {
			try {
				// Get the default base palette. (Basically only the control group).				
				Palette ref = (Palette) rset.getEObject(basePaletteRoot, true);
				paletteRoot = (PaletteRoot) ref.getEntry();
				// Get the two standard tools. Since we only load this part of the palette once, it can never change.
				// TODO Need a better way of doing this that isn't so hardcoded.				
				newChildren = paletteRoot.getChildren();
				if (newChildren.size() >= 1 && newChildren.get(0) instanceof PaletteContainer) {
					PaletteContainer controlGroup = (PaletteContainer) newChildren.get(0);
					newChildren = controlGroup.getChildren();
					if (newChildren.size() >= 3) {
						final PaletteToolEntryAction ptSel = (PaletteToolEntryAction) graphicalActionRegistry.getAction(JavaVisualEditorActionContributor.PALETTE_SELECTION_ACTION_ID);
						ptSel.setToolEntry((ToolEntry) newChildren.get(0));
						ptSel.setId(JavaVisualEditorActionContributor.PALETTE_SELECTION_ACTION_ID);	// Because setToolEntry resets it
						ptSel.setChecked(true);	// Selection always initially checked. (i.e. selected).
						final PaletteToolEntryAction ptMarq = (PaletteToolEntryAction) graphicalActionRegistry.getAction(JavaVisualEditorActionContributor.PALETTE_MARQUEE_SELECTION_ACTION_ID);
						ptMarq.setToolEntry((ToolEntry) newChildren.get(1));
						ptMarq.setChecked(false);
						ptMarq.setId(JavaVisualEditorActionContributor.PALETTE_MARQUEE_SELECTION_ACTION_ID);	// Because setToolEntry resets it
						final PaletteToolbarDropDownAction ptDropDown = (PaletteToolbarDropDownAction) graphicalActionRegistry.getAction(JavaVisualEditorActionContributor.PALETTE_DROPDOWN_ACTION_ID);
						ptDropDown.setToolEntry((ToolEntry) newChildren.get(2));
						ptDropDown.setChecked(false);
						ptDropDown.setPaletteRoot(paletteRoot);
						ptDropDown.setId(JavaVisualEditorActionContributor.PALETTE_DROPDOWN_ACTION_ID);	// Because setToolEntry resets it						
						
						// Add palette viewer listener so that we can set the correct selection state and msg.
						final PaletteRoot froot = paletteRoot; 
						editDomain.getPaletteViewer().addPaletteListener(new PaletteListener() {
							public void activeToolChanged(PaletteViewer palette, ToolEntry tool) {
								ptSel.setChecked(tool == froot.getChildren().get(0));
								ptMarq.setChecked(tool == froot.getChildren().get(1));
							
								String msg = ""; //$NON-NLS-1$
								if (tool.createTool() instanceof CreationTool ){
									msg = MessageFormat.format(CodegenEditorPartMessages.getString("JVEActionContributor.Status.Creating(label)"), new Object[]{tool.getLabel()}); //$NON-NLS-1$
								}
								setStatusMsg(getEditorSite().getActionBars(),msg,null);																
							}
						});
					}				
				}
			} catch (RuntimeException e) {
				JavaVEPlugin.log(e);
				return;	// Can't build the root even.
			}
		} else {
			// We have a palette already, so reset the root into the drop down action so that it knows to recreate when needed.
			PaletteToolbarDropDownAction ptDropDown = (PaletteToolbarDropDownAction) graphicalActionRegistry.getAction(JavaVisualEditorActionContributor.PALETTE_DROPDOWN_ACTION_ID);
			ptDropDown.setPaletteRoot(paletteRoot);
			
			// Can't just plop new one in (GEF restriction). So we will copy over the control group to the
			// newChildren.
			List c = paletteRoot.getChildren();
			newChildren = new ArrayList(c.size());
			for (int i = 0; i < c.size(); i++) {
				if (!(c.get(i) instanceof PaletteDrawer))
					newChildren.add(c.get(i));
			}			
		}
	
		// TODO This is a huge kludge for this release. We're not handling sharing of palette or modifications. We aren't cleaning up.
		// Now get the extension palette cats and add them.
		if (!paletteCategories.isEmpty()) {
			boolean firstCat = true;
			for (Iterator iter = paletteCategories.iterator(); iter.hasNext();) {
				Category element = (Category) iter.next();
				PaletteDrawer drawer = (PaletteDrawer) element.getEntry();
				if (firstCat) {
					drawer.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
					firstCat = false;
				}
				newChildren.add(drawer);
			}
		}
		
		// Now set the new children back into the root. We also set the root back into the palette, but
		// this is ok if not the first time. If not the first time it will simply ignore it since same root.
		// But for the first time it will wait until now to actually build the entire palette visuals.
		paletteRoot.setChildren(newChildren);
		editDomain.setPaletteRoot(paletteRoot);			
	}
	

	/*
	 * Create the primary viewer, which is the Graphical Viewer.
	 */
	protected Control createPrimaryViewer(Composite parent) {
		if (VCEPreferences.isXMLTextOn()) {
			// If the Pref store wants the XML page put it on a sash
			SashForm sashform = new SashForm(parent, SWT.VERTICAL);
			createGraphicalViewer(sashform);
			createXMLTextViewerControl(sashform);
			return sashform;
		} else {
			return createGraphicalViewer(parent);
		}
	}

	protected Control createGraphicalViewer(Composite parent) {
		primaryViewer = new ScrollingGraphicalViewer();

		Control gviewer = primaryViewer.createControl(parent);
		editDomain.addViewer(primaryViewer);
		editDomain.setViewerData(primaryViewer, ZoomController.ZOOM_KEY, new ZoomController());
		editDomain.setViewerData(primaryViewer, GridController.GRID_KEY, new GridController());
		editDomain.setViewerData(primaryViewer, DistributeController.DISTRIBUTE_KEY, new DistributeController(primaryViewer));
		primaryViewer.setEditPartFactory(new DefaultGraphicalEditPartFactory(ClassDescriptorDecoratorPolicy.getPolicy(editDomain)));

		// Set the connection router to manhattan by default
		ScalableFreeformRootEditPart rootEditPart = new ScalableFreeformRootEditPart();		
 		primaryViewer.setRootEditPart(rootEditPart);
		((ConnectionLayer)rootEditPart.getLayer(LayerConstants.CONNECTION_LAYER)).setConnectionRouter(new ManhattanConnectionRouter()); 		
 		
		SubclassCompositionComponentsGraphicalEditPart viewerContents = new SubclassCompositionComponentsGraphicalEditPart(null);				
		primaryViewer.setContents(viewerContents);
		getSelectionSynchronizer().addViewer(primaryViewer);

		openActionGroup = new OpenActionGroup(this);
		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		Menu menu = menuMgr.createContextMenu(gviewer);
		gviewer.setMenu(menu);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager menuMgr) {
				GEFActionConstants.addStandardActionGroups(menuMgr);
				menuMgr.appendToGroup(GEFActionConstants.GROUP_UNDO, new Separator(IContextMenuConstants.GROUP_OPEN));
				openActionGroup.setContext(new ActionContext(primaryViewer.getSelection()));
				openActionGroup.fillContextMenu(menuMgr);
				openActionGroup.setContext(null);					
				menuMgr.appendToGroup(GEFActionConstants.GROUP_UNDO, getAction(ActionFactory.UNDO.getId()));
				menuMgr.appendToGroup(GEFActionConstants.GROUP_UNDO, getAction(ActionFactory.REDO.getId()));														
				menuMgr.appendToGroup(GEFActionConstants.GROUP_EDIT, graphicalActionRegistry.getAction(ActionFactory.DELETE.getId()));
				IAction customize = graphicalActionRegistry.getAction(CustomizeJavaBeanAction.ACTION_ID);
				if (customize.isEnabled()) 
					menuMgr.appendToGroup(GEFActionConstants.GROUP_EDIT, customize);
			}
		});
		getSite().registerContextMenu("JavaVisualEditor.contextMenu", menuMgr, primaryViewer); //$NON-NLS-1$

		loadingFigureController.startListener(primaryViewer);

		if (setupJob != null && setupJob.getState() == Job.NONE)
			initializeViewers();
		final DeleteAction deleteAction = new DeleteAction((IWorkbenchPart) this);
		deleteAction.setSelectionProvider(primaryViewer);
		graphicalActionRegistry.registerAction(deleteAction);
		final SelectionAction customizeAction = (SelectionAction) graphicalActionRegistry.getAction(CustomizeJavaBeanAction.ACTION_ID);
		customizeAction.setSelectionProvider(primaryViewer);
		primaryViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				deleteAction.update();
				customizeAction.update();
			}
		});	
		KeyHandler keyHandler = new KeyHandler();				
		keyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0), deleteAction);					
		primaryViewer.setKeyHandler(new GraphicalViewerKeyHandler(primaryViewer).setParent(keyHandler));
		
		paletteSplitter.hookDropTargetListener(primaryViewer);
		return gviewer;
	}

	/*
	 * Bit of a KLDUGE but during setup we may not have primary viewer yet, and during primary viewer creation
	 * we may not yet have Setup completed. So we need a common synchronized method that will set the diagram data
	 * when both are ready. Both places will call this method at thier appropriate time.
	 * 
	 * NOTE: Must run in display thread.
	 */
	protected void initializeViewers() {
		Assert.isTrue(Display.getCurrent() != null);
		
		if (primaryViewer != null && modelBuilder.getModelRoot()!=null) {
			Diagram d = modelBuilder.getDiagram();
			try {
			    ModelChangeController modelChangeController = (ModelChangeController)editDomain.getData(ModelChangeController.MODEL_CHANGE_CONTROLLER_KEY);
			    modelChangeController.transactionBeginning(ModelChangeController.INIT_VIEWERS_PHASE);			    
			    
				TimerTests.basicTest.startStep("Initialize Viewers"); //$NON-NLS-1$
				if (doTimerStep)
					PerformanceMonitorUtil.getMonitor().snapshot(118);
				if (d != null) {
					editDomain.setViewerData(primaryViewer, EditDomain.DIAGRAM_KEY, d);
					setRootModel(modelBuilder.getModelRoot()); // Set into viewers.
				}
				loadingFigureController.showLoadingFigure(false);
				setReloadEnablement(true);
				modelChangeController.setHoldState(ModelChangeController.READY_STATE, null); // Restore to allow updates.
				
				if (doTimerStep)
					PerformanceMonitorUtil.getMonitor().snapshot(119);
				TimerTests.basicTest.stopStep("Initialize Viewers"); //$NON-NLS-1$
				TimerTests.basicTest.stopStep(JVE_STEP);
				TimerTests.basicTest.printIt();
				TimerTests.basicTest.clearTests();
				TimerTests.basicTest.testState(false);
				if (doTimerStep) {
					doTimerStep = false;	// Done with first load, don't do it again.
					PerformanceMonitorUtil.getMonitor().snapshot(101);	// Done complete load everything is now changable by user.					
				}
			} catch (RuntimeException e) {
				noLoadPrompt(e);
				throw e;
			}
			finally {
			    modelChangeController.transactionEnded(ModelChangeController.INIT_VIEWERS_PHASE);
				bumpUIPriority(false,null);
			}
		}
	}
	
	/*
	 * Put up a not loaded prompt. Called if any errors during loading has been detected.
	 */
	protected void noLoadPrompt(final Exception e) {
		// If we are disposed, then it doesn't matter what the error is. This can occur because we closed
		// while loading and there is no way to stop this thread when that occurs. We don't want main thread
		// to just wait until this thread finishes when closing because that holds everything up when we don't
		// really care. This way we let it throw an exception because something was in a bad state and just
		// don't put up a message.
		// It is slightly possible that we are shutting down whole app and so shell no longer exists,
		// so we treat that as disposed too.
		if (!isDisposed() && getSite().getShell() != null) {
			getSite().getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					if (isDisposed())
						return;
					
					loadingFigureController.showLoadingFigure(false);	// Bring down only the loading figure.
					processParseError(true);	// Treat it as a parse error, the model parser couldn't even get far enough to signal parse error.
					setReloadEnablement(true);	// Because it was disabled.							
					
					String title = CodegenEditorPartMessages.getString("JavaVisualEditor.ErrorTitle"); //$NON-NLS-1$
					String msg = CodegenEditorPartMessages.getString("JavaVisualEditor.ErrorDesc"); //$NON-NLS-1$
					Shell shell = getSite().getShell();
					if (e instanceof CoreException)
						ErrorDialog.openError(shell, title, msg, ((CoreException) e).getStatus());
					else
						ErrorDialog.openError(shell, title, msg, new Status(IStatus.ERROR, JavaVEPlugin.getPlugin().getBundle().getSymbolicName(), 0, e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getClass().getName(), e));
				}
			});
			JavaVEPlugin.log(e);
		}
		
	}

	protected void createXMLTextViewerControl(Composite parent) {
		xmlTextPage = new XMLTextPage();
		xmlTextPage.createControl(parent);

		// Add a command stack listener to refresh the text of the XML source page
		// each time something happens
		editDomain.getCommandStack().addCommandStackListener(new CommandStackListener() {
			public void commandStackChanged(java.util.EventObject anEventObject) {
				refreshTextPage();
			}
		});

		// Having created the page give it the initial source
		refreshTextPage();
	}

	private static final Map XML_TEXT_OPTIONS;
	static {
		XML_TEXT_OPTIONS = new HashMap(2);
		XML_TEXT_OPTIONS.put(XMLResource.OPTION_PROCESS_DANGLING_HREF, XMLResource.OPTION_PROCESS_DANGLING_HREF_RECORD);
		XML_TEXT_OPTIONS.put(XMLResource.OPTION_LINE_WIDTH, new Integer(100));
	}
	public void refreshTextPage() {
		if (xmlTextPage != null && modelBuilder != null && modelBuilder.getModelRoot() != null) {
			if (modelBuilder.getModelRoot().eResource() != null) {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				try {
					modelBuilder.getModelRoot().eResource().save(os, XML_TEXT_OPTIONS);
					xmlTextPage.setText(os.toString());
				} catch (Exception e) {
					JavaVEPlugin.log(e, Level.WARNING);
					xmlTextPage.setText(""); //$NON-NLS-1$
				}
			} else {
				xmlTextPage.setText(""); //$NON-NLS-1$
			}
		}
	}

	private static final URI basePaletteRoot = URI.createURI("platform:/plugin/org.eclipse.ve.java.core/java_palette.xmi#java_palette"); //$NON-NLS-1$
	/*
	 * A default contributor that works with the palette extension point format. This here so that 
	 * the default registration contributor can do the same thing with its palette contribution.
	 * 
	 * @since 1.0.0
	 */
	static class DefaultVEContributor implements IVEContributor1 {
		/**
		 * Configuration element to use for contribution. Set this before usage. That way an instance of this
		 * can be reused.
		 */
		public IConfigurationElement paletteContribution;
		static Map paletteCategories = new HashMap(10);

		
		/* (non-Javadoc)
		 * @see org.eclipse.ve.internal.java.codegen.editorpart.IVEContributor#contributePalleteCats(java.util.List, org.eclipse.emf.ecore.resource.ResourceSet)
		 */
		public boolean contributePalleteCats(List currentCategories, ResourceSet rset) {
			String cat = paletteContribution.getAttributeAsIs(PI_CATEGORIES);
			if (cat == null || cat.length() == 0)
				return false;
			String bundleName = null;
			if (cat.charAt(0) != '/')
				bundleName = paletteContribution.getDeclaringExtension().getNamespace();
			else {
				if (cat.length() > 4) {
					int pend = cat.indexOf('/', 1);
					if (pend == -1 || pend >= cat.length()-1)
						return false;	// invalid
					bundleName = cat.substring(1, pend);
					cat = cat.substring(pend+1);
				} else
					return false;	// invalid
			}
			URI catsURI = URI.createURI(JEMUtilPlugin.PLATFORM_PROTOCOL+":/"+JEMUtilPlugin.PLATFORM_PLUGIN+'/'+bundleName+'/'+cat); //$NON-NLS-1$

			List cats = (List) paletteCategories.get(catsURI);
			if (cats == null) {			
				Resource res = rset.getResource(catsURI, true);
				cats = (List) EcoreUtil.getObjectsByType(res.getContents(), PalettePackage.eINSTANCE.getCategory());
				paletteCategories.put(catsURI,cats);				
			}
			
			
			if (cats.isEmpty())
				return false;
			// Check to see if the first cat is already in the list of categories. This means we already processed this once.
			if (currentCategories.contains(cats.get(0)))
				return false;
			if (!PI_LAST.equals(paletteContribution.getAttributeAsIs(PI_LOC)))
				currentCategories.addAll(0, cats);
			else
				currentCategories.addAll(cats);
			return true;
		}
		
		
		/* (non-Javadoc)
		 * @see org.eclipse.ve.internal.java.codegen.editorpart.IVEContributor1#modifyPaletteCatsList(java.util.List)
		 */
		public boolean modifyPaletteCatsList(List currentCategories) {
			return false;
		}
	}
	
	/*
	 * This will create the proxy factory registry.
	 */
	private ProxyFactoryRegistry.IRegistryListener registryListener = new ProxyFactoryRegistry.IRegistryListener() {
		/**
		 * @see org.eclipse.jem.internal.proxy.core.ProxyFactoryRegistry.IRegistryListener#registryTerminated(ProxyFactoryRegistry)
		 */
		public void registryTerminated(ProxyFactoryRegistry registry) {
			// Need to deactivate all of the editparts because they don't work without a vm available.
			Display.getDefault().asyncExec(new Runnable() {
				/**
				 * @see java.lang.Runnable#run()
				 */
				public void run() {
					setLoadIsPending(true);
					setRootModel(null);
					if (++recycleCntr < 3) {
						// We went down prematurely, so recycle the vm. But we haven't gone down three times in a row prematurely.
						if (getSite().getWorkbenchWindow().getActivePage().getActiveEditor() == JavaVisualEditorPart.this && activationListener.shellActive) {
							restartVM();	// Our editor is the active (though a view could be the active part), our shell is active. Restart now.
						} else
							restartVMNeeded = true;	// Restart next time our editor is activated or given focus.
					}
				}
			});
		}
	};

	private int recycleCntr = 0; // A counter to handle pre-mature terminations. We don't want to keep recycling if it keeps going down.
	private boolean restartVMNeeded = false;

	/*
	 * Restart the vm. This will not reset the recycle vm counter. That is because these are implicit restarts. Not
	 * explicit reload requests. Explicit reload requests will go directly through loadModel(true).
	 */
	private void restartVM() {
		restartVMNeeded = false;
		loadModel(false);
	}

	protected SelectionSynchronizer getSelectionSynchronizer() {
		if (selectionSynchronizer == null)
			selectionSynchronizer = new SelectionSynchronizer();
		return selectionSynchronizer;
	}

	public void dispose() {
		if (DO_TIMER_TESTS)
			TimerTests.basicTest.testState(true);
		TimerTests.basicTest.clearTests();	// Clear any outstanding because we want to test only dispose time.
		try {
			TimerTests.basicTest.startStep("Dispose"); //$NON-NLS-1$
			JavaVisualEditorVMController.disposeEditor(((IFileEditorInput) getEditorInput()).getFile());
			
			if (proxyFactoryRegistry != null) {
				// Remove the proxy registry first so that we aren't trying to listen to any changes and sending them through since it isn't necessary.
				proxyFactoryRegistry.removeRegistryListener(registryListener); // We're going away, don't let the listener come into play.
			}
			
			boolean queuedJobDispose = setupJob != null && setupJob.getState() != Job.NONE;
			if (queuedJobDispose) {
				// We can't actually wait for it because we could get into a deadlock since it may
				// try to do syncExec for some reason. So we will farm off and join in a thread.
				setupJob.cancel();
				final Display d = Display.getCurrent();
				(new Job(CodegenEditorPartMessages.getString("JavaVisualEditorPart.CleanupJVE")) { //$NON-NLS-1$
					protected IStatus run(IProgressMonitor monitor) {
						while (true) {
							try {
								setupJob.join();
								break;
							} catch (InterruptedException e) {
							}
						}
						// Slight possibility display is already disposed, that this occurred on shutdown, so don't bother with final dispose.
						if (!d.isDisposed()) {
							d.asyncExec(new Runnable() {
								public void run() {
									finalDispose();							
								}
							});
						}
						return Status.OK_STATUS;
					}
				}).schedule();
			}
								
			getSite().getPage().removeSelectionListener(selectionServiceListener);
			
			IWorkbenchWindow window = getSite().getWorkbenchWindow();
			
			window.getPartService().removePartListener(activationListener);
			Shell shell = window.getShell();
			if (shell != null && !shell.isDisposed())
				window.getShell().removeShellListener(activationListener);
		
			graphicalActionRegistry.dispose();
			commonActionRegistry.dispose();
			
			if (!queuedJobDispose)
				finalDispose();
		} catch (Exception e) {
		} finally {
			TimerTests.basicTest.stopStep("Dispose"); //$NON-NLS-1$
			TimerTests.basicTest.printIt();
			if (DO_TIMER_TESTS) 
				TimerTests.basicTest.testState(false);
		}
		super.dispose();
	}
	
	private void finalDispose() {
		// This is expected to run in the display thread. It is for the editdomain dispose.
		
		// This is the final dispose that would be called after the setup job is finished. 
		// It is farmed off to here so that the setup job can complete without errors, BUT
		// complete after the rest of the editor has already been closed. This is because
		// we can't wait for the job in the UI thread during dispose. There would be lockups
		// if we did that.
		// Note: No need to sync(this) to access proxyFactoryRegistry because we are guarenteed that
		// we won't be calling finalDispose except if there is no Setup job active. 
		if (proxyFactoryRegistry != null) {
			TimerTests.basicTest.startStep("Dispose Proxy Registry"); //$NON-NLS-1$
			
			// Now we can terminate
			proxyFactoryRegistry.terminateRegistry();
			proxyFactoryRegistry = null;
			TimerTests.basicTest.stopStep("Dispose Proxy Registry"); //$NON-NLS-1$
		}

		modelBuilder.dispose();
		
		if (modelSynchronizer != null) {
			modelSynchronizer.stopSynchronizer();
		}
		
		EditDomain ed = editDomain;
		synchronized (this) {
			editDomain = null;
		}
		ed.dispose();
	}
	
	protected synchronized boolean isDisposed() {
		return editDomain == null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#updateStatusField(java.lang.String)
	 */
	protected void updateStatusField(String category) {
		if (category.equals(JavaVisualEditorActionContributor.STATUS_FIELD_CATEGORY)) {
			IStatusField field= getStatusField(category);
			if (field != null)
				field.setText(currentStatusMessage);
			// Also update the aux viewers status fields if they exist.
			// It is possible that the property sheet page is created, but not yet initialized so the action bars aren't ready yet.
			if (propertySheetPage != null && propertySheetPage.jveStatusField != null)
				propertySheetPage.jveStatusField.setText(currentStatusMessage);
			// It is possible that the outline page is created, but not yet initialized so the action bars aren't ready yet.
			if (beansListPage != null && beansListPage.jveStatusField != null)
				beansListPage.jveStatusField.setText(currentStatusMessage);
			
		} else
			super.updateStatusField(category);
	}
	
	protected IDescriptorPropertySheetEntry rootPropertySheetEntry, currentPropertySheetEntry;
		
	protected IPropertySheetPage getPropertySheetPage() {
		if (propertySheetPage == null)
			createPropertySheetPage();
		return propertySheetPage;
	}
	
	/*
	 * This is the property sheet page for this editor. There
	 * is one page per editor. There is only one PropertySheet, but it
	 * switches to the appropriate page for the current editor automatically.
	 * 
	 * This page can be created and disposed at any time, and recreated again. 
	 */
	protected JavaVisualEditorPropertySheetPage createPropertySheetPageClass() {
		return new JavaVisualEditorPropertySheetPage(this);
	}
	protected void createPropertySheetPage() {
		propertySheetPage = createPropertySheetPageClass();

		rootPropertySheetEntry = new JavaCommandStackPropertySheetEntry(editDomain, editDomain.getCommandStack(), null, null);
		rootPropertySheetEntry.setData(editDomain);
		propertySheetPage.setRootEntry(rootPropertySheetEntry);

		propertySheetPage.addListener(new EToolsPropertySheetPage.Listener() {
			public void controlCreated(Control control) {
				control.addDisposeListener(new DisposeListener() {
					public void widgetDisposed(DisposeEvent e) {
						propertySheetPage = null; // We no longer have one.
						currentPropertySheetEntry = rootPropertySheetEntry = null;
					}
				});
			}
		});
	}
	
	protected IPropertySheetEntryListener propertySheetEntryListener;	
	protected IPropertySheetEntryListener getPropertySheetEntryListener(){
		// Lazily create it because until we have a property sheet and selected entry we don't need the listener.
		if ( propertySheetEntryListener == null){
			propertySheetEntryListener = new IPropertySheetEntryListener(){
				public void childEntriesChanged(IPropertySheetEntry entry){
				}
				public void errorMessageChanged(IPropertySheetEntry entry){
				}
				public void valueChanged(IPropertySheetEntry entry){
					resetHighlightRange();					
					Object source = ((JavaCommandStackPropertySheetEntry)entry).getEditValue();
					Object id = ((JavaCommandStackPropertySheetEntry)entry).getId() ;
					if ((source instanceof IJavaInstance) && id instanceof EStructuralFeature && (getSite().getPage().getActivePart() instanceof PropertySheet)) {
						// Also make sure that the PS is in focus for it to drive to source
						PropertySheet ps = (PropertySheet) getSite().getPage().getActivePart();
						JavaCommandStackPropertySheetEntry pentry = (JavaCommandStackPropertySheetEntry) ((JavaCommandStackPropertySheetEntry) entry).getParent();
						if (ps.getCurrentPage().equals(propertySheetPage) && pentry.getPropertySources() != null && pentry.getPropertySources().length > 0) {							
							IJavaInstance parent = null ;
							if (pentry.getPropertySources()[0].getEditableValue() instanceof IJavaInstance) {
							 	parent = (IJavaInstance) pentry.getPropertySources()[0].getEditableValue() ;
							}
							else {
								// KLUDGE Some intermediate. Get root entry instead.
								IDescriptorPropertySheetEntry rootEntry = (IDescriptorPropertySheetEntry) propertySheetPage.getRootEntry();
								Object val = rootEntry.getValues()[0];
								if (val instanceof EditPart)
									val = ((EditPart) val).getModel();
								if (val instanceof IJavaInstance)
									parent = (IJavaInstance) val;
							}
							if (parent != null)
								select(SELECT_PROPERTY_CHANGE, parent, (EStructuralFeature) id);
						}
					}
				}		
			};
		}
		return propertySheetEntryListener;
	}
	
	/*
	 * This method is used to cause the appropriate section of code
	 * in the text editor to be selected.
	 * 
	 * This method is used whenever a bean is selected in the graphical/tree viewer
	 * or a property has been selected. 
	 * 
	 * NOTE: This should only be called from UI thread.
	 */
	protected void select(int selectionType, Notifier notifier, EStructuralFeature sf) {
		ICodeGenAdapter codeGenAdapter = (ICodeGenAdapter) EcoreUtil.getExistingAdapter(notifier, ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);
		if (codeGenAdapter == null)
		   codeGenAdapter = (ICodeGenAdapter) EcoreUtil.getExistingAdapter(notifier, ICodeGenAdapter.JVE_CODEGEN_EXPRESSION_SOURCE_RANGE);
		if (codeGenAdapter == null)
		   codeGenAdapter = (ICodeGenAdapter) EcoreUtil.getExistingAdapter(notifier, ICodeGenAdapter.JVE_CODE_GEN_TYPE);
		   
		if ((selectionType == SELECT_PROPERTY || selectionType == SELECT_PROPERTY_CHANGE) 
		    	&& codeGenAdapter != null && sf != null && (codeGenAdapter instanceof BeanDecoderAdapter)) {
			// We are driving a property, use it's parent/sf
			ICodeGenAdapter[] list =  ((BeanDecoderAdapter)codeGenAdapter).getSettingAdapters(sf) ;
			if (list != null && list.length>0)
			   codeGenAdapter = list[0] ;
		}
		
		if (codeGenAdapter == null) {
			if (JavaVEPlugin.isLoggingLevel(Level.FINE))
				JavaVEPlugin.log("JavaVisualEditorPart.markerProcessed(): No CodeGen Adapter on: " + notifier, //$NON-NLS-1$
					Level.FINE);
		} else {
			try {
				final ICodeGenSourceRange sourceRange = codeGenAdapter.getHighlightSourceRange();
				// Drive the selection in the java editor
				// Only do this if the source editor is not in focus, because if so then the user
				// may be typing in it and if we drive the cursor away from them they won't like it
				if (!textEditorFocus && sourceRange != null) {
					try {
						setHighlightRange(sourceRange.getOffset(), sourceRange.getLength(), true);
					} catch (Exception exc) {
						exc.printStackTrace();
						// Do nothing - We get assertion failures that I don't fully understand, especially when
						// dropping onto the JavaBeans viewer
					}
				}
			} catch (CodeGenException exc) {
				JavaVEPlugin.log(exc);
			}
		}
	}

	protected JavaVisualEditorModelChangeController modelChangeController;
	
	/*
	 * Job to do the setup for a new input file (and initial initialization if needed).
	 */
	private boolean doTimerStep = true;	// Whether this is the first time for the editor and so do timer stepping.	
	private static final Integer NATURE_KEY = new Integer(0);	// Used within Setup to save the nature	 
	private class Setup extends Job {
		
		public class CreateRegistry extends Job {
			IFile file;
			public CreateRegistry(String name, IFile file) {
				super(name);
				this.file = file;
			}	
			
			protected IStatus run(IProgressMonitor monitor) {

				if (proxyFactoryRegistry != null && proxyFactoryRegistry.isValid()) {
					// Since we need to create a new one, we need to get rid of the old one.
					proxyFactoryRegistry.removeRegistryListener(registryListener); // We're going away, don't let the listener come into play.
					proxyFactoryRegistry.terminateRegistry();			
				}
				
				try {
					JavaVisualEditorVMController.RegistryResult regResult = JavaVisualEditorVMController.getRegistry(file);
					
					// Everything is all set up. Now let's see if we need to rebuild the palette.
					// This is a two pass process.
					
					// First run though all visible containers that implement the IVEContributor1 interface.
					final ResourceSet rset = EMFEditDomainHelper.getResourceSet(editDomain);				
					for (Iterator iter = regResult.configInfo.getContainers().entrySet().iterator(); iter.hasNext();) {
						final Map.Entry entry = (Map.Entry) iter.next();
						if (((Boolean) entry.getValue()).booleanValue())
							if (entry.getKey() instanceof IVEContributor) {
								Platform.run(new ISafeRunnable() {
									public void handleException(Throwable exception) {
										// Default logs fro us.
									}
									public void run() throws Exception {
										rebuildPalette = ((IVEContributor) entry.getKey()).contributePalleteCats(paletteCategories, rset) || rebuildPalette;
									}
								});
							} else if (entry.getKey() instanceof IVEContributor1) {
								Platform.run(new ISafeRunnable() {
									public void handleException(Throwable exception) {
										// Default logs fro us.
									}
									public void run() throws Exception {
										rebuildPalette = ((IVEContributor1) entry.getKey()).contributePalleteCats(paletteCategories, rset) || rebuildPalette;
									}
								}); 
						}
					}
					
					// Now run though visible containers ids.
					final DefaultVEContributor[] defaultVE = new DefaultVEContributor[1];
					if (!regResult.configInfo.getContainerIds().isEmpty()) {
						for (Iterator iter = regResult.configInfo.getContainerIds().entrySet().iterator(); iter.hasNext();) {
							Map.Entry entry = (Map.Entry) iter.next();
							if (((Boolean) entry.getValue()).booleanValue()) {
								final IConfigurationElement[] contributors = JavaVEPlugin.getPlugin().getContainerConfigurations((String) entry.getKey());
								if (contributors != null) {
									for (int i = 0; i < contributors.length; i++) {
										final int ii = i;
										Platform.run(new ISafeRunnable() {
											public void handleException(Throwable exception) {
												// Default logs exception for us.
											}
											public void run() throws Exception {
												if (contributors[ii].getName().equals(PI_PALETTE)) {
													if (defaultVE[0] == null)
														defaultVE[0] = new DefaultVEContributor();
													defaultVE[0].paletteContribution = contributors[ii];
													rebuildPalette = defaultVE[0].contributePalleteCats(paletteCategories, rset) || rebuildPalette;
												} else if (contributors[ii].getName().equals(PI_CONTRIBUTOR)){
													Object contributor = contributors[ii].createExecutableExtension(PI_CLASS);
													if (contributor instanceof IVEContributor)
														rebuildPalette = ((IVEContributor) contributor).contributePalleteCats(paletteCategories, rset) || rebuildPalette;
													else if (contributor instanceof IVEContributor1)
														rebuildPalette = ((IVEContributor1) contributor).contributePalleteCats(paletteCategories, rset) || rebuildPalette;
												}
											}
										});
									}
								}
							}
						}
					}
					
					// Now run though visible plugin ids.
					if (!regResult.configInfo.getPluginIds().isEmpty()) {
						for (Iterator iter = regResult.configInfo.getPluginIds().entrySet().iterator(); iter.hasNext();) {
							Map.Entry entry = (Map.Entry) iter.next();
							if (((Boolean) entry.getValue()).booleanValue()) {
								final IConfigurationElement[] contributors = JavaVEPlugin.getPlugin().getPluginConfigurations((String) entry.getKey());
								if (contributors != null) {
									for (int i = 0; i < contributors.length; i++) {
										final int ii = i;
										Platform.run(new ISafeRunnable() {
											public void handleException(Throwable exception) {
												// Default logs exception for us.
											}
											public void run() throws Exception {
												if (contributors[ii].getName().equals(PI_PALETTE)) {
													if (defaultVE[0] == null)
														defaultVE[0] = new DefaultVEContributor();
													defaultVE[0].paletteContribution = contributors[ii];
													rebuildPalette = defaultVE[0].contributePalleteCats(paletteCategories, rset) || rebuildPalette;
												} else if (contributors[ii].getName().equals(PI_CONTRIBUTOR)){
													try {
														Object contributor = contributors[ii].createExecutableExtension(PI_CLASS);
														if (contributor instanceof IVEContributor)
															rebuildPalette = ((IVEContributor) contributor).contributePalleteCats(paletteCategories, rset) || rebuildPalette;
														else if (contributor instanceof IVEContributor1)
															rebuildPalette = ((IVEContributor1) contributor).contributePalleteCats(paletteCategories, rset) || rebuildPalette;
													} catch (CoreException e) {
														JavaVEPlugin.getPlugin().getLogger().log(e, Level.WARNING);
													}
												}
											}
										});
									}
								}
							}
						}
					}

					// Now pass2 which is for modify palette.
					// Now run though all visible containers that implement the IVEContributor1 interface 
					for (Iterator iter = regResult.configInfo.getContainers().entrySet().iterator(); iter.hasNext();) {
						final Map.Entry entry = (Map.Entry) iter.next();
						if (((Boolean) entry.getValue()).booleanValue())
							if (entry.getKey() instanceof IVEContributor1) {
								Platform.run(new ISafeRunnable() {
									public void handleException(Throwable exception) {
										// Default logs fro us.
									}
									public void run() throws Exception {
										rebuildPalette = ((IVEContributor1) entry.getKey()).modifyPaletteCatsList(paletteCategories) || rebuildPalette;
									}
								}); 
						}
					}
					
					// Now run though visible containers ids.
					if (!regResult.configInfo.getContainerIds().isEmpty()) {
						for (Iterator iter = regResult.configInfo.getContainerIds().entrySet().iterator(); iter.hasNext();) {
							Map.Entry entry = (Map.Entry) iter.next();
							if (((Boolean) entry.getValue()).booleanValue()) {
								final IConfigurationElement[] contributors = JavaVEPlugin.getPlugin().getContainerConfigurations((String) entry.getKey());
								if (contributors != null) {
									for (int i = 0; i < contributors.length; i++) {
										final int ii = i;
										Platform.run(new ISafeRunnable() {
											public void handleException(Throwable exception) {
												// Default logs exception for us.
											}
											public void run() throws Exception {
												if (contributors[ii].getName().equals(PI_PALETTE)) {
													if (defaultVE[0] == null)
														defaultVE[0] = new DefaultVEContributor();
													defaultVE[0].paletteContribution = contributors[ii];
													rebuildPalette = defaultVE[0].modifyPaletteCatsList(paletteCategories) || rebuildPalette;
												} else if (contributors[ii].getName().equals(PI_CONTRIBUTOR)){
													Object contributor = contributors[ii].createExecutableExtension(PI_CLASS);
													if (contributor instanceof IVEContributor1)
														rebuildPalette = ((IVEContributor1) contributor).modifyPaletteCatsList(paletteCategories) || rebuildPalette;
												}
											}
										});
									}
								}
							}
						}
					}
					
					// Now run though visible plugin ids.
					if (!regResult.configInfo.getPluginIds().isEmpty()) {
						for (Iterator iter = regResult.configInfo.getPluginIds().entrySet().iterator(); iter.hasNext();) {
							Map.Entry entry = (Map.Entry) iter.next();
							if (((Boolean) entry.getValue()).booleanValue()) {
								final IConfigurationElement[] contributors = JavaVEPlugin.getPlugin().getPluginConfigurations((String) entry.getKey());
								if (contributors != null) {
									for (int i = 0; i < contributors.length; i++) {
										final int ii = i;
										Platform.run(new ISafeRunnable() {
											public void handleException(Throwable exception) {
												// Default logs exception for us.
											}
											public void run() throws Exception {
												if (contributors[ii].getName().equals(PI_PALETTE)) {
													if (defaultVE[0] == null)
														defaultVE[0] = new DefaultVEContributor();
													defaultVE[0].paletteContribution = contributors[ii];
													rebuildPalette = defaultVE[0].modifyPaletteCatsList(paletteCategories) || rebuildPalette;
												} else if (contributors[ii].getName().equals(PI_CONTRIBUTOR)){
													try {
														Object contributor = contributors[ii].createExecutableExtension(PI_CLASS);
														if (contributor instanceof IVEContributor1)
															rebuildPalette = ((IVEContributor1) contributor).modifyPaletteCatsList(paletteCategories) || rebuildPalette;
													} catch (CoreException e) {
														JavaVEPlugin.getPlugin().getLogger().log(e, Level.WARNING);
													}
												}
											}
										});
									}
								}
							}
						}
					}

					synchronized (JavaVisualEditorPart.this) {
						proxyFactoryRegistry = regResult.registry;
						proxyFactoryRegistry.addRegistryListener(registryListener);
						beanProxyAdapterFactory.setProxyFactoryRegistry(proxyFactoryRegistry);
					}
				} catch (CoreException e) {
					// Wrapper the exception in a status so that when we join we can get the original exception back.
					return new Status(IStatus.ERROR, JavaVEPlugin.getPlugin().getBundle().getSymbolicName(), 16, e.getStatus().getMessage(), e);
				}
				return Status.OK_STATUS;
							
			}
		}
		
		CreateRegistry registryCreateJob;	// Not null if we have one.

		public Setup(String name) {
			super(name);
		}
			
		
		/*
		 * Start the create registry job.
		 */
		private void startCreateProxyFactoryRegistry(IFile file) {
			registryCreateJob = new CreateRegistry(CodegenEditorPartMessages.getString("JavaVisualEditorPart.CreateRemoteVMForJVE"), file); //$NON-NLS-1$
			registryCreateJob.schedule();
		}
		
		
		protected IStatus run(IProgressMonitor monitor) {
			int curPriority = Thread.currentThread().getPriority();
		    ModelChangeController changeController = (ModelChangeController) editDomain.getData(ModelChangeController.MODEL_CHANGE_CONTROLLER_KEY);			
			try {				
			    changeController.transactionBeginning(ModelChangeController.SETUP_PHASE);			    			   
			    
				Thread.currentThread().setPriority(curPriority+BRING_UP_PRIORITY_BUMP);
				if (DO_TIMER_TESTS)
					TimerTests.basicTest.testState(true);
				TimerTests.basicTest.startStep(SETUP_STEP);
				TimerTests.basicTest.startAccumulating(BeaninfoClassAdapter.INTROSPECT);
				TimerTests.basicTest.startAccumulating(BeaninfoClassAdapter.LOAD_FROM_CACHE);
				TimerTests.basicTest.startAccumulating(BeaninfoClassAdapter.REMOTE_INTROSPECT);
				TimerTests.basicTest.startAccumulating(BeaninfoClassAdapter.APPLY_EXTENSIONS);	
				TimerTests.basicTest.startAccumulating(BeaninfoClassAdapter.REFLECT_PROPERTIES);
				TimerTests.basicTest.startAccumulating(JavaClassJDOMAdaptor.REFLECT_CLASS);
				TimerTests.basicTest.startAccumulating(JavaClassJDOMAdaptor.REFLECT_METHODS);
				TimerTests.basicTest.startAccumulating(JavaMethodJDOMAdaptor.REFLECT_METHOD);
				
				restartVMNeeded = false;	// We will be restarting the vm, don't need to have any hanging around.
				monitor.beginTask("", 200); //$NON-NLS-1$
				if (!initialized) {
					initialize(new SubProgressMonitor(monitor, 50));
				} else {
					beanProxyAdapterFactory.setThisTypeName(null);	// Unset the this type since it could actually change after the load is done.
					modelSynchronizer.setIgnoreTypeName(null);	// Nothing to ignore at this time.
					// Check to see if nature is still valid. If it is not, we need to reinitialize for it. OR it 
					// could be we were moved to another project entirely.
					BeaninfoNature nature = (BeaninfoNature) editDomain.getData(NATURE_KEY);
					IFile file = ((IFileEditorInput) getEditorInput()).getFile();					
					if (nature == null || !nature.isValidNature() || !file.getProject().equals(nature.getProject()))
						initializeForProject(file);
					else {
						boolean createFactory = false;
						synchronized (JavaVisualEditorPart.this) {
							createFactory = proxyFactoryRegistry == null || !proxyFactoryRegistry.isValid();
						}
						if (createFactory)
							startCreateProxyFactoryRegistry(file);	// The registry is gone, need new one.
						monitor.worked(100);
					}
				}
			
				if (monitor.isCanceled()) {
					setLoadIsPending(false);
					return canceled();
				}
				
				DiagramData dd = modelBuilder.getModelRoot();
				if (dd != null) {
					// See if we have an old model, and if we do, get the root in progress lock and then clear
					// the model. This will stop the set root from working on an old root by mistake.
					// We don't want to hold up ui, so we will just hold it long enough to release old model.
					setRootInProgressLock.acquire();
					try {
						Resource res = dd.eResource();
						if (res != null) {
							ResourceSet rset = res.getResourceSet();
							if (rset != null)
								rset.getResources().remove(res);
						}
					} finally {
						setRootInProgressLock.release();
					}
				}
			
				TimerTests.basicTest.startStep("Load Model");				 //$NON-NLS-1$
				if (doTimerStep)
					PerformanceMonitorUtil.getMonitor().snapshot(114);	// Starting codegen loading for the first time
				
				modelBuilder.loadModel((IFileEditorInput) getEditorInput(), new SubProgressMonitor(monitor, 100));
				
				if (doTimerStep)
					PerformanceMonitorUtil.getMonitor().snapshot(115);	// Ending codegen loading for the first time
				TimerTests.basicTest.stopStep("Load Model"); //$NON-NLS-1$
				
				monitor.subTask(CodegenEditorPartMessages.getString("JavaVisualEditorPart.InitializingModel")); //$NON-NLS-1$

				if (monitor.isCanceled()) {
					setLoadIsPending(false);
					return canceled();
				}
				dd = modelBuilder.getModelRoot();
				if (dd != null) {
					editDomain.setDiagramData(dd);
					InverseMaintenanceAdapter ia = new InverseMaintenanceAdapter() {
						protected boolean subShouldPropagate(
							EReference ref,
							Object newValue) {
							return !(newValue instanceof JavaAllocation) && !(newValue instanceof Diagram); // On the base BeanComposition, don't propagate into Diagram references or JavaAllocations.
						}
						
						protected boolean subShouldReference(EReference ref, Object newValue) {
							return !(newValue instanceof EClassifier);	// Never reference into the EClass structure
						}
					};
					dd.eAdapters().add(ia);
					ia.propagate();
	
//					TimerTests.basicTest.startStep("Join with remote vm");
					joinCreateRegistry();	// At this point in time we need to have the registry available so that we can initialize all of the proxies.
//					TimerTests.basicTest.stopStep("Join with remote vm");
					
					beanProxyAdapterFactory.setThisTypeName(modelBuilder.getThisTypeName());	// Now that we've joined and have a registry, we can set the this type name into the proxy domain.
					modelSynchronizer.setIgnoreTypeName(modelBuilder.getThisTypeName());
					if (EcoreUtil.getExistingAdapter(
							dd,
							CompositionProxyAdapter.BEAN_COMPOSITION_PROXY)
							== null) {
						CompositionProxyAdapter a =
							new CompositionProxyAdapter();
						dd.eAdapters().add(a);
						
						TimerTests.basicTest.startStep("Create Bean Instances on Target VM");					 //$NON-NLS-1$
						if (doTimerStep)
							PerformanceMonitorUtil.getMonitor().snapshot(116);
						a.initBeanProxy();
						if (doTimerStep)
							PerformanceMonitorUtil.getMonitor().snapshot(117);
						TimerTests.basicTest.stopStep("Create Bean Instances on Target VM");						 //$NON-NLS-1$
					}
					
					if (!monitor.isCanceled())
						getSite().getShell().getDisplay().asyncExec(new Runnable() {
							public void run() {
								if (!isDisposed())
									initializeViewers();
							}
						});
					else
						bumpUIPriority(false,null);
				} else {
					// We didn't get a model for some reason, so just bring down the load controller, the parse error flag should already be set.
					getSite().getShell().getDisplay().asyncExec(new Runnable() {
						public void run() {
							loadingFigureController.showLoadingFigure(false);	// Bring down only the loading figure.
							setReloadEnablement(true);	// Because it was disabled.														
						}
					});
					setLoadIsPending(false);
					return canceled();
				}
			
			} catch (final Exception x) {
				noLoadPrompt(x);
				setLoadIsPending(false);
				canceled();
				return (x instanceof CoreException) ? ((CoreException) x).getStatus() : Status.CANCEL_STATUS;
			}
			finally {
				Thread.currentThread().setPriority(curPriority);
			    changeController.transactionEnded(ModelChangeController.SETUP_PHASE);				
			}
			
			if (rebuildPalette && !monitor.isCanceled()) {
				// We want to rebuild palette and the monitor was not cancelled.
				getSite().getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						if (!isDisposed())
							rebuildPalette();
					}
				});				
			}
			
			monitor.done();
			setLoadIsPending(false);
			TimerTests.basicTest.stopAccumulating(BeaninfoClassAdapter.REMOTE_INTROSPECT);
			TimerTests.basicTest.stopAccumulating(BeaninfoClassAdapter.APPLY_EXTENSIONS);				
			TimerTests.basicTest.stopAccumulating(BeaninfoClassAdapter.REFLECT_PROPERTIES);			
			TimerTests.basicTest.stopAccumulating(BeaninfoClassAdapter.INTROSPECT);
			TimerTests.basicTest.stopAccumulating(BeaninfoClassAdapter.LOAD_FROM_CACHE);
			TimerTests.basicTest.stopAccumulating(JavaClassJDOMAdaptor.REFLECT_CLASS);
			TimerTests.basicTest.stopAccumulating(JavaClassJDOMAdaptor.REFLECT_METHODS);
			TimerTests.basicTest.stopAccumulating(JavaMethodJDOMAdaptor.REFLECT_METHOD);
			TimerTests.basicTest.stopStep(SETUP_STEP);
			return !monitor.isCanceled() ? Status.OK_STATUS : Status.CANCEL_STATUS;
		}

		/**
		 * @throws Exception
		 * 
		 * @since 1.0.0
		 */
		private void joinCreateRegistry() throws Exception {
			if (registryCreateJob != null) {
				while (true) {
					try {
						registryCreateJob.join(); // Need to join up so that we don't have hanging out there.
						break;
					} catch (InterruptedException e) {
					}
				}
				if (registryCreateJob.getResult().getSeverity() == IStatus.ERROR) {
					// It had a severe error, get the exception out of the status (which we had put there) and rethrow it.
					Exception e = (Exception) registryCreateJob.getResult().getException();
					registryCreateJob = null;	// So we don't wait again.
					throw e;
				}
				registryCreateJob = null;	// So we don't wait again.
			}
		}


		/**
		 * @return
		 * 
		 * @since 1.0.0
		 */
		private IStatus canceled() {
			if (registryCreateJob != null) {
				while (true) {
					try {
						bumpUIPriority(false,null);
						registryCreateJob.join(); // Need to join up so that we don't have hanging out there.
						break;
					} catch (InterruptedException e) {
					}
				}
				registryCreateJob = null;	// We waited and so no more. We don't care if there were any errors while creating the registry.
			}
			return Status.CANCEL_STATUS;
		}


		protected void initialize(final IProgressMonitor monitor) throws CoreException {
			monitor.beginTask("", 100); //$NON-NLS-1$
			initialized = true;
			initializeEditDomain();		
			modelBuilder.setSynchronizerSyncDelay(VCEPreferences.getPlugin().getPluginPreferences().getInt(VCEPreferences.SOURCE_SYNC_DELAY));
			beanProxyAdapterFactory = new BeanProxyAdapterFactory(null, editDomain, new BasicAllocationProcesser());
			
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			initializeForProject(file);

			if (monitor.isCanceled())
				return;
			monitor.worked(25);
			
			// Add listener to part activation so that we can handle recycle the vm when reactivated.
			final IWorkbenchWindow window = getSite().getWorkbenchWindow();
			window.getPartService().addPartListener(activationListener);
			window.getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					if (!window.getShell().isDisposed() && !isDisposed() && !monitor.isCanceled())
						window.getShell().addShellListener(activationListener);
				}
			});
			activationListener.partActivated(window.getPartService().getActivePart());	// Initialize to current active part
			monitor.worked(25);
			
			synchronized (JavaVisualEditorPart.this) {
				// Add the model synchronizer.
				modelSynchronizer = new JavaModelSynchronizer(beanProxyAdapterFactory, JavaCore.create(file.getProject()), new JavaModelSynchronizer.TerminateRunnable() {
					/**
					 * @see java.lang.Runnable#run()
					 */
					public void run(boolean close) {
						if (close)
							recycleCntr = Integer.MAX_VALUE;	// Set it to max to indicate don't reload at this time. This is because our project is being deleted/closed.
						else
							recycleCntr = 0; // So that this resets the cntr for premature terminates.
						if (proxyFactoryRegistry != null)
							proxyFactoryRegistry.terminateRegistry();	// Terminate registry
					}
				});
			}
			monitor.worked(25);
			
			// When the user types into a java editor the model is changed incrementally
			// We need to know when this happens so we can drive a refresh on the property sheet
			// AND refresh the text viewer.
			modelBuilder.addIBuilderListener(new IDiagramModelBuilder.IBuilderListener() {
				public void modelUpdated() {
					getSite().getShell().getDisplay().asyncExec(new Runnable() {
						public void run() {
							if (rootPropertySheetEntry != null) {
								// Horrid kludge: The model could be reloaded from scratch at this point,
								// which means any editpart has been deselected. However, if the PropertySheet
								// was the active viewer, it wouldn't know this because for him he listens to
								// Workbench selection provider, and when he is the active viewer, he IS the
								// selection provider. In that case the deselect in the graph and tree viewer
								// would go unnoticed. Also, the editparts would no longer exist at this point,
								// so the PS is using old deactivated editparts. So to get around this, we will get
								// the root entry and see if it is an editpart and that editpart is not selected.
								// If that is so, we will set no selection into the PS so that it won't have 
								// an invalid value.
								//
								// The reason this happens is because if you do a ctrl-z (undo) from the PS,
								// this could cause a reload from scratch to occur if complicated enough. Now the
								// PS doesn't expect the model that it is currently handling to be pulled out
								// from under it while it has focus. It uses the Workbench Selection provider to
								// handle changes.
							
								Object[] value = rootPropertySheetEntry.getValues();
								if (value.length == 1 && value[0] instanceof EditPart) {
									if (((EditPart) value[0]).getSelected() == EditPart.SELECTED_NONE)
										rootPropertySheetEntry.setValues(new Object[0]);
									else
										rootPropertySheetEntry.refreshFromRoot();
								} else
									rootPropertySheetEntry.refreshFromRoot();
							}
							refreshTextPage();	// Because model has been updated.
						}
					});
				}

		    	public void statusChanged (String msg){
		    		currentStatusMessage = msg;
		    		getSite().getShell().getDisplay().asyncExec(new Runnable() {
						public void run() {
							updateStatusField(JavaVisualEditorActionContributor.STATUS_FIELD_CATEGORY);
						}
					});
		    	}
				public void reloadIsNeeded(){
					getSite().getShell().getDisplay().asyncExec(new Runnable() {
						public void run() {
							loadModel(true);
						}
					});
				}
				
				public void parsingStatus (boolean error){
					processParseError(error);
				}
				
				public void parsingPaused(boolean paused) {
					if (paused) {
						// If a snippet job is canceled, we will move into the paused state
						((ReloadAction) graphicalActionRegistry.getAction(ReloadAction.RELOAD_ACTION_ID)).setPause();
					}
				}
			});
			
			monitor.done();
		}

		protected void initializeForProject(IFile file) throws CoreException {
			IProject proj = file.getProject();
			BeaninfoNature nature = BeaninfoNature.getRuntime(proj);
			JavaProjectUtilities.addToBuildSpec(JavaVEPlugin.VE_BUILDER_ID, proj);
			ResourceSet rs = nature.newResourceSet();
			EMFEditDomainHelper.setResourceSet(rs, editDomain);
			editDomain.setData(NATURE_KEY, nature);	// Need to save the nature so we can check for validity later if project is renamed.
			
			// Add the property source and property descriptor adapter factories
			rs.getAdapterFactories().add(new PropertySourceAdapterFactory(editDomain));
			rs.getAdapterFactories().add(new PropertyDescriptorAdapterFactory());
			
			// Initialize the nature for the project that creates a remote VM and registers the 
			// java:/ protocol in MOF
			JavaInstantiation.initialize(nature.getResourceSet());
			
			// Make sure that there is an AdaptorFactory for BeanProxies installed
			rs.getAdapterFactories().add(beanProxyAdapterFactory);
			
			// Create the target VM that is used for instances within the document we open
			startCreateProxyFactoryRegistry(file);
						
			if (modelSynchronizer != null)
				modelSynchronizer.setProject(JavaCore.create(proj));
		}
		
		protected void initializeEditDomain() {
			// Initialize the editdomain. None of this is required to just create the viewers. That initialization
			// was done back in the main init method. This is required though before we can start loading the model
			// and the viewer with the data.
			editDomain.setRuleRegistry(JVEStyleRegistry.getJVEStyleRegistry().getStyle(VCEPreferences.getStyleID()));
			editDomain.setData(ExpressionDecoderFactory.CodeGenDecoderFactory_KEY, new ExpressionDecoderFactory());
			editDomain.setData(MethodGeneratorFactory.CodeGenMethodGeneratorFactory_KEY, new MethodGeneratorFactory());

			editDomain.setAnnotationLinkagePolicy(new EMFAnnotationLinkagePolicy());
			ClassDescriptorDecoratorPolicy policy = ClassDescriptorDecoratorPolicy.getPolicy(editDomain);
			CDEUtilities.setModelAdapterFactory(editDomain, new DefaultModelAdapterFactory(policy));

			NameInCompositionPropertyDescriptor desc = new NameInMemberPropertyDescriptor(VCEMessages.getString("nameInComposition.displayName"), new FieldNameValidator()); //$NON-NLS-1$
			editDomain.registerKeyedPropertyDescriptor(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY, desc);
			// Make the default add annotations command be the one to make names unique.
			try {
				editDomain.setDefaultAddAnnotationsCommandClass(AddAnnotationsWithName.class);
			} catch (ClassCastException e) {
				// Can't happen, but it throws it
			} catch (NoSuchMethodException e) {
				// Can't happen, but it throws it
			}
		}
	};

	private ActivationListener activationListener = new ActivationListener();

	/*
	 * This class listens for activation either the workbench or because we clicked
	 * back to the workbench after being on some other window. Its purpose is for
	 * the RemoteVM. When the part becomes active, or is given focus, we want to
	 * restart the VM if it had been shutdown for some reason.
	 */
	private class ActivationListener extends ShellAdapter implements IPartListener {

		public IWorkbenchPart activePart;
		private boolean isHandlingActivation = false;
		public boolean shellActive = true;
		// Assume that when we get added that the shell is active, or we wouldn't of been able to run the code to add it.

		/*
		 * @see IPartListener#partActivated(IWorkbenchPart)
		 */
		public void partActivated(IWorkbenchPart part) {
			// Activation driven selection is about to come about
			firstSelection=true;			
			activePart = part;
			handleActivation();
		}

		protected void handleActivation() {
			if (isHandlingActivation)
				return;

			if (activePart == JavaVisualEditorPart.this && restartVMNeeded) {
				isHandlingActivation = true;
				try {
					restartVM();
				} finally {
					isHandlingActivation = false;
				}
			}
		}

		/*
		 * @see IPartListener#partBroughtToTop(IWorkbenchPart)
		 */
		public void partBroughtToTop(IWorkbenchPart part) {
		}

		/*
		 * @see IPartListener#partClosed(IWorkbenchPart)
		 */
		public void partClosed(IWorkbenchPart part) {
		}

		/*
		 * @see IPartListener#partDeactivated(IWorkbenchPart)
		 */
		public void partDeactivated(IWorkbenchPart part) {
			activePart = null;
		}

		/*
		 * @see IPartListener#partOpened(IWorkbenchPart)
		 */
		public void partOpened(IWorkbenchPart part) {
		}

		/*
		 * @see org.eclipse.swt.events.ShellListener#shellActivated(ShellEvent)
		 */
		public void shellActivated(ShellEvent e) {
			shellActive = true;
			handleActivation();
		}

		/**
		 * @see org.eclipse.swt.events.ShellListener#shellDeactivated(ShellEvent)
		 */
		public void shellDeactivated(ShellEvent e) {
			shellActive = false;
		}
	};
	
	/*
	 * Focus listener to determine whether text or graphical editor is active.
	 * 
 	 * It listens only for focus given. That way if focus is given to some other part entirely,
	 * the last editor (text/graphical) in focus will still be considered to be the provider.
	 * 
	 * It will also switch in the appropriate common actions (like Delete) for the current editor
	 */
	private boolean textEditorActive = false;
	private boolean textEditorFocus = false;
	private boolean firstSelection = true ;  // First selection after activation
	private ICommandManager commandMgr = PlatformUI.getWorkbench().getCommandSupport().getCommandManager();
	private FocusListener focusListener = new FocusListener() {
		private void enableDelCommand(boolean flag) {
			//		TODO: this is a temporary workaround to the fact that the compilation unit editor
			//      uses a package protected AdapterSourceViewer to handle command's target.
			ICommand c = commandMgr.getCommand("org.eclipse.ui.edit.delete") ; //$NON-NLS-1$
			try {
				Class cmdClazz = c.getClass();				
				Method m = cmdClazz.getDeclaredMethod("setDefined", new Class[] { boolean.class } ); //$NON-NLS-1$
				m.setAccessible(true);
				m.invoke(c, new Object[] {new Boolean(flag)}) ;				
			} catch (Exception e1) {
				JavaVEPlugin.log(e1);
			}
		}
		public void focusGained(FocusEvent e) {			
			textEditorFocus = textEditorActive = e.getSource() == getSourceViewer().getTextWidget();
// TODO fix for Eclipse 3.1			enableDelCommand(textEditorFocus); // disable the del command if the GEF pane is in focus.
			if (textEditorActive) {
				
				// Set the common actions up from the text editor.
				Iterator itr = commonActionRegistry.getActions();
				while (itr.hasNext()) {
					RetargetTextEditorAction action = (RetargetTextEditorAction) itr.next();
					action.setAction(JavaVisualEditorPart.this.superGetAction(action.getId()));
				}
			} else {
				// Set the common actions up from the graph editor.
				Iterator itr = commonActionRegistry.getActions();
				while (itr.hasNext()) {
					RetargetTextEditorAction action = (RetargetTextEditorAction) itr.next();
					action.setAction(graphicalActionRegistry.getAction(action.getId()));
				}				
			}
		}
		public void focusLost(FocusEvent e) {
			if (textEditorActive)
				textEditorFocus = false;
//	TODO fix for Eclipse 3.1		enableDelCommand(true);  // ReActivate the command for other editors
		}
	};
		
	/*
	 * Selection provider for this editor. It signals out selections from either
	 * the graph viewer of the java text editor, whichever one is currently active.
	 * We need to use whichever is active.
	 * We just can't fire selections from either one when they occur because selections
	 * in one will cause selections in the other and visa-versa.
	 * 
	 * This is then given to the site so that it can be the provider for this workbench part.
	 * 
	 * Note: It needs to be created after the controls are created so that the appropriate
	 * providers can be accessed.
	 */
	private class JavaSelectionProvider implements ISelectionProvider {
		ListenerList listeners = new ListenerList();
		
		public JavaSelectionProvider() {
			getSelectionProvider().addSelectionChangedListener(new ISelectionChangedListener() {
				public void selectionChanged(SelectionChangedEvent event) {
					if (textEditorActive)
						fireSelection(event);
				}
			});
			primaryViewer.addSelectionChangedListener(new ISelectionChangedListener() {
				public void selectionChanged(SelectionChangedEvent event) {
					if (!textEditorActive)
						fireSelection(event);
				}
			});			
		}
		
		protected void fireSelection(SelectionChangedEvent event) {
			final SelectionChangedEvent parentEvent = new SelectionChangedEvent(this, event.getSelection());
			Object[] listeners = this.listeners.getListeners();
			for (int i = 0; i < listeners.length; ++i) {
				final ISelectionChangedListener l = (ISelectionChangedListener)listeners[i];
				Platform.run(new SafeRunnable() {
					public void run() {
						l.selectionChanged(parentEvent);
					}
					public void handleException(Throwable e) {
						super.handleException(e);
						//If and unexpected exception happens, remove it
						//to make sure the workbench keeps running.
						removeSelectionChangedListener(l);
					}
				});		
			}
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ISelectionProvider#addSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
		 */
		public void addSelectionChangedListener(ISelectionChangedListener listener) {
			listeners.add(listener);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
		 */
		public ISelection getSelection() {
			return (textEditorActive) ?	getSelectionProvider().getSelection() : primaryViewer.getSelection();
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ISelectionProvider#removeSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
		 */
		public void removeSelectionChangedListener(ISelectionChangedListener listener) {
			listeners.remove(listener);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse.jface.viewers.ISelection)
		 */
		public void setSelection(ISelection selection) {
			if (textEditorActive)
				getSelectionProvider().setSelection(selection);
			else
				primaryViewer.setSelection(selection);
		}

	}

	private void processParseError(boolean parseError) {
		modelChangeController.setHoldState(parseError ? PARSE_ERROR_STATE : ModelChangeController.READY_STATE, null);
		((ReloadAction) graphicalActionRegistry.getAction(ReloadAction.RELOAD_ACTION_ID)).parseError(parseError);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapterKey) {
		if (adapterKey == IPropertySheetPage.class)
			return getPropertySheetPage();
		else if (adapterKey == BeansList.class)
			return getContentOutlinePage();
		else if (adapterKey == EditPartViewer.class)
			return primaryViewer;	// Current impl. only has one active editpart viewer. The outline viewer is its own IWorkbenchPart.
		else if (adapterKey == CommandStack.class)
			return editDomain.getCommandStack();
		else if (adapterKey == PalettePage.class){
			return getPalettePage();
		}
		else
			return super.getAdapter(adapterKey);
	}
	
	private class CustomPalettePage extends PaletteViewerPage {
		public CustomPalettePage(PaletteViewerProvider provider) {
			super(provider);
		}
		public void createControl(Composite parent) {
			super.createControl(parent);
			if (paletteSplitter != null)
				paletteSplitter.setExternalViewer(viewer);
		}
		public void dispose() {
			if (paletteSplitter != null)
				paletteSplitter.setExternalViewer(null);
			super.dispose();
		}
		public PaletteViewer getPaletteViewer() {
			return viewer;
		}
	}
	
	protected PalettePage getPalettePage(){
		if (paletteSplitter == null) {
			// The palettePage is saved only because we not yet created out main control. It is
			// needed in there to setup. Once setup, it will be nulled out.
			palettePage = new CustomPalettePage(getPaletteViewerProvider());
			return palettePage;
		}
		return new CustomPalettePage(getPaletteViewerProvider());
	}
	
	protected IContentOutlinePage getContentOutlinePage(){
		if (beansListPage == null)
			beansListPage = new JavaVisualEditorOutlinePage(this, new TreeViewer());
		return beansListPage;
	}

	/*
	 * The purpose of this class is to listen to the Workbench page selection service.
	 * This way we only get one notification of selection whether it came from the BeansList,
	 * Graphical Viewer, or PropertySheet. The problem this is solving is whether the selection came from
	 * the BeansList or the GraphicalViewer we want to know the selection. If we just listened
	 * to the graphviewer, then we will only see selections of editparts that are in the graph viewer.
	 * The tree viewer, through the selection synchronizer, will try to select the corresponding editparts
	 * in the graph viewer. But if they don't exist there, then the graph viewer doesn't fire selection.
	 * Because of this we will miss these. However we don't to just listen to both, because then we would
	 * get two selection notifications, one from each. So instead, we will listen to the Selection Service.
	 * This will be only one selection sent, and we get the selection from whichever one is active. 
	 * 
	 * Adding in property sheet too just so we only have one listener to worry about.
	 * 
	 * Another advantage is that we don't hear selections that were made programatically while the viewer/editor
	 * was not the active one.
	 */
	private class SelectionServiceListener implements ISelectionListener {
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if (part == JavaVisualEditorPart.this) {
				if (!textEditorActive)
					editPartSelectionChanged(part, selection);
			} else if (part instanceof BeansList && ((BeansList) part).getCurrentPage() == beansListPage)
				editPartSelectionChanged(part, selection);
			else if (part instanceof PropertySheet && ((PropertySheet) part).getCurrentPage() == propertySheetPage)
				propertySheetSelectionChanged(selection);			
		}
		
		protected void editPartSelectionChanged(IWorkbenchPart part, ISelection epSelection) {
			// Get the JavaBeans that are selected
			StructuredSelection selection = (StructuredSelection) epSelection;
			IActionBars actionBars = null;
			if (part instanceof IEditorPart)
				actionBars = ((IEditorPart) part).getEditorSite().getActionBars();
			else if (part instanceof IViewPart)
				actionBars = ((IViewPart) part).getViewSite().getActionBars();
				
			setStatusMsg(actionBars,null,null); // Clear the status bar as we try and show any errors for the selected JavaBean
			// Only try and drive the source editor selection if one edit part is selected
			// If more than one is selected then don't bother 
			if (selection.size() == 1 && selection.getFirstElement() instanceof EditPart) {
				EditPart editPart = (EditPart) selection.getFirstElement();
				if (editPart.getModel() instanceof Notifier) {
					IErrorNotifier errNotifier =
						(IErrorNotifier) EcoreUtil.getExistingAdapter((Notifier) editPart.getModel(), IErrorNotifier.ERROR_NOTIFIER_TYPE);
					if (errNotifier != null) {
						// See whether or not there are any errors associated with the part - if so show them on the taskBar
						if (errNotifier.getErrorStatus() != IErrorNotifier.ERROR_NONE) {
							Iterator errors = errNotifier.getErrors().iterator();
							IErrorHolder.ErrorType error = (IErrorHolder.ErrorType) errors.next();
							setStatusMsg(actionBars,error.getMessage(),error.getImage());
						}
					}
				}
				
				// Drive the selection of the model so the action bar and status range are updated correctly
				modelSelected(editPart.getModel());
				
			}			
		}
		
		protected void propertySheetSelectionChanged(ISelection selection) {
			if (!selection.isEmpty()) {
				// We know the property sheet is a single selection viewer, so we don't need to worry about multiple selections
				// Also, since we know it is our property sheet, then we know the entries are of the correct type.
				IDescriptorPropertySheetEntry entry = (IDescriptorPropertySheetEntry) ((IStructuredSelection) selection).getFirstElement();
				// We must listen to each entry as it is selected ( and stop listener to it as well )
				// so that we can listen to when the value is changed so we can drive selection
				if (currentPropertySheetEntry != entry) {
					if (currentPropertySheetEntry != null)
						currentPropertySheetEntry.removePropertySheetEntryListener(getPropertySheetEntryListener());
					entry.addPropertySheetEntryListener(getPropertySheetEntryListener());
					currentPropertySheetEntry = entry;
				}
				if (!entry.isStale()) {
					// It is possible that there are pending changes that need to be generated to code
					// before we can select the source range
					if (entry.getId() instanceof EStructuralFeature) {
						Object prop = entry.getPropertySources().length>0 && 
						              entry.getPropertySources()[0] != null ? 
						              entry.getPropertySources()[0].getEditableValue() : null ;
						if (prop != null && (prop instanceof IJavaObjectInstance) && 
						    EcoreUtil.getExistingAdapter((Notifier)prop,ICodeGenAdapter.JVE_CODEGEN_EXPRESSION_SOURCE_RANGE)!=null) {
							select(SELECT_PROPERTY, (Notifier)prop, null);
						}						
						else {
							IDescriptorPropertySheetEntry pentry = entry.getParent();
							if (pentry.getPropertySources() != null && pentry.getPropertySources().length > 0) {
								Object val = pentry.getPropertySources()[0].getEditableValue();
								IJavaInstance parent = null;
								if (val instanceof IJavaInstance) {
									parent = (IJavaInstance) val;
								}
								else {
									// KLUDGE Can be an intermediate, just select the root of the property sheet. It should be an editpart. Need a better way of handling this.
									IDescriptorPropertySheetEntry rootEntry = (IDescriptorPropertySheetEntry) propertySheetPage.getRootEntry();
									val = rootEntry.getValues()[0];
									if (val instanceof EditPart)
										val = ((EditPart) val).getModel();
									if (val instanceof IJavaInstance)
										parent = (IJavaInstance) val;
								}
								if (parent != null)
									select(SELECT_PROPERTY, parent, (EStructuralFeature) entry.getId());
							}
						}
					}
				}
			} else {
				// need to remove if one is listening
				if (currentPropertySheetEntry != null) {
					currentPropertySheetEntry.removePropertySheetEntryListener(getPropertySheetEntryListener());
					currentPropertySheetEntry = null;
				}
			}
		}
	}
	
	protected IAction superGetAction(String id) {
		return super.getAction(id);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.ITextEditor#getAction(java.lang.String)
	 */
	public IAction getAction(String actionID) {
		// See if a common action, or text, or graphical action.
		IAction result = commonActionRegistry.getAction(actionID);
		if (result == null) {
			result = super.getAction(actionID);
			if (result == null)
				result = graphicalActionRegistry.getAction(actionID);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
		if (primaryViewer == null || textEditorActive)
			super.setFocus();
		else
			primaryViewer.getControl().setFocus();
			
	}
	/* 
	 * A model was selected.  We need to select the corect source range for the code 
	 */
	public void modelSelected(Object model) {
		if (firstSelection) {
			// Selection is driven because the editor/viewer is activated - not because
			// a user has selected the model.  
			firstSelection = false ;
			return ;
		}
		// If a cursor has moved, we want to drive back - reset the HighlightRange()
		resetHighlightRange() ;
		if (model instanceof IJavaInstance)
			select(SELECT_JVE, (IJavaInstance) model, null);
		else if (model instanceof EventInvocationAndListener) {
			EventInvocationAndListener eil = (EventInvocationAndListener) model ;
			AbstractEventInvocation ei = (AbstractEventInvocation) eil.getEventInvocations().get(0) ;
			select(SELECT_JVE, ei, null);
		} else if (model instanceof Notifier)
			select(SELECT_JVE, (Notifier) model, null);								
	}
	
	protected void setStatusMsg (IActionBars bars, String msg, Image icon) {
		if (msg==null || bars==null) {
			statusMsgSet=false;			
		}
		else
			statusMsgSet=true;		
		
		bars.getStatusLineManager().setMessage(icon, msg);		
	}
	
	

	/** 
	 *  Overide 
	 *  The JavaEditor will erase our error msg on a post selection
	 */
	protected void setStatusLineMessage(String msg) {
		if (msg != null || !statusMsgSet)
		   super.setStatusLineMessage(msg);
	}
	protected void setLoadIsPending(boolean flag) {
		synchronized (loadCompleteSync) {
			if (flag) {
				isLoadPending=true;
			}
			else {
				isLoadPending=false;
				loadCompleteSync.notifyAll();				
			}
		}
	}
	/**
	 * Suspend thread, if a reaload is pending until the load is completed.
	 * @param displayThread  if true will spawn a thread and not block the UI display loop.
	 */
	public void waitIfLoading(boolean displayThread) {
		if (!isLoadPending)
			return ;   // most likely case
		
		if (!displayThread) {
			// Sleep until load is complete
		 synchronized (loadCompleteSync) {			
			while (isLoadPending)
				try {
					loadCompleteSync.wait();
				} catch (InterruptedException e) {}
		 }
		}
		else {
			try {
				PlatformUI.getWorkbench().getProgressService().busyCursorWhile(new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						monitor.beginTask(CodegenEditorPartMessages.getString("JavaVisualEditorPart.26"), 100); //$NON-NLS-1$
						 synchronized (loadCompleteSync) {			
							while (isLoadPending)
								try {
									loadCompleteSync.wait();
								} catch (InterruptedException e) {}
						 }
						 monitor.done();
					}
				});
			} catch (InvocationTargetException e) {
				JavaVEPlugin.log(e.getCause());
			} catch (InterruptedException e) {}
		}
		
		
	}
	public void doSave(IProgressMonitor progressMonitor) {
		progressMonitor.beginTask(MessageFormat.format(CodegenEditorPartMessages.getString("JavaVisualEditorPart.27"), new Object[] {getEditorInput().getName()}),100); //$NON-NLS-1$
		super.doSave(new SubProgressMonitor(progressMonitor, 50));
		modelBuilder.doSave(new SubProgressMonitor(progressMonitor,50));
		progressMonitor.done();		
	}
}
