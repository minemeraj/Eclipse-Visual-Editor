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
 *  $Revision: 1.24 $  $Date: 2004-09-09 16:19:27 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cdm.Annotation;

import org.eclipse.ve.internal.jcm.BeanSubclassComposition;

import org.eclipse.ve.internal.java.codegen.core.CodegenMessages;
import org.eclipse.ve.internal.java.codegen.java.rules.IThisReferenceRule;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.codegen.util.TypeResolver.Resolved;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
 
/**
 * 
 * @since 1.0.0
 */
public class BDMMerger {
	protected IBeanDeclModel mainModel = null ;
	protected IBeanDeclModel newModel = null ;
	protected List changedHandles = null;
	
	protected List needToRedecodeExpressions = new ArrayList();
	
	/**
	 * 
	 * @param mainModel
	 * @param newModel
	 * @param changedHandles  the list of changed handles in the CU - the beans effected by these handles will be processed first.
	 * @param isNewModelCompleteCU
	 * @param display
	 * 
	 * @since 1.0.0
	 */
	public BDMMerger(IBeanDeclModel mainModel, IBeanDeclModel newModel, List changedHandles){
		this.mainModel = mainModel;
		this.newModel = newModel;
		this.changedHandles = changedHandles;
		needToRedecodeExpressions.clear();
	}
	
	/**
	 * Tries to merge/update the main BDM with the contents of the new BDM
	 * 
	 * @return Returns whether there was a successful merge.
	 * @throws CodeGenException
	 * 
	 * @since 1.0.0
	 */
	public boolean merge() throws CodeGenException{
		boolean merged = true;
		if( mainModel != null && newModel != null ){
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) return true ;
			merged = merged && removeDeletedBeans() ;
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) return true ;
			merged = merged && removeDeletedMethods() ;
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) return true ;
			merged = merged && updateEventHandlers() ;
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) return true ;
			merged = merged && addNewBeans() ;
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) return true ;
			merged = merged && addThisMethod() ;
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) return true ;
			merged = merged && updateMethods() ;
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) return true ;
			merged = merged && mergeAllBeans() ;
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) return true ;
			merged = merged && updateFreeForm() ;
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) return true ;
			merged = merged && clean() ;
		}
		needToRedecodeExpressions.clear();
		return merged ;
	}
	
	/**
	 * Things which might have gotten bad during the merge need to be cleaned up -
	 * like bad expressions etc.
	 * 
	 * @return
	 * 
	 * @since 1.0.0
	 */
	private boolean clean() {
		boolean cleaned = true;
		if(mainModel.getBeans()!=null){
			Iterator beanItr = mainModel.getBeans().iterator();
			while (beanItr.hasNext()) {
				BeanPart bp = (BeanPart) beanItr.next();
				List badExpressions = bp.getBadExpressions();
				if(badExpressions!=null && badExpressions.size()>0){
					for (int badExpCount = 0; badExpCount < badExpressions.size(); badExpCount++) {
						CodeExpressionRef exp = (CodeExpressionRef) badExpressions.get(badExpCount);
						badExpressions.remove(exp);
						exp.dispose();
						badExpCount--;
					}
				}
			}
		}
		return cleaned;
	}

	/**
	 * Even though a majority of methods in the main BDM  are created when a new bean 
	 * is created, there could be methods in the main BDM which do not init any bean - 
	 * methods like initConnections() which do not have any initializing bean, but contribute
	 * expressions to an existing bean. Hence update all methods in the main BDM with correct
	 * offsets and contents.
	 * 
	 * @return  Returns whether a successful addition of methods has been performed
	 * 
	 * @since 1.0.0
	 */
	protected boolean updateMethods() {
		if(newModel.getTypeRef()!=null){
			Iterator newMethodsItr = newModel.getTypeRef().getMethods();
			while (newMethodsItr.hasNext()) {
				CodeMethodRef updMethod = (CodeMethodRef) newMethodsItr.next();
				CodeMethodRef mainMethod = mainModel.getMethod(updMethod.getMethodHandle());
				if(mainMethod==null){
					// main model doesnt have a method which the updated model has - add it
					createNewMainMethodRef(updMethod);
				}else{
					updateMethodOffsetAndContent(mainMethod, updMethod);
				}
			}
		}
		return true;
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
				if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) return true ;
				BeanPart bean = (BeanPart) itr.next() ;
				
				// if a bean was added to a container, the decoder will reflect this in the BeamModel
				
				// Model is build (but annotations).   Turn the model on, as the EditParts may slam dunc
				// new element (e.g., a content pane).  We need to react and generate the appropriate code.
//				boolean previousUPDATINGJVEMODELState = mainModel.isStateSet(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL) ;
//				boolean previousUPANDRUNNINGState = mainModel.isStateSet(IBeanDeclModel.BDM_STATE_UP_AND_RUNNING) ;
//				mainModel.setState(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL, false) ;
//				mainModel.setState(IBeanDeclModel.BDM_STATE_UP_AND_RUNNING,true) ;
				
				connectBeanToBSC(bean,mainModel.getCompositionModel().getModelRoot()) ;

