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
 * $RCSfile: ControlGraphicalEditPart.java,v $ $Revision: 1.13 $ $Date: 2005-03-28 22:09:51 $
 */

package org.eclipse.ve.internal.swt;

import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.*;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.java.core.*;

public class ControlGraphicalEditPart extends AbstractGraphicalEditPart implements IExecutableExtension, IJavaBeanGraphicalContextMenuContributor, IDirectEditableEditPart {
	
	protected ImageFigureController imageFigureController;
	protected IJavaInstance bean;
	protected ErrorFigure fErrorIndicator;
	protected IBeanProxyHost.ErrorListener fBeanProxyErrorListener;
	protected IPropertySource propertySource;	// This is the property source.
	protected ControlVisualModelAdapter constraintHandler;	
	protected boolean transparent;
	protected boolean border = false; // Whether there should be a border or not around the figure.
	
	protected DirectEditManager manager = null;
	protected EStructuralFeature sfDirectEditProperty = null;

	public ControlGraphicalEditPart(Object model) {
		setModel(model);
	}
	
	/**
	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(IConfigurationElement, String, Object)
	 */
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		if (data instanceof String)
			border = Boolean.valueOf((String) data).booleanValue();
	}
	
	protected IFigure createFigure() {
		ImageFigure fig = new ImageFigure();
		if (border)
			fig.setBorder(new OutlineBorder());
		fig.setOpaque(!transparent);
		if (!transparent) {
			imageFigureController = new ImageFigureController();
			imageFigureController.setImageFigure(fig);
		}
		fErrorIndicator = new ErrorFigure(IBeanProxyHost.ERROR_NONE);
		fig.add(fErrorIndicator);
		
		IFigure ToolTipFig = ToolTipContentHelper.createToolTip(ToolTipAssistFactory.createToolTipProcessors(getBean()));
		fig.setToolTip(ToolTipFig);
				
		return fig;
	}
	public void activate() {
		super.activate();
		
		if (!transparent) {
			imageFigureController.setImageNotifier(getVisualComponent());
		}
			
		// Listen to the IBeanProxyHost so it tells us when errors occur
		fBeanProxyErrorListener = new IErrorNotifier.ErrorListenerAdapter(){
			public void errorStatusChanged(){
				CDEUtilities.displayExec(ControlGraphicalEditPart.this,  new Runnable() {
					public void run() {
						setSeverity(getControlProxy().getErrorStatus());
					}
				});
			}
		};
	
		setSeverity(getControlProxy().getErrorStatus());	// Set the initial status
		getControlProxy().addErrorListener(fBeanProxyErrorListener);
	
	}
	public void setTransparent(boolean aBool){
		transparent = aBool;
	}

	public void setPropertySource(IPropertySource source) {
		propertySource = source;
	}
	public Object getAdapter(Class type) {
		if (type == IVisualComponent.class)
			return getVisualComponent();
		else if (type == IPropertySource.class)
			if (propertySource != null)
				return propertySource;
			else
				return EcoreUtil.getRegisteredAdapter((IJavaObjectInstance) getModel(), IPropertySource.class);
		else if (type == IConstraintHandler.class) {
			if (constraintHandler == null) {
				constraintHandler = new ControlVisualModelAdapter(getModel());
			}
			return constraintHandler;
		}
		else if (type == IActionFilter.class)
			return getControlActionFilter();
		
		Object result = super.getAdapter(type);
		if ( result != null ) {
			return result;
		} else {
			// See if any of the MOF adapters on our target can return a value for the request
			Iterator mofAdapters = ((IJavaInstance)getModel()).eAdapters().iterator();
			while(mofAdapters.hasNext()){
				Object mofAdapter = mofAdapters.next();
				if ( mofAdapter instanceof IAdaptable ) {
					Object mofAdapterAdapter = ((IAdaptable)mofAdapter).getAdapter(type);
					if ( mofAdapterAdapter != null ) {
						return mofAdapterAdapter;
					}
				}
			}
		}
		return null;
	}		

	protected void setSeverity(int severity) {
		fErrorIndicator.sevSeverity(severity);
		getFigure().setVisible(!(severity == IBeanProxyHost.ERROR_SEVERE));
	}
	protected void createEditPolicies() {
		// Default component role allows delete and basic behavior of a component within a parent edit part that contains it
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DefaultComponentEditPolicy());

		sfDirectEditProperty = getDirectEditTargetProperty();
		if (sfDirectEditProperty != null) {
		    installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new BeanDirectEditPolicy());
		}
	}
	protected IVisualComponent getVisualComponent() {
		return (IVisualComponent) BeanProxyUtilities.getBeanProxyHost(getBean());
	}
	protected IBeanProxyHost getControlProxy() {
		return BeanProxyUtilities.getBeanProxyHost(getBean());
	}
	public IJavaInstance getBean() {
		if(bean == null){
			bean = (IJavaInstance) getModel();
		}
		return bean;
	}	
	
	private IActionFilter getControlActionFilter() {
		return JavaBeanActionFilter.INSTANCE;
	}
	
	private class ControlVisualModelAdapter extends ControlModelAdapter {
		
		protected IJavaObjectInstance control;		

		public ControlVisualModelAdapter(Object aControl) {
			super(aControl);
			control = (IJavaObjectInstance) aControl;
		}   	

		private ListenerList listeners;
		private VisualComponentListener vListener;

		private class VisualComponentListener implements IVisualComponentListener {
			public void componentHidden() {
			}
			public void componentMoved(int x, int y) {
			}
			public void componentRefreshed() {
				// For the initial resize get the bounds and treat this as a resize
				// Don't do this with a synchronous call to getVisualComponent.getBounds()
				// as this creates a deadlock, so we do an async exec so that the target VM is freed
				// by this method returning
				Display.getDefault().asyncExec(new Runnable(){
					public void run(){
						Rectangle bounds = getVisualComponent().getBounds();
						componentResized(bounds.width,bounds.height);				
					}
				});
			}
			public void componentResized(int width, int height) {
				Object[] listens = listeners.getListeners();
				for (int i = 0; i < listens.length; i++) {
					((IConstraintHandlerListener) listens[i]).sizeChanged(width, height);
				}
			}
			public void componentShown() {
			}
		}
		public void addConstraintHandlerListener(IConstraintHandlerListener listener) {
			if (listeners == null)
				listeners = new ListenerList(1);
			if (vListener == null) {
				// About to add first one, so also add visual component listener
				IVisualComponent visualComponent = (IVisualComponent) BeanProxyUtilities.getBeanProxyHost(control);
				vListener = new VisualComponentListener();
				visualComponent.addComponentListener(vListener);
			}
			listeners.add(listener);
		}
		public void removeConstraintHandlerListener(IConstraintHandlerListener listener) {
			if (listeners != null) {
				listeners.remove(listener);
				if (listeners.isEmpty() && vListener != null) {
					// No more, so get rid of visual component listener
					 ((IVisualComponent) BeanProxyUtilities.getBeanProxyHost(control)).removeComponentListener(vListener);
					vListener = null;
				}
			}
		}
		public void contributeModelSize(org.eclipse.ve.internal.cdm.model.Rectangle modelConstraint) {
			Rectangle bounds = getVisualComponent().getBounds();
			modelConstraint.width = bounds.width;
			modelConstraint.height = bounds.height;		
		}
	}
	public List getEditPolicies() {
		List result = new ArrayList();
		AbstractEditPart.EditPolicyIterator i = super.getEditPolicyIterator();
		while (i.hasNext()) {
			result.add(i.next());
		}
		return result.isEmpty() ? Collections.EMPTY_LIST : result;
	}
	
	private EStructuralFeature getDirectEditTargetProperty() {
		EStructuralFeature target = null;
		IJavaObjectInstance component = (IJavaObjectInstance)getModel();
		JavaClass modelType = (JavaClass) component.eClass();
		
		// Hard coded string properties to direct edit.
		// If more than one is available, it'll choose the first in the list below
				
		target = modelType.getEStructuralFeature("text"); //$NON-NLS-1$
		if (target != null) {
			return target;			
		}
		target = modelType.getEStructuralFeature("label"); //$NON-NLS-1$
		if (target != null) {
			return target;
		}
		target = modelType.getEStructuralFeature("title"); //$NON-NLS-1$
		return target;
	}
	
	private void performDirectEdit(){
		if(manager == null)
			manager = new BeanDirectEditManager(this, 
				TextCellEditor.class, new ControlCellEditorLocator(getFigure()), sfDirectEditProperty);
		manager.show();
	}

	public void performRequest(Request request){
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT && sfDirectEditProperty != null)
			performDirectEdit();
	}
	
	public EStructuralFeature getSfDirectEditProperty() {
		return sfDirectEditProperty;
	}
}  
