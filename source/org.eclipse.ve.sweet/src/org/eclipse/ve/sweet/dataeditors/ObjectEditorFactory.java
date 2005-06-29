/*
 * Copyright (C) 2005 db4objects Inc.  http://www.db4o.com
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     db4objects - Initial API and implementatiobn
 */
package org.eclipse.ve.sweet.dataeditors;


/**
 * ObjectEditorFactory.  Constructs ObjectEditors for an application.  Enables
 * various IObjectEditor implementations to be painlessly swapped in an
 * application.
 *
 * @author djo
 */
public class ObjectEditorFactory {
    /**
     * The actual IObjectEditorFactory instance that will be used to create
     * new IObjectEditor objects.  Defaults to the Db4oObjectEditorFactory.
     */
    public static IObjectEditorFactory factory = null;
    
    /**
     * Construct a new IObjectEditor and set its initial input object.
     * 
     * @param input The initial input object
     * @return The constructed IObjectEditor
     */
    public static IObjectEditor construct(Object input) {
        IObjectEditor result = construct();
        result.setInput(input);
        return result;
    }
    
    /**
     * Construct a new IObjectEditor object.
     * 
     * @return The IObjectEditor that was constructed.
     */
    public static IObjectEditor construct() {
        return factory.construct();
    }
}
