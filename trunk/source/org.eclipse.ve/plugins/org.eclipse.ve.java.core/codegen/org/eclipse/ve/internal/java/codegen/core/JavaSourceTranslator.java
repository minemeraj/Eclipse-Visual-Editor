package org.eclipse.ve.internal.java.codegen.core;
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
 *  $RCSfile: JavaSourceTranslator.java,v $
 *  $Revision: 1.6 $  $Date: 2004-02-20 00:44:30 $ 
 */
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cdm.*;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.IModelChangeController;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.jcm.*;

import org.eclipse.ve.internal.java.codegen.editorpart.IJVEStatus;
import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.java.rules.*;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;




public class JavaSourceTranslator implements IDiagramSourceDecoder, IDiagramModelBuilder { 
	


JavaBeanModelBuilder    fJavaModelBldr = null ;         
IBeanDeclModel          fBeanModel ;
IDiagramModelInstance   fCompositionModel = null ;
IWorkingCopyProvider    fWorkingCopy = null ;
JavaSourceSynchronizer  fSrcSync = null ;
int						fSrcSyncDelay = JavaSourceSynchronizer.DEFAULT_SYNC_DELAY ;
String                  fRSuri = null ;
IFile                   fFile = null ;
char[][]				fPackageName = null ;
TransientErrorManager   fTransientErrorManager = new TransientErrorManager();
IJVEStatus              fMsgRrenderer = null ;
EditDomain 			    fEDomain = null ;
final ArrayList		  	fTranslatorListeners = new ArrayList() ;
private static boolean  fSnippetProcessingInProgress = false ;
boolean                 fdisconnected = true ;
boolean				   	fpauseRoundTripping = false ;
private boolean fModelLoaded = false;
public static String    fPauseSig = CodegenMessages.getString("JavaSourceTranslator.Round_Tripping_is_Paused_1") ;  //$NON-NLS-1$
public static int       fCommitAndFlushNestGuard = 0 ;

BeanSubclassComposition fRoot = null;
Resource fResource = null;
IDiagramSourceDecoder fSourceDecoder = null;
IJVEStatus fMsgRenderer = null;

String fUri;




  /**
   *  This is the strategy for updating the shared Java document after
   *  a change in the local document.
   */	
  class  SharedToLocalUpdater implements IBackGroundWorkStrategy {
  	ICancelMonitor    fMonitor = null ;
  	IDocumentListener fDocListener = null ;
  	int               fCollisionCount = 0 ;
  	  	  	
  	boolean          fHold = false ;
  	String            fHoldMsg = null ;
  	int          	  fSetHold = 0 ;
  	
  	final int DELTA_SKIP 			= 1 ;
  	final int DELTA_PARSE_ERROR 	= 2 ;
  	final int DELTA_BDM_CHANGE 	= 3 ;
  	final int DELTA_COMPLEX_DELTA	= 4 ;
  	
  	
  	/**
	 * Check to see if the non method delta is important 
	 */
	private int getDeltaStatus (Object[] items) {

		if (items == null || items.length==0) return DELTA_SKIP ;
		
		// Skip changes to document areas that are not resolved - no need
		// to add them to the local document
		for (int i = 0; i < items.length; i++) {

			if (SynchronizerWorkItem.INSTANCE_ANNOTATION_HANDLE.equals(((SynchronizerWorkItem) items[i]).getChangedElementHandle())
				|| SynchronizerWorkItem.THIS_ANNOTATION_HANDLE.equals(((SynchronizerWorkItem) items[i]).getChangedElementHandle()))
				return DELTA_COMPLEX_DELTA;
			if (SynchronizerWorkItem.RELOAD_HANDLE.equals(((SynchronizerWorkItem) items[i]).getChangedElementHandle())
				/* ||
					     SynchronizerWorkItem.CLASS_IMPLEMENT_EXTENDS_HANDLE.equals(((SynchronizerWorkItem) items[i]).getChangedElementHandle())) */
				)
				// ** At this time changes to the extends/implements does not refelect in the JavaModel.. so
				//    no reason to reaload		     
				return DELTA_BDM_CHANGE;
		    if (isEventProcessingNeeded(((SynchronizerWorkItem) items[i])))
		      return DELTA_COMPLEX_DELTA ;
		    // Package changes are major events - BDM could be changed in subtle ways
		    if(((SynchronizerWorkItem)items[i]).isPackage())
		    	return DELTA_BDM_CHANGE;
		}

		// Take a closer look, and see if new fields, or new initMetod were added
		// Note that this will pick up changes to imports.
		JavaBeanModelBuilder modelBldr =
			new JavaBeanShadowModelBuilder(fEDomain,CodeGenUtil.getRefWorkingCopyProvider(fWorkingCopy.getWorkingCopy(false)), fWorkingCopy.getFile().getLocation().toFile().toString(), null);
		modelBldr.setDiagram(fCompositionModel);
		IBeanDeclModel bdm = null;
		try {
			bdm = modelBldr.build();
		}
		catch (Exception e) {
			// Syntax error
			return DELTA_PARSE_ERROR;
		}
		if (fBeanModel == null || !fBeanModel.isStateSet(IBeanDeclModel.BDM_STATE_UP_AND_RUNNING) || fBeanModel.getBeans().size() != bdm.getBeans().size())
			return DELTA_BDM_CHANGE;

		ArrayList beanList = new ArrayList();
		for (int i = 0; i < fBeanModel.getBeans().size(); i++) {
			BeanPart b = (BeanPart) bdm.getBeans().get(i);
			BeanPart oB = fBeanModel.getABean(b.getUniqueName());
			if (oB != null && oB.getFieldDeclHandle() != null) // Internal Beans do not have IField
				beanList.add(oB.getFieldDeclHandle());
			// If components have changed 
			if (oB == null || !oB.isEquivalent(b))
				return DELTA_BDM_CHANGE;
			// Has init method changed
			if ((b.getInitMethod() == null && oB.getInitMethod() != null) || (b.getInitMethod() != null && oB.getInitMethod() == null))
				return DELTA_BDM_CHANGE;
			if (b.getInitMethod() != null)
				if (!oB.getInitMethod().getMethodHandle().equals(b.getInitMethod().getMethodHandle()))
					return DELTA_BDM_CHANGE;
		}
		for (int i = 0; i < items.length; i++) {
			SynchronizerWorkItem elm = (SynchronizerWorkItem) items[i];
			if (elm.isField() && beanList.contains(elm.getChangedElementHandle()))
				return DELTA_BDM_CHANGE;
		}
		// Even though we compared BDMs, arguments (like Color) are not compared, and may need changes
		for (int i = 0; i < items.length; i++) {
			SynchronizerWorkItem elm = (SynchronizerWorkItem) items[i];
			if (elm.isImport()){
				ITypeResolver sharedResolver = fBeanModel;
				final CodegenTypeResolver localCodegenResolver = new CodegenTypeResolver(CodeGenUtil.getMainType(fWorkingCopy.getWorkingCopy(true)));
				ITypeResolver localResolver = new ITypeResolver() {
					public String resolve(String unresolved) {
						return localCodegenResolver.resolveTypeComplex(unresolved);
					}
					public String resolveType(String unresolved) {
						return localCodegenResolver.resolveTypeComplex(unresolved,true);
					}					
					public String resolveThis() {
						return localCodegenResolver.resolveTypeComplex(CodeGenUtil.getMainType(fWorkingCopy.getWorkingCopy(true)).getElementName());
					}
				};
				List mainBeans = fBeanModel.getBeans();
				List deltaBeans = bdm.getBeans();
				if(mainBeans==null || deltaBeans==null || deltaBeans.size()!=mainBeans.size())
					continue;
				for(int bc=0;bc<deltaBeans.size();bc++){
					BeanPart delBean = (BeanPart) deltaBeans.get(bc);
					BeanPart mainBean = fBeanModel.getABean(delBean.getUniqueName());
					if(delBean==null || mainBean==null)
						continue; // maybe some bean was not instantiable - and hence was removed from the model.
					List delExps = new ArrayList();
					delExps.addAll(delBean.getRefExpressions());
					delExps.addAll(delBean.getBadExpressions());
					List mainExps = new ArrayList();
					mainExps.addAll(mainBean.getRefExpressions());
					mainExps.addAll(mainBean.getBadExpressions());
					if(delExps.size()!=mainExps.size())
						return DELTA_BDM_CHANGE;
					List nonCommonExpressions = new ArrayList();
					nonCommonExpressions.addAll(mainExps);
					nonCommonExpressions.addAll(delExps);
					for(int edc=0;edc<delExps.size();edc++){
						CodeExpressionRef de = (CodeExpressionRef) delExps.get(edc);
						for(int emc=0;emc<mainExps.size();emc++){
							CodeExpressionRef me = (CodeExpressionRef) mainExps.get(emc);
							try {
								if(me.isEquivalent(de)>-1){
									nonCommonExpressions.remove(me);
									nonCommonExpressions.remove(de);
									if(me.isEquivalentChanged(localResolver, de, sharedResolver))
										return DELTA_BDM_CHANGE;
								}
							} catch (CodeGenException e) {
								JavaVEPlugin.log(e, Level.WARNING);
							}
						}
					}
					if(nonCommonExpressions.size()>0)
						return DELTA_BDM_CHANGE;
				}
			}
		}
		return DELTA_SKIP;
	}  	
	
