/*
 * Adapter class installed on BeanSubclassComposition that allows top level SWT controls
 * to be hosted by the target VM so they are live and their properties can be obtained
 * as well as their graphic 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.*;

import org.eclipse.jem.internal.beaninfo.adapters.*;
import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.jcm.*;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.ve.internal.java.core.*;

public class FreeFormControlHostAdapter extends AdapterImpl {

	private IBeanProxyDomain fDomain;
	protected ResourceSet rset;
	protected EClass classControl;	
	protected BeanComposition composition;

	public FreeFormControlHostAdapter(IBeanProxyDomain domain, BeanComposition aComposition) {
		fDomain = domain;
		composition = aComposition; 
	}
	private void ensureEMFDetailsCached(){
		if ( classControl == null ) 
			classControl = (EClass) Utilities.getJavaClass("org.eclipse.swt.widgets.Control", composition.eResource().getResourceSet()); //$NON-NLS-1$		
	}
	
	public void notifyChanged(Notification msg) {
		ensureEMFDetailsCached();		
		switch (msg.getEventType()) {
			case Notification.ADD :
			case Notification.SET :
				if (!CDEUtilities.isUnset(msg)) {
					applied(msg.getNewValue());
					break;
				}	// Else flow into unset
			case Notification.UNSET :
			case Notification.REMOVE :
				canceled(msg.getOldValue());
			break;
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
