package org.eclipse.ve.internal.jfc.core;

/*****************************************************************************************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Common Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************************************************************************/
/*
 * $RCSfile: ComponentGraphicalEditPart.java,v $ $Revision: 1.6 $ $Date: 2004-06-29 18:20:33 $
 */
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
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.*;

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
public class ComponentGraphicalEditPart extends AbstractGraphicalEditPart implements IExecutableExtension, IJavaBeanGraphicalContextMenuContributor, IDirectEditableEditPart {

	protected boolean transparent = false; // Whether there should be an image or not.
	protected ImageFigureController imageFigureController;

	protected IPropertySource propertySource; // This is the property source.

	protected ComponentModelAdapter constraintHandler; // A subclass of this is for the editpart constraint handler.
	protected IBeanProxyHost.ErrorListener fBeanProxyErrorListener;

	protected boolean border = false; // Whether there should be a border or not around the figure.
	protected DirectEditManager manager = null;

	protected EStructuralFeature sfDirectEditProperty = null;
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

	private class ComponentVisualModelAdapter extends ComponentModelAdapter {

		private ListenerList listeners;

		private VisualComponentListener vListener;

		private class VisualComponentListener implements IVisualComponentListener {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.ve.internal.cde.core.IVisualComponentListener#componentHidden()
			 */
			public void componentHidden() {
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.ve.internal.cde.core.IVisualComponentListener#componentMoved(int, int)
			 */
			public void componentMoved(int x, int y) {
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.ve.internal.cde.core.IVisualComponentListener#componentRefreshed()
			 */
			public void componentRefreshed() {
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

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.ve.internal.cde.core.IVisualComponentListener#componentShown()
			 */
			public void componentShown() {
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
			Rectangle bounds = getVisualComponent().getBounds();
			modelConstraint.width = bounds.width;
			modelConstraint.height = bounds.height;
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

	public void setPropertySource(IPropertySource source) {
		propertySource = source;
	}

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
		// Listen to the IBeanProxyHost so it tells us when errors occur
		fBeanProxyErrorListener = new IErrorNotifier.ErrorListenerAdapter() {
			public void errorStatusChanged() {
				CDEUtilities.displayExec(ComponentGraphicalEditPart.this,  new Runnable() {
					public void run() {
						setSeverity(getComponentProxy().getErrorStatus());
					}
				}); 
			}
		};
		setSeverity(getComponentProxy().getErrorStatus()); // Set the initial
		// status
		getComponentProxy().addErrorListener(fBeanProxyErrorListener);
	}

	protected void setSeverity(int severity) {
		fErrorIndicator.sevSeverity(severity);
		getFigure().setVisible(!(severity == IBeanProxyHost.ERROR_SEVERE));
	}

	public void deactivate() {
		if (imageFigureController != null)
			imageFigureController.deactivate();
		if (fBeanProxyErrorListener != null) {
			IBeanProxyHost beanProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) getModel());
			beanProxyHost.removeErrorListener(fBeanProxyErrorListener);
		}
		super.deactivate();
	}

	private static final String FREEFORM_EDITPOLICY = "free_form editpolicy";

	/**
	 * createInputPolicies method comment.
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DefaultComponentEditPolicy());
		sfDirectEditProperty = getDirectEditTargetProperty();
		if (sfDirectEditProperty != null) {
			installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new ComponentDirectEditPolicy());
		}
		if (getParent() instanceof ContentsGraphicalEditPart) {
			// TODO At the moment a bit of kludge to get the FreeForm Dialogs
			// working.
			// It will only add when parent is the FreeForm.
			EditPart parent = getParent();
			if (parent.getEditPolicy(FREEFORM_EDITPOLICY) == null) {
				parent.installEditPolicy(FREEFORM_EDITPOLICY, new CompositionFreeFormComponentsEditPolicy());
			}
		}
	}

	private EStructuralFeature getDirectEditTargetProperty() {
		EStructuralFeature target = null;
		IJavaObjectInstance component = (IJavaObjectInstance) getModel();
		JavaClass modelType = (JavaClass) component.eClass();
		// Hard coded string properties to direct edit.
		// If more than one is available, it'll choose the first in the list
		// below
		target = modelType.getEStructuralFeature("text"); //$NON-NLS-1$
		if (target != null) { return target; }
		target = modelType.getEStructuralFeature("label"); //$NON-NLS-1$
		if (target != null) { return target; }
		target = modelType.getEStructuralFeature("title"); //$NON-NLS-1$
		return target;
	}

	private void performDirectEdit() {
		if (manager == null)
			manager = new ComponentDirectEditManager(this, TextCellEditor.class, new ComponentCellEditorLocator(getFigure()), sfDirectEditProperty);
		manager.show();
	}

	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT && sfDirectEditProperty != null)
			performDirectEdit();
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
	
	public boolean getTransparent(){
		return transparent;
	}

	/**
	 * Return the visual component. Creation date: (3/15/00 12:33:41 PM)
	 * 
	 */
	protected IVisualComponent getVisualComponent() {
		return (IVisualComponent) getComponentProxy(); // For AWT, the
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
	 * @return
	 */
	public EStructuralFeature getSfDirectEditProperty() {
		return sfDirectEditProperty;
	}
}
