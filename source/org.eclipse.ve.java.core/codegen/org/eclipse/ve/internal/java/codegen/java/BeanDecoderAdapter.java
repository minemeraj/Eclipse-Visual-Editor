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
 *  $RCSfile: BeanDecoderAdapter.java,v $
 *  $Revision: 1.9 $  $Date: 2004-03-25 23:44:11 $ 
 */

import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.Label;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.viewsupport.ImageDescriptorRegistry;
import org.eclipse.jdt.internal.ui.viewsupport.JavaElementImageProvider;
import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.CDEUtilities;

import org.eclipse.ve.internal.jcm.AbstractEventInvocation;

import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.core.*;

/**
 *  This Adapter represents the VCE model listener for a component.
 *  It will add/remove sub-components, and associated expressions
 */

public class BeanDecoderAdapter extends MemberDecoderAdapter implements  IAdaptable, IJavaToolTipProposalAdapter {
	  
  
  protected  BeanPart fBean=null ;			   // Component associated with this adapter
  Hashtable  fNullValuedSF = new Hashtable() ;    // Hold Exp. Decoder Adapters for null values
  Hashtable  fChildrens    = new Hashtable() ;    // Hold Exp. Decoder Adapters for components SF
  HashMap	 fSettings     = new HashMap() ;
  private   ImageDescriptorRegistry fJavaImageRegistry= JavaPlugin.getImageDescriptorRegistry();
  Label		 fpreviousLabel = null ;  // remember the last label, in case we are paused
  
  String     traceMsg[] = { "CREATE", //$NON-NLS-1$
	                    	"SET", //$NON-NLS-1$
	                    	"UNSET",    //$NON-NLS-1$
          	            	"ADD", //$NON-NLS-1$
 	                    	"REMOVE", //$NON-NLS-1$
	                    	"ADD_MANY", //$NON-NLS-1$
 	                    	"REMOVE_MANY", //$NON-NLS-1$
	                    	"MOVE", //$NON-NLS-1$
                        	"TOUCH", //$NON-NLS-1$
                        	"REMOVING_ADAPTER" } ; //$NON-NLS-1$
                        	
  EStructuralFeature fEventsSF = null ;
  
  
  public EStructuralFeature getEventsSF() {
  	if (fEventsSF != null) return fEventsSF ;
  	 fEventsSF = JavaInstantiation.getSFeature((IJavaObjectInstance)fBean.getEObject(),JavaBeanEventUtilities.EVENTS);
  	 return fEventsSF ;
  }                        	
                        	
                        	
  
/**
 * 
 * A reference adapter will be put on intermediates roots, to point back to the JFCBeanDecoderAdapter
 * that is on the component.
 */
protected  class BeanDecoderRefAdapter extends BeanDecoderAdapter {
	
	BeanDecoderAdapter fTargetAdapter ;
	EStructuralFeature fSF ;
	int			   fMsg = Notification.SET ;
	IExpressionDecoder fDecoder ;
	
