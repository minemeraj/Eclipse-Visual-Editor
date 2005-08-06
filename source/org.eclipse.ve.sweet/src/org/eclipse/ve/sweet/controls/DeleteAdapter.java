package org.eclipse.ve.sweet.controls;

/**
 * Class DeleteAdapter.  An adapter for the IDeleteHandler interface.
 * 
 * @author djo
 */
public class DeleteAdapter implements IDeleteHandler {

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.controls.IDeleteHandler#canDelete(int)
	 */
	public boolean canDelete(int rowInCollection) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.controls.IDeleteHandler#deleteRow(int)
	 */
	public void deleteRow(int rowInCollection) {
	}

}
