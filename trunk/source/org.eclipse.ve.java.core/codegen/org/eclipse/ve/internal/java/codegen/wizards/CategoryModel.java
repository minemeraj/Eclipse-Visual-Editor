package org.eclipse.ve.internal.java.codegen.wizards;

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
			return configElement.getAttribute("name");
		return null;
	}

	public String getId() {
		if (configElement != null)
			return configElement.getAttributeAsIs("id");
		return null;
	}

	public Object getParent() {
		return parent;
	}

	public int getPriority() {
		if (configElement != null) {
			String priority = configElement.getAttributeAsIs("priority");
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
		String defaultExpand = configElement.getAttributeAsIs("defaultExpand");
		if (defaultExpand != null)
			return (defaultExpand.equalsIgnoreCase("true")) ? true : false;
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

