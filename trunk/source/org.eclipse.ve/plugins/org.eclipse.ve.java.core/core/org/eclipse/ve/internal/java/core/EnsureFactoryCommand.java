/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: EnsureFactoryCommand.java,v $
 *  $Revision: 1.2 $  $Date: 2006-02-09 15:03:05 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.util.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaRefFactory;

import org.eclipse.ve.internal.cdm.DiagramData;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.jcm.BeanComposition;
import org.eclipse.ve.internal.jcm.JCMPackage;

import org.eclipse.ve.internal.java.core.FactoryCreationData.MethodData;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;
 


/**
 * Command to process java allocation to ensure that there is a factory available for it, adding one if necessary. In this case it will find the
 * beaninfo for the factory-class, and if it is a factory, it will try to change to the proper factory. The allocation must be of the form 
 * "{factory:factoryclassname}.method(...)" or else it will not be recognized as a factory. 
 * It can handle multiple factory references in the allocation, but they have to be all of the same type because it will assign them all to the same factory.
 * This allows other references to the factory within the argments to the method invocation.  
 * <p>Using the criteria:
 * <ol>
 * <li>Use the {@link FactoryCreationData.MethodData#isStatic()} to just use the class as a static reference. The method data comes from the method that is being called in the allocation.
 * <li>Use the {@link FactoryCreationData#isShared()} to determine if it should share with parent or always use a new one. If not shared,
 * then always create a new one.
 * <li>See if parent is also a factory invocation of the same type. If it is, use the parent's factory.
 * For determining if the parent has a factory it
 * checks to see if the parent is a method invocation, and the receiver is instance ref to an object that is a class of the same type as the
 * factory classname passed in.
 * <li>Find valid factorys of the given type. A valid factory is one that is in the same member container as the parent, or any global ones.
 * <li>If only one factory is valid, then use that one. If more than one, then at drop time ask the client which one to use.
 * <li>If none, then create one.
 * </ol>
 * @since 1.2.0
 */
public class EnsureFactoryCommand extends CommandWrapper {
	
	public static final String FACTORY_PREFIX_FLAG = "{factory:"; //$NON-NLS-1$
	private final IJavaInstance child;
	private final IJavaInstance parent;
	private final EditDomain ed;	

