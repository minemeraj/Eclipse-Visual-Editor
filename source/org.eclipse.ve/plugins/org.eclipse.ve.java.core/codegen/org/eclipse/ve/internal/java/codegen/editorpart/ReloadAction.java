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
 *  $Revision: 1.5 $  $Date: 2005-01-24 22:26:44 $ 
 */
package org.eclipse.ve.internal.java.codegen.editorpart;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.ve.internal.cde.core.CDEPlugin;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
 
/**
 * Reload JVE action.
 * @since 1.0.0
 */
public class ReloadAction extends Action {
	// dbk save image descriptors
	private static final String JVE_STATUS_MSG_ERROR = CodegenEditorPartMessages.getString("JVE_STATUS_MSG_ERROR");
	private static final String JVE_STATUS_BAR_MSG_PARSE_ERROR = CodegenEditorPartMessages.getString("JVE_STATUS_BAR_MSG_PARSE_ERROR_");
	private static final String JVE_STATUS_MSG_PAUSE = CodegenEditorPartMessages.getString("JVE_STATUS_MSG_PAUSE");
	private static final String JVE_STATUS_MSG_RELOAD = CodegenEditorPartMessages.getString("JVE_STATUS_MSG_RELOAD");
	private static final ImageDescriptor PLAY_IMAGE_DESCRIPTOR = CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/cview16/play.gif");
	public static final ImageDescriptor PAUSE_IMAGE_DESCRIPTOR = CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/cview16/pause.gif");
	private static final ImageDescriptor ERROR_IMAGE_DESCRIPTOR = CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/cview16/error_obj.gif");
	
	/**
	 * Action ID for Reload action.
	 */
	public static final String RELOAD_ACTION_ID = "org.eclipse.ve.java.core.Reload"; //$NON-NLS-1$
	
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
	private Boolean lastCheckedState = null;	
	private void setCorrectText() {
		boolean isChecked = isChecked();
		// dbk avoid spurious updates
		if (lastCheckedState != null && lastCheckedState.booleanValue() == isChecked)
			return;
		lastCheckedState = new Boolean(isChecked);		
		// TODO We need full gammit of icons for this.
		if (isChecked) {
			setToolTipText(JVE_STATUS_MSG_RELOAD);
			setText(getToolTipText());
			setHoverImageDescriptor(PLAY_IMAGE_DESCRIPTOR);
		} else {
			setToolTipText(JVE_STATUS_MSG_PAUSE);
			setText(getToolTipText());
			setHoverImageDescriptor(PAUSE_IMAGE_DESCRIPTOR);
		}
	}

	/**
	 * Tell Reload action whether there is a parse error or not.
	 * 
	 * @param error <code>true</code> if a parse error occured. <code>false</code> if parse error cleared.
	 * 
	 * @since 1.0.0
	 */
	private Boolean lastParseErrorState = null;
	public void parseError(boolean error) {
		// dbk avoid spurious updates
		if (lastParseErrorState != null && lastParseErrorState.booleanValue() == error)
			return;
		lastParseErrorState = new Boolean(error);
		if (error) {
			setToolTipText(JVE_STATUS_BAR_MSG_PARSE_ERROR);
			setText(JVE_STATUS_MSG_ERROR);
			setHoverImageDescriptor(ERROR_IMAGE_DESCRIPTOR);
			setChecked(true);
		} else {
			setChecked(false);
			setCorrectText();
			setEnabled(false);
			setEnabled(true);	// Kludge. The button is not redrawing correctly. without cycling through the enabled state, it shows the new icon superimposed over the old one.
		}
	}
}
