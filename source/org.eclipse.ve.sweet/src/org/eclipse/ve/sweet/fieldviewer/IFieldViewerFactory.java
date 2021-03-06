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

import org.eclipse.ve.sweet.objectviewer.IObjectViewer;
import org.eclipse.ve.sweet.objectviewer.IPropertyEditor;

public interface IFieldViewerFactory {

    /**
     * Construct an IFieldViewer for some control, IObjectViewer, and IPropertyEditor
     * 
     * @param control The SWT control
     * @param objectViewer The IObjectViewer of the object to edit
     * @param propertyEditor The IPropertyEditor of the property to edit
     * 
     * @return The IFieldViewer that was constructed
     */
    public IFieldViewer construct(Object control,
            IObjectViewer objectViewer, IPropertyEditor propertyEditor);

}