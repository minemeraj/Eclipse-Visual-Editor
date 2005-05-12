/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: FreeFormComponentsHost.java,v $
 *  $Revision: 1.3 $  $Date: 2005-05-12 21:03:55 $ 
 */

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyEvent;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;

import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.java.core.*;
/**
 * This is the free form host for JFC (AWT and Swing) components.
 * 
 * @since 1.1.0
 */
public class FreeFormComponentsHost implements CompositionProxyAdapter.IFreeFormHost {
	
	/**
	 * Controller for a freeform dialog. There is one instance for AWT and one for Swing. 
	 * 
	 * @since 1.1.0
	 */
	public class FreeFormComponentProxy {

		protected IProxy dialogProxy;
		protected IProxyBeanType dialogTypeProxy;

		protected FreeFormComponentProxy(ExpressionProxy dialogExpressionProxy, IProxyBeanType dialogTypeProxy) {
			this.dialogProxy = dialogExpressionProxy;
			dialogExpressionProxy.addProxyListener(new ExpressionProxy.ProxyAdapter(){
				public void proxyResolved(ProxyEvent event) {
					dialogProxy = event.getProxy();
				}
			});
			this.dialogTypeProxy = dialogTypeProxy;
			if (dialogTypeProxy.isExpressionProxy()) {
				((ExpressionProxy) dialogTypeProxy).addProxyListener(new ExpressionProxy.ProxyAdapter(){
					public void proxyResolved(ProxyEvent event) {
						FreeFormComponentProxy.this.dialogTypeProxy = (IProxyBeanType) event.getProxy();
					}
				});				
			}
		}
		
		/**
		 * Get the composition proxy adapter this proxy is for.
		 * @return
		 * 
		 * @since 1.1.0
		 */
		public CompositionProxyAdapter getCompostionAdapter() {
			return FreeFormComponentsHost.this.getCompositionAdapter();
		}
	
		
		/**
		 * Add the component to dialog proxy.
		 * 
		 * @param component
		 * 
		 * @since 1.1.0
		 */
		protected void add(IProxy component, IExpression expression) {
			IProxyMethod addMethod = dialogTypeProxy.getMethodProxy(expression, "add", //$NON-NLS-1$
					new String[] {"java.awt.Component"} //$NON-NLS-1$ 
			);
			
			expression.createSimpleMethodInvoke(addMethod, dialogProxy, new IProxy[] {component}, false);
		}
		
		/**
		 * Remove the component from the dialog.
		 * @param component
		 * @param expression
		 * 
		 * @since 1.1.0
		 */
		public void remove(IBeanProxy component, IExpression expression) {
			if (dialogProxy.isBeanProxy() && ((IBeanProxy) dialogProxy).isValid()) {
				IProxyMethod removeMethod = dialogTypeProxy.getMethodProxy(expression, "remove", //$NON-NLS-1$
						new String[] {"java.awt.Component"} //$NON-NLS-1$
				);
				
				expression.createSimpleMethodInvoke(removeMethod, dialogProxy, new IProxy[] {component}, false);
				
			}
		}
		
		/**
		 * Set the use component size for the given component on the dialog proxy. This must be set at least once before
		 * freeform host will handle the layout of the child. It won't do anything until this method has been called.
		 * 
		 * @param component
		 * @param useComponentSize
		 * @param expression
		 * 
		 * @since 1.1.0
		 */
		public void useComponentSize(IProxy component, boolean useComponentSize, IExpression expression) {
			IProxyMethod useMethod = dialogTypeProxy.getMethodProxy(expression, "setUseComponentSize", //$NON-NLS-1$
					new String[] { "java.awt.Component", "boolean" } //$NON-NLS-1$ //$NON-NLS-2$
			);
			expression.createSimpleMethodInvoke(useMethod, dialogProxy, new IProxy[] {component, expression.getRegistry().getBeanProxyFactory().createBeanProxyWith(useComponentSize)}, false);
		}

		/*
		 * dispose of the proxy.
		 */
		protected void dispose(IExpression expression) {
			if (dialogProxy != null && dialogProxy.isBeanProxy() && ((IBeanProxy) dialogProxy).isValid()) {
				IProxyMethod dispose = BeanAwtUtilities.getWindowDisposeMethodProxy(expression);
				expression.createSimpleMethodInvoke(dispose, null, new IProxy[] {dialogProxy},false);
			}
		}
	}
	
	protected FreeFormComponentProxy awtDialogHost, swingDialogHost;

	protected IBeanProxyDomain beanProxyDomain;

