package org.eclipse.ve.internal.java.codegen.editorpart;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JavaVisualEditorPart.java,v $
 *  $Revision: 1.16 $  $Date: 2004-03-24 15:07:39 $ 
 */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.*;
import org.eclipse.draw2d.*;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.gef.*;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.*;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.gef.ui.actions.*;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.parts.*;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.ui.IContextMenuConstants;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.IRewriteTarget;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.texteditor.*;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.navigator.ResourceNavigatorMessages;
import org.eclipse.ui.views.properties.*;

import org.eclipse.jem.internal.beaninfo.adapters.BeaninfoNature;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.cdm.Diagram;
import org.eclipse.ve.internal.cdm.DiagramData;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator;
import org.eclipse.ve.internal.cde.emf.*;
import org.eclipse.ve.internal.cde.palette.*;
import org.eclipse.ve.internal.cde.palette.Category;
import org.eclipse.ve.internal.cde.palette.Palette;
import org.eclipse.ve.internal.cde.properties.*;

import org.eclipse.ve.internal.jcm.AbstractEventInvocation;
import org.eclipse.ve.internal.jcm.BeanSubclassComposition;

import org.eclipse.ve.internal.java.codegen.core.*;
import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.vce.*;
import org.eclipse.ve.internal.java.vce.rules.JVEStyleRegistry;

import org.eclipse.ve.internal.propertysheet.EToolsPropertySheetPage;
import org.eclipse.ve.internal.propertysheet.IDescriptorPropertySheetEntry;


/**
 * @author richkulp
 *
 * Java Visual Editor
 */
public class JavaVisualEditorPart extends CompilationUnitEditor implements DirectSelectionInput {
	
	public final static String MARKER_PropertySelection	= "propertySheetSelection" ; //$NON-NLS-1$
	public final static String MARKER_PropertyChange 	= "propertySheetValueChange" ; //$NON-NLS-1$
	public final static String MARKER_JVESelection	  	= "VCE Selection" ; //$NON-NLS-1$
	
	public final static String PI_CLASS = "class";
	public final static String PI_PALETTE = "palette";
	public final static String PI_CONTRIBUTOR = "contributor";
	public final static String PI_LOC = "loc";
	public final static String PI_CATEGORIES = "categories";
	public final static String PI_LAST = "last";
	
	protected boolean initialized = false;
	protected EditDomain editDomain;

	protected SelectionSynchronizer selectionSynchronizer;
	protected JavaSelectionProvider mainSelectionProvider;

	protected GraphicalViewer primaryViewer;
	protected XMLTextPage xmlTextPage;

	protected LoadingFigureController loadingFigureController;

	protected StatusRenderer statusController;
	protected IDiagramModelBuilder modelBuilder;

	protected ProxyFactoryRegistry proxyFactoryRegistry;
	protected JavaModelSynchronizer modelSynchronizer;
	protected BeanProxyAdapterFactory beanProxyAdapterFactory;
	protected boolean rebuildPalette = true;
	
	protected EToolsPropertySheetPage propertySheetPage;
	
	protected JavaOutlinePage beansListPage;
	
	protected ActionRegistry graphicalActionRegistry = new ActionRegistry();	// Registry of just the graphical ones that need to be accessed by action contributor.
	
	// Registry of actions that are in common with the graph viewer and the java text editor that need to be accessed by action contributor.
	// What is different is that these actions are all RetargetTextEditorActions. The focus listener will make sure that whichever
	// viewer/editor is in focus, the appropriate action from either the text/graph viewer is set into the retarget action.
	// This allows the action bar/menu of the editor contributor to have the correct action depending on which is in focus. 
	protected ActionRegistry commonActionRegistry = new ActionRegistry();
	
	protected OpenActionGroup openActionGroup;
	
	protected SelectionServiceListener selectionServiceListener = new SelectionServiceListener();

