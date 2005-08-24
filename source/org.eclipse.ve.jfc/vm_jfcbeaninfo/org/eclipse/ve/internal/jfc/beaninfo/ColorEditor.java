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
package org.eclipse.ve.internal.jfc.beaninfo;
/*
 *  $RCSfile: ColorEditor.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:11 $ 
 */

public class ColorEditor extends java.beans.PropertyEditorSupport {

	private ColorPropertyEditor cpe = null;

    public ColorEditor() {
    	super();
    	
    	cpe = new ColorPropertyEditor();
    }	

	public java.awt.Component getCustomEditor() {
		cpe.initialize();
		return cpe;
	}

	public String getAsText() {
		return cpe.getAsText();
	}

	public String getJavaInitializationString() {
		return cpe.getJavaInitializationString();
	}

	public Object getValue() {
		return cpe.getValue();
	}

	public boolean isPaintable() {
		return true;
	}

	public void paintValue(java.awt.Graphics gfx, java.awt.Rectangle box) {
		cpe.paintValue(gfx, box);
	}

	public void setAsText(String text) throws IllegalArgumentException {
		throw new IllegalArgumentException(text);
	}

	public void setValue(Object newValue) {
		cpe.setValue( newValue );
		super.setValue(newValue);
	}

	public boolean supportsCustomEditor() {
		return true;
	}		 
}
