/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.emf;
/*
 *  $RCSfile: EMFGraphicalEditorPart.java,v $
 *  $Revision: 1.17 $  $Date: 2005-08-24 23:12:48 $ 
 */


import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.*;
import org.eclipse.gef.ui.actions.*;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.parts.*;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.ContainerGenerator;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.*;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;

import org.eclipse.ve.internal.cdm.Diagram;
import org.eclipse.ve.internal.cdm.DiagramData;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.palette.Root;
import org.eclipse.ve.internal.cde.rules.IRuleRegistry;

import org.eclipse.ve.internal.propertysheet.*;
import org.eclipse.ve.internal.propertysheet.command.CommandStackPropertySheetEntry;
/**
 * Base class to use for MOG Graphical Editors.
 * It is abstract and must be specialized.
 *
 * This will be changed in the future. IT needs to be
 * redesigned and may go away.
 */
public abstract class EMFGraphicalEditorPart
	extends EditorPart
{
protected boolean wantResourceListener = true;
// This class listens to changes to the file system in the workspace, and 
// makes changes accordingly.
// 1) An open, saved file gets deleted -> close the editor
// 2) An open file gets renamed or moved -> change the editor's input accordingly	
class ResourceTracker implements IResourceChangeListener, IResourceDeltaVisitor {
	protected boolean resourcesChanged = false;
	public void resourceChanged(IResourceChangeEvent e) {
		IResourceDelta delta = e.getDelta();
		try {
			if (delta != null) {
				// The accept will execute visit(IResourceDelta delta) and
				// visit all the children of the delta resource if visit 
				// returns true.
				delta.accept(this);
				// If any resources were changed during the visit calls,
				// make the calls to handle this by notifying the editparts, etc.
				if (resourcesChanged) {
					// run this separately since Markers may be involved which
					// can't be changed while were in the middle of a resourceChanged().
					Display display = getSite().getShell().getDisplay();
					display.asyncExec(new Runnable() {
						public void run() {
							// Calling setInput() would have the undesired effect of flushing
							// the command stack.  To avoid that, superSetInput() method is called.
							handleResourceChanged();
						}
					});
					resourcesChanged = false;
				}
			}
		} catch (CoreException x) {
			// What should be done here?
		}
	}
	public boolean visit(IResourceDelta delta) {
		if (delta == null)
			return true;

		if (delta.getResource().equals(((IFileEditorInput) getEditorInput()).getFile())) {
			if (delta.getKind() == IResourceDelta.REMOVED) {
				if ((IResourceDelta.MOVED_TO & delta.getFlags()) == 0) { // if the file was deleted
					// NOTE: The case where an open, unsaved file is deleted is being handled by the 
					// PartListener added to the Workbench in the initialize() method.
					if (!isDirty())
						closeEditor(false);
				} else { // else if it was moved or renamed
					final IFile newFile = ResourcesPlugin.getWorkspace().getRoot().getFile(delta.getMovedToPath());
					Display display = getSite().getShell().getDisplay();
					display.asyncExec(new Runnable() {
						public void run() {
							// Calling setInput() would have the undesired effect of flushing
							// the command stack.  To avoid that, superSetInput() method is called.
							superSetInput(new FileEditorInput(newFile));
						}
					});
				}
			}
		} else {
			// If the resource is a file, see if it's part of our resource set.
			// If so, remove it from our resource set and let the subclasses
			// handle the rest.
			// Note: ignore marker changes.
			if (delta.getKind() == IResourceDelta.CHANGED
				&& ((IResourceDelta.MARKERS & delta.getFlags()) == 0)
				&& delta.getResource().getType() == IResource.FILE) {
				String filename = delta.getResource().getFullPath().makeRelative().toString();
				if (fResourceSet != null) {
					Iterator iter = fResourceSet.getResources().iterator();
					while (iter.hasNext()) {
						Resource res = (Resource) iter.next();
						if (res.getURI().toFileString().equals(filename)) {
							// TODO MOF has a major bug that has to be fixed here before we remove this
							// resource from the resource set. Otherwise  we can't save the this file
							// because we throw a NullPointerException later on trying to resolve 
							// this resource's workspace.
							// !!!! UNCOMMENT THE NEXT LINE when the bug gets fixed. !!!!
							//			getResourceSet().remove(res);
							resourcesChanged = true;
							// Remove the following line when the code is fixed.
							createWarningMessage(filename);
							break;
						}
					}
				}
			}
		}
		return true;
	}
}

private ActionRegistry actionRegistry;
private DefaultEditDomain domain;
protected EToolsPropertySheetPage fPropertySheetPage;
protected ListenerList fPropertySheetSelectionListeners = new ListenerList(1);

protected EditPartViewer primaryViewer;
protected PaletteViewer paletteViewer;

protected Resource fResource;
protected ResourceSet fResourceSet;

protected SelectionSynchronizer selectionSynchronizer;

private boolean fDirtyState = false;

// Remove the following 2 fields after a MOF bug is fixed. See resourceTracker for details.
private boolean needToCloseEditor = false; 
private String filenameOfChangedResource = ""; //$NON-NLS-1$

protected CommandStackListener fDirtyListener = new CommandStackListener() {
	public void commandStackChanged(EventObject event){
		setDirty(true);
	}
};

protected IResourceChangeListener resourceListener;

public EMFGraphicalEditorPart() {
}

private KeyHandler outlineKeyHandler;
private KeyHandler canvasKeyHandler;
/**
 * Returns the KeyHandler for the outline view
 */
protected KeyHandler getOutlineKeyHandler(){
	if (outlineKeyHandler == null){
		outlineKeyHandler = new KeyHandler();
		outlineKeyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0),getOutlineDeleteAction());
	}
	return outlineKeyHandler;
}
/**
 * Returns the KeyHandler for the canvas view
 */
protected KeyHandler getCanvasKeyHandler(){
	if (canvasKeyHandler == null){
		canvasKeyHandler = new KeyHandler();
		canvasKeyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0),getCanvasDeleteAction());
	}
	return canvasKeyHandler;
}
protected WorkbenchPartAction fOutlineDeleteAction;
protected WorkbenchPartAction fCanvasDeleteAction;

