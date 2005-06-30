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
package org.eclipse.ve.sweet.dataeditors.java;

import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.ve.sweet.CannotSaveException;
import org.eclipse.ve.sweet.dataeditors.IEditedObject;
import org.eclipse.ve.sweet.dataeditors.IObjectEditor;
import org.eclipse.ve.sweet.dataeditors.IObjectListener;
import org.eclipse.ve.sweet.dataeditors.IPropertyEditor;
import org.eclipse.ve.sweet.dataeditors.java.internal.JavaProperty;
import org.eclipse.ve.sweet.field.FieldControllerFactory;
import org.eclipse.ve.sweet.field.IFieldController;
import org.eclipse.ve.sweet.reflect.RelaxedDuckType;

/**
 * JavaObject. An implementation of IObjectEditor for a regular Java object.
 * 
 * Persistence frameworks can be supported by inheriting from this class and
 * overriding the saveObject, commitObject, refreshObject, rollbackObject,
 * and deleteObject methods.
 */
public class JavaObject implements IObjectEditor {
    private Object input = null;
    private IEditedObject inputBean = null;
    private boolean dirty = false;

    // The IFieldEditors that are bound to this object
    private LinkedList bindings = new LinkedList();

    private LinkedList objectListeners = new LinkedList();

    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectEditor#setInput(java.lang.Object)
     */
    public boolean setInput(Object input) {
        if (this.input != null && (!verifyAndSaveEditedFields() || !verifyAndSaveObject() || input == null)) {
            return false;
        }
        try {
            commit();
        } catch (CannotSaveException e) {
            throw new RuntimeException("Should be able to save if fields and object verify", e);
        }
        this.input = input;
        inputBean = (IEditedObject) RelaxedDuckType.implement(IEditedObject.class, input);
        refreshFieldsFromInput();
        return true;
    }