	public JavaVisualEditorPart() {
		// User TimerStep APIs for performance measurements
//		TimerStep.instance().writeEnvironment(com.ibm.etools.logging.util.BuildInfo.getWSABuildLevel());
//		TimerStep.instance().writeCounters2(100);		
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
							CodegenEditorPartMessages.getString("JavaVisualEditor.notJavaProject"), //$NON-NLS-1$
							new Object[] { proj.getName(), input.getName()}));
			} catch (CoreException e) {
				throw new PartInitException(e.getStatus());
			}
		} else
			throw new PartInitException(
				MessageFormat.format(CDEMessages.getString("NOT_FILE_INPUT_ERROR_"), new Object[] { input.getName()}));

		// We need the following now because both the thread that will be spawned off in doSetInput and 
		// in the createPartControl need the statusController and loadingFigureControler and EditDomain
		// to exist at that time. 
		//
		// Actually the createPartControl will probably occur before the thread runs to do the rest of
		// the initialization.	
		statusController = new StatusRenderer();
		loadingFigureController = new LoadingFigureController();
		statusController.addStatusListener(loadingFigureController);

		// Do any initializations that are needed for both the viewers and the codegen parser when they are created.
		// Need to be done here because there would be race condition between the two threads otherwise.
		editDomain = new EditDomain(this);
		ClassDescriptorDecoratorPolicy policy = new ClassDescriptorDecoratorPolicy();
		// Get the default decorator and put a JavaBean label provider onto it
		ClassDescriptorDecorator defaultClassDescriptorDecorator = (ClassDescriptorDecorator) policy.getDefaultDecorator(ClassDescriptorDecorator.class);
		defaultClassDescriptorDecorator.setLabelProviderClassname("org.eclipse.ve.cde/org.eclipse.ve.internal.cde.properties.DefaultLabelProviderWithName"); //$NON-NLS-1$
		
		ClassDescriptorDecoratorPolicy.setClassDescriptorDecorator(editDomain, policy);
		editDomain.setCommandStack(new JavaVisualEditorCommandStack(modelChangeController));
		editDomain.setData(IModelChangeController.MODEL_CHANGE_CONTROLLER_KEY, modelChangeController);		
				
		modelBuilder = new JavaSourceTranslator(editDomain);

		// Create the common actions
		ISharedImages images = PlatformUI.getWorkbench().getSharedImages();
		RetargetTextEditorAction deleteAction = new RetargetTextEditorAction(CodegenEditorPartMessages.RESOURCE_BUNDLE, "Action.Delete."); //$NON-NLS-1$
		deleteAction.setHoverImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_HOVER));
		deleteAction.setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		deleteAction.setDisabledImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
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
			
		super.init(site, input);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#doSetInput(org.eclipse.ui.IEditorInput)
	 */
	protected void doSetInput(IEditorInput input) throws CoreException {
		// The input has changed.
		modelChangeController.waitForCompleteTransaction(); // Don't want any transactions pending when we change input.
		super.doSetInput(input);

		setRootModel(null); // Clear it out so we don't see all of the changes.
		loadModel();
	}
	
	protected void loadModel() {
		// Loading the Model will reSet everyting; make sure all commit and flush
		// callers have been called.
		modelBuilder.commit() ;
//		Kick off the setup thread. Doing so that system stays responsive.
		Thread setup = new Thread(new Setup(), "Setup JavaVisualEditorPart"); //$NON-NLS-1$
		setup.setPriority(Thread.currentThread().getPriority() - 1); // Make it slightly slower so that ui thread still active
		setup.start();
	}

	/*
	 * Set a new model into the root editparts. This can happen because the setup and the initial creation
	 * of the viewers can be in a race condition with each other.
	 */
	protected void setRootModel(final BeanSubclassComposition root) {
		Runnable run = new Runnable() {
			public void run() {
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
			}
		};

		if (Display.getCurrent() != null)
			run.run(); // Already in display thread
		else
			Display.getDefault().asyncExec(run);
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
			site.getPage().showView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$	
		}
		if (store.getBoolean(VCEPreferences.OPEN_JAVABEANS_VIEW)) {
			site.getPage().showView("org.eclipse.ve.internal.java.codegen.editorpart.BeansList"); //$NON-NLS-1$
		}

		// Now restore focus to new editor since the above caused it to be lost.
		site.getPage().activate(site.getPage().findEditor(getEditorInput()));

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

	/*
	 * Create the editor as a split pane editor with the graphical editor on top and the text editor on the bottom.
	 */
	protected void createSplitpaneEditor(Composite parent, Preferences store) {
		// Split them all on the same parent.
		CustomSashForm jveParent = new CustomSashForm(parent, SWT.HORIZONTAL, CustomSashForm.NO_MAX_RIGHT);

		// Palette will be on the left.
		createPaletteViewer(jveParent);

		// JVE/Text editor split on the right under editorComposite
		CustomSashForm editorParent = new CustomSashForm(jveParent, SWT.VERTICAL);
		createPrimaryViewer(editorParent);

		// Let the super java text editor fill it in.			
		super.createPartControl(editorParent);

		jveParent.setSashBorders(new boolean[] { false, true });
		jveParent.setWeights(getPaletteSashWeights());

		editorParent.setSashBorders(new boolean[] { true, true });

		// Display the palette if the preferences state it should be initially shown
		boolean showPalette = store.getBoolean(VCEPreferences.SPLITPANE_SHOW_GEF_PALETTE);
		if (!showPalette)
			jveParent.maxLeft();
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

		// Create a 1:4 sash with the palette on the left and the JVE on the right
		CustomSashForm jveParent = new CustomSashForm(editorParent, SWT.HORIZONTAL, CustomSashForm.NO_MAX_RIGHT);
		createPaletteViewer(jveParent);
		createPrimaryViewer(jveParent);
		jveParent.setSashBorders(new boolean[] { false, true });
		jveParent.setWeights(getPaletteSashWeights());
		// Display the palette if the preferences state it should be initially shown
		boolean showPalette = store.getBoolean(VCEPreferences.NOTEBOOK_SHOW_GEF_PALETTE);
		if (!showPalette)
			jveParent.maxLeft();

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

	/*
	 * create the palette viewer.
	 */
	protected void createPaletteViewer(Composite parent) {
		PaletteViewer paletteViewer = new PaletteViewer();
		paletteViewer.createControl(parent);
		editDomain.setPaletteViewer(paletteViewer);
	}
	
	private List paletteCategories = new ArrayList(5);
	/*
	 * Rebuild the palette.
	 * NOTE: This must be run in the display thread.
	 */
	protected void rebuildPalette() {
		rebuildPalette = false;
		PaletteRoot paletteRoot = editDomain.getPaletteRoot();
		ResourceSet rset = EMFEditDomainHelper.getResourceSet(editDomain);
		
		// TODO This whole area needs to be rethinked for customization and for caching and other things.

		if (paletteRoot == null) {
			try {
				// Get the default base palette. (Basically only the control group).				
				Palette ref = (Palette) rset.getEObject(basePaletteRoot, true);
				paletteRoot = (PaletteRoot) ref.getEntry();
				editDomain.setPaletteRoot(paletteRoot);
				// Get the two standard tools. Since we only load this part of the palette once, it can never change.
				// TODO Need a better way of doing this that isn't so hardcoded.				
				List c = paletteRoot.getChildren();
				if (c.size() >= 1 && c.get(0) instanceof PaletteContainer) {
					PaletteContainer controlGroup = (PaletteContainer) c.get(0);
					c = controlGroup.getChildren();
					if (c.size() >= 3) {
						final PaletteToolEntryAction ptSel = (PaletteToolEntryAction) graphicalActionRegistry.getAction(JavaVisualEditorActionContributor.PALETTE_SELECTION_ACTION_ID);
						ptSel.setToolEntry((ToolEntry) c.get(0));
						ptSel.setId(JavaVisualEditorActionContributor.PALETTE_SELECTION_ACTION_ID);	// Because setToolEntry resets it
						ptSel.setChecked(true);	// Selection always initially checked. (i.e. selected).
						final PaletteToolEntryAction ptMarq = (PaletteToolEntryAction) graphicalActionRegistry.getAction(JavaVisualEditorActionContributor.PALETTE_MARQUEE_SELECTION_ACTION_ID);
						ptMarq.setToolEntry((ToolEntry) c.get(1));
						ptMarq.setChecked(false);
						ptMarq.setId(JavaVisualEditorActionContributor.PALETTE_MARQUEE_SELECTION_ACTION_ID);	// Because setToolEntry resets it
						final PaletteToolbarDropDownAction ptDropDown = (PaletteToolbarDropDownAction) graphicalActionRegistry.getAction(JavaVisualEditorActionContributor.PALETTE_DROPDOWN_ACTION_ID);
						ptDropDown.setToolEntry((ToolEntry) c.get(2));
						ptDropDown.setChecked(false);
						ptDropDown.setPaletteRoot(paletteRoot);
						ptDropDown.setId(JavaVisualEditorActionContributor.PALETTE_DROPDOWN_ACTION_ID);	// Because setToolEntry resets it						
						
						// Add palette viewer listener so that we can set the correct selection state and msg.
						final List lc = c; 
						editDomain.getPaletteViewer().addPaletteListener(new PaletteListener() {
							public void activeToolChanged(PaletteViewer palette, ToolEntry tool) {
								ptSel.setChecked(tool == lc.get(0));
								ptMarq.setChecked(tool == lc.get(1));
							
								String msg = ""; //$NON-NLS-1$
								if (tool.createTool() instanceof CreationTool ){
									msg = MessageFormat.format(CodegenEditorPartMessages.getString("JVEActionContributor.Status.Creating(label)"), new Object[]{tool.getLabel()}); //$NON-NLS-1$
								}
								getEditorSite().getActionBars().getStatusLineManager().setMessage(msg);									
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
			
			// Can't just plop new one in (GEF restriction). Need to remove
			// all of the drawers first. (This leaves the control group still there).
			PaletteEntry[] c = (PaletteEntry[]) paletteRoot.getChildren().toArray(new PaletteEntry[paletteRoot.getChildren().size()]);
			for (int i = 0; i < c.length; i++) {
				if (c[i] instanceof PaletteDrawer)
				paletteRoot.remove(c[i]);
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
				paletteRoot.add(drawer);
			}
		}
	}
	

	/*
	 * Create the primary viewer, which is the Graphical Viewer.
	 */
	protected void createPrimaryViewer(Composite parent) {
		if (VCEPreferences.isXMLTextOn()) {
			// If the Pref store wants the XML page put it on a sash
			SashForm sashform = new SashForm(parent, SWT.VERTICAL);
			createGraphicalViewer(sashform);
			createXMLTextViewerControl(sashform);
		} else {
			createGraphicalViewer(parent);
		}
	}

	protected void createGraphicalViewer(Composite parent) {
		primaryViewer = new ScrollingGraphicalViewer();

		Control gviewer = primaryViewer.createControl(parent);
		editDomain.addViewer(primaryViewer);
		editDomain.setViewerData(primaryViewer, ZoomController.ZOOM_KEY, new ZoomController());
		editDomain.setViewerData(primaryViewer, GridController.GRID_KEY, new GridController());
		editDomain.setViewerData(primaryViewer, DistributeController.DISTRIBUTE_KEY, new DistributeController(primaryViewer));
		primaryViewer.setEditPartFactory(new DefaultGraphicalEditPartFactory(ClassDescriptorDecoratorPolicy.getPolicy(editDomain)));

		primaryViewer.setRootEditPart(new ScalableFreeformRootEditPart());
		primaryViewer.setContents(new SubclassCompositionComponentsGraphicalEditPart(null));

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
	}

	/*
	 * Bit of a KLDUGE but during setup we may not have primary viewer yet, and during primary viewer creation
	 * we may not yet have Setup completed. So we need a common synchronized method that will set the diagram data
	 * when both are ready. Both places will call this method at thier appropriate time.
	 * 
	 * NOTE: Must be careful of no deadlocks, so quick in and out and no further locks.
	 */
	protected synchronized void initializeViewers() {
		if (primaryViewer != null && modelBuilder.getModelRoot()!=null) {
			Diagram d = modelBuilder.getDiagram();
			if (d != null) {
				editDomain.setViewerData(primaryViewer, EditDomain.DIAGRAM_KEY, d);
				setRootModel(modelBuilder.getModelRoot()); // Set into viewers.
			}
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
	static class DefaultVEContributor implements IVEContributor {
		/**
		 * Configuration element to use for contribution. Set this before usage. That way an instance of this
		 * can be reused.
		 */
		public IConfigurationElement paletteContribution;
		
		/* (non-Javadoc)
		 * @see org.eclipse.ve.internal.java.codegen.editorpart.IVEContributor#contributePalleteCats(java.util.List, org.eclipse.emf.ecore.resource.ResourceSet)
		 */
		public boolean contributePalleteCats(List currentCategories, ResourceSet rset) {
			String cat = paletteContribution.getAttributeAsIs(PI_CATEGORIES);
			if (cat == null || cat.length() == 0)
				return false;
			IPluginDescriptor plugin = null;
			if (cat.charAt(0) != '/')
				plugin = paletteContribution.getDeclaringExtension().getDeclaringPluginDescriptor();
			else {
				if (cat.length() > 4) {
					int pend = cat.indexOf('/', 1);
					if (pend == -1 || pend >= cat.length()-1)
						return false;	// invalid
					plugin = Platform.getPluginRegistry().getPluginDescriptor(cat.substring(1, pend));
					cat = cat.substring(pend+1);
				} else
					return false;	// invalid
			}
			URI catsURI = URI.createURI(plugin.getInstallURL().toString()+cat);
			Resource res = rset.getResource(catsURI, true);
			List cats = (List) EcoreUtil.getObjectsByType(res.getContents(), PalettePackage.eINSTANCE.getCategory());
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
	}
	/*
	 * This will create the proxy factory registry.
	 */
	protected void createProxyFactoryRegistry(IFile aFile) throws CoreException {
		if (isDisposed())
			return;	// No registry because we already closed.
		if (proxyFactoryRegistry != null && proxyFactoryRegistry.isValid()) {
			// Since we need to create a new one, we need to get rid of the old one.
			proxyFactoryRegistry.removeRegistryListener(registryListener); // We're going away, don't let the listener come into play.
			proxyFactoryRegistry.terminateRegistry();			
		}

		ConfigurationContributorAdapter jcmCont = new ConfigurationContributorAdapter() {
			IConfigurationContributionInfo info;
			
			/* (non-Javadoc)
			 * @see org.eclipse.jem.internal.proxy.core.ConfigurationContributorAdapter#initialize(org.eclipse.jem.internal.proxy.core.IConfigurationContributionInfo)
			 */
			public void initialize(IConfigurationContributionInfo info) {
				this.info = info;
			}
			
			/* (non-Javadoc)
			 * @see org.eclipse.jem.internal.proxy.core.ConfigurationContributorAdapter#contributeClasspaths(org.eclipse.jem.internal.proxy.core.IConfigurationContributionController)
			 */
			public void contributeClasspaths(IConfigurationContributionController controller) throws CoreException {
				// Add in the remote vm jar and any nls jars that is required for JBCF itself.
				controller.contributeClasspath(JavaVEPlugin.getPlugin().getDescriptor(), "vm/javaremotevm.jar", IConfigurationContributionController.APPEND_USER_CLASSPATH, true); //$NON-NLS-1$
			}
			
			/* (non-Javadoc)
			 * @see org.eclipse.jem.internal.proxy.core.ConfigurationContributorAdapter#contributeToRegistry(org.eclipse.jem.internal.proxy.core.ProxyFactoryRegistry)
			 */
			public void contributeToRegistry(ProxyFactoryRegistry registry) {
				// Call the setup method in the target VM to initialize statics
				// and other environment variables.
				IBeanTypeProxy aSetupBeanTypeProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.java.remotevm.Setup"); //$NON-NLS-1$
				IMethodProxy setupMethodProxy = aSetupBeanTypeProxy.getMethodProxy("setup"); //$NON-NLS-1$
				setupMethodProxy.invokeCatchThrowableExceptions(aSetupBeanTypeProxy);
				
				// Everything is all set up. Now let's see if we need to rebuild the palette.
				// First run though all visible containers that implement the IVEContributor interface.
				final ResourceSet rset = EMFEditDomainHelper.getResourceSet(editDomain);				
				for (Iterator iter = info.getContainers().entrySet().iterator(); iter.hasNext();) {
					final Map.Entry entry = (Map.Entry) iter.next();
					if (((Boolean) entry.getValue()).booleanValue() && entry.getKey() instanceof IVEContributor) {
						Platform.run(new ISafeRunnable() {
							public void handleException(Throwable exception) {
								// Default logs fro us.
							}
							public void run() throws Exception {
								rebuildPalette = ((IVEContributor) entry.getKey()).contributePalleteCats(paletteCategories, rset) || rebuildPalette;
							}
						});
					}
				}
				
				// Now run though visible containers ids.
				final DefaultVEContributor[] defaultVE = new DefaultVEContributor[1];
				if (!info.getContainerIds().isEmpty()) {
					for (Iterator iter = info.getContainerIds().entrySet().iterator(); iter.hasNext();) {
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
											}
										}
									});
								}
							}
						}
					}
				}
				
				// Now run though visible plugin ids.
				if (!info.getPluginIds().isEmpty()) {
					for (Iterator iter = info.getPluginIds().entrySet().iterator(); iter.hasNext();) {
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
				
			}
		};
		
		IConfigurationContributor[] contribs =
			new IConfigurationContributor[] { BeaninfoNature.getRuntime(aFile.getProject()).getConfigurationContributor(), jcmCont };

		ProxyFactoryRegistry registry = ProxyLaunchSupport.startImplementation(aFile.getProject(), "VM for " + aFile.getName(), //$NON-NLS-1$
				contribs, new NullProgressMonitor());
		registry.getBeanTypeProxyFactory().setMaintainNotFoundTypes(true);	// Want to maintain list of not found types so we know when those types have been added.
		
		synchronized (this) {
			if (isDisposed()) {
				// Editor closed while we were opening it. So close the registry.
				registry.terminateRegistry(); 
				return;
			}
			proxyFactoryRegistry = registry;
			proxyFactoryRegistry.addRegistryListener(registryListener);			
		}
		return;
	}
	private ProxyFactoryRegistry.IRegistryListener registryListener = new ProxyFactoryRegistry.IRegistryListener() {
		/**
		 * @see org.eclipse.jem.internal.proxy.core.ProxyFactoryRegistry.IRegistryListener#registryTerminated(ProxyFactoryRegistry)
		 */
		public void registryTerminated(ProxyFactoryRegistry registry) {
			final EObject root = modelBuilder.getModelRoot();
			// Need to deactivate all of the editparts because they don't work without a vm available.
			Display.getDefault().asyncExec(new Runnable() {
				/**
				 * @see java.lang.Runnable#run()
				 */
				public void run() {
					setRootModel(null);
					root.eNotify(new ENotificationImpl((InternalEObject) root, CompositionProxyAdapter.RELEASE_PROXIES, null, null, null, false));
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

	private void restartVM() {
		restartVMNeeded = false;
		Runnable run = new Runnable() {
			/**
			 * @see java.lang.Runnable#run()
			 */
			public void run() {
				try {					
					IFile file = ((IFileEditorInput) getEditorInput()).getFile();
					createProxyFactoryRegistry(file);
					if (proxyFactoryRegistry != null) {
						beanProxyAdapterFactory.setProxyFactoryRegistry(proxyFactoryRegistry);
						loadModel();	// Now reload the model.
					}
					
					if (rebuildPalette)
						rebuildPalette();
				} catch (CoreException e) {
				}
			}
		};
		
		if (Display.getCurrent() != null)
			run.run();
		else
			Display.getDefault().asyncExec(run);
	}

	protected SelectionSynchronizer getSelectionSynchronizer() {
		if (selectionSynchronizer == null)
			selectionSynchronizer = new SelectionSynchronizer();
		return selectionSynchronizer;
	}

	public void dispose() {
		try {
			
			// Remove the proxy registry first so that we aren't trying to listen to any changes and sending them through since it isn't necessary.
			if (proxyFactoryRegistry != null) {
				proxyFactoryRegistry.removeRegistryListener(registryListener); // We're going away, don't let the listener come into play.
				proxyFactoryRegistry.terminateRegistry();
			}
			
			modelBuilder.dispose();
			
			if (statusController != null)
			    statusController.dispose();
			

			if (modelSynchronizer != null) {
				modelSynchronizer.stopSynchronizer();
			}
			
			EditDomain ed = editDomain;
			synchronized (this) {
				editDomain = null;	// This indicates we are disposed!
			}

			getSite().getPage().removeSelectionListener(selectionServiceListener);
			
			IWorkbenchWindow window = getSite().getWorkbenchWindow();
			
			window.getPartService().removePartListener(activationListener);
			Shell shell = window.getShell();
			if (shell != null && !shell.isDisposed())
				window.getShell().removeShellListener(activationListener);
			
			ed.dispose();
			
			graphicalActionRegistry.dispose();
			commonActionRegistry.dispose();
		} catch (Exception e) {
		}
		super.dispose();
	}
	
	protected synchronized boolean isDisposed() {
		return editDomain == null;
	}

	/*
	 * Answer the stored palette ratios.
	 * 
	 * TODO currently only the default is stored. Don't have a mechanism to save the current setting because
	 * no save per editor available.
	 */
	protected int[] getPaletteSashWeights() {
		// The weights are stored as preferences so as the user changes it new editors open with the 
		// customized size
		int leftRatio = VCEPreferences.getPlugin().getPluginPreferences().getInt(VCEPreferences.PALETTE_SASH_LEFT_RATIO);
		int rightRatio = VCEPreferences.getPlugin().getPluginPreferences().getInt(VCEPreferences.PALETTE_SASH_RIGHT_RATIO);
		return new int[] { leftRatio, rightRatio };
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#updateStatusField(java.lang.String)
	 */
	protected void updateStatusField(String category) {
		if (category.equals(IJVEStatus.STATUS_CATEGORY_SYNC_ACTION))
			statusController.updateSyncAction();
		else if (category.equals(IJVEStatus.STATUS_CATEGORY_SYNC_STATUS))
			statusController.updateSyncStatus();
		else
			super.updateStatusField(category);
	}
	
	protected IDescriptorPropertySheetEntry rootPropertySheetEntry, currentPropertySheetEntry;
		
	protected IPropertySheetPage getPropertySheetPage() {
		if (propertySheetPage == null)
			createPropertySheetPage();
		return propertySheetPage;
	}
	
	protected void createPropertySheetPage() {
		propertySheetPage = new EToolsPropertySheetPage() {
			public void setActionBars(IActionBars actionBars) {
				super.setActionBars(actionBars);
				// The menu and toolbars have RetargetActions for UNDO and REDO
				// Set an action handler to redirect these to the action registry's actions so they work when the property sheet is enabled
				actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), getAction(ActionFactory.UNDO.getId()));
				actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), getAction(ActionFactory.REDO.getId()));
			}
		};

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
							if (parent != null) {
								EStructuralFeature sf = (EStructuralFeature) id;
								modelChangeController.flushCodeGen(new EditorSelectionSynchronizer((IJavaInstance) parent, sf), MARKER_PropertyChange); //$NON-NLS-1$
							}
						}
					}
				}		
			};
		}
		return propertySheetEntryListener;
	}
	
	/*
	 * This class is used to cause the appropriate section of code
	 * in the text editor to be selected. It is called by code generation
	 * when it reaches the code finished being written to the working copy.
	 * 
	 * This class is instantiated whenever a bean is selected in the graphical/tree viewer
	 * or a property has been selected. It causes a flush of the codegen cycle and then
	 * when flush is completed this class is called to drive the text selection.
	 */
	private class EditorSelectionSynchronizer implements ISynchronizerListener {
		Notifier fNotifier;
		EStructuralFeature fSF = null ;
		EditorSelectionSynchronizer(Notifier aJavaBean) {
			fNotifier = aJavaBean;
		}
		EditorSelectionSynchronizer(IJavaInstance aParentJavaBean, EStructuralFeature sf) {
			this(aParentJavaBean) ;
			fSF = sf ;
		}
		
		public void markerProcessed(String marker) {
			ICodeGenAdapter codeGenAdapter = null ;
			if (MARKER_JVESelection.equals(marker) || MARKER_PropertySelection.equals(marker) || MARKER_PropertyChange.equals(marker))   
			   codeGenAdapter = (ICodeGenAdapter) EcoreUtil.getExistingAdapter(fNotifier,ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);
			if (codeGenAdapter == null)
			   codeGenAdapter = (ICodeGenAdapter) EcoreUtil.getExistingAdapter(fNotifier,ICodeGenAdapter.JVE_CODEGEN_EXPRESSION_SOURCE_RANGE);
			if (codeGenAdapter == null)
			   codeGenAdapter = (ICodeGenAdapter) EcoreUtil.getExistingAdapter(fNotifier,ICodeGenAdapter.JVE_CODE_GEN_TYPE);
			   
			if ((MARKER_PropertySelection.equals(marker) || MARKER_PropertyChange.equals(marker)) 
			    && codeGenAdapter != null && fSF!=null && (codeGenAdapter instanceof BeanDecoderAdapter)) {
				// We are driving a property, use it's parent/sf
				ICodeGenAdapter[] list =  ((BeanDecoderAdapter)codeGenAdapter).getSettingAdapters(fSF) ;
				if (list != null && list.length>0)
				   codeGenAdapter = list[0] ;
			}
			if (codeGenAdapter == null) {
				JavaVEPlugin.log("JavaVisualEditorPart.markerProcessed(): No CodeGen Adapter on: " + fNotifier, //$NON-NLS-1$
				Level.FINE);
			} else {
				try {
					final ICodeGenSourceRange sourceRange = codeGenAdapter.getHighlightSourceRange();
					// Drive the selection in the java editor
					// This must be done on the Display thread so that SWT works correctly
					// Only do this if the source editor is not in focus, because if so then the user
					// may be typing in it and if we drive the cursor away from them they won't like it
					if (!textEditorFocus && sourceRange != null) {
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								try {
									setHighlightRange(sourceRange.getOffset(), sourceRange.getLength(), true);
								} catch (Exception exc) {
									exc.printStackTrace();
									// Do nothing - We get assertion failures that I don't fully understand, especially when
									// dropping onto the JavaBeans viewer
								}
							}
						});
					}
				} catch (CodeGenException exc) {
					JavaVEPlugin.log(exc);
				}
			}
		}
	}

	// TODO Remove this as soon as possible. Need a better model controller. Need to work with Gili on this.
	protected JavaModelChangeController modelChangeController = new JavaModelChangeController();
	private class JavaModelChangeController implements IModelChangeController, ICodeGenFlushController {
		// The undomanager doesn't handle nesting of compound changes, so we need to do it here.
		private int compoundChangeCount = 0;
		private boolean inTransaction = false;
		private int isRunning = 0; // Note that a Runable is outstanding
		private boolean allowingChanges = false;
		private boolean fHoldChanges = false;
		private String fHoldMsg = null;
		private ArrayList fCodeGenFlushReq = new ArrayList();

		public synchronized boolean inTransaction() {
			return inTransaction;
		}
		public synchronized void setHoldChanges(boolean flag, String msg) {
			fHoldChanges = flag;
			fHoldMsg = msg;
			while (flag && inTransaction && isRunning > 0) {
				try {
					this.wait();
				} catch (InterruptedException e) {
				}
			}
		}

		public synchronized boolean isHoldChanges() {
			return fHoldChanges;
		}

		public void waitForCompleteTransaction() {
			modelBuilder.commit();
			// Make sure there are no changes outstanding. Will wait until they are committed. Will immediately wake up committer.
		}

		// TODO This is a hack, we should have an Editor Error manager in the future to handle
		// errors, their context, and the way we denote them to the user.
		private void showMsg(String msg) {
			IStatusLineManager statusLine = getEditorSite().getActionBars().getStatusLineManager();
			if (msg != null)
				statusLine.setMessage(IErrorHolder.ErrorType.getSevereErrorImage(), msg);
			else
				statusLine.setMessage(null, null);
		}

		public boolean run(Runnable runnable, boolean updatePS) {
			boolean localAllowChanges = false;
			boolean localHoldChanges; // Hold instance may be changing in the middle
			boolean localRunit;
			String localHoldMsg = null;
			synchronized (this) {
				// No point to query the JavaEditor if we are on hold
				isRunning++;
				localHoldChanges = fHoldChanges;
				localHoldMsg = fHoldMsg;
			}

			try {
				localAllowChanges = startChange();
				localRunit = !localHoldChanges && localAllowChanges;

				if (localRunit) {
					runnable.run();
					if (updatePS && rootPropertySheetEntry != null)
						rootPropertySheetEntry.refreshFromRoot();
				} else if (localHoldChanges)
					showMsg(localHoldMsg);
				else
					showMsg(CodegenEditorPartMessages.getString("EditorPart.Msg.FileReadOnly_ERROR_")); //$NON-NLS-1$
			} finally {
				if (localAllowChanges)
					stopChange();
				synchronized (this) {
					isRunning--;
					if (isRunning <= 0) {
						this.notifyAll();
						isRunning = 0;
					}
				}
			}

			return localRunit;
		}

		// There is still a small window for a race condition, but it can't be addressed for beta. Needs better
		// support within codegen to transactialize it.
		//
		// If startChange returns true, then stopChange MUST be called, otherwise counters will get messed up.
		//
		// NOTE: This thread must NOT already be sync on "this" or else it won't work.
		private boolean startChange() {
			// starts changes and returns whether changes allowed.
			boolean doit = false;
			final IRewriteTarget rewriteTarget = (IRewriteTarget) getAdapter(IRewriteTarget.class);
			JavaVEPlugin.log("(+1) Starting change", Level.FINEST); //$NON-NLS-1$
			synchronized (this) {
				if (compoundChangeCount++ == 0) {
					if (rewriteTarget != null) {
						// We only check for allow changes once, when we start a nested change group.
						// While nested we don't allow changes depending upon the initial test.
						try { // Must be called on the Display thread 
							// It is important that we clear the compoundChangCount if there are any fooFoo.			
							allowingChanges = validateEditorInputState();
							;
						} catch (Throwable t) {
							JavaVEPlugin.log(t, Level.WARNING);
						}
						if (allowingChanges)
							doit = true;
						else
							compoundChangeCount = 0; // Go back to no transaction.
					} else
						allowingChanges = true; // Editor not fully up yet, so not a transaction that can be undone. Just go do it.		
				} else
					JavaVEPlugin.log("(+2) Starting nested transaction", Level.FINEST); //$NON-NLS-1$
			}

			if (doit) {
				JavaVEPlugin.log("(+1a) Starting wait for completion", Level.FINEST); //$NON-NLS-1$
				// Needed to have commitAndFlush outside of sync block because the commitAndFlushAsync below may also try to access "this", but from a different thread.
				// Can't have any pending changes before beginning next group of changes.				
				waitForCompleteTransaction();
				JavaVEPlugin.log("(-1a) Ending wait for completion", Level.FINEST); //$NON-NLS-1$
				JavaVEPlugin.log("(+1b) Starting begin compound change", Level.FINEST); //$NON-NLS-1$
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						// Stopping the reDraw causes the SWT (Editor) Text Widget to be
						// Out of sync with the JDT model and the AnnotatorPainter that tries
						// to maintain the errors on this widget
//						rewriteTarget.setRedraw(false); // So we don't get so much flicker.							
						JavaVEPlugin.getPlugin().getLogger().log("Redraw off", Level.FINER); //$NON-NLS-1$
						rewriteTarget.beginCompoundChange();
					}
				}); // setRedraw needs to be in a UI thread.
				JavaVEPlugin.log("(-1b) Ending begin compound change", Level.FINEST); //$NON-NLS-1$
				synchronized (this) {
					inTransaction = true;
				}
				JavaVEPlugin.getPlugin().getLogger().log("(+3) Transaction started", Level.FINEST); //$NON-NLS-1$
			}
			JavaVEPlugin.log("(-1) Ending starting change", Level.FINEST); //$NON-NLS-1$
			return allowingChanges;
		}

		private synchronized void stopChange() {
			// stops changes
			if (--compoundChangeCount <= 0) {
				JavaVEPlugin.log("(3a) Starting end transaction", Level.FINEST); //$NON-NLS-1$
				compoundChangeCount = 0; // In case we get out of sync.
				if (inTransaction) {
					// We have done a beginCompoundChange, must now do endCompoundChange
					boolean localAllow = allowingChanges;
					allowingChanges = false;
					if (localAllow) {
						// When the CDE has finished executing the command we need to flush and commit
						// This tells the code generation to complete updating the source.					
						modelBuilder.commitAndFlush(new ISynchronizerListener() {
							public void markerProcessed(String marker) {
								final IRewriteTarget rewriteTarget = (IRewriteTarget) getAdapter(IRewriteTarget.class);
								boolean doEnd = true;
								JavaVEPlugin.log("(+3b) Starting async commit processing", Level.FINEST); //$NON-NLS-1$
								synchronized (JavaModelChangeController.this) {
									if (compoundChangeCount <= 0) {
										inTransaction = false; // We haven't started a new one while processing the termination.
										JavaModelChangeController.this.notifyAll();
									} else {
										doEnd = false;
										// We've started a new transaction since this end request went through, so treat it as nested.
										JavaVEPlugin.log("(3b) Another transaction has started, treat it as nested for now.", Level.FINEST); //$NON-NLS-1$
									}
								}
								processCodeGenFlushIfNeeded();
								final boolean finalDoEnd = doEnd;
								Display.getDefault().syncExec(new Runnable() {
									public void run() {
										if (finalDoEnd) {
											rewriteTarget.endCompoundChange();
											JavaVEPlugin.log("(-3) End transaction", Level.FINEST); //$NON-NLS-1$
										}
//										rewriteTarget.setRedraw(true);
										JavaVEPlugin.getPlugin().getLogger().log("Redraw on", Level.FINER); //$NON-NLS-1$
									}
								}); // setRedraw needs to be in a UI thread.
								JavaVEPlugin.log("(-3b) End async commit processing", Level.FINEST); //$NON-NLS-1$
							}
						}, "modelChangeMarker"); //$NON-NLS-1$
					}
				}
			} else
				JavaVEPlugin.log("(-2) Ending nested transaction", Level.FINEST); //$NON-NLS-1$
		}

		/**
		 * @see org.eclipse.ve.internal.cde.core.IModelChangeController#getHoldMsg()
		 */
		public String getHoldMsg() {
			return fHoldMsg;
		}

		/**
		 * Called from stopChage();
		 * 
		 * Assuming that this is called after we finished a transaction
		 * (where a commit/Flush was already called)
		 */
		private void processCodeGenFlushIfNeeded() {

			ArrayList localList;
			synchronized (this) {
				localList = fCodeGenFlushReq;
				fCodeGenFlushReq = new ArrayList();
			}

			Iterator itr = localList.iterator();
			while (itr.hasNext()) {
				ISynchronizerListener l = (ISynchronizerListener) itr.next();
				String m = (String) itr.next();
				// No need to call commitAndFlush - already did, and is too expensive
				// at this point.
				try {
					l.markerProcessed(m);
				} catch (Throwable t) {
					JavaVEPlugin.log(t);
				}

			}
		}
		/**
		 * This method is to be used to ask for CodeGen to flush generated code to the JavaEditor.
		 *  
		 * If we are in the middle of a transaction, queue the flush request to CodeGen.
		 */
		public synchronized void flushCodeGen(ISynchronizerListener listener, String marker) {
			if (inTransaction) {
				fCodeGenFlushReq.add(listener);
				fCodeGenFlushReq.add(marker);
			} else if (!isHoldChanges())
				modelBuilder.commitAndFlush(listener, marker);
			else
				try {
					listener.markerProcessed(marker);
				} catch (Throwable t) {
					JavaVEPlugin.log(t);
				}
		}

	};

	/*
	 * Listener to listen for code status changes.
	 * Update the graph viewer with a "Loading..." message when
	 * reload from scratch is in progress.
	 */
	protected static final Insets INSETS = new Insets(10, 25, 10, 25);
	private class LoadingFigureController implements IJVEStatusChangeListener {

		protected GraphicalViewer viewer;
		protected Label loadingFigure;
		protected boolean showingLoadingFigure = true;
		
		public IFigure getRootFigure(IFigure target) {
			IFigure parent = target.getParent();
			while (parent.getParent() != null)
				parent = parent.getParent();
			return parent;
		}		

		public LoadingFigureController() {
		}

		/**
		 * Call when we have a viewer to actually work with.
		 * At this point in time we can now display the current loading status.
		 * 
		 * This allows us to start listening before we have a viewer.
		 * This should only be called once.
		 */
		public void startListener(GraphicalViewer viewer) {
			this.viewer = viewer;
				loadingFigure = new Label(CodegenMessages.getString("CodeGenVisualGraphicalEditorPart.StatusChangeListener.loading")) {//$NON-NLS-1$
	Locator locator = new Locator() {
					public void relocate(IFigure target) {
						// Center the figure in the middle of the canvas
						Dimension canvasSize = getRootFigure(target).getSize();					
						Dimension prefSize = target.getPreferredSize();
						int newX = (canvasSize.width - prefSize.width) / 2;
						int newY = (canvasSize.height - prefSize.height) / 2;
						Rectangle b = new Rectangle(newX, newY, prefSize.width, prefSize.height);
						target.translateToRelative(b);
						target.setBounds(b);
					}
				};
				
				public void validate() {
					if (!isValid())
						locator.relocate(this);
					super.validate();
				}
			};
			loadingFigure.setEnabled(true);
			loadingFigure.setOpaque(true);
			loadingFigure.setBorder(new AbstractBorder() {
				public Insets getInsets(IFigure figure) {
					return INSETS;
				}

				public void paint(IFigure figure, Graphics graphics, Insets insets) {			
					graphics.setLineWidth(1);
					graphics.setLineStyle(Graphics.LINE_SOLID);
					graphics.setXORMode(false);
					Rectangle rect = getPaintRectangle(figure, insets);
					// Draw a Black border out the outside to distinquish between the label and surroundings,
					graphics.setForegroundColor(ColorConstants.black);
					rect.resize(-1, -1);
					graphics.drawRectangle(rect);					
					// Draw a white border just inside so that we have a white box around it.
					graphics.setForegroundColor(ColorConstants.white);					
					rect.translate(1, 1);
					rect.resize(-2, -2);
					graphics.drawRectangle(rect);
				}
			});
			if (showingLoadingFigure)
				showLoadingFigure();
		}

		public void statusChanged(int oldStatus, int newStatus) {
			if ((newStatus & (ICodeGenStatus.JVE_CODEGEN_STATUS_RELOAD_IN_PROGRESS | ICodeGenStatus.JVE_CODEGEN_STATUS_RELOAD_PENDING))
				> 0) {
				if (!showingLoadingFigure) {
					showLoadingFigure();
				}
			} else if (showingLoadingFigure)
				removeLoadingFigure();
		}

		protected Layer getLoadingLayer() {
			return (Layer) ((LayerManager) viewer.getEditPartRegistry().get(LayerManager.ID)).getLayer(LayerConstants.HANDLE_LAYER);
		}

		private FigureListener rootFigureListener = new FigureListener() {
			public void figureMoved(IFigure source) {
				loadingFigure.revalidate();
			}
		};

		private PropertyChangeListener scrolledListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (RangeModel.PROPERTY_VALUE.equals(evt.getPropertyName()))
					loadingFigure.revalidate();	// Scrollbar has moved, so revalidate.
			}

		};
		
		protected void removeLoadingFigure() {
			showingLoadingFigure = false;
			if (viewer != null) {
				Layer layer = getLoadingLayer();
				if (layer.getChildren().contains(loadingFigure)) {
					layer.remove(loadingFigure);
				}
				Viewport vp = getViewport(layer);
				if (vp != null) {
					vp.getHorizontalRangeModel().removePropertyChangeListener(scrolledListener);
					vp.getVerticalRangeModel().removePropertyChangeListener(scrolledListener);
				}
				getRootFigure(layer).removeFigureListener(rootFigureListener);
			}
		}
		
		protected Viewport getViewport(IFigure figure) {
			IFigure f = figure;
			while (f != null && !(f instanceof Viewport))
				f = f.getParent();
			return (Viewport) f;
		}

		protected void showLoadingFigure() {
			showingLoadingFigure = true;
			if (viewer != null) {
				Layer layer = getLoadingLayer();
				layer.add(loadingFigure);
				Viewport vp = getViewport(layer);
				if (vp != null) {
					vp.getHorizontalRangeModel().addPropertyChangeListener(scrolledListener);
					vp.getVerticalRangeModel().addPropertyChangeListener(scrolledListener);
				}				
				getRootFigure(layer).addFigureListener(rootFigureListener);
				loadingFigure.revalidate();
			}
		}
	}

	/*
	 * This class is responsible to keep the status bar in sync. with the current editor.
	 * It is also a notifier of status changes.
	 */
	private class StatusRenderer implements IJVEStatus, MouseListener {

		int fState = JVE_CODEGEN_STATUS_OUTOFSYNC;
		int fPendingCounter = 0;
		int fPauseProcessing = 0;
		ListenerList listeners = new ListenerList(1);

		/**
		* @see IJVEStatus#showMsg(String, int)
		*/
		public void showMsg(final String msg, int kind) {
			final IStatusLineManager statusLine = getEditorSite().getActionBars().getStatusLineManager();
			if (statusLine == null)
				return;
			final Image image;
			if (kind == ERROR_MSG)
				image = IBeanProxyHost.ErrorType.getSevereErrorImage();
			else if (kind == WARNING_MSG)
				image = IBeanProxyHost.ErrorType.getWarningErrorImage();
			else
				image = null;

			if (msg != null)
				JavaVEPlugin.log(msg, Level.FINE);

			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					statusLine.setMessage(image, msg);
				}
			});

		}

		/**
		 * @see org.eclipse.ve.internal.java.core.codegen.ICodeGenStatus#getStatus()
		 */
		public int getState() {
			return fState;
		}

		public void refreshStatus(final int oldState) {
			if (Display.getCurrent() != null)
				primRefreshStatus(oldState);
			else
				Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					primRefreshStatus(oldState);
				}
			});
		}

		private void primRefreshStatus(int oldState) {

			updateSyncStatus();
			updateSyncAction();

			Object[] lsts = listeners.getListeners();
			for (int i = 0; i < lsts.length; i++) {
				IJVEStatusChangeListener listener = (IJVEStatusChangeListener) lsts[i];
				try {
					listener.statusChanged(oldState, getState());
				} catch (Throwable t) {
					JavaVEPlugin.log(t);
				}
			}
		}

		public void updateSyncAction() {
			IJVEActionField actionField = (IJVEActionField) getStatusField(IJVEStatus.STATUS_CATEGORY_SYNC_ACTION);
			if (actionField == null)
				return;

			actionField.setPauseListener(this);

			// Update Arrows color
			if (isStatusSet(JVE_CODEGEN_STATUS_UPDATING_SOURCE)) {
				actionField.setModel2Src(true);
			} else {
				actionField.setModel2Src(false);
			}

			if (isStatusSet(JVE_CODEGEN_STATUS_UPDATING_JVE_MODEL)) {
				actionField.setSrc2Model(true);

			} else {
				actionField.setSrc2Model(false);
			}
			// Update Buttom's play/pause
			if (isStatusSet(JVE_CODEGEN_STATUS_PARSE_ERRROR))
				actionField.setError(true);
			else {
				actionField.setError(false) ;
				if (isStatusSet(JVE_CODEGEN_STATUS_PAUSE)) {
					actionField.setPause(true);
				} else {
					actionField.setPause(false);
				}
			}

		}
		
		public void dispose() {
			IJVEActionField actionField = (IJVEActionField) getStatusField(IJVEStatus.STATUS_CATEGORY_SYNC_ACTION);
			if (actionField != null) {			
				  actionField.unsetPauseListener(this) ;			
			}
		}

		public void updateSyncStatus() {
			IStatusField textField = getStatusField(IJVEStatus.STATUS_CATEGORY_SYNC_STATUS);
			if (textField == null)
				return;

			String msg = null;
			if (isStatusSet(JVE_CODEGEN_STATUS_OUTOFSYNC))
				msg = IJVEStatus.STATUS_MSG_SYNC_STATUS_OUTOFSYNC;
			else
				msg = IJVEStatus.STATUS_MSG_SYNC_STATUS_INSYNC;

			if (isStatusSet(JVE_CODEGEN_STATUS_RELOAD_IN_PROGRESS) || isStatusSet(JVE_CODEGEN_STATUS_RELOAD_PENDING))
				msg = IJVEStatus.STATUS_MSG_SYNC_STATUS_RELOAD;
			else if (isStatusSet(JVE_CODEGEN_STATUS_SYNCHING))
				msg = IJVEStatus.STATUS_MSG_SYNC_STATUS_SYNCING;
			else if (isStatusSet(JVE_CODEGEN_STATUS_PAUSE))
				msg = IJVEStatus.STATUS_MSG_SYNC_STATUS_PAUSE;

			if (isStatusSet(JVE_CODEGEN_STATUS_PARSE_ERRROR))
				msg = IJVEStatus.STATUS_MSG_SYNC_STATUS_PARSE_ERROR;

			textField.setText(msg);
		}

		/**
		 * @see org.eclipse.ve.internal.java.core.codegen.ICodeGenStatus#isStatusSet(int)
		 */
		public synchronized boolean isStatusSet(int state) {
			return ((getState() & state) == state);
		}

		/**
		 * @see org.eclipse.ve.internal.java.core.codegen.ICodeGenStatus#setStatus(int, boolean)
		 */
		public synchronized void setStatus(int flag, boolean state) {
			int oldState = getState();
			if (state)
				fState |= flag;
			else
				fState &= (~flag);
			if (oldState != getState())
				refreshStatus(oldState);
		}

		/**
		 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(MouseEvent)
		 */
		public void mouseDoubleClick(MouseEvent e) {
		}
		public void mouseDown(MouseEvent e) {
		}
		public void mouseUp(MouseEvent e) {
			if (fPauseProcessing > 0)
				return;

			if (isStatusSet(JVE_CODEGEN_STATUS_PARSE_ERRROR))
				return;

			boolean flag = !isStatusSet(JVE_CODEGEN_STATUS_PAUSE);

			try {
				fPauseProcessing++;
				modelBuilder.pauseRoundTripping(flag);
			} catch (Throwable t) {
				JavaVEPlugin.log(t);
			} finally {
				fPauseProcessing--;
				if (fPauseProcessing < 0)
					fPauseProcessing = 0;
			}
		}

		/**
		 * @see org.eclipse.ve.internal.java.core.codegen.ICodeGenStatus#setReloadPending(boolean)
		 */
		public synchronized boolean setReloadPending(boolean flag) {
			if (flag)
				fPendingCounter++;
			else if (fPendingCounter > 0)
				fPendingCounter--;
			if (fPendingCounter > 0) {
				setStatus(JVE_CODEGEN_STATUS_RELOAD_PENDING, true);
				return true;
			} else {
				setStatus(JVE_CODEGEN_STATUS_RELOAD_PENDING, false);
				return false;
			}
		}

		/**
		 * @see org.eclipse.ve.internal.java.codegen.editorpart.IJVEStatus#addStatusListener(IJVEStatusChangeListener)
		 */
		public synchronized void addStatusListener(IJVEStatusChangeListener sl) {
			listeners.add(sl);
		}

		/**
		 * @see org.eclipse.ve.internal.java.codegen.editorpart.IJVEStatus#removeStatusListener(IJVEStatusChangeListener)
		 */
		public synchronized void removeStatusListener(IJVEStatusChangeListener sl) {
			listeners.remove(sl);
		}

	}

	/*
	 * Runnable to do the setup for a new input file (and initial initialization if needed) in a
	 * separate thread. Loaded from doSetup.
	 */
	private static final Integer NATURE_KEY = new Integer(0);	// Used within Setup to save the nature	 
	private class Setup implements Runnable {

		public void run() {
// final boolean doTimer = !initialized;	// TODO Remove all comments about timer step.			
			try {				
				if (!initialized)
					initialize();
				else {
					// Check to see if nature is still valid. If it is not, we need to reinitialize for it. OR it 
					// could be we were moved to another project entirely.
					BeaninfoNature nature = (BeaninfoNature) editDomain.getData(NATURE_KEY);
					IFile file = ((IFileEditorInput) getEditorInput()).getFile();					
					if (nature == null || !nature.isValidNature() || !file.getProject().equals(nature.getProject()))
						initializeForProject(file);
				}
				if (isDisposed())
					return;
				// TODO Rich, we need some type of call back - this will hand hold Sri in the meantime
				while (modelChangeController.inTransaction()) {
					modelBuilder.commit() ;
				}
				// Make sure all callbacks have been called.
				modelBuilder.commit() ;

				// Now do rest of setup, which is the same whether first time or not.								
				statusController.setStatus(ICodeGenStatus.JVE_CODEGEN_STATUS_RELOAD_IN_PROGRESS, true);
//				if (doTimer)
//					TimerStep.instance().writeCounters2(50);			
				statusController.setReloadPending(true);
				try {
					
						// TODO move back off thread
						// Currently there are other reloads from Gili's side that can occur at the same time,
						// but happen on the GUI thread. So push this off to GUI too. Don't like it because it
						// locks up the GUI, but can't help. No safe way to handle the interaction.
						// Next release we need to get ALL source -> jve model interactions queued onto another thread
						// including reloads. But it must all be the same thread so that we don't get race conditions.
						// Needs to be carefully thought out because the jve model can also be updated from any where.						
						final Exception[] ex = new Exception[1];
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								try {
									statusController.setReloadPending(false);		
									modelBuilder.loadModel((IFileEditorInput) getEditorInput(), null);
//									if (doTimer) {
//										TimerStep.instance().writeCounters2(51);	//TODO Remove doTimer variable too when getting rid of TimerStep.			
//										TimerStep.instance().writeCounters2(101);	//TODO Remove doTimer variable too when getting rid of TimerStep.			
//									}
								} catch (CodeGenException e) {
									ex[0] = e;
								}
							}
						});
						if (ex[0] != null)
							throw ex[0];

						DiagramData dd = modelBuilder.getModelRoot();
						editDomain.setDiagramData(dd);
						InverseMaintenanceAdapter ia = new InverseMaintenanceAdapter() {
							/**
							 * @see org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter#subShouldPropagate(EReference, Object)
							 */
							protected boolean subShouldPropagate(
								EReference ref,
								Object newValue) {
								return !(newValue instanceof Diagram);
								// On the base BeanComposition, don't propagate into Diagram references.
							}
						};
						dd.eAdapters().add(ia);
						ia.propagate();

						if (dd != null
							&& EcoreUtil.getExistingAdapter(
								dd,
								CompositionProxyAdapter.BEAN_COMPOSITION_PROXY)
								== null) {
							CompositionProxyAdapter a =
								new CompositionProxyAdapter();
							dd.eAdapters().add(a);
							a.initBeanProxy();
						}
										

					initializeViewers();
				} finally {					
					statusController.setStatus(ICodeGenStatus.JVE_CODEGEN_STATUS_RELOAD_IN_PROGRESS, false);
				}
			} catch (final Exception x) {
				// If we are disposed, then it doesn't matter what the error is. This can occur because we closed
				// while loading and there is no way to stop this thread when that occurs. We don't want main thread
				// to just wait until this thread finishes when closing because that holds everything up when we don't
				// really care. This way we let it throw an exception because something was in a bad state and just
				// don't put up a message.
				if (!isDisposed()) {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							String title = CodegenEditorPartMessages.getString("JavaVisualEditor.ErrorTitle"); //$NON-NLS-1$
							String msg = CodegenEditorPartMessages.getString("JavaVisualEditor.ErrorDesc"); //$NON-NLS-1$
							Shell shell = getSite().getShell();
							if (x instanceof CoreException)
								ErrorDialog.openError(shell, title, msg, ((CoreException) x).getStatus());
							else
								ErrorDialog.openError(shell, title, msg, new Status(IStatus.ERROR, JavaVEPlugin.getPlugin().getDescriptor().getUniqueIdentifier(), 0, x.getLocalizedMessage() != null ? x.getLocalizedMessage() : x.getClass().getName(), x));
						}
					});
					JavaVEPlugin.log(x);
				}
			}
		}

		protected void initialize() throws CoreException {
			initialized = true;
			// In the case that we are called because a target VM recycle, make sure
			// all outstanding callbacks are called from the Synchronizer.
			modelBuilder.commit() ;
			initializeEditDomain();
			modelBuilder.setMsgRenderer(statusController);			
			modelBuilder.setSynchronizerSyncDelay(VCEPreferences.getPlugin().getPluginPreferences().getInt(VCEPreferences.SOURCE_SYNC_DELAY));

			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			initializeForProject(file);

			// Add listener to part activation so that we can handle recycle the vm when reactivated.
			final IWorkbenchWindow window = getSite().getWorkbenchWindow();
			window.getPartService().addPartListener(activationListener);
			window.getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					window.getShell().addShellListener(activationListener);
				}
			});
			activationListener.partActivated(window.getPartService().getActivePart());	// Initialize to current active part

			synchronized (JavaVisualEditorPart.this) {
				if (isDisposed())
					return;
				// Add the model synchronizer.
				modelSynchronizer = new JavaModelSynchronizer(beanProxyAdapterFactory, JavaCore.create(file.getProject()), new Runnable() {
					/**
					 * @see java.lang.Runnable#run()
					 */
					public void run() {
						recycleCntr = 0; // So that this resets the cntr for premature terminates.
						proxyFactoryRegistry.terminateRegistry();	// Terminate registry
					}
				});
			}
			
			// When the user types into a java editor the model is changed incrementally
			// We need to know when this happens so we can drive a refresh on the property sheet
			// AND refresh the text viewer.
			modelBuilder.addIBuilderListener(new IDiagramModelBuilder.IBuilderListener() {
				public void modelUpdated() {
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

		    	public void statusChanged (String msg){}
				public void reloadIsNeeded(boolean flag){}
				public void parsingStatus (boolean error){}  
				public void parsingPaused(boolean paused) {}
			});
			
			if (rebuildPalette) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						if (!isDisposed())
							rebuildPalette();
					}
				});				
			}
		}

		protected void initializeForProject(IFile file) throws CoreException {
			IProject proj = file.getProject();
			BeaninfoNature nature = BeaninfoNature.getRuntime(proj);
			ResourceSet rs = nature.newResourceSet();
			EMFEditDomainHelper.setResourceSet(rs, editDomain);
			editDomain.setData(NATURE_KEY, nature);	// Need to save the nature so we can check for validity later if project is renamed.
			
			// Add the property source and property descriptor adapter factories
			rs.getAdapterFactories().add(new PropertySourceAdapterFactory(editDomain));
			rs.getAdapterFactories().add(new PropertyDescriptorAdapterFactory());
			
			// Initialize the nature for the project that creates a remote VM and registers the 
			// java:/ protocol in MOF
			JavaInstantiation.initialize(nature.getResourceSet());
			
			// Create the target VM that is used for instances within the document we open
			createProxyFactoryRegistry(file);
			
			// Make sure that there is an AdaptorFactory for BeanProxies installed
			beanProxyAdapterFactory = new BeanProxyAdapterFactory(proxyFactoryRegistry, editDomain, new BasicAllocationProcesser());
			rs.getAdapterFactories().add(beanProxyAdapterFactory);
			
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

			NameInCompositionPropertyDescriptor desc = new NameInCompositionPropertyDescriptor(VCEMessages.getString("nameInComposition.displayName")); //$NON-NLS-1$
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
	private FocusListener focusListener = new FocusListener() {
		public void focusGained(FocusEvent e) {
			textEditorFocus = textEditorActive = e.getSource() == getSourceViewer().getTextWidget();
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

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave(IProgressMonitor progressMonitor) {
		modelChangeController.waitForCompleteTransaction();
		super.doSave(progressMonitor);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
	 */
	public void doSaveAs() {
		modelChangeController.waitForCompleteTransaction();
		super.doSaveAs();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class required) {
		if (required == IPropertySheetPage.class)
			return getPropertySheetPage();
		else if (required == BeansList.class)
			return getContentOutlinePage();
		else if (required == EditPartViewer.class)
			return primaryViewer;	// Current impl. only has one active editpart viewer. The outline viewer is its own IWorkbenchPart.
		else if (required == CommandStack.class)
			return editDomain.getCommandStack();
		else
			return super.getAdapter(required);
	}
	
	protected IContentOutlinePage getContentOutlinePage(){
		if (beansListPage == null)
			beansListPage = new JavaOutlinePage(new TreeViewer());
		return beansListPage;
	}

	/*
	 * This is the beans list outline page for this editor. There
	 * is one page per editor. There is only one BeansList, but it
	 * switches to the appropriate page for the current editor automatically.
	 * 
	 * This page can be created and disposed at any time, and recreated again. 
	 */
	private class JavaOutlinePage extends ContentOutlinePage {

		private class ShowOverviewAction extends ResourceAction {
			private static final String RESOURCE_PREFIX = "ShowOverviewAction."; //$NON-NLS-1$
			public ShowOverviewAction() {
				super(CodegenEditorPartMessages.RESOURCE_BUNDLE, RESOURCE_PREFIX, IAction.AS_CHECK_BOX);
				// TODO Eventually we need to also get the hover and disabled images.
				setChecked(CDEPlugin.getPlugin().getPluginPreferences().getBoolean(CDEPlugin.PREF_SHOW_OVERVIEW_KEY));
			}			

			public void run() {
				if (isChecked()) {
					setText(CodegenEditorPartMessages.getString(RESOURCE_PREFIX+"labelOutline")); //$NON-NLS-1$
					setToolTipText(CodegenEditorPartMessages.getString(RESOURCE_PREFIX+"tooltipOutline")); //$NON-NLS-1$
				} else {
					setText(CodegenEditorPartMessages.getString(RESOURCE_PREFIX+"label")); //$NON-NLS-1$
					setToolTipText(CodegenEditorPartMessages.getString(RESOURCE_PREFIX+"tooltip"));					 //$NON-NLS-1$
				}
				showOverview(isChecked());
				CDEPlugin.getPlugin().getPluginPreferences().setValue(CDEPlugin.PREF_SHOW_OVERVIEW_KEY, isChecked());
			}

		}
		
		private class CollapseAllAction extends Action {
			public CollapseAllAction() {
				super(ResourceNavigatorMessages.getString("CollapseAllAction.title"), IAction.AS_PUSH_BUTTON); //$NON-NLS-1$
				setToolTipText(ResourceNavigatorMessages.getString("CollapseAllAction.toolTip")); //$NON-NLS-1$
				setImageDescriptor(getImageDescriptor("elcl16/collapseall.gif")); //$NON-NLS-1$
				setHoverImageDescriptor(getImageDescriptor("clcl16/collapseall.gif")); //$NON-NLS-1$				
			}
			
			/*
			 * Copied from Resource navigator to do the same thing.
			 */
			protected ImageDescriptor getImageDescriptor(String relativePath) {
				String iconPath = "icons/full/"; //$NON-NLS-1$
				try {
					AbstractUIPlugin plugin = (AbstractUIPlugin) Platform.getPlugin(PlatformUI.PLUGIN_ID);
					URL installURL = plugin.getDescriptor().getInstallURL();
					URL url = new URL(installURL, iconPath + relativePath);
					return ImageDescriptor.createFromURL(url);
				} catch (MalformedURLException e) {
					// should not happen
					return ImageDescriptor.getMissingImageDescriptor();
				}
			}
			
			public void run() {
				Tree tree = (Tree) outline;
				collapse(tree.getItems());			
			}
			
			private void collapse(TreeItem[] items) {
				for (int i = 0; i < items.length; i++) {
					TreeItem item = items[i];
					item.setExpanded(false);
					collapse(item.getItems());
				}
			}
		}
		
		private class ShowHideEventsAction extends Action {
			int fStyle;
			public ShowHideEventsAction(String text, int style, int currentStyle){
				super(text,AS_RADIO_BUTTON);
				fStyle = style;
				setChecked(currentStyle == fStyle);
			}
			public void run() {
				// See whether we are now checked or unchecked
				// If we are checked then our style becomes the one used
				// If we are unchecked then reverse this and check us
				if (isChecked()) {
					editDomain.setData(JavaVEPlugin.SHOW_EVENTS, new Integer(fStyle));
					// Also update the stored preferences.
					JavaVEPlugin.getPlugin().getPluginPreferences().setValue(JavaVEPlugin.SHOW_EVENTS, fStyle);					
					// We need to refresh the viewer each time someone is checked
					TreeViewer gefTreeViewer = (TreeViewer)getViewer();
					refreshAll(gefTreeViewer.getContents());
				}
			}
			
			private void refreshAll(EditPart ep) {
				ep.refresh();
				List children = ep.getChildren();
				for(int i=0; i<children.size(); i++)
					refreshAll((EditPart) children.get(i));
			}			
		}
		

		private PageBook pageBook;
		private Control outline;
		private Canvas overview;
		private Thumbnail thumbnail;		
		private ShowOverviewAction showOverviewAction;
		private CollapseAllAction collapseAllAction;
		private DeleteAction deleteAction;

		public JavaOutlinePage(EditPartViewer viewer ){
			super(viewer);
		}
		
		public void init(IPageSite pageSite) {
			super.init(pageSite);
			
			IActionBars actionBars = pageSite.getActionBars();
			
			// The menu and toolbars have RetargetActions for DELETE, UNDO and REDO
			// Set an action handler to redirect these to the action registry's actions so they work
			// with the content outline without having to separately contribute these
			// to the outline page's toolbar
			deleteAction = new DeleteAction((IWorkbenchPart) JavaVisualEditorPart.this);
			
			actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);	// However, we don't actually add it the beanslist toolbar, we use the retarget from the editor contributor instead.
			actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), getAction(ActionFactory.UNDO.getId()));
			actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), getAction(ActionFactory.REDO.getId()));
			actionBars.setGlobalActionHandler(JavaVisualEditorActionContributor.PALETTE_SELECTION_ACTION_ID, getAction(JavaVisualEditorActionContributor.PALETTE_SELECTION_ACTION_ID));
			actionBars.setGlobalActionHandler(JavaVisualEditorActionContributor.PALETTE_MARQUEE_SELECTION_ACTION_ID, getAction(JavaVisualEditorActionContributor.PALETTE_MARQUEE_SELECTION_ACTION_ID));
			actionBars.setGlobalActionHandler(JavaVisualEditorActionContributor.PALETTE_DROPDOWN_ACTION_ID, getAction(JavaVisualEditorActionContributor.PALETTE_DROPDOWN_ACTION_ID));
			actionBars.setGlobalActionHandler(CustomizeJavaBeanAction.ACTION_ID, getAction(CustomizeJavaBeanAction.ACTION_ID));			

			IToolBarManager tbm = actionBars.getToolBarManager();
			collapseAllAction = new CollapseAllAction();
			tbm.add(collapseAllAction);			
			showOverviewAction = new ShowOverviewAction();
			tbm.add(showOverviewAction);
			
			IMenuManager mm = actionBars.getMenuManager();
			int showEvents = 0;
			Integer showEventsInt = (Integer) editDomain.getData(JavaVEPlugin.SHOW_EVENTS);
			if (showEventsInt == null) {
				// First beans list for this editor, so get it out of preferences
				showEvents = JavaVEPlugin.getPlugin().getPluginPreferences().getInt(JavaVEPlugin.SHOW_EVENTS);
				editDomain.setData(JavaVEPlugin.SHOW_EVENTS, new Integer(showEvents));	// Save current style for this editor.
			} else {
				// We've closed this beanslist once already, so reuse the setting at the time of the close.
				showEvents = showEventsInt.intValue();
			}
			// Create three actions - No events , Basic events and Expert events
			ShowHideEventsAction noEventsAction = new ShowHideEventsAction(CodegenEditorPartMessages.getString("JavaVisualEditor.NoEvents"),JavaVEPlugin.EVENTS_NONE, showEvents); //$NON-NLS-1$
			ShowHideEventsAction basicEventsAction = new ShowHideEventsAction(CodegenEditorPartMessages.getString("JavaVisualEditor.ShowEvents"),JavaVEPlugin.EVENTS_BASIC, showEvents); //$NON-NLS-1$
			ShowHideEventsAction expertEventsAction = new ShowHideEventsAction(CodegenEditorPartMessages.getString("JavaVisualEditor.ExpertEvents"),JavaVEPlugin.EVENTS_EXPERT, showEvents); //$NON-NLS-1$
			// Put the event actions - None, Basic and Export on the menu	
			mm.add(noEventsAction);
			mm.add(basicEventsAction);
			mm.add(expertEventsAction);			
		}
	
		public void createControl(Composite parent){
			pageBook = new PageBook(parent, SWT.NONE);
			pageBook.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent e) {
					beansListPage = null;
				}
			});
			
			outline = getViewer().createControl(pageBook);

			editDomain.addViewer(getViewer());
			KeyHandler outlineKeyHandler = new KeyHandler();
			outlineKeyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0), deleteAction);			
			getViewer().setKeyHandler(outlineKeyHandler);
			getSelectionSynchronizer().addViewer(getViewer());
			getViewer().setEditPartFactory(new DefaultTreeEditPartFactory(ClassDescriptorDecoratorPolicy.getPolicy(editDomain)));
						
			getViewer().setContents(new SubclassCompositionComponentsTreeEditPart(modelBuilder.getModelRoot()!=null ? modelBuilder.getModelRoot() : null));			
			
			Control control = getViewer().getControl();
			
			MenuManager menuMgr = new MenuManager();
			menuMgr.setRemoveAllWhenShown(true);
			Menu menu = menuMgr.createContextMenu(control);
			control.setMenu(menu);
			menuMgr.addMenuListener(new IMenuListener() {
				public void menuAboutToShow(IMenuManager menuMgr) {					
					GEFActionConstants.addStandardActionGroups(menuMgr);
					menuMgr.appendToGroup(GEFActionConstants.GROUP_UNDO, new Separator(IContextMenuConstants.GROUP_OPEN));
					openActionGroup.setContext(new ActionContext(getViewer().getSelection()));
					openActionGroup.fillContextMenu(menuMgr);
					openActionGroup.setContext(null);					
					menuMgr.appendToGroup(GEFActionConstants.GROUP_UNDO, getAction(ActionFactory.UNDO.getId()));
					menuMgr.appendToGroup(GEFActionConstants.GROUP_UNDO, getAction(ActionFactory.REDO.getId()));										
					menuMgr.appendToGroup(GEFActionConstants.GROUP_EDIT, deleteAction);
					IAction customize = graphicalActionRegistry.getAction(CustomizeJavaBeanAction.ACTION_ID);
					if (customize.isEnabled()) 
						menuMgr.appendToGroup(GEFActionConstants.GROUP_EDIT, customize);
				}
			});
			getSite().registerContextMenu("JavaVisualEditor.beansViewer", menuMgr, getViewer()); //$NON-NLS-1$
			
			showOverview(showOverviewAction.isChecked());
			
			deleteAction.setSelectionProvider(getViewer());
			getViewer().addSelectionChangedListener(new ISelectionChangedListener() {
				public void selectionChanged(SelectionChangedEvent event) {
					deleteAction.update();
				}
			});
		}
	
		public void dispose(){
			getSelectionSynchronizer().removeViewer(getViewer());
			editDomain.removeViewer(getViewer());		
			super.dispose();
		}
	
		public Control getControl() {
			return pageBook;
		}

		protected void initializeOverview() {
			overview = new Canvas(pageBook, SWT.NONE);			
			LightweightSystem lws = new LightweightSystem(overview);
			ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) primaryViewer.getRootEditPart();
			thumbnail = new ScrollableThumbnail((Viewport)root.getFigure());
			thumbnail.setSource(root.getLayer(LayerConstants.PRINTABLE_LAYERS));
			lws.setContents(thumbnail);
		}
	
		protected void showOverview(boolean show) {
			if (!show) {
				if (overview != null)
					thumbnail.setVisible(false);
				pageBook.showPage(outline);
				collapseAllAction.setEnabled(true);
			} else {
				if (overview == null)
					initializeOverview();
				collapseAllAction.setEnabled(false);					
				thumbnail.setVisible(true);
				pageBook.showPage(overview);
			}
		}
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
				
			actionBars.getStatusLineManager().setMessage((Image) null, null); // Clear the status bar as we try and show any errors for the selected JavaBean
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
							actionBars.getStatusLineManager().setMessage(error.getImage(), error.getMessage());
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
							modelChangeController.flushCodeGen(new EditorSelectionSynchronizer((Notifier)prop), MARKER_PropertySelection); //$NON-NLS-1$
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
								if (parent != null) {
									EStructuralFeature sf = (EStructuralFeature) entry.getId();
									modelChangeController.flushCodeGen(new EditorSelectionSynchronizer(parent, sf), MARKER_PropertySelection); //$NON-NLS-1$
								}
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
		if (model instanceof IJavaInstance) {
			// It is possible that the javaBean has just been added or changes made to it
			// that have not made it to the source yet.
			// We therefore need to flush any pending changes through before we query the source range
			// to drive selection otherwise we are going to drive source changes ahead of the source being actually changed
			modelChangeController.flushCodeGen(new EditorSelectionSynchronizer((IJavaInstance) model), MARKER_JVESelection); //$NON-NLS-1$
		} else if (model instanceof EventInvocationAndListener) {
			EventInvocationAndListener eil = (EventInvocationAndListener) model ;
			AbstractEventInvocation ei = (AbstractEventInvocation) eil.getEventInvocations().get(0) ;
			modelChangeController.flushCodeGen(new EditorSelectionSynchronizer(ei), MARKER_JVESelection); //$NON-NLS-1$
		} else if (model instanceof Notifier) {
		   	Notifier n = (Notifier) model ;
			modelChangeController.flushCodeGen(new EditorSelectionSynchronizer(n), MARKER_JVESelection); //$NON-NLS-1$								
		}
	}
}
