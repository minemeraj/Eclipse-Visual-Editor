package org.eclipse.ve.internal.java.codegen.wizards;
/*******************************************************************************
 * Copyright (c) 2004 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Red Hat, Inc. - 1.0 implementation
 *******************************************************************************/
/*
 *  $RCSfile: VisualElementModel.java,v $
 *  $Revision: 1.6 $  $Date: 2004-09-08 22:38:50 $ 
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
			return configElement.getAttribute("name");
		return null;
	}

	public String getSuperClass() {
		if (configElement != null)
			return configElement.getAttributeAsIs("type");
		return null;
	}

	public String getCategory() {
		if (configElement != null)
			return configElement.getAttributeAsIs("category");
		return null;
	}

	public String getContainer() {
		if (configElement != null)
			return configElement.getAttributeAsIs("container");
		return null;
	}
	
	public String getPluginId() {
		if (configElement != null)
			return configElement.getAttributeAsIs("pluginId");
		return null;
	}

	public Object getParent() {
		return parent;
	}

	public String getIconFile() {
		if (configElement != null)
			return configElement.getAttributeAsIs("icon");
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
		return "category: " + getCategory() + ", name: " + getName() + ", type: " + getSuperClass();
	}
}