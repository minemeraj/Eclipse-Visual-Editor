/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: RenameRequestCollector.java,v $
 *  $Revision: 1.5 $  $Date: 2005-11-22 16:36:52 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.*;
import java.util.logging.Level;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;

import org.eclipse.ve.internal.java.codegen.java.AnnotationDecoderAdapter.BeanPartNodesFinder;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

 

public class RenameRequestCollector implements Runnable {
	
	private static class RenameRequest{
		public String currentBeanName = null;
		public int afterOffset=-1;
		public String newBeanName=null;
		public IMethod beanMethod=null;
		public RenameRequest(String currentBeanName, int afterOffset, String newBeanName, IMethod beanMethod){
			this.afterOffset = afterOffset;
			this.currentBeanName = currentBeanName;
			this.newBeanName = newBeanName;
			this.beanMethod = beanMethod;
		}
	}
	
	private List renameRequests = null;
	private IBeanDeclModel bdm = null;
	private ICompilationUnit compilationUnit = null;
	private CompilationUnit cuNode = null;
	
	public RenameRequestCollector(IBeanDeclModel bdm, ICompilationUnit compilationUnit){
		this.bdm = bdm;
		this.compilationUnit = compilationUnit;
	}
	
	public void run() {
		if(renameRequests!=null && renameRequests.size()>0){
			bdm.suspendSynchronizer();
			try{
				// Create AST to change
				ASTParser parser = ASTParser.newParser(AST.JLS2);
				parser.setSource(compilationUnit);
				parser.setResolveBindings(true);
				cuNode = (CompilationUnit) parser.createAST(null);
				ASTRewrite rewrite = ASTRewrite.create(cuNode.getAST());
				
				// process rename requests
				for (int requestSize = 0; requestSize < renameRequests.size(); requestSize++) {
					RenameRequest renameRequest = (RenameRequest)renameRequests.get(requestSize);
					processRenameRequest(
							renameRequest.currentBeanName, 
							renameRequest.afterOffset, 
							renameRequest.newBeanName, 
							renameRequest.beanMethod, 
							rewrite);
				}
				
				// commit changes to document
				IDocument document = new Document(compilationUnit.getBuffer().getContents());
				TextEdit edits = rewrite.rewriteAST(document, null);
				edits.apply(document);
				compilationUnit.getBuffer().setContents(document.get());
			} catch (Throwable e) {
				JavaVEPlugin.log(e, Level.WARNING);
			}finally{
				renameRequests.clear();
				AnnotationDecoderAdapter.setRenameRequestCollector(compilationUnit, null);
				bdm.resumeSynchronizer();
			}
		}
	}
	
	private void processRenameRequest(String currentBeanName, int afterOffset, final String newBeanName, IMethod beanMethod, final ASTRewrite rewrite) {
		BeanPartNodesFinder visitor = new BeanPartNodesFinder(currentBeanName, afterOffset, beanMethod);
		cuNode.accept(visitor);
		if(visitor.getVariableName()!=null){
			// Rename variable
			SimpleName simpleName = visitor.getVariableName();
			final String oldName = simpleName.getFullyQualifiedName();
			rename(cuNode, simpleName, newBeanName, rewrite);
			
			// Rename getter if available
			SimpleName methodName = visitor.getVariableMethodName();
			if(methodName!=null){
				String vName = new String(new char[]{Character.toUpperCase(newBeanName.charAt(0))}) + newBeanName.substring(1);
				String prefix = methodName.getFullyQualifiedName().startsWith("create") ? "create" : "get"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				rename(cuNode, methodName, prefix+vName, rewrite);
				
				if(methodName.getParent() instanceof MethodDeclaration){
					MethodDeclaration md = (MethodDeclaration) methodName.getParent();
					Javadoc jd = md.getJavadoc();
					renameInJavadoc(jd, oldName, newBeanName, rewrite);
				}
			}
		}
	}

	/**
	 * Renames occurences of 'oldName' in the javadoc with 'newName' using the ASTRewrite
	 * 
	 * @param javadoc
	 * @param oldName
	 * @param newName
	 * @param rewrite
	 * 
	 * @since 1.2.0
	 */
	protected void renameInJavadoc(Javadoc javadoc, final String oldName, final String newName, final ASTRewrite rewrite) {
		if(javadoc!=null){
			List tags = javadoc.tags();
			for (Iterator tagItr = tags.iterator(); tagItr.hasNext();) {
				TagElement te = (TagElement) tagItr.next();
				te.accept(new ASTVisitor(){
					public boolean visit(TextElement node) {
						boolean changesMade = false;
						StringBuffer comment = new StringBuffer(node.getText());
						int nameIndex = comment.indexOf(oldName);
						while(nameIndex>-1 && nameIndex<comment.length()){
							// prefix ?
							boolean isCorrectPrefix = false;
							if(nameIndex==0){
								isCorrectPrefix = true; // beginning of line
							} else {
								char prefixChar = comment.charAt(nameIndex-1);
								if(! (Character.isJavaIdentifierPart(prefixChar) || Character.isJavaIdentifierStart(prefixChar)) )
									isCorrectPrefix = true;
							}

							// suffix ?
							boolean isCorrectSuffix = false;
							if((nameIndex+oldName.length())==comment.length()){
								isCorrectSuffix = true;
							} else{
								char suffixChar = comment.charAt(nameIndex+oldName.length());
								if(!Character.isJavaIdentifierPart(suffixChar))
									isCorrectSuffix = true;
							}
							
							if(isCorrectPrefix && isCorrectSuffix){
								changesMade = true;
								comment.replace(nameIndex, nameIndex+oldName.length(), newName);
								nameIndex = comment.indexOf(oldName, nameIndex + newName.length());
							}else{
								nameIndex = comment.indexOf(oldName, nameIndex + oldName.length());
							}
						}
						if(changesMade){
							rewrite.set(node, TextElement.TEXT_PROPERTY, comment.toString(), null);
						}
						return true;
					}
				});
			}
		}
	}

	private void rename(CompilationUnit cuNode, SimpleName simpleName, final String newName, final ASTRewrite rewrite) {
		final IBinding simpleNameBinding = simpleName.resolveBinding();
		if(simpleNameBinding!=null){
			cuNode.accept(new ASTVisitor(){
				public boolean visit(SimpleName node) {
					if(simpleNameBinding.isEqualTo(node.resolveBinding())){
						rewrite.set(node, SimpleName.IDENTIFIER_PROPERTY, newName, null);
					}
					return super.visit(node);
				}
			});
		}
	}
 
	public void addRequest(String currentBeanName, int afterOffset, String newBeanName, IMethod beanMethod) {
		if(renameRequests==null)
			renameRequests = new ArrayList();
		renameRequests.add(new RenameRequest(currentBeanName, afterOffset, newBeanName, beanMethod));
	}
}
