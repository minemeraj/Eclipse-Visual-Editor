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
package org.eclipse.ve.sweet.dataeditors;

/**
 * IEditedObject. A relaxed duck interface for Java objects that are being edited.
 * These methods will be called at the appropriate times in the object's 
 * life cycle if they exist on the object.
 * 
 * Note that not all these methods will be applicable in all cases.  For example,
 * an IObjectEditor implementation may be designed for regular Java objects 
 * that are not themselves persistent.  In this case, the IEditedObject may or
 * may not receive commit, refresh, rollback, etc., events.
 *
 * @author djo
 */
public interface IEditedObject {

    /**
     * Method verifyObject.
     * 
     * Returns if the underlying object is in a consistent state so that
     * changes can be committed to a persistent store.  This method returns
     * the result of calling the verifyObject() method on the object if it 
     * exists on the underlying object, and returns true if the method does
     * not exist.
     * 
     * @return boolean true if all fields in the object have legal values
     * and the object itself is consistent.
     */
    public boolean verifyObject();
    
    /**
     * Method commit.
     * 
     * Commit any changes that have occurred to the specified object.
     */
    public void commit();
    
    /**
     * Method refresh.
     * 
     * Refresh the specified object from its (multiuser) persistent store.
     */
    public void refresh();
    
    /**
     * Method rollback.
     * 
     * Roll back changes to this object (if possible/applicable).
     */
    public void rollback();
    
    /**
     * Method delete.
     * 
     * Remove this object from the persistent store (if possible/applicable)
     */
    public void delete();
}
