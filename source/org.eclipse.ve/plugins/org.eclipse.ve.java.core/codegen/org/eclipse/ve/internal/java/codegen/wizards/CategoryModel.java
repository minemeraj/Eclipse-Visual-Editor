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

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * @author pmuldoon
 *
 * Category Model for the Style Tree in the 
 * New Visual Class Wizard
 */

public class CategoryModel {

    private List treeElements;

    protected CategoryModel parent;

    private String id;

    private String name;

    private int priority;

    private boolean defaultExpanded;

    public CategoryModel() {
        treeElements = new ArrayList();
    }

    public CategoryModel(String name, String id, String priority,
            String defaultExpanded) {
        this();
        this.name = name;
        this.id = id;
        if (priority == null)
            this.priority = 10000;
        else
            try {
                this.priority = Integer.parseInt(priority);
            } catch (NumberFormatException nx) {
                this.priority = 10000;
                JavaVEPlugin.log(nx, Level.FINEST);
            }
        this.defaultExpanded = true;
        if (defaultExpanded != null)
            if (defaultExpanded.equalsIgnoreCase("false"))
                this.defaultExpanded = false;
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
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public Object getParent() {
        return parent;
    }

    public int getPriority() {
        return this.priority;
    }

    public boolean getDefaultExpand() {
        return this.defaultExpanded;
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


  
    

