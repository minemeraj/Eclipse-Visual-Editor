package org.eclipse.ve.internal.jfc.beaninfo;

import java.beans.PropertyEditorSupport;

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
 *  $RCSfile: MnemonicEditor.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:33 $ 
 */
public class MnemonicEditor extends PropertyEditorSupport {

	private MnemonicPropertyEditor mpe = null;

    public MnemonicEditor() {
    	super();
    	
    	mpe = new MnemonicPropertyEditor();
    }	

	public java.awt.Component getCustomEditor() {
		mpe.initialize();
		return mpe;
	}

	public String getAsText() {
		return mpe.getAsText();
	}

	public String getJavaInitializationString() {
		return mpe.getJavaInitializationString();
	}

	public Object getValue() {
		return mpe.getValue();
	}

	public boolean isPaintable() {
		return true;
	}

	public void setAsText(String text) throws IllegalArgumentException {
		throw new IllegalArgumentException(text);
	}

	public void setValue(Object newValue) {
		mpe.setValue( newValue );
		super.setValue(newValue);
	}

	public boolean supportsCustomEditor() {
		return true;
	}		 
}

