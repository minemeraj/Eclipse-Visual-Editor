/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: EventDecoderHelper.java,v $
 *  $Revision: 1.2 $  $Date: 2003-11-11 22:40:20 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.*;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.compiler.ast.*;

import org.eclipse.ve.internal.jcm.*;
import org.eclipse.jem.internal.java.JavaClass;
import org.eclipse.jem.internal.java.Method;
import org.eclipse.jem.internal.java.impl.JavaClassImpl;

import org.eclipse.jem.internal.beaninfo.MethodProxy;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.ve.internal.java.core.JavaBeanEventUtilities;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.model.CodeEventRef;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.jcm.BeanSubclassComposition;

/**
 * @author Gili Mendel
 *
 */
public abstract class EventDecoderHelper implements IEventDecoderHelper {

	IEventDecoder fOwner = null;
	Statement fExpr; // This hold the parsed Source
	String fExprSig; // This holds the actual source
	BeanPart fbeanPart = null;
	String fdebugString = null;
	AbstractEventInvocation   fEventInvocation = null ;
	BeanSubclassComposition fbsc = null ;
	IEventSrcGenerator  fSrcGenerator = null ;
	String			    fIndentFiller = null ;
	EventDecoderAdapter	feventAdapter = null ;
	EStructuralFeature  fEventSF = null;
	

	abstract protected  IEventSrcGenerator getSrcGenerator(Object args[]) ;
	abstract protected  List getCallBackList() ;
	
	public EventDecoderHelper(BeanPart bean, Statement exp,  IEventDecoder owner) {
		fOwner = owner;
		fbeanPart = bean;
		fExpr = exp;
		if (fExpr != null)
			fdebugString = fExpr.toString();
	}
	
	protected BeanSubclassComposition getBSC() {
		if (fbsc != null) return fbsc ;
		fbsc = fbeanPart.getModel().getCompositionModel().getModelRoot();
		return fbsc ;
	}
	
    /**
     * Validates that the code is adding an event to the bean associated with this decoder
     */
    protected boolean isValidReceiver(MessageSend exp) {
    	if (exp.receiver instanceof MessageSend) {
    		MessageSend r = (MessageSend) exp.receiver ;
    		if (fbeanPart.getInitMethod() != null && 
    		    fbeanPart.getInitMethod().getMethodName().equals(new String(r.selector))) {
    		    return true ;
    		}
    	} else if (exp.receiver instanceof SingleNameReference ||
    	            exp.receiver instanceof ThisReference) {
    		String receiver = exp.receiver.toString() ;
    		if (fbeanPart.getSimpleName().equals(receiver))
    		   return true ;
    	}
		CodeGenUtil.logParsingError(fExpr.toString(), fbeanPart.getInitMethod().getMethodName(), "Invalid Receiver",true) ; //$NON-NLS-1$
    	return false ;  	
    }
    /**
     * Validate that the bean.addFooEvent() method reflects the event setting of this decoder
     */
    protected abstract boolean isValidSelector(String selector) ;
    protected abstract boolean isValidArguments (Expression[] exps) ;
    
    protected JavaClass getAllocatedType(TypeReference type) {
    	String name = null ;    	
    	if (type instanceof QualifiedTypeReference)    	
    	   name = CodeGenUtil.tokensToString(((QualifiedTypeReference)type).tokens) ;
    	else if (type instanceof SingleTypeReference) {
    	   name = fbeanPart.getModel().resolve(new String (((SingleTypeReference)type).token)) ;
    	}
    	   
    	if (name != null)
    	  return (JavaClass) org.eclipse.jem.internal.java.impl.JavaClassImpl.reflect(name,fbeanPart.getModel().getCompositionModel().getModelResourceSet()) ;
    	
    	return null ;
    }
    

    protected boolean sameString(String s1, String s2) {
    	    	    
    	if (s1 == null) {
    	   if (s2 == null) return true ;
    	   else return s2.length()==0 ;
    	}
        else if (s2 == null) {
                 return s1.length() == 0 ;
        } else
            return s1.equals(s2) ;
    }
    
