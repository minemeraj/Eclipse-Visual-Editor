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
 *  $RCSfile: CategoryModel.java,v $
 *  $Revision: 1.6 $  $Date: 2005-04-05 22:48:22 $ 
 */
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.core.runtime.IConfigurationElement;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * @author pmuldoon
 * 
 * Category Model for the Style Tree in the New Visual Class Wizard
 */

public class CategoryModel {

	private List treeElements;

	protected CategoryModel parent;

	private IConfigurationElement configElement;

	public CategoryModel() {
		treeElements = new ArrayList();
	}

	public CategoryModel(IConfigurationElement configElement) {
		this();
		this.configElement = configElement;
	}

	protected void addVisualElement(VisualElementModel element) {
		element.parent = this;
		treeElements.add(element);
	}

	public void removeVisualElement(VisualElementModel element) {
		treeElements.remove(element);
	}

	protected void addStyle(CategoryModel element) {
		element.parent = this;
		treeElements.add(element);
	}

	public void removeStyle(CategoryModel set) {
		treeElements.remove(set);
	}

	public String getName() {
		if (configElement != null)
			return configElement.getAttribute("name"); //$NON-NLS-1$
		return null;
	}

	public String getId() {
		if (configElement != null)
			return configElement.getAttributeAsIs("id"); //$NON-NLS-1$
		return null;
	}

	public Object getParent() {
		return parent;
	}

	public int getPriority() {
		if (configElement != null) {
			String priority = configElement.getAttributeAsIs("priority"); //$NON-NLS-1$
			if (priority != null) {
				try {
					return Integer.parseInt(priority);
				} catch (NumberFormatException nx) {
					JavaVEPlugin.log(nx, Level.FINEST);
					return 10000;
				}
			}
		}
		return 10000;
	}

	public boolean getDefaultExpand() {
		String defaultExpand = configElement.getAttributeAsIs("defaultExpand"); //$NON-NLS-1$
		if (defaultExpand != null)
			return (defaultExpand.equalsIgnoreCase("true")) ? true : false; //$NON-NLS-1$
		return true;
	}

	public Object[] getChildren() {
		if (treeElements.size() > 0)
			return treeElements.toArray();
		return new Object[0];
	}

	public Object[] getStyles() {
		return treeElements.toArray();
	}

}

