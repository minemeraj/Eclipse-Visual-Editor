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
package org.eclipse.ve.sweet.field;

import org.eclipse.ve.sweet.dataeditors.IObjectEditor;
import org.eclipse.ve.sweet.dataeditors.IPropertyEditor;
import org.eclipse.ve.sweet.field.swt.SWTFieldControllerFactory;


public class FieldControllerFactory {
    public static IFieldControllerFactory factory = null;

    public static IFieldController construct(Object control,
            IObjectEditor objectEditor, IPropertyEditor propertyEditor) 
    {
        if (factory == null) {
            factory = new SWTFieldControllerFactory();
        }
        return factory.construct(control, objectEditor, propertyEditor);
    }
}
