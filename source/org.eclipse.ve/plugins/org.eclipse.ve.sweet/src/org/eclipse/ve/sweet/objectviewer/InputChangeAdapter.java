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
package org.eclipse.ve.sweet.objectviewer;

/**
 * InputChangeAdapter.
 * 
 * A class assisting to implement IInputChangeListener.
 *
 * @author djo
 */
public abstract class InputChangeAdapter implements IInputChangeListener {

    /* (non-Javadoc)
     * @see org.eclipse.ve.sweet.dataeditors.IInputChangeListener#inputChanging(java.lang.Object, java.lang.Object)
     */
    public boolean inputChanging(Object oldInput, Object newInput) {
        return true;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ve.sweet.dataeditors.IInputChangeListener#inputChanged(java.lang.Object)
     */
    public abstract void inputChanged(Object newInput);

}
