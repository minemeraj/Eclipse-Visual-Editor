/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: BDMMerger.java,v $
 *  $Revision: 1.4 $  $Date: 2004-03-26 23:08:01 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cdm.Annotation;

import org.eclipse.ve.internal.jcm.BeanSubclassComposition;

import org.eclipse.ve.internal.java.codegen.core.CodegenMessages;
import org.eclipse.ve.internal.java.codegen.java.rules.IThisReferenceRule;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
 
/**
 * 
 * @since 1.0.0
 */
public class BDMMerger {
	protected IBeanDeclModel mainModel = null ;
	protected IBeanDeclModel newModel = null ;
	protected boolean isNewModelCompleteCU = false;
	protected Display display = null ;
	
	public BDMMerger(IBeanDeclModel mainModel, IBeanDeclModel newModel, boolean isNewModelCompleteCU, Display display){
		this.mainModel = mainModel;
		this.newModel = newModel;
		this.isNewModelCompleteCU = isNewModelCompleteCU;
		this.display = display;
	}
	
	public boolean merge() throws CodeGenException{
		boolean merged = true;
		if( mainModel != null && newModel != null ){
			merged &= removeDeletedBeans() ;
			merged &= removeDeletedMethods() ;
			merged &= addNewBeans() ;
			merged &= mergeAllBeans() ;
			merged &= updateFreeForm() ;
		}
		return merged ;
	}
	
	
	/**
	 * 
	 * @return
	 * 
	 * @since 1.0.0
	 */
	protected boolean updateFreeForm(){
		try {
			// Decoders have analyzed and acted on the Expressions - 
			// it is time to hook them together withn the Compsition
			// Model
			Iterator itr = mainModel.getBeans().iterator() ;
			while (itr.hasNext()) {
				BeanPart bean = (BeanPart) itr.next() ;
				
				// if a bean was added to a container, the decoder will reflect this in the BeamModel
				
				// Model is build (but annotations).   Turn the model on, as the EditParts may slam dunc
				// new element (e.g., a content pane).  We need to react and generate the appropriate code.
				boolean previousUPDATINGJVEMODELState = mainModel.isStateSet(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL) ;
				boolean previousUPANDRUNNINGState = mainModel.isStateSet(IBeanDeclModel.BDM_STATE_UP_AND_RUNNING) ;
				mainModel.setState(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL, false) ;
				mainModel.setState(IBeanDeclModel.BDM_STATE_UP_AND_RUNNING,true) ;
				
				connectBeanToBSC(bean,mainModel.getCompositionModel().getModelRoot()) ;

				mainModel.setState(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL, previousUPDATINGJVEMODELState) ;
				mainModel.setState(IBeanDeclModel.BDM_STATE_UP_AND_RUNNING, previousUPANDRUNNINGState) ;
				
				if(	bean.getSimpleName().equals(BeanPart.THIS_NAME) ||
						bean.isInstanceVar()){
					if(bean.getFFDecoder()!=null)
						bean.getFFDecoder().decode();
				}
			}
		} catch (CodeGenException e) {
			JavaVEPlugin.log(e, Level.WARNING) ;
			return false;
		}
		return true;
	}

	protected void	connectBeanToBSC(BeanPart bp, BeanSubclassComposition bsc) throws CodeGenException {
		boolean thisPart = bp.getSimpleName().equals(BeanPart.THIS_NAME) ? true : false ;

		if(!bp.isInJVEModel())
			bp.addToJVEModel() ;
		if (thisPart) {
			//TODO Is this statement needed ? if (!bsc.eIsSet(JCMPackage.eINSTANCE.getBeanSubclassComposition_ThisPart()))
			if(bsc.getThisPart()==null || !bsc.getThisPart().equals(bp.getEObject()))
				bsc.setThisPart((IJavaObjectInstance)bp.getEObject()) ;	 
		}else 
			if( bp.getContainer()==null && 
				bp.isInstanceVar() && 
				!bsc.getComponents().contains(bp.getEObject()))
			bsc.getComponents().add(bp.getEObject()) ; 
	}
	
