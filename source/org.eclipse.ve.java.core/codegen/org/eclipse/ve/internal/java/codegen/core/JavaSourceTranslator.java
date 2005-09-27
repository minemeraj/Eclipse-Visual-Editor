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
package org.eclipse.ve.internal.java.codegen.core;
/*
 *  $RCSfile: JavaSourceTranslator.java,v $
 *  $Revision: 1.90 $  $Date: 2005-09-27 15:12:09 $ 
 */
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IFileEditorInput;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.util.TimerTests;

import org.eclipse.ve.internal.cdm.Annotation;
import org.eclipse.ve.internal.cdm.Diagram;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.jcm.BeanSubclassComposition;
import org.eclipse.ve.internal.jcm.JavaCacheData;

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
boolean					fCanceled = false;
boolean					floadInProgress=false;
boolean					fmodelLoaded=false;
boolean					fparseError=false;
ArrayList				fListeners = new ArrayList();

public static String    fPauseSig = CodegenEditorPartMessages.JVE_STATUS_MSG_PAUSED ;  
public static int       fCommitAndFlushNestGuard = 0 ;


IDiagramSourceDecoder fSourceDecoder = null;


  /**
   *  This is the strategy for updating the shared Java document after
   *  a change in the local document.
   */	
  class  SharedToLocalUpdater implements IBackGroundWorkStrategy {
  	ICompilationUnit fWorkingCopy = null ;
  	  	  	
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
  	protected List changedHandles = new ArrayList();

  	/**
  	 *  ReLoad the BDM model from stratch
  	 */
  	private  void Reload(Display disp,IProgressMonitor monitor) {
  		monitor.beginTask(CodegenMessages.JavaSourceTranslator_0,2); 

  		if (fSrcSync!=null) {
			// We have no idea when the load will be called by the editor.
			// We need to stop the synch. from driving snippets, as we are going
			// to load from scratch anyhow
			fSrcSync.disconnect();
			fdisconnected=true;
			monitor.worked(1);
			try {
				if (fBeanModel!=null) 
				 fBeanModel.setState(IBeanDeclModel.BDM_STATE_DOWN,true);
			} catch (CodeGenException e) {}
			fireReloadIsNeeded();
  		}
		monitor.done();
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
  	
  	protected boolean takeCurrentSnapshot(final IEditorUpdateState editorState, final List allEvents, final ICompilationUnit workingCopy){
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
  			// Disable updates to the IDocument.. hence disabling new bottom up startups
  			synchronized(JavaSourceTranslator.this.fWorkingCopy.getDocumentLock()){
  				if(allEvents.contains(JavaSourceSynchronizer.RELOAD_HANDLE)){
  					// Reload request
  					allEvents.clear();
  					editorState.setCollectingDeltas(false);
  					return true;
  				}else{
  					try {  				  		
 						currentSource = JavaSourceTranslator.this.fWorkingCopy.getWorkingCopy(true).getBuffer().getContents();
  						
  						IType primaryType = workingCopy.findPrimaryType() ;
  						
  						// Record JDT methods
  						takeMethodsSnapshot(primaryType) ;
  						
  						// Record JDT fields
  						takeFieldSnapshot(primaryType) ;
  						
  						// Record JDT imports
  						takeImportSnapshot(workingCopy) ;
  						
  						recordChanges(allEvents, workingCopy);
  					} catch (JavaModelException e) {
  						JavaVEPlugin.log(e);
  					} finally {  	  					
  	  					allEvents.clear();
  	  					editorState.setCollectingDeltas(false);
  					}
  				}  				
  			}
  		}
  		return false;
  	}
  	
  	/**
	 * @param allEvents
	 * @param workingCopy
	 * 
	 * @since 1.0.0
	 */
	protected void recordChanges(List allEvents, ICompilationUnit workingCopy) {
		changedHandles.clear();
		// if a revert was performed, there will be only one doc event 
		// containing all the source - pointless to check for specific changes
		if(allEvents.size()==1){
			try {
				DocumentEvent de = ((DocumentEvent)allEvents.get(0));
				if(		de.getText()!=null && workingCopy!=null && 
						workingCopy.getSourceRange()!=null && 
						de.getText().length()==workingCopy.getSourceRange().getLength())
					return;
			} catch (JavaModelException e) {}
		}
		
		// since the doc events were snapshots, we need to figure
		// out where these changes are in the present document. 
		// hence need to figure out the new location/offsets of the doc events.
		int[][] fromToDiff = new int[allEvents.size()][3];
		for (int ec = 0; ec < allEvents.size(); ec++) {
			DocumentEvent de = (DocumentEvent) allEvents.get(ec);
			int deFrom = de.getOffset();
			int deTo = de.getOffset() + de.getLength();
			int deDiff = (de.getText()==null?0:de.getText().length()) - de.getLength();
			fromToDiff[ec][0] = deFrom;
			fromToDiff[ec][1] = deTo;
			fromToDiff[ec][2] = deDiff;
			for (int pec = 0; pec < ec; pec++) {
				int pdeFrom = fromToDiff[pec][0];
				int pdeTo = fromToDiff[pec][1];
				if(deFrom > pdeTo){
					// de is completely below the previous de - no change 
				}else if(	(deTo <= pdeFrom) || // de is completely above prev de - prev de gets effected
						(deTo>pdeFrom && deTo<pdeTo)) { // de is intersecting with the prev de -
					fromToDiff[pec][0] += fromToDiff[ec][2];
					fromToDiff[pec][1] += fromToDiff[ec][2];
				}
			}
		}
		for (int ec = 0; ec < fromToDiff.length; ec++) {
			int effectiveFrom = fromToDiff[ec][0];
			int effectiveTo = fromToDiff[ec][1] + fromToDiff[ec][2];
			if(effectiveFrom==effectiveTo)
				effectiveTo++;
			for (int posC = effectiveFrom; posC < effectiveTo; posC++) {
				IJavaElement element = null;
				try {
					element = workingCopy.getElementAt(posC);
				} catch (JavaModelException e) {}
				if(element!=null){
					String elementHandle = element.getHandleIdentifier();
					if(elementHandle!=null && !changedHandles.contains(elementHandle)){
						changedHandles.add(elementHandle);
					}
				}
			}
		}
	}

	protected CompilationUnit parse(String source, IProgressMonitor monitor){
   		ASTParser parser = ASTParser.newParser(AST.JLS2);
		parser.setSource(source.toCharArray());
		return (CompilationUnit) parser.createAST(monitor);
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
  	protected boolean handleNonParseable(CompilationUnit cuAST, ICompilationUnit workingCopy, IProgressMonitor cancelMonitor){
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
  	protected boolean merge( IBeanDeclModel mainModel, IBeanDeclModel newModel, IProgressMonitor m ) throws CodeGenException {
  		BDMMerger merger = new BDMMerger(mainModel, newModel, changedHandles, m);
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
  	protected boolean handleParseable(CompilationUnit cuAST, ICompilationUnit workingCopy, IProgressMonitor cancelMonitor) throws CodeGenException {
  		JavaBeanModelBuilder modelBldr =
			new CodeSnippetModelBuilder(fEDomain, getWorkingCopyProvider(), currentSource, methodHandles, importStarts, importEnds, fieldStarts, fieldEnds, methodStarts, methodEnds, workingCopy, cancelMonitor, fBeanModel.getScannerFactory());
 
		modelBldr.setDiagram(fVEModel);
		IBeanDeclModel bdm = null;
		try {
			bdm = modelBldr.build();
		} catch (CodeGenException e) {
			JavaVEPlugin.log(e) ;
		}
		if(!cancelMonitor.isCanceled() && bdm!=null)
			return merge(fBeanModel, bdm, cancelMonitor);
		return false;
		}
  	
  	
	protected void processCancel(IEditorUpdateState editorState) {  		
  		primPause();		
  		editorState.setCollectingDeltas(false);
  	}
  	
	public void run(
			Display disp,
			ICompilationUnit workingCopy,
			IEditorUpdateState editorState,
			List allDocEvents,
			IProgressMonitor monitor) {
			fWorkingCopy = workingCopy;
			fDisplay = disp;
			
			fireSnippetProcessing(true);
			
			if (monitor==null) monitor = new NullProgressMonitor();

			if (monitor.isCanceled()) {
				processCancel(editorState);
				return;
			}

			
			try {
				monitor.beginTask(CodegenMessages.JavaSourceTranslator_1,10);;				 
				
				// We have to call takeCurrentSnapShot to clear events properly
				monitor.subTask(CodegenMessages.JavaSourceTranslator_2); 
				boolean reloadRequired = takeCurrentSnapshot(editorState, allDocEvents, workingCopy) ;
				monitor.worked(1);				
				reloadRequired |= ((fBeanModel == null) && !floadInProgress) ;				
				if (reloadRequired) {
					Reload(fDisplay, new SubProgressMonitor(monitor,2));
				} else {
					if (currentSource == null || monitor.isCanceled()) {
						processCancel(editorState);
						return;
					}
					
					monitor.subTask(CodegenMessages.JavaSourceTranslator_3); 
					CompilationUnit ast = parse(currentSource, new SubProgressMonitor(monitor, 3));
					monitor.worked(1);

					if (monitor.isCanceled()) {
						processCancel(editorState);
						return;
					}

					// only one updater can be touching the model at any time.
					//TODO: Can not lock here the JavaTranslator because isBusy() may be called
					//      recursivly
					synchronized(fSrcSync.getLockObject()){

						// TODO Adapters will not react for GUI deltas !!!
						if (fBeanModel!=null)
							fBeanModel.setState(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL, true);
						
						if (containsParseErrors(ast)) {
							reloadRequired = reloadRequired || !handleNonParseable(ast, fWorkingCopy, monitor);
							if (editorState.getButtomUpProcessingCount()<=1)
							   fireParseError(true);
						} else {
							reloadRequired = reloadRequired || !handleParseable(ast, fWorkingCopy, new SubProgressMonitor(monitor,3));
							if (editorState.getButtomUpProcessingCount()<=1)
							    fireParseError(false);
						}
						monitor.worked(1);
						if (monitor.isCanceled()) {
							processCancel(editorState);
							return;
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
				if (fBeanModel != null)
					fireSnippetProcessing(false);
				monitor.done();
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
 *   Get the current RS.
 * @deprecate
 */
public ResourceSet getModelResourceSet() {
	return EMFEditDomainHelper.getResourceSet(fEDomain);
}





public EditDomain getEditDomain() {
	return fEDomain;
}

public static final String LOADING_PHASE = "LOADING_PHASE";//$NON-NLS-1$


/**
 * @param input File input
 * @param removeVECache true if a cache exists it will be removed.  
 *        If it is false, and a cache exists it will be used.
 */
public  void loadModel(final IFileEditorInput input, boolean removeVECache, final IProgressMonitor pm) throws CodeGenException  {	
    
    // Push the code through the change controller which is the gatekeeper for all model updates   
    ModelChangeController changeController = (ModelChangeController) getEditDomain().getData(ModelChangeController.MODEL_CHANGE_CONTROLLER_KEY);
    changeController.transactionBeginning(LOADING_PHASE);
    try{
        pm.beginTask("", 100); //$NON-NLS-1$
        pm.subTask(CodegenMessages.JavaSourceTranslator_LoadingFromSource);	 
        waitforNotBusy(true);
        floadInProgress = true;
        if (fVEModel != null) {
            if (fBeanModel != null && !fdisconnected) {
                disconnect(false);
                if (fBeanModel != null)
                    fBeanModel.setState(IBeanDeclModel.BDM_STATE_DOWN, true);
                fBeanModel = null;
            }
        }
        fireStatusChanged(CodegenEditorPartMessages.JVE_STATUS_MSG_LOAD); 
        fFile = input.getFile();	
        if (removeVECache)
        	VEModelCacheUtility.removeCache(fFile);
        decodeDocument(fFile, pm);					    					
        pm.done();
    } finally {
        changeController.transactionEnded(LOADING_PHASE);
    }
}
/**
 *  Decode the expression (code) impact on the bean (part)
 */
boolean  decodeExpression(CodeExpressionRef code) throws CodeGenException {
	
    return code.decodeExpression() ;
}

protected EObject  createAJavaInstance(BeanPart bean, JavaCacheData cache, IProgressMonitor pm) throws CodeGenException {
	
	   pm.subTask(CodegenMessages.JavaSourceTranslator_5+bean.getSimpleName()); 
	   EObject obj = null;
	   if (cache == null)
	      obj = bean.createEObject() ;
	   else {
	   	  obj = (EObject) cache.getNamesToBeans().get(bean.getUniqueName());
	   	  bean.setEObject(obj);
	   	  bean.setIsInJVEModel(obj!=null);
	   }
	   pm.worked(1);
       return obj;
}

/**
 *  Create MOF instances 
 * @param pm  Expects a new progress monitor as a beginTask() will be called
 */
void  createJavaInstances (IProgressMonitor pm) throws CodeGenException {
	List beans = fBeanModel.getBeans(false);
	pm.beginTask("", beans.size()+5); // +5 for disposing the error beans //$NON-NLS-1$
	Iterator itr = beans.iterator() ;
	ArrayList err = new ArrayList() ;
    BeanSubclassComposition comp = fVEModel.getModelRoot() ;
    JavaCacheData cache = VEModelCacheUtility.getJavaCacheData(fVEModel);
	while (itr.hasNext()) {
	   BeanPart bean = (BeanPart) itr.next() ;
	   if (bean.isImplicit()) continue;
	   
	   EObject obj = createAJavaInstance(bean,cache,pm);
	   String annotatedName= bean.getSimpleName() ;
	   
	      // The Model Builder will clean up irrelevent beans
       if (!bean.getSimpleName().equals(BeanPart.THIS_NAME)) {
    	   if (!(obj instanceof IJavaObjectInstance)) {    	   	  
    	      obj = null ;
    	      if (JavaVEPlugin.isLoggingLevel(Level.FINE))
    	      	JavaVEPlugin.log("Bad Object: "+bean.getType()+": "+bean.getUniqueName(),Level.FINE) ; //$NON-NLS-1$ //$NON-NLS-2$
    	   }
       }
       else {  // a this part
          if (obj != null) {
          	if (cache==null)
                ((XMIResource)comp.eResource()).setID(obj,BeanPart.THIS_NAME);
             // If no annotation, the PS will not allow you to edit the name in composition
            annotatedName = null ;
          }
       }

       if (obj == null) {
	   	if (JavaVEPlugin.isLoggingLevel(Level.FINE))
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
	   	if (cache == null) {
	     Annotation an = CodeGenUtil.addAnnotation(obj) ;	
         if (annotatedName != null)
           CodeGenUtil.addAnnotatedName(an, annotatedName); 
         comp.getAnnotations().add(an) ;
	   	}
	   }
	}	
	for (int i = 0; i < err.size(); i++) {
		pm.subTask(CodegenMessages.JavaSourceTranslator_7+((BeanPart)err.get(i)).getSimpleName()); 
        ((BeanPart)err.get(i)).deactivate() ;
    }	
	pm.done();
}

protected boolean isDown(IProgressMonitor pm) {
	if (pm!=null)
	   fCanceled = pm.isCanceled();
	return (fdisconnected || fCanceled);
}

/**
 *  Given the BeanDOM, build the Composition Model
 *  @return true if built ok, false if errors
 */
protected boolean	buildCompositionModel(IProgressMonitor pm) throws CodeGenException {
	if (fBeanModel == null || fVEModel == null) throw new CodeGenException ("null Builder") ; //$NON-NLS-1$
	
	fBeanModel.setState(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL,true) ;	
	
	try{
		TimerTests.basicTest.startStep("Create IJavaObject Instances"); //$NON-NLS-1$
		// Before handle expressions, make sure all BeanPart s have instances
		createJavaInstances(new SubProgressMonitor(pm, 10)) ;
		TimerTests.basicTest.stopStep("Create IJavaObject Instances"); //$NON-NLS-1$
	
		// Create a new progress monitor for the decoding of expressions
		SubProgressMonitor expProgressMonitor = new SubProgressMonitor(pm, 45);
		expProgressMonitor.beginTask("",  getTotalExpressionCount()); //$NON-NLS-1$
		
	    // Decode the relevant expressions	
		Iterator itr = fBeanModel.getBeans(true).iterator() ;
		ArrayList badExprssions = new ArrayList() ;
		while (itr.hasNext()) {
			if (isDown(pm)) 
				return false;
		    BeanPart bean = (BeanPart) itr.next() ;
			Collection expressions = new ArrayList(bean.getRefExpressions()) ;
			expressions.addAll(bean.getRefEventExpressions()) ;
		    Iterator refs = expressions.iterator() ;
		    // Process the expression referencing the bean, and build the 
		    // Composition.
		    while (refs.hasNext()) {
			  CodeExpressionRef codeRef = (CodeExpressionRef)refs.next() ;
			  // Expression may be decoded already - instigated by another expression decoder 
			  if (codeRef.isStateSet(CodeExpressionRef.STATE_EXIST)) continue ;
		      
		      //if (getCorrespondingFeature(codeRef,obj) != null)
		      try {
		      	expProgressMonitor.subTask(CodegenMessages.JavaSourceTranslator_11+codeRef.getCodeContent()); 
			  if (!decodeExpression (codeRef)) {
			  	if (JavaVEPlugin.isLoggingLevel(Level.FINE))
			  		JavaVEPlugin.log ("JavaSourceTranslator.buildCompositionModel() : Did not Decoded: "+codeRef, Level.FINE) ;						 //$NON-NLS-1$
				 badExprssions.add(codeRef) ;			 
			  }
			  expProgressMonitor.worked(1);
		      }
		      catch (Exception e) {
		      	if (JavaVEPlugin.isLoggingLevel(Level.WARNING)) {					
		      		JavaVEPlugin.log("Skipping expression: "+codeRef.getCodeContent(),Level.WARNING) ; //$NON-NLS-1$ //$NON-NLS-2$		      		
		      		JavaVEPlugin.log(e, Level.WARNING);
		      	}
		        badExprssions.add(codeRef) ;	
		      }
		    }
		}
		expProgressMonitor.done();
		expProgressMonitor = null;
		if (isDown(pm))
			return false;
		// Clean up
		itr = badExprssions.iterator() ;
		while (itr.hasNext()) {
			CodeExpressionRef codeRef = (CodeExpressionRef) itr.next() ;
			pm.subTask(CodegenMessages.JavaSourceTranslator_12+codeRef.getCodeContent()); 
			codeRef.getMethod().removeExpressionRef(codeRef) ;
			codeRef.getBean().removeRefExpression(codeRef) ;
			codeRef.getBean().addBadExpresion(codeRef);
		}
		pm.worked(2);
	
		BeanPart[] unreferencedBPs = fBeanModel.getUnreferencedBeanParts();
		for (int bpCount = 0; bpCount < unreferencedBPs.length; bpCount++) {
			unreferencedBPs[bpCount].deactivate();
		}
		
	      // Decoders have analyzed and acted on the Expressions - 
	      // it is time to hook them together withn the Compsition
	      // Model
	      itr = fBeanModel.getBeans(true).iterator() ;
		  while (itr.hasNext()) {
		  	 if (isDown(pm))
				return false ;		 		  	
	         BeanPart bean = (BeanPart) itr.next() ;
	         bean.getBadExpressions().clear();
	         
	         // clean parent expressions which were'nt used
	        bean.getParentExpressons().clear();
	         
	         // If deactivated dont add to model 
			 if(!bean.isActive())
				   continue;
		   // if a bean was added to a container, the decoder will reflect this in the BeamModel
		   
		   // Model is build (but annotations).   Turn the model on, as the EditParts may slam dunc
		   // new element (e.g., a content pane).  We need to react and generate the appropriate code.
		   fBeanModel.setState(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL, false) ;
		   fBeanModel.setState(IBeanDeclModel.BDM_STATE_UP_AND_RUNNING,true) ;
		   
		   pm.subTask("Adding bean "+bean.getSimpleName()+CodegenMessages.JavaSourceTranslator_4); 
		   if (!fVEModel.isFromCache())
		     CodeGenUtil.addBeanToBSC(bean,fVEModel.getModelRoot(), false) ;

			try {
				fBeanModel.setState(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL, true);
				if(bean.getFFDecoder()!=null){
					pm.subTask(CodegenMessages.JavaSourceTranslator_15+bean.getSimpleName()); 
					if (fBeanModel.getCompositionModel().isFromCache())
						bean.getFFDecoder().restore();
					else
					    bean.getFFDecoder().decode();
				}
			} finally {
				fBeanModel.setState(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL, false);
			}
		}
		  pm.worked(3);
	}finally{
		fBeanModel.setState(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL, false) ;
		fBeanModel.setState(IBeanDeclModel.BDM_STATE_UP_AND_RUNNING,true) ;
		fVEModel.loadFromCacheComplete();
	}
	return true;
} 
 
/**
 * @return
 * 
 * @since 1.0.2
 */
private int getTotalExpressionCount() {
	int count=0;
	Iterator bitr = fBeanModel.getBeans(false).iterator();
	while (bitr.hasNext()) {
		BeanPart bp = (BeanPart) bitr.next();
		count += bp.getRefEventExpressions().size();
		count += bp.getRefExpressions().size();
	}
	return count;
}

/**
 * 
 * @param pm
 * @return true of reverParse with no errors, false if errors encountered.
 * @throws CodeGenException
 * 
 * @since 1.1.0
 */
protected boolean reverseParse (IProgressMonitor pm) throws CodeGenException {
	
	boolean errors = false;
    try {
    	fSrcSync.getUpdateStatus().setBottomUpProcessing(true);
		pm.beginTask(CodegenMessages.JavaSourceTranslator_16,100); 
		TimerTests.basicTest.startStep("Reverse Parsing"); //$NON-NLS-1$
		TimerTests.basicTest.startStep("Parsing"); //$NON-NLS-1$
		synchronized (fSrcSync.getLockObject()) {
			JavaBeanModelBuilder builder  = new JavaBeanModelBuilder(fEDomain, fSrcSync, fWorkingCopy,
		                                              fWorkingCopy.getFile().getLocation().toFile().toString(),null,new SubProgressMonitor(pm, 40, SubProgressMonitor.PREPEND_MAIN_LABEL_TO_SUBTASK)) ;
			
		    
			builder.setDiagram(fVEModel) ;
			fBeanModel = builder.build() ;
			CodeGenUtil.markSameLineExpressions(fBeanModel);
			
			errors = builder.isErrors();
			if (fBeanModel!=null)
			   fBeanModel.setSourceSynchronizer(fSrcSync) ;	
			TimerTests.basicTest.stopStep("Parsing"); //$NON-NLS-1$
			
			TimerTests.basicTest.startStep("Decoding"); //$NON-NLS-1$
			errors |= !buildCompositionModel(new SubProgressMonitor(pm,60)) ;
		}
		pm.done();
		TimerTests.basicTest.stopStep("Decoding"); //$NON-NLS-1$
    }
    catch (CodeGenSyntaxError e) {
		errors = true;
		fireParseError(true);
		floadInProgress = false ;
		throw e;
    }
    finally {    
    	if (fBeanModel!=null && fBeanModel.getCompositionModel()!=null)
    		fBeanModel.getCompositionModel().loadFromCacheComplete();
		floadInProgress = false ;
		fSrcSync.getUpdateStatus().setBottomUpProcessing(false);	
		fSrcSync.resumeProcessing();
	}
	fmodelLoaded = true;
	fireProcessingPause(fdisconnected);
	fireStatusChanged(CodegenEditorPartMessages.JVE_STATUS_MSG_INSYNC); 
	TimerTests.basicTest.stopStep("Reverse Parsing"); //$NON-NLS-1$
	return !errors;
}

/**
 *  Go for parsing a Java Source
 */ 
public boolean  decodeDocument (final IFile sourceFile,IProgressMonitor pm) throws CodeGenException {
	
	if (sourceFile == null || !sourceFile.exists()) 
	    throw new CodeGenException("Invalid Source File") ;	 //$NON-NLS-1$
	
	boolean errors = false;

	
    reConnect(sourceFile) ;   
    fSrcSync.stallProcessing();
    if (fVEModel.isFromCache()) {
    	// Model is already constructed from cache, we can let
    	// the GUI bulding go, and build the BDM and such in the background.
    	Job job = new ReverseParserJob(sourceFile) {
			protected IStatus doRun(IProgressMonitor monitor) {
				boolean removeCache = false;
				try {						
				  //fireStatusChanged(CodegenEditorPartMessages.getString("JVE_STATUS_MSG_NOT_IN_SYNC")); //$NON-NLS-1$
				  fireSnippetProcessing(true);					
				  removeCache = ! reverseParse(monitor);					
				} catch (Exception e) {
					removeCache = true;
					if (!isDown(monitor)) {
						fireParseError(true);						
						String msg = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();						
						return new Status(Status.ERROR,CDEPlugin.getPlugin().getPluginID(), 0, msg!=null?msg:"" ,e); //$NON-NLS-1$
				    }
				}
				finally {
					if (removeCache) {
 					  if (fBeanModel!=null) 
						   VEModelCacheUtility.removeCache(fBeanModel.getCompositionModel());
					  else
						   VEModelCacheUtility.removeCache(sourceFile);
					}
				}
				return Status.OK_STATUS;
			}
		};    		  
		job.schedule();
		fmodelLoaded=true;
    	return false ;    
    }
    else {
		try {					
			errors = !reverseParse(pm);			
			// Save the file and cache the model into the .xmi file iff the code hasn't been changed.
			if (!errors && !JavaUI.getDocumentProvider().canSaveDocument(fWorkingCopy.getEditor()) &&
				!VEModelCacheUtility.isValidCache(sourceFile)) {
			  if (!JavaUI.getDocumentProvider().canSaveDocument(fWorkingCopy.getEditor())) {
				// Create a cache in the background
				  Job job = new ReverseParserJob(sourceFile) {

					protected IStatus doRun(IProgressMonitor monitor) {
						doSave(monitor);
						return Status.OK_STATUS;
					}
				  };
				  job.schedule();
			  }
			}
		} catch (Exception e) {
			fSrcSync.resumeProcessing();
			// Cache may not have been valid to begin with... remove it if needed
			if (fBeanModel!=null) 
				   VEModelCacheUtility.removeCache(fBeanModel.getCompositionModel());
				else
				   VEModelCacheUtility.removeCache(sourceFile);			
			fireParseError(true);			
		}
    	return true;
    }		
}


/**
 *  Process a member that has no impact on the composition model
 */
protected int processDefaultMemberSave(IMember content, StringBuffer proposedBuff, 
                                       IProgressMonitor pm,int pOffset) 
                 throws JavaModelException {
	pm.subTask(MessageFormat.format("", new Object[]{content.getElementName()})) ; //$NON-NLS-1$
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
	pm.subTask(MessageFormat.format("", new Object[]{cu.getElementName()})) ; //$NON-NLS-1$
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
	pm.subTask(MessageFormat.format("", new Object[]{cu.getElementName()})) ; //$NON-NLS-1$
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
	
	Iterator itr = fBeanModel.getBeans(false).iterator() ;
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


public void primPause() {
	disconnect(false) ;
	fireProcessingPause(true);    
}

public synchronized boolean pause()  {

	if (fSrcSync==null || fSrcSync.getUpdateStatus().isBottomUpProcessing() || floadInProgress)
		return false ;

	if (fdisconnected) return true;     
    primPause();
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

public void reConnect(IFile file) {
	// clearModel(true) ;
	TimerTests.basicTest.startStep("WorkingCopy connection"); //$NON-NLS-1$
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
    	boolean ignoreCache = JavaUI.getDocumentProvider().canSaveDocument(fWorkingCopy.getEditor());
		fVEModel.createComposition(ignoreCache);
	} catch (CodeGenException e) {		
		JavaVEPlugin.log(e);
	}

	fdisconnected=false ;
	TimerTests.basicTest.stopStep("WorkingCopy connection"); //$NON-NLS-1$
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

	try {
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
	}
	finally {
		fdisconnected=true ;
//      This will cause a loop, where a disconnect is called before a loadModel,
//      The editor sometime on an async. will come back, and deCapitate the model
//      potentially after the model is reLoaded		
//		fireProcessingPause(fdisconnected);
	}
    fireStatusChanged(fPauseSig);
    if (fFile!=null)
      ReverseParserJob.cancelJobs(fFile);
}

/**
 * Clear the VCE Model, disconnect, and get rid of all resources.
 */
public synchronized void dispose() {
	
	if (fSrcSync != null) {
		commit();
		fSrcSync.disconnect() ;
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
	fSrcSync.getUpdateStatus().setTopDownProcessing(true);
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
	fSrcSync.getUpdateStatus().setTopDownProcessing(false);
	if (JavaVEPlugin.isLoggingLevel(Level.FINEST))
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

public  BeanSubclassComposition getModelRoot() {
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
	    	msg = CodegenEditorPartMessages.JVE_STATUS_MSG_ERROR; 
	    else
	      if (flag) {
	    	msg = CodegenEditorPartMessages.JVE_STATUS_MSG_NOT_IN_SYNC ;  
	      }
	      else {
	        msg = CodegenEditorPartMessages.JVE_STATUS_MSG_INSYNC ;  
	    }
	    fireStatusChanged(msg);
	}
	
	protected void fireParseError(boolean error) {
		fparseError=error;	
		if (error)
		    fireStatusChanged(CodegenEditorPartMessages.JVE_STATUS_MSG_ERROR); 
		else
			fireStatusChanged(CodegenEditorPartMessages.JVE_STATUS_MSG_INSYNC); 
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
public boolean isBusy() {
	if (fSrcSync!=null)
	    return fSrcSync.getUpdateStatus().isBottomUpProcessing();
	else
		return true;
}

public IWorkingCopyProvider getWorkingCopyProvider() {
	return fWorkingCopy;
}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.core.IDiagramModelBuilder#getThisTypeName()
	 */
	public String getThisTypeName() {
		ICompilationUnit cu = null ;
		if (fBeanModel!=null && !fBeanModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) {
			cu = fBeanModel.getCompilationUnit();
		}
		if (cu==null) {		
		  	cu = (ICompilationUnit) JavaCore.create(fVEModel.getFile());	  
		}
		IType type = CodeGenUtil.getMainType(cu);
		return type.getFullyQualifiedName();
		  
	}
	public void doSave(IProgressMonitor monitor) {
		if (fBeanModel != null && !fBeanModel.isStateSet(IBeanDeclModel.BDM_STATE_DOWN) && 
			!fparseError && !fdisconnected) {
		  VEModelCacheUtility.doSaveCache(fBeanModel, monitor);
		  // We may have stopped processing bottom up so that the model will not change.
		  if (fSrcSync!=null)
		     fSrcSync.resumeProcessing();
		  else  // Editor closed during the save.
			 VEModelCacheUtility.removeCache(fFile);
		}
		else {
			// File is being saved, but our model is not in sync... remove the old cache
			VEModelCacheUtility.removeCache(fFile);
		}
	}

	public void waitforNotBusy(boolean cancelJobs) {
        // See if there is an fFile, if not then there is no jobs waiting.
        if (fFile != null) {
            if (cancelJobs)
                ReverseParserJob.cancelJobs(fFile);
            while (true) {
                try {
                    ReverseParserJob.join(fFile, null);
                    break;
                } catch (OperationCanceledException e) {
                } catch (InterruptedException e) {
                }
            } 
        }	
	}
}
