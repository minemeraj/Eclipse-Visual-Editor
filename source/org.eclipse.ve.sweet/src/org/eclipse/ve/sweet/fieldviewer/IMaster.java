package org.eclipse.ve.sweet.fieldviewer;

import org.eclipse.ve.sweet.objectviewer.IObjectViewer;

/**
 * Interface IMaster.  An interface for IFieldViewers that can also behave as master objects
 * in a master-detail relationship.
 *  
 * @author djo
 */
public interface IMaster {

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#addDetailViewer(org.eclipse.ve.sweet.objectviewer.IObjectViewer)
	 */
	public void addDetailViewer(IObjectViewer detailViewer);

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#removeDetailViewer(org.eclipse.ve.sweet.objectviewer.IObjectViewer)
	 */
	public void removeDetailViewer(IObjectViewer detailViewer);

}