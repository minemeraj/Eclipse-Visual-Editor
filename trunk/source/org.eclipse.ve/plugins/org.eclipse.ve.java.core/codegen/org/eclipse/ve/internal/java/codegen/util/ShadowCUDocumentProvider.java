/*
 * Created on Apr 1, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.eclipse.ve.internal.java.codegen.util;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ShadowCUDocumentProvider.java,v $
 *  $Revision: 1.3 $  $Date: 2004-01-13 21:11:52 $ 
 */

import java.lang.reflect.Field;

import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.internal.filebuffers.TextFileBufferManager;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.core.DefaultWorkingCopyOwner;
import org.eclipse.jdt.internal.ui.javaeditor.filebuffers.*;
import org.eclipse.jface.text.ILineTracker;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.IFileEditorInput;


/**
 * @author gmendel
 *
 * TODO: This class is temporary, untill we get rid of the Local shadow WC VE uses
 */
public class ShadowCUDocumentProvider extends CompilationUnitDocumentProvider2 {
	
	static DefaultWorkingCopyOwner fWCowner = new DefaultWorkingCopyOwner(){
	    public IBuffer createBuffer(ICompilationUnit workingCopy) {
		   if (this.factory == null) return super.createBuffer(workingCopy);
		   return this.factory.createBuffer(workingCopy);
	   }
	};
	
	// Use a buffer factory to pint to DocumentAdapter3
	static CustomBufferFactory2 fBuffFactory = new CustomBufferFactory2() {
		public IBuffer createBuffer(IOpenable owner) {
			if (owner instanceof ICompilationUnit) {
				ICompilationUnit unit = (ICompilationUnit) owner;
				ICompilationUnit original = (ICompilationUnit) unit.getOriginalElement();
				IResource resource = original.getResource();
				if (resource instanceof IFile) {
					return new DocumentAdapter3(unit, (IFile) resource);
				}

			}
			return DocumentAdapter2.NULL;
		}
	};	
	
	// overide DocumentAdapter2 to point to our shadow IDocument (using our buff. mgr)
	public static class DocumentAdapter3 extends DocumentAdapter2 {
		public DocumentAdapter3(IOpenable owner, IFile file) {
			super(owner, file);
			try {
				Field f = DocumentAdapter2.class.getDeclaredField("fOwner");
				f.setAccessible(true);
				f.set(this, owner);

				f = DocumentAdapter2.class.getDeclaredField("fFile");
				f.setAccessible(true);
				f.set(this, file);

				//					fOwner= owner;
				//					fFile= file;		
				initialize();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		private void initialize() {
			try {
				ITextFileBufferManager manager = fBuffMgr;
				Field f = DocumentAdapter2.class.getDeclaredField("fFile");
				f.setAccessible(true);
				IPath location = ((IFile) f.get(this)).getFullPath();
				//			IPath location= fFile.getFullPath();
				try {
					getDocument().removePrenotifiedDocumentListener(this);

					manager.connect(location, new NullProgressMonitor());
					f = DocumentAdapter2.class.getDeclaredField("fTextFileBuffer");
					f.setAccessible(true);
					f.set(this, manager.getTextFileBuffer(location));
					//				fTextFileBuffer= manager.getTextFileBuffer(location);
					f = DocumentAdapter2.class.getDeclaredField("fDocument");
					f.setAccessible(true);
					f.set(this, manager.getTextFileBuffer(location).getDocument());
					//				fDocument= fTextFileBuffer.getDocument();
				} catch (CoreException x) {
					//				fStatus= x.getStatus();
					//				fDocument= manager.createEmptyDocument(location);
				}
				getDocument().addPrenotifiedDocumentListener(this);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

    

	 
	static TextFileBufferManager	fBuffMgr = new TextFileBufferManager() ; // private buffer mgr. will not colide with JDT
		
	static ShadowCUDocumentProvider fInstance = new ShadowCUDocumentProvider() ;
	
	public ShadowCUDocumentProvider() {
		fWCowner.factory= fBuffFactory ;
	}
	static IAnnotationModel   fAModel = new IAnnotationModel() {
		public void addAnnotationModelListener(org.eclipse.jface.text.source.IAnnotationModelListener listener) {
		}
		public void removeAnnotationModelListener(org.eclipse.jface.text.source.IAnnotationModelListener listener) {
		}
		public void connect(org.eclipse.jface.text.IDocument document) {
		}
		public void disconnect(org.eclipse.jface.text.IDocument document) {
		}
		public void addAnnotation(org.eclipse.jface.text.source.Annotation annotation, org.eclipse.jface.text.Position position) {
		}
		public void removeAnnotation(org.eclipse.jface.text.source.Annotation annotation) {
		}
		public java.util.Iterator getAnnotationIterator() {
			return new java.util.Iterator() {
				public boolean hasNext() {	
					return false;
				}

				public java.lang.Object next() {
					return null;
				}

				public void remove() {
				}
			} ;
		} 
		public org.eclipse.jface.text.Position getPosition(org.eclipse.jface.text.source.Annotation annotation) {
			return null;
		}
	} ;
	

	public static ShadowCUDocumentProvider getProvider() {
		return fInstance ;
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitDocumentProvider#createCompilationUnitAnnotationModel(java.lang.Object)
	 */
	protected IAnnotationModel createCompilationUnitAnnotationModel(Object element) throws CoreException {		
		return fAModel ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitDocumentProvider#createLineTracker(java.lang.Object)
	 */
	public ILineTracker createLineTracker(Object element) {
		return null;
	}



	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitDocumentProvider#isHandlingTemporaryProblems()
	 */
	protected boolean isHandlingTemporaryProblems() {		
		return false ;
	}
	
	/* 
	 * This method will foce the creation of an independant WorkingCopy and will not use the automatic
	 * creation of Workingcopy in M3
	 */
	protected FileInfo createFileInfo(Object element) throws CoreException {
		if (!(element instanceof IFileEditorInput))
			return null;
			
		IFileEditorInput input= (IFileEditorInput) element;
		ICompilationUnit original= createCompilationUnit(input.getFile());
		if (original == null)
			return null;
		
        // from super.super to create the IDocument
        IPath location=((IFileEditorInput)element).getFile().getFullPath() ;  
        
		
		CompilationUnitInfo cuInfo = null ;
		if (location != null) {			
				fBuffMgr.connect(location, getProgressMonitor());
				fBuffMgr.requestSynchronizationContext(location);
				ITextFileBuffer fileBuffer= fBuffMgr.getTextFileBuffer(location);
			
				cuInfo= (CompilationUnitInfo)createEmptyFileInfo();
			    cuInfo.fTextFileBuffer= fileBuffer;
			    cuInfo.fCachedReadOnlyState= isSystemFileReadOnly(cuInfo);			
		}
		if (cuInfo==null) return null ;
								
		IProblemRequestor requestor= cuInfo.fModel instanceof IProblemRequestor ? (IProblemRequestor) cuInfo.fModel : null;

//		if (JavaPlugin.USE_WORKING_COPY_OWNERS)  {
//			original.becomeWorkingCopy(requestor, getProgressMonitor());
//			cuInfo.fCopy= original;
//		} else  {
//			cuInfo.fCopy= (ICompilationUnit) original.getSharedWorkingCopy(getProgressMonitor(), JavaPlugin.getDefault().getBufferFactory(), requestor);
//		}

        // Create an off the side WC
		cuInfo.fCopy= (ICompilationUnit) original.getWorkingCopy(fWCowner,requestor,null) ;
		
		return cuInfo;			
	}
}