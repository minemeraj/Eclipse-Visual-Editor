package org.eclipse.ve.internal.java.vce.rules;
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
/*
 *  $RCSfile: IEditorStyle.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */
import org.eclipse.ve.internal.cde.rules.IRuleRegistry;

/**
 * @author Gili Mendel
 * This interface is used to define style specific configurations:
 * 	Rules
 * 	Templates
 * 	Pref. Page
 * 	
 */
public interface IEditorStyle extends IRuleRegistry {
	
	public static final String  EXT_PREF_UI 	= "prefui" ; //$NON-NLS-1$
	public static final String  EXT_RULE 	= "rule" ; //$NON-NLS-1$
	public static final String  EXT_TEMPLATE	= "template" ; //$NON-NLS-1$
	public static final String  EXT_JET 		= "jet" ; //$NON-NLS-1$

	
	
	
	// Key Value pairs  
	Object		getTemplate(String id) ;
	String		getDescription() ;
	// Name-ID
	String		getStyleID() ;
	String		getPluginID() ;
	IEditorStylePrefUI getPrefUI() ;		                                                                     
}