	protected boolean removeMethodRef(final CodeMethodRef m){
		if(m != null ){
			Collection initBPs = mainModel.getBeansInitilizedByMethod(m);
			BeanPart retBP = mainModel.getBeanReturned(m.getMethodName());
			for (Iterator iter = initBPs.iterator(); iter.hasNext();) {
				BeanPart bp = (BeanPart) iter.next();
				logFiner("Disposing init bean "+bp.getSimpleName()+" when disposing method "+m.getMethodHandle());
				bp.dispose() ;
			}
			if(retBP!=null && !initBPs.contains(retBP)){
				logFiner("Disposing return bean "+retBP.getSimpleName()+" when disposing method "+m.getMethodHandle());
				retBP.dispose() ;
			}
			m.dispose() ;
			return true ;
		}
		return false ;
	}
	
	protected boolean removeDeletedMethods(){
		boolean removed = true ;
		Iterator methods = mainModel.getAllMethods();
		while(methods.hasNext()){
			CodeMethodRef m = (CodeMethodRef) methods.next();
			if(newModel.getMethod(m.getMethodHandle())==null){
				// Method is not to be found in the new model - hence remove it
				// Could be removed because methods got merged
				logFiner("Removing method "+m.getMethodHandle() + "since it is not found in update");
				removed &= removeMethodRef(m);
			}
		}
		return removed ;
	}
	
	protected HashMap getUniquenameToBeanMap(List beanParts){
		HashMap map = new HashMap(beanParts.size());
		Iterator bpItr = beanParts.iterator();
		while (bpItr.hasNext()) {
			BeanPart bp = (BeanPart) bpItr.next();
			map.put(bp.getUniqueName(), bp);
		}
		return map;
	}
	
	protected boolean updateMethodOffset(CodeMethodRef mainMethod, CodeMethodRef updatedMethod){
		if(mainMethod==null || updatedMethod==null)
			return false ;
		if(mainMethod.getOffset() != updatedMethod.getOffset())
			mainMethod.setOffset(updatedMethod.getOffset());
		return true ;
	}
	
	protected void updateReturnMethod(BeanPart mainBP, BeanPart updatedBP){
		CodeMethodRef retMainBP = mainBP.getReturnedMethod();
		CodeMethodRef retUpdatedBP = updatedBP.getReturnedMethod();
		if(retMainBP==null && retUpdatedBP==null)
			return ;
		if(retMainBP==null && retUpdatedBP!=null){
			CodeMethodRef mainRetM = mainModel.getMethod(retUpdatedBP.getMethodHandle());
			if(mainRetM!=null){
				mainBP.addReturnMethod(mainRetM);
			}
			return ;
		}
		if(retMainBP!=null && retUpdatedBP==null){
			mainBP.removeReturnMethod(retMainBP);
			return ;
		}
		if(!retMainBP.getMethodHandle().equals(retUpdatedBP.getMethodHandle())){
			mainBP.removeReturnMethod(retMainBP);
			CodeMethodRef mainRetM = mainModel.getMethod(retUpdatedBP.getMethodHandle());
			if(mainRetM!=null){
				mainBP.addReturnMethod(mainRetM);
			}
			return ;
		}
	}
	
	protected boolean updateBeanPart(BeanPart mainBeanPart, BeanPart updatedBeanPart) {
		updateMethodOffset(mainBeanPart.getInitMethod(), updatedBeanPart.getInitMethod()) ;
		updateReturnMethod(mainBeanPart, updatedBeanPart);
		List allMainBPExpressions = new ArrayList(mainBeanPart.getRefExpressions()) ;
		List allUpdateBPExpressions = new ArrayList(updatedBeanPart.getRefExpressions()) ;
		allMainBPExpressions.addAll(mainBeanPart.getRefEventExpressions()) ;
		allUpdateBPExpressions.addAll(updatedBeanPart.getRefEventExpressions()) ;
		boolean regular = processExpressions(allMainBPExpressions, allUpdateBPExpressions) ;
		return regular ;
	}

