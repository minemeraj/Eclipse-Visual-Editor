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
 * IInputChangeListener.
 * 
 * An interface for monitoring input changes on objects with an input property.
 *
 * @author djo
 */
public interface IInputChangeListener {
    /**
     * Request permission to change the input from oldInput to newInput.
     * The receiver may accept the input change by returning true or may
     * veto it by returning false.
     *  
     * @param oldInput The old input object
     * @param newInput The new input object
     * @return true if the change is permitted; false otherwise
     */
    public boolean inputChanging(Object oldInput, Object newInput);
    
    /**
     * Indicate that the input has been changed to the specified object.
     * 
     * @param newInput The new input object.
     */
    public void inputChanged(Object newInput);
}
