package org.eclipse.ve.internal.java.codegen.util;
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
 *  $RCSfile: CodeGenUtil.java,v $
 *  $Revision: 1.7 $  $Date: 2004-01-23 21:04:08 $ 
 */



import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IFileEditorInput;

import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaRefFactory;

import org.eclipse.ve.internal.cdm.*;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;

import org.eclipse.ve.internal.jcm.MemberContainer;

import org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance;
import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.CodeMethodRef;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.vce.rules.IEditorStyle;
import org.eclipse.ve.internal.java.vce.rules.VCEPostSetCommand;


/**
 *  Assortments of utilities for Code Generation
 */

public class CodeGenUtil {
		

/**
 *  Extract the end resulted write method or public field access.
 */	
static public String getWriteMethod(Statement expr)	{
   if (expr == null) return null ;
   // TODO  Must deal with non Literal also
   if (expr instanceof MessageSend)
       return new String (((MessageSend)expr).selector) ;
   else if (expr instanceof Assignment) {
       // public field access, e.g., gridBagConstraints.gridx = 0 ;
       if (((Assignment)expr).lhs instanceof QualifiedNameReference) {
           char[][] tokens = ((QualifiedNameReference)((Assignment)expr).lhs).tokens ;
          return new String (tokens[tokens.length-1]) ;
       }
	   else if (((Assignment)expr).lhs instanceof SingleNameReference) {
		 SingleNameReference lhs = (SingleNameReference) ((Assignment)expr).lhs ;
		 return new String(lhs.token) ;
	}
   }
   else if (expr instanceof LocalDeclaration) {
       TypeReference tr = ((LocalDeclaration)expr).type ;
       if (tr instanceof QualifiedTypeReference)
           return "new " + tokensToString(((QualifiedTypeReference)tr).tokens) ; //$NON-NLS-1$
       else {
           // TODO  Need to resolve
           return "new" ; //$NON-NLS-1$
       }
   }
   return null ;
}

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
	if(Character.isJavaIdentifierPart(main.charAt(index)))
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
public static IJavaInstance createInstance(String instanceType, IDiagramModelInstance cm) throws CodeGenException {
	if (cm.getModelResourceSet() == null)
		throw new CodeGenException("MOF is not set up"); //$NON-NLS-1$
	JavaVEPlugin.log("CodeGenUtil.createInstance(" + instanceType + ")", MsgLogger.LOG_FINE); //$NON-NLS-1$ //$NON-NLS-2$

	return createInstance(instanceType, cm.getModelResourceSet());
}

public static EClassifier getMetaClass (String qualifiedName, IDiagramModelInstance cm) {
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
	IDiagramModelInstance cm)
	throws Exception {

	IJavaInstance value = null;
	CodeGenUtil.propertyCleanup(target, sf);
	if (initVal != null) {
		value = createInstance("java.lang.String", cm); //$NON-NLS-1$
		value.setAllocation(InstantiationFactory.eINSTANCE.createInitStringAllocation(initVal));
		pOwner.getProperties().add(value);
	}
	target.eSet(sf, value);
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
		return types.length > 0 ? types[0] : null;
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
/**
 *   CU's content might have changed, and an old method is stale.
 */
public static IMethod refreshMethod(IMethod method) {
	if (method == null) return method ;
	
	ICompilationUnit cu = method.getCompilationUnit() ;
	try {
	  if (!cu.isConsistent()) cu.reconcile() ;
	}
	catch (JavaModelException e) {}
	
	IType t = CodeGenUtil.getMainType(cu) ;
	return getMethod(t,method) ;
}

public static IMethod refreshMethod(String handle, ICompilationUnit cu) {
	if (handle == null) return null ;
	try {
	  if (!cu.isConsistent()) cu.reconcile() ;
	}
	catch (JavaModelException e) {}
	return getMethod(getMainType(cu),handle) ;
}

public static IField getField (String handle, ICompilationUnit cu) {
	if (handle == null) return null ;
	try {
	    if (!cu.isConsistent()) cu.reconcile() ;
		
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
	    if (!cu.isConsistent()) cu.reconcile() ;
		
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
public static boolean isComponentInComposition (IBeanDeclModel m, EObject component, IDiagramModelInstance model) throws CodeGenException {
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
   
   IExpressionDecoder decoder = getDecoderFactory(model).getExpDecoder((IJavaObjectInstance)parent) ;
   Iterator itr = decoder.getChildren((IJavaObjectInstance)parent).iterator() ;
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
public static IJavaObjectInstance getParent (IBeanDeclModel bmodel, IJavaObjectInstance child, IDiagramModelInstance model) throws CodeGenException {
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
   return  target.eClass().getEStructuralFeature("allocation") ;       //$NON-NLS-1$
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
		boolean startAllright = index==0 || (index>0 && !Character.isJavaIdentifierPart(searchIn.charAt(index-1)));
		boolean endAllright = indexEnd==searchIn.length() || (indexEnd<searchIn.length()&&!Character.isJavaIdentifierPart(searchIn.charAt(indexEnd+1)));
		if(startAllright && endAllright)
			return index;
	}
	return -1;
}

public static String getInitString(IJavaInstance javaInstance) {
	JavaAllocation alloc = javaInstance.getAllocation();
	if (alloc instanceof InitStringAllocation)
		return ((InitStringAllocation) alloc).getInitString();
	else if (alloc instanceof ParseTreeAllocation) {
		return ((ParseTreeAllocation) alloc).getExpression().toString();	// TODO For now the Expression.toString() will give us an init string.
	} else {
		// There is none, so let's create the default ctor (only valid for classes)
		// Return construct with default ctor when not explicitly set.
		JavaHelpers jc = javaInstance.getJavaType();
		if (!jc.isPrimitive()) { 
			String qn = jc.getQualifiedName();
			return "new " + qn + "()";
		} else
			return "";	// Shouldn't get here for prims. They should have an allocation.
	}
}
public static String getResolvedInitString(Statement stmt, ITypeResolver resolver){
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
public static void snoozeAlarm(EObject obj, ResourceSet rs) {
	
	// Should be in the model
    if (obj != null && obj.eContainer() != null) {
    	// This adapter will maintain all references but no the container
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
						snoozeAlarm(parent,rs) ;	
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
/**
 * Generate a simple working copy provider, using the Compilation Unit as a reference.
 * This provider will only a small set of APIs.
 */
public static IWorkingCopyProvider getRefWorkingCopyProvider(ICompilationUnit refCU) {
	        final ICompilationUnit cu = refCU ;
	  	    IWorkingCopyProvider wcp = new IWorkingCopyProvider(){
	  	    	CodegenTypeResolver resolver = new CodegenTypeResolver(getMainType(cu));
	  	    		  	    		  	    	

				public String resolve(String unresolved){
					try{
						if(resolver==null)
							return unresolved;
						else
							return resolver.resolveTypeComplex(unresolved);
					}catch(Exception e){
						return unresolved;
					}
				}
				public String resolveThis(){
					return getMainType(cu).getTypeQualifiedName();
				}
		        public ITypeHierarchy getHierarchy() {
		          try {
		            IType t = getMainType(cu) ;
		            return t.newSupertypeHierarchy(null) ;
		          }
		          catch (org.eclipse.jdt.core.JavaModelException e) {}
		          return null ;
		        }
		        public ICompilationUnit getWorkingCopy(boolean forceReconcile) { return cu; }
		        public IFileEditorInput getEditor() { return null; }
		        public IFile getFile() { return null; }
		        public IDocument getDocument() { return null; }
		        public Object    getDocLock() { return null; }
		        public void disconnect() {}
		        public void connect(IFile file) {}
		        public ISourceRange getSourceRange(String handle) { return null; }
		        public int getLineNo(int Offset) { return -1; }
		        public void dispose() {}
		        public IJavaElement getElement(String handle) { return null; }

	 	};
	 	return wcp ;
}
public static void clearCache () {
	
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
 * If the target has a setting on the given sf, and the setting is owned by the properties MemberContainer
 * remove it.
 */
public static boolean propertyCleanup(EObject target, EStructuralFeature sf) { 
   boolean result = false ;
   if (target.eIsSet(sf)) {
   	 EObject p = (EObject) target.eGet(sf) ;
   	 if (p!=null && p.eContainer() instanceof MemberContainer) {
   	 	MemberContainer c = (MemberContainer)p.eContainer() ;
   	 	result = c.getProperties().contains(p) ;
   	 	c.getProperties().remove(p) ;
   	 }
   }
   return result ;
}

public static void logParsingError(String exp, String method, String msg, boolean event) {
	String context ;
	if (event)
	   context = "as an event registration" ; //$NON-NLS-1$
	else
	   context = "as a property setting" ; //$NON-NLS-1$
	JavaVEPlugin.log ("\n/**\n Could not parse the following expression "+context+":\n\t"+method+"(): "+exp+"\n\treason: "+msg+"\n**/\n",MsgLogger.LOG_FINE) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
}

public static String expressionToString (Expression exp) {
	StringBuffer sb = new StringBuffer() ;
	exp.printExpression(4,sb) ;
	return sb.toString();	
}

}



