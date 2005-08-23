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
package org.eclipse.ve.sweet.objectviewer.pojo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.ve.sweet.CannotSaveException;
import org.eclipse.ve.sweet.fieldviewer.FieldViewerFactory;
import org.eclipse.ve.sweet.fieldviewer.IFieldViewer;
import org.eclipse.ve.sweet.fieldviewer.SubObject;
import org.eclipse.ve.sweet.hinthandler.DelegatingHintHandler;
import org.eclipse.ve.sweet.hinthandler.IHintHandler;
import org.eclipse.ve.sweet.objectviewer.IEditStateListener;
import org.eclipse.ve.sweet.objectviewer.IEditedObject;
import org.eclipse.ve.sweet.objectviewer.IInputChangeListener;
import org.eclipse.ve.sweet.objectviewer.IObjectViewer;
import org.eclipse.ve.sweet.objectviewer.IObjectViewerFactory;
import org.eclipse.ve.sweet.objectviewer.IPropertyEditor;
import org.eclipse.ve.sweet.objectviewer.NullProperty;
import org.eclipse.ve.sweet.objectviewer.ObjectViewerFactory;
import org.eclipse.ve.sweet.objectviewer.pojo.internal.JavaProperty;
import org.eclipse.ve.sweet.reflect.RelaxedDuckType;

/**
 * JavaObjectViewer. An implementation of IObjectViewer for a regular Java object.
 * 
 * Persistence frameworks can be supported by inheriting from this class and
 * overriding the saveObject, commitObject, refreshObject, rollbackObject,
 * and deleteObject methods.
 */
public class JavaObjectViewer implements IObjectViewer {
    private Object input = null;
    private IEditedObject inputBean = null;
    private boolean dirty = false;

    // The IFieldViewers that are bound to this object
    private LinkedList bindings = new LinkedList();

    private LinkedList objectListeners = new LinkedList();
    
    private DelegatingHintHandler hintHandler = new DelegatingHintHandler();
    
    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectViewer#setInput(java.lang.Object)
     */
    public boolean setInput(Object input) {
        if (this.input != null && (validateAndSaveEditedFields() != null || validateAndSaveObject() != null || input == null)) {
            return false;
        }
        try {
            commit();
        } catch (CannotSaveException e) {
            throw new RuntimeException("Should be able to save if fields and object verify", e);
        }
        
        if (!fireInputChangingEvent(this.input, input)) {
            return false;
        }
        
        this.input = input;
        inputBean = (IEditedObject) RelaxedDuckType.implement(IEditedObject.class, input);
        refreshFieldsFromInput();
        
        fireInputChangedEvent(input);
        return true;
    }