	/**
  	 *  ReLoad the BDM model from stratch
  	 */
  	private synchronized void Reload(Display disp,ICancelMonitor monitor) {
  		IModelChangeController controller = (IModelChangeController) getEditDomain().getData(IModelChangeController.MODEL_CHANGE_CONTROLLER_KEY);
		if(controller!=null && controller.inTransaction()){
			// If there are any commands which are being processed, append a reload handle into the queue
			// and exit the Reload process. The commands need to get executed, else there will be cases where
			// commands will get lost becuase some previous command resulted in the Reload mechanism kicking in.
			fSrcSync.appendReloadRequest(null);
			return ;
		}
  				
  	    try {
  			Object lock = fBeanModel== null ? new Object() : fBeanModel.getDocumentLock() ;  			  
  			fCollisionCount=0 ;
  			synchronized (lock) {
  					  if (fMsgRrenderer.setReloadPending(false) == false) // No more pending, go for it
			             reloadFromScratch(disp,monitor) ;	                               		           
			          else
			             JavaVEPlugin.log("Reload: reload is pending, skipping",Level.FINE) ; //$NON-NLS-1$
  			}
  		}
  		catch (Throwable t) {
  				  JavaVEPlugin.log(t, Level.WARNING);                    
  		}      
 	}
  	
  	protected boolean isEventProcessingNeeded(SynchronizerWorkItem item) {
  		if (fBeanModel == null) return true ;
  		
  		if (item.isMethod()) {
			IEventProcessingRule rule = (IEventProcessingRule) CodeGenUtil.getEditorStyle(fBeanModel).getRule(IEventProcessingRule.RULE_ID) ;
  			Iterator itr = fBeanModel.getTypeRef().getMethods() ;
  			while (itr.hasNext()) {
				CodeMethodRef m = (CodeMethodRef) itr.next();
				if (m.getMethodHandle().equals(item.getChangedElementHandle()))
				   return rule.isEventInitMethodSigniture(m.getMethodName()) ;
			}
  		}
  		else if (item.isInnerClass()) return true ;
  		
  		return false ;
  	}
  	
  	/**
  	 * 
  	 * Determine if a change is bounded to the content of a single method, and does not
  	 * involves a changed to an inner class (which may requite a multi method analysis for style 2 events)
  	 *  
  	 * @param items   Work items that are to be processed
  	 * @return true if we should drive a delta update
  	 *          false imply that a reLoad or Skip should be considered.
  	 */
	protected boolean performDeltaUpdate(SynchronizerWorkItem[] items) {

		if (items.length >= 0 && fBeanModel != null && 
		    fBeanModel.isStateSet(IBeanDeclModel.BDM_STATE_UP_AND_RUNNING)) {

			boolean deltaUpdate = true;
			// Perform a delta update only if a BDM exists and the delta spans a 
			// single method content change
			for (int i = 0; i < items.length; i++) {
				if (!(items[i].isMethod()
					&& items[i].getChangedIndex() >= 0
					&& items[i].getSourceCode() != null
					&& !items[i].getChangedElementHandle().equals(SynchronizerWorkItem.THIS_ANNOTATION_HANDLE)
					&& !items[i].getChangedElementHandle().equals(SynchronizerWorkItem.RELOAD_HANDLE)
					&& !isEventProcessingNeeded(items[i]))) {
					deltaUpdate = false;
					break;
				}
			}
			return deltaUpdate;
		}
		else
			return false;
	}
  	