    protected boolean sameClass(JavaClass c1, JavaClass c2) {
    	if (c1 == null)
    	   if (c2 == null) return true ;
    	   else return false ;
    	else if (c2 == null) return false ;
    	else return c1.equals(c2) ;
    }
    
    protected boolean sameArray (Object[] a1, Object[] a2) {
    	if (a1 == null)
    	   if (a2== null) return true ;
    	   else 
    	     if (a2.length>0) return false ;
    	     else return true ;
    	else
    	   if (a2==null)
    	      if (a1.length==0) return true ;
    	      else return false ;
    	   else 
    	      if (a1.length != a2.length) 
    	         return false ;
    	      else 
    	         for (int i = 0; i < a2.length; i++) {
					if (!a1[i].equals(a2[i])) return false ;
				}
        return true ;
    }
    /**
     * Get an existing ListenerType if one exists.
     * Create a new one otherwise.
     */
    protected ListenerType   getListenerType(String name, boolean thisPart, JavaClass extend, Object[] imp, JavaClass is) {
    	List lst = getBSC().getListenerTypes() ;
    	ListenerType result = null ;
    	for (int i = 0; i < lst.size(); i++) {
    		ListenerType lt = (ListenerType) lst.get(i) ;
    		if (!sameString(lt.getName(),name)) continue ;
    		else {
    			// For inner classes a name represents everyting, given that extends/implements may have
    			// been changed in the source and are not reflected in the model yet.
    			if (name != null && name.length()>0) {
    				// If it is the same name, this is it... implements/extend will be refreshed later
    				result = lt ;
    				break ;
    			}
    		}
    		if (lt.isThisPart() != thisPart) continue ;
    		if (!sameClass(lt.getExtends(),extend)) continue ;
    		if (!sameArray(lt.getImplements().toArray(),imp)) continue ;
    		result = lt ;
    		break ;
    	}
    	if (result == null) {
    	   result = JCMFactory.eINSTANCE.createListenerType() ;
    	   if (name != null)
    	     result.setName(name) ;
    	   result.setThisPart(thisPart) ;
    	   if (extend!=null)
    	      result.setExtends(extend) ;
    	   if (imp!=null && imp.length>0) 
    	   	  for (int i = 0; i < imp.length; i++) {
				result.getImplements().add(imp[i]) ;
			  }
    	}
    	return result ;
    }
    
    /**
     * Anonymous Listener is always a new listener, and may be sharing a ListenerType
     */
    protected Listener  getAnonymousListener(JavaClass extend,  Object[] imp) {
		Listener l = org.eclipse.ve.internal.jcm.JCMFactory.eINSTANCE.createListener() ;
		ListenerType lt = getListenerType(null,false,extend,imp,null) ;
		l.setListenerType(lt) ;
		return l ;
    }
    
	EStructuralFeature getEventSF() {
		if (fEventSF!=null) return fEventSF;
        //	events SF
		fEventSF = JavaInstantiation.getSFeature((IJavaObjectInstance)fbeanPart.getEObject(),JavaBeanEventUtilities.EVENTS);
		return fEventSF;		
	}
    protected void addInvocationToModel(AbstractEventInvocation ee, int index) {
    	ListenerType lt = ee.getListener().getListenerType() ;
    	if (lt.eContainer() == null) {
    		BeanSubclassComposition bsc = getBSC() ;
    		bsc.getListenerTypes().add(lt) ;
    	}
    	if (ee.eContainer() == null) {
    	 // events SF
    	 EStructuralFeature sf = getEventSF();
    	 if (index>=0)
		 ((List)fbeanPart.getEObject().eGet(sf)).add(index,ee) ;
    	 else
    	   ((List)fbeanPart.getEObject().eGet(sf)).add(ee) ;
    	}
    }
    
