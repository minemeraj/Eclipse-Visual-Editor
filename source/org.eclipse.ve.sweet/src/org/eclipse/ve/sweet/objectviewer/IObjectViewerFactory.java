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
package org.eclipse.ve.sweet.objectviewer;

/**
 * IObjectViewerFactory. An interface for factory objects that can construct
 * IObjectViewer implementations.
 *
 * @author djo
 */
public interface IObjectViewerFactory {

    /**
     * Method construct.
     * 
     * Construct and return an IObjectViewer according to the application's
     * editing policy.
     * 
     * @return The constructed IObjectViewer.
     */
    IObjectViewer construct();

}
