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
package org.eclipse.ve.internal.java.codegen.java.rules;
/*
 *  $RCSfile: ThisReferenceRule.java,v $
 *  $Revision: 1.12 $  $Date: 2005-08-24 23:30:48 $ 
 */

import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.java.*;

import org.eclipse.ve.internal.cde.rules.IRuleRegistry;

import org.eclipse.ve.internal.java.codegen.java.ISourceVisitor;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;

public class ThisReferenceRule implements IThisReferenceRule {

	public final static String COMPONENT_CLASS = "java.awt.Component"; //$NON-NLS-1$
	public final static String APPLET_CLASS = "java.applet.Applet"; //$NON-NLS-1$

	public final static int COMPONENT_CLASS_INDEX = 0;
	public final static int APPLET_CLASS_INDEX = 1;
	public final static int DEFAULT_CLASS_INDEX = COMPONENT_CLASS_INDEX;
	public final static String[] INIT_METHOD_NAME = { "initialize", "init" }; //$NON-NLS-1$ //$NON-NLS-2$
	public final static String[] INIT_METHOD_MODIFIERS = { "private", "public" }; //$NON-NLS-1$ //$NON-NLS-2$

	// The following are list of sf that should not determine if to have a "this" part
	public final static String[] ignoredAttributes = new String[] { "allocation", "implicit", "instantiateUsing", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		"serializeData", "class", "listeners", "events" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

	public ISourceVisitor overideThisReferenceVisit(MethodDeclaration mDecl, MethodInvocation exp, IBeanDeclModel model) {
		return null;
	}

	protected boolean ignoreAttribute(String att) {
		if (att == null)
			return true;
		for (int i = 0; i < ignoredAttributes.length; i++) {
			if (att.equals(ignoredAttributes[i]))
				return true;
		}
		return false;
	}

	public boolean shouldProcess(MethodDeclaration method, MethodInvocation stmt, ITypeHierarchy h) {

		boolean isThisApplet = false;
		IType[] classes = h.getAllClasses();
		for (int i = 0; i < classes.length; i++) {
			if (classes[i].getFullyQualifiedName().equals(APPLET_CLASS)){
				isThisApplet = true;
				break;
			}
		}
		
		// VAJ's initialization method
		String sel = method.getName().getIdentifier();
		for (int i = 0; i < INIT_METHOD_NAME.length; i++)
			if (sel.equals(INIT_METHOD_NAME[i])){
				if(i==APPLET_CLASS_INDEX){ // Is this method name 'init' ?
					if(isThisApplet) // Is this class an applet ?
						return true; // method name is 'init' and this is applet
					else
						return false; // method name is 'init' and this is NOT an applet
				}
				if(isThisApplet)
					return false; // Applets wont take any other init method except 'init'
				return true;
			}

		String initMethods[] = VCEPrefContributor.getMethodsFromStore();
		for (int i = 0; i < initMethods.length; i++)
			if (sel.equals(initMethods[i]))
				return true;

		return false;
	}

	public String[] getThisInitMethodName(ITypeHierarchy h) {
		IType[] classes = h.getAllClasses();
		for (int i = 0; i < classes.length; i++) {
			if (classes[i].getFullyQualifiedName().equals(APPLET_CLASS))
				return new String[] { INIT_METHOD_NAME[APPLET_CLASS_INDEX], INIT_METHOD_MODIFIERS[APPLET_CLASS_INDEX] };
		}
		return new String[] { INIT_METHOD_NAME[DEFAULT_CLASS_INDEX], INIT_METHOD_MODIFIERS[DEFAULT_CLASS_INDEX] };
	}

	/*
	 * @see IThisReferenceRule#useInheritance(ITypeHierarchy)
	 */
	public boolean useInheritance(String superClass, ResourceSet rs) {

		JavaHelpers clazz = JavaRefFactory.eINSTANCE.reflectType(superClass, rs);
		EList feature = ((JavaClass) clazz).getAllProperties();
		for (Iterator iterator = feature.iterator(); iterator.hasNext();) {
			EStructuralFeature f = (EStructuralFeature) iterator.next();
			if (f.isTransient())
				continue;
			if (ignoreAttribute(f.getName()))
				continue;
			// Check to see if the attribute is a design time attribute
			PropertyDecorator pd = Utilities.getPropertyDecorator(f);
			if (pd != null) {
				if (pd.isSetDesignTime()) {
					if (!pd.isDesignTime())
						continue;
				}
				if (pd.getField() != null)
					continue;
			}
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.rules.IRule#setRegistry(org.eclipse.ve.internal.cde.rules.IRuleRegistry)
	 */
	public void setRegistry(IRuleRegistry registry) {
		// no-op. We don't care.
	}

}
