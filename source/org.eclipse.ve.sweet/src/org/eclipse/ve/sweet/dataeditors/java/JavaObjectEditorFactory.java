/*
 * Copyright (C) 2005 db4objects Inc.  http://www.db4o.com
 */
package org.eclipse.ve.sweet.dataeditors.java;

import org.eclipse.ve.sweet.dataeditors.IObjectEditor;
import org.eclipse.ve.sweet.dataeditors.IObjectEditorFactory;
import org.eclipse.ve.sweet.dataeditors.java.internal.JavaObject;

public class JavaObjectEditorFactory implements IObjectEditorFactory {

    public IObjectEditor construct() {
        return new JavaObject();
    }

}