	/**
	 * 
	 * @return the index of the removed Invocation
	 */
	protected int removeInvocationToModel(AbstractEventInvocation ee) {
		ListenerType lt = ee.getListener().getListenerType();
		if (lt!=null)
		    lt.getListeners().remove(ee.getListener()) ;
		
		// events SF
		EStructuralFeature sf = JavaInstantiation.getSFeature((org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance) fbeanPart.getEObject(), JavaBeanEventUtilities.EVENTS);
		List eeList = (List) fbeanPart.getEObject().eGet(sf);
		int index = eeList.indexOf(ee);
		eeList.remove(ee);
		
		ee.getCallbacks().clear() ;
		if (ee instanceof PropertyChangeEventInvocation)
		  ((PropertyChangeEventInvocation)ee).getProperties().clear() ;
		
		return index;
	}

    protected String getInnerName (JavaClass c) {
    	if (!isInnerClass(c)) return null ;
    	return c.getName().substring(c.getName().indexOf('$')+1) ;
    }
    
    /**
     * Search the JDT model for a type associated with c
     */
	IType getInnerType(JavaClass c) throws JavaModelException {
		ICompilationUnit cu = fbeanPart.getModel().getWorkingCopyProvider().getSharedWorkingCopy();
		IType[] types = cu.getAllTypes();
		IType cType = null;
		String cName = c.getName() ;
		if (!cName.startsWith(c.getEPackage().getName()))
		   cName = c.getEPackage().getName()+"."+cName ; //$NON-NLS-1$
		for (int i = 0; i < types.length; i++) {
			if (types[i].getFullyQualifiedName('$').equals(cName)) {
				cType = types[i];
				break;
			}
		}
		return cType;
	}
	
	/**
     * Search the JDT model for a type associated with c
     */
	IType getMainType() throws JavaModelException {
		ICompilationUnit cu = fbeanPart.getModel().getWorkingCopyProvider().getSharedWorkingCopy();
        return cu.getTypes()[0] ;
	}
	
    /**
     * InnerClasses may not be persisted... use the JDT model to get extends/implements
     * Inner class will only set a Listener's name,  implements/extend
     */    
    protected Listener getInnerListener(JavaClass c) {
    	// check first to see if a listener exists. If not, create one
    	// For inner classes, the name is the only differentiator at this time
    	ListenerType lt = getListenerType(c.getName().replace('$', '.'),false,null,null,null) ;
    	Listener l = null ; 
    	if (lt.getListeners().size()>0) 
    		l = (Listener) lt.getListeners().get(0) ;
    	else {
			l = org.eclipse.ve.internal.jcm.JCMFactory.eINSTANCE.createListener() ;
		    l.setListenerType(lt) ;
    	}
		
		// Refresh the extend/implements in our listenerType
    	try {
			IType cType = getInnerType(c) ;
			if (cType == null) {
				// Can not find it in source ... get rid of it
				getBSC().getListenerTypes().remove(lt) ;
				return null ;
			}
			String interfaces[] = cType.getSuperInterfaceNames() ;
			for (int i = 0; i < interfaces.length; i++) {
				JavaClass it = (JavaClass) JavaClassImpl.reflect(interfaces[i],fbeanPart.getModel().getCompositionModel().getModelResourceSet()) ;
				if (it != null && it.isExistingType()) {
					if (it.isInterface()) 
					   if (!lt.getImplements().contains(it))
					      lt.getImplements().add(it) ;
				}
			}
			String superClass = fbeanPart.getModel().resolve(cType.getSuperclassName()) ;
			if (superClass!=null && superClass.length()>0) {
			    JavaClass it = (JavaClass) JavaClassImpl.reflect(superClass,fbeanPart.getModel().getCompositionModel().getModelResourceSet()) ;cType.getSuperclassName() ;
			    if (it != null && it.isExistingType() && lt.getExtends()!= it)
			       lt.setExtends(it)  ;
			}
		
		    // enter it to the model if needed	       
			if (lt.eContainer() == null)
			  getBSC().getListenerTypes().add(lt) ;
		}
		catch (JavaModelException e) {
			return null ;
		}
		return l ;
    }
    