	public BeanDecoderRefAdapter (BeanDecoderAdapter a, IExpressionDecoder d, int msgType) {
		super(a.getBeanPart()) ;
		fTargetAdapter = a ;
		fMsg = msgType ;
		fDecoder = d ;
		fSF = fDecoder.getSF() ;
	}
	
	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.JFCBeanDecoderAdapter#addSettingAdapter(EStructuralFeature, ICodeGenAdapter)
	 */
	public void addSettingAdapter(EStructuralFeature sf, ICodeGenAdapter a) {
		fTargetAdapter.addSettingAdapter(sf, a);
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ICodeGenAdapter#getBDMSourceRange()
	 */
	public ICodeGenSourceRange getBDMSourceRange() throws CodeGenException {
		return fTargetAdapter.getBDMSourceRange();
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.JFCBeanDecoderAdapter#getBeanPart()
	 */
	public BeanPart getBeanPart() {
		return fTargetAdapter.getBeanPart();
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ICodeGenAdapter#getHighlightSourceRange()
	 */
	public ICodeGenSourceRange getHighlightSourceRange() throws CodeGenException {
		return fTargetAdapter.getHighlightSourceRange();
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ICodeGenAdapter#getJavaSourceRange()
	 */
	public ICodeGenSourceRange getJavaSourceRange() throws CodeGenException {
		return fTargetAdapter.getJavaSourceRange();
	}

	/**
	 * @see org.eclipse.emf.common.notify.Adapter#isAdapterForType(Object)
	 */
	public boolean isAdapterForType(Object type) {
		return fTargetAdapter.isAdapterForType(type);
	}

	/**
	 * @see org.eclipse.emf.common.notify.Adapter#notifyChanged(Notification)
	 */
	public void notifyChanged(Notification notification) {
		
		
		Object[] added = fDecoder.getAddedInstance() ;
	    EObject root = null ;
	    if (added.length>=2)
	       root = (EObject)added[1] ;
	       
	    int msgType ;
	    switch (notification.getEventType()) {
	    	case Notification.ADD:
	    	case Notification.ADD_MANY:
	    	case Notification.MOVE:
	    	case Notification.REMOVE:
	    	case Notification.REMOVE_MANY:
	    	case Notification.SET:
	    	case Notification.UNSET:
	    	          // Overide here, as this may be a SET for an intermediate feature
	    	          msgType= fMsg ;
	    	          break ;
	    	default: msgType = notification.getEventType() ;
	    }

		Notification n = new ENotificationImpl((InternalEObject)notification.getNotifier(), 
		                                       msgType,
		                                       fSF, 
		                                       root!=null? root: notification.getOldValue(), 
		                                       root!=null? root: notification.getNewValue(),
		                                       notification.getPosition());
		fTargetAdapter.notifyChanged(n);
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.JFCBeanDecoderAdapter#removeSettingAdapter(EStructuralFeature, ICodeGenAdapter)
	 */
	public void removeSettingAdapter(EStructuralFeature sf, ICodeGenAdapter a) {
		fTargetAdapter.removeSettingAdapter(sf, a);
	}
	
	public String toString() {
		return "BeanDecoderRefAdapter->"+fTargetAdapter ; //$NON-NLS-1$
	}

}

public BeanDecoderAdapter(BeanPart bean) {
	super(bean.getModel()) ;
	fBean       = bean ;
}

/**
 * isAdaptorForType.
 */
public boolean isAdapterForType(Object type) {
	return JVE_CODE_GEN_TYPE.equals(type) ||	        
	        JVE_CODEGEN_BEAN_PART_ADAPTER.equals(type) ||
	        IJavaToolTipProposalAdapter.JAVA_ToolTip_Proposal_TYPE.equals(type);
}






/**
 * Get the CODE_GEN adapter for a given object.  For null values, the adapter
 * is stored in fNullValuedSF
 */
protected ICodeGenAdapter getDecoderAdapter(Object obj, Object sf) {
	
      if (obj == null)
          return getNullValuedAdapter((EStructuralFeature)sf) ;		
          
      if (!(obj instanceof Notifier)) 
          return null ;      
          
      ICodeGenAdapter a =  (ICodeGenAdapter) EcoreUtil.getExistingAdapter((Notifier)obj, JVE_CODEGEN_EXPRESSION_ADAPTER) ;
      if (a == null) {
      	 org.eclipse.ve.internal.java.core.JavaVEPlugin.log("JFCBeanDecoderAdapter.getDecoderAdapter() : No ExpressionAdapter <-- check me") ; //$NON-NLS-1$
         a = (ICodeGenAdapter) EcoreUtil.getExistingAdapter((Notifier)obj,JVE_CODE_GEN_TYPE) ;
      }
                    
      return a ;
}


protected void removeDecoderAdapter (Object obj, ICodeGenAdapter a, Object sf) {
	if (a == null) return ;
      if (obj == null || !(obj instanceof Notifier))
	    removeNullValue((EStructuralFeature)sf) ;
	else
	   ((Notifier)obj).eAdapters().remove(a) ;	
}






protected boolean sanityCheck(boolean flag, String msg) {
	if (!flag) {
		org.eclipse.ve.internal.java.codegen.util.CodeGenException e = new org.eclipse.ve.internal.java.codegen.util.CodeGenException(msg) ;
		JavaVEPlugin.log(e, Level.WARNING) ;
	}
	return flag ;
}







/**
 * Process Notification.SET message
 */
protected void setElement (Notification msg) {
    
    ICodeGenAdapter oldAdapter =null ;  // Holds the expression associated with this feature, e.g., setLayout()
    Object[] args = null ;
    
    // In a setElement it is not an isMany feature
    ICodeGenAdapter adapters[] = getSettingAdapters((EStructuralFeature)msg.getFeature()) ;
    if (adapters.length>0) oldAdapter = adapters[0] ;
    
    boolean oldAdapterTookNotification = true;
    if (oldAdapter != null) {
    	// expression exists, update it
    	try {
			oldAdapter.notifyChanged(msg) ;
		} catch (CodeGenRuntimeException e) {
			 IExpressionDecoder d = ((ExpressionDecoderAdapter)oldAdapter).getDecoder() ;
			CodeExpressionRef exp = d.getExprRef() ;
			
			// The old Adapter was unable to handle it due to Codegen reasons, 
			// Hence remove the expression and let a new one get created.
			d.deleteFromSrc() ;
			exp.dispose() ;		
			oldAdapterTookNotification = false;
		}
    }
    if((oldAdapter==null) || (oldAdapter!=null && !oldAdapterTookNotification)){
       // Need to generate an expression
		ExpressionRefFactory eGen = new ExpressionRefFactory(fBean, (EStructuralFeature) msg.getFeature());
		try {
			CodeExpressionRef exp = eGen.createFromJVEModel(args);
			if (exp != null)
				exp.insertContentToDocument();
		}
		catch (org.eclipse.ve.internal.java.codegen.util.CodeGenException e) {
			JavaVEPlugin.log(e, Level.WARNING);
			return;
		}
    }
    
    

}

protected IJavaObjectInstance getComponentFromSpecialRoot(EObject root) {
    if (CodeGenUtil.isConstraintComponentValue(root)) 
      return CodeGenUtil.getCCcomponent(root) ;
//    else if (CodeGenUtil.isTabPaneComponentValue(root))
//      return (IJavaObjectInstance) root.eGet(
//                   (EStructuralFeature) root.eClass().getEStructuralFeature(JTabbedPaneAddDecoderHelper.COMPONENT_ATTR_NAME)
//                   ) ; 
    return null ;      
}

protected IJavaObjectInstance getConstraintFromSpecialRoot(EObject root) {
    if (CodeGenUtil.isConstraintComponentValue(root)) 
       return CodeGenUtil.getCCconstraint(root) ;
    return null ;
}

protected ICodeGenAdapter getExistingAdapter(Notification msg) {
	
	EStructuralFeature sf = (EStructuralFeature) msg.getFeature();
	ICodeGenAdapter adapters[] = getSettingAdapters(sf);
	if (!sf.isMany()) {
		if (adapters.length > 0)
			return adapters[0];
	}
	else {
		for (int i = 0; i < adapters.length; i++) {
			if (adapters[i] instanceof ExpressionDecoderAdapter) {
				ExpressionDecoderAdapter a = (ExpressionDecoderAdapter) adapters[i];
				// [added Component, Root]
				Object added[] = a.getDecoder().getAddedInstance();
				if (added.length >= 2) {
					EObject root ;
					if (added[1] != null)
					 root = (EObject) added[1];
				    else
				     root = (EObject) added[0] ;
					if (root == msg.getOldValue()) {
						return a;
					}
				}
			}
		}
	}
	return null;
}
/**
 * Process Notification.REMOVE message
 */
protected void removeElement (Notification msg) {
                        
            ICodeGenAdapter oldAdapter = getExistingAdapter(msg) ;
			if (oldAdapter != null) {
			    oldAdapter.notifyChanged(msg) ;
		     }				
}
/**
 * 
 * @deprecte
 */
protected boolean isVanillaConstraint(IJavaObjectInstance c) {
     return (!(c != null && !c.getJavaType().getJavaName().equals("java.lang.String"))) ;  //$NON-NLS-1$
}

protected boolean isIntermediateValue(EObject val) {
	if (MemberDecoderAdapter.skipIntermediate(val,null)[0]==val) 
	  return false ;
	else
	  return true ;
}


protected void addEventElement (Notification msg) {
	
	 //  It is possible that an ADD is routed by a reference adapter for an existing feature
	AbstractEventInvocation aei = (AbstractEventInvocation) msg.getNewValue() ;		
	EventRefFactory ef = new EventRefFactory(fBean,aei) ; ;					
	try {
		CodeEventRef exp = ef.createFromJVEModel() ;
		exp.insertContentToDocument() ;
	}
	catch (CodeGenException e) {
		org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e) ;
	}
	
}

/**
 * Process Notification.ADD message
 */
protected void addElement (Notification msg) {
	 
	 //  It is possible that an ADD is routed by a reference adapter for an existing feature
	ExpressionDecoderAdapter oldAdapter = (ExpressionDecoderAdapter) getExistingAdapter(msg);
	// At this time just delete/reCreate it
	if (oldAdapter != null) {
		IExpressionDecoder d = oldAdapter.getDecoder();
		CodeExpressionRef e = d.getExprRef();
		d.deleteFromSrc();
		e.dispose();
		oldAdapter = null;
	}

	Object[] args = null;

	if (oldAdapter == null) {
		if (isIntermediateValue((EObject) msg.getNewValue())) { // Special Relationship e.g., contentPane		     	 
			// Process the Component        	
			args = new Object[] { msg.getNewValue()};
		}
		else {
			if (msg.getNewValue() instanceof IJavaObjectInstance) {
				IJavaObjectInstance comp = (IJavaObjectInstance) msg.getNewValue();
				BeanPart b = fBean.getModel().getABean(comp);
				if (b == null || fBean.getModel().getDeleteDesignated(comp, false) != null) {
					try {
						createBeanInstance(comp);
					} catch (CodeGenException e) {
						JavaVEPlugin.log(e) ;
					}
					b = fBean.getModel().getABean(comp);
				}
				
				args = new Object[] { comp };
				b.addBackRef(fBean, (EReference)msg.getFeature());
				fBean.addChild(b);
			}
		}

		// Brand new expression
		ExpressionRefFactory eGen = new ExpressionRefFactory(fBean, (EStructuralFeature) msg.getFeature());
		try {
			CodeExpressionRef exp = eGen.createFromJVEModel(args);
			exp.insertContentToDocument();
		}
		catch (Exception e) {
			JavaVEPlugin.log(e, Level.WARNING);
			return;
		}
	}
	else {
		// expression exists, update it
		oldAdapter.notifyChanged(msg);
	}
}



/**
 * applied: A setting has been applied to the mof object,
 * notify the decoder
 */ 
public void notifyChanged(Notification msg) {

	try {
		// In process of building the composition ??
		if (ignoreMsg(msg))
			return;

		String action = null;
		switch (msg.getEventType()) {

			case Notification.ADD_MANY :
				action = "ADD_MANY"; //$NON-NLS-1$
				List al = (List) msg.getNewValue();
				int i = 0;
				Iterator aitr = al.iterator();
				// TODO This should be changed to have the add function in a method and call it explicitly instead of resending a notifyChanged.
				int pos = msg.getPosition();
				while (aitr.hasNext()) {
					int mpos = pos < 0 ? pos : pos + i++;
					Object o = aitr.next();
					notifyChanged(new ENotificationImpl((InternalEObject) msg.getNotifier(), Notification.ADD, (EStructuralFeature) msg.getFeature(), null, o, mpos));
				}
				break;
			case Notification.REMOVE_MANY :
				action = "REMOVE_MANY"; //$NON-NLS-1$
				List rl = (List) msg.getOldValue();
				Iterator ritr = rl.iterator();
				pos = msg.getPosition();
				while (ritr.hasNext()) {
					Object o = ritr.next();
					// TODO This should be changed to have the remove function in a method and call it explicitly instead of resending a notifyChanged.
					notifyChanged(new ENotificationImpl((InternalEObject) msg.getNotifier(), Notification.REMOVE, (EStructuralFeature) msg.getFeature(), o, null, pos));
				}
				break;
			case Notification.ADD :
				action = "ADD"; //$NON-NLS-1$
				if (msg.getFeature().equals(getEventsSF())) {
					addEventElement(msg);
				}
				else
					addElement(msg);
				break;

			case Notification.SET : // New, Different Value
				if (!CDEUtilities.isUnset(msg)) {
					if (!msg.isTouch())
						action = "SET"; //$NON-NLS-1$
					if (msg.getFeature().equals(getEventsSF())) {
						JavaVEPlugin.log("Event feature setting ?????? on : "+msg.getNewValue(),Level.FINE); //$NON-NLS-1$
					}
					else
						setElement(msg);
					break;
				} // Else flow into unset because really is unset.
			case Notification.UNSET :
				action = "UNSET"; //$NON-NLS-1$
				ICodeGenAdapter a = getExistingAdapter(msg);
				if (a != null) {
					a.notifyChanged(msg);
				}

				break;

			case Notification.REMOVE :
				action = "REMOVE"; //$NON-NLS-1$
				if (msg.getFeature().equals(getEventsSF())) {
					// TODO We need to drive the removal of the invocation method here
					// It works now, becase when the listener is removed from the Inv. JCMMethod
					// the EventDecoder on the IM reflect and cleans upl
				}
				else
					removeElement(msg);
				break;
		    case Notification.REMOVING_ADAPTER :
		         //noOp
		         break;
			default :
				JavaVEPlugin.log(this +" No action= ????? (" + msg.getEventType() + ")", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
				return;

		}
		JavaVEPlugin.log(this +" action= " + action + " completed.", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
		JavaVEPlugin.log("SourceRange: " + getJavaSourceRange(), Level.FINE); //$NON-NLS-1$
	}
	catch (Throwable t) {
		JavaVEPlugin.log(t, Level.WARNING);
	}
}


public BeanPart getBeanPart() {
	return fBean ;
}

/**
 * @deprecated
 */
public void  addNullValue(EStructuralFeature sf,ICodeGenAdapter adapter) {
	fNullValuedSF.put(sf,adapter) ;
}
/**
 * @deprecated
 */
public void  removeNullValue(EStructuralFeature sf) {
	fNullValuedSF.remove(sf) ;
}
/**
 * @deprecated
 */
public ICodeGenAdapter getNullValuedAdapter(EStructuralFeature sf) {
	return (ICodeGenAdapter) fNullValuedSF.get(sf) ;
}


/**
 *   Container support
 *@deprecated 
 *  
 */
public void  addChildAdapter(Object child, ICodeGenAdapter adapter) {
	if (child != null)
	   fChildrens.put(child,adapter) ;
}


public void addSettingAdapter(EStructuralFeature sf, ICodeGenAdapter a) {
  if (sf.isMany()) {
  	List l = (List) fSettings.get(sf) ;
  	if (l == null) {
  		l = new ArrayList() ;
  		fSettings.put(sf,l) ;
  	}
  	l.add(a) ;
  	   
  }
  else {
  	fSettings.put(sf,a) ;
  }
}
public void removeSettingAdapter(EStructuralFeature sf, ICodeGenAdapter a) {
  if (sf.isMany()) {
  	List l = (List) fSettings.get(sf) ;
  	if (l != null) {
  	  l.remove(a) ;
  	}  	   
  }
  else {
  	fSettings.remove(sf) ;
  }
}

public final ICodeGenAdapter[] getSettingAdapters(EStructuralFeature sf) {
	if (sf.isMany()) {
		List l = (List) fSettings.get(sf) ;
		if (l == null) 
		   return new ICodeGenAdapter[0] ;
		else
		   return  (ICodeGenAdapter[]) l.toArray(new ICodeGenAdapter[l.size()]) ;
	}
	else {
		ICodeGenAdapter a[] ;
		if (fSettings.get(sf) != null) 
		    a = new ICodeGenAdapter[] { (ICodeGenAdapter) fSettings.get(sf) };
		else
		    a = new ICodeGenAdapter[0] ;
		return a ;
	}
}

/**
 *@deprecated 
 */
public void  removeChildAdapter(Object child) {
	if (child != null)
	   fChildrens.remove(child) ;
}
/**
 *@deprecated 
 */
public ICodeGenAdapter getChildAdapter(Object child) {
	if (child != null)
	   return (ICodeGenAdapter) fChildrens.get(child) ;
	else
	   return null ;
}

/**
 * @return the Java Source Range of the method that initializes the bean.
 */
public ICodeGenSourceRange getJavaSourceRange() throws CodeGenException {
   
    CodeMethodRef mr = fBean.getInitMethod() ;
    if (mr != null)
      return  mr.getTargetSourceRange() ;
    else
      return null ;
     
}

/**
 * Returns a String that represents the value of this object.
 * @return a string representation of the receiver
 */
public String toString() {
	// Insert code to print the receiver here.
	// This implementation forwards the message to super.
	// You may replace or supplement this.
	return "\t"+"JFCBeanDecoderAdapter:"+fBean.getUniqueName(); //$NON-NLS-1$ //$NON-NLS-2$
}

/**
 * @see ICodeGenAdapter#getBDMSourceRange()
 */
public ICodeGenSourceRange getBDMSourceRange() throws CodeGenException {
   return new CodeGenSourceRange(fBean.getInitMethod().getOffset(),
                                  fBean.getInitMethod().getLen()) ;
}


/**
 * @see org.eclipse.ve.internal.java.codegen.java.ICodeGenAdapter#getSelectionSourceRange()
 */
public ICodeGenSourceRange getHighlightSourceRange()
	throws CodeGenException {
	CodeMethodRef mr = fBean.getInitMethod() ;
	if (mr != null)
	  return  mr.getHighlightSourceRange() ;
	else
	  return null ;
}
public Object getAdapter(Class aClass){
	if ( aClass == IJavaElement.class ) { 
		// Return the IField for this bean.
		IJavaElement e = (IField) JavaCore.create(fBean.getFieldDeclHandle());
		ICompilationUnit cu = fBean.getModel().getCompilationUnit() ;
		IJavaElement[] es = cu.findElements(e);
		if (es != null && es.length == 1)
			return es[0];
	}
	return null;
}

public BeanDecoderAdapter getRefAdapter(IExpressionDecoder d, int msgType) {
	return new BeanDecoderRefAdapter(this, d, msgType) ;
}

public Label getInstanceDisplayInformation() {	
	Label l = new Label() ;
	
	ImageDescriptor	d = null ;
	if (fBean.isInstanceVar()) {
		if (fBean.getFieldDeclHandle() != null) {
			IJavaElement je = JavaCore.create(fBean.getFieldDeclHandle());
			if (je instanceof IField) {
				IField f = (IField) je;
				int flags = 0;
				try {
					flags = f.getFlags();
				}
				catch (JavaModelException e) {
					f = CodeGenUtil.getFieldByName(fBean.getSimpleName(), fbeanModel.getCompilationUnit());
					if (f != null) {
						fBean.setFieldDeclHandle(f.getHandleIdentifier()) ;
						try {
							flags = f.getFlags();
						}
						catch (JavaModelException e1) {}
					}
				}
				d = JavaElementImageProvider.getFieldImageDescriptor(false, flags);
			}
		}		
		if (d == null)
		   d =  JavaElementImageProvider.getFieldImageDescriptor(false, 0) ;
	}
	else
	       d = JavaPluginImages.DESC_OBJS_LOCAL_VARIABLE ;
    l.setIcon(fJavaImageRegistry.get(d)) ;
    l.setText(fBean.getSimpleName()+"\t"+fBean.getType()) ;     //$NON-NLS-1$
    return l ;	
} 

public String getInstanceName() {
	return fBean.getSimpleName() ;
}

public Label getReturnMethodDisplayInformation() {
	Label l = new Label() ;
		
	if (fBean.getReturnedMethod() != null) {
		ImageDescriptor d = null;
		if (fBean.getReturnedMethod().getMethodHandle() != null) {
			IJavaElement je = JavaCore.create(fBean.getInitMethod().getMethodHandle());
			if (je instanceof IMethod) {
				IMethod m = (IMethod) je;
				int flags = 0;
				try {
					flags = m.getFlags();
				}
				catch (JavaModelException e) {
					IJavaElement[] elms = fBean.getModel().getCompilationUnit().findElements(je);
					if (elms != null && elms.length > 0 && elms[0] instanceof IMethod) {
						m = (IMethod) elms[0];
						fBean.getInitMethod().setMethodHandle(m.getHandleIdentifier());
						try {
							flags = m.getFlags();
						}
						catch (JavaModelException e1) {}
					}
				}
				d = JavaElementImageProvider.getMethodImageDescriptor(false, flags);
			}

		}
		if (d == null)
			d = JavaElementImageProvider.getMethodImageDescriptor(false, 0);
		l.setIcon(fJavaImageRegistry.get(d));
		l.setText(fBean.getInitMethod().getMethodName() + CodeGenJavaMessages.getString("BeanDecoderAdapter.()_5")); //$NON-NLS-1$
		fpreviousLabel = l ;
	}
	else {
		if (fpreviousLabel == null) {		
			l.setIcon(null);
			l.setText(null);
		}
		else {
			l = fpreviousLabel ;
		}
	}
	return l ;		
}

public String getInitMethodName() {
	return fBean.getInitMethod().getMethodName() ;
}

/**
 * @deprecated
 */
public String getToolTipContent() {
	StringBuffer st = new StringBuffer() ;
	st.append(fBean.getSimpleName()) ;
	if (fBean.isInstanceVar())
	    st.append(CodeGenJavaMessages.getString("BeanDecoderAdapter._is_an_instance_variable_of_type__1")) ; //$NON-NLS-1$
	else
	    st.append(CodeGenJavaMessages.getString("BeanDecoderAdapter._is_a_local_variable_of_type__2")) ; //$NON-NLS-1$
    st.append(fBean.getType()+CodeGenJavaMessages.getString("BeanDecoderAdapter.._3")) ; //$NON-NLS-1$
    st.append(CodeGenJavaMessages.getString("BeanDecoderAdapter._nIt_is_initialized_by_the_method___4")+fBean.getInitMethod().getMethodName()+CodeGenJavaMessages.getString("BeanDecoderAdapter.()_5")) ; //$NON-NLS-1$ //$NON-NLS-2$
    st.append (CodeGenJavaMessages.getString("BeanDecoderAdapter._n_6")+CodeGenJavaMessages.getString("BeanDecoderAdapter.Paste_to_source_7")) ; //$NON-NLS-1$ //$NON-NLS-2$
    st.append (CodeGenJavaMessages.getString("BeanDecoderAdapter._n_8")+CodeGenJavaMessages.getString("BeanDecoderAdapter.Copy_text_to_clipboard_9")); //$NON-NLS-1$ //$NON-NLS-2$
    
    return st.toString();
}



}