/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JavaVisualEditorPropertySheetPage.java,v $
 *  $Revision: 1.3 $  $Date: 2005-07-19 22:58:38 $ 
 */
package org.eclipse.ve.internal.java.codegen.editorpart;

import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.texteditor.StatusLineContributionItem;

import org.eclipse.ve.internal.propertysheet.EToolsPropertySheetPage;
 
/**
 * Property Sheet page for the Java Visual Editor.
 * <p>
 * Not meant to be subclassed, but here just in case someone does and wants to the actionbar.
 * 
 * @since 1.0.0
 */
public class JavaVisualEditorPropertySheetPage extends EToolsPropertySheetPage {
	// The jve status field for when property sheet is in focus. It will be kept up to date through
	// updateStatusField method in JavaVisualEditorPart. 
	protected StatusLineContributionItem jveStatusField;
	protected JavaVisualEditorPart jve;
	public JavaVisualEditorPropertySheetPage(JavaVisualEditorPart jve) {
		this.jve = jve;
	}
	
	public void setActionBars(IActionBars actionBars) {
		super.setActionBars(actionBars);
		// Create the status field and put on status line.
		jveStatusField = new StatusLineContributionItem(JavaVisualEditorActionContributor.STATUS_FIELD_CATEGORY);
		jveStatusField.setActionHandler(jve.getAction(ReloadNowAction.RELOADNOW_ACTION_ID));
		actionBars.getStatusLineManager().add(jveStatusField);
		jve.updateStatusField(JavaVisualEditorActionContributor.STATUS_FIELD_CATEGORY);	// So that it gets the latest settings.
		
		// The menu and toolbars have RetargetActions for UNDO and REDO and pause/reload.
		// Set an action handler to redirect these to the action registry's actions so they work when the property sheet is enabled
		actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), jve.getAction(ActionFactory.UNDO.getId()));
		actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), jve.getAction(ActionFactory.REDO.getId()));
		actionBars.setGlobalActionHandler(JavaVisualEditorReloadActionController.RELOAD_ACTION_ID, jve.getAction(JavaVisualEditorReloadActionController.RELOAD_ACTION_ID));
	}
}