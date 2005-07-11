/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * $RCSfile: WidgetProxyAdapter.java,v $ $Revision: 1.20 $ $Date: 2005-06-15 20:19:21 $
 */
package org.eclipse.ve.internal.swt;



import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;

import org.eclipse.ve.internal.java.core.*;

/**
 * Proxy adapter for SWT Widgets.
 * 
 * @since 1.0.0
 */
public class WidgetProxyAdapter extends UIThreadOnlyProxyAdapter {

	public WidgetProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	// Style bit. Calculated once and saved. It is used repeatedly.
	private static final int NO_STYLE = -1;

	private int style = NO_STYLE;

	/*
	 * Process release. It is not guarenteed to be on UI thread. If the bean proxy is owned and is instantiated it will be on UI thread. Otherwise it
	 * will not. Overrides should go to the UI thread if they need to.
	 * <p>
	 * <b>Note:</b> Overrides <b>MUST</b> call super.primPrimReleaseBeanProxy(IExpression).
	 * 
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	protected void primPrimReleaseBeanProxy(IExpression expression) {
		style = NO_STYLE; // Uncache the style bit
		if (isOwnsProxy() && isBeanProxyInstantiated()) {
			BeanSWTUtilities.invoke_WidgetDispose(getProxy(), expression);
		}
	}

	/**
	 * @return the int style value by interrogate getStyle() on the targetVM on the correct thread
	 * 
	 * @since 1.0.0
	 */
	public int getStyle() {
		if (style == NO_STYLE && isBeanProxyInstantiated()) {
			invokeSyncExecCatchThrowable(new DisplayManager.DisplayRunnable() {

				public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
					IBeanProxy widgetBeanProxy = getBeanProxy();
					IMethodProxy getStyleMethodProxy = widgetBeanProxy.getTypeProxy().getMethodProxy("getStyle"); //$NON-NLS-1$
					IIntegerBeanProxy styleBeanProxy = (IIntegerBeanProxy) getStyleMethodProxy.invoke(widgetBeanProxy);
					style = styleBeanProxy.intValue();
					return null;
				}
			});
		}
		return style;
	}

}