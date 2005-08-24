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
package com.ibm.jve.sample.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IBeanTypeProxy;
import org.eclipse.jem.internal.proxy.core.IMethodProxy;
import org.eclipse.jem.internal.proxy.core.ProxyFactoryRegistry;
import org.eclipse.jem.internal.proxy.core.ThrowableProxy;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;
import org.eclipse.jem.internal.proxy.swt.IControlProxyHost;
import org.eclipse.jem.internal.proxy.swt.JavaStandardSWTBeanConstants;
import org.eclipse.jem.internal.proxy.swt.DisplayManager.DisplayRunnable.RunnableException;
import org.eclipse.ve.internal.cde.core.IImageListener;
import org.eclipse.ve.internal.cde.core.IVisualComponent;
import org.eclipse.ve.internal.cde.core.IVisualComponentListener;
import org.eclipse.ve.internal.cde.core.ImageNotifierSupport;
import org.eclipse.ve.internal.java.core.BeanProxyAdapter;
import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.swt.ControlManager;
import org.eclipse.ve.internal.swt.SwtPlugin;

public class UIContainerProxyAdapter extends BeanProxyAdapter implements IVisualComponent , IControlProxyHost {

	protected static final String UICONTAINER_HOST = "com.ibm.jve.sample.internal.vm.UIContainerHost";
	private IBeanProxy compositeArgumentProxy;	
	
	protected List fControlListeners = null; // Listeners for IComponentNotification.
	protected ControlManager fControlManager; // The listener on the IDE	
	protected ImageNotifierSupport imSupport;
	private IControlProxyHost parentProxyHost;
	private static final String UICONTAINER_CLASSNAME = "com.ibm.jve.sample.core.UIContainer";
	private static final String CONCRETE_UICONTAINER = "com.ibm.jve.sample.internal.vm.ConcreteUIContainer";	

	public UIContainerProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}
	
	
	protected void setupBeanProxy(IBeanProxy beanProxy) {	
		
		// UIContainer is abstract, and a subclass of UIContainer will try to instantiate the superclass
		// Because it can't be created there is a target VM helper class "com.ibm.jve.sample.internal.vm.ConcreteUIContainer" that is used
		// as the prototype		
		ProxyFactoryRegistry registry = beanProxy.getProxyFactoryRegistry();		
		if (fOwnsProxy && beanProxy != null && !beanProxy.getTypeProxy().isKindOf(registry.getBeanTypeProxyFactory().getBeanTypeProxy(UICONTAINER_CLASSNAME))) { //$NON-NLS-1$			
			IBeanProxy newProxy = null;
			try {
				newProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy(CONCRETE_UICONTAINER).newInstance(); //$NON-NLS-1$
			} catch (ThrowableProxy e) {
				// This shouldn't of happened. Should always be able to instantiate the ConcreteUIContainer
				JavaVEPlugin.log(e, Level.FINE);
			}
			// Get rid of the old bean proxy and swop out iwth the one we just created
			beanProxy.getProxyFactoryRegistry().releaseProxy(beanProxy);
			beanProxy = newProxy;
			initializeUIContainer(beanProxy);
			// The new bean proxy should be set as our one
			super.setupBeanProxy(beanProxy);
			if (hasImageListeners()){
				initializeControlManager();
			}		
		} else {
			super.setupBeanProxy(beanProxy);
			initializeUIContainer(beanProxy);
		}
	}

	private void initializeUIContainer(final IBeanProxy beanProxy) {
		invokeSyncExecCatchThrowableExceptions(new DisplayManager.DisplayRunnable(){
			public Object run(IBeanProxy displayProxy) throws ThrowableProxy, RunnableException {
				
				// To create the UIComposite requires calling the method
				// com.ibm.jve.sample.internal.vm.UIContainerHost.addContainerHost(UIContainer);
				IBeanTypeProxy uiContainerHost = getBeanProxyDomain().getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(UICONTAINER_HOST);				
				IMethodProxy addContainerHostMethodProxy = uiContainerHost.getMethodProxy("addContainerHost","com.ibm.jve.sample.core.UIContainer");
				compositeArgumentProxy = addContainerHostMethodProxy.invoke(uiContainerHost,beanProxy);
				return null;
				
			}
		});
	}	
	
	public void releaseBeanProxy() {
		
	final IBeanProxy beanProxy = getBeanProxy();
	if(beanProxy != null){
		invokeSyncExecCatchThrowableExceptions(new DisplayManager.DisplayRunnable(){
			public Object run(IBeanProxy displayProxy) throws ThrowableProxy, RunnableException { 
					// Find the target VM helper UIContainerHost class and remove the UIContainer 							
					IBeanTypeProxy uiContainerHostTypeProxy = getBeanProxyDomain().getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(UICONTAINER_HOST);
					IMethodProxy removeUIContainerMethodProxy = uiContainerHostTypeProxy.getMethodProxy("removeContainerHost","com.ibm.jve.sample.core.UIContainer"); //$NON-NLS-1$ //$NON-NLS-2$
				    removeUIContainerMethodProxy.invokeCatchThrowableExceptions(uiContainerHostTypeProxy,beanProxy);
					return null;
				}			
			});
			super.releaseBeanProxy();
		}		
	}
		
	protected Object invokeSyncExecCatchThrowableExceptions(DisplayManager.DisplayRunnable runnable)  {
		return JavaStandardSWTBeanConstants.invokeSyncExecCatchThrowableExceptions(getBeanProxyDomain().getProxyFactoryRegistry(), runnable);
	}	
	
	public Rectangle getBounds() {
		if(fControlManager == null){
			initializeControlManager();
		}
		return fControlManager.getBounds();
	}
	public Point getLocation() {
		if(fControlManager == null){
			initializeControlManager();
		}
		return fControlManager.getLocation();

	}
	public Dimension getSize() {
		if(fControlManager == null){
			initializeControlManager();
		}
		return fControlManager.getSize();
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
				fControlManager.setControlBeanProxy(compositeArgumentProxy);				
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
	
	public void invalidateImage() {		
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
	public void childValidated(IControlProxyHost childProxy) {
        // We are the top with no parents, do a layout() on us
		invokeSyncExecCatchThrowableExceptions(new DisplayManager.DisplayRunnable() {
	
            public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
            	// Not sure why the bean proxy is null here but need to check for it
            	if (getBeanProxy() != null) {
            		// Call the layout() method on the target VM's					
					IBeanTypeProxy uiContainerHostTypeProxy = getBeanProxyDomain().getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(UICONTAINER_HOST);
					IMethodProxy layoutMethodProxy = uiContainerHostTypeProxy.getMethodProxy("layoutUIContainerComposite","org.eclipse.ve.widgets.Composite"); //$NON-NLS-1$ //$NON-NLS-2$
				    layoutMethodProxy.invokeCatchThrowableExceptions(uiContainerHostTypeProxy,compositeArgumentProxy);					
            	}					
            	return null;
            }
        });
        if (imSupport != null) refreshImage();	
	}
	
	public IControlProxyHost getParentProxyHost() {
		return parentProxyHost;
	}	
	
	public void setParentProxyHost(IControlProxyHost aParentProxyHost) {
		parentProxyHost = aParentProxyHost;
	}

	public IBeanProxy getVisualControlBeanProxy() {
		return compositeArgumentProxy;
	}	
	
	
}
