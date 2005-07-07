package org.eclipse.ve.internal.propertysheet;
/*******************************************************************************
 * Copyright (c)  2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: EToolsPropertyDescriptor.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:47:33 $ 
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