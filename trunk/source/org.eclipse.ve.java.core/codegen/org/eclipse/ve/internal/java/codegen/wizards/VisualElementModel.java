package org.eclipse.ve.internal.java.codegen.wizards
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
;

/**
 * @author pmuldoon
 *
 * Visual Element Object Model for New Visual Class Wizard
 */
public class VisualElementModel {

    public CategoryModel parent;

    protected String name;

    protected String superclass;

    protected String codeContributor;

    public VisualElementModel(String name, String superclass,
            String codeContributor) {
        this.name = name;
        this.superclass = superclass;
        this.codeContributor = codeContributor;
    }

    public String getName() {
        return this.name;
    }

    public String getSuperClass() {
        return this.superclass;
    }

    public String getCodeContributor() {
        return this.codeContributor;
    }

    public Object getParent() {
        return parent;
    }
}
