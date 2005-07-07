/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: WorkbenchPartRootContainerObjectActionDelegate.java,v $
 *  $Revision: 1.4 $  $Date: 2005-07-07 21:33:29 $ 
 */
package org.eclipse.ve.internal.jface.codegen;

import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionDelegate;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.java.rules.IInstanceVariableCreationRule;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

import org.eclipse.ve.internal.jface.*;
import org.eclipse.ve.internal.swt.SwtPlugin;
 

/**
 * Create the "top" control for a WorkbenchPart action.
 * @since 1.1
 */
public class WorkbenchPartRootContainerObjectActionDelegate extends ActionDelegate implements IObjectActionDelegate {

	protected IStructuredSelection selection;
	protected MethodDeclaration methodDeclaration = null;
	protected CodeMethodRef methodRef = null;
	protected BeanPart argumentBP = null;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO This is done outside the command stack AND so undo will not work correctly.
		methodRef = null;
		methodDeclaration = null;
		argumentBP = null;
		if (!(selection instanceof IStructuredSelection))
			action.setEnabled(false);
		else {
			// Disable if any already have a contentpane.
			// We can assume we are on implementers of RootPaneContainer because that is handled in the plugin.xml.
			this.selection = (IStructuredSelection) selection;
			Iterator itr = this.selection.iterator();
			boolean enabled = false;
			while (itr.hasNext()) {
				EditPart ep = (EditPart) itr.next();
				IJavaObjectInstance model = (IJavaObjectInstance) ep.getModel();
				EditPart argumentEditPart = null;
				EditPart workbenchEditPart = null;
				if (ep instanceof WorkbenchPartGraphicalEditPart || ep instanceof WorkbenchPartTreeEditPart) {
					workbenchEditPart =  ep;
					EStructuralFeature sfDelegateControl = ((JavaClass) model.getJavaType()).getEStructuralFeature(SwtPlugin.DELEGATE_CONTROL); 
					if (sfDelegateControl != null || model.eIsSet(sfDelegateControl)) {
						IJavaObjectInstance argumentEObject = (IJavaObjectInstance) model.eGet(sfDelegateControl);
						Iterator workbenchChildrenItr = workbenchEditPart.getChildren().iterator();
						while (workbenchChildrenItr!=null && workbenchChildrenItr.hasNext()) {
							EditPart ep1 = (EditPart) workbenchChildrenItr.next();
							if(argumentEObject.equals(ep1.getModel())){
								argumentEditPart = ep1;
								break;
							}
						}
					}
				}else{
					// we have to be the argument editpart due to the checks made by the 
					// plugin.xml's visibility filters
					argumentEditPart = ep;
					workbenchEditPart = ep.getParent();
				}
				
				if(argumentEditPart!=null && workbenchEditPart!=null){
					try{
						EObject argumentEObject = (EObject) argumentEditPart.getModel();
						EStructuralFeature controlsSF = argumentEObject.eClass().getEStructuralFeature("controls");
						if(controlsSF!=null){ // there should be a controls SF
							if(!argumentEObject.eIsSet(controlsSF)){
								// no controls set - now check if method is COMPLETELY empty
								BeanDecoderAdapter bda = (BeanDecoderAdapter) EcoreUtil.getExistingAdapter(
										argumentEObject, ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);
								if(bda!=null){
									argumentBP = bda.getBeanPart();
									methodRef = argumentBP!=null ? argumentBP.getInitMethod() : null;
									if(methodRef!=null){
										ICompilationUnit cu = methodRef.getTypeRef().getBeanModel().getCompilationUnit();
										IType type = CodeGenUtil.getMainType(cu);
										IMethod iMethod = CodeGenUtil.getMethod(type, methodRef.getMethodHandle());
										ASTParser parser = ASTParser.newParser(AST.JLS2);
										parser.setSource(iMethod.getSource().toCharArray());
										parser.setKind(ASTParser.K_CLASS_BODY_DECLARATIONS);
										TypeDeclaration td = (TypeDeclaration) parser.createAST(null);
										methodDeclaration = td.getMethods()[0];
										if(methodDeclaration!=null && methodDeclaration.getBody()!=null){
											// there is a method declaration and a method body
											if(methodDeclaration.getBody().statements()==null || methodDeclaration.getBody().statements().size()<1)
												enabled = true;
										}
									}
								}
							}
						}
					} catch (JavaModelException e) {
						JavaVEPlugin.log(e, Level.FINER);
					}finally{
						
					}
				}
			}
			action.setEnabled(enabled); 
		}
		super.selectionChanged(action, selection);
	}

	public void run(IAction action) {
		if(methodRef!=null){
			createChildCompositeIfNecessary(methodRef);
		}
	}
	/*
	 * 
	 * 
	 * @since 1.1
	 */
	private void createChildCompositeIfNecessary(final CodeMethodRef codeMethodRef) {
		/* VE doesnt handle method arguments. As a fix for 98417, a completely empty 
		 * View/Editor Part createPartControl() with NO content in it will have a field
		 * and a method inserted into it, so that snippet update can pick it up.*/
		final IBeanDeclModel fModel = codeMethodRef.getTypeRef().getBeanModel(); 
		fModel.getDomain().getEditorPart().getEditorSite().getShell().getDisplay().asyncExec(
			new Runnable() {
				public void run() {
					try {
						fModel.suspendSynchronizer(); // suspend snippet update
						
						// Same as CodeMethodRef's getUsableOffsetAndFiller() where no statements are found
						ICodeGenSourceRange sr = ExpressionRefFactory.getOffsetForFirstExpression(CodeGenUtil.getMethod(CodeGenUtil.getMainType(fModel.getCompilationUnit()), methodRef.getMethodHandle()));
						StringBuffer sb = new StringBuffer() ;
						for (int i = 0; i < sr.getLength(); i++) {
							sb.append(' ') ;
						}
						String filler = sb.toString() ;   
						int offset = sr.getOffset() + codeMethodRef.getOffset();
						
						// Composite initialization statement
						String childName = "top";
						EditDomain domain = methodRef.getTypeRef().getBeanModel().getDomain();
						IInstanceVariableCreationRule cr = (IInstanceVariableCreationRule) domain.getRuleRegistry().getRule(IInstanceVariableCreationRule.RULE_ID);
						if(cr!=null){
							childName = cr.getValidInstanceVariableName(argumentBP.getEObject(), childName, CodeGenUtil.getMainType(fModel.getCompilationUnit()), fModel);
						}
						
						String argumentName = "parent";
						if(methodDeclaration!=null){
							List params = methodDeclaration.parameters();
							SingleVariableDeclaration svd = (SingleVariableDeclaration) params.get(0);
							argumentName = svd.getName().getFullyQualifiedName();
						}
						fModel.getCompilationUnit().getBuffer().replace(offset,0, filler+childName+" = new Composite("+argumentName+", SWT.NONE);"+fModel.getLineSeperator());
						
						// Field for the composite
						CodeGenUtil.getMainType(fModel.getCompilationUnit()).createField("private Composite "+childName+" = null;", null, true, null);
						
						// Import for the SWT
						CodeExpressionRef.handleImportStatements(fModel.getCompilationUnit(), fModel, Collections.singletonList("org.eclipse.swt.SWT"));
					} catch (JavaModelException e) {
						JavaVEPlugin.log(e, Level.WARNING);
					}finally{
						fModel.resumeSynchronizer(); // resume snippet update
					}
				}
			}
		);
	}

}
