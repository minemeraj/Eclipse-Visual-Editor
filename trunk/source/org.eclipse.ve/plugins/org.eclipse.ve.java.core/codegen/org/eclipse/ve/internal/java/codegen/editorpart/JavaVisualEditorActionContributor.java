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
 *  $RCSfile: JavaVisualEditorActionContributor.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.gef.ui.actions.*;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditorActionContributor;
import org.eclipse.jface.action.*;
import org.eclipse.ui.*;
import org.eclipse.ui.actions.LabelRetargetAction;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.texteditor.*;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.core.MenuCreatorRetargetAction;
import org.eclipse.ve.internal.java.core.CustomizeJavaBeanAction;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * @author richkulp
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JavaVisualEditorActionContributor extends CompilationUnitEditorActionContributor {
	
	protected static final String[] STATUS_FIELDS = new String[] {
		IJVEStatus.STATUS_CATEGORY_SYNC_STATUS,
		IJVEStatus.STATUS_CATEGORY_SYNC_ACTION
	};
	
	private IStatusField[] statusFields = new IStatusField[] {
		new StatusLineContributionItem(IJVEStatus.STATUS_CATEGORY_SYNC_STATUS),
		new JVEActionStatusLineContributionItem(IJVEStatus.STATUS_CATEGORY_SYNC_ACTION)		
	};
	
	protected IEditorPart activeEditor;
	public static final String 
		PALETTE_SELECTION_ACTION_ID = "paletteSelection", //$NON-NLS-1$
		PALETTE_MARQUEE_SELECTION_ACTION_ID = "paletteMarqueeSelection", //$NON-NLS-1$
		PALETTE_DROPDOWN_ACTION_ID = "paletteDropdown"; //$NON-NLS-1$
		
	private RetargetAction deleteAction, undoAction, redoAction, palSelectAction, palMarqueeAction, customizeAction, alignmentWindowRetargetAction;
	private MenuCreatorRetargetAction palDropdownAction;
	
	private AlignmentWindowAction alignmentWindowAction;
	 
	public JavaVisualEditorActionContributor() {
		// These actions are retargeted so that it works with both the JavaBeans viewer and the editor part
		// and also it actually works with the source viewer and the outline list of methods and fields.
		// The java beans viewer simply sets its version of the actions into its global action handler registry 
		// for its action bars. That way when switching to that viewer, these retarget actions will go to the
		// viewers action bar handler registry and pick up the appropriate actions. Those that aren't in the
		// viewers registry are disabled on the editor action bar.
		// LabelRetargetAction will change label/images depending upon the action handler.
		deleteAction = new DeleteRetargetAction();
		deleteAction.setEnabled(false);
		markAsPartListener(deleteAction);
		
		undoAction = new UndoRetargetAction();
		undoAction.setEnabled(false);
		markAsPartListener(undoAction);
	
		redoAction = new RedoRetargetAction();
		redoAction.setEnabled(false);
		markAsPartListener(redoAction);
		
		// For now (maybe forever) the actual palette that displays on the toolbar will only be 
		// selection, marqueeSelection, chooseBean. And chooseBean will include dropdown for the categories.
		// These will be retargetable so that they work with the current selected editor. Only the dropdown
		// will vary depending upon the editor.
		palSelectAction = new LabelRetargetAction(PALETTE_SELECTION_ACTION_ID,""); //$NON-NLS-1$
		palSelectAction.setHoverImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(CDEPlugin.getPlugin(), "images/arrow16.gif"));		 //$NON-NLS-1$
		palSelectAction.setEnabled(false);
		palSelectAction.setChecked(false);
		markAsPartListener(palSelectAction);

		palMarqueeAction = new LabelRetargetAction(PALETTE_MARQUEE_SELECTION_ACTION_ID,""); //$NON-NLS-1$
		palMarqueeAction.setHoverImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(CDEPlugin.getPlugin(), "images/marquee16.gif"));		 //$NON-NLS-1$
		palMarqueeAction.setEnabled(false);
		palMarqueeAction.setChecked(false);
		markAsPartListener(palMarqueeAction);
		
		palDropdownAction = new MenuCreatorRetargetAction(PALETTE_DROPDOWN_ACTION_ID,""); //$NON-NLS-1$
		palDropdownAction.setHoverImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/cview16/selectbean_view.gif")); //$NON-NLS-1$
		palDropdownAction.setEnabled(false);
		markAsPartListener(palDropdownAction);
		
		customizeAction = new LabelRetargetAction(CustomizeJavaBeanAction.ACTION_ID, ""); //$NON-NLS-1$
		customizeAction.setHoverImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/etool16/customizebean_co.gif")); //$NON-NLS-1$
		customizeAction.setEnabled(false);
		markAsPartListener(customizeAction);
		
		alignmentWindowRetargetAction = new LabelRetargetAction(AlignmentWindowAction.ACTION_ID, ""); //$NON-NLS-1$
		alignmentWindowRetargetAction.setHoverImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(CDEPlugin.getPlugin(), "icons/full/clcl16/aligndialog_obj.gif")); //$NON-NLS-1$
		alignmentWindowRetargetAction.setChecked(false);
		markAsPartListener(alignmentWindowRetargetAction);		
	}
	
	public void contributeToToolBar(IToolBarManager tbm) {
		super.contributeToToolBar(tbm);	
		if (alignmentWindowAction == null) {
			// Didn't have a page until now.
			alignmentWindowAction = new AlignmentWindowAction(getPage().getWorkbenchWindow(), this);
			getActionBars().setGlobalActionHandler(AlignmentWindowAction.ACTION_ID, alignmentWindowAction);
		}
		tbm.add(deleteAction);
		tbm.add(undoAction);
		tbm.add(redoAction);	
		tbm.add(new Separator());
		tbm.add(palSelectAction);
		tbm.add(palMarqueeAction);
		tbm.add(palDropdownAction);	
		tbm.add(new Separator());
		tbm.add(customizeAction);
		tbm.add(alignmentWindowRetargetAction);
	}	
	
	public void contributeToStatusLine(IStatusLineManager statusLineManager) {
		super.contributeToStatusLine(statusLineManager);
		for (int i = 0; i < statusFields.length; i++) {
			statusLineManager.add((IContributionItem) statusFields[i]);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorActionBarContributor#setActiveEditor(org.eclipse.ui.IEditorPart)
	 */
	public void setActiveEditor(IEditorPart part) {
		if (part == getActiveEditorPart())
			return;
			
		if (getActiveEditorPart() instanceof ITextEditorExtension) {
			ITextEditorExtension extension = (ITextEditorExtension) getActiveEditorPart();
			for (int i = 0; i < STATUS_FIELDS.length; i++) {
				extension.setStatusField(null, STATUS_FIELDS[i]);
				if (statusFields[i] instanceof JVEActionStatusLineContributionItem) {
					// When the editor is closed, we nee to remove the link or the outgoing editor
					// In case that the IDE keep holding this contributor
					((JVEActionStatusLineContributionItem) statusFields[i]).setPauseListener(null);
				}
			}
		}
		
		super.setActiveEditor(part);
		
		if (getActiveEditorPart() instanceof ITextEditorExtension) {
			ITextEditorExtension extension= (ITextEditorExtension) getActiveEditorPart();
			for (int i= 0; i < STATUS_FIELDS.length; i++)
				extension.setStatusField(statusFields[i], STATUS_FIELDS[i]);
		}
		
		ITextEditor textEditor = (getActiveEditorPart() instanceof ITextEditor) ? (ITextEditor) getActiveEditorPart() : null;
		
		IActionBars bars = getActionBars();
		bars.setGlobalActionHandler(GEFActionConstants.DELETE, getAction(textEditor, GEFActionConstants.DELETE));
		bars.setGlobalActionHandler(GEFActionConstants.UNDO, getAction(textEditor, GEFActionConstants.UNDO));
		bars.setGlobalActionHandler(GEFActionConstants.REDO, getAction(textEditor, GEFActionConstants.REDO));
		bars.setGlobalActionHandler(PALETTE_SELECTION_ACTION_ID, getAction(textEditor, PALETTE_SELECTION_ACTION_ID));
		bars.setGlobalActionHandler(PALETTE_MARQUEE_SELECTION_ACTION_ID, getAction(textEditor, PALETTE_MARQUEE_SELECTION_ACTION_ID));
		bars.setGlobalActionHandler(PALETTE_DROPDOWN_ACTION_ID, getAction(textEditor, PALETTE_DROPDOWN_ACTION_ID));
		bars.setGlobalActionHandler(CustomizeJavaBeanAction.ACTION_ID, getAction(textEditor, CustomizeJavaBeanAction.ACTION_ID));		
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorActionBarContributor#dispose()
	 */
	public void dispose() {
		if (alignmentWindowAction != null) {
			alignmentWindowAction.dispose();
			alignmentWindowAction = null;
		}
		super.dispose();
	}

}
