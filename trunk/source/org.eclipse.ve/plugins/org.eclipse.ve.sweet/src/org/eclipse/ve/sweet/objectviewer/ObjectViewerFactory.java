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

import org.eclipse.ve.sweet.objectviewer.pojo.JavaObjectViewerFactory;


/**
 * ObjectViewerFactory.  Constructs ObjectViewers for an application.  Enables
 * various IObjectViewer implementations to be painlessly swapped in an
 * application.
 *
 * @author djo
 */
public class ObjectViewerFactory {
    /**
     * The actual IObjectViewerFactory instance that will be used to create
     * new IObjectViewer objects.
     */
    private static IObjectViewerFactory factory = null;
    
    /*(non-API)
     * Method getDefault.  Return the default IObjectViewerFactory.  If none
     * has been explicitly specified, defaults to JavaObjectViewerFactory 
     * for editing POJOs.
     *
     * @return IObjectViewerFactory the default IObjectViewerFactory
     */
    private static IObjectViewerFactory getDefault() {
    	if (factory == null) {
    		factory = new JavaObjectViewerFactory();
    	}
    	return factory;
    }
    
    /**
     * Method setDefault.  Sets the default IObjectViewerFactory.
     *
     * @param factory The IObjectViewerFactory to set.
     */
    public static void setDefault(IObjectViewerFactory factory) {
    	ObjectViewerFactory.factory = factory;
    }
    
    /**
     * Construct a new IObjectViewer and set its initial input object.
     * 
     * @param input The initial input object
     * @return The constructed IObjectViewer
     */
    public static IObjectViewer edit(Object input) {
        IObjectViewer result = getDefault().construct();
        result.setInput(input);
        return result;
    }
    
    /**
     * Construct a new IObjectViewer object.
     * 
     * @return The IObjectViewer that was constructed.
     */
    public static IObjectViewer construct() {
        return getDefault().construct();
    }
}
