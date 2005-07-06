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

import org.eclipse.ve.sweet.CannotSaveException;
import org.eclipse.ve.sweet.fieldviewer.IFieldController;



/**
 * IObjectEditor.  Encapsulates editing operations on an object.
 *
 * @author djo
 */
public interface IObjectViewer {
    
    /**
     * Method setInput.
     * 
     * Sets the input object for this object editor.  The new input object
     * must have all the same properties as were being edited on the old
     * input object.
     * 
     * @param input The new input object.
     * @return true if the input could be successfully set.  false otherwise.
     */
    public boolean setInput(Object input);
    
    /**
     * Method getInput.
     * 
     * Returns the current input object for this object editor.
     * 
     * @return Object the current input object or null if there is none.
     */
    public Object getInput();
    
	/**
     * Method getProperty.
     * 
     * Returns an IPropertyEditor object specified by the given name.
     * 
	 * @param name The property name
	 * @return an IPropertyEditor bound to the specified object/property
     * @throws NoSuchMethodException if the property does not exist
	 */
	public IPropertyEditor getProperty(String name) throws NoSuchMethodException;
    
    /**
     * Method bind.
     * 
     * Analyze the specified control and (object, property) and return an
     * appropriate IFieldController for editing that (object, property)
     * pair using the specified control.
     * <p>
     * If we eventually implement masked editing, a masked edit implementation
     * will simply be another IFieldController implementation than the
     * standard IVerifier-based one, and will be automatically selected
     * and returned from this factory based on the presence of a masked-edit
     * based IVerifier on the property.
     * 
     * @param control The control to use as an editor
     * @param propertyName The name of the property to edit
     * @return An IFieldController configured to edit propertyName on this object
     */
    public IFieldController bind(Object control, String propertyName);

    /**
     * Method verifyAndSaveEditedFields.
     * 
     * Make sure that all fields that have been edited can be safely saved
     * and saves them.
     * 
     * @return true if all fields may be safely saved.  false otherwise.
     */
    public boolean verifyAndSaveEditedFields();
    
    /**
     * Method verifyAndSaveObject.
     * 
     * Returns if the underlying object is in a consistent state so that
     * changes can be committed to a persistent store.  If the underlying
     * persistent store implements transactional semantics, the actual
     * save operation will be performed here.  Otherwise, it will be performed
     * in the commit() method.
     * 
     * @return boolean true if all fields in the object have legal values
     * and the object itself is consistent and the object was successfully
     * saved.
     */
    public boolean verifyAndSaveObject();
    
    /**
     * Method addObjectListener.
     * 
     * Adds an IObjectListener to the set of listeners that will be notified
     * when the state of the edited object changes.
     * 
     * @param listener The IObjectListener to add.
     */
    public void addObjectListener(IEditStateListener listener);
    
    /**
     * Method removeObjectListener.
     * 
     * Removes an IObjectListener from the set of listeners that will be notified
     * when the state of the edited object changes.
     * 
     * @param listener The IObjectListener to remove.
     */
    public void removeObjectListener(IEditStateListener listener);
    
    /**
     * Method fireObjectListenerEvent.
     * 
     * Notifies all object listeners that the editing state of this object
     * has changed.  Normally this is called automatically from the 
     * IFieldController.
     */
    public void fireObjectListenerEvent();
    
    /**
     * Method addInputChangeListener.
     * 
     * Adds listener to the set of listeners that will be notified when
     * this object editor's input object changes.
     * 
     * @param listener The listener to add.
     */
    public void addInputChangeListener(IInputChangeListener listener);
    
    /**
     * Method removeInputChangeListener.
     * 
     * Removes listener from the set of listeners that will be notified when
     * this object editor's input object changes.
     * 
     * @param listener The listener to remove.
     */
    public void removeInputChangeListener(IInputChangeListener listener);
    
    /**
     * Method isDirty.
     * 
     * Returns if any of the objects' fields have been edited (are dirty)
     * but haven't been saved back to the object or if the fields have been
     * saved back to the object but the object itself hasn't been saved to
     * the persistent store.
     * 
     * @return true if fields have been edited but not saved, false otherwise.
     */
    public boolean isDirty();
    
    /**
     * Method commit.
     * 
     * Commit any changes that have occurred since the last commit()
     * 
     * @throws CannotSaveException if the data could not be saved.
     */
    public void commit() throws CannotSaveException;
    
    /**
     * Method refresh.
     * 
     * Refresh the specified object from its (multiuser) persistent store.
     */
    public void refresh();
    
    /**
     * Method rollback.
     * 
     * Roll back changes since the last commit() (if possible/applicable).
     */
    public void rollback();
    
    /**
     * Method delete.
     * 
     * Remove this object from the persistent store (if possible/applicable)
     */
    public void delete();
}

