/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * Created on Jun 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.eclipse.ve.internal.java.codegen.wizards;

import java.util.HashMap;

/*
 *  $RCSfile: IVisualClassCreationSourceGenerator.java,v $
 *  $Revision: 1.8 $  $Date: 2005-08-24 23:30:48 $ 
 */

/**
 * @author sri, pmuldoon
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IVisualClassCreationSourceGenerator {
	/**
	 * Should return the source required for the input type.
	 * It is upto the implementor to generate source as per
	 * its requirement.
	 * 
	 * 
	 * @param defaultGeneratedType
	 * @return
	 */
    // pmuldoon: Added hashmap constants, and argumentMatrix argument
    public static final String CREATE_MAIN = "createMain"; //$NON-NLS-1$
    public static final String CREATE_SUPER_CONSTRUCTORS = "createSuperConstructors"; //$NON-NLS-1$
    public static final String CREATE_INHERITED_ABSTRACT = "createInheritedAbstract"; //$NON-NLS-1$
    public static final String TARGET_PACKAGE_NAME = "targetPackageName"; //$NON-NLS-1$
    public static final String TARGET_TYPE = "targetType"; //$NON-NLS-1$
    
    public String generateSource(String typeName, String superClassName,
            HashMap argumentMatrix);
}