	protected boolean processEquivalentExpressions(final CodeExpressionRef mainExp, final CodeExpressionRef newExp, int equivalencyLevel){
		newExp.getBean().setProxy(mainExp.getBean());
		switch(equivalencyLevel){
			case 0:
				logFiner("Updating changed expression "+newExp.getCodeContent());
				if(!mainExp.isStateSet(CodeExpressionRef.STATE_INIT_EXPR)){
					if(mainExp.getOffset()!=newExp.getOffset())
						mainExp.setOffset(newExp.getOffset());
					mainExp.refreshFromJOM(newExp);
				}
			   break;
			case 1:
				logFiner("Updating identical expression "+ newExp.getCodeContent());
				if(mainExp.getOffset()==newExp.getOffset()){
					// Absolutely no change, even in location
					mainExp.setContent(newExp.getContentParser())  ;
				}else{
					// Offset has been changed - might have to decode it as it might contain
					// expression ordering in it. Ex: add(comp1); add(comp2) etc.
					mainExp.setOffset(newExp.getOffset());
					// No need to refresh when a shadow expression 
					// We also do not care about event ordering
					if(!newExp.isStateSet(CodeExpressionRef.STATE_SHADOW) &&
						!(newExp instanceof CodeEventRef))
					   // Will take care of reordering of expressions
					   mainExp.refreshFromJOM(newExp); 
				}
				break;
		}
		newExp.getBean().setProxy(null);
		return true;
	}

	protected void removeDeletedExpression(final CodeExpressionRef deletedExp){
		logFiner("Remove deleted expression "+deletedExp.getCodeContent());
		deletedExp.dispose() ;
	}

	protected boolean processExpressions(Collection mainExpressions, Collection updatedExpressions) {
		boolean processed = true;
		List mainExpTobeProcessed = new ArrayList(mainExpressions);
		List updatedExpTobeProcessed = new ArrayList(updatedExpressions);
		
		for(int mainExpCount = 0; mainExpCount < mainExpTobeProcessed.size(); mainExpCount++ ){
			CodeExpressionRef mainExp = (CodeExpressionRef) mainExpTobeProcessed.get(mainExpCount);
			boolean equivalentExpFound = false ;
			for (int updatedExpCount = 0 ; updatedExpCount < updatedExpTobeProcessed.size(); updatedExpCount++) {
				CodeExpressionRef updExp = (CodeExpressionRef) updatedExpTobeProcessed.get(updatedExpCount);
				if (mainExp != null && updExp != null && !updExp.isStateSet(CodeExpressionRef.STATE_EXP_IN_LIMBO)) {
					int equivalency = -1;
					try {
						equivalency = mainExp.isEquivalent(updExp) ;
					} catch (CodeGenException e) {} 
					if ( equivalency < 0) 
						continue ; // Not the same expressions
					equivalentExpFound = true;
					processed = processed & processEquivalentExpressions(mainExp, updExp, equivalency);
					mainExpTobeProcessed.remove(mainExpTobeProcessed.indexOf(mainExp)) ;
					updatedExpTobeProcessed.remove(updatedExpTobeProcessed.indexOf(updExp)) ;
					mainExpCount -- ;
					updatedExpCount -- ;
					break;
				}
			}
			if(!equivalentExpFound){
				// No Equivalent expression was found - delete it
				mainExpTobeProcessed.remove(mainExpTobeProcessed.indexOf(mainExp)) ;
				mainExpCount -- ;
				removeDeletedExpression(mainExp);
			}
		}
		
		// Now add the newly added expressions
		for (int newExpCount = 0; newExpCount < updatedExpTobeProcessed.size(); newExpCount++) {
			CodeExpressionRef exp = (CodeExpressionRef) updatedExpTobeProcessed.get(newExpCount);
			processed = processed & addNewExpression(exp);
		}
		return processed;
	}

	private List getExpressions(CodeMethodRef m) {
		List l = new ArrayList() ;
		Iterator itr = m.getAllExpressions() ;
		while (itr.hasNext())
			l.add(itr.next()) ;					
		return l;
	}
	
