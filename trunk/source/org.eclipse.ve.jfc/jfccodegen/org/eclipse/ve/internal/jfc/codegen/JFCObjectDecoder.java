package org.eclipse.ve.internal.jfc.codegen;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JFCObjectDecoder.java,v $
 *  $Revision: 1.2 $  $Date: 2004-02-10 23:37:16 $ 
 */


import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance;
import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.java.ObjectDecoder;
import org.eclipse.ve.internal.java.codegen.java.SimpleAttributeDecoderHelper;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;



/**
 *  An Object Decoder will only work on Simple Attributes
 */
public class JFCObjectDecoder extends ObjectDecoder  {

    

public JFCObjectDecoder (CodeExpressionRef expr, IBeanDeclModel model, IDiagramModelInstance cm, BeanPart part) {
	super (expr,model,cm,part) ;
}

public JFCObjectDecoder() {
	super () ;
}


protected void initialFeatureMapper(){	
	   // Use a Simple Attribute Mapper
	super.initialFeatureMapper() ;                  
}

protected void initialFeatureMapper(EStructuralFeature sf) {
         fFeatureMapper = new AttributeFeatureMapper() ;
         fFeatureMapper.setFeature(sf) ;
}

protected void initialDecoderHelper() {
	// Bind an Attribute Mapper
	if (isChildValue(fFeatureMapper.getFeature(null), (IJavaObjectInstance)fbeanPart.getEObject(), false))  {
		JavaVEPlugin.log("ObjectDecoder using *Delegate Helper* for "+fFeatureMapper.getFeature(null), //$NON-NLS-1$
		MsgLogger.LOG_FINE) ;
		fhelper = new JFCChildRelationshipDecoderHelper(fbeanPart, fExpr,  fFeatureMapper, this);
	}
	else
		super.initialDecoderHelper() ;	
}

protected boolean isSimpleObject(IJavaObjectInstance obj) {
    if (super.isSimpleObject(obj)) return true ;
    else {
       String type = obj.getJavaType().getQualifiedName() ;
       if (type.equals("java.awt.Dimension") || //$NON-NLS-1$
	       type.equals("java.awt.Rectangle"))   //$NON-NLS-1$
	       return true ;																		  
    }
    return false ;
}

/* (non-Javadoc)
 * @see org.eclipse.ve.internal.java.codegen.java.IJVEDecoder#createCodeGenInstanceAdapter()
 */
public ICodeGenAdapter createCodeGenInstanceAdapter(BeanPart bp) {
	return new JFCBeanDecoderAdapter(bp);
}




}


