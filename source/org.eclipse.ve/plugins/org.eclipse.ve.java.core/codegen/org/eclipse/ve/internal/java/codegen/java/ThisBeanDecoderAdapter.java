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
package org.eclipse.ve.internal.java.codegen.java;
/*
 *  $RCSfile: ThisBeanDecoderAdapter.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:30:44 $ 
 */

import java.util.logging.Level;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.jdt.core.IJavaElement;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.jcm.JCMMethod;
import org.eclipse.ve.internal.jcm.JCMPackage;

import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.model.CodeMethodRef;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * @version 	1.0
 * @author
 */
public class ThisBeanDecoderAdapter extends BeanDecoderAdapter {
// TODO need to create a JSFThisBeanDecoderAdapter to extend  JSFBeanDecodeAdapter
    /**
     * Constructor for ThisBeanDecoderAdapter.
     * @param target
     * @param bean
     */
    public ThisBeanDecoderAdapter(Notifier target, BeanPart bean) {
        super(bean);
    }


protected boolean isAddingElement(Notification msg) {
    
    switch (msg.getEventType()) {
        case Notification.SET:
        	if (CDEUtilities.isUnset(msg) || msg.isTouch())
        		return false;
        case Notification.ADD:
        case Notification.ADD_MANY:
              return true ;
    }
    return false ;
}

protected void createInitMethodIfNedded(Notification msg) throws CodeGenException {
    
    CodeMethodRef m = fBean.getInitMethod() ;
	if (m == null) {
		JCMMethod compMethod = (JCMMethod) InverseMaintenanceAdapter.getFirstReferencedBy((Notifier) msg.getNotifier(), JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		// It is possible that the notifier is not set up with an initialization method yet		

		BeanPartFactory bg = new BeanPartFactory(fBean.getModel(), fBean.getModel().getCompositionModel());
		m = bg.generateThisInitMethod();
		if (compMethod != null)
			m.setCompMethod(compMethod);
		else
			m.getCompMethod(); // Induce its generation
	}
    
}
    
/**
 * applied: A setting has been applied to the mof object,
 * notify the decoder
 */ 
public void notifyChanged(Notification msg){
    
    if (ignoreMsg(msg)) return ;
    if (isAddingElement(msg))
    try {
       createInitMethodIfNedded(msg) ;
    }
    catch (CodeGenException e) {
        JavaVEPlugin.log(e, Level.WARNING) ;
        return ;
    }
    super.notifyChanged(msg) ;
}

/* (non-Javadoc)
 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
 */
public Object getAdapter(Class adapter) {
	if (adapter == IJavaElement.class) {
		// TODO Need to change setFieldDeclHandler on BeanPart for "this" so that it is the 
		// appropriate decl handle for this class. However, there are currently several usages
		// of the hard-coded string now used and this will need to be looked into.
		return CodeGenUtil.getMainType(fbeanModel.getCompilationUnit());
	}
	return super.getAdapter(adapter);
}

}
