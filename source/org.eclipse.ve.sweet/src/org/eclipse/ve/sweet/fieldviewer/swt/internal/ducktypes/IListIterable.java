package org.eclipse.ve.sweet.fieldviewer.swt.internal.ducktypes;

import java.util.ListIterator;

/**
 * A duck type for collections that support a listeIterator() method.
 * 
 * @author djo
 */
public interface IListIterable {
	/**
	 * Returns a ListIterator onto the collection.
	 * 
	 * @return a ListIterator to iterate over the collection's contents.
	 */
	ListIterator listIterator();
}
