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
/*
 *  $RCSfile: ShellPrototypeFactory.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:52:55 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.ParseTreeAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.core.PrototypeFactory;
 
/**
 *  For a Shell set the class instance creation with the correct ParseTreeAllocation.  This is because it is dropped onto the free form
 *  and no editpolicy exists to add this in
 * 	<allocation xsi:type="org.eclipse.jem.internal.instantiation:ParseTreeAllocation">
 *    <expression xsi:type="org.eclipse.jem.internal.instantiation:PTClassInstanceCreation" type="org.eclipse.swt.widgets.Shell"/>
 *	</allocation>
 *
 * @since 1.0.0
 */
public class ShellPrototypeFactory implements PrototypeFactory {

	public EObject createPrototype(EClass aClass) {
		IJavaObjectInstance shellInstance = (IJavaObjectInstance)aClass.getEPackage().getEFactoryInstance().create(aClass);
		
		ParseTreeAllocation parseTreeAllocation = InstantiationFactory.eINSTANCE.createParseTreeAllocation();
		PTClassInstanceCreation classInstanceCreation = InstantiationFactory.eINSTANCE.createPTClassInstanceCreation("org.eclipse.swt.widgets.Shell",null); //$NON-NLS-1$

		parseTreeAllocation.setExpression(classInstanceCreation);
		shellInstance.setAllocation(parseTreeAllocation);
		
		return shellInstance;
	}
}
