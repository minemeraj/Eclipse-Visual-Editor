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
 *  $RCSfile: AbstractEventDecoder.java,v $
 *  $Revision: 1.15 $  $Date: 2005-08-24 23:30:45 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;


import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.ve.internal.jcm.*;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper.VEexpressionPriority;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.BeanMethodTemplate;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;

/**
 * @author Gili Mendel
 *
 */
public abstract class AbstractEventDecoder implements IEventDecoder {

	Statement fExpr = null;
	CodeEventRef fEventRef = null;
	IEventDecoderHelper fhelper = null;

	IBeanDeclModel fBeanModel = null;
	IVEModelInstance fCompositonModel = null;
	String   fFiller = BeanMethodTemplate.getInitExprFiller() ;
	BeanPart fbeanPart = null;
	String fdebugString = null;
	Object fPriority = null;
	AbstractEventInvocation fEventInvocation = null ;
	
	
	

	public abstract IEventDecoderHelper createDecoderHelper(Statement exp);

	IEventDecoderHelper getHelper() {
		if (fhelper == null) {
			fPriority = null;
			fhelper = createDecoderHelper(fExpr);
		}
		if (fhelper != null)
		   fhelper.setFiller(fFiller) ;
		return fhelper;
	}

	/**
	 * Make sure a IJavaFeatureMapper, and a IExpressionDecoderHelper have been
	 * allocated to the specific expression at hand. 
	 */
	protected synchronized boolean Initialize(AbstractEventInvocation ei) {

        fEventInvocation = ei ;
		if (getHelper() == null)
			return false;
		if (fExpr != null)
			getHelper().setDecodingContent(fExpr);		
	    getHelper().setEventInvocation(ei) ;

		return true;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoder#decode()
	 */
	public boolean decode() throws CodeGenException {
		// Refresh decoder 
		if ((getExprRef().getExprStmt() != null) && (getExprRef().getExprStmt() != fExpr))
			fExpr = getExprRef().getExprStmt();

		if (!Initialize(fEventInvocation))
			return false;

		// Go for it
		boolean result = false;
		boolean fromCache = fbeanPart.getModel().getCompositionModel().isFromCache();
		try {
			if (fromCache)
				result = fhelper.restore();
			else
				result = fhelper.decode();
		}
		catch (Exception e) {
			org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e) ;
		}

		if (result) {
			//fExprRef.setState(fExprRef.getState() | CodeExpressionRef.STATE_EXIST | CodeExpressionRef.STATE_IN_SYNC) ;
			fEventRef.setState(CodeExpressionRef.STATE_EXIST, true);
			fEventRef.setState(CodeExpressionRef.STATE_IN_SYNC, true);
			fhelper.adaptToCompositionModel(this);
		}
		return result;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoder#determinePriority()
	 */
	public VEexpressionPriority determinePriority() {
		if(!Initialize(getEventInvocation()))
			return IJavaFeatureMapper.DEFAULTPriority;
		return getHelper().getPriorityOfExpression();
	}


	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoder#setExpression(CodeEventRef)
	 */
	public void setExpression(CodeExpressionRef expr) throws CodeGenException {
		if (!(expr instanceof CodeEventRef))  throw new CodeGenException("Invalid Parameter") ; //$NON-NLS-1$
	
		fEventRef = (CodeEventRef) expr;
		fExpr = expr.getExprStmt();
		fEventRef.setDecoder(this);
		if (fExpr != null)
			fdebugString = fExpr.toString();
		if (fhelper != null) 
			fhelper.setDecodingContent(fExpr);
		if (fEventRef.getParser() != null)
	        setFiller(fEventRef.getParser().getFiller()) ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoder#setBeanModel(IBeanDeclModel)
	 */
	public void setBeanModel(IBeanDeclModel model) {
		fBeanModel = model;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoder#getBeanModel()
	 */
	public IBeanDeclModel getBeanModel() {
		return fBeanModel;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoder#setCompositionModel(IDiagramModelInstance)
	 */
	public void setCompositionModel(IVEModelInstance cm) {
		fCompositonModel = cm ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoder#getCompositionModel()
	 */
	public IVEModelInstance getCompositionModel() {
		return fCompositonModel;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoder#setBeanPart(BeanPart)
	 */
	public void setBeanPart(BeanPart part) {
		fbeanPart = part ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoder#getBeanPart()
	 */
	public BeanPart getBeanPart() {
		return fbeanPart;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoder#isDeleted()
	 */
	public boolean isDeleted() {
		boolean result = false ;
		result = getHelper().primIsDeleted() ;
		if (result==true)
		   fhelper.unadaptToCompositionModel() ;
		return result ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoder#getCurrentExpression()
	 */
	public String getCurrentExpression() {
		if (!isDeleted())
			return fhelper.getCurrentExpression();
		else
			return null;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoder#reflectExpression(String)
	 */
	public String reflectExpression(String expSig) throws CodeGenException {
		return null;
	}

	
	protected void markExprAsDeleted() {
		fEventRef.clearState();
		fEventRef.setState(CodeExpressionRef.STATE_DELETE, true); 
	}
	
	/**
	 *  Mark as delete, And Remove from document
	 */
	public void deleteFromSrc() {
		fhelper.unadaptToCompositionModel();
		markExprAsDeleted();
		fEventRef.updateDocument(true);
	}

	public  void dispose() {		
		if (fhelper != null){
			if (!isDeleted()) {
				deleteFromComposition() ;
			}
			else
				fhelper.unadaptToCompositionModel();
		}
		markExprAsDeleted();		
		fhelper = null;
		fdebugString = null;
	}
	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoder#reflectMOFchange()
	 */
	public void reflectMOFchange() {
		fEventRef.setState(CodeExpressionRef.STATE_IN_SYNC, false); 
		if((!fEventRef.isAnyStateSet()) || fEventRef.isStateSet(CodeExpressionRef.STATE_NO_SRC))  
		   return ;
		fEventRef.updateDocument(true) ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoder#deleteFromComposition()
	 */
	public void deleteFromComposition() {
		fhelper.removeFromModel();
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoder#getExprRef()
	 */
	public CodeExpressionRef getExprRef() {
		return fEventRef ;
	}

	/**
	 * Returns the eventDecorator.
	 * @return EventSetDecorator
	 */
	public AbstractEventInvocation getEventInvocation() {
		return fEventInvocation;
	}

	/**
	 * Sets the eventInvocation.  It is expected that if envocation is changed,
	 * The expression (fexpStmt) has changed as well.
	 * 
	 */
	public boolean setEventInvocation(AbstractEventInvocation ei) {
		boolean result = true;
		if (fEventInvocation!=null && ei!=fEventInvocation) {
			if (fhelper != null) {
				// Potentially active decoder.
				// This new invocation/expression may require a different
				// type of helper
				IEventDecoderHelper d = createDecoderHelper(fExpr) ;
				if (!d.getClass().isInstance(fhelper)) {
					// Clean up, and reset the helper 
					fhelper.removeFromModel() ;
					fhelper=d ;
				}
			}
			result = fhelper.setEventInvocation(ei);
		}
		if (result)
		   fEventInvocation = ei;
		return result;
	}

	public String generate(AbstractEventInvocation ei, Object[] args) throws CodeGenException {
	
	if (!Initialize(ei)) return null ;
	
	
	// Go for it

	String result = null ;	
	try {
	    result = fhelper.generate(args) ;
	}
	catch (CodeGenException e) {}
	

	if (result!=null) {
	   //fExprRef.setState(fExprRef.getState() | CodeExpressionRef.STATE_EXIST | CodeExpressionRef.STATE_IN_SYNC) ;
	   fEventRef.setState(CodeExpressionRef.STATE_EXIST, true);
	   fEventRef.setState(CodeExpressionRef.STATE_IN_SYNC, true);
	   fhelper.adaptToCompositionModel(this) ;
	}
	else {
	   fEventRef.dispose() ;	   
	}
		
		
	fdebugString=result ;
	return result ;
}		
		
		
	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoder#setFiller(String)
	 */
	public void setFiller(String filler) {
		fFiller = filler ;
		if (fhelper != null)
		   fhelper.setFiller(filler) ;
	}
	public String getFiller() {
		return fFiller ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoder#getCallBackSourceRange(org.eclipse.ve.internal.jcm.Callback)
	 */
	public ICodeGenSourceRange getCallBackSourceRange(Callback c) {
		if (fhelper != null)
		   return fhelper.getCallBackSourceRange(c) ;
		return null ;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoder#removeCallBack(org.eclipse.ve.internal.jcm.Callback)
	 */
	public void removeCallBack(Callback c) {
		getHelper().removeCallBack(c) ;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoder#addCallBack(org.eclipse.ve.internal.jcm.Callback)
	 */
	public void addCallBack(Callback c) {
		getHelper().addCallBack(c) ;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoder#getPropertyEventSourceRange(org.eclipse.ve.internal.jcm.PropertyEvent)
	 */
	public ICodeGenSourceRange getPropertyEventSourceRange(PropertyEvent pe) {
		if (fhelper != null)
			return fhelper.getPropertyEventSourceRange(pe);
		return null;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoder#addPropertyEvent(org.eclipse.ve.internal.jcm.PropertyEvent)
	 */
	public void addPropertyEvent(PropertyEvent pe) {
		getHelper().addPropertyEvent(pe) ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoder#removePropertyEvent(org.eclipse.ve.internal.jcm.PropertyEvent)
	 */
	public void removePropertyEvent(PropertyEvent pe) {
		getHelper().removePropertyEvent(pe) ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IJVEDecoder#setStatement(org.eclipse.jdt.core.dom.Statement)
	 */
	public void setStatement(Statement s) {
		fExpr = s;
		getHelper().setDecodingContent(s);
	}

}
