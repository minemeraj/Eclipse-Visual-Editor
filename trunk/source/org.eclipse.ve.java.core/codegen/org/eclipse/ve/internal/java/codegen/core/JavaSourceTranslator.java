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
 *  $Revision: 1.16 $  $Date: 2004-04-01 00:51:21 $ 
 */
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.DocumentEvent;
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
import org.eclipse.ve.internal.java.codegen.editorpart.IJVEStatus;
import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.java.rules.InstanceVariableCreationRule;
import org.eclipse.ve.internal.java.codegen.java.rules.InstanceVariableRule;
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
IJVEStatus              fMsgRrenderer = null ;
EditDomain 			    fEDomain = null ;
boolean                 fdisconnected = true ;
boolean					floadInProgress=false;
boolean					fmodelLoaded=false;
boolean					fparseError=false;
ArrayList				fListeners = new ArrayList();


public static String    fPauseSig = CodegenEditorPartMessages.getString("JVE_STATUS_MSG_PAUSED") ;  //$NON-NLS-1$
public static int       fCommitAndFlushNestGuard = 0 ;


IDiagramSourceDecoder fSourceDecoder = null;
IJVEStatus fMsgRenderer = null;




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
  	boolean reloadRequired = false ;
  	
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
		    
  	    for (int i = 0; i < fListeners.size(); i++) {
       		((IBuilderListener)fListeners.get(i)).reloadIsNeeded();
	
	    }
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
			for (Iterator iter = pureSourceFields.iterator(); iter.hasNext();) {
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
  	
  	protected void takeCurrentSnapshot(final ICodegenLockManager lockManager, final List allEvents, final ICompilationUnit workingCopy){
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
  			synchronized(((DocumentEvent)allEvents.get(0)).getDocument()){
  				if(allEvents.contains(JavaSourceSynchronizer.RELOAD_HANDLE)){
  					// Reload request
  					fEventsProcessedCount = allEvents.size()-1;
  					allEvents.clear();
  				}else{
  					fEventsProcessedCount = allEvents.size();
  					allEvents.clear();
  					lockManager.setThreadScheduled(false);
  					try {
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
  	
  	protected synchronized void handleNonParseable(CompilationUnit cuAST, ICompilationUnit workingCopy, ICancelMonitor cancelMonitor){
  		fBeanModel.refreshMethods();
  	}
  	
  	protected void merge( IBeanDeclModel mainModel, IBeanDeclModel newModel, ICancelMonitor m ){
  		BDMMerger merger = new BDMMerger(mainModel, newModel, true, fDisplay);
  		try{
  			if(!merger.merge())
  				Reload(fDisplay, m);
  		}catch( Throwable t) {
  			JavaVEPlugin.log("Merge failed : "+t.getMessage(), Level.WARNING);
  			Reload(fDisplay, m);
  		}
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
  	
  	protected synchronized void handleParseable(CompilationUnit cuAST, ICompilationUnit workingCopy, ICancelMonitor cancelMonitor){
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
			merge(fBeanModel, bdm, cancelMonitor);
 	}
  	
	public void run(
			Display disp,
			ICompilationUnit workingCopy,
			ICodegenLockManager lockManager,
			List allDocEvents,
			ICancelMonitor monitor) {

			fWorkingCopy = workingCopy;
			fDisplay = disp;

			if (monitor != null && monitor.isCanceled())
				return;

			try {
				if (monitor != null && monitor.isCanceled())
					return;

				fEventsProcessedCount = 0;
				reloadRequired = false;
				takeCurrentSnapshot(lockManager, allDocEvents, workingCopy);
				if (reloadRequired) {
					Reload(fDisplay, monitor);
				} else {
					if (currentSource == null)
						return;

					if (monitor != null && monitor.isCanceled())
						return;

					CompilationUnit ast = parse(currentSource);

					if (monitor != null && monitor.isCanceled())
						return;

					// TODO Adapters will not react for GUI deltas !!!
					fBeanModel.setState(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL, true);

					if (containsParseErrors(ast)) {
						handleNonParseable(ast, fWorkingCopy, monitor);
						fireParseError(true);
					} else {
						handleParseable(ast, fWorkingCopy, monitor);
						fireParseError(false);

					}

					if (monitor != null && monitor.isCanceled())
						return;

					if (fEventsProcessedCount > 0) {
						for (int i = 0; i < fEventsProcessedCount; i++) {
							lockManager.setGUIReadonly(false);
						}
					}
					
					//TODO: we need to only notify if the VE model was updated
					for (int i = 0; i < fListeners.size(); i++) {
					   ((IDiagramModelBuilder.IBuilderListener)fListeners.get(i)).modelUpdated();						
					}

					if (monitor != null && monitor.isCanceled())
						return;

					fBeanModel.setState(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL, false);

					if (fBeanModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) {
						Reload(disp, monitor);
					}
				}
			} catch (Throwable e) {
				JavaVEPlugin.log(e, Level.FINE);
				// Reload from scratch will re-set the state of the BDM
				Reload(disp, monitor);
			} finally {
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
		synchronized (this) {
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
		for (int i = 0; i < fListeners.size(); i++) {
			((IBuilderListener) fListeners.get(i)).statusChanged(CodegenEditorPartMessages.getString("JVE_STATUS_MSG_LOAD"));
		}
		fFile = input.getFile();
		try {
			decodeDocument(fFile, pm);
			synchronized (this) {
				floadInProgress = false;
				fmodelLoaded = true;
			}
			for (int i = 0; i < fListeners.size(); i++) {
				((IBuilderListener) fListeners.get(i)).parsingPaused(fdisconnected);
				((IBuilderListener) fListeners.get(i)).statusChanged(CodegenEditorPartMessages.getString("JVE_STATUS_MSG_INSYNC"));
			}
		} catch (CodeGenSyntaxError e) {
			fireParseError(true);	// This exception is only for syntax errors, so that would be parse errors.
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
public void decodeDocument (IFile sourceFile,IProgressMonitor pm) throws CodeGenException {
	
	if (sourceFile == null || !sourceFile.exists()) 
	    throw new CodeGenException("Invalid Source File") ;	 //$NON-NLS-1$
	
    fMsgRrenderer.setStatus(IJVEStatus.JVE_CODEGEN_STATUS_SYNCHING,true) ;
	fMsgRrenderer.setStatus(IJVEStatus.JVE_CODEGEN_STATUS_OUTOFSYNC,true) ;
	fMsgRrenderer.setStatus(IJVEStatus.JVE_CODEGEN_STATUS_UPDATING_JVE_MODEL,true) ;
	
	
    reConnect(sourceFile) ;    	
		
	// TODO Need to dispose the working copy at some point
	
	if (isReloadPending()) return;	
			
	JavaBeanModelBuilder builder  = new JavaBeanModelBuilder(fEDomain, fWorkingCopy,
                                              fWorkingCopy.getFile().getLocation().toFile().toString(),null) ;              
    
	builder.setDiagram(fVEModel) ;
	fBeanModel = builder.build() ; 	
	fBeanModel.setSourceSynchronizer(fSrcSync) ;
	fBeanModel.setFStatus(fMsgRrenderer) ;	
	
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

protected void setHoldGUIChanges(boolean flag, String msg) {
	IModelChangeController controller = (IModelChangeController) fEDomain.getData(IModelChangeController.MODEL_CHANGE_CONTROLLER_KEY);
	controller.setHoldChanges(flag, msg) ;
	fMsgRrenderer.setStatus(ICodeGenStatus.JVE_CODEGEN_STATUS_PARSE_ERRROR,flag) ;
}

/**
 * @deprecated
 */
public void reloadFromScratch(Display disp, ICancelMonitor monitor) throws CodeGenException {
	
	
	if (fFile==null)
	  fFile = fWorkingCopy.getFile() ; 
	  
	if (fFile == null) throw new CodeGenException ("No Resource") ; //$NON-NLS-1$
	
	try {
		// Do not clear the VCE model - may not be on the UI thread.
		if (monitor != null && monitor.isCanceled())
			return;

		
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
							decodeDocument(fFile, null);
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
					}
				}
			});
		
	}
	finally {
		fMsgRrenderer.setStatus(ICodeGenStatus.JVE_CODEGEN_STATUS_RELOAD_IN_PROGRESS, false);
	}
}

public synchronized boolean pause()  {

	if (fSrcSync==null || fSrcSync.getLockMgr().isGUIReadonly() || floadInProgress)
		return false ;

	if (fdisconnected) return true;     
    disconnect(false) ;
    for (int i = 0; i < fListeners.size(); i++) {
       	((IBuilderListener)fListeners.get(i)).parsingPaused(true); 	
    }
    return true;
}

/**
 * @deprecated
 */
public void pauseRoundTripping(boolean flag){
	if (flag) {
		pause();
		fMsgRrenderer.setStatus(ICodeGenStatus.JVE_CODEGEN_STATUS_PAUSE,true) ;
	}
	else{
		IFileEditorInput in = new org.eclipse.ui.part.FileEditorInput(fFile);
		try {
			loadModel(in, null);
			fMsgRrenderer.setStatus(ICodeGenStatus.JVE_CODEGEN_STATUS_PAUSE,false) ;
		} catch (CodeGenException e) {			
			e.printStackTrace();
		}
    }
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

/**
 * This will clear the BDM, and optionaly the VCE model
 * @deprecate call deCapitateModel()
 */
private   void clearModel(boolean clearJVEModel) {
	 
	try {
	if (fVEModel != null && fVEModel.getModelResource()!=null) {
        EObject root = fVEModel.getModelRoot() ;
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
   
   
   if (clearJVEModel == true && fVEModel != null && fVEModel.getModelRoot()!=null) {
      org.eclipse.ve.internal.jcm.BeanSubclassComposition comp = fVEModel.getModelRoot() ;
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
		JavaVEPlugin.log(e,Level.WARNING) ;
	}
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
	   fSrcSync.setStatus(fMsgRrenderer) ;
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
    for (int i = 0; i < fListeners.size(); i++) {
       ((IBuilderListener)fListeners.get(i)).parsingPaused(fdisconnected);
	   ((IBuilderListener)fListeners.get(i)).statusChanged(fPauseSig);	
	}

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


/* (non-Javadoc)
 * @see org.eclipse.ve.internal.java.codegen.core.IDiagramModelBuilder#startTransaction()
 */
public void startTransaction() {
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




public void setMsgRenderer (IJVEStatus mr) {
    fMsgRrenderer = mr ;
    if (fSrcSync != null)
       fSrcSync.setStatus(mr) ;
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
	    if (flag) {
	    	msg = CodegenEditorPartMessages.getString("JVE_STATUS_MSG_NOT_IN_SYNC") ;  //$NON-NLS-1$.getString("JVE_STATUS_MSG_SYNCHRONIZING") ;  //$NON-NLS-1$
	    }
	    else {
	       msg = CodegenEditorPartMessages.getString("JVE_STATUS_MSG_INSYNC") ;  //$NON-NLS-1$
	    }
	    for (int i = 0; i < fListeners.size(); i++) {
	   		((IBuilderListener)fListeners.get(i)).statusChanged(msg);	
		}
	}
	
	protected void fireParseError(boolean error) {
		if (error==fparseError) return ;
		fparseError=error;
		for (int i = 0; i < fListeners.size(); i++) {
       		((IBuilderListener)fListeners.get(i)).parsingStatus(error);
       		((IBuilderListener) fListeners.get(i)).statusChanged(CodegenEditorPartMessages.getString("JVE_STATUS_MSG_ERROR"));
	    }
	}
	

/**
 * @return Returns the fbusy.
 */
public synchronized boolean isBusy() {
	return fSrcSync.getLockMgr().isGUIReadonly();
}
}