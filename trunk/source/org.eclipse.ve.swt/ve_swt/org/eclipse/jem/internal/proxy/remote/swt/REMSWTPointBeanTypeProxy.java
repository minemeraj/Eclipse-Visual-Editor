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
 * Beantype proxy for org.eclipse.swt.graphics.Point
 */
public class REMSWTPointBeanTypeProxy extends REMAbstractBeanTypeProxy implements IREMSpecialBeanTypeProxy {
	
	protected REMSWTPointBeanTypeProxy(REMProxyFactoryRegistry aRegistry, Integer anID, String aClassname, IBeanTypeProxy aSuperType) {
		super(aRegistry, anID, aClassname, aSuperType);
	}
	REMSWTPointBeanProxy createPointBeanProxy(Integer objectID) {
		return new REMSWTPointBeanProxy(fRegistry, objectID, this);
	}
	

	public IREMBeanTypeProxy newBeanTypeForClass(Integer anID, String aClassname, boolean anAbstract) {
		return newBeanTypeForClass(anID, aClassname, anAbstract, this);
	}

	public IREMBeanTypeProxy newBeanTypeForClass(Integer anID, String aClassname, boolean anAbstract, IBeanTypeProxy superType) {
		if (!anAbstract)
			return new REMSWTPointBeanTypeProxy(fRegistry, anID, aClassname, superType);
		else
			return new REMAnAbstractBeanTypeProxy(fRegistry, anID, aClassname, superType, this);
	}
	public IREMBeanProxy newBeanProxy(Integer objectID) {
		return createPointBeanProxy(objectID);
	}	

}