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
package org.eclipse.jem.internal.proxy.remote.swt;

import org.eclipse.jem.internal.proxy.remote.*;
import org.eclipse.jem.internal.proxy.core.*;
/**
 * Beantype proxy for org.eclipse.swt.graphics.Rectangle
 */
public class REMSWTRectangleBeanTypeProxy extends REMAbstractBeanTypeProxy implements IREMSpecialBeanTypeProxy {
	
	protected REMSWTRectangleBeanTypeProxy(REMProxyFactoryRegistry aRegistry, Integer anID, String aClassname, IBeanTypeProxy aSuperType) {
		super(aRegistry, anID, aClassname, aSuperType);
	}
	REMSWTRectangleBeanProxy createRectangleBeanProxy(Integer objectID) {
		return new REMSWTRectangleBeanProxy(fRegistry, objectID, this);
	}
	
	public IREMBeanTypeProxy newBeanTypeForClass(Integer anID, String aClassname, boolean anAbstract) {
		return newBeanTypeForClass(anID, aClassname, anAbstract, this);
	}

		public IREMBeanTypeProxy newBeanTypeForClass(Integer anID, String aClassname, boolean anAbstract, IBeanTypeProxy superType) {
		if (!anAbstract)
			return new REMSWTRectangleBeanTypeProxy(fRegistry, anID, aClassname, superType);
		else
			return new REMAnAbstractBeanTypeProxy(fRegistry, anID, aClassname, superType, this);
	}
	
	/**
	 * Specialized from REMAbstractBeanTypeProxy to ensure ExceptionBeanProxies are created correctly.
	 */
	public IREMBeanProxy newBeanProxy(Integer objectID) {
		return createRectangleBeanProxy(objectID);
	}	

}