/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IRuleProvider.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */
package org.eclipse.ve.internal.java.vce.rules;

import org.eclipse.ve.internal.cde.rules.IRule;

/**
 * @author Gili Mendel
 *
 * A rule provider is responsible to generate/cache a rule Object
 * Rules can be created from an extending plugin, .java source etc.
 *
 */
public interface IRuleProvider {

	public IRule getRule();
	public long getTimeStamp();
	// This is string, as it could potentialy be outside the workspace
	public String getSourceLocation();
	public String getStyle();
	public String getRuleID();
	public String getProviderID();
}
