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
/*
 *  $RCSfile: JavaBeanShadowModelBuilder.java,v $
 *  $Revision: 1.12 $  $Date: 2005-08-24 23:30:44 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.logging.Level;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IFileEditorInput;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.codegen.model.BeanDeclModel;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * @author gmendel
 * 
 * This class is intendent to build a logical BDM that will not harm, or be connected to existing JVE model or source code. The compilation unit that
 * is used, is the shared compilation unit.
 * 
 * It is used mainly to build a temporary BDM for comparisons
 *  
 */
public class JavaBeanShadowModelBuilder extends JavaBeanModelBuilder {

	protected ICompilationUnit referenceCU = null;

	protected String fwcContents = null;

	/**
	 * Constructor for JavaBeanShadowModelBuilder.
	 * 
	 * @param wcp
	 * @param filePath
	 * @param packageName
	 */
	public JavaBeanShadowModelBuilder(EditDomain d, IWorkingCopyProvider wcpArg, String filePath, char[][] packageName, IProgressMonitor monitor) {
		this(d, filePath, packageName, monitor);

		ICompilationUnit wcp = wcpArg.getWorkingCopy(false);
		fWCP = createPseudoWorkingCopyProvider(wcp, wcpArg.getResolver());
		try {
			fwcContents = wcp.getSource();
		} catch (JavaModelException e) {
			if (JavaVEPlugin.isLoggingLevel(Level.FINEST))
				JavaVEPlugin.log("JavaBeanShadowModelBuilder - *error* getting source code"); //$NON-NLS-1$
		}
		// Make sure fCU use is limited to fWCP
		fCU = null;

	}

	protected IWorkingCopyProvider createPseudoWorkingCopyProvider(ICompilationUnit cu, final TypeResolver resolver) {
		referenceCU = cu;

		//	Create a pseudo working provider that is limited to resolve, and provide a CU
		IWorkingCopyProvider wcp = new IWorkingCopyProvider() {

			public TypeResolver getResolver() {
				return resolver;
			}

			public ITypeHierarchy getHierarchy() {
				return resolver.getMainTypeHierarchy();
			}

			public String toString() {
				return "Shadow - " + super.toString(); //$NON-NLS-1$
			}

			public ICompilationUnit getWorkingCopy(boolean forceReconcile) {
				// TODO Warning - should not be required. This provider is only for resolving
				return referenceCU;
			}

			public IFileEditorInput getEditor() {
				return null;
			}

			public IFile getFile() {
				return null;
			}

			public IDocument getDocument() {
				return null;
			}

			public void disconnect() {
			}

			public void connect(IFile file) {
			}

			public ISourceRange getSourceRange(String handle) {
				return null;
			}

			public int getLineNo(int Offset) {
				return -1;
			}

			public void dispose() {
			}

			public IJavaElement getElement(String handle) {
				return null;
			}
			
			public Object getDocumentLock() {
				return null;
			}

		};

		return wcp;
	}

	public JavaBeanShadowModelBuilder(EditDomain d, String fileName, char[][] packageName, IProgressMonitor monitor) {
		super(d, fileName, packageName, monitor);
	}

	protected IBeanDeclModel createDefaultModel(EditDomain d) {
		BeanDeclModel model = new BeanDeclModel();
		model.setDomain(d);
		try {
			model.setState(IBeanDeclModel.BDM_STATE_SNIPPET, true);
		} catch (CodeGenException e) {
		}
		return model;
	}

	/**
	 * Read the source file
	 */
	protected char[] getFileContents() throws CodeGenException {
		if (fwcContents != null)
			return fwcContents.toCharArray();
		else
			return new char[0];
	}

}

