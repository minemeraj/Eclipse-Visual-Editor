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
 *  $RCSfile: CompositionDecoderAdapter.java,v $
 *  $Revision: 1.13 $  $Date: 2005-10-13 20:31:05 $ 
 */
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.IJavaElement;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.jcm.BeanSubclassComposition;
import org.eclipse.ve.internal.jcm.JCMPackage;

import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * 
 * This CodeGen adapter listens to the BeanSubclassComposition
 * 
 * @author Gili Mendel
 * @since 0.5.0
 */
public class CompositionDecoderAdapter extends MemberDecoderAdapter implements IAdaptable {
	

public CompositionDecoderAdapter (BeanPart b) {
		super(b.getModel()) ;
}


/**
 * When methods are added to bean subclass, we want to attach MemberDecoderAdapter to them
 */
	protected void processMethods(Notification msg) {

		switch (msg.getEventType()) {
			case Notification.ADD_MANY:
			case Notification.REMOVE_MANY:
				int type = msg.getEventType() == Notification.ADD_MANY ? Notification.ADD : Notification.REMOVE;
				List al = type==Notification.ADD?  (List) msg.getNewValue(): (List)msg.getOldValue();
				int i = 0;
				Iterator aitr = al.iterator();
				int pos = msg.getPosition();
				
				while (aitr.hasNext()) {
					int mpos = pos < 0 ? pos : pos + i++;
					Object o = aitr.next();
					processMethods(new ENotificationImpl((InternalEObject) msg.getNotifier(), type, (EStructuralFeature) msg.getFeature(), null, o,
							mpos));
				}
				break;
			case Notification.ADD:
				MemberDecoderAdapter a = (MemberDecoderAdapter) EcoreUtil.getExistingAdapter(((EObject) msg.getNewValue()),
						ICodeGenAdapter.JVE_MEMBER_ADAPTER);
				// TODO Is this really necessary? Can you have another JVE_MEMBER_ADAPTER on a JCMMethod.
				// We are only JCMMethods at this point. This test is making sure that it is ONLY
				// a MemberDecoderAdapter and not a subclass of it. If it is a subclass, it will
				// then go and add a NEW MemberDecoderAdapter IN ADDITION to the one already out there.
				// But this means we have TWO JVE_MEMBER_ADAPTERs then. And we only clean up the first
				// one in the REMOVE.
				if (a != null && a.getClass()!=MemberDecoderAdapter.class)
					a = null;
				if (a == null) {
					a = new MemberDecoderAdapter(fbeanModel);
					a.setTarget((Notifier) msg.getNewValue());
					((Notifier) msg.getNewValue()).eAdapters().add(a);
				}
			case Notification.REMOVE:
				if (msg.getOldValue() != null) {
					Adapter oa = EcoreUtil.getExistingAdapter((Notifier) msg.getOldValue(), ICodeGenAdapter.JVE_MEMBER_ADAPTER);
					if (oa != null)
						((Notifier) msg.getOldValue()).eAdapters().remove(oa);
				}
				break;
			default:
				if (JavaVEPlugin.isLoggingLevel(Level.FINE))
					JavaVEPlugin.log(this + " No action= ????? (" + msg.getEventType() + ")", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
				return;
		}

	}

/**
 * The method will induce the generation of settings and parent child relashinships
 */
protected void processSettings(Notification msg) {

	switch ( msg.getEventType()) {		
			case Notification.ADD_MANY:
			case Notification.REMOVE_MANY:
				 int type = msg.getEventType() == Notification.ADD_MANY ? Notification.ADD : Notification.REMOVE ;
				 List al = type==Notification.ADD?  (List) msg.getNewValue(): (List)msg.getOldValue();
                 
                 int i = 0 ;
                 Iterator aitr = al.iterator() ;
                 int pos = msg.getPosition();
                
                 while (aitr.hasNext()) {
                 	int mpos = pos<0 ? pos : pos + i++ ;
                 	Object o = aitr.next() ;
                 	processSettings(new ENotificationImpl((InternalEObject)msg.getNotifier(), type, (EStructuralFeature) msg.getFeature(), null, o, mpos)) ;
                 }             
             break ;
		case Notification.ADD :			  
		      IMethodTextGenerator mg = CodeGenUtil.getMethodTextFactory(fbeanModel).getMethodGenerator((IJavaObjectInstance)msg.getNewValue(),fbeanModel) ;		      
		      try {
				mg.generateExpressionsContent() ;
			  }
			  catch (CodeGenException e) {
			  	JavaVEPlugin.log(e) ;
			  }
		      break ;
		case Notification.REMOVE :	
		     // No need to remove anything from the source at this point	    
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
      if (ignoreMsg(msg)) return  ;
      
      
      switch (msg.getFeatureID(BeanSubclassComposition.class)) {
      	case JCMPackage.BEAN_SUBCLASS_COMPOSITION__COMPONENTS:  
      	      // All settings, and parent/child relationships have be set in the model
      		  processSettings(msg) ;    	
      		  break ;
        case JCMPackage.BEAN_SUBCLASS_COMPOSITION__IMPLICITS:
        case JCMPackage.BEAN_SUBCLASS_COMPOSITION__MEMBERS:
        	  // Generate/remove meta, and skelatons in the BDM
              processMembers(msg) ;
              break ;
        case JCMPackage.BEAN_SUBCLASS_COMPOSITION__METHODS:
        	  // Listen to changes for this method
        	  processMethods(msg) ;
              break ;
        case JCMPackage.BEAN_SUBCLASS_COMPOSITION__LISTENER_TYPES:
          	  break ;
        default:
        	if (JavaVEPlugin.isLoggingLevel(Level.FINE))
        		JavaVEPlugin.log("CompositionDecoderAdapter: Did not process msg: "+msg.getFeature(), Level.FINE) ; //$NON-NLS-1$
      } 
 }
 catch (Throwable t) {
     JavaVEPlugin.log(t, Level.WARNING) ;
 }
}



/**
 * Returns a String that represents the value of this object.
 * @return a string representation of the receiver
 */
public String toString() {
	// Insert code to print the receiver here.
	// This implementation forwards the message to super.
	// You may replace or supplement this.
		 return "\t"+"CompositionDecoderAdapter"+target; //$NON-NLS-1$ //$NON-NLS-2$
}
	

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (adapter == IJavaElement.class) {
			// Major KLUDGE. Need to do this so that when selecting the FF, there is an IType sent out
			// so that JavaBrowsing Perspective will maintain Type selection. It needs to be the IType
			// from the SharedWorkingCopy. If it was our local WCP, then the IType would not match and
			// would be not found in the TypesView. If we didn't return anything, then the PackagesView and
			// the TypesView would go blank.
			return CodeGenUtil.getMainType(fbeanModel.getCompilationUnit());
		}
		return null;
	}

}
