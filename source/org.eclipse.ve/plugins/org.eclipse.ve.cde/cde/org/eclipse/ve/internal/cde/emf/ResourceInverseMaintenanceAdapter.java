/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * Created on Jul 30, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.eclipse.ve.internal.cde.emf;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Inverse Maintenance adapter to add to a Resource so that
 * any EObjects added to it will get an InverseMaintenanceAdapter
 * added to it, and the adapter will be propagated.
 */
public class ResourceInverseMaintenanceAdapter extends AdapterImpl {
	public static final Class ADAPTER_KEY = ResourceInverseMaintenanceAdapter.class;
	
	protected boolean allowCrossDoc;

	public ResourceInverseMaintenanceAdapter() {
		this(false);
	}

	public ResourceInverseMaintenanceAdapter(boolean allowCrossDoc) {
		this.allowCrossDoc = allowCrossDoc;
	}
	
	/**
	 * Create the InverseMaintenanceAdapter. Subclasses can override to
	 * create a different kind.
	 */
	protected InverseMaintenanceAdapter createAdapter() {
		return new InverseMaintenanceAdapter(allowCrossDoc);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Adapter#isAdapterForType(java.lang.Object)
	 */
	public boolean isAdapterForType(Object type) {
		return ADAPTER_KEY == type;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Adapter#notifyChanged(org.eclipse.emf.common.notify.Notification)
	 */
	public void notifyChanged(Notification msg) {
		if (msg.getFeatureID(null) != Resource.RESOURCE__CONTENTS)
			return;
		switch (msg.getEventType()) {
			case Notification.SET:			
			case Notification.ADD:
				addAdapter((Notifier) msg.getNewValue());
				break;
			case Notification.ADD_MANY:
				List added = (List) msg.getNewValue();
				for (int i = 0; i < added.size(); i++) {
					addAdapter((Notifier) added.get(i));
				}
				break;
		}
	}
	
	protected final void addAdapter(Notifier newValue) {
		InverseMaintenanceAdapter inverseAdapter = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(newValue, InverseMaintenanceAdapter.ADAPTER_KEY);
		if (inverseAdapter == null) {
			inverseAdapter = createAdapter();
			newValue.eAdapters().add(inverseAdapter);			
		}
		inverseAdapter.propagate();
	}

	/**
	 * To propagate to all of the contents. This is used
	 * when already have a filled in Resource, such as one
	 * loaded from a file. After attaching to the resource,
	 * call propagate.
	 * 
	 * Not needed to call this if starting with an empty resource and
	 * then loading it up. In that case the adapters will propagate automatically.
	 */
	public void propagate() {
		Iterator itr = ((Resource) getTarget()).getContents().iterator();
		while (itr.hasNext()) {
			EObject content = (EObject) itr.next();
			addAdapter(content);
		} 
	}
}
