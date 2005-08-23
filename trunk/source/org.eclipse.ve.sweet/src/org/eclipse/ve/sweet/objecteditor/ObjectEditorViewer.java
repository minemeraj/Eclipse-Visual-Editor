/*
 * Copyright (C) 2005 by David Orme  <djo@coconut-palm-software.com>
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     David Orme - Initial API and implementation
 */
package org.eclipse.ve.sweet.objecteditor;

import org.eclipse.ve.sweet.CannotSaveException;
import org.eclipse.ve.sweet.fieldviewer.IFieldViewer;
import org.eclipse.ve.sweet.hinthandler.DelegatingHintHandler;
import org.eclipse.ve.sweet.hinthandler.IHintHandler;
import org.eclipse.ve.sweet.objectviewer.IObjectViewer;
import org.eclipse.ve.sweet.objectviewer.IPropertyEditor;
import org.eclipse.ve.sweet.table.CompositeTable;

/**
 * Class ObjectEditorViewer. An IFieldViewer implementation for the ObjectEditor SWT control
 * 
 * @author djo
 */
public class ObjectEditorViewer implements IFieldViewer {

	private ObjectEditor objectEditor;
	private IObjectViewer objectViewer;
	private IPropertyEditor propertyEditor;

	private DelegatingHintHandler hintHandler = new DelegatingHintHandler();

	/**
	 * Constructor ObjectEditorViewer.  Construct a ObjectEditorViewer.
	 * 
	 * @param control The ObjectEditor object to bind.
	 * @param objectViewer The IObjectViewer encapsulating the object we're editing
	 * @param propertyEditor The property of the object we're editing
	 */
	public ObjectEditorViewer(Object control, IObjectViewer objectViewer, IPropertyEditor propertyEditor) {
		objectEditor = (ObjectEditor) control;
		this.objectViewer = objectViewer;
		try {
			setInput(propertyEditor);
		} catch (CannotSaveException e) {
			throw new RuntimeException("Should not get CannotSaveException on construction!");
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#setInput(org.eclipse.ve.sweet.objectviewer.IPropertyEditor)
	 */
	public void setInput(IPropertyEditor input) throws CannotSaveException {
		propertyEditor = input;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#getInput()
	 */
	public IPropertyEditor getInput() {
		return propertyEditor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#getPropertyName()
	 */
	public String getPropertyName() {
		return propertyEditor.getName();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#setHintHandler(org.eclipse.ve.sweet.hinthandler.IHintHandler)
	 */
	public void setHintHandler(IHintHandler hintHandler) {
		this.hintHandler.delegate = hintHandler;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#isDirty()
	 */
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#setDirty(boolean)
	 */
	public void setDirty(boolean dirty) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#undo()
	 */
	public void undo() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#save()
	 */
	public void save() throws CannotSaveException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#validate()
	 */
	public String validate() {
		// TODO Auto-generated method stub
		return null;
	}

}