protected WorkbenchPartAction getOutlineDeleteAction(){
	if (fOutlineDeleteAction == null){
		fOutlineDeleteAction = new DeleteAction((IWorkbenchPart) this);
		((DeleteAction) fOutlineDeleteAction).setSelectionProvider(getContentOutlinePage());
	}
	return fOutlineDeleteAction;
}
protected WorkbenchPartAction getCanvasDeleteAction(){
	if (fCanvasDeleteAction == null){
		fCanvasDeleteAction = new DeleteAction((IWorkbenchPart) this);
		((DeleteAction) fCanvasDeleteAction).setSelectionProvider(getPrimaryViewer());
	}
	return fCanvasDeleteAction;
}
// The storing and caching of these fields is because GEFMessages is an internal package that is explicitly not exported in its plugin manifest
public static String GEF_DELETE_LABEL;
public static String getGEFDeleteLabel(){
	if ( GEF_DELETE_LABEL == null ) {
		GEF_DELETE_LABEL = Platform.getResourceString(Platform.getBundle("org.eclipse.gef"), "%DeleteAction.Label"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	return GEF_DELETE_LABEL;
}
public static String GEF_DELETE_TOOLTIP;
public static String getGEFDeleteTooltip(){
	if ( GEF_DELETE_TOOLTIP == null ) {
		GEF_DELETE_TOOLTIP = Platform.getResourceString(Platform.getBundle("org.eclipse.gef"), "%DeleteAction.Tooltip"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	return GEF_DELETE_TOOLTIP;
}
/** Closes this editor. */
protected void closeEditor( final boolean save ){
	Display display = getSite().getShell().getDisplay();
	display.asyncExec( new Runnable(){
		public void run(){
			getSite().getPage().closeEditor( EMFGraphicalEditorPart.this, save );
		}
	} );
}

protected ActionRegistry createActionRegistry(){
	return new ActionRegistry();
}

/**
 * This may be overridden to supply a subclass of 
 * DefaultEditDomain.
 */
protected DefaultEditDomain createEditDomain(){
	org.eclipse.ve.internal.cde.core.EditDomain dom = new org.eclipse.ve.internal.cde.core.EditDomain(this);
	dom.setRuleRegistry(createRuleRegistry());
	dom.setAnnotationLinkagePolicy(createLinkagePolicy());
	// Give it a default do nothing special model controller.
	dom.setData(ModelChangeController.MODEL_CHANGE_CONTROLLER_KEY, new ModelChangeController() {
				
		/* (non-Javadoc)
         * @see org.eclipse.ve.internal.cde.core.ModelChangeController#getRootPropertySheetEntry()
         */
        protected IDescriptorPropertySheetEntry getRootPropertySheetEntry() {
            return rootPropertySheetEntry;
        }
		
		/* (non-Javadoc)
		 * @see org.eclipse.ve.internal.cde.core.ModelChangeController#getHoldState()
		 */	
		public int getHoldState() {
			return holdState;
		}
		
		protected synchronized void startChange(boolean nested) {
		}

		protected synchronized void stopChange(boolean nested) {
		}

		/* (non-Javadoc)
		 * @see org.eclipse.ve.internal.cde.core.ModelChangeController#getHoldMsg()
		 */
		public String getHoldMsg() {
			return holdMsg;
		}

	});
	return dom;
}

protected IRuleRegistry createRuleRegistry() {
	return null;	// By default, no rule registry.
}

/**
 * This should be overridden if a different linkage policy is needed.
 */
protected AnnotationLinkagePolicy createLinkagePolicy() {
	return new EMFAnnotationLinkagePolicy();
}

protected void addToCanvasContextMenu(IMenuManager menu) {
	WorkbenchPartAction deleteAction = getCanvasDeleteAction();
	if (deleteAction.isEnabled())
		menu.add(deleteAction);
}
protected void addToOutlineContextMenu(IMenuManager menu) {
	WorkbenchPartAction deleteAction = getOutlineDeleteAction();
	if (deleteAction.isEnabled())
		menu.add(deleteAction);
}
/**
 * This is the method to override to dynamically add popup menu items.
 * This is hack for now and needs to be designed more thoroughly with IActionFilter
 * so that items are added based on filtering logic and merging algorithms.
 */
protected void contextMenuAboutToShow(IMenuManager menu, ISelectionProvider aSelectionProvider) {
	// TODO just a hack for now to allow you to dynamically add menu items... needs to be designed.
};

protected void createPrimaryViewer(){
	primaryViewer = new ScrollingGraphicalViewer();
	primaryViewer.setRootEditPart( new ScalableFreeformRootEditPart() );
	primaryViewer.setEditDomain(getDomain());
	((org.eclipse.ve.internal.cde.core.EditDomain) getDomain()).setViewerData(primaryViewer, org.eclipse.ve.internal.cde.core.ZoomController.ZOOM_KEY, new org.eclipse.ve.internal.cde.core.ZoomController());
	((org.eclipse.ve.internal.cde.core.EditDomain) getDomain()).setViewerData(primaryViewer, org.eclipse.ve.internal.cde.core.GridController.GRID_KEY, new org.eclipse.ve.internal.cde.core.GridController());
	((org.eclipse.ve.internal.cde.core.EditDomain) getDomain()).setViewerData(primaryViewer, org.eclipse.ve.internal.cde.core.DistributeController.DISTRIBUTE_KEY, new org.eclipse.ve.internal.cde.core.DistributeController((GraphicalViewer) primaryViewer));
	primaryViewer.setKeyHandler(new GraphicalViewerKeyHandler((GraphicalViewer)primaryViewer).setParent(getCanvasKeyHandler()));
	// The registry action holds a delete action because this is what the toolbar uses
	actionRegistry.registerAction(getCanvasDeleteAction());	
	getDomain().addViewer(primaryViewer);
}

public void createPrimaryViewerControl(Composite parent) {
	Canvas canvas = new Canvas(parent, SWT.BORDER);
	StackLayout stack = new StackLayout();
	stack.topControl = getPrimaryViewer().createControl(canvas);
	canvas.setLayout(stack);

	MenuManager menuMgr = createMenuManager(stack.topControl);
	// A menu listener is added to perform the menuAboutToShow() which will first
	// add the delete action, then the dynamic actions which subclasses can 
	// override via the contextMenuAboutToShow(IMenuManager) method,
	menuMgr.addMenuListener(new IMenuListener() {
		public void menuAboutToShow(IMenuManager menuMgr) {
			addToCanvasContextMenu(menuMgr);
			contextMenuAboutToShow(menuMgr,getPrimaryViewer());
			menuMgr.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		}
	});
	getSite().registerContextMenu("EMFGraphicalEditorPart.graphviewer", menuMgr, getPrimaryViewer()); //$NON-NLS-1$
}
/**
 * Create a MenuManager to handle the context sensitive menu.
 * A specific group marker ("additions") is added so that any static 
 * menus/menuitems can be added that are defined in the plugin through the 
 * Eclipse plugin extension point="org.eclipse.ui.popupMenus".
 * 
 * Note:In order for this MenuManager to include the static items defined in the 
 * 		plugin.xml, it must be registered with the WorkbenchPartSite via the API
 * 		IWorkbenchPartSite.registerContextMenu(String menuId, MenuManager menuManager, ISelectionProvider selectionProvider)
 */
protected MenuManager createMenuManager(Control control) {
	MenuManager menuMgr= new MenuManager();
	menuMgr.setRemoveAllWhenShown(true);
	Menu menu= menuMgr.createContextMenu(control);
	control.setMenu(menu);
	return menuMgr;
}
public void createPaletteControl(Composite parent){
	primCreatePaletteControl(parent);
}
protected void primCreatePaletteControl(Composite parent) {
//	Canvas canvas = new Canvas(parent, SWT.NONE);
//	canvas.setLayoutData(new Splitter.Weight(100,0,0));
	if (paletteViewer == null){
		paletteViewer = new PaletteViewer();
		paletteViewer.createControl(parent);
	}
//	paletteViewer.setControl(canvas);
	getDomain().setPaletteViewer(paletteViewer);
	// Subclasses can supply their own palette, otherwise it is read from the
	// plugin extension point palette="nnn" where nnn is an href to the palette
	// in the plugin.xml file that defined this editor.
	setPaletteRoot();	
}

/**
 * By now we have parsed the document (see readDocument()) and any severe errors
 * will have been logged as markers via handleException(Exception, IFile) and 
 * through validateModel(EObject).
 * If there are no severe errors, create the palette and primary viewer control,
 * otherwise, create a part control to show the errors in case the task list is
 * not showing.
 */
public void createPartControl(Composite c) {
	SashForm splitter = new SashForm(c, SWT.HORIZONTAL);
	createPaletteControl(splitter);
	createPrimaryViewerControl(splitter);
	splitter.setWeights(new int []{1,4});
}
/**
 * Create controls to display the errors (markers).
 */
protected void createPartControlWithErrors(Composite parent, IMarker[] markers) {
	// First consolidate the markers with messages into a String array.
	String[] errorMessages = new String [markers.length];
	for (int i=0; i<markers.length; i++) {
		try {
			if (markers[i].exists() || 
				markers[i].getAttribute(IMarker.MESSAGE) != null) 
			{
				String msg = (String)markers[i].getAttribute(IMarker.MESSAGE);
				// Include line number in the message if there is one
				if (markers[i].getAttribute(IMarker.LINE_NUMBER) != null) {
					Integer linenum = (Integer) markers[i].getAttribute(IMarker.LINE_NUMBER);
					msg = MessageFormat.format(CDEEmfMessages.Line_number, new Object[] {msg, linenum.toString()}); 
				}
				errorMessages[i] = msg;
			}
		} catch ( CoreException exc ) {
			CDEPlugin.getPlugin().getLog().log(exc.getStatus());
		}
	}
	// Now create the SWT controls to show the messages in a list.
	Composite composite= new Composite(parent, SWT.NONE);
	composite.setLayout(new GridLayout());
	composite.setLayoutData(new GridData());
//	composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));
	
	Text text = new Text(composite,SWT.MULTI|SWT.READ_ONLY|SWT.WRAP);
	text.setForeground(text.getDisplay().getSystemColor(SWT.COLOR_RED));
	text.setBackground(text.getDisplay().getSystemColor(SWT.COLOR_GRAY));
	text.setText(getErrorTextMessage());
	text.setLayoutData(new GridData());
	
	text = new Text(composite,SWT.SINGLE|SWT.READ_ONLY);
	text.setForeground(text.getDisplay().getSystemColor(SWT.COLOR_BLUE));
	text.setBackground(text.getDisplay().getSystemColor(SWT.COLOR_GRAY));
	text.setText(CDEEmfMessages.List_of_errors); 
	text.setLayoutData(new GridData());

	org.eclipse.swt.widgets.List list= new org.eclipse.swt.widgets.List(composite, SWT.BORDER|SWT.V_SCROLL|SWT.H_SCROLL);
	list.setItems(errorMessages);
	list.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL|GridData.GRAB_HORIZONTAL));
}

public void dispose() {
	try {
		if (getEditorInput() != null && resourceListener != null) {
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			file.getWorkspace().removeResourceChangeListener(resourceListener);
		}
		if (partListener != null)
			getSite().getWorkbenchWindow().getPartService().removePartListener(partListener);		
	} catch (Throwable t) {
		CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, "", t)); //$NON-NLS-1$
	}

	try {
		getDomain().setDefaultTool(null);
	} catch (Throwable t) {
		CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, "", t)); //$NON-NLS-1$
	}

	getActionRegistry().dispose();

	try {
		((org.eclipse.ve.internal.cde.core.EditDomain) getDomain()).dispose();
	} catch (Throwable t) {
		//		JBCFPlugin.log(t) ;
	}
	super.dispose();
}

