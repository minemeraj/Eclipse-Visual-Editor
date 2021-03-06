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
package org.eclipse.ve.sweet.fieldviewer.swt.internal;


import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ve.sweet.CannotSaveException;
import org.eclipse.ve.sweet.converter.ConverterRegistry;
import org.eclipse.ve.sweet.converter.IConverter;
import org.eclipse.ve.sweet.fieldviewer.IFieldViewer;
import org.eclipse.ve.sweet.fieldviewer.swt.internal.ducktypes.ITextControl;
import org.eclipse.ve.sweet.hinthandler.DelegatingHintHandler;
import org.eclipse.ve.sweet.hinthandler.IHintHandler;
import org.eclipse.ve.sweet.objectviewer.IObjectViewer;
import org.eclipse.ve.sweet.objectviewer.IPropertyEditor;
import org.eclipse.ve.sweet.reflect.RelaxedDuckType;
import org.eclipse.ve.sweet.validator.IValidator;
import org.eclipse.ve.sweet.validator.ValidatorRegistry;
import org.eclipse.ve.sweet.validators.reusable.ReadOnlyValidator;


/**
 * TextFieldViewer. An IFieldViewer that can bind any object with a
 * setText (and optionally a getText) method.
 * 
 * @author djo
 */
public class TextFieldViewer implements IFieldViewer {
    private ITextControl control;
    private IObjectViewer input;
    private IPropertyEditor property;
    private boolean readOnly;
    private boolean dirty = false;
    
    private IValidator verifier;
    private IConverter object2String;
    private IConverter string2Object;
    
    private DelegatingHintHandler hintHandler = new DelegatingHintHandler();
    
    private Object propertyValue;
	private int loadingControl=0;
    
    public TextFieldViewer(Object control, IObjectViewer object, IPropertyEditor property) {
        this.input = object;
        this.control = (ITextControl) RelaxedDuckType.implement(ITextControl.class, control);
        try {
            setInput(property);
        } catch (CannotSaveException e) {
            throw new RuntimeException("Object just created: should not need to save", e);
        }
        addListeners();
    }
    
    protected void addListeners() {
        control.addDisposeListener(disposeListener);
        control.addVerifyListener(verifyListener);
        control.addFocusListener(focusListener);
    }

    protected void removeListeners() {
        control.removeDisposeListener(disposeListener);
        control.removeVerifyListener(verifyListener);
        control.removeFocusListener(focusListener);
    }

    private void loadEditControl() {
        String valueToEdit = (String) object2String.convert(propertyValue);
        ++loadingControl;
        control.setText(valueToEdit);
        setDirty(false);
        --loadingControl;
    }

    /* (non-Javadoc)
	 * @see org.eclipse.jface.binding.IWidgetBinding#getPropertyName()
	 */
	public String getPropertyName() {
		return property.getName();
	}
    
	/* (non-Javadoc)
	 * @see org.eclipse.jface.binding.IWidgetBinding#isDirty()
	 */
	public boolean isDirty() {
		return dirty;
	}
    
	/* (non-Javadoc)
	 * @see org.eclipse.jface.binding.IWidgetBinding#setDirty(boolean)
	 */
	public void setDirty(boolean dirty) {
        this.dirty = dirty;
        if (loadingControl < 1) {
        	input.fireObjectListenerEvent();
        }
	}
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.binding.IWidgetBinding#undo()
     */
    public void undo() {
        loadEditControl();
        setDirty(false);
    }
    
    /* (non-Javadoc)
	 * @see org.eclipse.jface.binding.field.IFieldController#save()
	 */
	public void save() throws CannotSaveException {
        if (readOnly) {
            return;
        }
        String error = validate();
        if (error != null) {
            throw new CannotSaveException(error);
        }
        String textValue = control.getText();
        propertyValue = string2Object.convert(textValue);
        property.set(propertyValue);
        setDirty(false);
	}
    
	/* (non-Javadoc)
	 * @see org.eclipse.jface.binding.IWidgetBinding#verify()
	 */
	public String validate() {
        String message = verifier.isValid(control.getText());
        if (message == null) {
            hintHandler.clearMessage();
        } else {
            hintHandler.setMessage(message);
        }
        return message;
	}

    /* (non-Javadoc)
     * @see com.db4o.binding.field.internal.IFieldController#setInput(com.db4o.binding.dataeditors.IPropertyEditor)
     */
    public void setInput(IPropertyEditor input) throws CannotSaveException {
    	// If we're resetting the dirty flag, make sure everyone knows about it now...
    	if (isDirty()) {
    		setDirty(false);
    	}
    	
        this.property = input;
        
        object2String = ConverterRegistry.get(property.getType(), String.class.getName());
        string2Object = ConverterRegistry.get(String.class.getName(), property.getType());
        
        readOnly = property.isReadOnly();
        if (readOnly) {
            verifier = ReadOnlyValidator.getDefault();
        } else {
            verifier = property.getVerifier();
            if (verifier == null) {
                verifier = ValidatorRegistry.get(property.getType());
            }
        }
        
        propertyValue = property.get();
        loadEditControl();
    }

    /* (non-Javadoc)
     * @see com.db4o.binding.field.internal.IFieldController#getInput()
     */
    public IPropertyEditor getInput() {
        return property;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#setHintHandler(org.eclipse.ve.sweet.hinthandler.IHintHandler)
     */
    public void setHintHandler(IHintHandler hintHandler) {
        this.hintHandler.delegate = hintHandler;
    }
    
	// Listeners here -------------------------------------------------------------------------------
	
	private VerifyListener verifyListener = new VerifyListener() {
        public void verifyText(VerifyEvent e) {
            String currentText = control.getText();
            String newValue = currentText.substring(0, e.start) + e.text + currentText.substring(e.end);
            String error = verifier.isValidPartialInput(newValue);
            if (error != null) {
                e.doit = false;
                hintHandler.setMessage(error);
            } else {
            	setDirty(true);
                hintHandler.clearMessage();
            }
        }
    };
    
    private FocusListener focusListener = new FocusAdapter() {
        public void focusLost(FocusEvent e) {
            if (validate() != null) {
                comeBackHerePlease();
            }
        }
    };
    
    private DisposeListener disposeListener = new DisposeListener() {
        public void widgetDisposed(DisposeEvent e) {
            removeListeners();
        }
    };

    protected void comeBackHerePlease() {
        Display.getCurrent().asyncExec(new Runnable() {
            public void run() {
                control.setFocus();
            }
        });
    }

}
