package org.eclipse.ve.internal.swt;
/***************************************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************/
/*
 * $RCSfile: RowLayoutEditPolicy.java,v $ $Revision: 1.3 $ $Date: 2004-03-05 20:57:04 $
 */
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
/**
 * Layout input policy for handling a SWT RowLayout and FillLayout. 
 * It'a based off the default FlowLayout for SWT and isHorizontal() 
 * returns true if the 'type' of the RowLayout is also horizontal.
 */
public class RowLayoutEditPolicy extends DefaultLayoutEditPolicy {
	private static final int HORIZONTAL = 1 << 8;
	private int type = HORIZONTAL;
	private EStructuralFeature sf_compositeLayout = null;
	/**
	 * Create with the container policy for handling DiagramFigures.
	 */
	public RowLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
		super(containerPolicy);
	}
	/**
	 * @see org.eclipse.gef.editpolicies.FlowLayoutEditPolicy#isHorizontal()
	 */
	protected boolean isHorizontal() {
		return type == HORIZONTAL;
	}
	/**
	 * Determine the RowLayout's type... horizontal or vertical
	 */
	private void determineType() {
		Object layout = ((EObject) containerPolicy.getContainer()).eGet(sf_compositeLayout);
		if (layout != null && layout instanceof IJavaObjectInstance) {
			IBeanProxyHost layoutProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) layout);
			IBeanProxy layoutProxy = layoutProxyHost.getBeanProxy();
			if (layoutProxy != null) {
				IFieldProxy fieldProxy = layoutProxy.getTypeProxy().getFieldProxy("type"); //$NON-NLS-1$
				if (fieldProxy != null) {
					try {
						IIntegerBeanProxy intProxy = (IIntegerBeanProxy) fieldProxy.get(layoutProxy);
						if (intProxy != null) {
							type = intProxy.intValue();
						}
					} catch (ThrowableProxy e) {
					}
				}
			}
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#activate()
	 */
	public void activate() {
		super.activate();
		// Get the layout structural feature from the target container
		sf_compositeLayout = JavaInstantiation.getSFeature((IJavaObjectInstance) containerPolicy.getContainer(),
				SWTConstants.SF_COMPOSITE_LAYOUT);
		determineType();
	}
}
