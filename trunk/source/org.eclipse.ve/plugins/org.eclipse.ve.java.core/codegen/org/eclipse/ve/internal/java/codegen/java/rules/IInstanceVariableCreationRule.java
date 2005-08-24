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
 *  $RCSfile: IInstanceVariableCreationRule.java,v $
 *  $Revision: 1.8 $  $Date: 2005-08-24 23:30:48 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.IType;

import org.eclipse.ve.internal.cde.rules.IRule;
import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;

/**
 *  Enable the rules to set an instance Variable name
 */
public interface IInstanceVariableCreationRule extends IRule {

	public static final String RULE_ID = "ruleInstanceVariableCreation"; //$NON-NLS-1$

	public String getInstanceVariableName(EObject obj, IType currentType, IVEModelInstance cm, IBeanDeclModel bdm);
	/**
	 * Get a valid name for the given object based upon the given base name. If the base name is the same as the 
	 * current name, a new name will be returned. So don't call this except with a base that is different then
	 * the current name.
	 * 
	 * This is used when the obj is contained in a resource.
	 * 
	 * @param obj The object to create the name for. This object must be contained within a resource.
	 * @param base The starting base name to use.
	 * @param bdm The BeanDeclModel for this model.
	 * 
	 * @return The valid name to use, based upon the base name, might be the same as the base name if it was valid.
	 */
	public String getValidInstanceVariableName(EObject obj, String base, IType currentType, IBeanDeclModel bdm);
	
	/**
	 * Get a valid name for the given object based upon the given base name. If the base name is the same as the 
	 * current name, a new name will be returned. So don't call this except with a base that is different then
	 * the current name.
	 * 
	 * This method is used when the obj is not yet contained within a resource.
	 *
	 * @param rs The resourceSet to look into. 
	 * @param obj The object to create the name for.
	 * @param base The starting base name to use.
	 * @param bdm The BeanDeclModel for this model.
	 * 
	 * @return The valid name to use, based upon the base name, might be the same as the base name if it was valid.
	 */
	
	public String getValidInstanceVariableName(ResourceSet rs, EObject obj, String base, IType currentType, IBeanDeclModel bdm);	
	public String getInstanceVariableMethodName(EObject obj, String InstanceName, IType currentType, IBeanDeclModel bdm);
	public boolean isLocalDecleration(EObject obj, IType currentType, IVEModelInstance cm);
	public boolean isGenerateAMethod(EObject obj, IType currentType, IVEModelInstance cm);

}