protected ActionRegistry getActionRegistry() {
	if (actionRegistry == null)
		actionRegistry = createActionRegistry();
	return actionRegistry;
}

public Object getAdapter(Class type) {
	if (type == IPropertySheetPage.class)
		return getPropertySheetPage();
	if (type == GraphicalViewer.class)
		return getPrimaryViewer();
	if (type == IContentOutlinePage.class)
		return getContentOutlinePage();
	if (type == CommandStack.class)
		return getDomain().getCommandStack();
	if (type == ActionRegistry.class)
		return getActionRegistry();
	if (type == org.eclipse.ve.internal.cde.core.EditDomain.class)
		return (org.eclipse.ve.internal.cde.core.EditDomain)getDomain();
	if (type == PaletteRoot.class)
		return ((org.eclipse.ve.internal.cde.core.EditDomain) getDomain()).getPaletteRoot();
	return super.getAdapter(type);
}
/**
 * Do not force creation of the property sheet page
 * This is because the property sheet viewer may not have been created yet (i.e. user didn't want it), so if it has not been created
 * yet then store the listener for when the page may get created
 */
public void addPropertySheetSelectionChangeListener(ISelectionChangedListener aSelectionListener){
	if ( fPropertySheetPage != null ) {
		fPropertySheetPage.addSelectionChangedListener(aSelectionListener);
	}	
	fPropertySheetSelectionListeners.add(aSelectionListener);	// Always add because if property sheet needs to be recreated we need all of them again.
}

public void removePropertySheetSelectionChangelListener(ISelectionChangedListener aSelectionListener){
	if ( fPropertySheetPage != null ) {
		fPropertySheetPage.removeSelectionChangedListener(aSelectionListener);
	}
	fPropertySheetSelectionListeners.remove(aSelectionListener);
}