//				mainModel.setState(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL, previousUPDATINGJVEMODELState) ;
//				mainModel.setState(IBeanDeclModel.BDM_STATE_UP_AND_RUNNING, previousUPANDRUNNINGState) ;
				
				if(bean.getFFDecoder()!=null)
					bean.getFFDecoder().decode();
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
			if(bp.getContainer()==null && bp.isInstanceVar()){
				 if(bp.getFFDecoder().isVisualOnFreeform()){
				 	// should be on the FF
				 	if(!bsc.getComponents().contains(bp.getEObject()))
				 		bsc.getComponents().add(bp.getEObject()) ;
				 }else{
				 	// should NOT be on the FF
				 	if(bsc.getComponents().contains(bp.getEObject()))
				 		bsc.getComponents().remove(bp.getEObject()) ;
				 }
			}
	}
	
	protected boolean removeMethodRef(final CodeMethodRef m){
		if(m != null ){
			Collection initBPs = mainModel.getBeansInitilizedByMethod(m);
			BeanPart retBP = mainModel.getBeanReturned(m.getMethodName());
			for (Iterator iter = initBPs.iterator(); iter.hasNext();) {
				BeanPart bp = (BeanPart) iter.next();
				logFiner("Disposing init bean "+bp.getSimpleName()+" when disposing method "+m.getMethodHandle());
				if(!BeanPart.THIS_NAME.equals(bp.getSimpleName())) // do not dispose THIS beanpart
					bp.dispose() ;
			}
			if(retBP!=null && !initBPs.contains(retBP)){
				logFiner("Disposing return bean "+retBP.getSimpleName()+" when disposing method "+m.getMethodHandle());
				if(!BeanPart.THIS_NAME.equals(retBP.getSimpleName())) // do not dispose THIS beanpart
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
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) return true ;
			CodeMethodRef m = (CodeMethodRef) methods.next();
			if(newModel.getMethod(m.getMethodHandle())==null){
				// Method is not to be found in the new model - hence remove it
				// Could be removed because methods got merged
				logFiner("Removing method "+m.getMethodHandle() + "since it is not found in update");
				removed = removed && removeMethodRef(m);
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
	
	protected boolean updateMethodOffsetAndContent(CodeMethodRef mainMethod, CodeMethodRef updatedMethod){
		if(mainMethod==null || updatedMethod==null)
			return false ;
		if(mainMethod.getOffset() != updatedMethod.getOffset())
			mainMethod.setOffset(updatedMethod.getOffset());
		if(updatedMethod.getContent()!=null && updatedMethod.getContent().equals(mainMethod.getContent()))
			mainMethod.setContent(updatedMethod.getContent());
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
	
	/**
	 * Removes the main BDMs CallBack expression from the main BDM.
	 * 
	 * @param mainExp
	 * @return  Return whether the CallBack expression has been successfully removed.
	 * 
	 * @since 1.0.0
	 */
	protected boolean removeDeletedCallBackExpression(CodeCallBackRef mainExp){
		mainExp.dispose();
		return true;
	}
	
	/**
	 * This method checks 
	 * 
	 * @param mainCBExp
	 * 
	 * @since 1.0.0
	 */
	protected void checkEventExpNeedsRedecoding(CodeCallBackRef mainCBExp){
		if(mainCBExp!=null && mainCBExp.getBean()!=null){
			Collection eventExps = mainCBExp.getBean().getRefEventExpressions();
			for (Iterator iter = eventExps.iterator(); iter.hasNext();) {
				CodeExpressionRef exp = (CodeExpressionRef) iter.next();
				if(!needToRedecodeExpressions.contains(exp))
					needToRedecodeExpressions.add(exp);
			}
		}
	}
	
	/**
	 * Updates the passed in main CallBack expression with the contents of the equivalent passed in 
	 * updated CallBack expression. Both expressions are equivalent with possibilly changed content
	 *  
	 * @param main
	 * @param updExp
	 * @param equivalency
	 * @return  Return whether a successful update on the main CallBack expression has been performed
	 * 
	 * @since 1.0.0
	 */
	protected boolean processSameCallBackExpression(CodeCallBackRef main, CodeCallBackRef updExp){
		if(main.getOffset()!=updExp.getOffset())
			main.setOffset(updExp.getOffset());
		main.setContent(updExp.getContent());
		main.setExprStmt(updExp.getExprStmt());
		return true;
	}
	
	/**
	 * Adds the passed in new CallBack expression to the main BDM. 
	 * 
	 * @param newExp
	 * @return  Returns whether the passed in CallBack expression was successfully added to the mainBDM 
	 * 
	 * @since 1.0.0
	 */
	protected boolean addNewCallBackExpression(CodeCallBackRef newExp){
		boolean added = false;
		CodeMethodRef newMethodRef = newExp.getMethod();
		BeanPart newBean = newExp.getBean();
		if(newMethodRef!=null && newExp!=null){
			CodeMethodRef mainMethod = mainModel.getMethod(newMethodRef.getMethodHandle());
			BeanPart mainBean = mainModel.getABean(newBean.getUniqueName());
			if(mainMethod!=null && mainBean!=null){
				CodeCallBackRef callBack = new CodeCallBackRef(newExp.getExprStmt(), mainMethod);
				callBack.setBean(mainBean);
				checkEventExpNeedsRedecoding(callBack);
				added = true;
			}
		}
		return added;
	}
	
	/**
	 * CallBackRefExpressions are used by the regular and event expressions during decoding. In order for 
	 * the correct decoding of the regular and event expressions, the call back expressions must be merged
	 * in first.
	 * 
	 * @param mainBeanPart
	 * @param updatedBeanPart
	 * @return Return whether a successful update of the main BDM has been performed.
	 * 
	 * @since 1.0.0
	 */
	protected boolean updateCallBackExpressions(BeanPart mainBeanPart, BeanPart updatedBeanPart){
		boolean processed = true;
		List mainCallBackExpressions = new ArrayList(mainBeanPart.getRefCallBackExpressions());
		List updatedCallBackExpressions = new ArrayList(updatedBeanPart.getRefCallBackExpressions());
		
		for(int mainExpCount = 0; mainExpCount < mainCallBackExpressions.size(); mainExpCount++ ){
			CodeCallBackRef mainExp = (CodeCallBackRef) mainCallBackExpressions.get(mainExpCount);
			boolean equivalentExpFound = false ;
			for (int updatedExpCount = 0 ; updatedExpCount < updatedCallBackExpressions.size(); updatedExpCount++) {
				CodeCallBackRef updExp = (CodeCallBackRef) updatedCallBackExpressions.get(updatedExpCount);
				if (mainExp != null && updExp != null && !updExp.isStateSet(CodeExpressionRef.STATE_EXP_IN_LIMBO)) {
					boolean contentSame = false;
					contentSame = mainExp.getContent().equals(updExp.getContent());
					if ( !contentSame ) 
						continue ; // Not the same expressions
					equivalentExpFound = true;
					processed = processed && processSameCallBackExpression(mainExp, updExp);
					mainCallBackExpressions.remove(mainCallBackExpressions.indexOf(mainExp)) ;
					updatedCallBackExpressions.remove(updatedCallBackExpressions.indexOf(updExp)) ;
					mainExpCount -- ;
					updatedExpCount -- ;
					break;
				}
			}
			if(!equivalentExpFound){
				// No Equivalent expression was found - delete it
				mainCallBackExpressions.remove(mainCallBackExpressions.indexOf(mainExp)) ;
				mainExpCount -- ;
				checkEventExpNeedsRedecoding(mainExp);
				removeDeletedCallBackExpression(mainExp);
			}
		}
		
		// Now add the newly added expressions
		for (int newExpCount = 0; newExpCount < updatedCallBackExpressions.size(); newExpCount++) {
			CodeCallBackRef exp = (CodeCallBackRef) updatedCallBackExpressions.get(newExpCount);
			processed = processed && addNewCallBackExpression(exp);
		}
		return processed;
	}
	
	/**
	 * Updates the regular and event expressions of the main BDMs beanpart with the new BDMs beanpart.
	 *  
	 * @param mainBeanPart
	 * @param updatedBeanPart
	 * @return Return whether a successful update of the main BDM has been performed.
	 * 
	 * @since 1.0.0
	 */
	protected boolean updateEventExpressions(BeanPart mainBeanPart, BeanPart updatedBeanPart){
		List allMainBPExpressions = new ArrayList(mainBeanPart.getRefEventExpressions()) ;
		List allUpdateBPExpressions = new ArrayList(updatedBeanPart.getRefEventExpressions()) ;
		return processExpressions(allMainBPExpressions, allUpdateBPExpressions) ;
	}
	
	/**
	 * Updates the beanpart of the main BDM with the contents of the beanpart in the updated BDM.
	 * Things which are updated are:
	 *  # Method offsets of the init method of the beanpart
	 *  # The return method of the beanpart (methods could have merged or split)
	 *  # The call back expressions are updated
	 *  # The regular and event expressions are updated.
	 *  
	 * @param mainBeanPart
	 * @param updatedBeanPart
	 * @return Return whether a successful update of the main BDM has been performed.
	 * 
	 * @since 1.0.0
	 */
	protected boolean updateBeanPart(BeanPart mainBeanPart, BeanPart updatedBeanPart) {
		updateMethodOffsetAndContent(mainBeanPart.getInitMethod(), updatedBeanPart.getInitMethod()) ;
		updateReturnMethod(mainBeanPart, updatedBeanPart);
		boolean update = updateCallBackExpressions(mainBeanPart, updatedBeanPart);
		update = update && updateParentExpressions(mainBeanPart, updatedBeanPart);
		update = update && updateEventExpressions(mainBeanPart, updatedBeanPart);
		return update ;
	}

	/**
	 * Parent expressions are those expressions which actually belong to the passed in 'mainBeanPart',
	 * but which physically exist in the parent's method. Ex: createComposite(); - Here the expression 
	 * is for the 'composite' bean - but it exists in the parent's method like 'createShell'.
	 * The beans in the main BDM will not be having any parent expressions as they would already have
	 * been processed by the initial load (processing parent expressions removes them from the beanparts).
	 * Since there will only be additions from the new BDM, we will try to add the parent expressions of
	 * the new BDM to the main BDM. 
	 * 
	 * @param mainBeanPart
	 * @param updatedBeanPart
	 * @return
	 * 
	 * @since 1.0.0
	 */
	protected boolean updateParentExpressions(BeanPart mainBeanPart, BeanPart updatedBeanPart) {
		boolean updated = true;
		List updatedParentExpressions = new ArrayList(updatedBeanPart.getParentExpressons());
		for (int uc = 0; uc < updatedParentExpressions.size(); uc++) {
			CodeExpressionRef updateParentExpression = (CodeExpressionRef) updatedParentExpressions.get(uc);
			if(		updateParentExpression!=null && updateParentExpression.getExprStmt()!=null &&
					updateParentExpression.getMethod()!=null &&
					updateParentExpression.getMethod().getMethodHandle()!=null){
				CodeMethodRef newExpMethod = mainModel.getMethod(updateParentExpression.getMethod().getMethodHandle());
				if(newExpMethod!=null){
					CodeExpressionRef newExp = new CodeExpressionRef(updateParentExpression.getExprStmt(), newExpMethod);
					mainBeanPart.addParentExpression(newExp);
				}
			}
		}
		return updated;
	}

	protected boolean processEquivalentExpressions(final CodeExpressionRef mainExp, final CodeExpressionRef newExp, int equivalencyLevel){
		if(newExp.getBean()!=null) // Expressions like 'createComposite()' have no bean defined on them - no need of a proxy
			newExp.getBean().setProxy(mainExp.getBean());
		switch(equivalencyLevel){
			case 0:
				logFiner("Updating changed expression "+newExp.getCodeContent());
				if(!mainExp.isStateSet(CodeExpressionRef.STATE_NO_MODEL)){
					if(mainExp.getOffset()!=newExp.getOffset())
						mainExp.setOffset(newExp.getOffset());
					mainExp.refreshFromJOM(newExp);
				}
			   break;
			case 1:
//				logFiner("Updating identical expression "+ newExp.getCodeContent());
				if(mainExp.getOffset()==newExp.getOffset()){
					// Absolutely no change, even in location
					mainExp.setContent(newExp.getContentParser())  ;
					// Code callback refs could have been added/removed/updated - 
					// need to re-decode them if need be.
					if(needToRedecodeExpressions.contains(mainExp))
						mainExp.refreshFromJOM(newExp);
				}else{
					// Offset has been changed - might have to decode it as it might contain
					// expression ordering in it. Ex: add(comp1); add(comp2) etc.
					mainExp.setOffset(newExp.getOffset());
					// No need to refresh when a shadow expression 
					// We also do not care about event ordering
					if(((!(newExp instanceof CodeEventRef)) || needToRedecodeExpressions.contains(mainExp))) {
					   // Will take care of reordering of expressions
					   logFiner("Updating because of changed offset "+newExp.getCodeContent());
					   mainExp.refreshFromJOM(newExp); 
					}
				}
				break;
		}
		if(newExp.getBean()!=null)
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
		
		for (int updatedExpCount = 0 ; updatedExpCount < updatedExpTobeProcessed.size(); updatedExpCount++) {
			CodeExpressionRef updExp = (CodeExpressionRef) updatedExpTobeProcessed.get(updatedExpCount);
			boolean equivalentExpFound = false ;
			for(int mainExpCount = 0; mainExpCount < mainExpTobeProcessed.size(); mainExpCount++ ){
				CodeExpressionRef mainExp = (CodeExpressionRef) mainExpTobeProcessed.get(mainExpCount);
				if (mainExp != null && updExp != null && !updExp.isStateSet(CodeExpressionRef.STATE_EXP_IN_LIMBO)) {
					int equivalency = -1;
					try {
						equivalency = mainExp.isEquivalent(updExp) ;
					} catch (CodeGenException e) {} 
					if ( equivalency < 0) 
						continue ; // Not the same expressions
					equivalentExpFound = true;
					processed = processed && processEquivalentExpressions(mainExp, updExp, equivalency);
					mainExpTobeProcessed.remove(mainExpTobeProcessed.indexOf(mainExp)) ;
					updatedExpTobeProcessed.remove(updatedExpTobeProcessed.indexOf(updExp)) ;
					mainExpCount -- ;
					updatedExpCount -- ;
					break;
				}
			}
			if(!equivalentExpFound){
				// No Equivalent expression was found 
				// Now add the newly added expressions
				updatedExpTobeProcessed.remove(updatedExpTobeProcessed.indexOf(updExp));
				updatedExpCount--;
				processed = processed && addNewExpression(updExp);
			}
		}

		// Now remove the old main expressions
		for (int delExpCount = 0; delExpCount < mainExpTobeProcessed.size(); delExpCount++) {
			CodeExpressionRef mainExp = (CodeExpressionRef) mainExpTobeProcessed.get(delExpCount);
			// Now there are cases where expressions are added to the beanpart after parsing and 
			// during decoding (ex: createTable() - which is added to the Shell beanpart, during the 
			// decoding of the constructor for Table). Such expressions are generally stored in the 
			// beanpart's parent expressions list - so check the parent expressions of all bean parts 
			// to determine if anyone will be adding this expression to the Shell beanpart later on.
			CodeExpressionRef parentExpression = null;
			Iterator newBeansItr = newModel.getBeans().iterator();
			while (newBeansItr.hasNext() && parentExpression==null) {
				BeanPart newBean = (BeanPart) newBeansItr.next();
				List newBPParentExps = newBean.getParentExpressons();
				if(newBPParentExps!=null && newBPParentExps.size()>0){
					for(int ec=0;ec<newBPParentExps.size();ec++){
						CodeExpressionRef newParentExp = (CodeExpressionRef) newBPParentExps.get(ec);
						String mainExpMethodName = mainExp.getMethodNameContent();
						String mainExpInMethod = mainExp.getMethod()==null?null:mainExp.getMethod().getMethodName();
						String newExpInMethod = newParentExp.getMethod()==null?null:newParentExp.getMethod().getMethodName();
						if(mainExpMethodName!=null && mainExpMethodName.equals(newParentExp.getMethodNameContent()) &&
								mainExpInMethod!=null && newExpInMethod!=null && mainExpInMethod.equals(newExpInMethod)){
							parentExpression = newParentExp;
							break;
						}
					}
				}
			}
			if(parentExpression==null)
				removeDeletedExpression(mainExp);
			else
				processEquivalentExpressions(mainExp, parentExpression, 1);
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
		if (getMainBDMExpression(e) != null) {
			logFiner("Ignoring creation of duplicate Expression" + e.getCodeContent()) ; //$NON-NLS-1$
			return null;
		}
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
			CodeExpressionRef newExp = createNewExpression(updateExp,mainMethod,!updateExp.isStateSet(CodeExpressionRef.STATE_NO_MODEL));//((dExp.getState() && dExp.STATE_NO_OP) != dExp.STATE_NO_OP)) ; 
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
	
	/**
	 * Performs a merge on all beans in the main BDM and the updating BDM. At this 
	 * place, the main BDM has all the deleted beans removed, the new beans added, 
	 * the new methods and event handlers added. There should be one-one correspondence
	 * between beans in the main BDM and the update BDM. 
	 *  
	 * @return Returns whether a successful merge has taken place.
	 * 
	 * @since 1.0.0
	 */
	protected boolean mergeAllBeans(){
		boolean merge = true ; 
		List mainModelBeans = mainModel.getBeans() ;
		mainModelBeans = orderBeansToMerge(mainModelBeans);
		// Update changed bean parts
		Iterator mainModelBeansItr = mainModelBeans.iterator();
		// Update all beans EXCEPT the regular expressions - they need to be done in order of expressions in method
		while (mainModelBeansItr.hasNext()) {
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) return true ;
			BeanPart mainBP = (BeanPart) mainModelBeansItr.next();
			// sometimes when statements (createTable()) are disposed in other methods, the beanpart (table)
			// is disposed. Check to see if it is still in the model before proceeding with the merge.
			if(mainBP.getModel()==null) 
				continue;
			BeanPart updateBP ;
			if((updateBP = newModel.getABean(mainBP.getUniqueName())) != null){
				merge = merge && updateBeanPart(mainBP, updateBP);
			}else{
				JavaVEPlugin.log("BDM Merger: Unable to find main BDM bean in new BDM at this point", Level.WARNING);
			}
		}
		
		merge = merge && updateBeanPartRegularExpressions();
		return merge;
	}

	protected void orderBeanPartExpressions(BeanPart bp, List orderedList){
		if(bp==null || bp.getRefExpressions()==null)
			return;
		Iterator bpExpItr = bp.getRefExpressions().iterator();
		while (bpExpItr.hasNext()) {
			CodeExpressionRef bpExp = (CodeExpressionRef) bpExpItr.next();
			int index=0;
			for(index=0;index<orderedList.size();index++){
				CodeExpressionRef orderedExp = (CodeExpressionRef) orderedList.get(index);
				if(orderedExp.getOffset()>bpExp.getOffset())
					break;
			}
			if(index<orderedList.size())
				orderedList.add(index, bpExp);
			else
				orderedList.add(bpExp);
		}
	}
	
	/**
	 * Now regular expressions of all beans are merged in. This special merge process is needed 
	 * to preserve the order of expressions between beans which are in the same method. 
	 * This is important in the case of layout constraints where the constraint needs all its 
	 * properties set, before it is set as the constraint. Else the UI is not reflected correctly.
	 * 
	 * @return
	 * 
	 * @since 1.0.0
	 */
	protected boolean updateBeanPartRegularExpressions(){
		boolean update = true;
		// Update the regular expressions of all beans
		HashMap beansInMethodMap = new HashMap();
		Iterator mainModelBeansItr = mainModel.getBeans().iterator();
		while (mainModelBeansItr.hasNext()) {
			BeanPart bp = (BeanPart) mainModelBeansItr.next();
			String key = "null";
			if(bp.getInitMethod()!=null && bp.getInitMethod().getMethodHandle()!=null)
				key = bp.getInitMethod().getMethodHandle();
			if(!beansInMethodMap.containsKey(key))
				beansInMethodMap.put(key, new ArrayList());
			List list = (List) beansInMethodMap.get(key);
			list.add(bp);
		}
		
		// Now merge the regular expressions, where expressions belong to a particular method
		Iterator methodHandleItr = beansInMethodMap.keySet().iterator();
		while (methodHandleItr.hasNext()) {
			List orderedMainExpressions = new ArrayList();
			List orderedUpdatedExpressions = new ArrayList();
			String key = (String) methodHandleItr.next();
			List beans = (List) beansInMethodMap.get(key);
			Iterator mainBeansItr = beans.iterator();
			// TODO - no need to order for null handles maybe?
			while (mainBeansItr.hasNext()) {
				BeanPart mainBP = (BeanPart) mainBeansItr.next();
				if(mainBP.getModel()==null)
					continue;
				BeanPart updateBP ;
				if((updateBP = newModel.getABean(mainBP.getUniqueName())) != null){

					// Order the bean expressions
					orderBeanPartExpressions(mainBP, orderedMainExpressions);
					orderBeanPartExpressions(updateBP, orderedUpdatedExpressions);
					
				}else{
					JavaVEPlugin.log("BDM Merger: Unable to find main BDM bean in new BDM at this point", Level.WARNING);
				}
			}
			update = update && processExpressions(orderedMainExpressions, orderedUpdatedExpressions);
		}
		return update;
	}
	
	/**
	 * The passed in beans are ordered so that beans which are effected by the 
	 * changed JDT handles in the CU are processed first than other beans. 
	 * 
	 * @param mainModelBeans
	 * @return
	 * 
	 * @since 1.0.0
	 */
	protected List orderBeansToMerge(List mainModelBeans) {
		List orderedBeans = new ArrayList();
		if(mainModelBeans!=null && mainModelBeans.size()>0 && changedHandles!=null && changedHandles.size()>0){
			for (Iterator mBeans = mainModelBeans.iterator(); mBeans.hasNext();) {
				BeanPart mainBean = (BeanPart) mBeans.next();
				if(mainBean!=null){
					if(	changedHandles.contains(mainBean.getFieldDeclHandle()) ||
						(mainBean.getInitMethod()!=null && changedHandles.contains(mainBean.getInitMethod().getMethodHandle())) ||
						(mainBean.getReturnedMethod()!=null && changedHandles.contains(mainBean.getReturnedMethod().getMethodHandle()))){
							orderedBeans.add(0, mainBean);
					}else{
						orderedBeans.add(mainBean);
					}
				}
			}
		}else
			orderedBeans.addAll(mainModelBeans);
		return orderedBeans;
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
			initMethod = createNewMainMethodRef(referenceBP.getInitMethod()) ;
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
		TypeResolver resolver = referenceBP.getModel().getResolver();
		String typeName = resolver.resolveMain().getName();
		Resolved superResolve = null ;
		if (referenceBP.getModel().getTypeRef().getTypeDecl().getSuperclass() != null) { 
			superResolve = resolver.resolveType(referenceBP.getModel().getTypeRef().getTypeDecl().getSuperclass());
		}
		ResourceSet rs = mainModel.getCompositionModel().getModelResourceSet() ;
		// The rule uses MOF reflection to introspect attributes : this works when the file is saved at this point.
		// So, try the super first    
		if ((superResolve != null && (thisRule.useInheritance(superResolve.getName(),rs)) || thisRule.useInheritance(typeName,rs))) {
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
	
	/**
	 * Adds beans which are present with an init method in the new BDM
	 * @since 1.0.0
	 */
	protected boolean addNewBeans(){
		boolean add = true ;
		Iterator newBeansItr = newModel.getBeans().iterator();
		while (newBeansItr.hasNext()) {
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) return true ;
			final BeanPart beanPart = (BeanPart) newBeansItr.next();
			if( mainModel.getABean(beanPart.getUniqueName()) == null &&
				beanPart.getInitMethod()!=null){
				if(beanPart.getSimpleName().equals(BeanPart.THIS_NAME))
					add = add && createThisBean(beanPart);
				else
					add = add && addNewBean(beanPart) ;
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
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) return true ;
			BeanPart mainBean = (BeanPart) mainBeansItr.next();
			if(newModel.getABean(mainBean.getUniqueName())==null){
				// Bean has been removed - hence remove
				logFiner("Removing deleted bean "+ mainBean.getSimpleName());
				removed = removed && removeDeletedBean( mainBean ); 
			}else{
				// Remove bean if type has changed
				BeanPart newBean = newModel.getABean(mainBean.getUniqueName());
				String mainType;
				String newType;
				boolean isMainBeanThisPart = mainBean.getSimpleName().equals(BeanPart.THIS_NAME);
				boolean isNewBeanThisPart = newBean.getSimpleName().equals(BeanPart.THIS_NAME);
				Name mainBeanExtendsName = (isMainBeanThisPart && mainBean.getModel().getTypeDecleration()!=null) ? mainBean.getModel().getTypeDecleration().getSuperclass() : null;
				Name newBeanExtendsName = (isNewBeanThisPart && newBean.getModel().getTypeDecleration()!=null) ? newBean.getModel().getTypeDecleration().getSuperclass() : null;
				TypeResolver resolver = mainBean.getModel().getResolver();
				if(isMainBeanThisPart && mainBeanExtendsName!=null)
					mainType = resolver.resolveType(mainBeanExtendsName).getName();
				else
					mainType = mainBean.getType();
				if(isNewBeanThisPart && newBeanExtendsName!=null)
					newType = resolver.resolveType(newBeanExtendsName).getName() ;
				else
					newType = newBean.getType();
				boolean typeChanged = !mainType.equals(newType) ;
				if(typeChanged){
					// Type has changed 
					// If extends has been removed, remove the bean
					// If extends there, then reload from scratch
					if(isMainBeanThisPart){
						if(newBeanExtendsName==null){
							// extends has been removed - just delete the this bean
							logFiner("Removing THIS bean ");
							removed = removed && removeDeletedBean( mainBean );
						}else{
							// extends is present - do a RLFS
							logFiner("This part's type has changed - will need to reload");
							return false;
						}
					}else{
						logFiner("Removing changed type bean "+ mainBean.getSimpleName());
						removed = removed && removeDeletedBean( mainBean );
					}
				}else{
					CodeMethodRef mainMethod = mainBean.getInitMethod();
					CodeMethodRef newMethod = newBean.getInitMethod();
					if(mainMethod!=null && newMethod!=null){
						String mainMethodHandle = mainMethod.getMethodHandle();
						String newMethodHandle = newMethod.getMethodHandle();
						if(mainMethodHandle==null || newMethodHandle==null || !mainMethodHandle.equals(newMethodHandle)){
							logFiner("Removing changed init method bean "+mainBean.getSimpleName());
							removed = removed && removeDeletedBean( mainBean );
						}
					}
				}
			}
		}
		return removed;
	}
	
	protected CodeMethodRef createNewMainMethodRef(CodeMethodRef newMethodRef){
		CodeMethodRef mainMethodRef =  new CodeMethodRef(
				newMethodRef.getDeclMethod(), 
				mainModel.getTypeRef(), 
				newMethodRef.getMethodHandle(),
				createSourceRange(newMethodRef.getOffset(), newMethodRef.getLen()), 
				newMethodRef.getContent()) ; 
		
		mainMethodRef.setModel(mainModel) ;
		
		BeanPart updateReturnedBP = newModel.getBeanReturned(newMethodRef.getMethodName());
		if(updateReturnedBP!=null){
			BeanPart mainReturnedBP = mainModel.getABean(updateReturnedBP.getUniqueName());
			if(mainReturnedBP!=null){
				// we habe a bean which is returned with this method - hook them up
				mainReturnedBP.addReturnMethod(mainMethodRef);
			}
		}
		return mainMethodRef ;
	}
	
	/**
	 * Creates an init method for THIS beanpart.
	 * @return
	 * @since 1.0.0
	 */
	protected boolean addThisMethod(){
		boolean add = true ;
		Iterator newBeansItr = newModel.getBeans().iterator();
		while (newBeansItr.hasNext()) {
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) return true ;
			BeanPart beanPart = (BeanPart) newBeansItr.next();
			if(	BeanPart.THIS_NAME.equals(beanPart.getSimpleName()) && 
					mainModel.getABean(beanPart.getSimpleName())!=null){
				BeanPart mainBP = mainModel.getABean(beanPart.getSimpleName());
				if(mainBP.getInitMethod()==null && beanPart.getInitMethod()!=null){
					// main BDM has no THIS init method, the new BDM has one..
					CodeMethodRef initMethod = mainModel.getMethod(beanPart.getInitMethod().getMethodHandle()) ;
					// If the method doesnt exist create it..
					if(initMethod==null){
						initMethod = createNewMainMethodRef(beanPart.getInitMethod()) ;
						logFiner("Created new init method "+initMethod.getMethodHandle()+" for THIS part");
					}
					mainBP.addInitMethod(initMethod);
				}
			}
		}
		return add;
	}
	
	/**
	 * Adds and removes the contents of the main event handler with the contents of 
	 * the passed in updated event handler.
	 * 
	 * @param mainEH
	 * @param updatedEH
	 * @return  Returns whether successful updating has taken place.
	 * 
	 * @since 1.0.0
	 */
	protected boolean updateEventHandler(CodeEventHandlerRef mainEH, CodeEventHandlerRef updatedEH){
		List mainEHMethods = new ArrayList();
		List updatedEHMethods = new ArrayList();
		for (Iterator iter = mainEH.getMethods(); iter.hasNext();) 
			mainEHMethods.add(iter.next());
		for (Iterator iter = updatedEH.getMethods(); iter.hasNext();) 
			updatedEHMethods.add(iter.next());
		
		// Check which main BDM event handler methods need to be removed
		for (int mainMtdCount = 0; mainMtdCount < mainEHMethods.size(); mainMtdCount++) {
			CodeMethodRef mainMethod = (CodeMethodRef) mainEHMethods.get(mainMtdCount);
			boolean suitableMethodFound = false;
			for (int updMtdCount = 0; updMtdCount < updatedEHMethods.size(); updMtdCount++) {
				CodeMethodRef updMethod = (CodeMethodRef) updatedEHMethods.get(updMtdCount);
				if(mainMethod.getMethodName().equals(updMethod.getMethodName())){
					suitableMethodFound = true;
					mainEHMethods.remove(mainMethod);
					mainMtdCount--;
					updatedEHMethods.remove(updMethod);
					updMtdCount--;
					updateMethodOffsetAndContent(mainMethod, updMethod);
				}
			}
			if(!suitableMethodFound){
				// remove method which is not to be found in the update
				mainEHMethods.remove(mainMethod);
				mainMtdCount--;
				mainMethod.dispose();
			}
		}
		
		// Add the new BDM event handler methods
		for (int updMtdCount = 0; updMtdCount < updatedEHMethods.size(); updMtdCount++) {
			CodeMethodRef updMethod = (CodeMethodRef) updatedEHMethods.get(updMtdCount);
			new CodeMethodRef(
				updMethod.getDeclMethod(), 
				(CodeTypeRef) mainEH, 
				updMethod.getMethodHandle(),
				createSourceRange(updMethod.getOffset(), updMethod.getLen()),
				updMethod.getContent());

		}
		return true;
	}
	
	/**
	 * Updates the main BDM with the types and methods of event handlers
	 * in the main BDM. It removes any deleted methods and types. The 
	 * call back expressions are processed later when beans are merged.
	 * 
	 * @return
	 * 
	 * @since 1.0.0
	 */
	protected boolean updateEventHandlers(){
		List mainEventHandlers = new ArrayList(mainModel.getEventHandlers());
		List updatedEventHandlers = new ArrayList(newModel.getEventHandlers());
		for (int mainEHC = 0; mainEHC < mainEventHandlers.size(); mainEHC++) {
			CodeEventHandlerRef mainEventHandler = (CodeEventHandlerRef) mainEventHandlers.get(mainEHC);
			String mainEventHanlderName = mainEventHandler.getName();
			boolean suitableEventHandlerFound = false;
			for (int updEHC = 0; updEHC < updatedEventHandlers.size(); updEHC++) {
				CodeEventHandlerRef updEventHandler = (CodeEventHandlerRef) updatedEventHandlers.get(updEHC);
				if(mainEventHanlderName.equals(updEventHandler.getName())){
					
					suitableEventHandlerFound = true;
					
					// Event handlers are same - remove them for further checking and 
					// update thier contents - methods, etc.
					mainEventHandlers.remove(mainEventHandler);
					mainEHC--;
					updatedEventHandlers.remove(updEventHandler);
					updEHC--;
					
					updateEventHandler(mainEventHandler, updEventHandler);
					break;
				}
			}
			if(!suitableEventHandlerFound){
				// The new BDM doesnt have this event handler - it was removed. 
				// Hence remove the event handler in the main BDM
				mainEventHandlers.remove(mainEventHandler);
				mainEHC--;
				mainEventHandler.dispose();
			}
		}
		// Now all main event handlers have been either updated or removed.
		// Now the new event handlers need to be added
		for (int updEHC = 0; updEHC < updatedEventHandlers.size(); updEHC++) {
			CodeEventHandlerRef newEventHandler = (CodeEventHandlerRef) updatedEventHandlers.get(updEHC);
			createNewEventHandler(newEventHandler);
		}
		return true;
	}
	
	/**
	 * Creates a new event handler in the main BDM with the passed in event handler as a template
	 * 
	 * @param newEventHandler
	 * 
	 * @since 1.0.0
	 */
	private void createNewEventHandler(CodeEventHandlerRef updateEventHandler) {
		CodeEventHandlerRef newEventHandler = new CodeEventHandlerRef(updateEventHandler.getTypeDecl(), mainModel);
		mainModel.getEventHandlers().add(newEventHandler);
		for (Iterator iter = updateEventHandler.getMethods(); iter.hasNext();) {
			CodeMethodRef updateEventHandlerMethod = (CodeMethodRef) iter.next();
			new CodeMethodRef(
				updateEventHandlerMethod.getDeclMethod(), 
				(CodeTypeRef) newEventHandler, 
				updateEventHandlerMethod.getMethodHandle(),
				createSourceRange(updateEventHandlerMethod.getOffset(), updateEventHandlerMethod.getLen()),
				updateEventHandlerMethod.getContent());
		}
	}

}
