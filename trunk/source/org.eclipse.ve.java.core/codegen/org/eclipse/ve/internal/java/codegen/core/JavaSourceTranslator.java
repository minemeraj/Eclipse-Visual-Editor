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
 *  $Revision: 1.33 $  $Date: 2004-05-12 11:39:40 $ 
 */
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.ISynchronizable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IFileEditorInput;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cdm.Annotation;
import org.eclipse.ve.internal.cdm.Diagram;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.IModelChangeController;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.jcm.BeanSubclassComposition;
import org.eclipse.ve.internal.jcm.JCMPackage;

import org.eclipse.ve.internal.java.codegen.editorpart.CodegenEditorPartMessages;
import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;




public class JavaSourceTranslator implements IDiagramSourceDecoder, IDiagramModelBuilder { 
	

         
IBeanDeclModel          fBeanModel ;
IVEModelInstance   		fVEModel = null ;
IWorkingCopyProvider    fWorkingCopy = null ;
JavaSourceSynchronizer  fSrcSync = null ;
int						fSrcSyncDelay = JavaSourceSynchronizer.DEFAULT_SYNC_DELAY ;
IFile                   fFile = null ;
EditDomain 			    fEDomain = null ;
boolean                 fdisconnected = true ;
boolean					floadInProgress=false;
boolean					fmodelLoaded=false;
boolean					fparseError=false;
ArrayList				fListeners = new ArrayList();


public static String    fPauseSig = CodegenEditorPartMessages.getString("JVE_STATUS_MSG_PAUSED") ;  //$NON-NLS-1$
public static int       fCommitAndFlushNestGuard = 0 ;


IDiagramSourceDecoder fSourceDecoder = null;


  /**
   *  This is the strategy for updating the shared Java document after
   *  a change in the local document.
   */	
  class  SharedToLocalUpdater implements IBackGroundWorkStrategy {
  	ICompilationUnit fWorkingCopy = null ;
  	int fEventsProcessedCount = 0; // Counts the # of times the lockManager.setGUIRO(false) should be called
  	  	  	
  	boolean          fHold = false ;
  	String            fHoldMsg = null ;
  	int          	  fSetHold = 0 ;
  	Display fDisplay = null;  
  	
  	// Gatther all the JDT information we can get in takeCurrentSnapshot()
  	protected String currentSource = null ;
  	protected int[] importStarts;
  	protected int[] importEnds;
  	protected int[] fieldStarts;
  	protected int[] fieldEnds;
  	protected int[] methodStarts;
  	protected int[] methodEnds;
  	protected String[] methodHandles;
  	protected String[] fieldHandles;
  	
  	/**
  	 *  ReLoad the BDM model from stratch
  	 */
  	private  void Reload(Display disp,ICancelMonitor monitor) {
  		IModelChangeController controller = (IModelChangeController) getEditDomain().getData(IModelChangeController.MODEL_CHANGE_CONTROLLER_KEY);
  		// If the controller is inTransaction, that means top down on display thread.
  		// CodeGen should have been marked as busy before it starts processing
		if(controller!=null && controller.inTransaction())
		    throw new RuntimeException("should not be here") ;

		// We have no idea when the load will be called by the editor.
		// We need to stop the synch. from driving snippets, as we are going
		// to load from scratch anyhow
		fSrcSync.stallProcessing();
		try {
			if (fBeanModel!=null) 
			 fBeanModel.setState(IBeanDeclModel.BDM_STATE_DOWN,true);
		} catch (CodeGenException e) {}
		fireReloadIsNeeded();
 
 	}
  	
  	
  	
