/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: IErrorNotifier.java,v $
 *  $Revision: 1.3 $  $Date: 2004-08-27 15:34:09 $ 
 */

/**
 * Implementers of this interface can hold errors and notify when status has changed.
 * 
 * @author richkulp
 */
public interface IErrorNotifier extends IErrorHolder {
		
	
	public static final Class ERROR_NOTIFIER_TYPE = IErrorNotifier.class;	// The type to use to ask for the adapter of type IErrorNotifier, in those case where it is being used.


	public interface ErrorListener {
		public void errorStatusChanged();	// Status has changed
		public void errorCleared(ErrorType error);	// A particular error has been cleared.
		public void errorAdded(ErrorType error);	// A particular error has been added.
	}

	public void addErrorListener(ErrorListener aListener);

	public void removeErrorListener(ErrorListener aListener);

	/**
	 * Default implementation of ErrorListener that can be subclassed to provide
	 * specific overrides and let the others do nothing.
	 * @author richkulp
	 */
	public static abstract class ErrorListenerAdapter implements ErrorListener {

		/**
		 * @see org.eclipse.ve.internal.java.core.IErrorNotifier.ErrorListener#errorCleared(Error)
		 */
		public void errorCleared(ErrorType error) {
		}

		/**
		 * @see org.eclipse.ve.internal.java.core.IErrorNotifier.ErrorListener#errorAdded(Error)
		 */
		public void errorAdded(ErrorType error) {
		}

	}

}
