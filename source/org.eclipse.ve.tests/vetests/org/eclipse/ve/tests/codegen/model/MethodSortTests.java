/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * Created on Apr 27, 2004 by Gili Mendel
 */
package org.eclipse.ve.tests.codegen.model;

import java.util.Iterator;

import junit.framework.TestCase;

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper.VEexpressionPriority;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;

/**
 * @author Gili Mendel
 *
 */
public class MethodSortTests extends TestCase {

	CodeTypeRef   tr = new CodeTypeRef("dummyType", new BeanDeclModel());
	CodeMethodRef method =  null;
	BeanPartDecleration decl1 = new BeanPartDecleration("DummyBean 1","noType");
	BeanPartDecleration decl2 = new BeanPartDecleration("DummyBean 2","noType");
	BeanPart bp1 = new BeanPart (decl1);
	BeanPart bp2 = new BeanPart (decl2);
	Object   sf = EcorePackage.eINSTANCE.getEAnnotation_Contents();
	
	int REGULAR = 10;
	int LOW = 5;
	
	class DummyExpression extends  CodeExpressionRef {		
	
		VEexpressionPriority pri = null ;
		int offset=-1;
		String filler="";
		
		public DummyExpression (BeanPart bp) {
		    super(method, bp);
        }        		
		/* (non-Javadoc)
		 * @see org.eclipse.ve.internal.java.codegen.model.CodeExpressionRef#getPriority()
		 */
		public VEexpressionPriority getPriority() {
			return pri;
		}
		public void setPriority (VEexpressionPriority p) {
			pri=p;
		}
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append(fBean.getSimpleName()+": ");
			
