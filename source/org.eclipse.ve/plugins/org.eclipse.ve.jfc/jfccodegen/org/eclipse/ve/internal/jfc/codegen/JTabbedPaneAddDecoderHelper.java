package org.eclipse.ve.internal.jfc.codegen;
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
 *  $RCSfile: JTabbedPaneAddDecoderHelper.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-12 21:44:36 $ 
 */
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.*;
import org.eclipse.jdt.internal.compiler.ast.*;

import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance;
import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.BeanDeclModel;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.util.*;

import org.eclipse.jem.internal.java.JavaClass;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * @version 	1.0
 * @author
 */
public class JTabbedPaneAddDecoderHelper extends AbstractContainerAddDecoderHelper {

	public static final URI ROOT_NAME = URI.createURI("platform:/plugin/org.eclipse.ve.jfc/overrides/javax/swing/jtabbedPaneVisuals.ecore#JTabComponent"); //$NON-NLS-1$
	public static final String COMPONENT_ATTR_NAME = "component"; //$NON-NLS-1$
	public static final String TAB_TITLE_ATTR_NAME = "tabTitle"; //$NON-NLS-1$
	public static final String TAB_TOOLTIP_ATTR_NAME = "tabTooltipText"; //$NON-NLS-1$
	public static final String TAB_ICON_ATTR_NAME = "tabIcon"; //$NON-NLS-1$

	public static final String TAB_EMPTY_STRING = ""; //$NON-NLS-1$

	IJavaObjectInstance fTitleInstance = null, fToolTipInstance = null, fIconInstance = null;

	/**
	 * Constructor for JTabbedPaneAddDecoderHelper.
	 * @param bean
	 * @param exp
	 * @param fm
	 * @param owner
	 */
	public JTabbedPaneAddDecoderHelper(BeanPart bean, Statement exp, IJavaFeatureMapper fm, IExpressionDecoder owner) {
		super(bean, exp, fm, owner);
	}

	protected IJavaObjectInstance getRootElement(EObject root, String sfName) {
		if (root == null)
			return null;
		EStructuralFeature sf = root.eClass().getEStructuralFeature(sfName);
		if (sf != null)
			return (IJavaObjectInstance) root.eGet(sf);
		else
			return null;
	}

	protected IJavaObjectInstance getComponent(EObject root) {
		return getRootElement(root, COMPONENT_ATTR_NAME);
	}

	protected IJavaObjectInstance getTabTitle(EObject root) {
		return getRootElement(root, TAB_TITLE_ATTR_NAME);
	}
	protected IJavaObjectInstance getToolTip(EObject root) {
		return getRootElement(root, TAB_TOOLTIP_ATTR_NAME);
	}
	protected IJavaObjectInstance getIcon(EObject root) {
		return getRootElement(root, TAB_ICON_ATTR_NAME);
	}

	protected EClass getRootClass() {
		if (fRootObj != null)
			return fRootObj.eClass();
		IDiagramModelInstance cm = fbeanPart.getModel().getCompositionModel();
		EClass rootClass = (EClass) cm.getModelResourceSet().getEObject(ROOT_NAME, true);
		return rootClass;
	}

