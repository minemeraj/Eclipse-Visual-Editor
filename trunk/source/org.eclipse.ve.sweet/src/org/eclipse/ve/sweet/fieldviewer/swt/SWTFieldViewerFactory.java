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
package org.eclipse.ve.sweet.fieldviewer.swt;

import org.eclipse.ve.sweet.fieldviewer.IFieldViewer;
import org.eclipse.ve.sweet.fieldviewer.IFieldViewerFactory;
import org.eclipse.ve.sweet.fieldviewer.swt.internal.CheckboxButtonFieldViewer;
import org.eclipse.ve.sweet.fieldviewer.swt.internal.CheckboxListFieldViewer;
import org.eclipse.ve.sweet.fieldviewer.swt.internal.ComboFieldViewer;
import org.eclipse.ve.sweet.fieldviewer.swt.internal.TextFieldViewer;
import org.eclipse.ve.sweet.fieldviewer.swt.internal.ducktypes.ICheckboxButtonControl;
import org.eclipse.ve.sweet.fieldviewer.swt.internal.ducktypes.ICheckboxListControl;
import org.eclipse.ve.sweet.fieldviewer.swt.internal.ducktypes.IComboControl;
import org.eclipse.ve.sweet.objectviewer.IObjectViewer;
import org.eclipse.ve.sweet.objectviewer.IPropertyEditor;
import org.eclipse.ve.sweet.reflect.DuckType;


/**
 * SWTFieldViewerFactory.  SWT constructor factory for IFieldViewer objects. 
 *
 * @author djo
 */
public class SWTFieldViewerFactory implements IFieldViewerFactory {
    /* (non-Javadoc)
     * @see com.db4o.binding.field.swt.IFieldControllerFactory#construct(java.lang.Object, com.db4o.binding.dataeditors.IObjectViewer, com.db4o.binding.dataeditors.IPropertyEditor)
     */
    public IFieldViewer construct(Object control, IObjectViewer objectEditor, IPropertyEditor propertyEditor) {
        IFieldViewer result = null;
        if (DuckType.instanceOf(ICheckboxButtonControl.class, control)) {
            result = new CheckboxButtonFieldViewer(control, objectEditor, propertyEditor);
        } else if (DuckType.instanceOf(ICheckboxListControl.class, control)) {
            result = new CheckboxListFieldViewer(control, objectEditor, propertyEditor);
        } else if (DuckType.instanceOf(IComboControl.class, control)) {
            result = new ComboFieldViewer(control, objectEditor, propertyEditor);
        } else {
            result = new TextFieldViewer(control, objectEditor, propertyEditor);
        }
        return result;
    } 
}
