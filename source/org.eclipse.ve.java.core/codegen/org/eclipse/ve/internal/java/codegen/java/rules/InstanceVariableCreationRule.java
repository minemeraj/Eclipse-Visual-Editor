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
package org.eclipse.ve.internal.java.codegen.java.rules;
/*
 *  $RCSfile: InstanceVariableCreationRule.java,v $
 *  $Revision: 1.18 $  $Date: 2005-06-07 18:15:53 $ 
 */
import java.util.*;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cdm.Annotation;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.emf.ClassDecoratorFeatureAccess;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;
import org.eclipse.ve.internal.cde.rules.IRuleRegistry;

import org.eclipse.ve.internal.jcm.BeanSubclassComposition;
import org.eclipse.ve.internal.jcm.JCMMethod;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.ExpressionDecoderFactory;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.codegen.util.IMethodTextGenerator;
import org.eclipse.ve.internal.java.vce.VCEPreferences;

public class InstanceVariableCreationRule implements IInstanceVariableCreationRule {
	
	public static int internalIndex = 1;
	public static Preferences fPrefStore = null;
	//public static List VisualCompoentns = new ArrayList() ;

	/**
	 * It the type is/extends any element in internalTypes, than it should be an internally
	 * defined class. If the parent of the input is the BeanSubclassComposition, then it 
	 * should not be internal. 
	 */
	private boolean isInternalType(EObject obj, ResourceSet rs) {
		boolean result = false;
		EObject parent = obj.eContainer();
		if (parent instanceof BeanSubclassComposition)
			result = false;
		else if(parent instanceof JCMMethod)
			result = true;
		return result;
	}
	/**
	 * 
	 * @param aClass
	 * @param sf
	 * @return  The default boolean value for that class and structural feature
	 * 
	 * @since 1.0.0
	 */
	public static boolean getDefaultBooleanValue(EClassifier aClass, EStructuralFeature sf){
		EAnnotation decr = ClassDecoratorFeatureAccess.getDecoratorWithFeature(aClass, "codegen.CodeGenHelperClass", sf); //$NON-NLS-1$
		if(decr!=null){
			Boolean visual = (Boolean) decr.eGet(sf);
			if(visual!=null)
				return visual.booleanValue();
		}
		return false;
	}

	private static EStructuralFeature getStructuralFeatureNamed(String name, ResourceSet rs){
		EClass cgHelperClass = (EClass) rs.getEObject(ExpressionDecoderFactory.URIcodeGenHelperClass, true) ; 
	   	EStructuralFeature sf = cgHelperClass.getEStructuralFeature(name) ; 
	   	return sf;
	}

