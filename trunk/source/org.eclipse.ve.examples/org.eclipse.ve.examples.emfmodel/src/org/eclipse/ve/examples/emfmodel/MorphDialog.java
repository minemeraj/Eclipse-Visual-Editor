/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.examples.emfmodel;

import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.JavaObjectInstance;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class MorphDialog extends Dialog {

	private IJavaInstance javaBean;

	protected MorphDialog(Shell parentShell) {
		super(parentShell);
	}
	
	public void setJavaBean(IJavaInstance aJavaInstance){
		javaBean = aJavaInstance;
	}

	protected Control createDialogArea(Composite parent) {

		Label l = new Label(parent,SWT.NONE);
		l.setText("Press OK to morph into the immediate superclass");
		return l;
		
	}
	
	protected void okPressed() {

		JavaClass supertype = ((JavaClass)javaBean.getJavaType()).getSupertype();
		((EObjectImpl)javaBean).eSetClass(supertype);
		
	}
	
}
