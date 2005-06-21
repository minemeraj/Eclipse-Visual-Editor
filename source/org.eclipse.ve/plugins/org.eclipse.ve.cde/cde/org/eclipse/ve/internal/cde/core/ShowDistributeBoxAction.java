/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: ShowDistributeBoxAction.java,v $
 *  $Revision: 1.6 $  $Date: 2005-06-21 21:43:42 $ 
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
		setText(CDEMessages.ShowDistributeBoxAction_label); 
		setId(ACTION_ID);

		String graphicName = "distributebox_obj.gif"; //$NON-NLS-1$
		setImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(CDEPlugin.getPlugin(), "icons/full/elcl16/" + graphicName)); //$NON-NLS-1$
		setHoverImageDescriptor(getImageDescriptor());
		setDisabledImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(CDEPlugin.getPlugin(), "icons/full/dlcl16/" + graphicName)); //$NON-NLS-1$
		this.addPropertyChangeListener(new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (IAction.CHECKED.equals(event.getProperty()))
					setToolTipText(isChecked() ? CDEMessages.ShowDistributeBoxAction_tooltip_hide : CDEMessages.ShowDistributeBoxAction_tooltip_show); 
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
