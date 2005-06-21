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
 *  $RCSfile: ReloadAction.java,v $
 *  $Revision: 1.11 $  $Date: 2005-06-21 22:12:53 $ 
 */
package org.eclipse.ve.internal.java.codegen.editorpart;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Event;

import org.eclipse.ve.internal.cde.core.CDEPlugin;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
 
/**
 * Reload JVE action.
 * @since 1.0.0
 */
public class ReloadAction extends Action {
	// dbk save image descriptors
	private static final String JVE_STATUS_MSG_ERROR = CodegenEditorPartMessages.JVE_STATUS_MSG_ERROR; 
	private static final String JVE_STATUS_BAR_MSG_PARSE_ERROR = CodegenEditorPartMessages.JVE_STATUS_BAR_MSG_PARSE_ERROR_; 
	private static final String JVE_STATUS_MSG_PAUSE = CodegenEditorPartMessages.JVE_STATUS_MSG_PAUSE; 
	private static final String JVE_STATUS_MSG_RELOAD = CodegenEditorPartMessages.JVE_STATUS_MSG_RELOAD; 
	private static final ImageDescriptor PLAY_IMAGE_DESCRIPTOR = CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/cview16/refresh_obj.gif"); //$NON-NLS-1$
	public static final ImageDescriptor PAUSE_IMAGE_DESCRIPTOR = CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/cview16/pause.gif"); //$NON-NLS-1$
	private static final ImageDescriptor ERROR_IMAGE_DESCRIPTOR = CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/cview16/error_obj.gif"); //$NON-NLS-1$
	
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
		 * @param clean true for a clean reload (e.g., remove cache)
		 * 
		 * @since 1.0.0
		 */
		public void reload(boolean useCache);
		
	}
	
	protected IReloadCallback reloadCallback;
	
	/**
	 * 
	 * 
	 * @since 1.0.0
	 */
	public ReloadAction(IReloadCallback reloadCallback) {
		super("", Action.AS_CHECK_BOX); //$NON-NLS-1$
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
		// Action is not called by a user
		// do not clean the cache
		run (false);
	}
	public void runWithEvent(Event event) {
		// If the action is invoked from the GUI, clean 
		// the cache.
        run(true);
    }
	public void run(boolean clean) {
		if (isChecked()) {
			setCorrectText();			
			reloadCallback.pause();
		} else {
			setCorrectText();
			reloadCallback.reload(clean);
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
		lastCheckedState = Boolean.valueOf(isChecked);		
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
		lastParseErrorState = Boolean.valueOf(error);
		if (error) {
			setToolTipText(JVE_STATUS_BAR_MSG_PARSE_ERROR);
			setText(JVE_STATUS_MSG_ERROR);
			setHoverImageDescriptor(ERROR_IMAGE_DESCRIPTOR);
			setChecked(true);
			lastCheckedState = Boolean.TRUE;			
		} else {
			setChecked(false);
			setCorrectText();
			setEnabled(false);
			setEnabled(true);	// Kludge. The button is not redrawing correctly. without cycling through the enabled state, it shows the new icon superimposed over the old one.
		}
	}
	public void setPause() {
		setChecked(true);
		run();
	}
    
    /**
     * This is when is explicitly unpaused due to a background reload.
     * 
     * 
     * @since 1.1.0
     */
    public void unPause() {
        setChecked(false);
        setCorrectText();
        setEnabled(true);
    }
}
