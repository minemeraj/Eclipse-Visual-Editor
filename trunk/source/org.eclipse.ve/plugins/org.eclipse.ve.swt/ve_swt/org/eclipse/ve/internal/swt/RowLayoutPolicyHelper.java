/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: RowLayoutPolicyHelper.java,v $
 *  $Revision: 1.1 $  $Date: 2004-01-28 10:31:58 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.List;
import org.eclipse.gef.commands.Command;
import org.eclipse.ve.internal.cde.commands.CommandBuilder;

import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
 
/**
 * 
 * @since 1.0.0
 */
public class RowLayoutPolicyHelper extends LayoutPolicyHelper {


public RowLayoutPolicyHelper(VisualContainerPolicy ep) {
	super(ep);
}
public RowLayoutPolicyHelper(){
}

protected void cancelConstraints(CommandBuilder commandBuilder, List children) {
	// TODO Auto-generated method stub
}

public Command getChangeConstraintCommand(List children, List constraints) {
	// TODO Auto-generated method stub
	return null;
}

public List getDefaultConstraint(List children) {
	// TODO Auto-generated method stub
	return null;
}

}
