package org.eclipse.ve.internal.swt;
/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2002 - All Rights Reserved. 
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp. 
 */

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.*;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.ve.internal.java.core.*;
 
public class CompositeProxyAdapter extends ControlProxyAdapter {
	//TODO AWT ContainerProxyAdapter has IHoldProcessing - does this need to be part of JBCF ?
	protected EReference sf_containerControls;
	private IMethodProxy getLayoutMethodProxy;  // Field to cache the IMethodProxy for the getLayout() method on the composite

	public CompositeProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());
		sf_containerControls = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_CONTROLS);

	}

	protected void applied(EStructuralFeature as, Object newValue, int position) {

		if (as == sf_containerControls) {
			addControl((IJavaObjectInstance) newValue, position);
		} else {
			super.applied(as, newValue, position);
		}
	}
	protected void canceled(EStructuralFeature sf, Object oldValue, int position) {

		if (sf == sf_containerControls) {
			removeControl((IJavaObjectInstance)oldValue);
		} else {
			super.canceled(sf, oldValue, position);
		}

	}

	protected IBeanProxy getLayoutBeanProxy(){
		// Invoke getLayout() on the display thread
		if(getLayoutMethodProxy == null){
			getLayoutMethodProxy = getBeanProxy().getTypeProxy().getMethodProxy("getLayout");
		}
		return getEnvironmentInvoke0ArgMethodProxy().invokeCatchThrowableExceptions(getBeanProxy(),getLayoutMethodProxy);
	}
	
	protected void addControl(IJavaObjectInstance aControl, int position) throws ReinstantiationNeeded {
		//TODO We need to add a listener to the control to know when its layoutData changes to handle layouts in SWT
		// For now just deal with x and y
		IBeanProxyHost controlProxyHost = BeanProxyUtilities.getBeanProxyHost(aControl);
		
		((ControlProxyAdapter)controlProxyHost).setParentProxyHost(this);
		
		// It is possible the component didn't instantiate.  We then can't apply it
		controlProxyHost.instantiateBeanProxy();
		// This is required because SWT will not invalidate and relayout the container	
		revalidateBeanProxy();
	}

	protected void removeControl(IJavaObjectInstance aControl) throws ReinstantiationNeeded {
		// Dispose the control
		IBeanProxyHost controlProxyHost = BeanProxyUtilities.getBeanProxyHost(aControl);
		if(controlProxyHost.isBeanProxyInstantiated()){
			((ControlProxyAdapter)controlProxyHost).releaseBeanProxy();
		}
		revalidateBeanProxy();
	}
	
}