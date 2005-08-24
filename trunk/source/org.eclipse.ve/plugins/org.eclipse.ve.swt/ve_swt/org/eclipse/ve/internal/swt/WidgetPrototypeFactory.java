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
 *  $RCSfile: WidgetPrototypeFactory.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:52:55 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.ve.internal.java.core.PrototypeFactory;
 
/**
 * 	<allocation xsi:type="org.eclipse.jem.internal.instantiation:ParseTreeAllocation">
 *    <expression xsi:type="org.eclipse.jem.internal.instantiation:PTClassInstanceCreation" type="QUALIFIED_CLASS_NAME">
 *      <arguments xsi:type="org.eclipse.jem.internal.instantiation:PTName" name="{parentComposite}"/>
 *      <arguments xsi:type="org.eclipse.jem.internal.instantiation:PTFieldAccess" field="NONE">
 *        <receiver xsi:type="org.eclipse.jem.internal.instantiation:PTName" name="org.eclipse.swt.SWT"/>
 *      </arguments>
 *    </expression>	  	
 *  </allocation>
 * 
 * 
 * @since 1.0.0
 */
public class WidgetPrototypeFactory implements PrototypeFactory {

	public EObject createPrototype(EClass aClass) {
		// TODO - Right now just return a default instance because the parent edit parts create the allocation tree OK
		// Discuss with Rich/Sri and others whether we should create the whole parse tree instead however
		return aClass.getEPackage().getEFactoryInstance().create(aClass);
	}
}
