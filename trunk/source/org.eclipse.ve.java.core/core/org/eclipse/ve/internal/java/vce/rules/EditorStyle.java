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
 *  $RCSfile: EditorStyle.java,v $
 *  $Revision: 1.2 $  $Date: 2004-06-02 15:57:22 $ 
 */
package org.eclipse.ve.internal.java.vce.rules;

import java.util.HashMap;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.ve.internal.cde.rules.IRule;


/**
 * @author Gili Mendel
 *
 */
public class EditorStyle implements IEditorStyle {
	
	private HashMap				fRules 	   = null ;
	private HashMap				fTemplates = null ;
	private String					fID ;
	private String					fDescription ;
	private IEditorStylePrefUI		fPrefUI = null ;
	
	private IConfigurationElement 	fConfig ;
	
	public static final String  EXT_DESC 	= "description" ;	// Description attribute Style config element //$NON-NLS-1$
	public static final String EXT_CLASS = "class"; // Attribute for Class for the preference store.			 //$NON-NLS-1$

	public EditorStyle (String styleId, IConfigurationElement ce) {
		fConfig = ce ;
		fID = styleId;
		fDescription = ce.getAttribute(EXT_DESC) ;				
	}
			
	/**
	 * @see org.eclipse.ve.internal.java.core.vce.IEditorStyle#getRules()
	 * 
	 * Get the rules only if asked ... posponed the bring up
	 * of the given plugin
	 * 
	 */
	protected HashMap getRules() {
		if (fRules != null) return fRules ;
		
		synchronized (this) {
			// Because this is a static instance shared by all editors, there can be a race
			// condition to create the rules map.
			// Now that synced, check that someone else didn't create it before us.
			if (fRules == null) {
				fRules = new HashMap() ;
				IConfigurationElement[] elements = fConfig.getChildren() ;
				for (int i = 0; i < elements.length; i++) {
					IConfigurationElement ce = elements[i];
					if (ce.getName().equals(EXT_RULE)) {
						IRuleProvider rp = new DefaultRuleProvider(ce, fID, this);
						fRules.put(rp.getRuleID(), rp);
					}
				}
			}
		}
		return fRules ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.core.vce.IEditorStyle#getTemplates()
	 */
	protected HashMap getTemplates() {
		return fTemplates ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.core.vce.IEditorStyle#getDescription()
	 */
	public String getDescription() {
		return fDescription ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.core.vce.IEditorStyle#getID()
	 */
	public String getStyleID() {
		return fID ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.core.vce.IEditorStyle#getPrefStore()
	 */
	public Preferences getPrefStore() {
		return null;
	}


	private boolean noStyle;	// If true, then we tried but it couldn't be accessed.
	/**
	 * @see org.eclipse.ve.internal.java.core.vce.IEditorStyle#getPrefUI()
	 */
	public IEditorStylePrefUI getPrefUI() {
		if (noStyle)
			return null;
		if (fPrefUI != null) return fPrefUI ;
		IConfigurationElement[] elements = fConfig.getChildren() ;
		for (int i = 0; i < elements.length; i++) {
			IConfigurationElement ce = elements[i];
			if (ce.getName().equals(EXT_PREF_UI)) {
				try {
					fPrefUI = (IEditorStylePrefUI) ce.createExecutableExtension(EXT_CLASS);
				}
				catch (CoreException e) {
					noStyle = true;
					org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e) ;
				} catch (ClassCastException e) {
					noStyle = true;
					org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e) ;					
				}
				break ;
			}
		}
		return fPrefUI ;
	}
	
	public String toString() {
		return "EditorStyle("+fID+")" ; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @see org.eclipse.ve.internal.java.core.vce.IEditorStyle#getPluginID()
	 */
	public String getPluginID() {
		return fConfig.getDeclaringExtension().getNamespace();
	}

	/**
	 * @see org.eclipse.ve.internal.java.core.vce.IEditorStyle#getRule(String)
	 */
	public IRule getRule(String id) {
		IRuleProvider p = (IRuleProvider) getRules().get(id) ;
		if (p != null) return p.getRule() ;
		return null ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.core.vce.IEditorStyle#getTemplates(String)
	 */
	public Object getTemplate(String id) {
		return null;
	}

}
