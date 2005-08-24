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
 * $RCSfile: ItemGraphicalEditPart.java,v $ $Revision: 1.3 $ $Date: 2005-08-24 23:52:56 $
 */
package org.eclipse.ve.internal.swt;

import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.*;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.*;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.properties.PropertySourceAdapter;

import org.eclipse.ve.internal.java.core.*;

/**
 * Base class for Items graphical editparts. It handles the figure and the direct edit of the text. It is
 * for the items which can give up a bounds.
 * 
 * @since 1.1.0
 */
public abstract class ItemGraphicalEditPart extends AbstractGraphicalEditPart implements IExecutableExtension, IJavaBeanGraphicalContextMenuContributor {


	protected ImageFigureController imageFigureController;

	protected ErrorFigure fErrorIndicator;

	protected IBeanProxyHost.ErrorListener fBeanProxyErrorListener;
	
	protected EStructuralFeature sfText;
	
	protected Adapter adapter = new AdapterImpl() {
		public void notifyChanged(Notification msg) {
			if (msg.getEventType() != Notification.REMOVING_ADAPTER)
				((ItemParentGraphicalEditPart) getParent()).refreshItems();
		}
	};
			
	public ItemGraphicalEditPart(Object model) {
		setModel(model);
		sfText = JavaInstantiation.getReference((IJavaObjectInstance) getModel(), SWTConstants.SF_ITEM_TEXT);
	}
	
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
				CDEUtilities.displayExec(ItemGraphicalEditPart.this, "ERROR_STATUS_CHANGED", new EditPartRunnable(ItemGraphicalEditPart.this) { //$NON-NLS-1$

					protected void doRun() {
						setSeverity(getBeanProxyAdapter().getErrorStatus());
					}
				});
			}
		};

		setSeverity(getBeanProxyAdapter().getErrorStatus()); // Set the initial status
		getBeanProxyAdapter().addErrorListener(fBeanProxyErrorListener);
		getBean().eAdapters().add(adapter);
		
		((ToolTipContentHelper.AssistedToolTipFigure) getFigure().getToolTip()).activate();
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
	 */
	public void deactivate() {
		((ToolTipContentHelper.AssistedToolTipFigure) getFigure().getToolTip()).deactivate();
		getBeanProxyAdapter().removeErrorListener(fBeanProxyErrorListener);
		getBean().eAdapters().remove(adapter);
		super.deactivate();
	}

	protected void setSeverity(int severity) {
		if (isActive()) {
			fErrorIndicator.setSeverity(severity);
			getFigure().setVisible(!(severity == IErrorHolder.ERROR_SEVERE));
		}
	}

	protected IBeanProxyHost getBeanProxyAdapter() {
		return BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) getModel());
	}

	public IJavaInstance getBean() {
		return (IJavaInstance) getModel();
	}

	public void refreshVisuals() {
		super.refreshVisuals();
		getFigure().getParent().setConstraint(getFigure(), getBounds());
	}

	/**
	 * Get the bounds of the item.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected abstract Rectangle getBounds();

	public Object getAdapter(Class type) {
		if (type == IPropertySource.class)
			return EcoreUtil.getRegisteredAdapter((IJavaObjectInstance) getModel(), IPropertySource.class);
		else if (type == IActionFilter.class)
			return getControlActionFilter();
		else  if (type == IErrorHolder.class)
			return getBeanProxyAdapter();
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
	
	private IActionFilter getControlActionFilter() {
		return JavaBeanActionFilter.INSTANCE;
	}
	
	public List getEditPolicies() {
		List result = new ArrayList();
		AbstractEditPart.EditPolicyIterator i = super.getEditPolicyIterator();
		while (i.hasNext()) {
			result.add(i.next());
		}
		return result.isEmpty() ? Collections.EMPTY_LIST : result;
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
		directEditProperty = getDirectEditTargetProperty();
		// Install policy that will allow direct edit capability on the table column
		if (directEditProperty != null)
			installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new BeanDirectEditPolicy());
	}
	
	protected DirectEditManager manager = null;
	
	private void performDirectEdit() {
		if (manager == null)
			manager = new CDEDirectEditManager(this, new BeanDirectEditCellEditorLocator(getFigure()), directEditProperty);
		manager.show();
	}
	
	IPropertyDescriptor directEditProperty;
	
	protected IPropertyDescriptor getDirectEditTargetProperty() {
		if (sfText != null) {
			IPropertySource source = (IPropertySource) getAdapter(IPropertySource.class);
			return PropertySourceAdapter.getDescriptorForID(source, sfText);
		} else
			return null;
	}

	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT && sfText != null)
			performDirectEdit();
		else
			super.performRequest(request);
	}	
}
