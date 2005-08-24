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
package org.eclipse.ve.internal.jfc.beaninfo;

import java.awt.GridBagConstraints;

/*
 *  $RCSfile: GridBagConstraintsBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:12 $ 
 */


public class GridBagConstraintsBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle resconstraints = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.gridbagconstraints");  //$NON-NLS-1$	
/**
 * Gets the bean class.
 */
public Class getBeanClass() {
	return java.awt.GridBagConstraints.class;
}
/**
 * @return java.beans.PropertyDescriptor[] for the gridx, gridy, gridwidth, gridheight, weightx,
 * weighty, anchor, fill, ipadx, ipady, insets, 
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		return new java.beans.PropertyDescriptor[] {
				createFieldPropertyDescriptor("gridx", GridBagConstraints.class.getField("gridx"), new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resconstraints.getString("gridbagconstraints.gridx"), //$NON-NLS-1$
						SHORTDESCRIPTION, resconstraints.getString("gridbagconstraints.gridx.Desc"), //$NON-NLS-1$						
				}),
				createFieldPropertyDescriptor("gridy", GridBagConstraints.class.getField("gridy"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resconstraints.getString("gridbagconstraints.gridy"), //$NON-NLS-1$
						SHORTDESCRIPTION, resconstraints.getString("gridbagconstraints.gridy.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("gridwidth", GridBagConstraints.class.getField("gridwidth"), new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resconstraints.getString("gridbagconstraints.gridwidth"), //$NON-NLS-1$
						SHORTDESCRIPTION, resconstraints.getString("gridbagconstraints.gridwidth.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("gridheight", GridBagConstraints.class.getField("gridheight"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resconstraints.getString("gridbagconstraints.gridheight"), //$NON-NLS-1$
						SHORTDESCRIPTION, resconstraints.getString("gridbagconstraints.gridheight.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("weightx", GridBagConstraints.class.getField("weightx"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resconstraints.getString("gridbagconstraints.weightx"), //$NON-NLS-1$
						SHORTDESCRIPTION, resconstraints.getString("gridbagconstraints.weightx.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("weighty", GridBagConstraints.class.getField("weighty"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resconstraints.getString("gridbagconstraints.weighty"), //$NON-NLS-1$
						SHORTDESCRIPTION, resconstraints.getString("gridbagconstraints.weighty.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("anchor", GridBagConstraints.class.getField("anchor"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resconstraints.getString("gridbagconstraints.anchor"), //$NON-NLS-1$
						SHORTDESCRIPTION, resconstraints.getString("gridbagconstraints.anchor.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("fill", GridBagConstraints.class.getField("fill"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resconstraints.getString("gridbagconstraints.fill"), //$NON-NLS-1$
						SHORTDESCRIPTION, resconstraints.getString("gridbagconstraints.fill.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("ipadx", GridBagConstraints.class.getField("ipadx"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resconstraints.getString("gridbagconstraints.ipadx"), //$NON-NLS-1$
						SHORTDESCRIPTION, resconstraints.getString("gridbagconstraints.ipadx.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("ipady", GridBagConstraints.class.getField("ipady"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resconstraints.getString("gridbagconstraints.ipady"), //$NON-NLS-1$
						SHORTDESCRIPTION, resconstraints.getString("gridbagconstraints.ipady.Desc"), //$NON-NLS-1$							
				}),
				createFieldPropertyDescriptor("insets", GridBagConstraints.class.getField("insets"), new Object[]{ //$NON-NLS-1$ //$NON-NLS-2$
						DISPLAYNAME, resconstraints.getString("gridbagconstraints.insets"), //$NON-NLS-1$
						SHORTDESCRIPTION, resconstraints.getString("gridbagconstraints.insets.Desc"), //$NON-NLS-1$											
				}),
		};
	} catch (SecurityException e) {
		handleException(e);
	} catch (NoSuchFieldException e) {
		handleException(e);
	}
	return null;
}
}
