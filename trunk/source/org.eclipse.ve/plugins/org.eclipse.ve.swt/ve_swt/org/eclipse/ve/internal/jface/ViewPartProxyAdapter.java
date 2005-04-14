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
 *  $RCSfile: ViewPartProxyAdapter.java,v $
 *  $Revision: 1.6 $  $Date: 2005-04-14 15:56:12 $ 
 */
package org.eclipse.ve.internal.jface;

import java.util.*;
import java.util.logging.Level;

import org.eclipse.draw2d.geometry.*;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.internal.IPreferenceConstants;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.util.PrefUtil;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.*;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.swt.*;


public class ViewPartProxyAdapter extends BeanProxyAdapter implements IVisualComponent , IControlProxyHost {
	
	public static final String TARGETVM_VIEWPARTHOST = "org.eclipse.ve.internal.jface.targetvm.ViewPartHost"; //$NON-NLS-1$
	protected List fControlListeners = null; // Listeners for IComponentNotification.
	protected ControlManager fControlManager; // The listener on the IDE	
	protected ImageNotifierSupport imSupport;
	
	protected IBeanProxy viewPaneBeanProxy;
	protected IBeanProxy compositeBeanProxy;
	
	private IControlProxyHost parentProxyHost;
	protected final Object imageAccessorSemaphore = new Object();	// Semaphore for access to image	

	public ViewPartProxyAdapter(IBeanProxyDomain domain) {
		super(domain);				
	}
	
	public synchronized void addDelegateComposite(final IJavaInstance aComposite){
		
		IControlProxyHost compositeProxyHost = (IControlProxyHost) BeanProxyUtilities.getBeanProxyHost(aComposite);
		
		if(compositeProxyHost.getBeanProxy() == null){
			compositeProxyHost.setParentProxyHost(null);			
			compositeProxyHost.instantiateBeanProxy();
			compositeProxyHost.setParentProxyHost(ViewPartProxyAdapter.this);			
		} else {
			compositeProxyHost.setParentProxyHost(ViewPartProxyAdapter.this);			
		}
		
		childValidated(ViewPartProxyAdapter.this);		
		
	}
	
	protected void applied(EStructuralFeature sf,Object newValue,int position){
		if(SwtPlugin.DELEGATE_CONTROL.equals(sf.getName())){
			addDelegateComposite((IJavaInstance)newValue);
		} else {
			super.applied(sf,newValue,position);
		}		
	}
	
	public IBeanProxy getBeanPropertyProxyValue(EStructuralFeature aBeanPropertyAttribute) {

		if(SwtPlugin.DELEGATE_CONTROL.equals(aBeanPropertyAttribute.getName())){
			return compositeBeanProxy;
		} else {
			return super.getBeanPropertyProxyValue(aBeanPropertyAttribute);
		}
		
	}
	
	public void releaseBeanProxy() {
		ProxyFactoryRegistry registry = getBeanProxy().getProxyFactoryRegistry();		
		releaseBeanProxy(getBeanProxy());

		if (registry.isValid()) {
			registry.releaseProxy(viewPaneBeanProxy);
			registry.releaseProxy(compositeBeanProxy);
		}		
		viewPaneBeanProxy = null;
		compositeBeanProxy = null;
	}
	
	protected void releaseBeanProxy(final IBeanProxy aBeanProxy){
		invokeSyncExecCatchThrowableExceptions(new DisplayManager.DisplayRunnable(){
			public Object run(IBeanProxy displayProxy) throws ThrowableProxy, RunnableException { 
				// Find the target VM helper ViewPartHost class and remove the ViewPart 		
			
				IBeanTypeProxy viewPartHostTypeProxy = getBeanProxyDomain().getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(TARGETVM_VIEWPARTHOST);
				IMethodProxy removeViewPartMethodProxy = viewPartHostTypeProxy.getMethodProxy("removeViewPart","org.eclipse.ui.part.WorkbenchPart"); //$NON-NLS-1$ //$NON-NLS-2$
			    removeViewPartMethodProxy.invokeCatchThrowableExceptions(viewPartHostTypeProxy,aBeanProxy);
				
				return null;
			}			
		});
		super.releaseBeanProxy();
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
				fControlManager.setControlBeanProxy(viewPaneBeanProxy);				
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
					IBeanTypeProxy viewPartHostTypeProxy = getBeanProxyDomain().getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(TARGETVM_VIEWPARTHOST);
					IMethodProxy layoutViewPartMethodProxy = viewPartHostTypeProxy.getMethodProxy("layoutViewPart","org.eclipse.ui.part.WorkbenchPart"); //$NON-NLS-1$ //$NON-NLS-2$
				    layoutViewPartMethodProxy.invokeCatchThrowableExceptions(viewPartHostTypeProxy,getBeanProxy());					
            	}
					
