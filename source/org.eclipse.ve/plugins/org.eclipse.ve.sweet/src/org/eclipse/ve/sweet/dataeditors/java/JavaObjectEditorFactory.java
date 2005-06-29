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
package org.eclipse.ve.sweet.dataeditors.java;

import org.eclipse.ve.sweet.dataeditors.IObjectEditor;
import org.eclipse.ve.sweet.dataeditors.IObjectEditorFactory;
import org.eclipse.ve.sweet.dataeditors.java.internal.JavaObject;

public class JavaObjectEditorFactory implements IObjectEditorFactory {

    public IObjectEditor construct() {
        return new JavaObject();
    }

}
