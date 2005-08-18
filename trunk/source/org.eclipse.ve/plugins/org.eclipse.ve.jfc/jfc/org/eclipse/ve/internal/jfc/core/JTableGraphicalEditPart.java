/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
 * $RCSfile: JTableGraphicalEditPart.java,v $ $Revision: 1.18 $ $Date: 2005-08-18 21:54:37 $
 */

import java.util.Collections;
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
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * JTable graphical edit part.
 * 
 * @since 1.1.0
 */
public class JTableGraphicalEditPart extends ComponentGraphicalEditPart {

	private EStructuralFeature sfColumns;

	private boolean isOnScrollPane = false;

	/**
	 * JTableGraphicalEditPart constructor
	 * @param aModel
	 * 
	 * @since 1.1.0
	 */
	public JTableGraphicalEditPart(Object aModel) {
		super(aModel);
	}

	private JTableImageListener fImageListener;

	protected JTableImageListener getJTableImageListener() {
		if (fImageListener == null)
			fImageListener = new JTableImageListener();
		return fImageListener;
	}

	/*
	 * This is for listening for updates to the image of the table coming back from the remote vm. This is important for listening for changes to the
	 * preferred size of table columns. if the image has changed, probably the columns have changed.
	 * 
	 * @since 1.0.0
	 * 
	 */
	protected class JTableImageListener implements IImageListener {

		public void imageChanged(ImageData imageData) {
			if (isOnScrollPane)
				refreshColumns();
		}
	}

	/*
	 * This is for listening to our own figure changing size. This would cause the columns to be resize.
	 */
	protected FigureListener hostFigureListener = new FigureListener() {

		public void figureMoved(IFigure source) {
			// Actually called if moved or resized, we only care about resize, but we don't know which it is.
			if (isOnScrollPane)
				refreshColumns();
		}
	};

	/*
	 * Listen for columns being added/removed, and if so refresh the children.
	 */
	private Adapter jTableAdapter = new EditPartAdapterRunnable(this) {

		protected void doRun() {
			refreshChildren();
		}

		public void notifyChanged(Notification notification) {
			if (notification.getFeature() == sfColumns && !notification.isTouch()) {
				queueExec(JTableGraphicalEditPart.this, "COLUMNS"); //$NON-NLS-1$
			}
		}
	};

	protected void refreshChildren() {
		isOnScrollPane = getParent() instanceof JScrollPaneGraphicalEditPart;	// The test needs to be here because when coming up we are refreshed BEFORE we are activated, so can't put setting within activate.	
		super.refreshChildren();
		if (isOnScrollPane)
			refreshColumns();	// Also refresh the columns to get the latest.
	}

	public void activate() {
		super.activate();
		((EObject) getModel()).eAdapters().add(jTableAdapter);
		getFigure().addFigureListener(hostFigureListener);
		getVisualComponent().addImageListener(getJTableImageListener());
	}

	public void deactivate() {
		super.deactivate();
		((EObject) getModel()).eAdapters().remove(jTableAdapter);
		getFigure().removeFigureListener(hostFigureListener);
		getVisualComponent().removeImageListener(getJTableImageListener());
	}

	protected void createEditPolicies() {
		EditDomain domain = EditDomain.getEditDomain(this);
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new org.eclipse.ve.internal.cde.core.FlowLayoutEditPolicy(new JTableContainerPolicy(domain), Boolean.TRUE));
		super.createEditPolicies();
	}

	protected IFigure createFigure() {
		ContentPaneFigure cfig = (ContentPaneFigure) super.createFigure();
		cfig.getContentPane().setLayoutManager(new XYLayout());
		return cfig;
	}

	/*
	 * Refresh the columns. It is a runnable so that it can be queued off on to the
	 * UI thread at the end of the transaction.
	 */
	private Runnable fRefreshColumnsRunnable = new EditPartRunnable(this) {

		protected void doRun() {
			if (getComponentProxy().isBeanProxyInstantiated()) {
				IArrayBeanProxy columnRectsProxy = BeanAwtUtilities.invoke_JTable_getAllColumnRects(getComponentProxy().getBeanProxy());
				if (columnRectsProxy != null) {
					try {
						IBeanProxy[] columnRects = columnRectsProxy.getSnapshot();
						List children = getChildren();
						// We can't just use index here because the index here of columnEP's may be different than the live object.
						// So we need to search for the column in the returns.
						for (int i = 0; i < children.size(); i++) {
							TableColumnGraphicalEditPart columnEP = (TableColumnGraphicalEditPart) children.get(i);
							TableColumnProxyAdapter columnProxyHost = (TableColumnProxyAdapter) BeanProxyUtilities.getBeanProxyHost((IJavaInstance) columnEP.getModel());
							if (!columnProxyHost.isBeanProxyInstantiated())
								continue;
							
							// Find this column in the returned columns.
							IBeanProxy cpProxy = columnProxyHost.getBeanProxy();
							for (int cpi = 0; cpi < columnRects.length; cpi+=5) {
								if (columnRects[cpi].sameAs(cpProxy)) {
									// We found it. Now compute the bounds for it.
									int ii = cpi;
									Rectangle bounds = new Rectangle();
									bounds.x = ((IIntegerBeanProxy) columnRects[++ii]).intValue();
									bounds.y = ((IIntegerBeanProxy) columnRects[++ii]).intValue();
									bounds.width = ((IIntegerBeanProxy) columnRects[++ii]).intValue();
									bounds.height = ((IIntegerBeanProxy) columnRects[++ii]).intValue();
									setLayoutConstraint(columnEP, columnEP.getFigure(), bounds);
									break;
								}
							}
						}						
					} catch (ThrowableProxy e) {
						JavaVEPlugin.log(e, Level.WARNING);
					}
				}
			}
		}
	};

	private void refreshColumns() {
		// Check to see if there's no columns on the table.
		if (!isOnScrollPane || getChildren().size() == 0)
			return;
		// Farm this off to the display thread, if we're not already on it.
		CDEUtilities.displayExec(JTableGraphicalEditPart.this, "REFRESH_COLUMNS", fRefreshColumnsRunnable); //$NON-NLS-1$
	}

	protected void refreshVisuals() {
		super.refreshVisuals();
		refreshColumns();
	}

	protected List getModelChildren() {
		if (isOnScrollPane) {
			return (List) ((EObject) getModel()).eGet(sfColumns);
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	protected EditPart createChild(Object child) {
		if (isOnScrollPane) {
			// TableColumnGraphicalEditPart is not allowed except on a JTable on a JScrollpane.
			// Therefore instead of it being defined in the .override for the JTable it is instantiated
			// directly by the JTableGraphicalEditPart
			return new TableColumnGraphicalEditPart(child);
		} else {
			return null;
		}
	}

	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);
		sfColumns = JavaInstantiation.getSFeature(((EObject) model).eClass().eResource().getResourceSet(), JFCConstants.SF_JTABLE_COLUMNS);
	}
}
