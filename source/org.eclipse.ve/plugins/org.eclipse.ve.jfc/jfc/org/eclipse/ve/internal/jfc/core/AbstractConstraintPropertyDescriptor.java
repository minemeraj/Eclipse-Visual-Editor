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
 *  $RCSfile: AbstractConstraintPropertyDescriptor.java,v $
 *  $Revision: 1.9 $  $Date: 2005-06-22 14:53:04 $ 
 */
import java.util.logging.Level;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.*;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator;
import org.eclipse.ve.internal.cde.decorators.DecoratorsPackage;
import org.eclipse.ve.internal.cde.emf.ClassDecoratorFeatureAccess;
import org.eclipse.ve.internal.cde.properties.AbstractPropertyDescriptorAdapter;

import org.eclipse.ve.internal.java.core.*;

import org.eclipse.ve.internal.propertysheet.EToolsPropertyDescriptor;
import org.eclipse.ve.internal.propertysheet.INeedData;
/**
 * This is a default base for the Constraint Property Descriptor.
 * LayoutPolicyFactories can choose to use a subclass of this,
 * or create their own if they wish.
 *
 * Subclasses will need to:
 *   1) Set nulls invalid to true if nulls are not valid as a constraint.
 */
public abstract class AbstractConstraintPropertyDescriptor extends EToolsPropertyDescriptor {
	
	/**
	 * Construct with the Constraint Structural Feature from the Constraint class.
	 */
	public AbstractConstraintPropertyDescriptor(EStructuralFeature sfConstraintConstraint) {
		super(sfConstraintConstraint, JFCMessages.ConstraintComponent_constraint);  
		setNullInvalid(false);	// By default, nulls are not invalid.
		setAlwaysIncompatible(true);	//By default, constraints are incompatible.
	}
	
	
	/**
	 * Default label provider that looks at the type of the setting and sees if there is a label
	 * provider for that type, and if there is, it will use that type. Otherwise, 
	 */
	public static class DefaultLabelProvider extends LabelProvider implements INeedData {
		protected EditDomain editDomain;
		
		public void setData(Object data) {
			this.editDomain = (EditDomain) data;
		}
		
		public String getText(Object element) {
			if (!(element instanceof EObject))
				return super.getText(element);
				
			// The default is to see if label provider has been provided for the class, and if so,
			// use it. Else just have one that does a toBeanString().
			ILabelProvider provider = null;
		
			BasePropertyDecorator bdec = (BasePropertyDecorator) ClassDecoratorFeatureAccess.getDecoratorWithFeature(((EObject) element).eClass(), BasePropertyDecorator.class, DecoratorsPackage.eINSTANCE.getBasePropertyDecorator_LabelProviderClassname());
			if (bdec != null) {
				try {
					String classNameAndData = bdec.getLabelProviderClassname();
					Class labelProviderClass = CDEPlugin.getClassFromString(classNameAndData);
					provider = AbstractPropertyDescriptorAdapter.createLabelProviderInstance(labelProviderClass, classNameAndData, null, null);
				} catch (ClassNotFoundException e) {
					// One specified, but incorrect, log it, but continue and see if we can get another way.
					JavaVEPlugin.getPlugin().getLogger().log(new Status(IStatus.WARNING, JFCVisualPlugin.getPlugin().getBundle().getSymbolicName(), 0, "", e), Level.WARNING); //$NON-NLS-1$
				}
			}
			
			if (provider != null) {
				if (provider instanceof INeedData)
					((INeedData) provider).setData(editDomain);
					
				return provider.getText(element);
			}
			
			if (element instanceof IJavaInstance) {
				// Do a toBeanString
				IBeanProxy elementProxy = BeanProxyUtilities.getBeanProxy((IJavaInstance)element, JavaEditDomainHelper.getResourceSet(editDomain));
				return elementProxy.toBeanString();
			}
			
			return super.getText(element);
		}
	}
	
	/**
	 * Return the default label provider. If the
	 * default is not sufficient, overrides should provide the appropriate one.
	 */
	public ILabelProvider getLabelProvider() {
		return new DefaultLabelProvider();
	}
}
