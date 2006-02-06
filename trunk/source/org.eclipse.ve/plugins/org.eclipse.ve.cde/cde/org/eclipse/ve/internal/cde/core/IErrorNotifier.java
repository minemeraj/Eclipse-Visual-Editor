/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IErrorNotifier.java,v $
 *  $Revision: 1.3 $  $Date: 2006-02-06 23:38:37 $ 
 */
package org.eclipse.ve.internal.cde.core;

import java.util.*;

import org.eclipse.core.runtime.ListenerList;


/**
 * Implementers of this interface can hold errors and notify when status has changed.
 * 
 * @author richkulp
 */
public interface IErrorNotifier extends IErrorHolder {
		
	
	/**
	 * The type to use to ask for the adapter of type IErrorNotifier, in those case where it is being used.
	 */
	public static final Class ERROR_NOTIFIER_TYPE = IErrorNotifier.class;


	public interface ErrorListener {
		public void errorStatusChanged();	// Status has changed
		public void errorCleared(ErrorType error);	// A particular error has been cleared.
		public void errorAdded(ErrorType error);	// A particular error has been added.
	}

	public void addErrorListener(ErrorListener aListener);

	public void removeErrorListener(ErrorListener aListener);

	
	/**
	 * Compound error notifier. This is used when there can be more than one error notifier
	 * and you want it to look like only one to someone else.
	 * 
	 * @since 1.1.0
	 */
	public static class CompoundErrorNotifier implements IErrorNotifier, IErrorNotifier.ErrorListener {
		
		/**
		 * List of notifiers.
		 */
		protected List notifiers;
		
		/**
		 * Listeners for errors.
		 */
		protected ListenerList errorListeners;


		/*
		 *  (non-Javadoc)
		 * @see org.eclipse.ve.internal.cde.core.IErrorNotifier#addErrorListener(org.eclipse.ve.internal.cde.core.IErrorNotifier.ErrorListener)
		 */
		public void addErrorListener(ErrorListener aListener) {
			if(errorListeners == null) 
				errorListeners = new ListenerList(ListenerList.IDENTITY);
			errorListeners.add(aListener);
		}

		/*
		 *  (non-Javadoc)
		 * @see org.eclipse.ve.internal.cde.core.IErrorNotifier#removeErrorListener(org.eclipse.ve.internal.cde.core.IErrorNotifier.ErrorListener)
		 */
		public void removeErrorListener(ErrorListener aListener) {
			if (errorListeners != null)
				errorListeners.remove(aListener);
		}

		/**
		 * Fire that the error status has changed.
		 * 
		 * 
		 * @since 1.1.0
		 */
		protected void fireErrorStatusChanged(){
			if ( errorListeners != null ) {
				Object[] listeners = errorListeners.getListeners();
				for (int i = 0; i < listeners.length; i++) {
					((ErrorListener)listeners[i]).errorStatusChanged();
				}
			}
		}

		/**
		 * Fire an error added
		 * @param e
		 * 
		 * @since 1.1.0
		 */
		protected void fireAddedError(ErrorType e){
			if ( errorListeners != null ) {
				Object[] listeners = errorListeners.getListeners();
				for (int i = 0; i < listeners.length; i++) {
					((ErrorListener)listeners[i]).errorAdded(e);
				}
			}
		}

		/**
		 * Fire an cleared.
		 * @param e
		 * 
		 * @since 1.1.0
		 */
		protected void fireClearedError(ErrorType e){
			if ( errorListeners != null ) {
				Object[] listeners = errorListeners.getListeners();
				for (int i = 0; i < listeners.length; i++) {
					((ErrorListener)listeners[i]).errorCleared(e);
				}
			}
		}
		
		/**
		 * Add an error notifier to the list of notifiers being compounded.
		 * If the notifier is already in the list it will not be added again.
		 * <p>
		 * <b>Note:</b> When one is added, it will be determined if the error status
		 * is now different and it will signal out if it is.
		 * @param notifier notifier to add, <code>null</code> is valid and will be ignored.
		 * 
		 * @since 1.1.0
		 */
		public void addErrorNotifier(IErrorNotifier notifier) {
			if (notifier == null)
				return;
			if (notifiers == null)
				notifiers = new ArrayList(2);
			if (!notifiers.contains(notifier)) {
				int currentStatus = getErrorStatus();
				notifiers.add(notifier);
				notifier.addErrorListener(this);
				if (currentStatus < getErrorStatus())
					fireErrorStatusChanged();
			}
		}
		
		/**
		 * Remove the error notifier from the list of compounded notifiers.
		 * If the notifier is not in the list no error happens.
		 * <b>Note:</b> When one is removed, it will be determined if the error status
		 * is now different and it will signal out if it is.
		 * @param notifier notifier to remove. <code>null</code> is valid and will be ignored.
		 * 
		 * @since 1.1.0
		 */
		public void removeErrorNotifier(IErrorNotifier notifier) {
			if (notifiers != null && notifier != null) {
				int currentStatus = getErrorStatus();
				notifiers.remove(notifier);
				notifier.removeErrorListener(this);
				if (currentStatus > getErrorStatus())
					fireErrorStatusChanged();
			}
		}
		
		/**
		 * Call when the compound error notifier is no longer needed. It will
		 * remove itself as a listener to all of the error notifiers that
		 * are compounded in. 
		 * <p>
		 * <b>Note:</b> It is important that this be called so that
		 * error notifiers aren't being held onto. And notifiers aren't
		 * holding onto the compound notifier.
		 * 
		 * @since 1.1.0
		 */
		public void dispose() {
			if (notifiers != null) {
				for (int i = 0; i < notifiers.size(); i++) {
					IErrorNotifier notifier = (IErrorNotifier) notifiers.get(i);
					notifier.removeErrorListener(this);
				}
				notifiers = null;
			}
		}
		
		public int getErrorStatus() {
			if (notifiers == null)
				return ERROR_NONE;
			int maxStatus = ERROR_NONE;
			for (int i = 0; i < notifiers.size(); i++) {
				IErrorNotifier notifier = (IErrorNotifier) notifiers.get(i);
				int status = notifier.getErrorStatus();
				if (status == ERROR_SEVERE)
					return ERROR_SEVERE;	// None higher.
				maxStatus = Math.max(maxStatus, status);
			}
			return maxStatus;
		}

		public List getErrors() {
			if (notifiers == null)
				return Collections.EMPTY_LIST;
			List errors = new ArrayList();
			for (int i = 0; i < notifiers.size(); i++) {
				IErrorNotifier notifier = (IErrorNotifier) notifiers.get(i);
				errors.addAll(notifier.getErrors());
			}
			return errors;
		}

		public void errorStatusChanged() {
			fireErrorStatusChanged();
		}

		public void errorCleared(ErrorType error) {
			fireClearedError(error);
		}

		public void errorAdded(ErrorType error) {
			fireAddedError(error);
		}
		
		
	}
	
	/**
	 * Default implementation of ErrorListener that can be subclassed to provide
	 * specific overrides and let the others do nothing.
	 * @author richkulp
	 */
	public static abstract class ErrorListenerAdapter implements ErrorListener {

		/**
		 * @see org.eclipse.ve.internal.cde.core.IErrorNotifier.ErrorListener#errorCleared(Error)
		 */
		public void errorCleared(ErrorType error) {
		}

		/**
		 * @see org.eclipse.ve.internal.cde.core.IErrorNotifier.ErrorListener#errorAdded(Error)
		 */
		public void errorAdded(ErrorType error) {
		}

	}

}
