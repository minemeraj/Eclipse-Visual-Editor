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
package org.eclipse.ve.internal.jfc.codegen;
/*
 *  $RCSfile: JTabbedPaneAddDecoderHelper.java,v $
 *  $Revision: 1.23 $  $Date: 2005-11-15 18:53:31 $ 
 */
import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.*;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.model.CodeMethodRef;
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
	boolean titleInstanceSet, toolTipInstanceSet, iconInstanceSet;

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
		
		if (toAdd.eContainer()==null) {
			// not in the model yet ... no Bean Part
			fbeanPart.getInitMethod().getCompMethod().getProperties().add(toAdd);
		}

		EClass rootClass = getRootClass();
		EObject root = rootClass.getEPackage().getEFactoryInstance().create(rootClass);

		//   root.eSet(root.eClass().getEStructuralFeature(COMPONENT_ATTR_NAME),
		//                    toAdd.getRefObject()) ;
		CodeGenUtil.eSet(root, root.eClass().getEStructuralFeature(COMPONENT_ATTR_NAME), toAdd, -1);

		if (titleInstanceSet)
			root.eSet(root.eClass().getEStructuralFeature(TAB_TITLE_ATTR_NAME), fTitleInstance);

		if (toolTipInstanceSet)
			root.eSet(root.eClass().getEStructuralFeature(TAB_TOOLTIP_ATTR_NAME), fToolTipInstance);

		if (iconInstanceSet)
			root.eSet(root.eClass().getEStructuralFeature(TAB_ICON_ATTR_NAME), fIconInstance);

		// target's add feature
		EStructuralFeature cf = fFmapper.getFeature(null);
		java.util.List compList = (java.util.List) target.getEObject().eGet(cf);
		if (JavaVEPlugin.isLoggingLevel(Level.FINE))
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
			bp = CodeGenUtil.getBeanPart(fbeanPart.getModel(), selector, fOwner.getExprRef().getMethod(), fOwner.getExprRef().getOffset());
		} else if (arg instanceof ClassInstanceCreation) {
			if (fAddedInstance==null) {
			  Resolved resolved = fbeanPart.getModel().getResolver().resolveType(((ClassInstanceCreation)arg).getName());
			  if (resolved == null)
				return null;
			  String clazzName = resolved.getName();
			  IJavaObjectInstance obj =
				(IJavaObjectInstance) CodeGenUtil.createInstance(clazzName, fbeanPart.getModel().getCompositionModel());
			  JavaClass c = (JavaClass) obj.getJavaType();
			  if (c.isExistingType()) {
				fAddedInstance = obj;
				fAddedInstance.setAllocation(getAllocation(arg));
			  }
			}
		}

		return bp;
	}

	protected BeanPart parseAddedPart(MethodInvocation exp) throws CodeGenException {
		// TODO  Need to deal with multiple arguments, and nesting

		if (exp == null)
			return null;

		BeanPart bp = null;

		List args = exp.arguments();

		bp = resolveAddedComponent((Expression)args.get(getAddedPartArgIndex(args.size())));

		return bp;
	}

	protected void processTitle(Expression arg) throws CodeGenException {
		if (arg instanceof NullLiteral) {
			fTitleInstance = null;
			titleInstanceSet = true;
		}else if(arg != null){
			titleInstanceSet = true;
			fTitleInstance = (IJavaObjectInstance) CodeGenUtil.createInstance("java.lang.String", fbeanPart.getModel().getCompositionModel()); //$NON-NLS-1$
            CodeMethodRef expOfMethod = (fOwner!=null && fOwner.getExprRef()!=null) ? fOwner.getExprRef().getMethod():null;
            JavaAllocation alloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation(
            		ConstructorDecoderHelper.getParsedTree(arg,expOfMethod, fOwner.getExprRef().getOffset(), fbeanPart.getModel(),getExpressionReferences()));
            fTitleInstance.setAllocation(alloc);
			fbeanPart.getInitMethod().getCompMethod().getProperties().add(fTitleInstance);
		} else
			titleInstanceSet = false;
	}

	protected void processIcon(Expression arg) throws CodeGenException {
		// TODO  Need to support local declarations
		if (arg instanceof NullLiteral) {
			fIconInstance = null;
			iconInstanceSet = true;
		} else if (arg != null) {
			iconInstanceSet = true;
			fIconInstance = (IJavaObjectInstance) CodeGenUtil.createInstance("javax.swing.Icon", fbeanPart.getModel().getCompositionModel()); //$NON-NLS-1$
			CodeMethodRef expOfMethod = (fOwner!=null && fOwner.getExprRef()!=null) ? fOwner.getExprRef().getMethod():null;
			fIconInstance.setAllocation(InstantiationFactory.eINSTANCE.createParseTreeAllocation(
            		ConstructorDecoderHelper.getParsedTree(arg,expOfMethod, fOwner.getExprRef().getOffset(), fbeanPart.getModel(),getExpressionReferences())));
			fbeanPart.getInitMethod().getCompMethod().getProperties().add(fIconInstance);
		}
	}

	protected void processToolTip(Expression arg) throws CodeGenException {

		if (arg instanceof NullLiteral) {
			fToolTipInstance = null;
			toolTipInstanceSet = true;
		}else if(arg != null){
			toolTipInstanceSet = true;
			fToolTipInstance = (IJavaObjectInstance) CodeGenUtil.createInstance("java.lang.String", fbeanPart.getModel().getCompositionModel()); //$NON-NLS-1$
            CodeMethodRef expOfMethod = (fOwner!=null && fOwner.getExprRef()!=null) ? fOwner.getExprRef().getMethod():null;
            JavaAllocation alloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation(
            		ConstructorDecoderHelper.getParsedTree(arg,expOfMethod, fOwner.getExprRef().getOffset(), fbeanPart.getModel(),getExpressionReferences()));
            fToolTipInstance.setAllocation(alloc);
			fbeanPart.getInitMethod().getCompMethod().getProperties().add(fToolTipInstance);
		}
	}

	protected boolean parseAndAddArguments(List args) throws CodeGenException {
		//  fAddedConstraintInstance = null ;
		if (fAddedPart != null || fAddedInstance != null) {
			// process the args.

			int index = -1;
			switch (args.size()) {
				case 1:
					break;	// add(Component)	- nothing else set.
				case 2:
					// add(Component, index), add(Component, Object), add(String, Component), or addTab(String, Component)
					String methodName = fFmapper.getMethodName();
					// RLK: Right now I don't know how to distinquish at this point add() because if complicated 
					// then it could be anyone. Try for if arg0=StringLiteral then add(String, Component) else it is add(Component, ...).
					if (JTabbedPaneDecoder.JTABBED_PANE_METHOD_ADDTAB.equals(methodName)) {
						// addTab(String, Component)
						processTitle((Expression)args.get(0));
					} else {
						if (args.get(0) instanceof StringLiteral) {
							// add(String, Component)
							processTitle((Expression)args.get(0));
						} else {
							// For now use if NumberLiteral then add(Component, index). Can't handle more complex type at this time.
							// If it is a StringLiteral, then treat as title. Anything else is too complex and we will ignore it.
							if (args.get(1) instanceof NumberLiteral) {
								fAddedIndex = args.get(1).toString();
								index = Integer.parseInt(fAddedIndex);
							} else if (args.get(1) instanceof StringLiteral) {
								// add(Component, Object) but if Object is a String, then it is title.
								processTitle((Expression)args.get(1));
							}
						}
					}
					break;
				case 3:
					// add(Component, Object, index) or addTab(String Component)
					methodName = fFmapper.getMethodName();
					if (JTabbedPaneDecoder.JTABBED_PANE_METHOD_ADDTAB.equals(methodName)) {
						// addTab(String, Icon, Component)
						processTitle((Expression)args.get(0));
						processIcon((Expression)args.get(1));						
					} else {
						// add(Component, Object, index)
						if (args.get(1) instanceof StringLiteral) {
							// add(Component, Object, index) but if Object is a String, then it is title. - index has to be a NumberLiteral for us right now.
							processTitle((Expression)args.get(1));
						}
						if (args.get(2) instanceof NumberLiteral) {
							fAddedIndex = args.get(2).toString();
							index = Integer.parseInt(fAddedIndex); 
						}						
					}
					break;
				case 4:
					// addTab(String, Icon, Component, Tooltip)
					processTitle((Expression)args.get(0));
					processIcon((Expression)args.get(1));						
					processToolTip((Expression)args.get(3));
					break;
				case 5:
					// insertTab(String, Icon, Component, Tooltip, index) - index has to be a NumberLiteral for us right now.
					processTitle((Expression)args.get(0));
					processIcon((Expression)args.get(1));						
					processToolTip((Expression)args.get(3));
					if (args.get(4) instanceof NumberLiteral) {
						fAddedIndex = args.get(4).toString();
						index = Integer.parseInt(fAddedIndex);
					}
					break;
				default:
					throw new IllegalArgumentException("Bad number of Arguments !!! "); //$NON-NLS-1$
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
			if (JavaVEPlugin.isLoggingLevel(Level.WARNING))
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
			AddedArg = CodeGenUtil.getInitString(fAddedInstance,fbeanPart.getModel(), fOwner.getExprRef().getReqImports(), getExpressionReferences());
		else if (fAddedPart.getInitMethod().equals(fbeanPart.getInitMethod())) // Added part is defined in the same method as the container
			AddedArg = fAddedPart.getSimpleName();
		else
			AddedArg = fAddedPart.getReturnedMethod().getMethodName() + ExpressionTemplate.LPAREN + ExpressionTemplate.RPAREN;

		List finalArgs = new ArrayList();

		// TODO  Need to deal with non String instances
		if (fTitleInstance != null)
			finalArgs.add(CodeGenUtil.getInitString(fTitleInstance,fbeanPart.getModel(), fOwner.getExprRef().getReqImports(), getExpressionReferences()));
		else
			finalArgs.add(SimpleAttributeDecoderHelper.NULL_STRING);

		if (fIconInstance != null)
			finalArgs.add(CodeGenUtil.getInitString(fIconInstance,fbeanPart.getModel(), fOwner.getExprRef().getReqImports(),getExpressionReferences()));
		else
			finalArgs.add(SimpleAttributeDecoderHelper.NULL_STRING);

		finalArgs.add(AddedArg);

		if (fToolTipInstance != null)
			finalArgs.add(CodeGenUtil.getInitString(fToolTipInstance,fbeanPart.getModel(), fOwner.getExprRef().getReqImports(), getExpressionReferences()));
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
				if (!sameInitString(fTitleInstance,(Expression)args.get(0)) ||
					!sameInitString(fIconInstance,(Expression)args.get(1)) ||
					!sameInitString(fToolTipInstance,(Expression)args.get(3)))
					shouldCommit=true;									
								
			} else if (args.size() == 2) {
				if (args.get(0) instanceof StringLiteral){
					if (!sameInitString(fTitleInstance,(Expression)args.get(0)))
					     shouldCommit = true;
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

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractContainerAddDecoderHelper#getAddedPartArgIndex()
	 */
	protected int getAddedPartArgIndex(int argsSize) {
		switch (argsSize) {
			case 1:
				return 0;	// add(Component)
			case 2:
				// add(Component, index), add(Component, Object), add(String, Component), or addTab(String, Component)
				String methodName = fFmapper.getMethodName();
				// RLK: Right now I don't know how to distinquish at this point add() because if complicated 
				// then it could be anyone. Try for if arg0=StringLiteral then add(String, Component) else it is add(Component, ...).
				if (JTabbedPaneDecoder.JTABBED_PANE_METHOD_ADDTAB.equals(methodName))
					return 1;	// addTab(String, Component)
				List args = ((MethodInvocation) getExpression(fExpr)).arguments();
				if (args.get(0) instanceof StringLiteral)
					return 1;	// add(String, Component)
				return 0;
			case 3:
				// add(Component, Object, index) or addTab(String Component)
				methodName = fFmapper.getMethodName();
				if (JTabbedPaneDecoder.JTABBED_PANE_METHOD_ADDTAB.equals(methodName))
					return 2;	// addTab(String, Icon, Component)
				else
					return 0;	// add(Component, Object, index)
			case 4:
				return 2;	// addTab(String, Icon, Component, Tooltip)
			case 5:
				return 2;	// insertTab(String, Icon, Component, Tooltip, index)
			default:
				throw new IllegalArgumentException("Bad number of Arguments !!! "); //$NON-NLS-1$
		}
	}

	public boolean restore() throws CodeGenException {
		boolean result = super.restore();
		if (result && fRootObj!=null) {
			fIconInstance = getIcon(fRootObj);
			fTitleInstance = getTabTitle(fRootObj);
			fToolTipInstance = getToolTip(fRootObj);
		}
		return result;
	}
	public Object[] getReferencedInstances() {		
		Collection result = CodeGenUtil.getReferences(fbeanPart.getEObject(),false);
		Object[] added = getAddedInstance();
		for (int i = 0; i < added.length; i++) 
			result.addAll(CodeGenUtil.getReferences(added[i],true));
		result.addAll(CodeGenUtil.getReferences(fIconInstance,true));
		result.addAll(CodeGenUtil.getReferences(fTitleInstance,true));
		result.addAll(CodeGenUtil.getReferences(fToolTipInstance,true));
		return result.toArray();
	}
}
