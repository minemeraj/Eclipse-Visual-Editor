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
 *  $RCSfile: WorkingCopyProvider.java,v $
 *  $Revision: 1.14 $  $Date: 2005-09-14 21:22:55 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;

import java.util.logging.Level;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.*;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * This class provides a primitive factory to hook with to the working copy and associated IDocument that is used by the JavaEditor Note: It is the
 * responsibility of the client of this class to call dispose() at some point.
 */
public class WorkingCopyProvider implements IWorkingCopyProvider {

	ICompilationUnit fCU = null; // "the" working copy shared with other editors

	IFileEditorInput fEditorIn = null; // Editor input associated with the file

	IFile fFile = null; // the file itself

	boolean fdisconnected;

	TypeResolver internalResolver = null;

	public WorkingCopyProvider(IFile file) {
		fFile = file;
	}
	
	
	/**
	 * @return boolean noting if a update was made
	 */
	protected boolean primReconcileCU(ICompilationUnit cu) {
		try {
			if (!cu.isConsistent()) {
				cu.reconcile(ICompilationUnit.NO_AST, false, null, new NullProgressMonitor());
				return true;
			}
		} catch (JavaModelException e) {
			JavaVEPlugin.log(e, Level.WARNING);
		}
		return false;
	}

	public ICompilationUnit primGetWorkingCopy() {

		synchronized (this) {
			if (fCU == null) {
				fCU = JavaUI.getWorkingCopyManager().getWorkingCopy(getEditor());
			}
		}
		primReconcileCU(fCU);
		return fCU;
	}

	public ICompilationUnit getWorkingCopy(boolean forceReconcile) {
		if (forceReconcile || fCU == null)
			return primGetWorkingCopy();
		else
			return fCU;
	}

	/**
	 * @return IFileEditorInput accociated with the file resource
	 */
	public synchronized IFileEditorInput getEditor() {
		if (fEditorIn != null)
			return fEditorIn;
		fEditorIn = new FileEditorInput(fFile);
		// Raise the ref count
		try {
			getDocumentProvider().connect(fEditorIn);
			getDocumentProvider().getAnnotationModel(fEditorIn).connect(getDocument());
		} catch (Exception e) {
			JavaVEPlugin.log(e, Level.WARNING);
		}

		return fEditorIn;
	}

	/**
	 * @return IFile resource associated with this provider
	 */
	public IFile getFile() {
		return fFile;
	}

	public TypeResolver getResolver() {
		if (internalResolver == null || !internalResolver.getMainType().equals(CodeGenUtil.getMainType(getWorkingCopy(false)))) {
			ICompilationUnit wc = getWorkingCopy(false);
			if (wc != null) {
				if(internalResolver!=null){
					internalResolver.dispose();
					internalResolver = null;
				}
				try {
					internalResolver = new TypeResolver(wc.getImports(), CodeGenUtil.getMainType(wc));
				} catch (JavaModelException e) {
					JavaVEPlugin.log(e, Level.WARNING);
					// Create one with no imports so we can continue.
					internalResolver = new TypeResolver(new IImportDeclaration[0], CodeGenUtil.getMainType(wc));
				}
			}
		}
		return internalResolver;
	}

	protected IDocumentProvider getDocumentProvider() {
		return JavaUI.getDocumentProvider();

	}

	/**
	 * @return IDocument associated with "the" Java Model working copy
	 */
	public synchronized IDocument getDocument() {
		return getDocumentProvider().getDocument(getEditor());

	}

	protected void primDisconnect() {
		getResolver().dispose();
		internalResolver = null;
		getDocumentProvider().getAnnotationModel(fEditorIn).disconnect(getDocument());
		getDocumentProvider().disconnect(fEditorIn);
	}

	/**
	 * Dispose this provider
	 */
	public synchronized void dispose() {
		disconnect();
		fFile = null;		
	}

	/**
	 * Disconnect from the providers.
	 */
	public synchronized void disconnect() {
		if (fdisconnected)
			return;

		primDisconnect();
		fCU = null;
		fEditorIn = null;
		fdisconnected = true;
	}

	/**
	 * ReConnect to the Shared/Local providers.
	 * 
	 * @param new
	 *            (or same) input file.
	 */
	public synchronized void connect(IFile file) {

		if (file == null)
			throw new IllegalArgumentException();
		if (!fdisconnected) {
			if (file.equals(fFile))
				return;
		} else
			disconnect();
		
		fFile = file;

		getEditor();
		fdisconnected = false;
	}

	public static IJavaElement getElement(String handle, IJavaElement root) {

		if (root != null) {
			if (root.getHandleIdentifier().equals(handle))
				return root;
			else if (root instanceof IParent)
				try {
					IJavaElement[] kids = ((IParent) root).getChildren();
					for (int i = 0; i < kids.length; i++) {
						IJavaElement r = getElement(handle, kids[i]);
						if (r != null)
							return r;
					}
				} catch (JavaModelException e) {
				}
		}
		return null;
	}

	public IJavaElement getElement(String handle) {
		return getElement(handle, getWorkingCopy(true));
	}

	public void selectDocumentRegion(int offset, int len) {

		org.eclipse.ui.IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
		if (windows != null && windows.length > 0) {
			for (int i = 0; i < windows.length; i++) {
				org.eclipse.ui.IEditorPart ep = windows[i].getActivePage().getActiveEditor();
				if (ep.getEditorInput().getName().equals(getEditor().getName()) && ep instanceof ITextEditor) {
					((ITextEditor) ep).selectAndReveal(offset, len);
					break;
				}
			}
		}
	}

	public ISourceRange getSourceRange(String handle) {
		IJavaElement shared = getElement(handle, getWorkingCopy(true));
		if (shared != null)
			try {
				if (shared instanceof ISourceReference)
					return ((ISourceReference) shared).getSourceRange();
			} catch (JavaModelException e) {
			}
		return null;
	}

	public int getLineNo(int Offset) {
		try {
			return getDocument().getLineOfOffset(Offset);
		} catch (BadLocationException e) {
		}
		return -1;
	}

	public ITypeHierarchy getHierarchy() {
		return internalResolver.getMainTypeHierarchy();
	}

	public String toString() {
		return "WCP [" + fFile + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}


	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.IWorkingCopyProvider#getDocumentLock()
	 */
	public Object getDocumentLock() {
		return ((ISynchronizable)getDocument()).getLockObject();
	}

}