	/**
	 * Get the corresponding expression
	 */
	private CodeExpressionRef getMainBDMExpression(CodeExpressionRef exp) throws CodeGenException {
		if(exp==null || exp.getMethod()==null)
			return null ;
		CodeMethodRef m = mainModel.getMethod(exp.getMethod().getMethodHandle());
		if (m == null)
			return null;
		Iterator itr = getExpressions(m).iterator();
		CodeExpressionRef result = null;
		// Look for the best matched expression
		while (itr.hasNext()) {
			CodeExpressionRef e = (CodeExpressionRef) itr.next();
			if (e == exp) {
				result = e;
				break;
			}
			else {
				int eqv = e.isEquivalent(exp);
				if (eqv < 0) {
					// no match
					continue;
				}
				else {

					if (eqv == 1) {
						// Found a match, stop looking
						result = e;
						break;
					}
					else
						if (result==null)
							// get the first partial match
							result = e ;
				}

			}
		}
		if (result != null) {
			// Do not consider this expression anymore
			getExpressions(m).remove(result);
			return result;
		}
		return null;
	}
	
	/**
	 *  Generate an expression that is hooked into the main BDM
	 */
	private CodeEventRef createNewEventExpression(CodeEventRef e, CodeMethodRef m, boolean decode) throws CodeGenException{
		if (getMainBDMExpression(e) != null) throw new CodeGenException("duplicate Expression") ; //$NON-NLS-1$
		BeanPart b = getMainBDMBean(e.getBean()) ;
		if (b == null) throw new CodeGenException("No Bean Part") ; //$NON-NLS-1$
		CodeEventRef newe = new CodeEventRef(m, b);
		newe.setState(CodeExpressionRef.STATE_NO_MODEL, e.isStateSet(CodeExpressionRef.STATE_NO_MODEL));
		newe.setState(CodeExpressionRef.STATE_INIT_EXPR, e.isStateSet(CodeExpressionRef.STATE_INIT_EXPR));
		newe.setState(CodeExpressionRef.STATE_SRC_LOC_FIXED, true); //fexpStmt.getState() | fexpStmt.STATE_SRC_LOC_FIXED) ;
		newe.setExprStmt(e.getExprStmt()) ;
		newe.setContent(e.getContentParser()) ;
		newe.setOffset(e.getOffset()) ;
		newe.setEventInvocation(e.getEventInvocation());
		if (decode) {
			if (!newe.decodeExpression()) {
				newe.dispose() ;
				newe = null ;
			}
		}
		return newe ;
	}

	/**
	 *  Generate an expression that is hooked into the main BDM
	 */
	private CodeExpressionRef createNewExpression(CodeExpressionRef e, CodeMethodRef m, boolean decode) throws CodeGenException{
		if (getMainBDMExpression(e) != null) throw new CodeGenException("duplicate Expression") ; //$NON-NLS-1$
		BeanPart b = getMainBDMBean(e.getBean()) ;
		if (b == null) throw new CodeGenException("No Bean Part") ; //$NON-NLS-1$
		ExpressionRefFactory gen = new ExpressionRefFactory(b,null) ;
		CodeExpressionRef newe = gen.createFromSource(e,m) ;
		newe.setState(CodeExpressionRef.STATE_NO_MODEL, e.isStateSet(CodeExpressionRef.STATE_NO_MODEL));
		newe.setState(CodeExpressionRef.STATE_INIT_EXPR, e.isStateSet(CodeExpressionRef.STATE_INIT_EXPR));
		if (decode) {
			if (!newe.decodeExpression()) {
				newe.dispose() ;
				newe = null ;
			}
		}
		return newe ;
	}
	
	protected boolean addNewExpression(final CodeExpressionRef updateExp) {
		logFiner("Adding new expression "+updateExp.getCodeContent());
		try {
			// Potentially for now, we do not decode if this flag is set. The expression
			// could still have a decoder though - for reasons like priority etc. 
			if(updateExp==null || updateExp.getMethod()==null)
				return true;
			CodeMethodRef mainMethod = mainModel.getMethod(updateExp.getMethod().getMethodHandle()) ;
			if(mainMethod==null)	
				return true;
			CodeExpressionRef newExp = createNewExpression(updateExp,mainMethod,!updateExp.isStateSet(CodeExpressionRef.STATE_NO_MODEL));//((dExp.getState() & dExp.STATE_NO_OP) != dExp.STATE_NO_OP)) ; 
			if(newExp==null && updateExp instanceof CodeEventRef)
				newExp = createNewEventExpression((CodeEventRef)updateExp,mainMethod,!updateExp.isStateSet(CodeExpressionRef.STATE_NO_MODEL));
		} catch (CodeGenException e) {
			JavaVEPlugin.log(e, Level.WARNING) ;
			return false;
		}
		return true;
	}
	
