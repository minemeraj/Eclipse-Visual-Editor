package org.eclipse.ve.sweet.objectviewer;

import java.beans.PropertyChangeListener;

import org.eclipse.ve.sweet.validator.IValidator;
import org.eclipse.ve.sweet.validators.reusable.ReadOnlyValidator;

/**
 * Class NullProperty.  A NullObjectPattern implementation of IPropertyEditor.
 * 
 * @author djo
 */
public class NullProperty implements IPropertyEditor {
	
	private String name;

	public NullProperty(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.objectviewer.IPropertyEditor#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.objectviewer.IPropertyEditor#get()
	 */
	public Object get() {
		return "null";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.objectviewer.IPropertyEditor#set(java.lang.Object)
	 */
	public void set(Object newValue) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.objectviewer.IPropertyEditor#isReadOnly()
	 */
	public boolean isReadOnly() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.objectviewer.IPropertyEditor#getType()
	 */
	public String getType() {
		return "java.lang.String";
	}
	
	private IValidator validator = new ReadOnlyValidator();

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.objectviewer.IPropertyEditor#getVerifier()
	 */
	public IValidator getVerifier() {
		return validator;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.objectviewer.IPropertyEditor#getUIValues()
	 */
	public String[] getUIValues() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.objectviewer.IPropertyEditor#getLegalValues()
	 */
	public Object[] getLegalValues() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.objectviewer.IPropertyEditor#getInsertHandler()
	 */
	public IInsertHandler getInsertHandler() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.objectviewer.IPropertyEditor#getDeleteHandler()
	 */
	public IDeleteHandler getDeleteHandler() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.objectviewer.IPropertyEditor#addChangeListener(java.beans.PropertyChangeListener)
	 */
	public void addChangeListener(PropertyChangeListener l) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.objectviewer.IPropertyEditor#removeChangeListener(java.beans.PropertyChangeListener)
	 */
	public void removeChangeListener(PropertyChangeListener l) {
	}

}
