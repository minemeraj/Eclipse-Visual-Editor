/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: SWTFactoryInstanceDecoderHelper.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-23 19:46:25 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.impl.NaiveExpressionFlattener;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaRefFactory;
import org.eclipse.jem.workbench.utility.ParseTreeCreationFromAST;

import org.eclipse.ve.internal.java.codegen.java.IExpressionDecoder;
import org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
 

public class SWTFactoryInstanceDecoderHelper extends SWTConstructorDecoderHelper {
	
	public static boolean isSWTFactoryInstanceCase(Statement fExpr, CodeExpressionRef expRef, final IBeanDeclModel fBeanModel) {
		if(fExpr!=null){
			Expression initAST = null;
			if (fExpr instanceof ExpressionStatement && ((ExpressionStatement)fExpr).getExpression() instanceof Assignment) {
				Assignment assignment = (Assignment) ((ExpressionStatement)fExpr).getExpression();
				initAST = assignment.getRightHandSide();
			}else if (fExpr instanceof VariableDeclarationStatement) {
				VariableDeclarationStatement vds = (VariableDeclarationStatement) fExpr;
				if(vds.fragments().size()>0 && vds.fragments().get(0) instanceof VariableDeclarationFragment)
					initAST = ((VariableDeclarationFragment)((VariableDeclarationStatement)fExpr).fragments().get(0)).getInitializer();
			}
			if(initAST!=null){
				List references = new ArrayList();
				ParseTreeCreationFromAST parser = new CGParseTreeCreationFromAST(new CGResolver(expRef.getMethod(), expRef.getOffset(), fBeanModel, references));
				parser.createExpression(initAST);
				if(references.size()>1 && references.get(0) instanceof EObject){
					EObject firstRef = (EObject) references.get(0);
					if(!isWidget(firstRef, fBeanModel.getCompositionModel().getModelResourceSet()))
						return true;
				}
			}
		}
		if(expRef.getBean()!=null && expRef.getBean().getEObject()!=null){
			IJavaObjectInstance jo = (IJavaObjectInstance) expRef.getBean().getEObject();
			EObject firstRef = getAllocationFirstReference(jo);
			if(firstRef!=null && !isWidget(firstRef, fBeanModel.getCompositionModel().getModelResourceSet())){
				if(fBeanModel.getABean(firstRef)!=null) // should be modelled by codegen
					return true;
			}
		}
		return false;
	}

	private static EObject getAllocationFirstReference(IJavaObjectInstance jo) {
		JavaAllocation ja = jo.getAllocation();
		if (ja instanceof ParseTreeAllocation) {
			ParseTreeAllocation pta = (ParseTreeAllocation) ja;
			PTExpression expression = pta.getExpression();
			final List returns = new ArrayList();
			NaiveExpressionFlattener bpFinder = new NaiveExpressionFlattener(){
				private boolean firstRef = true;
				public boolean visit(PTInstanceReference node) {
					if(firstRef){ // Just the first reference
						firstRef = false;
						IJavaObjectInstance obj = node.getObject() ;
						returns.add(obj);
					}
					return false;
				}
			};
			expression.accept(bpFinder);
			if(returns.size()>0)
				return (EObject) returns.get(0);
		}
		return null;
	}
	
	private static boolean isWidget(EObject eObject, ResourceSet rs) {
		JavaHelpers widgetType = JavaRefFactory.eINSTANCE.reflectType("org.eclipse.swt.widgets.Widget", rs) ; //$NON-NLS-1$
		if(widgetType!=null && widgetType.isAssignableFrom(eObject.eClass())){
			// first reference is not a widget - use the factory instance approach
			return true;
		}
		return false;
	}

	public SWTFactoryInstanceDecoderHelper(BeanPart bean, Statement exp, IJavaFeatureMapper fm, IExpressionDecoder owner, String constructorSF) {
		super(bean, exp, fm, owner, constructorSF);
	}

	protected BeanPart getParent() {
		if (fParent!=null) 
			return fParent;
		if (getExpressionReferences().size()>1){
			EObject secondRef = (EObject)getExpressionReferences().get(1);
			if(isWidget(secondRef, fbeanPart.getModel().getCompositionModel().getModelResourceSet())){
				fParent = fbeanPart.getModel().getABean(secondRef);
				return fParent;
			}
		}
		return super.getParent();
	}

	/*
	 * Just need to update the ExpressionReferences
	 *  (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#generate(java.lang.Object[])
	 */
	public String generate(Object[] args) throws CodeGenException {
		EObject firstRef = getAllocationFirstReference((IJavaObjectInstance)fbeanPart.getEObject());
		if(!getExpressionReferences().contains(firstRef))
			getExpressionReferences().add(firstRef);
		return super.generate(args);
	}
}
