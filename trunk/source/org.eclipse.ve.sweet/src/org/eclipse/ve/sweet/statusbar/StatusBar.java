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
package org.eclipse.ve.sweet.statusbar;

public class StatusBar {
    
    private static class NullStatusBar implements IStatusBar {
        public void setMessage(String message) {
        }
        public void clearMessage() {
        }
    }
    
    private static IStatusBar statusBar = new NullStatusBar();
    
    public static void setStatusBar(IStatusBar statusBar) {
        StatusBar.statusBar = statusBar;
    }

    public static IStatusBar getDefault() {
        return statusBar;
    }

}