	protected EClass classComponent, classJComponent;

	private final CompositionProxyAdapter compositionProxyAdapter;

	/**
	 * Construct it. This should be constructed by ProxyHost's that understand AWT/Swing FFormHost.
	 * @param beanProxyDomain
	 * 
	 * @since 1.1.0
	 */
	public FreeFormComponentsHost(
		IBeanProxyDomain beanProxyDomain,
		CompositionProxyAdapter compositionProxyAdapter) {
		super();
		this.beanProxyDomain = beanProxyDomain;
		this.compositionProxyAdapter = compositionProxyAdapter;

		ensureEMFDetailsCached(EMFEditDomainHelper.getResourceSet(beanProxyDomain.getEditDomain()));
		
		compositionProxyAdapter.addFreeForm(FreeFormComponentsHost.class, this);
	}

	/**
	 * Is the type of the host a Swing type.
	 * @param aProxy
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected boolean isSwingType(IBeanProxyHost aProxy) {
		return classJComponent.isInstance(aProxy.getTarget());
	}

	/**
	 * Get the free form host proxy for the given type.
	 * @param swingType
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected FreeFormComponentProxy getDialogProxy(boolean swingType, IExpression expression) {
		if (swingType) {
			if (swingDialogHost == null) {
				Point loc = BeanAwtUtilities.getOffScreenLocation();
				IProxyBeanType dialogType = beanProxyDomain.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression, "org.eclipse.ve.internal.jfc.vm.FreeFormSwingDialog");
				ExpressionProxy dialogProxy = expression.createProxyAssignmentExpression(ForExpression.ROOTEXPRESSION);
				expression.createClassInstanceCreation(ForExpression.ASSIGNMENT_RIGHT, dialogType, 2);
				expression.createPrimitiveLiteral(ForExpression.CLASSINSTANCECREATION_ARGUMENT, loc.x);
				expression.createPrimitiveLiteral(ForExpression.CLASSINSTANCECREATION_ARGUMENT, loc.y);
				swingDialogHost = new FreeFormComponentProxy(dialogProxy, dialogType); //$NON-NLS-1$
			}
			return swingDialogHost;
		} else {
			if (awtDialogHost == null) {
				Point loc = BeanAwtUtilities.getOffScreenLocation();
				IProxyBeanType dialogType = beanProxyDomain.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression, "org.eclipse.ve.internal.jfc.vm.FreeFormAWTDialog");
				ExpressionProxy dialogProxy = expression.createProxyAssignmentExpression(ForExpression.ROOTEXPRESSION);
				expression.createClassInstanceCreation(ForExpression.ASSIGNMENT_RIGHT, dialogType, 2);
				expression.createPrimitiveLiteral(ForExpression.CLASSINSTANCECREATION_ARGUMENT, loc.x);
				expression.createPrimitiveLiteral(ForExpression.CLASSINSTANCECREATION_ARGUMENT, loc.y);
				awtDialogHost = new FreeFormComponentProxy(dialogProxy, dialogType); //$NON-NLS-1$
			}
			return awtDialogHost;
		}
	}

	/**
	 * Add the component to the appropriate dialog.
	 * @param aComponentProxyHost
	 * @param aComponentProxy the IProxy for the component. Typically when add is called the proxy host is not yet initialized, and so doesn't have the proxy yet. So we need to send it in.
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public FreeFormComponentProxy add(IBeanProxyHost aComponentProxyHost, IProxy aComponentProxy, IExpression expression) {
		FreeFormComponentProxy dialogHost = getDialogProxy(isSwingType(aComponentProxyHost), expression);
		dialogHost.add(aComponentProxy, expression);
		return dialogHost;
	}

	protected void ensureEMFDetailsCached(ResourceSet rset){
		if ( classComponent == null ) 
			classComponent = Utilities.getJavaClass("java.awt.Component", rset); //$NON-NLS-1$
		if ( classJComponent == null )
			classJComponent = Utilities.getJavaClass("javax.swing.JComponent", rset); //$NON-NLS-1$
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.CompositionProxyAdapter.IFreeFormHost#dispose(org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	public void dispose(IExpression expression) {
		if (awtDialogHost != null)
			awtDialogHost.dispose(expression);
		if (swingDialogHost != null)
			swingDialogHost.dispose(expression);

		awtDialogHost = swingDialogHost = null;

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.CompositionProxyAdapter.IFreeFormHost#getCompositionAdapter()
	 */
	public CompositionProxyAdapter getCompositionAdapter() {
		return compositionProxyAdapter;
	}
}
