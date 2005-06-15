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
package org.eclipse.ve.internal.cde.core;

/*
 *  $RCSfile: IErrorHolder.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-15 20:19:34 $ 
 */
 
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
 
/**
 * This interface is for error holder. It returns severities and errors.
 * @author richkulp
 */
public interface IErrorHolder {
		
	public final static int ERROR_SEVERE = 3;
	public final static int ERROR_WARNING = 2;	
	public final static int ERROR_INFO = 1;
	public final static int ERROR_NONE = 0;
	
	/**
	 * The type to use to ask for the adapter of type IErrorHolder, in those case where it is being used.
	 */
	public final static Class ERROR_HOLDER_TYPE = IErrorHolder.class;	
	
	/**
	 * Return a severity of NONE, SEVERE or WARNING to indicate whether the object
	 * is in error.  
	 */
	public int getErrorStatus();
	/**
	 * Return a list of errors. They will be of type Error.
	 */
	public List getErrors();
	
	/**
	 * The error type class returned from {@link IErrorHolder#getErrors()}.
	 * 
	 * @since 1.1.0
	 */
	public static class ErrorType {
	
		private int severity;
		protected static Image INFORMATION_IMAGE;	// 16x16 information image
		protected static Image INFORMATION_OVERLAY_IMAGE;	// small information image designed for overlaying on a 16x16 JavaBean image in the tree
		protected static Image WARNING_IMAGE;	// 16x16 warning image
		protected static Image WARNING_OVERLAY_IMAGE;	// small warning image designed for overlaying on a 16x16 JavaBean image in the tree
		protected static Image SEVERE_ERROR_IMAGE;		// 16x16 error image
		protected static Image SEVERE_ERROR_OVERLAY_IMAGE; // small error image designed for overlaying on a 16x16 JavaBean image in the tree

		private ErrorType cause;	// For when error type is wrappering another error type.
		
		
		public ErrorType(int severity) {
			this.severity = severity;
		}
		
		public ErrorType(int severity, ErrorType cause) {
			this(severity);
			this.cause = cause;
		}
		
		/**
		 * Get the cause ErrorType if wrappering another error type.
		 * @return the cause if wrappering another error type, or <code>null</code> if not wrappering one.
		 * 
		 * @since 1.1.0
		 */
		public ErrorType getCause() {
			return cause;
		}

		/**
		 * Get the error message
		 * @return
		 * 
		 * @since 1.1.0
		 */
		public String getMessage() {
			if (cause != null)
				return cause.getMessage();
			else
				return ""; //$NON-NLS-1$
		}

		
		/**
		 * Get the severity
		 * @return
		 * 
		 * @since 1.1.0
		 */
		public int getSeverity() {
			return severity;
		}

		/**
		 * Get the image for the severity. Do not dispose of the image. It is
		 * owned by the Error.
		 * <p>
		 * Note: This must be called from the UI thread.
		 * @return
		 * 
		 * @since 1.1.0
		 */
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

		/**
		 * Get Severe error image. Do not dispose of the image. It is
		 * owned by the Error.
		 * <p>
		 * Note: This must be called from the UI thread.
		 * 
		 * @return
		 * 
		 * @since 1.1.0
		 */
		public static Image getSevereErrorImage() {
			if (SEVERE_ERROR_IMAGE == null) {
				SEVERE_ERROR_IMAGE = CDEPlugin.getImageFromPlugin(CDEPlugin.getPlugin(), "icons/full/cview16/error_obj.gif"); //$NON-NLS-1$
			}
			return SEVERE_ERROR_IMAGE;
		}
		
		/**
		 * Get Warning error image. Do not dispose of the image. It is
		 * owned by the Error.
		 * <p>
		 * Note: This must be called from the UI thread.
		 * 
		 * @return
		 * 
		 * @since 1.1.0
		 */
		public static Image getWarningErrorImage() {
			if (WARNING_IMAGE == null) {
				WARNING_IMAGE = CDEPlugin.getImageFromPlugin(CDEPlugin.getPlugin(), "icons/full/cview16/warning_obj.gif"); //$NON-NLS-1$
			}
			return WARNING_IMAGE;
		}
		
