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
 *  $RCSfile: BeanPart.java,v $
 *  $Revision: 1.33 $  $Date: 2005-04-13 14:33:33 $ 
 */
import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cdm.Annotation;
import org.eclipse.ve.internal.cdm.VisualInfo;

import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.jcm.BeanSubclassComposition;

import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.java.rules.IParentChildRelationship;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public class BeanPart {
    
    public final static String         THIS_NAME = "this" ; //$NON-NLS-1$
    public final static String		 THIS_HANDLE = "_this_Annotation_handle"; //$NON-NLS-1$
    
	
	
	BeanPartDecleration fDecleration = null;	
	ArrayList	    fBeanInitMethods = new ArrayList () ;			// JCMMethod/s where the Bean is created
	ArrayList      	fBeanRefExpressions =  new ArrayList () ;		// JCMMethod/s which update bean attributes
	ArrayList       fBeanEventExpressions = new ArrayList() ;
	ArrayList		fBeanReturnMethods = new ArrayList () ;		// Metods/s which return the value of this bean
	ArrayList		fEventInitMethods = new ArrayList() ;
	ArrayList		fCallBackExpressions = new ArrayList() ;
	ArrayList		fNoSrcExpressions = new ArrayList();
	ArrayList		fparentExpressions = new ArrayList() ;     // Expressions that needs to move to a parent
	EObject   		fEObject = null ;							// Mof Instance of this Bean
	ArrayList    	fbackReferences = new ArrayList() ;		// Mof Object that contains this object 
	EObject			fContainer = null ;                        //  Parent (Container) of this object - null ? part of Composition
	ArrayList      	fChildren = new ArrayList () ;				// Beans this part may contain components	    
    BeanPart    	fProxyBeanPart = null ;					// This bean part is not in the BeanDecModel    
    boolean			fisInstanceInstantiation = false ;         // Is this bean part initilized with its decleration ?
    FreeFormAnnoationDecoder fFFDecoder = null ;				// Responsible parse/generate FF tags   
    List        	fBadExpressions = null;
    boolean			isInJVEModel = false ;
    boolean			fSettingProcessingRequired = false ;
    int				uniqueIndex = 0;
    

/**
 *  Construct a BeanPart for a bean discovered from C. Model
 */
public  BeanPart (BeanPartDecleration decl) 	 {
	 fDecleration = decl;  
	 decl.addBeanPart(this);
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
          if(fDecleration.isInstanceVar())
          	fFFDecoder = new FreeFormAnnoationDecoder(this) ;
          else
          	fFFDecoder = new FreeFormInnerVariableAnnotationDecoder(this);
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
            if (va != null) {
              vi.eAdapters().remove(va) ;              
            }
            vi.eAdapters().add(a) ;
        }           
    }

    return fFFDecoder ;    
}

public void setFieldDeclHandle(String handle) {
    fDecleration.setFieldDeclHandle(handle);
}

public String getFieldDeclHandle() {
    return fDecleration.getFieldDeclHandle();
}

/**
 *   Add a method where the bean is created
 */
