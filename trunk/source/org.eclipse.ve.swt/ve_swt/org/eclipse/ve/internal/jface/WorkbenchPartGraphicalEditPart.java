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
 *  $RCSfile: WorkbenchPartGraphicalEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-15 20:19:21 $ 
 */
package org.eclipse.ve.internal.jface;

import java.util.*;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.IErrorNotifier.ErrorListenerAdapter;

import org.eclipse.ve.internal.java.core.*;

import org.eclipse.ve.internal.swt.ControlGraphicalEditPart;
import org.eclipse.ve.internal.swt.SwtPlugin;

/**
 * RCP Workbench part graphical edit part 
 * 
 * @since 1.1.0
 */
public class WorkbenchPartGraphicalEditPart extends AbstractGraphicalEditPart implements IJavaBeanGraphicalContextMenuContributor {
	
	public WorkbenchPartGraphicalEditPart(Object model) {
		setModel(model);
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
	
	private ImageFigureController imageFigureController;
	private ErrorFigure fErrorIndicator;
	private ErrorListenerAdapter fBeanProxyErrorListener;
	private Object constraintHandler;

	protected IErrorNotifier getErrorNotifier() {
		return (IErrorNotifier) EcoreUtil.getExistingAdapter((Notifier) getModel(), IErrorNotifier.ERROR_NOTIFIER_TYPE);
	}
	
	protected IFigure createFigure() {
		ContentPaneFigure cfig = new ContentPaneFigure();
		ImageFigure ifig = new ImageFigure();
		ifig.setOpaque(true);
		ifig.setLayoutManager(new XYLayout());
		imageFigureController = new ImageFigureController();			
		imageFigureController.setImageFigure(ifig);
		cfig.setContentPane(ifig);
		fErrorIndicator = new ErrorFigure();
		cfig.add(fErrorIndicator);
		IFigure ToolTipFig = ToolTipContentHelper.createToolTip(ToolTipAssistFactory.createToolTipProcessors((IJavaInstance) getModel(), getErrorNotifier()));
		cfig.setToolTip(ToolTipFig);
		return cfig;
	}
	
	public void activate() {
		super.activate();
		
		imageFigureController.setImageNotifier(getVisualComponent());
			
		// Listen to the error notifier so it tells us when errors occur
		fBeanProxyErrorListener = new IErrorNotifier.ErrorListenerAdapter() {
			public void errorStatusChanged() {
				CDEUtilities.displayExec(WorkbenchPartGraphicalEditPart.this, "STATUS_CHANGED", new EditPartRunnable(WorkbenchPartGraphicalEditPart.this) { //$NON-NLS-1$
					protected void doRun() {
						setSeverity(getErrorNotifier().getErrorStatus());
					}
				}); 
			}
		};
	
		getErrorNotifier().addErrorListener(fBeanProxyErrorListener);
	
		((ToolTipContentHelper.AssistedToolTipFigure) getFigure().getToolTip()).activate();
	}

	public void deactivate() {
		((ToolTipContentHelper.AssistedToolTipFigure) getFigure().getToolTip()).deactivate();
		
		if (imageFigureController != null)
			imageFigureController.deactivate();
		if (fBeanProxyErrorListener != null) {
			getErrorNotifier().removeErrorListener(fBeanProxyErrorListener);
		}
		super.deactivate();
	}
	
	protected void setSeverity(int severity) {
		if (isActive()) {
			fErrorIndicator.setSeverity(severity);
			getFigure().setVisible(!(severity == IErrorHolder.ERROR_SEVERE));
		}
	}
	
	
	protected void createEditPolicies() {
		// Default component role allows delete and basic behavior of a component within a parent edit part that contains it
		installEditPolicy(VisualComponentsLayoutPolicy.LAYOUT_POLICY, new VisualComponentsLayoutPolicy(false));
	}
	
	protected IVisualComponent getVisualComponent() {
		return (IVisualComponent) BeanProxyUtilities.getBeanProxyHost((IJavaInstance) getModel());
	}
		
	public List getModelChildren() {
		// The "delegate_control" is our child
		List result = new ArrayList(1);
		IJavaInstance delegate_control = getDelegateComposite();
		if(delegate_control != null){
			result.add(delegate_control);
			return result;			
		} else {
			return Collections.EMPTY_LIST;
		}	
	}
	
	private IJavaInstance getDelegateComposite(){
		return BeanUtilities.getFeatureValue((IJavaInstance)getModel(),SwtPlugin.DELEGATE_CONTROL);		
	}
	
	protected EditPart createChild(Object model) {
		EditPart ep = super.createChild(model);
		try {
			ControlGraphicalEditPart controlep = (ControlGraphicalEditPart) ep;
			controlep.installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, new NonResizableEditPolicy());
			controlep.setTransparent(true); // So that it doesn't create an image, we subsume it here.
		} catch (ClassCastException e) {
			// For the rare times that it is not a ControlGraphicalEditPart (e.g. undefined).
		}
		return ep;
	}
	
	protected class ConstraintHandler implements IConstraintHandler {
			private ListenerList listeners;
			private VisualComponentListener vListener;

			private class VisualComponentListener implements IVisualComponentListener {
				public void componentHidden() {
				}
				public void componentMoved(int x, int y) {
				}
				public void componentRefreshed() {
					Rectangle bounds = getVisualComponent().getBounds();
					componentResized(bounds.width,bounds.height);				
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
					IVisualComponent visualComponent = getVisualComponent();
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
						 getVisualComponent().removeComponentListener(vListener);
						vListener = null;
					}
				}
			}
			public void contributeModelSize(org.eclipse.ve.internal.cdm.model.Rectangle modelConstraint) {
				Rectangle bounds = getVisualComponent().getBounds();
				modelConstraint.width = bounds.width;
				modelConstraint.height = bounds.height;		
			}
			public boolean isResizeable() {
				return false;
			}
			public Command contributeOrphanChildCommand() {
				return null;
			}
			public void contributeFigureSize(Rectangle figureConstraint) {
			}
			public Command contributeSizeCommand(int width, int height, EditDomain domain) {
				return null;
			}
		} 
	
	public Object getAdapter(Class type) {
		if (type == IVisualComponent.class)
			return getVisualComponent();
		else if (type == IPropertySource.class)
			return EcoreUtil.getRegisteredAdapter((IJavaObjectInstance) getModel(), IPropertySource.class);
		else if (type == IConstraintHandler.class) {
			if (constraintHandler == null) {
				constraintHandler = new ConstraintHandler();
			}
			return constraintHandler;
		}
		else if (type == IActionFilter.class)
			return getControlActionFilter();
		else if (type == IErrorHolder.class)
			return getErrorNotifier();
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

}