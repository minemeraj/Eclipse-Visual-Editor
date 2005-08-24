/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.vce.rules;
/*
 *  $RCSfile: IEditorStyle.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:48 $ 
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
