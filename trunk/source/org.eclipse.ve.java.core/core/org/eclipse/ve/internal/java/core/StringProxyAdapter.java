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
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: StringProxyAdapter.java,v $
 *  $Revision: 1.6 $  $Date: 2005-02-15 23:23:54 $ 
 */
import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.java.JavaParameter;
import org.eclipse.jem.java.JavaRefFactory;
import org.eclipse.jem.java.impl.JavaClassImpl;

public class StringProxyAdapter extends BeanProxyAdapter {
	
	protected boolean isExternalized;
	
public StringProxyAdapter(IBeanProxyDomain aDomain){
	super(aDomain);
}
protected IBeanProxy instantiateWithString(IBeanTypeProxy targetClass, String initString) throws ThrowableProxy, InstantiationException {
	
	// If the initString is completely enclosed by a " at the beginning and end then it is a regular
	// String
	if ( initString.length() > 2 && initString.charAt(0) == '"' && initString.charAt(initString.length()-1) == '"'){
		isExternalized = false;
		
		// TODO Need to fix this compile error... for now just return null
		return null;
		// return super.instantiateWithString(targetClass, initString);
	}
	// We have some kind of string that may be have come from a resource bundle
	// VAJava generated a style of string like
	// bundle.getString("String");
	// where bundle is a reference to a resource bundle field in the class
	// while WSAD can generate a reference like
	// MyMessages.getString("String");
	// where MyMessages is actually a class that re-directs the getString(String) method to the initilized bundle 
	// ( and therefore makes the loading of the bundle occur only once and is a bit more efficient )
	// Either way, the pattern is XXXXX.getString("YYYYY");
	// We should look for this and if so make the actual string be "{YYYYY}", with the angle brackets showing that the
	// string came from a bundle
	int indexOfGetString = initString.indexOf(".getString("); //$NON-NLS-1$
	if ( indexOfGetString != -1 && initString.charAt(initString.length()-1) == ')') {
		
		// We have two possibilites here, 1. VAJ style in which case no resolution is possible
		// on the target VM as is because it relies on a bundle instance.
		// 2. WSAD externalization pattern which relies on a static method, and can be
		//    resolved on the target VM.
		// Put a bandAid for the time being...
		boolean sGetString = false ;  // is there a static getString() method
		String className = initString.substring(0,indexOfGetString) ;
		org.eclipse.jem.java.JavaHelpers h = JavaRefFactory.eINSTANCE.reflectType(className,(EObject)target) ;
		if (h instanceof JavaClassImpl) {
			 JavaClassImpl clazz = (JavaClassImpl) h ;	
			 for (Iterator itr=clazz.getMethodsExtended().iterator(); itr.hasNext();) {			 	
			 	org.eclipse.jem.java.Method m = (org.eclipse.jem.java.Method) itr.next() ;			 	
			 	if (m.isStatic() && m.getName().equals("getString")) { //$NON-NLS-1$
			 		org.eclipse.emf.common.util.EList params = m.getParameters() ;
			 		if (params.size() == 1) {
			 			JavaParameter p = (JavaParameter) params.get(0) ;
			 			if (p.getJavaType().getJavaName().equals("java.lang.String")) { //$NON-NLS-1$
			 				sGetString = true ;
			 				break ;
			 			}
			 		}
			 	}
			 }
		}		
		if (!sGetString) {
			String bundleKey = initString.substring(indexOfGetString + 12, initString.length() - 2);
			// Having got the string we need to put some brackets around it as well as some quotes
			// The brackets are designed to let the user know the string came from a bundle.
			// We also store this in a field just in case we want to reference it from the CellEditor later
			isExternalized = true;
			StringBuffer buffer = new StringBuffer();
			buffer.append('"');
			buffer.append('{');
			buffer.append(bundleKey);
			buffer.append('}');
			buffer.append('"');
			return instantiateWithString(targetClass, buffer.toString());
		}
	}
	isExternalized = false;

	// TODO Need to fix this compile error... for now just return null
	return null;
//	return super.instantiateWithString(targetClass,initString);
}
}