/**
 * So that users can know if the property sheet has been created.
 * If it hasn't been created (or has been disposed) then they
 * know and they won't call getPropertySheetPage to create it.
 */
public final boolean isPropertySheetCreated() {
	return fPropertySheetPage != null;
}

public final EToolsPropertySheetPage getPropertySheetPage(){
	if ( fPropertySheetPage == null ){
		fPropertySheetPage = createPropertySheetPage();
		if (!fPropertySheetSelectionListeners.isEmpty()){
			Object[] listeners = fPropertySheetSelectionListeners.getListeners();
			for (int i = 0; i < listeners.length; i++) {
				fPropertySheetPage.addSelectionChangedListener((ISelectionChangedListener) listeners[i]);
			}
		}
		
		fPropertySheetPage.setRootEntry(createRootPropertySheetEntry());		
		
		fPropertySheetPage.addListener(new EToolsPropertySheetPage.Listener() {
			/**
			 * @see org.eclipse.ve.internal.propertysheet.EToolsPropertySheetPage.Listener#controlCreated()
			 */
			public void controlCreated(Control control) {
				control.addDisposeListener(new DisposeListener() {
					/**
					 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(DisposeEvent)
					 */
					public void widgetDisposed(DisposeEvent e) {
						fPropertySheetPage = null;	// We no longer have one.
						rootPropertySheetEntry = null;
					}
				});
			}
	
		});
		
	}
	return fPropertySheetPage;
}

protected EToolsPropertySheetPage createPropertySheetPage() {
	return new EToolsPropertySheetPage(){
		public void setActionBars(IActionBars actionBars) {
			super.setActionBars(actionBars);
			// The menu and toolbars have RetargetActions for UNDO and REDO
			// Set an action handler to redirect these to the action registry's actions so they work when the property sheet is enabled
			ActionRegistry registry = getActionRegistry();			
			actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(),registry.getAction(ActionFactory.UNDO.getId()));
			actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(),registry.getAction(ActionFactory.REDO.getId()));
		}		
	};
}

protected AbstractPropertySheetEntry rootPropertySheetEntry;
protected AbstractPropertySheetEntry createRootPropertySheetEntry(){
	rootPropertySheetEntry = new CommandStackPropertySheetEntry(getCommandStack(), null, null);
	return rootPropertySheetEntry;
}

protected CommandStack getCommandStack(){
	return getDomain().getCommandStack();
}

public final DefaultEditDomain getDomain(){
	if (domain == null) {
		domain = createEditDomain();
		// Listen to the command stack so that the first time a command is pushed we make ourselves
		// dirty
		getDomain().getCommandStack().addCommandStackListener(fDirtyListener);
	}
	return domain;
}

public EditPartViewer getPrimaryViewer() {
	return primaryViewer;
}

public EditPartViewer getPaletteViewer() {
	return paletteViewer;
}


public class EMFContentOutlinePage extends ContentOutlinePage {

	private PageBook pageBook;
	private Control outline;
	private Canvas overview;
	private IAction showOverviewAction;
	static final int ID_OUTLINE  = 0;
	static final int ID_OVERVIEW = 1;
	private boolean overviewInitialized;
	private Thumbnail thumbnail;

	public EMFContentOutlinePage(EditPartViewer viewer ){
		super(viewer);
	}
		
	public void setActionBars(IActionBars actionBars) {
		ActionRegistry registry = getActionRegistry();
	
		super.setActionBars(actionBars);

		// The menu and toolbars have RetargetActions for DELETE, UNDO and REDO
		// Set an action handler to redirect these to the action registry's actions so they work
		// with the content outline without having to separately contribute these
		// to the outline page's toolbar
		actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(),getOutlineDeleteAction());
		actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(),registry.getAction(ActionFactory.UNDO.getId()));
		actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(),registry.getAction(ActionFactory.REDO.getId()));		
	}
	
	/*
	 * override for further configuration
	 */
	protected void configureOutlineViewer(){
		getViewer().setEditDomain(getDomain());
		getDomain().addViewer(getViewer());
		getViewer().setKeyHandler(getOutlineKeyHandler());
		
		IToolBarManager tbm = getSite().getActionBars().getToolBarManager();
		showOverviewAction = new Action("", IAction.AS_CHECK_BOX) {//$NON-NLS-1$
			public void run() {
				showPage(isChecked() ? ID_OVERVIEW : ID_OUTLINE);
			}
		};
		showOverviewAction.setImageDescriptor(ImageDescriptor.createFromFile(
								CDEPlugin.class,"images/overview.gif")); //$NON-NLS-1$
		tbm.add(showOverviewAction);
		showPage(ID_OUTLINE);
	}
	
	public void createControl(Composite parent){
		pageBook = new PageBook(parent, SWT.NONE);
		outline = getViewer().createControl(pageBook);
		overview = new Canvas(pageBook, SWT.NONE);
		pageBook.showPage(outline);

		configureOutlineViewer();
		hookOutlineViewer();
		initializeOutlineViewer();
		Control control = getViewer().getControl();
		// A menu listener is added to perform the menuAboutToShow() which will first
		// add the delete action, then the dynamic actions which subclasses can 
		// override via the contextMenuAboutToShow(IMenuManager) method,
		MenuManager menuMgr = createMenuManager(control);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager menuMgr) {
				addToOutlineContextMenu(menuMgr);
				contextMenuAboutToShow(menuMgr,getViewer());
				menuMgr.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
			}
		});
		getSite().registerContextMenu("EMFGraphicalEditorPart.beansViewer", menuMgr, getViewer()); //$NON-NLS-1$
	}
	
	public void dispose(){
		unhookOutlineViewer();
		getDomain().removeViewer(getViewer());		
		super.dispose();
	}
	
	protected void hookOutlineViewer(){
		getSelectionSynchronizer().addViewer(getViewer());
	}

	protected void initializeOutlineViewer(){
		getViewer().setContents(getRootTreeEditPart());
	}
	
	protected void unhookOutlineViewer(){
		getSelectionSynchronizer().removeViewer(getViewer());
	}	

	public Control getControl() {
		return pageBook;
	}

	protected void initializeOverview() {
		LightweightSystem lws = new LightweightSystem(overview);
		RootEditPart rep = primaryViewer.getRootEditPart();
		if (rep instanceof ScalableFreeformRootEditPart) {
			ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart)rep;
			thumbnail = new ScrollableThumbnail((Viewport)root.getFigure());
			thumbnail.setSource(root.getLayer(LayerConstants.PRINTABLE_LAYERS));
			lws.setContents(thumbnail);
		}
	}
	
	protected void showPage(int id) {
		if (id == ID_OUTLINE) {
			showOverviewAction.setChecked(false);
			pageBook.showPage(outline);
			if (thumbnail != null)
				thumbnail.setVisible(false);
		} else if (id == ID_OVERVIEW) {
			if (!overviewInitialized)
				initializeOverview();
			showOverviewAction.setChecked(true);
			pageBook.showPage(overview);
			thumbnail.setVisible(true);
		}
	}
}