  	/**
  	 * Takes the snapshots of constructors and ordinary methods. No CLINIT and Synthetic
  	 * type methods will be recorded, as they are not needed. Snapshot includes method
  	 * handles, their start and end positions
  	 * 
  	 * @param primaryType
  	 * @throws JavaModelException
  	 * 
  	 * @since 1.0.0
  	 */
  	protected void takeMethodsSnapshot(final IType primaryType) throws JavaModelException{
  		if(primaryType==null)
  			return ;
  		List pureSourceMethods = new ArrayList() ;
  		IMethod[] methods = primaryType.getMethods() ;
  		if(methods!=null){
  			for (int mc = 0; mc < methods.length; mc++) {
  				// Ignore transient methods, as they are generated by compiler 
  				// and are found only in binary types
				if(Flags.isTransient(methods[mc].getFlags()))
					continue ;
				// Ignore CLInit methods, as they are not required for model parsing (Java Model Builder)
				// and also multiple static{} declarations in code result in only ONE CLInit method in AST, and none in JDT
				// CLINIT methods are not returned for source types - since we are taking snapshots 
				// of source types, we dont have to worry about them.
				
				// Ignore constructors as there is nothing useful as of now in constructors
				// TODO - Consider the case for having expressions in constructors
				if(methods[mc].isConstructor())
					continue ;
				
				pureSourceMethods.add(methods[mc]) ;
			}
  		}
  		methodHandles = new String[pureSourceMethods.size()] ;
  		methodStarts = new int[pureSourceMethods.size()] ;
  		methodEnds = new int[pureSourceMethods.size()] ;
  		if(pureSourceMethods.size()>0){
  			int count = 0 ;
  			for (Iterator iter = pureSourceMethods.iterator(); iter.hasNext(); count++) {
				IMethod method = (IMethod) iter.next();
				methodHandles[count] = method.getHandleIdentifier() ;
				ISourceRange sr = method.getSourceRange() ;
				methodStarts[count] = sr.getOffset() ;
				methodEnds[count] = sr.getOffset() + sr.getLength() ;
			}
  		}
  	}
  	
  	protected void takeFieldSnapshot(final IType primaryType) throws JavaModelException{
  		if(primaryType==null)
  			return ;
  		List pureSourceFields = new ArrayList() ;
  		IField[] fields = primaryType.getFields() ;
  		if(fields!=null){
  			for (int fc = 0; fc < fields.length; fc++) {
				if(Flags.isTransient(fields[fc].getFlags()))
					continue ;
				pureSourceFields.add(fields[fc]) ;
			}
  		}
		fieldHandles = new String[fields.length] ;
		fieldStarts = new int[fields.length] ;
		fieldEnds = new int[fields.length] ;
		if(pureSourceFields.size()>0){
			int count = 0 ;
			for (Iterator iter = pureSourceFields.iterator(); iter.hasNext(); count++) {
				IField field = (IField) iter.next();
				fieldHandles[count] = field.getHandleIdentifier() ;
				ISourceRange sr = field.getSourceRange() ;
				fieldStarts[count] = sr.getOffset() ;
				fieldEnds[count] = sr.getOffset() + sr.getLength() ;
			}
  		}
  	}
  	
  	protected void takeImportSnapshot(final ICompilationUnit cu) throws JavaModelException{
  		if(cu==null)
  			return ;
  		IImportDeclaration[] imports = cu.getImports() ; 
  		if(imports!=null){
  			importEnds = new int[imports.length] ;
  			importStarts = new int[imports.length] ;
  			for (int ic = 0; ic < imports.length; ic++) {
  				ISourceRange sr = imports[ic].getSourceRange() ; 
  				importStarts[ic] = sr.getOffset() ; 
  				importEnds[ic] = sr.getOffset() + sr.getLength() ;
  			}
  		}
  	}
  	
  	protected void allSnapshotEventsProcessed(ICodegenLockManager lockManager){
		if (fEventsProcessedCount > 0) {
			for (int i = 0; i < fEventsProcessedCount; i++) {
				lockManager.setGUIReadonly(false);
			}
			fEventsProcessedCount = 0;
		}
  	}
  	
