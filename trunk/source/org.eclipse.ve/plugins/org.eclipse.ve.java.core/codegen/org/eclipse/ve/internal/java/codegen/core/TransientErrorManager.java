package org.eclipse.ve.internal.java.codegen.core;
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
 *  $RCSfile: TransientErrorManager.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.ICancelMonitor;

/**
 * @version 	1.0
 * @author
 */
public class TransientErrorManager {

protected List fTransientErrorListeners = null;
protected List fTransientErrorHistoryHolder = null;

public TransientErrorManager(){
	fTransientErrorHistoryHolder = new ArrayList();
}

/**
 * Add Transient error listeners
 */
public void addTransientErrorListener(ITransientErrorListener listener){
	if(fTransientErrorListeners==null)
		fTransientErrorListeners = new ArrayList();
	if(listener!=null)
		fTransientErrorListeners.add(listener);
}

/**
 *  Remove Transient error listeners
 */
public void removeTransientErrorListener(ITransientErrorListener listener){
	if(fTransientErrorListeners!=null && listener!=null)
		fTransientErrorListeners.remove(listener);
}

protected void fireTransientError(ITransientErrorEvent event){
    if (fTransientErrorListeners==null) return ;
    
	for(int i=0;i<fTransientErrorListeners.size();i++) {
		  String eType = event.getErrorType() ;
	      if (eType.equals(ITransientErrorEvent.TYPE_CORRECTED_BEAN) || eType.equals(ITransientErrorEvent.TYPE_CORRECTED_FEATURE) || eType.equals(ITransientErrorEvent.TYPE_INFO))
	        ((ITransientErrorListener)fTransientErrorListeners.get(i)).correctionOccured(event);
	      else
		    ((ITransientErrorListener)fTransientErrorListeners.get(i)).errorOccured(event);
	}
}

public void handleLocalToSharedChanged(IBeanDeclModel fBeanModel, ICancelMonitor monitor){
	fireCorrections(fBeanModel, monitor, false);
}

public void handleSharedToLocalChanges(IBeanDeclModel fBeanModel, List errors, ICancelMonitor monitor){
      // Notify of transient errors..
      // TRANSIENT ERRORS
      final List errorsInCodeDelta = errors;
      boolean parseError = false;
      if(errorsInCodeDelta!=null && errorsInCodeDelta.size()>0){
	  	if (monitor != null && monitor.isCanceled()) 
	  		return ;
	  	for(int error=0;error<errorsInCodeDelta.size();error++){
	  		TransientErrorEvent event = ((TransientErrorEvent)errorsInCodeDelta.get(error));
	  		primRegisterError(event);
	  		if(event.getErrorType().equals(ITransientErrorEvent.TYPE_PARSER_ERROR))
	  			parseError=true;
	  	}
      }
	fireCorrections(fBeanModel, monitor, parseError);
}

protected void primRegisterError(TransientErrorEvent event){
	fireTransientError(event);
}

protected void fireCorrections(IBeanDeclModel fBeanModel, ICancelMonitor monitor, boolean isInParseError){
      // TRANSIENT CORRECTIONS
      if(fBeanModel!=null){
      	List inErrorBeans = new ArrayList();
      	Iterator itr = fBeanModel.getBeans().iterator();
      	while(itr.hasNext()){
      		boolean beanPartInError = false;
      		BeanPart bp = (BeanPart) itr.next();
      		if(isInParseError){
      			inErrorBeans.add(bp);
      		}else{
      			Iterator expressions = bp.getRefExpressions().iterator();
	      		while(expressions.hasNext()){
	      			CodeExpressionRef exp = (CodeExpressionRef) expressions.next();
	      			if(exp.isStateSet(CodeExpressionRef.STATE_EXP_IN_LIMBO)){
	      				beanPartInError = true;
	      				break;
	      			}
	      		}
      			if(beanPartInError)
      				inErrorBeans.add(bp);
      		}
      	}
      	Iterator prevErrBeans = fTransientErrorHistoryHolder.iterator();
      	while(prevErrBeans.hasNext()){
      		BeanPart bp = (BeanPart) prevErrBeans.next();
      		if(!inErrorBeans.contains(bp)){
      			// Was in error, now is not.. notify
      			fireTransientError(
      				new TransientErrorEvent(
      					ITransientErrorEvent.TYPE_CORRECTED_BEAN,
      					(EObject)bp.getEObject(),
      					bp.getUniqueName()));
      		}
      	}
      	fTransientErrorHistoryHolder.clear();
      	fTransientErrorHistoryHolder.addAll(inErrorBeans);
      }
}

public void clear(){
	fTransientErrorHistoryHolder.clear();
}

}
