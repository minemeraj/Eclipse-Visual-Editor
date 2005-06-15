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
 *  $RCSfile: TableGraphicalEditPart.java,v $
 *  $Revision: 1.9 $  $Date: 2005-06-15 20:19:21 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.List;
import java.util.logging.Level;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.ImageData;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

/**
 * swt Table Graphical Edit part.
 * 
 * @since 1.0.0
 */
public class TableGraphicalEditPart extends CompositeGraphicalEditPart {

	private IBeanProxyHost beanProxyProxyAdapter;

	public TableGraphicalEditPart(Object aModel) {
		super(aModel);
	}

	private EStructuralFeature sfColumns;

	protected VisualContainerPolicy getContainerPolicy() {
		return new TableContainerPolicy(EditDomain.getEditDomain(this)); // SWT standard Composite/Container Edit Policy
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

	private Adapter tableAdapter = new EditPartAdapterRunnable(this) {

		protected void doRun() {
			refreshChildren();
		}

		public void notifyChanged(Notification notification) {
			if (notification.getFeature() == sfColumns) {
				queueExec(TableGraphicalEditPart.this, "COLUMNS"); //$NON-NLS-1$
			}
		}
	};

	protected void refreshChildren() {
		super.refreshChildren();
		refreshColumns(); // Also refresh the columns to get the latest.
	}

	protected void refreshVisuals() {
		super.refreshVisuals();
		refreshColumns();
	}

	public void activate() {
		super.activate();
		((EObject) getModel()).eAdapters().add(tableAdapter);
		getFigure().addFigureListener(hostFigureListener);
		getVisualComponent().addImageListener(getTableImageListener());
	}

	public void deactivate() {
		super.deactivate();
		((EObject) getModel()).eAdapters().remove(tableAdapter);
		getFigure().removeFigureListener(hostFigureListener);
		getVisualComponent().removeImageListener(getTableImageListener());
		beanProxyProxyAdapter = null;
	}

	protected IFigure createFigure() {
		ContentPaneFigure cfig = (ContentPaneFigure) super.createFigure();
		cfig.getContentPane().setLayoutManager(new XYLayout());
		return cfig;
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

	/*
	 * Return the proxy adapter associated with this TabFolder.
	 */
	protected IBeanProxyHost getBeanProxyAdapter() {
		if (beanProxyProxyAdapter == null) {
			beanProxyProxyAdapter = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) getModel());
		}
		return beanProxyProxyAdapter;
	}

	private Runnable fRefreshColumnsRunnable = new EditPartRunnable(this) {

		protected void doRun() {
			if (getBeanProxyAdapter().isBeanProxyInstantiated()) {
				IArrayBeanProxy columnRectsProxy = BeanSWTUtilities.invoke_Table_getAllColumnRects(getBeanProxyAdapter().getBeanProxy());
				if (columnRectsProxy != null) {
					try {
						IBeanProxy[] columnRects = columnRectsProxy.getSnapshot();
						List children = getChildren();
						// We can't just use index here because the index here of columnEP's may be different than the live object.
						// So we need to search for the column in the returns.
						for (int i = 0; i < children.size(); i++) {
							TableColumnGraphicalEditPart columnEP = (TableColumnGraphicalEditPart) children.get(i);
							IBeanProxyHost columnProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) columnEP.getModel());
							if (!columnProxyHost.isBeanProxyInstantiated()) {
								columnEP.getFigure().setVisible(false);
								continue;
							}

							// Find this column in the returned columns. The actual order may be different than added, so we need to search each time.
							IBeanProxy cpProxy = columnProxyHost.getBeanProxy();
							boolean found = false;
							for (int cpi = 0; cpi < columnRects.length; cpi += 5) {
								if (columnRects[cpi].sameAs(cpProxy)) {
									// We found it. Now compute the bounds for it.
									int ii = cpi;
									Rectangle bounds = new Rectangle();
									bounds.x = ((IIntegerBeanProxy) columnRects[++ii]).intValue();
									bounds.y = ((IIntegerBeanProxy) columnRects[++ii]).intValue();
									bounds.width = ((IIntegerBeanProxy) columnRects[++ii]).intValue();
									bounds.height = ((IIntegerBeanProxy) columnRects[++ii]).intValue();
									setLayoutConstraint(columnEP, columnEP.getFigure(), bounds);
									found = true;
									break;
								}
							}
							columnEP.getFigure().setVisible(found);
						}
					} catch (ThrowableProxy e) {
						JavaVEPlugin.log(e, Level.WARNING);
					}
				} else {
					// Hide all of the columns.
					List children = getChildren();
					// We can't just use index here because the index here of columnEP's may be different than the live object.
					// So we need to search for the column in the returns.
					for (int i = 0; i < children.size(); i++) {
						((TableColumnGraphicalEditPart) children.get(i)).getFigure().setVisible(false);
					}
				}
			}
		}
	};

	private void refreshColumns() {
		// Check to see if there's no columns on the table.
		if (getChildren().size() == 0)
			return;
		// Farm this off to the display thread, if we're not already on it.
		CDEUtilities.displayExec(TableGraphicalEditPart.this, "REFRESH_COLUMNS", fRefreshColumnsRunnable); //$NON-NLS-1$
	}

	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);
		sfColumns = JavaInstantiation.getSFeature(((EObject) model).eClass().eResource().getResourceSet(), SWTConstants.SF_TABLE_COLUMNS);
	}
}