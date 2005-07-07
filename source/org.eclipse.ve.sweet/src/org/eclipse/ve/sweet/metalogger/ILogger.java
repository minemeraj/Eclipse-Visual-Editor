/*
 * Copyright (C) 2005 db4objects Inc.  http://www.db4o.com
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     db4objects - Initial API and implementation
 */
package org.eclipse.ve.sweet.metalogger;

/**
 * ILogger.  An abstract logging interface.  Concrete implementations are
 * expected to be provided to connect the logger to stdout/stderr, a log
 * file in the file system, Eclipse's logger, log4j, etc.
 *
 * @author djo
 */
public interface ILogger {

    /**
     * Log a message
     * 
     * @param message
     */
    void message(String message);

    /**
     * Log an error
     * 
     * @param t The Throwable associated with this error
     * @param message
     */
    void error(Throwable t, String message);

    /**
     * Send preformatted data to the log file
     * 
     * @param data The data String
     */
    void data(String data);

    /**
     * Log a debug message associated with a particular Class
     * 
     * @param subject The subject, or class, we're debugging
     * @param message The message to log
     */
    void debug(Class subject, String message);

    /**
     * Returns if unfiltered debug logging is enabled
     * 
     * @return
     */
    boolean isDebug();

    /**
     * Turns unfiltered debug logging on or off.  Default is off.
     * 
     * @param debugMode
     */
    void setDebug(boolean debugMode);

    /**
     * Turns debugging on just for the specified class.
     * 
     * @param subject The subject, or class, that we are debugging
     * @param enabled true to log debug messages associated with this class; false otherwise.
     */
    void setDebug(Class subject, boolean enabled);

    /**
     * Indicates if debugging is on for a particular subject class.
     * 
     * @param subject The subject class
     * @return true if we are debugging the specified subject class; false otherwise.
     */
    boolean isDebug(Class subject);

}
