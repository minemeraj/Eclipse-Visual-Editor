/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.codegen;
/*
 *  $RCSfile: JTabbedPaneAddDecoderHelper.java,v $
 *  $Revision: 1.12 $  $Date: 2004-08-27 15:34:49 $ 
 */
import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.*;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.BeanDeclModel;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.codegen.util.TypeResolver.Resolved;
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
		IVEModelInstance cm = fbeanPart.getModel().getCompositionModel();
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
		JavaVEPlugin.log("JTabbedPaneAddDecoderHelper.add(" + toAdd + "," + target + "@" + i + ")", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
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
		if (arg instanceof MethodInvocation) {
			// Look to see of if this method returns a Bean
			String selector = ((MethodInvocation) arg).getName().getIdentifier();
			bp = fOwner.getBeanModel().getBeanReturned(selector);
		} else if (arg instanceof SimpleName) {
			// Simple reference to a bean
			String selector = ((SimpleName) arg).getIdentifier();
			bp = fOwner.getBeanModel().getABean(selector);
			if (bp == null) {
				bp = fOwner.getBeanModel().getABean(BeanDeclModel.constructUniqueName(fOwner.getExprRef().getMethod(), selector));
				//bp = fOwner.getBeanModel().getABean(fOwner.getExprRef().getMethod().getMethodHandle()+"^"+selector);
			}
		} else if (arg instanceof ClassInstanceCreation) {
			Resolved resolved = fbeanPart.getModel().getResolver().resolveType(((ClassInstanceCreation)arg).getName());
			if (resolved == null)
				return null;
			String clazzName = resolved.getName();
			IJavaObjectInstance obj =
				(IJavaObjectInstance) CodeGenUtil.createInstance(clazzName, fbeanPart.getModel().getCompositionModel());
			JavaClass c = (JavaClass) obj.getJavaType();
			if (c.isExistingType())
				fAddedInstance = obj;
		}

		return bp;
	}

	protected BeanPart parseAddedPart(MethodInvocation exp) throws CodeGenException {
		// TODO  Need to deal with multiple arguments, and nesting

		if (exp == null)
			return null;

		BeanPart bp = null;

		List args = exp.arguments();

		if (args.size() >= 4)
			bp = resolveAddedComponent((Expression)args.get(2));
		else if (args.size() == 2)
			bp = resolveAddedComponent((Expression)args.get(1));
		else if (args.size() == 1)
			bp = resolveAddedComponent((Expression)args.get(0));
		else
			throw new CodeGenException("Bad Arguments !!! " + exp); //$NON-NLS-1$

		return bp;
	}

	protected void processTitle(Expression arg) throws CodeGenException {
		if (arg instanceof NullLiteral)
			fTitleInstance = null;
		else if (arg instanceof StringLiteral) {
			fTitleInstance = (IJavaObjectInstance) CodeGenUtil.createInstance("java.lang.String", fbeanPart.getModel().getCompositionModel()); //$NON-NLS-1$
			setInitString(fTitleInstance, ((StringLiteral)arg).getLiteralValue());
			fbeanPart.getInitMethod().getCompMethod().getProperties().add(fTitleInstance);
		}
	}

	protected void processIcon(Expression arg) throws CodeGenException {
		// TODO  Need to support local declarations
		if (arg instanceof NullLiteral)
			fIconInstance = null;
		else if (arg instanceof ClassInstanceCreation) {
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
			setInitString(fToolTipInstance, ((StringLiteral) arg).getLiteralValue());
			fbeanPart.getInitMethod().getCompMethod().getProperties().add(fToolTipInstance);
		}

	}

	protected boolean parseAndAddArguments(List args) throws CodeGenException {
		//  fAddedConstraintInstance = null ;
		if (fAddedPart != null || fAddedInstance != null) {
			// process the Title

			if (args.size() >= 4) {
				processTitle((Expression)args.get(0));
				processIcon((Expression)args.get(1));
				processToolTip((Expression)args.get(3));
			} else if (args.size() == 2) {
				if (args.get(0) instanceof StringLiteral)
					processTitle((Expression)args.get(0));
			}

			int index = -1;
			if (args.size() == 5 && args.get(4) instanceof NumberLiteral) {
				fAddedIndex = args.get(4).toString();
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
			JavaVEPlugin.log("SimpleAttr.DecoderHelper.primRefreshFromComposition(): Error", Level.WARNING); //$NON-NLS-1$
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
			AddedArg = CodeGenUtil.getInitString((IJavaInstance) fAddedInstance,fbeanPart.getModel(), fOwner.getExprRef().getReqImports());
		else if (fAddedPart.getInitMethod().equals(fbeanPart.getInitMethod())) // Added part is defined in the same method as the container
			AddedArg = fAddedPart.getSimpleName();
		else
			AddedArg = fAddedPart.getReturnedMethod().getMethodName() + ExpressionTemplate.LPAREN + ExpressionTemplate.RPAREN;

		List finalArgs = new ArrayList();

		// TODO  Need to deal with non String instances
		if (fTitleInstance != null)
			finalArgs.add(CodeGenUtil.getInitString(fTitleInstance,fbeanPart.getModel(), fOwner.getExprRef().getReqImports()));
		else
			finalArgs.add(SimpleAttributeDecoderHelper.NULL_STRING);

		if (fIconInstance != null)
			finalArgs.add(CodeGenUtil.getInitString(fIconInstance,fbeanPart.getModel(), fOwner.getExprRef().getReqImports()));
		else
			finalArgs.add(SimpleAttributeDecoderHelper.NULL_STRING);

		finalArgs.add(AddedArg);

		if (fToolTipInstance != null)
			finalArgs.add(CodeGenUtil.getInitString(fToolTipInstance,fbeanPart.getModel(), fOwner.getExprRef().getReqImports()));
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
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractContainerAddDecoderHelper#generateSrc()
	 */
	protected String generateSrc() throws CodeGenException {
		ExpressionTemplate exp = getExpressionTemplate();
		return exp.toString();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractContainerAddDecoderHelper#shouldCommit(org.eclipse.ve.internal.java.codegen.model.BeanPart, org.eclipse.ve.internal.java.codegen.model.BeanPart, org.eclipse.emf.ecore.EObject, java.util.List)
	 */
	protected boolean shouldCommit(BeanPart oldAddedPart, BeanPart newAddedPart, EObject newAddedInstance, List args) {
		boolean shouldCommit = super.shouldCommit(oldAddedPart, newAddedPart, newAddedInstance, args);
		// Affected by offset changes?
		if(!shouldCommit){
			if (args.size() >= 4) {
				String currentTitle = fTitleInstance==null?null:CodeGenUtil.getInitString(fTitleInstance, fbeanPart.getModel(), null);
				String currentIcon = fIconInstance==null?null:CodeGenUtil.getInitString(fIconInstance, fbeanPart.getModel(), null);
				String currentTooltip = fToolTipInstance==null?null:CodeGenUtil.getInitString(fToolTipInstance, fbeanPart.getModel(), null);
				
				String newTitle =  args.get(0) instanceof StringLiteral?"\""+((StringLiteral)args.get(0)).getLiteralValue()+"\"":null;
				String newIcon =  args.get(1) instanceof StringLiteral?"\""+((StringLiteral)args.get(1)).getLiteralValue()+"\"":null;
				String newTooltip =  args.get(3) instanceof StringLiteral?"\""+((StringLiteral)args.get(3)).getLiteralValue()+"\"":null;
				
				boolean titleChanged = true;
				boolean iconChanged = true;
				boolean tooltipChanged = true;
				
				if(newTitle==currentTitle || (newTitle!=null && newTitle.equals(currentTitle)) || (currentTitle!=null && currentTitle.equals(newTitle)))
					titleChanged = false;
				if(newIcon==currentIcon || (newIcon!=null && newIcon.equals(currentIcon)) || (currentIcon!=null && currentIcon.equals(newIcon)))
					iconChanged = false;
				if(newTooltip==currentTooltip || (newTooltip!=null && newTooltip.equals(currentTooltip)) || (currentTooltip!=null && currentTooltip.equals(newTooltip)))
					tooltipChanged = false;
				
				shouldCommit = titleChanged || iconChanged || tooltipChanged;
				
			} else if (args.size() == 2) {
				if (args.get(0) instanceof StringLiteral){
					String currentTitle = fTitleInstance==null?null:CodeGenUtil.getInitString(fTitleInstance, fbeanPart.getModel(), null);
					String newTitle =  args.get(0) instanceof StringLiteral?"\""+((StringLiteral)args.get(0)).getLiteralValue()+"\"":null;
					boolean titleChanged = true;
					if(newTitle==currentTitle || (newTitle!=null && newTitle.equals(currentTitle)) || (currentTitle!=null && currentTitle.equals(newTitle)))
						titleChanged = false;
					shouldCommit = titleChanged;
				}
			}

			if(!shouldCommit){
				if (args.size() == 5 && args.get(4) instanceof NumberLiteral) {}
				else shouldCommit = !canAddingBeSkippedByOffsetChanges();
			}
		}
		if(!shouldCommit){
			if(fAddedPart!=null){
		      	  boolean backRefAdded = false;
			      BeanPart[] bRefs = fAddedPart.getBackRefs();
			      int bRefCount = 0;
			      for (; bRefCount < bRefs.length; bRefCount++) 
					if(fbeanPart.equals(bRefs[bRefCount])){
						backRefAdded = true;
						break;
					}
				    
			       boolean childAdded = false;
				   Iterator childItr = fbeanPart.getChildren();
				   while (childItr.hasNext()) {
						BeanPart child = (BeanPart) childItr.next();
						if(child.equals(fAddedPart)){
							childAdded = true;
							break;
						}
				   }
					shouldCommit = !backRefAdded || !childAdded;
			}
			if(!shouldCommit){
				int index = findIndex(fbeanPart);
				EStructuralFeature sf = fFmapper.getFeature(null);
				EObject targetEObject = fbeanPart.getEObject();
				EObject addedOne = getRootObject(false);
				if (sf.isMany()) {
					List elements = getRootComponentList();
					if((!elements.contains(addedOne)) || (index>-1 && elements.indexOf(addedOne)!=index))
						shouldCommit = true;
				}else
				   shouldCommit =  (!targetEObject.eIsSet(sf)) || (!targetEObject.eGet(sf).equals(addedOne));
			}
		}
		return shouldCommit;
	}

}
