/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IPropertyEventDecoder.java,v $
 *  $Revision: 1.4 $  $Date: 2005-02-15 23:28:35 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import org.eclipse.ve.internal.jcm.PropertyEvent;

/**
 * @author gmendel
 */
public interface IPropertyEventDecoder extends IJVEDecoder {


	public void addChangeListener(IExpressionChangeListener l) ;
	public void removeChangeListener(IExpressionChangeListener l) ;
	public void deleteFromSrc() ;
	public void setPropertyEvent(PropertyEvent c) ;
	public PropertyEvent getPropertyEvent() ;

}
