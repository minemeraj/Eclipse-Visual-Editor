package org.eclipse.ve.internal.java.codegen.model;
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
 *  $RCSfile: CodeMethodRef.java,v $
 *  $Revision: 1.5 $  $Date: 2004-02-04 15:47:50 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;

import org.eclipse.jem.internal.core.MsgLogger;

import org.eclipse.ve.internal.jcm.JCMFactory;
import org.eclipse.ve.internal.jcm.JCMMethod;

import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;


public class CodeMethodRef extends AbstractCodeRef {


protected   AbstractMethodDeclaration   fdeclMethod = null ;
protected   CodeTypeRef	             fTypeRef = null ;
protected   ArrayList                   fExpressions = new ArrayList() ;
protected   ArrayList					 fEventExpressions = new ArrayList() ;
protected	 String                      fMethodHandle = null ;  // Unique handle in a class
protected   String                      fMethodName ;
protected   Object                      fSync ;
protected   IBeanDeclModel              fModel = null ;
protected   JCMMethod					 fcompMethod = null ;
protected	 boolean					 fgenerationRequired = false ;
protected	 boolean					 fStaleOffset = false ;
	


    public CodeMethodRef (AbstractMethodDeclaration method,CodeTypeRef tRef, String methodHandle,ISourceRange range, String content) {
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

protected void setDeclMethod(AbstractMethodDeclaration method){
	fdeclMethod = method ;
	if(fdeclMethod!=null){// && fMethod==null){
		fMethodName = new String(fdeclMethod.selector);
	}
}

public String getMethodName() {
	return fMethodName ;
}

public AbstractMethodDeclaration getDeclMethod() {
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
	if (fExpressions!= null)
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
					expression.setProprity(new int[]{IJavaFeatureMapper.PRIORITY_INIT_EXPR, 0});
				}else{
					expression.setProprity(new int[]{IJavaFeatureMapper.PRIORITY_DEFAULT, 0});
				}
			}
		}catch(Exception e ){JavaVEPlugin.log(e, MsgLogger.LOG_WARNING) ;}
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
					expression.setProprity(new int[]{IJavaFeatureMapper.PRIORITY_INIT_EXPR, 0});
				}else{
					expression.setProprity(new int[]{IJavaFeatureMapper.PRIORITY_DEFAULT, 0});
				}
			}
		}catch(Exception e ){JavaVEPlugin.log(e, MsgLogger.LOG_WARNING) ;}
	}
}

/**
 * Manipulates the input List to rearrange the expressions based on the beans.
 * 
 * @param newfExpressions  The list in which expressions are sorted based on the 
 * @return  Two Objects: { 
 * 	the first offset that can be used to place an expression (Integer), 
 * 	the first filler that can be used (String) 
 * }
 */
