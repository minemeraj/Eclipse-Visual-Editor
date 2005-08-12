package org.eclipse.ve.sweet.fieldviewer.swt.internal;

import org.eclipse.ve.sweet.CannotSaveException;
import org.eclipse.ve.sweet.controls.CompositeTable;
import org.eclipse.ve.sweet.fieldviewer.IFieldViewer;
import org.eclipse.ve.sweet.hinthandler.IHintHandler;
import org.eclipse.ve.sweet.objectviewer.IPropertyEditor;

/**
 * Class CompositeTableViewer.  An IFieldViewer that binds the objects in any 
 * IListIterable (duck typed) property to the row objects in a CompositeTableViewer.
 * This object will iterate over all objects in the row object's tab order and will
 * use ObjectViewerFactory.construct() to generate editors for each of these controls.
 * It uses Java reflection to figure out the names of the corresponding property
 * for each control and binds each control to an object property of the same name.
 * 
 * @author djo
 */
public class CompositeTableViewer implements IFieldViewer {
	private CompositeTable table = null;
	private IPropertyEditor input;
	private IHintHandler hintHandler;

	public void setInput(IPropertyEditor input) throws CannotSaveException {
		this.input = input;
	}

	public IPropertyEditor getInput() {
		return input;
	}

	public String getPropertyName() {
		return input.getName();
	}

	public void setHintHandler(IHintHandler hintHandler) {
		this.hintHandler = hintHandler;
	}

	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setDirty(boolean dirty) {
		// TODO Auto-generated method stub

	}

	public void undo() {
		// TODO Auto-generated method stub

	}

	public void save() throws CannotSaveException {
		// TODO Auto-generated method stub

	}

	public String validate() {
		// TODO Auto-generated method stub
		return null;
	}

}
