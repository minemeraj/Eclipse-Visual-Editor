package org.eclipse.ve.internal.java.visual;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ILayoutPolicyFactory.java,v $
 *  $Revision: 1.3 $  $Date: 2004-03-04 12:05:24 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPolicy;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ve.internal.cde.core.ContainerPolicy;

import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

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
 */
IJavaInstance getLayoutManagerInstance(JavaHelpers javaClass, ResourceSet rset);
}