	/**
	 * Get the coresponding BeanPart
	 */
	private BeanPart getMainBDMBean (BeanPart b) {
		Iterator itr = mainModel.getBeans().iterator() ;
		while (itr.hasNext()) {
			BeanPart bean = (BeanPart) itr.next() ;
			if (bean.isEquivalent(b)) 
				if(bean.getUniqueName().equals(b.getUniqueName()))
					return bean ;
		}
		return null ;
	}
	
	protected boolean mergeAllBeans(){
		boolean merge = true ; 
		List mainModelBeans = mainModel.getBeans() ;
		
		// Update changed bean parts
		Iterator mainModelBeansItr = mainModelBeans.iterator();
		while (mainModelBeansItr.hasNext()) {
			BeanPart mainBP = (BeanPart) mainModelBeansItr.next();
			BeanPart updateBP ;
			if((updateBP = newModel.getABean(mainBP.getUniqueName())) != null){
				merge = merge & updateBeanPart(mainBP, updateBP);
			}else{
				JavaVEPlugin.log("BDM Merger: Unable to find main BDM bean in new BDM at this point", Level.WARNING);
			}
		}
		return merge;
	}
	
	protected ISourceRange createSourceRange(final int offset, final int len){
		return new ISourceRange(){
			public int getLength() {
				return len;
			}
			public int getOffset() {
				return offset ;
			}
		} ; 
	}
	