    private void refreshFieldsFromInput() {
        for (Iterator bindingIter = bindings.iterator(); bindingIter.hasNext();) {
            IFieldViewer controller = (IFieldViewer) bindingIter.next();
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
     * @see com.db4o.binding.dataeditors.IObjectViewer#getInput()
     */
    public Object getInput() {
        return input;
    }

    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectViewer#getProperty(java.lang.String)
     */
    public IPropertyEditor getProperty(String name) throws NoSuchMethodException {
    	if (input != null)
    		return JavaProperty.construct(getInput(), name);
    	else
    		return new NullProperty(name);
    }
    
    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectViewer#bind(Object, java.lang.String)
     */
    public IFieldViewer bind(Object control, String propertyName) {
        IPropertyEditor propertyEditor;
        try {
            propertyEditor = getProperty(propertyName);
        } catch (NoSuchMethodException e) {
            return null;
        }
        
        IFieldViewer result = FieldViewerFactory.construct(control, this, propertyEditor);
        
        if (result != null) {
            bindings.addLast(result);
        }
        
        if (result.validate() != null) {
            dirty=true;
        }
        
        // Remember to tell the new IFieldViewer about any delegated IHintHandler
        result.setHintHandler(hintHandler.delegate);
        
        return result;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ve.sweet.objectviewer.IObjectViewer#bind(java.lang.String)
     */
    public IObjectViewer bind(String propertyName) {
    	return bind(propertyName, ObjectViewerFactory.factory);
    }
    
    private HashMap subObjects = new HashMap();
    
    /* (non-Javadoc)
     * @see org.eclipse.ve.sweet.objectviewer.IObjectViewer#bind(java.lang.String, org.eclipse.ve.sweet.objectviewer.IObjectViewerFactory)
     */
    public IObjectViewer bind(String propertyName, IObjectViewerFactory factory) {
    	IObjectViewer result = (IObjectViewer) subObjects.get(propertyName);
    	if (result != null) {
            if (result.validateAndSaveEditedFields() != null) {
                dirty=true;
            }
    		return result;
    	}
    	
    	IPropertyEditor propertyEditor;
        try {
            propertyEditor = getProperty(propertyName);
        } catch (NoSuchMethodException e) {
            return null;
        }
        
    	result = factory.construct();
        subObjects.put(propertyName, result);
        
        IFieldViewer wrapper = new SubObject(result, propertyEditor);
        bindings.addLast(wrapper);
        
        if (wrapper.validate() != null) {
            dirty=true;
        }
        
        // Remember to tell the new IFieldViewer about any delegated IHintHandler
        wrapper.setHintHandler(hintHandler.delegate);
        
        return result;
    }

    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectViewer#verifyEditedFields()
     */
    public String validateAndSaveEditedFields() {
        for (Iterator bindingsIter = bindings.iterator(); bindingsIter.hasNext();) {
            IFieldViewer field = (IFieldViewer) bindingsIter.next();
            if (field.isDirty()) {
                dirty=true;
                String error = field.validate();
                if (error != null) {
                    return error;
                }
                try {
                    field.save();
                } catch (CannotSaveException e) {
                    return "Unknown error: field validated but it could not save.";
                }
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectViewer#verifyObject()
     */
    public String validateAndSaveObject() {
    	String error = validateAndSaveEditedFields();
    	if (error != null) {
    		return error;
    	}
        /*
         * The return type for RelaxedDuckType is false for boolean types if
         * the method does not exist.  So we have to test explicitly here...
         */
    	error = inputBean.validateObject();
    	if (error != null) {
    		return error;
    	}
        
        // If the underlying persistent store supports transactions, this is
        // where the object should be saved back to the persistent store,
        // but the transaction not committed yet.  The object will appear
        // dirty until the transaction is committed.
        saveObject(input);
        
        return null;
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
     * @see com.db4o.binding.dataeditors.IObjectViewer#commit()
     */
    public void commit() throws CannotSaveException {
        if (input == null) {
            return;
        }
        String error = validateAndSaveEditedFields();
        if (error != null)
            throw new CannotSaveException(error);
        
        // Ask the bean to verify itself for consistency
        error = validateAndSaveObject();
        if (error != null)
            throw new CannotSaveException(error);
        
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
     * @see com.db4o.binding.dataeditors.IObjectViewer#refresh()
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
     * @see com.db4o.binding.dataeditors.IObjectViewer#rollback()
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
     * @see com.db4o.binding.dataeditors.IObjectViewer#delete()
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
     * @see com.db4o.binding.dataeditors.IObjectViewer#addObjectListener(com.db4o.binding.dataeditors.IObjectListener)
     */
    public void addObjectListener(IEditStateListener listener) {
        objectListeners.add(listener);
    }

    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectViewer#removeObjectListener(com.db4o.binding.dataeditors.IObjectListener)
     */
    public void removeObjectListener(IEditStateListener listener) {
        objectListeners.remove(listener);
    }
    
    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectViewer#fireObjectListenerEvent()
     */
    public void fireObjectListenerEvent() {
        for (Iterator listeners = objectListeners.iterator(); listeners.hasNext();) {
            IEditStateListener listener = (IEditStateListener) listeners.next();
            listener.stateChanged(this);
        }
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ve.sweet.objectviewer.IObjectViewer#setHintHandler(org.eclipse.ve.sweet.hinthandler.IHintHandler)
     */
    public void setHintHandler(IHintHandler hintHandler) {
        this.hintHandler.delegate = hintHandler;
    }
    
    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectViewer#isDirty()
     */
    public boolean isDirty() {
        if (dirty)
            return true;
        
        for (Iterator bindingsIter = bindings.iterator(); bindingsIter.hasNext();) {
            IFieldViewer fieldController = (IFieldViewer) bindingsIter.next();
            if (fieldController.isDirty()) {
                return true;
            }
        }
        return false;
    }
    
    private LinkedList inputChangeListeners = new LinkedList();

    /* (non-Javadoc)
     * @see org.eclipse.ve.sweet.dataeditors.IObjectViewer#addInputChangeListener(org.eclipse.ve.sweet.dataeditors.IInputChangeListener)
     */
    public void addInputChangeListener(IInputChangeListener listener) {
        inputChangeListeners.add(listener);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ve.sweet.dataeditors.IObjectViewer#removeInputChangeListener(org.eclipse.ve.sweet.dataeditors.IInputChangeListener)
     */
    public void removeInputChangeListener(IInputChangeListener listener) {
        inputChangeListeners.remove(listener);
    }
    
    private boolean fireInputChangingEvent(Object oldInput, Object newInput) {
        for (Iterator inputChangeListenersIter = inputChangeListeners.iterator(); inputChangeListenersIter.hasNext();) {
            IInputChangeListener listener = (IInputChangeListener) inputChangeListenersIter.next();
            if (!listener.inputChanging(oldInput, newInput))
                return false;
        }
        return true;
    }

    private void fireInputChangedEvent(Object newInput) {
        for (Iterator inputChangeListenersIter = inputChangeListeners.iterator(); inputChangeListenersIter.hasNext();) {
            IInputChangeListener listener = (IInputChangeListener) inputChangeListenersIter.next();
            listener.inputChanged(newInput);
        }
    }
}
