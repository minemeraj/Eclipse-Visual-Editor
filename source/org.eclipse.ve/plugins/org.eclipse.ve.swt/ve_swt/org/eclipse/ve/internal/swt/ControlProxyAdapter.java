package org.eclipse.ve.internal.swt;

import java.util.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.jcm.*;

import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.jem.internal.proxy.core.*;

public class ControlProxyAdapter extends WidgetProxyAdapter implements IVisualComponent {
	
	protected List fControlListeners = null; // Listeners for IComponentNotification.
	protected ControlManager fControlManager; // The listener on the IDE	
	protected ImageNotifierSupport imSupport;	
	public ControlProxyAdapter parentProxyAdapter;
	public IFieldProxy environmentFreeFormHostFieldProxy;

	public ControlProxyAdapter(IBeanProxyDomain domain) {
		super(domain);				
	}
	
	protected void primInstantiateBeanProxy() {
		try {
			IBeanTypeProxy environmentBeanTypeProxy = getEnvironmentBeanTypeProxy();
			// Create the control with the constructor of its parent composite
			IJavaObjectInstance control = (IJavaObjectInstance)getTarget();
			IJavaObjectInstance composite = (IJavaObjectInstance) InverseMaintenanceAdapter.getFirstReferencedBy(control, (EReference)JavaInstantiation.getSFeature(control.eResource().getResourceSet(),SWTConstants.SF_COMPOSITE_CONTROLS)); 
			IBeanProxy compositeBeanProxy = null;
			// The parent composite either comes because we are logically owned by a composite
			if(composite != null){
				compositeBeanProxy = BeanProxyUtilities.getBeanProxy(composite);
			} else {
				// or else are on the free form
				compositeBeanProxy = getEnvironmentFreeFormHostFieldProxy().get(getEnvironmentBeanTypeProxy());
			}
			// Get the constructor to create the control, new Control(Composite,int);
			IBeanTypeProxy compositeBeanTypeProxy = domain.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.swt.widgets.Composite");
			IBeanTypeProxy intBeanTypeProxy = domain.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("int");			
			// Get the class of the control
			IBeanTypeProxy controlBeanTypeProxy = domain.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(((JavaClass)control.getJavaType()).getJavaName()); 
			// Now we have the control type and the argument types, get the constructor
			IConstructorProxy createControlProxy = controlBeanTypeProxy.getConstructorProxy(new IBeanTypeProxy[] {compositeBeanTypeProxy,intBeanTypeProxy});
			// Create a proxy for the value zero
			IBeanProxy zeroBeanProxy = domain.getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(0);
			IBeanProxy controlProxy = null;
			controlProxy = getEnvironmentInvoke2ArgConstructorProxy().invoke(getEnvironmentBeanTypeProxy(),new IBeanProxy[] {createControlProxy,compositeBeanProxy,zeroBeanProxy});
			setBeanProxy(controlProxy);							 
		} catch (ThrowableProxy exc) {
			JavaVEPlugin.log("Unable to set create the Shell Bean Proxy on the target VM");
			JavaVEPlugin.log(exc, MsgLogger.LOG_WARNING);
		}				
	}	
	
	private IFieldProxy getEnvironmentFreeFormHostFieldProxy(){
		if(environmentFreeFormHostFieldProxy == null){
			environmentFreeFormHostFieldProxy = getEnvironmentBeanTypeProxy().getFieldProxy("freeFormHost");
		}
		return environmentFreeFormHostFieldProxy;
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
		if(fControlManager == null){
			createControlManager();
		}
		return fControlManager.getClientBox();
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
					createControlManager(); // Create the control listener on the bean and add all
				}
			}
	}
	protected void createControlManager() {
		// Create an instance of com.ibm.etools.jbcf.visual.vm.ComponentManager on the target VM
		if (fControlManager == null) {
			fControlManager = new ControlManager();
			// Having created the ComponentManager in the IDE transfer all existing people listening to us
			// to the component listener
			if(fControlListeners != null){
				Iterator listeners = fControlListeners.iterator();
				while (listeners.hasNext()) {
					fControlManager.addComponentListener((IVisualComponentListener) listeners.next());
				}				
			}
		}
		fControlManager.setControlBeanProxy(getBeanProxy());
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
	public void invalidateImage() {
		// TODO Auto-generated method stub
	}
	public void refreshImage() {
		if(fControlManager == null) createControlManager();
		fControlManager.captureImage();
		imSupport.fireImageChanged(fControlManager.getImageData());
	}
	public void removeImageListener(IImageListener listener) {
		imSupport.removeImageListener(listener);
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
	}
	public void setParentProxyHost(ControlProxyAdapter aParentControlProxyAdapter){
		parentProxyAdapter = aParentControlProxyAdapter;
	}	
	
	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		// Make sure the FreeFormControlHostAdapter exists
		EObject container = ((EObject)newTarget).eContainer();
		if(container instanceof BeanComposition){
			Adapter existingAdapter = EcoreUtil.getExistingAdapter(container,FreeFormControlHostAdapter.class);		
			if(existingAdapter == null){
				FreeFormControlHostAdapter adapter = new FreeFormControlHostAdapter(domain,(BeanComposition)container);
				adapter.setTarget(container);
				container.eAdapters().add(adapter);	
				adapter.add(this);					
			}
			
		}
	}
}
