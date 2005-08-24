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
/*
 *  $RCSfile: JLayeredPaneProxyAdapter.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:38:09 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.java.core.*;

/**
 * Proxy adapter for JLayeredPanes.
 * 
 * @since 1.1.0
 */
public class JLayeredPaneProxyAdapter extends ContainerProxyAdapter {

	/**
	 * Constructor for JLayeredPaneProxyAdapter.
	 * 
	 * @param domain
	 */
	public JLayeredPaneProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	protected void applySetting(EStructuralFeature feature, Object value, int index, IExpression expression) {
		if (feature == sfContainerComponents && !inInstantiation()) {
			// We are not in instantiation, and we are adding a component. For this we must
			// NOT apply until the end of transaction, and then re-add all components back in the correct
			// order. JLayeredPane's insertion index DOES NOT mean z-order. This is because the
			// index is the index within the layer the component is on. It is not the overall index
			// of the component in the parent.
			layoutChanged(expression);
		} else
			super.applySetting(feature, value, index, expression);
	}

	/**
	 * Return the children (awt.components, not constraintComponents) in the order found on the vm. This is necessary because the order they show on
	 * the vm is different than the order they are added. Because of this and to get painting and clipping working correctly on the outlines in the
	 * graphical view, we need them in the live order.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public List getCurrentOrder() {
		List children = Collections.EMPTY_LIST;
		if (isBeanProxyInstantiated()) {
			processPendingLayoutChange();
			IArrayBeanProxy childrenArrayProxy = BeanAwtUtilities.invoke_getComponents(getBeanProxy());
			IBeanProxy[] childrenArray;
			try {
				childrenArray = childrenArrayProxy.getSnapshot();
			} catch (ThrowableProxy e) {
				JavaVEPlugin.log(e, Level.WARNING); // This shouldn't occur.
				childrenArray = new IBeanProxy[0];
			}
			childrenArrayProxy.getProxyFactoryRegistry().getBeanProxyFactory().releaseProxy(childrenArrayProxy);

			// The order of paint is reverse of GEF, awt paints from end to first, while gef paints
			// first to end. So when compute the order, we will compute it in reverse from this list.

			// The sort we are using O(n*2) but the number of entries will probably be small.

			// There may be more components returned from the bean proxy then there are components in the
			// EMF model. That is because we could be subclassing and there may be others we didn't add.
			List constraintComponentsEMF = (List) getEObject().eGet(sfContainerComponents);

			// To make the comparisons quicker, we will first put the entries into the array
			// as IBeanProxyHost's. And as they are found, we will convert them to the EMF object to be returned as the children.
			Object[] childrenEMF = new Object[constraintComponentsEMF.size()];
			for (int i = 0; i < constraintComponentsEMF.size(); ++i) {
				IJavaInstance child = (IJavaInstance) ((EObject) constraintComponentsEMF.get(i)).eGet(sfConstraintComponent);
				if (child != null)
					childrenEMF[i] = getSettingBeanProxyHost(child);
			}

			// The sort will be to go in reverse order through the array from the live bean, find the cooresponding bean in
			// the childrenEMF array, and swap it to the current merge point in the childrenEMF array, turning it in the EMF
			// object as we do so. This way the EMF objects will be in the reverse order from the bean proxy.
			int mergePoint = 0;
			for (int c = childrenArray.length - 1; c >= 0; c--) {
				IBeanProxy child = childrenArray[c];
				// Find the child, at or after the merge point that matches this guy.
				for (int ce = mergePoint; ce < childrenEMF.length; ce++) {
					Object childEMF = childrenEMF[ce];
					if (childEMF != null && child == ((IBeanProxyHost) childrenEMF[ce]).getBeanProxy()) {
						// We found the child. Swap it to the merge point, converting it back to the EMF object, and increment mergepoint.
						Object swap = childrenEMF[mergePoint];
						childrenEMF[mergePoint] = ((IBeanProxyHost) childrenEMF[ce]).getTarget();
						if (ce != mergePoint)
							childrenEMF[ce] = swap;
						mergePoint++;
						break; // Go on to next child from beanproxy.
					}
				}
			}

			// Finally, if mergePoint is not the same as the size of the childrenEMF array, we have some EMF components not found on bean proxy.
			// Leave them at the end but convert back to EMF objects.
			while (mergePoint < childrenEMF.length) {
				Object childEMF = childrenEMF[mergePoint];
				if (childEMF != null)
					childrenEMF[mergePoint] = ((IBeanProxyHost) childEMF).getTarget();
				mergePoint++;
			}
			children = Arrays.asList(childrenEMF);
		}
		return children;
	}
}
