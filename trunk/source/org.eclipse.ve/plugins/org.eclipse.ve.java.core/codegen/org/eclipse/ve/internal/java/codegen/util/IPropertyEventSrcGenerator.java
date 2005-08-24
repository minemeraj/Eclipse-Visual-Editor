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
 *  $RCSfile: IPropertyEventSrcGenerator.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:47 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;

import org.eclipse.ve.internal.jcm.PropertyEvent;

/**
 * @author gmendel
 */
public interface IPropertyEventSrcGenerator extends IEventSrcGenerator{

	public String generatePropertiesBlocks(PropertyEvent[] props) ;

}
