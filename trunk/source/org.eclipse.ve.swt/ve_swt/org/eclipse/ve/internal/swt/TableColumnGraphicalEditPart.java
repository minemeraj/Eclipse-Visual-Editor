/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: TableColumnGraphicalEditPart.java,v $
 *  $Revision: 1.3 $  $Date: 2005-02-15 23:51:49 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.core.DefaultComponentEditPolicy;
import org.eclipse.ve.internal.cde.core.OutlineBorder;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;

/**
 * org.eclipse.swt.widgets.TableColumn does not inherit from org.eclipse.swt.widgets.Control The edit part should create a figure that has the height
 * of the parent Table the width of getWidth() from the TableColumn and the x based on the width of all the preceeding columns
 * 
 * @since 1.0.0
 */
public class TableColumnGraphicalEditPart extends AbstractGraphicalEditPart {

	private TableColumnProxyAdapter tableColumnProxyAdapter;

	protected Adapter adapter = new EditPartAdapterRunnable() {

		public void notifyChanged(Notification notification) {
			if (notification.getEventType() == Notification.REMOVING_ADAPTER)
				return;
			// Else assume a refresh is needed.
			queueExec(TableColumnGraphicalEditPart.this);
		}

		public void run() {
//			if (isActive())
//				((TableGraphicalEditPart) getParent()).refreshItems();
		}
	};

	public Object getAdapter(Class type) {
		if (type == IPropertySource.class)
				return EcoreUtil.getRegisteredAdapter((IJavaObjectInstance) getModel(), IPropertySource.class);
		Object result = super.getAdapter(type);
		if (result != null) {
			return result;
		} else {
			// See if any of the MOF adapters on our target can return a value
			// for the request
			Iterator mofAdapters = ((IJavaInstance) getModel()).eAdapters().iterator();
			while (mofAdapters.hasNext()) {
				Object mofAdapter = mofAdapters.next();
				if (mofAdapter instanceof IAdaptable) {
					Object mofAdapterAdapter = ((IAdaptable) mofAdapter).getAdapter(type);
					if (mofAdapterAdapter != null) { return mofAdapterAdapter; }
				}
			}
		}
		return null;
	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DefaultComponentEditPolicy());
	}

	protected IFigure createFigure() {
		Figure figure = new Figure();
		figure.setOpaque(false);
		figure.setBackgroundColor(ColorConstants.cyan);
		figure.setBorder(new OutlineBorder());
		return figure;
	}

	/*
	 * Return the proxy adapter associated with this TabFolder.
	 */
	protected TableColumnProxyAdapter getTableColumnProxyAdapter() {
		if (tableColumnProxyAdapter == null) {
			IBeanProxyHost tableColumnProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) getModel());
			tableColumnProxyAdapter = (TableColumnProxyAdapter) tableColumnProxyHost;
		}
		return tableColumnProxyAdapter;
	}

	protected TableColumnProxyAdapter getControlProxy() {
		return getTableColumnProxyAdapter();
	}

	public IJavaInstance getBean() {
		return (IJavaInstance) getModel();
	}

	protected Rectangle bounds = null;
	public Rectangle getBounds() {
		return bounds;
	}
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public void refresh(){
		super.refresh();
		if (bounds != null) {
			getFigure().setBounds(getBounds());
		}
		
	}
	

}