package org.eclipse.ve.internal.swt;


import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.PTExpression;
import org.eclipse.jem.internal.instantiation.ParseTreeAllocation;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;
import org.eclipse.jem.internal.proxy.swt.JavaStandardSWTBeanConstants;

import org.eclipse.ve.internal.jcm.BeanFeatureDecorator;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.BeanProxyAdapter;
import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

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
		style = -1;	// Uncache the style bit]
		explicitStyle = -1; // Uncache the explicit style bit set in source
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
	private int explicitStyle = -1;
	/**
	 * Return the style that is actuall expcitly set when this widget is constructed by us
	 */
	public int getExplicitStyle(){
		if(explicitStyle == -1){
			IBeanProxy styleBeanProxy = null;
			// Get the arguments from the source that are the style bits that are explicitly set
			if (getJavaObject().getAllocation() instanceof ParseTreeAllocation){
				PTExpression expression = ((ParseTreeAllocation)getJavaObject().getAllocation()).getExpression();
				if(expression instanceof PTClassInstanceCreation){
					PTClassInstanceCreation classInstanceCreation = (PTClassInstanceCreation)expression;
					if(classInstanceCreation.getArguments().size() == 2){
						PTExpression styleExpression = (PTExpression)classInstanceCreation.getArguments().get(1);
						try {
							styleBeanProxy = BasicAllocationProcesser.instantiateWithString(
									styleExpression.toString(), 
									getBeanProxyDomain().getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("int"));							
						} catch (AllocationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			if(styleBeanProxy == null){
				explicitStyle = SWT.NONE;
			} else {
				explicitStyle = ((IIntegerBeanProxy)styleBeanProxy).intValue();
			}
		}
		return explicitStyle;		
	}	
}