    private void refreshFieldsFromInput() {
        for (Iterator bindingIter = bindings.iterator(); bindingIter.hasNext();) {
            IFieldController controller = (IFieldController) bindingIter.next();
            try {
                controller.setInput(getProperty(controller.getPropertyName()));
            } catch (CannotSaveException e) {
                throw new RuntimeException("Should be able to save if fields and object verify", e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Should be able to save if fields and object verify", e);
            }
        }
        dirty=false;
    }

    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectEditor#getInput()
     */
    public Object getInput() {
        return input;
    }

    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectEditor#getProperty(java.lang.String)
     */
    public IPropertyEditor getProperty(String name) throws NoSuchMethodException {
        return JavaProperty.construct(getInput(), name);
    }
    
    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectEditor#getWidgetBinding(org.eclipse.swt.widgets.Control, java.lang.String)
     */
    public IFieldController bind(Object control, String propertyName) {
        IPropertyEditor propertyEditor;
        try {
            propertyEditor = getProperty(propertyName);
        } catch (NoSuchMethodException e) {
            return null;
        }
        
        IFieldController result = FieldControllerFactory.construct(control, this, propertyEditor);
        
        if (result != null) {
            bindings.addLast(result);
        }
        
        if (!result.verify()) {
            dirty=true;
        }
        
        return result;
    }

    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectEditor#verifyEditedFields()
     */
    public boolean verifyAndSaveEditedFields() {
        for (Iterator bindingsIter = bindings.iterator(); bindingsIter.hasNext();) {
            IFieldController field = (IFieldController) bindingsIter.next();
            if (field.isDirty()) {
                dirty=true;
                if (!field.verify()) {
                    return false;
                }
                try {
                    field.save();
                } catch (CannotSaveException e) {
                    return false;
                }
            }
        }
        return true;
    }

    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectEditor#verifyObject()
     */
    public boolean verifyAndSaveObject() {
        if (!verifyAndSaveEditedFields()) {
            return false;
        }
        /*
         * The return type for RelaxedDuckType is false for boolean types if
         * the method does not exist.  So we have to test explicitly here...
         */
        if (RelaxedDuckType.includes(input, "verifyObject", new Class[] {}) && !inputBean.verifyObject()) {
            return false;
        }
        
        // If the underlying persistent store supports transactions, this is
        // where the object should be saved back to the persistent store,
        // but the transaction not committed yet.  The object will appear
        // dirty until the transaction is committed.
        saveObject(input);
        
        return true;
    }

    /**
     * Method SaveObject.
     * 
     * Override this method to supply your own object saving semantics.
     * 
     * If your underlying persistent store does not supply transaction 
     * semantics, you should leave this empty and override commitObject()
     * instead.
     * 
     * @param toSave The object to save to the persistent store.
     */
    protected void saveObject(Object toSave) {
    }

    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectEditor#commit()
     */
    public void commit() throws CannotSaveException {
        if (input == null) {
            return;
        }
        if (!verifyAndSaveEditedFields())
            throw new CannotSaveException("Unable to save edited fields");
        
        // Ask the bean to verify itself for consistency
        if (!verifyAndSaveObject())
            throw new CannotSaveException("Unable to save object");
        
        // Let the bean itself know it is about to be saved
        inputBean.commit();
        
        // This is where we would normally commit the changes to our
        // persistent store (Db4o, Hibernate, EJB3, etc.)
        commitObject(input);
        
        // Now reset the dirty flag
        dirty=false;
    }

    /**
     * Method CommitObject.
     * 
     * Commit the current transaction, if the underlying persistent store
     * supports transactions, otherwise just save the object here.
     * 
     * @param toCommit The object to commit.
     */
    protected void commitObject(Object toCommit) {
    }

    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectEditor#refresh()
     */
    public void refresh() {
        // This is where we would normally refresh the object from the
        // persistent store (Db4o, Hibernate, EJB3, etc.)
        refreshObject(input);

        inputBean.refresh();
        refreshFieldsFromInput();
    }

    /**
     * Method refreshObject.
     * 
     * Refresh the object from the persistent store.  ie: make sure that all
     * multiuser updates have been applied to the local copy.
     * 
     * @param input The input object to refresh.
     */
    protected void refreshObject(Object input) {
    }

    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectEditor#rollback()
     */
    public void rollback() {
        // This is where we would normally rollback the transaction in the
        // persistent store (Db4o, Hibernate, EJB3, etc.)
        rollbackObject(input);

        inputBean.rollback();
        refresh();
    }

    /**
     * Method rollbackObject.
     * 
     * Rollback the current transaction, if the underlying persistent store
     * supports transactions.  Refresh() will be called automatically
     * after this.
     * 
     * @param input The input object whose changes should be rolled back.
     */
    protected void rollbackObject(Object input) {
    }

    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectEditor#delete()
     */
    public void delete() {
        inputBean.delete();

        // This is where we would normally delete the object from the
        // persistent store (Db4o, Hibernate, EJB3, etc.)
        deleteObject(input);
    }
    
    /**
     * Method deleteObject.
     * 
     * Delete the specified object from the persistent store.
     * 
     * @param toDelete The object to delete.
     */
    protected void deleteObject(Object toDelete) {
    }

    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectEditor#addObjectListener(com.db4o.binding.dataeditors.IObjectListener)
     */
    public void addObjectListener(IObjectListener listener) {
        objectListeners.add(listener);
    }

    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectEditor#removeObjectListener(com.db4o.binding.dataeditors.IObjectListener)
     */
    public void removeObjectListener(IObjectListener listener) {
        objectListeners.remove(listener);
    }
    
    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectEditor#fireObjectListenerEvent()
     */
    public void fireObjectListenerEvent() {
        for (Iterator listeners = objectListeners.iterator(); listeners.hasNext();) {
            IObjectListener listener = (IObjectListener) listeners.next();
            listener.stateChanged(this);
        }
    }
    
    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectEditor#isDirty()
     */
    public boolean isDirty() {
        if (dirty)
            return true;
        
        for (Iterator bindingsIter = bindings.iterator(); bindingsIter.hasNext();) {
            IFieldController fieldController = (IFieldController) bindingsIter.next();
            if (fieldController.isDirty()) {
                return true;
            }
        }
        return false;
    }

}
