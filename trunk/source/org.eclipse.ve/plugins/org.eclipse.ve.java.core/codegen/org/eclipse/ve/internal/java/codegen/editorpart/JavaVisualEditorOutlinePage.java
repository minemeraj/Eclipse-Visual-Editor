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
 *  $RCSfile: JavaVisualEditorOutlinePage.java,v $
 *  $Revision: 1.30 $  $Date: 2005-10-26 20:41:55 $ 
 */
package org.eclipse.ve.internal.java.codegen.editorpart;

import java.net.URL;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.gef.*;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jdt.ui.IContextMenuConstants;
import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.texteditor.ResourceAction;
import org.eclipse.ui.texteditor.StatusLineContributionItem;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.core.CustomizeLayoutWindowAction;
import org.eclipse.ve.internal.cde.emf.ClassDescriptorDecoratorPolicy;
import org.eclipse.ve.internal.cde.emf.DefaultTreeEditPartFactory;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.vce.SubclassCompositionComponentsTreeEditPart;
import org.eclipse.ve.internal.java.vce.VCEPreferences;
 
/*
 * Java Outline Page (BeansList)
 * <p>
 * <package-protected> because not meant to be accessed outside of the Java visual editor.
 * 
 * @since 1.0.0
 */
public class JavaVisualEditorOutlinePage extends ContentOutlinePage {
	// dbk cache IMG_COLLAPSE_ALL / IMG_COLLAPSE_ALL_DISABLED
	private static final ImageDescriptor IMG_COLLAPSE_ALL = getUIImageDescriptor("elcl16/collapseall.gif"); //$NON-NLS-1$
	private static final ImageDescriptor IMG_COLLAPSE_ALL_DISABLED = getUIImageDescriptor("dlcl16/collapseall.gif"); //$NON-NLS-1$
	private static final ImageDescriptor IMG_SELECTION_SYNC = getUIImageDescriptor("elcl16/synced.gif"); //$NON-NLS-1$
	private static final ImageDescriptor IMG_SELECTION_SYNC_DISABLED = getUIImageDescriptor("dlcl16/synced.gif"); //$NON-NLS-1$
	private static final String TITLE = CodegenEditorPartMessages.CollapseAllAction_label;
	private static final String TOOL_TIP = CodegenEditorPartMessages.CollapseAllAction_toolTip;
	
	private class ShowOverviewAction extends ResourceAction {
		private static final String RESOURCE_PREFIX = "ShowOverviewAction_"; //$NON-NLS-1$

		public ShowOverviewAction() {
			super(JavaVisualEditorPart.RESOURCE_BUNDLE, RESOURCE_PREFIX, IAction.AS_CHECK_BOX);
			setChecked(CDEPlugin.getPlugin().getPluginPreferences().getBoolean(CDEPlugin.PREF_SHOW_OVERVIEW_KEY));			
		}			

		public void run() {
			if (isChecked()) {
				setText(CodegenEditorPartMessages.ShowOverviewAction_labelOutline); //$NON-NLS-1$
				setToolTipText(CodegenEditorPartMessages.ShowOverviewAction_tooltipOutline); //$NON-NLS-1$
			} else {
				setText(CodegenEditorPartMessages.ShowOverviewAction_label); //$NON-NLS-1$
				setToolTipText(CodegenEditorPartMessages.ShowOverviewAction_tooltip);					 //$NON-NLS-1$
			}
			showOverview(isChecked());
			CDEPlugin.getPlugin().getPluginPreferences().setValue(CDEPlugin.PREF_SHOW_OVERVIEW_KEY, isChecked());
		}

	}
	
	/*
	 * Copied from Resource navigator to do the same thing.
	 */
	static protected ImageDescriptor getUIImageDescriptor(String relativePath) { 
		URL url = Platform.getBundle(PlatformUI.PLUGIN_ID).getEntry("icons/full/" + relativePath);	//$NON-NLS-1$
		return url != null ? ImageDescriptor.createFromURL(url) : ImageDescriptor.getMissingImageDescriptor();
	}
	
	private class LinkSourceSelectionAction extends Action{
		public LinkSourceSelectionAction(){
			super(CodegenEditorPartMessages.JavaVisualEditorOutlinePage_LinkSelectionSourceEditor_Action_Label, IAction.AS_CHECK_BOX);
			setToolTipText(CodegenEditorPartMessages.JavaVisualEditorOutlinePage_LinkSelectionSourceEditor_Action_Label);
			setImageDescriptor(IMG_SELECTION_SYNC);
			setDisabledImageDescriptor(IMG_SELECTION_SYNC_DISABLED);			
		}
		
		public void run() {
			if(isChecked())
				jve.installCodegenSelectionListener();
			else
				jve.uninstallCodegenSelectionListener();
		}
	}
	
	private class CollapseAllAction extends Action {

