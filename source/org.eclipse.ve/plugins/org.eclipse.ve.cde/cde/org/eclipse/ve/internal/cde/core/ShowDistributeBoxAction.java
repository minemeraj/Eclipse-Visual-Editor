package org.eclipse.ve.internal.cde.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ShowDistributeBoxAction.java,v $
 *  $Revision: 1.2 $  $Date: 2003-11-20 20:04:04 $ 
 */

import org.eclipse.gef.ui.actions.EditorPartAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbenchPart;

public class ShowDistributeBoxAction extends EditorPartAction {
	public static final String ACTION_ID = "cde.SHOWDISTRIBUTEBOX"; //$NON-NLS-1$

	public ShowDistributeBoxAction() {
		super(null);
		setText(CDEMessages.getString("ShowDistributeBoxAction.label")); //$NON-NLS-1$
		setId(ACTION_ID);

		String graphicName = "distributebox_obj.gif"; //$NON-NLS-1$
		setImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(CDEPlugin.getPlugin(), "icons/full/elcl16/" + graphicName)); //$NON-NLS-1$
		setHoverImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(CDEPlugin.getPlugin(), "icons/full/clcl16/" + graphicName)); //$NON-NLS-1$
		setDisabledImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(CDEPlugin.getPlugin(), "icons/full/dlcl16/" + graphicName)); //$NON-NLS-1$
		this.addPropertyChangeListener(new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (IAction.CHECKED.equals(event.getProperty()))
					setToolTipText(isChecked() ? CDEMessages.getString("ShowDistributeBoxAction.tooltip.hide") : CDEMessages.getString("ShowDistributeBoxAction.tooltip.show")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		});
		setChecked(false); // It is assumed that the box is initially not visible.
	}
	/**
	 * Creates and executes the alignment command on the selected objects.
	 */
	public void run() {
		DistributeController dc = DistributeController.getDistributeController(getEditorPart());
		dc.setBoxVisible(isChecked());
	}
	
	/**
	 * @see org.eclipse.gef.ui.actions.EditorPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		boolean result = false;
		if (getEditorPart() != null) {
			DistributeController dc = DistributeController.getDistributeController(getEditorPart());
			result = dc != null;
		} else {
			result = false;
		}
		
		return result;
	}
	
	public void setWorkbenchPart(IWorkbenchPart part) {
		// To work around GEF's refusal to do the right thing and leave it public.
		super.setWorkbenchPart(part);
		// If the controller says the box is visible, make sure it is
		if (getEditorPart() != null) {
			DistributeController dc = DistributeController.getDistributeController(getEditorPart());
			if (dc != null)
				if ( (!isChecked() && dc.isBoxVisible()) || (isChecked() && !dc.isBoxVisible()) )
					setChecked(dc != null ? dc.isBoxVisible() : false);
		}		
	}	
}