package org.eclipse.ve.internal.java.codegen.java;
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
 *  $RCSfile: ObjectDecoder.java,v $
 *  $Revision: 1.5 $  $Date: 2004-02-05 16:13:50 $ 
 */


import java.util.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;



/**
 *  An Object Decoder will only work on Simple Attributes
 */
public class ObjectDecoder extends AbstractExpressionDecoder {
     

public ObjectDecoder (CodeExpressionRef expr, IBeanDeclModel model, IDiagramModelInstance cm, BeanPart part) {
	super (expr,model,cm,part) ;
}

public ObjectDecoder() {
	super () ;
}

public static EStructuralFeature getAllocationFeature (IJavaObjectInstance obj) {
	if (obj != null) {
		return obj.eClass().getEStructuralFeature(AllocationFeatureMapper.ALLOCATION_FEATURE) ;
	}
	return null ;
}

protected void initialFeatureMapper(){	
	     if (fExprRef.isStateSet(CodeExpressionRef.STATE_INIT_EXPR)) // initialization expression
	     	fFeatureMapper = new AllocationFeatureMapper();
	     else  // Use a Simple Attribute Mapper
            fFeatureMapper = new PropertyFeatureMapper()  ;                      
}

protected void initialFeatureMapper(EStructuralFeature sf) {
	if (sf.equals(getAllocationFeature((IJavaObjectInstance)fbeanPart.getEObject()))){
		fFeatureMapper = new AllocationFeatureMapper() ;
	}
	else {
         fFeatureMapper = new PropertyFeatureMapper() ;        
	}
	fFeatureMapper.setFeature(sf) ;
}


protected void initialDecoderHelper() {
	// Bind an Attribute Mapper
	if (fFeatureMapper.getFeature(null).equals(getAllocationFeature((IJavaObjectInstance)fbeanPart.getEObject())))
		fhelper = new ConstructorDecoderHelper(fbeanPart, fExpr,  fFeatureMapper, this);
	else if (isChildValue(fFeatureMapper.getFeature(null), (IJavaObjectInstance)fbeanPart.getEObject(), false))  {
        JavaVEPlugin.log("ObjectDecoder using *Delegate Helper* for "+fFeatureMapper.getFeature(null), //$NON-NLS-1$
        MsgLogger.LOG_FINE) ;
        fhelper = new ChildRelationshipDecoderHelper(fbeanPart, fExpr,  fFeatureMapper, this);
    }
    else
	   fhelper = new SimpleAttributeDecoderHelper(fbeanPart, fExpr,  fFeatureMapper,this) ;	
}


protected boolean isAnyAttributeSet(IJavaObjectInstance obj) {
	if (obj == null)
		return false;
	Iterator itr = ((JavaClass)obj.eClass()).getAllProperties().iterator();
	while (itr.hasNext()) {
		EStructuralFeature sf = (EStructuralFeature) itr.next();
		if (obj.eIsSet(sf))
			if (!MethodTextGenerator.isNotToBeCodedAttribute(sf, obj))
				return true;
	}
	return false;
}

protected boolean isSimpleObject(IJavaObjectInstance obj) {
    if (obj == null) return true ;
    else {    
       String type = obj.getJavaType().getQualifiedName() ;
       if (type.equals("java.lang.String")) return true ;
    }
    return false ;
}


/**
 * Determine if the the SF is a heavy weight or not
 * @param check if any attributes are set.  During decode time, attributes may not be set yet.
 */
protected boolean isChildValue(EStructuralFeature sf, IJavaInstance val, boolean checkAttributes) {
    if (sf == null || val == null || !(val instanceof IJavaObjectInstance)) return false ;
    // Known SF to be ignored
    if (MethodTextGenerator.isNotToBeCodedAttribute(sf,(EObject)val)) return false ;
    // Known Instances to be ignored
    if (isSimpleObject((IJavaObjectInstance)val)) return false ;
    if (checkAttributes)
      if (! isAnyAttributeSet((IJavaObjectInstance)val)) return false ;
    
    return true ;
}

/**
 * A simple attribute is a component that has no special settings on it.
 * If it does has settings on it (and not a child), we need to create a BeanPart and 
 * Treat it as a regular bean instance (e.g., setLayout(xxx) ; where xxx was initialized with
 * some attributes.
 */
protected void addNonSimpleAttribute (IJavaObjectInstance obj, List list) {
    
    if (obj == null) return  ;
    
	Iterator itr = ((JavaClass)obj.eClass()).getAllProperties().iterator() ;
	while (itr.hasNext()) {
		EStructuralFeature sf = (EStructuralFeature) itr.next() ;
		if (obj.eIsSet(sf)) {
			// TODO "RLK: I can't tell if this could handle isMany features or not, so I will treat it as if it did
			Object val = obj.eGet(sf);
			if (sf.isMany()) {
				Iterator vals = ((List) val).iterator();
				while (vals.hasNext())
					addNonSimpleAttribute(list, sf, vals.next());					
			} else
				addNonSimpleAttribute(list, sf, val);
		}
	}
}

private void addNonSimpleAttribute(List list, EStructuralFeature sf, Object val) {
	if (val instanceof IJavaInstance && isChildValue(sf, (IJavaInstance) val, true)) {
		list.add(val);
		list.add(sf);
	}
}


/**
 *  Get the first level descendents
 */
public List getChildren(IJavaObjectInstance component) {
	// Simple Object has not childrens
	List v = new ArrayList() ;
//  In the future we should support adding non-simple attribute (an instance of an attribute)
	addNonSimpleAttribute(component,v) ;
	return v ;
}



/**
 * @see org.eclipse.ve.internal.java.codegen.java.AbstractExpressionDecoder#isPriorityCacheable()
 */
protected boolean isPriorityCacheable() {
	return true;
}

/* (non-Javadoc)
 * @see org.eclipse.ve.internal.java.codegen.java.IJVEDecoder#createCodeGenInstanceAdapter()
 */
public ICodeGenAdapter createCodeGenInstanceAdapter(BeanPart bp) {
	return new BeanDecoderAdapter(bp);
}

/* (non-Javadoc)
 * @see org.eclipse.ve.internal.java.codegen.java.IJVEDecoder#createThisCodeGenInstanceAdapter(org.eclipse.ve.internal.java.codegen.model.BeanPart)
 */
public ICodeGenAdapter createThisCodeGenInstanceAdapter(BeanPart bp) {
	EObject bean = bp.getEObject();
	return new ThisBeanDecoderAdapter(bean, bp);
}

}


