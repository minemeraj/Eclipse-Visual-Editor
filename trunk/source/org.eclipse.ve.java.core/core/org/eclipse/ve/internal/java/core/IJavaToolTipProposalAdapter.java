/*
 * Created on May 23, 2003
 * by gmendel
 *
*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IJavaToolTipProposalAdapter.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:23:54 $ 
 */
package org.eclipse.ve.internal.java.core;

import org.eclipse.draw2d.Label;

/**
 * @author gmendel
 */
public interface IJavaToolTipProposalAdapter {
	public final static Class JAVA_ToolTip_Proposal_TYPE = IJavaToolTipProposalAdapter.class ;
	
	public Label getInstanceDisplayInformation() ;
	
	public String getInstanceName() ;
	
	public Label getReturnMethodDisplayInformation() ;		
	
	public String getInitMethodName() ;	
}
