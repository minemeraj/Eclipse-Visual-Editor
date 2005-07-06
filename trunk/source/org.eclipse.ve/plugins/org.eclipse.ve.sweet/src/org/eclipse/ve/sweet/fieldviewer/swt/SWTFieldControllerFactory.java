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

import org.eclipse.ve.sweet.fieldviewer.IFieldController;
import org.eclipse.ve.sweet.fieldviewer.IFieldControllerFactory;
import org.eclipse.ve.sweet.fieldviewer.swt.internal.CheckboxButtonFieldController;
import org.eclipse.ve.sweet.fieldviewer.swt.internal.CheckboxListFieldController;
import org.eclipse.ve.sweet.fieldviewer.swt.internal.ComboFieldController;
import org.eclipse.ve.sweet.fieldviewer.swt.internal.TextFieldController;
import org.eclipse.ve.sweet.fieldviewer.swt.internal.interfaces.ICheckboxButtonField;
import org.eclipse.ve.sweet.fieldviewer.swt.internal.interfaces.ICheckboxListField;
import org.eclipse.ve.sweet.fieldviewer.swt.internal.interfaces.IComboField;
import org.eclipse.ve.sweet.objectviewer.IObjectViewer;
import org.eclipse.ve.sweet.objectviewer.IPropertyEditor;
import org.eclipse.ve.sweet.reflect.DuckType;


/**
 * SWTFieldControllerFactory.  SWT constructor factory for IFieldController objects. 
 *
 * @author djo
 */
public class SWTFieldControllerFactory implements IFieldControllerFactory {
    /* (non-Javadoc)
     * @see com.db4o.binding.field.swt.IFieldControllerFactory#construct(java.lang.Object, com.db4o.binding.dataeditors.IObjectEditor, com.db4o.binding.dataeditors.IPropertyEditor)
     */
    public IFieldController construct(Object control, IObjectViewer objectEditor, IPropertyEditor propertyEditor) {
        IFieldController result = null;
        if (DuckType.instanceOf(ICheckboxButtonField.class, control)) {
            result = new CheckboxButtonFieldController(control, objectEditor, propertyEditor);
        } else if (DuckType.instanceOf(ICheckboxListField.class, control)) {
            result = new CheckboxListFieldController(control, objectEditor, propertyEditor);
        } else if (DuckType.instanceOf(IComboField.class, control)) {
            result = new ComboFieldController(control, objectEditor, propertyEditor);
        } else {
            result = new TextFieldController(control, objectEditor, propertyEditor);
        }
        return result;
    } 
}
