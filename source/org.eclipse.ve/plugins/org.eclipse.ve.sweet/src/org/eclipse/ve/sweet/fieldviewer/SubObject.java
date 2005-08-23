package org.eclipse.ve.sweet.fieldviewer;

import org.eclipse.ve.sweet.CannotSaveException;
import org.eclipse.ve.sweet.hinthandler.DelegatingHintHandler;
import org.eclipse.ve.sweet.hinthandler.IHintHandler;
import org.eclipse.ve.sweet.objectviewer.IObjectViewer;
import org.eclipse.ve.sweet.objectviewer.IPropertyEditor;

public class SubObject implements IFieldViewer {

	private IObjectViewer childObjectEditor;
	private IPropertyEditor propertyEditor;

    private DelegatingHintHandler hintHandler = new DelegatingHintHandler();
    
	/**
	 * @param object
	 * @param propertyEditor
	 */
	public SubObject(IObjectViewer object, IPropertyEditor propertyEditor) {
		this.childObjectEditor = object;
		this.propertyEditor = propertyEditor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#setInput(org.eclipse.ve.sweet.objectviewer.IPropertyEditor)
	 */
	public void setInput(IPropertyEditor input) throws CannotSaveException {
		String error = childObjectEditor.validateAndSaveObject();
		if (error != null) {
			hintHandler.setMessage(error);
			throw new CannotSaveException(error);
		}
		
		childObjectEditor.setInput(propertyEditor.get());
		this.propertyEditor = input;
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
		return childObjectEditor.isDirty();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#setDirty(boolean)
	 */
	public void setDirty(boolean dirty) {
		if (!dirty) {
			childObjectEditor.rollback();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#undo()
	 */
	public void undo() {
		childObjectEditor.rollback();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#save()
	 */
	public void save() throws CannotSaveException {
		String error = childObjectEditor.validateAndSaveObject();
		if (error != null) {
			throw new CannotSaveException(error);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#validate()
	 */
	public String validate() {
		return childObjectEditor.validateAndSaveEditedFields();
	}

}
