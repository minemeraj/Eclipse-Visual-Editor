/*****************************************************************************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the terms
 * of the Common Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************************************************************************/
/*
 * $RCSfile: ToolItemGraphicalEditPart.java,v $ $Revision: 1.1 $ $Date: 2004-08-22 22:42:51 $
 */
package org.eclipse.ve.internal.swt;

import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.proxy.awt.IRectangleBeanProxy;

import org.eclipse.ve.internal.cde.core.*;

import org.eclipse.ve.internal.java.core.*;

public class ToolItemGraphicalEditPart extends AbstractGraphicalEditPart {

	protected Rectangle bounds = null;

	protected ImageFigureController imageFigureController;

	protected IJavaInstance bean;

	private ToolItemProxyAdapter toolItemProxyAdapter;

	protected ErrorFigure fErrorIndicator;

	protected IBeanProxyHost.ErrorListener fBeanProxyErrorListener;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	public void activate() {
		super.activate();
		// Listen to the IBeanProxyHost so it tells us when errors occur
		fBeanProxyErrorListener = new IErrorNotifier.ErrorListenerAdapter() {

			public void errorStatusChanged() {
				CDEUtilities.displayExec(ToolItemGraphicalEditPart.this, new Runnable() {

					public void run() {
						setSeverity(getControlProxy().getErrorStatus());
					}
				});
			}
		};

		setSeverity(getControlProxy().getErrorStatus()); // Set the initial status
		getControlProxy().addErrorListener(fBeanProxyErrorListener);
		//		IRectangleBeanProxy rectBeanProxy = getToolItemProxyAdapter().getBounds();
		//		if (rectBeanProxy != null) {
		//			bounds = new Rectangle(rectBeanProxy.getX(), rectBeanProxy.getY(), rectBeanProxy.getWidth(), rectBeanProxy.getHeight());
		//			refresh();
		//		}
	}

	/*
	 * Return the proxy adapter associated with this TabFolder.
	 */
	protected ToolItemProxyAdapter getToolItemProxyAdapter() {
		if (toolItemProxyAdapter == null) {
			IBeanProxyHost toolItemProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) getModel());
			toolItemProxyAdapter = (ToolItemProxyAdapter) toolItemProxyHost;
		}
		return toolItemProxyAdapter;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	protected void setSeverity(int severity) {
		fErrorIndicator.sevSeverity(severity);
		getFigure().setVisible(!(severity == IBeanProxyHost.ERROR_SEVERE));
	}

	protected IVisualComponent getVisualComponent() {
		return (IVisualComponent) getControlProxy(); // For AWT, the component proxy is the visual component.
	}

	protected ToolItemProxyAdapter getControlProxy() {
		IBeanProxyHost beanProxy = BeanProxyUtilities.getBeanProxyHost(getBean());
		return (ToolItemProxyAdapter) beanProxy;
	}

	public IJavaInstance getBean() {
		if (bean == null) {
			bean = (IJavaInstance) getModel();
		}
		return bean;
	}

	public void refresh() {
		super.refresh();
		if (bounds != null) {
			getFigure().setBounds(getBounds());
		}
	}

	public Rectangle getBounds() {
		return bounds;
	}

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

	protected IFigure createFigure() {
		ImageFigure figure = new ImageFigure();
		imageFigureController = new ImageFigureController();
		imageFigureController.setImageFigure(figure);
		figure.setOpaque(false);
		IFigure ToolTipFig = ToolTipContentHelper.createToolTip(ToolTipAssistFactory.createToolTipProcessors(getBean()));
		figure.setToolTip(ToolTipFig);
		fErrorIndicator = new ErrorFigure(IBeanProxyHost.ERROR_NONE);
		figure.add(fErrorIndicator);
		IRectangleBeanProxy rectBeanProxy = getToolItemProxyAdapter().getBounds();
		if (rectBeanProxy != null) {
			bounds = new Rectangle(rectBeanProxy.getX(), rectBeanProxy.getY(), rectBeanProxy.getWidth(), rectBeanProxy.getHeight());
		}
		return figure;
	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DefaultComponentEditPolicy());
		//		sfDirectEditProperty = getDirectEditTargetProperty();
		//		if (sfDirectEditProperty != null) {
		//			installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new ComponentDirectEditPolicy());
	}
}