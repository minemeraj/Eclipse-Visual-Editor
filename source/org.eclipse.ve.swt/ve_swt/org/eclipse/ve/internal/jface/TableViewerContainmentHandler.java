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
 *  $RCSfile: TableViewerContainmentHandler.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-24 21:36:04 $ 
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
 * TableViewer containment handler. This handles dropping a table viewer onto a Composite or a Table.
 * In case of a composite it creates a TableViewer and an implicit tree. In case of a Table, it
 * creates a TableViewer with the explicit table as its table setting.
 * 
 * @since 1.2.0
 */
public class TableViewerContainmentHandler implements IContainmentHandler {

	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation, EditDomain domain) throws NoAddException {
		if (creation) {
			if (parent instanceof IJavaObjectInstance) {
				IJavaObjectInstance pjo = (IJavaObjectInstance) parent;
				IJavaObjectInstance cjo = (IJavaObjectInstance) child; // Assuming it is a tableviewer because this handler is being called.
				// Need to check for table before composite because table is an instance of composite.
				ResourceSet rset = EMFEditDomainHelper.getResourceSet(domain);
				JavaClass tableClass = (JavaClass) JavaRefFactory.eINSTANCE.reflectType("org.eclipse.swt.widgets.Table", rset);
				if (tableClass.isInstance(pjo)) {
					// Dropping table viewer onto a table.
					return dropOnTable(pjo, cjo, preCmds, domain);
				} else {
					JavaHelpers compositeClass = JavaRefFactory.eINSTANCE.reflectType("org.eclipse.swt.widgets.Composite", rset);
					if (compositeClass.isInstance(pjo)) {
						// Dropping table viewer onto a composite.
						return dropOnComposite(pjo, cjo, preCmds, tableClass, rset);
					}
				}
			}
		}
		throw new NoAddException("Parent not valid for a TableViewer");
	}

	/*
	 * Dropping on a composite.
	 */
	private Object dropOnComposite(IJavaObjectInstance parent, IJavaObjectInstance tableViewer, CommandBuilder preCmds, JavaClass treeClass,
			ResourceSet rset) {
		// Set the allocation to the parent.
		WidgetContainmentHandler.processAllocation(parent, tableViewer, preCmds);
		// Create a table with implicit allocation to the table viewer, and set it into the table feature.
		EStructuralFeature tableFeature = tableViewer.eClass().getEStructuralFeature("table");
		ImplicitAllocation allocation = InstantiationFactory.eINSTANCE.createImplicitAllocation(tableViewer, tableFeature);
		IJavaInstance table = BeanUtilities.createJavaObject(treeClass, rset, allocation);
		preCmds.applyAttributeSetting(tableViewer, tableFeature, table);
		return table;
	}

	/*
	 * Dropping on a table.
	 */
	private Object dropOnTable(IJavaObjectInstance parentTable, IJavaObjectInstance tableViewer, CommandBuilder preCmds, EditDomain domain) {
		if (!tableViewer.isSetAllocation()) {
			// Needs an allocation. Use default of new Widget(parent, SWT.NONE);
			ParseTreeAllocation parseTreeAllocation = InstantiationFactory.eINSTANCE.createParseTreeAllocation();
			PTClassInstanceCreation classInstanceCreation = InstantiationFactory.eINSTANCE.createPTClassInstanceCreation(tableViewer.getJavaType()
					.getQualifiedNameForReflection(), null);
			PTInstanceReference parentReference = InstantiationFactory.eINSTANCE.createPTInstanceReference(parentTable);
			classInstanceCreation.getArguments().add(parentReference);
			parseTreeAllocation.setExpression(classInstanceCreation);
			preCmds.applyAttributeSetting(tableViewer, JavaInstantiation.getAllocationFeature(tableViewer), parseTreeAllocation);
		} else if (tableViewer.isParseTreeAllocation())
			preCmds.append(new EnsureCorrectParentCommand(tableViewer, parentTable));

		// Set the new table viewer's "table" feature to the parent table.
		EStructuralFeature treeFeature = tableViewer.eClass().getEStructuralFeature("table");
		preCmds.applyAttributeSetting(tableViewer, treeFeature, parentTable);

		// Assign membership of the new TableViewer to be relative to the table.
		RuledCommandBuilder rcb = new RuledCommandBuilder(domain);
		rcb.assignMembership(tableViewer, parentTable);
		preCmds.append(rcb.getCommand());
		return null; // There is no child in this case because the parent is a table already.
	}

}