	/**
	 * Determines if this class is to be modelled or not
	 * This value is set via the overrides mechanism
	 *   
	 * @param aClass
	 * @return  If this bean is to be modelled or not
	 * 
	 * @since 1.0.0
	 */
	public static boolean isModelled(EClassifier aClass, ResourceSet rs){
		if (rs == null || aClass == null)
			return false;
		return getDefaultBooleanValue(aClass, getStructuralFeatureNamed("modelled", rs)); //$NON-NLS-1$
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
	public String getInstanceVariableName(EObject obj, IType currentType, IVEModelInstance cm, IBeanDeclModel bdm) {

		String name = null;
		String nameEntry = null;

		// Try to get its annotated value 
		Annotation a = CodeGenUtil.getAnnotation(obj);
		if (a != null) {
			nameEntry = name = (String) a.getKeyedValues().get(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
			name = getValidInstanceVariableName(obj, name, currentType, bdm);
		}

		if (name == null) {
			name = ((IJavaObjectInstance) obj).getJavaType().getJavaName();
			if (name.indexOf('.') > 0)
				name = name.substring(name.lastIndexOf('.') + 1);

			name = CDEUtilities.lowCaseFirstCharacter(name);
			// Since we are creating the name, better make it valid. If name already 
			// existed then leave it alone (dropped from choosebean dialog)
			name = getValidInstanceVariableName(obj, name, currentType, bdm);
		}

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

	protected String addMethodPrefix (EObject obj, String name, IBeanDeclModel bdm) {
		IMethodTextGenerator mg = CodeGenUtil.getMethodTextFactory(bdm).getMethodGenerator((IJavaObjectInstance)obj,bdm) ;
        return addPrefix(mg.getMethodPrefix(),name) ;
	}
	/**
	 *  
	 *  
	 */
	public String getInstanceVariableMethodName(EObject obj, String InstanceName, IType currentType, IBeanDeclModel bdm) {

		if (isInternalType(obj, obj.eResource().getResourceSet()))
			return null; // No method for a utility object

		String name = InstanceName;
		//	int index = name.indexOf(DEFAULT_VAR_PREFIX)+DEFAULT_VAR_PREFIX.length() ;

		String methodName = addMethodPrefix(obj, name, bdm);
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
	public boolean isLocalDecleration(EObject obj, IType currentType, IVEModelInstance cm) {
		if (isInternalType(obj, obj.eResource().getResourceSet()))
			return true;
		else
			return false;
	}

	/**
	 * Is a uniqe method required to initialize this bean
	 */
	public boolean isGenerateAMethod(EObject obj, IType currentType, IVEModelInstance cm) {
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

	public static void clearCache() {
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.rules.IInstanceVariableCreationRule#getValidInstanceVariableName(EObject, String)
	 */
	public String getValidInstanceVariableName(EObject obj, String base, IType currentType, IBeanDeclModel bdm) {
		ResourceSet rs = obj.eResource().getResourceSet();
		return getValidInstanceVariableName(rs, obj, base, currentType, bdm);
	}

	public String getValidInstanceVariableName(ResourceSet rs, EObject obj, String base, IType currentType, IBeanDeclModel bdm) {
		if (base.charAt(0) == '"')
			base = base.substring(1);
		if (base.charAt(base.length() - 1) == '"')
			base = base.substring(0, base.length() - 2);

		if (isInternalType(obj, rs)){
			// 98395: since it is internal we need to look at the method 
			// in source to see if any other variable has this name.
			if(existsMethodVariable(base, obj, bdm))
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

	private boolean existsMethodVariable(final String base, EObject obj, IBeanDeclModel bdm) {
		BeanPart b = bdm.getABean(obj);
		CodeMethodRef initMethod = null;
		if(b!=null)
			initMethod = b.getInitMethod();
		else{
			if(obj.eContainer() instanceof JCMMethod){
				JCMMethod jcmMethod = (JCMMethod) obj.eContainer();
				Iterator mitr = bdm.getAllMethods();
				while (mitr.hasNext()) {
					CodeMethodRef cmr = (CodeMethodRef) mitr.next();
					if(jcmMethod.equals(cmr.getCompMethod())){
						initMethod = cmr;
						break;
					}
				}
			}
		}
		if(initMethod!=null){
			String methodContent = initMethod.getContent();
			ASTParser parser = ASTParser.newParser(AST.JLS2);
			parser.setSource(methodContent.toCharArray());
			parser.setKind(ASTParser.K_CLASS_BODY_DECLARATIONS);
			ASTNode node = parser.createAST(null);
			final List visitorReturn = new ArrayList(); // just so we can get return of anon astvisitor
			node.accept(new ASTVisitor(){
				public boolean visit(VariableDeclarationFragment node) {
					if(base.equals(node.getName().getFullyQualifiedName()))
						visitorReturn.add(Boolean.TRUE);
					return visitorReturn.size()<1 && super.visit(node);
				}
				public boolean visit(SingleVariableDeclaration node) {
					if(base.equals(node.getName().getFullyQualifiedName()))
						visitorReturn.add(Boolean.TRUE);
					return visitorReturn.size()<1 && super.visit(node);
				}
			});
			if(visitorReturn.size()>0)
				return ((Boolean)visitorReturn.get(0)).booleanValue();
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
