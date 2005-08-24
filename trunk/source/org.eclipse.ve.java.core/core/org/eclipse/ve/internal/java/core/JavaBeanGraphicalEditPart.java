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
/*
 * $RCSfile: JavaBeanGraphicalEditPart.java,v $ $Revision: 1.11 $ $Date: 2005-08-24 23:30:46 $
 */
package org.eclipse.ve.internal.java.core;

import java.util.*;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ui.IActionFilter;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.DefaultGraphicalEditPart;

/**
 * Default Non-visual bean graphical edit part.
 * 
 * @since 1.1.0
 */
public class JavaBeanGraphicalEditPart extends DefaultGraphicalEditPart implements IJavaBeanGraphicalContextMenuContributor {

	protected IBeanProxyHost.ErrorListener fBeanProxyErrorListener;

	protected ErrorFigure fErrorIndicator;

	public JavaBeanGraphicalEditPart(Object model) {
		setModel(model);
	}

	public IJavaInstance getBean() {
		return (IJavaInstance) getModel();
	}

	public void setModel(Object model) {
		super.setModel(model);
	}

	protected IErrorNotifier getErrorNotifier() {
		return (IErrorNotifier) EcoreUtil.getExistingAdapter((Notifier) getModel(), IErrorNotifier.ERROR_NOTIFIER_TYPE);
	}
	
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(CopyAction.REQ_COPY,new DefaultCopyEditPolicy());
	}	
	
	public void activate() {
		super.activate();
		fBeanProxyErrorListener = new IErrorNotifier.ErrorListenerAdapter() {

			public void errorStatusChanged() {
				CDEUtilities.displayExec(JavaBeanGraphicalEditPart.this, "STATUS_CHANGED", new EditPartRunnable(JavaBeanGraphicalEditPart.this) {

					protected void doRun() {
						setSeverity(getErrorNotifier().getErrorStatus());
					}
				});
			}
		};

		IErrorNotifier errorNotifier = getErrorNotifier();
		setSeverity(errorNotifier.getErrorStatus()); // Set the initial status
		errorNotifier.addErrorListener(fBeanProxyErrorListener);
		
		((ToolTipContentHelper.AssistedToolTipFigure) getFigure().getToolTip()).activate();
	}

	protected void setSeverity(int severity) {
		fErrorIndicator.setSeverity(severity);
	}

	public void deactivate() {
		((ToolTipContentHelper.AssistedToolTipFigure) getFigure().getToolTip()).deactivate();
		if (fBeanProxyErrorListener != null) {
			getErrorNotifier().removeErrorListener(fBeanProxyErrorListener);
			fBeanProxyErrorListener = null;
		}
		super.deactivate();
	}

	public Object getAdapter(Class aKey) {
		// See if any of the MOF adapters on our target can return a value for the request
		Object result = super.getAdapter(aKey);
		if (result != null) {
			return result;
		} else if (aKey == IActionFilter.class)
			return getJavaActionFilter();
		else if (aKey == IErrorHolder.class)
			return getErrorNotifier();
		else {
			Iterator mofAdapters = ((IJavaInstance) getModel()).eAdapters().iterator();
			while (mofAdapters.hasNext()) {
				Object mofAdapter = mofAdapters.next();
				if (mofAdapter instanceof IAdaptable) {
					Object mofAdapterAdapter = ((IAdaptable) mofAdapter).getAdapter(aKey);
					if (mofAdapterAdapter != null) { return mofAdapterAdapter; }
				}
			}
		}
		return null;
	}

	protected IActionFilter getJavaActionFilter() {
		return JavaBeanActionFilter.INSTANCE;
	}

	public List getEditPolicies() {
		List result = new ArrayList();
		EditPolicyIterator i = getEditPolicyIterator();
		while (i.hasNext()) {
			result.add(i.next());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		IFigure fig = super.createFigure();
		fErrorIndicator = new ErrorFigure();
		fig.add(fErrorIndicator);
		ToolTipContentHelper.AssistedToolTipFigure toolTipFig = ToolTipContentHelper.createToolTip(ToolTipAssistFactory.createToolTipProcessors(
				getBean(), getErrorNotifier()));
		fig.setToolTip(toolTipFig);
		return fig;
	}

}
