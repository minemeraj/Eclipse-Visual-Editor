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
 *  $RCSfile: JVEStyleRegistry.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:30:48 $ 
 */
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.runtime.*;

import org.eclipse.ve.internal.cde.rules.IRule;

/**
 * @author Gili Mendel
 *
 */
public class JVEStyleRegistry implements IStyleRegistry {

	public final static String RULE_OVERRIDE_ID = "org.eclipse.ve.java.core.style"; //$NON-NLS-1$
	public static final String EXT_ID = "id";	// ID attribute for Style config element	 //$NON-NLS-1$

	private final static JVEStyleRegistry _Registry = new JVEStyleRegistry();
	private HashMap fStyles;

	private class NullStyle implements IEditorStyle {
		/**
		* @see org.eclipse.ve.internal.java.core.vce.IEditorStyle#getDescription()
		*/
		public String getDescription() {
			return null;
		}

		/**
		 * @see org.eclipse.ve.internal.java.core.vce.IEditorStyle#getPluginID()
		 */
		public String getPluginID() {
			return null;
		}

		/**
		 * @see org.eclipse.ve.internal.java.core.vce.IEditorStyle#getPrefUI()
		 */
		public IEditorStylePrefUI getPrefUI() {
			return null;
		}

		/**
		 * @see org.eclipse.ve.internal.java.core.vce.IEditorStyle#getRule(String)
		 */
		public IRule getRule(String id) {
			return null;
		}

		/**
		 * @see org.eclipse.ve.internal.java.core.vce.IEditorStyle#getStyleID()
		 */
		public String getStyleID() {
			return null;
		}

		/**
		 * @see org.eclipse.ve.internal.java.core.vce.IEditorStyle#getTemplate(String)
		 */
		public Object getTemplate(String id) {
			return null;
		}

	}

	protected JVEStyleRegistry() {
		fStyles = new HashMap();
	}

	public IEditorStyle getStyle(String styleId) {

		IEditorStyle result = (IEditorStyle) fStyles.get(styleId);
		if (result != null)
			return result;

		IExtensionPoint xp = Platform.getExtensionRegistry().getExtensionPoint(RULE_OVERRIDE_ID);
		if (xp != null) {
			IExtension[] extensions = xp.getExtensions();
			if (extensions.length > 0) {
				for (int i = 0; i < extensions.length; i++) {
					IConfigurationElement[] ces = extensions[i].getConfigurationElements();
					for (int j = 0; j < ces.length; j++) {
						IConfigurationElement ce = ces[j];
						String id = ce.getAttribute(EXT_ID);
						if (id.equals(styleId)) {
							synchronized (this) {
								// Need to synchronize this because the JVEStyleRegistry is shared as a static
								// and if opening more than one editors at a time you can get a race condition
								// and mess up style dictionary. 
								// Now that synced, see if race condition already created it.							
								if (fStyles.get(id) == null) {
									IEditorStyle es = new EditorStyle(id, ce);
									fStyles.put(id, es);
									return es;
								} else
									return (IEditorStyle) fStyles.get(id);
							}
						}
					}
				}
			}
		}
		
		if (result == null) {
			result = new NullStyle();
			fStyles.put(styleId, result);
		}
		return result;
	}

	/**
	 * @see org.eclipse.ve.internal.java.core.vce.IStyleRegistry#getStyleIDs()
	 */
	public String[] getStyleIDs() {

		ArrayList sl = new ArrayList();
		IExtensionPoint xp = Platform.getExtensionRegistry().getExtensionPoint(RULE_OVERRIDE_ID);
		if (xp != null) {
			IExtension[] extensions = xp.getExtensions();
			if (extensions.length > 0) {
				for (int i = 0; i < extensions.length; i++) {
					IConfigurationElement[] ces = extensions[i].getConfigurationElements();
					for (int j = 0; j < ces.length; j++) {
						IConfigurationElement ce = ces[j];
						String id = ce.getAttribute(EXT_ID);
						sl.add(id);
					}
				}
			}
		}
		final String[] result = (String[]) sl.toArray(new String[sl.size()]);
		return result;
	}
	
	public static IStyleRegistry getJVEStyleRegistry() {
		return _Registry ;
	}

}
