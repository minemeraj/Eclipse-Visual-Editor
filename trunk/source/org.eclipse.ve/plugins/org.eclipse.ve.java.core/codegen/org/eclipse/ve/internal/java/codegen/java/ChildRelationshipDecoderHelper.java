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
 *  $RCSfile: ChildRelationshipDecoderHelper.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-12 21:44:11 $ 
 */
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.internal.compiler.ast.*;

import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.vce.rules.VCEPostSetCommand;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.codegen.model.BeanDeclModel;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.util.*;



public class ChildRelationshipDecoderHelper extends AbstractIndexedChildrenDecoderHelper {
	
static final String                   NULL_STRING ="null" ;	 //$NON-NLS-1$
	
	protected BeanPart                        fAddedPart = null ;

public ChildRelationshipDecoderHelper(BeanPart bean, Statement exp,  IJavaFeatureMapper fm, IExpressionDecoder owner){
	super (bean,exp,fm,owner) ;
}


/**
 *  We use implicit methods for childrens
 */
public boolean isGenerateOnImplicit() {
	return true ;
}



/*
 * Decoder The Delegate Attribute
 */
protected void add(BeanPart toAdd,BeanPart target) throws CodeGenException {	
      //  Make this relationship in the Composition Model	
      Object previousValue = target.getEObject().eGet(fFmapper.getFeature(null));
      int index = findIndex(toAdd,target);
      toAdd.addToJVEModel() ;      
      // Must do this first, as if we move a component out off FF, need
      // to remove it first. 
	  if (isChildRelationship()) {      
	    toAdd.addBackRef(target, (EReference)fFmapper.getFeature(null)) ;
	    target.addChild(toAdd) ;
	  }	         
      if(previousValue instanceof EList){
      	EList list = (EList) previousValue;
      	if(index<0)
      		list.add(toAdd.getEObject());
      	else{
      		// The list contains the expression being moved. hence the if().
      		if(list.contains(toAdd.getEObject()))
      			list.remove(toAdd.getEObject());
      		else
      			index++;
      		list.add(index, toAdd.getEObject());
      	}
      }else{
        // TODO  Need to stop eSet like this when MOF ordering is in place
        // Set Scoping First        
      	CodeGenUtil.eSet(target.getEObject(), fFmapper.getFeature(null), toAdd.getEObject(), index) ;
      }
      JavaVEPlugin.log("DelegateRelationShipDecoderHelper.add("+toAdd+","+target+")", MsgLogger.LOG_FINE) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
}

/**
 * Try to figure out where ti insert this object, with accordance to its position
 * in the code, in relation to other components.
 */
protected int findIndex(BeanPart toAdd, BeanPart target) {

	if (fOwner.getExprRef().getOffset() < 0)
		return -1;

	Object val = target.getEObject().eGet(fFmapper.getFeature(null));
	if (val instanceof List) {
		List compList = (List) val;
		if (compList == null || compList.size() == 0)
			return -1;
	
		int thisOffset = fOwner.getExprRef().getOffset();
		int result = -1;
		for (int i = 0; i < compList.size(); i++) {
			try {
				EObject cc = (EObject) compList.get(i);
				if(cc.equals(target.getEObject()))	
					continue; // account for presence of refobject in the list.
				ICodeGenAdapter a = (ICodeGenAdapter) EcoreUtil.getExistingAdapter(cc,ICodeGenAdapter.JVE_CODEGEN_EXPRESSION_SOURCE_RANGE);
				int tmpOffset = a.getBDMSourceRange().getOffset();
				if (a != null && thisOffset >= tmpOffset)
					result = i;
			} catch (CodeGenException e) {
			}
		}
		return result;
	}
	return -1;
}

protected BeanPart parseAddedPart(MessageSend expr)  throws CodeGenException {
    
     BeanPart added=null ;
     
    
     Expression[] args = (expr).arguments ;
     if (args==null || args.length != 1) throw new CodeGenException("No Arguments !!! " + expr) ; //$NON-NLS-1$
      
      // Parse the arguments to figure out which bean to add to this container
     if (args[0] instanceof MessageSend)  {
          // Look to see of if this method returns a Bean
          String selector = new String (((MessageSend)args[0]).selector) ;  
          added = fOwner.getBeanModel().getBeanReturned(selector) ;           
     }
     else if (args[0] instanceof SingleNameReference){
            // Simple reference to a bean
            String selector = new String (((SingleNameReference)args[0]).token);
            added = fOwner.getBeanModel().getABean(selector) ;
            if(added == null)
            	added = fOwner.getBeanModel().getABean(BeanDeclModel.constructUniqueName(fOwner.getExprRef().getMethod(),selector));
            	//added = fOwner.getBeanModel().getABean(fOwner.getExprRef().getMethod().getMethodHandle()+"^"+selector);
     }
          
     return added ;    
}

/**
 *   Add new Componet to target Bean,
 */
protected boolean  addComponent () throws CodeGenException {
	
	// TODO  Need to deal with multiple arguments, and nesting
	
	 BeanPart oldAddedPart = fAddedPart ;
    if (!(fExpr instanceof MessageSend)) return false ;
    
    fAddedPart = parseAddedPart((MessageSend)fExpr)  ;
    
    if (oldAddedPart != null)
       if (fAddedPart != oldAddedPart) {
       	oldAddedPart.removeBackRef(fbeanPart,true) ;
       }
          
    	          
    if (fAddedPart!= null) {	
           add(fAddedPart,fbeanPart) ;                  
           return true ;
    }	
    boolean parseOK = fAddedPart != null ;
//    if (!parseOK)
//	   CodeGenUtil.logParsingError(fExpr.toString(), fbeanPart.getInitMethod().getMethodName(), "Could not resolve setting instance",false) ; //$NON-NLS-1$ 
	return parseOK ;
}

/**
 * Returns the beanpart in the BDM which reflects to
 * the respective StructuralFeature(SF). Sometimes the value
 * of the SF is not a single object, in those instances
 * the help of the passed in argument is taken to determine
 * the required value.
 */
protected BeanPart getCurrentAddedPart(Object arg) {
      Object currentVal = fbeanPart.getEObject().eGet(fFmapper.getFeature(fExpr)) ;	
      BeanPart  currentBean=null ;
	if (currentVal != null && currentVal instanceof EObject)  {
		currentBean = fOwner.getBeanModel().getABean((EObject)currentVal) ;		
	}else 
		if(currentVal != null && currentVal instanceof EList) {
			EList addedList = (EList) currentVal;
			Iterator itr = addedList.iterator();
			EObject added = null;
			while(itr.hasNext()){
				EObject ro = (EObject) itr.next();
				if(ro.equals(arg)){
					added = ro;
					break;
				}
			}
			if(added!=null)
				currentBean = fOwner.getBeanModel().getABean((EObject)added);
		}
	
	return currentBean ;  
}

public String primRefreshFromComposition(String expSig) throws CodeGenException {
      
      BeanPart  currentBean=getCurrentAddedPart(null) ;
	if (currentBean ==null || !currentBean.equals(fAddedPart)) {
        // Isolate the constraint argument using the last used constraint
            
          
	  int start = expSig.indexOf(fFmapper.getMethodName())+fFmapper.getMethodName().length()+1 ;	
	  int end = expSig.lastIndexOf(')') ;
	  
	  fAddedPart = currentBean ;
	  
	  String arg = getSourceCodeArg() ;
	  
	  StringBuffer newExp = new StringBuffer(expSig) ;
	  newExp.replace(start,end,arg) ;
 	  fExprSig = newExp.toString() ;
	  return fExprSig ;
	}
	else 
	  return (expSig) ;
}

/**
 *  Deleted from a decoder's point of view is that there is no source code associated
 *  with it. 
 */
public boolean primIsDeleted() {
   
   IJavaObjectInstance obj = (IJavaObjectInstance) fbeanPart.getEObject() ;
   org.eclipse.emf.ecore.EStructuralFeature sf = fFmapper.getFeature(fExpr) ;
   boolean result ;
   if (sf.isMany()) {
       EObject added = fAddedPart.getEObject() ;
       result = !((List) obj.eGet(sf)).contains(added) ;
   }
   else
      result = !obj.eIsSet(fFmapper.getFeature(fExpr)) ;
   
   if (result && fAddedPart != null)
        fAddedPart.removeBackRef(fbeanPart.getEObject(),false) ;
      
   return result ;
}

/**
 *   Go for it
 */
public boolean decode() throws CodeGenException {

   if (fFmapper.getFeature(fExpr) == null ||
	   fExpr == null) throw new CodeGenException ("null Feature:"+fExpr) ; //$NON-NLS-1$

      return addComponent() ;
}

public void delete() {
	unadaptToCompositionModel() ;
	
	org.eclipse.emf.ecore.EStructuralFeature sf = fFmapper.getFeature(fExpr) ;
	IJavaObjectInstance parent = (IJavaObjectInstance)fbeanPart.getEObject() ;	
    if (sf.isMany()) {
        EObject added = fAddedPart.getEObject() ;
       ((List) parent.eGet(sf)).remove(added) ;
    }
    else	
	    parent.eUnset(sf) ; 
    fAddedPart.removeBackRef(parent,true) ;
}


//public Object getPriorityOfExpression(){
//	Integer pri = (Integer)super.getPriorityOfExpression();
//	if(fFmapper!=null && fFmapper.getMethodName()!=null && fFmapper.getMethodName().equals(JTableDecoder.JTABLE_ADDCOLUMN_METHOD)){
//		int indexOfChildInParent = 0;
//		if(fAddedPart!=null){
//			if(fFmapper.getFeature(fExpr).isMany()){
//				List ccs = (List)fAddedPart.getBackRefs()[0].getEObject().eGet(fFmapper.getFeature(fExpr));
//				for(int i=0;i<ccs.size();i++){
//					EObject cc = (EObject)ccs.get(i);
//					if("ConstraintComponent".equals(cc.eClass().getName())) //$NON-NLS-1$
//						cc = CodeGenUtil.getCCcomponent(cc);
//					if(cc.equals(fAddedPart.getEObject())){
//						indexOfChildInParent = i;
//						break;
//					}
//				}
//			}
//		}
//		indexOfChildInParent++;
//		return new Integer(IJavaFeatureMapper.PRIORITY_ADD_CHANGE + pri.intValue() + (IJavaFeatureMapper.INTER_LAYOUT_ADD_PRIORITIES_GAP-indexOfChildInParent));
//	}
//	return new Integer(IJavaFeatureMapper.PRIORITY_IMPLICIT + pri.intValue());
//}

/**
 * @see org.eclipse.ve.internal.java.codegen.java.AbstractIndexedChildrenDecoderHelper#getIndexedEntries()
 */
protected List getIndexedEntries() {
	return null;
}

/**
 * @see org.eclipse.ve.internal.java.codegen.java.AbstractIndexedChildrenDecoderHelper#getIndexedEntry()
 */
protected Object getIndexedEntry() {
	if(fAddedPart!=null)
		return fAddedPart.getEObject();
	return null;
}

/**
 * Copied from SimpleAttributeDecoderHelper. This basically takes care of the special case of 
 * the layout being a box layout, where its constructor expects extra arguments.
 * @see SimpleAttributeDecoderHelper.boxLayoutOverride(String)
 */
private String boxLayoutOveride(String st) {
	int index =  st.indexOf("(,") ; //$NON-NLS-1$
	if (index>=0) {            
		String toAdd = fbeanPart.getSimpleName();
		StringBuffer s = new StringBuffer (st) ;            
		s.replace(index, index+2, "("+toAdd+", ") ; //$NON-NLS-1$ //$NON-NLS-2$
	  return s.toString() ;
	}
	return st ;
}

/**
 *  Detemine the source code argument for the setXXX() method
 */
private String getSourceCodeArg()  {
   
   if (fAddedPart == null) {
   	  Object currentVal = fbeanPart.getEObject().eGet(fFmapper.getFeature(fExpr)) ;
      if (!(currentVal instanceof IJavaInstance))
             return NULL_STRING ;
      else         
             return boxLayoutOveride(CodeGenUtil.getInitString((IJavaInstance)currentVal)) ;
   }
      
   StringBuffer st = new StringBuffer() ;
   String ref ;
   if (fAddedPart.getReturnedMethod() != null) 
     ref = fAddedPart.getReturnedMethod().getMethodName() ;
   else
     ref = fAddedPart.getSimpleName() ;
   st.append(ref) ;  
   // Send the implicit argument     
   // TODO  Support for implicit
   if (false && isImplicit(new Object[] {fAddedPart})) {
   	String rMethod = fFmapper.getReadMethodName() ;
   	st.append (ExpressionTemplate.LPAREN+fbeanPart.getSimpleName()+ExpressionTemplate.DOT+
   	rMethod+ExpressionTemplate.LPAREN+ExpressionTemplate.RPAREN+
   	ExpressionTemplate.RPAREN) ;
   }
   else {
     if (fAddedPart.isInstanceVar())
       st.append (ExpressionTemplate.LPAREN+ExpressionTemplate.RPAREN) ;   
   }
   return st.toString() ;
}
/**
 *  Overide the abstract method, to deal No Decorations.
 */
protected ExpressionTemplate getExpressionTemplate() throws CodeGenException {
		
	
	// TODO  Need to deal with layout constraints
	
	String arg = getSourceCodeArg() ; // Arguments for the add() method
	
	String sel = fbeanPart.getSimpleName() ;
	String mtd = fFmapper.getMethodName() ;
	ExpressionTemplate exp = new ExpressionTemplate (sel,mtd, new String[] { arg },	                                                
	                                                 null,
	                                                 0 ) ;
	exp.setLineSeperator(fbeanPart.getModel().getLineSeperator()) ;	
	return exp ;   	
}

protected boolean isChildRelationship() {
	EStructuralFeature sf = fFmapper.getFeature(null) ;
	return  sf instanceof EAttribute ||
			(sf instanceof EReference) && (
				((EReference)sf).isContainment() ||
				VCEPostSetCommand.isChildRelationShip((EReference)sf)) ;			  	
}

public String generate(Object[] args) throws CodeGenException {

 	
	if (fFmapper.getFeature(null) == null) 
	    throw new CodeGenException ("null Feature") ; //$NON-NLS-1$
      fAddedPart = getCurrentAddedPart(args==null? null:args[0]) ;
      if (fAddedPart == null) throw new CodeGenException ("No Added Part") ; //$NON-NLS-1$      

      if (isChildRelationship()) {
        fbeanPart.addChild(fAddedPart) ;
        fAddedPart.addBackRef(fbeanPart, (EReference)fFmapper.getFeature(null)) ;
      }
      
	ExpressionTemplate exp = getExpressionTemplate() ;
      fExprSig = exp.toString() ;	                                                 
      return fExprSig ;	     	
}

 
public boolean isImplicit(Object args[]) {
return false;
}

public Object[] getArgsHandles(Statement expr) {
	if (!fFmapper.getFeature(fExpr).isMany())
	    return null ;  // Argument is an attribute if it is not a many feature.
    Object[] result = null ;    
    try {
      if (fAddedPart == null && expr != null) {
        // Brand new expression
        BeanPart bp = parseAddedPart((MessageSend)expr) ;
        if (bp != null) 
          result = new Object[] { bp.getType()+"["+bp.getSimpleName()+"]" } ; //$NON-NLS-1$ //$NON-NLS-2$
       }
       else if (fAddedPart != null) {
           return new Object[] { fAddedPart.getType()+"["+fAddedPart.getSimpleName()+"]" } ; //$NON-NLS-1$ //$NON-NLS-2$
       }
    }    
    catch (CodeGenException e) {}
    return result ;
}


	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#getInstances()
	 */
		public Object[] getAddedInstance() {
		if (fAddedPart != null)
		   return new Object[] { fAddedPart.getEObject(), null } ;
		else
		   return new Object[0] ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.ExpressionDecoderHelper#getSFPriority()
	 */
	protected int getSFPriority() {
		if(fFmapper!=null){
			String methodName = null;
			if (fFmapper.getDecorator() != null)
				methodName = fFmapper.getDecorator().getWriteMethod().getName();
			if (methodName == null)
				methodName = CodeGenUtil.getWriteMethod(fExpr);
			if(methodName!=null)
				return fFmapper.getPriorityIncrement(methodName) + super.getSFPriority();
		}
		return super.getSFPriority();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#canRefreshFromComposition()
	 */
	public boolean canRefreshFromComposition() {
		BeanPart bp = getCurrentAddedPart(null);
		if (bp != null) return true;
		// Could not find a bean part from the JVE Model... it may have been deleted.
		if (fAddedPart != null) return true;
		else return false ;	
	}

}