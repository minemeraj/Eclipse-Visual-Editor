package org.eclipse.ve.sweet.controls;

/**
 * Interface IRowListener.  An interface for objects that want to listen to and have the
 * possibility of vetoing row change events on a CompositeTable.
 * 
 * @author djo
 */
public interface IRowListener {

	/**
	 * Method requestRowChange.  Requests permission to change rows.  This method is
	 * called immediately before a row change occurs.  Listeners must return true to
	 * grant permission for the row change to occur or return false to veto it.  If
	 * any listener returns false, the entire row change operation is aborted.
	 *  
	 * @param sender The CompositeTable sending the event.
	 * @return true to permit the row change to occur; false otherwise.
	 */
	boolean requestRowChange(CompositeTable sender);

	/**
	 * Method rowChanged.  Notifies receiver that the current row has just been changed.
	 * 
	 * @param sender The CompositeTable sending the event.
	 */
	void rowChanged(CompositeTable sender);

}
