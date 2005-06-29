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
package org.eclipse.ve.sweet.dataeditors;

/**
 * IObjectListener.  An interface for objects that listen to state change
 * events in an edited object.
 *
 * @author djo
 */
public interface IObjectListener {
    /**
     * Called whenever the edited object's state changes.
     * 
     * @param sender The IObjectEditor that is managing editing for this object
     */
    public void stateChanged(IObjectEditor sender);
}