protected static List sortExpressionsByBeans(List newfExpressions){
	Hashtable beanToExpHash = new Hashtable();
	for(int count=0;count<newfExpressions.size();count++){
		CodeExpressionRef exp = (CodeExpressionRef)newfExpressions.get(count);
		if (exp.isStateSet(CodeExpressionRef.STATE_DELETE)) continue ;
		
		if(!beanToExpHash.containsKey(exp.getBean())){
			beanToExpHash.put(exp.getBean(), new ArrayList());;
		}
		List expHolder = (List) beanToExpHash.get(exp.getBean());
		expHolder.add(exp);
	}
	
	List tmpFinalOrder = new ArrayList();
	//int firstReferenceIndex = -1;
	//int firstReferenceOffset = Integer.MAX_VALUE;
	//String firstReferenceFiller = null;
	// Now there are unique beans (as keys) in the hashtable.
	Enumeration beansToBeVisited = determineBeanOrder(beanToExpHash.keys());
	while(beansToBeVisited.hasMoreElements()){
		Vector sortedExpressions = new Vector();  // Will hold the expressions sorted purely by priority.
		List expressions = (List) beanToExpHash.get(beansToBeVisited.nextElement());
		SortedSet methodSorterSet = new TreeSet(getDefaultMethodComparator());
		for(int ec=0;ec<expressions.size();ec++){
			methodSorterSet.add(expressions.get(ec));
		}
		Iterator sorted = methodSorterSet.iterator();
		while(sorted.hasNext())
			sortedExpressions.add(sorted.next());

		Vector beanFinalOrder = new Vector(sortedExpressions.size());
		for(int expc=0;expc<sortedExpressions.size();expc++){
			CodeExpressionRef exp = (CodeExpressionRef)sortedExpressions.get(expc);
			if(exp.isStateSet(CodeExpressionRef.STATE_SRC_LOC_FIXED)){
				// Record the top-most available STATE_SRC_LOC_FIXED,
				// as this is our reference for newly generated expressions.
//				if(exp.getOffset()<firstReferenceOffset){
//					firstReferenceOffset=exp.getOffset();
//					firstReferenceIndex=expc;
//					firstReferenceFiller=exp.getFillerContent();
//				}
				// Location fixed.. find the right place 
				// in the final order, irrespective of 
				// priority
				int bestPosition = expc;
				for(int i=0;i<beanFinalOrder.size();i++){
					CodeExpressionRef texp = (CodeExpressionRef)beanFinalOrder.get(i);
					if(texp.isStateSet(CodeExpressionRef.STATE_SRC_LOC_FIXED)){
						if(texp.getOffset()>exp.getOffset()){
							bestPosition=i;	// insert at this place
						}else{
							// Do nothing as the expression is in the right place.
						}
					}
				}
				beanFinalOrder.insertElementAt(exp, bestPosition);
			}else{
				beanFinalOrder.add(expc, exp);
			}
		}
		tmpFinalOrder.addAll(beanFinalOrder);
	};
	return tmpFinalOrder;
}

protected List reorderInitExpsToTop(List unordered){
	int maxPriorityCount = 0;
	Iterator allExpressions = unordered.iterator();
	ArrayList ordered = new ArrayList(unordered.size());
	while(allExpressions.hasNext()){
		CodeExpressionRef exp = (CodeExpressionRef) allExpressions.next();
		if(((int[])exp.getPriority())[0]==IJavaFeatureMapper.PRIORITY_INIT_EXPR){
			if(exp.getBean()!=null &&
			   exp.getBean().getReturnedMethod()!=null &&
			   exp.getBean().getReturnedMethod().equals(this))
				ordered.add(0,exp);
			else
				ordered.add(maxPriorityCount,exp);
			maxPriorityCount++;
		}else{
			ordered.add(exp);
		}
	}
	return ordered;
}

protected List orderExpressions(List expressions){
	List tmpExpressionOrder = new ArrayList(expressions);
	tmpExpressionOrder = sortExpressionsByBeans(tmpExpressionOrder);
	tmpExpressionOrder = reorderInitExpsToTop(tmpExpressionOrder);
	return tmpExpressionOrder;
}


