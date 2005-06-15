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
 *  $RCSfile: JFaceColorProxyRegistration.java,v $
 *  $Revision: 1.4 $  $Date: 2005-06-15 20:19:21 $ 
 */
package org.eclipse.ve.internal.jface;

import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.RGB;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;
import org.eclipse.jem.internal.proxy.swt.JavaStandardSWTBeanConstants;

/**
 * Initialize the JFace ColorRegistry with the JFacePreferences colors from the IDE. The ColorRegistry is normally primed from the WorkBench themes
 * but because the WorkBench isn't loaded in the remote VM, it never gets set and when you set the color of a SWT control using the JFace preferences
 * colors, it doesn't show correctly in the remote VM. For RCP applications, the workbench will be loaded in the running application so the
 * ColorRegistry should be primed as well and show up correctly.
 * 
 * @since 1.1.0
 */
public class JFaceColorProxyRegistration {

	/**
	 * Initialize the color registry from the IDE settings.
	 * 
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	public static void initialize(IExpression expression) {
		// To handle the case in which this is being called but there is no SWT in the remote vm,
		// check to see if the SWT RGB class even exists. If not, just return.
		// Use try {
		// ...init...
		// } catch (ClassNotFoundException e) {
		// }
		IStandardBeanTypeProxyFactory beanTypeFactory = expression.getRegistry().getBeanTypeProxyFactory();
		expression.createTry();

		IProxyBeanType hashMapBeanTypeProxy = beanTypeFactory.getBeanTypeProxy(expression, "java.util.HashMap"); //$NON-NLS-1$
		IProxyBeanType objectBeanTypeProxy = beanTypeFactory.getBeanTypeProxy(expression, "java.lang.Object"); //$NON-NLS-1$
		IProxyMethod putMethodProxy = hashMapBeanTypeProxy.getMethodProxy(expression,
				"put", new IProxyBeanType[] { objectBeanTypeProxy, objectBeanTypeProxy}); //$NON-NLS-1$
		IProxyBeanType rgbBeanTypeProxy = beanTypeFactory.getBeanTypeProxy(expression, "org.eclipse.swt.graphics.RGB"); //$NON-NLS-1$

		// hashMap = new HashMap();
		IProxy hashMapProxy = expression.createProxyAssignmentExpression(ForExpression.ROOTEXPRESSION);
		expression.createClassInstanceCreation(ForExpression.ASSIGNMENT_RIGHT, hashMapBeanTypeProxy, 0);

		// Now populate the map with the JFace color preferences found in JFacePreferences

		// hashmap.put(JFacePreferences.ERROR_COLOR, new RGB(errorcolor));
		expression.createMethodInvocation(ForExpression.ROOTEXPRESSION, putMethodProxy, true, 2);
		expression.createProxyExpression(ForExpression.METHOD_RECEIVER, hashMapProxy);
		expression.createStringLiteral(ForExpression.METHOD_ARGUMENT, JFacePreferences.ERROR_COLOR);
		getRGBBeanProxy(JFaceResources.getColorRegistry().getRGB(JFacePreferences.ERROR_COLOR), expression, rgbBeanTypeProxy);

		// hashmap.put(JFacePreferences.HYPERLINK_COLOR, new RGB(HYPERLINK_COLOR));
		expression.createMethodInvocation(ForExpression.ROOTEXPRESSION, putMethodProxy, true, 2);
		expression.createProxyExpression(ForExpression.METHOD_RECEIVER, hashMapProxy);
		expression.createStringLiteral(ForExpression.METHOD_ARGUMENT, JFacePreferences.HYPERLINK_COLOR);
		getRGBBeanProxy(JFaceResources.getColorRegistry().getRGB(JFacePreferences.HYPERLINK_COLOR), expression, rgbBeanTypeProxy);

		// hashmap.put(JFacePreferences.ACTIVE_HYPERLINK_COLOR, new RGB(ACTIVE_HYPERLINK_COLOR));
		expression.createMethodInvocation(ForExpression.ROOTEXPRESSION, putMethodProxy, true, 2);
		expression.createProxyExpression(ForExpression.METHOD_RECEIVER, hashMapProxy);
		expression.createStringLiteral(ForExpression.METHOD_ARGUMENT, JFacePreferences.ACTIVE_HYPERLINK_COLOR);
		getRGBBeanProxy(JFaceResources.getColorRegistry().getRGB(JFacePreferences.ACTIVE_HYPERLINK_COLOR), expression, rgbBeanTypeProxy);

		// JFaceColorRegistry.init(hashmap);
		IProxyBeanType jfaceColorInitBeanTypeProxy = beanTypeFactory.getBeanTypeProxy(expression,
				"org.eclipse.ve.internal.jface.targetvm.JFaceColorRegistryInitializer"); //$NON-NLS-1$
		expression.createSimpleMethodInvoke(jfaceColorInitBeanTypeProxy.getMethodProxy(expression, "init", new String[] { "java.util.Map",	//$NON-NLS-1$ //$NON-NLS-2$
				"org.eclipse.ve.internal.swt.targetvm.Environment"}), null, new IProxy[] { hashMapProxy,	//$NON-NLS-1$
				JavaStandardSWTBeanConstants.getConstants(expression.getRegistry()).getEnvironmentProxy()}, false);

		// catch (ClassNotFoundException) {
		// }
		expression.createTryCatchClause("java.lang.ClassNotFoundException", false);	//$NON-NLS-1$
		expression.createTryEnd();
	}

	/**
	 * Called to make an RGB Beanproxy as a method argument.
	 * 
	 * @param rgb
	 * @param expression
	 * @param beantypeFactory
	 * @param beanFactory
	 * 
	 * @since 1.1.0
	 */
	private static void getRGBBeanProxy(RGB rgb, IExpression expression, IProxyBeanType rgbBeanTypeProxy) {
		expression.createClassInstanceCreation(ForExpression.METHOD_ARGUMENT, rgbBeanTypeProxy, 3);
		expression.createPrimitiveLiteral(ForExpression.CLASSINSTANCECREATION_ARGUMENT, rgb.red);
		expression.createPrimitiveLiteral(ForExpression.CLASSINSTANCECREATION_ARGUMENT, rgb.green);
		expression.createPrimitiveLiteral(ForExpression.CLASSINSTANCECREATION_ARGUMENT, rgb.blue);
	}

}
