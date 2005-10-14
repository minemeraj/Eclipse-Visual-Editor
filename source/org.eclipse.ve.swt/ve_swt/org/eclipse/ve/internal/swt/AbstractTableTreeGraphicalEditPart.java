/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: AbstractTableTreeGraphicalEditPart.java,v $
 *  $Revision: 1.2 $  $Date: 2005-10-14 22:03:20 $ 
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

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

import org.eclipse.ve.internal.java.core.*;

/**
 * Base class for swt Table or Tree graphical edit part.
 * 
 * @since 1.0.0
 */
public abstract class AbstractTableTreeGraphicalEditPart extends ControlGraphicalEditPart {

	private IBeanProxyHost beanProxyProxyAdapter;

	public AbstractTableTreeGraphicalEditPart(Object aModel) {
		super(aModel);
	}

	protected EStructuralFeature sfColumns;

	private TreeTableImageListener fImageListener;

	protected TreeTableImageListener getTreeTableImageListener() {
		if (fImageListener == null)
			fImageListener = new TreeTableImageListener();
		return fImageListener;
	}

	/*
	 * This is for listening for updates to the image of the table/table coming back from the remote vm. This is important for listening for changes to the
	 * size of table columns.
	 * 
	 * @since 1.0.0
	 * 
	 */
	protected class TreeTableImageListener implements IImageListener {

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
				queueExec(AbstractTableTreeGraphicalEditPart.this, "COLUMNS"); //$NON-NLS-1$
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
		getVisualComponent().addImageListener(getTreeTableImageListener());
	}

	public void deactivate() {
		super.deactivate();
		((EObject) getModel()).eAdapters().remove(tableAdapter);
		getFigure().removeFigureListener(hostFigureListener);
		getVisualComponent().removeImageListener(getTreeTableImageListener());
		beanProxyProxyAdapter = null;
	}

	protected IFigure createFigure() {
		ContentPaneFigure cfig = (ContentPaneFigure) super.createFigure();
		cfig.getContentPane().setLayoutManager(new XYLayout());
		return cfig;
	}

	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new FlowLayoutEditPolicy(getContainerPolicy(), Boolean.TRUE));
	}
	
	protected abstract ContainerPolicy getContainerPolicy();

	protected List getModelChildren() {
		return (List) getBean().eGet(sfColumns);
	}

	/**
	 * TableTreeColumnGraphicalEditPart is not allowed on the free form as it is specially designed for a TableColumn hosted inside a Table. Therefore
	 * instead of it being defined in the .override for the Table it is instantiated directly by the TableTreeGraphicalEditPart
	 */
	protected EditPart createChild(Object child) {
		TableTreeColumnGraphicalEditPart result = new TableTreeColumnGraphicalEditPart();
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
	
	protected abstract IArrayBeanProxy getAllColumnRects(IBeanProxy modelProxy);

	private Runnable fRefreshColumnsRunnable = new EditPartRunnable(this) {

		protected void doRun() {
			if (getBeanProxyAdapter().isBeanProxyInstantiated()) {
				IArrayBeanProxy columnRectsProxy = getAllColumnRects(getBeanProxyAdapter().getBeanProxy());
				if (columnRectsProxy != null) {
					try {
						IBeanProxy[] columnRects = columnRectsProxy.getSnapshot();
						List children = getChildren();
						// We can't just use index here because the index here of columnEP's may be different than the live object.
						// So we need to search for the column in the returns.
						for (int i = 0; i < children.size(); i++) {
							TableTreeColumnGraphicalEditPart columnEP = (TableTreeColumnGraphicalEditPart) children.get(i);
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
						((TableTreeColumnGraphicalEditPart) children.get(i)).getFigure().setVisible(false);
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
		CDEUtilities.displayExec(AbstractTableTreeGraphicalEditPart.this, "REFRESH_COLUMNS", fRefreshColumnsRunnable); //$NON-NLS-1$
	}
}
