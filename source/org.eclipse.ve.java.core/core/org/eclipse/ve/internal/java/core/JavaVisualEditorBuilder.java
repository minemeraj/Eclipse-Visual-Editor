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
 *  Created Jan 20, 2005 by Gili Mendel
 * 
 *  $RCSfile: JavaVisualEditorBuilder.java,v $
 *  $Revision: 1.11 $  $Date: 2005-06-28 20:13:15 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.jem.workbench.utility.JemProjectUtilities;
 

/**
 * @since 1.1.0
 * 
 * @deprecated with Eclipse 3.1 we should not require a builder... a clean
 *             notification can be used. This will be removed in VE 1.2
 */
public class JavaVisualEditorBuilder extends IncrementalProjectBuilder {

	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
		// Get rid of outselves. (Go away). Suicide.
		JemProjectUtilities.removeFromBuildSpec("org.eclipse.ve.java.core.vebuilder", getProject());
		return null;
	}
}
