package org.eclipse.ve.internal.swt;


import java.util.logging.Level;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;
import org.eclipse.jem.internal.proxy.swt.JavaStandardSWTBeanConstants;
import org.eclipse.jem.internal.proxy.swt.DisplayManager.DisplayRunnable.RunnableException;
import org.eclipse.jem.java.JavaHelpers;

import org.eclipse.ve.internal.jcm.BeanFeatureDecorator;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

public class WidgetProxyAdapter extends BeanProxyAdapter {
	
	public WidgetProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}
	
	protected IBeanProxy primReadBeanFeature(final PropertyDecorator propDecor, final IBeanProxy aSource) throws ThrowableProxy{
		return (IBeanProxy) invokeSyncExecCatchRunnable(new DisplayManager.DisplayRunnable() {
			public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
				return WidgetProxyAdapter.super.primReadBeanFeature(propDecor, aSource);
			}
		});
	}			
	
	protected final IBeanTypeProxy getEnvironmentBeanTypeProxy(){
		return JavaStandardSWTBeanConstants.getConstants(getBeanProxyDomain().getProxyFactoryRegistry()).getEnvironmentBeanTypeProxy();
	}
	
	protected Object invokeSyncExec(DisplayManager.DisplayRunnable runnable) throws ThrowableProxy, RunnableException {
		return JavaStandardSWTBeanConstants.invokeSyncExec(getBeanProxyDomain().getProxyFactoryRegistry(), runnable);
	}
	
	/**
	 * Invoke sync exec on the runnable, but catch any runnable exceptions that occurred in the runnable and just log them.
	 * This should be used for simple ones where you know you don't throw any RunnableExceptions.
	 *  
	 * @param runnable
	 * @return
	 * @throws ThrowableProxy
	 * 
	 * @since 1.0.0
	 */
	protected Object invokeSyncExecCatchRunnable(DisplayManager.DisplayRunnable runnable) throws ThrowableProxy {
		try {
			return JavaStandardSWTBeanConstants.invokeSyncExec(getBeanProxyDomain().getProxyFactoryRegistry(), runnable);
		} catch (RunnableException e) {
			SwtPlugin.getDefault().getLogger().log(e.getCause(), Level.WARNING);	// Only log the runnable exceptions.
		}
		return null;
	}	
	
	/**
	 * Invoke the runnable on the runnable and catch all RunnableExceptions and ThrowableProxy exceptions and just log them.
	 * @param runnable
	 * @return
	 * 
	 * @since 1.0.0
	 */
	protected Object invokeSyncExecCatchThrowableExceptions(DisplayManager.DisplayRunnable runnable) {
		return JavaStandardSWTBeanConstants.invokeSyncExecCatchThrowableExceptions(getBeanProxyDomain().getProxyFactoryRegistry(), runnable);
	}
	
	protected void primApplyBeanFeature(final EStructuralFeature sf , final PropertyDecorator propDecor , final BeanFeatureDecorator featureDecor, final IBeanProxy settingBeanProxy) throws ThrowableProxy {
		invokeSyncExecCatchRunnable(new DisplayManager.DisplayRunnable() {
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
					int numberOfArguments = classInstanceCreation.getArguments().size();
					PTExpression styleExpression = null;
					// Shell has a single argument for style
					if (numberOfArguments == 1 ) {
						JavaHelpers shell = Utilities.getJavaClass("org.eclipse.swt.widgets.Shell", ((IJavaObjectInstance) target).eResource().getResourceSet());
						if (shell.isAssignableFrom(((IJavaObjectInstance) target).getJavaType())) {
								// Shell that already have a style argument
							styleExpression = (PTExpression) classInstanceCreation.getArguments().get(0);		
						}
					}
				    else if (numberOfArguments == 2)
				    	styleExpression = (PTExpression) classInstanceCreation.getArguments().get(1);
					
					if(styleExpression!=null){						
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
