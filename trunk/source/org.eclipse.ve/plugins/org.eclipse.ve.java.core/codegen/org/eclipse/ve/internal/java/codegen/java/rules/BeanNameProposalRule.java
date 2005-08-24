/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * Created on Sep 4, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.eclipse.ve.internal.java.codegen.java.rules;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.IType;
import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.rules.IRuleRegistry;
import org.eclipse.ve.internal.java.rules.IBeanNameProposalRule;

import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;

/**
 * @author richkulp
 */
public class BeanNameProposalRule implements IBeanNameProposalRule {

	protected IInstanceVariableCreationRule instanceVariableCreationRule;

	/**
	 * 
	 */
	public BeanNameProposalRule() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.vce.rules.IBeanNameProposalRule#getProspectInstanceVariableName(java.lang.String, java.lang.Object[], org.eclipse.emf.ecore.resource.ResourceSet)
	 */
	public String getProspectInstanceVariableName(String typeName, Object[] args, ResourceSet rs) {

		if (args != null) {
			IType type = null;
			for (int i = 0; i < args.length; i++) {
				if (args[i] instanceof IType) {
					type = (IType) args[i];
					break;
				}
			}
			if (type != null) {
				try {
					EObject obj = CodeGenUtil.createInstance(typeName, rs);
					String base = typeName;
					if (base.indexOf('.') > 0)
						base = base.substring(base.lastIndexOf('.') + 1);
					base = CDEUtilities.lowCaseFirstCharacter(base);
					return instanceVariableCreationRule.getValidInstanceVariableName(rs, obj, base, type, null);
				} catch (CodeGenException e) {
				}
			}
		}
		return null;

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.rules.IRule#setRegistry(org.eclipse.ve.internal.cde.rules.IRuleRegistry)
	 */
	public void setRegistry(IRuleRegistry registry) {
		instanceVariableCreationRule = (IInstanceVariableCreationRule) registry.getRule(IInstanceVariableCreationRule.RULE_ID);
	}

}
