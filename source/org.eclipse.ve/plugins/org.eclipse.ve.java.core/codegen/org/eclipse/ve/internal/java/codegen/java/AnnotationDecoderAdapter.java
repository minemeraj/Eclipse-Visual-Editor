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
 *  $RCSfile: AnnotationDecoderAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jem.internal.core.MsgLogger;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cdm.VisualInfo;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;



/**
 * @version 	1.0
 * @author
 */
public class AnnotationDecoderAdapter implements ICodeGenAdapter {

	  
  
  protected IAnnotationDecoder fDecoder=null ;


public AnnotationDecoderAdapter(IAnnotationDecoder decoder) {
	super() ;
	fDecoder = decoder ;
}

/**
 * isAdaptorForType.
 */
public boolean isAdapterForType(Object type) {
	return JVE_CODE_GEN_TYPE.equals(type)||
	        JVE_CODEGEN_ANNOTATION_ADAPTER.equals(type);
}

/**
 * applied: A setting has been applied to the mof object,
 * notify the decoder
 */ 
public void notifyChanged(Notification msg){

  try {
      
      if (fDecoder.getBeanModel() == null  || fDecoder.getBeanPart().getModel() == null ||
          fDecoder.getBeanPart().getModel().isStateSet(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL) ||
          fDecoder.getBeanPart().getModel().isStateSet(IBeanDeclModel.BDM_STATE_SNIPPET)) return  ;
      
      if (msg.getFeature() == null) return ;
      String action=null ;
	switch ( msg.getEventType() ) {		     
		case Notification.SET : 
			 if (!CDEUtilities.isUnset(msg)) {
				     action = msg.isTouch() ? "TOUCH" : "SET" ;		      //$NON-NLS-1$	//$NON-NLS-2$
				     break ;
			 }	// else flow into unset.
		case Notification.UNSET : 
		     action = "UNSET" ; //$NON-NLS-1$
		     break ;		     
		case Notification.ADD :
		     action = "ADD" ; //$NON-NLS-1$
		     break ;
		case Notification.REMOVE :
		     action = "REMOVE" ; //$NON-NLS-1$
		     break ;
		case Notification.REMOVING_ADAPTER:
		     action = "REMOVING_ADAPTER" ;		      //$NON-NLS-1$
		     break;
	}
	JavaVEPlugin.log(this+" action= "+action, MsgLogger.LOG_FINE) ; //$NON-NLS-1$
JavaVEPlugin.log("SourceRange ="+getJavaSourceRange(), MsgLogger.LOG_FINE) ;	 //$NON-NLS-1$
	
	  // Note: this doesn't handle add_many/remove_many, but currently we don't do that.
      switch (msg.getEventType()) {

        case Notification.UNSET:
        case Notification.REMOVE:
             if (msg.getOldValue() instanceof VisualInfo) {
                  ((Notifier)msg.getOldValue()).eAdapters().remove(this) ;
             }
             fDecoder.reflectMOFchange() ;	             
			 break;
			 
        case Notification.SET:     
        case Notification.ADD:
             if (msg.getOldValue() instanceof VisualInfo) {
                  ((Notifier)msg.getOldValue()).eAdapters().remove(this) ;
             }        
              if (msg.getNewValue() instanceof VisualInfo) {
                  ((EObject)msg.getNewValue()).eAdapters().add(this) ;
                  return ;
              }
        case Notification.MOVE:        
	    fDecoder.reflectMOFchange() ;		     
	    break ;      	
      }
  }
  catch (Throwable t) {
      JavaVEPlugin.log(t, MsgLogger.LOG_WARNING) ;
  }
}


public IAnnotationDecoder getDecoder() {
	return fDecoder ;
}

/**
 * Returns a String that represents the value of this object.
 * @return a string representation of the receiver
 */
public String toString() {
	// Insert code to print the receiver here.
	// This implementation forwards the message to super.
	// You may replace or supplement this.
	return "\tAnnotationDecoderAdapter:"+fDecoder; //$NON-NLS-1$
}

public ICodeGenSourceRange getJavaSourceRange() throws CodeGenException {
    //return fDecoder.getExprRef().getTargetSourceRange() ;
    return null ;
}

/**
 * @see ICodeGenAdapter#getBDMSourceRange()
 */
public ICodeGenSourceRange getBDMSourceRange() throws CodeGenException {
    return null;
}

/**
 * @see org.eclipse.ve.internal.java.codegen.java.ICodeGenAdapter#getSelectionSourceRange()
 */
public ICodeGenSourceRange getHighlightSourceRange()
	throws CodeGenException {
	return getJavaSourceRange();
}

	/**
	 * @see org.eclipse.emf.common.notify.Adapter#getTarget()
	 */
	public Notifier getTarget() {
		return null;
	}

	/**
	 * @see org.eclipse.emf.common.notify.Adapter#setTarget(Notifier)
	 */
	public void setTarget(Notifier newTarget) {
	}

}
