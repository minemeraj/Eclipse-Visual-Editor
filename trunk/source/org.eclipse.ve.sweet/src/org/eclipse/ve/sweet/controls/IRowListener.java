/*
 * Copyright (C) 2005 David Orme <djo@coconut-palm-software.com>
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     David Orme     - Initial API and implementation
 */
package org.eclipse.ve.sweet.controls;

import org.eclipse.swt.widgets.Control;

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
	 * any listener returns false, the entire row change operation is aborted.<p>
	 *  
	 * @param sender The CompositeTable sending the event.
	 * @param currentObjectOffset The offset of the current object in the data structure.
	 * @param row The row control that is losing focus.
	 * @return true to permit the row change to occur; false otherwise.
	 */
	boolean requestRowChange(CompositeTable sender, int currentObjectOffset, Control row);
	
	/**
	 * Method depart.  Called after requstRowChange has been called to indicate that
	 * the focus is departing the specified row.
	 * 
	 * @param sender
	 * @param currentObjectOffset
	 * @param row
	 */
	void depart(CompositeTable sender, int currentObjectOffset, Control row);

	/**
	 * Method arrive.  Notifies receiver that the current row has just been changed.
	 * 
	 * @param sender The CompositeTable sending the event.
	 */
	void arrive(CompositeTable sender, int currentObjectOffset, Control newRow);

}
