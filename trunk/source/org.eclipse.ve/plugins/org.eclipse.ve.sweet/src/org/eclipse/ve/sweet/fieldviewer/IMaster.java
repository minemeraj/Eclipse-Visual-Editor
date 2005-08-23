package org.eclipse.ve.sweet.fieldviewer;

import org.eclipse.ve.sweet.objectviewer.IObjectViewer;

/**
 * Interface IMaster.  An interface for IFieldViewers that can also behave as master objects
 * in a master-detail relationship.
 *  
 * @author djo
 */
public interface IMaster {

	/**
	 * Method addDetailViewer.  Add the specified IObjectViewer to the set of IObjectViewers whose
	 * input will be refreshed when the "currently focused object" changes.  The concept of the 
	 * "currently focused object" is implementation-dependent.
	 *
	 * @param detailViewer The IObjectViewer to add.
	 */
	public void addDetailViewer(IObjectViewer detailViewer);

	/**
	 * Method addDetailViewer.  Removes the specified IObjectViewer from the set of IObjectViewers whose
	 * input will be refreshed when the "currently focused object" changes.  The concept of the 
	 * "currently focused object" is implementation-dependent.
	 *
	 * @param detailViewer The IObjectViewer to remove.
	 */
	public void removeDetailViewer(IObjectViewer detailViewer);

}