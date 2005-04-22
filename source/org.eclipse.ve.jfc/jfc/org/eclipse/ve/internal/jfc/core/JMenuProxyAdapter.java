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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: JMenuProxyAdapter.java,v $
 *  $Revision: 1.7 $  $Date: 2005-04-22 20:57:54 $ 
 */

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.jem.internal.proxy.core.*;

/**
 * @author pwalker
 *
 * Proxy adapter for JMenu. The correct structural feature is
 * retrieved based on "items" SF for the specific type. see getSFItems().
 *
 */
public class JMenuProxyAdapter extends ComponentProxyAdapter {
	protected EStructuralFeature sfItems;
	protected JavaClass classComponent,
							classAction,
							classString;
	protected IMethodProxy fAddActionMethodProxy,
							fAddStringMethodProxy;

	/**
	 * Constructor for JMenuProxyAdapter.
	 * @param domain
	 */
	public JMenuProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());
		classComponent = Utilities.getJavaClass("java.awt.Component", rset); //$NON-NLS-1$
		classAction = Utilities.getJavaClass("javax.swing.Action", rset); //$NON-NLS-1$
		classString = Utilities.getJavaClass("java.lang.String", rset); //$NON-NLS-1$
	}
	
	protected void applied(EStructuralFeature as, Object newValue, int position) {

		if (as == getSFItems()) {
			addComponent((EObject) newValue, position);
		} else {
			super.applied(as, newValue, position);
		}
	}
	protected void canceled(EStructuralFeature sf, Object oldValue, int position) {

		if (sf == getSFItems()) {
			removeComponent((EObject) oldValue);
		} else {
			super.canceled(sf, oldValue, position);
		}

	}
	private void addComponent(EObject aComponent, int position) {
		IBeanProxyHost componentProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) aComponent);
		// Ensure the bean is instantiated
		componentProxyHost.instantiateBeanProxy();
		// It is possible the component didn't instantiate.  We then can't apply it
		// and we should flag it as having a warning error
		if (componentProxyHost.getErrorStatus() == IBeanProxyHost.ERROR_SEVERE
			|| getErrorStatus() == IBeanProxyHost.ERROR_SEVERE) {
			// Either we or the setting could not be instantiated, so don't add.
			// Need to get the ability to add an error for a structural feature against a single attribute setting
			return;
		}
		IBeanProxy componentBeanProxy = componentProxyHost.getBeanProxy();
		IBeanProxy beforeBeanProxy = null; // The beanproxy to go before.

		if (position != -1)
			beforeBeanProxy = getBeanProxyAt(position + 1);
		// Need to do +1 because we (componentBeanProxy) are already at that position in the EMF list. So we want to go before next guy.

		try {
			// Invoke a method to add the menuitem to the JMenu.
			if (classComponent.isInstance(aComponent)) {
				if (beforeBeanProxy != null) {
					BeanAwtUtilities.invoke_add_Component_before(getBeanProxy(), componentBeanProxy, beforeBeanProxy);
				} else {
					BeanAwtUtilities.invoke_add_Component(getBeanProxy(), componentBeanProxy);
				}
			} else if(classAction.isInstance(aComponent)) {
				IMethodProxy addActionMethodProxy = getAddActionMethodProxy();
				if (addActionMethodProxy != null) {
					addActionMethodProxy.invoke(getBeanProxy(), componentBeanProxy);				
				}
				// Invoke the method to add a javax.swing.Action to the JMenu.
			} else if(classString.isInstance(aComponent)) {
				// Invoke the method to add a java.lang.String to the JMenu.
				IMethodProxy addStringMethodProxy = getAddStringMethodProxy();
				if (addStringMethodProxy != null) {
					addStringMethodProxy.invoke(getBeanProxy(), componentBeanProxy);				
				}
			}
		} catch (ThrowableProxy e) {
			processError(sfItems, e, aComponent);
		}
		
		// Now that we've added it, set the parent component.
		if (classComponent.isInstance(aComponent)) {
			IComponentProxyHost componentAdapter = (IComponentProxyHost) componentProxyHost;
			componentAdapter.setParentComponentProxyHost((IComponentProxyHost) this);
		}

		revalidateBeanProxy();
		
		clearError(sfItems, aComponent);		

	}
	/*
	 * Return the first instantiated menuitem bean proxy at or after the given index.
	 * It is assumed that tests for the menu being instantiated has already been done.
	 * Return null if not found.
	 */
	protected IBeanProxy getBeanProxyAt(int position) {
		List menuitems = (List) ((EObject) getTarget()).eGet(getSFItems());
		for (int i=position; i<menuitems.size(); i++) {
			EObject menuitem = (EObject) menuitems.get(i);
			IBeanProxyHost componentProxyHost =	BeanProxyUtilities.getBeanProxyHost((IJavaInstance) menuitem);
			if (componentProxyHost.isBeanProxyInstantiated())
				return componentProxyHost.getBeanProxy();
		}
		
		return null;
	}

	/*
	 * Return the "items" structural feature for this component which could
	 * be a JMenu or JPopupMenu.
	 */
	protected EStructuralFeature getSFItems() {
		if (sfItems == null) {
			JavaClass modelType = (JavaClass) ((EObject) getTarget()).eClass();
			sfItems = modelType.getEStructuralFeature("items"); //$NON-NLS-1$
		}
		return sfItems;
	}
	
	protected void removeComponent(EObject aComponent) {
		clearError(getSFItems(), aComponent);
		
		// The component to actually remove is within the ConstraintComponent too.
		IBeanProxyHost aComponentBeanProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) aComponent);

		// It is possible the component or ourselves didn't instantiate.  We then can't cancel it.
		if (aComponentBeanProxyHost.getErrorStatus() != IBeanProxyHost.ERROR_SEVERE && getErrorStatus() != IBeanProxyHost.ERROR_SEVERE) {
			if (classComponent.isInstance(aComponent))
				BeanAwtUtilities.invoke_remove_Component(getBeanProxy(), aComponentBeanProxyHost.getBeanProxy());
			else if (classString.isInstance(aComponent))
				BeanAwtUtilities.invoke_jmenu_remove_jmenuitem_string(getBeanProxy(), (IStringBeanProxy) aComponentBeanProxyHost.getBeanProxy());
			else if (classAction.isInstance(aComponent))
				BeanAwtUtilities.invoke_jmenu_remove_jmenuitem_action(getBeanProxy(), aComponentBeanProxyHost.getBeanProxy());
			
			aComponentBeanProxyHost.releaseBeanProxy();
			// This is required because AWT will not invalidate and relayout the container
			revalidateBeanProxy();
		}
	}
	protected IMethodProxy getAddActionMethodProxy() {
		if (fAddActionMethodProxy == null) {
			fAddActionMethodProxy = getBeanProxy().getTypeProxy().getMethodProxy("add", "javax.swing.Action");	//$NON-NLS-1$ //$NON-NLS-2$
		}
		return fAddActionMethodProxy;
	}
	protected IMethodProxy getAddStringMethodProxy() {
		if (fAddStringMethodProxy == null) {
			fAddStringMethodProxy = getBeanProxy().getTypeProxy().getMethodProxy("add", "java.lang.String");	//$NON-NLS-1$ //$NON-NLS-2$
		}
		return fAddStringMethodProxy;
	}
	/**
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#releaseBeanProxy()
	 */
	public void releaseBeanProxy() {
		super.releaseBeanProxy();
		
		fAddActionMethodProxy = null;
		fAddStringMethodProxy = null;
	}

}