  	protected boolean takeCurrentSnapshot(final ICodegenLockManager lockManager, final List allEvents, final ICompilationUnit workingCopy){
  		currentSource = null ;
  		importEnds = new int[0] ;
  		importStarts = new int[0] ;
  		fieldEnds = new int[0] ;
  		fieldStarts = new int[0] ;
  		methodEnds = new int[0] ;
  		methodStarts = new int[0] ;
  		methodHandles = new String[0] ;
  		fieldHandles = new String[0];
  		
  		if(allEvents!=null && allEvents.size()>0){
  			Object lock = ((DocumentEvent)allEvents.get(0)).getDocument();
  			if (lock instanceof ISynchronizable)
      		        lock = ((ISynchronizable)lock).getLockObject();
  			synchronized(lock){
  				if(allEvents.contains(JavaSourceSynchronizer.RELOAD_HANDLE)){
  					// Reload request
  					fEventsProcessedCount = allEvents.size()-1;
  					allEvents.clear();
  					return true;
  				}else{
  					fEventsProcessedCount = allEvents.size();
  					allEvents.clear();
  					lockManager.setThreadScheduled(false);
  					try {
  				  		workingCopy.reconcile(false, false, null, null);
 						currentSource = workingCopy.getBuffer().getContents();
  						
  						IType primaryType = workingCopy.findPrimaryType() ;
  						
  						// Record JDT methods
  						takeMethodsSnapshot(primaryType) ;
  						
  						// Record JDT fields
  						takeFieldSnapshot(primaryType) ;
  						
  						// Record JDT imports
  						takeImportSnapshot(workingCopy) ;
  					} catch (JavaModelException e) {
  						JavaVEPlugin.log(e);
  					}
  				}  				
  			}  			
  		}
  		return false;
  	}
  	
  	protected CompilationUnit parse(String source){
   		ASTParser parser = ASTParser.newParser(AST.LEVEL_2_0);
		parser.setSource(source.toCharArray());
		return (CompilationUnit) parser.createAST(null);
  	}
  	
  	protected boolean containsParseErrors(CompilationUnit cuAST){
  		IProblem[] parseProblems = cuAST.getProblems();
  		boolean parseErrors = false;
  		for (int i = 0; i < parseProblems.length; i++) {
			if(parseProblems[i].isError())
				parseErrors = true;
		}
		return parseErrors;
  	}
  	
  	/**
  	 * Handles the non-parseable stage, and returns whether successful or not.
  	 * 
  	 * @param cuAST
  	 * @param workingCopy
  	 * @param cancelMonitor
  	 * @return
  	 * 
  	 * @since 1.0.0
  	 */
  	protected boolean handleNonParseable(CompilationUnit cuAST, ICompilationUnit workingCopy, ICancelMonitor cancelMonitor){
  		fBeanModel.refreshMethods();
  		return true;
  	}
  	
  	/**
  	 * Tries to merge the main BDM with the newly constructed BDM.
  	 * Returns whether it was successful in merging or not.
  	 * @param mainModel
  	 * @param newModel
  	 * @param m
  	 * @return
  	 * @throws CodeGenException
  	 * 
  	 * @since 1.0.0
  	 */
  	protected boolean merge( IBeanDeclModel mainModel, IBeanDeclModel newModel, ICancelMonitor m ) throws CodeGenException {
  		BDMMerger merger = new BDMMerger(mainModel, newModel, true, fDisplay);
  		return merger.merge();
   	}
  	
  	/*
  	 * This method should return the method handles of all methods 
  	 * in the compilation unit in the exact same order that the TypeVisitor
  	 * would visit the method declarations
  	 */
  	protected String[] getAllMethodHandles(ICompilationUnit cu){
  		if(cu!=null && cu.findPrimaryType()!=null){
  			try {
				IMethod[] methods = cu.findPrimaryType().getMethods() ;
				String[] methodHandles = new String[methods==null ? 0 : methods.length];
				for (int methodC = 0; methods!=null && methodC < methods.length; methodC++) {
					methodHandles[methodC] = methods[methodC].getHandleIdentifier() ;
				}
				return methodHandles ;
			} catch (JavaModelException e) {
				JavaVEPlugin.log(e) ;
			}
  		}
  		return new String[0] ;
  	}
  	
