package org.eclipse.ve.internal.cde.commands;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: InstantiateAndApplyCommand.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:07 $ 
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
