package org.eclipse.ve.internal.propertysheet;
/*******************************************************************************
 * Copyright (c)  2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: EToolsPropertySheetAction.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:32:00 $ 
 */



import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.views.properties.IPropertySheetEntry;
/**
 * This knows the TableTree that is used by the PropertySheetViewer.
 * This is extremelly kludgy but the PropertySheetViewer is packaged
 * protected and we can't get access to it. The TableTree gives
 * us what we need, basically the root entry and the selected entries.
 */
public abstract class EToolsPropertySheetAction extends Action {
	
	protected EToolsPropertySheetPage propertySheet;
	
	public EToolsPropertySheetAction(EToolsPropertySheetPage page) {
		super();
		propertySheet = page;
	}
	
	protected IPropertySheetEntry getRootEntry() {
		return propertySheet.getRootEntry();
	}
	
	/**
	 * The <code>PropertySheetAction</code> implementation of this 
	 * <code>ISelectionProvider</code> method
	 * returns the result as a <code>StructuredSelection</code>.
	 * <p>
	 * Note that this method only includes <code>IPropertySheetEntry</code>
	 * in the selection (no categories).
	 * </p>
	 */
	public ISelection getSelection() {
		return propertySheet.getSelection();
	}		
}