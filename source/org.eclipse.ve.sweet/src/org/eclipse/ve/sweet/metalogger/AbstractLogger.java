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

import java.util.HashMap;

/**
 * AbstractLogger. A default implementation of the common methods required for
 * an ILogger implementation.
 *
 * @author djo
 */
public abstract class AbstractLogger implements ILogger {
    private boolean debugMode = false;

    public void setDebug(boolean debugMode) {
        this.debugMode = debugMode;
    }
    
    public boolean isDebug() {
        return debugMode;
    }
    
    private HashMap debugClasses = new HashMap();
    
    public void setDebug(Class subject, boolean enabled) {
        if (enabled) {
            debugClasses.put(subject, Boolean.TRUE);
        }
    }
    
    public boolean isDebug(Class subject) {
        Object isEnabled = debugClasses.get(subject);
        return isEnabled != null;
    }
}
