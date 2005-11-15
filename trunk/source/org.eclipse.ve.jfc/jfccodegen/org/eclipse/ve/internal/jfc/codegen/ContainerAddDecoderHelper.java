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
 *  $RCSfile: ContainerAddDecoderHelper.java,v $
 *  $Revision: 1.31 $  $Date: 2005-11-15 18:53:31 $ 
 */

import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.impl.NaiveExpressionFlattener;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.codegen.util.TypeResolver.Resolved;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public class ContainerAddDecoderHelper extends AbstractIndexedChildrenDecoderHelper {

	private static final URI ConstraintComponentURI = URI.createURI("platform:/plugin/org.eclipse.ve.jfc/overrides/java/awt/containerVisuals.ecore#ConstraintComponent"); //$NON-NLS-1$

	// TODO This needs to be changed to parse and store parse trees and not init strings for the constraints.
	BeanPart fAddedPart = null;
	IJavaObjectInstance fAddedInstance = null;
	EObject fCC = null;
	IJavaObjectInstance fAddedConstraintInstance = null;
	String fAddedConstraint = null;
	String fnonResolvedAddedConstraint = null;
	boolean fisAddedConstraintSet = false;
	String fAddedIndex = null;
	boolean indexValueFound = false;

	public ContainerAddDecoderHelper(BeanPart bean, Statement exp, IJavaFeatureMapper fm, IExpressionDecoder owner) {
		super(bean, exp, fm, owner);
	}
	/**
	 * Overide the default action, as we are maintained as a list
	 */
	public void adaptToCompositionModel(IExpressionDecoder decoder) {

		unadaptToCompositionModel();

		super.adaptToCompositionModel(decoder);
		// BeanPart can be a This Part
		BeanDecoderAdapter ba =
			(BeanDecoderAdapter) EcoreUtil.getExistingAdapter(fbeanPart.getEObject(), ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);

		BeanDecoderAdapter refAdapter = ba.getRefAdapter(fOwner, org.eclipse.emf.common.notify.Notification.ADD);
		fCC.eAdapters().add(refAdapter);
		IJavaObjectInstance c = CodeGenUtil.getCCconstraint(fCC);
		// Add a SourceRange adapter for the constraint
		if (c != null
			&& // If we already have an Expression Adapter on it, do not add a Range Adapter e.g., GridBagConstraint
		EcoreUtil
			.getExistingAdapter(
			c,
			ICodeGenAdapter.JVE_CODEGEN_EXPRESSION_SOURCE_RANGE)
				== null)
			c.eAdapters().add(fexpAdapter.getShadowSourceRangeAdapter());

	}

	/**
	 * Most helpers should consider overiding this one
	 */
	public void unadaptToCompositionModel() {

		super.unadaptToCompositionModel();
		if (fCC != null) {

			BeanDecoderAdapter adapter = // Reference Adapter
				(BeanDecoderAdapter) EcoreUtil.getExistingAdapter(fCC, ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);
			if (adapter != null)
				fCC.eAdapters().remove(adapter);
			IJavaObjectInstance c = CodeGenUtil.getCCconstraint(fCC);
			// Add a SourceRange adapter for the constraint
			if (c != null) {
				ICodeGenAdapter a = (ICodeGenAdapter) EcoreUtil.getExistingAdapter(c, ICodeGenAdapter.JVE_CODEGEN_EXPRESSION_SOURCE_RANGE);
				if (a != null && !(a instanceof ExpressionDecoderAdapter))
					c.eAdapters().remove(a);
				// If we already have an Expression Adapter on it, do not add a Range Adapter e.g., GridBagConstraint

			}
		}

	}

	/*
	 * Decoder Specific Add 
	 */
	protected void add(BeanPart toAdd, BeanPart target) throws CodeGenException {
		add(null, toAdd, target, -1);
	}

	public static EObject getNewCC(IVEModelInstance cm) {
		EClass ccClass = (EClass) cm.getModelResourceSet().getEObject(ConstraintComponentURI, true);
		return ccClass.getEPackage().getEFactoryInstance().create(ccClass);
	}

	/**
	 * Add a component into a container object at a specified index
	 */
	public static EObject addComponentToContainer(
		EObject cc,
		EObject componentToAdd,
		EObject targetContainer,
		int index,
		IVEModelInstance cm) {
		// Create a ConstraintComponet object   
		EObject CC = (cc == null) ? getNewCC(cm) : cc;

		// TODO  Need to stop doing this when MOF maintains ordering
		CodeGenUtil.eSet(CC, CC.eClass().getEStructuralFeature("component"), //$NON-NLS-1$
		componentToAdd, -1);

		EStructuralFeature cf = CodeGenUtil.getComponentFeature(targetContainer);
		java.util.List compList = (java.util.List) targetContainer.eGet(cf);
		if (JavaVEPlugin.isLoggingLevel(Level.FINE))
			JavaVEPlugin.log("ContainerAddDecoderHelper(" + componentToAdd.eClass() + "," + targetContainer.eClass() + "@" + index + ")", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		if (index < 0)
			compList.add(CC);
		else
			compList.add(Math.min(compList.size(), index), CC);

		return CC;
	}

	/**
	 * Decoder Specific Add 
	 * @return EObject the ConstraintComponent object
	 * 
	 * @param addCompToModel  will insert the Instance with no Beanpart into the model
	 */
	protected EObject add(EObject cc, EObject comp, BeanPart target, int index, boolean addCompToModel) throws CodeGenException {
		int i = index;
		if (i < 0)
			i = findIndex(target);
		fAddedConstraintInstance = CodeGenUtil.getCCconstraint(cc);
		if (fAddedConstraintInstance != null
			&& // Not a bean part, make it a property
		fbeanPart.getModel().getABean(fAddedConstraintInstance) == null)
			fbeanPart.getInitMethod().getCompMethod().getProperties().add(fAddedConstraintInstance);
		if (addCompToModel)
			fbeanPart.getInitMethod().getCompMethod().getProperties().add(comp);

		//  Make this relationship in the Composition Model
		fCC = addComponentToContainer(cc, comp, target.getEObject(), i, fbeanPart.getModel().getCompositionModel());
		return fCC;
	}

	/*
	 * Decoder Specific Add 
	 * @return EObject the ConstraintComponent object
	 */
	protected EObject add(EObject cc, BeanPart toAdd, BeanPart target, int index) throws CodeGenException {
		toAdd.addBackRef(target, (EReference) fFmapper.getFeature(null));
		toAdd.addToJVEModel();
		target.addChild(toAdd);

		return add(cc, toAdd.getEObject(), target, index, false);
	}

	/**
	 * @return List of all the components aggregated by fbeanPart
	 */
	protected List getComponentList() {
		return CodeGenUtil.getChildrenComponents(fbeanPart.getEObject());
	}

	/**
	 * @return EObject denoting the ConstratintComponent object associated this expression
	 */
	protected EObject getConstraintComponent(boolean cache) {
		if (fCC != null && cache)
			return fCC;
		List compList = getComponentList();
		// Look for out added part
		EObject targetCC = null;
		if (compList != null && compList.size() > 0) {
			// This component has childrents, Process them first, so that we 
			// can add them as we generate the current method
			Iterator itr = compList.iterator();
			while (itr.hasNext()) {
				EObject CC = (EObject) itr.next();
				IJavaObjectInstance child = CodeGenUtil.getCCcomponent(CC);
				if (child != null && child.equals(fAddedInstance)) {
					targetCC = CC;
					break;
				}
			}
		}
		if (targetCC != null)
			fCC = targetCC;
		return targetCC;
	}

	protected void clearPreviousIfNeeded() {

		List compList = CodeGenUtil.getChildrenComponents(fbeanPart.getEObject());
		// Look for out added part
		EObject targetCC = getConstraintComponent(false);
		if (targetCC != null && compList != null && compList.size() > 0) {
			compList.remove(targetCC);
		}
		if (targetCC != null) {
			targetCC.eUnset(targetCC.eClass().getEStructuralFeature("component")); //$NON-NLS-1$
		}
		
		List references = fOwner.getExprRef().getReferences();
		if(references.contains(fAddedInstance))
			references.remove(fAddedInstance);
		if(references.contains(fAddedConstraintInstance))
			references.remove(fAddedConstraintInstance);
		
		if (fAddedPart == null)
			cleanProperty(fAddedInstance);
		cleanProperty(fAddedConstraintInstance);
	}
	
	protected JavaAllocation getAllocation (Expression exp) {
		CodeMethodRef expOfMethod = (fOwner!=null && fOwner.getExprRef()!=null) ? fOwner.getExprRef().getMethod():null;
		   JavaAllocation alloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation(
		 		  ConstructorDecoderHelper.getParsedTree(exp,
		 		  expOfMethod,fOwner.getExprRef().getOffset(), fbeanPart.getModel(),getExpressionReferences()));
		return alloc;
	}

	protected BeanPart parseAddedPart(MethodInvocation exp) throws CodeGenException {
		// TODO   Need to deal with multiple arguments, and nesting

		if (exp == null)
			return null;

		BeanPart bp = null;

		List args = exp.arguments();
		if (args.size() < 1)
			throw new CodeGenException("No Arguments !!! " + exp); //$NON-NLS-1$

		// Parse the arguments to figure out which bean to add to this container
		if (args.get(0) instanceof MethodInvocation) {
			// Look to see of if this method returns a Bean
			String selector = ((MethodInvocation)args.get(0)).getName().getIdentifier();
			bp = fOwner.getBeanModel().getBeanReturned(selector);
		} else if (args.get(0) instanceof SimpleName) {
			// Simple reference to a bean
			String beanName = ((SimpleName)args.get(0)).getIdentifier();
			bp = CodeGenUtil.getBeanPart(fbeanPart.getModel(), beanName, fOwner.getExprRef().getMethod(), fOwner.getExprRef().getOffset());
		} else if (args.get(0) instanceof ClassInstanceCreation) {
			if (fAddedInstance==null) {
 			 Resolved resolved = fbeanPart.getModel().getResolver().resolveType(((ClassInstanceCreation)args.get(0)).getName());
			 if (resolved == null)
				return null;
			 String clazzName = resolved.getName();  			
			 IJavaObjectInstance obj =
				(IJavaObjectInstance) CodeGenUtil.createInstance(clazzName, fbeanPart.getModel().getCompositionModel());
			 JavaClass c = (JavaClass) obj.getJavaType();
			 if (c.isExistingType()) {
				  fAddedInstance = obj;
			      fAddedInstance.setAllocation(getAllocation((Expression)args.get(0)));
			 }
			}
		}
		if (bp != null)
			fAddedInstance = (IJavaObjectInstance) bp.getEObject();
		return bp;
	}

	/**
	 *   Add new Componet to target Bean,
	 */
	protected boolean addComponent() throws CodeGenException {

		BeanPart oldAddedPart = fAddedPart;
		BeanPart newAddedPart = parseAddedPart((MethodInvocation) ((ExpressionStatement)fExpr).getExpression());
		if (newAddedPart == null && fAddedInstance == null)
			throw new CodeGenException("No Added Part"); //$NON-NLS-1$
		List args = ((MethodInvocation)((ExpressionStatement)fExpr).getExpression()).arguments();
		return understandAddArguments(args, oldAddedPart, newAddedPart);
	}

	/**
	 * ed hoc manner to qualify classes.  Need to do a better job by actyally parsing this.
	 * 
	 * @param original  
	 * @param mayBeQualified
	 * @param fullyQualified
	 * @return
	 */
	protected String addQualifier(String original, String mayBeQualified, String fullyQualified) {
		if (mayBeQualified.equals(fullyQualified))
			return original;

		char[] noQualifiers = CharOperation.replace(original.toCharArray(), fullyQualified.toCharArray(), mayBeQualified.toCharArray());

		return new String(CharOperation.replace(noQualifiers, mayBeQualified.toCharArray(), fullyQualified.toCharArray()));

	}
	protected IJavaObjectInstance parseAllocatedConstraint(ClassInstanceCreation exp) {
		CodeMethodRef expOfMethod = (fOwner!=null && fOwner.getExprRef()!=null) ? fOwner.getExprRef().getMethod():null;
		PTExpression pt = ConstructorDecoderHelper.getParsedTree(exp, expOfMethod, fOwner.getExprRef().getOffset(), fbeanPart.getModel(), getExpressionReferences());
		IJavaObjectInstance result = null;
		try {
			result = (IJavaObjectInstance) CodeGenUtil.createInstance("java.lang.Object", fbeanPart.getModel().getCompositionModel()); //$NON-NLS-1$
			result.setAllocation(InstantiationFactory.eINSTANCE.createParseTreeAllocation(pt));
		} catch (CodeGenException e) {
		}

		return result;
	}

	/**
	 * Smart decoding capability:
	 * This method returns whether decoding should be performed or not due to offset
	 * changes. This is helpful during snippet update process where re-decoding is done for 
	 * offset changes, and decoding should not be done unless there is some 
	 * content change, or if the expression ordering has not changed 
	 * 
	 * @return
	 */
	protected boolean canAddingBeSkippedByOffsetChanges(BeanPart oldAddedPart, BeanPart newAddedPart) {
		if(oldAddedPart==null && newAddedPart==null)
			return true;
		if(oldAddedPart==newAddedPart){
			List components = getComponentList();
			EObject currentCC = getConstraintComponent(false);
			int currentIndex = components.indexOf(currentCC);
			Collection expressions = fbeanPart.getRefExpressions();
			for (Iterator expItr = expressions.iterator(); expItr.hasNext();) {
				CodeExpressionRef exp = (CodeExpressionRef) expItr.next();
				Object[] expAddedInstances = exp.getAddedInstances();
				for(int eaic=0; expAddedInstances!=null && eaic<expAddedInstances.length; eaic++){
					if(		components.contains(expAddedInstances[eaic]) && 
							(	(components.indexOf(expAddedInstances[eaic]) < currentIndex && 
								exp.getOffset()>=fOwner.getExprRef().getOffset()) ||
								(components.indexOf(expAddedInstances[eaic]) > currentIndex && 
								exp.getOffset()<=fOwner.getExprRef().getOffset()))){
								return false;
							}
				}
			}
			return true;
		}
		return false;
	}

	protected boolean understandAddArguments(List args, BeanPart oldAddedPart, BeanPart newAddedPart) throws CodeGenException {
		boolean addedPartChanged = newAddedPart!=oldAddedPart;		
		if (!addedPartChanged) {			
			// added part may be a property e.g., add (new JPanel()) .. no BeanParts
			if (fCC!=null) {
				if (args.get(0) instanceof ClassInstanceCreation) {
				   JavaAllocation astAlloc = getAllocation((Expression)args.get(0));
				   if (!CodeGenUtil.areAllocationsEqual(fAddedInstance.getAllocation(), astAlloc)) {
				   	fAddedInstance.setAllocation(astAlloc);
				   	addedPartChanged = true;
				   }
				}
			}
			else
				addedPartChanged = fAddedInstance != null;
		}
		boolean constraintChanged = !canAddingBeSkippedByOffsetChanges(oldAddedPart, newAddedPart);
		
		IJavaObjectInstance tmpAddedConstraintInstance = null;
		String tmpAddedConstraint = null;
		String tmpNonResolvedAddedConstraint = null;
		boolean tmpIsAddedConstraintSet = false;
		String tmpAddedIndex = null;
		boolean tmpIndexValueFound = false;
		BeanPart tmpConstraintBeanPart = null;

		boolean defaultConstraintFound = false;
		int indexValuePosition = -1;

		if (fAddedPart != null || fAddedInstance != null) {
			// TODO  Deal with all signitures of add()
			if (args.size() >= 2) {
				// Get the current constraint
				EObject currentComponentConstraint = getConstraintComponent(false);
				EObject currentConstraint = null;
				if (currentComponentConstraint != null) {
					EStructuralFeature sf = currentComponentConstraint.eClass().getEStructuralFeature("constraint"); //$NON-NLS-1$
					currentConstraint = (IJavaObjectInstance) currentComponentConstraint.eGet(sf);
				}
				
				// Process all arguments to determine which is constraint and which is index.
				for (int arg = 1; arg < args.size(); arg++) {
					// Check to see if there is layout manager constraint
					if (args.get(arg) instanceof MethodInvocation) {
						MethodInvocation msgSnd = (MethodInvocation) args.get(arg);
						if (msgSnd.getExpression() instanceof MethodInvocation) {
							// check for getFooBean().getName() - our default           
							String method = msgSnd.getName().getIdentifier();
							if (!(ContainerDecoder.DEFAULT_CONSTRAINT.indexOf(method) < 0)) {
								defaultConstraintFound = false;
								continue;
							}
						}
						if (msgSnd.getExpression()==null || msgSnd.getExpression() instanceof ThisExpression) {
							// Things like getCurrentIndex() or getDefaultConstraint();
						}
					} else //  This is a Temporary solution !!!! we need to better fit for param. number for semantics
						if (args.get(arg) instanceof StringLiteral) {
							defaultConstraintFound = true;
							tmpAddedConstraint = tmpNonResolvedAddedConstraint = args.get(arg).toString();
							
							// SMART UPDATE CHECK
							if(!constraintChanged){
								if(currentConstraint instanceof IJavaObjectInstance){
									IJavaObjectInstance javaConstraint = (IJavaObjectInstance) currentConstraint;
									if(javaConstraint.getAllocation() instanceof InitStringAllocation){
										InitStringAllocation alloc = (InitStringAllocation) javaConstraint.getAllocation();
										constraintChanged = !tmpAddedConstraint.equals(alloc.getInitString());
									}else
										constraintChanged = true;
								}else
									constraintChanged = true;
							}
						} else if (args.get(arg) instanceof NumberLiteral) {
							tmpIndexValueFound = true;
							indexValuePosition = arg;
							
							// SMART UPDATE CHECK - determine the current index, and see if it has changed?
							if(fbeanPart!=null && fbeanPart.getEObject()!=null){
								EStructuralFeature cf = CodeGenUtil.getComponentFeature(fbeanPart.getEObject());
								java.util.List compList = (java.util.List) fbeanPart.getEObject().eGet(cf);
								if(compList!=null && compList.contains(currentComponentConstraint)){
									int currentIndex = compList.indexOf(currentComponentConstraint);
									int newIndex = Integer.parseInt(args.get(arg).toString());
									constraintChanged = currentIndex!=newIndex;
								}
							}
						} else if (args.get(arg) instanceof SimpleName) {
							// A Variable - like a bean name
							String beanName = ((SimpleName)args.get(arg)).getIdentifier();							
							tmpConstraintBeanPart = CodeGenUtil.getBeanPart(fbeanPart.getModel(), 
									     beanName, fOwner.getExprRef().getMethod(), fOwner.getExprRef().getOffset());
							
							// SMART UPDATE CHECK
							if(!constraintChanged){
								if(tmpConstraintBeanPart!=null)
									constraintChanged = (currentConstraint!=tmpConstraintBeanPart.getEObject());
								else
									constraintChanged = true;
							}
						} else if (args.get(arg) instanceof QualifiedName) {
							// Something dot separated - like a fqn class name, or a static variable.
							// Since chances are that it is a static variable like Button.NoRTH it 
							// should become the defaultConstraint if one is not found already.
							if (!defaultConstraintFound) {
								defaultConstraintFound = true;
								tmpNonResolvedAddedConstraint = args.get(arg).toString();
								TypeResolver.FieldResolvedType resolvedField = fbeanPart.getModel().getResolver().resolveWithPossibleField((Name)args.get(arg));
								if (resolvedField != null) {
									StringBuffer sb = new StringBuffer(tmpNonResolvedAddedConstraint.length());
									sb.append(resolvedField.resolvedType.getName());
									String[] accessors = resolvedField.fieldAccessors;
									for (int i = 0; i < accessors.length; i++) {
										sb.append('.');
										sb.append(accessors[i]);
									}
									tmpAddedConstraint = sb.toString();
								} else
									tmpAddedConstraint = tmpNonResolvedAddedConstraint;
								
								// SMART UPDATE CHECK
								if(!constraintChanged){
									if (currentConstraint instanceof IJavaObjectInstance) {
										IJavaObjectInstance javaConstraint = (IJavaObjectInstance) currentConstraint;
										if (javaConstraint.getAllocation() instanceof InitStringAllocation) {
											InitStringAllocation stringAlloc = (InitStringAllocation) javaConstraint.getAllocation();
											constraintChanged = !stringAlloc.getInitString().equals(tmpAddedConstraint);
										}else
											constraintChanged = true;
									}else
										constraintChanged = true;
								}
							}
						} else if (args.get(arg) instanceof NullLiteral) {
							// TODO  Arg index should be consulted
							defaultConstraintFound = true;
							tmpAddedConstraint = tmpNonResolvedAddedConstraint = null;
							
							// SMART UPDATE CHECK
							if(!constraintChanged){
								constraintChanged = currentConstraint!=null;
							}
						} else if (args.get(arg) instanceof ClassInstanceCreation) {
							tmpAddedConstraintInstance = parseAllocatedConstraint((ClassInstanceCreation) args.get(arg));
							
							// SMART UPDATE CHECK
							if(!constraintChanged){
								if(	(currentConstraint!=null && tmpAddedConstraintInstance==null) ||
									(currentConstraint==null && tmpAddedConstraintInstance!=null))
										constraintChanged = true;
								else{
									// both current and new constraints are not null
									IJavaObjectInstance javaConstraint = (IJavaObjectInstance) currentConstraint;
									if (javaConstraint.isParseTreeAllocation() && tmpAddedConstraintInstance.isParseTreeAllocation()) {
										ParseTreeAllocation currentPTAlloc = (ParseTreeAllocation) javaConstraint.getAllocation();
										ParseTreeAllocation newPTAlloc = (ParseTreeAllocation) tmpAddedConstraintInstance.getAllocation();
										
										NaiveExpressionFlattener flattener = new NaiveExpressionFlattener();
										currentPTAlloc.getExpression().accept(flattener);
										String currentAlloc = flattener.getResult();
										flattener.reset();
										
										newPTAlloc.getExpression().accept(flattener);
										String newAlloc = flattener.getResult();
										flattener.reset();
										constraintChanged=!currentAlloc.equals(newAlloc);
										if(!constraintChanged && fbeanPart!=null){
											// problem with interchanging simple var name and class instance creation decl
											constraintChanged = fbeanPart.getModel().getABean(currentConstraint)!=null;
										}
									}else
										constraintChanged = true; // allocations of both are not PTAllocs - something changed
								}
							}
						}
				}
			}
			
			// Smart decoding - we would like to change the EMF model
			// only if anything has changed, else no point in changing
			if(addedPartChanged || constraintChanged){
				
				// Added part changed - disconnect previous one
				if(addedPartChanged){
					fAddedPart = newAddedPart;
					if (oldAddedPart != null)
						oldAddedPart.removeBackRef(fbeanPart, true);
				}
				
				// Clear all things in EMF
				clearPreviousIfNeeded();
				
				// Apply changes to the values
				fAddedConstraintInstance = tmpAddedConstraintInstance;
				fisAddedConstraintSet = tmpIsAddedConstraintSet;
				fAddedConstraint = tmpAddedConstraint;
				fnonResolvedAddedConstraint = tmpNonResolvedAddedConstraint;
				fisAddedConstraintSet = tmpIsAddedConstraintSet;
				fAddedIndex = tmpAddedIndex;
				indexValueFound = tmpIndexValueFound;
				if (tmpConstraintBeanPart != null) {
					tmpConstraintBeanPart.addBackRef(fbeanPart, (EReference) fFmapper.getFeature(null));
					fbeanPart.addChild(tmpConstraintBeanPart);
					fAddedConstraintInstance = (IJavaObjectInstance) tmpConstraintBeanPart.getEObject();
					fisAddedConstraintSet = fAddedConstraint != null;
					tmpConstraintBeanPart.addToJVEModel();
				}

				List references = fOwner.getExprRef().getReferences();
				if(fAddedInstance!=null && !references.contains(fAddedInstance))
					references.add(fAddedInstance);
				if(fAddedConstraintInstance!=null && !references.contains(fAddedConstraintInstance))
					references.add(fAddedConstraintInstance);


				try {
					EObject oldConstraint = (EObject) (fCC != null ? fCC.eGet(CodeGenUtil.getConstraintFeature(fCC)) : null);
	
					int index = -1;
					if (indexValueFound)
						//index = Integer.parseInt((CodeGenUtil.expressionToString(args[indexValuePosition])));
						index = Integer.parseInt(args.get(indexValuePosition).toString());
					EObject CC = getNewCC(fbeanPart.getModel().getCompositionModel());
					if (defaultConstraintFound) {
						fisAddedConstraintSet = true;
						// Add the constraint to the added part
						CodeGenUtil.addConstraintString(
							fbeanPart.getInitMethod().getCompMethod(),
							CC,
							fAddedConstraint,
							CodeGenUtil.getConstraintFeature(CC),
							fOwner.getCompositionModel());
					} else if (fAddedConstraintInstance != null) {
						CC.eSet(CodeGenUtil.getConstraintFeature(CC), fAddedConstraintInstance);
					}
					if (indexValueFound) {
						//fAddedIndex = CodeGenUtil.expressionToString(args[indexValuePosition]);
						fAddedIndex = args.get(indexValuePosition).toString();
					}
					// Now add it to the model
					if (fAddedPart != null)
						add(CC, fAddedPart, fbeanPart, index);
					else
						add(CC, fAddedInstance, fbeanPart, index, true);
					CodeGenUtil.propertyCleanup(oldConstraint);
				} catch (Exception e) {
					throw new CodeGenException(e);
				}
			}
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
			JavaVEPlugin.log("SimpleAttr.DecoderHelper.primRefreshFromComposition(): Error", Level.FINE); //$NON-NLS-1$
			return expSig;
		}
		// Get the latest constraint	
		EObject CC = getConstraintComponent(false);
		ICodeGenAdapter constraintAdapter = null;
		if (fAddedConstraintInstance != null) {
			constraintAdapter =
				(ICodeGenAdapter) EcoreUtil.getExistingAdapter(
					fAddedConstraintInstance,
					ICodeGenAdapter.JVE_CODEGEN_EXPRESSION_SOURCE_RANGE);
		}
		// It is possible that we are moving from No constraint to a new one, so create a source range adapter.
		if (constraintAdapter == null) {
			ExpressionDecoderAdapter a =
				(ExpressionDecoderAdapter) EcoreUtil.getExistingAdapter(CC, ICodeGenAdapter.JVE_CODEGEN_EXPRESSION_ADAPTER);
			constraintAdapter = a!=null ? a.getShadowSourceRangeAdapter() : null;
		}
		if (CC != null) {
			EStructuralFeature sf = CC.eClass().getEStructuralFeature("constraint"); //$NON-NLS-1$
			fAddedConstraintInstance = (IJavaObjectInstance) CC.eGet(sf);
			if (constraintAdapter != null && fAddedConstraintInstance != null)
				fAddedConstraintInstance.eAdapters().add(constraintAdapter);
			fisAddedConstraintSet = CC.eIsSet(sf);
		}
		updateAddedArguments();
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
	 *  Look for our added part, 
	 */
	public boolean primIsDeleted() {

		EObject CC = getConstraintComponent(false);
		if (CC == null) {
			if (fAddedPart != null && fbeanPart != null && fbeanPart.getEObject() != null)
				fAddedPart.removeBackRef(fbeanPart.getEObject(), false);
			return true;
		} else
			return false;
	}

	/**
	 *   Overidable test: Is this expression fits this decoder
	 */
	protected boolean isMySigniture() {
		return fFmapper.getFeature(fExpr).equals(CodeGenUtil.getComponentFeature(fbeanPart.getEObject()));
	}

	protected boolean isValid() throws CodeGenException {
		if (fFmapper.getFeature(fExpr) == null || fExpr == null) {
			CodeGenUtil.logParsingError(fExpr.toString(), fbeanPart.getInitMethod().getMethodName(), "Feature " + fFmapper.getMethodName() + " is not recognized.", false); //$NON-NLS-1$ //$NON-NLS-2$
			throw new CodeGenException("null Feature:" + fExpr); //$NON-NLS-1$
		}
		return isMySigniture();
	}
	/**
	 *   Go for it
	 */
	public boolean decode() throws CodeGenException {
		if (isValid())
			return addComponent();
		else
			return false;
	}
	public boolean restore() throws CodeGenException {	
		if (isValid()) {
			fAddedPart = parseAddedPart((MethodInvocation) ((ExpressionStatement)fExpr).getExpression());
			if (fAddedPart==null || fAddedInstance==null) {
				CodeGenUtil.logParsingError(fExpr.toString(), fbeanPart.getInitMethod().getMethodName(), "Could not resolve added component", false); //$NON-NLS-1$
				return false;
			}
			// update fCC
			getConstraintComponent(false);
			EStructuralFeature sf = fCC.eClass().getEStructuralFeature("constraint"); //$NON-NLS-1$
			fAddedConstraintInstance = (IJavaObjectInstance) fCC.eGet(sf);
			fisAddedConstraintSet = fCC.eIsSet(sf);
			fAddedPart.addBackRef(fbeanPart, (EReference) fFmapper.getFeature(null));			
			fAddedPart.addChild(fAddedPart);
			// TODO fAddedIndex is not processed at this time... need to also check the
			//      Smart decoding for the fAddedIndex
			
			// Update references so that they dont get deactivated at the end
			List references = fOwner.getExprRef().getReferences();
			if(fAddedInstance!=null && !references.contains(fAddedInstance))
				references.add(fAddedInstance);
			if(fAddedConstraintInstance!=null && !references.contains(fAddedConstraintInstance))
				references.add(fAddedConstraintInstance);
			
			return true;
		}
		else
			return false;
	}	

	public void removeFromModel() {
		unadaptToCompositionModel();

		IJavaObjectInstance parent = (IJavaObjectInstance) fbeanPart.getEObject();
		// refresh CC
		getConstraintComponent(false);

		// TODO  Consider setting a new constraint to child
		List parentsChildren = CodeGenUtil.getChildrenComponents(parent);
		if (fCC != null) {
			parentsChildren.remove(fCC);
			EObject o = CodeGenUtil.getCCcomponent(fCC);
			cleanProperty(o);

			o = CodeGenUtil.getCCconstraint(fCC);
			cleanProperty(o);
		}

		if (fAddedPart != null)
			fAddedPart.removeBackRef(parent, true);
	}

	//public Object getPriorityOfExpression(){
	//	Integer pri = (Integer)super.getPriorityOfExpression();
	//	int indexOfChildInParent = 0;
	//	if(fAddedInstance!=null || fAddedPart!=null){
	////		List ccs = CodeGenUtil.getChildrenComponents(fAddedPart.getContainer().getRefObject());
	//        // It is possible that the fAddedPart was taken to another container (with a later on
	//        // parsed add() for another container.. so this expression is somewhat stale.  But the
	//        // expression itself is releated to the BeanPart, not to the Added Part's container
	//		List ccs = fbeanPart!=null?CodeGenUtil.getChildrenComponents(fbeanPart.getEObject()):null;
	//		if (ccs != null) {
	//			EObject childInstance = null;
	//			if(fAddedPart!=null)
	//				childInstance = fAddedPart.getEObject();
	//			else
	//				childInstance = fAddedInstance;
	//			if(childInstance!=null){
	//			  for(int i=0;i<ccs.size();i++){
	//				EObject cc = (EObject)ccs.get(i);
	//				if("ConstraintComponent".equals(cc.eClass().getName())) //$NON-NLS-1$
	//					cc = CodeGenUtil.getCCcomponent(cc);
	//				if (cc == null) {
	//					JavaVEPlugin.log("ContainerAddDecoderHelper.getPriorityOfExpression(): null Component in a CC :"+this+org.eclipse.ve.internal.java.core.Level.FINE) ; //$NON-NLS-1$
	//					continue ;
	//				}
	//				if(cc.equals(childInstance)){
	//					indexOfChildInParent = i;
	//					break;
	//				}
	//			  }
	//		 	}
	//		}
	//	}
	//	indexOfChildInParent++;
	//	return new Integer(IJavaFeatureMapper.PRIORITY_ADD_CHANGE + pri.intValue() + (IJavaFeatureMapper.INTER_LAYOUT_ADD_PRIORITIES_GAP-indexOfChildInParent));}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractIndexedChildrenDecoderHelper#getIndexedEntries()
	 */
	protected List getIndexedEntries() {
		return getComponentsFromConstraintComponents(fbeanPart != null ? CodeGenUtil.getChildrenComponents(fbeanPart.getEObject()) : null);
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractIndexedChildrenDecoderHelper#getIndexedEntry()
	 */
	protected Object getIndexedEntry() {
		if (fAddedPart != null)
			return fAddedPart.getEObject();
		else
			return fAddedInstance;
	}

	/**
	 *  Detemine the source code argument for the add() method
	 */
	private String[] getSourceCodeArgs() {

		String AddedArg;
		if (fAddedPart == null) {
			// Simple property: add(new JLabel("Boo"),null)
			AddedArg = CodeGenUtil.getInitString(fAddedInstance,fbeanPart.getModel(), fOwner.getExprRef().getReqImports(), getExpressionReferences());
		} else if (
			fAddedPart.getInitMethod().equals(fbeanPart.getInitMethod())) // Added part is defined in the same method as the container
			AddedArg = fAddedPart.getSimpleName();
		else if (fAddedPart.getReturnedMethod() != null)
			AddedArg = fAddedPart.getReturnedMethod().getMethodName() + ExpressionTemplate.LPAREN + ExpressionTemplate.RPAREN;
		else // In the case a bean has an init method, instance var., but no return method.
			AddedArg = fAddedPart.getSimpleName();

		List finalArgs = new ArrayList();
		finalArgs.add(AddedArg);
		if (fAddedConstraint != null) {
			// Supplied constraint;
			finalArgs.add(fnonResolvedAddedConstraint);
		} else {
			// Generate a getName() by default
			if (!fisAddedConstraintSet) {
				if (ContainerDecoder.DEFAULT_CONSTRAINT != null) {
					String defArg = AddedArg + ContainerDecoder.DEFAULT_CONSTRAINT;
					finalArgs.add(defArg);
				}
			} else // Generate a true null constraint
				finalArgs.add("null"); //$NON-NLS-1$
		}
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
		String mtd = fFmapper.getMethodName();
		ExpressionTemplate exp = new ExpressionTemplate(sel, mtd, args, null, 0);
		exp.setLineSeperator(fbeanPart.getModel().getLineSeperator());
		return exp;
	}

	protected void updateAddedArguments() {

		fAddedConstraint = null;
		if (fAddedConstraintInstance != null) {
			// get the contstraints value
			BeanPart cbp = fbeanPart.getModel().getABean(fAddedConstraintInstance);
			if (cbp == null) // Vanilla constraint
				fnonResolvedAddedConstraint = fAddedConstraint = CodeGenUtil.getInitString(fAddedConstraintInstance,fbeanPart.getModel(), fOwner.getExprRef().getReqImports(), getExpressionReferences());
			else
				fnonResolvedAddedConstraint = fAddedConstraint = cbp.getSimpleName();
		}
		fAddedIndex = null;
		// If you dont want this variable indexValuefound, then there should be
		// some way of telling whether an index is necessary (like finding out the
		// the type of the layout).
		if (indexValueFound) {
			// Add an index position, as it was found.
			List parentsChildren = CodeGenUtil.getChildrenComponents(fbeanPart.getEObject());
			int indexOfAddedPart = parentsChildren.indexOf(fAddedPart.getEObject());
			fAddedIndex = Integer.toString(indexOfAddedPart);
		}
	}

	public String generate(Object[] args) throws CodeGenException {

		if (fFmapper.getFeature(null) == null)
			throw new CodeGenException("null Feature"); //$NON-NLS-1$
		if (args == null || args[0] == null)
			throw new CodeGenException("ContainerAddDecoderHelper.generate() : no Target"); //$NON-NLS-1$

		EObject CC = (EObject) args[0];
		fCC = CC;
		fAddedInstance = CodeGenUtil.getCCcomponent(CC);
		fAddedPart = fbeanPart.getModel().getABean(fAddedInstance);
		if (fAddedPart != null) {
			fbeanPart.addChild(fAddedPart);
			fAddedPart.addBackRef(fbeanPart, (EReference) fFmapper.getFeature(null));
		}

		EStructuralFeature sf = CC.eClass().getEStructuralFeature("constraint"); //$NON-NLS-1$
		fAddedConstraintInstance = (IJavaObjectInstance) CC.eGet(sf);
		fisAddedConstraintSet = CC.eIsSet(sf);

		updateAddedArguments();

		ExpressionTemplate exp = getExpressionTemplate();
		fExprSig = exp.toString();
		return fExprSig;
	}

	public boolean isImplicit(Object args[]) {
		return false;
	}

	public Object[] getArgsHandles(Statement expr) {
		Object[] result = null;
		try {
			if (fAddedPart == null && expr != null) {
				// Brand new expression
				BeanPart bp = parseAddedPart((MethodInvocation)getExpression());
				if (bp != null)
					result = new Object[] { bp.getType() + "[" + bp.getUniqueName() + "]" }; //$NON-NLS-1$ //$NON-NLS-2$
			} else if (fAddedPart != null) {
				return new Object[] { fAddedPart.getType() + "[" + fAddedPart.getUniqueName() + "]" }; //$NON-NLS-1$ //$NON-NLS-2$
			}
		} catch (CodeGenException e) {
		}
		return result;
	}

	/**
	 * Overide to include the constraint SF.
	 */
	public boolean isRelevantFeature(EStructuralFeature sf) {
		if (super.isRelevantFeature(sf))
			return true;
		if (sf != null && fCC != null) {
			EStructuralFeature hSF = fCC.eClass().getEStructuralFeature("constraint"); //$NON-NLS-1$
			if (sf.equals(hSF))
				return true;
			//		 || (((XMIResource)hSF.eResource()).getID(hSF) != null && ((XMIResource)hSF.eResource()).getID(hSF).((XMIResource)equals(sf.eResource()).getID(equals(sf)))) 		  
		}
		return false;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#getArgs()
	 */
	public Object[] getAddedInstance() {
		if (fAddedPart != null)
			return new Object[] { fAddedPart.getEObject(), fCC };
		else if (fAddedInstance != null)
			return new Object[] { fAddedInstance, fCC };

		return new Object[0];
	}
	
	public Object[] getReferencedInstances() {
		Collection result = CodeGenUtil.getReferences(fbeanPart.getEObject(),false);
		if (fAddedPart!=null) 

			result.addAll(CodeGenUtil.getReferences(fAddedPart.getEObject(),true));
		else 
			result.addAll (CodeGenUtil.getReferences(fAddedInstance,true));
		
			
		result.addAll (CodeGenUtil.getReferences(fAddedConstraintInstance,true));
		return result.toArray();
	}
	protected EObject getIndexParent() {
		return fbeanPart.getEObject();
	}
	
}
