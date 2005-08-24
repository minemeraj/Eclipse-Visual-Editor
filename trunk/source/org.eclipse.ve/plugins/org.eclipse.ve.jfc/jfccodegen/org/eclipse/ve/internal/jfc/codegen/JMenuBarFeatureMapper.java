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
package org.eclipse.ve.internal.jfc.codegen;
/*
 *  $RCSfile: JMenuBarFeatureMapper.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:38:13 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.dom.Statement;

/**
 * @author gmendel
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class JMenuBarFeatureMapper extends ContainerFeatureMapper {

	public EStructuralFeature getFeature(Statement expr) {

		if (fSF != null)
			return fSF;

		if (fRefObj == null || expr == null)
			return null;

		getMethodName(expr);

		fSF = ((EObject) fRefObj).eClass().getEStructuralFeature(JMENUBAR_FEATURE_NAME);
		fSFname = fMethodName;

		return fSF;
	}

}
