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
 * $RCSfile: ControlGraphicalEditPart.java,v $ $Revision: 1.27 $ $Date: 2005-08-24 23:52:55 $
 */

package org.eclipse.ve.internal.swt;

import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.*;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.ImageFigure;
import org.eclipse.ve.internal.cde.properties.PropertySourceAdapter;

import org.eclipse.ve.internal.java.core.*;

public class ControlGraphicalEditPart extends AbstractGraphicalEditPart implements IExecutableExtension, IJavaBeanGraphicalContextMenuContributor {
	
	protected ImageFigureController imageFigureController;
	protected IJavaInstance bean;
	protected ErrorFigure fErrorIndicator;
	protected IBeanProxyHost.ErrorListener fBeanProxyErrorListener;
	protected IPropertySource propertySource;	// This is the property source.
	protected ControlVisualModelAdapter constraintHandler;	
	protected boolean transparent;
	protected boolean border = false; // Whether there should be a border or not around the figure.
	
	protected DirectEditManager manager = null;
	protected IPropertyDescriptor sfDirectEditProperty = null;

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
	
	protected IErrorNotifier.CompoundErrorNotifier errorNotifier = new IErrorNotifier.CompoundErrorNotifier();
	
	protected IFigure createFigure() {
		ContentPaneFigure cfig = new ContentPaneFigure();
		ImageFigure ifig = new ImageFigure();
		if (border)
			ifig.setBorder(new OutlineBorder(150, ColorConstants.lightGray, null, Graphics.LINE_SOLID));
		ifig.setOpaque(!transparent);
		if (!transparent) {
			imageFigureController = new ImageFigureController();			
			imageFigureController.setImageFigure(ifig);
		}
		cfig.setContentPane(ifig);
		fErrorIndicator = new ErrorFigure();
		cfig.add(fErrorIndicator);
		IFigure ToolTipFig = ToolTipContentHelper.createToolTip(ToolTipAssistFactory.createToolTipProcessors(getBean(), errorNotifier));
		cfig.setToolTip(ToolTipFig);
		return cfig;
	}
	
	public void activate() {
		super.activate();
		
		if (!transparent) {
			imageFigureController.setImageNotifier(getVisualComponent());
		}
			
		// Listen to the error notifier so it tells us when errors occur
		fBeanProxyErrorListener = new IErrorNotifier.ErrorListenerAdapter() {
			public void errorStatusChanged() {
				CDEUtilities.displayExec(ControlGraphicalEditPart.this, "STATUS_CHANGED", new EditPartRunnable(ControlGraphicalEditPart.this) { //$NON-NLS-1$
					protected void doRun() {
						setSeverity(errorNotifier.getErrorStatus());
					}
				}); 
			}
		};
	
		errorNotifier.addErrorListener(fBeanProxyErrorListener);
		errorNotifier.addErrorNotifier((IErrorNotifier) EcoreUtil.getExistingAdapter((Notifier) getModel(), IErrorNotifier.ERROR_NOTIFIER_TYPE));	// This will signal initial severity if not none.
		errorNotifier.addErrorNotifier(otherNotifier);
	
		((ToolTipContentHelper.AssistedToolTipFigure) getFigure().getToolTip()).activate();
	}
	
	public void setTransparent(boolean aBool){
		transparent = aBool;
	}
	
	public void deactivate() {
		((ToolTipContentHelper.AssistedToolTipFigure) getFigure().getToolTip()).deactivate();
		
		if (imageFigureController != null)
			imageFigureController.deactivate();
		if (fBeanProxyErrorListener != null) {
			errorNotifier.removeErrorListener(fBeanProxyErrorListener);
		}
		errorNotifier.dispose();
		super.deactivate();
	}

	/**
	 * Used by other graphical editparts to say even though this is modeling an awt.Component, use this
	 * guy as the property source. This is used by ContainerGraphicalEditPart, or JTabbedPaneEditPart, or
	 * any other container type editpart that uses an intermediate object. The intermediate object will
	 * be responsible for showing through the correct awt.Component properties.
	 * @param source
	 * 
	 * @since 1.1.0
	 */
	public void setPropertySource(IPropertySource source) {
		propertySource = source;
	}
	
