/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: NonBoundsBeanPropertySource.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:10 $ 
 */
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.propertysheet.*;
import org.eclipse.ve.internal.propertysheet.command.*;

/**
 * @author pwalker
 *
 * A IPropertySource used to wrapper the actual IPropertySource adapter in order to 
 * exclude the "bounds", "size", and "location" properties in order to hide them on the property sheet.
 */
public class NonBoundsBeanPropertySource implements IPropertySource {
	protected IPropertySource fSourceAdapter;

	public NonBoundsBeanPropertySource(Notifier target) {
		super();
		fSourceAdapter = (IPropertySource) EcoreUtil.getRegisteredAdapter((EObject) target,IPropertySource.class);
	}
	/*
	 * Need to merge in the properties of the component.
	 * We will have to wrapper any ISourced or ICommand descriptors because
	 * those will have the wrong source passed into them. We need to intercept
	 * and have the correct source (the component) passed into them.
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] theirs = fSourceAdapter.getPropertyDescriptors();
		IPropertyDescriptor[] wrappedTheirs = new IPropertyDescriptor[theirs.length];
		int wi = 0;
		for (int i = 0; i < theirs.length; i++) {
			IPropertyDescriptor pd = theirs[i];
			if (pd.getId() instanceof EStructuralFeature) {
				// exclude bounds/size/location
				String fn = ((EStructuralFeature) pd.getId()).getName();
				if ("bounds".equals(fn) || "size".equals(fn) || "location".equals(fn)) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					continue;
			}
			if (pd instanceof ISourcedPropertyDescriptor || pd instanceof ICommandPropertyDescriptor)
				wrappedTheirs[wi++] = new WrapperedPropertyDescriptor(this, pd);
			else
				wrappedTheirs[wi++] = pd;
		}

		// Finally build the complete list.
		IPropertyDescriptor[] finalList = new IPropertyDescriptor[wi];
		System.arraycopy(wrappedTheirs, 0, finalList, 0, wi);
		return finalList;
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
	 */
	public Object getEditableValue() {
		return fSourceAdapter.getEditableValue();
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(Object)
	 * Forward to the source adapter
	 */
	public Object getPropertyValue(Object id) {
		return fSourceAdapter.getPropertyValue(id);
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertySource#isPropertySet(Object)
	 * Forward to the source adapter
	 */
	public boolean isPropertySet(Object id) {
		return fSourceAdapter.isPropertySet(id);
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(Object)
	 * Forward to the source adapter
	 */
	public void resetPropertyValue(Object id) {
		fSourceAdapter.resetPropertyValue(id);
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(Object, Object)
	 * Forward to the source adapter
	 */
	public void setPropertyValue(Object id, Object value) {
		fSourceAdapter.setPropertyValue(id, value);
	}

}
