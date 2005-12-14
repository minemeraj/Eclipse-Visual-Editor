/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: FreeFormComponentsHost.java,v $
 *  $Revision: 1.3 $  $Date: 2005-12-14 21:44:40 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.draw2d.geometry.Point;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyEvent;
import org.eclipse.jem.internal.proxy.swt.JavaStandardSWTBeanConstants;

import org.eclipse.ve.internal.java.core.CompositionProxyAdapter;


/**
 * This is the free form host for SWT controls.
 * 
 * @since 1.1.0
 */
public class FreeFormComponentsHost implements CompositionProxyAdapter.IFreeFormHost {
	
	/**
	 * Controller for a freeform dialog. There is one instance.
	 * It must be created on the Environment's UI thread. 
	 * 
	 * @since 1.1.0
	 */
	protected class FreeFormComponentProxy {

		protected IProxy ffHost;	// Not used for anything other than to know we have a valid ffHost.

		protected FreeFormComponentProxy(IExpression expression) {
			create(expression);
		}
		
		private void create(IExpression expression) {
			Point loc = BeanSWTUtilities.getOffScreenLocation(expression.getRegistry());
			ProxyFactoryRegistry registry = expression.getRegistry();
			IBeanProxy environment = JavaStandardSWTBeanConstants.getConstants(registry).getEnvironmentProxy();
			IStandardBeanProxyFactory proxyFactory = registry.getBeanProxyFactory();
			ffHost = expression.createSimpleMethodInvoke(environment.getTypeProxy().getMethodProxy(expression, "initializeFreeFormHost", new String[] {"int", "int"}), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					environment, new IProxy[] {proxyFactory.createBeanProxyWith(loc.x), proxyFactory.createBeanProxyWith(loc.y)}, true);
			((ExpressionProxy) ffHost).addProxyListener(new ExpressionProxy.ProxyAdapter(){
				public void proxyResolved(ProxyEvent event) {
					ffHost = event.getProxy();
				}
			});
			
		}
		
		private void recreateIfNecessary(IExpression expression) {
			if (ffHost == null || (ffHost.isBeanProxy() && !((IBeanProxy) ffHost).isValid()))
				create(expression);	// It is not yet created (shouldn't happen), or it is now invalid (registry has been recycled).
		}
		
		/**
		 * Add the control to dialog. This must be called on the Environment's UI thread.
		 * 
		 * @param useSetSize use set size versus preferred size
		 * @param expression
		 * @return the parent composite proxy to use for the control.
		 * 
		 * @since 1.1.0
		 */
		public IProxy add(boolean useSetSize, IExpression expression) {
			recreateIfNecessary(expression);
			IBeanProxy environment = JavaStandardSWTBeanConstants.getConstants(expression.getRegistry()).getEnvironmentProxy();
			
			return expression.createSimpleMethodInvoke(environment.getTypeProxy().getMethodProxy(expression, "addToFreeFormParent", new String[]{"boolean"}),  //$NON-NLS-1$ //$NON-NLS-2$
					environment, new IProxy[] {expression.getRegistry().getBeanProxyFactory().createBeanProxyWith(useSetSize)}, true);
		}
		
		/**
		 * Set the use component size for the given component on the dialog proxy. This must be set at least once before
		 * freeform host will handle the layout of the child. It won't do anything until this method has been called.
		 * This must be called on the Environment's UI thread.
		 * 
		 * @param control
		 * @param useComponentSize
		 * @param expression
		 * 
		 * @since 1.1.0
		 */
		public void useControlSize(IProxy control, boolean useSetSize, IExpression expression) {
			recreateIfNecessary(expression);
			IBeanProxy environment = JavaStandardSWTBeanConstants.getConstants(expression.getRegistry()).getEnvironmentProxy();
			
			expression.createSimpleMethodInvoke(environment.getTypeProxy().getMethodProxy(expression, "setUseSetSize", new String[]{"org.eclipse.swt.widgets.Control", "boolean"}),  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					environment, new IProxy[] {control, expression.getRegistry().getBeanProxyFactory().createBeanProxyWith(useSetSize)}, false);
		}

		/**
		 * Dispose of the proxy. This does not need to be on the Environment's UI thread.
		 * @param expression
		 * 
		 * @since 1.1.0
		 */
		protected void dispose(IExpression expression) {
			if (expression != null && ffHost != null && ffHost.isBeanProxy() && ((IBeanProxy) ffHost).isValid()) {
				// We don't want to recreate on a dispose. That would fluff it up for no reason.
				IBeanProxy environment = JavaStandardSWTBeanConstants.getConstants(expression.getRegistry()).getEnvironmentProxy();
				
				expression.createSimpleMethodInvoke(environment.getTypeProxy().getMethodProxy(expression, "disposeFreeFormHost"),  //$NON-NLS-1$
						environment, null, false);
				ffHost = null;
			}
		}
	}
	
	protected FreeFormComponentProxy ffHost;

	private final CompositionProxyAdapter compositionProxyAdapter;

	/**
	 * Construct it. This should be constructed by ProxyHost's that understand SWT FFormHost.
	 * @param compositionProxyAdapter
	 * 
	 * @since 1.1.0
	 */
	public FreeFormComponentsHost(CompositionProxyAdapter compositionProxyAdapter) {
		super();
		this.compositionProxyAdapter = compositionProxyAdapter;

		compositionProxyAdapter.addFreeForm(FreeFormComponentsHost.class, this);
	}

	/**
	 * Get the parent composite that the control should use to be on the freeform host.
	 * This must be called on the Environment's UI thread.
	 * @param useSetSize <code>true</code> if the ff host should use the set size of the control, or <code>false</code> if it should use the preferred size.
	 * @param expression
	 * @return proxy to a Composite that should be used as the parent for the control to be on the freeform.
	 * 
	 * @since 1.1.0
	 */
	public IProxy add(boolean useSetSize, IExpression expression) {
		if (ffHost == null) {
			ffHost = new FreeFormComponentProxy(expression);
		}
		return ffHost.add(useSetSize, expression);
	}
	
	/**
	 * Set the useSetSize flag for the control
	 * @param control
	 * @param useSetSize <code>true</code> to use the size of the control as set, <code>false</code> to use the preferred size.
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	public void setUseComponentSize(IProxy control, boolean useSetSize, IExpression expression) {
		if (ffHost != null) {
			ffHost.useControlSize(control, useSetSize, expression);
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.CompositionProxyAdapter.IFreeFormHost#dispose(org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	public void dispose(IExpression expression) {
		if (ffHost != null)
			ffHost.dispose(expression);

		ffHost = null;

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.CompositionProxyAdapter.IFreeFormHost#getCompositionAdapter()
	 */
	public CompositionProxyAdapter getCompositionAdapter() {
		return compositionProxyAdapter;
	}
}
