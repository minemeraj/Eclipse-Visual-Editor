/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: BDMMerger.java,v $
 *  $Revision: 1.57 $  $Date: 2005-07-28 22:25:46 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cdm.Annotation;

import org.eclipse.ve.internal.jcm.BeanSubclassComposition;

import org.eclipse.ve.internal.java.codegen.java.rules.IThisReferenceRule;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.codegen.util.TypeResolver.Resolved;
import org.eclipse.ve.internal.java.codegen.util.TypeResolver.ResolvedType;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
 
/**
 * 
 * @since 1.0.0
 */
public class BDMMerger {
	protected IBeanDeclModel mainModel = null ;
	protected IBeanDeclModel newModel = null ;
	protected List changedHandles = null;
	protected IProgressMonitor monitor;
	
	protected List needToRedecodeExpressions = new ArrayList();
	protected HashMap newBDMToMainBDMBeanPartMap = new HashMap();
	protected HashMap expressionEquivalencyMap = new HashMap();
	
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
	public BDMMerger(IBeanDeclModel mainModel, IBeanDeclModel newModel, List changedHandles, IProgressMonitor mon){
		this.mainModel = mainModel;
		this.newModel = newModel;
		this.changedHandles = changedHandles;
		needToRedecodeExpressions.clear();
		if (mon!=null)
			monitor = mon;
		else
			monitor = new NullProgressMonitor();		
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
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)||monitor.isCanceled()) return true ;
			merged = merged && updateTypeRef() ;
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)||monitor.isCanceled()) return true ;
			merged = merged && activateDeactivatedBeans() ;
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)||monitor.isCanceled()) return true ;
			merged = merged && removeDeletedBeans() ;
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)||monitor.isCanceled()) return true ;
			merged = merged && removeDeletedMethods() ;
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)||monitor.isCanceled()) return true ;
			merged = merged && updateEventHandlers() ;
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)||monitor.isCanceled()) return true ;
			merged = merged && addNewBeans() ;
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)||monitor.isCanceled()) return true ;
			merged = merged && addThisMethod() ;
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)||monitor.isCanceled()) return true ;
			merged = merged && updateMethods() ;
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)||monitor.isCanceled()) return true ;
			merged = merged && mergeAllBeans() ;
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)||monitor.isCanceled()) return true ;
			merged = merged && deactivateUnreferencedBeans() ;
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)||monitor.isCanceled()) return true ;
			merged = merged && updateFreeForm() ;
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)||monitor.isCanceled()) return true ;
			merged = merged && clean() ;
		}
		needToRedecodeExpressions.clear();
		expressionEquivalencyMap.clear();
		newBDMToMainBDMBeanPartMap.clear();
		return merged ;
	}
	
	private boolean updateTypeRef() {
		CodeTypeRef mainType = mainModel.getTypeRef();
		CodeTypeRef newType = newModel.getTypeRef();
		if(mainType!=null && newType!=null)
			mainType.refresh(newType);
		return true;
	}

	/**
	 * Deactivates beans which are unreferenced. Some beans might 
	 * have been activated in #activateDeactivatedBeans() 
	 * @return
	 * @see #activateDeactivatedBeans()
	 * @since 1.1
	 */
	private boolean deactivateUnreferencedBeans() {
		// Determine unreferenced and deactivate
		BeanPart[] unreferencedBPs = mainModel.getUnreferencedBeanParts();
		for (int bpCount = 0; bpCount < unreferencedBPs.length; bpCount++) {
			unreferencedBPs[bpCount].deactivate();
		}
		return true;
	}

	/**
	 * Activate deactivated beans. Unnecessary beans will be deactivated in #deactivateUnreferencedBeans()
	 * @see #deactivateUnreferencedBeans()
	 * @since 1.1
	 */
	private boolean activateDeactivatedBeans() {
		Iterator mainBeansItr = mainModel.getBeans().iterator();
		while(mainBeansItr.hasNext()){
			if (monitor.isCanceled())
				return false;			
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) return true ;
			BeanPart mainBean = (BeanPart) mainBeansItr.next();
			if(mainBean!=null && !mainBean.isActive())
				mainBean.activate();
		}
		return true;
	}

	private int determineEquivalency(CodeExpressionRef exp1, CodeExpressionRef exp2) throws CodeGenException{
		boolean exp1_exp2_cacheFound = false;
		boolean exp2_exp1_cacheFound = false;
		if(expressionEquivalencyMap.containsKey(exp1)){
			HashMap exp1Map = (HashMap) expressionEquivalencyMap.get(exp1);
			if(exp1Map!=null){
				if(exp1Map.containsKey(exp2)){
					exp1_exp2_cacheFound = true;
				}
			}
		}
		if(!exp2_exp1_cacheFound && expressionEquivalencyMap.containsKey(exp2)){
			HashMap exp2Map = (HashMap) expressionEquivalencyMap.get(exp2);
			if(exp2Map!=null){
				if(exp2Map.containsKey(exp1)){
					exp2_exp1_cacheFound = true;
				}
			}
		}
		
		int equivalency ;
		if(exp1_exp2_cacheFound || exp2_exp1_cacheFound){
			HashMap map = (HashMap) (exp1_exp2_cacheFound?expressionEquivalencyMap.get(exp1):expressionEquivalencyMap.get(exp2));
			Integer eqInt = (Integer) map.get(exp1_exp2_cacheFound?exp2:exp1);
			equivalency = eqInt.intValue();
		}else{
			// no cache - call isEquivalent
			HashMap map = (HashMap) expressionEquivalencyMap.get(exp1);
			if(map==null){
				map = new HashMap();
				expressionEquivalencyMap.put(exp1, map);
			}
			equivalency = exp1.isEquivalent(exp2);
			map.put(exp2, new Integer(equivalency));
		}
		return equivalency;
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
				if (monitor.isCanceled())
					return false;
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
			// it is time to hook them together withn the Compsition Model
			Iterator itr = mainModel.getBeans().iterator() ;
			while (itr.hasNext()) {
				if (monitor.isCanceled())
					return false;
				if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) return true ;
				BeanPart bean = (BeanPart) itr.next() ;
				if(!bean.isActive())
					continue;
				CodeGenUtil.addBeanToBSC(bean,mainModel.getCompositionModel().getModelRoot(), true) ;				
				if(bean.getFFDecoder()!=null) {
					monitor.subTask(bean.getSimpleName());
					bean.getFFDecoder().decode();
				}
			}
		} catch (CodeGenException e) {
			JavaVEPlugin.log(e, Level.WARNING) ;
			return false;
		}
		return true;
	}
	
	protected boolean removeMethodRef(final CodeMethodRef m){
		if(m != null ){
			Collection initBPs = mainModel.getBeansInitilizedByMethod(m);
			BeanPart retBP = mainModel.getBeanReturned(m.getMethodName());
			for (Iterator iter = initBPs.iterator(); iter.hasNext();) {
				BeanPart bp = (BeanPart) iter.next();
				if (JavaVEPlugin.isLoggingLevel(Level.FINER))
					JavaVEPlugin.log("BDM Merger >> "+"Disposing init bean "+bp.getSimpleName()+" when disposing method "+m.getMethodHandle(), Level.FINER); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				if(!BeanPart.THIS_NAME.equals(bp.getSimpleName())) // do not dispose THIS beanpart
					bp.dispose() ;
			}
			if(retBP!=null && !initBPs.contains(retBP)){
				if (JavaVEPlugin.isLoggingLevel(Level.FINER))
					JavaVEPlugin.log("BDM Merger >> "+"Disposing return bean "+retBP.getSimpleName()+" when disposing method "+m.getMethodHandle(), Level.FINER); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
			if (monitor.isCanceled())
				return false;
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) return true ;
			CodeMethodRef m = (CodeMethodRef) methods.next();			
			if(newModel.getMethod(m.getMethodHandle())==null){
				monitor.subTask(m.getMethodName());
				if (JavaVEPlugin.isLoggingLevel(Level.FINER))
					JavaVEPlugin.log("BDM Merger >> "+"Removing method "+m.getMethodHandle() + "since it is not found in update", Level.FINER); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				removed = removed && removeMethodRef(m);
			}
		}
		return removed ;
	}
	
	protected boolean updateMethodOffsetAndContent(CodeMethodRef mainMethod, CodeMethodRef updatedMethod){
		if(mainMethod==null || updatedMethod==null)
			return false ;
		return mainMethod.refresh(updatedMethod);
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
			BeanPart mainBean = determineCorrespondingBeanPart(newBean, mainModel);
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
	protected boolean updateNonRegularBeanPartExpressions(BeanPart mainBeanPart, BeanPart updatedBeanPart) {
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
					// If the main model already has an expression with the same 
					// content - then all we need to do is update the offsets. 
					// If it doesnt then we need to add a new parent expression 
					// to the main BDM
					CodeExpressionRef mainParentExp = null;
					if(updateParentExpression.getCodeContent()!=null){
						Iterator exps = newExpMethod.getAllExpressions();
						while (exps.hasNext()) {
							CodeExpressionRef mainExp = (CodeExpressionRef) exps.next();
							if(mainExp.isStateSet(CodeExpressionRef.STATE_NO_SRC))
								continue;
							if(updateParentExpression.getCodeContent().equals(mainExp.getCodeContent())){
									mainParentExp = mainExp;
									break;
							}
						}
					}
					if(mainParentExp==null){
						// no main BDM expression was found with the same 
						// content - hence create a new parent expression
						CodeExpressionRef newExp = new CodeExpressionRef(updateParentExpression.getExprStmt(), newExpMethod);
						mainBeanPart.addParentExpression(newExp);
					}else{
						// main BDM expression was found with the same content
						// hence just update the offsets
						processEquivalentExpressions(mainParentExp, updateParentExpression, 1);
					}
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
				if (JavaVEPlugin.isLoggingLevel(Level.FINER))
					JavaVEPlugin.log("BDM Merger >> "+"Updating changed expression "+newExp.getCodeContent(), Level.FINER); //$NON-NLS-1$ //$NON-NLS-2$
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
						if (JavaVEPlugin.isLoggingLevel(Level.FINER))
							JavaVEPlugin.log("BDM Merger >> "+"Updating because of changed offset "+newExp.getCodeContent(), Level.FINER); //$NON-NLS-1$ //$NON-NLS-2$
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
		if (JavaVEPlugin.isLoggingLevel(Level.FINER))
			JavaVEPlugin.log("BDM Merger >> "+"Remove deleted expression "+deletedExp.getCodeContent(), Level.FINER); //$NON-NLS-1$ //$NON-NLS-2$
		EObject o = deletedExp.getBean().getEObject();				
		deletedExp.dispose() ;
		CodeGenUtil.snoozeAlarm(o,mainModel.getCompositionModel().getModelResourceSet(), new HashMap());
	}
	
	/*
	 * This method figures out which equivalent expressions have offset changes, then changes their offsets 
	 * and marks them for re-decoding. Later on when the expressions are really merged in processEquivalentExpressions(), 
	 * even though the offset hasnt changed, if the expression is in the marked list it is re-decoded.
	 * 
	 * This process of marking all expressions, updating their offsets and then decoding them is required as 
	 * when an expression is decoded it expects all other expressions to be having the correct offsets. When
	 * decoding is done without updating the offsets of other expressions, problems arise with expressions 
	 * like constructors where the index of the component is determined from offsets (77074). 
	 */
	protected void updateChangedOffsetsAndMarkExpressions(Collection mainExpressions, Collection updatedExpressions){
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
						equivalency = determineEquivalency(mainExp, updExp);
					} catch (CodeGenException e) {} 
					if ( equivalency < 0) 
						continue ; // Not the same expressions
					equivalentExpFound = true;
					
					switch(equivalency){
						case 0:
							if(!mainExp.isStateSet(CodeExpressionRef.STATE_NO_MODEL)){
								if(mainExp.getOffset()!=updExp.getOffset())
									mainExp.setOffset(updExp.getOffset());
								needToRedecodeExpressions.add(mainExp);
							}
						   break;
						case 1:
							if(mainExp.getOffset()!=updExp.getOffset()){
								// Offset has been changed - might have to decode it as it might contain
								// expression ordering in it. Ex: add(comp1); add(comp2) etc.
								mainExp.setOffset(updExp.getOffset());
								needToRedecodeExpressions.add(mainExp);
							}
							break;
					}
						
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
			}
		}
	}

	/**
	 * This is a special equivalence determining API which should be called
	 * only from #processInitExpressions(List, List)
	 * This API doesnt call the expression's isEquivalent() method if both expressions
	 * are INIT expressions. The reason this is necessary is becuase an expression's
	 * isEquivalent() is based on the offsets of the bean's INIT expressions. Since we are 
	 * in the middle of updating the offsets for INIT expressions, we shouldnt rely on it.
	 * 
	 * 
	 * @param mainExp
	 * @param updExp
	 * @return
	 * @throws CodeGenException
	 * 
	 * @since 1.0.2
	 */
	protected int isSpecialEquivalent(CodeExpressionRef mainExp, CodeExpressionRef updExp) throws CodeGenException {
		if(mainExp.isStateSet(CodeExpressionRef.STATE_INIT_EXPR) && updExp.isStateSet(CodeExpressionRef.STATE_INIT_EXPR)){
	        if (mainExp.equals(updExp))
				return 1;
			BeanPart mainBP = mainExp.getBean();
			BeanPart updBP = updExp.getBean();
	        if (mainBP==null && updBP!=null) 
				return -1 ;
			else if (mainBP!=null && updBP==null) 
				return -1 ;
			if(mainBP.getSimpleName().equals(updBP.getSimpleName()) &&
		       mainBP.getType().equals(updBP.getType())){
				if(!mainExp.isStateSet(CodeExpressionRef.STATE_NO_SRC) &&
					!updExp.isStateSet(CodeExpressionRef.STATE_NO_SRC)){
					if(mainExp.getCodeContent().equals(updExp.getCodeContent()))
						return 1;
					return 0;
				}
				return 1;
			}return -1;
		}else{
			return determineEquivalency(mainExp, updExp) ;
		}
	}

	protected boolean processExpressions(Collection mainExpressions, Collection updatedExpressions) {
		// Before merging the expressions, we should update all the changed offsets and mark them.
		// This is required so that expressions which are offset sensitive dont decode erroneously becuase 
		// offsets of following expressions arent updated yet.
		updateChangedOffsetsAndMarkExpressions(mainExpressions, updatedExpressions);
		
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
						equivalency = determineEquivalency(mainExp, updExp) ;
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
				int eqv = determineEquivalency(e, exp);
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
			if (JavaVEPlugin.isLoggingLevel(Level.FINER))
				JavaVEPlugin.log("BDM Merger >> "+"Ignoring creation of duplicate Expression" + e.getCodeContent(), Level.FINER); //$NON-NLS-1$ //$NON-NLS-2$
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
			}else{
				CodeGenUtil.snoozeAlarm(b.getEObject(), mainModel.getCompositionModel().getModelResourceSet(), new HashMap());
			}
		}
		return newe ;
	}
	
	protected boolean addNewExpression(final CodeExpressionRef updateExp) {
		if (JavaVEPlugin.isLoggingLevel(Level.FINER))
			JavaVEPlugin.log("BDM Merger >> "+"Adding new expression "+updateExp.getCodeContent(), Level.FINER); //$NON-NLS-1$ //$NON-NLS-2$
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
		return determineCorrespondingBeanPart(b, mainModel);
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
		merge = merge && updateNonRegularBeanPartExpressions();
		merge = merge && updateRegularBeanPartExpressions();
		return merge;
	}
	
	protected boolean updateNonRegularBeanPartExpressions(){
		boolean merge = true;
		List mainModelBeans = mainModel.getBeans() ;
		mainModelBeans = orderBeansToMerge(mainModelBeans);
		// Update changed bean parts
		Iterator mainModelBeansItr = mainModelBeans.iterator();
		// Update all beans EXCEPT the regular expressions - they need to be done in order of expressions in method
		while (mainModelBeansItr.hasNext()) {
			if (monitor.isCanceled())
				return false;
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) return true ;
			BeanPart mainBP = (BeanPart) mainModelBeansItr.next();
			// sometimes when statements (createTable()) are disposed in other methods, the beanpart (table)
			// is disposed. Check to see if it is still in the model before proceeding with the merge.
			if(mainBP.getModel()==null) 
				continue;
			BeanPart updateBP = determineCorrespondingBeanPart(mainBP, newModel);
			if(updateBP != null){
				merge = merge && updateNonRegularBeanPartExpressions(mainBP, updateBP);
			}else{
				JavaVEPlugin.log("BDM Merger: Unable to find main BDM bean in new BDM at this point", Level.WARNING); //$NON-NLS-1$
			}
		}
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
	protected boolean updateRegularBeanPartExpressions(){
		boolean update = true;
		// Update the regular expressions of all beans
		HashMap beansInMethodMap = new HashMap();
		Iterator mainModelBeansItr = mainModel.getBeans().iterator();
		while (mainModelBeansItr.hasNext()) {
			BeanPart bp = (BeanPart) mainModelBeansItr.next();
			String key = "null"; //$NON-NLS-1$
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
				if(mainBP.isDisposed() || mainBP.getModel()==null)
					continue;
				BeanPart updateBP = determineCorrespondingBeanPart(mainBP, newModel);
				if(updateBP != null){

					// Order the bean expressions
					orderBeanPartExpressions(mainBP, orderedMainExpressions);
					orderBeanPartExpressions(updateBP, orderedUpdatedExpressions);
					
				}else{
					JavaVEPlugin.log("BDM Merger: Unable to find main BDM bean in new BDM at this point", Level.WARNING); //$NON-NLS-1$
				}
			}
			update = update && processInitExpressions(orderedMainExpressions, orderedUpdatedExpressions);
			update = update && processExpressions(orderedMainExpressions, orderedUpdatedExpressions);
		}
		return update;
	}
	
	protected boolean processInitExpressions(List orderedMainExpressions, List orderedUpdatedExpressions) {
		boolean processed = true;
		List mainInitExpressions = new ArrayList();
		List updateInitExpressions = new ArrayList();
		// populate the init expressions 
		for (int i = 0; i < orderedMainExpressions.size(); i++) {
			CodeExpressionRef exp = (CodeExpressionRef) orderedMainExpressions.get(i);
			if(exp.isStateSet(CodeExpressionRef.STATE_INIT_EXPR)){
				orderedMainExpressions.remove(exp);
				i--;
				mainInitExpressions.add(exp);
			}
		}
		for (int i = 0; i < orderedUpdatedExpressions.size(); i++) {
			CodeExpressionRef exp = (CodeExpressionRef) orderedUpdatedExpressions.get(i);
			if(exp.isStateSet(CodeExpressionRef.STATE_INIT_EXPR)){
				orderedUpdatedExpressions.remove(exp);
				i--;
				updateInitExpressions.add(exp);
			}
		}
		
		for (int updatedExpCount = 0 ; updatedExpCount < updateInitExpressions.size(); updatedExpCount++) {
			CodeExpressionRef updExp = (CodeExpressionRef) updateInitExpressions.get(updatedExpCount);
			boolean equivalentExpFound = false ;
			for(int mainExpCount = 0; mainExpCount < mainInitExpressions.size(); mainExpCount++ ){
				CodeExpressionRef mainExp = (CodeExpressionRef) mainInitExpressions.get(mainExpCount);
				if (mainExp != null && updExp != null && !updExp.isStateSet(CodeExpressionRef.STATE_EXP_IN_LIMBO)) {
					int equivalency = -1;
					try {
						equivalency = isSpecialEquivalent(mainExp, updExp);
					} catch (CodeGenException e) {} 
					if ( equivalency < 0) 
						continue ; // Not the same expressions
					equivalentExpFound = true;
					processed = processed && processEquivalentExpressions(mainExp, updExp, equivalency);
					mainInitExpressions.remove(mainInitExpressions.indexOf(mainExp)) ;
					updateInitExpressions.remove(updateInitExpressions.indexOf(updExp)) ;
					mainExpCount -- ;
					updatedExpCount -- ;
					break;
				}
			}
			if(!equivalentExpFound){
				// No Equivalent expression was found 
				// Now add the newly added expressions
				updateInitExpressions.remove(updateInitExpressions.indexOf(updExp));
				updatedExpCount--;
				processed = processed && addNewExpression(updExp);
			}
		}
		// Now remove the old main expressions
		for (int delExpCount = 0; delExpCount < mainInitExpressions.size(); delExpCount++) {
			CodeExpressionRef mainExp = (CodeExpressionRef) mainInitExpressions.get(delExpCount);
			removeDeletedExpression(mainExp);
		}
		
		return processed;
	}

