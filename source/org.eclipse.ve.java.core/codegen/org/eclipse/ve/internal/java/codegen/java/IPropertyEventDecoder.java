/*
 * Created on Jun 6, 2003
 * by gmendel
 *
*******************************************************************************
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
 *  $RCSfile: IPropertyEventDecoder.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import org.eclipse.ve.internal.jcm.PropertyEvent;

/**
 * @author gmendel
 */
public interface IPropertyEventDecoder extends IJVEDecoder {


	public void addChangeListener(IExpressionChangeListener l) ;
	public void removeChangeListener(IExpressionChangeListener l) ;
	public void delete() ;
	public void setPropertyEvent(PropertyEvent c) ;
	public PropertyEvent getPropertyEvent() ;

}
