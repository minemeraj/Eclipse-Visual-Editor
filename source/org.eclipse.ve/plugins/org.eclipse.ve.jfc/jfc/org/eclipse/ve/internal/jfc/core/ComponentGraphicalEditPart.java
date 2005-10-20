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
package org.eclipse.ve.internal.jfc.core;

/*
 * $RCSfile: ComponentGraphicalEditPart.java,v $ $Revision: 1.33 $ $Date: 2005-10-20 22:30:49 $
 */
import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.*;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.ImageFigure;
import org.eclipse.ve.internal.cde.properties.PropertySourceAdapter;

import org.eclipse.ve.internal.java.core.*;

/**
 * EditPart for a java.awt.Component. The parent editpart is responsible to set transparent. If transparent, then there won't be any image capture.
 * 
 * Note: Bit of a kludge, but because a component could be either on the freeform or within another component, we need to differentiate for
 * propertySource between the component and the componentConstraint (or whatever). This is so that when on the freeform, the component property
 * source is it, but when on another component, we want to merge the constraint info with the component features. This is done by setting into the
 * editpart who should be used as the model to find the propertySource. The default is the model of the editpart, while if set, should use the
 * propertySourceModel. The component itself is considered to be the model. This is so that when moving a component between two different parents, it
 * will see the component and not the constraint as to what is being added. The policy will create a new constraint for where it is going.
 * 
 * The initialization data is used to configure whether this edit part should show borders on the figure or not. The default is false. The
 * initialization data can be "true" or "false", case-insensitive. This init data can be placed in the xmi to determine what it should be.
 */
public class ComponentGraphicalEditPart extends CDEAbstractGraphicalEditPart implements IExecutableExtension, IJavaBeanGraphicalContextMenuContributor {

	protected boolean transparent = false; // Whether there should be an image or not.
	protected ImageFigureController imageFigureController;

	protected IPropertySource propertySource; // This is the property source.

	protected ComponentModelAdapter constraintHandler; // A subclass of this is for the editpart constraint handler.
	protected IBeanProxyHost.ErrorListener fBeanProxyErrorListener;

	protected boolean border = false; // Whether there should be a border or not around the figure.
	protected DirectEditManager manager = null;

	protected IPropertyDescriptor sfDirectEditProperty = null;
	protected IJavaInstance bean;

	public ComponentGraphicalEditPart(Object model) {
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
			ifig.setBorder(new OutlineBorder(125, ColorConstants.gray, null, Graphics.LINE_SOLID));
		ifig.setOpaque(!transparent);
		if (!transparent) {
			imageFigureController = new ImageFigureController();			
			imageFigureController.setImageFigure(ifig);
		}
		cfig.setContentPane(ifig);
		fErrorIndicator = new ErrorFigure();
		cfig.add(fErrorIndicator);
		return cfig;
	}

	private class ComponentVisualModelAdapter extends ComponentModelAdapter {

		private ListenerList listeners;

		private VisualComponentListener vListener;

		private class VisualComponentListener extends VisualComponentAdapter {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.ve.internal.cde.core.IVisualComponentListener#componentRefreshed()
			 */
			public void componentRefreshed() {
				// Treat this as a resized, but get the new size.
				Dimension dim = getVisualComponent().getSize();
				Object[] listens = listeners.getListeners();
				for (int i = 0; i < listens.length; i++) {
					((IConstraintHandlerListener) listens[i]).sizeChanged(dim.width, dim.height);
				}				
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.ve.internal.cde.core.IVisualComponentListener#componentResized(int, int)
			 */
			public void componentResized(int width, int height) {
				Object[] listens = listeners.getListeners();
				for (int i = 0; i < listens.length; i++) {
					((IConstraintHandlerListener) listens[i]).sizeChanged(width, height);
				}
			}
		}

		public ComponentVisualModelAdapter(Object component) {
			super(component);
		}

		/**
		 * contributeModelSize For create: This isn't called.
		 */
		public void contributeModelSize(org.eclipse.ve.internal.cdm.model.Rectangle modelConstraint) {
			// We will use the size out of the live component instead.
			Dimension size = getVisualComponent().getSize();
			modelConstraint.width = size.width;
			modelConstraint.height = size.height;			
		}

