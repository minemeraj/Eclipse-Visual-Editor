package org.eclipse.ve.internal.java.codegen.java.rules;
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
 *  $RCSfile: InstanceVariableCreationRule.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-12 21:44:11 $ 
 */
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.*;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;
import org.eclipse.ve.internal.cde.rules.IRuleRegistry;
import org.eclipse.ve.internal.cdm.Annotation;
import org.eclipse.jem.internal.java.JavaHelpers;
import org.eclipse.jem.internal.java.impl.JavaClassImpl;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;

import org.eclipse.ve.internal.java.vce.VCEPreferences;
import org.eclipse.ve.internal.jcm.BeanSubclassComposition;

public class InstanceVariableCreationRule implements IInstanceVariableCreationRule {

	// The following types will not use the ivj Prefix, and will not use a declaration JCMMethod.
	public static final String[] internalTypes = { "java.awt.GridBagConstraints", //$NON-NLS-1$
		"java.awt.LayoutManager", //$NON-NLS-1$
		"javax.swing.text.Caret" }; //$NON-NLS-1$
	public static ArrayList internalHelpers = null;
	private static ResourceSet fRS = null;
	public static final String[] internalPrefix = { "cons", //$NON-NLS-1$
		"lay", //$NON-NLS-1$
		"caret" }; //$NON-NLS-1$
	public static int internalIndex = 1;
	public static Preferences fPrefStore = null;
	public static JavaHelpers fComponentMeta = null;

	private static List getMetaTypes(ResourceSet rs) {

		if (fRS != null && fRS.equals(rs))
			return internalHelpers;

		ArrayList a = new ArrayList();
		org.eclipse.ve.internal.java.core.JavaVEPlugin.log("InstanceVariableCreationRule: loading cache", org.eclipse.jem.internal.core.MsgLogger.LOG_FINE); //$NON-NLS-1$
		for (int i = 0; i < internalTypes.length; i++) {
			JavaHelpers sType = JavaClassImpl.reflect(internalTypes[i], rs);
			if (sType == null)
				throw new IllegalArgumentException("Invalid Type"); //$NON-NLS-1$
			a.add(sType);
		}
		internalHelpers = a;

		fComponentMeta = JavaClassImpl.reflect("java.awt.Component", rs); //$NON-NLS-1$

		fRS = rs;
		return internalHelpers;
	}

	/**
	 * It the type is/extends any element in internalTypes, than it should be an internally
	 * defined class. If the parent of the input is the BeanSubclassComposition, then it 
	 * should not be internal. 
	 */
	private boolean isInternalType(EObject obj, ResourceSet rs) {

		EClassifier meta = (EClassifier) ((IJavaInstance) obj).getJavaType();
		boolean result = false;
		for (int i = 0; i < internalTypes.length; i++)
			if (((JavaHelpers) (getMetaTypes(rs).get(i))).isAssignableFrom(meta)) {
				result = true;
			}
		if (result) {
			EObject parent = obj.eContainer();
			if (parent instanceof BeanSubclassComposition)
				result = false;
		}
		return result;

	}

	public static boolean usePrefix(EClassifier meta, ResourceSet rs) {
		if (rs == null || meta == null)
			return true;
		return (!getComponentMeta(rs).isAssignableFrom((EClassifier) meta));
	}

	/**
	 * Get the default prefix name for a given object type.
	 */
	public static String getPrefix(EClassifier meta, ResourceSet rs) {

		for (int i = 0; i < internalTypes.length; i++)
			if (((JavaHelpers) (getMetaTypes(rs).get(i))).isAssignableFrom(meta)) {
				return internalPrefix[i];
			}

		if (usePrefix(meta, rs))
			return DEFAULT_VAR_PREFIX;
		else
			return ""; //$NON-NLS-1$
	}

	public static String addPrefix(String pre, String name) {
		if (pre == null || pre.length() == 0)
			return name;
		StringBuffer sb = new StringBuffer(pre);
		sb.append(Character.toUpperCase(name.charAt(0)));
		sb.append(name.substring(1));
		return sb.toString();
	}

