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
package org.eclipse.ve.internal.jfc.codegen;
/*
 *  $RCSfile: JFCBeanDecoderAdapter.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:12 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.codegen.java.BeanDecoderAdapter;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;

/**
 *  This Adapter represents the VCE model listener for a component.
 *  It will add/remove sub-components, and associated expressions
 */

public class JFCBeanDecoderAdapter extends BeanDecoderAdapter  {
	  


/**
 * @param bean
 */
public JFCBeanDecoderAdapter(BeanPart bean) {
	super(bean);
}

protected IJavaObjectInstance getComponentFromSpecialRoot(EObject root) {
	IJavaObjectInstance comp = super.getComponentFromSpecialRoot(root);
	if (comp==null)
		if (CodeGenUtil.isTabPaneComponentValue(root))
	      return (IJavaObjectInstance) root.eGet(
	                   root.eClass().
	                   getEStructuralFeature(JTabbedPaneAddDecoderHelper.COMPONENT_ATTR_NAME)) ; 
    return null ;      
}


}
