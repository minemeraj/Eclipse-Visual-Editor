/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: BeanValidator.java,v $
 *  $Revision: 1.1 $  $Date: 2004-06-29 18:20:23 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.util.*;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.java.core.IBeanValidator.ErrorListener;
 
/**
 * 
 * @since 1.0.0
 */
public class BeanValidator implements IBeanValidator {

	protected List errors = new ArrayList(1);
	protected IJavaInstance bean;
	private IBeanProxyHost beanProxyHost;
	protected List fErrorListeners;
	
	public void reset() {
		errors = new ArrayList(1);
	}
	public void validateAll() {
	}
	public List getErrors() {
		return errors;
	}		
	public void setJavaInstance(IJavaInstance javaInstance) {
		bean = javaInstance;
	}
	protected IBeanProxyHost getBeanProxyHost(){ 
		if(beanProxyHost == null){
			beanProxyHost = BeanProxyUtilities.getBeanProxyHost(bean);
			if(!beanProxyHost.isBeanProxyInstantiated()) beanProxyHost.instantiateBeanProxy();
		}
		return beanProxyHost;
	}
	public void validate(EStructuralFeature sf) {
		// Default behavior to just revalidate everything, subclasses can be more granular for efficiency
		reset();
		validateAll();
	}
	public void addErrorListener(ErrorListener errorListener) {
		if(fErrorListeners == null) fErrorListeners = new ArrayList(1);
		fErrorListeners.add(errorListener);
	}
	public void removeErrorListener(ErrorListener errorListener) {
		if(fErrorListeners == null) return;
		fErrorListeners.remove(errorListener);
	}
	protected void fireErrorStatusChanged(){
		if(fErrorListeners == null) return;
		Iterator iter = fErrorListeners.iterator();
		while(iter.hasNext()){
			ErrorListener errorListener = (ErrorListener)iter.next();
			errorListener.errorStatusChanged();
		}
	}
}