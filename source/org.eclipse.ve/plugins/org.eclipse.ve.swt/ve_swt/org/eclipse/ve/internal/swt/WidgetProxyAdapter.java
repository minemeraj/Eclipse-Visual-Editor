package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;
import org.eclipse.jem.internal.proxy.swt.JavaStandardSWTBeanConstants;

import org.eclipse.ve.internal.jcm.BeanFeatureDecorator;

import org.eclipse.ve.internal.java.core.BeanProxyAdapter;
import org.eclipse.ve.internal.java.core.IBeanProxyDomain;

public class WidgetProxyAdapter extends BeanProxyAdapter {
	
	public WidgetProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}
	
	protected IBeanProxy primReadBeanFeature(final PropertyDecorator propDecor, final IBeanProxy aSource) throws ThrowableProxy{
		return (IBeanProxy) invokeSyncExec(new DisplayManager.DisplayRunnable() {
			public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
				return WidgetProxyAdapter.super.primReadBeanFeature(propDecor, aSource);
			}
		});
	}			
	
	protected final IBeanTypeProxy getEnvironmentBeanTypeProxy(){
		return JavaStandardSWTBeanConstants.getConstants(getBeanProxyDomain().getProxyFactoryRegistry()).getEnvironmentBeanTypeProxy();
	}
	
	protected Object invokeSyncExec(DisplayManager.DisplayRunnable runnable) throws ThrowableProxy {
		return JavaStandardSWTBeanConstants.invokeSyncExec(getBeanProxyDomain().getProxyFactoryRegistry(), runnable);
	}
	
	protected Object invokeSyncExecCatchThrowableExceptions(DisplayManager.DisplayRunnable runnable) {
		return JavaStandardSWTBeanConstants.invokeSyncExecCatchThrowableExceptions(getBeanProxyDomain().getProxyFactoryRegistry(), runnable);
	}
	
	protected void primApplyBeanFeature(final EStructuralFeature sf , final PropertyDecorator propDecor , final BeanFeatureDecorator featureDecor, final IBeanProxy settingBeanProxy) throws ThrowableProxy {
		invokeSyncExec(new DisplayManager.DisplayRunnable() {
			public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
				WidgetProxyAdapter.super.primApplyBeanFeature(sf, propDecor, featureDecor, settingBeanProxy);
				return null;
			}
		});
	}	
	
	public void releaseBeanProxy() {
		style = -1;	// Uncache the style bit
		if(isBeanProxyInstantiated()){
			invokeSyncExecCatchThrowableExceptions(new DisplayManager.DisplayRunnable() {
				public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
					IBeanProxy widgetBeanProxy = getBeanProxy();
					IMethodProxy disposeWidgetMethodProxy = widgetBeanProxy.getTypeProxy().getMethodProxy("dispose");
					disposeWidgetMethodProxy.invoke(widgetBeanProxy);
					return null;
				}
			});
		}
		super.releaseBeanProxy();
	}
	// Replace this with code that listens to the expression being applied to the allocation - JRW
	public void hackRefresh(){
		releaseBeanProxy();	
		instantiateBeanProxy();
	}

	/**
	 * @return the int style value by interrogate getStyle() on the targetVM on the correct thread
	 * 
	 * @since 1.0.0
	 */
	private int style = -1;
	public int getStyle() {
		if(isBeanProxyInstantiated() && style == -1){
			invokeSyncExecCatchThrowableExceptions(new DisplayManager.DisplayRunnable() {
				public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
					IBeanProxy widgetBeanProxy = getBeanProxy();
					IMethodProxy getStyleMethodProxy = widgetBeanProxy.getTypeProxy().getMethodProxy("getStyle");
					IIntegerBeanProxy styleBeanProxy = (IIntegerBeanProxy) getStyleMethodProxy.invoke(widgetBeanProxy);
					 style = styleBeanProxy.intValue();
					 return null;
				}
			});
		}
		return style;
	} 
}
