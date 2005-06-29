/*
 * Copyright (C) 2005 db4objects Inc.  http://www.db4o.com
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     db4objects - Initial API and implementatiobn
 */
package org.eclipse.ve.sweet;

/**
 * Class CannotSaveException.  The exception that is thrown when a 
 * save/persist operation is requested but it cannot be performed for 
 * any reason.
 *
 * @author djo
 */
public class CannotSaveException extends Exception {
	
    private static final long serialVersionUID = 2811075811672352626L;
    
    /**
     * Constructor CannotSaveException.
     * 
	 * @param message The message.
	 */
	public CannotSaveException(String message) {
		super(message);
	}
	/**
     * Constructor CannotSaveException.
     * 
	 * @param message The message.
	 * @param cause The exception that was the real cause.
	 */
	public CannotSaveException(String message, Throwable cause) {
		super(message, cause);
	}
	/**
     * Constructor CannotSaveException.
     * 
	 * @param cause The exception that was the real cause.
	 */
	public CannotSaveException(Throwable cause) {
		super(cause);
	}
}