		/**
		 * addConstraintHandlerListener
		 * 
		 * For create: This isn't called.
		 */
		public void addConstraintHandlerListener(IConstraintHandlerListener listener) {
			if (listeners == null)
				listeners = new ListenerList(1);
			if (vListener == null) {
				// About to add first one, so also add visual component
				// listener
				IVisualComponent visualComponent = (IVisualComponent) BeanProxyUtilities.getBeanProxyHost(component);
				vListener = new VisualComponentListener();
				visualComponent.addComponentListener(vListener);
			}
			listeners.add(listener);
		}

		/**
		 * removeConstraintHandlerListener
		 * 
		 * For create: This isn't called.
		 */
		public void removeConstraintHandlerListener(IConstraintHandlerListener listener) {
			if (listeners != null) {
				listeners.remove(listener);
				if (listeners.isEmpty() && vListener != null) {
					// No more, so get rid of visual component listener
					((IVisualComponent) BeanProxyUtilities.getBeanProxyHost(component)).removeComponentListener(vListener);
					vListener = null;
				}
			}
		}
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
				constraintHandler = new ComponentVisualModelAdapter(getModel());
			}
			return constraintHandler;
		} else if (type == IActionFilter.class)
			return getComponentActionFilter();
		else if (type == LayoutList.class)
			return BeanAwtUtilities.getDefaultLayoutList();
		else if (type == IErrorHolder.class)
			return errorNotifier;
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
	
	/**
	 * Used by other graphical editparts to say even though this is modeling an awt.Component, use this
	 * guy as an error notifier. This is used by ContainerGraphicalEditPart, or JTabbedPaneEditPart, or
	 * any other container type editpart that uses an intermediate object. This component will then
	 * show the errors from itself (the awt.Component) and from the error notifier set in. Only
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

	private IErrorNotifier otherNotifier;
	
	protected ErrorFigure fErrorIndicator;

	/**
	 * If we are transparent, then have the proxy set to don't apply visible. If on the freeform (i.e. not transparent) we want it to always be
	 * visible.
	 */
	public void activate() {
		super.activate();
		if (!transparent) {
			imageFigureController.setImageNotifier(getVisualComponent());
		}
		// Listen to the error notifier so it tells us when errors occur
		fBeanProxyErrorListener = new IErrorNotifier.ErrorListenerAdapter() {
			public void errorStatusChanged() {
				CDEUtilities.displayExec(ComponentGraphicalEditPart.this, "STATUS_CHANGED", new EditPartRunnable(ComponentGraphicalEditPart.this) {
					protected void doRun() {
						setSeverity(errorNotifier.getErrorStatus());
					}
				}); 
			}
		};
		
		errorNotifier.addErrorListener(fBeanProxyErrorListener);
		errorNotifier.addErrorNotifier((IErrorNotifier) EcoreUtil.getExistingAdapter((Notifier) getModel(), IErrorNotifier.ERROR_NOTIFIER_TYPE));	// This will signal initial severity if not none.
		errorNotifier.addErrorNotifier(otherNotifier);
		
		((ToolTipContentHelper) getFigure().getToolTip()).activate();
	}

	protected void setSeverity(int severity) {
		fErrorIndicator.setSeverity(severity);
		getFigure().setVisible(!(severity == IErrorHolder.ERROR_SEVERE));
	}

	public void deactivate() {
		((ToolTipContentHelper) getFigure().getToolTip()).deactivate();
		if (imageFigureController != null)
			imageFigureController.deactivate();
		if (fBeanProxyErrorListener != null) {
			errorNotifier.removeErrorListener(fBeanProxyErrorListener);
		}
		errorNotifier.dispose();
		super.deactivate();
	}