/*
 * Cache and return the outline page
 */
protected EMFContentOutlinePage outlinePage;

protected EMFContentOutlinePage getContentOutlinePage(){
	if ( outlinePage == null ) {
		outlinePage = createOutlinePage();
	}
	return outlinePage;
}

protected EMFContentOutlinePage createOutlinePage(){
	return new EMFContentOutlinePage(new org.eclipse.gef.ui.parts.TreeViewer());
}

/**
 * This will get the palette root, or the default, and set it into the EditDomain.
 */
protected final void setPaletteRoot() {
	PaletteRoot palette = getPaletteRoot();
	if ( palette == null ) {
		// Use default CDE pallete.
		try {
			palette = (PaletteRoot) getResourceSet().getEObject(URI.createURI("platform:/plugin/org.eclipse.ve.cde/cde_palette.xmi#cde_palette"), true); //$NON-NLS-1$
		} catch (RuntimeException e) {
			CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getBundle().getSymbolicName(), 0, "", e));//$NON-NLS-1$
		}
	}
	
	// TODO For now we just automatically have all of the drawers closed. Randy may of fixed this so the default is not open for PaletteDrawer.
	closeDrawers(palette);
	
	getDomain().setPaletteRoot(palette);
}
protected String getPaletteHRef(){
	return getConfigurationElement().getAttributeAsIs("palette"); //$NON-NLS-1$
}
/**
 * Return the root of the palette to use for this
 * EditorPart. It may be changed at later time by
 * getting the domain and setting it in there.
 */
protected PaletteRoot getPaletteRoot() {
	String paletteHRef = getPaletteHRef();
	if ( paletteHRef != null ) {
		try {
			Root ref = (Root) getResourceSet().getEObject(URI.createURI(paletteHRef), true);
			return (PaletteRoot) ref.getEntry();
		} catch (RuntimeException e) {
			CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getBundle().getSymbolicName(), 0, "", e));//$NON-NLS-1$
		}
	}	
	
	return null;
}

protected void closeDrawers(PaletteContainer ctr) {
	if (ctr instanceof PaletteDrawer)
		((PaletteDrawer) ctr).setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
	java.util.List c = ctr.getChildren();
	for (int i=0; i<c.size(); i++) {
		PaletteEntry e = (PaletteEntry) c.get(i);
		if (e instanceof PaletteContainer)
			closeDrawers((PaletteContainer) e);
	}
}
/**
 * Starting up with an input.
 */
public void init(IEditorSite site, IEditorInput input) 
	throws PartInitException 
{
	// Check input.
	if (!(input instanceof IFileEditorInput))
		throw new PartInitException(MessageFormat.format(CDEMessages.NOT_FILE_INPUT_ERROR_, new Object[] {input.getName()})); 

	// Save input.
	setSite(site);
	initialize();
	setInput(input);
}

private IPartListener partListener;
protected void initialize() {
	initActionRegistry();
	initViewers();
	if (wantResourceListener) {
		// If an open, unsaved file was deleted, query the user to either do a "Save As"
		// or close the editor.		
		partListener = new IPartListener() {
			public void partActivated(IWorkbenchPart part) {
				if (part != EMFGraphicalEditorPart.this)
					return;
				if (!((IFileEditorInput) getEditorInput()).getFile().exists()) {
					Shell shell = getSite().getShell();
					String title = CDEEmfMessages.FILE_DELETED_TITLE_UI_; 
					String message = CDEEmfMessages.FILE_DELETED_WITHOUT_SAVE_INFO_; 
					String[] buttons = { CDEEmfMessages.SAVE_BUTTON_UI_, CDEEmfMessages.CLOSE_BUTTON_UI}; 
					MessageDialog dialog = new MessageDialog(shell, title, null, message, MessageDialog.QUESTION, buttons, 0);
					if (dialog.open() == 0) {
						if (!performSaveAs())
							partActivated(part);
					} else {
						closeEditor(false);
					}
				}
			}
			public void partBroughtToTop(IWorkbenchPart part) {
			}
			public void partClosed(IWorkbenchPart part) {
			}
			public void partDeactivated(IWorkbenchPart part) {
			}
			public void partOpened(IWorkbenchPart part) {
			}
		};
		getSite().getWorkbenchWindow().getPartService().addPartListener(partListener);
	}
}

public boolean isDirty() {
	return fDirtyState;
}

public boolean isSaveAsAllowed() {
	return true;
}

protected void initActionRegistry(){
	getActionRegistry(); // Force lazy init of the ActionRegistry
	actionRegistry.registerAction(new UndoAction(this));
	actionRegistry.registerAction(new RedoAction(this));
	actionRegistry.registerAction(new org.eclipse.gef.ui.actions.SaveAction(this));

	actionRegistry.registerAction(new org.eclipse.ve.internal.cde.core.ZoomAction(this));
	actionRegistry.registerAction(new org.eclipse.ve.internal.cde.core.ZoomInAction(this));
	actionRegistry.registerAction(new org.eclipse.ve.internal.cde.core.ZoomOutAction(this));
	
	org.eclipse.ve.internal.cde.core.ShowGridAction sgAction = new org.eclipse.ve.internal.cde.core.ShowGridAction(this);
	actionRegistry.registerAction(sgAction);
	actionRegistry.registerAction(new org.eclipse.ve.internal.cde.core.GridPropertiesAction(this, sgAction));
	actionRegistry.registerAction(new org.eclipse.ve.internal.cde.core.SnapToGridAction(this, sgAction));	
}

protected void initViewers(){
	createPrimaryViewer();
	getSite().setSelectionProvider(getPrimaryViewer());
	// Keep the graph view and tree view in sync
	getSelectionSynchronizer().addViewer(getPrimaryViewer());
}
protected SelectionSynchronizer getSelectionSynchronizer(){
	if (selectionSynchronizer == null)
		selectionSynchronizer = new SelectionSynchronizer();
	return selectionSynchronizer;
}

public void setActionRegistry(ActionRegistry registry) {
	actionRegistry = registry;
}

