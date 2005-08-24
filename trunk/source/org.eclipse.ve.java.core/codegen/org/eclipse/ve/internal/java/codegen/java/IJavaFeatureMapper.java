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
 *  $RCSfile: IJavaFeatureMapper.java,v $
 *  $Revision: 1.19 $  $Date: 2005-08-24 23:30:45 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

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

public static class VEexpressionPriority {	
    int 			priority;  // Expression "feature" level priority ordering withing a bean
    VEpriorityIndex index;

	public static class VEpriorityIndex {
		EStructuralFeature  sf;
		Integer			    index;
		EObject 			parent;
		
		public VEpriorityIndex (EStructuralFeature sf, int idx, EObject parent) {
			this.sf = sf;
			this.index = new Integer(idx);
			this.parent = parent;
		}
		
		public Integer getIndex() {
			return index;
		}		
		public EObject getParent() {
			return parent;
		}		
		public EStructuralFeature getSf() {
			return sf;
		}
		public String toString() {
			StringBuffer st = new StringBuffer();
			st.append("("); //$NON-NLS-1$
			st.append(index);
			st.append(", "); //$NON-NLS-1$			
			st.append(sf.getName());
			st.append(" feature on parent: ");
			if (parent instanceof IJavaObjectInstance)
			     st.append(((IJavaObjectInstance)parent).getAllocation().toString());
			else
				st.append(parent.toString());
			st.append(")"); //$NON-NLS-1$
			return st.toString();
		}
	}
    
    public VEexpressionPriority (int p, EStructuralFeature sf, int idx, EObject parent) {
    	priority = p;
    	if (sf != null && parent!= null)
    	   index = new VEpriorityIndex(sf, idx, parent);
    	else
    	   index = null;
    }
    public VEexpressionPriority (int p, VEpriorityIndex  pIndex) {
    	priority = p;
    	index=pIndex;    	
    }
	public int getProiority() {  		
		return priority;
	}
	public boolean isIndexed() {
		return index!=null;
	}
	public VEpriorityIndex getProiorityIndex() {  	
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
				st.append(index.toString()); //$NON-NLS-1$
				st.append("]"); //$NON-NLS-1$
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
		if (sameIndexFeature(p)) { // same SF
			  if (getProiorityIndex().getIndex().intValue()>p.getProiorityIndex().getIndex().intValue())
				  return -1;
			  else if (getProiorityIndex().getIndex().intValue()<p.getProiorityIndex().getIndex().intValue())
				       return 1;
			       else
				       return 0;
	      }
	      else
			  return 0;  // index is not on the same SF
	}
	
	public boolean sameIndexFeature(VEexpressionPriority p) {
		boolean result = false;
		if (getProiorityIndex()!=null && p.getProiorityIndex()!=null) {
			result = (getProiorityIndex().getSf()==p.getProiorityIndex().getSf()) &&
			         (getProiorityIndex().getParent() == p.getProiorityIndex().getParent());
		}
		return result;
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
