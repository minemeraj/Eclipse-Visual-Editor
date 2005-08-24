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
package org.eclipse.ve.internal.propertysheet;
/*
 *  $RCSfile: EToolsPropertyDescriptor.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:44:29 $ 
 */



import org.eclipse.ui.views.properties.PropertyDescriptor;
/**
 * This is a default implementation of IEToolsPropertyDescriptor.
 */

public abstract class EToolsPropertyDescriptor extends PropertyDescriptor implements IEToolsPropertyDescriptor {
	protected boolean fNullsInvalid = true;
	protected boolean fExpandable = true;
	protected boolean fReadOnly = false;
	
	public EToolsPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}
	
	public void setNullInvalid(boolean nullsInvalid) {
		fNullsInvalid = nullsInvalid;
	}
	
	public boolean areNullsInvalid() {
		return fNullsInvalid;
	}
	
	public void setExpandable(boolean expandable) {
		fExpandable = expandable;
	}
	
	public boolean isExpandable() {
		return fExpandable;
	}
	
	public boolean isReadOnly(){
		return fReadOnly;
	}
	public void setReadOnly(boolean readOnly){
		fReadOnly = readOnly;
	}
}