public void setFocus(){
	Control c = getPrimaryViewer().getControl();
	if (c != null && !c.isDisposed()) {
		// TODO Remove the following "if block" after a MOF bug is fixed. See resourceListener for details.
		if (needToCloseEditor) {
			String msg = java.text.MessageFormat.format(CDEEmfMessages.Resource_changed_please_close_editor_message_WARN_,  
						new Object[] {filenameOfChangedResource});
			Shell shell= getSite().getShell();
			MessageDialog.openWarning(shell, CDEEmfMessages.Warning_message_dialog_title_WARN_, msg); 
			needToCloseEditor = false;
			return;
		}
		c.setFocus();
	}
}

/**
 * Set the dirty state. Only signal the first time it goes dirty until
 * it becomes clean.
 */
protected void setDirty(boolean dirty) {
	// If being set clean, signal. Else if not already dirty, then signal.
	if (!dirty || !fDirtyState) {
		fDirtyState = dirty;
		firePropertyChange(PROP_DIRTY);
	}
}

/**
 * Create and return an empty document.  This 
 * method should be implemented to create a new document
 * and a new root within the document. It should be
 * created within the given ResourceSet.
 * 
 * NOTE: There really shouldn't be empty resources, they
 * should be created correctly by wizards, but as a fallback
 * this method is available.
 */
protected abstract Resource createEmptyResource(String filename, ResourceSet rset);


/**
 * Get the base Model object of the resource being edited. This will open
 * the file.
 */
protected EObject getModel(IFile file) throws CoreException, IOException, Exception {

	EObject rootModel = null;
	InputStream is = null;
	try {
		is = file.getContents();
	} catch (CoreException exc) {
		// look for the specific case where the file is not in sync
		if (exc.getStatus().getCode() == IResourceStatus.OUT_OF_SYNC_LOCAL) {
			// refresh from local and try it again
			file.refreshLocal(IResource.DEPTH_ZERO, null);
			is = file.getContents();
		} else {
			throw exc;
		}
	}
	try {
		if (is.available() == 0) {  // New file
			fResource = createEmptyResource(file.getLocation().toString(), getResourceSet());
		} else {
			fResource = readDocument(file.getFullPath().toString(), is);
		}
		if (fResource != null) {
			rootModel = getModel(fResource);
		}
	} finally {
		try {
			is.close();	// Make sure the stream is closed
		} catch (IOException e) {
		}
	}
	
	return rootModel;
}

/**
 * Subclasses need to call this when they get the DiagramData.
 */
protected void setupDiagramData(DiagramData dd) {
	((org.eclipse.ve.internal.cde.core.EditDomain) getDomain()).setDiagramData(dd);
	Diagram d = null;
	Iterator iter = dd.getDiagrams().iterator();
	while (iter.hasNext()) {
		Diagram element = (Diagram) iter.next();
		if (Diagram.PRIMARY_DIAGRAM_ID.equals(element.getId())) {
			d = element;
			break;
		}
	}
	if (d != null)
		((org.eclipse.ve.internal.cde.core.EditDomain) getDomain()).setViewerData(getPrimaryViewer(), org.eclipse.ve.internal.cde.core.EditDomain.DIAGRAM_KEY, d);	// Put out the diagram for the viewer.

}

/**
 * The File has been parsed into a document which is the argument
 * Return the EObject you want to be the model of the base EditPart
 */
protected abstract EObject getModel(Resource doc);

/**
 * The EObject argument is the base object returned by
 * getModel(Resource).  Return the edit part you want to be used
 * as the base ( top level ) edit part by the graph viewer
 */
protected abstract EditPart getEditPart(EObject rootModel);

/**
 * The EObject argument is the base object returned by
 * getModel(Resource).  Return the tree edit part you want to be used
 * as the base ( top level ) tree edit part by the outline viewer
 */
protected abstract EditPart getTreeEditPart(EObject rootModel);


/**
 * Return the resourceSet. This is public so that it can be retrieved
 * by others.
 */
public final ResourceSet getResourceSet() {
	if (fResourceSet == null) {
		fResourceSet = createResourceSet();
		initializeResourceSet(fResourceSet);
	}
	return fResourceSet;
}

/**
 * Return the resource. This is public so that it can be retrievd by others.
 */
public final Resource getResource() {
	return fResource;
}



/**
 * Create the ResourceSet to use and initialize it
 */
protected ResourceSet createResourceSet() {
//	ResourceSet rs = new HierarchicalResourceSet();
	ResourceSet rs = new ResourceSetImpl();
//	context.setURIConverter(new EMFWorkbenchURIConverterImpl());
// TODO Not sure what to use now for uri converter. Check w/Dan later.
//	context.setURIConverter(new WorkbenchURIConverterImpl());
//	rs.setContext(context);
	return rs;
}

protected void initializeResourceSet(ResourceSet rs){
// TODO Don't have contexts any more, we aren't using the global at the moment anyway.
//	rs.getContext().setParent(getGlobalResourceSet().getContext());
	EMFEditDomainHelper.setResourceSet(rs,(org.eclipse.ve.internal.cde.core.EditDomain)getDomain());	
}	
	
/**
 * The global resourceSet is where we store resources that are to be available
 * to all MOF Editors.
 */
private static ResourceSet sGlobalResourceSet;
public final static ResourceSet getGlobalResourceSet(){
	if (sGlobalResourceSet == null) {
		sGlobalResourceSet = new ResourceSetImpl();
	}
	return sGlobalResourceSet;
}


protected Resource  readDocument(String uri, InputStream is) throws Exception { 
	Resource res = null;
	// If an error occurs reading or loading the xmi file,
	// the handle the exception and create markers for the errors.
	res = getResourceSet().createResource(URI.createURI(uri));
	res.load(is, Collections.EMPTY_MAP);
	// TODO Errors during loading are handled differently now.
	return res;
}
/**
 * Return the error message to be displayed in the viewer when there are
 * errors found parsing the document.
 * Subclasses can override with their own specific error message.
 */