//	private void pushInitExpressionsToTop(List orderedList) {
//		// Bring init expressions to top. Determination of the exact reused
//		// variable is based on the offset of init expression - hence need to 
//		// process them first.
//		if(orderedList.size()>1){
//			int nextInitExpIndex = 0;
//			for (int i = 0; i < orderedList.size(); i++) {
//				CodeExpressionRef exp = (CodeExpressionRef) orderedList.get(i);
//				if(exp.isStateSet(CodeExpressionRef.STATE_INIT_EXPR)){
//					if(i!=nextInitExpIndex){
//						orderedList.remove(exp);
//						orderedList.add(nextInitExpIndex, exp);
//					}
//					nextInitExpIndex++;
//				}
//			}
//		}
//	}

	protected BeanPart determineCorrespondingBeanPart(BeanPart otherModelBP, IBeanDeclModel model) {
		BeanPart modelBP = null;
		if(newBDMToMainBDMBeanPartMap.size()>0){
			// check to see if we created the bean part ourselves - without
			// the init expression
			if(newBDMToMainBDMBeanPartMap.containsKey(otherModelBP))
				modelBP = (BeanPart) newBDMToMainBDMBeanPartMap.get(otherModelBP);
			else if(newBDMToMainBDMBeanPartMap.containsValue(otherModelBP)){
				Iterator keysItr = newBDMToMainBDMBeanPartMap.keySet().iterator();
				while (keysItr.hasNext()) {
					BeanPart keyBP = (BeanPart) keysItr.next();
					if(newBDMToMainBDMBeanPartMap.get(keyBP)==otherModelBP)
						modelBP = keyBP;
				}
			}
		}
		if(modelBP==null){
			BeanPartDecleration otherModelBPDecl = otherModelBP.getDecleration();
			BeanPartDecleration modelBPDecl = model.getModelDecleration(otherModelBPDecl);
			updateBPDeclAST(otherModelBPDecl, modelBPDecl);
			BeanPart[] modelBPs = modelBPDecl==null?null:modelBPDecl.getBeanParts();
			if(modelBPs!=null && modelBPs.length>0){
				for (int i = 0; i < modelBPs.length; i++) {
					if(otherModelBP.isEquivalent(modelBPs[i])){
						modelBP = modelBPs[i];
						break;
					}
				}
			}else{
				// no bp decl - just ask by unique name
				modelBP = model.getABean(otherModelBP.getUniqueName());
			}
			if(modelBP!=null){
				// found out a bean using equivalency tests 
				// BUT the equivalency tests could be false if no init expressions
				// were available for the beanparts we created - hence check our
				// cache to see if the bean was created by us. If so we cant use 
				// the equivalency tests
				if(newBDMToMainBDMBeanPartMap.containsValue(modelBP) || 
						newBDMToMainBDMBeanPartMap.containsKey(modelBP))
					modelBP = null;
			}
		}
		return modelBP;
	}

	/**
	 * Any search for Codegen's annotation - //@jve:decl-index=0:visual-constraint="x,y" -
	 * happens on the BeanPartDeclaration's AST node for that bean. If this is not updated
	 * there is a risk that the bean could be deactivated even if it has "decl-index=0" in its
	 * annotation. Hence this update is necessary.
	 * 
	 * @param otherModelBPDecl
	 * @param modelBPDecl
	 * 
	 * @since 1.1
	 */
	private void updateBPDeclAST(BeanPartDecleration otherModelBPDecl, BeanPartDecleration mainModelBPDecl) {
		if(otherModelBPDecl!=null && mainModelBPDecl!=null){
			if(		otherModelBPDecl.getFieldDeclHandle()!=null && 
					mainModelBPDecl.getFieldDeclHandle()!=null && 
					otherModelBPDecl.getFieldDeclHandle().equals(mainModelBPDecl.getFieldDeclHandle()))
				mainModelBPDecl.setFieldDecl(otherModelBPDecl.getFieldDecl());
			else if(	otherModelBPDecl.getDeclerationHandle()!=null && 
						mainModelBPDecl.getDeclerationHandle()!=null && 
						otherModelBPDecl.getDeclerationHandle().equals(mainModelBPDecl.getDeclerationHandle()))
				mainModelBPDecl.setFieldDecl(otherModelBPDecl.getFieldDecl());
		}
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
					if (JavaVEPlugin.isLoggingLevel(Level.FINE))
						JavaVEPlugin.log("Bad Object: " + newBP.getType() + ": " + newBP.getUniqueName(), Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
				}
			} else { // a this part
				if (obj != null) {
					((XMIResource)comp.eResource()).setID(obj,BeanPart.THIS_NAME); 
					// If no annotation, the PS will not allow you to edit the name in composition
					annotatedName = null;
				}
			}

			if (obj == null) {
				if (JavaVEPlugin.isLoggingLevel(Level.FINE))
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
			}
		} catch (CodeGenException e) {
			if (JavaVEPlugin.isLoggingLevel(Level.WARNING))
				JavaVEPlugin.log("Exception when creating EObject for BeanPart "+newBP.getType()+" with message "+e.getMessage(), Level.WARNING) ; //$NON-NLS-1$ //$NON-NLS-2$
		}
		return err;
	}
	
	protected boolean addNewBean(final BeanPart referenceBP){
		// New bean - add it and any methods associated with it
		BeanPart newBP ;
		String refBPHandle = referenceBP.getDecleration().getDeclerationHandle();
		BeanPartDecleration decl = mainModel.getModelDecleration(refBPHandle);
		int uniqueIndex = 0;
		if(decl==null){
			ASTNode fd = referenceBP.getFieldDecl();
			if(fd!=null){
				if (referenceBP.getFieldDecl() instanceof FieldDeclaration)
					decl = new BeanPartDecleration((FieldDeclaration)referenceBP.getFieldDecl()) ;
				else
					decl = new BeanPartDecleration((VariableDeclarationStatement)referenceBP.getFieldDecl());
			}else{
				decl = new BeanPartDecleration(referenceBP.getDecleration().getName(), referenceBP.getDecleration().getType());
			}
		}else{
			//find the least valued unique number
			uniqueIndex=Integer.MAX_VALUE;
			BeanPart[] bps = decl.getBeanParts();
			for (int uniqueNum = 0; uniqueNum <= bps.length; uniqueNum++) {
				boolean used = false;
				for (int i = 0; i < bps.length; i++) {
					if(bps[i].getUniqueIndex()==uniqueNum){
						used = true;
						break;
					}
				}
				if(!used){
					uniqueIndex = uniqueNum;
					break;
				}
			}
			if(uniqueIndex==Integer.MAX_VALUE){
				JavaVEPlugin.log("Should be having an unique number!", Level.FINE); //$NON-NLS-1$
				uniqueIndex = bps.length;
			}
		}
		newBP = new BeanPart(decl);
		newBP.setInstanceInstantiation(referenceBP.isInstanceInstantiation()) ;
		newBP.setUniqueIndex(uniqueIndex);
		newBP.setIsInJVEModel(false) ;
		
		if (JavaVEPlugin.isLoggingLevel(Level.FINER))
			JavaVEPlugin.log("BDM Merger >> "+"Created new BP "+newBP.getSimpleName(), Level.FINER); //$NON-NLS-1$ //$NON-NLS-2$
		
		CodeMethodRef initMethod = null ;
		if( (initMethod = mainModel.getMethod(referenceBP.getInitMethod().getMethodHandle())) == null ){
			// Init method of the bean is not present in the main model - 
			// add it to the main model, and hook the bean part up.
			initMethod = createNewMainMethodRef(referenceBP.getInitMethod()) ;
			if (JavaVEPlugin.isLoggingLevel(Level.FINER))
				JavaVEPlugin.log("BDM Merger >> "+"Created new init method "+initMethod.getMethodHandle()+" for new bean part"+newBP.getSimpleName(), Level.FINER); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		newBP.addInitMethod(initMethod);
		if (referenceBP.getDecleration().isInstanceVar())
			decl.setDeclaringMethod(null);
		else
			decl.setDeclaringMethod(initMethod);
		newBP.setModel(mainModel) ;
		mainModel.addBean(newBP);
		
		CodeMethodRef referenceReturnMethod = referenceBP.getReturnedMethod();
		if(referenceReturnMethod!=null){
			CodeMethodRef retMethod = mainModel.getMethod(referenceReturnMethod.getMethodHandle());
			if(retMethod!=null)
				newBP.addReturnMethod(retMethod);
		}
		try {
			newBP.addToJVEModel();
		} catch (CodeGenException e) {
			JavaVEPlugin.log(e);
			newBP.dispose();
			return false;
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
		newBDMToMainBDMBeanPartMap.put(referenceBP, newBP);
		return true;
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
			if (JavaVEPlugin.isLoggingLevel(Level.FINER))
				JavaVEPlugin.log("BDM Merger >> "+"Successfully created this bean part : "+ thisBP.getSimpleName(), Level.FINER); //$NON-NLS-1$ //$NON-NLS-2$

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
				if (JavaVEPlugin.isLoggingLevel(Level.FINER))
					JavaVEPlugin.log("BDM Merger >> "+"Successfully created init method for this bean part "+initMethod.getMethodHandle(), Level.FINER); //$NON-NLS-1$ //$NON-NLS-2$
			}
			thisBP.addInitMethod(initMethod);
			
			List errorBeans = createNewBeanJavaInstance(thisBP);
			if(errorBeans.size()>0){
				// Error instantiaging the bean - error beans returned
				for (Iterator iter = errorBeans.iterator(); iter.hasNext();) {
					BeanPart errBP = (BeanPart) iter.next();
					if (JavaVEPlugin.isLoggingLevel(Level.FINER))
						JavaVEPlugin.log("BDM Merger >> "+"Disposing bean part "+errBP.getSimpleName()+" when createing java instance for "+referenceBP.getSimpleName(), Level.FINER); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
			if (monitor.isCanceled())
				return false;
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) return true ;
			final BeanPart beanPart = (BeanPart) newBeansItr.next();			
			if( determineCorrespondingBeanPart(beanPart, mainModel) == null &&
				beanPart.getInitMethod()!=null){
				monitor.subTask(beanPart.getSimpleName());
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
			EObject eObject = b.getEObject();
			// remove expressions which reference this bean
			List toDeleteExpressions = new ArrayList();
			Iterator expItr = b.getInitMethod().getAllExpressions();
			while (expItr.hasNext()) {
				CodeExpressionRef exp = (CodeExpressionRef) expItr.next();
				if(b.equals(exp.getBean()))
					continue;
				Object[] addedInstances = exp.getAddedInstances();
				for (int i = 0; addedInstances!=null && i < addedInstances.length; i++) {
					if(eObject==addedInstances[i]){
						if(ChildRelationshipDecoderHelper.isChildRelationship(exp.getSF()))
							continue; // we only want to delete non-containment expressions
						if(!toDeleteExpressions.contains(exp))
							toDeleteExpressions.add(exp);
					}
				}
			}
			for (Iterator iter = toDeleteExpressions.iterator(); iter.hasNext();) {
				CodeExpressionRef exp = (CodeExpressionRef) iter.next();
				exp.dispose();
			}
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
		List visitedDecls = new ArrayList();
		Iterator mainBeansItr = mainModel.getBeans().iterator();
		while(mainBeansItr.hasNext()){
			if (monitor.isCanceled())
				return false;			
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) return true ;
			BeanPart mainBean = (BeanPart) mainBeansItr.next();
			// Could have been deleted when a parent was deleted
			if (mainBean.isDisposed()) continue;
			
			BeanPartDecleration mainBeanDecl = mainBean.getDecleration();
			if(visitedDecls.contains(mainBeanDecl))
				continue; // we have visited all beans in this bean decl
			visitedDecls.add(mainBeanDecl);
			BeanPart[] mainBPs = mainBeanDecl.getBeanParts();
			if(mainBPs==null || mainBPs.length<1)
				JavaVEPlugin.log("BDMMerger: BPDecl with no BPs - shouldnt be so", Level.WARNING); //$NON-NLS-1$
			
			List toDeleteBeansList = new ArrayList();
			BeanPartDecleration newBPDecl = newModel.getModelDecleration(mainBeanDecl);
			BeanPart[] newBPs = newBPDecl==null?new BeanPart[0]: newBPDecl.getBeanParts();
			if(mainBPs.length!=newBPs.length){
				for (int i = 0; i < mainBPs.length; i++) {
					toDeleteBeansList.add(mainBPs[i]);
				}
			}else{
				for (int i = 0; i < mainBPs.length; i++) {
					if(shouldRemoveMainBean(mainBPs[i], newBPs[i]))
						toDeleteBeansList.add(mainBPs[i]);
				}
			}
			
			Iterator toDeleteBPItr = toDeleteBeansList.iterator();
			while (toDeleteBPItr.hasNext()) {
				BeanPart bp = (BeanPart) toDeleteBPItr.next();
				if (JavaVEPlugin.isLoggingLevel(Level.FINER))
					JavaVEPlugin.log("BDM Merger >> "+"Removing deleted bean "+ bp.getSimpleName(), Level.FINER); //$NON-NLS-1$ //$NON-NLS-2$
				monitor.subTask(bp.getSimpleName());
				removed = removed && removeDeletedBean( bp ); 
			}
		}
		return removed;
	}
	
	protected boolean shouldRemoveMainBean(BeanPart mainBean, BeanPart newBean){
		// Remove bean if type has changed	
		boolean shouldRemove = false;
		String mainType;
		String newType;
		boolean isMainBeanThisPart = mainBean.getSimpleName().equals(BeanPart.THIS_NAME);
		boolean isNewBeanThisPart = newBean.getSimpleName().equals(BeanPart.THIS_NAME);
		Name mainBeanExtendsName = (isMainBeanThisPart && mainBean.getModel().getTypeDecleration()!=null) ? mainBean.getModel().getTypeDecleration().getSuperclass() : null;
		Name newBeanExtendsName = (isNewBeanThisPart && newBean.getModel().getTypeDecleration()!=null) ? newBean.getModel().getTypeDecleration().getSuperclass() : null;
		TypeResolver resolver = mainBean.getModel().getResolver();
		if(isMainBeanThisPart && mainBeanExtendsName!=null) {
			ResolvedType resolveType = resolver.resolveType(mainBeanExtendsName);
			if (resolveType != null)
				mainType = resolveType.getName();
			else
				mainType = null;
		} else
			mainType = mainBean.getType();
		if(isNewBeanThisPart && newBeanExtendsName!=null) {
			ResolvedType resolveType = resolver.resolveType(newBeanExtendsName);
			if (resolveType != null)
				newType = resolveType.getName() ;
			else
				newType = null;
		} else
			newType = newBean.getType();
		boolean typeChanged = mainType == null || newType == null || !mainType.equals(newType) ;
		if(typeChanged){
			monitor.subTask(mainBean.getSimpleName());
			// Type has changed 
			// If extends has been removed, remove the bean
			// If extends there, then reload from scratch
			if(isMainBeanThisPart){
				if(newBeanExtendsName==null){
					if (JavaVEPlugin.isLoggingLevel(Level.FINER))
						JavaVEPlugin.log("BDM Merger >> "+"Removing THIS bean ", Level.FINER); //$NON-NLS-1$ //$NON-NLS-2$
					shouldRemove = true;
				}else{
					if (JavaVEPlugin.isLoggingLevel(Level.FINER))
						JavaVEPlugin.log("BDM Merger >> "+"This part's type has changed - will need to reload", Level.FINER); //$NON-NLS-1$ //$NON-NLS-2$
					shouldRemove = true;
				}
			}else{
				if (JavaVEPlugin.isLoggingLevel(Level.FINER))
					JavaVEPlugin.log("BDM Merger >> "+"Removing changed type bean "+ mainBean.getSimpleName(), Level.FINER); //$NON-NLS-1$ //$NON-NLS-2$
				shouldRemove = true;
			}
		}else{
			CodeMethodRef mainMethod = mainBean.getInitMethod();
			CodeMethodRef newMethod = newBean.getInitMethod();
			if(mainMethod!=null && newMethod!=null){
				String mainMethodHandle = mainMethod.getMethodHandle();
				String newMethodHandle = newMethod.getMethodHandle();
				if(mainMethodHandle==null || newMethodHandle==null || !mainMethodHandle.equals(newMethodHandle)){
					if (JavaVEPlugin.isLoggingLevel(Level.FINER))
						JavaVEPlugin.log("BDM Merger >> "+"Removing changed init method bean "+mainBean.getSimpleName(), Level.FINER); //$NON-NLS-1$ //$NON-NLS-2$
					shouldRemove = true;
				}
			}
		}
		return shouldRemove;
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
			BeanPart mainReturnedBP = determineCorrespondingBeanPart(updateReturnedBP, mainModel);
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
			if (monitor.isCanceled())
				return false;
			if (mainModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) return true ;
			BeanPart beanPart = (BeanPart) newBeansItr.next();
			if(	BeanPart.THIS_NAME.equals(beanPart.getSimpleName()) && 
					mainModel.getABean(beanPart.getSimpleName())!=null){
				BeanPart mainBP = mainModel.getABean(beanPart.getSimpleName());
				if(mainBP.getInitMethod()==null && beanPart.getInitMethod()!=null){
					// main BDM has no THIS init method, the new BDM has one..
					CodeMethodRef initMethod = mainModel.getMethod(beanPart.getInitMethod().getMethodHandle()) ;
					monitor.subTask(initMethod.getMethodName());
					// If the method doesnt exist create it..
					if(initMethod==null){
						initMethod = createNewMainMethodRef(beanPart.getInitMethod()) ;
						if (JavaVEPlugin.isLoggingLevel(Level.FINER))
							JavaVEPlugin.log("BDM Merger >> "+"Created new init method "+initMethod.getMethodHandle()+" for THIS part", Level.FINER); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
				mainEH, 
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
			if (monitor.isCanceled())
				return false;
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
				monitor.subTask(mainEventHanlderName);
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
				newEventHandler, 
				updateEventHandlerMethod.getMethodHandle(),
				createSourceRange(updateEventHandlerMethod.getOffset(), updateEventHandlerMethod.getLen()),
				updateEventHandlerMethod.getContent());
		}
	}

}
