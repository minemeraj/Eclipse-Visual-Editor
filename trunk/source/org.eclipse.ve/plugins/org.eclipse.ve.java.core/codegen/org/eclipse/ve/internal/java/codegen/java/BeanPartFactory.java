package org.eclipse.ve.internal.java.codegen.java;
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
 *  $RCSfile: BeanPartFactory.java,v $
 *  $Revision: 1.21 $  $Date: 2004-04-05 03:38:38 $ 
 */

import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.*;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.*;

import org.eclipse.ve.internal.cdm.Annotation;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator;
import org.eclipse.ve.internal.cde.emf.*;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;

import org.eclipse.ve.internal.jcm.*;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.rules.IInstanceVariableCreationRule;
import org.eclipse.ve.internal.java.codegen.java.rules.IThisReferenceRule;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;




public class BeanPartFactory  {
	
	public final String[]       INSTANCE_VAR_DEFAULT_COMMENT = {"Generated"} ; //$NON-NLS-1$
			
	IBeanDeclModel            fBeanModel ;
	IVEModelInstance     fCompositionModel ;
	List                      fTotalGeneratedList = new ArrayList() ;
	private IInstanceVariableCreationRule fRule = null ;
		
	

public BeanPartFactory (IBeanDeclModel bmodel,IVEModelInstance cmodel) {
	fBeanModel = bmodel ;
	fCompositionModel = cmodel ;
}

/**
 *  Will create the BDM entries associated with the component and its children.
 */


protected IInstanceVariableCreationRule getVarRule() {
    if (fRule != null) return fRule ;
    fRule = (IInstanceVariableCreationRule) CodeGenUtil.getEditorStyle(fBeanModel).getRule(IInstanceVariableCreationRule.RULE_ID) ;
    return fRule ;
}


/**
 * @deprecated
 */
protected void  generateInLineBean (IJavaObjectInstance component, CodeMethodRef mref, String varName,  List kids, ICompilationUnit cu) throws CodeGenException {
    
    IMethodTextGenerator mgen = CodeGenUtil.getMethodTextFactory(fBeanModel).getMethodGenerator(component,fBeanModel);
    mgen.generateInLine(mref,varName,kids) ;
    
    
}

