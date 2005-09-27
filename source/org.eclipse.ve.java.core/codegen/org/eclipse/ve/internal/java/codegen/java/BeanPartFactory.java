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
 *  $RCSfile: BeanPartFactory.java,v $
 *  $Revision: 1.55 $  $Date: 2005-09-27 15:12:09 $ 
 */

import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.internal.instantiation.ImplicitAllocation;
import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.*;

import org.eclipse.ve.internal.cdm.Annotation;

import org.eclipse.ve.internal.cde.core.CDECreationTool;
import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator;
import org.eclipse.ve.internal.cde.emf.ClassDecoratorFeatureAccess;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;

import org.eclipse.ve.internal.jcm.*;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.rules.IInstanceVariableCreationRule;
import org.eclipse.ve.internal.java.codegen.java.rules.IThisReferenceRule;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.core.VECreationPolicy;




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
    	if (JavaVEPlugin.isLoggingLevel(Level.FINE))
    		JavaVEPlugin.log("BeanPartGenerator.fixOffsetIfNeeded(): Can not find expression in method:\n\t"+exp, Level.FINE) ;    //$NON-NLS-1$
    	continue ;
    }
    if (exp.getOffset() != index) {
    	if (JavaVEPlugin.isLoggingLevel(Level.FINEST))
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
protected IJavaElement getSiblingForNewMethod(IType type, boolean isConstructor, boolean topMost){
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
			else {
				// Vanila old method... make sure to put it before a main, if one exits.
				sibling = getMainMethod(type);
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
	 * This method will use the allocation feature to generate a new init
	 * expression from the model
	 */
	protected CodeExpressionRef createInitExpression(BeanPart bp,
			IJavaObjectInstance component) throws CodeGenException {
		// Use the allocation feature to generate from the model
		ExpressionRefFactory f = new ExpressionRefFactory(bp, ObjectDecoder
				.getAllocationFeature(component));
		CodeExpressionRef exp = f.createFromJVEModel(null);
		exp.setState(CodeExpressionRef.STATE_INIT_EXPR, true);
		exp.insertContentToDocument();
		if (!exp.getBean().getDecleration().isSingleDecleration())
			  exp.getBean().getDecleration().refreshDeclerationSource();
		return exp;
	}

   protected IMethod getMainMethod(IType t) {
		IMethod main = t.getMethod("main", new String[]{Signature //$NON-NLS-1$
				.createTypeSignature("String[]", false)}); //$NON-NLS-1$
		if (main != null && main.exists())
			return main;
		return null;
	}
	protected void generateMainIfNeeded(IType t, String content) throws JavaModelException {
		IMethod main = getMainMethod(t);
		if (main!=null) {
			try {
				// main already exists
				// See if there are any statement in it.
				String clazz = "class Foo {\n" + main.getSource() + "\n}"; //$NON-NLS-1$ //$NON-NLS-2$
				ASTParser parser = ASTParser.newParser(AST.JLS2);
				parser.setSource(clazz.toCharArray());
				parser.setSourceRange(0, clazz.length());
				parser.setKind(ASTParser.K_COMPILATION_UNIT);
				CompilationUnit ast = (CompilationUnit) parser.createAST(null);
				MethodDeclaration mt = ((TypeDeclaration) ast.types().get(0))
						.getMethods()[0];
				if (mt.getBody() != null && mt.getBody().statements().size() > 0)
					return; // already have statements in this method
				else
					main.delete(true,null); // recreate it
			} catch (Exception e) {
				return;
			}
		}
		// go for it
		t.createMethod(content, getSiblingForNewMethod(t, false, false), false,
				null);
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
			List reqImports = new ArrayList() ;
			String newMSrc = mgen.generateMethod(mref, methodName, bp.getSimpleName(), reqImports);

			IMethod newMethod = null;
			// Create it as the last method
			try {
				// Offsets will be updated with a call to refreshMethods on mref
				newMethod = cuType.createMethod(newMSrc, getSiblingForNewMethod(cuType, false, false), false, null);
				mref.setMethodHandle(newMethod.getHandleIdentifier());
				// Need to set the source overhere, so that we can parse the init expression
				mref.setContent(newMethod.getSource());
				fBeanModel.addMethodInitializingABean(mref);
				
				// create imports if needed
				CodeExpressionRef.handleImportStatements(cu, null, reqImports); // no need to update expressions
				
				// Generate main, if needed				
				newMSrc = mgen.generateMain(cuType.getElementName());
				if (newMSrc!=null) {
					// This method generator contributes a main method					
					generateMainIfNeeded (cuType, newMSrc);
				}
				
			} catch (JavaModelException e) {
				JavaVEPlugin.log(e, Level.WARNING);
				throw new CodeGenException(e);
			}
			// template also created the init expression; e.g., new Foo()
			parseInitExpression(bp);
			if (JavaVEPlugin.isLoggingLevel(Level.FINE))
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

//	InstanceVariableTemplate ft = new InstanceVariableTemplate(varName, ((IJavaObjectInstance) component).getJavaType().getQualifiedName(), INSTANCE_VAR_DEFAULT_COMMENT);
	// Bug 64039 shows that there can be init expressions without new
	// in them - hence need to handle imports here itself.
	CodeExpressionRef.handleImportStatements(cuType.getCompilationUnit(), bp.getModel(), Collections.singletonList(component.getJavaType().getQualifiedName()));
	
	InstanceVariableTemplate ft = new InstanceVariableTemplate(varName, component.getJavaType().getSimpleName(), INSTANCE_VAR_DEFAULT_COMMENT);
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
	if (JavaVEPlugin.isLoggingLevel(Level.FINE))
		JavaVEPlugin.log("Adding Instance Var: \n" + ft + "\n", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
}
/**
 * The null constructor typically will call the initialization method.  The exception
 * is for Applet, where the init method is called by the container.
 */
protected boolean isNeedToCallInit(BeanPart b) {

    JavaHelpers AppletType = JavaRefFactory.eINSTANCE.reflectType("java.applet.Applet",b.getEObject()) ; //$NON-NLS-1$
	return !AppletType.isAssignableFrom(
	                    ((IJavaObjectInstance) b.getEObject()).getJavaType()) ;
}

protected void generateNullConstructorIfNeeded(BeanPart b, CodeMethodRef iniMethod) {
    
    IType t = CodeGenUtil.getMainType(fBeanModel.getCompilationUnit()) ;
    // TODO  Need to parse here
    IMethod firstM = null ;
    try {
		IMethod[] mtds = t.getMethods() ;
	    List constructorList = new ArrayList();
	    for (int i = 0; i < mtds.length; i++) {	        
	        IMethod method = mtds[i];
	        if (firstM == null) firstM = method ;
	        if (method.isConstructor()) {
	        	constructorList.add(method);
	        }	        
	    }
	    if (constructorList.size()>0) {
	    	if (isNeedToCallInit(b)) {
	        // Update existing constructors
		    	for (int i = 0; i < constructorList.size(); i++) {
					IMethod constructor = (IMethod)constructorList.get(i);
			        // Is the bean an instance of Applet ??? In this case we do not want to call 
		            // the init method.	            					
					MethodParser mp = new MethodParser(constructor, fBeanModel.getLineSeperator());
					mp.addMethodCallIfNeeded(iniMethod.getMethodName());
					// force a reconcile
					fBeanModel.getCompilationUnit();
		    	}
	    	}
        }
        else { // create a null one
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
                VECreationPolicy  policy = null ;
                JavaClass superClazz = clazz.getSupertype();
                ClassDescriptorDecorator decorator = (ClassDescriptorDecorator)ClassDecoratorFeatureAccess.getDecoratorWithKeyedFeature(superClazz, 
                                                                           ClassDescriptorDecorator.class, 
                                                                           CDECreationTool.CREATION_POLICY_KEY);            
                if ( decorator != null ) {
                    String creationPolicyClassName = (String)decorator.getKeyedValues().get(CDECreationTool.CREATION_POLICY_KEY);
                    if (creationPolicyClassName != null) {
						try {
							Class cpClass = CDEPlugin.getClassFromString(creationPolicyClassName);
							if (VECreationPolicy.class.isAssignableFrom(cpClass)) {
								policy = (VECreationPolicy) cpClass.newInstance();
								CDEPlugin.setInitializationData(policy, creationPolicyClassName, null);
							}
						} catch (ClassCastException e) {
							JavaVEPlugin.log(e, Level.WARNING);
						} catch (ClassNotFoundException e) {
							JavaVEPlugin.log(e, Level.WARNING);
						} catch (InstantiationException e) {
							JavaVEPlugin.log(e, Level.WARNING);
						} catch (IllegalAccessException e) {
							JavaVEPlugin.log(e, Level.WARNING);
						} catch (CoreException e) {
							JavaVEPlugin.log(e, Level.WARNING);
						}
						if (policy != null) {
							String superOveride = policy.getDefaultSuperString(superClazz);
							if (superOveride != null)
								template.setSuperInitString(superOveride + ";"); //$NON-NLS-1$
						}
						
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
      
   BeanMethodTemplate template = new BeanMethodTemplate(BeanMethodTemplate.VOID, //$NON-NLS-1$
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
	        newMethod = cuType.createMethod(newSrc,getSiblingForNewMethod(cuType, false, true),false,null) ;    
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
   
   if (JavaVEPlugin.isLoggingLevel(Level.FINE))
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
 * If a decleration for this instance already exists, use it.
 * @since 1.1.0
 */
protected void normalizeDecleration(BeanPart bp, CodeMethodRef method) {
	bp.getDecleration().setDeclaringMethod(method);
	bp.setModel(fBeanModel);
	BeanPartDecleration modelDecleration = fBeanModel.getModelDecleration(bp.getDecleration());
	if (modelDecleration!=null)
		bp.setBeanPartDecleration(modelDecleration);		
}


protected void generateSetFeatures(BeanPart bp) throws CodeGenException {
	DefaultMethodTextGenerator gen = new DefaultMethodTextGenerator(bp.getEObject(), bp.getModel());
	gen.generateExpressionsContent();
}

/**
 * This method is called when an implicit instance is added to the JVE model.
 * It will only create the BeanPart, 
 */
public BeanPart createImplicitFromJVEModel(IJavaObjectInstance component, ICompilationUnit cu) throws CodeGenException {
		
	  ImplicitAllocation allocation = (ImplicitAllocation)component.getAllocation();
	  // This should drive the creation of the implicit
	  BeanPart parent = fBeanModel.getABean(allocation.getParent());
	  EStructuralFeature sf = allocation.getFeature();
	  
	  BeanPart implicitBean = null;
	  try {
			// Create the implicit bean
			BeanPartDecleration bpd = new BeanPartDecleration(parent, sf);
			implicitBean = new BeanPart(bpd);	
			implicitBean.setImplicitParent(parent, sf);
			parent.getModel().addBean(implicitBean);
			implicitBean.setEObject(component);
			
			// Create an init expressio
			implicitBean.addInitMethod(parent.getInitMethod());
			EStructuralFeature asf = CodeGenUtil.getAllocationFeature(implicitBean.getEObject());
			ExpressionRefFactory eGen = new ExpressionRefFactory(implicitBean, asf);	
			// prime the proper helpers
			CodeExpressionRef initExpression = eGen.createFromJVEModelWithNoSrc(null);
			initExpression.setState(CodeExpressionRef.STATE_INIT_EXPR, true);
			
			 eGen = new ExpressionRefFactory(parent, sf);		
			// prime the proper helpers
			eGen.createFromJVEModelWithNoSrc(new Object[] { implicitBean.getEObject() } );
			
			generateSetFeatures(implicitBean);
		} catch (CodeGenException e) {
			JavaVEPlugin.log(e);
		}
		return implicitBean;	  
}

/**
 * This method is called when an instance is added to the JVE model.
 * It will only create the BeanPart, and generate the Instance Variable decleration
 * in the source.
 * 
 * Settings/JCMMethod generation will be constructed later
 * 
 */
public BeanPart createFromJVEModel(IJavaObjectInstance component, ICompilationUnit cu) throws CodeGenException {
		
	
	  if (component.getAllocation() instanceof ImplicitAllocation)
		  return createImplicitFromJVEModel(component, cu);
			
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
      String bType = component.getJavaType().getQualifiedName() ;
      BeanPartDecleration decl = new BeanPartDecleration(varName,bType);      
      bp = new BeanPart (decl) ;
      boolean instanceVar = true;                 
      // Instance variable are always members of BSC
      boolean thisPart = false ;
      if (component.eContainer() instanceof BeanSubclassComposition) {
         if (component.equals(((BeanSubclassComposition)component.eContainer()).getThisPart()))
           thisPart=true ;
         else
           instanceVar = true ;
      }
      else
         instanceVar=false ;
      
      MemberDecoderAdapter ma = null ;
      CodeMethodRef decMethod = null;
      if (instanceVar)
      	normalizeDecleration(bp,null);
      else {
      	ma = (MemberDecoderAdapter) EcoreUtil.getExistingAdapter(component.eContainer(),ICodeGenAdapter.JVE_MEMBER_ADAPTER) ;
     	decMethod = ma.getMethodRef() ;
     	normalizeDecleration(bp,decMethod);
      }
           
      bp.setEObject(component) ;
      bp.setSettingProcessingRequired(true) ;
      bp.setIsInJVEModel(true) ;
      
      fBeanModel.addBean(bp) ;
      
      
      
      
      if (instanceVar) {
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
      	 bp.addInitMethod(decMethod) ;
      	 generateLocalVariable(component,ma.getMethodRef(),varName, cu) ;
      }
      bp.setModel(fBeanModel) ;
      fBeanModel.refreshMethods();
	  return bp;
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
//TODO need to reUse the dispose() here, rather to duplicate this logic
public void removeBeanPart (BeanPart bean) {
	boolean jdtChangesMade = false ; 	 // MethodRef offsets not being updated - hence check.
	BeanPartDecleration needRefresh = null; // Deleting a BeanPart may need refreshing of a reUse instance's init expression 
	IType tp = CodeGenUtil.getMainType(fBeanModel.getCompilationUnit()) ;
	
	if (bean.getDecleration().isInstanceVar() &&
		bean.getDecleration().isSingleDecleration()) { 	  
	  IField f = tp.getField(bean.getSimpleName()) ;
	  if (f != null) {		// delete the field
		try {
			if (JavaVEPlugin.isLoggingLevel(Level.FINE))
				JavaVEPlugin.log("\tRemoving Field: "+f, Level.FINE) ; //$NON-NLS-1$
		  f.delete(true,null) ;
		  jdtChangesMade = true;
		}
		catch (JavaModelException e) {} 
	  }
	  else if (JavaVEPlugin.isLoggingLevel(Level.FINE))
	     JavaVEPlugin.log ("BeanPartGenerator.removeBeanPart: field is not in source: "+bean.getUniqueName()+"  <--- check me", Level.FINE) ; //$NON-NLS-1$ //$NON-NLS-2$
	}
	else {
		// It is a local variable, and it is possible that
		// It is a reusable instance.
		needRefresh=bean.getDecleration();
	}
	 
	// TODO  Need to maintain Bean Ref Count in method, and remove it when the last ref. is gone
	CodeMethodRef mr = bean.getInitMethod() ;
    if (mr == null) {
    	if (JavaVEPlugin.isLoggingLevel(Level.FINEST))
        	JavaVEPlugin.log("BeanPartGenerator.removeBeanPart() : No MethodRef for "+bean) ; //$NON-NLS-1$
    }
	// TODO  Instance Var. may share methods
	
// Delte should be done from the model.	
	Collection beansInMethod = mr==null?new ArrayList():fBeanModel.getBeansInitilizedByMethod(mr);
	List deleteDependentBeans = new ArrayList();
	Iterator bitr = beansInMethod.iterator();
	
	boolean areOtherInstancesFound = false;
	boolean isAnyNonInstanceVariableBeingReturned = false;
	
	bitr = beansInMethod.iterator();
	while(bitr.hasNext()){
		BeanPart bp = (BeanPart) bitr.next();
		if(bp.equals(bean))
			continue;
		if(deleteDependentBeans.contains(bp))
			continue;
		areOtherInstancesFound = true;
	}
	
	BeanPart returnedBean = mr==null?null:fBeanModel.getBeanReturned(mr.getMethodName());
	isAnyNonInstanceVariableBeingReturned = returnedBean==null||
											returnedBean.equals(bean)||
											deleteDependentBeans.contains(returnedBean)?false:returnedBean.getDecleration().isInstanceVar();
	
	boolean shouldMethodBeRemoved = mr!=null && !(areOtherInstancesFound || isAnyNonInstanceVariableBeingReturned);
	Iterator itr=null ;
	if (shouldMethodBeRemoved) {
		// a Seperate JCMMethod exist for this bean    	
		tp = CodeGenUtil.getMainType(fBeanModel.getCompilationUnit()); // Offsets have changed if we removed the field	
		try {
			if (mr != null) {
				itr = mr.getExpressions() ;
				IMethod m = CodeGenUtil.getMethod(tp, mr.getMethodHandle());
				String handle = mr.getMethodHandle();
				if (JavaVEPlugin.isLoggingLevel(Level.FINE))
					JavaVEPlugin.log("\tRemoving JCMMethod: " + handle, Level.FINE); //$NON-NLS-1$
				m.delete(true, null);
				jdtChangesMade = true;
			}
			else if (JavaVEPlugin.isLoggingLevel(Level.FINE))
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
		deleteList.add(e);
	}
	// If the method is not removed, we need to remove the expressions
	if (!shouldMethodBeRemoved) {
	  // updateDocument will delete the expressions
	  fBeanModel.refreshMethods();
	  for (int i = deleteList.size() - 1; i >= 0; i--) {
		// If it is an instance bean, we removed the method
	  	// We mark expressions as deleted as we remove them because
	  	// updating of subsequent expression offsets is not performed
	  	// if they are marked as deleted - something which will cause
	  	// the removal of incorrect code causing broken code. (60079)
		  boolean isFieldExp = ((CodeExpressionRef) deleteList.get(i)).isStateSet(CodeExpressionRef.STATE_FIELD_EXP);
		((CodeExpressionRef) deleteList.get(i)).clearState(); 
	  	((CodeExpressionRef) deleteList.get(i)).setState(CodeExpressionRef.STATE_FIELD_EXP, isFieldExp);
	  	((CodeExpressionRef) deleteList.get(i)).setState(CodeExpressionRef.STATE_DELETE, true);
		((CodeExpressionRef) deleteList.get(i)).updateDocument(i == 0); // Update the source code once
	  }
	}
	else {
		// expressions will be disposed when mr is disposed.
		  for (int i = deleteList.size() - 1; i >= 0; i--) {
			// If it is an instance bean, we removed the method
		  	((CodeExpressionRef) deleteList.get(i)).clearState();
		  	((CodeExpressionRef) deleteList.get(i)).setState(CodeExpressionRef.STATE_DELETE, true);
		  }
	}
	
	if (shouldMethodBeRemoved) {
	  fBeanModel.removeMethodRef(mr) ;	
	  mr.dispose() ;		
	}
    bean.dispose();
    if (needRefresh!=null) {
    	// A local variable is deleted, and a reused one
    	// need to regenerate and create the formal (type) decleration 
    	needRefresh.refreshDeclerationSource();
    }
}

public BeanPart createThisBeanPartIfNeeded(CodeMethodRef initMethod) {
     
     BeanPart bean = fBeanModel.getABean(BeanPart.THIS_NAME) ;
     
     if (bean == null) {    
       String tname = fBeanModel.getResolver().resolveMain().getName();
       BeanPartDecleration decl = new BeanPartDecleration(BeanPart.THIS_NAME);       
       decl.setType(tname);     
       decl.setDeclaringMethod(null);
       decl.setModel(fBeanModel);
       bean = new BeanPart (decl) ;	       
       fBeanModel.addBean(bean) ;
     }
     if (initMethod != null)     
        bean.addInitMethod(initMethod) ;
     
     return bean ;	
}

static void setBeanPartAsImplicit (BeanPart implicitBean, BeanPart parent, EStructuralFeature sf) throws CodeGenException {
	
	implicitBean.setImplicitParent(parent, sf);
	// Let the parent point to its implicit child
    parent.getEObject().eSet(sf, implicitBean.getEObject());		
	ExpressionRefFactory eGen = new ExpressionRefFactory(parent, sf);		
	// prime the proper helpers
	eGen.createFromJVEModelWithNoSrc(new Object[] { implicitBean.getEObject() } );
	implicitBean.addBackRef(parent, (EReference)sf);
	
}
/**
 * 
 * This method is called by the construction of a wrapper (parent).
 * 
 * Set up an implicit BeanPart, given a parent, and a parent feature.
 * It is typically called when a Parent constructor is reverse parsed.
 * 
 * During the visiting portion, an "implicit" Bean may have been created
 * without the decode information (e.g., SF, parent etc.)
 * 
 * will merge this information forward.
 * 
 * TreeViewer treeViewer = new TreeViewer(....)  
 * treeViewer.getTree().setEnabled(true).
 * 
 *    
 * 
 * @param parent
 * @param sf
 * @param createImplicitInitExpression
 * @return
 * 
 * @since 1.2.0
 */
public BeanPart createImplicitBeanPart (BeanPart parent, EStructuralFeature sf) {
	BeanPart implicitBean = null;
	try {
		// Create the implicit bean
		BeanPartDecleration bpd = new BeanPartDecleration(parent, sf);
		BeanPartDecleration current = fBeanModel.getModelDecleration(bpd);
		if (current!= null) {
			// Implicit decleration was parsed in via a "parent.getImplicit().setFoo();" expression
			current.setType(bpd.getType());
			bpd = current;
			implicitBean = bpd.getBeanParts()[0];			
		}
		else { 
		  // Create an implicit bean
		  implicitBean = new BeanPart(bpd);	
		  implicitBean.setImplicitParent(parent, sf);
		  parent.getModel().addBean(implicitBean);
		}
		
		if (implicitBean.getDecleration().isImplicitDecleration()) {
			// BeanPart is being created from scratch.
			implicitBean.createEObject();
			ImplicitAllocation ia = InstantiationFactory.eINSTANCE.createImplicitAllocation(parent.getEObject(), sf);
			((IJavaObjectInstance)implicitBean.getEObject()).setAllocation(ia);		
			
			implicitBean.addInitMethod(parent.getInitMethod());
			EStructuralFeature asf = CodeGenUtil.getAllocationFeature(implicitBean.getEObject());
			ExpressionRefFactory eGen = new ExpressionRefFactory(implicitBean, asf);	
			// prime the proper helpers
			CodeExpressionRef initExpression = eGen.createFromJVEModelWithNoSrc(null);
			initExpression.setNoSrcExpression(true);
			// Force a full cascaded decoding (SWT decoders may generate other expressions).
			initExpression.decodeExpression();
			// During building, this bean will be added the to EMF model
		}
		setBeanPartAsImplicit(implicitBean, parent, sf);
	} catch (CodeGenException e) {
		JavaVEPlugin.log(e);
	}
	return implicitBean;
}

public BeanPart restoreImplicitBeanPart (BeanPart parent, EStructuralFeature sf, boolean createImplicitInitExpression) {
	BeanPart implicitBean = null;
	try {
		// Create the implicit bean
		BeanPartDecleration bpd = new BeanPartDecleration(parent, sf);
		BeanPartDecleration current = fBeanModel.getModelDecleration(bpd);
		if (current!= null) {
			current.setType(bpd.getType());
			bpd = current;
			implicitBean = bpd.getBeanParts()[0];			
		}
		else {
		  implicitBean = new BeanPart(bpd);	
		  implicitBean.setImplicitParent(parent, sf);
		  parent.getModel().addBean(implicitBean);
		}
		
		EObject implicit = (EObject) parent.getEObject().eGet(sf);
		implicitBean.setEObject(implicit);
		
		if (createImplicitInitExpression) {
			implicitBean.addInitMethod(parent.getInitMethod());
			EStructuralFeature asf = CodeGenUtil.getAllocationFeature(implicitBean.getEObject());
			ExpressionRefFactory eGen = new ExpressionRefFactory(implicitBean, asf);	
			// prime the proper helpers
			CodeExpressionRef initExpression = eGen.createFromJVEModelWithNoSrc(null);
			initExpression.setNoSrcExpression(true);
			// Force a full cascaded decoding (SWT decoders may generate other expressions).
			initExpression.decodeExpression();			
		}
		setBeanPartAsImplicit(implicitBean, parent, sf);
	} catch (CodeGenException e) {
		JavaVEPlugin.log(e);
	}
	return implicitBean;
}

}
