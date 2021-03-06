/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.codegen.util;
/*
 *  $RCSfile: CodeGenUtil.java,v $
 *  $Revision: 1.68 $  $Date: 2006-05-17 20:14:52 $ 
 */



import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.ArrayType;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.instantiation.impl.NaiveExpressionFlattener;
import org.eclipse.jem.java.*;
import org.eclipse.jem.util.CharacterUtil;

import org.eclipse.ve.internal.cdm.*;
import org.eclipse.ve.internal.cdm.Annotation;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;

import org.eclipse.ve.internal.jcm.*;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.java.rules.InstanceVariableCreationRule;
import org.eclipse.ve.internal.java.codegen.java.rules.InstanceVariableRule;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.core.ASTMethodUtil;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.vce.rules.IEditorStyle;
import org.eclipse.ve.internal.java.vce.rules.VCEPostSetCommand;

import com.ibm.icu.text.UTF16;
import com.ibm.icu.util.StringTokenizer;


/**
 *  Assortments of utilities for Code Generation
 */

public class CodeGenUtil {
	
	public static boolean isExactlyPresent(String main, String target){
	if(main==null || target==null)
		return false;
	int index = main.indexOf(target);
	if(index<0)
		return false;
	index += target.length();
	if(index>=main.length())
		return true;
	// Any more characters present?
	if(CharacterUtil.isJavaIdentifierPart(UTF16.charAt(main, index)))
		return false;
	else
		return true;
}

/**
 *  Convert an array of token elements, to a '.' seperated String
 */
static public String tokensToString(char tokens[][]) {

    StringBuffer buf = new StringBuffer() ;	
	for (int i=0; i<tokens.length; i++) {
		if (i>0 && buf.length()>0) buf.append(".") ; //$NON-NLS-1$
		buf.append(tokens[i]) ;
	}
	return buf.toString() ;	
}


/**
 * Create and Java Object Instance for a given type
 */
public  static IJavaInstance  createInstance (String instanceType,ResourceSet rs) throws CodeGenException {
  
  JavaHelpers iClass =  JavaRefFactory.eINSTANCE.reflectType(instanceType, rs);
  IJavaInstance inst = (IJavaInstance) iClass.getEPackage().getEFactoryInstance().create(iClass) ;
  return inst ;  
}

/**
 * Create and Java Object Instance for a given type
 */
public static IJavaInstance createInstance(String instanceType, IVEModelInstance cm) throws CodeGenException {
	if (cm.getModelResourceSet() == null)
		throw new CodeGenException("MOF is not set up"); //$NON-NLS-1$
	if (JavaVEPlugin.isLoggingLevel(Level.FINE))
		JavaVEPlugin.log("CodeGenUtil.createInstance(" + instanceType + ")", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$

	return createInstance(instanceType, cm.getModelResourceSet());
}

public static EClassifier getMetaClass (String qualifiedName, IVEModelInstance cm) {
     if (cm.getModelResourceSet() == null || qualifiedName == null) return null ;
     return  JavaRefFactory.eINSTANCE.reflectType(qualifiedName, cm.getModelResourceSet());
}

/**
 * Create a "name in composition" annotation for the given annotation.
 */
public static void addAnnotatedName(Annotation a, String name) {
	if (a != null) {
		EStringToStringMapEntryImpl sentry = (EStringToStringMapEntryImpl) EcoreFactory.eINSTANCE.create(EcorePackage.eINSTANCE.getEStringToStringMapEntry());
		sentry.setKey(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
		if (name != null && name.length() > 0)
			sentry.setValue(name);
		CDEUtilities.putEMapEntry(a.getKeyedValues(), sentry);
	}
}


/**
 * Get the one and only constraint feature for this project.
 * This is a one to one feature
 */
public static EStructuralFeature getConstraintFeature(EObject componentConstraint) {
   return  componentConstraint.eClass().getEStructuralFeature("constraint") ;      // NO global constant at this time    //$NON-NLS-1$
}

/**
 * Add a generic constraint to a target object 
 */
public static void addConstraintString(
	MemberContainer pOwner,
	EObject target,
	String initVal,
	EStructuralFeature sf,
	IVEModelInstance cm)
	throws Exception {

	IJavaInstance value = null;
	EObject oldConstraint = (EObject) target.eGet(sf);
	if (initVal != null) {
		value = createInstance("java.lang.String", cm); //$NON-NLS-1$
		value.setAllocation(InstantiationFactory.eINSTANCE.createInitStringAllocation(initVal));
		pOwner.getProperties().add(value);
	}
	target.eSet(sf, value);
	CodeGenUtil.propertyCleanup(oldConstraint);
}
/**
 * Add a generic constraint to a target object 
 */
public static void addConstraintInstance(EObject target,IJavaObjectInstance val, EStructuralFeature sf) throws Exception {
  
  target.eSet(sf,val);  	
}

/**
 * Get the one and only component feature for this project.
 * This is a one to many feature
 */
public static EStructuralFeature getComponentFeature(EObject target) { 

  
   	   return target.eClass().getEStructuralFeature("components") ;      // NO global constant at this time       //$NON-NLS-1$       
}

/**
 * Get the one and only Parent component feature for this project.
 * This is a one to many feature
 * @deprecated - parent componentContainer is not used anymove
 */
public static EStructuralFeature getParentContainerFeature(EObject target) {  
   return  target.eClass().getEStructuralFeature("parentContainer") ;      // NO global constant at this time       //$NON-NLS-1$
}

/**
 * Get the main Type of a compilation unit
 */
public static IType getMainType(ICompilationUnit cu) {
	// TODO  make use of ICompilationUnit.findPrimaryType() instead.
	if(cu == null)
		return null;
	try {
		IType[] types = cu.getTypes();
		int index = 0;
		while (types[index].isAnnotation() && index<types.length)
			index++;
		return types.length > index ? types[index] : null;
	} catch (JavaModelException e) {
	}
	
	return null;
}

/**
 * Get the specified Type of a compilation unit
 */
public static IType getType(ICompilationUnit cu, String name) {
	if(cu == null)
		return null;
	IType[] elements = null ;
	IType main = getMainType(cu);
	try {
		if (main != null) {
			elements = main.getTypes() ;
			for (int i = 0; i < elements.length; i++) {
				if (elements[i].getElementName().equals(name))
				   return elements[i] ;
			}
		}
	}
	catch (JavaModelException e) {}
    return null ;		
}
/**
 *  Get the methods associated with the main type of a compilation unit
 */ 
public static IMethod[] getMethods(ICompilationUnit cu) {
	if(cu == null)
		return null;
	IType mt = getMainType(cu) ;
	try {
	 if (mt != null)	
	    return mt.getMethods() ;
	}
	catch (JavaModelException e) {}
    return null ;
}

public static JavaElementInfo[] getMethodsInfo(ICompilationUnit cu) {
	IMethod[] mtds = getMethods(cu);
	if (mtds!=null) {
		JavaElementInfo[] info = new JavaElementInfo[mtds.length];
		for (int i = 0; i < mtds.length; i++) {
		   info[i] = new JavaElementInfo(mtds[i]);					
		}
		return info;
	}
	return null;	
}

/**
 *  Get the methods associated with the main type of a compilation unit
 */ 
public static IMethod[] getMethods(ICompilationUnit cu, String typeName) {
	if(cu == null)
		return null;
	IType mt = getType(cu,typeName) ;
	try {
	 if (mt != null)	
	    return mt.getMethods() ;
	}
	catch (JavaModelException e) {}
    return null ;
}

public static JavaElementInfo[] getMethodsInfo(ICompilationUnit cu, String typeName) {
	if(cu == null)
		return null;
	IMethod[] mtds = getMethods(cu,typeName);
	if (mtds!=null) {
		JavaElementInfo[] info = new JavaElementInfo[mtds.length];
		for (int i = 0; i < mtds.length; i++) {
		   info[i] = new JavaElementInfo(mtds[i]);					
		}
		return info;
	}
	return null;	
}

/**
 *   CU's content might have changed, and an old method is stale.
 */
public static IMethod refreshMethod(IMethod method) {
	if (method == null) return method ;
	
	ICompilationUnit cu = method.getCompilationUnit() ;
	try {
	  if (!cu.isConsistent()) cu.reconcile(ICompilationUnit.NO_AST, false, null, new NullProgressMonitor()) ;
	}
	catch (JavaModelException e) {}
	
	IType t = CodeGenUtil.getMainType(cu) ;
	return getMethod(t,method) ;
}

public static IMethod refreshMethod(String handle, ICompilationUnit cu) {
	if (handle == null) return null ;
	try {
	  if (!cu.isConsistent()) cu.reconcile(ICompilationUnit.NO_AST, false, null, new NullProgressMonitor()) ;
	}
	catch (JavaModelException e) {}
	return getMethod(getMainType(cu),handle) ;
}

public static IField getField (String handle, ICompilationUnit cu) {
	if (handle == null) return null ;
	try {
	    if (!cu.isConsistent()) cu.reconcile(ICompilationUnit.NO_AST, false, null, new NullProgressMonitor()) ;
		
    	IType t = getMainType(cu) ;
    	IField[] flds = t.getFields() ;
    	for (int i = 0; i < flds.length; i++) {
            IField iField = flds[i];
            if (iField.getHandleIdentifier().equals(handle)) 
               return iField ;
        }	
    }
	catch (JavaModelException e) {}
	return null ;
}

public static IField getFieldByName (String name, ICompilationUnit cu) {
	if (name == null || cu==null) return null ;
	try {
	    if (!cu.isConsistent()) cu.reconcile(ICompilationUnit.NO_AST, false, null, new NullProgressMonitor()) ;
		
    	IType t = getMainType(cu) ;
    	IField[] flds = t.getFields() ;
    	for (int i = 0; i < flds.length; i++) {
            IField iField = flds[i];
            if (iField.getElementName().equals(name)) 
               return iField ;
        }	
    }
	catch (JavaModelException e) {}
	return null ;
}

public static IMethod getMethod (IType t, String handle) {
	if (t == null)
	   return null ;
	try {
	 IMethod[] mt = t.getMethods() ;
	 for (int index=0;index<mt.length;index++) 
	  if (mt[index].getHandleIdentifier().equals(handle)) // getHandleIdentifier() is slow, consider a cache
	      return (mt[index]) ;
	}
	catch (JavaModelException e) {}
    return null ;	
}

public static IMethod getMethod (IType t, IMethod old) {
	
	try {
	 IMethod[] mt = t.getMethods() ;
	 for (int index=0;index<mt.length;index++) 
	  if (mt[index].isSimilar(old)) 
	      return (mt[index]) ;
	}
	catch (JavaModelException e) {}
    return null ;	
}

/**
 *  Update methods source offset
 */ 
public static void refreshMethodOffsets (IType t, IBeanDeclModel model) throws CodeGenException {
	try {
	 IMethod[] methods = t.getMethods() ;
	 for (int i=0; i<methods.length; i++) {
	   CodeMethodRef mr = model.getMethodInitializingABean(methods[i].getHandleIdentifier()) ;	  
	    if (mr != null)
	       mr.setOffset(methods[i].getSourceRange().getOffset()) ;
	 }
	}
	catch (JavaModelException e) {
	  	throw new CodeGenException (e) ;
	}
}


/**
 * 
 */ 
private static boolean isComponentContained (IBeanDeclModel m,EObject component, EObject parent) throws CodeGenException {
   if (parent==null || component==null) return false ;
   if (component.equals(parent)) return true ;
   
   IExpressionDecoder decoder = getDecoderFactory(m).getExpDecoder((IJavaObjectInstance)parent) ;
   Iterator itr = decoder.getChildren((IJavaObjectInstance)parent).iterator() ;
   while (itr.hasNext()) {
   	EObject child = (EObject) itr.next() ;
   	itr.next() ; // Skip the SF
   	if (isComponentContained(m,component,child)) 
   	   return true ;
   }   
   return false ;   	
}
/**
 *  Update methods source offset
 */ 
public static boolean isComponentInComposition (IBeanDeclModel m, EObject component, IVEModelInstance model) throws CodeGenException {
	Iterator itr = model.getModelRoot().getComponents().iterator() ;
	while (itr.hasNext()) {
		if (isComponentContained(m, component,(EObject)itr.next())) 
		   return true ;
	}
	return false ;	
}

/**
 *
 */

private static IJavaObjectInstance getParent (IBeanDeclModel model, IJavaObjectInstance child, IJavaObjectInstance parent) throws CodeGenException {
   if (parent==null || child==null) return null ;
   if (parent.equals(child)) return null ;
   
   IExpressionDecoder decoder = getDecoderFactory(model).getExpDecoder(parent) ;
   Iterator itr = decoder.getChildren(parent).iterator() ;
   while (itr.hasNext()) {
   	IJavaObjectInstance c = (IJavaObjectInstance) itr.next() ;
   	itr.next() ; // Skip the SF
   	if (c.equals(child)) return parent ;
   	IJavaObjectInstance p = (getParent(model,child,c))  ;
   	if (p != null) return p ;
   }   
   return null ;   	
}

/**
 *  Get parent (implicit or not implicit) of this component
 */
public static IJavaObjectInstance getParent (IBeanDeclModel bmodel, IJavaObjectInstance child, IVEModelInstance model) throws CodeGenException {
	Iterator itr = model.getModelRoot().getComponents().iterator() ;
	while (itr.hasNext()) {
		IJavaObjectInstance p = getParent (bmodel, child,(IJavaObjectInstance)itr.next()) ;
		if (p != null) return p ;
	}
	return null ;	
}




/**
 *  Check if the layout property is set to NULL
 */ 
public static boolean isContainerHasLayoutManager(EObject container) {
	if (container == null) return false ;
	EStructuralFeature sf = container.eClass().getEStructuralFeature("layout") ; //$NON-NLS-1$
	return !(container.eIsSet(sf) && container.eGet(sf) == null);
}
/**
 *  Get a containers top childrens
 *  @return List of children instances
 */
public static List getChildrenComponents (EObject parent) {
    if (parent == null) return null ;
    List compList=null ;
    EStructuralFeature sf = getComponentFeature(parent) ;
    if (sf != null) {
     try {
       compList = (List) parent.eGet(getComponentFeature(parent));
     }
     catch (Exception e) { 
     }
    }    
    // Look for out added part
    if (compList != null && compList.size()==0) return null ;
    return compList ;	
}
/**
 *  Get a component's container
 *  @return the container's instances
 */
public static EObject getParentComponent (EObject child) {
	
    if (child == null) return null ;
    // Not there anymore because a parent may use different features CC, Tabbed, NoConstraint etc.
    return null ;
//    EObject obj = null ;
//    try {
//       obj  = (EObject) child.eGet(getParentContainerFeature(child));
//    }
//    catch (Exception e) { 
//    }    
//    return obj ;
}

public static IJavaObjectInstance getCCcomponent(EObject constraintComponent) {
	IJavaObjectInstance comp = (IJavaObjectInstance)
	                      constraintComponent.eGet(constraintComponent.eClass().getEStructuralFeature("component")) ; //$NON-NLS-1$
	return comp ;
}

public static IJavaObjectInstance getCCconstraint(EObject constraintComponent) {
	try {
		IJavaObjectInstance cons = (IJavaObjectInstance) constraintComponent.eGet(constraintComponent.eClass().getEStructuralFeature("constraint")); //$NON-NLS-1$
		return cons;
	} catch (IllegalArgumentException e) {
	};
	return null;
}


/**
 * Get the initialization String Feature
 */
public static EStructuralFeature getAllocationFeature(EObject target) {
   return  target.eClass().getEStructuralFeature(JavaInstantiation.ALLOCATION) ;       //$NON-NLS-1$
}

/**
 * Returns the index of searchFor in searchIn, taking into 
 * account the presence of 
 * 
 * @param searchIn
 * @param seachFor
 * @return int
 */
public static int getExactJavaIndex(String searchIn, String seachFor){
	int index = -1;
	int indexEnd = -1;
	// prepare for [<spc>|\t|\r|\n]new[<spc>|\t|\r|\n] etc.
	while(searchIn.indexOf(seachFor, indexEnd)>-1){
		index = searchIn.indexOf(seachFor, indexEnd);
		indexEnd = index+seachFor.length();
		boolean startAllright = index==0 || (index>0 && !CharacterUtil.isJavaIdentifierPart(UTF16.charAt(searchIn, index-1)));
		boolean endAllright = indexEnd==searchIn.length() || (indexEnd<searchIn.length()&&!CharacterUtil.isJavaIdentifierPart(UTF16.charAt(searchIn, indexEnd+1)));
		if(startAllright && endAllright)
			return index;
	}
	return -1;
}

public static boolean areAllocationsEqual(JavaAllocation exp1, JavaAllocation exp2) {
	if (exp1 instanceof ParseTreeAllocation && exp2 instanceof ParseTreeAllocation)
		return areParseTreesEqual(((ParseTreeAllocation) exp1).getExpression(), ((ParseTreeAllocation) exp2).getExpression());
	else if (exp1 instanceof InitStringAllocation && exp2 instanceof InitStringAllocation) {
		return ((InitStringAllocation) exp1).getInitString().equals(((InitStringAllocation) exp2).getInitString());
	} else
		return exp1 == exp2;	// This handles both being null.
}

public static boolean areParseTreesEqual(PTExpression exp1, PTExpression exp2){
	PTExpressionComparator comparator = new PTExpressionComparator(exp2);
	exp1.accept(comparator);
	return comparator.isEqual();
}

public static String getInitString(IJavaInstance javaInstance, IBeanDeclModel model, List importList, List refList) {
	JavaAllocation alloc = javaInstance.getAllocation();
	if (alloc instanceof InitStringAllocation)
		return ((InitStringAllocation) alloc).getInitString();
	else if (alloc instanceof ParseTreeAllocation) {
		PTExpression e = ((ParseTreeAllocation)javaInstance.getAllocation()).getExpression();
		// Resolve references if needed
		CodeGenExpFlattener f = new CodeGenExpFlattener(model, importList, refList);
		e.accept(f);
		return f.getResult();
	} else {
		JavaHelpers jc = javaInstance.getJavaType();
		if(jc.isInterface() || jc.isAbstract()){
			PTAnonymousClassDeclaration anon = ASTMethodUtil.createAnonymousDeclaration(jc, model.getCompilationUnit());
			if (anon != null) {
				importList.addAll(anon.getImports());
				return anon.getDeclaration();
			} else
				return "";	// Shouldn't get here, but in case an error in the create. //$NON-NLS-1$
		}
		
		// There is none, so let's create the default ctor (only valid for classes)
		// Return construct with default ctor when not explicitly set.
		if (!jc.isPrimitive()) {
			String qn;
			if (importList!=null) {
			  // use Imports
			  String importName = jc.getQualifiedName();
			  if(((JavaClass)jc).isNested())
				  importName = ((JavaClass)jc).getDeclaringClass().getQualifiedName();
			  if (!importList.contains(importName))
			      importList.add(importName);
			  qn = jc.getSimpleName();
		    }
			else // use full qualifier
			  qn = jc.getQualifiedName();
			return "new " + qn + "()"; //$NON-NLS-1$ //$NON-NLS-2$
		} else
			return "";	// Shouldn't get here for prims. They should have an allocation. //$NON-NLS-1$
	}
}

/*public static String getResolvedInitString(Statement stmt, ITypeResolver resolver){
	String resolved = null;
	if (stmt instanceof TypeReference && resolved==null) {
		TypeReference tr = (TypeReference) stmt;
		return resolver.resolve(CodeGenUtil.tokensToString(tr.getTypeName()));
	}
	if (stmt instanceof SingleNameReference) {
		SingleNameReference snr = (SingleNameReference) stmt;
		return resolver.resolve(new String(snr.token));
	}
	if(stmt instanceof AllocationExpression && resolved==null){
		AllocationExpression ae = (AllocationExpression) stmt;
		resolved = ae.toString();
		TypeReference str = ae.type;
		String type = CodeGenUtil.tokensToString(str.getTypeName());
		String resolvedType = getResolvedInitString(str,resolver);
		int newStart = getExactJavaIndex(resolved,"new"); //$NON-NLS-1$
		// prepare for new<spc>\t\r\n etc.
		int start = resolved.indexOf(type, newStart);
		int end = start+type.length();
		if(start>0 && end>0 && start<=resolved.length() && end<=resolved.length())
			resolved = resolved.substring(0,start) + resolvedType + resolved.substring(end, resolved.length());
		
		// Resolve any parameters, Ex: new JToolBar(JToolBar.HORIZONTAL);
		if(ae.arguments!=null){
			for(int pc=0;pc<ae.arguments.length;pc++){
				String unresolvedARG = ae.arguments[pc].toString();
				start = resolved.indexOf(unresolvedARG, end);
				end = start + unresolvedARG.length();
				if(start > 0 && end > 0){
					String resolvedARG = getResolvedInitString(ae.arguments[pc], resolver);
					resolved = resolved.substring(0, start) + resolvedARG + resolved.substring(end);
					end+=resolvedARG.length() - unresolvedARG.length();
				}else{
					break;
				}
			}
		}
	}
	if (stmt instanceof CastExpression && resolved==null) {
		CastExpression castExpr = (CastExpression) stmt;
		resolved = castExpr.toString();
		String castUnResolved = castExpr.type.toString();
		String castResolved = getResolvedInitString(castExpr.type, resolver);
		String expUnResolved = castExpr.expression.toString();
		String expResolved = getResolvedInitString(castExpr.expression, resolver);
		int castFrom = getExactJavaIndex(resolved,castUnResolved);
		int castTo = castFrom<0?-1:castFrom+castUnResolved.length();
		if(castFrom>-1 && castTo>-1 && castFrom<=resolved.length() && castTo<=resolved.length() && 
		   (!castUnResolved.equals(castResolved)))
			resolved = resolved.substring(0,castFrom)+castResolved+resolved.substring(castTo,resolved.length());
		int expFrom = getExactJavaIndex(resolved,expUnResolved);
		int expTo = expFrom<0?-1:expFrom+expUnResolved.length();
		if(expFrom>-1 && expTo>-1 && expFrom<=resolved.length() && expTo<=resolved.length() && 
		   (!expUnResolved. equals(expResolved)))
			resolved = resolved.substring(0,expFrom)+expResolved+resolved.substring(expTo,resolved.length());
	}
	if (stmt instanceof MessageSend) {
		MessageSend m = (MessageSend) stmt;
		StringBuffer ms = new StringBuffer (m.toString()) ;
		String r = m.receiver.toString() ;
		ms.replace(0,r.length(),resolver.resolve(r)) ;
		resolved = ms.toString() ;			
	}
	if (stmt instanceof Assignment && resolved==null) {
		Assignment assgn = (Assignment) stmt;
		resolved = assgn.toString();
		if(assgn.expression instanceof AllocationExpression)
			resolved = getResolvedInitString(assgn.expression, resolver);
		else if(assgn.expression instanceof MessageSend) 
			resolved = getResolvedInitString(assgn.expression, resolver);
		else if(assgn.expression instanceof CastExpression)
			resolved = getResolvedInitString(assgn.expression, resolver);
	}
	if (stmt instanceof LocalDeclaration && resolved==null) {
		LocalDeclaration ld = (LocalDeclaration) stmt;
		resolved = ld.toString();
		if(ld.initialization instanceof AllocationExpression ||
		   ld.initialization instanceof ArrayAllocationExpression ||
		   ld.initialization instanceof CastExpression ||
		   ld.initialization instanceof MessageSend){
			resolved = getResolvedInitString(ld.initialization, resolver);
		}
	}
	if (stmt instanceof QualifiedNameReference && resolver!=null) {
		QualifiedNameReference qnr = (QualifiedNameReference) stmt;
		resolved = resolver.resolve(qnr.toString());
	}
	if(resolved==null)
		resolved = stmt.toString();
	return resolved;
}
*/

public static boolean isConstraintComponentValue (Object val) {
	return (val != null && val instanceof EObject && 
		  ((EObject)val).eClass().getName().equals("ConstraintComponent")) ; //$NON-NLS-1$
	
}

public static boolean isTabPaneComponentValue (Object val) {
	return (val != null && val instanceof EObject && 
		  ((EObject)val).eClass().getName().equals("JTabComponent")) ; //$NON-NLS-1$
	
}

public static boolean isThisPart(EObject model) {
	BeanDecoderAdapter beanDecoderAdapter = (BeanDecoderAdapter) EcoreUtil.getExistingAdapter(model, ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);
	// ThisBeanDecoderAdapter subclasses JFCBeanDecoderAdapter, hence the weird logic			
	return beanDecoderAdapter instanceof ThisBeanDecoderAdapter;	
}

/**
 * @deprecated
 * Is this object a meta root object that agregate a JavaObjectInstance (e.g., CC)
 */
public static boolean isSpecialRootComponent(Object val) {
    
    boolean result = false ;
    if (val instanceof EObject) {
	   result = isConstraintComponentValue(val) ||
	            isTabPaneComponentValue(val);
    }
    return result ;	    
}

/**
 * @return the annotation held by obj
 */
public static Annotation getAnnotation (EObject obj) {
	
	// This is no longer done via bi-directional references. org.eclipse.emf now prevents that.
	// Normally AnnotationLinkagePolicy (retrieved from EditDomain) would be used to get the
	// annotation from the object. But at this point we don't have access to the edit domain.
	//
	// Instead we know it is EMF format, so we copy the code from AnnotationLinkagePolicy here to do it.
    if (obj==null) return null;
	AnnotationEMF.ParentAdapter a = (AnnotationEMF.ParentAdapter) EcoreUtil.getExistingAdapter(obj, AnnotationEMF.ParentAdapter.PARENT_ANNOTATION_ADAPTER_KEY);
	return a != null ? a.getParentAnnotation() : null;
}

/**
 * Create a new annotation structure for obj
 */
public static Annotation addAnnotation(EObject obj) {
		AnnotationEMF annotation = CDMFactory.eINSTANCE.createAnnotationEMF();
		annotation.setAnnotates(obj);
		return annotation ;
}

/**
 *   This is a workaround to touch obj, up the container stack, so that the proxy adapter
 *   forces an invalidate on the VM.
 */
public static void snoozeAlarm(EObject obj, ResourceSet rs, HashMap history) {
	
	// Should be in the model
    if (obj != null && obj.eContainer() != null && rs!=null) {
    	// This adapter will maintain all references but no the container
    	history.put(obj, obj);
		InverseMaintenanceAdapter ai = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(obj, InverseMaintenanceAdapter.ADAPTER_KEY);
		if (ai != null) {
			EReference[] refs = ai.getFeatures();
			for (int sfIdx = 0; sfIdx < refs.length; sfIdx++) {
				EReference sf = refs[sfIdx] ;
				// Only touch elements that reference us, but not our parent 
				if (!sf.isContainment() && !VCEPostSetCommand.isChildRelationShip(sf) &&
				    !sf.getName().equals("initializes") &&  //$NON-NLS-1$
					!sf.getName().equals("return")) {  //$NON-NLS-1$
					EObject[] list = ai.getReferencedBy(sf);
					for (int i = 0; i < list.length; i++) {
						EObject parent = list[i];
						if (sf.isMany()) {
						   List elements = (List) parent.eGet(sf) ;
						   elements.set (elements.indexOf(obj), obj) ;
						}
						else {
							parent.eSet(sf, obj) ;
						}
						if (history.get(parent) == null)
						    snoozeAlarm(parent,rs, history) ;	
					}
				}
			}
		}
    }
}

/**
 * Currently MOF move's of element from one container to another will not guarentee
 * an unset before the set notifications
 */
public static void eSet(EObject obj, EStructuralFeature sf, EObject val, int index) {

	if (sf.isMany()) {
		List elements = (List) obj.eGet(sf) ;
		if (index>=0)
		   if(elements.contains(val))
		      elements.set(index,val) ;
	       else
	          elements.add(index,val) ;
		else
		   elements.add(val) ;
	}
	else
	   obj.eSet(sf, val) ;
}
public static void clearCache () {
	PropertyFeatureMapper.clearCache() ;
	InstanceVariableCreationRule.clearCache() ;
	InstanceVariableRule.clearCache() ;
}

public static IEditorStyle getEditorStyle (org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel beanModel) {
  	return (IEditorStyle) beanModel.getDomain().getRuleRegistry();
}

public static ExpressionDecoderFactory getDecoderFactory (org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel beanModel) {
  	EditDomain d = beanModel.getDomain() ;
  	return (ExpressionDecoderFactory) d.getData(ExpressionDecoderFactory.CodeGenDecoderFactory_KEY) ;
}

public static MethodGeneratorFactory getMethodTextFactory (org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel beanModel) {
	EditDomain d = beanModel.getDomain() ;
	return (MethodGeneratorFactory) d.getData(MethodGeneratorFactory.CodeGenMethodGeneratorFactory_KEY) ;
}


/**
 * Clean up a specific property.
 * @param property if this is a "property" of a member container, it will be removed from the member container. If can be null.
 * @return <code>true</code> if it was removed.
 * 
 * @since 1.1.0
 */
public static boolean propertyCleanup(EObject property) {
  	 if (property!=null && property.eContainingFeature() == JCMPackage.eINSTANCE.getMemberContainer_Properties()) {
  	     ((MemberContainer) property.eContainer()).getProperties().remove(property);
  	     return true;
  	 } else
  	 	return false;
}

public static void logParsingError(String exp, String method, String msg, boolean event) {
	if (JavaVEPlugin.isLoggingLevel(Level.FINE)) {
		String context ;
		if (event)
		   context = "as an event registration" ; //$NON-NLS-1$
		else
		   context = "as a property setting" ; //$NON-NLS-1$
		JavaVEPlugin.log ("\n/**\n Could not parse the following expression "+context+":\n\t"+method+"(): "+exp+"\n\treason: "+msg+"\n**/\n",Level.FINE) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}
}

/*public static String expressionToString (Expression exp) {
	StringBuffer sb = new StringBuffer() ;
	exp.printExpression(4,sb) ;
	return sb.toString();	
}
*/


/**
 * Determines where in the first parameter, the second parameter
 * occurs. It ignores the presence of spaces in both Strings.
 * It returns two integers
 * 		Index where the second String starts
 *		Index where the second String stops
 *  
 * Ex: 
 *     main :   "ivjButton1.  setBackground   (new Color  (100, 2 , 1 )   )"
 *     find :   "new Color(   100,2   ,1)"
 *     
 *     returns: "ivjButton1.  setBackground   (new Color  (100, 2 , 1 )   )"
 *                                             ^                      ^
 *                                             |                      |
 */
public static int[] indexOfIgnoringSpace(String main, String find){
	if(main==null || main.length()<1 || find==null || find.length()<1)
		return new int[] {-1,-1};
	char[] mainTokens = main.toCharArray();
	StringTokenizer tokenizer = new StringTokenizer(find, " \t", false); //$NON-NLS-1$
	String findS = new String();
	while(tokenizer.hasMoreTokens())
		findS = findS.concat(tokenizer.nextToken());
	char[] findTokens = findS.toCharArray();
	int tokenFindStart = -1;
	int tokenFindEnd = -1;
	for(int mc=0;mc<mainTokens.length;mc++){
		if(Character.isWhitespace(mainTokens[mc]))
			continue;
		tokenFindStart = -1;
		tokenFindEnd = -1;
		if(mainTokens[mc]==findTokens[0]){
			tokenFindStart = mc;
			int fc=0;
			int nc=0;
			for(nc=mc; fc<findTokens.length && nc<mainTokens.length; nc++){
				if(Character.isWhitespace(mainTokens[nc]))
					continue;
				if(mainTokens[nc]==findTokens[fc]){
					fc++;
				}else{
					break;
				}
			}
			if(fc==findTokens.length){
				tokenFindEnd = nc-1;
				break;
			}
		}
	}
	if(tokenFindStart>-1 && tokenFindEnd>-1 && tokenFindStart<=tokenFindEnd){
		return new int[] {tokenFindStart, tokenFindEnd+1};
	}else{
		return new int[] {-1,-1};
	}
}

public static BeanPart determineParentBeanpart(final BeanPart child){
	// FIRST - Try to figure out by the BDM parent-child relationships
	BeanPart[] brefs = child.getBackRefs();
	if(brefs!=null){
		for (int i = 0; i < brefs.length; i++) {
			if(brefs[i]!=null){
				Iterator children = brefs[i].getChildren();
				while (children.hasNext()) {
					BeanPart child1 = (BeanPart) children.next();
					if(child.equals(child1))
						return brefs[i];
				}
			}
		}
	}
	// SECOND - Try to find parent by parse tree 
	// (SWT - doesnt allocate parent-child when instantiaing)
	if(child.getEObject()!=null && child.getEObject() instanceof IJavaObjectInstance){
		List bps = getAllocationReferences(child);
		if(bps.size()>0)
			return (BeanPart) bps.get(bps.size()-1);
	}
	return null;
}
/**
 * This method will return a BeanPart instance that is related to a particular expOffset in a method.
 * 
 * 
 * @param model
 * @param name
 * @param m method
 * @param expOffset offset withing the method
 * @return BeanPart if found, null if none
 * 
 * @since 1.1.0
 */
public static BeanPart getBeanPart (IBeanDeclModel model, String name, CodeMethodRef m, int expOffset) {
	BeanPartDecleration d = model.getModelDecleration(BeanPartDecleration.createDeclerationHandle(m,name));
	BeanPart b = null;
	if (d == null)
		d = model.getModelDecleration(BeanPartDecleration.createDeclerationHandle((CodeMethodRef)null,name));
	if (d!=null) {
		// instance variable decleration
	  	b = d.getBeanPart(m,expOffset);
		if (b==null) {
			// No bean found in the method with the given name from the offset specified
			// Currently we dont model arguments - check to see if it is a argument -
			boolean isArgument = false;
			String[] argNames = m.getArgumentNames();
			if(argNames!=null){
				for (int argCount = 0; argCount < argNames.length; argCount++) {
					if(name.equals(argNames[argCount])){
						isArgument = true;
						break;
					}
				}
			}
			if (!isArgument) {
					// It is possible that the instance is declared in a different method - like createComposite()
					BeanPart[] beans = d.getBeanParts();
					// TODO: we need to actually find the bean with an init method
					// that calls this method
					if (beans.length > 0)
						b = beans[0];
				}
		}
	}
	if(b!=null && !b.isActive())
		b.activate();
	return b;
}

/**
 * return EObject references this object makes in his allocation (contructor),
 * by traversing the allocation parsed tree.
 * 
 * @param o root object 
 * 
 * @since 1.1.0
 */
public static Collection getReferences(Object o, boolean includeO) {
	final Collection refs = new ArrayList();
	if (o!=null && o instanceof IJavaObjectInstance) {
		if (includeO)
			refs.add(o);
	  JavaAllocation alloc = ((IJavaObjectInstance)o).getAllocation();
	  if (alloc instanceof ParseTreeAllocation) {
		ParseTreeAllocation ptAlloc = (ParseTreeAllocation) alloc;
		ParseVisitor visitor = new ParseVisitor() {
			public boolean visit(PTInstanceReference node) {
				IJavaInstance r = node.getReference();				
				refs.addAll(getReferences(r,true));	
				return false;
			}
		};
		ptAlloc.getExpression().accept(visitor);
	  }
	}
	return refs;
}

	/**
	 * Used by {@link FreeFormAnnoationDecoder} to see if there is a need for a freeform annotation
	 * in the code for the beanpart. It is called for a beanpart that is being put on the freeform by
	 * a user, but there was no visual constraint supplied. It must already be assigned
	 * within the freeform or "thisPart" in the composition so that it can be determined
	 * if floating annotation needed. This method will return if a freeform
	 * floating annotation is required. If it was required and we didn't put one on, then the next 
	 * reparse would remove it from the freeform. It is determined to be required by the following
	 * criteria all be met:
	 * 
	 * <ol>
	 * <li>Simple name not starting with <code>ivj</code>
	 * <li>Not <code>Modeled</code>
	 * </ol>
	 * <p>
	 * If the simple name did start with "ivj" or it was "modeled" then it would automatically be
	 * added to the freeform on a reparse. So in that case a freeform annotation is not needed.
	 * Otherwise a freeform annotation is needed to maintain being on the freeform when reparsed.
	 * 
	 * @param bp the beanpart. This beanpart must be one that is being added to the freeform by the user. It must already 
	 * be assigned to either the "thisPart" or the freeform. If not then it will return <code>false</code>.
	 * @return <code>true</code> if a floating freeform annotation is needed, <code>false</code> otherwise.
	 * 
	 * @see #addBeanToBSC(BeanPart, BeanSubclassComposition, boolean) if that is changed then this will need to be changed.
	 * @since 1.2.0
	 */
	public static boolean needFreeFormAnnotation(BeanPart bp) {
		IBeanDeclModel model = bp.getModel();
		EObject bean = bp.getEObject();
		BeanSubclassComposition bsc = model.getCompositionModel().getModelRoot();
		if (bsc.getThisPart() == bean)
			return false;	// No annotation needed for this part bean.
		if (!bsc.getComponents().contains(bean))
			return false;	// Not a freeform component, so no annotation.
		return !bp.getSimpleName().toLowerCase().startsWith("ivj") && !InstanceVariableCreationRule.isModelled(bean.eClass(), model.getCompositionModel().getModelResourceSet()); //$NON-NLS-1$
	}
	
	/**
	 * Adds the bean part to the model. The added bean part is put on the freeform only if it is either
	 * <ol>
	 * <li> <code>THIS</code>
	 * <li> Simple name starts with <code>ivj</code>
	 * <li> <code>Modelled</code>, has no container <code>EObject</code>, EITHER has no codegen annotation OR has <code>visual-constraint</code> in the codegen annotation, AND doesnt have implicit-allocation.
	 * <li> Not <code>Modelled</code>, has no container <code>EObject</code>, and has <code>visual-constraint</code> in the codegen annotation
	 * </ol>
	 * 
	 * @param bp Bean to be added.
	 * @param bsc  Freeform to where it should be added.
	 * @param checkForExisting  When true checks for existence of the objects in model before adding
	 * @throws CodeGenException
	 * @see #needFreeFormAnnotation(BeanPart) if addBeanToBSC is changed then needFreeFormAnnoation should be changed.
	 * @since 1.1
	 */
	public static void addBeanToBSC(BeanPart bp, BeanSubclassComposition bsc, boolean checkForExisting) throws CodeGenException {
		boolean thisPart = bp.getSimpleName().equals(BeanPart.THIS_NAME) ? true : false;

		/** 
		 * if this method is changed, please modify {@link #needFreeFormAnnotation(BeanPart)} too
		 */
		if(checkForExisting){
			if (!bp.isInJVEModel())
				bp.addToJVEModel();
		}else{
			bp.addToJVEModel();
		}
		
		if (thisPart) {
			if (bsc.eIsSet(JCMPackage.eINSTANCE.getBeanSubclassComposition_ThisPart())){
				if(!checkForExisting) 
					throw new CodeGenException("this Already initialized"); //$NON-NLS-1$
			}
			if(checkForExisting){
				if (!bsc.eIsSet(JCMPackage.eINSTANCE.getBeanSubclassComposition_ThisPart())){
					bsc.setThisPart((IJavaObjectInstance) bp.getEObject());
				}
			}else{
				bsc.setThisPart((IJavaObjectInstance) bp.getEObject());
			}
		} else if (bp.getEObject() != null) {
			boolean shouldBeOnFreeform = false;
			if(bp.getSimpleName().toLowerCase().startsWith("ivj")){ //$NON-NLS-1$
				// Compatibility with ivj..
				if(bp.getContainer()==null)
					shouldBeOnFreeform = true;
			}else if (InstanceVariableCreationRule.isModelled(bp.getEObject().eClass(), bp.getModel().getCompositionModel().getModelResourceSet())) {
				// Is modelled
				if (bp.getContainer() == null){
					if(bp.getFFDecoder().getAnnotationComment()==null || bp.getFFDecoder().isVisualOnFreeform())
						if (bp.getEObject() instanceof IJavaObjectInstance) {
							IJavaObjectInstance bpInstance = (IJavaObjectInstance) bp.getEObject();
							shouldBeOnFreeform = !bpInstance.isImplicitAllocation();
						}else
						shouldBeOnFreeform = true;
				}
			} else {
				// Not modelled
				// does it have visual-constraint="x,y" annotation?
				if (bp.getContainer()==null){
					if(bp.getFFDecoder().isVisualOnFreeform()){
						shouldBeOnFreeform = true;
					}
				}
			}
			if (shouldBeOnFreeform) {
				if (!bsc.getComponents().contains(bp.getEObject()))
					bsc.getComponents().add(bp.getEObject());
			} else {
				if (bsc.getComponents().contains(bp.getEObject()))
					bsc.getComponents().remove(bp.getEObject());
			}
		}
	}

	/**
	 * Returns a list of beanparts which the passed in beanpart's 
	 * allocation references.
	 * 
	 * @param fbeanPart
	 * @return list of beanparts. Returns empty list if none.
	 * 
	 * @since 1.1
	 */
	public static List getAllocationReferences(final BeanPart fbeanPart) {
		final List bps = new ArrayList();
		if(fbeanPart.getEObject()!=null && fbeanPart.getEObject() instanceof IJavaObjectInstance){
			IJavaObjectInstance jo = (IJavaObjectInstance) fbeanPart.getEObject();
			JavaAllocation ja = jo.getAllocation();
			if (ja != null) {
				if (ja.isParseTree()) {
					ParseTreeAllocation pta = (ParseTreeAllocation) ja;
					PTExpression expression = pta.getExpression();
					NaiveExpressionFlattener bpFinder = new NaiveExpressionFlattener() {

						public boolean visit(PTInstanceReference node) {
							IJavaInstance obj = node.getReference();
							BeanPart bp = fbeanPart.getModel().getABean(obj);
							if (bp != null){
								bps.add(bp);
								while(bp!=null && bp.getDecleration().isImplicitDecleration()){
									bp = bp.getImplicitParent();
									bps.add(0, bp); // #determineParentBeanPart() always expects last child to be the parent
								}
							} else {
								if (obj != null && obj.isParseTreeAllocation()) {
									ParseTreeAllocation alloc = (ParseTreeAllocation) obj.getAllocation();
									alloc.getExpression().accept(this);
								}
							}
							return super.visit(node);
						}
					};
					expression.accept(bpFinder);
				} else if (ja.isImplicit()) {
					// We need to get the parent from the implicit parent
					// e.g., TreeViewer.getTree()'s parent is the TreeViewer's constructor
					EObject pObj = ((ImplicitAllocation) ja).getParent();
					BeanPart bp = fbeanPart.getModel().getABean(pObj);
					return getAllocationReferences(bp);
				}
			}
		}
		return bps;
	}

	/**
	 * Marks relevent expressions in the passed BDM if they are on the same line
	 * with the CodeExpressionRef.STATE_SHARED_LINE. This method should 
	 * typically be called immediately after the BDM is built so that all the method
	 * contents are accurate, as required by ExpressionParser.getExpressionsOnSameLine()
	 * 
	 * @param bdm
	 * 
	 * @since 1.1.0.1
	 */
	public static void markSameLineExpressions(IBeanDeclModel bdm){
		if (bdm==null) 
			return;
		Iterator methodItr = bdm.getAllMethods();
		while (methodItr.hasNext()) {
			CodeMethodRef method = (CodeMethodRef) methodItr.next();
			List sameLineExpressions = new ArrayList();
			Iterator expItr = method.getAllExpressions();
			while (expItr.hasNext()) {
				CodeExpressionRef exp = (CodeExpressionRef) expItr.next();
				if(exp.isStateSet(CodeExpressionRef.STATE_NO_SRC))
					continue; // no source - no need to check for same line
				if(sameLineExpressions.contains(exp))
					continue; // exp is already on the same line as someone else
				List expsOnSameLine = ExpressionParser.getExpressionsOnSameLine(exp, method);
				if(expsOnSameLine.size()>1){
					for (int expCount = 0; expCount < expsOnSameLine.size(); expCount++) {
						CodeExpressionRef sameLineExp = (CodeExpressionRef) expsOnSameLine.get(expCount);
						sameLineExp.setState(CodeExpressionRef.STATE_SHARED_LINE, true);
						sameLineExp.setSameLineExpressions(expsOnSameLine);
						sameLineExpressions.add(sameLineExp); // no need to visit other same line expressions again
					}
				}else{
					exp.setState(CodeExpressionRef.STATE_SHARED_LINE, false);
					exp.setSameLineExpressions(null);
				}
			}
		}
	}
	
	public static String getTypeName(Type type){
		String typeName = null;
		if(type!=null){
			if(type.isSimpleType()){
				typeName = ((SimpleType)type).getName().getFullyQualifiedName();
			}else if(type.isQualifiedType()){
				typeName = ((QualifiedType)type).getName().getFullyQualifiedName();
			}else if(type.isArrayType()){
				typeName = getTypeName(((ArrayType)type).getElementType());
			}else if(type.isPrimitiveType()){
				typeName = ((PrimitiveType)type).getPrimitiveTypeCode().toString();
			}
		}
		return typeName;
	}
}



