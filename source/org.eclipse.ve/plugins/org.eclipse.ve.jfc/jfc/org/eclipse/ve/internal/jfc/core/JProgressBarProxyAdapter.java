/*
 * Created on Jun 12, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.eclipse.ve.internal.jfc.core;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JProgressBarProxyAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.IBeanProxyDomain;

/**
 * @author sri
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JProgressBarProxyAdapter extends ComponentProxyAdapter {

	protected EStructuralFeature sfString;
	/**
	 * @param domain
	 */
	public JProgressBarProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());
		sfString = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JPROGRESSBAR_STRING);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#canceled(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int)
	 */
	protected void canceled(EStructuralFeature as, Object oldValue, int position) {
		// When 'restoring default' the 'string' sf, it sets an incorrect
		//  value instead of setting to the value of the 'value' sf.
		if (as == sfString && !inInstantiation())
			throw new ReinstantiationNeeded();
		super.canceled(as, oldValue, position);
	}

}