public  void addInitMethod(CodeMethodRef methodRef) {
	if (!fBeanInitMethods.contains(methodRef))
	   fBeanInitMethods.add(methodRef) ;
	if (fDecleration.getModel() != null) {
		fDecleration.getModel().addMethodInitializingABean(methodRef);
	   methodRef.setModel(fDecleration.getModel()) ;
	   fDecleration.getModel().updateBeanNameChange(this);
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
	if (fDecleration.getModel() != null)
	  try {
	  	fDecleration.getModel().addMethodReturningABean(methodRef.getMethodName(),getUniqueName()) ;
	   methodRef.setModel(fDecleration.getModel()) ;
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
	return fDecleration.getName() ;
}



public String getUniqueName() {
	String name = fDecleration.getUniqueHandle(this);
	if (uniqueIndex>0)
		name+="{"+uniqueIndex+"}";
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
	return fDecleration.getType() ;
}

/**
 *
 */
public ASTNode getFieldDecl() {
      return fDecleration.getFieldDecl();
}
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
	
	if (fDecleration.getModel() != null)	
		fDecleration.getModel().UpdateRefObjKey(this,old) ;
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
	
	IParentChildRelationship pcRule = (IParentChildRelationship) CodeGenUtil.getEditorStyle(fDecleration.getModel()).getRule(IParentChildRelationship.RULE_ID) ;
		
	if (!fbackReferences.contains(bean))
	   fbackReferences.add(bean) ;
    // Refresh the bean's status
	fDecleration.getModel().addBean(this) ;
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

	if (fDecleration.getModel()==null) return ;
    BeanPart bp = fDecleration.getModel().getABean(bean) ;
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
	  return fDecleration.getModel() ;
}

public void setModel(IBeanDeclModel model) {
    if (fDecleration.getModel() != null && model != null && fDecleration.isInModel()) 
       return ;  // No need to set the type and resolve
	fDecleration.setModel(model) ;
    if (getFieldDeclHandle() == null && model!=null) {
    	IField f = CodeGenUtil.getFieldByName(getSimpleName(),model.getCompilationUnit()) ;
    	if (f !=null)
    	   setFieldDeclHandle(f.getHandleIdentifier()) ;
    }
    else if (getSimpleName().equals(THIS_NAME)) 
        setFieldDeclHandle(THIS_HANDLE) ;
}

public String toString () {
   return super.toString() + "  " + fDecleration.getName() + "(" + fDecleration.getType() + ")";	 //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
}


public boolean isProxy () {
	return fProxyBeanPart!=null ;
}

public void setProxy (BeanPart proxy) {
    
	if(proxy==this) throw new IllegalArgumentException() ;	
	fProxyBeanPart = proxy ;	
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

private boolean isDisposing = false;
public  void dispose() {

	if (isDisposing) return ;
	isDisposing=true;
	
    IBeanDeclModel model = fDecleration.getModel() ;
    

    if (fFFDecoder!=null)
    	fFFDecoder.dispose() ;
    fFFDecoder=null;
	if (model != null)
	  model.removeBean(this) ;	
	
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
	if (isInJVEModel() && !model.isStateSet(IBeanDeclModel.BDM_STATE_UPDATING_DOCUMENT))
	   removeFromJVEModel() ;
	fDecleration.removeBeanPart(this);
	fDecleration = null;
	fEObject = null ;	
}

public boolean isEquivalent(BeanPart b) {
   if (b==null) return false ;
   if(this==b) return true;
   if (getSimpleName().equals(b.getSimpleName()) &&
       getType().equals(b.getType())){
		BeanPartDecleration thisDecl = getDecleration();
		BeanPartDecleration bDecl = b.getDecleration();
		if(thisDecl!=null && bDecl!=null){
			return thisDecl.getBeanPartIndex(this)== bDecl.getBeanPartIndex(b);
		}else{
			return true;
		}

   }
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
	
	/**
	 * During parsing, it is possible that a parent can not resolve a child
	 * relationship associated with an expression.
	 * A potential child will hold on to this expression... assuming that it would be able to resolve
	 * its parent (e.g., SWT constructor will resolve a parent,,, and a call to
	 * a creatChild() for the parent to be resolve to the proper child) with a proper decoder.
	 * 
	 * @param exp un resolved expression
	 */
	public void addParentExpression (CodeExpressionRef exp) {
		if (!fparentExpressions.contains(exp)) 
			fparentExpressions.add(exp) ;		
	}
	public List getParentExpressons () {
		return fparentExpressions;
	}
	/*
	 * During parse time, parent/child expressions with no child indications were 
	 * found by a parent... it is up for each child to determine it is the child.
	 * e.g. createTable() vs. container.addTable()
	 * Only specialize decoders could descide whome the createTable is related to.
	 * During the decode phase, decoders will call this method to try
	 * and resolve this relationship...
	 */
	public void resolveParentExpressions(BeanPart parent) {
		if (parent != null) {
			for (int i = 0; i < fparentExpressions.size(); i++) {
				CodeExpressionRef e = (CodeExpressionRef) fparentExpressions.get(i);
				if (e.getMethod() == parent.getInitMethod() &&
					!e.isStateSet(CodeExpressionRef.STATE_EXIST)) {
					e.setBean(parent);
					parent.addRefExpression(e);
					boolean ok = false;
					try {
						e.setArguments(new Object[] {this} );
						ok = e.decodeExpression();
					} catch (CodeGenException e1) {
					}
					if (!ok) {
						e.getMethod().removeExpressionRef(e);
						e.getBean().removeRefExpression(e);
						e.getBean().addBadExpresion(e);
						if (JavaVEPlugin.isLoggingLevel(Level.FINE))
							JavaVEPlugin.log(
								"BeanPart.resolveParentExpressions() : Did not Decoded: " //$NON-NLS-1$
										+ e, Level.FINE); //$NON-NLS-1$
					}
				} 
			}
		}
		fparentExpressions.clear();		
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
		bp.addToJVEModel();
	}
	if (isInJVEModel() || isProxy() || getModel() == null || 
	     getModel().isStateSet(IBeanDeclModel.BDM_STATE_SNIPPET)) 
	     return ;
	
	BeanSubclassComposition bsc = getModel().getCompositionModel().getModelRoot() ;	
	boolean thisPart = getSimpleName().equals(BeanPart.THIS_NAME) ;
	
	if (getEObject() == null) 
	    createEObject();
	
	
	CodeMethodRef m = getInitMethod() ;
	
	if (!thisPart) {
		// Need to figure out who owns this bean part,
		// Instance variables are owned/scoped by the composition
		if (fDecleration.isInstanceVar()) {
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

	EObject bean = getEObject();
	// Remove from whomever physically owns this bean.
	EcoreUtil.remove(bean);

	// Now remove any still existing pointers to it since it is going away.
	InverseMaintenanceAdapter ai = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(bean, InverseMaintenanceAdapter.ADAPTER_KEY);
	if (ai != null) {
		EReference[] refs = ai.getFeatures();
		for (int i = 0; i < refs.length; i++) {
			EReference ref = refs[i];
			EObject[] srcs = ai.getReferencedBy(ref);
			for (int j = 0; j < srcs.length; j++) {
				EcoreUtil.remove(srcs[j], ref, bean);	
			}
		}
	}

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
		if (method == null)
			return false;
		CodeMethodRef initMethod = getInitMethod();
		if (initMethod == null)
			return false;
		MethodDeclaration md = initMethod.getDeclMethod();
		if (md == null)
			return false;

		if (method.getName().getIdentifier().equals(md.getName().getIdentifier())) {
			if (md.parameters().size() != method.parameters().size())
				return false;
			else
				return true ;//TODO Need to be more specif

		}
		return false;
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

	public BeanPartDecleration getDecleration() {
		return fDecleration;
	}
	public void setBeanPartDecleration(BeanPartDecleration d) {
		fDecleration = d;
		fDecleration.addBeanPart(this);
	}
	public CodeExpressionRef getInitExpression() {
		Iterator itr = getRefExpressions().iterator();
		while (itr.hasNext()) {
			CodeExpressionRef e = (CodeExpressionRef) itr.next();
			if (e.isStateSet(CodeExpressionRef.STATE_INIT_EXPR))
				return e;
		}
		return null;
	}
	public int getUniqueIndex() {
		return uniqueIndex;
	}
	public void setUniqueIndex(int uniqueIndex) {
		this.uniqueIndex = uniqueIndex;
	}
}
