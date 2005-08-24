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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: BoxLayoutPolicyFactory.java,v $
 *  $Revision: 1.14 $  $Date: 2005-08-24 23:38:09 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPolicy;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.java.*;

import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.visual.*;

public class BoxLayoutPolicyFactory implements ILayoutPolicyFactory {

	public BoxLayoutPolicyFactory() {
		super();
	}
	
	public EditPolicy getLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
		return new BoxLayoutEditPolicy(containerPolicy);
	} 
	
	public ILayoutPolicyHelper getLayoutPolicyHelper(VisualContainerPolicy ep) {
		return new FlowLayoutPolicyHelper(ep);
	}
	public ILayoutSwitcher getLayoutSwitcher(VisualContainerPolicy cp) {
		return new FlowLayoutSwitcher(cp);
	}

	public IPropertyDescriptor getConstraintPropertyDescriptor(EStructuralFeature sfConstraint) {
		return null; // No constraint, so no property descriptor.
	}

	/**
	 * Since we can't set a property for Axis on a boxlayout. 
	 * there are two possible BoxLayout's in the layout dropdown list. One for
	 * each possible axis orientation. A special object is used for each. 
	 * Use the type of object to determine the parse tree allocation for the
	 * correct construction of the BoxLayout.
	 */
	public IJavaInstance getLayoutManagerInstance(IJavaObjectInstance container, JavaHelpers javaClass, ResourceSet rset) {
		// The parse tree allocation for the object must be correct to allow for instantiation of the BoxLayout instance
		// that needs the container constructor argument
		// <allocation xsi:type="org.eclipse.jem.internal.instantiation:ParseTreeAllocation">
		//   <expression xsi:type="org.eclipse.jem.internal.instantiation:PTClassInstanceCreation" type="javax.swing.BoxLayout"/>
  	    //     <arguments xsi:type="org.eclipse.jem.internal.instantiation:PTInstanceReference" object=(..container...)/>
	    //     <arguments xsi:type="org.eclipse.jem.internal.instantiation:PTFieldAccess" field="X_AXIS>
	    //       <receiver xsi:type="org.eclipse.jem.internal.instantiation:PTName" name="javax.swing.BoxLayout"/>
	    //     </arguments>
	    //   </expression>	  			
		// </allocation>

		// Class Creation tree - new javax.swing.BoxLayout()
		PTClassInstanceCreation ic = InstantiationFactory.eINSTANCE.createPTClassInstanceCreation() ;
		ic.setType("javax.swing.BoxLayout") ; //$NON-NLS-1$
		
		// set the arguments
		PTInstanceReference ir = InstantiationFactory.eINSTANCE.createPTInstanceReference() ;
		ir.setObject(container) ;	
		PTFieldAccess fa = InstantiationFactory.eINSTANCE.createPTFieldAccess();	
		PTName name = InstantiationFactory.eINSTANCE.createPTName("javax.swing.BoxLayout") ; //$NON-NLS-1$
		if (javaClass.getName().equals("BoxLayoutY_Axis")) { //$NON-NLS-1$
			fa.setField("Y_AXIS"); //$NON-NLS-1$ 
		} else {
			fa.setField("X_AXIS");	// default to X_AXIS //$NON-NLS-1$
		}		
		fa.setReceiver(name) ;
			
		ic.getArguments().add(ir);
		ic.getArguments().add(fa) ;
		
		JavaAllocation alloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation(ic);	
		JavaHelpers boxLayoutJavaClass = JavaRefFactory.eINSTANCE.reflectType("javax.swing.BoxLayout", rset); //$NON-NLS-1$		
		
		return BeanUtilities.createJavaObject(boxLayoutJavaClass, rset, alloc);
	}

	public JavaClass getConstraintClass(ResourceSet rSet) {
		return null;
	}
}