	/**
	 * createInputPolicies method comment.
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DefaultComponentEditPolicy());
		sfDirectEditProperty = getDirectEditTargetProperty();
		if (sfDirectEditProperty != null) {
			installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new BeanDirectEditPolicy());
		}
		installEditPolicy(CopyAction.REQ_COPY,new ComponentCopyEditPolicy(EditDomain.getEditDomain(this)));		
	}

	protected IPropertyDescriptor getDirectEditTargetProperty() {
		EStructuralFeature feature = null;
		IJavaObjectInstance component = (IJavaObjectInstance) getModel();
		JavaClass modelType = (JavaClass) component.eClass();
		// Hard coded string properties to direct edit.
		// If more than one is available, it'll choose the first in the list
		// below
		feature = modelType.getEStructuralFeature("text"); //$NON-NLS-1$
		if (feature == null) {
			feature = modelType.getEStructuralFeature("label"); //$NON-NLS-1$
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

	private void performDirectEdit() {
		if (manager == null)
			manager = new CDEDirectEditManager(this, new BeanDirectEditCellEditorLocator(getFigure()), sfDirectEditProperty);
		manager.show();
	}

	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT && sfDirectEditProperty != null)
			performDirectEdit();
		else
			super.performRequest(request);
	}

	/**
	 * Return the IBean
	 */
	public IJavaInstance getBean() {
		if(bean == null){
			bean = (IJavaInstance) getModel();
		}
		return bean;
	}

	protected IActionFilter getComponentActionFilter() {
		return JavaBeanActionFilter.INSTANCE;
	}

	/**
	 * Return the ComponentProxy. Creation date: (3/15/00 12:33:41 PM)
	 * 
	 */
	protected ComponentProxyAdapter getComponentProxy() {
		IBeanProxyHost beanProxy = BeanProxyUtilities.getBeanProxyHost(getBean());
		return (ComponentProxyAdapter) beanProxy;
	}

	/**
	 * Parents of this will call this and pass in true if the image should be retrieved. It is important that this is called by parent editpart
	 * before the editpart is activated (this is best done in the createEditPart method of the parent edit part).
	 */
	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}


	/**
	 * Return the visual component. Creation date: (3/15/00 12:33:41 PM)
	 * 
	 */
	protected IVisualComponent getVisualComponent() {
		return getComponentProxy(); // For AWT, the
		// component proxy is
		// the visual component.
	}

	/**
	 * Return a list of edit policies.
	 * 
	 * This should not be needed but GEF doesn't make the edit policies public except by a specific key.
	 */
	public List getEditPolicies() {
		List result = new ArrayList();
		AbstractEditPart.EditPolicyIterator i = super.getEditPolicyIterator();
		while (i.hasNext()) {
			result.add(i.next());
		}
		return result.isEmpty() ? Collections.EMPTY_LIST : result;
	}

	/**
	 * Lighten this figure excluding the child figures
	 */
	public void emphasizeChildren(List childEditParts) {		
		if (imageFigureController != null) {
			// Lighten our figure
			imageFigureController.addLightenFigure(getContentPane());
			Iterator editParts = childEditParts.iterator();
			while (editParts.hasNext()) {
				// Get the figures for each of the edit parts and add them to a set to unlighten				
				IFigure figure = ((GraphicalEditPart) editParts.next()).getContentPane();
				imageFigureController.addUnlightenFigure(figure);
			}
		}
	}

	public void unEmphasizeChildren(List childEditParts){
		if(imageFigureController != null){
			// Get the figures for each of the edit parts and remove them from the set that is holding onto the ligten set
			Iterator editParts = childEditParts.iterator();
			while (editParts.hasNext()) {
				// Get the figures for each of the edit parts and add them to a set to unlighten				
				IFigure figure = ((GraphicalEditPart) editParts.next()).getContentPane();
				imageFigureController.removeUnLightenFigure(figure);
			}			
			// If there are no children being lightened then unlighten the overall figure
			if (!imageFigureController.hasUnlightenedFigures()){
				imageFigureController.removeLightenFigure(getContentPane());
			}
		}
	}
	
	public void unEmphasizeChild(GraphicalEditPart childEditPart){
		if(imageFigureController != null){
			IFigure figure = childEditPart.getContentPane();
			imageFigureController.removeUnLightenFigure(figure);
			// If there are no children being lightened then unlighten the overall figure
			if (!imageFigureController.hasUnlightenedFigures()){
				imageFigureController.removeLightenFigure(getContentPane());
			}			
		}
	}
	
	public void setLightenColor(RGB lightenColor) {
		if (imageFigureController != null)
			imageFigureController.setLightenColor(lightenColor);
	}
	
	public void setLightenCrossHatch(boolean crossHatch) {
		if (imageFigureController != null)
			imageFigureController.setCrossHatch(crossHatch);
	}

	protected ToolTipProcessor[] createToolTipProcessors() {
		return ToolTipAssistFactory.createToolTipProcessors(getBean(), errorNotifier);
	}
}
