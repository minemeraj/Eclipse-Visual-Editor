/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jface.binding;

public interface IUpdatableTable extends IUpdatableCollection {

	public Class[] getColumnTypes();
	
	public Object[] getValues(int index);

	public void setElementAndValues(int index, Object element, Object[] values);

	public int addElementWithValues(int index, Object element, Object[] values);

	public void setValues(int index, Object[] values);
	
}
