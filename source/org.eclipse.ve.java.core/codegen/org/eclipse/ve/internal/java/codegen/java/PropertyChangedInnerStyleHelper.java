/*
 * Created on Jun 6, 2003
 * by gmendel
 *
*******************************************************************************
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
 *  $RCSfile: PropertyChangedInnerStyleHelper.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-13 16:16:38 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.*;

import org.eclipse.jdt.internal.compiler.ast.*;

import org.eclipse.ve.internal.jcm.*;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.Method;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.model.CodeCallBackRef;
import org.eclipse.ve.internal.java.codegen.util.IEventSrcGenerator;

/**
 * @author gmendel
 */
public class PropertyChangedInnerStyleHelper extends PropertyChangeInvocationHelper implements IExpressionChangeListener{
	
	JavaClass fEventHandler = null ;
	HashMap	  fPropertyEventDecoders = new HashMap() ;  // This will hold a map by method name
													  //  a method may hold a Decoder, or a list of Decoders
	

	public PropertyChangedInnerStyleHelper(BeanPart bean, Statement exp, IEventDecoder owner) {
				super(bean, exp, owner);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.EventDecoderHelper#getSrcGenerator(java.lang.Object[])
	 */
	protected IEventSrcGenerator getSrcGenerator(Object[] args) {		
		return null;  // No generation to style 2 at this time
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.EventDecoderHelper#processEvent(org.eclipse.jdt.internal.compiler.ast.MessageSend)
	 */
	protected boolean processEvent(MessageSend event) {
							
		Expression exp = event.arguments[event.arguments.length-1] ;
		
		cleanUpPreviousIfNedded() ;
		int index = getInvocationIndex();			
		PropertyChangeEventInvocation ee = (PropertyChangeEventInvocation) fEventInvocation ;   
		
		Method listenRegMethod = getAddMethod(event) ;
		if (listenRegMethod != null)
		   ee.setAddMethod(listenRegMethod) ;	 
		          
		if (exp instanceof SingleNameReference) {
			// Instance of Event
			SingleNameReference nr = (SingleNameReference) exp;
			JavaClass clazz = resolveInstance(nr.token);
			if (clazz == null)
				return false;
				
			Listener l = null ;
			if (isInnerClass(clazz))
				l = getInnerListener(clazz);
			else
				l = getIsClassListener(clazz) ;
			if (l == null)
				return false;
			ee.setListener(l);
			List impl = getExplicitProperties(clazz, ee);
			for (Iterator itr = impl.iterator(); itr.hasNext();) {
				 PropertyEvent property = (PropertyEvent) itr.next();
				 IPropertyEventDecoder d = (IPropertyEventDecoder) itr.next() ;
				 d.setPropertyEvent(property) ;
			}
			addInvocationToModel(ee,index);
			return true;
		}
		return false;

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#getPriorityOfExpression()
	 */
	public Object getPriorityOfExpression() {		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#getCurrentExpression()
	 */
	public String getCurrentExpression() {		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#removeCallBack(org.eclipse.ve.internal.jcm.Callback)
	 */
	public void removeCallBack(Callback c) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#addCallBack(org.eclipse.ve.internal.jcm.Callback)
	 */
	public void addCallBack(Callback c) {
		// Not Supported
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#addPropertyEvent(org.eclipse.ve.internal.jcm.PropertyEvent)
	 */
	public void addPropertyEvent(PropertyEvent c) {
		// Not Supported
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#removePropertyEvent(org.eclipse.ve.internal.jcm.PropertyEvent)
	 */
	public void removePropertyEvent(PropertyEvent p) {
		String pName = p.getPropertyName() ;
		
        IPropertyEventDecoder d = getPropertyEventDecoder(p) ;
		unadaptPropertyEvent(p) ;		     
        if (d != null) {
             d.removeChangeListener(this) ;
             removePropertyDecoderFromMap(pName, d) ;
             d.delete() ;
        }
        else {
	       org.eclipse.ve.internal.java.core.JavaVEPlugin.log("PropertyChangedInnerStyleHelper.removePropertyEvent: No Property decoder for"+pName) ; //$NON-NLS-1$
        }
		

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#getCallBackSourceRange(org.eclipse.ve.internal.jcm.Callback)
	 */
	public ICodeGenSourceRange getCallBackSourceRange(Callback c) {
		// TODO Auto-generated method stub
		return null;
	}
	


	/**
	 * search for: foo.getPropertyName().equals("propertyName")
	 * 
	 * @param condition
	 * @return
	 */
	protected static String processCondition (Expression condition) {
		String result = null;
		if (condition instanceof MessageSend) {
			if ((((MessageSend) condition).receiver) instanceof MessageSend) {
				MessageSend rec = (MessageSend) (((MessageSend) condition).receiver);
				if (new String(rec.selector).equals(PropertyChangeInvocationHelper.PROPERTY_NAME_GETTER)) {
					MessageSend ms = (MessageSend) condition;
					if (ms.arguments != null && ms.arguments.length == 1 && ms.arguments[0] instanceof StringLiteral)
						result = new String(((StringLiteral) ms.arguments[0]).source());
				}
			}
		}
		else if (condition instanceof BinaryExpression) {
			BinaryExpression be = (BinaryExpression) condition;
			result = processCondition(be.left);
			if (result == null)
				result = processCondition(be.right);
		}
		return result;
	}
	
	public static String parsePropertyFromIfStatement(IfStatement stmt) {
		return processCondition(stmt.condition) ;		
	}
	/**
	 * During parsing, we should have matched call back expressions to a given bean
	 */
	protected List getExplicitProperties(JavaClass c, PropertyChangeEventInvocation ee) {
				
		List args = new ArrayList() ;
		args.add("java.beans.PropertyChangeEvent") ;  //$NON-NLS-1$
				        
		fEventHandler = c ;
		List pList = new ArrayList() ;				
		for (Iterator iter = fbeanPart.getRefCallBackExpressions().iterator(); iter.hasNext();) {
			CodeCallBackRef exp = (CodeCallBackRef) iter.next();
			if (getInnerName(c).equals(exp.getMethod().getTypeRef().getName())) {
				String mname = exp.getMethod().getMethodName() ;
				if (mname.equals(PROPERTY_CALLBACK_NAME) && exp.getExpression() instanceof IfStatement) {
					String property = parsePropertyFromIfStatement((IfStatement)exp.getExpression()) ;					
					
					PropertyEvent pe = JCMFactory.eINSTANCE.createPropertyEvent();
					pList.add(pe) ;
					
					IPropertyEventDecoder d = (IPropertyEventDecoder) exp.getEventDecoder();
					d.addChangeListener(this);					
					pList.add(d);
					
					pe.setPropertyName(property);
					pe.setUseIfExpression(true);
					ee.getProperties().add(pe);
					addPropertyDecoderToMap(pe.getPropertyName(), d);								
				}				
			}			
		}
		return pList ;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionChangeListener#expressionChanged(org.eclipse.ve.internal.java.codegen.java.ICallbackDecoder)
	 */
	public void expressionChanged(ICallbackDecoder decoder) {		
	}
	
	protected   void addPropertyDecoderToMap (String pName, IPropertyEventDecoder d) {
		 Object current = fPropertyEventDecoders.get(pName) ;

		 if (current == null) { 
			fPropertyEventDecoders.put(pName,d) ;
		 }
		 else if (current instanceof IPropertyEventDecoder) {
			   List l = new ArrayList() ;
			   l.add(current) ;
			   l.add(d) ;
			   fPropertyEventDecoders.put(pName,l) ;
		 }
		 else {
			 List l = (List) current ;
			 l.add(d) ;    		    		 
		 }
	 }
	 
	protected void removePropertyDecoderFromMap(String pName, IPropertyEventDecoder d) {
		Object current = fPropertyEventDecoders.get(pName) ;
		if (current == null) return ;
		
		if (current instanceof IPropertyEventDecoder) {
			fPropertyEventDecoders.remove(pName);
		}
		else {
			List l = (List) current;
			l.remove(d);
		}
	}
	 
	protected IPropertyEventDecoder getPropertyEventDecoder (PropertyEvent pe) {		
		Object current = fPropertyEventDecoders.get(pe.getPropertyName()) ;
		if (current == null) return null ;
		
		IPropertyEventDecoder d = null ;
		if (current instanceof IPropertyEventDecoder)
			d = (IPropertyEventDecoder) current ;
		else {
			List l = (List) current ;
			for (int i = 0; i < l.size(); i++) {
				IPropertyEventDecoder cbd = (IPropertyEventDecoder) l.get(i) ;
				if (cbd.getPropertyEvent().equals(pe)) {
					d = cbd ;
					break ;
				}
			}
		}
		return d ;
	}
	 
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IEventDecoderHelper#getPropertyEventSourceRange(org.eclipse.ve.internal.jcm.PropertyEvent)
	 */
	public ICodeGenSourceRange getPropertyEventSourceRange(PropertyEvent pe) {
		IPropertyEventDecoder d = getPropertyEventDecoder(pe);
		if (d != null) {
			CodeCallBackRef exp = (CodeCallBackRef) d.getExprRef();
			return exp.getTargetSourceRange();
		}
		else {
			org.eclipse.ve.internal.java.core.JavaVEPlugin.log("PropertyChangedInnerStyleHelper.getPropertyEventSourceRange: No Property decoder for" + pe.getPropertyName()); //$NON-NLS-1$
			return null;
		}
	}

	

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionChangeListener#expressionChanged(org.eclipse.ve.internal.java.codegen.java.IPropertyEventDecoder)
	 */
	public void expressionChanged(IPropertyEventDecoder decoder) {
	}

}
