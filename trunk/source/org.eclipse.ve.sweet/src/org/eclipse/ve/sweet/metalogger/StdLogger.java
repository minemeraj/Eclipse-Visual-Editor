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
 * StdLogger. A stdout/stderr metalogger implementation.
 *
 * @author djo
 */
public class StdLogger extends AbstractLogger implements ILogger {

    public void message(String message) {
        System.out.println(message);
    }

    public void error(Throwable t, String message) {
        System.err.println(message);
        t.printStackTrace(System.err);
    }

    public void data(String data) {
        System.out.print(data);
    }

    public void debug(Class subject, String message) {
        if (isDebug(subject)) {
            System.out.println(message);
        }
    }

}
