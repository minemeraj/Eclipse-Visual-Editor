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
package org.eclipse.ve.internal.java.codegen.java;
/*
 *  $RCSfile: IJavaFeatureMapper.java,v $
 *  $Revision: 1.16 $  $Date: 2005-07-07 19:30:33 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

/**
 *  An IFeatureMapper is a IExpressionDecoder helper which comes
 *  to provide java method to PropertyDecorator/SF mapping (and vice versa)
 */
public interface IJavaFeatureMapper  {
	
	public static String  CONSTRAINT_FEATURE_NAME = "constraints" ; //$NON-NLS-1$
	public static String  COMPONENT_FEATURE_NAME = "components" ; //$NON-NLS-1$
	
//  Given an expression, what is the SF.  The SF will be
//  calculated once, and cached from that point.	
public EStructuralFeature	getFeature(Statement expr) ;
public void  setFeature(EStructuralFeature sf) ;
public String getFeatureName() ;
public void setRefObject(IJavaInstance obj) ;
public IJavaInstance getRefObject() ;
public String getMethodName() ;
public String getIndexMethodName() ;
public String getReadMethodName() ;
public PropertyDecorator getDecorator() ;
public boolean isFieldFeature() ;

// Expresson priority will determine where new expressions will 
// be inserted in the code

public class VEexpressionPriority {	
    int priority;  // Expression "feature" level priority ordering withing a bean
	Object[] index;  // First element is an Integer for index value, second is the SF to index on
	                 // This will determine Z ordering priority between beans
	                 // lower index comes first.
    
    public VEexpressionPriority (int p, Object [] i) {
    	priority = p;
    	index=i;    	
    }
	public int getProiority() {  		
		return priority;
	}
	public boolean isIndexed() {
		return index!=null;
	}
	public Object[] getProiorityIndex() {  	
		return index;
	}
	public String toString() {
		StringBuffer st = new StringBuffer();
		if (priority<0) 
			st.append("[NO Priority]"); //$NON-NLS-1$
		else{
			st.append("["); //$NON-NLS-1$
			st.append(priority);
			st.append(":"); //$NON-NLS-1$
			if (index!=null) {
				st.append("("); //$NON-NLS-1$
				st.append(index[0]);
				st.append(","); //$NON-NLS-1$
				if (index[1] instanceof EStructuralFeature)
				    st.append(((EStructuralFeature)index[1]).getName());
				else
					st.append("???"); //$NON-NLS-1$
				st.append(")]"); //$NON-NLS-1$
			}
			else {
				st.append("NoIndex]"); //$NON-NLS-1$
			}
		}
        return st.toString();
	}
	/**
	 *  @return 0 if equal, 
	 *          1 if this comes before p, 
	 *          -1 if this comes after p
	 **/
	public int comparePriority (VEexpressionPriority p) {		   
		   if (getProiority() == p.getProiority()) {
			   if (isIndexed() && p.isIndexed())  {
			      return compareIndex(p);
			   }
			   else
				   return 0;  // Both are not indexed;
		   }
		   else 
			   if (getProiority()>p.getProiority())
		           return 1;
		        else
		           return -1;
	}
	/**
	 * Assuming both priorities has an index
	 * @param p
	 * @return  0 if equal or not compared, 1 if this comes before p, -1 if this comes after p
	 * 
	 * @since 1.1.0
	 */
	public int compareIndex(VEexpressionPriority p) {
		if (getProiorityIndex()[1]==p.getProiorityIndex()[1]) { // same SF
			  if (((Integer)(getProiorityIndex()[0])).intValue()>((Integer)(p.getProiorityIndex()[0])).intValue())
				  return -1;
			  else if (((Integer)(getProiorityIndex()[0])).intValue()<((Integer)(p.getProiorityIndex()[0])).intValue())
				       return 1;
			       else
				       return 0;
	      }
	      else
			  return 0;  // index is not on the same SF
	}
	
}

/**
 * 
 * @param methodName  feature's method name
 * @return feature level priority
 * The VEexpresssionPriority will determine where an expression be inserted
 * in the code.
 * The feature level priority comes to force ordering between features.
 * e.g., setLayout()  will come before add(component)
 */
public int getFeaturePriority(String methodName);

// Higher Values will come first in the code ... 
// priority will be determined by FeatureMappers using
// getFeaturePriority()
// The following are generic, preCanned priorities
public static final int PRIORITY_DEFAULT =				10000;
//Add to a container should be at the end all default expression settings 
public static final int PRIORITY_ADD = 					PRIORITY_DEFAULT - 5000; 
//Event expressions in source 
public static final int PRIORITY_EVENT = 					PRIORITY_DEFAULT - 7000; 
// Constructor Expression, all at the top
public static final int PRIORITY_CONSTRUCTOR = 			PRIORITY_DEFAULT;


public static final VEexpressionPriority NOPriority = new VEexpressionPriority(-1,null);
public static final VEexpressionPriority DEFAULTPriority = new VEexpressionPriority(PRIORITY_DEFAULT,null);

}