protected String getErrorTextMessage() {
	return CDEEmfMessages.Error_text_message_ERROR_; 
}
//protected String getErrorMessageFrom(WarningException exc){
//	String msg = ""; //$NON-NLS-1$
//	if ( exc instanceof BadTypeException ){
//		msg = MessageFormat.format(CDEEmfMessages.getString("Warning_exception_bad_type_EXC_"),  //$NON-NLS-1$
//				new Object[] {((BadTypeException)exc).getValue()});
//	} else if ( exc instanceof BadValueException ) {
//		msg = MessageFormat.format(CDEEmfMessages.getString("Warning_exception_bad_value_EXC_"),  //$NON-NLS-1$
//				new Object[] {((BadValueException)exc).getValue()});
//	} else if ( exc instanceof com.ibm.xmi.base.ClassNotFoundException ) {
//		msg = MessageFormat.format(CDEEmfMessages.getString("Warning_exception_missing_class_EXC_"),  //$NON-NLS-1$
//				new Object[] {((com.ibm.xmi.base.ClassNotFoundException)exc).getName()});
//	} else if ( exc instanceof FeatureNotFoundException ) {
//		msg = MessageFormat.format(CDEEmfMessages.getString("Warning_exception_missing_feature_EXC_"),  //$NON-NLS-1$
//				new Object[] {((FeatureNotFoundException)exc).getName()});
//	} else if ( exc instanceof NamespaceNotFoundException ) {
//		msg = MessageFormat.format(CDEEmfMessages.getString("Warning_exception_missing_namespace_EXC_"),  //$NON-NLS-1$
//				new Object[] {((NamespaceNotFoundException)exc).getName()});
//	} else if ( exc instanceof PackageNotFoundException ) {
//		msg = MessageFormat.format(CDEEmfMessages.getString("Warning_exception_missing_package_EXC_"),  //$NON-NLS-1$
//				new Object[] {((PackageNotFoundException)exc).getPrefix()});
//	} else if ( exc instanceof UnresolvedReferenceException ) {
//		msg = MessageFormat.format(CDEEmfMessages.getString("Warning_exception_bad_reference_EXC_"),  //$NON-NLS-1$
//				new Object[] {((UnresolvedReferenceException)exc).getId()});
//	} else {
//		msg = MessageFormat.format(CDEEmfMessages.getString("Warning_exception_error_EXC_"),  //$NON-NLS-1$
//				new Object[] {exc.getLocalizedMessage()});
//	}
//	return msg;
//}
/**
 * Subclasses need to handle when a marker is selected from the task list.
 * Basically, find the edit part for this model and select it so the user
 * can correct the problem.
 */
public abstract void gotoMarker(IMarker marker);

/**
 * Subclasses need to handle when a resource changes.
 */
protected abstract void handleResourceChanged();

/**
 * Save the current resource back out.
 */
public void doSave(IProgressMonitor monitor) {
	// If the underlying editor input is null, or the resource file does not exist, do a 
	// "Save As" instead.
	if( getEditorInput() == null || ((IFileEditorInput)getEditorInput()).getFile() == null ||
			!((IFileEditorInput)getEditorInput()).getFile().exists() ){
		if( isSaveAsAllowed() )
			doSaveAs();
		// What should be done if "Save As" is not allowed?
		// Pop up an error message?
		return;
	}
	
	save(monitor);
}	

private static final Map XML_TEXT_OPTIONS;
static {
	XML_TEXT_OPTIONS = new HashMap(2);
	XML_TEXT_OPTIONS.put(XMLResource.OPTION_PROCESS_DANGLING_HREF, XMLResource.OPTION_PROCESS_DANGLING_HREF_RECORD);
	XML_TEXT_OPTIONS.put(XMLResource.OPTION_LINE_WIDTH, new Integer(100));
}
/**
 * Perform the save.
 */
protected void save(IProgressMonitor monitor) {
	
	org.eclipse.ve.internal.cde.core.EditDomain dom = (org.eclipse.ve.internal.cde.core.EditDomain) getDomain();	
	org.eclipse.gef.commands.Command cmd = dom.getAnnotationLinkagePolicy().cleanupDiagramData(dom.getDiagramData());
	if (cmd != null)
		cmd.execute();
		
	WorkspaceModifyOperation op= new WorkspaceModifyOperation() {	
		public void execute(final IProgressMonitor monitor) throws CoreException {		
			monitor.beginTask(CDEMessages.SAVING_UI_, 2000); 
			try {		
				IFile file = ((IFileEditorInput) getEditorInput()).getFile();
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				try {
					EObject rootmodel = getModel(fResource);
					if (rootmodel != null) {
						validateModel(rootmodel);
					}
					fResource.save(os, XML_TEXT_OPTIONS);
				} catch (Exception e) {
					throw new CoreException(new Status(IStatus.ERROR, CDEPlugin.getPlugin().getBundle().getSymbolicName(), 0, MessageFormat.format(CDEMessages.SAVE_FAIL_ERROR_, new Object[] {file.getFullPath().toString()}), e));		 
				}
				monitor.worked(1000);
				InputStream stream= new ByteArrayInputStream(os.toByteArray());
				file.setContents(stream, false, true, new SubProgressMonitor(monitor, 1000));				
			} catch (Exception e) {
				throw new CoreException(new Status(IStatus.ERROR, CDEPlugin.getPlugin().getBundle().getSymbolicName(), 0, MessageFormat.format(CDEMessages.SAVE_FAIL_ERROR_, new Object[] {fResource.getURI().toString()}), e)); 
			}
			setDirty(false);
			monitor.done();
		}
	};
	
	try {
		op.run(monitor);
	} catch (InterruptedException e) {
	} catch (InvocationTargetException e) {
		Throwable eReal = e.getTargetException();
		IStatus status = null;
		if (eReal instanceof CoreException) {
			status = ((CoreException) eReal).getStatus();
		} else {
			status = new Status(IStatus.ERROR, CDEPlugin.getPlugin().getBundle().getSymbolicName(), 0, MessageFormat.format(CDEMessages.SAVE_FAIL_ERROR_, new Object[] {fResource.getURI().toString()}), eReal); 
		}
		CDEPlugin.getPlugin().getLog().log(status);
		showErrorMessage(status.getMessage());
	}
	
	

}

/**
 * Select a new name to save it as, and then save it.
 */
public void doSaveAs() {
	performSaveAs();
}

/**
 * Returns a boolean indicating whether the user decided to save, or to
 * cancel.  True, if saved; false otherwise.
 */
