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

import org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper;
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
	BeanPart bp1 = new BeanPart ("DummyBean 1","noType");
	BeanPart bp2 = new BeanPart ("DummyBean 2","noType");
	
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
		   return pri.toString() + offset+"*"+ filler +"*";
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

	/**     b1      b2      b1     b2      b1       b2      b1      b2     b1       b2
     *  [[9, 3], [9, 2], [9, 1], [8, 3], [8, 2], [8, 1], [7, 3], [7, 2], [7, 1], [6, 3]]
     **/		
	protected void buildMethod() {
	
	    method = new CodeMethodRef(tr, "dummyMethod") ;
	   // Add dummy expressions... high to low priorities
	   int pri= 10, index=3;
	   BeanPart bp ;
	   
	   for (int i=0; i< 10; i++) {
	       if (i%3==0) {
	          pri--;
	          index=3;
	       }
	       
	       if (i%2==0)
	          bp = bp1;
	       else
	         bp = bp2 ;
	       
	       VEexpressionPriority p = new VEexpressionPriority(pri,index--) ;
	       DummyExpression exp = new DummyExpression(bp);
	       exp.setState(CodeExpressionRef.STATE_SRC_LOC_FIXED, true);
	       exp.setOffset(10*i);
	       exp.setPriority(p);	       
	   }	   
	}
	
	protected CodeExpressionRef getExp (int index) {	   
	    for (Iterator iter = method.getExpressions(); iter.hasNext(); index--) {
			CodeExpressionRef element = (CodeExpressionRef) iter.next();
			if (index==0) return element;			
		}
		return null;
	}
	
	protected CodeExpressionRef create (BeanPart bp, int pri, int index) {
	   	VEexpressionPriority p = new VEexpressionPriority(pri, index);
		DummyExpression exp = new DummyExpression(bp);
		exp.setPriority(p);		
		return exp;
	}
	
	/**
     *  Add element with low priority to the end of the expressions list
     **/
	public void test1() {
	
		CodeExpressionRef exp = create(bp1, 3, 0);
		try {
			method.updateExpressionOrder();
		} catch (CodeGenException e) {
			e.printStackTrace();
		}
	    assertEquals("Failed to add low priority expression",exp,getExp(10)) ;
	}
	
	/**
     *  Add element with NO Priority at the end
     **/
	public void test2() {
	
		VEexpressionPriority p = IJavaFeatureMapper.NOPriority;
		DummyExpression exp = new DummyExpression(bp1);
		exp.setPriority(p);			
		try {
			method.updateExpressionOrder();
		} catch (CodeGenException e) {
			e.printStackTrace();
		}
	    assertEquals("Failed to add low priority expression",exp,getExp(10)) ;
	}
	
	/**
     *  Add element with High priority
     **/
	public void test3() {
	
		CodeExpressionRef exp = create(bp1, 20, 0);
		try {
			method.updateExpressionOrder();
		} catch (CodeGenException e) {
			e.printStackTrace();
		}
	    assertEquals("Failed to add high priority expression",exp,getExp(0)) ;
	}
	

	public void test4() {
	   //    b1      b2      b1     b2      b1       b2      b1      b2     b1             b2        
	   // [[9, 3], [9, 2], [9, 1], [8, 3], [8, 2], [8, 1], [7, 3], [7, 2], [7, 1], <***> [6, 3]]
	   //    0        1       2       3      4       5        6       7        8            9
		CodeExpressionRef exp = create(bp1, 7, 1);
		try {
			method.updateExpressionOrder();
		} catch (CodeGenException e) {
			e.printStackTrace();
		}
	    assertEquals(exp,getExp(9)) ;
	}	


	/**
     *  
     **/
	public void test5() {
	   //    b1      b2      b1     b2      b1       b2      b1      b2      b1            b2        
	   // [[9, 3], [9, 2], [9, 1], [8, 3], [8, 2], [8, 1], [7, 3], [7, 2], [7, 1], <***> [6, 3]]
	   //    0        1       2       3      4       5        6       7      8              9
		CodeExpressionRef exp = create(bp2, 7, 1);
		try {
			method.updateExpressionOrder();
		} catch (CodeGenException e) {
			e.printStackTrace();
		}
	    assertEquals(exp,getExp(9)) ;
	}	

	/**
     *  
     **/
	public void test6() {
	   //    b1      b2      b1     b2      b1       b2      b1      b2       b1           b2        
	   // [[9, 3], [9, 2], [9, 1], [8, 3], [8, 2], [8, 1], [7, 3], [7, 2],  [7, 1], <**> [6, 3]]
	   //    0        1       2       3      4       5        6       7        8            9
		CodeExpressionRef exp = create(new BeanPart ("DummyBean 3","noType"), 7, 1);
		try {
			method.updateExpressionOrder();
		} catch (CodeGenException e) {
			e.printStackTrace();
		}
	    assertEquals(exp,getExp(9)) ;
	}	

	/**
     *  
     **/
	public void test7() {
	   //          b1      b2      b1     b2      b1       b2      b1      b2       b1           b2        
	   // [<***> [9, 3], [9, 2], [9, 1], [8, 3], [8, 2], [8, 1], [7, 3], [7, 2],  [7, 1], [6, 3]]
	   //          0        1       2       3      4       5        6       7        8            9
		CodeExpressionRef exp = create(bp1, 9, 5);
		try {
			method.updateExpressionOrder();
		} catch (CodeGenException e) {
			e.printStackTrace();
		}
	    assertEquals(exp,getExp(0)) ;
	}			
		
	public void test8() {
	   //   b1      b2              b1     b2      b1       b2      b1      b2       b1           b2        
	   // [9, 3], [9, 2], <***> [9, 1], [8, 3], [8, 2], [8, 1], [7, 3], [7, 2],  [7, 1], [6, 3]]
	   //   0       1              2       3      4       5        6       7        8            9
		CodeExpressionRef exp = create(bp1, 9, 2);
		try {
			method.updateExpressionOrder();
		} catch (CodeGenException e) {
			e.printStackTrace();
		}
	    assertEquals(exp,getExp(2)) ;
	}			
	
	
	public void test9() {
	   //   b1       b2      b1     b2      b1       b2               b1      b2       b1      b2        
	   // [9, 3],  [9, 2], [9, 1], [8, 3], [8, 2], [8, 1], <***>   [7, 3], [7, 2],  [7, 1], [6, 3]]
	   //   0        1      2       3      4       5                  6       7        8         9
		CodeExpressionRef exp = create(bp1, 7, 4);
		try {
			method.updateExpressionOrder();
		} catch (CodeGenException e) {
			e.printStackTrace();
		}
	    assertEquals(exp,getExp(6)) ;
	}
	
	public void test10() {
		   //   b1       b2      b1     b2      b1       b2       b1      b2       b1      b2        
		   // [9, 3],  [9, 2], [9, 1], [8, 3], [8, 2], [8, 1],  [7, 3], [7, 2],  [7, 1], [6, 3] <***>]
		   //   0        1      2       3      4       5          6       7        8         9
			CodeExpressionRef exp = create(bp1, 6, 3);
			try {
				method.updateExpressionOrder();
			} catch (CodeGenException e) {
				e.printStackTrace();
			}
		    assertEquals(exp,getExp(10)) ;
		}
	
	
	public void test11() {
		   //   b1       b2      b1     b2      b1       b2       b1      b2       b1     b1     b2     b2        
		   // [9, 3],  [9, 2], [9, 1], [8, 3], [8, 2], [8, 1],  [7, 3], [7, 2],  [7, 1],<1111> <222> [6, 3]]
		   //   0        1      2       3      4       5          6       7        8       9     10
		   //																				  <333>
			CodeExpressionRef exp = create(bp1, 7, 1);
			try {
				method.updateExpressionOrder();
			} catch (CodeGenException e) {
				e.printStackTrace();
			}
		    assertEquals(exp,getExp(9)) ;
		    
			exp = create(bp2, 7, 1);
			try {
				method.updateExpressionOrder();
			} catch (CodeGenException e) {
				e.printStackTrace();
			}
		    assertEquals(exp,getExp(10)) ;
		    
			exp = create(bp1, 7, 1);
			try {
				method.updateExpressionOrder();
			} catch (CodeGenException e) {
				e.printStackTrace();
			}
		    assertEquals(exp,getExp(10)) ;


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
