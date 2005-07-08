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


/**
 * DelegatingHintHandler.
 * 
 * Implements an IHintHandler that delegates to a specified delegate, or to
 * the default HintHandler if no delegate has been specified.
 *
 * @author djo
 */
public class DelegatingHintHandler implements IHintHandler {
    public IHintHandler delegate = null;

    public void setMessage(String message) {
        if (delegate != null) {
            delegate.setMessage(message);
            return;
        }
        HintHandler.getDefault().setMessage(message);
    }

    public void clearMessage() {
        if (delegate != null) {
            delegate.clearMessage();
            return;
        }
        HintHandler.getDefault().clearMessage();
    }
    
}