  	public void run(Display disp, IDocumentListener docListener,
  	                Object[] items,
  	                ICancelMonitor monitor) {
  		fMonitor = monitor ;
  		fDocListener = docListener ;
  		
  		if (items == null || items.length == 0) return ;
  		  		  		
  		  
		if (performDeltaUpdate ((SynchronizerWorkItem[])items)) {
			// Delta Merge
			for (int i = 0; i < items.length; i++) {
				if (monitor != null && monitor.isCanceled())
					return;

				final SynchronizerWorkItem we = (SynchronizerWorkItem) items[i];
				
				IModelChangeController controller = (IModelChangeController) fEDomain.getData(IModelChangeController.MODEL_CHANGE_CONTROLLER_KEY);				    
				try {
					we.refreshContents(fWorkingCopy.getWorkingCopy(true));
					CodeSnippetTranslator translator =
						new CodeSnippetTranslator(
							we.getSourceCode(),
							we.getPackageName(),
							we.getExtends(),
							we.getImplements(),
							we.getImports(),
							we.getFields(),
							we.getMethods(),
							monitor,
							we.getCompilationUnit(),
							we.getMethodsHandles(),
							we.getMethodSkeletons(),
							we.getInnerTypeHandles(),
							we.getInnerTypeSkeletons(),
							we.getChangedIndex(),
							we.isMethod());
					synchronized (controller) {
						// It is possible that multi work elements stagger, and we do not want
						// a later work element, to cache, an UnRestored state.
						if (fSetHold++==0) {
						  fHoldMsg = controller.getHoldMsg();
						  fHold = controller.isHoldChanges();						  
						}
						controller.setHoldChanges(true, null);
				    }
					Object lock = fBeanModel == null ? new Object() : fBeanModel.getDocumentLock();
					synchronized (lock) {
						if (monitor != null && monitor.isCanceled())
							return;

						translator.setDiagram(fCompositionModel);
						final ICodeDelta delta =
							translator.generateCodeDelta(
								fBeanModel,
								we.getChangedElementHandle(),
								we.getMethodSources()[we.getChangedIndex()]);
						if (monitor != null && monitor.isCanceled())
							return;

						// TODO Adapters will not react for GUI deltas !!!
						fBeanModel.setState(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL, true);
						if (delta == null || delta.getDeltaMethod() == null)
							JavaVEPlugin.log("NOT Driving a delta merge: DeltaMethod==NULL", Level.FINEST); //$NON-NLS-1$
						else
							JavaVEPlugin.log("Driving a delta merge: " + delta.getDeltaMethod(), Level.FINEST); //$NON-NLS-1$
						if (fBeanModel != null && delta != null && delta.getDeltaMethod() != null) {
							// Display thread
							disp.syncExec(new Runnable() {
								public void run() {
									try {
										CodeSnippetMergelet merglet =
											new CodeSnippetMergelet(
												delta,
												we.getChangedElementContent(),
												we.getChangedElementHandle(),
												we.isMethod());
										if (merglet.updateBDM(fBeanModel)) {
											fireUpdateNotification();
										}
									}
									catch (Throwable t) {
										JavaVEPlugin.log(t, Level.WARNING);
									}
								}
							});
						}
						if (fTransientErrorManager != null) {
							fTransientErrorManager.handleSharedToLocalChanges(fBeanModel, translator.getErrorsInCodeDelta(), monitor);
						}
						// We tried to apply the delta.  Check to see if we need
						// to reload from stratch - delta failure (e.g., method became an init, or not any more
						// and init method 		           		          
						if (fBeanModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) {
							fTransientErrorManager.handleSharedToLocalChanges(fBeanModel, translator.getErrorsInCodeDelta(), monitor);
							// Note that we still hold the lock. We can cause a dead lock if we try to Reload here.
							// Reload(disp, monitor);							
						}
						else {
							// If expressions removed beans from their container
							// we need to add them to the FF
							refreshFreeFrom(disp);
							fBeanModel.setState(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL, false);
						}
					}
					if (fBeanModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) {
						Reload(disp, monitor);
						break ;
					}
				}
				catch (Throwable e) {
					JavaVEPlugin.log(e, Level.FINE);
					// Reload from scratch will re-set the state of the BDM 		      	
					Reload(disp, monitor);
					break ;
				}
				finally {
					if (fBeanModel != null)
						fireSnippetProcessing(false);
					synchronized (controller) {
						   if (--fSetHold<=0) {
				             controller.setHoldChanges(fHold,fHoldMsg) ;
				             fSetHold = 0  ;
						   }
					}
				}
			}
		}
  		else {  // It is not a method delta
  		  try {  		  	
  			switch (getDeltaStatus(items)) {
  				case DELTA_SKIP: JavaVEPlugin.log(":) Skipping Unresolved Work Element Handle",Level.FINEST) ;  //$NON-NLS-1$
  				                  break ;
  				case DELTA_COMPLEX_DELTA:
  				case DELTA_BDM_CHANGE:
  				case DELTA_PARSE_ERROR:  				   
  			                      Reload(disp,monitor) ;
  			}
  		  }
  		  catch (Throwable t) {
  		  	JavaVEPlugin.log(t,Level.WARNING) ;
  		  }
          finally {
                 fireSnippetProcessing(false) ;        
          }  			
  		}
  	}	
  }
  
/**
 * Bean parts that needs to be on the FF and not, will be added
 * @param disp, if need to run async.
 */  
protected synchronized void refreshFreeFrom(Display disp) {
       
   BeanSubclassComposition ff = fCompositionModel.getModelRoot() ;   
   if (ff == null) return ;
   
   final EList ffBeans = ff.getComponents() ;
   final ArrayList     beansToAdd = new ArrayList() ;
   final ArrayList     beansToRemove = new ArrayList() ;
   
   Iterator itr = fBeanModel.getRootBeans().iterator() ;
   while (itr.hasNext()) {
       BeanPart bp = (BeanPart) itr.next() ;
       if (bp.getEObject() != null && bp.isInstanceVar() &&
           (bp.getEObject().eContainer() == null ||
           !bp.getEObject().eContainer().equals(ff))
          )
          beansToAdd.add(bp.getEObject()) ;       
   }   
   itr = ffBeans.iterator() ;
   while (itr.hasNext()) {
		EObject c = (EObject) itr.next();
		BeanPart bp = fBeanModel.getABean(c) ;
		if (bp == null || bp.getContainer()!=null) {
		   JavaVEPlugin.log("JavaSourceTranslator.refreshFreeFrom(): Removing: "+bp.getSimpleName()) ;  //$NON-NLS-1$
		   beansToRemove.add(c) ;		
		}
	}
   
   if (beansToAdd.size() == 0 && beansToRemove.size()==0) return ;
   
   if (disp != null) {
       disp.syncExec(new Runnable() {
            public void run() {    
                  try {
                        Iterator itr = beansToAdd.iterator() ;
                        while (itr.hasNext()) {
                            EObject obj = (EObject)itr.next() ;
                            ffBeans.add(obj) ;
                        }
                        itr = beansToRemove.iterator() ;
                        while (itr.hasNext()) {
							EObject obj = (EObject) itr.next();
							ffBeans.remove(obj) ;							
						}
                  }catch (Throwable t) {
                    JavaVEPlugin.log(t, Level.WARNING);
                  }
            }
        }) ;
   }
   else {
       try {
         Iterator i = beansToAdd.iterator() ;
         while (i.hasNext()) {
            EObject obj = (EObject)i.next() ;
            ffBeans.add(obj) ;
         }
         i = beansToRemove.iterator() ;
         while (itr.hasNext()) {
			EObject obj = (EObject) itr.next();
			ffBeans.remove(obj) ;							
		 }
      }catch (Throwable t) {
         JavaVEPlugin.log(t, Level.WARNING);
      }
   }
   
    
}  

/**
 *   Get the current RS.
 */
public ResourceSet getModelResourceSet() {
	return EMFEditDomainHelper.getResourceSet(fEDomain);
}

/**
 * Will create the proper MOF structure, and root element
 */
public EObject createEmptyComposition() throws CodeGenException {

	if (fUri == null)
		throw new CodeGenException("Model URI is not set"); //$NON-NLS-1$

	ResourceSet rs = getModelResourceSet();
	Resource cr = rs.getResource(URI.createURI(fUri), false);
	if (cr != null)
		rs.getResources().remove(cr);

	fResource = rs.createResource(URI.createURI(fUri));
	fRoot = JCMFactory.eINSTANCE.createBeanSubclassComposition();
	Diagram d = CDMFactory.eINSTANCE.createDiagram();
	d.setId(Diagram.PRIMARY_DIAGRAM_ID);
	fRoot.getDiagrams().add(d);
	fResource.getContents().add(fRoot);
	return fRoot;
}

public Diagram getDiagram() {
	if (fRoot == null)
		return null;
	List diagrams = fRoot.getDiagrams();
	for (int i = 0; i < diagrams.size(); i++) {
		Diagram element = (Diagram) diagrams.get(i);
		if (Diagram.PRIMARY_DIAGRAM_ID.equals(element.getId())) {
			return element;
		}
	}
	return null;
}

public EditDomain getEditDomain() {
	return fEDomain;
}

/**
 * Get the composition root
 */
public BeanSubclassComposition getModelRoot() {
	return fRoot;
}

/**
 * get the MOF Resource
 */
public Resource getModelResource() {
	return fResource;
}

/**
 * Set a new model document file, this function will drive all the configuration
 * changes that are needed to deal with a new document
 */
public void setInputResource(IFile file) throws CodeGenException {

	if (file == null)
		throw new CodeGenException("null Input File"); //$NON-NLS-1$
	disconnect(false);	// We are getting a new model, so disconnect everything.
	clearModel();

	fFile = file;
	fUri = fFile.getFullPath().toString();
}

/**
 * Erase the current Model
 */
public void clearModel() {

	// TODO May need to do more here
	setModelLoaded(false);
	fResource = null;
	if (fRoot != null)
		fRoot.eAdapters().clear();
	fRoot = null;
	fSourceDecoder = null;
	fFile = null;
	fUri = null;
}
 
/**
 *
 */
public void saveModel(IProgressMonitor pm) throws CodeGenException {
	saveDocument(pm);
}

/**
 * load the model from a file
 */
public void loadModel(IProgressMonitor pm) throws CodeGenException {
	if (fResource == null)
		createEmptyComposition();

	decodeDocument(this, fUri, fFile, pm);
	addTransientErrorListener(new DefaultTransientErrorHandler(fMsgRenderer));
	setModelLoaded(true);
}
 
/**
 * Add Transient error listeners
 */
public void addTransientErrorListener(ITransientErrorListener listener){
	if(fTransientErrorManager==null)
		fTransientErrorManager = new TransientErrorManager();
	fTransientErrorManager.addTransientErrorListener(listener);
}

/**
 *  Remove Transient error listeners
 */
public void removeTransientErrorListener(ITransientErrorListener listener){
	if(fTransientErrorManager!=null)
		fTransientErrorManager.removeTransientErrorListener(listener);
}

/**
 *  Decode the expression (code) impact on the bean (part)
 */
boolean  decodeExpression(CodeExpressionRef code) throws CodeGenException {
	
    return code.decodeExpression() ;
}

/**
 *  Create MOF instances 
 */
void  createJavaInstances () throws CodeGenException {
	Iterator itr = fBeanModel.getBeans().iterator() ;
	ArrayList err = new ArrayList() ;
    BeanSubclassComposition comp = fCompositionModel.getModelRoot() ;
	while (itr.hasNext()) {
	   BeanPart bean = (BeanPart) itr.next() ;
	   
	   EObject obj = bean.createEObject() ;
       String annotatedName= bean.getSimpleName() ;
       
       // The Model Builder will clean up irrelevent beans
       if (!bean.getSimpleName().equals(BeanPart.THIS_NAME)) {
    	   if (!(obj instanceof IJavaObjectInstance)) {    	   	  
    	      obj = null ;
    	      JavaVEPlugin.log("Bad Object: "+bean.getType()+": "+bean.getUniqueName(),Level.WARNING) ; //$NON-NLS-1$ //$NON-NLS-2$
    	   }
       }
       else {  // a this part
          if (obj != null) {
             ((XMIResource)comp.eResource()).setID(obj,MessageFormat.format(BeanPart.THIS_NAME+CodegenMessages.getString("CodegenMessages.ThisPart.uriID"), new Object[] {fRSuri})) ; //$NON-NLS-1$
             // If no annotation, the PS will not allow you to edit the name in composition
             annotatedName = null ;
          }
       }
       
       
	   if (obj == null) {
	    JavaVEPlugin.log("Could not create a JavaObjectInstance for: "+bean.getType()+": "+bean.getUniqueName(),Level.FINE) ; //$NON-NLS-1$ //$NON-NLS-2$
	    err.add(bean) ;
	    // Children will not be connected to the VCE model
	    Iterator bItr = bean.getChildren() ;
	    if (bItr != null)
	      while (bItr.hasNext()) 
	        err.add(bItr.next()) ;
	    // Remove from the JVE model if needed
	    bean.setEObject(null) ;
	   }
	   else {	        	    	   
	     Annotation an = CodeGenUtil.addAnnotation(obj) ;	
         if (annotatedName != null)
           CodeGenUtil.addAnnotatedName(an, annotatedName); 
         comp.getAnnotations().add(an) ;
	     try {		     
	       BeanPartFactory.updateInstanceInitString(bean) ;
	     }
	     catch (IllegalArgumentException e) {
	     	JavaVEPlugin.log(e,Level.FINE) ;
	     	if (!err.contains(bean)) {
	     	   err.add(bean) ;
	     	   // Children will not be connected to the VCE model
	           Iterator bItr = bean.getChildren() ;
	           if (bItr != null)
	             while (bItr.hasNext()) 
	                  err.add(bItr.next()) ;
	     	}
	     }
	   }
	}	
	for (int i = 0; i < err.size(); i++) {
        ((BeanPart)err.get(i)).dispose() ;
    }
}


void	addBeanPart(BeanPart bp, BeanSubclassComposition bsc) throws CodeGenException {
	
	boolean thisPart = bp.getSimpleName().equals(BeanPart.THIS_NAME) ? true : false ;

	bp.addToJVEModel() ;
	if (thisPart) {
	   if (bsc.eIsSet(JCMPackage.eINSTANCE.getBeanSubclassComposition_ThisPart())) 
	        throw new CodeGenException ("this Already initialized") ; //$NON-NLS-1$
	   bsc.setThisPart((IJavaObjectInstance)bp.getEObject()) ;	 
	}
	else if(bp.getContainer()==null && bp.isInstanceVar())
	   bsc.getComponents().add(bp.getEObject()) ; 
}

/**
 *  Given the BeanDOM, build the Composition Model
 */
void	buildCompositionModel() throws CodeGenException {
	if (fBeanModel == null || fCompositionModel == null) throw new CodeGenException ("null Builder") ; //$NON-NLS-1$
	
	fBeanModel.setState(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL,true) ;
	
	try{
		// Before handle expressions, make sure all BeanPart s have instances
		createJavaInstances() ;
	
	    // Decode the relevant expressions	
		Iterator itr = fBeanModel.getBeans().iterator() ;
		ArrayList badExprssions = new ArrayList() ;
		while (itr.hasNext()) {
			if (isReloadPending()) return ;
			
		    BeanPart bean = (BeanPart) itr.next() ;
			Collection expressions = new ArrayList(bean.getRefExpressions()) ;
			expressions.addAll((Collection)bean.getRefEventExpressions()) ;
		    Iterator refs = expressions.iterator() ;
		    // Process the expression referencing the bean, and build the 
		    // Composition.
		    while (refs.hasNext()) {
			  CodeExpressionRef codeRef = (CodeExpressionRef)refs.next() ;
		      
		      //if (getCorrespondingFeature(codeRef,obj) != null)
		      try {
			  if (!decodeExpression (codeRef)) {
				 JavaVEPlugin.log ("JavaSourceTranslator.buildCompositionModel() : Did not Decoded: "+codeRef, Level.FINE) ;						 //$NON-NLS-1$
				 badExprssions.add(codeRef) ;			 
			  }
		      }
		      catch (Exception e) {
		        JavaVEPlugin.log("Skipping expression: "+codeRef,Level.WARNING) ; //$NON-NLS-1$
		        JavaVEPlugin.log(e,Level.WARNING) ;
		        badExprssions.add(codeRef) ;	
		      }
		    }
		}	
		// Clean up
		itr = badExprssions.iterator() ;
		while (itr.hasNext()) {
			CodeExpressionRef codeRef = (CodeExpressionRef) itr.next() ;
			codeRef.getMethod().removeExpressionRef(codeRef) ;
			codeRef.getBean().removeRefExpression(codeRef) ;
			codeRef.getBean().addBadExpresion(codeRef);
		}
	
	
	      // Decoders have analyzed and acted on the Expressions - 
	      // it is time to hook them together withn the Compsition
	      // Model
	      itr = fBeanModel.getBeans().iterator() ;
		  while (itr.hasNext()) {
		  	
		  	if (isReloadPending()) return ;
		  	
	         BeanPart bean = (BeanPart) itr.next() ;
	       
		   // if a bean was added to a container, the decoder will reflect this in the BeamModel
		   
		   // Model is build (but annotations).   Turn the model on, as the EditParts may slam dunc
		   // new element (e.g., a content pane).  We need to react and generate the appropriate code.
		   fBeanModel.setState(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL, false) ;
		   fBeanModel.setState(IBeanDeclModel.BDM_STATE_UP_AND_RUNNING,true) ;
		   
		   addBeanPart(bean,fCompositionModel.getModelRoot()) ;
		   
//		   if (bean.getContainer() == null) {
//	       		// We are on the free from, 
//	       		BeanSubclassComposition comp = fCompositionModel.getModelRoot() ;
//	       		if (bean.getSimpleName().equals(BeanPart.THIS_NAME)) {
//	       		    if (comp.eIsSet(VcePackage.eINSTANCE.getBeanSubclassComposition_ThisPart())) throw new CodeGenException ("this Already initialized") ; //$NON-NLS-1$
//	           		comp.setThisPart((IJavaObjectInstance)bean.getEObject()) ;	           	           		
//	           		CDEHack.fixMe("Need to deal with FF annotation") ; //$NON-NLS-1$
//	       		}
//	       		else {	       		
//	          		comp.getComponents().add(bean.getEObject()) ;	          		
//	       		}
//	       		
	       		if (isReloadPending()) return ;
				if (bean.isInstanceVar()  || bean.getSimpleName().equals(BeanPart.THIS_NAME))
					try {
						fBeanModel.setState(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL, true);
						bean.getFFDecoder().decode();
					}
					finally {
						fBeanModel.setState(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL, false);
					}
//	   	   }
		}
	}finally{
		fBeanModel.setState(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL, false) ;
		fBeanModel.setState(IBeanDeclModel.BDM_STATE_UP_AND_RUNNING,true) ;
	}
} 
 
/**
 *  Go for parsing a Java Source
 */ 
public void decodeDocument (IDiagramModelInstance cm, String uri, IFile sourceFile,IProgressMonitor pm) throws CodeGenException {
	
	if (sourceFile == null || !sourceFile.exists()) 
	    throw new CodeGenException("Invalid Source File") ;	 //$NON-NLS-1$
	fCompositionModel = cm ;
	
    fMsgRrenderer.setStatus(IJVEStatus.JVE_CODEGEN_STATUS_SYNCHING,true) ;
	fMsgRrenderer.setStatus(IJVEStatus.JVE_CODEGEN_STATUS_OUTOFSYNC,true) ;
	fMsgRrenderer.setStatus(IJVEStatus.JVE_CODEGEN_STATUS_UPDATING_JVE_MODEL,true) ;
	
	
    reConnect(sourceFile) ;
    fFile = sourceFile ;

	fdisconnected=false ;
	fRSuri = uri ;

	fBeanModel = null ;
		
	// TODO Need to dispose the working copy at some point
	
	if (isReloadPending()) return;	
			
    fJavaModelBldr = new JavaBeanModelBuilder(fEDomain, fWorkingCopy,
                                              fWorkingCopy.getFile().getLocation().toFile().toString(),null) ;              
    
    fJavaModelBldr.setDiagram(fCompositionModel) ;
	fBeanModel = fJavaModelBldr.build() ; 	
	fBeanModel.setSourceSynchronizer(fSrcSync) ;
	fBeanModel.setFStatus(fMsgRrenderer) ;
	fJavaModelBldr = null ;
	
	if (isReloadPending()) return;	
		
	
	try {		
	  buildCompositionModel() ;
	  fMsgRrenderer.setStatus(ICodeGenStatus.JVE_CODEGEN_STATUS_OUTOFSYNC,false) ;
	}
	catch (Exception e) {
		JavaVEPlugin.log (e, Level.SEVERE) ; //$NON-NLS-1$
	}
	
	fMsgRrenderer.setStatus(ICodeGenStatus.JVE_CODEGEN_STATUS_SYNCHING,false) ;
	fMsgRrenderer.setStatus(ICodeGenStatus.JVE_CODEGEN_STATUS_UPDATING_JVE_MODEL,false) ;
	fireUpdateNotification();
		
	return;
}


/**
 *  Process a member that has no impact on the composition model
 */
protected int processDefaultMemberSave(IMember content, StringBuffer proposedBuff, 
                                       IProgressMonitor pm,int pOffset) 
                 throws JavaModelException {
	pm.subTask(MessageFormat.format(CodegenMessages.getString("JavaSourceTranslator.ProgressMonitor.MemeberSave"), new Object[]{content.getElementName()})) ; //$NON-NLS-1$
	pm.worked(1) ;  

    // deal with in between gaps	
	int cOffset = content.getSourceRange().getOffset() ;
	if (pOffset>=0 && cOffset>pOffset) {
		proposedBuff.append(content.getCompilationUnit().getBuffer().
		                    getText(pOffset,cOffset-pOffset)) ;		
	}	  			        	
	proposedBuff.append(content.getSource()) ;
	return cOffset+content.getSourceRange().getLength()  ;
}




/**
 * Process imports, package def, etc.
 */
protected void processPrefix(ICompilationUnit cu,StringBuffer proposedBuff, IProgressMonitor pm) 
                 throws JavaModelException {
	pm.subTask(MessageFormat.format(CodegenMessages.getString("JavaSourceTranslator.ProgressMonitor.ProcessCUHeaders"), new Object[]{cu.getElementName()})) ; //$NON-NLS-1$
	pm.worked(1) ;   
	int Offset=-1,Index=0 ;
	IJavaElement[] elements = cu.getChildren() ;
	if (elements.length>0) {
		while (Index<elements.length) {
// TODO need to consider inner classes
			if (elements[Index] instanceof IType) {
				IMethod[] methods = ((IType)elements[Index]).getMethods() ;
				if (methods.length>0) {
					Offset = methods[0].getSourceRange().getOffset() ;
					break ;
				}
			} ;
			Index++ ;
		}			
	}
	if (Offset==-1) Offset = cu.getSourceRange().getOffset() ;
	proposedBuff.append(cu.getBuffer().getText(0,Offset)) ;
}


/**
 * Process anything from the end of the last method
 */
protected void processPostfix(ICompilationUnit cu,StringBuffer proposedBuff, IProgressMonitor pm) 
                 throws JavaModelException {
	pm.subTask(MessageFormat.format(CodegenMessages.getString("JavaSourceTranslator.ProgressMonitor.ProcessCUFooters"), new Object[]{cu.getElementName()})) ; //$NON-NLS-1$
	pm.worked(1) ;   
	int Offset=-1 ;
	IMethod [] methods = CodeGenUtil.getMethods(cu) ;
	if (methods.length>0) {
			Offset = methods[methods.length-1].getSourceRange().getOffset()+
					 methods[methods.length-1].getSourceRange().getLength() ;
	}
	if (Offset>=0 &&cu.getSourceRange().getLength()-Offset>0) 
	   proposedBuff.append(cu.getBuffer().getText(Offset,cu.getSourceRange().getLength()-Offset)) ;
}

/**
 *  Delete the instance variale, and Code associated with bean
 */
protected void deleteBeanPart(ICompilationUnit CU, Vector changedElements, BeanPart bean) throws CodeGenException {

      BeanPartFactory bgen = new BeanPartFactory(fBeanModel,fCompositionModel) ;
      bgen.removeBeanPart(bean) ;
}


/**
 *  Remove methods/instance var. for Beans that are not in the composition anymore.
 */
protected boolean removeStaleMethods(ICompilationUnit workingCU, Vector changedElements, IProgressMonitor pm) throws CodeGenException {
	boolean removed=false ;
	
	Iterator itr = fBeanModel.getBeans().iterator() ;
	while (itr.hasNext()) {
		BeanPart bean = (BeanPart)itr.next() ;
		EObject obj = bean.getEObject() ;
		if (!CodeGenUtil.isComponentInComposition(fBeanModel, obj,fCompositionModel)) {
		   deleteBeanPart(workingCU, changedElements, bean) ;
		   removed=true ;
		}
	}	
	return removed ;
}



/**
 *  At this time the assumption is that the JAVA source code has not changed
 *  since we last parse, and that the MODEL has not updated the source online.
 */ 
public void saveDocument (IProgressMonitor pm) throws CodeGenException {
	if (fBeanModel == null) throw new CodeGenException ("No Bean Model") ; //$NON-NLS-1$
	

    //CU may have changed by now...
pm.beginTask(CodegenMessages.getString("JavaSourceTranslator.ProgressMonitor.SaveDocument"),20) ; //$NON-NLS-1$

    
    
 
    try {     
//    	// workingCU =(ICompilationUnit) fBeanModel.getCompilationUnit().getWorkingCopy() ;
//    	workingCU = fWorkingCopy.getLocalWorkingCopy() ;
////JavaVEPlugin.log("Starting with:\n["+workingCU.getSource()+"]\n") ;   	
//    	// Update Code/Model with new components
//    	walkCompositionModel(workingCU,changedElements,pm) ;
//    	// Sync. Code with bean's context 
////JavaVEPlugin.log("After walking composition:\n["+workingCU.getSource()+"]\n") ;   	    	
//    	walkJavaCode(workingCU,changedElements,pm) ;
////JavaVEPlugin.log("After walking Java:\n["+workingCU.getSource()+"]\n") ;   	    	    	
//    	needToCommit = changedElements.size() > 0 ;
//    	
//    	
//    	if (needToCommit) 
//    		JavaVEPlugin.log("Updating Java Working Copy") ;    	
//    	else
//    	   JavaVEPlugin.log("No Update is needed to the Java Working Copy") ;
//    	    
//    	//fWorkingCopy.UpdateDeltaToShared(pm,changedElements,true)   ;  		
//    	fWorkingCopy.UpdateDeltaToShared(null,null,changedElements,true)   ;  		
    	
    	// The big red Reload button
    	reloadFromScratch(Display.getCurrent(),null) ;
    	      
    } catch (Exception e) {
    	JavaVEPlugin.log(e, Level.WARNING);
    }
    finally {
//    	if (workingCU!=null)  workingCU.destroy() ;    	    	
    }             
}
	
// What file extention does this decoder works with
public String  getFileExt() {  
	return JAVAExt; 
}

protected void setHoldGUIChanges(boolean flag, String msg) {
	IModelChangeController controller = (IModelChangeController) fEDomain.getData(IModelChangeController.MODEL_CHANGE_CONTROLLER_KEY);
	controller.setHoldChanges(flag, msg) ;
	fMsgRrenderer.setStatus(ICodeGenStatus.JVE_CODEGEN_STATUS_PARSE_ERRROR,flag) ;
}

public void reloadFromScratch(Display disp, ICancelMonitor monitor) throws CodeGenException {
	
	
	if (fFile==null)
	  fFile = fWorkingCopy.getFile() ; 
	  
	if (fFile == null) throw new CodeGenException ("No Resource") ; //$NON-NLS-1$
	
	try {
		// Do not clear the VCE model - may not be on the UI thread.
		if (monitor != null && monitor.isCanceled())
			return;

		synchronized (getLoadLock()) {
			disp.syncExec(new Runnable() {
				public void run() {
					try {
						fMsgRrenderer.setStatus(ICodeGenStatus.JVE_CODEGEN_STATUS_RELOAD_IN_PROGRESS, true);
						JavaVEPlugin.log("*** Reloading", Level.FINE); //$NON-NLS-1$
						disconnect(false); // clear the BDM, but do not dispose of local Doc, and Synch.

						fMsgRrenderer.setStatus(ICodeGenStatus.JVE_CODEGEN_STATUS_SYNCHING, true);
						fMsgRrenderer.setStatus(ICodeGenStatus.JVE_CODEGEN_STATUS_OUTOFSYNC, true);
						fMsgRrenderer.setStatus(ICodeGenStatus.JVE_CODEGEN_STATUS_UPDATING_JVE_MODEL, true);
						setHoldGUIChanges(false, null);
						clearModel(true);
						try {						
							decodeDocument(fCompositionModel, fRSuri, fFile, null);
						}
						catch (CodeGenException e) {
							if (e instanceof CodeGenSyntaxError) throw e ;							
						}
						if (fMsgRrenderer != null)
							fMsgRrenderer.showMsg(null, IJVEStatus.NORMAL_MSG);
						setHoldGUIChanges(false, null);
					} catch (Throwable t) {
						if (fMsgRrenderer != null)
							fMsgRrenderer.showMsg(t.getMessage(), IJVEStatus.ERROR_MSG);

						Level severity = Level.WARNING;
						if (t instanceof CodeGenException)
							severity = Level.FINE;
						if (t instanceof CodeGenSyntaxError)
							JavaVEPlugin.log(t.toString(), severity);
						else
							JavaVEPlugin.log(t, severity);
						setHoldGUIChanges(true, t.getMessage());
						fireUpdateNotification();
					}
				}
			});
		}
	}
	finally {
		fMsgRrenderer.setStatus(ICodeGenStatus.JVE_CODEGEN_STATUS_RELOAD_IN_PROGRESS, false);
	}
}

public void pauseRoundTripping(boolean flag) throws CodeGenException {

    // We need to clean up locking and such, but we should not try to lock the model while on the display thread unless nothing
    // is going on
    if (fMsgRrenderer.isStatusSet(ICodeGenStatus.JVE_CODEGEN_STATUS_SYNCHING)||
        fMsgRrenderer.isStatusSet(ICodeGenStatus.JVE_CODEGEN_STATUS_OUTOFSYNC)||
        fMsgRrenderer.isStatusSet(ICodeGenStatus.JVE_CODEGEN_STATUS_UPDATING_SOURCE)||
        fMsgRrenderer.isStatusSet(ICodeGenStatus.JVE_CODEGEN_STATUS_UPDATING_JVE_MODEL)||
        fMsgRrenderer.isStatusSet(ICodeGenStatus.JVE_CODEGEN_STATUS_RELOAD_PENDING)||
        fMsgRrenderer.isStatusSet(ICodeGenStatus.JVE_CODEGEN_STATUS_RELOAD_IN_PROGRESS))
        return ;
        
    if (fpauseRoundTripping && flag) return ;
    IModelChangeController controller = (IModelChangeController) fEDomain.getData(IModelChangeController.MODEL_CHANGE_CONTROLLER_KEY);
    if (flag) {  // Want to pause
    	fMsgRrenderer.setStatus(ICodeGenStatus.JVE_CODEGEN_STATUS_PAUSE,true) ;
    	if (controller.isHoldChanges()) return ; // Already paused
    	fpauseRoundTripping = true ;
    	Object lock = fBeanModel == null ? new Object() : fBeanModel.getDocumentLock() ;
    	synchronized (lock) {
    	  disconnect(false) ;    	
    	  controller.setHoldChanges(true,fPauseSig) ;    	//$NON-NLS-1$    	
    	}
    } else {
		if (!controller.isHoldChanges())
			return;

		if (fPauseSig.equals(controller.getHoldMsg())) { //$NON-NLS-1$
			try {
				Object lock = fBeanModel == null ? new Object() : fBeanModel.getDocumentLock() ;
				synchronized (lock) {
					controller.setHoldChanges(false, null);
					fMsgRrenderer.setStatus(ICodeGenStatus.JVE_CODEGEN_STATUS_RELOAD_IN_PROGRESS, true);
					fMsgRrenderer.setStatus(ICodeGenStatus.JVE_CODEGEN_STATUS_PAUSE, false);
					fpauseRoundTripping = false;
					reconnect(null, null);
				}
			}
			finally {
				fMsgRrenderer.setStatus(ICodeGenStatus.JVE_CODEGEN_STATUS_RELOAD_IN_PROGRESS, false);
			}
		}
	}
}

/**
 * This will clear the BDM, and optionaly the VCE model
 */
private   void clearModel(boolean vceModel) {
	 
	try {
	if (fCompositionModel != null) {
        EObject root = fCompositionModel.getModelRoot() ;
        if (root != null) {
             ICodeGenAdapter a = (ICodeGenAdapter)EcoreUtil.getExistingAdapter(root,ICodeGenAdapter.JVE_CODE_GEN_TYPE) ;
             while  (a != null) {
                root.eAdapters().remove(a);
                a = (ICodeGenAdapter)EcoreUtil.getExistingAdapter(root,ICodeGenAdapter.JVE_CODE_GEN_TYPE) ;
             }
        }    
    }
       
   if (fBeanModel != null) { 
        fBeanModel.dispose() ;        
        fBeanModel = null ;        
   }
   
   
   if (vceModel == true && fCompositionModel != null) {
      org.eclipse.ve.internal.jcm.BeanSubclassComposition comp = fCompositionModel.getModelRoot() ;
      // TODO Deal with multiple diagrams
	  
	  if (comp.getComponents().size()>0)
	     comp.getComponents().clear() ;
	  if (comp.eIsSet(JCMPackage.eINSTANCE.getBeanSubclassComposition_ThisPart()))
	     comp.eUnset(JCMPackage.eINSTANCE.getBeanSubclassComposition_ThisPart()) ;
	  if (comp.getDiagrams().size()>0) {
	     Diagram d = (Diagram) comp.getDiagrams().get(0) ;
	     if (d.getVisualInfos().size()>0)                             
	        d.getVisualInfos().clear() ;
	  }
	  if (comp.getAnnotations().size()>0)
	     comp.getAnnotations().clear() ;
	  if (comp.getMethods().size()>0)
	     comp.getMethods().clear() ;
	  if (comp.getMembers().size()>0)
	     comp.getMembers().clear() ;
	  if (comp.getListenerTypes().size() > 0)
	     comp.getListenerTypes().clear() ;
	     
   }
	}
	catch (Exception e) {
		JavaVEPlugin.log(e,Level.FINE) ;
	}
}

public synchronized void reConnect(IFile file) {
	clearModel(true) ;
	if (fWorkingCopy == null) {
	    fWorkingCopy = new WorkingCopyProvider(file) ;		    
    }
    else if (fdisconnected)
       fWorkingCopy.connect(file) ;
       
    if (fSrcSync == null) {
	   fSrcSync = new JavaSourceSynchronizer(fWorkingCopy,this) ;
	   fSrcSync.setSharedUpdatingLocalStrategy(new SharedToLocalUpdater()) ;
	   fSrcSync.setDelay(fSrcSyncDelay) ;
	   fSrcSync.setStatus(fMsgRrenderer) ;
    }
    else if(fdisconnected)
       fSrcSync.connect() ;

	fdisconnected=false ;
}

public synchronized void reconnect(org.eclipse.ui.IFileEditorInput input,IProgressMonitor pm) throws CodeGenException {
	IFile file ;
	if (input != null) 
	  file = input.getFile() ;
    else
      file = fFile ;
	decodeDocument(fCompositionModel, file.getFullPath().toString(), file, pm) ;
}

/**
 * Same as dispose, but do not destroy the WorkingProvider
 */
public synchronized void disconnect(boolean clearVCEModel) {

    if (fSrcSync != null) {
       commit();    
       fSrcSync.disconnect() ;
    }


    clearModel(clearVCEModel) ;
        
    if (fSrcSync != null) // fWorkingCopy may not be null yet if called from dispose
       fWorkingCopy.disconnect() ;
    
   
    fTransientErrorManager = new TransientErrorManager();
    fdisconnected=true ;
    

}

/**
 * Clear the VCE Model, disconnect, and get rid of all resources.
 */
public synchronized void dispose() {
	
	if (fSrcSync != null) {
		fSrcSync.setSharedUpdatingLocalStrategy(null) ;
		commit();
		fSrcSync.uninstall() ;
		fSrcSync = null ;
		// The following is a hack until CodeGen uses the EditDomain.		
		CodeGenUtil.clearCache()  ;
		PropertyFeatureMapper.clearCache() ;
		InstanceVariableCreationRule.clearCache() ;
		InstanceVariableRule.clearCache() ;
	}
    
    disconnect(true) ;
		
	if (fWorkingCopy != null) 
	   fWorkingCopy.dispose() ;
	
	fWorkingCopy = null ;
}

public boolean isReloadPending() {
	if (fMsgRrenderer.isStatusSet(ICodeGenStatus.JVE_CODEGEN_STATUS_RELOAD_PENDING)) return true ;
	
	Display display = Display.getCurrent();          
    if (display != null) {
    	boolean run = true;
    	while (run) {
    		try {
				run = display.readAndDispatch();
    		} catch (Exception e) {
    			JavaVEPlugin.log(e);
    		}
    	}
    	if (fMsgRrenderer.isStatusSet(ICodeGenStatus.JVE_CODEGEN_STATUS_RELOAD_PENDING)) return true ;
    }
    return false ;    	 	
}

/**
 * This one provide a Synchronous call to drive a commit process
 * This will induce CodeGen to remove any beans that were marked for deletion
 * and will be considered an end to a top down transaction.  
 */
public void commit() {
	
	// First commit	
	if (fBeanModel != null && !fBeanModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) {
		fBeanModel.deleteDesignatedBeans() ;
		fBeanModel.docChanged();
	}
      	 		      	 			                    
    JavaVEPlugin.log("JavaSourceTranslator: commit",Level.FINEST) ;         //$NON-NLS-1$
}
/**
 * This one provide an Async. registration for a notification on flush process
 * 
 * No need to use asynchroneous commit anymore, as no flushing is performed anymore.
 * Only synchrenous commnet commit use commit(boolean) instead
 * 
 * @deprecate
 */
public void commitAndFlush(ISynchronizerListener listener, String marker) {
    
    // First commit
    
    commit() ;

    // No need to wait for the synchronizer anymore to flush.  Top Down
    // is done on the actual CU.
    if (listener == null) return ;
    else listener.markerProcessed(marker) ;
    
//    
//        // If we are doing reload from scratch, or bring up, no point to wait 
//    if (fBeanModel == null || !fBeanModel.isStateSet(IBeanDeclModel.BDM_STATE_UP_AND_RUNNING)) {
//       JavaVEPlugin.log("JavaSourceTranslator: commitAndFlush(sync,  BringUp - returned")	 ; //$NON-NLS-1$
//       listener.markerProcessed(marker) ;
//       return ;
//    }
//    
//    JavaVEPlugin.log("JavaSourceTranslator: commitAndFlush(async) - start",Level.FINEST) ;          //$NON-NLS-1$
//    if (fSrcSync == null) 
//      listener.markerProcessed(marker) ;
//    else     
//      fSrcSync.notifyOnMarker(listener,marker,false) ;    
    JavaVEPlugin.log("JavaSourceTranslator: commitAndFlush - done",Level.FINEST) ;         //$NON-NLS-1$
}

public void setSynchronizerSyncDelay(int delay) {
    fSrcSyncDelay = delay ;
    if (fSrcSync != null)
      fSrcSync.setDelay(fSrcSyncDelay) ;
}


protected void fireUpdateNotification() {
    
    JavaVEPlugin.log("JavaSourceTranslator.fireUpdateNotification",Level.FINEST) ; //$NON-NLS-1$
    for (Iterator itr = fTranslatorListeners.iterator(); itr.hasNext();) {
        ISourceTranslatorListener element = (ISourceTranslatorListener) itr.next();
        try {
            element.modelUpdated() ;
        }
        catch (Throwable t) {
            JavaVEPlugin.log(t,Level.WARNING) ;
        }        
    }
}

public  void  fireSnippetProcessing(boolean start) {
    
    
     for (Iterator itr = fTranslatorListeners.iterator(); itr.hasNext();) {
        ISourceTranslatorListener element = (ISourceTranslatorListener) itr.next();
        try {
            if (start) {
              // Snippet Operations may have started, but cancelled in the middle
              // to make room for the new update that is coming in.
              if (fSnippetProcessingInProgress) return ;
              fSnippetProcessingInProgress = true ;
              element.snippetProcessingStart() ;
            }
            else {  
              if (fSrcSync.isWorkQueued()) return ;  // Wait
              element.snippetProcessingCompleted() ;
              fSnippetProcessingInProgress = false ;
            }
        }
        catch (Throwable t) {
            JavaVEPlugin.log(t,Level.WARNING) ;
        }        
     }  
     JavaVEPlugin.log("JavaSourceTranslator.fireSnippetProcessin, Start="+start,Level.FINEST) ; //$NON-NLS-1$
}    


public void addTranslatorListener (ISourceTranslatorListener listener) {
    if (listener == null) return ;
    if (fTranslatorListeners.contains(listener)) return ;
    fTranslatorListeners.add(listener) ;
}

public void removeTranslatorListener (ISourceTranslatorListener listener) {
    if (listener == null) return ;
    fTranslatorListeners.remove(listener) ;    
}

public void setMsgRenderer (IJVEStatus mr) {
    fMsgRrenderer = mr ;
    if (fSrcSync != null)
       fSrcSync.setStatus(mr) ;
}
public void setEditDomain(EditDomain d) {
	fEDomain = d ;
}

protected synchronized void setModelLoaded(boolean fModelLoaded) {
	this.fModelLoaded = fModelLoaded;
}

public synchronized boolean isModelLoaded() {
	return fModelLoaded;
}



private Object loadLock = new Object();
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.codegen.IDiagramModelBuilder#getDocumentLock()
	 */
	public synchronized Object getLoadLock() {
		return loadLock;
	}

}