protected boolean performSaveAs(){
	Shell shell= getSite().getShell();

	SaveAsDialog dialog= new SaveAsDialog(shell);
	dialog.setOriginalFile(((IFileEditorInput) getEditorInput()).getFile());
	dialog.open();
	IPath path= dialog.getResult();
	if (path == null)
		return false;

	final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
	
	org.eclipse.ve.internal.cde.core.EditDomain dom = (org.eclipse.ve.internal.cde.core.EditDomain) getDomain();
	org.eclipse.gef.commands.Command cmd = dom.getAnnotationLinkagePolicy().cleanupDiagramData(dom.getDiagramData());
	if (cmd != null)
		cmd.execute();

	WorkspaceModifyOperation op= new WorkspaceModifyOperation() {
		public void execute(final IProgressMonitor monitor) throws CoreException {
			monitor.beginTask(CDEMessages.SAVING_AS_UI_, 2000); 
			saveAs(new SubProgressMonitor(monitor,1000), file, fResource);
			setInput(new FileEditorInput(file));
			monitor.done();			
		}
	};

	try {
		PlatformUI.getWorkbench().getProgressService().run(false, false, op); // Not cancelable.
	} catch (InterruptedException e) {
	} catch (InvocationTargetException e) {
		Throwable eReal = e.getTargetException();
		IStatus status = null;
		if (eReal instanceof CoreException) {
			status = ((CoreException) eReal).getStatus();
		} else {
			status = new Status(IStatus.ERROR, CDEPlugin.getPlugin().getBundle().getSymbolicName(), 0, MessageFormat.format(CDEMessages.SAVE_FAIL_ERROR_, new Object[] {path.toString()}), eReal); 
		}
		CDEPlugin.getPlugin().getLog().log(status);
		showErrorMessage(status.getMessage());
	}
	return true;
}

protected void saveAs(IProgressMonitor monitor, IFile file, Resource resource) throws CoreException {
	monitor.beginTask("", 3000); //$NON-NLS-1$
	ByteArrayOutputStream os = new ByteArrayOutputStream();
	try {
		resource.save(os, XML_TEXT_OPTIONS);
	} catch (Exception e) {
		throw new CoreException(new Status(IStatus.ERROR, CDEPlugin.getPlugin().getBundle().getSymbolicName(), 0, MessageFormat.format(CDEMessages.SAVE_FAIL_ERROR_, new Object[] {file.getFullPath().toString()}), e));		 
	}
	
	InputStream stream= new ByteArrayInputStream(os.toByteArray());
	if (file.exists()) {
		file.setContents(stream, false, true, new SubProgressMonitor(monitor, 2000));
	} else {
		ContainerGenerator locator= new ContainerGenerator(file.getParent().getFullPath());
		locator.generateContainer(new SubProgressMonitor(monitor, 1000));
		file.create(stream, false, new SubProgressMonitor(monitor, 1000));
	}
	
	monitor.done();
}

/*
 * Called by outline page to set the contents
 */
protected EditPart getRootTreeEditPart() {
	return getTreeEditPart(getRootModel());	
}

private EObject rootModel;
protected EObject getRootModel() {
	return rootModel;
}

/**
 * A new input has been entered for this editpart. It can be assumed that it is
 * an IFileEditorInput because that must be tested for within the init(...) method
 * override.
 */
protected void setInput(IEditorInput input) {
	if (!input.equals(getEditorInput())) {
		superSetInput(input);
		IFileEditorInput fileInput = (IFileEditorInput) input;
		IFile file = fileInput.getFile();
		try { 
			Resource oldResource = fResource;	// Need to save it so that it can be unloaded after new file put into the viewer.
			// Get the model. If any severe problems occur, markers are created 
			// via the exceptions thrown (see handleException(Exception, IFile)).
			
			org.eclipse.ve.internal.cde.core.EditDomain dom = (org.eclipse.ve.internal.cde.core.EditDomain) getDomain();
			// Need to release the old diagram data.
			dom.setDiagramData(null);
			getResourceSet();	// Initialize the resource set
			
			rootModel = getModel(file);
			// It is important that if any exceptions are thrown that this method continues through to get a root edit part and then
			// sets it as the contents on the edit part.  If not the user sees a grey screen.
			// The kind of exceptions that might be thrown are generally found in some kind of adapter that comes across a code pattern it 
			// can't recognize.  Rather than give the user a grey screen give them one a white one that may be recoverable
			try { 
				initializeWithNewRoot(rootModel);
			} catch (Exception exc){
				CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getBundle().getSymbolicName(), 0, "", exc));	//$NON-NLS-1$
			}
			if (rootModel != null) {
				EditPart baseEditPart = getEditPart(rootModel);
				getPrimaryViewer().setContents(baseEditPart);
				// Validate the model. It's here that the editorpart 
				// should create any errors as markers to be shown
				// in the task list.
				validateModel(rootModel);
				getCommandStack().flush();	// Clear the command stack
				if (oldResource != null)
					getResourceSet().getResources().remove(oldResource);
			}
			
		} catch (Exception exc) {
			CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getBundle().getSymbolicName(), 0, "", exc)); //$NON-NLS-1$
		}
	}
	// This has been moved out of the if statement so as to handle the case
	// when the user saves a file as itself.
	setDirty(false);	// Mark not dirty
}

/*
 * A new root model has been retrieved. This is called just before
 * the model is wrappered within edit parts.
 */
protected void initializeWithNewRoot(EObject newModel){
}

protected void showErrorMessage( String message ) {
	Shell shell= getSite().getShell();
	MessageDialog.openError(shell, CDEMessages.ERROR_TITLE_UI_, message); 
}

// This method is mainly a result of code-factoring.  Also, there was a need to call
// super.setInput() from one of the inner classes (ResourceTracker).
protected void superSetInput( IEditorInput input ){
	
	// The workspace never changes for an editor.  So, removing and re-adding the 
	// resourceListener is not necessary.  But it is being done here for the sake
	// of proper implementation.  Plus, the resourceListener needs to be added 
	// to the workspace the first time around.
	if( getEditorInput() != null && resourceListener != null ){
		IFile file = ((IFileEditorInput)getEditorInput()).getFile();
		file.getWorkspace().removeResourceChangeListener( resourceListener );
	}
	
	super.setInput( input );
	
	if( getEditorInput() != null ){
		IFile file = ((IFileEditorInput)getEditorInput()).getFile();
		if (wantResourceListener) {
			if (resourceListener == null)
				resourceListener =  new ResourceTracker();
			file.getWorkspace().addResourceChangeListener( resourceListener );
		}
		setPartName( file.getName() );
	}
}
/**
 * Validate the model. It's here that subclasses should create any soft (or severe)
 * errors as markers to be shown in the task list. 
 *
 * Note: Plugin specific markers can be used here to allow editor parts to create 
 *       and manage only those markers (errors) that are relevent to them.
 */
protected abstract void validateModel(EObject model);
/**
 * This code is temporary and should be removed once a MOF bug is fixed.
 * See the resourceTracker for more information.
 */
protected void createWarningMessage(String filename) {
    // TODO Remove this method after a MOF bug is fixed. See resourceListener for details.
	needToCloseEditor = true;
	filenameOfChangedResource = filename;
}
}
