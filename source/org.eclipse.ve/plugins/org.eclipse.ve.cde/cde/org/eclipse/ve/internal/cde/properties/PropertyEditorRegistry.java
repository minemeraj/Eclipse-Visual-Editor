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
package org.eclipse.ve.internal.cde.properties;
/*
 *  $RCSfile: PropertyEditorRegistry.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:12:48 $ 
 */

import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator;
import org.eclipse.ve.internal.cde.decorators.DecoratorsFactory;
import org.eclipse.ve.internal.propertysheet.*;

public class PropertyEditorRegistry {
	static public void registerDefaultEditors() {
		EcorePackage pkg = EcorePackage.eINSTANCE;
		DecoratorsFactory dFact = DecoratorsFactory.eINSTANCE;

		String pid = CDEPlugin.getPlugin().getPluginID();
		String pspid = PSheetPlugin.getPlugin().getBundle().getSymbolicName();

		BasePropertyDecorator bpd = dFact.createBasePropertyDecorator();
		bpd.setCellEditorClassname(pspid + "/" + NumberCellEditor.class.getName() + ":byte"); //$NON-NLS-1$ //$NON-NLS-2$
		bpd.setLabelProviderClassname(pid + "/" + NumberLabelProvider.class.getName()); //$NON-NLS-1$
		pkg.getEByte().getEAnnotations().add(bpd);

		bpd = dFact.createBasePropertyDecorator();
		bpd.setCellEditorClassname(pspid + "/" + NumberCellEditor.class.getName() + ":double"); //$NON-NLS-1$ //$NON-NLS-2$
		bpd.setLabelProviderClassname(pid + "/" + NumberLabelProvider.class.getName()); //$NON-NLS-1$
		pkg.getEDouble().getEAnnotations().add(bpd);

		bpd = dFact.createBasePropertyDecorator();
		bpd.setCellEditorClassname(pspid + "/" + NumberCellEditor.class.getName() + ":float"); //$NON-NLS-1$ //$NON-NLS-2$
		bpd.setLabelProviderClassname(pid + "/" + NumberLabelProvider.class.getName()); //$NON-NLS-1$
		pkg.getEFloat().getEAnnotations().add(bpd);

		bpd = dFact.createBasePropertyDecorator();
		bpd.setCellEditorClassname(pspid + "/" + NumberCellEditor.class.getName() + ":integer"); //$NON-NLS-1$ //$NON-NLS-2$
		bpd.setLabelProviderClassname(pid + "/" + NumberLabelProvider.class.getName()); //$NON-NLS-1$
		pkg.getEInt().getEAnnotations().add(bpd);

		bpd = dFact.createBasePropertyDecorator();
		bpd.setCellEditorClassname(pspid + "/" + NumberCellEditor.class.getName() + ":long"); //$NON-NLS-1$ //$NON-NLS-2$
		bpd.setLabelProviderClassname(pid + "/" + NumberLabelProvider.class.getName()); //$NON-NLS-1$
		pkg.getELong().getEAnnotations().add(bpd);

		bpd = dFact.createBasePropertyDecorator();
		bpd.setCellEditorClassname(pspid + "/" + NumberCellEditor.class.getName() + ":short"); //$NON-NLS-1$ //$NON-NLS-2$
		bpd.setLabelProviderClassname(pid + "/" + NumberLabelProvider.class.getName()); //$NON-NLS-1$
		pkg.getEShort().getEAnnotations().add(bpd);

		bpd = dFact.createBasePropertyDecorator();
		bpd.setCellEditorClassname(pspid + "/" + StringCellEditor.class.getName()); //$NON-NLS-1$
		// String doesn't need a label provider. If there isn't one, it just does toString(), which is good.
		pkg.getEString().getEAnnotations().add(bpd);

		bpd = dFact.createBasePropertyDecorator();
		bpd.setCellEditorClassname(pspid + "/" + BooleanCellEditor.class.getName()); //$NON-NLS-1$
		bpd.setLabelProviderClassname(pid + "/" + BooleanLabelProvider.class.getName()); //$NON-NLS-1$
		pkg.getEBoolean().getEAnnotations().add(bpd);
	}
}
