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
 *  $RCSfile: InnerClassStyleHelper.java,v $
 *  $Revision: 1.15 $  $Date: 2005-09-06 16:40:32 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.*;
import java.util.logging.Level;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;


import org.eclipse.jem.internal.beaninfo.MethodProxy;
import org.eclipse.ve.internal.jcm.*;

import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.Method;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.model.CodeCallBackRef;
import org.eclipse.ve.internal.java.codegen.util.IEventSrcGenerator;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * @author Gili Mendel
 *
 */
public class InnerClassStyleHelper extends EventInvocationHelper implements IExpressionChangeListener {
	
	
	JavaClass fEventHandler = null ;
	HashMap	  fCallBackDecoders = new HashMap() ;  // This will hold a map by method name
	                                               //  a method may hold a Decoder, or a list of Decoders


   public InnerClassStyleHelper(
		BeanPart bean,
		Statement exp,
		IEventDecoder owner) {
		super(bean, exp, owner);
	}

				
		protected boolean processEvent(MethodInvocation event, boolean addToEMFmodel) {
			
			Expression exp = (Expression) event.arguments().get(0) ;
			
			int index = getInvocationIndex(addToEMFmodel);	
			if (index<0) 
				return false;
				
			if (!addToEMFmodel) {
				restoreInvocationFromModel(index);
				return true;
			}

			// Build a new Event Invocation, and compare it to the current one
			// If need to change it go for it, if not throw it away
			EventInvocation ee = JCMFactory.eINSTANCE.createEventInvocation();
			ee.setEvent(((EventInvocation)fEventInvocation).getEvent());
			if (exp instanceof SimpleName) {
				// Instance of Event
				SimpleName nr = (SimpleName) exp;
				JavaClass clazz = resolveInstance(nr.getIdentifier());
				if (clazz == null)
					return false;
				
				Listener l = null ;
				if (isInnerClass(clazz))
				    l = getInnerListener(clazz);
				else
				    l = getIsClassListener(clazz) ;
				if (l == null)
					return false;
				
				// 92611 : The listener returned could be a live one which will 
				// be disposed if things are different and the previous values are
				// cleared. Hence check if they are, and if so put in a new listener.
				if(l==fEventInvocation.getListener()){
					// l will be disposed if clearPrevious is called in addInvocationToModel()
					// Hence create a new one
					ListenerType listenerType = getListenerType(clazz.getName().replace('$', '.'),false,null,null,null) ;
					Listener newListener = org.eclipse.ve.internal.jcm.JCMFactory.eINSTANCE.createListener() ;
					newListener.setListenerType(listenerType) ;
					l = newListener;
				}
				
				ee.setListener(l);
				List impl = getExplicitTypeEventMethods(clazz);
				for (Iterator itr = impl.iterator(); itr.hasNext();) {
					 Method m = (Method) itr.next();
					 ICallbackDecoder d = (ICallbackDecoder) itr.next() ;
					 d.setCallBack(addMethod(ee,m,false));
				}
				addInvocationToModel(ee,index);
				return true;
			}
			else if (exp instanceof ClassInstanceCreation) {
				ClassInstanceCreation ae = (ClassInstanceCreation) exp;
				JavaClass clazz = resolveInstance(ae.getName().toString());
				if (clazz == null)
					return false;
				Listener l = null;
				if (isInnerClass(clazz))
					l = getInnerListener(clazz);
				else
					l = getIsClassListener(clazz);
				if (l == null)
					return false;
				ee.setListener(l);
				addInvocationToModel(ee, index);
				return true;
			}
			return false;
		}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#getCurrentExpression()
	 */
	public String getCurrentExpression() {
		return null;
	}
		
	public String toString() {
		return "InnerClassStyleHelper: "+fExpr ; //$NON-NLS-1$
	}

    protected   void addCallBackDecoderToMap (String mName, ICallbackDecoder d) {
    	Object current = fCallBackDecoders.get(mName) ;

    	if (current == null) { 
    	   fCallBackDecoders.put(mName,d) ;
    	}
    	else if (current instanceof ICallbackDecoder) {
    	      List l = new ArrayList() ;
    	      l.add(current) ;
    	      l.add(d) ;
    	      fCallBackDecoders.put(mName,l) ;
    	}
    	else {
    		List l = (List) current ;
    		l.add(d) ;    		    		 
    	}
    }
    
