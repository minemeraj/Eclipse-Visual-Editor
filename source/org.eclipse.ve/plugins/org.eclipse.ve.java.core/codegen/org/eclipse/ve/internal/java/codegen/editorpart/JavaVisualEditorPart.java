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
 *  $Revision: 1.20 $  $Date: 2004-04-02 00:11:35 $ 
 */

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
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.Viewport;
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
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.*;
import org.eclipse.gef.palette.ToolEntry;
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
	
	public final static int SELECT_JVE = 1;
	public final static int SELECT_PROPERTY = 2;
	public final static int SELECT_PROPERTY_CHANGE = 3;
	
	public final static int PARSE_ERROR_STATE = 3;	// Model controller update state to indicate parse error.
	
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

	protected JaveVisualEditorLoadingFigureController loadingFigureController;

	protected IDiagramModelBuilder modelBuilder;

	protected ProxyFactoryRegistry proxyFactoryRegistry;
	protected JavaModelSynchronizer modelSynchronizer;
	protected BeanProxyAdapterFactory beanProxyAdapterFactory;
	protected boolean rebuildPalette = true;
	
	protected EToolsPropertySheetPage propertySheetPage;
	
	protected JavaOutlinePage beansListPage;
	
	protected String currentStatusMessage = "";
	
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

	public JavaVisualEditorPart() {
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
// TODO GET RID of this when Gili no longer needs one.
modelBuilder.setMsgRenderer(new IJVEStatus() {
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.editorpart.IJVEStatus#showMsg(java.lang.String, int)
	 */
	public void showMsg(String msg, int kind) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.editorpart.IJVEStatus#addStatusListener(org.eclipse.ve.internal.java.codegen.core.IJVEStatusChangeListener)
	 */
	public void addStatusListener(IJVEStatusChangeListener sl) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.editorpart.IJVEStatus#removeStatusListener(org.eclipse.ve.internal.java.codegen.core.IJVEStatusChangeListener)
	 */
	public void removeStatusListener(IJVEStatusChangeListener sl) {
		// TODO Auto-generated method stub
	}
	int status;
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.core.ICodeGenStatus#setStatus(int, boolean)
	 */
	public void setStatus(int flag, boolean state) {
		if (state) {
			status |= flag;
		} else {
			status &= ~flag;
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.core.ICodeGenStatus#getState()
	 */
	public int getState() {
		// TODO Auto-generated method stub
		return status;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.core.ICodeGenStatus#isStatusSet(int)
	 */
	public boolean isStatusSet(int state) {
		// TODO Auto-generated method stub
		return (status & state) != 0;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.core.ICodeGenStatus#setReloadPending(boolean)
	 */
	public boolean setReloadPending(boolean flag) {
		// TODO Auto-generated method stub
		return false;
	}
});

		modelChangeController = new JavaVisualEditorModelChangeController(this, modelBuilder);
		editDomain.setCommandStack(new JavaVisualEditorCommandStack(modelChangeController));
		editDomain.setData(IModelChangeController.MODEL_CHANGE_CONTROLLER_KEY, modelChangeController);	
		
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
		
		// Create the pause/reload action.
		graphicalActionRegistry.registerAction(new ReloadAction(new ReloadAction.IReloadCallback() {
			public void pause() {
				modelChangeController.setHoldState(IModelChangeController.NO_UPDATE_STATE, null);	// So no updates while paused.
				modelBuilder.pause();
			}
			public void reload() {
				loadModel();
			}
		}));
			
		super.init(site, input);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#doSetInput(org.eclipse.ui.IEditorInput)
	 */
	protected void doSetInput(IEditorInput input) throws CoreException {
		// The input has changed.
		super.doSetInput(input);
		loadModel();
	}
	
	protected Job setupJob = null; 
	protected void loadModel() {
		ReloadAction rla = (ReloadAction) graphicalActionRegistry.getAction(ReloadAction.RELOAD_ACTION_ID);
		rla.setEnabled(false);	// While reloading, don't want button to be pushed.
		
		modelChangeController.setHoldState(IModelChangeController.NO_UPDATE_STATE, null);	// Don't allow updates..
		
		setRootModel(null); // Clear it out so we don't see all of the changes that are about to happen.
		loadingFigureController.showLoadingFigure(true);	// Start the loading figure.
		// Kick off the setup thread. Doing so that system stays responsive.
		if (setupJob == null) {
			setupJob = new Setup("Setup Java Visual Editor");
			setupJob.setPriority(Job.SHORT); // Make it slightly slower so that ui thread still active
		}
		// TODO Not quite happy with this. Need way to cancel and join any previous to be on safe side.
		// This here will reschedule it to run again if one already running.
		setupJob.schedule();	// Start asap.
	}

	private BeanSubclassComposition currentSetRoot;
	/*
	 * Set a new model into the root editparts. This can happen because the setup and the initial creation
	 * of the viewers can be in a race condition with each other.
	 * NOTE: Must run in display thread.
	 */
	protected void setRootModel(final BeanSubclassComposition root) {
		Assert.isTrue(Display.getCurrent() != null);
		
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

		try {
			if (currentSetRoot != root && currentSetRoot != null)
				currentSetRoot.eNotify(new ENotificationImpl((InternalEObject) currentSetRoot, CompositionProxyAdapter.RELEASE_PROXIES, null, null, null, false));
		} finally {
			currentSetRoot = root;
		}
		if (propertySheetPage != null) {
			// At this point in time the property sheet may be in focus, so it is not listening to selection changes
			// from us. Because of this it may have an active entries which are now pointing to the obsolete
			// model. This model won't have a resource set, etc. So the entries need to be sent away.
			propertySheetPage.selectionChanged(this, primaryViewer.getSelection());	
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
	 * NOTE: Must run in display thread.
	 */
	protected void initializeViewers() {
		Assert.isTrue(Display.getCurrent() != null);
		
		if (primaryViewer != null && modelBuilder.getModelRoot()!=null) {
			Diagram d = modelBuilder.getDiagram();
			if (d != null) {
				editDomain.setViewerData(primaryViewer, EditDomain.DIAGRAM_KEY, d);
				setRootModel(modelBuilder.getModelRoot()); // Set into viewers.
			}
			loadingFigureController.showLoadingFigure(false);			
			ReloadAction rla = (ReloadAction) graphicalActionRegistry.getAction(ReloadAction.RELOAD_ACTION_ID);	// Now it can be enabled.
			rla.setEnabled(true);
			modelChangeController.setHoldState(IModelChangeController.READY_STATE, null);	// Restore to allow updates.
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
	}	/*
	 * This will create the proxy factory registry.
	 */
	protected void createProxyFactoryRegistry(IFile aFile, IProgressMonitor monitor) throws CoreException {
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
				contribs, monitor);
		registry.getBeanTypeProxyFactory().setMaintainNotFoundTypes(true);	// Want to maintain list of not found types so we know when those types have been added.
		
		synchronized (this) {
			if (isDisposed()) {
				// Editor closed while we were opening it. So close the registry.
				registry.terminateRegistry(); 
				return;
			}
			proxyFactoryRegistry = registry;
			proxyFactoryRegistry.addRegistryListener(registryListener);
			beanProxyAdapterFactory.setProxyFactoryRegistry(proxyFactoryRegistry);
		}
		return;
	}
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

	private void restartVM() {
		restartVMNeeded = false;
		loadModel();
	}

	protected SelectionSynchronizer getSelectionSynchronizer() {
		if (selectionSynchronizer == null)
			selectionSynchronizer = new SelectionSynchronizer();
		return selectionSynchronizer;
	}

	public void dispose() {
		try {
			if (setupJob != null) {
				// We can't actually wait for it because we could get into a deadlock since it may
				// try to do syncExec for some reason.
				setupJob.cancel();
			}
			
			// Remove the proxy registry first so that we aren't trying to listen to any changes and sending them through since it isn't necessary.
			if (proxyFactoryRegistry != null) {
				proxyFactoryRegistry.removeRegistryListener(registryListener); // We're going away, don't let the listener come into play.
				proxyFactoryRegistry.terminateRegistry();
			}
			
			modelBuilder.dispose();
			
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
		if (category.equals(JavaVisualEditorActionContributor.STATUS_FIELD_CATEGORY)) {
			IStatusField field= getStatusField(category);
			if (field != null)
				field.setText(currentStatusMessage);
		} else
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
				// The menu and toolbars have RetargetActions for UNDO and REDO and pause/reload.
				// Set an action handler to redirect these to the action registry's actions so they work when the property sheet is enabled
				actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), getAction(ActionFactory.UNDO.getId()));
				actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), getAction(ActionFactory.REDO.getId()));
				actionBars.setGlobalActionHandler(ReloadAction.RELOAD_ACTION_ID, getAction(ReloadAction.RELOAD_ACTION_ID));
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
	private static final Integer NATURE_KEY = new Integer(0);	// Used within Setup to save the nature	 
	private class Setup extends Job {

		public Setup(String name) {
			super(name);
		}
				
		protected IStatus run(IProgressMonitor monitor) {			
			try {
				monitor.beginTask("", 300);
				if (!initialized) {
					initialize(new SubProgressMonitor(monitor, 100));
				} else {
					// Check to see if nature is still valid. If it is not, we need to reinitialize for it. OR it 
					// could be we were moved to another project entirely.
					BeaninfoNature nature = (BeaninfoNature) editDomain.getData(NATURE_KEY);
					IFile file = ((IFileEditorInput) getEditorInput()).getFile();					
					if (nature == null || !nature.isValidNature() || !file.getProject().equals(nature.getProject()))
						initializeForProject(file, new SubProgressMonitor(monitor, 100));
					else {
						boolean createFactory = false;
						synchronized (JavaVisualEditorPart.this) {
							createFactory = proxyFactoryRegistry == null || !proxyFactoryRegistry.isValid();
						}
						if (createFactory)
							createProxyFactoryRegistry(file, new SubProgressMonitor(monitor, 100));	// The registry is gone, need new one.
						else
							monitor.worked(100);
					}
				}
			
				if (monitor.isCanceled())
					return Status.CANCEL_STATUS;
				
				modelBuilder.loadModel((IFileEditorInput) getEditorInput(), new SubProgressMonitor(monitor, 100));
				monitor.subTask("Initializing model");

				if (monitor.isCanceled())
					return Status.CANCEL_STATUS;
				
				DiagramData dd = modelBuilder.getModelRoot();
				if (dd != null) {
					editDomain.setDiagramData(dd);
					InverseMaintenanceAdapter ia = new InverseMaintenanceAdapter() {
						protected boolean subShouldPropagate(
							EReference ref,
							Object newValue) {
							return !(newValue instanceof Diagram); // On the base BeanComposition, don't propagate into Diagram references.
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
					
					if (!isDisposed() && !monitor.isCanceled())
						getSite().getShell().getDisplay().asyncExec(new Runnable() {
							public void run() {
								initializeViewers();
							}
						});
				} else {
					// We didn't get a model for some reason, so just bring down the load controller, the parse error flag should already be set.
					getSite().getShell().getDisplay().asyncExec(new Runnable() {
						public void run() {
							loadingFigureController.showLoadingFigure(false);	// Bring down only the loading figure.
							ReloadAction rla = (ReloadAction) graphicalActionRegistry.getAction(ReloadAction.RELOAD_ACTION_ID);	// Now it can be enabled.
							rla.setEnabled(true);	// Because it was disabled.														
						}
					});	
					return Status.CANCEL_STATUS;
				}
			
			} catch (final Exception x) {
				// If we are disposed, then it doesn't matter what the error is. This can occur because we closed
				// while loading and there is no way to stop this thread when that occurs. We don't want main thread
				// to just wait until this thread finishes when closing because that holds everything up when we don't
				// really care. This way we let it throw an exception because something was in a bad state and just
				// don't put up a message.
				if (!isDisposed()) {
					getSite().getShell().getDisplay().asyncExec(new Runnable() {
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
					getSite().getShell().getDisplay().asyncExec(new Runnable() {
						public void run() {
							loadingFigureController.showLoadingFigure(false);	// Bring down only the loading figure.
							processParseError(true);	// Treat it as a parse error, the model parser couldn't even get far enough to signal parse error.
							ReloadAction rla = (ReloadAction) graphicalActionRegistry.getAction(ReloadAction.RELOAD_ACTION_ID);	// Now it can be enabled.
							rla.setEnabled(true);	// Because it was disabled.							
						}
					});					
					return Status.CANCEL_STATUS;
				}
			}
			
			monitor.done();
			return !monitor.isCanceled() ? Status.OK_STATUS : Status.CANCEL_STATUS;
		}

		protected void initialize(IProgressMonitor monitor) throws CoreException {
			initialized = true;
			initializeEditDomain();		
			modelBuilder.setSynchronizerSyncDelay(VCEPreferences.getPlugin().getPluginPreferences().getInt(VCEPreferences.SOURCE_SYNC_DELAY));

			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			initializeForProject(file, monitor);

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
							loadModel();
						}
					});
				}
				
				public void parsingStatus (boolean error){
					processParseError(error);
				}
				
				public void parsingPaused(boolean paused) {
					// We don't need this. Since we cause the pause, we know when paused.
				}
			});
			
			if (rebuildPalette) {
				getSite().getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						if (!isDisposed())
							rebuildPalette();
					}
				});				
			}
		}

		protected void initializeForProject(IFile file, IProgressMonitor monitor) throws CoreException {
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
			
			// Make sure that there is an AdaptorFactory for BeanProxies installed
			beanProxyAdapterFactory = new BeanProxyAdapterFactory(null, editDomain, new BasicAllocationProcesser());
			rs.getAdapterFactories().add(beanProxyAdapterFactory);
			
			// Create the target VM that is used for instances within the document we open
			createProxyFactoryRegistry(file, monitor);
						
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

	private void processParseError(boolean parseError) {
		modelChangeController.setHoldState(parseError ? PARSE_ERROR_STATE : IModelChangeController.READY_STATE, null);
		((ReloadAction) graphicalActionRegistry.getAction(ReloadAction.RELOAD_ACTION_ID)).parseError(parseError);
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
			actionBars.setGlobalActionHandler(ReloadAction.RELOAD_ACTION_ID, getAction(ReloadAction.RELOAD_ACTION_ID));

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
}
