/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ReloadAction.java,v $
 *  $Revision: 1.2 $  $Date: 2004-04-02 00:11:35 $ 
 */
package org.eclipse.ve.internal.java.codegen.editorpart;

import org.eclipse.jface.action.Action;

import org.eclipse.ve.internal.cde.core.CDEPlugin;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
 
/**
 * Reload JVE action.
 * @since 1.0.0
 */
public class ReloadAction extends Action {
	
	/**
	 * Action ID for Reload action.
	 */
	public static final String RELOAD_ACTION_ID = "org.eclipse.ve.java.core.Reload";
	
	/**
	 * Used to callback into the JavaVisualEditorPart to perform the required actions.
	 * 
	 * @since 1.0.0
	 */
	public interface IReloadCallback {
		
		/**
		 * Pause was requested.
		 * 
		 * 
		 * @since 1.0.0
		 */
		public void pause();
		
		/**
		 * Reload was requested.
		 * 
		 * 
		 * @since 1.0.0
		 */
		public void reload();
		
	}
	
	protected IReloadCallback reloadCallback;
	
	/**
	 * 
	 * 
	 * @since 1.0.0
	 */
	public ReloadAction(IReloadCallback reloadCallback) {
		super("", Action.AS_CHECK_BOX);
		setId(RELOAD_ACTION_ID);
		setEnabled(false);
		setChecked(false);
		setCorrectText();
		this.reloadCallback = reloadCallback;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		if (isChecked()) {
			setCorrectText();			
			reloadCallback.pause();
		} else {
			setCorrectText();
			reloadCallback.reload();
		}
	}
	
	/*
	 * Set the text correctly according to the current check status.
	 */
	private void setCorrectText() {
		// TODO We need full gammit of icons for this.
		if (isChecked()) {
			setToolTipText(CodegenEditorPartMessages.getString("JVE_STATUS_MSG_RELOAD"));
			setText(getToolTipText());
			setHoverImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/cview16/play.gif"));
		} else {
			setToolTipText(CodegenEditorPartMessages.getString("JVE_STATUS_MSG_PAUSE"));
			setText(getToolTipText());
			setHoverImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/cview16/pause.gif"));			
		}
	}

	/**
	 * Tell Reload action whether there is a parse error or not.
	 * 
	 * @param error <code>true</code> if a parse error occured. <code>false</code> if parse error cleared.
	 * 
	 * @since 1.0.0
	 */
	public void parseError(boolean error) {
		if (error) {
			setToolTipText(CodegenEditorPartMessages.getString("JVE_STATUS_BAR_MSG_PARSE_ERROR"));
			setText(CodegenEditorPartMessages.getString("JVE_STATUS_MSG_ERROR"));
			setHoverImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/cview16/error_obj.gif"));
			setChecked(true);
		} else {
			setChecked(false);
			setCorrectText();
			setEnabled(false);
			setEnabled(true);	// Kludge. The button is not redrawing correctly. without cycling through the enabled state, it shows the new icon superimposed over the old one.
		}
	}
}
