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
package org.eclipse.ve.sweet.hinthandler;

public class HintHandler {
    
    private static class NullStatusBar implements IHintHandler {
        public void setMessage(String message) {
        }
        public void clearMessage() {
        }
    }
    
    private static IHintHandler statusBar = new NullStatusBar();
    
    public static void setHintHandler(IHintHandler statusBar) {
        HintHandler.statusBar = statusBar;
    }

    public static IHintHandler getDefault() {
        return statusBar;
    }

}
