/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;

import java.beans.PropertyChangeListener;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

public class FontPropertyEditor implements PropertyEditor {
	
	
	protected FontCustomPropertyEditor customFontEditor;
	private IJavaObjectInstance fExistingValue;
	protected Font value;
	
	/* (non-Javadoc)
	 * @see PropertyEditor#createControl(org.eclipse.swt.widgets.Composite, int)
	 */
	public Control createControl(Composite parent, int style) {
		if (customFontEditor == null || customFontEditor.isDisposed()) {
			customFontEditor = new FontCustomPropertyEditor(parent, style, value, fExistingValue);
			parent.setLayout(new FillLayout());
		}
		return customFontEditor;
	}
	
	public void setValue(Object v) {
		if (customFontEditor != null)
			customFontEditor.setValue(v);
		if (v != null && v instanceof Font)
			value = (Font)v;
	}
	public Object getValue() {
		return customFontEditor != null ? customFontEditor.getValue() : null;
	}
	
	public String getJavaInitializationString() {
		if(customFontEditor != null)
			return customFontEditor.getJavaInitializationString();
		return "null";
	}
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		if(customFontEditor != null)
			customFontEditor.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		if(customFontEditor != null)
			customFontEditor.removePropertyChangeListener(listener);
	}	
	public String getText(){
		if(fExistingValue != null){
			return FontJavaClassLabelProvider.getText(fExistingValue);			
		} else {
			return ""; //$NON-NLS-1$
		}
	}
	
	public void setJavaObjectInstanceValue(IJavaObjectInstance value) {
		fExistingValue = value;
		if(customFontEditor != null)
			customFontEditor.setExistingValue(value);
	}

}