protected Object[] getUsableOffsetAndFiller(List allExpressionsSorted) throws CodeGenException{
	int offset = -1;
	String filler = null;
	
	/*
	 * First, we look at all the expressions passed in to find out 
	 * 
	 * (A) Which is the last SRC_LOC_FIXED expression, before a floating expression -OR-
	 * (B) Which is the first SRC_LOC_FIXED expression, after a floating expression  
	 * 
	 * which can be used to provide a filler and an offset. If one is found it is returned. 
	 */
	 boolean floatingExpessionFound = false;
	 for(int i=0;i<allExpressionsSorted.size();i++){
	 	CodeExpressionRef ce = (CodeExpressionRef) allExpressionsSorted.get(i);
	 	if(ce.isStateSet(CodeExpressionRef.STATE_SRC_LOC_FIXED)){
	 		offset = ce.getOffset();
	 		filler = ce.getFillerContent();
	 	}else{
	 		floatingExpessionFound = true;
	 	}
		if(floatingExpessionFound && offset!=-1 && filler!=null)
			break;
	 }
	if(offset!=-1 && filler!=null)
		return new Object[]{new Integer(offset), filler};
	
	/*
	 * If a SRC_LOC_FIXED expression is not found, then a default is calculated by looking at the
	 * AST and getting default values.
	 */
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
 * Updates the expression order so that:
 * 
 * (*) Expressions modifying a particular bean always stick
 *     togeether. Except the declaration statements, because
 *     they might be used by other beans
 * 
 * The way it is done is:
 * (*) Determine the priority of all the expressions. Priority
 *     of expressions is determined by the expression's decoders.
 *     If the expression contains a 'new', it is assigned 
 *     Integer.MAX_VALUE priority. If any problem occurs, the
 *     expression is set a priority of -1.
 * (*) 
 */
public  void updateExpressionOrder() throws CodeGenException{

	resetExpressionPriorities();
	
	List sortedExpressions = orderExpressions(fExpressions);
	List sortedEventExpressions = orderExpressions(fEventExpressions);
	
	List allExpressions = new ArrayList(sortedExpressions);
	allExpressions.addAll(sortedEventExpressions);

	// Get the best usable filler and offset for placing new expressions.
	int bestUsablePosition = -1;
	String bestUsableFiller = new String();
	try {
		Object[] ret = getUsableOffsetAndFiller(allExpressions);
		bestUsablePosition = ((Integer)ret[0]).intValue();
		bestUsableFiller = (String)ret[1];
	} catch (CodeGenException e) {
		JavaVEPlugin.log(e, MsgLogger.LOG_WARNING);
	}
	
	// Place the expressions
	for(int c=0;c<allExpressions.size();c++){
		CodeExpressionRef exp = (CodeExpressionRef)allExpressions.get(c);
		if(exp.isStateSet(CodeExpressionRef.STATE_SRC_LOC_FIXED)){
			if(bestUsablePosition<(exp.getOffset()+exp.getLen())){
				bestUsableFiller = exp.getFillerContent();
				bestUsablePosition = exp.getOffset() + exp.getLen();
			}
		}else{
			if(bestUsableFiller!=null)
				exp.setFillerContent(bestUsableFiller);
			{// ALWAYS keep an expression which is floating after ALL
			  // the 'new' expressions in the method ! 
				Iterator itr = allExpressions.iterator();
				while(itr.hasNext()){
					CodeExpressionRef tmpexp = (CodeExpressionRef) itr.next();
					if(tmpexp.isStateSet(CodeExpressionRef.STATE_SRC_LOC_FIXED) && 
					   ((int[])tmpexp.getPriority())[0]==IJavaFeatureMapper.PRIORITY_INIT_EXPR&&
					   tmpexp.getOffset()>bestUsablePosition)
					   	bestUsablePosition = tmpexp.getOffset()+tmpexp.getLen();
				}
			}
			exp.setOffset(bestUsablePosition);
			bestUsablePosition += exp.getLen();
			exp.setState(CodeExpressionRef.STATE_SRC_LOC_FIXED, true); //exp.setState(exp.getState() | exp.STATE_SRC_LOC_FIXED); // line is not readjusted again
		}
	}
	
	fExpressions = (ArrayList) sortedExpressions;
	fEventExpressions = (ArrayList) sortedEventExpressions;
}


protected static Enumeration determineBeanOrder(Enumeration beans){
	SortedSet beanSorter = new TreeSet(getDefaultBeanOrderComparator());
	while(beans.hasMoreElements()){
		boolean added = beanSorter.add((BeanPart) beans.nextElement());
		if(!added)
			JavaVEPlugin.log("No addition to the treeset", MsgLogger.LOG_FINE); //$NON-NLS-1$
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
				return -1;
			}else{
				if(np1 == np2){
					return 1;
				}else{
					return 1;
				}
			}
		}
		
		protected int getImportanceCount(BeanPart main, BeanPart subMain){
			if(isAChildOf(main, subMain))
				return Integer.MAX_VALUE;
			int parentCount = getParentCount(main);
			int constraintCount = getConstraintCount(main);
			return parentCount + constraintCount;
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
			while(b.getBackRefs().length>0 && safetyCount>0){
				pc++;
				safetyCount--;
				b = b.getBackRefs()[0];
			}
			return pc;
		}
	};
}

