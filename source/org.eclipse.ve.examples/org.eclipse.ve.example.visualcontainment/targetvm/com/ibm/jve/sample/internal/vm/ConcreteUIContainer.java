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
package com.ibm.jve.sample.internal.vm;
import org.eclipse.swt.widgets.Composite;

import com.ibm.jve.sample.core.UIContainer;

public class ConcreteUIContainer extends UIContainer {

	private Composite parent;

	protected void create() {
		parent = createBaseContainer(1, false);
		Composite c1 = createComposite(parent, 1);
	}
	
}
