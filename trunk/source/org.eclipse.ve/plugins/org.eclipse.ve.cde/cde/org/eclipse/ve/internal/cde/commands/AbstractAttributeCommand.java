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
package org.eclipse.ve.internal.cde.commands;
/*
 *  $RCSfile: AbstractAttributeCommand.java,v $
 *  $Revision: 1.5 $  $Date: 2005-12-14 21:25:58 $ 
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.ve.internal.propertysheet.common.commands.AbstractCommand;

abstract public class AbstractAttributeCommand extends AbstractCommand {
	protected EStructuralFeature feature;
	private List values;
	private EObject target;

	public AbstractAttributeCommand(String name) {
		super(name);
	}

	public AbstractAttributeCommand() {
		super();
	}

	public boolean prepare() {
		return feature != null && target != null;
	}

	public EObject getTarget() {
		return target;
	}

	public void setTarget(EObject aTarget) {
		target = aTarget;
	}

	protected List getAttributeSettingValues() {
		return values;
	}

	public void redo() {
		execute();
	}

	public void setAttribute(EStructuralFeature attribute) {
		feature = attribute;
	}
	public void setAttributeSettingValue(Object newValue) {
		values = new ArrayList(1);
		values.add(newValue);
	}
	public void setAttributeSettingValue(List newValues) {
		values = newValues;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getClass().getName()+ " (" + (feature != null ? feature.getName() : "?") + ") : " + target + (getLabel() != null ? " - " + getLabel() : ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}

}
