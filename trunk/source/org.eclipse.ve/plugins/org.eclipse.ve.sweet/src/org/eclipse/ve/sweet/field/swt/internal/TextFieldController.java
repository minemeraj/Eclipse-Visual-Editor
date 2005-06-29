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
package org.eclipse.ve.sweet.field.swt.internal;


import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ve.sweet.CannotSaveException;
import org.eclipse.ve.sweet.converter.Converter;
import org.eclipse.ve.sweet.converter.IConverter;
import org.eclipse.ve.sweet.dataeditors.IObjectEditor;
import org.eclipse.ve.sweet.dataeditors.IPropertyEditor;
import org.eclipse.ve.sweet.field.IFieldController;
import org.eclipse.ve.sweet.field.swt.internal.interfaces.ITextField;
import org.eclipse.ve.sweet.hinthandler.HintHandler;
import org.eclipse.ve.sweet.reflect.RelaxedDuckType;
import org.eclipse.ve.sweet.verifier.IVerifier;
import org.eclipse.ve.sweet.verifier.Verifier;
import org.eclipse.ve.sweet.verifiers.reusable.ReadOnlyVerifier;


/**
 * TextFieldController. An IFieldController that can bind any object with a
 * setText (and optionally a getText) method.
 * 
 * @author djo
 */
public class TextFieldController implements IFieldController {
    private ITextField control;
    private IObjectEditor input;
    private IPropertyEditor property;
    private boolean readOnly;
    private boolean dirty = false;
    
    private IVerifier verifier;
    private IConverter object2String;
    private IConverter string2Object;
    
    private Object propertyValue;
    
    public TextFieldController(Object control, IObjectEditor object, IPropertyEditor property) {
        this.input = object;
        this.control = (ITextField) RelaxedDuckType.implement(ITextField.class, control);
        addListeners();
        try {
            setInput(property);
        } catch (CannotSaveException e) {
            throw new RuntimeException("Object just created: should not need to save", e);
        }
    }
    
    private void addListeners() {
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
        control.setText(valueToEdit);
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
	}
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.binding.IWidgetBinding#undo()
     */
    public void undo() {
        loadEditControl();
        dirty = false;
        input.fireObjectListenerEvent();
    }
    
    /* (non-Javadoc)
	 * @see org.eclipse.jface.binding.field.IFieldController#save()
	 */
	public void save() throws CannotSaveException {
        if (readOnly) {
            return;
        }
        if (!verify()) {
            throw new CannotSaveException(verifier.getHint());
        }
        String textValue = control.getText();
        propertyValue = string2Object.convert(textValue);
        property.set(propertyValue);
        dirty = false;
        input.fireObjectListenerEvent();
	}
    
	/* (non-Javadoc)
	 * @see org.eclipse.jface.binding.IWidgetBinding#verify()
	 */
	public boolean verify() {
        if (verifier.verifyFullValue(control.getText())) {
            HintHandler.getDefault().setMessage("");
            return true;
        } else {
            HintHandler.getDefault().setMessage(verifier.getHint());
            return false;
        }
	}

    /* (non-Javadoc)
     * @see com.db4o.binding.field.internal.IFieldController#setInput(com.db4o.binding.dataeditors.IPropertyEditor)
     */
    public void setInput(IPropertyEditor input) throws CannotSaveException {
        if (dirty) {
            save();
        }
        
        this.property = input;
        
        object2String = Converter.get(property.getType(), String.class.getName());
        string2Object = Converter.get(String.class.getName(), property.getType());
        
        readOnly = property.isReadOnly();
        if (readOnly) {
            verifier = ReadOnlyVerifier.getDefault();
        } else {
            verifier = property.getVerifier();
            if (verifier == null) {
                verifier = Verifier.get(property.getType());
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

    private VerifyListener verifyListener = new VerifyListener() {
        public void verifyText(VerifyEvent e) {
            String currentText = control.getText();
            String newValue = currentText.substring(0, e.start) + e.text + currentText.substring(e.end);
            if (!verifier.verifyFragment(newValue)) {
                e.doit = false;
                HintHandler.getDefault().setMessage(verifier.getHint());
            } else {
                dirty = true;
                input.fireObjectListenerEvent();
                HintHandler.getDefault().clearMessage();
            }
        }
    };
    
    private FocusListener focusListener = new FocusAdapter() {
        public void focusLost(FocusEvent e) {
            if (!verify()) {
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
