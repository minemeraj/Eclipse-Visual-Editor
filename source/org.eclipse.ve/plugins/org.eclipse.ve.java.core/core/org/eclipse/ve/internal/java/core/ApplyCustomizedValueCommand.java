/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
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
 *  $RCSfile: ApplyCustomizedValueCommand.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:30:45 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.ve.internal.propertysheet.common.commands.AbstractCommand;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;

public class ApplyCustomizedValueCommand extends AbstractCommand {

	protected IJavaObjectInstance fBean;
	protected IJavaInstance newSetting;
	protected IBeanProxy oldBeanProxy;
	protected EStructuralFeature fFeature;
	protected IJavaInstance oldSetting;
	protected boolean oldIsSet;


public void setTarget(IJavaObjectInstance aBean){fBean = aBean;}
public void setFeature(EStructuralFeature aFeature){fFeature = aFeature;}
public void setOldBeanProxy(IBeanProxy aBeanProxy){oldBeanProxy = aBeanProxy;}
public void setValue(IJavaInstance aValue){newSetting = aValue;}


public void execute(){
	oldIsSet = fBean.eIsSet(fFeature);
	if (oldIsSet)
		oldSetting = (IJavaInstance)fBean.eGet(fFeature);
	else {
		// It wasn't previously set, but we've already changed the value before this command was executed, so the origSettings table is wrong
		// So we will now force in our old setting that was passed in.
		IInternalBeanProxyHost beanProxyHost = (IInternalBeanProxyHost) BeanProxyUtilities.getBeanProxyHost(fBean);
		beanProxyHost.getOriginalSettingsTable().put(fFeature, oldBeanProxy);
	}
	fBean.eSet(fFeature,newSetting);
	
}
/**
 * On undo if there was a previous value apply it
 * if not we must revert the bean to its previous value that is the oldBeanProxy
 */
public void undo(){
	
	if (oldIsSet) {
		fBean.eSet(fFeature,oldSetting);
	} else {
		fBean.eUnset(fFeature);
	}	
}
public void redo(){
	fBean.eSet(fFeature,newSetting);
}
public boolean canExecute(){
	return true;
}
}
