package org.eclipse.ve.internal.swt;
/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2002 - All Rights Reserved. 
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp. 
 */

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.*;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;

import org.eclipse.ve.internal.java.core.*;
 
public class CompositeProxyAdapter extends ControlProxyAdapter {
	//TODO AWT ContainerProxyAdapter has IHoldProcessing - does this need to be part of JBCF ?
	protected EReference sf_containerControls;
	private IMethodProxy getLayoutMethodProxy;  // Field to cache the IMethodProxy for the getLayout() method on the composite
	private IMethodProxy layoutMethodProxy;  // Field for method proxy to layout();

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
		return (IBeanProxy) invokeSyncExecCatchThrowableExceptions(new DisplayManager.DisplayRunnable() {
			public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
				return getLayoutMethodProxy.invoke(getBeanProxy());
			}
		}); 
	}
	
	protected IMethodProxy layoutMethodProxy(){
		if(layoutMethodProxy == null){
			layoutMethodProxy = getBeanProxy().getTypeProxy().getMethodProxy("layout");
		}
		return layoutMethodProxy;
	}
	
	protected void addControl(IJavaObjectInstance aControl, int position) throws ReinstantiationNeeded {
		
		// Whenever a control is added unless it is at the end there is no way to insert it at a particular
		// position, so the existing controls before it must all be disposed 
		// Then add in the new control and instantiate everyone behind him to maintain target VM order
		List controls = (List) ((IJavaObjectInstance)getTarget()).eGet(sf_containerControls);
		Iterator iter = controls.iterator();
		int controlCount = 0;
		while(iter.hasNext()){
			// If the number of times we have iterated over controls > position we are adding continue
			if(controlCount++ < position) continue;			
			IJavaObjectInstance control = (IJavaObjectInstance)iter.next();
			IBeanProxyHost controlProxyHost = BeanProxyUtilities.getBeanProxyHost(control);
			((ControlProxyAdapter)controlProxyHost).setParentProxyHost(this);
			// Release the bean proxy and then instantiate it.  This is because we are inserting at a position
			// and any already existing controls must be added in the corrected order
			controlProxyHost.releaseBeanProxy();
			controlProxyHost.instantiateBeanProxy();
		}		
		childValidated(this);
	}

	protected void removeControl(IJavaObjectInstance aControl) throws ReinstantiationNeeded {
		// Dispose the control
		IBeanProxyHost controlProxyHost = BeanProxyUtilities.getBeanProxyHost(aControl);
		if(controlProxyHost.isBeanProxyInstantiated()){
			((ControlProxyAdapter)controlProxyHost).releaseBeanProxy();
		}
		revalidateBeanProxy();
	}
	
	public void childValidated(ControlProxyAdapter childProxy) {
		if(parentProxyAdapter != null){
			super.childValidated(childProxy);
		} else {
			try {
				// We are the top with no parents, do a layout() on us
				invokeSyncExec(new DisplayManager.DisplayRunnable() {
					public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
						// Call the layout() method
						return layoutMethodProxy().invoke(getBeanProxy());
					}
				});
			} catch (ThrowableProxy e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}	
}