	/*
	 * Tries to instantiate an EObject for the beanpart, and returns all the error 
	 * beanparts which should be disposed becuase the EObject couldnt be 
	 * instantiated.
	 */
	protected List createNewBeanJavaInstance(BeanPart newBP) {
		List err = new ArrayList();
		try {
			BeanSubclassComposition comp = mainModel.getCompositionModel().getModelRoot();
			EObject obj = newBP.createEObject();
			String annotatedName = newBP.getSimpleName();

			// The Model Builder will clean up irrelevent beans
			if (!newBP.getSimpleName().equals(BeanPart.THIS_NAME)) {
				if (!(obj instanceof IJavaObjectInstance)) {
					obj = null;
					JavaVEPlugin.log("Bad Object: " + newBP.getType() + ": " + newBP.getUniqueName(), Level.WARNING); //$NON-NLS-1$ //$NON-NLS-2$
				}
			} else { // a this part
				if (obj != null) {
					((XMIResource)comp.eResource()).setID(obj,MessageFormat.format(BeanPart.THIS_NAME+CodegenMessages.getString("CodegenMessages.ThisPart.uriID"),new Object[] {mainModel.getWorkingCopyProvider().getFile().getFullPath().toString()})) ; //$NON-NLS-1$
					// If no annotation, the PS will not allow you to edit the name in composition
					annotatedName = null;
				}
			}

			if (obj == null) {
				JavaVEPlugin.log("Could not create a JavaObjectInstance for: " + newBP.getType() + ": " + newBP.getUniqueName(), Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
				err.add(newBP);
				// Children will not be connected to the VCE model
				Iterator bItr = newBP.getChildren();
				if (bItr != null)
					while (bItr.hasNext())
						err.add(bItr.next());
				// Remove from the JVE model if needed
				newBP.setEObject(null);
			} else {
				Annotation an = CodeGenUtil.addAnnotation(obj);
				if (annotatedName != null)
					CodeGenUtil.addAnnotatedName(an, annotatedName);
				comp.getAnnotations().add(an);
				try {
					BeanPartFactory.updateInstanceInitString(newBP);
				} catch (IllegalArgumentException e) {
					JavaVEPlugin.log(e, Level.FINE);
					if (!err.contains(newBP)) {
						err.add(newBP);
						// Children will not be connected to the VCE model
						Iterator bItr = newBP.getChildren();
						if (bItr != null)
							while (bItr.hasNext())
								err.add(bItr.next());
					}
				}
			}
		} catch (CodeGenException e) {
			JavaVEPlugin.log("Exception when creating EObject for BeanPart "+newBP.getType()+" with message "+e.getMessage(), Level.WARNING) ;
		}
		return err;
	}
	
	protected boolean addNewBean(final BeanPart referenceBP){
		// New bean - add it and any methods associated with it
		BeanPart newBP ;
		if (referenceBP.getFieldDecl() instanceof FieldDeclaration)
			newBP = new BeanPart((FieldDeclaration)referenceBP.getFieldDecl()) ;
		else
			newBP = new BeanPart((VariableDeclarationStatement)referenceBP.getFieldDecl());
		newBP.setInstanceInstantiation(referenceBP.isInstanceInstantiation()) ;
		newBP.setInstanceVar(referenceBP.isInstanceVar()) ;
		newBP.setModel(mainModel) ;
		newBP.setIsInJVEModel(false) ;
		mainModel.addBean(newBP);
		logFiner("Created new BP "+newBP.getSimpleName());
		
		CodeMethodRef initMethod = null ;
		if( (initMethod = mainModel.getMethod(referenceBP.getInitMethod().getMethodHandle())) == null ){
			// Init method of the bean is not present in the main model - 
			// add it to the main model, and hook the bean part up.
			CodeMethodRef updatedMethodRef = referenceBP.getInitMethod() ;
			initMethod =  new CodeMethodRef(
					updatedMethodRef.getDeclMethod(), 
					mainModel.getTypeRef(), 
					updatedMethodRef.getMethodHandle(),
					createSourceRange(updatedMethodRef.getOffset(), updatedMethodRef.getLen()), 
					updatedMethodRef.getContent()) ; 
			
			initMethod.setModel(mainModel) ;
			
			BeanPart updateReturnedBP = newModel.getBeanReturned(updatedMethodRef.getMethodName());
			if(updateReturnedBP!=null){
				BeanPart mainReturnedBP = mainModel.getABean(updateReturnedBP.getUniqueName());
				if(mainReturnedBP!=null){
					// we habe a bean which is returned with this method - hook them up
					mainReturnedBP.addReturnMethod(initMethod);
				}
			}
			
			logFiner("Created new init method "+initMethod.getMethodHandle()+" for new bean part"+newBP.getSimpleName());
		}
		newBP.addInitMethod(initMethod);
		
		CodeMethodRef referenceReturnMethod = referenceBP.getReturnedMethod();
		if(referenceReturnMethod!=null){
			CodeMethodRef retMethod = mainModel.getMethod(referenceReturnMethod.getMethodHandle());
			if(retMethod!=null)
				newBP.addReturnMethod(retMethod);
		}
		
		List errorBeans = createNewBeanJavaInstance(newBP) ;
		if(errorBeans.size()>0){
			// Error instantiaging the bean - error beans returned
			for (Iterator iter = errorBeans.iterator(); iter.hasNext();) {
				BeanPart errBP = (BeanPart) iter.next();
				errBP.dispose() ;
			}
		}else{
			// Instatiation went on successfully
		}
		return true;
	}
	
	protected void logFiner(String message){
		JavaVEPlugin.log("BDM Merger >> "+message, Level.FINER);
	}
	
	protected boolean createThisBean(final BeanPart referenceBP){
		mainModel.setTypeDecleration(referenceBP.getModel().getTypeDecleration());
		IThisReferenceRule thisRule = (IThisReferenceRule) CodeGenUtil.getEditorStyle(mainModel).getRule(IThisReferenceRule.RULE_ID) ;
		String typeName = referenceBP.getModel().resolveThis() ;
		String superName = null ;
		if (referenceBP.getModel().getTypeRef().getTypeDecl().getSuperclass() != null)
			superName = CodeGenUtil.resolve(referenceBP.getModel().getTypeRef().getTypeDecl().getSuperclass(),referenceBP.getModel()) ;
		ResourceSet rs = mainModel.getCompositionModel().getModelResourceSet() ;
		// The rule uses MOF reflection to introspect attributes : this works when the file is saved at this point.
		// So, try the super first    
		if ((superName!= null && (thisRule.useInheritance(superName,rs)) || thisRule.useInheritance(typeName,rs))) {
			BeanPartFactory bpg = new BeanPartFactory(mainModel,null) ;
			// No Init method yet.
			BeanPart thisBP = bpg.createThisBeanPartIfNeeded(null) ;
			logFiner("Successfully created this bean part : "+ thisBP.getSimpleName());

			CodeMethodRef initMethod = null ;
			if( (initMethod = mainModel.getMethod(referenceBP.getInitMethod().getMethodHandle())) == null ){
				// Init method of the bean is not present in the main model - 
				// add it to the main model, and hook the bean part up.
				CodeMethodRef updatedMethodRef = referenceBP.getInitMethod() ;
				initMethod =  new CodeMethodRef(
						updatedMethodRef.getDeclMethod(), 
						mainModel.getTypeRef(), 
						updatedMethodRef.getMethodHandle(),
						createSourceRange(updatedMethodRef.getOffset(), updatedMethodRef.getLen()), 
						updatedMethodRef.getContent()) ; 
				initMethod.setModel(mainModel) ;
				logFiner("Successfully created init method for this bean part "+initMethod.getMethodHandle());
			}
			thisBP.addInitMethod(initMethod);
			
			List errorBeans = createNewBeanJavaInstance(thisBP);
			if(errorBeans.size()>0){
				// Error instantiaging the bean - error beans returned
				for (Iterator iter = errorBeans.iterator(); iter.hasNext();) {
					BeanPart errBP = (BeanPart) iter.next();
					logFiner("Disposing bean part "+errBP.getSimpleName()+" when createing java instance for "+referenceBP.getSimpleName());
					errBP.dispose() ;
				}
			}else{
				// Instatiation went on successfully
			}
		}
		return true ;
	}	
	
	protected boolean addNewBeans(){
		boolean add = true ;
		Iterator newBeansItr = newModel.getBeans().iterator();
		while (newBeansItr.hasNext()) {
			final BeanPart beanPart = (BeanPart) newBeansItr.next();
			if( mainModel.getABean(beanPart.getUniqueName()) == null &&
				beanPart.getInitMethod()!=null){
				if(beanPart.getSimpleName().equals(BeanPart.THIS_NAME))
					add &= createThisBean(beanPart);
				else
					add &= addNewBean(beanPart) ;
			}
		}
		return add ;
	}
	
	protected boolean removeDeletedBean(final BeanPart b){
		boolean remove = false ; 
		if( b != null ){
			b.dispose();
			remove = true ;
		}
		return remove ;
	}
	
	/**
	 * Remove all beans in main model which are not present 
	 * 
	 * @return
	 * 
	 * @since 1.0.0
	 */
	protected boolean removeDeletedBeans(){
		boolean removed = true ;
		
		Iterator mainBeansItr = mainModel.getBeans().iterator();
		while(mainBeansItr.hasNext()){
			BeanPart mainBean = (BeanPart) mainBeansItr.next();
			if(newModel.getABean(mainBean.getUniqueName())==null){
				// Bean has been removed - hence remove
				logFiner("Removing deleted bean "+ mainBean.getSimpleName());
				removed &= removeDeletedBean( mainBean ); 
			}else{
				// Remove bean if type has changed
				BeanPart newBean = newModel.getABean(mainBean.getUniqueName());
				boolean isThisPart = mainBean.getSimpleName().equals(BeanPart.THIS_NAME);
				String mainType = isThisPart?CodeGenUtil.resolve(mainBean.getModel().getTypeDecleration().getSuperclass(),mainBean.getModel()) : mainBean.getType();
				String newType = isThisPart?CodeGenUtil.resolve(newBean.getModel().getTypeDecleration().getSuperclass(),mainBean.getModel()) : newBean.getType();
				boolean typeChanged = !mainType.equals(newType) ;
				if(typeChanged){
					logFiner("Removing changed type bean "+ mainBean.getSimpleName());
					removed &= removeDeletedBean( mainBean );
				}else{
					String mainMethodHandle = mainBean.getInitMethod().getMethodHandle();
					String newMethodHandle = newBean.getInitMethod().getMethodHandle();
					if(mainMethodHandle==null || newMethodHandle==null || !mainMethodHandle.equals(newMethodHandle)){
						logFiner("Removing changed init method bean "+mainBean.getSimpleName());
						removed &= removeDeletedBean( mainBean );
					}
				}
			}
		}
		return removed;
	}
}
