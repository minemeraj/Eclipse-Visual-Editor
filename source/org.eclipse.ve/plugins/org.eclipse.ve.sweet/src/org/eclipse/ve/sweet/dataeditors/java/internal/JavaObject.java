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
package org.eclipse.ve.sweet.dataeditors.java.internal;

import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ve.sweet.CannotSaveException;
import org.eclipse.ve.sweet.dataeditors.IEditedObject;
import org.eclipse.ve.sweet.dataeditors.IObjectEditor;
import org.eclipse.ve.sweet.dataeditors.IObjectListener;
import org.eclipse.ve.sweet.dataeditors.IPropertyEditor;
import org.eclipse.ve.sweet.field.FieldControllerFactory;
import org.eclipse.ve.sweet.field.IFieldController;
import org.eclipse.ve.sweet.reflect.RelaxedDuckType;

public class JavaObject implements IObjectEditor {
    private Object input = null;
    private IEditedObject inputBean = null;
    

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
        return result;
    }

    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectEditor#verifyEditedFields()
     */
    public boolean verifyAndSaveEditedFields() {
        for (Iterator bindingsIter = bindings.iterator(); bindingsIter.hasNext();) {
            IFieldController field = (IFieldController) bindingsIter.next();
            if (field.isDirty()) {
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
        return true;
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
    }

    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectEditor#refresh()
     */
    public void refresh() {
        inputBean.refresh();
        refreshFieldsFromInput();
    }

    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectEditor#rollback()
     */
    public void rollback() {
        inputBean.rollback();
        refresh();
    }

    /* (non-Javadoc)
     * @see com.db4o.binding.dataeditors.IObjectEditor#delete()
     */
    public void delete() {
        inputBean.delete();
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
        for (Iterator bindingsIter = bindings.iterator(); bindingsIter.hasNext();) {
            IFieldController fieldController = (IFieldController) bindingsIter.next();
            if (fieldController.isDirty()) {
                return true;
            }
        }
        return false;
    }

}
