package org.eclipse.ve.internal.cde.utility.impl;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: URLResourceBundleImpl.java,v $
 *  $Revision: 1.2 $  $Date: 2004-02-11 16:03:28 $ 
 */
import java.net.*;
import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.osgi.framework.Bundle;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.utility.URLResourceBundle;
import org.eclipse.ve.internal.cde.utility.UtilityPackage;

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
		return UtilityPackage.eINSTANCE.getURLResourceBundle();
	}

	public java.util.ResourceBundle getBundle() {
		if (!fLoaded) {
			fLoaded = true;
			if (getBundleName() != null && bundleURLs != null && bundleURLs.size() > 0) {
				ArrayList urls = new ArrayList(bundleURLs.size() * 2);
				Iterator itr = bundleURLs.iterator();
				while (itr.hasNext()) {
					try {
						String urlString = (String) itr.next();
						urls.add(new URL(urlString));
						if (urlString.startsWith("platform:/plugin/")) { //$NON-NLS-1$
							// Special, need to get the fragments too.
							// TODO Need to change this to use OSGi API when it is stable.
							int begPluginName = "platform:/plugin/".length(); //$NON-NLS-1$
							int endPluginName = urlString.indexOf('/', begPluginName);
							String pluginName = urlString.substring(begPluginName, endPluginName);
							String pid = getId(pluginName);
							String vid = getVersion(pluginName);
							String rest = urlString.substring(endPluginName);
							IPluginRegistry registry = Platform.getPluginRegistry();
							IPluginDescriptor desc = (vid == null || vid.equals("")) ? registry.getPluginDescriptor(pid) : registry.getPluginDescriptor(pid, new PluginVersionIdentifier(vid)); //$NON-NLS-1$
							if (desc != null) {
								// The plugin descriptor can't get the fragments directly. We will go to the 
								// plugin, from there get the bundle, and ask it. (This will only work with legacy plugins. OSGi plugins do not show in list).
								try {
									Bundle[] fragments = desc.getPlugin().getBundle().getFragments();
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
								} catch (CoreException e) {
									// Do nothing. Probably won't occur.
								}
							}
						}
					} catch (MalformedURLException e) {
						return null; // An error, don't go on.
					}
				}
				URLClassLoader cl = new URLClassLoader((URL[]) urls.toArray(new URL[urls.size()]), null);
				try {
					fBundle = java.util.ResourceBundle.getBundle(getBundleName(), Locale.getDefault(), cl);
				} catch (MissingResourceException e) {
					CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, "", e)); //$NON-NLS-1$
				}
			}
		}
		return fBundle;
	}

	protected String getId(String spec) {
		int i = spec.lastIndexOf('_'); //$NON-NLS-1$
		return i >= 0 ? spec.substring(0, i) : spec;
	}

	protected String getVersion(String spec) {
		int i = spec.lastIndexOf('_');
		return i >= 0 ? spec.substring(i + 1, spec.length()) : ""; //$NON-NLS-1$
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
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case UtilityPackage.URL_RESOURCE_BUNDLE__BUNDLE_NAME:
				return getBundleName();
			case UtilityPackage.URL_RESOURCE_BUNDLE__BUNDLE_UR_LS:
				return getBundleURLs();
		}
		return eDynamicGet(eFeature, resolve);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case UtilityPackage.URL_RESOURCE_BUNDLE__BUNDLE_NAME:
				return BUNDLE_NAME_EDEFAULT == null ? bundleName != null : !BUNDLE_NAME_EDEFAULT.equals(bundleName);
			case UtilityPackage.URL_RESOURCE_BUNDLE__BUNDLE_UR_LS:
				return bundleURLs != null && !bundleURLs.isEmpty();
		}
		return eDynamicIsSet(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(EStructuralFeature eFeature, Object newValue) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case UtilityPackage.URL_RESOURCE_BUNDLE__BUNDLE_NAME:
				setBundleName((String)newValue);
				return;
			case UtilityPackage.URL_RESOURCE_BUNDLE__BUNDLE_UR_LS:
				getBundleURLs().clear();
				getBundleURLs().addAll((Collection)newValue);
				return;
		}
		eDynamicSet(eFeature, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case UtilityPackage.URL_RESOURCE_BUNDLE__BUNDLE_NAME:
				setBundleName(BUNDLE_NAME_EDEFAULT);
				return;
			case UtilityPackage.URL_RESOURCE_BUNDLE__BUNDLE_UR_LS:
				getBundleURLs().clear();
				return;
		}
		eDynamicUnset(eFeature);
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
