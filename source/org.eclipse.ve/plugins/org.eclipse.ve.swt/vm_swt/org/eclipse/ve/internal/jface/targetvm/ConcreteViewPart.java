/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ConcreteViewPart.java,v $
 *  $Revision: 1.1 $  $Date: 2005-04-03 06:04:11 $ 
 */
package org.eclipse.ve.internal.jface.targetvm;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * This is a dummy subclass of ViewPart used by ViewPartProxyAdapter when a ViewPart subclass is being edited
 * The Visual Editor instantiates the superclass of the class being edited as its prototype instance for the bean proxy
 * however because ViewPart is abstract and its immediate superclass WorkbenchPart also is this means the closest
 * non-abstract class is Object.
 * ConcreteViewPart therefore is used if ViewPartProxyAdapter is unable to instantiate a class that inherits from
 * ViewPart
 * 
 * @since 1.0.2
 */

public class ConcreteViewPart extends ViewPart {

	public void createPartControl(Composite parent) { }

	public void setFocus() {}

}
