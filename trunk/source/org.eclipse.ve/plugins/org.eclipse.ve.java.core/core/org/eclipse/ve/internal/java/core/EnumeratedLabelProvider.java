package org.eclipse.ve.internal.java.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: EnumeratedLabelProvider.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.ve.internal.propertysheet.INeedData;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.core.*;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.internal.java.JavaClass;
import org.eclipse.jem.internal.java.JavaHelpers;

public class EnumeratedLabelProvider extends org.eclipse.jface.viewers.LabelProvider implements INeedData {
	
	protected String[] fDisplayNames;
	protected String[] fInitStrings;
	protected IBeanProxy[] fBeanProxies;
	protected JavaHelpers fFeatureType;
	protected EditDomain editDomain;

public EnumeratedLabelProvider(IArrayBeanProxy aBeanInfoValuesArray, JavaHelpers aFeatureType){
	fFeatureType = aFeatureType;
	// Iterate over the array of values, these are stored in the format
	// displayName, object, initString
	// We only care about storing the displayName and the initStrings
	int length = aBeanInfoValuesArray.getLength();
	int j=0;
	fDisplayNames = new String[length/3];
	fInitStrings = new String[length/3];
	try {
		for ( int i=0;i<length;i+=3 ) {
			fDisplayNames[j] = ((IStringBeanProxy)aBeanInfoValuesArray.get(i)).stringValue();
			fInitStrings[j] = ((IStringBeanProxy)aBeanInfoValuesArray.get(i+2)).stringValue();
			// NOTE - We cannot in any way use the object that came in from the values array
			// This came from the VM that did introspection that is NOT the same as the
			// one the editor is necessarily running in so we must re-create the
			// bean proxies from the init string each time
			j++;
		}
	} catch ( ThrowableProxy exc ){
		JavaVEPlugin.log("Unable to determine enumeration values", MsgLogger.LOG_WARNING); //$NON-NLS-1$
		JavaVEPlugin.log(exc, MsgLogger.LOG_WARNING);
	}	
}
public String getText(Object aJavaInstance){
	// Now that we have an object we have a remote VM.  If we have not turned the init
	// strings into objects do this now
	IBeanProxy valueProxy = BeanProxyUtilities.getBeanProxy((IJavaInstance)aJavaInstance, JavaEditDomainHelper.getResourceSet(editDomain));
	if ( fBeanProxies == null ) {
		// Mistake on my part, didn't get getQualifiedNameForReflection() added to JavaHelpers, so only available on JavaClass.
		String featureTypeName = null;
		if (fFeatureType instanceof JavaClass)
			featureTypeName = ((JavaClass) fFeatureType).getQualifiedNameForReflection();
		else
			featureTypeName = fFeatureType.getQualifiedName();			
		IBeanTypeProxy aBeanTypeProxy = valueProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(featureTypeName);
		fBeanProxies = new IBeanProxy[fInitStrings.length];
		int index=0;
		try {
			for(int i=0;i<fInitStrings.length;i++){
				index = i;
				fBeanProxies[i] = aBeanTypeProxy.newInstance(fInitStrings[i]);
			}
		} catch ( ThrowableProxy exc ) {
			JavaVEPlugin.log("Unable to create enumeration value for " + fInitStrings[index], MsgLogger.LOG_WARNING); //$NON-NLS-1$
			JavaVEPlugin.log(exc,MsgLogger.LOG_WARNING);
		} catch ( InstantiationException exc ) {
			JavaVEPlugin.log("Unable to create enumeration value for " + fInitStrings[index], MsgLogger.LOG_WARNING); //$NON-NLS-1$
			JavaVEPlugin.log(exc,MsgLogger.LOG_WARNING);
		}
	}
	// This can occur following an introspection error
	if ( fBeanProxies == null ) {
		return JavaMessages.getString("LabelProvider.Enumerated.getText_ERROR_"); //$NON-NLS-1$
	}
	
	// Now that we have an array of bean proxies compare the value against the entries
	for( int i=0;i<fBeanProxies.length;i++){
		if (fBeanProxies[i] == null) {
			if (valueProxy == null) {
				return fDisplayNames[i];
			}
		} else if(fBeanProxies[i].equals(valueProxy)){
			return fDisplayNames[i];
		}
	}	
	// TODO This needs more thought"); //$NON-NLS-1$
	return JavaMessages.getString("LabelProvider.Enumerated.getText_ERROR_"); //$NON-NLS-1$
}
	/**
	 * @see INeedData#setData(Object)
	 */
	public void setData(Object data) {
		editDomain = (EditDomain) data;
	}

}