	/**
	 * Decoder Specific Add 
	 * @return EObject the ConstraintComponent object
	 */
	protected EObject add(EObject toAdd, BeanPart target, int index) {
		int i = index;
		if (i < 0) {
			i = findIndex(target);
			// fAddedIndex may inforce an insert mathod, vs. and add method
			//  fAddedIndex = Integer.toString(i) ;
		}

		EClass rootClass = getRootClass();
		EObject root = (EObject) rootClass.getEPackage().getEFactoryInstance().create(rootClass);

		//   root.eSet(root.eClass().getEStructuralFeature(COMPONENT_ATTR_NAME),
		//                    toAdd.getRefObject()) ;
		CodeGenUtil.eSet(root, root.eClass().getEStructuralFeature(COMPONENT_ATTR_NAME), toAdd, -1);

		if (fTitleInstance != null)
			root.eSet(root.eClass().getEStructuralFeature(TAB_TITLE_ATTR_NAME), fTitleInstance);

		if (fToolTipInstance != null)
			root.eSet(root.eClass().getEStructuralFeature(TAB_TOOLTIP_ATTR_NAME), fToolTipInstance);

		if (fIconInstance != null)
			root.eSet(root.eClass().getEStructuralFeature(TAB_ICON_ATTR_NAME), fIconInstance);

		// target's add feature
		EStructuralFeature cf = fFmapper.getFeature(null);
		java.util.List compList = (java.util.List) target.getEObject().eGet(cf);
		JavaVEPlugin.log("JTabbedPaneAddDecoderHelper.add(" + toAdd + "," + target + "@" + i + ")", MsgLogger.LOG_FINE); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		if (i < 0)
			compList.add(root);
		else
			compList.add(Math.min(compList.size(), i), root);

		fRootObj = root;
		//   cf = CodeGenUtil.getParentContainerFeature(root) ;
		//   if (cf != null)
		//      root.eSet(cf,target.getRefObject()) ;
		return root;

	}
	/**
	 * Decoder Specific Add 
	 * @return EObject the ConstraintComponent object
	 */
	protected EObject add(BeanPart toAdd, BeanPart target, int index) {
		toAdd.addBackRef(target, (EReference) fFmapper.getFeature(null));
		target.addChild(toAdd);
		return add(toAdd.getEObject(), target, index);
	}

	protected BeanPart resolveAddedComponent(Expression arg) throws CodeGenException {
		BeanPart bp = null;

		// Parse the arguments to figure out which bean to add to this container
		if (arg instanceof MessageSend) {
			// Look to see of if this method returns a Bean
			String selector = new String(((MessageSend) arg).selector);
			bp = fOwner.getBeanModel().getBeanReturned(selector);
		} else if (arg instanceof SingleNameReference) {
			// Simple reference to a bean
			String selector = new String(((SingleNameReference) arg).token);
			bp = fOwner.getBeanModel().getABean(selector);
			if (bp == null) {
				bp = fOwner.getBeanModel().getABean(BeanDeclModel.constructUniqueName(fOwner.getExprRef().getMethod(), selector));
				//bp = fOwner.getBeanModel().getABean(fOwner.getExprRef().getMethod().getMethodHandle()+"^"+selector);
			}
		} else if (arg instanceof AllocationExpression) {
			String clazzName = fbeanPart.getModel().resolve(((AllocationExpression) arg).type.toString());
			IJavaObjectInstance obj =
				(IJavaObjectInstance) CodeGenUtil.createInstance(clazzName, fbeanPart.getModel().getCompositionModel());
			JavaClass c = (JavaClass) obj.getJavaType();
			if (c.isExistingType())
				fAddedInstance = obj;
		}

		return bp;
	}

	protected BeanPart parseAddedPart(MessageSend exp) throws CodeGenException {
		// TODO  Need to deal with multiple arguments, and nesting

		if (exp == null)
			return null;

		BeanPart bp = null;

		Expression[] args = exp.arguments;

		if (args.length >= 4)
			bp = resolveAddedComponent(args[2]);
		else if (args.length == 2)
			bp = resolveAddedComponent(args[0]);
		else if (args.length == 1)
			bp = resolveAddedComponent(args[0]);
		else
			throw new CodeGenException("Bad Arguments !!! " + exp); //$NON-NLS-1$

		return bp;
	}

	protected void processTitle(Expression arg) throws CodeGenException {
		if (arg instanceof NullLiteral)
			fTitleInstance = null;
		else if (arg instanceof StringLiteral) {
			fTitleInstance = (IJavaObjectInstance) CodeGenUtil.createInstance("java.lang.String", fbeanPart.getModel().getCompositionModel()); //$NON-NLS-1$
			setInitString(fTitleInstance, new String(((StringLiteral) arg).source()));
			fbeanPart.getInitMethod().getCompMethod().getProperties().add(fTitleInstance);
		}
	}

