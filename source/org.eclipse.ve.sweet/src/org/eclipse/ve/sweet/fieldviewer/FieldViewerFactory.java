/*
 * Copyright (C) 2005 db4objects Inc.  http://www.db4o.com
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     db4objects - Initial API and implementation
 */
package org.eclipse.ve.sweet.fieldviewer;

import org.eclipse.ve.sweet.fieldviewer.swt.SWTFieldViewerFactory;
import org.eclipse.ve.sweet.objectviewer.IObjectViewer;
import org.eclipse.ve.sweet.objectviewer.IPropertyEditor;


public class FieldViewerFactory {
    public static IFieldViewerFactory factory = null;

    public static IFieldViewer construct(Object control,
            IObjectViewer objectEditor, IPropertyEditor propertyEditor) 
    {
        if (factory == null) {
            factory = new SWTFieldViewerFactory();
        }
        return factory.construct(control, objectEditor, propertyEditor);
    }
}