			String states = "States: "; //$NON-NLS-1$
			if(isStateSet(STATE_EXIST))
				states = states.concat("EXIST#"); //$NON-NLS-1$
			if(isStateSet(STATE_EXP_IN_LIMBO))
				states = states.concat("INLIMBO#"); //$NON-NLS-1$
			if(isStateSet(STATE_IMPLICIT))
				states = states.concat("IMPLICIT#"); //$NON-NLS-1$
			if(isStateSet(STATE_IN_SYNC))
				states = states.concat("INSYNC#"); //$NON-NLS-1$	
			if(isStateSet(STATE_INIT_EXPR))
				states = states.concat("INIT#"); //$NON-NLS-1$			
			if(isStateSet(STATE_SRC_LOC_FIXED))
				states = states.concat("SRCLOCFIXED#"); //$NON-NLS-1$
			if(isStateSet(STATE_UPDATING_SOURCE))
				states = states.concat("UPDATINGSRC#"); //$NON-NLS-1$
		    if(isStateSet(STATE_EXP_NOT_PERSISTED))
		        states = states.concat("NOTPERSISTED#"); //$NON-NLS-1$    
		    if(isStateSet(STATE_MASTER))
		        states = states.concat("MASTER#"); //$NON-NLS-1$
		    if(isStateSet(STATE_MASTER_DELETED))
		        states = states.concat("MASTER_DELETED#"); //$NON-NLS-1$
		    if(isStateSet(STATE_DELETE))
		    	states = states.concat("DELETE#"); //$NON-NLS-1$    
		    if (isStateSet(STATE_NO_SRC))
		   	    states = states.concat("STATE_NO_SRC#"); //$NON-NLS-1$
		   	if (isStateSet(STATE_NO_MODEL))
		   		states = states.concat("STATE_NO_MODEL#"); //$NON-NLS-1$    
		    sb.append(states);
		    sb.append(":Priority="+pri.toString());
			sb.append("offset("+offset+")"+"filler<"+ filler +">");
			return sb.toString();
		}
				
		
		/* (non-Javadoc)
		 * @see org.eclipse.ve.internal.java.codegen.model.CodeExpressionRef#setFillerContent(java.lang.String)
		 */
		public void setFillerContent(String filler) throws CodeGenException {
 			this.filler = filler; 
		}
		/* (non-Javadoc)
		 * @see org.eclipse.ve.internal.java.codegen.model.CodeExpressionRef#getFillerContent()
		 */
		public String getFillerContent() {
			return filler;
		}
		/* (non-Javadoc)
		 * @see org.eclipse.ve.internal.java.codegen.model.AbstractCodeRef#getOffset()
		 */
		public int getOffset() {
		   return offset;
		}
		/* (non-Javadoc)
		 * @see org.eclipse.ve.internal.java.codegen.model.AbstractCodeRef#setOffset(int)
		 */
		public void setOffset(int off) {
			offset = off;
		}
	}
	
	public class DummyEObject extends EObjectImpl {
		public DummyEObject() {
			super();
		}
	}
	
	int offset;
	/**
	 * 
	 * @param bp  Bean
	 * @param fixed true if this expression position is fixed
	 * @param init true if init expression
	 * @param reference if it reference the other bean (not bp), or bp1 by default
	 * @param pri priority structure
	 * @return
	 * 
	 * @since 1.1.0
	 */
	DummyExpression createExpression(BeanPart bp, boolean fixed, boolean init, boolean reference, VEexpressionPriority pri) {
		DummyExpression exp = new DummyExpression(bp);
		exp.setState(CodeExpressionRef.STATE_SRC_LOC_FIXED, fixed);
		exp.setState(CodeExpressionRef.STATE_INIT_EXPR,init);
		exp.setState (CodeExpressionRef.STATE_IN_SYNC,true);
		offset+=10;
		exp.setOffset(offset);
		if (bp.getEObject()==null) {
			try {
		       bp.setEObject(new DummyEObject());
			}
			catch (Exception e) {};
		}
		if (reference) {
			if (bp==bp1)
				exp.getReferences().add(bp2.getEObject());
			else
				exp.getReferences().add(bp1.getEObject());
		}
		exp.setPriority(pri);		
		return exp;		
	}
	
	/**
	 * 
	 * @param zOrder if >=0 set up a priority order
	 * @param regular priority of REGULAR if true, LOW if false
	 * @return
	 * 
	 * @since 1.1.0
	 */
	VEexpressionPriority getPriority(int zOrder, boolean regular) {
		Object[] z ;
		if (zOrder>=0) 
			z = new Object[] { new Integer(zOrder), sf };
		else
			z = null;
		
		if (regular)
			return new VEexpressionPriority(REGULAR,z);
		else
			return new VEexpressionPriority(LOW,z);
	}

	
	protected void buildMethod() {
	
	    method = new CodeMethodRef(tr, "dummyMethod") ;
		offset=0;			
		
		
		// b1 init expression z order = 1
		// b1 regular expression
		// b1 regular expression
		// b1 low priority expression
		
		// b2 init expression z order = 2
		// b2 regular expression
		// b2 regular expression
		// b2 low priority expression
		
		// b1 regular expression z order 4
		
		createExpression(bp1, true, true, false, getPriority(1,true));
		createExpression(bp1, true, false, false, getPriority(-1,true));		   
		createExpression(bp1, true, false, false, getPriority(-1,true));
		createExpression(bp1, true, false, false, getPriority(-1,false));
		
		createExpression(bp2, true, true, false, getPriority(2,true));
		createExpression(bp2, true, false, false, getPriority(-1,true));		   
		createExpression(bp2, true, false, false, getPriority(-1,true));
		createExpression(bp2, true, false, false, getPriority(-1,false));
		
		createExpression(bp1, true, false, true, getPriority(4,true));
		
	}
	
	protected CodeExpressionRef getExp (int index) {	   
	    for (Iterator iter = method.getExpressions(); iter.hasNext(); index--) {
			CodeExpressionRef element = (CodeExpressionRef) iter.next();
			if (index==0) return element;			
		}
		return null;
	}
	
	/**
	 * @deprecated
	 * @param bp
	 * @param pri
	 * @param index
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected CodeExpressionRef create (BeanPart bp, int pri, int index) {
	   	VEexpressionPriority p = new VEexpressionPriority(pri, null);
		DummyExpression exp = new DummyExpression(bp);
		exp.setPriority(p);		
		return exp;
	}
	
	/**
     *  Add element with low priority to bp1
     **/
	public void test1() {
	
		CodeExpressionRef exp = createExpression(bp1, false, false, false, getPriority(-1,false));
		try {
			method.updateExpressionOrder();
		} catch (CodeGenException e) {
			e.printStackTrace();
		}
	    assertEquals("Failed to add low priority expression to bp1",exp,getExp(4)) ;
	}
	
	/**
     *  Add element with high priority to bp1
     **/
	public void test2() {
	
		CodeExpressionRef exp = createExpression(bp1, false, false, false, getPriority(-1,true));
		try {
			method.updateExpressionOrder();
		} catch (CodeGenException e) {
			e.printStackTrace();
		}
	    assertEquals("Failed to add low priority expression to bp1",exp,getExp(3)) ;
	}
	
	/**
     *  Add element with regular priority to bp2
     **/
	public void test3() {
	
		CodeExpressionRef exp = createExpression(bp2, false, false, false, getPriority(-1,false));
		try {
			method.updateExpressionOrder();
		} catch (CodeGenException e) {
			e.printStackTrace();
		}
	    assertEquals("Failed to add low priority expression to bp1",exp,getExp(8)) ;
	}
	
	/**
     *  Add element with regular priority to bp2
     **/
	public void test4() {
		CodeExpressionRef exp = createExpression(bp2, false, false, false, getPriority(-1,true));
		try {
			method.updateExpressionOrder();
		} catch (CodeGenException e) {
			e.printStackTrace();
		}
	    assertEquals("Failed to add low priority expression to bp1",exp,getExp(7)) ;
	}	


	/**
     *  Add element with high priority to bp1
     **/
	public void test5() {
		VEexpressionPriority p = getPriority(-1,true);
		p = new VEexpressionPriority(REGULAR+1,p.getProiorityIndex());
		CodeExpressionRef exp = createExpression(bp1, false, false, false, p);
		try {
			method.updateExpressionOrder();
		} catch (CodeGenException e) {
			e.printStackTrace();
		}
	    assertEquals("Failed to add low priority expression to bp1",exp,getExp(1)) ;
	}	

	/**
     *  Add low priority b2 element with Z order of 4
     **/
	public void test6() {
		CodeExpressionRef exp = createExpression(bp2, false, false, false, getPriority(5,false));
		try {
			method.updateExpressionOrder();
		} catch (CodeGenException e) {
			e.printStackTrace();
		}
	    assertEquals("Failed to add low priority expression to bp1",exp,getExp(9)) ;
	}	

	/**
     *  Add regular priority b2 element with Z order of 3
     **/
	public void test7() {
		CodeExpressionRef exp = createExpression(bp2, false, false, false, getPriority(3,true));
		try {
			method.updateExpressionOrder();
		} catch (CodeGenException e) {
			e.printStackTrace();
		}
	    assertEquals("Failed to add low priority expression to bp1",exp,getExp(7)) ;
	}			
	/**
     *  Add regular priority b1 that is dependant on b2
     **/		
	public void test8() {
		CodeExpressionRef exp = createExpression(bp1, false, false, true, getPriority(-1,true));
		try {
			method.updateExpressionOrder();
		} catch (CodeGenException e) {
			e.printStackTrace();
		}
	    assertEquals("Failed to add low priority expression to bp1",exp,getExp(8)) ;
	}			
	
	/**
     *  Add regular priority b2 that is dependant on b1
     **/			
	public void test9() {
		CodeExpressionRef exp = createExpression(bp2, false, false, true, getPriority(-1,true));
		try {
			method.updateExpressionOrder();
		} catch (CodeGenException e) {
			e.printStackTrace();
		}
	    assertEquals("Failed to add low priority expression to bp1",exp,getExp(7)) ;
	}
	/**
     *  Add a brand new bean
     **/		
	public void test10() {
		CodeExpressionRef exp = createExpression(new BeanPart(decl1), false, true, false, getPriority(-1,true));
		try {
			method.updateExpressionOrder();
		} catch (CodeGenException e) {
			e.printStackTrace();
		}
	    assertEquals("Failed to add low priority expression to bp1",exp,getExp(0)) ;
		}
	
	/**
     *  Add a brand new bean that is dependant on b1
     **/			
	public void test11() {
		CodeExpressionRef exp = createExpression(new BeanPart(decl1), false, true, true, getPriority(-1,true));
		try {
			method.updateExpressionOrder();
		} catch (CodeGenException e) {
			e.printStackTrace();
		}
	    assertEquals("Failed to add low priority expression to bp1",exp,getExp(4)) ;		
		}				
	
	/**
	 * Add a brand new bean with order priority of 3
	 */
	public void test12() {
		CodeExpressionRef exp = createExpression(new BeanPart(decl1), false, true, true, getPriority(3,true));
		try {
			method.updateExpressionOrder();
		} catch (CodeGenException e) {
			e.printStackTrace();
		}
	    assertEquals("Failed to add low priority expression to bp1",exp,getExp(8)) ;		
		}				
	
	/**
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		buildMethod();
	}

	/**
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();		
	}	
}
