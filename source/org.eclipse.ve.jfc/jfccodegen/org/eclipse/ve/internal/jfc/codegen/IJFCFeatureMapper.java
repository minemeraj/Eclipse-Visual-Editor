/*
 * Created on Sep 25, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.eclipse.ve.internal.jfc.codegen;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

import org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper;

/**
 * @author gmendel
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface IJFCFeatureMapper extends IJavaFeatureMapper {

	public static String  CONSTRAINT_BOUND = "setBounds" ;  //$NON-NLS-1$
	public static String  CONSTRAINT_BOUND_FEATURE = "bounds" ;  //$NON-NLS-1$
	public static String  CONSTRAINT_BOUND_CLASS = "java.awt.Rectangle" ; //$NON-NLS-1$

	public static String  CONSTRAINT_SIZE =  "setSize" ; //$NON-NLS-1$
	public static String  CONSTRAINT_SIZE_FEATURE =  "size" ; //$NON-NLS-1$
	public static String  CONSTRAINT_SIZE_CLASS  = "java.awt.Dimension" ; //$NON-NLS-1$

	public static String  JMENUBAR_FEATURE_NAME =	"menus" ; //$NON-NLS-1$
	public static String  JMEN_FEATURE_NAME =		"items" ; //$NON-NLS-1$

	public static String  LOCATION_FEATURE_NAME 	= "location" ; //$NON-NLS-1$
	public static String  LOCATION_NAME 			= "setLocation" ; //$NON-NLS-1$
	
	public static String  LAYOUT_FEATURE_NAME		="layout";
	public static String  LAYOUT_NAME				="setLayout";
	
	public static String  JTABLE_MODEL_FEATURE_NAME 		="model";
	public static String  JTABLE_MODEL_NAME				="setModel";
	
	public static String  JTABLE_AUTOCREATECOLUMNSFROMMODEL_FEATURE_NAME = "autoCreateColumnsFromModel";
	public static String  JTABLE_AUTOCREATECOLUMNSFROMMODEL_NAME = "setAutoCreateColumnsFromModel";
	
	
	// Some priorities that are used by the JFC feature mappers
	final static int PRIORITY_JTABLE_MODEL = IJavaFeatureMapper.PRIORITY_DEFAULT;
	// AutoCreate column must come before the setModel
	final static int PRIORITY_JTABLE_AUTOCREATECOLUMNSFROMMODEL = PRIORITY_JTABLE_MODEL + 1000 ;
	final static int PRIORITY_JTABLECOLUMN_ADDCOLUMN = IJavaFeatureMapper.PRIORITY_ADD+1000 ; // a bit more than a regular add
	final static int PRIORITY_LAYOUT = IJavaFeatureMapper.PRIORITY_DEFAULT + 3000;
	
	
}
