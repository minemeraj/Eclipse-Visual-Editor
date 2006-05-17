/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.vce.rules;
/*
 *  $RCSfile: DefaultRuleProvider.java,v $
 *  $Revision: 1.8 $  $Date: 2006-05-17 20:14:53 $ 
 */
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.ve.internal.cde.rules.IRule;
import org.eclipse.ve.internal.cde.rules.IRuleRegistry;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.vce.templates.TemplateUtil;

/**
 * @author Gili Mendel
 *
 * This is a standard rule provider to provide a rule from a configuration element within a plugin.
 */
public class DefaultRuleProvider implements IRuleProvider {

	public static final String EXT_ID = "id"; // ID attribute in Config Element //$NON-NLS-1$
	public static final String EXT_CLASS = "class"; // Attribute for Class for the rule.	 //$NON-NLS-1$
	public static final String RulePath = "Rules"; // Rules source is located in "Rules" subdirectory of plugin. 	 //$NON-NLS-1$

	protected IRule fRule = null;
	protected IConfigurationElement ce;
	protected String fID;
	protected String fStyleID;
	protected int fTimeStamp = 0; // Anyone can overide us
	protected IRuleRegistry ruleRegistry;

	public DefaultRuleProvider(IConfigurationElement ce, String styleID, IRuleRegistry registry) {
		this.ce = ce;
		fStyleID = styleID;
		fID = ce.getAttribute(EXT_ID);
		this.ruleRegistry = registry;
	}


	private boolean noRule;	// If true it means we couldn't create the rule.
	/**
	 * @see org.eclipse.ve.internal.java.core.codegen.java.rules.IRuleProvider#getRule()
	 */
	public IRule getRule() {
		if (noRule)
			return null;
		if (fRule == null) {
			synchronized (this) {
				try {
					IRule rule = (IRule) ce.createExecutableExtension(EXT_CLASS);
					rule.setRegistry(ruleRegistry);
					fRule = rule;
				} catch (CoreException e) {
					noRule = true;
					JavaVEPlugin.log(e);
					return null;
				} catch (ClassCastException e) {
					noRule = true;
					JavaVEPlugin.log(e);
					return null;
				}
			}
		}
		return fRule;
	}
	/**
	 * @see org.eclipse.ve.internal.java.core.codegen.java.rules.IRuleProvider#getTimeStamp()
	 */
	public long getTimeStamp() {
		return fTimeStamp;
	}
	/**
	 * @see org.eclipse.ve.internal.java.core.codegen.java.rules.IRuleProvider#getSourceLocation()
	 */
	public String getSourceLocation() {
		// Get the rule to find class name.
		Object rule = getRule();
		if (rule != null)
			return TemplateUtil.getPathForBundleFile(
				ce.getDeclaringExtension().getContributor().getName(),
				rule.getClass().getName().replace('.', '/'));
		else
			return null;
	}

	/**
	 * @see org.eclipse.ve.internal.java.core.codegen.java.rules.IRuleProvider#getStyle()
	 */
	public String getStyle() {
		return fStyleID;
	}

	/**
	 * @see org.eclipse.ve.internal.java.core.codegen.java.rules.IRuleProvider#getID()
	 */
	public String getRuleID() {
		return fID;
	}

	public String toString() {
		return "Default Rule(id=" + getRuleID() + "class=" + ce.getAttributeAsIs(EXT_CLASS) + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * @see org.eclipse.ve.internal.java.core.codegen.java.rules.IRuleProvider#getProviderID()
	 */
	public String getProviderID() {
		return ce.getDeclaringExtension().getContributor().getName();
	}

}