	/**
	 *  
	 *  
	 */
	public String getInstanceVariableName(EObject obj, IType currentType, IDiagramModelInstance cm, IBeanDeclModel bdm) {

		String name = null;
		String nameEntry = null;

		// Try to get its annotated value 
		Annotation a = CodeGenUtil.getAnnotation(obj);
		if (a != null) {
			nameEntry = name = (String) a.getKeyedValues().get(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
		}

		if (name == null) {
			name = ((IJavaObjectInstance) obj).getJavaType().getJavaName();
			if (name.indexOf('.') > 0)
				name = name.substring(name.lastIndexOf('.') + 1);

			name = CDEUtilities.lowCaseFirstCharacter(name);
		}

		name = getValidInstanceVariableName(obj, name, currentType, bdm);

		if (a == null) {
			// We don't have an annotation. We must have an annotation if we have an instance variable. Otherwise the model will be inconsistent.
			a = CodeGenUtil.addAnnotation(obj);
			cm.getModelRoot().getAnnotations().add(a);
		}

		if (!name.equals(nameEntry)) {
			// No need to go through commands because we would not be here except for a new entry. Also, since name needs to
			// be in-sync with the instance variable name, you can't undo to a previous name without changing the variable name too,
			// so a simple undo is now allowed.				
			CodeGenUtil.addAnnotatedName(a, name);
		}

		return name;
	}

	/**
	 *  
	 *  
	 */
	public String getInstanceVariableMethodName(EObject obj, String InstanceName, IType currentType, IDiagramModelInstance cm) {

		if (isInternalType(obj, obj.eResource().getResourceSet()))
			return null; // No method for a utility object

		String name = InstanceName;
		//	int index = name.indexOf(DEFAULT_VAR_PREFIX)+DEFAULT_VAR_PREFIX.length() ;

		String methodName = addPrefix(DEFAULT_METHOD_PREFIX, name);
		String ori = methodName;

		int Index = 2;
		try {
			IMethod[] methods = currentType.getMethods();
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getElementName().equals(methodName)) { // name conflict
					methodName = ori + Integer.toString(Index++);
					// Try again
					i = -1;
				}
			}
		} catch (JavaModelException e) {
		}
		return methodName;
	}

	/**
	 * Should an instance Variable be declared, or a local one ?
	 * @return true if local declaration, false if instance variable
	 */
	public boolean isLocalDecleration(EObject obj, IType currentType, IDiagramModelInstance cm) {
		if (isInternalType(obj, obj.eResource().getResourceSet()))
			return true;
		else
			return false;
	}

	/**
	 * Is a uniqe method required to initialize this bean
	 */
	public boolean isGenerateAMethod(EObject obj, IType currentType, IDiagramModelInstance cm) {
		if (isLocalDecleration(obj, currentType, cm))
			return false;
		// At this time default is always to generate a method
		return true;

	}

	/**
	 * Returns the fPrefStore.
	 * @return Preferences
	 */
	public static Preferences getPrefStore() {
		if (fPrefStore == null)
			fPrefStore = VCEPreferences.getPlugin().getPluginPreferences();
		return fPrefStore;
	}

	/**
	 * Returns the fComponentMeta.
	 * @return JavaHelpers
	 */
	public static JavaHelpers getComponentMeta(ResourceSet rs) {

		if (fRS != null && fRS.equals(rs))
			return fComponentMeta;
		getMetaTypes(rs);
		return fComponentMeta;
	}

	public static void clearCache() {
		fRS = null;
		internalHelpers = null;
		fComponentMeta = null;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.rules.IInstanceVariableCreationRule#getValidInstanceVariableName(EObject, String)
	 */
	public String getValidInstanceVariableName(EObject obj, String base, IType currentType, IBeanDeclModel bdm) {
		ResourceSet rs = obj.eResource().getResourceSet();
		return getValidInstanceVariableName(rs, obj, base, currentType, bdm);
	}

	public String getValidInstanceVariableName(ResourceSet rs, EObject obj, String base, IType currentType, IBeanDeclModel bdm) {
		EClassifier meta = ((IJavaInstance) obj).getJavaType();
		if (base.charAt(0) == '"')
			base = base.substring(1);
		if (base.charAt(base.length() - 1) == '"')
			base = base.substring(0, base.length() - 2);

		String prefix = getPrefix(meta, rs);
		if (prefix.length() != 0) {
			if (!base.startsWith(prefix))
				base = addPrefix(prefix, base);
			if (isInternalType(obj, rs))
				base += Integer.toString(internalIndex++);
		}

		String name = base;

		int Index = 1;
		try {
			IField[] fields = currentType.getFields();
			for (int i = 0; i < fields.length; i++) {
				if (fields[i].getElementName().equals(name)) { // name conflict
					name = base + Integer.toString(Index++);
					// Try again from the top.
					i = -1;
				}
			}

			if (bdm != null) {
				BeanPart beans[] = (BeanPart[]) bdm.getBeans().toArray(new BeanPart[bdm.getBeans().size()]);
				for (int i = 0; i < beans.length; i++) {
					if (beans[i].getSimpleName().equals(name)) {
						name = base + Integer.toString(Index++);
						// Try again from top
						i = -1;
					}
				}
			}
		} catch (JavaModelException e) {
		}

		return name;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.rules.IRule#setRegistry(org.eclipse.ve.internal.cde.rules.IRuleRegistry)
	 */
	public void setRegistry(IRuleRegistry registry) {
		// no-op. We don't care.
	}

}
