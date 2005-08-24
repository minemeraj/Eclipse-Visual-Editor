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
package org.eclipse.ve.examples.cdm.dept.dinner.ui;
/*
 *  $RCSfile: DinnerContentsGraphicalEditPart.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:16:43 $ 
 */

import java.io.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.*;

import org.eclipse.ve.internal.cdm.Diagram;
import org.eclipse.ve.internal.cdm.DiagramFigure;
import org.eclipse.ve.examples.cdm.dept.Company;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
/**
 * Christmas Dinner Edit Part for the Graph Viewer.
 */
public class DinnerContentsGraphicalEditPart extends DiagramContentsGraphicalEditPart {
		
	public DinnerContentsGraphicalEditPart(Diagram model) {
		setModel(model);
	}
	
	protected void createEditPolicies() {
		DiagramFigureXYLayoutEditPolicy ep = new DiagramFigureXYLayoutEditPolicy(new DinnerContentsContainerPolicy(EditDomain.getEditDomain(this)));
		ep.setZoomable(true);
		installEditPolicy(org.eclipse.gef.EditPolicy.LAYOUT_ROLE, ep);
	}	
	
	protected Adapter modelAdapter = new AdapterImpl() {
		/**
		 * @see org.eclipse.emf.common.notify.Adapter#notifyChanged(Notification)
		 */
		public void notifyChanged(Notification notification) {
			Notification kvMsg = KeyedValueNotificationHelper.notifyChanged(notification, DinnerConstants.COMPANY_URL);
			if (kvMsg != null) {
				switch(kvMsg.getEventType()) {
					case Notification.SET:
						refreshCompany(((BasicEMap.Entry) kvMsg.getNewValue()).getValue());
						break;
					case Notification.UNSET:
						refreshCompany(null);
						break;
				}
				refreshChildren();
			}
		}
	};
	public void activate() {
		super.activate();
		// Also add a listener in case it changes.
		((Diagram) getModel()).eAdapters().add(modelAdapter);
	}
	
	public void deactivate() {
		((Diagram) getModel()).eAdapters().remove(modelAdapter);		
		super.deactivate();
	}		

	protected void refreshVisuals() {
		super.refreshVisuals();
		// We will now get the model and store in the viewer data.
		// This is only an example. There would be better places for this in a full-fledged
		// implementation.
		Object kv = ((Diagram) getModel()).getKeyedValues().get(DinnerConstants.COMPANY_URL);
		refreshCompany(kv);		
	}

	protected void refreshCompany(Object kv) {
		Company company = null;
		if (kv instanceof String) {
			Resource dRes = ((Diagram) getModel()).eResource();			
			
			URIConverter converter = dRes.getResourceSet().getURIConverter();
			try {
				String uriString = (String) kv;
				URI uri = null;
				if (!uriString.startsWith("/")) {
					// Local to the dinner file.
					URI resURI = dRes.getURI().trimFileExtension().trimSegments(1);
					uri = URI.createURI(resURI.toString() + '/' + uriString);
				} else
					uri = URI.createPlatformResourceURI(uriString);
				InputStream is = converter.createInputStream(uri);
				if (is != null) {
					ObjectInputStream ois = new ObjectInputStream(is);
					company = (Company) ois.readObject();
					ois.close();
				}
			} catch (IOException e) {
			} catch (ClassNotFoundException e) {
			}
		}
		EditPartViewer v = getRoot().getViewer();
		EditDomain dom = (EditDomain) v.getEditDomain();
		dom.setData(getModel(), company);
	}
				
	/**
	 * Create the child edit part. It will be a DiagramFigure in our case. Also in
	 * our specific case the children will represent entree's (Chicken, etc.).
	 */
	protected EditPart createChild(Object child) {
		DiagramFigure childModel = (DiagramFigure) child;
		return new EntreeGraphicalEditPart(childModel);
	}

	public Object getAdapter(Class adapter) {
		if (adapter == IPropertySource.class)
			return new MealPropertySource((Diagram) getModel());
		else
			return super.getAdapter(adapter);
	}

}
