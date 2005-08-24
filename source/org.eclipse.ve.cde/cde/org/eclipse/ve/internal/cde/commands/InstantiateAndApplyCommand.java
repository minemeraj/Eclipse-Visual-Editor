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
 *  $RCSfile: InstantiateAndApplyCommand.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:48 $ 
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

public class InstantiateAndApplyCommand extends ApplyAttributeSettingCommand {
	protected EClass emfClass;
	protected EObject fNewInstance;

	public InstantiateAndApplyCommand(String name) {
		super(name);
	}

	public InstantiateAndApplyCommand() {
		super();
	}

	protected List getAttributeSettingValues() {
		if (super.getAttributeSettingValues() == null) {
			List values = new ArrayList(1);
			values.add(getNewInstance());
			setAttributeSettingValue(values);
		}
		return super.getAttributeSettingValues();
	}

	public EObject getNewInstance() {
		if (fNewInstance == null) {
			fNewInstance = emfClass.getEPackage().getEFactoryInstance().create(emfClass);
		}
		return fNewInstance;
	}

	public void setClass(EClass emfClass) {
		this.emfClass = emfClass;
	}
}
