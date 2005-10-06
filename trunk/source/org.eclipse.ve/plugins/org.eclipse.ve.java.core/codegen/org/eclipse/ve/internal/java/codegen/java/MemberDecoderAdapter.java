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
package org.eclipse.ve.internal.java.codegen.java;
/*
 *  $RCSfile: MemberDecoderAdapter.java,v $
 *  $Revision: 1.12 $  $Date: 2005-10-06 22:01:09 $ 
 */
import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.jcm.*;

import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;


/**
 * This is a generic Membership adapter.  
 * As is, it is designed to deal with additons to methods
 * 
 * @author Gili Mendel
 * @since 0.5.0
 */
public class MemberDecoderAdapter extends AdapterImpl implements ICodeGenAdapter {
	
	public final static Class JVE_MEMBER_ADAPTER = MemberDecoderAdapter.class;  // Any Code Gen Adapter
	
	protected IBeanDeclModel fbeanModel ;
	CodeMethodRef  fMethodRef = null ;
		
	

public MemberDecoderAdapter (IBeanDeclModel model) {
	fbeanModel = model ;
}

protected boolean ignoreMsg(Notification msg) {
	// Should not happen
	if (msg.getFeature() == null)
		return true;

	// Annotations has a special adapter
	if (msg.getNewValue() != null && (msg.getNewValue() instanceof org.eclipse.ve.internal.cdm.AnnotationEMF))
		return true;

	// Construction time -- do not update source or generate anything
	if (fbeanModel.isStateSet(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL)
		|| fbeanModel.isStateSet(IBeanDeclModel.BDM_STATE_SNIPPET)
		|| !fbeanModel.isStateSet(IBeanDeclModel.BDM_STATE_UP_AND_RUNNING)
		|| ((msg.getFeature() instanceof EStructuralFeature) && ((EStructuralFeature) msg.getFeature()).isTransient())
		)
		return true;

	return false;
}

/**
 *  Create a new BeanPart instance and it relevant source code
 */
protected void  createBeanInstance(IJavaObjectInstance obj) throws CodeGenException{
    
    if (obj == null || fbeanModel.getABean(obj)!=null) throw new CodeGenException ("Bean already exists ??") ; //$NON-NLS-1$
    
    BeanPart bp = fbeanModel.getDeleteDesignated(obj, true) ;
    if (bp != null) {
        // Bring back to life childrens as well
        Iterator itr = fbeanModel.getABean(obj).getChildren() ;
        while (itr != null && itr.hasNext()) {
            BeanPart b = (BeanPart) itr.next() ;
            createBeanInstance((IJavaObjectInstance)b.getEObject()) ;         
        }
        // TODO We need to check if any attribute was set/removed and update the source
        return ;
    }
	
    // We are not going to create the bean right now, as it may refer to other beans
	// that are not in the model (e.g., a Composite/Button were just pasted,
	// Composite may not be in the model at the time the Button is created.
	//
//    BeanPartFactory bgen = new BeanPartFactory(fbeanModel,fbeanModel.getCompositionModel()) ;
//    try {      
//      bgen.createFromJVEModel(obj,fbeanModel.getCompilationUnit()) ;
//    }
//    catch (org.eclipse.ve.internal.java.codegen.util.CodeGenException e) {
//      JavaVEPlugin.log(e, Level.WARNING) ;
//      return ;
//    }
	
	// BDM will create the BeanPart lazily
	fbeanModel.lazyCreateBean(obj);
}

protected void removeChildrenIfNeeded (BeanPart child) {
	
	ArrayList Grandkids = new ArrayList () ;
	Iterator itr = child.getChildren() ;
	while (itr.hasNext())  Grandkids.add(itr.next()) ;
	
	for (int i=0; i<Grandkids.size(); i++) {
		BeanPart b = (BeanPart) Grandkids.get(i) ;
		removeChildrenIfNeeded (b) ;
		removeBeanInstance((IJavaObjectInstance)b.getEObject()) ;
	}			
}



/**
 *  Remove an existing BeanPart instance and its relevant source code
 *  Use the removeChidrenIfNeeded to prune a branch
 */
protected void  removeBeanInstance(IJavaObjectInstance obj) {

         if (obj == null) return ;         
         BeanPart b = fbeanModel.getABean(obj) ;
         if (b != null) {
           BeanPartFactory bgen = new BeanPartFactory(fbeanModel, fbeanModel.getCompositionModel()) ;
           bgen.removeBeanPart(b) ;	
         }
}


protected boolean isInBeanSubClassComposition(IJavaObjectInstance obj) {
	
	// If the bean is dedignated as deleted, it is "not" there
	if (obj == null || fbeanModel.getDeleteDesignated(obj,false)!=null) return false ;
	
	Iterator itr = fbeanModel.getRootBeans().iterator() ;
    while (itr.hasNext()) {
		BeanPart b = (BeanPart) itr.next();
		if (b.getEObject()==obj) 
		   return true ;
	}
	return false ;
}


/**
 * In some cases we use intermediates  source->intermediate->target.
 * 
 * @param  obj source or intermediate
 * @param  sf  SF used
 * 
 * @return Object array container the skiped obj/sf pair
 */
public static Object[] skipIntermediate(EObject obj, EStructuralFeature sf) {
   EObject container = obj.eContainer() ;
   // JVE model stores instances on a memberContainer by default
   // unless it is an intermediate object
   if (container == null ||((container instanceof MemberContainer)))
      return new Object[]  { obj, sf } ;
   else
      return new Object[] { container, obj.eContainmentFeature() } ;	
}
/**
 * When a new value is added, it is possible that it was promoted from
 * a Property to a Member. In this case there is already a decoder/expression
 * associated with this value.  We need to track it down, and re-generate this expression 
 * to reflect the association with a new BeanPart.
 * @deprecated
 */
protected void processPromotionIfNeeded(Notification msg) {
	
	EObject val = (EObject) msg.getNewValue();

	InverseMaintenanceAdapter ai = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(val, InverseMaintenanceAdapter.ADAPTER_KEY);
	if (ai == null)
		return;
		
	EReference[] refs = ai.getFeatures();
	for (int sfIdx = 0; sfIdx < refs.length; sfIdx++) {

		EObject[] list = ai.getReferencedBy(refs[sfIdx]);
		for (int i = 0; i < list.length; i++) {
			// No need to process a MemberContainer
			if (list[i] instanceof MemberContainer) continue ;
			// Skip Intermediate, if list[i] is one.
			Object[] skipPair = skipIntermediate(list[i], refs[sfIdx]) ;
			EObject obj = (EObject) skipPair[0] ;
			EStructuralFeature sf = (EStructuralFeature) skipPair[1] ;
			// addedVal is val, or an intermediate
			EObject addedVal = obj.equals(list[i]) ? val  : list[i] ;
			BeanPart bp = fbeanModel.getABean(obj);
			if (bp != null) {
				// Found a BeanPart that references this Instance
				// Now look for a decoder for this SF.
				CodeExpressionRef[] expList = (CodeExpressionRef[]) bp.getRefExpressions().toArray(new CodeExpressionRef[bp.getRefExpressions().size()]);
				for (int j = 0; j < expList.length; j++) {
					if (expList[j].isStateSet(CodeExpressionRef.STATE_NO_MODEL) || !expList[j].isStateSet(CodeExpressionRef.STATE_EXIST))
						continue;

					IExpressionDecoder d = expList[j].getExpDecoder();
					if (d != null && d.getSF().equals(sf)){
						// TODO At this time, deal with a single instance add, and no index considerations
						Object[] addedInstances = d.getAddedInstance() ;
						if (addedInstances != null && addedInstances.length>0) {
							if (addedInstances[0] != msg.getNewValue())
							   continue ;
						}
						d.deleteFromSrc() ;
						expList[j].dispose() ;
						BeanDecoderAdapter bAdapter = (BeanDecoderAdapter) EcoreUtil.getExistingAdapter(bp.getEObject(), ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);
						// ReCreate this feature in code			                                                
						Notification nMsg = new ENotificationImpl((InternalEObject) msg.getNotifier(), msg.getEventType(), sf, null, addedVal, true);
						bAdapter.notifyChanged(nMsg);
						break ;
					}
				}
			}
		}
	}
}

/**
 * A new instance is added/removed
 */
protected void processMembers(Notification msg) {

	switch ( msg.getEventType()) {		
			case Notification.ADD_MANY:
			case Notification.REMOVE_MANY:
                 List al = (List) msg.getNewValue() ;
                 int i = 0 ;
                 Iterator aitr = al.iterator() ;
                 int pos = msg.getPosition();
                 int type = msg.getEventType() == Notification.ADD_MANY ? Notification.ADD : Notification.REMOVE ;
                 while (aitr.hasNext()) {
                 	int mpos = pos<0 ? pos : pos + i++ ;
                 	Object o = aitr.next() ;
                 	processMembers(new ENotificationImpl((InternalEObject)msg.getNotifier(), type, (EStructuralFeature) msg.getFeature(), null, o, mpos)) ;
                 }             
             break ;
		case Notification.ADD :
		      if (!isInBeanSubClassComposition((IJavaObjectInstance)msg.getNewValue())) {
		         try {
					createBeanInstance((IJavaObjectInstance)msg.getNewValue()) ;
				} catch (CodeGenException e) {
					// We may be getting feedback, while parsing
				}		         
		      }
		     break ;
		case Notification.REMOVE :
		     if (!isInBeanSubClassComposition((IJavaObjectInstance)msg.getNewValue())) {
		         removeBeanInstance((IJavaObjectInstance)msg.getOldValue()) ;
		      }		     
		     break ;		     		    
	      default:
	      	if (JavaVEPlugin.isLoggingLevel(Level.FINE))
	           JavaVEPlugin.log(this+" No action= ????? ("+msg.getEventType()+")", Level.FINE) ; //$NON-NLS-1$ //$NON-NLS-2$
	        return ;
	}
	
}
	
	
public void notifyChanged(Notification msg){

 try {
      // In process of building the composition ??
	if (ignoreMsg(msg))
		return;

	switch (msg.getFeatureID(MemberContainer.class)) {
		case JCMPackage.MEMBER_CONTAINER__IMPLICITS:
		case JCMPackage.MEMBER_CONTAINER__MEMBERS :
			// Generate/remove meta, and skelatons in the BDM
			processMembers(msg);
			break;
		case JCMPackage.MEMBER_CONTAINER__PROPERTIES :
		// At some point initializes should drive the allocation decoder
		case JCMPackage.JCM_METHOD__INITIALIZES:
			// Do nothing
			break;
		default :
			if (JavaVEPlugin.isLoggingLevel(Level.FINE))
				JavaVEPlugin.log("CompositionDecoderAdapter: Did not process msg: " + msg.getFeature(), Level.FINE); //$NON-NLS-1$
			return ;
	}
      
      
      
      
      if (msg.getFeatureID(BeanSubclassComposition.class) != JCMPackage.BEAN_SUBCLASS_COMPOSITION__COMPONENTS)
         return ;
         
      String action=null ; 
	switch ( msg.getEventType()) {	
		case Notification.SET :
			 if (!CDEUtilities.isUnset(msg)) {
			     action = msg.isTouch() ? "TOUCH" : "SET" ; //$NON-NLS-1$ //$NON-NLS-2$
			     break ;
			 }	// else flow into unset because really is unset.
		case Notification.UNSET : 
		     action = "UNSET" ; //$NON-NLS-1$
		     break ;			 
		case Notification.ADD :
		     action = "ADD" ; //$NON-NLS-1$
		     if (!isInBeanSubClassComposition((IJavaObjectInstance)msg.getNewValue())) 
		         createBeanInstance((IJavaObjectInstance)msg.getNewValue()) ;
		     break ;
		case Notification.REMOVE :
		     action = "REMOVE" ; //$NON-NLS-1$
		     BeanPart b = fbeanModel.getABean((EObject)msg.getOldValue()) ;
		     removeChildrenIfNeeded(b) ;
		     removeBeanInstance((IJavaObjectInstance)msg.getOldValue()) ;		     		     		     
		     break ;
		case Notification.REMOVING_ADAPTER:
		     action = "REMOVING_ADAPTER" ;		      //$NON-NLS-1$
		     break;
		case Notification.ADD_MANY:
             action = "ADD_MANY" ; //$NON-NLS-1$
             List al = (List) msg.getNewValue() ;
             int i = 0 ;
             Iterator aitr = al.iterator() ;
             // TODO This should be changed to have the add function in a method and call it explicitly instead of resending a notifyChanged.
             int pos = msg.getPosition();
             while (aitr.hasNext()) {
                 	int mpos = pos<0 ? pos : pos + i++ ;
                 	Object o = aitr.next() ;
                 	notifyChanged(new ENotificationImpl((InternalEObject)msg.getNotifier(), Notification.ADD, (EStructuralFeature) msg.getFeature(), null, o, mpos)) ;
             }             
             break ;
       case Notification.REMOVE_MANY:
             action = "REMOVE_MANY" ; //$NON-NLS-1$
             List rl = (List) msg.getOldValue() ;
             Iterator ritr = rl.iterator() ;
             pos = msg.getPosition();
             while (ritr.hasNext()) {
                 	Object o = ritr.next() ;
	                 // TODO This should be changed to have the remove function in a method and call it explicitly instead of resending a notifyChanged.
                 	notifyChanged(new ENotificationImpl((InternalEObject)msg.getNotifier(), Notification.REMOVE, (EStructuralFeature) msg.getFeature(), o, null, pos)) ;
             }
             break ;		     
	}
	if (JavaVEPlugin.isLoggingLevel(Level.FINE))
		JavaVEPlugin.log(this+" action= "+action, Level.FINE) ; //$NON-NLS-1$
 }
 catch (Throwable t) {
     JavaVEPlugin.log(t, Level.WARNING) ;
 }
}

/**
 * isAdaptorForType.
 */
public boolean isAdapterForType(Object type) {
	return ICodeGenAdapter.JVE_CODE_GEN_TYPE.equals(type) ||	        
	        JVE_MEMBER_ADAPTER.equals(type);
}

/**
 * Returns a String that represents the value of this object.
 * @return a string representation of the receiver
 */
public String toString() {
	// Insert code to print the receiver here.
	// This implementation forwards the message to super.
	// You may replace or supplement this.
		 return "MemberDecoderAdapter: "+target; //$NON-NLS-1$ //$NON-NLS-2$
}
	

	/**
	 * Returns the methodRef.
	 * @return CodeMethodRef
	 */
	public CodeMethodRef getMethodRef() {
		return fMethodRef;
	}

	/**
	 * Sets the methodRef.
	 * @param methodRef The methodRef to set
	 */
	public void setMethodRef(CodeMethodRef methodRef) {
		fMethodRef = methodRef;
	}
	


	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ICodeGenAdapter#getBDMSourceRange()
	 */
	public ICodeGenSourceRange getBDMSourceRange() throws CodeGenException {
		return null;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ICodeGenAdapter#getHighlightSourceRange()
	 */
	public ICodeGenSourceRange getHighlightSourceRange() throws CodeGenException {
		return null;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ICodeGenAdapter#getJavaSourceRange()
	 */
	public ICodeGenSourceRange getJavaSourceRange() throws CodeGenException {
		return null;
	}

}
