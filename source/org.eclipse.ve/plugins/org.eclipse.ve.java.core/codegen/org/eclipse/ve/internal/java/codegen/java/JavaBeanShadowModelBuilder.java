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
 *  $RCSfile: JavaBeanShadowModelBuilder.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.ui.IFileEditorInput;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.java.codegen.model.BeanDeclModel;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.codegen.util.*;

/**
 * @author gmendel
 * 
 * This class is intendent to build a logical BDM that will not harm, or be connected
 * to existing JVE model or source code. 
 * The compilation unit that is used, is the shared compilation unit.
 * 
 * It is used mainly to build a temporary BDM for comparisons
 * 
 */
public class JavaBeanShadowModelBuilder extends JavaBeanModelBuilder { 
 
		
	protected ICompilationUnit referenceCU =null;
	protected CodegenTypeResolver resolver = null ;
	protected String			   fwcContents = null ;
	
	
	/**
	 * Constructor for JavaBeanShadowModelBuilder.
	 * @param wcp
	 * @param filePath
	 * @param packageName
	 */
	public JavaBeanShadowModelBuilder(EditDomain d, IWorkingCopyProvider wcpArg, String filePath, char[][] packageName) {
		this(d, filePath, packageName);

		fWCP = createPseudoWorkingCopyProvider(wcpArg.getSharedWorkingCopy());
		try {
			fwcContents = wcpArg.getSharedWorkingCopy().getSource() ;
		}
		catch (JavaModelException e) {
			org.eclipse.ve.internal.java.core.JavaVEPlugin.log("JavaBeanShadowModelBuilder - *error* getting source code") ; //$NON-NLS-1$
		}
		// Make sure fCU use is limited to fWCP
		fCU = null ;

	}
	
	protected IWorkingCopyProvider createPseudoWorkingCopyProvider(ICompilationUnit cu) {
		referenceCU = cu ;
		resolver = new CodegenTypeResolver(CodeGenUtil.getMainType(referenceCU));

		//	Create a pseudo working provider that is limited to resolve, and provide a CU
		IWorkingCopyProvider wcp = new IWorkingCopyProvider() {

			public ICompilationUnit getSharedWorkingCopy() {
				return null;
			}
			public ICompilationUnit getSharedWorkingCopy(boolean force) {
				return null;
			}
			/**
			 * Let the visitors the ability to get element handels etc.
			 */
			public ICompilationUnit getLocalWorkingCopy() {
				return  referenceCU;
			}
			public IFileEditorInput getEditor() {
				return null;
			}
			public IFile getFile() {
				return null;
			}
			public IDocument getSharedDocument() {
				return null;
			}
			public IDocument getLocalDocument() {
				return null;
			}
			public Object getLocalDocLock() {
				return null;
			}
			public void ReplaceWithLocalContent(IProgressMonitor pm, boolean commit) throws CodeGenException {
				;
			}
			public void UpdateDeltaToShared(ICancelMonitor pm, IDocumentListener docListener, List handles, boolean commit) throws CodeGenException {
				;
			}
			public void aboutToChangeShared() {
				;
			}
			public void changeCompleteShared() {
				;
			}
			public ISourceRange getSharedSourceRange(String handle) {
				return null;
			}
			public int getSharedLineNo(int Offset) {
				return -1;
			}
			public void dispose() {
				;
			}
			public String resolve(String unresolved) {
				try {
					if (resolver == null)
						return unresolved;
					else
						return resolver.resolveTypeComplex(unresolved);
				}
				catch (Exception e) {
					return unresolved;
				}
			}
			public String resolveThis() {
				return CodeGenUtil.getMainType(referenceCU).getTypeQualifiedName();
			}
			public ITypeHierarchy getHierarchy() {
				try {
					IType t = CodeGenUtil.getMainType(referenceCU);
					return t.newSupertypeHierarchy(null);
				}
				catch (org.eclipse.jdt.core.JavaModelException e) {}
				return null;
			}
			public void disconnect() {}
			public void reconnect(IFile file) {}
			public String toString() {
				return "Shadow - "+super.toString() ; //$NON-NLS-1$
			}
				
		};
		
		return wcp ;
	}

	public JavaBeanShadowModelBuilder(EditDomain d, String fileName, char[][] packageName) {
		super(d,fileName, packageName);
	}

	protected IBeanDeclModel createDefaultModel(EditDomain d) {
		BeanDeclModel model = new BeanDeclModel();
		model.setDomain(d) ;
		try {
			model.setState(IBeanDeclModel.BDM_STATE_SNIPPET, true);
		}
		catch (CodeGenException e) {}
		return model;
	}
	
	/**
	*  Read the source file
	*/
	protected char[] getFileContents() throws CodeGenException {
		if (fwcContents != null)
			return fwcContents.toCharArray();
		else
			return new char[0];
	}


	
}


