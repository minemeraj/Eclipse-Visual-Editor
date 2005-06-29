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
 * StdLogger.
 *
 * FIXME:  This is a stub because the old code that was referenced
 * was CPL, so I couldn't include it.
 * 
 * @author djo
 */
public class StdLogger implements ILogger {

    public void message(String message) {
        System.out.println(message);
    }

}