            	return null;
            }
        });
        if (imSupport != null) refreshImage();	
	}
	
	protected void setupBeanProxy(IBeanProxy beanProxy) {
		ProxyFactoryRegistry registry = beanProxy.getProxyFactoryRegistry();
		// ViewPart is abstract, and a subclass of ViewPart will try to instantiate the superclass
		// Because it can't be created there is a target VM helper class "org.eclipse.ve.jface.targetvm.ConcreteViewPart" that is used
		// as the prototype
		if (fOwnsProxy && beanProxy != null && !beanProxy.getTypeProxy().isKindOf(registry.getBeanTypeProxyFactory().getBeanTypeProxy(SwtPlugin.VIEWPART_CLASSNAME))) { //$NON-NLS-1$
			IBeanProxy newProxy = null;
			try {
				newProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy(SwtPlugin.CONCRETE_VIEWPART_CLASSNAME).newInstance(); //$NON-NLS-1$
			} catch (ThrowableProxy e) {
				// This shouldn't of happened. Should always be able to instantiate the ConcreteViewPar.
				JavaVEPlugin.log(e, Level.FINE);
			}
			// Get rid of the old bean proxy and swop out iwth the one we just created
			beanProxy.getProxyFactoryRegistry().releaseProxy(beanProxy);
			beanProxy = newProxy;
			initializeViewPart(beanProxy);
			// The new bean proxy should be set as our one
			super.setupBeanProxy(beanProxy);
			if (hasImageListeners()){
				initializeControlManager();
			}
		} else {
			super.setupBeanProxy(beanProxy);
			initializeViewPart(beanProxy);
		}
		
	}
	
	protected void initializeViewPart(final IBeanProxy aBeanProxy){
		invokeSyncExecCatchThrowableExceptions(new DisplayManager.DisplayRunnable(){
			public Object run(IBeanProxy displayProxy) throws ThrowableProxy, RunnableException {
				// Now we have a ViewPart subclass, we want to get it correctly instantiated on the target VM
				// 1) org.eclipse.ve.jface.targetvm.ViewPartHost.addViewPart(WorkbenchPart,String) with the ViewPart as the argument
				//    and the second argument the name of the text to appear as the ViewPart title
			
				IBeanTypeProxy viewPartHostTypeProxy = getBeanProxyDomain().getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(TARGETVM_VIEWPARTHOST);
				// Ensure the view part host is positioned off screen and that it has the correct tab style and theme colors
				org.eclipse.swt.graphics.Point offscreen = BeanSWTUtilities.getOffScreenLocation();
				IStandardBeanProxyFactory beanProxyFactory = aBeanProxy.getProxyFactoryRegistry().getBeanProxyFactory();
				IIntegerBeanProxy intXBeanProxy = beanProxyFactory.createBeanProxyWith(offscreen.x);
				IIntegerBeanProxy intYBeanProxy = beanProxyFactory.createBeanProxyWith(offscreen.y);
				// Tab style (square or rounded)
				boolean traditionalTabStyle = PrefUtil.getAPIPreferenceStore().getBoolean(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS);
				IBooleanBeanProxy traditionalTabBeanProxy = beanProxyFactory.createBeanProxyWith(traditionalTabStyle);
				// Tab location (top or bottom)
				int tabPosition = WorkbenchPlugin.getDefault().getPreferenceStore().getInt(IPreferenceConstants.VIEW_TAB_POSITION);
				IIntegerBeanProxy tabPositionBeanProxy = beanProxyFactory.createBeanProxyWith(tabPosition);
				
				// Invoking the method setDetails(...) on the ViewPartHost that passes in the
				// details in a single method call to reduce target VM round trips (as opposed to lots of different method calls for each setting)
				IMethodProxy setDetailsMethodProxy = viewPartHostTypeProxy.getMethodProxy("setDetails",
						new IBeanTypeProxy[] {
						intXBeanProxy.getTypeProxy(),
						intYBeanProxy.getTypeProxy(),
						traditionalTabBeanProxy.getTypeProxy(),
						tabPositionBeanProxy.getTypeProxy()});
				setDetailsMethodProxy.invoke(viewPartHostTypeProxy,new IBeanProxy[] {
						intXBeanProxy,
						intYBeanProxy,
						traditionalTabBeanProxy,
						tabPositionBeanProxy});
				
				// Get the name of the type of ViewPart and use it as the title of the Tab folder
				// This is an approximation as in the final runtime it will come from the plugin.xml
				String javaTypeName = ((IJavaInstance)getTarget()).getJavaType().getName();
				IBeanProxy javaTypeNameProxy = getBeanProxyDomain().getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(javaTypeName);
				
				IMethodProxy addViewPartMethodProxy = viewPartHostTypeProxy.getMethodProxy("addViewPart", new String[] {"org.eclipse.ui.part.WorkbenchPart","java.lang.String"}); //$NON-NLS-1$ //$NON-NLS-2$ // $NON-NLS-3$
				// The method addViewPart returns a two element array typed to org.eclipse.swt.Composite
				// The first is the composite for the outer ViewPane, the second for the inner composite that is the argument to createPartControl(Composite)
				IArrayBeanProxy compositeArrayBeanProxy = (IArrayBeanProxy) addViewPartMethodProxy.invoke(viewPartHostTypeProxy,new IBeanProxy[] {aBeanProxy,javaTypeNameProxy});
				
				viewPaneBeanProxy = compositeArrayBeanProxy.get(0);
				compositeBeanProxy = compositeArrayBeanProxy.get(1);
				
				return null;
			}			
		}); 
	}
	
	public IControlProxyHost getParentProxyHost() {
		return parentProxyHost;
	}	
	
	public void setParentProxyHost(IControlProxyHost aParentProxyHost) {
		parentProxyHost = aParentProxyHost;
	}

	public IBeanProxy getVisualControlBeanProxy() {
		return viewPaneBeanProxy;
	}
	
}
