/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
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
 *  $Revision: 1.5 $  $Date: 2006-05-17 20:15:54 $ 
 */
package org.eclipse.ve.internal.rcp;

import java.util.*;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cdm.*;
import org.eclipse.ve.internal.cdm.model.CDMModelConstants;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.IErrorNotifier.ErrorListenerAdapter;
import org.eclipse.ve.internal.cde.core.VisualInfoPolicy.VisualInfoListener;

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
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected ContentPaneFigure getContentPaneFigure() {
		return (ContentPaneFigure) getFigure();
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
		IFigure ToolTipFig = new ToolTipContentHelper(ToolTipAssistFactory.createToolTipProcessors((IJavaInstance) getModel(),
				getErrorNotifier()));
		cfig.setToolTip(ToolTipFig);
		return cfig;
	}

	private VisualInfoListener viListener;

	private class WorkBenchPartVIListener extends VisualInfoListener {

		protected WorkBenchPartVIListener(Object model, Diagram diagram, EditDomain domain) {
			super(model, diagram, domain);
		}

		public void notifyVisualInfoChanges(Notification msg) {

			if (msg.getFeatureID(VisualInfo.class) == CDMPackage.VISUAL_INFO__KEYED_VALUES) {
				Notification kvMsg = KeyedValueNotificationHelper.notifyChanged(msg, CDMModelConstants.VISUAL_CONSTRAINT_KEY);
				if (kvMsg != null) {
					// The constraint keyedvalue was changed
					switch (kvMsg.getEventType()) {
						case Notification.SET: // It was changed.
						case Notification.UNSET: // It was removed
							queueRefresh();
							break;
					}
				}
			}

		}

		public void notifyVisualInfo(int eventType, VisualInfo oldVI, VisualInfo newVI) {
			queueRefresh();
		}
	};
	
	private void queueRefresh() {
		CDEUtilities.displayExec(this,
				"REFRESH_CONSTRAINT", new EditPartRunnable(this) { //$NON-NLS-1$
					protected void doRun() {
						Object currentConstraint = getCurrentConstraint();
						try {
							WorkbenchPartProxyAdapter wbpa = (WorkbenchPartProxyAdapter) BeanProxyUtilities
									.getBeanProxyHost((IJavaInstance) getModel());
							if (currentConstraint instanceof org.eclipse.ve.internal.cdm.model.Rectangle) {
								org.eclipse.ve.internal.cdm.model.Rectangle cRect = (org.eclipse.ve.internal.cdm.model.Rectangle) currentConstraint;
								wbpa.setWorkbenchPartMinDisplayedSize(cRect.width, cRect.height);
							} else
								wbpa.setWorkbenchPartMinDisplayedSize(XYLayoutUtility.PREFERRED_SIZE, XYLayoutUtility.PREFERRED_SIZE);
						} catch (ClassCastException e) {
							// OK. Just means constraint is not a rect or the proxy adapter is not valid.
						}
						
					}
				});
	}

	private Object getCurrentConstraint() {
		Object kv = null;
		VisualInfo vi = VisualInfoPolicy.getVisualInfo(this);
		if (vi != null) {
			kv = vi.getKeyedValues().get(CDMModelConstants.VISUAL_CONSTRAINT_KEY);
		}
		return kv;
	}


	public void activate() {
		super.activate();

		imageFigureController.setImageNotifier(getVisualComponent());

		// Listen to the error notifier so it tells us when errors occur
		fBeanProxyErrorListener = new IErrorNotifier.ErrorListenerAdapter() {

			public void errorStatusChanged() {
				CDEUtilities.displayExec(WorkbenchPartGraphicalEditPart.this,
						"STATUS_CHANGED", new EditPartRunnable(WorkbenchPartGraphicalEditPart.this) { //$NON-NLS-1$

							protected void doRun() {
								setSeverity(getErrorNotifier().getErrorStatus());
							}
						});
			}
		};

		getErrorNotifier().addErrorListener(fBeanProxyErrorListener);

		((ToolTipContentHelper) getFigure().getToolTip()).activate();
		
		EditDomain dom = EditDomain.getEditDomain(this);
		viListener = new WorkBenchPartVIListener(getModel(), dom.getDiagram(this.getRoot().getViewer()), dom);
		queueRefresh();
	}

	public void deactivate() {
		viListener.removeListening();
		viListener = null;
		
		((ToolTipContentHelper) getFigure().getToolTip()).deactivate();

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
		if (delegate_control != null) {
			result.add(delegate_control);
			return result;
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	private IJavaInstance getDelegateComposite() {
		IJavaInstance modelJI = (IJavaInstance) getModel();
		EStructuralFeature sfDelgateControl = modelJI.eClass().getEStructuralFeature(SwtPlugin.DELEGATE_CONTROL);
		return (IJavaInstance) (sfDelgateControl != null ? modelJI.eGet(sfDelgateControl) : null);
	}

	protected EditPart createChild(Object model) {
		EditPart ep = super.createChild(model);
		try {
			ControlGraphicalEditPart controlep = (ControlGraphicalEditPart) ep;
			controlep.installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, new NonResizableEditPolicy());
			controlep.installEditPolicy(EditPolicy.CONTAINER_ROLE, new WorkbenchParentArgumentEditPolicy());
			controlep.setTransparent(true); // So that it doesn't create an image, we subsume it here.
			// Create special property source adapter for the only child to return no descriptors
			controlep.setPropertySource(new WorkbenchPartChildPropertySourceAdapter());
		} catch (ClassCastException e) {
			// For the rare times that it is not a ControlGraphicalEditPart (e.g. undefined).
		}
		return ep;
	}

	protected class ConstraintHandler implements IConstraintHandler {

		private ListenerList listeners;

		private VisualComponentListener vListener;

		private class VisualComponentListener extends VisualComponentAdapter {

			public void componentRefreshed() {
				Rectangle bounds = getVisualComponent().getBounds();
				componentResized(bounds.width, bounds.height);
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
				listeners = new ListenerList(ListenerList.IDENTITY);
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
			return true;
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
		} else if (type == IActionFilter.class)
			return getControlActionFilter();
		else if (type == IErrorHolder.class)
			return getErrorNotifier();
		Object result = super.getAdapter(type);
		if (result != null) {
			return result;
		} else {
			// See if any of the MOF adapters on our target can return a value for the request
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

}
