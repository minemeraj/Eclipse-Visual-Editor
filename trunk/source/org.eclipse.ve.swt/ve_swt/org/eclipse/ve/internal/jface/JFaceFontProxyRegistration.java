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
 *  $RCSfile: JFaceFontProxyRegistration.java,v $
 *  $Revision: 1.2 $  $Date: 2005-06-15 20:19:21 $ 
 */
package org.eclipse.ve.internal.jface;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;
import org.eclipse.jem.internal.proxy.swt.JavaStandardSWTBeanConstants;

/**
 * Initialize the JFace FontRegistry with the JFacePreferences fonts from the IDE.
 * 
 * @since 1.1.0
 */
public class JFaceFontProxyRegistration {

	/**
	 * Initialize the JFace font registry from IDE settings.
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
		IProxyBeanType fontDataBeanTypeProxy = beanTypeFactory.getBeanTypeProxy(expression, "org.eclipse.swt.graphics.FontData"); //$NON-NLS-1$
		IProxyBeanType fontDataArrayBeanTypeProxy = beanTypeFactory.getBeanTypeProxy(expression, "org.eclipse.swt.graphics.FontData", 1); //$NON-NLS-1$

		// hashMap = new HashMap();
		IProxy hashMapProxy = expression.createProxyAssignmentExpression(ForExpression.ROOTEXPRESSION);
		expression.createClassInstanceCreation(ForExpression.ASSIGNMENT_RIGHT, hashMapBeanTypeProxy, 0);

		// Now populate the map with the JFace color preferences found in JFacePreferences

		// hashmap.put(JFaceResources.BANNER_FONT, new FontData(BANNER_FONT));
		expression.createMethodInvocation(ForExpression.ROOTEXPRESSION, putMethodProxy, true, 2);
		expression.createProxyExpression(ForExpression.METHOD_RECEIVER, hashMapProxy);
		expression.createStringLiteral(ForExpression.METHOD_ARGUMENT, JFaceResources.BANNER_FONT);
		getFontDataArrayBeanProxy(JFaceResources.getFontRegistry().get(JFaceResources.BANNER_FONT), expression, fontDataArrayBeanTypeProxy, fontDataBeanTypeProxy);

		// hashmap.put(JFaceResources.DEFAULT_FONT, new FontData(DEFAULT_FONT));
		expression.createMethodInvocation(ForExpression.ROOTEXPRESSION, putMethodProxy, true, 2);
		expression.createProxyExpression(ForExpression.METHOD_RECEIVER, hashMapProxy);
		expression.createStringLiteral(ForExpression.METHOD_ARGUMENT, JFaceResources.DEFAULT_FONT);
		getFontDataArrayBeanProxy(JFaceResources.getFontRegistry().get(JFaceResources.DEFAULT_FONT), expression, fontDataArrayBeanTypeProxy, fontDataBeanTypeProxy);

		// hashmap.put(JFaceResources.DIALOG_FONT, new FontData(DIALOG_FONT));
		expression.createMethodInvocation(ForExpression.ROOTEXPRESSION, putMethodProxy, true, 2);
		expression.createProxyExpression(ForExpression.METHOD_RECEIVER, hashMapProxy);
		expression.createStringLiteral(ForExpression.METHOD_ARGUMENT, JFaceResources.DIALOG_FONT);
		getFontDataArrayBeanProxy(JFaceResources.getFontRegistry().get(JFaceResources.DIALOG_FONT), expression, fontDataArrayBeanTypeProxy, fontDataBeanTypeProxy);

		// hashmap.put(JFaceResources.HEADER_FONT, new FontData(HEADER_FONT));
		expression.createMethodInvocation(ForExpression.ROOTEXPRESSION, putMethodProxy, true, 2);
		expression.createProxyExpression(ForExpression.METHOD_RECEIVER, hashMapProxy);
		expression.createStringLiteral(ForExpression.METHOD_ARGUMENT, JFaceResources.HEADER_FONT);
		getFontDataArrayBeanProxy(JFaceResources.getFontRegistry().get(JFaceResources.HEADER_FONT), expression, fontDataArrayBeanTypeProxy, fontDataBeanTypeProxy);

		// hashmap.put(JFaceResources.TEXT_FONT, new FontData(TEXT_FONT));
		expression.createMethodInvocation(ForExpression.ROOTEXPRESSION, putMethodProxy, true, 2);
		expression.createProxyExpression(ForExpression.METHOD_RECEIVER, hashMapProxy);
		expression.createStringLiteral(ForExpression.METHOD_ARGUMENT, JFaceResources.TEXT_FONT);
		getFontDataArrayBeanProxy(JFaceResources.getFontRegistry().get(JFaceResources.TEXT_FONT), expression, fontDataArrayBeanTypeProxy, fontDataBeanTypeProxy);

		// JFaceColorRegistry.init(hashmap);
		IProxyBeanType jfaceFontInitBeanTypeProxy = beanTypeFactory.getBeanTypeProxy(expression,
				"org.eclipse.ve.internal.jface.targetvm.JFaceFontRegistryInitializer"); //$NON-NLS-1$
		expression.createSimpleMethodInvoke(jfaceFontInitBeanTypeProxy.getMethodProxy(expression, "init", new String[] { "java.util.Map",	//$NON-NLS-1$ //$NON-NLS-2$
				"org.eclipse.ve.internal.swt.targetvm.Environment"}), null, new IProxy[] { hashMapProxy,	//$NON-NLS-1$
				JavaStandardSWTBeanConstants.getConstants(expression.getRegistry()).getEnvironmentProxy()}, false);

		// catch (ClassNotFoundException) {
		// }
		expression.createTryCatchClause("java.lang.ClassNotFoundException", false);	//$NON-NLS-1$
		expression.createTryEnd();
	}

	/**
	 * Called to make an Fontdata Beanproxy as a method argument.
	 * 
	 * @param font
	 * @param expression
	 * @param fontDataArrayBeanTypeProxy
	 * @param fontDataBeanTypeProxy
	 * 
	 * @since 1.1.0
	 */
	private static void getFontDataArrayBeanProxy(Font font, IExpression expression, IProxyBeanType fontDataArrayBeanTypeProxy, IProxyBeanType fontDataBeanTypeProxy) {
		FontData[] fontdata = font.getFontData();
		expression.createArrayCreation(ForExpression.METHOD_ARGUMENT, fontDataArrayBeanTypeProxy, 0);
		expression.createArrayInitializer(fontdata.length);
		for (int i = 0; i < fontdata.length; i++) {
			expression.createClassInstanceCreation(ForExpression.ARRAYINITIALIZER_EXPRESSION, fontDataBeanTypeProxy, 1);
			expression.createStringLiteral(ForExpression.CLASSINSTANCECREATION_ARGUMENT, fontdata[i].toString());			
		}
	}
}
