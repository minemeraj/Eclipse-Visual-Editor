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
 *  $RCSfile: JFCChildRelationshipDecoderHelper.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:38:12 $ 
 */
import java.util.List;

import org.eclipse.jdt.core.dom.Statement;


import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.java.ChildRelationshipDecoderHelper;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;


public class JFCChildRelationshipDecoderHelper extends ChildRelationshipDecoderHelper {


/**
 * @param bean
 * @param exp
 * @param fm
 * @param owner
 */
public JFCChildRelationshipDecoderHelper(BeanPart bean, Statement exp, IJavaFeatureMapper fm, IExpressionDecoder owner) {
		super(bean, exp, fm, owner);
}

/**
 * @see org.eclipse.ve.internal.java.codegen.java.AbstractIndexedChildrenDecoderHelper#getIndexedEntries()
 */
protected List getIndexedEntries() {
	if(fFmapper!=null && fFmapper.getMethodName()!=null && fFmapper.getMethodName().equals(JTableDecoder.JTABLE_ADDCOLUMN_METHOD))
		if(fFmapper.getFeature(fExpr).isMany() && fAddedPart!=null)
			if (fAddedPart.getBackRefs().length>0)
			    return getComponentsFromConstraintComponents((List)fAddedPart.getBackRefs()[0].getEObject().eGet(fFmapper.getFeature(fExpr)));
	return null;
}


}
