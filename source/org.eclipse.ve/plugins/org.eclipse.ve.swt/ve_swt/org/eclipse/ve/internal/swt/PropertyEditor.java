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

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public interface PropertyEditor {
	public Control createControl(Composite parent, int style);
	public void setValue(Object value);
	public void setJavaObjectInstanceValue(IJavaObjectInstance value);
	public Object getValue();
	public String getText();
	public String getJavaInitializationString();
	public void addPropertyChangeListener(PropertyChangeListener event);
	public void removePropertyChangeListener(PropertyChangeListener event);
}