  	/**
  	 * Handles the parseable stage, and returns whether successful or not.
  	 * 
  	 * @param cuAST
  	 * @param workingCopy
  	 * @param cancelMonitor
  	 * @return
  	 * 
  	 * @since 1.0.0
  	 */
  	protected boolean handleParseable(CompilationUnit cuAST, ICompilationUnit workingCopy, ICancelMonitor cancelMonitor) throws CodeGenException {
  		JavaBeanModelBuilder modelBldr =
			new CodeSnippetModelBuilder(fEDomain, currentSource, methodHandles, importStarts, importEnds, fieldStarts, fieldEnds, methodStarts, methodEnds, workingCopy);
		modelBldr.setDiagram(fVEModel);
		IBeanDeclModel bdm = null;
		try {
			bdm = modelBldr.build();
		} catch (CodeGenException e) {
			JavaVEPlugin.log(e) ;
		}
		if(bdm!=null)
			return merge(fBeanModel, bdm, cancelMonitor);
		return false;
 	}
  	
	public void run(
			Display disp,
			ICompilationUnit workingCopy,
			ICodegenLockManager lockManager,
			List allDocEvents,
			ICancelMonitor monitor) {

			fWorkingCopy = workingCopy;
			fDisplay = disp;
			
			fireSnippetProcessing(true);

			if (monitor != null && monitor.isCanceled()) {
				if (fBeanModel != null)
					fireSnippetProcessing(false);
				lockManager.setThreadScheduled(false);
				return;
			}

			try {
				if (monitor != null && monitor.isCanceled()) {
					if (fBeanModel != null)
						fireSnippetProcessing(false);
					lockManager.setThreadScheduled(false);
					return;
				}

				fEventsProcessedCount = 0;
				
				// We have to call takeCurrentSnapShot to clear events properly
				boolean reloadRequired = takeCurrentSnapshot(lockManager, allDocEvents, workingCopy) ;
				synchronized (JavaSourceTranslator.this) {
				        reloadRequired |= ((fBeanModel == null) && !floadInProgress) ;
				}
				if (reloadRequired) {
					Reload(fDisplay, monitor);
				} else {
					if (currentSource == null || monitor != null && monitor.isCanceled()) {
						if (fBeanModel != null)
							fireSnippetProcessing(false);
						
						return;
					}
					
					CompilationUnit ast = parse(currentSource);

					if (monitor != null && monitor.isCanceled()) {
						if (fBeanModel != null)
							fireSnippetProcessing(false);
						return;
					}

					// only one updater can be touching the model at any time.
					//TODO: Can not lock here the JavaTranslator because isBusy() may be called
					//      recursivly
					synchronized(JavaSourceTranslator.this.fWorkingCopy.getDocLock()){

						// TODO Adapters will not react for GUI deltas !!!
						if (fBeanModel!=null)
							fBeanModel.setState(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL, true);
						
						if (containsParseErrors(ast)) {
							reloadRequired = reloadRequired || !handleNonParseable(ast, fWorkingCopy, monitor);
							fireParseError(true);
						} else {
							reloadRequired = reloadRequired || !handleParseable(ast, fWorkingCopy, monitor);
							fireParseError(false);
						}

						//TODO: we need to only notify if the VE model was updated
						fireModelChanged();
	
						fBeanModel.setState(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL, false);
	
						reloadRequired = reloadRequired || fBeanModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN);

						if(reloadRequired){
							Reload(disp, monitor);
						}
					}
				}
			} catch (Throwable e) {
				JavaVEPlugin.log(e, Level.FINE);
				// Reload from scratch will re-set the state of the BDM
				Reload(disp, monitor);
			} finally {
				allSnapshotEventsProcessed(lockManager);
				if (fBeanModel != null)
					fireSnippetProcessing(false);
			}
		}
  }

	/**
	 * @since 1.0.0
	 */
	public JavaSourceTranslator(EditDomain ed) {
		fEDomain=ed;
	}  
  
/**
 * Bean parts that needs to be on the FF and not, will be added
 * @param disp, if need to run async.
 * @deprecated
 */  
