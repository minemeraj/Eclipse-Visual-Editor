/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ComboViewerContainmentHandler.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-25 19:12:43 $ 
 */
package org.eclipse.ve.internal.jface;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.java.*;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.IContainmentHandler;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

import org.eclipse.ve.internal.swt.EnsureCorrectParentCommand;
import org.eclipse.ve.internal.swt.WidgetContainmentHandler;

/**
 * ComboViewer containment handler. This handles dropping a combo viewer onto a Composite or a Combo.
 * In case of a composite it creates a ComboViewer and an implicit combo. In case of a Combo, it
 * creates a ComboViewer with the explicit combo as its combo setting.
 * 
 * @since 1.2.0
 */
public class ComboViewerContainmentHandler implements IContainmentHandler {

	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation, EditDomain domain) throws NoAddException {
		if (creation) {
			if (parent instanceof IJavaObjectInstance) {
				IJavaObjectInstance pjo = (IJavaObjectInstance) parent;
				IJavaObjectInstance cjo = (IJavaObjectInstance) child; // Assuming it is a comboviewer because this handler is being called.
				// Need to check for combo before composite because combo is an instance of composite.
				ResourceSet rset = EMFEditDomainHelper.getResourceSet(domain);
				JavaClass comboClass = (JavaClass) JavaRefFactory.eINSTANCE.reflectType("org.eclipse.swt.widgets.Combo", rset);
				if (comboClass.isInstance(pjo)) {
					// Dropping combo viewer onto a combo.
					return dropOnCombo(pjo, cjo, preCmds, domain);
				} else {
					JavaHelpers compositeClass = JavaRefFactory.eINSTANCE.reflectType("org.eclipse.swt.widgets.Composite", rset);
					if (compositeClass.isInstance(pjo)) {
						// Dropping combo viewer onto a composite.
						return dropOnComposite(pjo, cjo, preCmds, comboClass, rset);
					}
				}
			}
		}
		throw new NoAddException("Parent not valid for a ComboViewer");
	}

	/*
	 * Dropping on a composite.
	 */
	private Object dropOnComposite(IJavaObjectInstance parent, IJavaObjectInstance comboViewer, CommandBuilder preCmds, JavaClass comboClass,
			ResourceSet rset) {
		// Set the allocation to the parent.
		WidgetContainmentHandler.processAllocation(parent, comboViewer, preCmds);
		// Create a combo with implicit allocation to the combo viewer, and set it into the combo feature.
		EStructuralFeature comboFeature = comboViewer.eClass().getEStructuralFeature("combo");
		ImplicitAllocation allocation = InstantiationFactory.eINSTANCE.createImplicitAllocation(comboViewer, comboFeature);
		IJavaInstance combo = BeanUtilities.createJavaObject(comboClass, rset, allocation);
		preCmds.applyAttributeSetting(comboViewer, comboFeature, combo);
		return combo;
	}

	/*
	 * Dropping on a combo.
	 */
	private Object dropOnCombo(IJavaObjectInstance parentCombo, IJavaObjectInstance comboViewer, CommandBuilder preCmds, EditDomain domain) {
		if (!comboViewer.isSetAllocation()) {
			// Needs an allocation. Use default of new Widget(parent, SWT.NONE);
			ParseTreeAllocation parseTreeAllocation = InstantiationFactory.eINSTANCE.createParseTreeAllocation();
			PTClassInstanceCreation classInstanceCreation = InstantiationFactory.eINSTANCE.createPTClassInstanceCreation(comboViewer.getJavaType().getQualifiedNameForReflection(), null);
			PTInstanceReference parentReference = InstantiationFactory.eINSTANCE.createPTInstanceReference(parentCombo);
			classInstanceCreation.getArguments().add(parentReference);
			parseTreeAllocation.setExpression(classInstanceCreation);
			preCmds.applyAttributeSetting(comboViewer, JavaInstantiation.getAllocationFeature(comboViewer), parseTreeAllocation);
		} else if (comboViewer.isParseTreeAllocation())
			preCmds.append(new EnsureCorrectParentCommand(comboViewer, parentCombo));

		// Set the new combo viewer's "combo" feature to the parent combo.
		EStructuralFeature comboFeature = comboViewer.eClass().getEStructuralFeature("combo");
		preCmds.applyAttributeSetting(comboViewer, comboFeature, parentCombo);

		// Assign membership of the new ComboViewer to be relative to the combo.
		RuledCommandBuilder rcb = new RuledCommandBuilder(domain);
		rcb.assignMembership(comboViewer, parentCombo);
		preCmds.append(rcb.getCommand());
		return null; // There is no child in this case because the parent is a combo already.
	}

}
