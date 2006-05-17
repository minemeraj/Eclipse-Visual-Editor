/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ErrorNotifier.java,v $
 *  $Revision: 1.3 $  $Date: 2006-05-17 20:13:53 $ 
 */
package org.eclipse.ve.internal.cde.core;

import java.util.*;

import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.core.runtime.ListenerList;
 

/**
 * A default implementation of IErrorNotifier. It can be used as a delegatee. Someone else
 * who wants to act like an IErrorHolder but doesn't want to reimplement everything can
 * just forward the messages to an instance of this class.
 * <p>
 * Or it can be subclassed to provide the functions.
 * @since 1.1.0
 */
public class ErrorNotifier implements IErrorNotifier {
	
	/**
	 * An ErrorNotifier that is also an Adapter. A convienence class for
	 * adapters that want to be error notifiers too.
	 * 
	 * @since 1.1.0
	 */
	public static class ErrorNotifierAdapter extends ErrorNotifier implements Adapter {

		public void notifyChanged(Notification notification) {
		}

		private Notifier target;
		
		public Notifier getTarget() {
			return target;
		}

		public void setTarget(Notifier newTarget) {
			target = newTarget;
		}

		public boolean isAdapterForType(Object type) {
			return type == ERROR_NOTIFIER_TYPE || type == ERROR_HOLDER_TYPE;
		}
		
	}
	
	/**
	 * Map of errors, key will be Structural feature or <code>NOT_PROPERTY</code> for non-property errors.
	 * The values will be either an Error (if only one) or a List of Errors if more than one.
	 */
	protected Map keyedErrors;
	
	/**
	 * Listeners for errors.
	 */
	protected ListenerList errorListeners;
	
