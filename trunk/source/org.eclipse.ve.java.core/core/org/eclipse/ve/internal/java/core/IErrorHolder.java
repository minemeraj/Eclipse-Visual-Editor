/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.core;

/*
 *  $RCSfile: IErrorHolder.java,v $
 *  $Revision: 1.4 $  $Date: 2005-02-15 23:23:54 $ 
 */
 
import java.text.MessageFormat;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.beaninfo.core.Utilities;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jem.internal.proxy.core.ThrowableProxy;
 
/**
 * This interface is for error holder. It returns severities and errors.
 * @author richkulp
 */
public interface IErrorHolder {
		
	public final int ERROR_SEVERE = 3;
	public final int ERROR_WARNING = 2;	
	public final int ERROR_INFO = 1;
	public final int ERROR_NONE = 0;
	
	public final Class ERROR_HOLDER_TYPE = IErrorHolder.class;	// The type to use to ask for the adapter of type IErrorHolder, in those case where it is being used.	
	
	/**
	 * Return a severity of NONE, SEVERE or WARNING to indicate whether the object
	 * is in error.  
	 */
	public int getErrorStatus();
	/**
	 * Return a list of errors. They will be of type Error.
	 */
	public List getErrors();
	
	public static class ErrorType {
	
		protected String message;
		protected int severity;
		protected static Image fInformationErrorImage;	// 16x16 information image
		protected static Image fInformationErrorImageOverlay;	// small information image designed for overlaying on a 16x16 JavaBean image in the tree
		protected static Image fWarningErrorImage;	// 16x16 warning image
		protected static Image fWarningErrorImageOverlay;	// small warning image designed for overlaying on a 16x16 JavaBean image in the tree
		protected static Image fSevereErrorImage;		// 16x16 error image
		protected static Image fSevereErrorImageOverlay; // small error image designed for overlaying on a 16x16 JavaBean image in the tree

		public ErrorType(String message, int severity) {
			this.message = message;
			this.severity = severity;
		}

		public String getMessage() {
			return message;
		}
		
		public int getSeverity() {
			return severity;
		}

		public Image getImage() {
			switch (severity) {
				case ERROR_SEVERE :
					return getSevereErrorImage();
				case ERROR_WARNING :
					return getWarningErrorImage();
				case ERROR_INFO :
					return getInformationErrorImage();
				default :
					return null;
			}
		}

		public static Image getSevereErrorImage() {
			if (fSevereErrorImage == null) {
				fSevereErrorImage = CDEPlugin.getImageFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/cview16/error_obj.gif"); //$NON-NLS-1$
			}
			return fSevereErrorImage;
		}
		public static Image getWarningErrorImage() {
			if (fWarningErrorImage == null) {
				fWarningErrorImage = CDEPlugin.getImageFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/cview16/warning_obj.gif"); //$NON-NLS-1$
			}
			return fWarningErrorImage;
		}
		public static Image getSevereErrorImageOverlay() {
			if (fSevereErrorImageOverlay == null) {
					fSevereErrorImageOverlay = CDEPlugin.getImageFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/cview16/error_overlay.gif"); //$NON-NLS-1$	
			}
			return fSevereErrorImageOverlay;
		}
		public static Image getWarningErrorImageOverlay() {
			if (fWarningErrorImageOverlay == null) {
				fWarningErrorImageOverlay = CDEPlugin.getImageFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/cview16/warning_overlay.gif"); //$NON-NLS-1$
			}
			return fWarningErrorImageOverlay;
		}		
		public static Image getInformationErrorImageOverlay() {
			if (fInformationErrorImageOverlay == null) {
				fInformationErrorImageOverlay = CDEPlugin.getImageFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/cview16/info_overlay.gif"); //$NON-NLS-1$
			}
			return fInformationErrorImageOverlay;
		}		
		public static Image getInformationErrorImage() {
			if (fInformationErrorImage == null) {
				fInformationErrorImage = CDEPlugin.getImageFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/cview16/info_obj.gif"); //$NON-NLS-1$
			}
			return fInformationErrorImage;
		}			
	}

	public static class ExceptionError extends ErrorType {
					
		public Throwable error;

		public ExceptionError(Throwable error, int severity) {
			super(null, severity);
			this.error = error;
		}
		public String getMessage() {
			if (error instanceof ThrowableProxy) {
				return MessageFormat.format(JavaMessages.getString("BeanProxyException.msg"), new Object[] {((ThrowableProxy) error).getTypeProxy().getTypeName(), ((ThrowableProxy) error).getProxyLocalizedMessage()}); //$NON-NLS-1$
			} else {
				return MessageFormat.format(JavaMessages.getString("BeanProxyException.msg"), new Object[] { error.getClass().getName(), error.getLocalizedMessage()}); //$NON-NLS-1$
			}
		}
	}
	

	/*
	 * Extension of PropertyError when the feature is a multi valued feature.
	 */
	public static class MultiPropertyError extends PropertyError {
		protected Object fObjectInError;
		public MultiPropertyError(Object errObject, Throwable error, EStructuralFeature sf) {
			this(errObject, error, ERROR_WARNING, sf);
		}
		
		public MultiPropertyError(Object errObject, Throwable error, int severity, EStructuralFeature sf) {
			super(error, severity, sf);
			fObjectInError = errObject;
		}
		
		
		public Object getErrorObject() {
			return fObjectInError;	
		}
	}


	
	

	/*
	 *  This is a specific error for when a property is applied
	 * and the target object throws an Throwable
	 */
	public static class PropertyError extends ExceptionError {
		protected EStructuralFeature fStructuralFeature;
		
		public PropertyError(Throwable error,EStructuralFeature sf){	
			this(error, ERROR_WARNING, sf);
		}
		
		public PropertyError(Throwable error, int severity, EStructuralFeature sf){	
			super(error, severity);
			fStructuralFeature = sf;
		}
	
		
		public String getMessage(){
			PropertyDecorator propertyDecorator = Utilities.getPropertyDecorator( (EModelElement) fStructuralFeature);
			// 230183 paw - We need to return the display name if it has one for NL purposes
			if (propertyDecorator != null)
					return propertyDecorator.getDisplayName() + " : " + super.getMessage(); //$NON-NLS-1$
			return fStructuralFeature.getName() + " : " + super.getMessage(); //$NON-NLS-1$
		}
		
		public EStructuralFeature getFeature() {
			return fStructuralFeature;
		}
	}
	

				

}