	private IErrorNotifier otherNotifier;
	
	/**
	 * Used by other graphical editparts to say even though this is modeling an awt.Component, use this
	 * guy as an error notifier. This is used by CompositeGraphicalEditPart, or TabFolderEditPart, or
	 * any other container type editpart that uses an intermediate object. This control will then
	 * show the errors from itself (the swt.Control) and from the error notifier set in. Only
	 * one can be set at a time. A new set will remove the old one from the list.
	 * @param otherNotifier
	 * 
	 * @since 1.1.0
	 */
	public void setErrorNotifier(IErrorNotifier otherNotifier) {
		if (this.otherNotifier != null)
			errorNotifier.removeErrorNotifier(this.otherNotifier);
		this.otherNotifier = otherNotifier;
		if (isActive())
			errorNotifier.addErrorNotifier(this.otherNotifier);	// Don't do if not active. When activated it will add it.
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
		else if (type == IErrorHolder.class)
			return errorNotifier;
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
		if (isActive()) {
			fErrorIndicator.setSeverity(severity);
			getFigure().setVisible(!(severity == IErrorHolder.ERROR_SEVERE));
		}
	}
	
	protected void createEditPolicies() {
		// Default component role allows delete and basic behavior of a component within a parent edit part that contains it
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DefaultComponentEditPolicy());

		sfDirectEditProperty = getDirectEditTargetProperty();
		if (sfDirectEditProperty != null) {
		    installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new BeanDirectEditPolicy());
		}
		installEditPolicy(CopyAction.REQ_COPY,new ControlCopyEditPolicy());
	}
	
	protected IVisualComponent getVisualComponent() {
		return (IVisualComponent) BeanProxyUtilities.getBeanProxyHost(getBean());
	}
	protected ControlProxyAdapter getControlProxy() {
		return (ControlProxyAdapter) BeanProxyUtilities.getBeanProxyHost(getBean());
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
	
	public class ControlVisualModelAdapter extends ControlModelAdapter {
		
		protected IJavaObjectInstance control;		

		public ControlVisualModelAdapter(Object aControl) {
			super(aControl);
			control = (IJavaObjectInstance) aControl;
		}   	

		private ListenerList listeners;
		private VisualComponentListener vListener;

		private class VisualComponentListener extends VisualComponentAdapter  {
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
	
	private IPropertyDescriptor getDirectEditTargetProperty() {

		EStructuralFeature feature = null;
		IJavaObjectInstance component = (IJavaObjectInstance) getModel();
		JavaClass modelType = (JavaClass) component.eClass();
		// Hard coded string properties to direct edit.
		// If more than one is available, it'll choose the first in the list
		// below
		feature = modelType.getEStructuralFeature("label"); //$NON-NLS-1$
		if (feature == null) {
			feature = modelType.getEStructuralFeature("text"); //$NON-NLS-1$
		}
		if (feature == null) {
			feature = modelType.getEStructuralFeature("title"); //$NON-NLS-1$	
		}
		if (feature != null) {
			IPropertySource source = (IPropertySource) getAdapter(IPropertySource.class);
			return PropertySourceAdapter.getDescriptorForID(source, feature);
		} else
			return null;
	}
	
	private void performDirectEdit(){
		if(manager == null)
			manager = new CDEDirectEditManager(this, new BeanDirectEditCellEditorLocator(getFigure()), sfDirectEditProperty);
		manager.show();
	}

	public void performRequest(Request request){
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT && getEditPolicy(EditPolicy.DIRECT_EDIT_ROLE) != null){
			if(sfDirectEditProperty == null){
				Object directEditPolicy = getEditPolicy(EditPolicy.DIRECT_EDIT_ROLE);
				if(directEditPolicy instanceof TabFolderGraphicalEditPart.TabItemDirectEditPolicy){
					sfDirectEditProperty = ((TabFolderGraphicalEditPart.TabItemDirectEditPolicy)directEditPolicy).getTabTextEditProperty();
				}
			}
			performDirectEdit();
		} else {
			super.performRequest(request);
		}
	}
}  
