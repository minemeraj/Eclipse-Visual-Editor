/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.codegen.java;

import org.eclipse.osgi.util.NLS;

public final class CodeGenJavaMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.java.codegen.java.messages";//$NON-NLS-1$

	private CodeGenJavaMessages() {
		// Do not instantiate
	}

	public static String BeanDecoderAdapter____5;
	public static String EventExpressionVisitor_TypeMethodExpression;
	public static String MethodVisitor_TypeMethod;
	public static String ExpressionVisitor_TypeMethodExpression;
	public static String JavaBeanModelBuilder_Task_CleanModel;
	public static String JavaBeanModelBuilder_Task_AnalyzeEvents;
	public static String JavaBeanModelBuilder_Task_BuildingModel;
	public static String JavaBeanModelBuilder_Task_ParsingSource;
	public static String ReturnStmtVisitor_TypeMethodExpression;
	public static String EventCallBackExpressionVisitor_TypeMethodExpression;

	static {
		NLS.initializeMessages(BUNDLE_NAME, CodeGenJavaMessages.class);
	}
}