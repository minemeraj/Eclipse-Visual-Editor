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
 *  $RCSfile: BoxLayoutProxyAdapter.java,v $
 *  $Revision: 1.4 $  $Date: 2005-02-15 23:42:05 $ 
 */

import org.eclipse.emf.ecore.EObject;

import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.jem.internal.proxy.core.*;

/**
 * Proxy adapter for javax.swing.BoxLayout.
 * It does not construct with a null ctor and uses a constructor with parms:
 *   BoxLayout(Container parent, int axisValue)
 */
public class BoxLayoutProxyAdapter extends BeanProxyAdapter {
	int X_AXIS = 0, Y_AXIS = 1;
	/**
	 * Constructor for BoxLayoutProxyAdapter.
	 * @param domain
	 */
	public BoxLayoutProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}
	/**
	 * The default instantiation for a BoxLayout in the JVE is with the X_AXIS
	 */
	protected IBeanProxy defaultInstantiate(IBeanTypeProxy typeClass) throws ThrowableProxy {
		try {
			return instantiateWithString(typeClass, "new javax.swing.BoxLayout(,javax.swing.BoxLayout.X_AXIS)"); //$NON-NLS-1$
		} catch (InstantiationException e) {
			return null;	// Shouldn't occur anymore
		}
	}
	/**
	 * BoxLayout requires a special ctor that takes a Container (the one it's set to) and the axis
	 * parameter which can be either X_AXIS or Y_AXIS. Since the first parm is always the parent 
	 * container, we'll skip over it and parse the axis parameter.
	 */
	protected IBeanProxy instantiateWithString(IBeanTypeProxy targetClass, String initString)
		throws ThrowableProxy, InstantiationException {
		// Since this is the initialization string for a BoxLayout, we are looking for the
		// second parm which is the axis parameter.
		int axisValue = X_AXIS; // default is X_AXIS
		int lastCommaIndex = initString.lastIndexOf(","); //$NON-NLS-1$
		if (lastCommaIndex != -1) {
			int lastParenthesisIndex = initString.lastIndexOf(")"); //$NON-NLS-1$
			if (lastParenthesisIndex != -1) {
				String axisString = initString.substring(lastCommaIndex + 1, lastParenthesisIndex).trim();
				axisString.trim();
				if (axisString.equals("0") //$NON-NLS-1$
					|| axisString.equals("X_AXIS") //$NON-NLS-1$
					|| axisString.equals("javax.swing.BoxLayout.X_AXIS")) //$NON-NLS-1$
					axisValue = X_AXIS;
				else if (
					axisString.equals("1") //$NON-NLS-1$
						|| axisString.equals("Y_AXIS") //$NON-NLS-1$
						|| axisString.equals("javax.swing.BoxLayout.Y_AXIS")) //$NON-NLS-1$
					axisValue = Y_AXIS;
			}
		}
		IConstructorProxy boxLayoutCtor =
			targetClass.getConstructorProxy(new String[] { "java.awt.Container", "int" }); //$NON-NLS-1$ //$NON-NLS-2$
		IBeanProxy beanProxy = (IBeanProxy) targetClass;
		// BoxLayout requires a special ctor that takes a Container (the one it's set to) and the axis.
		IBeanProxy containerProxy =
			BeanProxyUtilities.getBeanProxy(
				(IJavaObjectInstance) InverseMaintenanceAdapter.getFirstReferencedBy((EObject) getTarget(), JavaInstantiation.getReference((IJavaObjectInstance) getTarget(), JFCConstants.SF_CONTAINER_LAYOUT)));
		IIntegerBeanProxy axisBeanProxy =
			beanProxy.getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(axisValue);
		// Create the BoxLayout
		if (containerProxy != null && boxLayoutCtor != null && axisBeanProxy != null)
			return boxLayoutCtor.newInstance(new IBeanProxy[] { containerProxy, axisBeanProxy });

		// TODO Need to fix this compile error... for now just return null
		return null;
//		return super.instantiateWithString(targetClass, initString);
	}
}