	protected void processIcon(Expression arg) throws CodeGenException {
		// TODO  Need to support local declarations
		if (arg instanceof NullLiteral)
			fIconInstance = null;
		else if (arg instanceof AllocationExpression) {
			fIconInstance = (IJavaObjectInstance) CodeGenUtil.createInstance("javax.swing.ImageIcon", fbeanPart.getModel().getCompositionModel()); //$NON-NLS-1$
			fIconInstance.setAllocation(InstantiationFactory.eINSTANCE.createInitStringAllocation(arg.toString()));
			fbeanPart.getInitMethod().getCompMethod().getProperties().add(fIconInstance);
		}
	}

	protected void processToolTip(Expression arg) throws CodeGenException {

		if (arg instanceof NullLiteral)
			fToolTipInstance = null;
		else if (arg instanceof StringLiteral) {
			fToolTipInstance = (IJavaObjectInstance) CodeGenUtil.createInstance("java.lang.String", fbeanPart.getModel().getCompositionModel()); //$NON-NLS-1$
			setInitString(fToolTipInstance, new String(((StringLiteral) arg).source()));
			fbeanPart.getInitMethod().getCompMethod().getProperties().add(fToolTipInstance);
		}

	}

	protected boolean parseAndAddArguments(Expression[] args) throws CodeGenException {
		//  fAddedConstraintInstance = null ;
		if (fAddedPart != null || fAddedInstance != null) {
			// process the Title

			if (args.length >= 4) {
				processTitle(args[0]);
				processIcon(args[1]);
				processToolTip(args[3]);
			} else if (args.length == 2) {
				if (args[1] instanceof StringLiteral)
					processTitle(args[1]);
			}

			int index = -1;
			if (args.length == 5 && args[4] instanceof IntLiteral) {
				fAddedIndex = ((IntLiteral) args[4]).toStringExpression();
				index = Integer.parseInt(fAddedIndex);
			}

			if (fAddedPart != null)
				add(fAddedPart, fbeanPart, index);
			else
				add(fAddedInstance, fbeanPart, index);
			return true;

		}
		CodeGenUtil.logParsingError(fExpr.toString(), fbeanPart.getInitMethod().getMethodName(), "Could not resolve added component", false); //$NON-NLS-1$	
		return false;
	}

