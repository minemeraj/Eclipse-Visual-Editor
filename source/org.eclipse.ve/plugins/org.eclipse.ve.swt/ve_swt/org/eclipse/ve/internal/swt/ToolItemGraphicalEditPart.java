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
 * $RCSfile: ToolItemGraphicalEditPart.java,v $ $Revision: 1.6 $ $Date: 2005-05-18 16:48:00 $
 */
package org.eclipse.ve.internal.swt;

import java.util.Iterator;

import org.eclipse.core.runtime.*;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.proxy.core.IRectangleBeanProxy;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

import org.eclipse.ve.internal.java.core.*;

public class ToolItemGraphicalEditPart extends AbstractGraphicalEditPart implements IExecutableExtension {


	protected ImageFigureController imageFigureController;

	private ToolItemProxyAdapter toolItemProxyAdapter;

	protected ErrorFigure fErrorIndicator;

	protected IBeanProxyHost.ErrorListener fBeanProxyErrorListener;
	
	protected Adapter adapter = new EditPartAdapterRunnable() {

		public void notifyChanged(Notification notification) {
			if (notification.getEventType() == Notification.REMOVING_ADAPTER)
				return;
			// Else assume a refresh is needed.
			queueExec(ToolItemGraphicalEditPart.this, "REFRESH"); //$NON-NLS-1$
		}

		public void run() {
			if (isActive())
				((ToolBarGraphicalEditPart) getParent()).refreshItems();
		}
	};
	
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
				CDEUtilities.displayExec(ToolItemGraphicalEditPart.this, "ERROR_STATUS_CHANGED", new Runnable() { //$NON-NLS-1$

					public void run() {
						setSeverity(getControlProxy().getErrorStatus());
					}
				});
			}
		};

		setSeverity(getControlProxy().getErrorStatus()); // Set the initial status
		getControlProxy().addErrorListener(fBeanProxyErrorListener);
		getBean().eAdapters().add(adapter);
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
	 */
	public void deactivate() {
		getControlProxy().removeErrorListener(fBeanProxyErrorListener);
		getBean().eAdapters().remove(adapter);
		super.deactivate();
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

	protected void setSeverity(int severity) {
		fErrorIndicator.setSeverity(severity);
		getFigure().setVisible(!(severity == IErrorHolder.ERROR_SEVERE));
	}

	protected ToolItemProxyAdapter getControlProxy() {
		return getToolItemProxyAdapter();
	}

	public IJavaInstance getBean() {
		return (IJavaInstance) getModel();
	}

	public void refreshVisuals() {
		super.refreshVisuals();
		getFigure().getParent().setConstraint(getFigure(), getBounds());
	}

	public Rectangle getBounds() {
		IRectangleBeanProxy rectBeanProxy = getToolItemProxyAdapter().getBounds();
		if (rectBeanProxy != null) {
			return new Rectangle(rectBeanProxy.getX(), rectBeanProxy.getY(), rectBeanProxy.getWidth(), rectBeanProxy.getHeight());
		}
		return new Rectangle(0, 0, 10, 10);
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

	protected boolean border;
	
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		if (data instanceof String)
			border = Boolean.valueOf((String) data).booleanValue();
	}
	
	/**
	 * Get the main figure as a {@link ContentPaneFigure}.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected ContentPaneFigure getContentPaneFigure() {
		return (ContentPaneFigure)getFigure();
	}
	
	public IFigure getContentPane() {
		return getContentPaneFigure().getContentPane();
	}
	
	protected IFigure createFigure() {
		ContentPaneFigure cfig = new ContentPaneFigure();
		ImageFigure ifig = new ImageFigure();
		if (border)
			ifig.setBorder(new OutlineBorder());
		cfig.setContentPane(ifig);
		fErrorIndicator = new ErrorFigure();
		cfig.add(fErrorIndicator);
		IFigure ToolTipFig = ToolTipContentHelper.createToolTip(ToolTipAssistFactory.createToolTipProcessors(getBean(), (IErrorNotifier) EcoreUtil.getExistingAdapter((Notifier) getModel(), IErrorNotifier.ERROR_NOTIFIER_TYPE)));
		cfig.setToolTip(ToolTipFig);
		return cfig;
	}


	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DefaultComponentEditPolicy());
		//		sfDirectEditProperty = getDirectEditTargetProperty();
		//		if (sfDirectEditProperty != null) {
		//			installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new ComponentDirectEditPolicy());
	}
}