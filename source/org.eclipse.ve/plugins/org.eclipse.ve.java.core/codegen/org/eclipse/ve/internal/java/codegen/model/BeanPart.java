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
 *  $RCSfile: BeanPart.java,v $
 *  $Revision: 1.15 $  $Date: 2004-04-19 22:12:51 $ 
 */
import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cdm.Annotation;
import org.eclipse.ve.internal.cdm.VisualInfo;

import org.eclipse.ve.internal.jcm.BeanSubclassComposition;

import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.java.rules.IParentChildRelationship;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public class BeanPart {
    
    public final static String         THIS_NAME = "this" ; //$NON-NLS-1$
    public final static String		 THIS_HANDLE = "_this_Annotation_handle";
	
	String 	fName = null ;
	String 	fType ;
	ASTNode 		fFieldDecl = null ;
	ArrayList		fBeanInitMethods = new ArrayList () ;			// JCMMethod/s where the Bean is created
	ArrayList      	fBeanRefExpressions =  new ArrayList () ;		// JCMMethod/s which update bean attributes
	ArrayList       fBeanEventExpressions = new ArrayList() ;
	ArrayList		fBeanReturnMethods = new ArrayList () ;		// Metods/s which return the value of this bean
	ArrayList		fEventInitMethods = new ArrayList() ;
	ArrayList		fCallBackExpressions = new ArrayList() ;
	ArrayList		fNoSrcExpressions = new ArrayList();
	EObject   		fEObject = null ;							// Mof Instance of this Bean
	ArrayList    	fbackReferences = new ArrayList() ;		// Mof Object that contains this object 
	EObject			fContainer = null ;                        //  Parent (Container) of this object - null ? part of Composition
	ArrayList      	fChildren = new ArrayList () ;				// Beans this part may contain components	
	IBeanDeclModel	fModel = null ;    
    BeanPart    	fProxyBeanPart = null ;					// This bean part is not in the BeanDecModel
    boolean			fisInstanceVar = true ;					// Is the bean an instance variable ?
    boolean			fisInstanceInstantiation = false ;         // Is this bean part initilized with its decleration ?
    FreeFormAnnoationDecoder fFFDecoder = null ;				// Responsible parse/generate FF tags
    String      	fFieldDeclHandle = null ;
    List        	fBadExpressions = null;
    boolean			isInJVEModel = false ;
    boolean			fSettingProcessingRequired = false ;
    


public  BeanPart (FieldDeclaration decl) {
	
	fFieldDecl = decl ;
	//TODO: support multi fields per decleration
	processFragment((VariableDeclaration)decl.fragments().get(0), decl.getType());
}

protected void processFragment(VariableDeclaration vd, Type tp) {
	fName = vd.getName().getIdentifier();
	if (tp instanceof SimpleType)
		setType(((SimpleType)tp).getName());
}

public  BeanPart (VariableDeclarationStatement decl) {	
	fFieldDecl = decl ;
	//TODO: support multi fields per decleration
	processFragment((VariableDeclaration)decl.fragments().get(0), decl.getType());
}

/**
 * @return the FreeForm Decoder associated with this bean
 */
public FreeFormAnnoationDecoder getFFDecoder() {
    boolean newDecoder = false ;
    if (fFFDecoder==null) {
       if (getSimpleName().equals(THIS_NAME)) 
          fFFDecoder = new FreeFormThisAnnotationDecoder(this) ;
       else
          fFFDecoder = new FreeFormAnnoationDecoder(this) ;
       newDecoder = true ;
    }
    
    Annotation ant = CodeGenUtil.getAnnotation(getEObject()) ;    
    
    if (ant != null) {
        ICodeGenAdapter a = (ICodeGenAdapter)EcoreUtil.getExistingAdapter(ant,ICodeGenAdapter.JVE_CODEGEN_ANNOTATION_ADAPTER) ;
        if (a != null && newDecoder) {
            ant.eAdapters().remove(a) ;
            a = null ;
        }
        
        // Need an adapter on the annotation in case the Visual Info is added/changed/removed
        if (a==null) {
          a = new AnnotationDecoderAdapter(fFFDecoder) ;
          ant.eAdapters().add(a) ;
        }
        // This is where the FF constraint is on
        VisualInfo vi = ant.getVisualInfo(getModel().getCompositionModel().getDiagram()) ;
        if (vi != null) {
            ICodeGenAdapter va = (ICodeGenAdapter)EcoreUtil.getExistingAdapter(vi,ICodeGenAdapter.JVE_CODEGEN_ANNOTATION_ADAPTER) ;
            if (va != null && newDecoder) {
              vi.eAdapters().remove(va) ;
              vi.eAdapters().add(a) ;
            }
        }           
    }

    return fFFDecoder ;    
}

public void setFieldDeclHandle(String dec) {
    fFieldDeclHandle = dec ;
}

public String getFieldDeclHandle() {
    return fFieldDeclHandle ;
}

/**
 *  
 */
public  BeanPart (FieldDeclaration decl,CodeMethodRef method,boolean initMethod)  {
	
	this(decl) ;
	
    if (method != null) {	
	  if (initMethod) fBeanInitMethods.add(method) ;
	  else fBeanReturnMethods.add(method) ;
    }		
}


/**
 *  
 */
public  BeanPart (FieldDeclaration decl,CodeExpressionRef exp)  {
	
	this(decl) ;

    if (exp != null)
       fBeanRefExpressions.add(exp) ;	
       
}


/**
 *  Construct a BeanPart for the class (this) we are parsing
 */
public  BeanPart (String thisType) 	 {
     this(THIS_NAME, thisType);
}

/**
 *  Construct a BeanPart for a bean discovered from C. Model
 */
public  BeanPart (String name,String theType) 	 {
     fName = name ;
     setType(theType);
}

public boolean isInstanceVar() {
    return fisInstanceVar ;
}

public void setInstanceVar(boolean flag) {
    fisInstanceVar = flag ;
}


/**
 *   Add a method where the bean is created
 */
public  void addInitMethod(CodeMethodRef methodRef) {	
	if (!fBeanInitMethods.contains(methodRef))
	   fBeanInitMethods.add(methodRef) ;
	if (fModel != null) {
	   fModel.addMethodInitializingABean(methodRef);
	   methodRef.setModel(fModel) ;
	   fModel.updateBeanNameChange(this);
	}
}

/**
 *   Add a method where the bean is created
 */
public  void addEventInitMethod(CodeMethodRef methodRef) {
	if (!fEventInitMethods.contains(methodRef)) {
		if (methodRef == getInitMethod()) // Make the initMethod the default EventInitMethod
		  fEventInitMethods.add(0,methodRef) ;
	    else
	      fEventInitMethods.add(methodRef) ;
	}
}

public  void removeInitMethod (CodeMethodRef m) {
	fBeanInitMethods.remove(m) ;
}

public  void removeEventInitMethod (CodeMethodRef m) {
	fEventInitMethods.remove(m) ;
}

public  void removeReturnMethod (CodeMethodRef m) {
	fBeanReturnMethods.remove(m) ;
}
/**
 *   Get the one and only one we will use
 */
public CodeMethodRef getInitMethod() {
	if (fBeanInitMethods.size() == 0) return null ;
	return (CodeMethodRef) fBeanInitMethods.get(0) ;
}

/**
 *   Get the one and only one we will use
 */
public CodeMethodRef getEventInitMethod() {
	if (fEventInitMethods.size() == 0 && getInitMethod() != null) 
       fEventInitMethods.add(getInitMethod()) ;
    else
	  if (getInitMethod() != null &&
	      getInitMethod() != fEventInitMethods.get(0)) {
	    	// Force style 3's init method
	    	fEventInitMethods.add(0,getInitMethod()) ;
	  }
	return fEventInitMethods.size()>0 ? (CodeMethodRef) fEventInitMethods.get(0) : null ;
}

/**
 *   Get the one and only one we will use
 */
public CodeMethodRef getReturnedMethod() {
	if (fBeanReturnMethods.size() == 0) return null ;
	return (CodeMethodRef) fBeanReturnMethods.get(0) ;
}

/**
 *   Add a method where the bean instance is returned
 */
public  void addReturnMethod(CodeMethodRef methodRef) {
	if (!fBeanReturnMethods.contains(methodRef))
	   fBeanReturnMethods.add(methodRef) ;
	if (fModel != null)
	  try {
	   fModel.addMethodReturningABean(methodRef.getMethodName(),getUniqueName()) ;
	   methodRef.setModel(fModel) ;
	  }
	  catch (org.eclipse.ve.internal.java.codegen.util.CodeGenException e) {
	  	JavaVEPlugin.log(e, Level.WARNING) ;
	  }
}


/**
 * Add a method where a bean's setXX() method is called
 */
public  void addRefExpression(CodeExpressionRef exp) {
	if (!fBeanRefExpressions.contains(exp))
	   fBeanRefExpressions.add(exp) ;
}

/**
 * Add a method where a bean's setXX() method is called
 */
public  void addEventExpression(CodeEventRef exp) {
	if (!fBeanEventExpressions.contains(exp))
	   fBeanEventExpressions.add(exp) ;
}

/**
 * Add a method where a bean's setXX() method is called
 */
public  void addCallBackExpression(CodeCallBackRef exp) {
	if (!fCallBackExpressions.contains(exp))
	   fCallBackExpressions.add(exp) ;
}

public  void removeCallBackExpression(CodeCallBackRef exp) {
	   fCallBackExpressions.remove(exp) ;
}

/**
 * Remove a method where a bean's setXX() method is called
 */
public  void removeRefExpression(CodeExpressionRef exp) {
	fBeanRefExpressions.remove(exp) ;
}

/**
 * Remove a method where a bean's setXX() method is called
 */
public  void removeEventExpression(CodeEventRef exp) {
	fBeanEventExpressions.remove(exp) ;
}

/**
 *
 */
public String getSimpleName () {
	return fName ;
}

protected String primGetUniqueName() {
	if(!isInstanceVar() && getInitMethod()!=null){
		return BeanDeclModel.constructUniqueName(getInitMethod(),getSimpleName()); //getInitMethod().getMethodHandle()+"^"+getSimpleName();
	}else{
		return getSimpleName();
	}
}

public String getUniqueName() {
	String base = primGetUniqueName() ;
	String name = base ;
	int i = 0 ;
	while (fModel.getABean(name) != null) {
		if(!fModel.getABean(name).equals(this))
			name = base + Integer.toString(++i) ; // If other bean is present with name, change our unique name
		else
			break;
	}
	return name;
}

/**
 *  During parsing, we may have collected expression that are not going to be used.
 *  Only init methods expressions will be used.
 */
public Collection getRefExpressions () {
	if (getInitMethod()==null) {
		for (Iterator itr = fBeanRefExpressions.iterator(); itr.hasNext();) {
			CodeExpressionRef exp = (CodeExpressionRef) itr.next();
			if (exp.getMethod() != null) {
				exp.getMethod().removeExpressionRef(exp) ;
			}
		}
		fBeanRefExpressions.clear() ;
	}
	else {
		CodeMethodRef mr = getInitMethod() ;
		for (int I=fBeanRefExpressions.size()-1; I>=0; I--) {
			CodeExpressionRef exp = (CodeExpressionRef) fBeanRefExpressions.get(I) ;
			if (exp.getMethod() != mr) {
				exp.getMethod().removeExpressionRef(exp) ;
				fBeanRefExpressions.remove(I);
			}
		}				
	}	   
	return new ArrayList(fBeanRefExpressions) ;
}

public Collection getRefEventExpressions () {
	if (getEventInitMethod()==null) {
		for (Iterator itr = fBeanEventExpressions.iterator(); itr.hasNext();) {
			CodeEventRef exp = (CodeEventRef) itr.next();
			if (exp.getMethod() != null) {
				exp.getMethod().removeEventRef(exp) ;
			}
		}
		fBeanEventExpressions.clear() ;
	}
	else {
		// There may be more than one Event Init JCMMethod (style 2, 3)
		for (int I=fBeanEventExpressions.size()-1; I>=0; I--) {
			CodeEventRef exp = (CodeEventRef) fBeanEventExpressions.get(I) ;
			if (!fEventInitMethods.contains(exp.getMethod())) {
				exp.getMethod().removeEventRef(exp) ;
				fBeanEventExpressions.remove(I);
			}
		}				
	}	   
	return new ArrayList(fBeanEventExpressions) ;
}

public Collection getRefCallBackExpressions() {
	return new ArrayList(fCallBackExpressions) ;
}

public Collection getNoSrcExpressions() {
	return new ArrayList(fNoSrcExpressions);
}



/**
 *
 */
public String getType () {
	return fType ;
}

/**
 *
 */
public ASTNode getFieldDecl() {
      return fFieldDecl ;	
}
///**
// *
// */
//public boolean isInstanceVariable() {
//	return fFieldDecl instanceof FieldDeclaration ;
//}
///**
// *
// */
//public boolean isLocalVariable() {
//	return fFieldDecl instanceof LocalDeclaration ;
//}
//	
/**
 *
 */
public EObject getEObject() {
	if (!isProxy())
	   return fEObject ;
	else
	   return fProxyBeanPart.getEObject() ;
}


/**
 * null, will clear the current EObject ;
 */
public void setEObject (EObject obj) {
	
	if (fEObject == obj) return ;
	
	if (obj == null && isInJVEModel()) {
		removeFromJVEModel() ;
	}
	
	EObject old = fEObject ;
	if (fEObject != null) {
       ICodeGenAdapter a = (ICodeGenAdapter)EcoreUtil.getExistingAdapter(fEObject,ICodeGenAdapter.JVE_CODE_GEN_TYPE) ;
       while (a != null) {
          fEObject.eAdapters().remove(a) ;
          a = (ICodeGenAdapter)EcoreUtil.getExistingAdapter(fEObject,ICodeGenAdapter.JVE_CODE_GEN_TYPE) ;
       }
	}
	
	fEObject=obj ;
	
	if (obj != null) {
		ICodeGenAdapter a = (ICodeGenAdapter) EcoreUtil.getExistingAdapter(obj, ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);
		if (a == null) {
			BeanDecoderAdapter ba;
			if (getSimpleName().equals(THIS_NAME))
				ba =
					(BeanDecoderAdapter) CodeGenUtil.getDecoderFactory(getModel()).getExpDecoder(
						(IJavaInstance) getEObject()).createThisCodeGenInstanceAdapter(
						this);
			else if (getEObject() instanceof IJavaObjectInstance)
				ba = (BeanDecoderAdapter) CodeGenUtil.getDecoderFactory(getModel()).
				      getExpDecoder((IJavaInstance) getEObject()).createCodeGenInstanceAdapter(this);
			else
				ba = (BeanDecoderAdapter) CodeGenUtil.getDecoderFactory(getModel()).
				      getDefaultExpDecoder().createCodeGenInstanceAdapter(this);

			obj.eAdapters().add(ba);
		}
	}
	
	if (fModel != null)	
	   fModel.UpdateRefObjKey(this,old) ;
}

/**
 * 
 * @param fType
 * 
 * @since 1.0.0
 * @deprecated use void setType(Name t)
 */
protected void setType(String fType){	
	// The BeanSubClassComposition pseodo bean part as an empty type
    if (getModel() != null && fType !=null && !fType.equals("")) { //$NON-NLS-1$
    	// TODO No need to check for compilation unit - resolver should return unresolved if unable to resolve
    	if(getModel().getCompilationUnit()!=null){
		  //IType t = CodeGenUtil.getMainType(getModel().getCompilationUnit()) ;
		  //String rt = CodeGenUtil.resolveTypeComplex(t,fType) ;
		String rt = getModel().resolve(fType);
		  if(rt!=null)
		  	this.fType = rt;
		  else
		  	this.fType = fType;
    	}
    }
    else
      this.fType = fType ;
}

protected void setType(Name t){	
	// The BeanSubClassComposition pseodo bean part has an empty type
	String result=t.toString();
	if (getModel() != null && t !=null && !t.toString().equals("")) { //$NON-NLS-1$
		String r=CodeGenUtil.resolve(t, getModel());
		if (r!=null)
			result=r;
	}
	fType=result;
}


/**
 *
 */
public final BeanPart[] getBackRefs() {
	return (BeanPart[]) fbackReferences.toArray(new BeanPart[fbackReferences.size()]) ;
}

/**
 *  Reference target to point to its source (e.g., child to parent)
 */
public void addBackRef (BeanPart bean, EReference sf) {
	
	IParentChildRelationship pcRule = (IParentChildRelationship) CodeGenUtil.getEditorStyle(fModel).getRule(IParentChildRelationship.RULE_ID) ;
		
	if (!fbackReferences.contains(bean))
	   fbackReferences.add(bean) ;
    // Refresh the bean's status
    fModel.addBean(this) ;
    if (bean != null && getModel().getCompositionModel() != null)
	   if (pcRule.isChildRelationShip(sf))  {
	   	  fContainer = bean.getEObject() ; 
          getModel().getCompositionModel().getModelRoot().getComponents().remove(getEObject()) ;
       }
      
}


public void removeBackRef (BeanPart bean, boolean updateFF) {
	removeBackRef(bean.getEObject(),updateFF);
}

/**
 * Remove bean's target reference from its source
 * @param bean
 * @param updateFF
 */
public void removeBackRef (EObject bean, boolean updateFF) {

	if (fModel==null) return ;
    BeanPart bp = fModel.getABean(bean) ;
    if (bp != null)		
	    fbackReferences.remove(bp) ;
	if (bean != null && getModel().getCompositionModel() != null)
	   if (fContainer != null && fContainer.equals(bean)) {
		  fContainer = null ;
		  if (updateFF && getEObject().eContainer() != null) {
		  	BeanSubclassComposition bsc = getModel().getCompositionModel().getModelRoot();
		  	if (!bsc.getComponents().contains(getEObject()))
		      bsc.getComponents().add(getEObject());
		  }
	   }
}
/**
 *
 */
public void addChild (BeanPart bean) {
    if (!fChildren.contains(bean))
	   fChildren.add(bean) ;
}
/**
 *
 */
public void removeChild (BeanPart bean) {
	fChildren.remove(bean) ;
}

public Iterator getChildren() {
	return fChildren.iterator() ;
}

public IBeanDeclModel getModel () { 
	if (isProxy())
	  return fProxyBeanPart.getModel() ;
	else
	  return fModel ;
}

public void setModel(IBeanDeclModel model) {
    if (fModel != null && model != null && fModel.equals(model)) 
       return ;  // No need to set the type and resolve
	fModel = model ;
	if(fType!=null && model!=null)
		setType(getType()) ; // Refresh Type
    if (getFieldDeclHandle() == null && model!=null) {
    	IField f = CodeGenUtil.getFieldByName(getSimpleName(),model.getCompilationUnit()) ;
    	if (f !=null)
    	   setFieldDeclHandle(f.getHandleIdentifier()) ;
    }
    else if (getSimpleName().equals(THIS_NAME)) 
        setFieldDeclHandle(THIS_HANDLE) ;
}

public String toString () {
   return super.toString() + "  " + fName + "(" + fType + ")";	 //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
}


public boolean isProxy () {
	return fProxyBeanPart!=null ;
}

public void setProxy (BeanPart proxy) {
    
	if(proxy==this) throw new IllegalArgumentException() ;	
	fProxyBeanPart = proxy ;	
	if(fType!=null)
		setType(getType()) ; // Refresh Type
}

public BeanPart getProxy() {
	return fProxyBeanPart ;
}


public void disposeMethod (CodeMethodRef m, IBeanDeclModel model) {
	if (m != null) {
		if (model != null) {
			Collection parts = model.getBeansInitilizedByMethod(m);
			if (parts != null) {
				if (parts.size() < 1 || (parts.size() <= 1 && parts.contains(this))) {
					// no more beans in that method OR I am the last bean
					m.dispose();
				} else {
					// has more beans - dont dispose the method.
					if (fBeanRefExpressions != null)
						for (int i = fBeanRefExpressions.size() - 1; i >= 0; i--) {
							((CodeExpressionRef) fBeanRefExpressions.get(i)).dispose();
						}
					if (fBeanEventExpressions != null)
						for (int i = fBeanEventExpressions.size() - 1; i >= 0; i--) {
							((CodeEventRef) fBeanEventExpressions.get(i)).dispose();
						}
				}
			} else {
				m.dispose();
			}
		} else {
			m.dispose();
		}
	}	
}

public  void dispose() {

    IBeanDeclModel model = fModel ;
    

    if (fFFDecoder!=null)
    	fFFDecoder.dispose() ;
    fFFDecoder=null;
	if (fModel != null)
	  fModel.removeBean(this) ;	
	// TODO: This should not work on the child relationship... need to work 
	//       on the inverse adapter instead
	for (int i = 0; i < fbackReferences.size(); i++) {
		// This should be empty if decoders had the chance to do their thing
		BeanPart bp = (BeanPart) fbackReferences.get(i);	
		Collection beanParts = bp.getRefExpressions();
		beanParts.addAll(bp.getNoSrcExpressions());
		for (Iterator iter = beanParts.iterator(); iter.hasNext();) {
			CodeExpressionRef exp = (CodeExpressionRef) iter.next();
			Object[] added = exp.getAddedInstances();
			if (added!=null)
				for (int j = 0; j < added.length; j++) {
					if (added[j]!=null && added[j].equals(getEObject())) {
						exp.dispose();
						break;
					}
				}
		}
	}
	
	for (int i = 0; i < fBeanInitMethods.size(); i++) 
		disposeMethod((CodeMethodRef) fBeanInitMethods.get(i),model);
	for (int i = 0; i < fEventInitMethods.size(); i++) 
		disposeMethod((CodeMethodRef) fEventInitMethods.get(i),model);
	
	for (int i = 0; i < fNoSrcExpressions.size(); i++) {
		((CodeExpressionRef)fNoSrcExpressions.get(i)).dispose();
	}
	
	for (int i = 0; i < fbackReferences.size(); i++) {
		// This should be empty if decoders had the chance to do their thing
		BeanPart bp = (BeanPart) fbackReferences.get(i);
		bp.removeBackRef(this,true) ;
	}
	

	fBeanInitMethods.clear() ;
	fEventInitMethods.clear() ;
	fBeanRefExpressions.clear() ;
	fBeanEventExpressions.clear() ;
	fBeanReturnMethods.clear() ;
	fbackReferences.clear();
	fChildren.clear() ;
	fNoSrcExpressions.clear();
	if (fEObject != null) {
	  ICodeGenAdapter a = (ICodeGenAdapter)EcoreUtil.getExistingAdapter(fEObject,ICodeGenAdapter.JVE_CODE_GEN_TYPE) ;
	  while (a != null) {
	    fEObject.eAdapters().remove(a);
	    a = (ICodeGenAdapter)EcoreUtil.getExistingAdapter(fEObject,ICodeGenAdapter.JVE_CODE_GEN_TYPE) ;
	  }
	}	
	removeFromJVEModel() ;
	fModel = null ;
	fEObject = null ;	
}

public boolean isEquivalent(BeanPart b) {
   if (b==null) return false ;
   
   if (getSimpleName().equals(b.getSimpleName()) &&
       getType().equals(b.getType()))
       return true ;
   return false ;
}

	/**
	 * TEMPORARY: The BDM's should contain bad expressions in some 
	 *            IGNORE state, instead of just removing them.
	 * Bad expressions are stored for situations which need their
	 * presence - like when import statements are added - When the
	 * Delta bdm and the main bdm are compared we dont know if an
	 * expression is not in the main bdm because it was not added
	 * or because it was undecodeable.
	 * @return List
	 */
	public List getBadExpressions() {
		if(fBadExpressions==null)
			fBadExpressions = new ArrayList();
		return fBadExpressions;
	}

	public void addBadExpresion(CodeExpressionRef fBadExpression) {
		if(fBadExpressions==null)
			fBadExpressions = new ArrayList();
		fBadExpressions.add(fBadExpression);
	}
	
	public void addNoSrcExpresion(CodeExpressionRef exp) {
		if (!fNoSrcExpressions.contains(exp))
		    fNoSrcExpressions.add(exp);
	}
	public void removeNoSrcExpresion(CodeExpressionRef exp) {		
			fNoSrcExpressions.remove(exp);
	}
	
	public void removeAllBadExpressions(){
		if(fBadExpressions!=null)	
			fBadExpressions.clear();
	}

	/**
	 * Returns the isInJVEModel.
	 * @return boolean
	 */
	public boolean isInJVEModel() {
		return isInJVEModel;
	}

	/**
	 * Sets the isInJVEModel.
	 * @param isInJVEModel The isInJVEModel to set
	 */
	public void setIsInJVEModel(boolean isInJVEModel) {
		this.isInJVEModel = isInJVEModel;
	}

public EObject createEObject() throws CodeGenException {
	if (getEObject() == null) {
	  EObject obj = CodeGenUtil.createInstance(getType(),getModel().getCompositionModel()) ;
	  setEObject(obj) ;
	}
	return getEObject() ;
}
	
/**
 * Add the Instance associated with bp to the JVE model so that it has an owner.
 * This will not set the bsc components or this references.
 */
public   void addToJVEModel() throws CodeGenException {
	if(isInJVEModel) return ;
	
	for (Iterator itr=getChildren(); itr.hasNext();) {
		BeanPart bp = (BeanPart) itr.next() ;
		bp.addToJVEModel() ;
	}
	if (isInJVEModel() || isProxy() || getModel() == null || 
	     getModel().isStateSet(IBeanDeclModel.BDM_STATE_SNIPPET)) 
	     return ;
	
	BeanSubclassComposition bsc = getModel().getCompositionModel().getModelRoot() ;	
	boolean thisPart = getSimpleName().equals(BeanPart.THIS_NAME) ;
	
	if (getEObject() == null) 
	    createEObject() ;
	
	
	CodeMethodRef m = getInitMethod() ;
	
	if (!thisPart) {
		// Need to figure out who owns this bean part,
		// Instance variables are owned/scoped by the composition
		if (isInstanceVar()) {
			//  Composition is the owner		
			bsc.getMembers().add(getEObject());
		}
		else {
			// We better have an initMethod
			m.getCompMethod().getMembers().add(getEObject());
		}
	}
	
	// Hook/Set up the JVE model method if needed
	if (m != null) {
		m.getCompMethod().getInitializes().add(getEObject()) ;
		if (m.equals(getReturnedMethod())) 
		   m.getCompMethod().setReturn(getEObject()) ;
	}
	setIsInJVEModel(true) ;
}


/**
 * Add the Instance associated with bp to the JVE model so that it has an owner.
 * This will not set the bsc components references.
 */
public   void removeFromJVEModel()  {
	
	if (!isInJVEModel() || getEObject()==null || isProxy() || getModel() == null || getModel().isStateSet(IBeanDeclModel.BDM_STATE_SNIPPET)) return ;
	
	BeanSubclassComposition bsc = getModel().getCompositionModel().getModelRoot() ;	
	boolean thisPart = false ;
		
	if (getSimpleName().equals(THIS_NAME)) {
	    thisPart = true ;
	}
	
	CodeMethodRef m = getInitMethod() ;
	
	// Need to figure out who owns this bean part,
	// Instance variables are owned/scoped by the composition
	if (thisPart) {
	    bsc.setThisPart(null) ;	           	           		
	}else if (isInstanceVar()) {
		//  Composition is the owner		
		bsc.getMembers().remove(getEObject()) ;
	}
	else {
		// We better have an initMethod
		if (m != null)
	        m.getCompMethod().getMembers().remove(getEObject()) ;
	}
	
	// Hook/Set up the JVE model method if needed
	if (m != null) {
		m.getCompMethod().getInitializes().remove(getEObject()) ;
		if (m.equals(getReturnedMethod())) 
		   m.getCompMethod().setReturn(null) ;
	}	
	getModel().getCompositionModel().getModelRoot().getComponents().remove(getEObject());
	setIsInJVEModel(false) ;
}

	/**
	 * Returns the settingProcessingRequired.
	 * @return boolean
	 */
	public boolean isSettingProcessingRequired() {
		return fSettingProcessingRequired;
	}

	/**
	 * Sets the settingProcessingRequired.
	 * @param settingProcessingRequired The settingProcessingRequired to set
	 */
	public void setSettingProcessingRequired(boolean settingProcessingRequired) {
		fSettingProcessingRequired = settingProcessingRequired;
	}
	
	public boolean isInitMethod(MethodDeclaration method) {
		MethodDeclaration md = getInitMethod() == null ? null : getInitMethod().getDeclMethod();
		if (md == null || method == null)
			return false;

		if (method.getName().getIdentifier().equals(md.getName().getIdentifier())) {
			if (md.parameters().size() != method.parameters().size())
				return false;
			else
				return true ;//TODO Need to be more specif

		}
		return (false);
	}

	/**
	 * @return
	 */
	public boolean isInstanceInstantiation() {
		return fisInstanceInstantiation;
	}

	/**
	 * @param b
	 */
	public void setInstanceInstantiation(boolean b) {
		fisInstanceInstantiation = b;
	}

	/**
	 * @return
	 */
	public EObject getContainer() {
		return fContainer;
	}

}