	public String primRefreshFromComposition(String expSig) throws CodeGenException {

		// Isolate the constraint argument using the last used constraint
		String[] args = getSourceCodeArgs();

		int start = expSig.indexOf(args[0]);
		// TODO  Need to deal with identical 
		int end = start < 0 ? -1 : expSig.lastIndexOf(args[args.length - 1]) + args[args.length - 1].length();
		if (start < 0 || end < 0) {
			JavaVEPlugin.log("SimpleAttr.DecoderHelper.primRefreshFromComposition(): Error", MsgLogger.LOG_WARNING); //$NON-NLS-1$
			return expSig;
		}

		removeShadowAdapters();
		// Get the latest constraint		
		primRefreshArguments();
		addShadowAdapters();

		args = getSourceCodeArgs();
		// Regenerate the arguments, only
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			if (i > 0)
				sb.append(", "); //$NON-NLS-1$
			sb.append(args[i]);
		}
		// Replace the arguments part	
		StringBuffer newExp = new StringBuffer(expSig);
		newExp.replace(start, end, sb.toString());
		fExprSig = newExp.toString();
		return fExprSig;
	}

	/**
	 *   Overidable test: Is this expression fits this decoder
	 */
	protected boolean isMySigniture() {
		if (fbeanPart.getEObject() == null)
			return false;

		EStructuralFeature sf = fbeanPart.getEObject().eClass().getEStructuralFeature(JTabbedPaneDecoder.JTABBED_PANE_FEATURE_NAME);
		if (sf != null)
			return fFmapper.getFeature(fExpr).equals(sf);
		else
			return false;
	}

	/**
	 *  Detemine the source code argument for the add() method
	 */
	private String[] getSourceCodeArgs() {

		String AddedArg;
		if (fAddedPart == null)
			AddedArg = CodeGenUtil.getInitString((IJavaInstance) fAddedInstance);
		else if (fAddedPart.getInitMethod().equals(fbeanPart.getInitMethod())) // Added part is defined in the same method as the container
			AddedArg = fAddedPart.getSimpleName();
		else
			AddedArg = fAddedPart.getReturnedMethod().getMethodName() + ExpressionTemplate.LPAREN + ExpressionTemplate.RPAREN;

		List finalArgs = new ArrayList();

		// TODO  Need to deal with non String instances
		if (fTitleInstance != null)
			finalArgs.add(CodeGenUtil.getInitString(fTitleInstance));
		else
			finalArgs.add(SimpleAttributeDecoderHelper.NULL_STRING);

		if (fIconInstance != null)
			finalArgs.add(CodeGenUtil.getInitString(fIconInstance));
		else
			finalArgs.add(SimpleAttributeDecoderHelper.NULL_STRING);

		finalArgs.add(AddedArg);

		if (fToolTipInstance != null)
			finalArgs.add(CodeGenUtil.getInitString(fToolTipInstance));
		else
			finalArgs.add(SimpleAttributeDecoderHelper.NULL_STRING);

		if (fAddedIndex != null) {
			finalArgs.add(fAddedIndex);
		}
		String[] args = new String[finalArgs.size()];
		for (int i = 0; i < finalArgs.size(); i++)
			args[i] = (String) finalArgs.get(i);
		return args;
	}

	/**
	 *  Overide the abstract method, to deal No Decorations.
	 */
	protected ExpressionTemplate getExpressionTemplate() throws CodeGenException {

		// TODO  Need to deal with layout constraints

		String[] args = getSourceCodeArgs(); // Arguments for the add() method

		String sel = fbeanPart.getSimpleName();
		String mtd;
		if (fAddedIndex != null)
			mtd = fFmapper.getIndexMethodName();
		else
			mtd = fFmapper.getMethodName();
		ExpressionTemplate exp = new ExpressionTemplate(sel, mtd, args, null, 0);
		exp.setLineSeperator(fbeanPart.getModel().getLineSeperator());
		return exp;
	}

	protected void primRefreshArguments() {

		// indexValueFound will designate if to use and index APi or not
		if (indexValueFound || fAddedIndex != null) {
			// Add an index position, as it was found.
			List parentsChildren = getRootComponentList();
			int indexOfAddedPart = parentsChildren.indexOf(getRootObject(false));
			fAddedIndex = Integer.toString(indexOfAddedPart);
		} else
			fAddedIndex = null;

		EObject root = getRootObject(false);
		fIconInstance = getIcon(root);
		fTitleInstance = getTabTitle(root);
		fToolTipInstance = getToolTip(root);

	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractContainerAddDecoderHelper#getComponentArguments(EObject)
	 */
	protected IJavaObjectInstance[] getComponentArguments(EObject root) {
		IJavaObjectInstance args[] = new IJavaObjectInstance[3];
		args[0] = getIcon(root);
		args[1] = getTabTitle(root);
		args[2] = getToolTip(root);

		return args;
	}

	/**
	 * Overide to include dependant SFs
	 */
	public boolean isRelevantFeature(EStructuralFeature sf) {
		if (super.isRelevantFeature(sf))
			return true;
		EObject root = getRootObject(true);
		if (root == null)
			return false;
		EClass rootEClass = root.eClass();

		return sf != null
			&& (sf.equals(rootEClass.getEStructuralFeature(TAB_ICON_ATTR_NAME))
				|| sf.equals(rootEClass.getEStructuralFeature(TAB_TOOLTIP_ATTR_NAME))
				|| sf.equals(rootEClass.getEStructuralFeature(TAB_TITLE_ATTR_NAME)));

		//	if (sf != null) {
		//		for (int i=0; i<hSF.length; i++) {
		//		  if (sf.equals(hSF[i])) {
		////		   || (hSF[i].((XMIResource)eResource()).getID(this) != null && hSF[i].((XMIResource)eResource()).getID(this).((XMIResource)equals(sf.eResource()).getID(equals(sf)))) 
		//		     return true ;
		//		  }
		//		}
		//	}
		//	return false ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractContainerAddDecoderHelper#getRootComponentSF()
	 */
	protected EStructuralFeature getRootComponentSF() {
		return getRootClass().getEStructuralFeature(COMPONENT_ATTR_NAME);
	}

}
