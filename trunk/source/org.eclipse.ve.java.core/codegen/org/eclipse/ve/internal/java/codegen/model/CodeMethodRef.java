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
package org.eclipse.ve.internal.java.codegen.model;
/*
 *  $RCSfile: CodeMethodRef.java,v $
 *  $Revision: 1.45 $  $Date: 2005-08-09 22:55:24 $ 
 */

import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.jcm.JCMFactory;
import org.eclipse.ve.internal.jcm.JCMMethod;

import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper.VEexpressionPriority;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.vce.rules.VCEPostSetCommand;


public class CodeMethodRef extends AbstractCodeRef {


protected   MethodDeclaration		fdeclMethod = null ;
protected   CodeTypeRef				fTypeRef = null ;
protected   ArrayList				fExpressions = new ArrayList() ;
protected   ArrayList				fEventExpressions = new ArrayList() ;
protected	String					fMethodHandle = null ;  // Unique handle in a class
protected   String					fMethodName ;
protected   Object					fSync ;
protected   IBeanDeclModel			fModel = null ;
protected   JCMMethod				fcompMethod = null ;
protected	boolean					fgenerationRequired = false ;
protected	boolean					fStaleOffset = false ;
private String[] 						fArgumentNames = null;


    public CodeMethodRef (MethodDeclaration method,CodeTypeRef tRef, String methodHandle,ISourceRange range, String content) {
	super(range.getOffset(),range.getLength(),content) ;
	fSync = this ;
	fTypeRef = tRef ;	      	
	setMethodHandle(methodHandle) ;
	setDeclMethod(method) ;
	tRef.addRefMethod(this)	 ;
}


public CodeMethodRef (CodeTypeRef tr,String mName) {
	super() ;	
	fSync = this ;
	fTypeRef = tr ;
	tr.addRefMethod(this) ;
	fMethodName=mName ;	
}

public CodeMethodRef (CodeTypeRef tr,String mName, JCMMethod cMethod) {
	this(tr,mName) ;	
	fcompMethod = cMethod ;
}

protected void setDeclMethod(MethodDeclaration method){
	fdeclMethod = method ;
	if(fdeclMethod!=null){// && fMethod==null){
		fMethodName = method.getName().getIdentifier();
		List parameters = method.parameters();
		fArgumentNames = null;
		if(parameters!=null){
			fArgumentNames = new String[parameters.size()];
			for (int argCount = 0; argCount < parameters.size(); argCount++) {
				SingleVariableDeclaration svd = (SingleVariableDeclaration) parameters.get(argCount);
				fArgumentNames[argCount] = svd.getName().getFullyQualifiedName();
			}
		}
	}
}

public String getMethodName() {
	return fMethodName ;
}

public MethodDeclaration getDeclMethod() {
	return fdeclMethod ;
}

public void setMethodHandle(String handle) {
	fMethodHandle = handle ;
	// The unique name of beans may have changed. 
	if (fModel!=null) { 
		Collection beans = fModel.getBeansInitilizedByMethod(this) ;
		for (Iterator iter = beans.iterator(); iter.hasNext();) {
			BeanPart bean = (BeanPart) iter.next();
			fModel.updateBeanNameChange(bean) ;
		}
	}
}
public String getMethodHandle() {
	return fMethodHandle ;
}

public int isEquivalent(AbstractCodeRef code){
	return -1;
}

/**
 *  Adds an expression to this method. Initially this process was 
 *  totally based off offsets of the expressions. Now, we are
 *  concerned about offsets *only* if the expression originated
 *  from the *original* source. Else they will be appended in the 
 *  end, and then the method will be 'refreshed' by the expression.
 */
public  void addExpressionRef(CodeExpressionRef ref) {
	int offset = ref.getOffset() ;
	if (fExpressions == null) 
		fExpressions = new ArrayList() ;
	if((ref.isStateSet(CodeExpressionRef.STATE_SRC_LOC_FIXED)) && //((ref.getState()&CodeExpressionRef.STATE_SRC_LOC_FIXED)==CodeExpressionRef.STATE_SRC_LOC_FIXED) && 
	   (offset>-1)){
		int index;
		for (index=0; index<fExpressions.size(); index++) 
			if (((CodeExpressionRef)fExpressions.get(index)).getOffset()>offset) break ;
	      if (index == fExpressions.size())
		   fExpressions.add(ref) ;
		else
		   fExpressions.add(index,ref) ;
	}else{
		fExpressions.add(ref);
	}
}

/**
 *  Adds Event expression to this method. Initially this process was 
 *  totally based off offsets of the expressions. Now, we are
 *  concerned about offsets *only* if the expression originated
 *  from the *original* source. Else they will be appended in the 
 *  end, and then the method will be 'refreshed' by the expression.
 */
public  void addEventExpressionRef(CodeEventRef ref) {
	int offset = ref.getOffset() ;
	if (fEventExpressions == null) 
		fEventExpressions = new ArrayList() ;
	if((ref.isStateSet(CodeExpressionRef.STATE_SRC_LOC_FIXED)) && //((ref.getState()&CodeExpressionRef.STATE_SRC_LOC_FIXED)==CodeExpressionRef.STATE_SRC_LOC_FIXED) && 
	   (offset>-1)){
		int index;
		for (index=0; index<fEventExpressions.size(); index++) 
			if (((CodeEventRef)fEventExpressions.get(index)).getOffset()>offset) break ;
	      if (index == fEventExpressions.size())
		   fEventExpressions.add(ref) ;
		else
		   fEventExpressions.add(index,ref) ;
	}else{
		fEventExpressions.add(ref);
	}
}

public  void removeExpressionRef(CodeExpressionRef ref) {
	if (ref instanceof CodeEventRef)
		removeEventRef((CodeEventRef) ref);
	else
		if (fExpressions!=null)
	       fExpressions.remove(ref) ;
}

public  void removeEventRef(CodeEventRef ref) {
	if (fEventExpressions!=null)
	  fEventExpressions.remove(ref) ;
}


/**
 * Get all expressions in this method that impact bean/s in the model
 */
public  Iterator getExpressions() {
	if (fExpressions == null) fExpressions = new ArrayList () ;
	return fExpressions.iterator() ;
}
/**
 * Get all Event expressions in this method that impact bean/s in the model
 */
public  Iterator getEventExpressions() {
	if (fEventExpressions == null) fEventExpressions = new ArrayList () ;
	return fEventExpressions.iterator() ;
}

public  Iterator getAllExpressions() {
	
	final Iterator exp = getExpressions() ;
	final Iterator events = getEventExpressions() ;	
	
	return new Iterator() {
		boolean eventsFlag = false ;
		
		public boolean hasNext() {
			return exp.hasNext() || events.hasNext() ;
		}

		public java.lang.Object next() {
			if (exp.hasNext()) 
			   return exp.next() ;
			else {
				eventsFlag = true ;
			   return events.next() ;
			}
		}

		public void remove() {
			if (eventsFlag) events.remove() ;
			else exp.remove() ;
		}
	};
 
  
 
}
/**
 * 
 * 
 * @todo Generated comment
 * @deprecated
 */
protected void resetExpressionPriorities(){
	// Reset priority of normal expressions
	for(int count=0;count<fExpressions.size();count++){
		CodeExpressionRef expression = (CodeExpressionRef)fExpressions.get(count);
		try{			
			if ((!expression.isStateSet(CodeExpressionRef.STATE_NO_MODEL)) &&  //((expression.getState()& expression.STATE_NO_OP) == 0) && 
				(expression.isAnyStateSet()) &&
				(!expression.isStateSet(CodeExpressionRef.STATE_DELETE))) { //expression.getState() != expression.STATE_NOT_EXISTANT) {
				 expression.setProprity(expression.primGetDecoder().determinePriority());
			}else{
				// TODO   Hard coding here is not scalable - we may have ivjFoo = getFrame().getContentPane() ; as init exp
				if(expression.isStateSet(CodeExpressionRef.STATE_INIT_EXPR)){ //$NON-NLS-1$
//					expression.setProprity(new int[]{IJavaFeatureMapper.PRIORITY_INIT_EXPR, 0});
				}else{
//					expression.setProprity(new int[]{IJavaFeatureMapper.PRIORITY_DEFAULT, 0});
				}
			}
		}catch(Exception e ){JavaVEPlugin.log(e, Level.WARNING) ;}
	}
	
	// Reset priority of event expressions
	for(int count=0;count<fEventExpressions.size();count++){
		CodeExpressionRef expression = (CodeExpressionRef)fEventExpressions.get(count);
		try{			
			if ((!expression.isStateSet(CodeExpressionRef.STATE_NO_MODEL)) &&  //((expression.getState()& expression.STATE_NO_OP) == 0) && 
				(expression.isAnyStateSet()) &&
				(!expression.isStateSet(CodeExpressionRef.STATE_DELETE))) { //expression.getState() != expression.STATE_NOT_EXISTANT) {
				 expression.setProprity(expression.primGetDecoder().determinePriority());
			}else{
				// TODO  hard coding here is not scalable - we may have ivjFoo = getFrame().getContentPane() ; as init exp.
				if(expression.isStateSet(CodeExpressionRef.STATE_INIT_EXPR)){ //$NON-NLS-1$
//					expression.setProprity(new int[]{IJavaFeatureMapper.PRIORITY_INIT_EXPR, 0});
				}else{
//					expression.setProprity(new int[]{IJavaFeatureMapper.PRIORITY_DEFAULT, 0});
				}
			}
		}catch(Exception e ){JavaVEPlugin.log(e, Level.WARNING) ;}
	}
}

/**
 * Called if this is the first expresson, and we need an offset from expression that is 
 * not in our model
 */
protected Object[] getUsableOffsetAndFiller() throws CodeGenException{
	int offset = -1;
	String filler = null;
	
	
	ArrayList list = new ArrayList();
	list.addAll(fExpressions);
	list.addAll(fEventExpressions);
	
	// find the last expression in our model
	for(int i=list.size()-1;i>=0;i--){
	 	CodeExpressionRef ce = (CodeExpressionRef) list.get(i);
	 	if(ce.isStateSet(CodeExpressionRef.STATE_SRC_LOC_FIXED) && ce.getOffset()>=0 && !ce.isStateSet(CodeExpressionRef.STATE_NO_SRC)){
	 		offset = ce.getOffset()+ce.getLen();
	 		filler = ce.getFillerContent();
	 		break;
	 	}
	 }
	 if(offset!=-1 && filler!=null)
		return new Object[]{new Integer(offset), filler};
			
	// No expression was found, use AST
	IType mainType = CodeGenUtil.getMainType(fModel.getCompilationUnit());
	IMethod fMethod = CodeGenUtil.getMethod(mainType, getMethodHandle());
	if(fMethod!=null){
		ICodeGenSourceRange sr = ExpressionRefFactory.getOffsetForFirstExpression(fMethod);
		if (sr == null) throw new CodeGenException ("Could not find Offset") ;     //$NON-NLS-1$
		offset=sr.getOffset() ;
		StringBuffer sb = new StringBuffer() ;
		for (int i = 0; i < sr.getLength(); i++) {
			sb.append(' ') ;
		}
		filler = sb.toString() ;   
	}
	return new Object[] {new Integer(offset), filler};
}


/**
 * 
 * @param l List of expressions
 * @param exp to add
 * @param index
 * 
 * Add the expression, and set up its offset/filler
 */
protected void addExpression (List l, CodeExpressionRef exp, int index) throws CodeGenException {
	int offset;
	String filler;
		
	int prevIndex = index-1;  // Previous valid expression	
	if (index<l.size()) {
		// find the first insertion target
		CodeExpressionRef targetExp = (CodeExpressionRef)l.get(index);	
		while (index<l.size() && targetExp.isStateSet(CodeExpressionRef.STATE_NO_SRC)){
			index++;
			if (index<l.size())
			   targetExp = (CodeExpressionRef)l.get(index);
		}
	}
	
	if (prevIndex>0) {	
		// find previous valid expression
		CodeExpressionRef targetExp = (CodeExpressionRef)l.get(prevIndex);
		while (prevIndex>=0 && targetExp.isStateSet(CodeExpressionRef.STATE_NO_SRC)) {
			prevIndex--;
			if (prevIndex>=0)
			   targetExp = (CodeExpressionRef)l.get(prevIndex);
		}
	}
					
	if (index>=l.size() && prevIndex<0) {
	  // No expression to piggy on from
	  Object[] result = getUsableOffsetAndFiller();	  
	  offset = ((Integer)result[0]).intValue();
	  filler = (String) result[1];
	}
	else {
		CodeExpressionRef cExp;
		if (prevIndex>=0) {
			// use the previou's filler.. we come right after
			cExp = (CodeExpressionRef) l.get(prevIndex);
			offset = cExp.getOffset()+cExp.getLen();
		}
		else {
			// use the one we come in front off
			cExp = (CodeExpressionRef) l.get(index);
			offset = cExp.getOffset();
		}
		if(cExp.isStateSet(CodeExpressionRef.STATE_SHARED_LINE)){
			filler = ((CodeExpressionRef) cExp.getSameLineExpressions().get(0)).getFillerContent();
			cExp.updateSharedLineExpressions(true);
			// offsets are update - refresh the offsets again
			if (prevIndex>=0) {
				// use the previou's filler.. we come right after
				offset = cExp.getOffset()+cExp.getLen();
			}
			else {
				// use the one we come in front off
				offset = cExp.getOffset();
			}
		}else
			filler=cExp.getFillerContent();
	}
	exp.setFillerContent(filler);
	exp.setOffset(offset);
	exp.setState(CodeExpressionRef.STATE_SRC_LOC_FIXED, true);
	l.add(index, exp);
}

/**
 * Will determine if the expression (expressions[index]) is in the bp's
 * grouping.  It is possible for an expression to be place out of grouping
 * because of dependencies, or z order.
 * 
 * @param bp Is the grouping for this bean
 * @param expressions Sorted method expression
 * @param index for the expression to determine if in bp's grouping
 * @return
 * 
 * @since 1.1.0
 */
protected  boolean isGrouping (BeanPart bp, List expressions, int index) {
	boolean result = false;
	for (int i=index; i>=0; i--) {	
	    CodeExpressionRef exp = (CodeExpressionRef) expressions.get(i);
		if (exp.getBean()!=bp)
			break; // Not in the grouping anymore
		if (exp.isStateSet(CodeExpressionRef.STATE_INIT_EXPR)) {
			result = true; 
			break;
		}
	}
	return result;
}

protected int getGroupLastIndex(List sortedList, BeanPart bp) {
	int idx=0;
	while (idx<sortedList.size()) {
		CodeExpressionRef exp = (CodeExpressionRef)sortedList.get(idx);
		if (exp.getBean()==bp && exp.isStateSet(CodeExpressionRef.STATE_INIT_EXPR)) {
			while (idx<sortedList.size()) {
				CodeExpressionRef exp2 = (CodeExpressionRef)sortedList.get(idx);
				if (exp2.getBean()!=bp)
					return idx-1;
				idx++;
			}
			return idx-1;
		}
		idx++;
	}
	return -1;
}


/**
 * 
 * This method will return true, if it is possible that exp, indirectly initializes
 * an Object that is listed in the references argument.
 * 
 * @param exp 
 * @param references
 * @return
 * 
 * @since 1.1.0
 */
 private boolean isRelated(CodeExpressionRef exp, List references) {
	 
 	EReference sf = (EReference)exp.getPriority().getProiorityIndex().getSf();
	if (VCEPostSetCommand.isChildRelationShip(sf)) {
		// cExp is a parent/child indexed expression
		// it can be that it is in the form of creatComposite(), in this case
		// it relates to the INIT expression of its argument
		// if exp is dependant on this object, we are dependent on cExp
		Object[] args = exp.getArgs();
		if (args!=null) {
			for (int i = 0; i < args.length; i++) {
				if (references.contains(args[i]))
					return true;								
			}
		}		
	}
	return false;
 }

/**
 * Sorting goes as following:
 * 
 *     Priority is of the form [w:(index,feature)]
 *     
 *     w is the expression weight (larger w comes first)
 *     index imply that the expression is bounded by a z order on a particular feature
 *     
 *     Expressions are grouped by beans, according to their weight. 
 *     Higher w goes first.
 *     
 *     New Bean (constructor) expression will be inserted at the top of the method, unless it is dependant
 *     on another beans.  This is a workaround for things like a GridBagConstraint, where 
 *     expressions are generated before the dependency of the GridBagConstrait is placed,
 *     and the dependency check does not work. ... additonal expressions on the same bean will be opted
 *     to be group together around the constructor.
 *     
 *     w is used to detemine where to place an expression within a grouping.
 *     
 *     if index is defined, order will be enforced across bean expression grouping.
 *     This may force an expression not to be grouped with its bean's expressions.
 *     Index will be enforced for same features only.
 *     
 *     
 * 
 * @param sortedList
 * @param exp
 * @throws CodeGenException
 * 
 * @since 1.1.0
 */
protected void addExpressionToSortedList(List sortedList, CodeExpressionRef exp) throws CodeGenException {
	// 
	// The sorted list may not be sorted the way we prioratize expressions, (it is may
	// be coming from the source code like this) .... so we have to go through ALL expressions
	// to find a window where we can add the expression.
	
	int index = -1;
	VEexpressionPriority expPriority = exp.getPriority();
	List dependantBeans = exp.getReferences();
	
	// find a target insersion window (first/last)
	// Start from the end, as expression may not be sorted
	// according to the priorities and dependencies.
	int first = -1, last = -1 ;
	
	if (expPriority.equals(IJavaFeatureMapper.NOPriority)) {
		// add to the end of the list
		index=sortedList.size();
	}
	else {
		boolean firstBean = true;  // should we insert this exp. at index 0		
				
		for (int i = sortedList.size()-1; i>=0 && first<0 ; i--) {
			
		   if (first>=0) break; 
			
		   CodeExpressionRef cExp = (CodeExpressionRef) sortedList.get(i);		   
		   if (cExp.isStateSet(CodeExpressionRef.STATE_NO_SRC)) 
			   continue;
		   
		   int compare;
		   // If We are dependant on the bean associated with this expression, or
		   // the current expression is the init expression of us
		   // We have to come after this one.
		   boolean sameBean = cExp.getBean()==exp.getBean();
		   firstBean &= !sameBean;
		   if (sameBean) {
		   	 if (exp.isStateSet(CodeExpressionRef.STATE_INIT_EXPR)) {
		   	 	    // new expression must come before this expression
		   	 		last = i;					
		   	 		continue;
		   	 }
		   	 else if (cExp.isStateSet(CodeExpressionRef.STATE_INIT_EXPR))
		   	 		compare = -1;  // new expression will come after the init expr
		   	 else if (cExp.getPriority().isIndexed()) {
				    if (expPriority.isIndexed()) {// drive index ordering in the group
						compare = expPriority.comparePriority(cExp.getPriority());
						if (compare==0) // no index dependencies
							if (isRelated(cExp, dependantBeans))
								compare = -1; // exp may be dependant on cExp
				    }
				    else {				    	
				        boolean grouping = isGrouping(exp.getBean(),sortedList, i);
				        boolean related = isRelated(cExp, dependantBeans);
				    	if (grouping || related) {
				    	  compare = 1;  //exp comes before this one
				    	  if (related)
				    		compare = -1;  // exp may be dependant on cExp, come after
				    	}
				    	else
				    		continue; // cExp is placed here because of index/dependency, not grouping
				    }
		   	      }
		   	 else {
		   	 	    // Check priorities within a BeanPart
		   	 		compare = expPriority.comparePriority(cExp.getPriority());
		   	 }
		   }
		   // Not the same bean
		   else {
			   boolean indexsSet = cExp.getPriority().isIndexed() && 
			   					   expPriority.isIndexed() &&
			   					   expPriority.sameIndexFeature(cExp.getPriority());
			   						
			   if ((indexsSet && // Z order comparison is needed					
				   cExp.getPriority().compareIndex(expPriority)>0) // Z order forces us to come after 
				   ||  
				   (dependantBeans.contains(cExp.getBean().getEObject()) && // we are dependant
				    isGrouping(cExp.getBean(), sortedList, i) //||
				     //!cExp.getPriority().isIndexed())  // It is part of the bean init or its group of expressions
				   )) { 
		   
			       // exp comes after cExp
				   // Skip all expression on this bean.
				   if (!dependantBeans.contains(cExp.getBean().getEObject()))
						   dependantBeans.add(cExp.getBean().getEObject());
				   int firstIndex = (i+1)>=sortedList.size()? sortedList.size() : i+1;
				   // skip all expressions for this bean
				   for (int j=i+1; j<sortedList.size(); j++) {
						CodeExpressionRef skip = (CodeExpressionRef) sortedList.get(j);
						if (skip.getBean()!=cExp.getBean())
							break; // different grouping
						firstIndex = (j+1)>=sortedList.size()? sortedList.size() : j+1;						
				   }
				   first = firstIndex;
				   continue;
			   }
			   else if (indexsSet && // Z order forced us to come before					
					   cExp.getPriority().compareIndex(expPriority)<0) {
				       last = i;					   
			        }
			   continue;
		   }
		   
		   switch (compare) {
		      case -1: // exp < cExp (comes after)
		      	       // This is the top most 
		               first = (i+1)>=sortedList.size()? sortedList.size() : i+1;
		               break;
		      case  0: // exp == cExp
		      		   if (last<0 || last>getGroupLastIndex(sortedList,exp.getBean())) { // first one
		      		   	 // bottom most
		      		     last = (i+1)>=sortedList.size()? sortedList.size() : i+1;
		      		   }
		      		   break;	
		      case 1: // exp > cExp (comes before)
				       last = i ;
					   break;
		   }
		}
		   	   
	   if (first>=0) {
	   	  index = last>=0 ? last : first;
	   }
	   else {
	   	  if (firstBean)
	   	  	index = 0; // New expression for a new Bean with no dependencies
	   	  else
  	   	    // This expression is likly with the highest priority
	   	    index = last>=0 ? last : 0 ;
	   	  
	   }	   
	}	
	addExpression (sortedList, exp, index) ;	
}
protected ArrayList sortExpressions(List list) throws CodeGenException {
	
	ArrayList  sortedList = new ArrayList();
	ArrayList  needSorting = new ArrayList();
	ArrayList  notReadyForSorting = new ArrayList(); 
	
	
	for (int i=0; i<list.size(); i++) {
		CodeExpressionRef exp = (CodeExpressionRef) list.get(i);
		if (!exp.isStateSet(CodeExpressionRef.STATE_IN_SYNC)) {
			//  expression generation may induce the generation of another one
			//  e.g., a lazy creation of a BeanPart... so this one is not ready to 
			//  be sorted yet.
			notReadyForSorting.add(exp);
		}
		else if (!exp.isStateSet(CodeExpressionRef.STATE_SRC_LOC_FIXED))
			needSorting.add(exp);
		else {
			// Those that are already set in code, will not change
			sortedList.add(exp);
			// sanity check
			if (exp.getOffset()<0) throw new CodeGenException("Invalid Offset: "+exp);//$NON-NLS-1$
		}
	}
	// TODO
	// The sorting will set offsets for the new exp... at this time, after the sort, a new expression will be
	// inserted into the document... updating everyone's else offset.
	// this insert is not done here at this point... so we can not update the 2nd expression's offset
	if (needSorting.size()>1)
		  throw new CodeGenException ("Limitation ... only one new expression is allowed"); //$NON-NLS-1$
	
	for (int i=0; i<needSorting.size(); i++) {
		addExpressionToSortedList(sortedList, (CodeExpressionRef)needSorting.get(i));
	}		
	sortedList.addAll(notReadyForSorting);
	return sortedList;	
}

public  void updateExpressionOrder() throws CodeGenException {
	
	ArrayList sortedList = sortExpressions(fExpressions);
	fExpressions = sortedList;
	
	// sorting of events has to take into consideration the sorted
	// order of regular expressions as well. 
	if(fEventExpressions!=null && fEventExpressions.size()>0){
		List allExpressions = new ArrayList(fExpressions);
		allExpressions.addAll(fEventExpressions);
		sortedList = sortExpressions(allExpressions); // sort all expressions in the method
		sortedList.removeAll(fExpressions); // remove the regular expressions as they were added only for reference
		fEventExpressions = sortedList; // now we have the sorted events only list
	}
}

/**
 * 
 * @param beans
 * @return
 * @todo Generated comment
 * @deprecated
 */
protected static Enumeration determineBeanOrder(Enumeration beans){
	SortedSet beanSorter = new TreeSet(getDefaultBeanOrderComparator());
	while(beans.hasMoreElements()){
		boolean added = beanSorter.add(beans.nextElement());
		if(!added)
			JavaVEPlugin.log("No addition to the treeset", Level.FINE); //$NON-NLS-1$
	};
	Vector order = new Vector();
	Iterator sorted = beanSorter.iterator();
	while(sorted.hasNext())
		order.add(sorted.next());
	return order.elements();
}

/**
 *  @return a negative integer, zero, or a positive integer as the
 * 	       first argument is less than, equal to, or greater than the
 *	       second.
 *  warning: Never return zero, as values are overwritten if equal.
 */
protected static Comparator getDefaultBeanOrderComparator(){
	return new Comparator(){
		public int compare(Object o1, Object o2){
			BeanPart bp1 = (BeanPart) o1;
			BeanPart bp2 = (BeanPart) o2;
			int np1 = getImportanceCount(bp1, bp2);
			int np2 = getImportanceCount(bp2, bp1);
			if(np1 < np2){
				return 1; // Return greater, becuase Sorted set returns in ascending
			}else{
				if(np1 == np2){
					return -1; // Return less, becuase Sorted set returns in ascending
				}else{
					return -1; // Return less, becuase Sorted set returns in ascending
				}
			}
		}
		
		protected boolean isReferenced (BeanPart parent, BeanPart reference) {
			EStructuralFeature sfs[] = InverseMaintenanceAdapter.getReferencesFrom(parent.getEObject(), reference.getEObject());
			if (sfs!=null && sfs.length>0)
				return true ;
			else
				return false;
		}
		
		protected int getImportanceCount(BeanPart main, BeanPart subMain){
			
			if(isAChildOf(main, subMain))				
				return Integer.MAX_VALUE;
			else if (isReferenced(main, subMain)) // if main ref. subMain, subMain must come first
				return Integer.MIN_VALUE;
			int mV = main.getDecleration().isInstanceVar()? 1 : 0;
			int sV = subMain.getDecleration().isInstanceVar() ? 1 : 0 ;
			if (mV>sV)  
				return Integer.MIN_VALUE;
			else if (mV<sV)
				return Integer.MAX_VALUE;
			int parentCount = getParentCount(main);
			int hasChildren = getConstraintCount(main);
			return - hasChildren - parentCount;
		}
		
		
		protected boolean isAChildOf(BeanPart parent, BeanPart reference){
			Iterator children = parent.getChildren();
			while(children.hasNext()){
				BeanPart child = (BeanPart) children.next();
				if(child.getSimpleName().equals(reference.getSimpleName()))
					return true;
				if(isAChildOf(child, reference))
					return true;
			}
			return false;
		}
		
		protected int getConstraintCount(BeanPart b){
			EStructuralFeature sf = b.getEObject().eClass().getEStructuralFeature("components"); //$NON-NLS-1$
			if(sf!=null)
				return 1;
			if(CodeGenUtil.getConstraintFeature(b.getEObject())!=null)
				return 1;
			return 0;
		}
		
		protected int getParentCount(BeanPart b){
			int pc = 0;
			int safetyCount = 1000;
			BeanPart parent = b;
			while(parent!=null && safetyCount>0){
				parent = CodeGenUtil.determineParentBeanpart(parent);
				pc++;
				safetyCount--;
			}
			return pc;
		}
	};
}

/**
 * This method will remove itself and of of its expressions from the model.
 * It will not remove BeanParts that it initializes
 */
public  void dispose() {
	
	if (fModel != null) {
		Iterator itr = fModel.getBeansInitilizedByMethod(this).iterator();
		while (itr.hasNext()) {
			BeanPart b = (BeanPart) itr.next();
			b.removeInitMethod(this);
			b.removeReturnMethod(this);
		}
	}
	
	if (fExpressions != null) {
	 // fExpression will be updated by the call to Exp.dispose() ;
	 Object[] tmp = fExpressions.toArray() ;
	 for (int i=0; i<tmp.length; i++) {
		((CodeExpressionRef)tmp[i]).dispose() ;
	 }
	}
	fExpressions = null ;
	
	if (fEventExpressions != null) {
	 // fExpression will be updated by the call to Exp.dispose() ;
	 Object[] tmp = fEventExpressions.toArray() ;
	 for (int i=0; i<tmp.length; i++) {
		((CodeEventRef)tmp[i]).dispose() ;
	 }
	}
	fExpressions = null ;
	
	if (fTypeRef != null)
	  fTypeRef.removeRefMethod(this) ;
	  	
	if (fModel != null)  
	   fModel.removeMethodRef(this) ;
	
	fdeclMethod = null ;
	if (fcompMethod != null)
	  fTypeRef.getBeanComposition().getMethods().remove(fcompMethod) ;
	  
	fTypeRef = null ;
	fcompMethod = null ;
}

public ICodeGenSourceRange getTargetSourceRange() {
    if (fModel == null) return null ;
    ISourceRange sr = fModel.getWorkingCopyProvider().getSourceRange(getMethodHandle()) ;
    CodeGenSourceRange result = new CodeGenSourceRange(sr) ;
    if (sr != null)
        result.setLineOffset(fModel.getWorkingCopyProvider().getLineNo(result.getOffset())) ;
    return result ;
}

public ICodeGenSourceRange getHighlightSourceRange() {
    if (fModel == null || fModel.getWorkingCopyProvider() == null)
			return null;
		try {
			IMethod mtd = (IMethod) fModel.getWorkingCopyProvider().getElement(getMethodHandle());
			if (mtd != null) {
				int start = mtd.getNameRange().getOffset();
				int end = mtd.getSourceRange().getOffset() + mtd.getSourceRange().getLength();
				start = fModel.getCompilationUnit().getSource().lastIndexOf('\n', start) + 1;
				CodeGenSourceRange result = new CodeGenSourceRange(start, end - start);
				result.setLineOffset(fModel.getWorkingCopyProvider().getLineNo(result.getOffset()));
				return result;
			}
		} catch (JavaModelException e) {}
		return null;
}

public void setModel(IBeanDeclModel model) {
    fModel = model ;
}

public void refreshIMethod(){
	try{
	    try {
	      if (!fModel.getCompilationUnit().isConsistent()) fModel.getCompilationUnit().reconcile(ICompilationUnit.NO_AST, false, null, new NullProgressMonitor()) ;
	    }
	    catch (JavaModelException e) {}
		IType mainType = CodeGenUtil.getMainType(fModel.getCompilationUnit());
		IMethod m = CodeGenUtil.getMethod(mainType, getMethodHandle());
		refreshIMethod(m);
	}catch(Exception e){
		JavaVEPlugin.log(e, Level.WARNING);
	}
}

public void refreshIMethod(IMethod m) {
	try {
		if(m!=null){
			setOffset(m.getSourceRange().getOffset()) ;
//			setContent(m.getSource());
		}
	} catch (JavaModelException e) {
		JavaVEPlugin.log(e, Level.WARNING);
	}			
}


public String _debugExpressions() {
    
    StringBuffer sb = new StringBuffer() ;
    int mOffset = getOffset() ;
    String   doc = fModel.getDocumentBuffer().getContents() ;
    Iterator itr = getExpressions() ;
	try {
      while (itr.hasNext()) {
        CodeExpressionRef exp = (CodeExpressionRef)itr.next() ;
        sb.append(exp.toString()+"\n\"") ; //$NON-NLS-1$
        if (!exp.isStateSet(CodeExpressionRef.STATE_DELETE) && !exp.isStateSet(CodeEventRef.STATE_NO_SRC))
            sb.append(doc.substring(mOffset+exp.getOffset(),mOffset+exp.getOffset()+exp.getLen())) ;
        else
            sb.append("STATE_NOT_EXISTANT") ; //$NON-NLS-1$
        sb.append("\"\n") ; //$NON-NLS-1$
      }
	}
	catch (Exception e) {
		sb.append("**Error***\n"); //$NON-NLS-1$
	}
    return sb.toString() ;    
}

/**
 * Returns the compMethod.
 * @return JCMMethod
 */
public JCMMethod getCompMethod() {
	if (fcompMethod == null) {
	  fcompMethod = JCMFactory.eINSTANCE.createJCMMethod() ;	  	  	
	  fcompMethod.setName(getMethodName()) ;
	  // Start listining to changes in its membership
	  MemberDecoderAdapter a = new MemberDecoderAdapter(fModel) ;
	  a.setMethodRef(this) ;
	  a.setTarget(fcompMethod) ;
      fcompMethod.eAdapters().add(a) ;
      fTypeRef.getBeanComposition().getMethods().add(fcompMethod) ;
	}
	return fcompMethod;
}

/**
 * If we are coming up from a cache, we need to restore the Member adapter
 * 
 * @since 1.0.0
 */
public void restore() {
	if (fcompMethod==null) {
		EList methods = fTypeRef.getBeanComposition().getMethods();
		for (int i = 0; i < methods.size(); i++) {
			JCMMethod method = (JCMMethod)methods.get(i);
			if (method.getName().equals(getMethodName())) {
				fcompMethod = method;
				MemberDecoderAdapter a = new MemberDecoderAdapter(fModel) ;
				a.setMethodRef(this) ;
				a.setTarget(fcompMethod) ;
			    fcompMethod.eAdapters().add(a) ;
				return;
			}
		}
		JavaVEPlugin.log("should not be here",Level.SEVERE); //$NON-NLS-1$
	}
	
}

public void setCompMethod(JCMMethod m)  throws CodeGenException {
	if (fcompMethod != null) throw new CodeGenException ("Already initialized") ; //$NON-NLS-1$
	fcompMethod = m ;
	MemberDecoderAdapter ma = (MemberDecoderAdapter) EcoreUtil.getExistingAdapter(m,ICodeGenAdapter.JVE_MEMBER_ADAPTER) ;
	ma.setMethodRef(this) ;
	m.setName(getMethodName()) ;
}


/**
 * Returns the enerationRequired.
 * @return boolean
 */
public boolean isGenerationRequired() {
	return fgenerationRequired;
}

/**
 * Sets the enerationRequired.
 * @param enerationRequired The enerationRequired to set
 */
public void setGenerationRequired(boolean enerationRequired) {
	fgenerationRequired = enerationRequired;
}

/**
 * Returns the typeRef.
 * @return CodeTypeRef
 */
public CodeTypeRef getTypeRef() {
	return fTypeRef;
}

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
public String toString() {
	return fStaleOffset ? "***StaleOffset***\n" + super.toString() : super.toString(); //$NON-NLS-1$
}

/* (non-Javadoc)
 * @see org.eclipse.ve.internal.java.codegen.model.AbstractCodeRef#setOffset(int)
 */
public void setOffset(int off) {
	super.setOffset(off);
	fStaleOffset = false;
}

public void setContent(String content) {
	super.setContent(content) ;	
	fStaleOffset = false ;
}
/**
 * 
 * @param offset
 * @param delta
 */
public void updateExpressionsOffset(int offset, int delta) {
	Iterator itr = getAllExpressions();
	while (itr.hasNext()) {
		CodeExpressionRef exp = (CodeExpressionRef) itr.next();
		// If this expression has updated the document, skip it
		if (exp.isStateSet(CodeExpressionRef.STATE_UPDATING_SOURCE) || exp.isStateSet(CodeExpressionRef.STATE_DELETE))
			continue;
		if (exp.getOffset() + getOffset() >= offset)
			exp.setOffset(exp.getOffset() + delta);
	}
}

protected void refreshExpressionOrder(List currentList, CodeExpressionRef exp) throws CodeGenException {
	currentList.remove(exp);
	addExpressionToSortedList(currentList, exp);
	
}

public void updateExpressionIndex(CodeExpressionRef exp) throws CodeGenException {
	
	if (exp.isStateSet(CodeExpressionRef.STATE_NO_SRC) || 
		exp.isStateSet(CodeExpressionRef.STATE_DELETE)) return ;
	
	if (JavaVEPlugin.isLoggingLevel(Level.FINE))
		JavaVEPlugin.log("CodeExpressionRef: moving:\n"+getContent()+"\n", Level.FINE) ; //$NON-NLS-1$ //$NON-NLS-2$
	try {
		// mark a controlled update (Top-Down)		
		exp.setState(CodeExpressionRef.STATE_UPDATING_SOURCE, true);		
		String txt = exp.getContent();
		int docOff = exp.getOffset()+getOffset() ;
		if (exp.isStateSet(CodeExpressionRef.STATE_SRC_LOC_FIXED)) {
			// Have to remove this expression from the source first
			exp.updateDocument(docOff, txt.length(),""); //$NON-NLS-1$
		}		
		
		if (fExpressions.indexOf(exp) >= 0) 
			refreshExpressionOrder(fExpressions, exp);
		else
			if (fEventExpressions.indexOf(exp)>0)
				refreshExpressionOrder(fEventExpressions, exp);
		
		txt = exp.getContent();
		docOff = exp.getOffset()+getOffset() ;
		exp.updateDocument(docOff, 0, txt) ;
		
	}
	finally {
		exp.setState(CodeExpressionRef.STATE_UPDATING_SOURCE, false);
	}

}

/**
 * Refreshes the contents of this MethodRef with the passed in one. 
 * This includes AST node, offset and content update. This method
 * does no checking whatsoever, so the appropriate method should 
 * be passed in. Generally called from snippet update mechanism.
 */
public boolean refresh(CodeMethodRef cmr) {
	if(cmr==null)
		return false;
	if(getOffset() != cmr.getOffset())
		setOffset(cmr.getOffset());
	if(getContent()!=null && !getContent().equals(cmr.getContent()))
		setContent(cmr.getContent());
	if(cmr.getDeclMethod()!=null)
		setDeclMethod(cmr.getDeclMethod());
	return true;
}

public String[] getArgumentNames(){
	return fArgumentNames;
}
}
