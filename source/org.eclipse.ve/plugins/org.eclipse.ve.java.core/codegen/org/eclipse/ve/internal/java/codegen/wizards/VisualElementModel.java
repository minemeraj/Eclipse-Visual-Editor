package org.eclipse.ve.internal.java.codegen.wizards;
/*******************************************************************************
 * Copyright (c) 2004 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Red Hat, Inc. - 1.0 implementation
 *******************************************************************************/
/*
 *  $RCSfile: VisualElementModel.java,v $
 *  $Revision: 1.8 $  $Date: 2005-04-05 22:48:22 $ 
 */
import org.eclipse.core.runtime.IConfigurationElement;

/**
 * @author pmuldoon
 * 
 * Visual Element Object Model for New Visual Class Wizard
 */
public class VisualElementModel {

	/**
	 * @return Returns the configElement.
	 */
	public IConfigurationElement getConfigElement() {
		return configElement;
	}

	public CategoryModel parent;

	private IConfigurationElement configElement;

	public VisualElementModel(IConfigurationElement configElement) {
		this.configElement = configElement;
	}

	public String getName() {
		if (configElement != null)
			return configElement.getAttribute("name"); //$NON-NLS-1$
		return null;
	}

	public String getSuperClass() {
		if (configElement != null)
			return configElement.getAttributeAsIs("type"); //$NON-NLS-1$
		return null;
	}

	public String getCategory() {
		if (configElement != null)
			return configElement.getAttributeAsIs("category"); //$NON-NLS-1$
		return null;
	}

	public String getContainer() {
		if (configElement != null)
			return configElement.getAttributeAsIs("container"); //$NON-NLS-1$
		return null;
	}
	
	public String getPluginId() {
		if (configElement != null)
			return configElement.getAttributeAsIs("pluginId"); //$NON-NLS-1$
		return null;
	}

	public Object getParent() {
		return parent;
	}

	public String getIconFile() {
		if (configElement != null)
			return configElement.getAttributeAsIs("icon"); //$NON-NLS-1$
		return null;
	}

	/**
	 * @return Returns the contributorBundleName.
	 */
	public String getContributorBundleName() {
		if (configElement != null)
			return configElement.getDeclaringExtension().getNamespace();
		return null;
	}

	public String toString() {
		return "category: " + getCategory() + ", name: " + getName() + ", type: " + getSuperClass(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}