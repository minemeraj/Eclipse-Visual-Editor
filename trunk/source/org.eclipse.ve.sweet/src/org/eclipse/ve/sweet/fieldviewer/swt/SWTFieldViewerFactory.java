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
package org.eclipse.ve.sweet.fieldviewer.swt;

import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.ve.sweet.fieldviewer.IFieldViewer;
import org.eclipse.ve.sweet.fieldviewer.IFieldViewerFactory;
import org.eclipse.ve.sweet.fieldviewer.swt.internal.factories.CheckboxButtonFieldViewerFactory;
import org.eclipse.ve.sweet.fieldviewer.swt.internal.factories.CheckboxListFieldViewerFactory;
import org.eclipse.ve.sweet.fieldviewer.swt.internal.factories.ComboFieldViewerFactory;
import org.eclipse.ve.sweet.fieldviewer.swt.internal.factories.CompositeTableViewerFactory;
import org.eclipse.ve.sweet.fieldviewer.swt.internal.factories.TextFieldViewerFactory;
import org.eclipse.ve.sweet.objectviewer.IObjectViewer;
import org.eclipse.ve.sweet.objectviewer.IPropertyEditor;


/**
 * SWTFieldViewerFactory.  SWT constructor factory for IFieldViewer objects.  New factories
 * must be registered ahead of (overriding) the built-in factory objects.
 * 
 * @author djo
 */
public class SWTFieldViewerFactory implements IFieldViewerFactory {
	
	/**
	 * Construct SWTFieldViewerFactory.  Default constructor.
	 */
	public SWTFieldViewerFactory() {
		// Register the built-in factories
		register(new CompositeTableViewerFactory());
		register(new CheckboxButtonFieldViewerFactory());
		register(new CheckboxListFieldViewerFactory());
		register(new ComboFieldViewerFactory());
		register(new TextFieldViewerFactory());		// The default (unconditional) factory rule
	}
	
	private LinkedList viewerFactories = new LinkedList();
	
	/**
	 * Register an IFieldViewerFactory object for a particular kind of IFieldViewer
	 * ahead of all the other current IFieldViewerFactory objects.
	 * 
	 * @param factoryRule The rule object to register.
	 */
	public void registerFirst(IFieldViewerFactory factoryRule) {
		viewerFactories.addFirst(factoryRule);
	}
	
	/**(non-API)
	 * Register an IFieldViewerFactory object for a particular kind of IFieldViewer
	 * behind all the other current IFieldViewerFactory objects.
	 * 
	 * @param factoryRule The rule object to register.
	 */
	protected void register(IFieldViewerFactory factoryRule) {
		viewerFactories.addLast(factoryRule);
	}
	
	/* (non-Javadoc)
     * @see com.db4o.binding.field.swt.IFieldControllerFactory#construct(java.lang.Object, com.db4o.binding.dataeditors.IObjectViewer, com.db4o.binding.dataeditors.IPropertyEditor)
     */
    public IFieldViewer construct(Object control, IObjectViewer objectViewer, IPropertyEditor propertyEditor) {
        IFieldViewer result = null;
        
        for (Iterator factoryIter = viewerFactories.iterator(); factoryIter.hasNext();) {
			IFieldViewerFactory factory = (IFieldViewerFactory) factoryIter.next();
			result = factory.construct(control, objectViewer, propertyEditor);
			if (result != null) {
				return result;
			}
		}
        
        // If a default factory rule was registered, this should never happen
        return null;
    }
}
