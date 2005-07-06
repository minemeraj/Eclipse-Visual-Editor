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

import org.eclipse.ve.sweet.validator.IValidator;

/**
 * IPropertyEditor.  An abstraction for a property.  Implementations may follow
 * JavaBeans naming specifications, may simply treat POJO fields as properties,
 * may represent a property of a remote object, etc...
 *
 * @author djo
 */
public interface IPropertyEditor {
    
    /**
     * Method getName.  Return's the property's name.
     * 
     * @return The property's name.
     */
    public String getName();
    
	/**
     * Method get().  Return the value of the property.
     * 
	 * @return the value of the property as an Object
	 */
	public Object get();
    
    /**
     * Method set().  Set the value of the property.
     * 
     * @param newValue The new value to set.
     */
    public void set(Object newValue);
    
    /**
     * Method isReadOnly().  Returns if the property is read-only.
     * 
     * @return true if the property can only be read but not written.
     */
    public boolean isReadOnly();
    
    /**
     * Method getType().  Returns the underlying property's type name.
     * 
     * @return String The property's type.
     */
    public String getType();
    
    /**
     * Method getVerifier().  Returns the property's verifier null if the 
     * property has no explicitly-defined verifier.
     * 
     * @return IVerifier The property's verifier.
     */
    public IValidator getVerifier();
    
    /**
     * Method getUIValues().  Returns the set of user interface choices 
     * corresponding to the property's valid values.  This is either 
     * defined as a part of the property or is derived by applying
     * toString() to each element in the legal values array.  If
     * no set of legal values has been defined, this method returns
     * null.
     * 
     * @return String[] the array of user interface choices corresponding
     * to the array of valid values.
     */
    public String[] getUIValues();
    
    /**
     * Method getLegalValues.  Returns an array containing all legal values
     * that this property can accept if the programmer has defined a
     * get<Prop>LegalValues() method for this property.  Otherwise, returns 
     * null.
     * 
     * @return Object[] an array containing all legal values that this
     * property can accept if the programmer has defined a LegalValues
     * method or null otherwise.
     */
    public Object[] getLegalValues();
}