		/**
		 * Get Severe error image overlay. Do not dispose of the image. It is
		 * owned by the Error.
		 * <p>
		 * Note: This must be called from the UI thread.
		 * 
		 * @return
		 * 
		 * @since 1.1.0
		 */
		public static Image getSevereErrorImageOverlay() {
			if (SEVERE_ERROR_OVERLAY_IMAGE == null) {
					SEVERE_ERROR_OVERLAY_IMAGE = CDEPlugin.getImageFromPlugin(CDEPlugin.getPlugin(), "icons/full/cview16/error_overlay.gif"); //$NON-NLS-1$	
			}
			return SEVERE_ERROR_OVERLAY_IMAGE;
		}
		
		/**
		 * Get Warning image overlay. Do not dispose of the image. It is
		 * owned by the Error.
		 * <p>
		 * Note: This must be called from the UI thread.
		 * 
		 * @return
		 * 
		 * @since 1.1.0
		 */
		public static Image getWarningErrorImageOverlay() {
			if (WARNING_OVERLAY_IMAGE == null) {
				WARNING_OVERLAY_IMAGE = CDEPlugin.getImageFromPlugin(CDEPlugin.getPlugin(), "icons/full/cview16/warning_overlay.gif"); //$NON-NLS-1$
			}
			return WARNING_OVERLAY_IMAGE;
		}
		
		/**
		 * Get Image image overlay. Do not dispose of the image. It is
		 * owned by the Error.
		 * <p>
		 * Note: This must be called from the UI thread.
		 * 
		 * @return
		 * 
		 * @since 1.1.0
		 */
		public static Image getInformationErrorImageOverlay() {
			
			if (INFORMATION_OVERLAY_IMAGE == null) {
				INFORMATION_OVERLAY_IMAGE = CDEPlugin.getImageFromPlugin(CDEPlugin.getPlugin(), "icons/full/cview16/info_overlay.gif"); //$NON-NLS-1$				
			}
			return INFORMATION_OVERLAY_IMAGE;
		}
		
		/**
		 * Get Information image. Do not dispose of the image. It is
		 * owned by the Error.
		 * <p>
		 * Note: This must be called from the UI thread.
		 * 
		 * @return
		 * 
		 * @since 1.1.0
		 */
		public static Image getInformationErrorImage() {
			if (INFORMATION_IMAGE == null) {
				Display display = Display.getCurrent();
				ImageData id = display.getSystemImage(SWT.ICON_INFORMATION).getImageData();
				id = id.scaledTo(16,16);
//				INFORMATION_IMAGE = CDEPlugin.getImageFromPlugin(CDEPlugin.getPlugin(), "icons/full/cview16/info_obj.gif"); //$NON-NLS-1$
				INFORMATION_IMAGE = new Image(display, id);
			}
			return INFORMATION_IMAGE;
		}
	}
	
	/**
	 * ErrorType is a message only.
	 * 
	 * @since 1.1.0
	 */
	public static class MessageError extends ErrorType {
		
		private final String message;

		/**
		 * Construct with message and severity.
		 * @param severity
		 * @param message
		 * 
		 * @since 1.1.0
		 */
		public MessageError(String message, int severity) {
			super(severity);
			this.message = message;
		}
		
		
		/* (non-Javadoc)
		 * @see org.eclipse.ve.internal.java.core.IErrorHolder.ErrorType#getMessage()
		 */
		public String getMessage() {
			return message;
		}
	}

	/**
	 * This is the error type where the error is an exception.
	 * 
	 * @since 1.1.0
	 */
	public static class ExceptionError extends ErrorType {
					
		private Throwable error;

		/**
		 * Construct with exception and severity. Use default message from the exception.
		 * @param error
		 * @param severity
		 * 
		 * @since 1.1.0
		 */
		public ExceptionError(Throwable error, int severity) {
			super(severity);
			this.error = error;
			CDEPlugin.getPlugin().getLogger().log(error, Level.INFO);
		}
		