/**
 *  @return a negative integer, zero, or a positive integer as the
 * 	       first argument is less than, equal to, or greater than the
 *	       second.
 *  warning: Never return zero, as values are overwritten if equal.
 */
protected static Comparator getDefaultMethodComparator(){
	// TODO  SortedMap doesnt store equal objects - please investigate
	return 
	new Comparator(){
		public int compare(Object e1, Object e2){
			CodeExpressionRef r1 = (CodeExpressionRef)e1;
			CodeExpressionRef r2 = (CodeExpressionRef)e2;
			int sfPriority1 = ((int[])r1.getPriority())[0];
			int sfPriority2 = ((int[])r2.getPriority())[0];
			int indexPriority1 = ((int[])r1.getPriority())[1];
			int indexPriority2 = ((int[])r2.getPriority())[1];
			int o1 = r1.getOffset();
			int o2 = r2.getOffset();
			if(o1<0)					// If the offsets are less than 0
				o1 = Integer.MAX_VALUE;		// then the expression is a new one.
			if(o2<0)					// Setting their offsets to MAX will
				o2 = Integer.MAX_VALUE;		// make the expression add itself at the end of the 
			if(sfPriority1<sfPriority2){					// list of similar priority expressions.
				return 1;  // Descending
			}else{
				if(sfPriority1==sfPriority2){ // Same Priority, look at offsets
					if(indexPriority1<indexPriority2){
						return 1;
					}else{
						if(indexPriority1==indexPriority2){
							if(o1<o2){
								return -1; // Less Than
							}else{
								return 1; // Greater than
							}
						}else{
							return -1;
						}
					}
				}else{
					return -1;  //Descending
				}
			}
		}
		public boolean equals(Object o){
			return super.equals(o);
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
	      if (!fModel.getCompilationUnit().isConsistent()) fModel.getCompilationUnit().reconcile() ;
	    }
	    catch (JavaModelException e) {}
		IType mainType = CodeGenUtil.getMainType(fModel.getCompilationUnit());
		IMethod m = CodeGenUtil.getMethod(mainType, getMethodHandle());
		refreshIMethod(m);
	}catch(Exception e){
		JavaVEPlugin.log(e, MsgLogger.LOG_WARNING);
	}
}

public void refreshIMethod(IMethod m) {
	try {
		if(m!=null){
			setOffset(m.getSourceRange().getOffset()) ;
			setContent(m.getSource());
		}
	} catch (JavaModelException e) {
		JavaVEPlugin.log(e, MsgLogger.LOG_WARNING);
	}			
}


public String _debugExpressions() {
    
    StringBuffer sb = new StringBuffer() ;
    int mOffset = getOffset() ;
    String   doc = fModel.getDocumentBuffer().getContents() ;
    Iterator itr = getExpressions() ;
    while (itr.hasNext()) {
        CodeExpressionRef exp = (CodeExpressionRef)itr.next() ;
        sb.append(exp.toString()+"\n\"") ; //$NON-NLS-1$
        if (!exp.isStateSet(CodeExpressionRef.STATE_DELETE))
            sb.append(doc.substring(mOffset+exp.getOffset(),mOffset+exp.getOffset()+exp.getLen())) ;
        else
            sb.append("STATE_NOT_EXISTANT") ; //$NON-NLS-1$
        sb.append("\"\n") ; //$NON-NLS-1$
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
	  fTypeRef.getBeanComposition().getMethods().add(fcompMethod) ;	
	  fcompMethod.setName(getMethodName()) ;
	  // Start listining to changes in its membership
	  MemberDecoderAdapter a = new MemberDecoderAdapter(fModel) ;
	  a.setMethodRef(this) ;
	  a.setTarget(fcompMethod) ;
      fcompMethod.eAdapters().add(a) ;
	}
	return fcompMethod;
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

}