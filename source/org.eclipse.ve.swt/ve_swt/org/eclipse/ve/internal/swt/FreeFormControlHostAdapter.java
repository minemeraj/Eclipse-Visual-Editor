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
/*
 * Adapter class installed on BeanSubclassComposition that allows top level SWT controls
 * to be hosted by the target VM so they are live and their properties can be obtained
 * as well as their graphic 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.cde.core.CDEUtilities;

import org.eclipse.ve.internal.jcm.*;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.IBeanProxyDomain;

public class FreeFormControlHostAdapter extends AdapterImpl {

	protected ResourceSet rset;
	protected EClass classControl;	
	protected BeanComposition composition;

	public FreeFormControlHostAdapter(IBeanProxyDomain domain, BeanComposition aComposition) {
		composition = aComposition; 
	}
	private void ensureEMFDetailsCached(){
		if ( classControl == null ) 
			classControl = Utilities.getJavaClass("org.eclipse.swt.widgets.Control", composition.eResource().getResourceSet()); //$NON-NLS-1$		
	}
	
	public void notifyChanged(Notification msg) {
		int fid = msg.getFeatureID(MemberContainer.class);
		if (fid != JCMPackage.MEMBER_CONTAINER__MEMBERS && fid != JCMPackage.MEMBER_CONTAINER__PROPERTIES) {
			// Members and properties relationships will be handled by their parent (or by components or this part if free form).
			ensureEMFDetailsCached();
			switch (msg.getEventType()) {
				case Notification.ADD :
				case Notification.SET :
					if (!CDEUtilities.isUnset(msg)) {
						applied(msg.getNewValue());
						break;
					} // Else flow into unset
				case Notification.UNSET :
				case Notification.REMOVE :
					canceled(msg.getOldValue());
					break;
			}
		} 
	}			
	
	protected void applied(Object newValue) {
		// Only deal with SWT controls		
		if (classControl.isInstance(newValue)) {
			((ControlProxyAdapter)BeanProxyUtilities.getBeanProxyHost((IJavaInstance)newValue)).instantiateBeanProxy();		
		}
	}	
	
	protected void add(ControlProxyAdapter aControlProxyHost) {
		// The control proxy host is instantiated within the context of the free form as its parent
		// For instantiation of the bean proxy
		aControlProxyHost.instantiateBeanProxy();
	}
	protected void canceled(Object value) {
		// The control proxy host is instantiated within the context of the free form as its parent
		// For instantiation of the bean proxy
		if (classControl.isInstance(value)){
			((ControlProxyAdapter)BeanProxyUtilities.getBeanProxyHost((IJavaInstance)value)).releaseBeanProxy();					
		}
	}	

	public boolean isAdapterForType(Object type) {
		return type == this.getClass();
	}

	

}
