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
 *  $Revision: 1.14 $  $Date: 2006-05-19 14:55:56 $ 
 */
import java.util.logging.Level;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.*;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

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

	private IVisualClassCreationSourceContributor contributor;

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
			return configElement.getDeclaringExtension().getContributor().getName();
		return null;
	}

	public String toString() {
		return "category: " + getCategory() + ", name: " + getName() + ", type: " + getSuperClass(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	/**
	 * @param resource 
	 * @return Returns a reason why the current element can't be used.  This could be because the build path
	 * of the project or type doesn't allow it.
	 * A null return value implies that the contribution can be used
	 */
	public IStatus getStatus(IResource resource) {
		if(resource == null || configElement.getAttribute("contributor") == null) return Status.OK_STATUS; //$NON-NLS-1$
		if(contributor == null){
			try {
				contributor = (IVisualClassCreationSourceContributor) configElement.createExecutableExtension("contributor"); //$NON-NLS-1$ 
			} catch (CoreException e) {
				JavaVEPlugin.log(e, Level.FINEST);
				return Status.OK_STATUS;
			} 
		}
		IStatus result = contributor.getStatus(resource);
		return result == null ? Status.OK_STATUS : result;				
	}
	
}