		/**
		 * Create with default severity of WARNING.
		 * @param error
		 * 
		 * @since 1.1.0
		 */
		public ExceptionError(Throwable error) {
			this(error, ERROR_WARNING);
		}
		

		public String getMessage() {
			return MessageFormat.format(CDEMessages.getString("Exception.msg"), new Object[] { getExceptionClassname(), getExceptionMessage()}); //$NON-NLS-1$);
		}
		
		/**
		 * Return the exception class name. By default this is the actual class name
		 * @return
		 * 
		 * @since 1.1.0
		 */
		protected String getExceptionClassname() {
			return error.getClass().getName();
		}
		
		/**
		 * Return the exception message. By default this is the actual exception message.
		 * @return
		 * 
		 * @since 1.1.0
		 */
		protected String getExceptionMessage() {
			return error.getLocalizedMessage();
		}
		/**
		 * Get the exception.
		 * @return
		 * 
		 * @since 1.1.0
		 */
		public Throwable getException() {
			return error;
		}
	}
	

	/**
	 * This is a specific error for when a property is applied
	 * and the target object throws an Throwable. It can contain
	 * the object in error and index of object if that is important.
	 * 
	 * @since 1.1.0
	 */
	public static class PropertyError extends ErrorType {
		protected EStructuralFeature fStructuralFeature;
		protected Object fObjectInError;
		
		/**
		 * Return the property message format. In the message:
		 * <dl>
		 * <dt>{0}</dt>
		 * <dd>property name</dd>
		 * <dt>{1}</dt>
		 * <dd>Message from error.</dd>
		 * </dl>
		 * @return
		 * 
		 * @since 1.1.0
		 */
		protected static String getPropertyMessageFormat() {
			return CDEMessages.getString("PropertyError.msg"); //$NON-NLS-1$
		}
				
		/**
		 * Return the property name. This may be overridden to return something else than the standard feature name.
		 * @return
		 * 
		 * @since 1.1.0
		 */
		protected String getPropertyName() {
			return fStructuralFeature.getName();
		}
		
		/**
		 * Create a property error for an exception.
		 * @param error
		 * @param severity
		 * @param sf
		 * 
		 * @since 1.1.0
		 */
		public PropertyError(Throwable error, int severity, EStructuralFeature sf){	
			this(severity, new ExceptionError(error, severity), sf, null);
		}
		
		/**
		 * Create a property error for an exception.
		 * @param severity
		 * @param error
		 * @param sf
		 * @param objectInError
		 * 
		 * @since 1.1.0
		 */
		public PropertyError(int severity, Throwable error, EStructuralFeature sf, Object objectInError) {
			this(severity, new ExceptionError(error, severity), sf, objectInError);
		}

		/**
		 * Create a property error.
		 * @param severity
		 * @param error
		 * @param sf
		 * @param objectInError
		 * 
		 * @since 1.1.0
		 */
		public PropertyError(int severity, ErrorType error, EStructuralFeature sf, Object objectInError) {
			super(severity, error);
			fStructuralFeature = sf;
			fObjectInError = objectInError;
		}
		
		/**
		 * Get the feature in error.
		 * @return
		 * 
		 * @since 1.1.0
		 */
		public EStructuralFeature getFeature() {
			return fStructuralFeature;
		}
		
		
		/* (non-Javadoc)
		 * @see org.eclipse.ve.internal.cde.core.IErrorHolder.ExceptionError#getMessage()
		 */
		public String getMessage() {
			return MessageFormat.format(getPropertyMessageFormat(), new Object[] {getPropertyName(), getCause().getMessage()});
		}
		
		/**
		 * Return the object in error.
		 * @return the error object or <code>null</code> if no object in error.
		 * 
		 * @since 1.1.0
		 */
		public Object getErrorObject() {
			return fObjectInError;
		}

	}
	

				

}
