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
 *  $RCSfile: TableTreeColumnContainmentHandler.java,v $
 *  $Revision: 1.5 $  $Date: 2005-12-14 21:44:40 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaRefFactory;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;

/**
 * Containment Handler for a SWT TableColumn
 * 
 * @since 1.2.0
 */
public class TableTreeColumnContainmentHandler extends WidgetContainmentHandler {

	/**
	 * @param model
	 * 
	 * @since 1.2.0
	 */
	public TableTreeColumnContainmentHandler(Object model) {
		super(model);
	}

	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation,
			final EditDomain domain) throws StopRequestException {
		if (creation && child instanceof IJavaObjectInstance && parent instanceof IJavaObjectInstance) {
			IJavaObjectInstance tjo = (IJavaObjectInstance) child;
			if (!tjo.isSetAllocation() && !tjo.isAnyFeatureSet()) {
				// If it has an allocation or properties we can't convert it. It is had been fine-tuned. Otherwise we will create a new one if not
				// compatible with current parent.
				ResourceSet rset = EMFEditDomainHelper.getResourceSet(domain);
								
				// If not a tree column and parent is a tree, change to a tree column.
				JavaHelpers tableClass = JavaRefFactory.eINSTANCE.reflectType("org.eclipse.swt.widgets.Table", rset); //$NON-NLS-1$
				String childClassname = tjo.getJavaType().getQualifiedNameForReflection();
				if (!"org.eclipse.swt.widgets.TableColumn".equals(childClassname) && tableClass.isInstance(parent)) { //$NON-NLS-1$
					child = BeanUtilities.createJavaObject(JavaRefFactory.eINSTANCE.reflectType("org.eclipse.swt.widgets.TableColumn", rset), null, (JavaAllocation) null); //$NON-NLS-1$
				} else {
					JavaHelpers treeClass = JavaRefFactory.eINSTANCE.reflectType("org.eclipse.swt.widgets.Tree", rset); //$NON-NLS-1$
					if (!"org.eclipse.swt.widgets.TreeColumn".equals(childClassname) && treeClass.isInstance(parent)) { //$NON-NLS-1$
						child = BeanUtilities.createJavaObject(JavaRefFactory.eINSTANCE.reflectType("org.eclipse.swt.widgets.TreeColumn", rset), null, (JavaAllocation) null); //$NON-NLS-1$
					}
				}
			}
			
			final IJavaObjectInstance jo = (IJavaObjectInstance) child;
			final EStructuralFeature sf_defaultWidth = jo.eClass().getEStructuralFeature("width"); //$NON-NLS-1$
			if (sf_defaultWidth != null && !jo.eIsSet(sf_defaultWidth)) {

				preCmds.append(new CommandWrapper() {

					protected boolean prepare() {
						return true;
					}

					public void execute() {
						IJavaInstance intObj = BeanUtilities.createJavaObject((JavaHelpers) sf_defaultWidth.getEType(), JavaEditDomainHelper
								.getResourceSet(domain), "60"); //$NON-NLS-1$
						RuledCommandBuilder cb = new RuledCommandBuilder(domain);
						cb.applyAttributeSetting(jo, sf_defaultWidth, intObj);
						command = cb.getCommand();
						command.execute();
					}
				});
			}
			
			// If we are dropping a column then we should have headers and lines.
			final IJavaObjectInstance pjo = (IJavaObjectInstance) parent;
			final EStructuralFeature sf_headerVisible = pjo.eClass().getEStructuralFeature("headerVisible"); //$NON-NLS-1$
			final EStructuralFeature sf_linesVisible = pjo.eClass().getEStructuralFeature("linesVisible"); //$NON-NLS-1$
			if (sf_headerVisible != null && sf_linesVisible != null && (!pjo.eIsSet(sf_headerVisible) || !pjo.eIsSet(sf_linesVisible))) {
				preCmds.append(new CommandWrapper() {

					protected boolean prepare() {
						return true;
					}

					public void execute() {
						CommandBuilder cb = new CommandBuilder();
						if (!pjo.eIsSet(sf_headerVisible)) {
							IJavaInstance booleanObj = BeanUtilities.createJavaObject((JavaHelpers) sf_headerVisible.getEType(), JavaEditDomainHelper
									.getResourceSet(domain), "true"); //$NON-NLS-1$
							cb.applyAttributeSetting(pjo, sf_headerVisible, booleanObj);
						}
						if (!pjo.eIsSet(sf_linesVisible)) {
							IJavaInstance booleanObj = BeanUtilities.createJavaObject((JavaHelpers) sf_linesVisible.getEType(), JavaEditDomainHelper
									.getResourceSet(domain), "true"); //$NON-NLS-1$
							cb.applyAttributeSetting(pjo, sf_linesVisible, booleanObj);
						}
						command = cb.getCommand();
						if (command != null)
							command.execute();
					}
				});
			}
		}
		return super.contributeToDropRequest(parent, child, preCmds, postCmds, creation, domain);
	}

}
