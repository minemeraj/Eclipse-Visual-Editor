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
package org.eclipse.ve.sweet.field.swt;

import org.eclipse.ve.sweet.CannotSaveException;
import org.eclipse.ve.sweet.dataeditors.IObjectEditor;
import org.eclipse.ve.sweet.dataeditors.IPropertyEditor;
import org.eclipse.ve.sweet.field.IFieldController;


public class ComboFieldController implements IFieldController {

    public ComboFieldController(Object control, IObjectEditor object, IPropertyEditor property) {
        // TODO Auto-generated constructor stub
    }

    public String getPropertyName() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isDirty() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setDirty(boolean dirty) {
        // TODO Auto-generated method stub

    }

    public void undo() {
        // TODO Auto-generated method stub

    }

    public void save() throws CannotSaveException {
        // TODO Auto-generated method stub

    }

    public boolean verify() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setInput(Object input) {
        // TODO Auto-generated method stub
        
    }

    public void setInput(IPropertyEditor input) throws CannotSaveException {
        // TODO Auto-generated method stub
    }

    public IPropertyEditor getInput() {
        // TODO Auto-generated method stub
        return null;
    }

}
