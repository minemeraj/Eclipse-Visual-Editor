/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.examples.java;
/*
 *  $RCSfile: BasicTypesBeanProxyAdapter.java,v $
 *  $Revision: 1.5 $  $Date: 2005-04-22 20:57:54 $ 
 */

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.proxy.core.IMethodProxy;
import org.eclipse.jem.internal.proxy.core.ThrowableProxy;

import org.eclipse.ve.internal.java.core.*;
/**
 * This bean proxy adapter sends a dispose method to the live bean
 * when it is disposed.  This is because the live bean has a Frame
 * within it that holds onto resources and needs custom code to 
 * dispose of its resources
 */
public class BasicTypesBeanProxyAdapter extends BeanProxyAdapter {
	
public BasicTypesBeanProxyAdapter(IBeanProxyDomain domain){
	super(domain);
}	
public void releaseBeanProxy(){
	if (fOwnsProxy && isBeanProxyInstantiated()) {
		IMethodProxy disposeFrameMethodProxy = getBeanProxy().getTypeProxy().getMethodProxy("disposeFrame"); //$NON-NLS-1$
        try {
        	disposeFrameMethodProxy.invoke(getBeanProxy());
        	disposeFrameMethodProxy.getProxyFactoryRegistry().releaseProxy(disposeFrameMethodProxy);
        } catch ( ThrowableProxy exc ) {
        	JavaVEPlugin.log("Unable to dispose the BasicTypes by calling disposeFrame", Level.WARNING); //$NON-NLS-1$
        	exc.printProxyStackTrace();
        }
	}
	super.releaseBeanProxy();
		
}
/**
 * This method is specialized to show you can provide a specific order
 * in which you want the attributes applied
 * As an example, we always want the property called "string" applied first
 */
protected void applyAllSettings() {
	if ( !(target instanceof EObject)) return;

	EStructuralFeature stringFeature = getEObject().eClass().getEStructuralFeature("string"); //$NON-NLS-1$
	Object stringValue = getEObject().eGet(stringFeature);
	if ( stringValue != null ) {
		applied(stringFeature,stringValue,-1);
	}
	
	// Now apply all the other values, excluding String because we have done this above
	EObject eTarget = getEObject();
	Iterator features = eTarget.eClass().getEAllStructuralFeatures().iterator();
	while (features.hasNext()) {
		EStructuralFeature sf = (EStructuralFeature) features.next();
		if (sf != stringFeature && eTarget.eIsSet(sf)) {
			if (sf.isMany()) {
				appliedList(sf,(List)eTarget.eGet(sf), Notification.NO_INDEX, true);	// Test validity because we are applying all.
			} else {
				// Do not apply the value if it is in error
				if (isValidFeature(sf)){
					applied(sf,eTarget.eGet(sf), Notification.NO_INDEX);			
				} 	
			}
		}
	}	
}
}
