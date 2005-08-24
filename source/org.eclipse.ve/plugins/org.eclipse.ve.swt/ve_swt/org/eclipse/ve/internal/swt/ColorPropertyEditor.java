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

package org.eclipse.ve.internal.swt;

import java.beans.PropertyChangeListener;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.propertysheet.INeedData;

public class ColorPropertyEditor implements PropertyEditor, INeedData {

	private ColorCustomPropertyEditor customColorEditor;
	private IJavaObjectInstance fExistingValue;
	private Color color;
	private EditDomain fEditDomain;
	
	public ColorPropertyEditor() {
		
	}
	
	public ColorPropertyEditor(Color initialColor) {
		setValue(initialColor);
	}
	
	public Control createControl(Composite parent, int style) {
		if (customColorEditor == null || customColorEditor.isDisposed()) {
			customColorEditor = new ColorCustomPropertyEditor(parent, style, color, fExistingValue, fEditDomain);
		}
		return customColorEditor;
	}
	

	public void setData(Object data) {
		fEditDomain = (EditDomain) data;
	}

	public void setValue(Object value) {
		if(customColorEditor != null){
			customColorEditor.setValue(value);
		}
		if(value != null && value instanceof Color){
			this.color = (Color)value;
		}
	}
	
	public Object getValue() {
		return customColorEditor != null ? customColorEditor.getValue(): null;
	}
	
	public String getJavaInitializationString() {
		String result = "null"; //$NON-NLS-1$
		if(customColorEditor != null)
			result = customColorEditor.getJavaInitializationString();
		return result;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		if(customColorEditor != null)
			customColorEditor.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		if(customColorEditor != null)
			customColorEditor.removePropertyChangeListener(listener);
	}

	public String getText() {
		if(fExistingValue != null){
			return ColorJavaClassLabelProvider.getText(fExistingValue);			
		} else {
			return ""; //$NON-NLS-1$
		}
	}

	/* 
	 * Set the existing value into the editor
	 */
	public void setJavaObjectInstanceValue(IJavaObjectInstance value) {
		fExistingValue = value;
		// We have the IDE object that points to the color on the target VM
		if(customColorEditor != null)
			customColorEditor.setJavaObjectInstanceValue(value);
	}
	

	
}
