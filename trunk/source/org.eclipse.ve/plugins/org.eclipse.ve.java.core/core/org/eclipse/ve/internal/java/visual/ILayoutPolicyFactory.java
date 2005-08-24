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
package org.eclipse.ve.internal.java.visual;
/*
 *  $RCSfile: ILayoutPolicyFactory.java,v $
 *  $Revision: 1.9 $  $Date: 2005-08-24 23:30:47 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPolicy;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaHelpers;

/**
 * This interface provides one MOF extension point from which to get
 * all the classes needed for a particular Layout manager such as the 
 * property editor, constraint converter, layout input policy, etc..
 * Creation date: (10/23/00 2:25:11 PM)
 * @author: Peter Walker
 */
public interface ILayoutPolicyFactory {
	
public static final String LAYOUT_POLICY_FACTORY_CLASSNAME_KEY = "org.eclipse.ve.internal.jfc.core.layoutpolicyfactoryclassnamekey"; //$NON-NLS-1$	
/**
 * Return the constraint converter for this Layout manager
 * Creation date: (10/23/00 2:25:22 PM)
 */
ILayoutSwitcher getLayoutSwitcher(VisualContainerPolicy ep);
/**
 * Return the GEF visual edit policy for this Layout manager.
 */
EditPolicy getLayoutEditPolicy(VisualContainerPolicy containerPolicy);

/**
 * Return the layout policy helper for this Layout manager.
 * Creation date: (10/23/00 2:25:22 PM)
 */
ILayoutPolicyHelper getLayoutPolicyHelper(VisualContainerPolicy ep);

/**
 * Return the propertyDescriptor to use for the constraint 
 * property for this kind of layout. Return null if there
 * is no constraint property for this kind of layout manager.
 */
IPropertyDescriptor getConstraintPropertyDescriptor(EStructuralFeature sfConstraint);

/**
 * Return a default LayoutManager instance. 
 * Used by the LayoutManagerCellEditor. The javaclass allows subclasses to be created too.
 * The container argument is because some layout manager construction (such as BorderLayout) requires
 * the actual parent container to be passed in
 */
IJavaInstance getLayoutManagerInstance(IJavaObjectInstance container, JavaHelpers javaClass, ResourceSet rset);

/**
 * @param ResourceSet for EMF
 * @return the class of the Constraint for the layout manager
 */
JavaClass getConstraintClass(ResourceSet rSet);

}


