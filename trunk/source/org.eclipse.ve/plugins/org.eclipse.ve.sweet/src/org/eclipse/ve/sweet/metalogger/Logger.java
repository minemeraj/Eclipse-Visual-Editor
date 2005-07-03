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
 * Logger.  Provides access to the current logging singleton.
 * 
 * @author djo
 */
public class Logger {
    
    public static ILogger logger = new StdLogger();

    /**
     * Return the current ILogger being used by the application.
     * 
     * @return The current ILogger
     */
    public static ILogger log() {
        return logger;
    }

}
