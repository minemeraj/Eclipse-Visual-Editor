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
 *  $RCSfile$
 *  $Revision$  $Date$ 
 */
package org.eclipse.ve.internal.swt;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
 

/**
 * For swt Decorations, to handle the MenuBar in addition to the rest from Composite.
 * @since 1.1.0.1
 */
public class DecorationsContainerPolicy extends CompositeContainerPolicy {

	protected EReference sfMenuBar;
	
	public DecorationsContainerPolicy(EditDomain domain) {
		super(domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain);
		sfMenuBar = JavaInstantiation.getReference(rset, SWTConstants.SF_DECORATIONS_MENU_BAR);
		
	}
	
	protected EStructuralFeature getContainmentSF(List children, Object positionBeforeChild, int requestType) {
		// It could be add/move/orphan. In any of those cases we can't handle a mixture of children.
		if (children.isEmpty())
			return null;
		else if (children.size() == 1) {
			// When only one it can be either a menuBar or something else. 
			return isMenuBar(children.get(0), requestType) ? sfMenuBar : super.getContainmentSF(children, positionBeforeChild, requestType);	
		} else {
			// If there is more than one, if there is any that are a menu, then it is invalid. That is because that would
			// require using more than one feature to the apply and we can't handle that.
			for (Iterator iter = children.iterator(); iter.hasNext();) {
				Object child = iter.next();
				if (isMenuBar(child, requestType))
					return null;
			}
			return super.getContainmentSF(children, positionBeforeChild, requestType);
		}
	}
	
	/*
	 * Is it a menubar (i.e. a Menu with the SWT.MENUBAR setting on it)?
	 * Unfortunately we can't handle fancy constructor or method calls. 
	 * Anything other than this default needs to be done through calls passing
	 * in the feature.
	 * 
	 * Currently a Menu can only have one style setting, so we don't need to worry about
	 * or'ing together of other settings.
	 */
	private boolean isMenuBar(Object child, int requestType) {
		if (sfMenuBar.getEType().isInstance(child)) {
			switch (requestType) {
				case ADD_REQ:
				case CREATE_REQ:
					// Add or create, then we need to look at constructor. This is because it is not
					// already set by us and so we can't easily determine its setting on us.
					IJavaObjectInstance javaChild = (IJavaObjectInstance) child;
					if (javaChild.getAllocation() != null) {
						if (javaChild.getAllocation() instanceof ParseTreeAllocation) {
							PTExpression expression = ((ParseTreeAllocation) javaChild.getAllocation()).getExpression();
							if (expression instanceof PTClassInstanceCreation) {
								PTClassInstanceCreation ptc = (PTClassInstanceCreation) expression;
								List args = ptc.getArguments();
								if (args.size() == 2) {
									PTExpression arg = (PTExpression) args.get(1);
									if (arg instanceof PTFieldAccess) {
										PTFieldAccess ptf = (PTFieldAccess) arg;
										if (ptf.getReceiver() instanceof PTName) {
											if ("org.eclipse.swt.SWT".equals(((PTName) ptf.getReceiver()).getName())) { // $NON_NLS-1$
												if ("BAR".equals(ptf.getField()))
													return true;
											}
										}
											
									}
								}
							}
						}
					}
					break;
				default:
					// The rest (move, orphan, delete) are for already set children, so we can easily check the inverse ref to see if it set to us.
					return InverseMaintenanceAdapter.getFirstReferencedBy((Notifier) child, sfMenuBar) == getContainer();
			}		
		}
		return false;
	}

	protected EStructuralFeature getContainmentSF(Object child, Object positionBeforeChild, int requestType) {
		return isMenuBar(child, requestType) ? sfMenuBar : super.getContainmentSF(child, positionBeforeChild, requestType);
	}	

}