	/**
	 * Key in keyedErrors for not a property error. 
	 */
	protected static final Object NOT_PROPERTY = new Object();

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IErrorHolder#getErrorStatus()
	 */
	public int getErrorStatus() {
		// See whether any of the featuer errors are warning or information
		if (keyedErrors == null || keyedErrors.isEmpty())
			return ERROR_NONE;
		Iterator errors = keyedErrors.values().iterator();
		int maxStatus = ERROR_NONE;
		while( errors.hasNext() ) {
			Object error = errors.next();
			if (error instanceof List) {
				List eList = (List) error;
				for (int i = 0; i < eList.size(); i++) {
					ErrorType eType = (ErrorType) eList.get(i);
					maxStatus = Math.max(maxStatus, eType.getSeverity());
					if (maxStatus == ERROR_SEVERE)
						return ERROR_SEVERE;	// This is the max, so might as well break out.
				}
			} else {
				ErrorType eType = (ErrorType) error;
				maxStatus = Math.max(maxStatus, eType.getSeverity());
				if (maxStatus == ERROR_SEVERE)
					return ERROR_SEVERE;	// This is the max, so might as well break out.
			}
		}
		return maxStatus;

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IErrorHolder#getErrors()
	 */
	public List getErrors() {
		if (keyedErrors == null || keyedErrors.isEmpty())
			return Collections.EMPTY_LIST;
		
		List result = new ArrayList();
		Iterator iter = keyedErrors.values().iterator();
		while(iter.hasNext()){
			Object v = iter.next();
			if (v instanceof List)
				result.addAll((List) v);
			else
				result.add(v);
		}		
		return result;
	}
	
	/**
	 * Process the error. Add to the list.
	 * @param error
	 * @param key the key to put this under. This is ignored for PropertyErrors. <code>null</code> means use default.
	 * 
	 * @since 1.1.0
	 */
	public void processError(ErrorType error, Object key) {
		if (error instanceof PropertyError) {
			// A property put under key of feature, but if property is not many, only one can be under key.
			PropertyError pe = (PropertyError) error;
			putToKey(pe.getFeature(), error, !pe.getFeature().isMany());
		} else {
			// Not a property error, so add with no key. There can be more than one of these.
			putToKey(key != null ? key : NOT_PROPERTY, error, false);	
		}
		fireAddedError(error);
		fireErrorStatusChanged();
	}
	
	/**
	 * Process the error using default keys.
	 * @param error
	 * 
	 * @since 1.1.0
	 */
	public void processError(ErrorType error) {
		processError(error, null);
	}
	
	/**
	 * Clear a particular key. In default usages this would be either <code>null</code> or a structural feature.
	 * @param key key to clear. <code>null</code> for default key. Normally this would be null of an {@link org.eclipse.emf.ecore.EStructuralFeature}.
	 * 
	 * @since 1.1.0
	 */
	public void clearError(Object key) {
		if (keyedErrors != null) {
			Object v = keyedErrors.remove(key);
			if (v instanceof ErrorType) {
				fireClearedError((ErrorType) v);
				fireErrorStatusChanged();
			} else if (v instanceof List) {
				List errors = (List) v;
				for (int i = 0; i < errors.size(); i++) {
					fireClearedError((ErrorType) errors.get(i));
				}
				fireErrorStatusChanged();
			}
		}
	}
	
	/**
	 * Clear a particular MultiPropertyError. From the given feature, the property error with the given error object.
	 * This should not be called for a non-many feature.
	 * @param key
	 * @param errorObject
	 * 
	 * @since 1.1.0
	 */
	public void clearError(EStructuralFeature sf, Object errorObject) {
		if (keyedErrors != null) {
			Object errors = keyedErrors.get(sf);
			if (errors != null)
				if (errors instanceof List) {
					Iterator itr = ((List) errors).iterator();
					while (itr.hasNext()) {
						PropertyError merr = (PropertyError) itr.next();
						if (errorObject == merr.getErrorObject()) {
							itr.remove();
							fireClearedError(merr);
							fireErrorStatusChanged();
							break;
						}
					}
					if (((List) errors).isEmpty())
						keyedErrors.remove(sf);
				} else if (!(errors instanceof PropertyError) || ((PropertyError) errors).getErrorObject() == errorObject) {
					// Either it switched types (previous is not a property error) or the error is for this object, so clear it.
					clearError(sf);
				}
		}

	}
	/**
	 * Manages keyErrors. Puts out the error under the key. 
	 * @param key
	 * @param error
	 * @param mustBeSingle <code>true</code> if there should never be more than one entry for this value.
	 * 
	 * @since 1.1.0
	 */
	protected void putToKey(Object key, ErrorType error, boolean mustBeSingle) {
		if (keyedErrors == null)
			keyedErrors = new HashMap();
		Object value = keyedErrors.get(key);
		if (value == null || mustBeSingle) {
			keyedErrors.put(key, error);
		} else if (value instanceof List) {
			((List) value).add(error);
		} else {
			List newValue = new ArrayList(2);
			newValue.add(value);
			newValue.add(error);
			keyedErrors.put(key, newValue);
		}
	}
	
	/**
	 * Return whether there are any errors of the given key.
	 * @param key
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public boolean hasErrorsOfKey(Object key) {
		return keyedErrors != null && keyedErrors.containsKey(key);
	}
	
	/**
	 * Return the errors of the given key.
	 * @param key
	 * @return return <code>null</code> if no errors of the given key, <code>ErrorType</code> if only one error, or <code>List</code> if more than one error.
	 * 
	 * @since 1.1.0
	 */
	public Object getErrorsOfKey(Object key) {
		if (keyedErrors == null)
			return null;
		return keyedErrors.get(key);
	}
	
	/**
	 * Return whether there are any error of the given key and object data.
	 * If the feature is a isMany, then it will look for a MultiPropertyError
	 * with the given object data. If the feature is not an isMany, then it will
	 * check only that there it has an error.
	 * 
	 * @param key
	 * @param objectData
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public boolean hasErrorsOfKeyObject(EStructuralFeature feature, Object objectData) {
		if (keyedErrors != null) {
			Object errors = keyedErrors.get(feature);
			if (errors != null)
				if (errors instanceof List) {
					List l = (List) errors;
					int len = l.size();
					for (int i = 0; i < len; i++) {
						if (isErrorOfObject((ErrorType) l.get(i), objectData))
							return true;
					}
				} else if (feature.isMany())
					return isErrorOfObject((ErrorType) errors, objectData);
				else return true;	// Not an is many, and has an error.
		}
		return false;
	}
	
	private boolean isErrorOfObject(ErrorType error, Object objectData) {
		try {
			return ((PropertyError) error).getErrorObject() == objectData;
		} catch (ClassCastException e) {
			return false;
		}
	}
	
	/**
	 * Clear all errors.
	 * 
	 * 
	 * @since 1.1.0
	 */
	public void clearAllErrors() {
		if (keyedErrors != null && !keyedErrors.isEmpty()) {
			List errors = getErrors();
			keyedErrors = null;
			for (Iterator iter = errors.iterator(); iter.hasNext();) {
				ErrorType error = (ErrorType) iter.next();
				fireClearedError(error);
			}
			fireErrorStatusChanged();
		}
	}


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

}
