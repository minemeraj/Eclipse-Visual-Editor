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
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.lang.reflect.Method;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.IJavaStatusConstants;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitDocumentProvider;
import org.eclipse.jdt.internal.ui.javaeditor.DocumentAdapter;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.ILineTracker;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;

/**
 * @author gmendel
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ShadowCUDocumentProvider extends CompilationUnitDocumentProvider {

	static ShadowCUDocumentProvider fInstance = new ShadowCUDocumentProvider() ;
	
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

	/**
	 * Returns the working copy this document provider maintains for the given
	 * element.
	 * 
	 * @param element the given element
	 * @return the working copy for the given element
	 */
	public ICompilationUnit getWCU(IEditorInput element) {
		ICompilationUnit cu = null ;
		// This method is package protected... use reflection until we get this opened up.
		try {
			// TODO If we can get to the IWorkingCopyManager, there is an API getWorkingCopy(IEditorInput) that is available and not internal.
			CompilationUnitDocumentProvider dp = this ;
			Method getWK = CompilationUnitDocumentProvider.class.getDeclaredMethod("getWorkingCopy", new Class[] { IEditorInput.class } );  //$NON-NLS-1$
			getWK.setAccessible(true) ;
			cu = (ICompilationUnit) getWK.invoke(dp, new Object[] { element });
		} catch (Exception e) {
			org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e) ;
		}
		return cu ;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitDocumentProvider#isHandlingTemporaryProblems()
	 */
	protected boolean isHandlingTemporaryProblems() {		
		return false ;
	}
	
	/*
	 * @see AbstractDocumentProvider#createElementInfo(Object)
	 */
	protected ElementInfo createElementInfo(Object element) throws CoreException {
		
		if ( !(element instanceof IFileEditorInput))
			return super.createElementInfo(element);
			
		IFileEditorInput input= (IFileEditorInput) element;
		ICompilationUnit original= createCompilationUnit(input.getFile());
		if (original != null) {
				
			try {
												
				IAnnotationModel m = createCompilationUnitAnnotationModel(input);
				ICompilationUnit c = (ICompilationUnit) original.getSharedWorkingCopy(getProgressMonitor(), getBufferFactory(), null);
				
				DocumentAdapter a= null;
				try {
					a= (DocumentAdapter) c.getBuffer();
				} catch (ClassCastException x) {
					IStatus status= new Status(IStatus.ERROR, JavaUI.ID_PLUGIN, IJavaStatusConstants.TEMPLATE_IO_EXCEPTION, "Shared working copy has wrong buffer", x); //$NON-NLS-1$
					throw new CoreException(status);
				}
				
				
				CompilationUnitInfo info= new CompilationUnitInfo(a.getDocument(), m, null, c);
				info.setModificationStamp(computeModificationStamp(input.getFile()));
				info.fStatus= a.getStatus();
				info.fEncoding= getPersistedEncoding(input);
				
				return info;
				
			} catch (JavaModelException x) {
				throw new CoreException(x.getStatus());
			}
		} else {		
			return super.createElementInfo(element);
		}
	}

}