 protected void  generateLocalVariable (IJavaObjectInstance component, CodeMethodRef mref, String varName,  ICompilationUnit cu) throws CodeGenException {
    
    IMethodTextGenerator mgen = CodeGenUtil.getMethodTextFactory(fBeanModel).getMethodGenerator(component,fBeanModel) ;
    mgen.generateInLine(mref,varName,new ArrayList()) ;
    
    
}

protected void setFreeFormAnnotation(InstanceVariableTemplate ft,BeanPart bean) throws CodeGenException{
    
    String FFtext = bean.getFFDecoder().generate(null,null) ;    
    if (FFtext != null && FFtext.length() > 0)
       ft.setComment(FFtext) ;    
    else
       ft.setComment(null) ;
}

/**
 * This method is a temporary workaround.
 */
public static void fixOffsetIfNeeded(String methodSrc, CodeMethodRef mref) throws CodeGenException {
	
   if (mref == null) return ;
   if (methodSrc == null) throw new CodeGenException("No Source") ; //$NON-NLS-1$
   	
   Iterator iter = mref.getExpressions() ;
   while (iter.hasNext()) {
    CodeExpressionRef exp = (CodeExpressionRef) iter.next();
    int index = methodSrc.indexOf(exp.getContentParser().getExpression(false)) ;
    if (index < 0) {
    	// It is possible that a filler was changed, with syntax error,, in this case plan B works, but where
    	// does it starts  e.g.   .....xxx...<expression>,  where xxx was added to the filler.
    	JavaVEPlugin.log("BeanPartGenerator.fixOffsetIfNeeded(): Can not find expression in method:\n\t"+exp, Level.FINE) ;    //$NON-NLS-1$
    	continue ;
    }
    if (exp.getOffset() != index) {
        JavaVEPlugin.log("BeanPartGenerator.fixOffsetsIfNeeded(): Fixing expression offset: "+exp) ; //$NON-NLS-1$
        exp.setOffset(index) ;
    }    
   }		
}

private void fixOffsetsIfNeeded(IMethod imethod, CodeMethodRef mref) throws CodeGenException {

   String src ;
   try { 
       src = imethod.getSource() ;
   } catch(JavaModelException e) {
       throw new CodeGenException(e) ;
   }

   fixOffsetIfNeeded(src,mref) ;        
}
protected IJavaElement getSiblingForNewMEthod(IType type, boolean isConstructor, boolean topMost){
	IMethod sibling = null;
	try {
		IMethod[] methods = type.getMethods();
		if(methods==null || methods.length<1)
			return null;
		if(isConstructor){
			sibling = methods[0];
		}else{
			if(topMost){
				for(int sc=0;sc<methods.length;sc++){
					if(methods[sc].isConstructor())
						continue;
					sibling = methods[sc];
					break;
				}
			}
		}
	} catch (JavaModelException e) {
		JavaVEPlugin.log(e, Level.FINE);
	}
	return sibling;
}


/**
 *  Init expression was already created with the method template,
 *  This method will parse and add a CodeExpRef to the BDM
 */
protected void parseInitExpression (BeanPart b) {
	ExpressionRefFactory f = new ExpressionRefFactory(b, null) ;
	CodeExpressionRef exp = f.parseInitExpression() ;
	exp.setState(CodeExpressionRef.STATE_INIT_EXPR,true);
}

/**
 *  This method will use the allocation feature to generate
 *  a new init expression from the model
 */
protected CodeExpressionRef createInitExpression(BeanPart bp, IJavaObjectInstance component) throws CodeGenException {
    // Use the allocation feature to generate from the model
	ExpressionRefFactory f = new ExpressionRefFactory(bp,ObjectDecoder.getAllocationFeature(component));
	CodeExpressionRef exp = f.createFromJVEModel(null) ;
	exp.setState(CodeExpressionRef.STATE_INIT_EXPR,true);
	exp.insertContentToDocument();
    return exp;
}

protected void generateInitMethod(BeanPart bp, IJavaObjectInstance component, CodeMethodRef mref, String methodName,  ICompilationUnit cu) throws CodeGenException {

 IMethodTextGenerator mgen = CodeGenUtil.getMethodTextFactory(fBeanModel).getMethodGenerator(component, fBeanModel);

		if (!mref.isGenerationRequired()) {
			// Init method is already there, just create the constructor
			fBeanModel.refreshMethods(); // If we created a field offset may have changed
			createInitExpression(bp, component);
			// Generate the rest of the expressions in the case that component has been set
			// with features already
			mgen.generateExpressionsContent();
		} else {

			IType cuType = CodeGenUtil.getMainType(cu);
			String newMSrc = mgen.generateMethod(mref, methodName, bp.getSimpleName());

			IMethod newMethod = null;
			// Create it as the last method
			try {
				// Offsets will be updated with a call to refreshMethods on mref
				newMethod = cuType.createMethod(newMSrc, getSiblingForNewMEthod(cuType, false, false), false, null);
				mref.setMethodHandle(newMethod.getHandleIdentifier());
				// Need to set the source overhere, so that we can parse the init expression
				mref.setContent(newMethod.getSource());
				fBeanModel.addMethodInitializingABean(mref);
			} catch (JavaModelException e) {
				JavaVEPlugin.log(e, Level.WARNING);
				throw new CodeGenException(e);
			}
			// template also created the init expression; e.g., new Foo()
			parseInitExpression(bp);
			JavaVEPlugin.log("Adding JCMMethod: \n" + newMSrc + "\n", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$    
			CodeGenUtil.refreshMethodOffsets(cuType, fBeanModel);
			// Workaround, as the create method may create a method which include other
			// comments,etc.
			fixOffsetsIfNeeded(newMethod, mref);
			mref.setGenerationRequired(false);
			mgen.generateExpressionsContent();
		}
	}

/**
 * Generates an instance variable decleration.
 */
protected void generateInstanceDecleration(BeanPart bp, IJavaObjectInstance component, String varName, ICompilationUnit cu) throws CodeGenException {

	IType cuType = CodeGenUtil.getMainType(cu);

	InstanceVariableTemplate ft = new InstanceVariableTemplate(varName, ((IJavaObjectInstance) component).getJavaType().getQualifiedName(), INSTANCE_VAR_DEFAULT_COMMENT);
	// Create it as the last field
	ft.setSeperator(fBeanModel.getLineSeperator());
	setFreeFormAnnotation(ft, bp);
	IField field = null;
	try {
		field = cuType.createField(ft.toString(), null, false, null);
		if (field != null) {
			bp.setFieldDeclHandle(field.getHandleIdentifier());
			// This may be an overkill, as the refreshMethods() will be called eventually.
			ISourceRange sr = field.getSourceRange();
			fBeanModel.driveExpressionChangedEvent(null,sr.getOffset(),sr.getLength()) ;
		}

	}
	catch (JavaModelException e) {
		throw new CodeGenException(e);
	}
	JavaVEPlugin.log("Adding Instance Var: \n" + ft + "\n", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
}
/**
 * The null constructor typically will call the initialization method.  The exception
 * is for Applet, where the init method is called by the container.
 */
protected boolean isNeedToCallInit(BeanPart b) {

    JavaHelpers AppletType = JavaRefFactory.eINSTANCE.reflectType("java.applet.Applet",b.getEObject()) ; //$NON-NLS-1$
	return !AppletType.isAssignableFrom(
	                    (EClassifier) ((IJavaObjectInstance) b.getEObject()).getJavaType()) ;
}

protected void generateNullConstructorIfNeeded(BeanPart b, CodeMethodRef iniMethod) {
    
    IType t = CodeGenUtil.getMainType(fBeanModel.getCompilationUnit()) ;
    // TODO  Need to parse here
    IMethod firstM = null ;
    try {
		IMethod[] mtds = t.getMethods() ;
	    IMethod  nullConstructor = null ;
	    for (int i = 0; i < mtds.length; i++) {	        
	        IMethod method = mtds[i];
	        if (firstM == null) firstM = method ;
	        if (method.isConstructor()) {
//	            String src = method.getSource() ;
//	            CDEHack.fixMe("asap") ; //$NON-NLS-1$
//	            if (src.indexOf(iniMethod.getMethodName()) >= 0)
//	               return ; // already there
	            if (method.getNumberOfParameters() == 0) {
	                // Found a null constructor
	                nullConstructor = method ;
	                break ;
	            }
	        }
	        
	    }
	    if (nullConstructor != null) {
	        // Update existing method
	        
	        // Is the bean an instance of Applet ??? In this case we do not want to call 
            // the init method.
            
			if (isNeedToCallInit(b)) {
				MethodParser mp = new MethodParser(nullConstructor, fBeanModel.getLineSeperator());
				mp.addMethodCallIfNeeded(iniMethod.getMethodName());
			}
        }
        else { // create one
            String name = fBeanModel.getCompilationUnit().getOriginalElement().getCorrespondingResource().getName() ;
            name = name.substring(0,name.indexOf(fBeanModel.getCompilationUnit().getOriginalElement().getCorrespondingResource().getFileExtension())-1) ;
            NullConstructorTemplate template = new NullConstructorTemplate(null,
  													   b.getSimpleName(),
                                                       name,     
                                                       null) ;    
            template.setSeperator(fBeanModel.getLineSeperator()) ;   
            // A null constructor may not be suffience.  check to see
            // if there is any Creation Policy that overides the default5
            JavaClass clazz = (JavaClass)((IJavaObjectInstance)b.getEObject()).getJavaType() ;
            if (clazz.getSupertype() != null) {
                EMFCreationTool.CreationPolicy  policy = null ;
                JavaClass superClazz = clazz.getSupertype();
                ClassDescriptorDecorator decorator = (ClassDescriptorDecorator)ClassDecoratorFeatureAccess.getDecoratorWithKeyedFeature(superClazz, 
                                                                           ClassDescriptorDecorator.class, 
                                                                           EMFCreationTool.CREATION_POLICY_KEY);            
                if ( decorator != null ) {
                    String creationPolicyClassName = (String)decorator.getKeyedValues().get(EMFCreationTool.CREATION_POLICY_KEY);
                    // The class name may be from another plugin so we must instantiate it correctly
                    try {
                        policy = (EMFCreationTool.CreationPolicy)CDEPlugin.createInstance(null, creationPolicyClassName);
                    } catch ( Exception exc ) {
                        // If the class can't be created then just drop down and let the regular command be returned
                    }
                    if (policy != null) {
                        String superOveride = policy.getDefaultSuperString((EClass)superClazz) ;
                        if (superOveride != null)
                           template.setSuperInitString(superOveride+";") ; //$NON-NLS-1$
                    }
                       
                }
                
            }
            String callInit="" ; //$NON-NLS-1$
            // Is the bean an instance of Applet ??? In this case we do not want to call 
            // the init method.            
            if (isNeedToCallInit(b)) {
            	callInit=iniMethod.getMethodName()+"();" ; //$NON-NLS-1$
            }
            
            
            String newSrc = template.getPrefix()+
                            NullConstructorTemplate.getInitExprFiller()+
                            callInit +
                            template.getPostfix();
                            
            t.createMethod(newSrc, firstM, false, null) ;              
        }
	} catch(JavaModelException e) {}
	
}

protected CodeMethodRef generateThisInitMethod() throws CodeGenException {

   IType cuType = CodeGenUtil.getMainType(fBeanModel.getCompilationUnit()) ;
   BeanPart bp = fBeanModel.getABean(BeanPart.THIS_NAME) ;
   if (fBeanModel == null) throw new CodeGenException ("No this BeanPart") ; //$NON-NLS-1$
   
   CodeMethodRef mref = bp.getInitMethod() ;
   if (mref != null) return mref ;
   
   IThisReferenceRule thisRule = (IThisReferenceRule) CodeGenUtil.getEditorStyle(fBeanModel).getRule(IThisReferenceRule.RULE_ID) ;
   String [] mNameModifier = thisRule.getThisInitMethodName(fBeanModel.getClassHierarchy()) ;
   mref = new CodeMethodRef(fBeanModel.getTypeRef(),mNameModifier[0]) ;  
   mref.setModel(fBeanModel) ;
      
   BeanMethodTemplate template = new BeanMethodTemplate("void", //$NON-NLS-1$
  													   bp.getSimpleName(),
                                                       mref.getMethodName(),     
                                                       null) ;    
   template.setSeperator(fBeanModel.getLineSeperator()) ;
   template.setThisMethod(true);
   template.setModifier(mNameModifier[1]) ;
   String newSrc = template.getPrefix()+template.getPostfix();
	
   
		    
   IMethod newMethod=null ;
   // Create it as the last method 
   try {        		
   		boolean isMethodAlreadyPresent = false;
   		IMethod[] methods = cuType.getMethods();
   		IMethod alreadyPresentMethod = null;
   		for(int i=0;i<methods.length;i++){
   			if(methods[i].getElementName().equals(mref.getMethodName()) &&
   			   methods[i].getParameterNames().length<1){ // Check if initialize() is present
   				isMethodAlreadyPresent = true;
   				alreadyPresentMethod = methods[i];
   				break;
   			}
   		}
   		if(isMethodAlreadyPresent){
   			newMethod = alreadyPresentMethod;
   			newSrc = newMethod.getSource();
   		}else{
	        newMethod = cuType.createMethod(newSrc,getSiblingForNewMEthod(cuType, false, true),false,null) ;    
   		}        
        mref.setMethodHandle(newMethod.getHandleIdentifier()) ;       
        // empty lines may shift to other methods
        mref.setContent(newMethod.getSource()) ;
//        if (newMSrc.length() != newMethod.getSource().length()) {
//             System.out.println ("JavaSourceTranslator.processAComponent(): newMethodSource("+newMSrc.length()+") JDOM("+newMethod.getSource().length()+")") ;
//        }
        fBeanModel.addMethodInitializingABean(mref) ;          
   }
   catch (JavaModelException e) {
     	 JavaVEPlugin.log(e, Level.WARNING) ;
    	 throw new CodeGenException(e) ;
   }    
   JavaVEPlugin.log("Adding \"this\" method: \n"+newSrc+"\n", Level.FINE) ;	 //$NON-NLS-1$ //$NON-NLS-2$
   //mref.setMethod(CodeGenUtil.refreshMethod(newMethod)) ;
   mref.refreshIMethod();
   
   bp.addInitMethod(mref) ;
   
   generateNullConstructorIfNeeded(bp,mref) ;
      
   try {
   	// If someone made a change to the document buffer, the IMethod's source 
   	// ranges will not reflect immediately - hence force it to do that.
	fBeanModel.getCompilationUnit().reconcile();
   } catch (JavaModelException e1) {
   		JavaVEPlugin.log(e1, Level.FINE);
   }
   
   CodeGenUtil.refreshMethodOffsets(cuType,fBeanModel) ;    
   
   return mref ;    
                
}

/**
 * This will return the method that initializes this component.
 * 
 * 
 */
protected JCMMethod getInitializingMethod(IJavaObjectInstance component) {
	
	EReference sf = JCMPackage.eINSTANCE.getJCMMethod_Initializes() ;
	JCMMethod m = (JCMMethod) InverseMaintenanceAdapter.getFirstReferencedBy(component,sf) ;
	return m ;
}
/**
 * This method is called when an instance is added to the JVE model.
 * It will only create the BeanPart, and generate the Instance Variable decleration
 * in the source.
 * 
 * Settings/JCMMethod generation will be constructed later
 * 
 */
public void createFromJVEModel(IJavaObjectInstance component, ICompilationUnit cu) throws CodeGenException {
		
			
      IType cuType = CodeGenUtil.getMainType(cu) ;
      String varName = getVarRule().getInstanceVariableName(component,cuType,fCompositionModel,fBeanModel) ;    
    
      BeanPart bp = fBeanModel.getABean(varName) ;
      if (bp != null) throw new CodeGenException ("BeanPart Already Exists") ; //$NON-NLS-1$
      
      // If new value doesnt have an annotation containing variable name, add one... the JavaBeans Viewer needs it.
     Annotation an = CodeGenUtil.getAnnotation(component);      
     if(an==null)
      	an = CodeGenUtil.addAnnotation(component);
     if(!an.getKeyedValues().containsKey(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY)){
     	CodeGenUtil.addAnnotatedName(an, varName);
     }
      
      // Set up a new BeanPart in the decleration Model
      String bType = ((IJavaObjectInstance)component).getJavaType().getQualifiedName() ;
      bp = new BeanPart (varName,bType) ;
      bp.setModel(fBeanModel) ;
      bp.setEObject(component) ;
      bp.setSettingProcessingRequired(true) ;
      bp.setIsInJVEModel(true) ;
            
      // Instance variable are always members of BSC
      boolean thisPart = false ;
      if (component.eContainer() instanceof BeanSubclassComposition) {
         if (component.equals(((BeanSubclassComposition)component.eContainer()).getThisPart()))
           thisPart=true ;
         else
           bp.setInstanceVar(true) ;
      }
      else
         bp.setInstanceVar(false) ;
        
      fBeanModel.addBean(bp) ;               
      
      MemberDecoderAdapter ma = null ;
      
      if (bp.isInstanceVar()) {
         generateInstanceDecleration(bp, component,varName, cu) ;
         
         // Generatate a skelaton method         
         JCMMethod m = getInitializingMethod(component) ;
         String methodName = m.getName() ;
         if (methodName == null) {
            methodName = getVarRule().getInstanceVariableMethodName(component,bp.getSimpleName(), cuType,fBeanModel) ;
            m.setName(methodName) ;
         }
         ma = (MemberDecoderAdapter) EcoreUtil.getExistingAdapter(m,ICodeGenAdapter.JVE_MEMBER_ADAPTER) ;
         CodeMethodRef mref = ma.getMethodRef() ;
         if (mref == null) {
         	 mref = new CodeMethodRef(fBeanModel.getTypeRef(),methodName,m) ;
             mref.setGenerationRequired(true) ;
             ma.setMethodRef(mref) ;
         }
         bp.addInitMethod(mref) ;
         if (m.getReturn() != null && m.getReturn().equals(component))
           bp.addReturnMethod(mref) ;         
         generateInitMethod(bp, component, mref, methodName, cu) ;
      }
      else if (thisPart) {
      	throw new CodeGenException("this part processing") ; //$NON-NLS-1$
      }
      else {      	       	 
      	 ma = (MemberDecoderAdapter) EcoreUtil.getExistingAdapter(component.eContainer(),ICodeGenAdapter.JVE_MEMBER_ADAPTER) ;
      	 CodeMethodRef mref = ma.getMethodRef() ;
      	 bp.addInitMethod(mref) ;
      	 bp.getFFDecoder();
         generateLocalVariable(component,ma.getMethodRef(),varName, cu) ;
      }
      fBeanModel.refreshMethods();
}


/**
 *  Remove the Bean from the model and document (including JCMMethod/Expr)
 * Operations:
 *  ++ If instance variable - remove field.
 *  ++ DO NOT Remove this beans init method if:
 * 	    ++ If another bean X in the init method is an 
 *         instance variable.
 *      ++ If another bean X in the init method is NOT 
 *         an instance variable BUT is being returned.
 *      ALSO::
 *      ++ If another bean X is a child of the bean being 
 *         deleted - then delete X irrespective of it 
 *         being an instance variable.
 *  
 */
public void removeBeanPart (BeanPart bean) {
	boolean jdtChangesMade = false ; // MethodRef offsets not being updated - hence check.
	IType tp = CodeGenUtil.getMainType(fBeanModel.getCompilationUnit()) ;
	if (bean.isInstanceVar()) { 	  
	  IField f = tp.getField(bean.getSimpleName()) ;
	  if (f != null) {		// delete the field
		try {
		  JavaVEPlugin.log("\tRemoving Field: "+f, Level.FINE) ; //$NON-NLS-1$
//		  String handle = (f.getHandleIdentifier()) ;
		  f.delete(true,null) ;
		  jdtChangesMade = true;
		}
		catch (JavaModelException e) {} 
	  }
	  else 
	     JavaVEPlugin.log ("BeanPartGenerator.removeBeanPart: field is not in source: "+bean.getUniqueName()+"  <--- check me", Level.FINE) ; //$NON-NLS-1$ //$NON-NLS-2$
	}
	 
	// TODO  Need to maintain Bean Ref Count in method, and remove it when the last ref. is gone
	CodeMethodRef mr = bean.getInitMethod() ;
    if (mr == null) {
        org.eclipse.ve.internal.java.core.JavaVEPlugin.log("BeanPartGenerator.removeBeanPart() : No MethodRef for "+bean) ; //$NON-NLS-1$
    }
	// TODO  Instance Var. may share methods
	
// Delte should be done from the model.	
	Collection beansInMethod = mr==null?new ArrayList():fBeanModel.getBeansInitilizedByMethod(mr);
	List deleteDependentBeans = new ArrayList();
	Iterator bitr = beansInMethod.iterator();
//	while(bitr.hasNext()){
//		BeanPart bp = (BeanPart) bitr.next();
//		if(bp.equals(bean))
//			continue;
//		BeanPart parent = bp.getBackRefs();
//		while(parent!=null){
//			if(parent.equals(bean)){
//				deleteDependentBeans.add(bp);
//				break;
//			}
//			parent = parent.getBackRefs();
//		};
//	}
	
	boolean areOtherInstanceVariablesFound = false;
	boolean isAnyNonInstanceVariableBeingReturned = false;
	
	bitr = beansInMethod.iterator();
	while(bitr.hasNext()){
		BeanPart bp = (BeanPart) bitr.next();
		if(bp.equals(bean))
			continue;
		if(deleteDependentBeans.contains(bp))
			continue;
		if(bp.isInstanceVar())
			areOtherInstanceVariablesFound = true;
	}
	
	BeanPart returnedBean = mr==null?null:fBeanModel.getBeanReturned(mr.getMethodName());
	isAnyNonInstanceVariableBeingReturned = returnedBean==null||
											returnedBean.equals(bean)||
											deleteDependentBeans.contains(returnedBean)?false:returnedBean.isInstanceVar();
	
	boolean shouldMethodBeRemoved = mr!=null && !(areOtherInstanceVariablesFound || isAnyNonInstanceVariableBeingReturned);
	Iterator itr=null ;
	if (shouldMethodBeRemoved) {
		// a Seperate JCMMethod exist for this bean    	
		tp = CodeGenUtil.getMainType(fBeanModel.getCompilationUnit()); // Offsets have changed if we removed the field	
		try {
			if (mr != null) {
				itr = mr.getExpressions() ;
				IMethod m = CodeGenUtil.getMethod(tp, mr.getMethodHandle());
				String handle = mr.getMethodHandle();
				JavaVEPlugin.log("\tRemoving JCMMethod: " + handle, Level.FINE); //$NON-NLS-1$
				m.delete(true, null);
				jdtChangesMade = true;
			}
			else
				JavaVEPlugin.log("deleteBeanPart: method is not in source: " + bean.getUniqueName(), Level.FINE); //$NON-NLS-1$
		}
		catch (JavaModelException e) {}
	}
	else
	   itr = bean.getRefExpressions().iterator();
	
	// Changes made directly to JDT - the methods need to 
	// refresh their offsets and code got moved.
	if(jdtChangesMade){
		fBeanModel.refreshMethods();
	}
	
	ArrayList deleteList = new ArrayList();
	while (itr != null && itr.hasNext()) {
		CodeExpressionRef e = (CodeExpressionRef) itr.next();
		//e.primSetState(e.STATE_NOT_EXISTANT) ;
		e.clearState();
		e.setState(CodeExpressionRef.STATE_DELETE, true);
		deleteList.add(e);
	}
	// If the method is not removed, we need to remove the expressions
	if (!shouldMethodBeRemoved) {
	  // updateDocument will delete the expressions
	  fBeanModel.refreshMethods();
	  for (int i = deleteList.size() - 1; i >= 0; i--) {
		// If it is an instance bean, we removed the method
		((CodeExpressionRef) deleteList.get(i)).updateDocument(i == 0); // Update the source code once
	  }
	}
	else {
		// expressions will be disposed when mr is disposed.
	}

	
	
	

    if (mr != null)
	   bean.removeInitMethod(mr) ;
	
	if (shouldMethodBeRemoved) {
	  fBeanModel.removeMethodRef(mr) ;	
	  mr.dispose() ;		
	}
    fBeanModel.removeBean(bean) ;    		 	
    // Remove associated Decode Adapters
    // We do not call dispose here, as the bean was removed from the model already
    bean.setEObject(null) ;	
    bean.setIsInJVEModel(false) ;
}

public BeanPart createThisBeanPartIfNeeded(CodeMethodRef initMethod) {
     
     BeanPart bean = fBeanModel.getABean(BeanPart.THIS_NAME) ;
     
     if (bean == null) {    
       String tname = fBeanModel.resolveThis();
       bean = new BeanPart (tname) ;	  
       fBeanModel.addBean(bean) ;
     }
     if (initMethod != null)     
        bean.addInitMethod(initMethod) ;
     
     return bean ;	
}

/**
 * Return the expression that create an instance of this bean. i.e., Foo x = new Foo() ;
 */
public static CodeExpressionRef getInstanceInitializationExpr(BeanPart bp) {
    if (bp == null) return null ;
    for (Iterator itr = bp.getRefExpressions().iterator(); itr.hasNext();) {
        CodeExpressionRef exp = (CodeExpressionRef) itr.next();
        if (exp.isStateSet(CodeExpressionRef.STATE_INIT_EXPR)) {
//            if (exp.getExprStmt()!=null) {
//                Expression e=null ;
//                if (exp.getExprStmt() instanceof Assignment) {
//                     e = ((Assignment)exp.getExprStmt()).expression ;
//                }
//                else if (exp.getExprStmt() instanceof LocalDeclaration) {
//                     e = ((LocalDeclaration)exp.getExprStmt()).initialization ;
//                }
//                // If it is of type STAT_INI_EXPR .. this should be it.
//                if (e != null && (e instanceof AllocationExpression ||
//								   e instanceof ArrayAllocationExpression ||
//                                   e instanceof CastExpression ||
//                                   e instanceof MessageSend))
//                            return exp ;
//            }
        	// Time to open this up.... any init expression will go - we should build
        	// the proper Parsed tree for this
        	return exp;
        }       
    }
    return null ;
}

/**
 * Update the IJavaObjectInstance initialization string
 * @deprecated Constructor decoder should update allocation from now on
 */
public static void updateInstanceInitString(BeanPart bp) {
    IJavaObjectInstance obj = (IJavaObjectInstance)bp.getEObject() ;
    if (obj == null) return ;
    CodeExpressionRef exp = getInstanceInitializationExpr(bp) ;
    // if there is no constructor decoder, initialize the allocation directly from the code
    if (exp != null && exp.isStateSet(CodeExpressionRef.STATE_NO_MODEL)) {
    	// we should not be here anymore !!!!!!!!
    	JavaVEPlugin.log("BeanPartFactory.updateInstanceInitString(): Should not be here",Level.WARNING);    	
    }
}

}