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
 *  $Revision: 1.8 $  $Date: 2004-03-05 22:07:37 $ 
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
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cdm.*;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.IModelChangeController;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.jcm.*;

import org.eclipse.ve.internal.java.codegen.editorpart.IJVEStatus;
import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.java.rules.InstanceVariableCreationRule;
import org.eclipse.ve.internal.java.codegen.java.rules.InstanceVariableRule;
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
  	private synchronized void Reload(Display disp,ICancelMonitor monitor) {
  		IModelChangeController controller = (IModelChangeController) getEditDomain().getData(IModelChangeController.MODEL_CHANGE_CONTROLLER_KEY);
		if(controller!=null && controller.inTransaction()){
			// If there are any commands which are being processed, append a reload handle into the queue
			// and exit the Reload process. The commands need to get executed, else there will be cases where
			// commands will get lost becuase some previous command resulted in the Reload mechanism kicking in.
			fSrcSync.appendReloadRequest();
			return ;
		}
  				
  	    try {
  			Object lock = fBeanModel== null ? new Object() : fBeanModel.getDocumentLock() ;  			  
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
  		return AST.parseCompilationUnit(source.toCharArray());
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
		modelBldr.setDiagram(fCompositionModel);
		IBeanDeclModel bdm = null;
		try {
			bdm = modelBldr.build();
		} catch (Exception e) {
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

			IModelChangeController controller = (IModelChangeController) fEDomain.getData(IModelChangeController.MODEL_CHANGE_CONTROLLER_KEY);
			try {
				synchronized (controller) {
					// It is possible that multi work elements stagger, and we do not want
					// a later work element, to cache, an UnRestored state.
					if (fSetHold++ == 0) {
						fHoldMsg = controller.getHoldMsg();
						fHold = controller.isHoldChanges();
					}
					controller.setHoldChanges(true, null);
				}

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
					} else {
						handleParseable(ast, fWorkingCopy, monitor);
					}

					if (monitor != null && monitor.isCanceled())
						return;

					if (fEventsProcessedCount > 0) {
						for (int i = 0; i < fEventsProcessedCount; i++) {
							lockManager.setGUIReadonly(false);
						}
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
				synchronized (controller) {
					if (--fSetHold <= 0) {
						controller.setHoldChanges(fHold, fHoldMsg);
						fSetHold = 0;
					}
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
		JavaVEPlugin.log(e,Level.WARNING) ;
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

/*
 * Each background thread will be running his own snapshot update. Since multiple
 * threads could be running at the same time, a new updater for each thread is 
 * required.
 */
public IBackGroundWorkStrategy createSharedToLocalUpdater(){
	return new SharedToLocalUpdater();
}

private Object loadLock = new Object();
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.codegen.IDiagramModelBuilder#getDocumentLock()
	 */
	public synchronized Object getLoadLock() {
		return loadLock;
	}

}