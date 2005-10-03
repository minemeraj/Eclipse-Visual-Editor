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

public interface IUpdatableCollection extends IUpdatable {

	public int getSize();

	public int addElement(Object value, int index);

	public void removeElement(int index);

	// TODO this is not needed
	public void setElement(int row, Object value);

	public Object getElement(int row);

	public Class getElementType();
}
