package org.eclipse.ve.internal.jfc.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JLayeredPaneProxyAdapter.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:42:05 $ 
 */
import java.util.*;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;
import org.eclipse.jem.internal.proxy.core.IArrayBeanProxy;
import org.eclipse.jem.internal.proxy.core.ThrowableProxy;

/**
 * PRoxy adapter for JLayeredPane
 * @author richkulp
 */
public class JLayeredPaneProxyAdapter extends ContainerProxyAdapter {
		

	protected boolean addingAll = false;
	/**
	 * Constructor for JLayeredPaneProxyAdapter.
	 * @param domain
	 */
	public JLayeredPaneProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}
	
	
	
	/**
	 * Return the current order of the children, as the mof objects.
	 */
	public List getCurrentOrder() {
		List children = Collections.EMPTY_LIST;
		if (getErrorStatus() != ERROR_SEVERE) {
			instantiateBeanProxy();	// Make sure instantiated, else this will fail.
			IArrayBeanProxy childrenArray = BeanAwtUtilities.invoke_getComponents(getBeanProxy());
			int clen = childrenArray.getLength();
			// The order of paint is reverse of GEF, awt paints from end to first, while gef paints
			// first to end. So we will reverse it here.
			children = new ArrayList(clen);
			for (int i=clen-1; 0<=i; i--) {
				try {
					children.add(childrenArray.get(i));
				} catch (ThrowableProxy e) {
				}
			}
			childrenArray.getProxyFactoryRegistry().getBeanProxyFactory().releaseProxy(childrenArray);
		}
		
		List constraintComponentsEMF = (List) getEObject().eGet(sfContainerComponents);
		EObject[] childrenEMF = new EObject[constraintComponentsEMF.size()];
		int notfound = childrenEMF.length;
		for (Iterator iter = constraintComponentsEMF.iterator(); iter.hasNext();) {
			EObject element = (EObject) iter.next();
			EObject child = (EObject) element.eGet(sfConstraintComponent);
			if (child != null) {
				IBeanProxyHost host = (IBeanProxyHost) EcoreUtil.getExistingAdapter(child,IBeanProxyHost.BEAN_PROXY_TYPE);
				if (host != null) {
					int childIndex = children.indexOf(host.getBeanProxy());
					if (childIndex != -1) {
						childrenEMF[childIndex] = child;
						continue;
					}
				}
				childrenEMF[--notfound] = child;	// Not found, so add at the end, in reverse order, just so we don't lose it.
			}
		}
		
		return Arrays.asList(childrenEMF);
	}

	/**
	 * @see org.eclipse.ve.internal.jfc.core.ContainerProxyAdapter#addComponentWithConstraint(EObject, IJavaInstance, int)
	 */
	protected void addComponentWithConstraint(EObject aConstraintComponent, IJavaInstance constraintAttributeValue, int position, boolean constraintSet)
		throws ReinstantiationNeeded {
		if (addingAll) 
			super.addComponentWithConstraint(aConstraintComponent, constraintAttributeValue, position, constraintSet);	// Go ahead do it, we are in rebuild.		
		else
			readdAll(false);
	}
	
	/*
	 * Need to readd all of them. Any add requires do from top.
	 */
	protected void readdAll(boolean testValidity) {
		addingAll = true;
		try {
			BeanAwtUtilities.invoke_removeAll_Components(getBeanProxy());
			super.appliedList(sfContainerComponents, (List) getEObject().eGet(sfContainerComponents), Notification.NO_INDEX, testValidity);
		} finally {
			addingAll = false;
		}
	}

	/**
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#applyAllSettings()
	 */
	protected void applyAllSettings() {
		// If we are applying all settings, then we are doing an add all.
		addingAll = true;
		try {
			super.applyAllSettings();
		} finally {
			addingAll = false;
		}
	}

	/**
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#appliedList(EStructuralFeature, List, int, boolean)
	 */
	protected void appliedList(EStructuralFeature sf, List newValues, int position, boolean testValidity) {
		if (sf != sfContainerComponents || addingAll)
			super.appliedList(sf, newValues, position, testValidity);
		else if (!addingAll) {
			readdAll(testValidity);	// Not adding all, so we need to re add all, but pass in the validity test.
		}
	}

}
