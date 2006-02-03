/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ExpandableCompositeBeanInfo.java,v $
 *  $Revision: 1.1 $  $Date: 2006-02-03 15:22:02 $ 
 */
package org.eclipse.ui.forms.widgets.beaninfo;

import java.beans.BeanDescriptor;

import org.eclipse.swt.widgets.beaninfo.IvjBeanInfo;
import org.eclipse.swt.widgets.beaninfo.SweetHelper;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

public class ExpandableCompositeBeanInfo extends IvjBeanInfo {

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return ExpandableComposite.class;
	}	
	
	public BeanDescriptor getBeanDescriptor() {
		BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
		descriptor.setValue(
			SweetHelper.STYLE_BITS_ID,
			new Object[] [] {
				{ "expansion toggle" , ExpandableCompositeMessages.getString("ExpandableCompositeBeanInfo.StyleBits.ExpansionToggle.Name"), Boolean.FALSE, new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
					ExpandableCompositeMessages.getString("ExpandableCompositeBeanInfo.StyleBits.ExpansionToggle.Value.Twistie") , "org.eclipse.ui.forms.widgets.ExpandableComposite.TWISTIE" , new Integer(ExpandableComposite.TWISTIE) , //$NON-NLS-1$ //$NON-NLS-2$
					ExpandableCompositeMessages.getString("ExpandableCompositeBeanInfo.StyleBits.ExpansionToggle.Value.TreeNode") , "org.eclipse.ui.forms.widgets.ExpandableComposite.TREE_NODE" , new Integer(ExpandableComposite.TREE_NODE) //$NON-NLS-1$ //$NON-NLS-2$										
				} },
				{ "initial expansion" , ExpandableCompositeMessages.getString("ExpandableCompositeBeanInfo.StyleBits.InitialExpansion.Name"), Boolean.TRUE, new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
					ExpandableCompositeMessages.getString("ExpandableCompositeBeanInfo.StyleBits.InitialExpansion.Value") , "org.eclipse.ui.forms.widgets.ExpandableComposite.EXPANDED" , new Integer(ExpandableComposite.EXPANDED) //$NON-NLS-1$ //$NON-NLS-2$
				} },					
				{ "title bar" , ExpandableCompositeMessages.getString("ExpandableCompositeBeanInfo.StyleBits.TitleBar.Name"), Boolean.FALSE, new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
					ExpandableCompositeMessages.getString("ExpandableCompositeBeanInfo.StyleBits.TitleBar.Value.TitleBar") , "org.eclipse.ui.forms.widgets.ExpandableComposite.TITLE_BAR" , new Integer(ExpandableComposite.TITLE_BAR) , //$NON-NLS-1$ //$NON-NLS-2$
					ExpandableCompositeMessages.getString("ExpandableCompositeBeanInfo.StyleBits.TitleBar.Value.ShortTitle") , "org.eclipse.ui.forms.widgets.ExpandableComposite.SHORT_TITLE_BAR" , new Integer(ExpandableComposite.SHORT_TITLE_BAR) , //$NON-NLS-1$ //$NON-NLS-2$					
					ExpandableCompositeMessages.getString("ExpandableCompositeBeanInfo.StyleBits.TitleBar.Value.NoTitle") , "org.eclipse.ui.forms.widgets.ExpandableComposite.NO_TITLE" , new Integer(ExpandableComposite.NO_TITLE) //$NON-NLS-1$ //$NON-NLS-2$					
				} },				
				{ "title focus" , ExpandableCompositeMessages.getString("ExpandableCompositeBeanInfo.StyleBits.FocusTitle.Name"), Boolean.FALSE, new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
					ExpandableCompositeMessages.getString("ExpandableCompositeBeanInfo.StyleBits.FocusTitle.Value") , "org.eclipse.ui.forms.widgets.ExpandableComposite.FOCUS_TITLE" , new Integer(ExpandableComposite.FOCUS_TITLE) //$NON-NLS-1$ //$NON-NLS-2$
				} },
				{ "compact size" , ExpandableCompositeMessages.getString("ExpandableCompositeBeanInfo.StyleBits.CompactSize.Name"), Boolean.FALSE, new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
					ExpandableCompositeMessages.getString("ExpandableCompositeBeanInfo.StyleBits.CompactSize.Value") , "org.eclipse.ui.forms.widgets.ExpandableComposite.COMPACT" , new Integer(ExpandableComposite.COMPACT) //$NON-NLS-1$ //$NON-NLS-2$
				} },	
				{ "indent client origin" , ExpandableCompositeMessages.getString("ExpandableCompositeBeanInfo.StyleBits.ClientIndent.Name"), Boolean.FALSE, new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
					ExpandableCompositeMessages.getString("ExpandableCompositeBeanInfo.StyleBits.ClientIndent.Value") , "org.eclipse.ui.forms.widgets.ExpandableComposite.CLIENT_INDENT" , new Integer(ExpandableComposite.CLIENT_INDENT) //$NON-NLS-1$ //$NON-NLS-2$
				} },					
				{ "left text alignment" , ExpandableCompositeMessages.getString("ExpandableCompositeBeanInfo.StyleBits.LeftTextAlignment.Name"), Boolean.FALSE, new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
					ExpandableCompositeMessages.getString("ExpandableCompositeBeanInfo.StyleBits.LeftTextAlignment.Value") , "org.eclipse.ui.forms.widgets.ExpandableComposite.LEFT_TEXT_CLIENT_ALIGNMENT" , new Integer(ExpandableComposite.LEFT_TEXT_CLIENT_ALIGNMENT) //$NON-NLS-1$ //$NON-NLS-2$
				} },				
			}
		);
		SweetHelper.mergeSuperclassStyleBits(descriptor);
		return descriptor;
	}
}
