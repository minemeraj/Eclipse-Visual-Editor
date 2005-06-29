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
package org.eclipse.ve.sweet.field;

import org.eclipse.ve.sweet.dataeditors.IObjectEditor;
import org.eclipse.ve.sweet.dataeditors.IPropertyEditor;

public interface IFieldControllerFactory {

    /**
     * Construct an IFieldController for some control, IObjectEditor, and IPropertyEditor
     * 
     * @param control The SWT control
     * @param objectEditor The IObjectEditor of the object to edit
     * @param propertyEditor The IPropertyEditor of the property to edit
     * 
     * @return The IFieldController that was constructed
     */
    public IFieldController construct(Object control,
            IObjectEditor objectEditor, IPropertyEditor propertyEditor);

}