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
 *  $Revision: 1.3 $  $Date: 2005-09-16 16:25:00 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.ArrayList;
import java.util.List;
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
		public IMethod beanRetMethod=null;
		public RenameRequest(String currentBeanName, int afterOffset, String newBeanName, IMethod beanRetMethod){
			this.afterOffset = afterOffset;
			this.currentBeanName = currentBeanName;
			this.newBeanName = newBeanName;
			this.beanRetMethod = beanRetMethod;
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
							renameRequest.beanRetMethod, 
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
	
	private void processRenameRequest(String currentBeanName, int afterOffset, String newBeanName, IMethod beanRetMethod, ASTRewrite rewrite) {
		BeanPartNodesFinder visitor = new BeanPartNodesFinder(currentBeanName, afterOffset, beanRetMethod);
		cuNode.accept(visitor);
		if(visitor.getVariableName()!=null){
			// Rename variable
			SimpleName simpleName = visitor.getVariableName();
			rename(cuNode, simpleName, newBeanName, rewrite);
			
			// Rename getter if available
			SimpleName getterMethodName = visitor.getGetterMethodName();
			if(getterMethodName!=null){
				String vName = new String(new char[]{Character.toUpperCase(newBeanName.charAt(0))}) + newBeanName.substring(1);
				rename(cuNode, getterMethodName, "get"+vName, rewrite);
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
 
	public void addRequest(String currentBeanName, int afterOffset, String newBeanName, IMethod beanRetMethod) {
		if(renameRequests==null)
			renameRequests = new ArrayList();
		renameRequests.add(new RenameRequest(currentBeanName, afterOffset, newBeanName, beanRetMethod));
	}
}