protected synchronized void refreshFreeFrom(Display disp) {
       
   BeanSubclassComposition ff = fVEModel.getModelRoot() ;   
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
 * @deprecate
 */
public ResourceSet getModelResourceSet() {
	return EMFEditDomainHelper.getResourceSet(fEDomain);
}





public EditDomain getEditDomain() {
	return fEDomain;
}

 

/**
 * load the model from a file
 */
public  void loadModel(IFileEditorInput input, IProgressMonitor pm) throws CodeGenException  {
		// loadModel is not synchronized as to
		// not block calls to isBusy(), pause() and such while the loadModel is going on
		pm.beginTask("", 100);
		pm.subTask("Loading model from source code");	
		Object l = fWorkingCopy==null? new Object() : fWorkingCopy.getDocLock();
		synchronized (l) {
			floadInProgress = true;
			if (fVEModel != null) {
				if (fBeanModel != null && !fdisconnected) {
					disconnect(false);
					if (fBeanModel != null)
						fBeanModel.setState(IBeanDeclModel.BDM_STATE_DOWN, true);
					fBeanModel = null;
				}
			}
		}
		fireStatusChanged(CodegenEditorPartMessages.getString("JVE_STATUS_MSG_LOAD"));

		fFile = input.getFile();
		try {
			synchronized (l) {   // only one thread is to update the model				
			    decodeDocument(fFile, pm);					    
				floadInProgress = false;
				fmodelLoaded = true;
			}
			fireProcessingPause(fdisconnected);
			fireStatusChanged(CodegenEditorPartMessages.getString("JVE_STATUS_MSG_INSYNC"));
			fireParseError(false);
		} catch (CodeGenSyntaxError e) {
			fireParseError(true);	// This exception is only for syntax errors, so that would be parse errors.			
		} finally {
			synchronized (l) {
				floadInProgress = false ;
			}			
		}
		
		
		pm.done();
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
    BeanSubclassComposition comp = fVEModel.getModelRoot() ;
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
             ((XMIResource)comp.eResource()).setID(obj,MessageFormat.format(BeanPart.THIS_NAME+CodegenMessages.getString("CodegenMessages.ThisPart.uriID"), new Object[] {fVEModel.getURI()})) ; //$NON-NLS-1$
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
	if (fBeanModel == null || fVEModel == null) throw new CodeGenException ("null Builder") ; //$NON-NLS-1$
	
	fBeanModel.setState(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL,true) ;
	
	try{
		// Before handle expressions, make sure all BeanPart s have instances
		createJavaInstances() ;
	
	    // Decode the relevant expressions	
		Iterator itr = fBeanModel.getBeans().iterator() ;
		ArrayList badExprssions = new ArrayList() ;
		while (itr.hasNext()) {
			
		    BeanPart bean = (BeanPart) itr.next() ;
			Collection expressions = new ArrayList(bean.getRefExpressions()) ;
			expressions.addAll((Collection)bean.getRefEventExpressions()) ;
		    Iterator refs = expressions.iterator() ;
		    // Process the expression referencing the bean, and build the 
		    // Composition.
		    while (refs.hasNext()) {
			  CodeExpressionRef codeRef = (CodeExpressionRef)refs.next() ;
			  // Expression may be decoded already - instigated by another expression decoder 
			  if (codeRef.isStateSet(CodeExpressionRef.STATE_EXIST)) continue ;
		      
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
		  			 		  	
	         BeanPart bean = (BeanPart) itr.next() ;
	       
		   // if a bean was added to a container, the decoder will reflect this in the BeamModel
		   
		   // Model is build (but annotations).   Turn the model on, as the EditParts may slam dunc
		   // new element (e.g., a content pane).  We need to react and generate the appropriate code.
		   fBeanModel.setState(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL, false) ;
		   fBeanModel.setState(IBeanDeclModel.BDM_STATE_UP_AND_RUNNING,true) ;
		   
		   addBeanPart(bean,fVEModel.getModelRoot()) ;
		   
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
				if (bean.isInstanceVar()  || bean.getSimpleName().equals(BeanPart.THIS_NAME)){
					try {
						fBeanModel.setState(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL, true);
						bean.getFFDecoder().decode();
					}
					finally {
						fBeanModel.setState(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL, false);
					}
				}else{
					bean.getFFDecoder();
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
public void decodeDocument (IFile sourceFile,IProgressMonitor pm) throws CodeGenException {
	
	if (sourceFile == null || !sourceFile.exists()) 
	    throw new CodeGenException("Invalid Source File") ;	 //$NON-NLS-1$
	

	
    reConnect(sourceFile) ;    	
		
			
	JavaBeanModelBuilder builder  = new JavaBeanModelBuilder(fEDomain, fSrcSync, fWorkingCopy,
                                              fWorkingCopy.getFile().getLocation().toFile().toString(),null) ;              
    
	builder.setDiagram(fVEModel) ;
	fBeanModel = builder.build() ; 	
	fBeanModel.setSourceSynchronizer(fSrcSync) ;	
		
	try {		
	  buildCompositionModel() ;	 
	}
	catch (Exception e) {
		JavaVEPlugin.log (e, Level.SEVERE) ; //$NON-NLS-1$
	}
			
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

      BeanPartFactory bgen = new BeanPartFactory(fBeanModel,fVEModel) ;
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
		if (!CodeGenUtil.isComponentInComposition(fBeanModel, obj,fVEModel)) {
		   deleteBeanPart(workingCU, changedElements, bean) ;
		   removed=true ;
		}
	}	
	return removed ;
}

	
// What file extention does this decoder works with
public String  getFileExt() {  
	return JAVAExt; 
}




public synchronized boolean pause()  {

	if (fSrcSync==null || fSrcSync.getLockMgr().isGUIReadonly() || floadInProgress)
		return false ;

	if (fdisconnected) return true;     
    disconnect(false) ;
    fireProcessingPause(true);
    return true;
}

/**
 * 
 * No need to dispose every element in the BDM,
 * Let the garbage collector do what it does best.
 * @since 1.0.0
 */
private void deCapitateModel() {
	if (fBeanModel != null) {
		try {			
			fBeanModel.setState(IBeanDeclModel.BDM_STATE_DOWN, true);
			fBeanModel.setSourceSynchronizer(null);
			fBeanModel.setWorkingCopyProvider(null);			
			fBeanModel=null;
		} catch (CodeGenException e) {}
		fBeanModel = null ;
	}	
	fVEModel=null;
}

public synchronized void reConnect(IFile file) {
	// clearModel(true) ;
	deCapitateModel();
	fVEModel = new VEModelInstance(file,fEDomain);
	if (fWorkingCopy == null) {
	    fWorkingCopy = new WorkingCopyProvider(file) ;		    
    }
    else if (fdisconnected)
       fWorkingCopy.connect(file) ;
       
    if (fSrcSync == null) {
	   fSrcSync = new JavaSourceSynchronizer(fWorkingCopy,this) ;
	   fSrcSync.setDelay(fSrcSyncDelay) ;
    }
    else if(fdisconnected)
       fSrcSync.connect() ;
    
    try {
		fVEModel.createEmptyComposition();
	} catch (CodeGenException e) {		
		JavaVEPlugin.log(e);
	}

	fdisconnected=false ;
}

public  void reconnect(org.eclipse.ui.IFileEditorInput input,IProgressMonitor pm) throws CodeGenException {
	IFile file ;
	if (input != null) 
	  file = input.getFile() ;
    else
      file = fFile ;	
	decodeDocument(file, pm) ;
}

/**
 * Same as dispose, but do not destroy the WorkingProvider
 */
public synchronized void disconnect(boolean clearVCEModel) {

    if (fSrcSync != null) {
       commit();    
       fSrcSync.disconnect() ;
    }

   fmodelLoaded=false;
   // clearModel(clearVCEModel) ;
    deCapitateModel();
        
    if (fSrcSync != null) // fWorkingCopy may not be null yet if called from dispose
       fWorkingCopy.disconnect() ;
   
    fSrcSync = null;
    
    fdisconnected=true ;
    fireProcessingPause(fdisconnected);
    fireStatusChanged(fPauseSig);
}

/**
 * Clear the VCE Model, disconnect, and get rid of all resources.
 */
public synchronized void dispose() {
	
	if (fSrcSync != null) {
		commit();
		fSrcSync.uninstall() ;
		fSrcSync = null ;
		// The following is a hack until CodeGen uses the EditDomain.		
		CodeGenUtil.clearCache()  ;
	}
    
    disconnect(true) ;
		
	if (fWorkingCopy != null) 
	   fWorkingCopy.dispose() ;
	
	fWorkingCopy = null ;
}


/* (non-Javadoc)
 * @see org.eclipse.ve.internal.java.codegen.core.IDiagramModelBuilder#startTransaction()
 */
public void startTransaction() {
	fBeanModel.aboutTochangeDoc();
	fSrcSync.getLockMgr().setGUIUpdating(true);
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
	fSrcSync.getLockMgr().setGUIUpdating(false);
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

public synchronized BeanSubclassComposition getModelRoot() {
	if (fVEModel!=null && fmodelLoaded)
		 return fVEModel.getModelRoot();
	else
		return null;
}

public Diagram getDiagram() {
	if (fVEModel!=null)
		 return fVEModel.getDiagram();
	else
		return null;
	
}



/*
 * Each background thread will be running his own snapshot update. Since multiple
 * threads could be running at the same time, a new updater for each thread is 
 * required.
 */
public IBackGroundWorkStrategy createSharedToLocalUpdater(){
	return new SharedToLocalUpdater();
}



	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.core.IDiagramModelBuilder#addIBuilderListener(org.eclipse.ve.internal.java.codegen.core.IDiagramModelBuilder.IBuilderListener)
	 */
	public void addIBuilderListener(IBuilderListener l) {
		if (!fListeners.contains(l))
			fListeners.add(l);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.core.IDiagramModelBuilder#removeIBuilderListener(org.eclipse.ve.internal.java.codegen.core.IDiagramModelBuilder.IBuilderListener)
	 */
	public void removeIBuilderListener(IBuilderListener l) {
		fListeners.remove(l);
	}
	
	public void fireSnippetProcessing(boolean flag) {
	    String msg;
	    if (fparseError)
	    	msg = CodegenEditorPartMessages.getString("JVE_STATUS_MSG_ERROR");
	    else
	      if (flag) {
	    	msg = CodegenEditorPartMessages.getString("JVE_STATUS_MSG_NOT_IN_SYNC") ;  //$NON-NLS-1$.getString("JVE_STATUS_MSG_SYNCHRONIZING") ;  //$NON-NLS-1$
	      }
	      else {
	        msg = CodegenEditorPartMessages.getString("JVE_STATUS_MSG_INSYNC") ;  //$NON-NLS-1$
	    }
	    fireStatusChanged(msg);
	}
	
	protected void fireParseError(boolean error) {
		fparseError=error;	
		if (error)
		    fireStatusChanged(CodegenEditorPartMessages.getString("JVE_STATUS_MSG_ERROR"));
		else
			fireStatusChanged(CodegenEditorPartMessages.getString("JVE_STATUS_MSG_INSYNC"));
		for (int i = 0; i < fListeners.size(); i++) {
       		((IBuilderListener)fListeners.get(i)).parsingStatus(error);
	    }
	}
	
	protected void fireReloadIsNeeded() {
 	    for (int i = 0; i < fListeners.size(); i++) {
       		((IBuilderListener)fListeners.get(i)).reloadIsNeeded();	
	    }
	}
	protected void fireModelChanged() {
		for (int i = 0; i < fListeners.size(); i++) {
			   ((IDiagramModelBuilder.IBuilderListener)fListeners.get(i)).modelUpdated();						
		}
	}
	
	public void fireStatusChanged (String msg) {
		for (int i = 0; i < fListeners.size(); i++) {
			((IBuilderListener) fListeners.get(i)).statusChanged(msg);
		}
	}
	protected void fireProcessingPause(boolean flag) {
		for (int i = 0; i < fListeners.size(); i++) {
			((IBuilderListener) fListeners.get(i)).parsingPaused(flag);		
		}
	}
	

/**
 * @return Returns the fbusy.
 */
public synchronized boolean isBusy() {
	if (fSrcSync!=null)
	    return fSrcSync.getLockMgr().isGUIReadonly();
	else
		return true;
}

public IWorkingCopyProvider getWorkingCopyProvider() {
	return fWorkingCopy;
}

}