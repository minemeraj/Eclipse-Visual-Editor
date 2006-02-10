/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: SWTBeanInfoConstants.java,v $
 *  $Revision: 1.1 $  $Date: 2006-02-10 21:53:46 $ 
 */
package org.eclipse.ve.swt.common;


/**
 * Constants required for SWT BeanInfos only.
 * @since 1.2.0
 */
public class SWTBeanInfoConstants {
	
	private SWTBeanInfoConstants() {
		
	}

	/**
	 * Feature Attribute key. Used on BeanDescriptors for Composite subclasses to override
	 * the applying of a default layout. For example, for normal composites if the default
	 * layout is GridLayout, then when a Composite is dropped then a GridLayout will automatically
	 * be applied to the composite.
	 * <p>
	 * This value for this key can be:
	 * <dl>
	 * <dt><code>Boolean</code>
	 * <dd><code>true</code> if default layout from preferences should be applied (this is the default). Or this can be used to
	 * override a setting from a super class BeanDescriptor. <code>false</code> to indicate no default layout should be applied. This
	 * is for those special Composite subclasses that have their own layouts already and so don't want them changed. Also if it
	 * <code>false</code> then the layout property will not show on the property sheet, nor will the Layout customizer show anything
	 * for switching layouts.
	 * <dt><code>String</code>
	 * <dd>The fully-qualified name of a layout to use instead of the default layout from the preferences.
	 * </dl>
	 * 
	 * @since 1.2.0
	 */
	public final static String DEFAULT_LAYOUT = "defaultlayout";	//$NON-NLS-1$
}
