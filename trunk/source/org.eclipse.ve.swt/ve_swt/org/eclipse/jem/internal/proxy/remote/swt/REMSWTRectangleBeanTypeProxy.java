package org.eclipse.jem.internal.proxy.remote.swt;
/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved. 
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp. 
 */

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