		public CollapseAllAction() {
			super(TITLE, IAction.AS_PUSH_BUTTON);
			setToolTipText(TOOL_TIP);
			setImageDescriptor(IMG_COLLAPSE_ALL);
			setDisabledImageDescriptor(IMG_COLLAPSE_ALL_DISABLED);			
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
				jve.editDomain.setData(JavaVEPlugin.SHOW_EVENTS, new Integer(fStyle));
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
	

	public JavaVisualEditorPart jve;
	private PageBook pageBook;
	private Control outline;
	private Canvas overview;
	private Thumbnail thumbnail;		
	private ShowOverviewAction showOverviewAction;
	private CollapseAllAction collapseAllAction;
	private LinkSourceSelectionAction linkSourceSelectionAction;
	private DeleteAction deleteAction;
	private CutJavaBeanAction cutBeanAction;
	private CopyJavaBeanAction copyBeanAction;
	private PasteJavaBeanAction pasteBeanAction;
	
	// The jve status field for when property sheet is in focus. It will be kept up to date through
	// updateStatusField method in JavaVisualEditorPart. 
	protected StatusLineContributionItem jveStatusField;

	public JavaVisualEditorOutlinePage(JavaVisualEditorPart jve, EditPartViewer viewer ){
		super(viewer);
		this.jve = jve;
	}

	public void init(IPageSite pageSite) {
		super.init(pageSite);
		
		IActionBars actionBars = pageSite.getActionBars();
		
		// Create the status field and put on status line.
		jveStatusField = new StatusLineContributionItem(JavaVisualEditorActionContributor.STATUS_FIELD_CATEGORY);
		jveStatusField.setActionHandler(jve.getAction(ReloadNowAction.RELOADNOW_ACTION_ID));
		actionBars.getStatusLineManager().add(jveStatusField);
		jve.updateStatusField(JavaVisualEditorActionContributor.STATUS_FIELD_CATEGORY);	// So that it gets the latest settings.
		
		
		// The menu and toolbars have RetargetActions for DELETE, UNDO and REDO
		// Set an action handler to redirect these to the action registry's actions so they work
		// with the content outline without having to separately contribute these
		// to the outline page's toolbar
		deleteAction = new DeleteAction((IWorkbenchPart)jve);
		copyBeanAction = new CopyJavaBeanAction(jve);
		pasteBeanAction = new PasteJavaBeanAction(jve,jve.editDomain);
		pasteBeanAction.executeImmediately = true;
		
		ISharedImages images = PlatformUI.getWorkbench().getSharedImages();		
		
		copyBeanAction = new CopyJavaBeanAction(jve){
			public void run(){
				super.run();
				// When the clipboard puts stuff into the clipboard the paste action needs to refresh
				// whether it should or shouldn't be enabled
				pasteBeanAction.update();
			}		
		};
		copyBeanAction.setSelectionProvider(this);
		copyBeanAction.setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		copyBeanAction.setText(CodegenEditorPartMessages.Action_Copy_Label);		
		
		cutBeanAction = new CutJavaBeanAction(jve){
			public void run(){
				super.run();
				// When the clipboard puts stuff into the clipboard the paste action needs to refresh
				// whether it should or shouldn't be enabled
				pasteBeanAction.update();
			}
		};		
		cutBeanAction.setSelectionProvider(this);
		cutBeanAction.setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		cutBeanAction.setText(CodegenEditorPartMessages.Action_Cut_Label);		
		
		pasteBeanAction.setSelectionProvider(this);
		pasteBeanAction.setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		pasteBeanAction.setText(CodegenEditorPartMessages.Action_Paste_Label);	
		
		actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);
		actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(), cutBeanAction);	
		actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), copyBeanAction);				
		actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), pasteBeanAction);		
		
		actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), jve.getAction(ActionFactory.UNDO.getId()));
		actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), jve.getAction(ActionFactory.REDO.getId()));
		actionBars.setGlobalActionHandler(JavaVisualEditorActionContributor.PALETTE_SELECTION_ACTION_ID, jve.getAction(JavaVisualEditorActionContributor.PALETTE_SELECTION_ACTION_ID));
		actionBars.setGlobalActionHandler(JavaVisualEditorActionContributor.PALETTE_MARQUEE_SELECTION_ACTION_ID, jve.getAction(JavaVisualEditorActionContributor.PALETTE_MARQUEE_SELECTION_ACTION_ID));
		actionBars.setGlobalActionHandler(JavaVisualEditorActionContributor.PALETTE_DROPDOWN_ACTION_ID, jve.getAction(JavaVisualEditorActionContributor.PALETTE_DROPDOWN_ACTION_ID));
		actionBars.setGlobalActionHandler(CustomizeJavaBeanAction.ACTION_ID, jve.getAction(CustomizeJavaBeanAction.ACTION_ID));
		actionBars.setGlobalActionHandler(JavaVisualEditorReloadActionController.RELOAD_ACTION_ID, jve.getAction(JavaVisualEditorReloadActionController.RELOAD_ACTION_ID));
		actionBars.setGlobalActionHandler(CustomizeLayoutWindowAction.ACTION_ID, jve.getEditorSite().getActionBars().getGlobalActionHandler(CustomizeLayoutWindowAction.ACTION_ID));

		IToolBarManager tbm = actionBars.getToolBarManager();
		linkSourceSelectionAction = new LinkSourceSelectionAction();
		linkSourceSelectionAction.setChecked(JavaVEPlugin.getPlugin().getPluginPreferences().getBoolean(VCEPreferences.CODEGEN_CARET_SELECT_UI_KEY));
		tbm.add(linkSourceSelectionAction);
		collapseAllAction = new CollapseAllAction();
		tbm.add(collapseAllAction);
		showOverviewAction = new ShowOverviewAction();
		tbm.add(showOverviewAction);
		
		IMenuManager mm = actionBars.getMenuManager();
		int showEvents = 0;
		Integer showEventsInt = (Integer) jve.editDomain.getData(JavaVEPlugin.SHOW_EVENTS);
		if (showEventsInt == null) {
			// First beans list for this editor, so get it out of preferences
			showEvents = JavaVEPlugin.getPlugin().getPluginPreferences().getInt(JavaVEPlugin.SHOW_EVENTS);
			jve.editDomain.setData(JavaVEPlugin.SHOW_EVENTS, new Integer(showEvents));	// Save current style for this editor.
		} else {
			// We've closed this beanslist once already, so reuse the setting at the time of the close.
			showEvents = showEventsInt.intValue();
		}
		// Create three actions - No events , Basic events and Expert events
		ShowHideEventsAction noEventsAction = new ShowHideEventsAction(CodegenEditorPartMessages.JavaVisualEditor_NoEvents,JavaVEPlugin.EVENTS_NONE, showEvents); 
		ShowHideEventsAction basicEventsAction = new ShowHideEventsAction(CodegenEditorPartMessages.JavaVisualEditor_ShowEvents,JavaVEPlugin.EVENTS_BASIC, showEvents); 
		ShowHideEventsAction expertEventsAction = new ShowHideEventsAction(CodegenEditorPartMessages.JavaVisualEditor_ExpertEvents,JavaVEPlugin.EVENTS_EXPERT, showEvents); 
		// Put the event actions - None, Basic and Export on the menu	
		mm.add(noEventsAction);
		mm.add(basicEventsAction);
		mm.add(expertEventsAction);		
	}

	public void createControl(Composite parent){
		pageBook = new PageBook(parent, SWT.NONE);
		pageBook.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				jve.beansListPage = null;
			}
		});
		
		outline = getViewer().createControl(pageBook);

		jve.editDomain.addViewer(getViewer());
		KeyHandler outlineKeyHandler = new KeyHandler();
		outlineKeyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0), deleteAction);			
		getViewer().setKeyHandler(outlineKeyHandler);
		jve.getSelectionSynchronizer().addViewer(getViewer());
		getViewer().setEditPartFactory(new DefaultTreeEditPartFactory(ClassDescriptorDecoratorPolicy.getPolicy(jve.editDomain)));
					
		getViewer().setContents(new SubclassCompositionComponentsTreeEditPart(jve.modelReady ? jve.modelBuilder.getModelRoot() : null));			
		
		Control control = getViewer().getControl();
		
		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		Menu menu = menuMgr.createContextMenu(control);
		control.setMenu(menu);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager menuMgr) {					
				GEFActionConstants.addStandardActionGroups(menuMgr);
				menuMgr.appendToGroup(GEFActionConstants.GROUP_UNDO, new Separator(IContextMenuConstants.GROUP_OPEN));
				jve.openActionGroup.setContext(new ActionContext(getViewer().getSelection()));
				jve.openActionGroup.fillContextMenu(menuMgr);
				jve.openActionGroup.setContext(null);					
				menuMgr.appendToGroup(GEFActionConstants.GROUP_UNDO, jve.getAction(ActionFactory.UNDO.getId()));
				menuMgr.appendToGroup(GEFActionConstants.GROUP_UNDO, jve.getAction(ActionFactory.REDO.getId()));
				menuMgr.appendToGroup(GEFActionConstants.GROUP_COPY, cutBeanAction);				
				menuMgr.appendToGroup(GEFActionConstants.GROUP_COPY, copyBeanAction);				
				menuMgr.appendToGroup(GEFActionConstants.GROUP_COPY, pasteBeanAction);					
				menuMgr.appendToGroup(GEFActionConstants.GROUP_EDIT, deleteAction);
				IAction customize = jve.graphicalActionRegistry.getAction(CustomizeJavaBeanAction.ACTION_ID);
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
				copyBeanAction.update();
				cutBeanAction.update();
				pasteBeanAction.update();
			}
		});
	}

	public void dispose(){
		jve.getSelectionSynchronizer().removeViewer(getViewer());
		jve.editDomain.removeViewer(getViewer());		
		super.dispose();
	}

	public Control getControl() {
		return pageBook;
	}

	protected void initializeOverview() {
		overview = new Canvas(pageBook, SWT.NONE);			
		LightweightSystem lws = new LightweightSystem(overview);
		ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) jve.primaryViewer.getRootEditPart();
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

	public PasteJavaBeanAction getPasteAction() {
		return pasteBeanAction;
	}
}

