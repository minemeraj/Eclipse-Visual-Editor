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
 *  $RCSfile: BorderEditor.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:11 $ 
 */

import javax.swing.border.Border;


public class BorderEditor extends java.beans.PropertyEditorSupport {
	protected Object fBorder = null;
	protected BorderPropertyEditor fCustomEditor = null;


public BorderEditor() {
	super();
	fCustomEditor = new BorderPropertyEditor();
}	
	
public String getAsText(){
	if (fBorder == null && fCustomEditor == null) return ""; //$NON-NLS-1$
	return fCustomEditor.getBorderName();
}

public java.awt.Component getCustomEditor(){
	fCustomEditor.buildPropertyEditor();
	return fCustomEditor;
}

public String getJavaInitializationString(){
	if (fCustomEditor != null){
		return fCustomEditor.getBorderInitializationString();
	}
	else{
		return fBorder.toString();
	}
}

public Object getValue(){
	if (fCustomEditor != null){
		return fCustomEditor.getBorderValue();
	}else{
		return super.getValue();
	}
}

public void setValue(Object newValue){
	fBorder = newValue;
	if(fCustomEditor != null){
		fCustomEditor.setBorderValue((Border)newValue);
	}
	super.setValue(newValue);
}

public boolean supportsCustomEditor(){
	return true;
}

}