	protected void removeCallBackDecoderFromMap(String mName, ICallbackDecoder d) {
		Object current = fCallBackDecoders.get(mName);
		if (current == null) return ;
		
		if (current instanceof ICallbackDecoder) {
			fCallBackDecoders.remove(mName);
		}
		else {
			List l = (List) current;
			l.remove(d);
		}
	}
	
	protected ICallbackDecoder getCallBackDecoder (Callback c) {		
		Object current = fCallBackDecoders.get(c.getMethod().getName()) ;
		if (current == null) return null ;
		
		ICallbackDecoder d = null ;
		if (current instanceof ICallbackDecoder)
			d = (ICallbackDecoder) current ;
		else {
			List l = (List) current ;
			for (int i = 0; i < l.size(); i++) {
				ICallbackDecoder cbd = (ICallbackDecoder) l.get(i) ;
				if (cbd.getCallBack().equals(c)) {
					d = cbd ;
					break ;
				}
			}
		}
		return d ;
	}
    
    

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionChangeListener#expressionChanged(ICallbackDecoder)
	 */
	public void expressionChanged(ICallbackDecoder decoder) {
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.EventDecoderHelper#getSrcGenerator(Object[])
	 */
	protected IEventSrcGenerator getSrcGenerator(Object[] args) {
        return null ;  // No Style 2 generation at this point
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#removeCallBack(org.eclipse.ve.internal.jcm.Callback)
	 */
	public void removeCallBack(Callback c) {
		String mName = c.getMethod().getName() ;
		
		ICallbackDecoder d = getCallBackDecoder(c) ;
		unadaptCallBack(c) ;
		     
		if (d != null) {
		   d.removeChangeListener(this) ;
		   removeCallBackDecoderFromMap(mName, d) ;
		   d.deleteFromSrc() ;
		}
		else {
			if (JavaVEPlugin.isLoggingLevel(Level.FINEST))
				JavaVEPlugin.log("InnerClassStyleHelper.removeCallBack: No Callback decoder for"+mName) ; //$NON-NLS-1$
		}
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#addCallBack(org.eclipse.ve.internal.jcm.Callback)
	 */
	public void addCallBack(Callback c) {
		// Not supported
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#getCallBackSourceRange(org.eclipse.ve.internal.jcm.Callback)
	 */
	public ICodeGenSourceRange getCallBackSourceRange(Callback c) {
		 
		ICallbackDecoder d = getCallBackDecoder(c) ;
		if (d != null) {
			CodeCallBackRef exp = (CodeCallBackRef)d.getExprRef() ;
			return exp.getTargetSourceRange() ;
		}
		else {
			if (JavaVEPlugin.isLoggingLevel(Level.FINEST))
				JavaVEPlugin.log("InnerClassStyleHelper.removeCallBack: No Callback decoder for"+c.getMethod().getName()) ; //$NON-NLS-1$
			return null ;
		}
	}
	
	/**
	 * During parsing, we should have matched call back expressions to a given bean
	 */
	protected List getExplicitTypeEventMethods(JavaClass c) {
        
		fEventHandler = c ;
		List mList = new ArrayList() ;				
		for (Iterator iter = fbeanPart.getRefCallBackExpressions().iterator(); iter.hasNext();) {
			CodeCallBackRef exp = (CodeCallBackRef) iter.next();						
			if (getInnerName(c).equals(exp.getMethod().getTypeRef().getSimpleName())) {
				String mname = exp.getMethod().getMethodName() ;
				for (Iterator iterator = fEventDecorator.getListenerMethods().iterator(); iterator.hasNext();) {
					MethodProxy mp = (MethodProxy) iterator.next();
					if (mp.getMethod().getName().equals(mname)) {
//						if (!mList.contains(mp.getMethod()))  Style2 may have dup if statements
							mList.add(mp.getMethod()) ;
						ICallbackDecoder d =  (ICallbackDecoder)exp.getEventDecoder();
						d.addChangeListener(this) ;						
						addCallBackDecoderToMap(mp.getMethod().getName(),d) ;
						mList.add(d) ;
						break ;
					}
				}				
			}			
		}
		return mList ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionChangeListener#expressionChanged(org.eclipse.ve.internal.java.codegen.java.IPropertyEventDecoder)
	 */
	public void expressionChanged(IPropertyEventDecoder decoder) {	
	}

}
