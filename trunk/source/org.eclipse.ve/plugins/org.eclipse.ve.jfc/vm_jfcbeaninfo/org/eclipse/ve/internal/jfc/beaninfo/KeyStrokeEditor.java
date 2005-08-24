/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.beaninfo;

import java.beans.PropertyEditorSupport;

/*
 *  $RCSfile: KeyStrokeEditor.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:11 $ 
 */
public class KeyStrokeEditor extends PropertyEditorSupport {

	private KeyStrokePropertyEditor kspe = null;

    public KeyStrokeEditor() {
    	super();
    	
    	kspe = new KeyStrokePropertyEditor();
    }	

	public java.awt.Component getCustomEditor() {
		kspe.initialize();
		return kspe;
	}

	public String getAsText() {
		return kspe.getAsText();
	}

	public String getJavaInitializationString() {
		return kspe.getJavaInitializationString();
	}

	public Object getValue() {
		return kspe.getValue();
	}

	public boolean isPaintable() {
		return true;
	}

	public void setAsText(String text) throws IllegalArgumentException {
		throw new IllegalArgumentException(text);
	}

	public void setValue(Object newValue) {
		kspe.setValue( newValue );
		super.setValue(newValue);
	}

	public boolean supportsCustomEditor() {
		return true;
	}		 
}
