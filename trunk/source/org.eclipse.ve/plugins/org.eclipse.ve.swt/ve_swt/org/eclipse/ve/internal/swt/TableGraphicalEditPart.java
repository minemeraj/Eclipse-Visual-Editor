/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: TableGraphicalEditPart.java,v $
 *  $Revision: 1.3 $  $Date: 2004-11-09 17:48:35 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.List;

import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.ImageData;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.IIntegerBeanProxy;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

/**
 * 
 * @since 1.0.0
 */
public class TableGraphicalEditPart extends CompositeGraphicalEditPart {
	private TableProxyAdapter tableProxyAdapter;

	public TableGraphicalEditPart(Object aModel) {
		super(aModel);
	}

	private EStructuralFeature sfColumns;

	protected VisualContainerPolicy getContainerPolicy() {
		return new TableContainerPolicy(EditDomain.getEditDomain(this)); // SWT standard Composite/Container Edit Policy
	}

	protected Rectangle getBounds() {
		return getFigure().getBounds().getCopy();
	}

	private TableImageListener fImageListener;

	protected TableImageListener getTableImageListener() {
		if (fImageListener == null)
			fImageListener = new TableImageListener();
		return fImageListener;
	}

	/*
	 * This is for listening for updates to the image of the table coming back from the remote vm. This is important for listening for changes to the
	 * size of table columns.
	 * 
	 * @since 1.0.0
	 *  
	 */
	protected class TableImageListener implements IImageListener {

		public void imageChanged(ImageData imageData) {
			refreshColumns();
		}
	}

	protected FigureListener hostFigureListener = new FigureListener() {

		public void figureMoved(IFigure source) {
			refreshColumns();
		}
	};

	private Adapter tableAdapter = new EditPartAdapterRunnable() {

		public void run() {
			if (isActive()) {
				refreshChildren();
				refreshColumns();
			}
		}

		public void notifyChanged(Notification notification) {
			if (notification.getFeature() == sfColumns) {
				queueExec(TableGraphicalEditPart.this);
			}
		}
	};

	public void activate() {
		super.activate();
		((EObject) getModel()).eAdapters().add(tableAdapter);
		getFigure().addFigureListener(hostFigureListener);
		getVisualComponent().addImageListener(getTableImageListener());
		//		getVisualComponent().addComponentListener(getTableComponentListener());
	}

	public void deactivate() {
		super.deactivate();
		((EObject) getModel()).eAdapters().remove(tableAdapter);
		getFigure().removeFigureListener(hostFigureListener);
		getVisualComponent().removeImageListener(getTableImageListener());
		tableProxyAdapter = null;
	}

	protected void createEditPolicies() {
		super.createEditPolicies();
	}

	protected void createLayoutEditPolicy() {
		EditDomain domain = EditDomain.getEditDomain(this);
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new DefaultLayoutEditPolicy(new TableContainerPolicy(domain)));
	}

	protected List getModelChildren() {
		return (List) getBean().eGet(sfColumns);
	}

	/**
	 * TableColumnGraphicalEditPart is not allowed on the free form as it is specially designed for a TableColumn hosted inside a Table. Therefore
	 * instead of it being defined in the .override for the Table it is instantiated directly by the TableGraphicalEditPart
	 */
	protected EditPart createChild(Object child) {
		TableColumnGraphicalEditPart result = new TableColumnGraphicalEditPart();
		result.setModel(child);
		return result;
	}

	protected int getHeaderHeight() {
		IIntegerBeanProxy intBeanProxy = getTableProxyAdapter().getHeaderHeight();
		if (intBeanProxy != null) { return intBeanProxy.intValue(); }
		return 0;
	}

	/*
	 * Return the proxy adapter associated with this TabFolder.
	 */
	protected TableProxyAdapter getTableProxyAdapter() {
		if (tableProxyAdapter == null) {
			IBeanProxyHost tableProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) getModel());
			tableProxyAdapter = (TableProxyAdapter) tableProxyHost;
		}
		return tableProxyAdapter;
	}

	private Runnable fRefreshColumnsRunnable = new Runnable() {

		public void run() {
			int columnWidth = 0;
			int columnHeight = getHeaderHeight();
			Rectangle parentRect = getBounds();
			Point nextColumnLocation = new Point(parentRect.x, parentRect.y);
			List children = getChildren();
			for (int i = 0; i < children.size(); i++) {
				TableColumnGraphicalEditPart columnEP = (TableColumnGraphicalEditPart) children.get(i);
				TableColumnProxyAdapter proxyAdapter = columnEP.getControlProxy();
				if (proxyAdapter != null && proxyAdapter.isBeanProxyInstantiated()) {
					columnWidth = ((IIntegerBeanProxy) columnEP.getControlProxy().getWidth()).intValue();
					columnEP.setBounds(new Rectangle(nextColumnLocation.x, nextColumnLocation.y, columnWidth, columnHeight));
					columnEP.refresh();
					nextColumnLocation.x += columnWidth;
				}
			}
		}
	};

	private void refreshColumns() {
		// Check to see if there's no columns on the table.
		if (getChildren().size() == 0)
			return;
		// Farm this off to the display thread, if we're not already on it.
		CDEUtilities.displayExec(TableGraphicalEditPart.this, fRefreshColumnsRunnable);
	}

	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);
		sfColumns = JavaInstantiation.getSFeature(((EObject) model).eClass().eResource().getResourceSet(), SWTConstants.SF_TABLE_COLUMNS);
	}
}