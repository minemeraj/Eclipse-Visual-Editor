package org.eclipse.ve.internal.swt;

import java.util.*;

import org.eclipse.draw2d.geometry.*;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.jcm.BeanComposition;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

public class ControlProxyAdapter extends WidgetProxyAdapter implements IVisualComponent {
	
	protected List fControlListeners = null; // Listeners for IComponentNotification.
	protected ControlManager fControlManager; // The listener on the IDE	
	protected ImageNotifierSupport imSupport;	
	public IMethodProxy environmentFreeFormHostMethodProxy;
	protected CompositeProxyAdapter parentProxyAdapter;
	protected EReference sf_layoutData;

	public ControlProxyAdapter(IBeanProxyDomain domain) {
		super(domain);				
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());
		sf_layoutData = JavaInstantiation.getReference(rset, SWTConstants.SF_CONTROL_LAYOUTDATA);
	}
	
	/*
	 * Use to call BeanProxyAdapter's beanProxyAllocation.
	 */
	protected IBeanProxy beanProxyAdapterBeanProxyAllocation(JavaAllocation allocation) throws AllocationException {
		return super.beanProxyAllocation(allocation);
	}
	
	/*
	 * The initString is evaluated using a static method on the Environment target VM class
	 * that ensures it is evaluated on the Display thread
	 */
	protected IBeanProxy basicInitializationStringAllocation(final String aString, final IBeanTypeProxy targetClass) throws IAllocationProcesser.AllocationException {
		try {
			Object result = invokeSyncExec(new DisplayManager.DisplayRunnable() {
				public Object run(IBeanProxy displayProxy) throws ThrowableProxy, RunnableException {
					try {
						return ControlProxyAdapter.super.basicInitializationStringAllocation(aString, targetClass);
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
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#beanProxyAllocation(org.eclipse.jem.internal.instantiation.JavaAllocation)
	 */
	protected IBeanProxy beanProxyAllocation(final JavaAllocation allocation) throws AllocationException {
		try {
			Object result = invokeSyncExec(new DisplayManager.DisplayRunnable() {
				public Object run(IBeanProxy displayProxy) throws ThrowableProxy, RunnableException {
					// TODO Need a better way to get a parent in if parent is null. (i.e. on
					// freeform. Then we can use
					// the standard allocation mechanism.
					// Create the control with the constructor of its parent composite
					IJavaObjectInstance control = (IJavaObjectInstance) getTarget();
					IJavaObjectInstance composite = getParentComposite(control);
					IBeanProxy compositeBeanProxy = null;
					// The parent composite either comes because we are logically owned by a
					// composite
					if (composite != null) {
						try {
							return beanProxyAdapterBeanProxyAllocation(allocation);
						} catch (AllocationException e) {
							throw new RunnableException(e);
						}
					} else {
						// or else are on the free form
						compositeBeanProxy = getEnvironmentFreeFormHostMethodProxy().invoke(null);
					}
					// Get the constructor to create the control, new Control(Composite,int);
					IBeanTypeProxy compositeBeanTypeProxy =
						getBeanProxyDomain().getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(
							"org.eclipse.swt.widgets.Composite");
					IBeanTypeProxy intBeanTypeProxy =
						getBeanProxyDomain().getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("int");
					// Get the class of the control
					IBeanTypeProxy controlBeanTypeProxy =
						getBeanProxyDomain().getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(
							((JavaClass) control.getJavaType()).getJavaName());
					// Now we have the control type and the argument types, get the constructor
					IConstructorProxy createControlProxy =
						controlBeanTypeProxy.getConstructorProxy(new IBeanTypeProxy[] { compositeBeanTypeProxy, intBeanTypeProxy });
					// Create a proxy for the value zero
					IBeanProxy zeroBeanProxy = getBeanProxyDomain().getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(0);
					return createControlProxy.newInstance(new IBeanProxy[] { compositeBeanProxy, zeroBeanProxy });
				}
			});
			return (IBeanProxy) result;
		} catch (ThrowableProxy e) {
			throw new AllocationException(e);
		} catch (DisplayManager.DisplayRunnable.RunnableException e) {
			throw (AllocationException) e.getCause();	// We know it is an allocation exception because that is the only runnable exception we throw.
		}
	}
	
	protected IJavaObjectInstance getParentComposite(IJavaObjectInstance control) {
		return (IJavaObjectInstance) InverseMaintenanceAdapter.getFirstReferencedBy(
			control,
			(EReference) JavaInstantiation.getSFeature(
				control.eResource().getResourceSet(),
				SWTConstants.SF_COMPOSITE_CONTROLS));		
	}

	private IMethodProxy getEnvironmentFreeFormHostMethodProxy(){
		if(environmentFreeFormHostMethodProxy == null){
			environmentFreeFormHostMethodProxy = getEnvironmentBeanTypeProxy().getMethodProxy("getFreeFormHost");
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
	 * Return the rectangle that defines the box of the client area in a coordinate system
	 * from the bounds of the control.  This is different to Composite.getClientArea() that is at
	 * 0,0,width,height as this always returns the client area origin as 0,0
	 * getClientBox() returns x and y as being the corner of the client area as offset from the bounds location
	 * so it's usually 2,2 if there is SWT.BORDER, or could be 4,29 for a shell with trim
	 */
	public Rectangle getClientBox(){
		initializeControlManager();
		if (fControlManager != null)
			return fControlManager.getClientBox();
		else
			return new Rectangle(0,0,0,0);
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
			// Create an instance of com.ibm.etools.jbcf.visual.vm.ComponentManager on the target VM
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
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#setupBeanProxy(org.eclipse.jem.internal.proxy.core.IBeanProxy)
	 */
	protected void setupBeanProxy(IBeanProxy beanProxy) {
		super.setupBeanProxy(beanProxy);
		initializeControlManager();
		// TODO This needs to be queued so that in the situation where a composite
		// does a recycle of a number of controls there is just a single refresh 
		if (imSupport != null) refreshImage();		
	}
	public void invalidateImage() {
		// TODO Auto-generated method stub
	}
	public void refreshImage() {
		initializeControlManager();
		if (fControlManager != null) {
			fControlManager.captureImage();
			imSupport.fireImageChanged(fControlManager.getImageData());
		}
	}
	public void removeImageListener(IImageListener listener) {
		imSupport.removeImageListener(listener);
	}
 	public void releaseBeanProxy() {
		if (fControlManager != null) fControlManager.release(); 		
		super.releaseBeanProxy();
		fControlManager = null;
	}
 	
 	protected void primReinstantiateBeanProxy() {
 		// If we are owned by a composite this must re-create us so that we are re-inserted in the correct position
 		if(parentProxyAdapter != null){
 			parentProxyAdapter.reinstantiateChild(this);
 			return;
 		}
 		super.primReinstantiateBeanProxy();
 	}
 	
	
	public void validateBeanProxy(){
		super.validateBeanProxy();
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				if (isBeanProxyInstantiated()) {
					
					// Still live at when invoked later.
					// Go up the chain and find all image listeners.
					List allImageListeners = new ArrayList(5);
					ControlProxyAdapter nextParentBean = parentProxyAdapter;
					ControlProxyAdapter parentBean = ControlProxyAdapter.this;
					if (parentBean.hasImageListeners())
						allImageListeners.add(parentBean);
					while (nextParentBean != null) {
						parentBean = nextParentBean;
						if (parentBean.hasImageListeners())
							allImageListeners.add(parentBean);
						nextParentBean = parentBean.parentProxyAdapter;
					}
					// Now refresh all of the components that notify.
					Iterator listeners = allImageListeners.iterator();
					while (listeners.hasNext()) {
						((ControlProxyAdapter) listeners.next()).refreshImage();
					}
				}
			}
		});
		childValidated(this);		
	}
	
	public void childValidated(ControlProxyAdapter childProxy) {
		parentProxyAdapter.childValidated(childProxy);
	}
		
	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		// Make sure the FreeFormControlHostAdapter exists
		EObject container = ((EObject)newTarget).eContainer();
		if(container instanceof BeanComposition){
			Adapter existingAdapter = EcoreUtil.getExistingAdapter(container,FreeFormControlHostAdapter.class);		
			if(existingAdapter == null){
				FreeFormControlHostAdapter adapter = new FreeFormControlHostAdapter(getBeanProxyDomain(),(BeanComposition)container);
				adapter.setTarget(container);
				container.eAdapters().add(adapter);	
				adapter.add(this);					
			}
			
		}
	}

	public void setParentProxyHost(CompositeProxyAdapter adapter) {
		parentProxyAdapter = adapter;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#canceled(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int)
	 * We need to apply null to the layout data when it's cancelled to prevent ClassCastExceptions
	 * caused by a mismatch of the wrong layoutData with a specific layout (e.g GridData on a RowLayout).
	 *  
	 * Note: The Layout switcher is responsible to cancel or set the layoutdata for each of the controls.
	 */
	protected void canceled(EStructuralFeature sf, Object oldValue, int position) {
		if (sf == sf_layoutData) {
			applyBeanPropertyProxyValue(sf, null);
		} else {
			super.canceled(sf, oldValue, position);
		}
	}
}