    protected boolean isInnerClass(JavaClass c) {
   	try {
			return getInnerType(c) != null ;
		} catch (JavaModelException e) {
			return false ;
		}
    }
    /**
     * An explicit class is used as a listener.
     * This listener will set the "is" property, unless
     * if it is an inner class.  In that case it will set it will set the name, extend/implements
     */
    protected Listener getIsClassListener(JavaClass c) {

		if (isInnerClass(c))
			return getInnerListener(c);

        ListenerType lt = getListenerType(c.getName().replace('$', '.'),false, null, null, c) ;
        Listener l = null ;
        if (lt.getListeners().size()>0) 
    		l = (Listener) lt.getListeners().get(0) ;
    	else {
			l = org.eclipse.ve.internal.jcm.JCMFactory.eINSTANCE.createListener() ;
		    l.setListenerType(lt) ;
    	}
		return l;
    }
    
    /**
     * Process the Anonymous  type
     * @return true if sucessful
     */
    protected abstract boolean processEvent(MessageSend event)  ;
    
	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#decode()
	 */
	public boolean decode() throws CodeGenException {
		if (fExpr != null && fExpr instanceof MessageSend) {
			MessageSend exp = (MessageSend)fExpr ;
			if (isValidReceiver(exp) &&
			    isValidSelector(new String (exp.selector)) &&
			    isValidArguments(exp.arguments)) {
				return processEvent(exp) ;
			}
		}		
		return false;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#generate(Object[])
	 */
	public String generate(Object[] args) throws CodeGenException {
		
        getSrcGenerator(args).setSeperator(fbeanPart.getModel().getLineSeperator()) ;
		String result = getSrcGenerator(args).generateEvent() ;
		return result ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#delete()
	 */
	public void delete() {
		cleanUpPreviousIfNedded();
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#primRefreshFromComposition(String)
	 */
	public String primRefreshFromComposition(String exprSig) throws CodeGenException {
		return null;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#primIsDeleted()
	 */
	public boolean primIsDeleted() {
		return fEventInvocation.getListener() == null ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#adaptToCompositionModel(IEventDecoder)
	 */
	public void adaptToCompositionModel(IEventDecoder decoder) {	
		// Make sure we do not have stale, need to be a bit smarter of how we do this.
		unadaptToCompositionModel() ;
    
		BeanDecoderAdapter a = (BeanDecoderAdapter) EcoreUtil.getExistingAdapter(fbeanPart.getEObject(),ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER) ;
		EventDecoderAdapter adapter = new EventDecoderAdapter(decoder) ;		   	  
		feventAdapter = adapter ;
		a.addSettingAdapter(a.getEventsSF(),adapter) ;
		
		fEventInvocation.eAdapters().add(adapter) ;
		
		List callbacks = fEventInvocation.getCallbacks() ;
		for(int i=0; i<callbacks.size(); i++){
			Callback c = (Callback) callbacks.get(i) ;
			adaptCallBack(c) ;
		}
	}
	
	protected void adaptCallBack(Callback c) {
		if (feventAdapter != null)
		   c.eAdapters().add(feventAdapter.getCallBackSourceRangeAdapter(c)) ;
		else
		   org.eclipse.ve.internal.java.core.JavaVEPlugin.log("EventDecoderHelper.adaptCallBack: NoAdapter",org.eclipse.jem.internal.core.MsgLogger.LOG_WARNING) ;  //$NON-NLS-1$
	}
	
	protected void unadaptCallBack(Callback c) {
		ICodeGenAdapter ca = (ICodeGenAdapter) EcoreUtil.getExistingAdapter(c,ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER) ;
		if (ca != null)
			c.eAdapters().remove(ca); 
	}

	/**
	 * Returns the eventDecorator.
	 * @return EventSetDecorator
	 */
	public AbstractEventInvocation getEventInvocation() {
		return fEventInvocation;
	}

	/**
	 * Sets the eventDecorator.
	 * @param eventDecorator The eventDecorator to set
	 */
	public void setEventInvocation(AbstractEventInvocation ei) {
		if (fEventInvocation != null && fEventInvocation!=ei)
		   cleanUpPreviousIfNedded();
		fEventInvocation = ei;
	}
	
	/**
	 * Add a method m, to an EventInvocation
	 */
	protected Callback addMethod (AbstractEventInvocation ee, Method m, boolean sharedMethod) {
		Callback c = JCMFactory.eINSTANCE.createCallback() ;
		c.setSharedScope(sharedMethod) ;
		c.setMethod(m) ;
		ee.getCallbacks().add(c) ;
		return c ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#getFiller(String)
	 */
	public String getFiller(String filler) {
		return fIndentFiller;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#setFiller(String)
	 */
	public void setFiller(String filler) {	
		fIndentFiller = filler ;
		if (fSrcGenerator != null)
		   fSrcGenerator.setIndent(fIndentFiller) ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#unadaptToCompositionModel()
	 */
	public void unadaptToCompositionModel() {
		BeanDecoderAdapter a = (BeanDecoderAdapter) EcoreUtil.getExistingAdapter(fbeanPart.getEObject(),ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER) ;
		a.removeSettingAdapter(a.getEventsSF(),feventAdapter) ;
		if (feventAdapter != null)
		   fEventInvocation.eAdapters().remove(feventAdapter) ;
		   
		List callbacks = fEventInvocation.getCallbacks() ;
		for(int i=0; i<callbacks.size(); i++){
			Callback c = (Callback) callbacks.get(i) ;
			unadaptCallBack(c) ; 
		}  
		feventAdapter = null ;
	}
	
	protected int cleanUpPreviousIfNedded() {
		if (fEventInvocation.getListener() != null) {
			unadaptToCompositionModel() ;
			return removeInvocationToModel(fEventInvocation) ;
		}
		else
		  return -1 ;		
	}
	
	protected int getInvocationIndex() {
		
		int index=0 ;
		// In some ordering cases, we may not be able to add an event in the proper index
		boolean  sanityCheck = false ;  
		CodeEventRef exp = (CodeEventRef) fOwner.getExprRef() ;
		for (Iterator events = exp.getMethod().getEventExpressions(); events.hasNext(); ){
			// Events are ordered according to their src. offsets
			// Find the event's code index
			CodeEventRef e = (CodeEventRef)events.next();
			if (e == exp) {
				// passed our test
				sanityCheck = true ;
				break ;
			}
			else
			  // Consider events for a given bean (e.g., initConnectons())
			  if (e.getBean().equals(fbeanPart))
			     index++;
		}		
		List events = (List)fbeanPart.getEObject().eGet(getEventSF()) ;
		sanityCheck &= events.size()>=index;
		return sanityCheck?index : -1;						
	}
	
	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#setDecodingContent(Statement)
	 */
	public void setDecodingContent(Statement exp) {
	   fExpr = exp ;	
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#getPropertyEventSourceRange(org.eclipse.ve.internal.jcm.PropertyEvent)
	 */
	public ICodeGenSourceRange getPropertyEventSourceRange(PropertyEvent pe) {
		// propertyChange decoders should overide this method
		return null;
	}
	
	
	protected BeanPart findABean(String simpleName) {
		List l = fbeanPart.getModel().getBeans() ;
		for (int i = 0; i < l.size(); i++) {
			if (((BeanPart)l.get(i)).getSimpleName().equals(simpleName) &&
				(((BeanPart)l.get(i)).getInitMethod() == fbeanPart.getInitMethod()))
				return (BeanPart)l.get(i);				
		}
		return null ;
	}
	/**
	 * This method resolve an instance variable name into the type that it 
	 * represents.
	 * 
	 * Need to re-parse, as the source code may have changed.
	 */	
	protected JavaClass resolveInstance(char[] instanceName) {
			
		try {
			IType t = getMainType();
			if (t == null)
			   return null;
			IField fields[] = t.getFields();
			String targetName = new String(instanceName);
			for (int i = 0; i < fields.length; i++) {
				if (fields[i].getElementName().equals(targetName)) {
					String type = Signature.toString(fields[i].getTypeSignature()) ;
					type = fbeanPart.getModel().resolve(type);
					return (JavaClass) JavaClassImpl.reflect(type, fbeanPart.getModel().getCompositionModel().getModelResourceSet());
				}
			}
			// Look for an actuall class type
			IType[] tps = t.getCompilationUnit().getTypes() ;
			for(int i=1; i<tps.length; i++)
			  if (tps[i].getElementName().equals(targetName)) {
			  	String type = tps[i].getFullyQualifiedName() ;
				type = fbeanPart.getModel().resolve(type);
				return (JavaClass) JavaClassImpl.reflect(type, fbeanPart.getModel().getCompositionModel().getModelResourceSet());
			  }
		}
		catch (JavaModelException e) {}		
		// Single reference may be for a an instance of a class
		// Figure out if we parsed an instance for it
		BeanPart bp = findABean(new String (instanceName)) ;
		if (bp != null) {
		  return (JavaClass) JavaClassImpl.reflect(bp.getType(), fbeanPart.getModel().getCompositionModel().getModelResourceSet());
		}
		return null;
	}
	
	protected String getEventArgName() {
			String result = null ;
			if (fExpr != null && fExpr instanceof MessageSend) {
				MessageSend ms = (MessageSend) fExpr ;
				if (ms.arguments != null && ms.arguments.length>0 && ms.arguments[0] instanceof QualifiedAllocationExpression) {
					QualifiedAllocationExpression alt = (QualifiedAllocationExpression) ms.arguments[0] ;
				
					if (alt.anonymousType instanceof AnonymousLocalTypeDeclaration) {
						AnonymousLocalTypeDeclaration lt = (AnonymousLocalTypeDeclaration) alt.anonymousType ;
						if(lt.methods != null && lt.methods.length>0) {
							MethodDeclaration md = (MethodDeclaration) lt.methods[0] ;
							if (md.arguments != null && md.arguments.length>0 && (md.arguments[0] instanceof Argument)) {
								Argument arg = (Argument) md.arguments[0] ;
								return arg.name();
							}
						}
					}													
				}						
			}
			return result ;
		}
	
	/**
	 * Analyze an explicity type, and figure out which (event) methods it implements
	 * --- at this point only method implemented by the class, not its supers
	 */
	protected List getExplicitTypeEventMethods(JavaClass c) {
		if (isInnerClass(c)) 
		   return getInnerTypeEventMethods(c) ;
    	
		return Collections.EMPTY_LIST;   
		// TODO For external classes, we will not list implemented methods		
//		List listenMethods = fEventDecorator.getListenerMethods() ;
//		List classMethods = c.getMethods()	;
//		for (Iterator classItr = classMethods.iterator(); classItr.hasNext();) {
//			Method cM = (Method) classItr.next();
//			for (Iterator listenIter = listenMethods.iterator(); listenIter.hasNext();) {
//				Method lM = ((MethodProxy)listenIter.next()).getMethod();
//				if (cM.getName().equals(lM.getName()) &&
//					cM.getParameters().size() == lM.getParameters().size()) {
//					com.ibm.etools.cde.CDEHack.fixMe("Need to compare return, and param sig");
//					ml.add(lM) ;
//					break ;
//				}
//			}
//		}
	}
	
	
	/**
	 * Analyze an Inner class from the working copy to figure out which event methods it implements
	 */
	protected List getInnerTypeEventMethods(JavaClass c) {
		List ml = new ArrayList();     	
		try {
			IType t = getInnerType(c) ;    	
			if (t == null) return null ;
			
			// In Style 3, all the methods count
			IMethod iMethods[] = t.getMethods() ;
			List    listenMethods = getCallBackList() ;
			for (int i = 0; i < iMethods.length; i++) {
				for (Iterator listenIter = listenMethods.iterator(); listenIter.hasNext();) {
				   Object element = listenIter.next() ;
				   
				   Method lM = element instanceof MethodProxy ? ((MethodProxy)element).getMethod() : (Method)element ;
				   if (iMethods[i].getElementName().equals(lM.getName()) &&
					iMethods[i].getNumberOfParameters() == lM.getParameters().size()) {
						// TODO Need to compare return, and param sig 
						ml.add(lM) ;
						break ;					
					}
				}				
			}
		}
		catch (JavaModelException e) {
			return null ;
		}    	    	
		return ml ;    	
	}


}
