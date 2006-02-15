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
package org.eclipse.ve.internal.cde.utility.impl;
import java.net.*;
import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.osgi.framework.Bundle;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.utility.URLResourceBundle;
import org.eclipse.ve.internal.cde.utility.UtilityPackage;

import com.ibm.icu.util.ULocale;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>URL Resource Bundle</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.utility.impl.URLResourceBundleImpl#getBundleName <em>Bundle Name</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.utility.impl.URLResourceBundleImpl#getBundleURLs <em>Bundle UR Ls</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */

public class URLResourceBundleImpl extends ResourceBundleImpl implements URLResourceBundle {
	/**
	 * The default value of the '{@link #getBundleName() <em>Bundle Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBundleName()
	 * @generated
	 * @ordered
	 */
	protected static final String BUNDLE_NAME_EDEFAULT = null;

	
	private java.util.ResourceBundle fBundle = null;
	private boolean fLoaded = false;

	/**
	 * The cached value of the '{@link #getBundleName() <em>Bundle Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBundleName()
	 * @generated
	 * @ordered
	 */
	protected String bundleName = BUNDLE_NAME_EDEFAULT;
	/**
	 * The cached value of the '{@link #getBundleURLs() <em>Bundle UR Ls</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBundleURLs()
	 * @generated
	 * @ordered
	 */
	protected EList bundleURLs = null;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected URLResourceBundleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return UtilityPackage.Literals.URL_RESOURCE_BUNDLE;
	}

	// dbk cache previously loaded bundles (custom classloader defeats ResourceBundle cache)
	
	private static HashMap rbCache = new HashMap();
	
	public java.util.ResourceBundle getBundle() {
		if (!fLoaded) {
			fLoaded = true;
			if (getBundleName() != null && bundleURLs != null && bundleURLs.size() > 0) {
				ArrayList urls = new ArrayList(bundleURLs.size() * 2);
				StringBuffer sb = new StringBuffer(); 
				sb.append(getBundleName());
				sb.append(':');
				Iterator itr = bundleURLs.iterator();
				while (itr.hasNext()) {
					try {
						String urlString = (String) itr.next();
						urls.add(new URL(urlString));
						sb.append(urlString + ";"); //dbk						
						if (urlString.startsWith("platform:/plugin/")) { //$NON-NLS-1$
							// Special, need to get the fragments too.
							int begPluginName = "platform:/plugin/".length(); //$NON-NLS-1$
							int endPluginName = urlString.indexOf('/', begPluginName);
							String pluginName = urlString.substring(begPluginName, endPluginName);
							String rest = urlString.substring(endPluginName);
							Bundle bundle = Platform.getBundle(pluginName);
							if (bundle != null) {
								// The plugin descriptor can't get the fragments directly. We will go to the 
								// plugin, from there get the bundle, and ask it. (This will only work with legacy plugins. OSGi plugins do not show in list).
								Bundle[] fragments = Platform.getFragments(bundle);
								// See if there are any fragments
								if (fragments != null)
									for (int j = 0; j < fragments.length; j++) {
										try {
											URL u = fragments[j].getEntry(rest);
											if (u != null)
												urls.add(u); //$NON-NLS-1$
										} catch (Exception e) {
											// Had problems with the file. Just skip it.
										}
									}
							}
						}
					} catch (MalformedURLException e) {
						return null; // An error, don't go on.
					}
				}
				
				String key = sb.toString(); //dbk
				fBundle = (ResourceBundle) rbCache.get(key); //dbk
				if (fBundle == null) { //dbk
					URLClassLoader cl = new URLClassLoader((URL[]) urls.toArray(new URL[urls.size()]), null);
					try {
						fBundle = java.util.ResourceBundle.getBundle(getBundleName(), ULocale.getDefault().toLocale(), cl);
						rbCache.put(key, fBundle); //dbk						
					} catch (MissingResourceException e) {
						CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, "", e)); //$NON-NLS-1$
					}
				}
			}
		}
		return fBundle;
	}

	public void setBundleName(String value) {
		// Clear cache
		fLoaded = false;
		fBundle = null;
		this.setBundleNameGen(value);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBundleName() {
		return bundleName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getBundleURLs() {
		if (bundleURLs == null) {
			bundleURLs = new EDataTypeUniqueEList(String.class, this, UtilityPackage.URL_RESOURCE_BUNDLE__BUNDLE_UR_LS);
		}
		return bundleURLs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case UtilityPackage.URL_RESOURCE_BUNDLE__BUNDLE_NAME:
				return getBundleName();
			case UtilityPackage.URL_RESOURCE_BUNDLE__BUNDLE_UR_LS:
				return getBundleURLs();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case UtilityPackage.URL_RESOURCE_BUNDLE__BUNDLE_NAME:
				setBundleName((String)newValue);
				return;
			case UtilityPackage.URL_RESOURCE_BUNDLE__BUNDLE_UR_LS:
				getBundleURLs().clear();
				getBundleURLs().addAll((Collection)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
			case UtilityPackage.URL_RESOURCE_BUNDLE__BUNDLE_NAME:
				setBundleName(BUNDLE_NAME_EDEFAULT);
				return;
			case UtilityPackage.URL_RESOURCE_BUNDLE__BUNDLE_UR_LS:
				getBundleURLs().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case UtilityPackage.URL_RESOURCE_BUNDLE__BUNDLE_NAME:
				return BUNDLE_NAME_EDEFAULT == null ? bundleName != null : !BUNDLE_NAME_EDEFAULT.equals(bundleName);
			case UtilityPackage.URL_RESOURCE_BUNDLE__BUNDLE_UR_LS:
				return bundleURLs != null && !bundleURLs.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (bundleName: ");
		result.append(bundleName);
		result.append(", bundleURLs: ");
		result.append(bundleURLs);
		result.append(')');
		return result.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBundleNameGen(String newBundleName) {
		String oldBundleName = bundleName;
		bundleName = newBundleName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UtilityPackage.URL_RESOURCE_BUNDLE__BUNDLE_NAME, oldBundleName, bundleName));
	}

}
