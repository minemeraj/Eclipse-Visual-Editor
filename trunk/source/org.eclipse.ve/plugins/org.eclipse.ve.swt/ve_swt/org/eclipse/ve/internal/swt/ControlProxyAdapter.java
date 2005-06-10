/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;

import java.util.*;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;
import org.eclipse.jem.internal.proxy.swt.IControlProxyHost;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.jcm.BeanComposition;
import org.eclipse.ve.internal.jcm.JCMPackage;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.visual.RectangleJavaClassCellEditor;

public class ControlProxyAdapter extends WidgetProxyAdapter implements IVisualComponent, IControlProxyHost {

	protected List fControlListeners = null; // Listeners for IComponentNotification.

	protected ControlManager fControlManager; // The listener on the IDE

	protected ImageNotifierSupport imSupport;

	public IMethodProxy environmentFreeFormHostMethodProxy;

	protected IControlProxyHost parentProxyAdapter;

	protected EReference sf_layoutData;

	protected EStructuralFeature sfComponentBounds, sfComponentLocation, sfComponentSize;

	public ControlProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());
		sf_layoutData = JavaInstantiation.getReference(rset, SWTConstants.SF_CONTROL_LAYOUTDATA);
		sfComponentBounds = JavaInstantiation.getSFeature(rset, SWTConstants.SF_CONTROL_BOUNDS);
		sfComponentLocation = JavaInstantiation.getSFeature(rset, SWTConstants.SF_CONTROL_LOCATION);
		sfComponentSize = JavaInstantiation.getSFeature(rset, SWTConstants.SF_CONTROL_SIZE);
	}

	/*
	 * Use to call BeanProxyAdapter's beanProxyAllocation.
	 */
	protected IBeanProxy beanProxyAdapterBeanProxyAllocation(JavaAllocation allocation) throws AllocationException {
		return super.beanProxyAllocation(allocation);
	}

	protected IBeanProxy beanProxyAdapterInitializationStringAllocation(String aString, IBeanTypeProxy targetClass) throws AllocationException {
		return super.basicInitializationStringAllocation(aString, targetClass);
	}

	/*
	 * The initString is evaluated using a static method on the Environment target VM class that ensures it is evaluated on the Display thread
	 */
	protected IBeanProxy basicInitializationStringAllocation(final String aString, final IBeanTypeProxy targetClass)
			throws IAllocationProcesser.AllocationException {
		try {
			Object result = invokeSyncExec(new DisplayManager.DisplayRunnable() {

				public Object run(IBeanProxy displayProxy) throws ThrowableProxy, RunnableException {
					try {
						if (aString != null)
							return ControlProxyAdapter.super.basicInitializationStringAllocation(aString, targetClass);

						// We are doing subclassing if the string is null.
						// Get FF host as parent.
						org.eclipse.swt.graphics.Point offscreen = BeanSWTUtilities.getOffScreenLocation();
						IIntegerBeanProxy intXBeanProxy = displayProxy.getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(
								offscreen.x);
						IIntegerBeanProxy intYBeanProxy = displayProxy.getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(
								offscreen.y);
						IBeanProxy parentBeanProxy = getEnvironmentFreeFormHostMethodProxy().invoke(null,
								new IBeanProxy[] { intXBeanProxy, intYBeanProxy});

						// Get the constructor to create the control, new Control(Composite,int);
						// First get the arg types.
						IBeanTypeProxy compositeBeanTypeProxy = getBeanProxyDomain().getProxyFactoryRegistry().getBeanTypeProxyFactory()
								.getBeanTypeProxy("org.eclipse.swt.widgets.Composite"); //$NON-NLS-1$

						IBeanTypeProxy intBeanTypeProxy = getBeanProxyDomain().getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(
								"int"); //$NON-NLS-1$

						// Now we have the target type and the argument types, get the constructor
						IConstructorProxy createControlProxy = targetClass.getConstructorProxy(new IBeanTypeProxy[] { compositeBeanTypeProxy,
								intBeanTypeProxy});
						// Create a proxy for the value zero
						IBeanProxy zeroBeanProxy = getBeanProxyDomain().getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(0);
						return createControlProxy.newInstance(new IBeanProxy[] { parentBeanProxy, zeroBeanProxy});

					} catch (AllocationException e) {
						throw new RunnableException(e);
					}
				}
			});
			return (IBeanProxy) result;
		} catch (ThrowableProxy e) {
			throw new AllocationException(e);
		} catch (DisplayManager.DisplayRunnable.RunnableException e) {
			throw (AllocationException) e.getCause();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#beanProxyAllocation(org.eclipse.jem.internal.instantiation.JavaAllocation)
	 */
	protected IBeanProxy beanProxyAllocation(final JavaAllocation allocation) throws AllocationException {
		try {
			Object result = invokeSyncExec(new DisplayManager.DisplayRunnable() {

				public Object run(IBeanProxy displayProxy) throws ThrowableProxy, RunnableException {
					try {
						return beanProxyAdapterBeanProxyAllocation(allocation);
					} catch (AllocationException e) {
						throw new RunnableException(e);
					}
				}
			});
			return (IBeanProxy) result;
		} catch (ThrowableProxy e) {
			throw new AllocationException(e);
		} catch (DisplayManager.DisplayRunnable.RunnableException e) {
			throw (AllocationException) e.getCause(); // We know it is an allocation exception because that is the only runnable exception we throw.
		}
	}

	protected IJavaObjectInstance getParentComposite(IJavaObjectInstance control) {
		return (IJavaObjectInstance) InverseMaintenanceAdapter.getFirstReferencedBy(control, (EReference) JavaInstantiation.getSFeature(control
				.eResource().getResourceSet(), SWTConstants.SF_COMPOSITE_CONTROLS));
	}

	private IMethodProxy getEnvironmentFreeFormHostMethodProxy() {
		if (environmentFreeFormHostMethodProxy == null) {
			environmentFreeFormHostMethodProxy = getEnvironmentBeanTypeProxy().getMethodProxy("getFreeFormHost", new String[] { "int", "int"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return environmentFreeFormHostMethodProxy;
	}

	public Rectangle getBounds() {
		if (fControlManager != null) {
			return fControlManager.getBounds();
		} else {
			// No proxy. Either called too soon, or there was an instantiation error and we can't get
			// a live component. So return just a default.
			return new Rectangle(0, 0, 0, 0);
		}

	}

	/**
	 * Return the rectangle that defines the box of the client area in a coordinate system from the bounds of the control. This is different to
	 * Composite.getClientArea() that is at 0,0,width,height as this always returns the client area origin as 0,0 getClientBox() returns x and y as
	 * being the corner of the client area as offset from the bounds location so it's usually 2,2 if there is SWT.BORDER, or could be 4,29 for a shell
	 * with trim
	 */
	public Rectangle getClientBox() {
		initializeControlManager();
		if (fControlManager != null)
			return fControlManager.getClientBox();
		else
			return new Rectangle(0, 0, 0, 0);
	}

	public Point getLocation() {
		if (fControlManager != null) {
			return fControlManager.getLocation();
		} else {
			// No proxy. Either called too soon, or there was an instantiation error and we can't get
			// a live component. So return just a default.
			return new Point(0, 0);
		}
	}

	public Dimension getSize() {
		if (fControlManager != null) {
			return fControlManager.getSize();
		} else {
			// No proxy. Either called too soon, or there was an instantiation error and we can't get
			// a live component. So return just a default.
			return new Dimension(0, 0);
		}
	}

	public void addComponentListener(IVisualComponentListener aListener) {
		if (fControlListeners == null)
			fControlListeners = new ArrayList(1);

		fControlListeners.add(aListener);
		if (fControlManager != null) {
			fControlManager.addComponentListener(aListener);
		} else {
			if (getBeanProxy() != null && getBeanProxy().isValid()) {
				initializeControlManager(); // Create the control listener on the bean and add all
			}
		}
	}

	protected void initializeControlManager() {
		if (isBeanProxyInstantiated()) {
			// Create an instance of ComponentManager on the target VM
			if (fControlManager == null) {
				fControlManager = new ControlManager();
				// Having created the ComponentManager in the IDE transfer all existing people listening to us
				// to the component listener
				if (fControlListeners != null) {
					Iterator listeners = fControlListeners.iterator();
					while (listeners.hasNext()) {
						fControlManager.addComponentListener((IVisualComponentListener) listeners.next());
					}
				}
				fControlManager.setControlBeanProxy(getBeanProxy());
			}
		}

	}

	public synchronized void removeComponentListener(IVisualComponentListener aListener) {
		// Remove from the local list and the proxy list.
		fControlListeners.remove(aListener);
		if (fControlManager != null) {
			fControlManager.removeComponentListener(aListener);
		}
	}

	public synchronized void addImageListener(IImageListener aListener) {
		if (imSupport == null)
			imSupport = new ImageNotifierSupport();
		imSupport.addImageListener(aListener);
	}

	public boolean hasImageListeners() {
		return (imSupport != null && imSupport.hasImageListeners());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#setupBeanProxy(org.eclipse.jem.internal.proxy.core.IBeanProxy)
	 */
	protected void setupBeanProxy(IBeanProxy beanProxy) {
		super.setupBeanProxy(beanProxy);
		initializeControlManager();
		// TODO This needs to be queued so that in the situation where a composite
		// does a recycle of a number of controls there is just a single refresh
		if (hasImageListeners())
			refreshImage();
	}

	public void invalidateImage() {
		// TODO Auto-generated method stub
	}

	public void refreshImage() {
		initializeControlManager();
		if (fControlManager != null) {
			getModelChangeController().execAtEndOfTransaction(new Runnable() {

				public void run() {
					if (fControlManager!=null) { 
					  // We were not disposed by the time we got here 
					  fControlManager.captureImage();
					  imSupport.fireImageChanged(fControlManager.getImageData());
					}
				}
			}, ModelChangeController.createHashKey(this, "image"));

		}
	}

	public void removeImageListener(IImageListener listener) {
		imSupport.removeImageListener(listener);
	}

	public void releaseBeanProxy() {
		if (fControlManager != null)
			fControlManager.release();
		super.releaseBeanProxy();
		fControlManager = null;
	}

	protected void primReinstantiateBeanProxy() {
		// If we are owned by a composite this must re-create us so that we are re-inserted in the correct position
		if (parentProxyAdapter != null) {
			parentProxyAdapter.reinstantiateChild(this);
			return;
		}
		super.primReinstantiateBeanProxy();
	}

	protected ModelChangeController getModelChangeController() {
		return (ModelChangeController) getBeanProxyDomain().getEditDomain().getData(ModelChangeController.MODEL_CHANGE_CONTROLLER_KEY);
	}

	public void validateBeanProxy() {
		super.validateBeanProxy();
		if (isBeanProxyInstantiated()) {

			// Still live at when invoked later.
			// Go up the chain and find all image listeners.
			List allImageListeners = new ArrayList(5);
			IVisualComponent nextParentBean = (IVisualComponent) parentProxyAdapter;
			IVisualComponent parentBean = ControlProxyAdapter.this;
			if (parentBean.hasImageListeners())
				allImageListeners.add(parentBean);
			while (nextParentBean != null) {
				parentBean = nextParentBean;
				if (parentBean.hasImageListeners()) {
					allImageListeners.add(parentBean);
				}
				nextParentBean = (IVisualComponent) ((IControlProxyHost) parentBean).getParentProxyHost();
			}
			// Now refresh all of the components that notify.
			Iterator listeners = allImageListeners.iterator();
			while (listeners.hasNext()) {
				((IVisualComponent) listeners.next()).refreshImage();
			}
		}
		childValidated(this);
	}

	public void childValidated(IControlProxyHost childProxy) {
		if (parentProxyAdapter != null) {
			parentProxyAdapter.childValidated(childProxy);
		}
	}

	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		// See whether or not we are on the free form
		if (newTarget != null) {
			EObject beanComposition = InverseMaintenanceAdapter.getFirstReferencedBy(newTarget, JCMPackage.eINSTANCE.getBeanComposition_Components());
			if (beanComposition != null) {
				Adapter existingAdapter = EcoreUtil.getExistingAdapter(beanComposition, FreeFormControlHostAdapter.class);
				if (existingAdapter == null) {
					FreeFormControlHostAdapter adapter = new FreeFormControlHostAdapter(getBeanProxyDomain(), (BeanComposition) beanComposition);
					adapter.setTarget(beanComposition);
					beanComposition.eAdapters().add(adapter);
					adapter.add(this);
				}

			}			
			
		}
	}

	public void setParentProxyHost(IControlProxyHost adapter) {
		parentProxyAdapter = adapter;
		if (fControlManager != null) {
			fControlManager.setControlParentBeanProxy(parentProxyAdapter != null ? parentProxyAdapter.getVisualControlBeanProxy() : null);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#canceled(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int) We need to
	 *      apply null to the layout data when it's cancelled to prevent ClassCastExceptions caused by a mismatch of the wrong layoutData with a
	 *      specific layout (e.g GridData on a RowLayout).
	 * 
	 * Note: The Layout switcher is responsible to cancel or set the layoutdata for each of the controls.
	 */
	protected void canceled(EStructuralFeature sf, Object oldValue, int position) {
		if (sf == sf_layoutData) {
			applyBeanPropertyProxyValue(sf, null);
		} else {
			if (sf == sfComponentBounds && (getJavaObject().eIsSet(sfComponentLocation) || getJavaObject().eIsSet(sfComponentSize)))
				return;	// Don't cancel because it will wipe out the location or size.
			if (sf == sfComponentSize && getJavaObject().eIsSet(sfComponentBounds))
				return;	// Don't cancel because it will wipe out the bounds
			if (sf == sfComponentLocation && getJavaObject().eIsSet(sfComponentBounds))
				return;	// Don't cancel because it will wipe out the bounds			
			super.canceled(sf, oldValue, position);
		}
	}

	protected void applied(EStructuralFeature as, Object newValue, int position) {
		// If the allocation is being applied then we must try to instantiate - bugzilla 91519
		if (!isBeanProxyInstantiated() && !isInstantiationFeature(as))
			return; // Nothing to apply to yet or could not construct.
		if (as == sfComponentBounds)
			appliedBounds(as, newValue, position); // Handle bounds
		else
			super.applied(as, newValue, position); // We letting the settings go through
	}

	protected void appliedBounds(final EStructuralFeature as, Object newValue, int position) {
		IRectangleBeanProxy rect = (IRectangleBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaInstance) newValue);
		if (rect != null
				&& (rect.getWidth() == -1 || rect.getHeight() == -1 || (rect.getX() == Integer.MIN_VALUE && rect.getY() == Integer.MIN_VALUE))) {
			Rectangle bounds = new Rectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

			ResourceSet rset = JavaEditDomainHelper.getResourceSet(getBeanProxyDomain().getEditDomain());
			IJavaInstance inst = BeanUtilities.createJavaObject("int", rset, String.valueOf(-1)); //$NON-NLS-1$
			IIntegerBeanProxy defval = (IIntegerBeanProxy) BeanProxyUtilities.getBeanProxy(inst);

			IJavaObjectInstance control = (IJavaObjectInstance) getTarget();
			IJavaObjectInstance composite = getParentComposite(control);
			if (NullLayoutEditPolicy.adjustForPreferredSizeAndPosition(getBeanProxy(), composite, bounds, 15, 5, defval, defval)) {
				String initString = RectangleJavaClassCellEditor.getJavaInitializationString(bounds, SWTConstants.RECTANGLE_CLASS_NAME);
				final IJavaInstance rectBean = BeanUtilities.createJavaObject(SWTConstants.RECTANGLE_CLASS_NAME, ((EObject) target).eResource()
						.getResourceSet(), initString);//$NON-NLS-1$
				Display.getDefault().asyncExec(new Runnable() {

					/**
					 * @see java.lang.Runnable#run()
					 */
					public void run() {
						// We may not be within the context of a change control, so we need to get a controller to handle the change.
						getModelChangeController().doModelChanges(new Runnable() {

							public void run() {
								// Set the constraints on the component bean. This will change the size of the component
								// Because we will be called back with notify and apply the constraints rectangle to the live bean
								//
								// Note: Need to use RuledCommandBuilder.
								RuledCommandBuilder cbld = new RuledCommandBuilder(getBeanProxyDomain().getEditDomain());
								cbld.applyAttributeSetting((EObject) target, as, rectBean);
								cbld.getCommand().execute();
							}
						}, true);
					}
				});
				return; // Let the notify back from the set here do the actual apply.
			}
		}
		super.applied(as, newValue, position);
	}

	public IControlProxyHost getParentProxyHost() {
		return parentProxyAdapter;
	}

	public IBeanProxy getVisualControlBeanProxy() {
		return getBeanProxy();
	}

	public Point getAbsoluteLocation() {
		if (fControlManager != null) {
			return fControlManager.getAbsoluteLocation();
		} else {
			// No proxy. Either called too soon, or there was an instantiation error and we can't get
			// a live component. So return just a default.
			return new Point(0, 0);
		}
	}
}
