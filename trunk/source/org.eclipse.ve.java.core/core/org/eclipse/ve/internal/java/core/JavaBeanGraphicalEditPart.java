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
/*
 * $RCSfile: JavaBeanGraphicalEditPart.java,v $ $Revision: 1.5 $ $Date: 2005-02-15 23:23:54 $
 */
package org.eclipse.ve.internal.java.core;

import java.util.*;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IActionFilter;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.emf.DefaultGraphicalEditPart;

public class JavaBeanGraphicalEditPart extends DefaultGraphicalEditPart implements IJavaBeanGraphicalContextMenuContributor {

	protected IBeanProxyHost.ErrorListener fBeanProxyErrorListener;
	protected IJavaInstance bean;

	public JavaBeanGraphicalEditPart(Object model) {
		setModel(model);
	}

	public IJavaInstance getBean(){
		if(bean == null){
			bean = (IJavaInstance)getModel();
		}
		return bean;
	}
	
	public void activate() {
		super.activate();
		IBeanProxyHost beanProxyHost = BeanProxyUtilities.getBeanProxyHost(getBean());
		if (fBeanProxyErrorListener == null) {
			fBeanProxyErrorListener = new IErrorNotifier.ErrorListenerAdapter() {
				public void errorStatusChanged(){
					CDEUtilities.displayExec(JavaBeanGraphicalEditPart.this, new Runnable() {
						public void run() {
							if (isActive())
								refreshVisuals();
						}
					});
				}
			};
		}
		beanProxyHost.addErrorListener(fBeanProxyErrorListener);
	}

	public void deactivate() {
		if (fBeanProxyErrorListener != null) {
			IBeanProxyHost beanProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) getModel());
			beanProxyHost.removeErrorListener(fBeanProxyErrorListener);
		}
		if (fOverlayImage != null) {
			fOverlayImage.dispose();
		}
		super.deactivate();
	}

	protected Image fOverlayImage;

	protected int fOverlaySeverity;

	protected void setFigureImage(Label aLabel, Image anImage) {

		// See whether or not the JavaBean is in error
		IBeanProxyHost beanProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) getModel());
		int beanProxyStatus = beanProxyHost.getErrorStatus();
		// If there is no error then just use the image
		if (beanProxyStatus == IBeanProxyHost.ERROR_NONE) {
			super.setFigureImage(aLabel, anImage);
		} else {
			// If we already have an overlay image and it is not for the same error severity then dispose it
			if (fOverlayImage != null && fOverlaySeverity != beanProxyStatus) {
				fOverlayImage.dispose();
				fOverlayImage = null;
			}
			// If we don't have an overlay image then create one
			if (fOverlayImage == null && anImage != null) {
				fOverlayImage = new Image(getViewer().getControl().getDisplay(), new JavaBeanTreeEditPart.JavaBeansImageDescriptor(anImage,
						beanProxyStatus).getImageData());
				fOverlaySeverity = beanProxyStatus;
			}
			super.setFigureImage(aLabel, fOverlayImage);
		}
	}

	public Object getAdapter(Class aKey) {
		// See if any of the MOF adapters on our target can return a value for the request
		Object result = super.getAdapter(aKey);
		if (result != null) {
			return result;
		} else if (aKey == IActionFilter.class)
			return getJavaActionFilter();
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
		ToolTipContentHelper.AssistedToolTipFigure toolTipFig = ToolTipContentHelper.createToolTip(ToolTipAssistFactory.createToolTipProcessors(getBean()));
		fig.setToolTip(toolTipFig);
		return fig;
	}

}