	public EnsureFactoryCommand(IJavaInstance child, IJavaInstance parent, EditDomain ed) {
		super();
		this.child = child;
		this.parent = parent;
		this.ed = ed;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper#prepare()
	 */
	protected boolean prepare() {
		return child != null && child.isParseTreeAllocation() && ((ParseTreeAllocation)  child.getAllocation()).getExpression() instanceof PTMethodInvocation;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper#execute()
	 */
	public void execute() {
		ParseTreeAllocation allocation = (ParseTreeAllocation) child.getAllocation();
		PTMethodInvocation cmi = (PTMethodInvocation) allocation.getExpression();
		if (cmi.getReceiver() instanceof PTName) {
			PTName cmiRecv = (PTName) cmi.getReceiver();
			final String flagName = cmiRecv.getName();
			if (flagName != null && flagName.startsWith(FACTORY_PREFIX_FLAG)) {
				// It is not a reg. builder (i.e. it is a forward undo) because we need to always have the setAllocation be last in the stack
				// because it is the setAllocation that all listeners are listening for to know there is an allocation change.
				final RuledCommandBuilder cbld = new RuledCommandBuilder(ed, false);	 
				final String classname = flagName.substring(FACTORY_PREFIX_FLAG.length(), flagName.length()-1);	// The classname, e.g. "{factory:classname}"
				JavaHelpers factoryType = JavaRefFactory.eINSTANCE.reflectType(classname, EMFEditDomainHelper.getResourceSet(ed));
				FactoryCreationData factoryData = FactoryCreationData.getCreationData(factoryType);
				if (factoryData == null)
					return; // Not a real factory. Let it fail with invalid syntax.
				MethodData fmd = factoryData.getMethodData(cmi.getName());
				// If there is no factory data, let it go through as an undefined factory method. This means that we don't know if is static or not or what the properties are.
				// We will treat it as non-static, non-Freeform factory method.
				final boolean staticFactory = fmd != null && fmd.isStatic();
				IJavaInstance factory = null;
				if (!staticFactory) {
					if (factoryData.isShared()) {
						JavaAllocation parentAlloc = parent.getAllocation();
						while (true) {
							if (parentAlloc instanceof ParseTreeAllocation) {
								PTExpression paexp = ((ParseTreeAllocation) parentAlloc).getExpression();
								if (paexp instanceof PTMethodInvocation) {
									PTMethodInvocation pmi = (PTMethodInvocation) paexp;
									if (pmi.getReceiver() instanceof PTInstanceReference) {
										PTInstanceReference factoryRef = (PTInstanceReference) pmi.getReceiver();
										IJavaInstance parentFactory = factoryRef.getReference();
										if (factoryType.isInstance(parentFactory)) {
											factory = parentFactory; // Got it. Use it.
										}
									}
								}
								break;
							} else if (parentAlloc instanceof ImplicitAllocation) {
								// Parent was an implicit, see if the who it is from has the factory to use.
								ImplicitAllocation impAlloc = (ImplicitAllocation) parentAlloc;
								EObject impParent = impAlloc.getParent();
								if (impParent instanceof IJavaInstance)
									parentAlloc = ((IJavaInstance) impParent).getAllocation();
								else
									break;
							} else
								break;
						}						
						if (factory == null) {
							// Not found for parent. Look for others.
							List validFactories = new ArrayList();
							// Find only global factories for now.
							// TODO Need to also try to handle those in same method as parent, but we have a problem of ordering (not
							// sure codegen would move to correct spot in method to prevent problems, need to test that)
							// or if the child became a GLOBAL_GLOBAL
							// it wouldn't have access to the toolkit. Would need codegen smart enough to create an init method that would
							// pass in everything that the child create method would need, such as parent and toolkit.
							DiagramData freeform = ed.getDiagramData();
							if (freeform instanceof BeanComposition) {
								List components = ((BeanComposition) freeform).getComponents();
								for (Iterator comps = components.iterator(); comps.hasNext();) {
									IJavaInstance comp = (IJavaInstance) comps.next();
									if (factoryType.isInstance(comp))
										validFactories.add(comp);
								}
							}
							if (!validFactories.isEmpty()) {
								// Popup a dialog.
								Shell s = ed.getEditorPart().getSite().getShell();
								FactorySelectorDialog fsd = new FactorySelectorDialog(s, ed);
								fsd.setInput(validFactories);
								int r = fsd.open();
								// If canceled, then create new one.
								if (r != Dialog.CANCEL)
									factory = (IJavaInstance) fsd.getSelectedFactory(); 
							}
						}
					}
					
					if (factory == null) {
						// One was not found, or it is not shared, so create one.
						String initString = factoryData.getInitString();
						JavaAllocation alloc;
						if (initString == null) {
							// Use default ctor.
							alloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation(InstantiationFactory.eINSTANCE.createPTClassInstanceCreation(classname, null));
						} else {
							alloc = BeanPropertyDescriptorAdapter.createAllocation(initString);
						}
						factory = BeanUtilities.createJavaObject(factoryType, EMFEditDomainHelper.getResourceSet(ed), alloc);
						// TODO If the child is GLOBAL_GLOBAL and factory is not, then codegen will fail because it does not
						// know how to generate code into the child's global init method BEFORE the construction of the child
						// itself.
						// What is needed instead is a way to assign it relative to child, but we don't know where child will be
						// until too late. For now we hardcoded in VCEPreSetCommand to put unassigned instances found in an allocation
						// into GLOBAL_GLOBAL.
						if (factoryData.isOnFreeform()) {
							// Need to force to freeform.
							BeanComposition bc = (BeanComposition) ed.getDiagramData();
							cbld.applyAttributeSetting(bc, JCMPackage.eINSTANCE.getBeanComposition_Components(), factory);
						}
					}
				}
				
				// Now go through the allocation replacing the flag's (flagName in a PTName) with the appropriate factory reference.
				final IJavaInstance factoryInstance = factory;
				cbld.setApplyRules(false);	// Don't apply the rules at this time. We are just building everything up. When the child get's added
				// to the parent the rules will applied then, so less walking of the containments.
				cmi.accept(new ParseVisitor() {
					public boolean visit(PTName node) {
						if (flagName.equals(node.getName())) {
							// Found a reference to the factory.
							if (staticFactory) {
								replaceWithStaticFactory(node, classname, cbld);
							} else {
								replaceWithFactory(node, factoryInstance, cbld);
							}
						}
						return true;
					}
				
				});
				if (!cbld.isEmpty()) {
					// We have something so we need to now reapply allocation to make sure the change is seen by others since they do not listen on
					// changes within allocation.
					cbld.applyAttributeSetting(child, allocation.eContainmentFeature(), allocation);
					command = cbld.getCommand();
					command.execute();
				}
			}

		}
		

	}
	
	private void replaceWithFactory(PTName node, IJavaInstance factory, RuledCommandBuilder cbld) {
		// Replace entire node with an instance ref node to the factory.
		PTInstanceReference tr = InstantiationFactory.eINSTANCE.createPTInstanceReference(factory);
		EReference reference = node.eContainmentFeature();
		EObject container = node.eContainer();
		if (reference.isMany())
			cbld.replaceAttributeSetting(container, reference, tr, ((List) container.eGet(reference)).indexOf(node));
		else
			cbld.applyAttributeSetting(container, reference, tr);
	}
	
	private void replaceWithStaticFactory(PTName node, String classname, RuledCommandBuilder cbld) {
		// Replace factory flag with static reference to the class.
		cbld.applyAttributeSetting(node, InstantiationPackage.eINSTANCE.getPTName_Name(), classname);	
	}

}
