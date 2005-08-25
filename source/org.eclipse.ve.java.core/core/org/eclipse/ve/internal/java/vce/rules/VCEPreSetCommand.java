/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.vce.rules;
/*
 *  $RCSfile: VCEPreSetCommand.java,v $
 *  $Revision: 1.15 $  $Date: 2005-08-25 16:07:00 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.*;

import org.eclipse.jem.internal.instantiation.ImplicitAllocation;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.*;

import org.eclipse.ve.internal.cdm.Annotation;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.commands.NoOpCommand;
import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.ClassDecoratorFeatureAccess;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.jcm.*;

import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;

/**
 * This is the default vce preSet command returned by the VCE Property Rule.
 * This should not be instantiated directly. The rule is the one who should
 * instantiate it. That is why the ctor is protected so that only the default
 * rule can instantiate it.
 * 
 * The following constraints on the target and value are invalid:
 * 
 *   1) Target is a JCMMethod and feature is not <members>. We don't know how to handle such situation.
 *   2) Target is immediately contained by a JCMMethod, but it is not the <members> or <properties> relationship. We
 *      don't know how to handle such situations (varient on (1)).
 *   3) Target is not immediately contained by a <properties> or <members> but somewhere above target the containment
 *      is by <properties>. This is invalid because if there were any settings on the top level <property>
 *      there should of been a preset call done on it and it would of been made a <member> instead.
 *   4) Target is not immediately contained by a JCMMethod but somewhere above target containment is
 *      a JCMMethod, but the containment is not <members>. This is invalid. This is a varient on (2) and (3).
 *   5) <properties> on BeanSubclassComposition are invalid. 
 */
public class VCEPreSetCommand extends CommandWrapper {
	
	/**
	 * Key in Annotation's KeyHolder for the BeanLocation override for a particular instance.
	 * The KeyValue entry will be a Keyed.
	 */
	public static final String BEAN_LOCATION_KEY = "org.eclipse.ve.java.BeanLocation";  //$NON-NLS-1$
	
	protected EditDomain domain;
	protected EObject target;
	protected EObject newValue;
	protected EReference feature;
	
	protected VCEPreSetCommand(EditDomain domain, EObject target, EObject newValue, EReference feature) {
		this.domain = domain;
		this.target = target;
		this.newValue = newValue;
		this.feature = feature;
	}
	
	protected boolean prepare() {
		// Need to override prepare because prepare expects to have a command
		// create, and at the time of prepare being called, we don't have a command yet.
		return target != null && domain != null;
	}
	
	public void execute() {
		// Are we contained eventually in a resource.
		// If not in a resource, then don't do anything because we
		// can't find out where things need to go in this case.
		// It is assumed that at a later time one of the containers
		// will have a preset done on it and that container will at
		// that time be in a resource.
		if (target.eResource() == null)
			command = NoOpCommand.INSTANCE;
		else {
			CommandBuilder cbld = new CommandBuilder();
			cbld.setExecuteAndAppend(true);	// So that changes are seen immediately as we process.
			
			// First thing we need to do is to get the target promoted if necessary.
			JCMMethod m = null;	// This is the method that newValue needs to go into (if it needs to).
			if (target instanceof BeanSubclassComposition)
				;	// Target doesn't need to change. Also there is no JCMMethod for newValue. It will put into BeanSubclassComposition itself.
			else if (target instanceof JCMMethod) {
				// This must be <members>, we don't allow others.
				// We need to put the initializes onto the method for this value since it is located in this method.
				// Just in case, moving from old initializes/return to new one for newValue since it is moving to this method.
				EObject oldInitialize = InverseMaintenanceAdapter.getFirstReferencedBy(newValue, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
				if (oldInitialize != null && oldInitialize != target)
					cbld.cancelAttributeSetting(oldInitialize, JCMPackage.eINSTANCE.getJCMMethod_Initializes(), newValue);
				EObject oldReturn = InverseMaintenanceAdapter.getFirstReferencedBy(newValue, JCMPackage.eINSTANCE.getJCMMethod_Return());
				if (oldReturn != null && oldReturn != target)
					cbld.cancelAttributeSetting(oldReturn, JCMPackage.eINSTANCE.getJCMMethod_Return(), newValue);
				// Assign initialize to this method, but there is no return. That is an indeterminate question.					
				cbld.applyAttributeSetting(target, JCMPackage.eINSTANCE.getJCMMethod_Initializes(), newValue);
				m = (JCMMethod) target;
			} else {
				// Pass member bldr same bldr as command bldr because we need target completely built before we go on				
				// This is because codegen uses assignment of membership to determine when to walk tree of the target to
				// handle all of its settings too. Since at this point the entire tree for the target has already been
				// built and assigned if there are any (since this would of been done in a previous command) or if there
				// were no settings, it is ok to apply the target to member right away.
				m = getMethod(cbld, cbld, target, null, m);
			}
			
			if (newValue != null)
				handleValue(cbld, m, newValue, feature, feature != null ? feature.isContainment() : false, new HashSet(10));	// If we don't have a feature, treat as not containment.
				
			if (!cbld.isEmpty())
				command = cbld.getCommand();
			else
				command = NoOpCommand.INSTANCE;	// Because of bug in CommandWrapper, we must have a command.

		}
	}

	protected JCMMethod getMethod(CommandBuilder cbld, CommandBuilder memberBldr, EObject value, EStructuralFeature feature, JCMMethod m) {
		if (value.eContainer() == null) {
			// We are not contained, we need to be in a method.
			// Need to promote this appropriately. If we don't have a method
			// to go into, then we need to go to global.
			// At this point, even if setting type is Property, we must go local because we are trying to create a method for it.			
			handleAnnotation(value, memberBldr);	// Need to handle annotation first.
			InstanceLocation settingType = settingType(value, feature);			
			if (m == null || settingType == InstanceLocation.GLOBAL_GLOBAL_LITERAL) {
				m = promoteGlobal(cbld, memberBldr, value);
			} else if(settingType == InstanceLocation.GLOBAL_LOCAL_LITERAL){
				m = promoteGlobalLocal(cbld,memberBldr,value,m);
			} else {
				m = promoteLocal(cbld, memberBldr, value, m);
			}	
		} else if (value.eContainmentFeature() == JCMPackage.eINSTANCE.getMemberContainer_Properties()) {
			// We are contained by a properties, we need to be in a method.
			// Need to promote this appropriately
			// At this point, even if setting type is Property, we must go local because we are trying to create a method for it.
			InstanceLocation settingType = settingType(value, feature);			
			if (settingType == InstanceLocation.GLOBAL_GLOBAL_LITERAL)
				m = promoteGlobal(cbld, memberBldr, value);
			else
				m = promoteLocal(cbld, memberBldr, value, (JCMMethod) value.eContainer());
		} else {
			// Find the method that eventually initializes top value (i.e. Keep going until we find one that does an initializes).
			// Walk up containment until we find a method, or we get to the top, but don't go beyond <members> containment. The
			// one that is at <members> should have an initialize method. Where we stop we need to create an initializes.
			m = findInitializesMethod(value,cbld);
		}
		return m;
	}

	protected JCMMethod findInitializesMethod(EObject value, CommandBuilder cbld){
		// Find the method that eventually initializes the value (i.e. Keep going until we find one that does an initializes).
		// Walk up containment until we find a method, or we get to the top, but don't go beyond <members> containment. The
		// one that is at <members> should have an initialize method. Where we stop we need to create an initializes.
		EStructuralFeature members = JCMPackage.eINSTANCE.getMemberContainer_Members();				
		EObject v = value;
		EObject oldV = null;
		JCMMethod initMethod = null;
		for (; v != null; oldV = v, v = v.eContainer()) {
			initMethod = (JCMMethod) InverseMaintenanceAdapter.getFirstReferencedBy(v, JCMPackage.eINSTANCE.getJCMMethod_Initializes());					
			if (initMethod != null)
				return initMethod;
			EStructuralFeature cFeature = v.eContainmentFeature();
			if (cFeature == members || v.eContainer() instanceof BeanSubclassComposition) {
				// This guy should of had an initialize, but didn't so we create one.
				initMethod = createInitMethod(cbld, v);
				return initMethod;
			}  
		}

		// We reached BeanSubclassComposition, so the top guy should of had an init method.
		initMethod = createInitMethod(cbld, oldV);	// Create a method to initialize the JavaBean	
		return initMethod;
	}

	/*
	 * Handle seeing if this is an implicit. If it is then on promotion
	 * we need to remove the allocation because it will now be the default allocation.
	 * TODO This will change when we trully handle implicits. Right now on promotion it is no longer implicit. It is a new guy.
	 */
	private void handlePromoteImplicit(CommandBuilder cbld, EObject member) {
		if (member instanceof IJavaInstance) {
			IJavaInstance javaInstance = (IJavaInstance) member;
			if (javaInstance.isSetAllocation()) {
				JavaAllocation alloc = javaInstance.getAllocation();
				if (alloc != null && alloc instanceof ImplicitAllocation) {
					cbld.cancelAttributeSetting(javaInstance, JavaInstantiation.getAllocationFeature(javaInstance));
				}
			}
		}
	}
	
	/*
	 * Promote to global, assumption is that not already contained by a non-<properties> relationship AND not already
	 * have initializes and return set for it.
	 */
	protected JCMMethod promoteGlobal(CommandBuilder cbld, CommandBuilder memberBldr, EObject member) {
		handlePromoteImplicit(cbld, member);
		JCMMethod m = createInitMethod(cbld, member);
		memberBldr.applyAttributeSetting(getComposition(), JCMPackage.eINSTANCE.getMemberContainer_Members(), member);		
		return m;
	}

	/*
	 * Promote to local, assumption is that not already contained by a non-<properties> relationship AND not already
	 * have initializes and return set for it.
	 */	
	protected JCMMethod promoteLocal(CommandBuilder cbld, CommandBuilder memberBldr, EObject member, JCMMethod method) {
		handlePromoteImplicit(cbld, member);
		cbld.applyAttributeSetting(method, JCMPackage.eINSTANCE.getJCMMethod_Initializes(), member);
		memberBldr.applyAttributeSetting(method, JCMPackage.eINSTANCE.getMemberContainer_Members(), member);
		return method;
	}

	/*
	 * Promote to local, assumption is that not already contained by a non-<properties> relationship AND not already
	 * have initializes and return set for it.
	 */	
	protected JCMMethod promoteGlobalLocal(CommandBuilder cbld, CommandBuilder memberBldr, EObject member, JCMMethod method) {
		handlePromoteImplicit(cbld, member);
		// The initializes method is from the argument
		cbld.applyAttributeSetting(method, JCMPackage.eINSTANCE.getJCMMethod_Initializes(), member);
		// The member is added to the composition
		memberBldr.applyAttributeSetting(getComposition(), JCMPackage.eINSTANCE.getMemberContainer_Members(), member);
		return method;
	}

	/**
	 * create the init method for this target. Overrides can return another method,
	 * e.g. "initializes" if desired. 
	 * 
	 * @return The init method. It must be all set up and added to the BeanSubclassComposition.
	 */
	protected JCMMethod createInitMethod(CommandBuilder cbld, EObject member) {
		JCMMethod m = JCMFactory.eINSTANCE.createJCMMethod();
		m.getInitializes().add(member);
		// Should this also be a return method. Look up on BeanDecorator.
		BeanDecorator bd = (BeanDecorator) ClassDecoratorFeatureAccess.getDecoratorWithFeature(member.eClass(), BeanDecorator.class, JCMPackage.eINSTANCE.getBeanDecorator_BeanReturn());
		if(bd == null || bd.isBeanReturn()) {
			// Either no decorator found set (so default is true) or specifically set to true.
			m.setReturn(member);
		}
		cbld.applyAttributeSetting(getComposition(), JCMPackage.eINSTANCE.getBeanSubclassComposition_Methods(), m);
		return m;
	}

	
	/*
	 * Return the BeanSubClassComposition for the target.
	 */
	protected BeanSubclassComposition getComposition() {
		return (BeanSubclassComposition) domain.getDiagramData();
	}
		
	/**
	 * Answer the promotion type. It will be based upon the property and the feature that property
	 * is being set into. It will first see if there is annotation on the property, and the annotation
	 * answers where to put it. If it doesn't then it will see if the property determines the promotion type, 
	 * if it does it will use that. Next it will see if the properties type (or a super class of it) answers
	 * the location. Finally if none of these are set it will answer GLOBAL_GLOBAL.
	 *  
	 * @param property 
	 * @param feature
	 * @return the location.
	 * 
	 * @since 1.0.0
	 */
	protected InstanceLocation settingType(EObject property, EStructuralFeature feature) {
		// First see if the property has an annotation with the location set.
		Annotation annotation = domain.getAnnotationLinkagePolicy().getAnnotation(property);
		if (annotation != null) {
			InstanceLocation il = (InstanceLocation) annotation.getKeyedValues().get(BEAN_LOCATION_KEY);
			if (il != null)
				return il;
		}
		
		// Next check if the feature has it set.
		// We may not have a feature if we are the target of the entire command (and not the value being set).
		BeanFeatureDecorator bfd = feature != null ? (BeanFeatureDecorator) CDEUtilities.findDecorator(feature, BeanFeatureDecorator.class) : null;
		if (bfd != null && bfd.isSetBeanLocation())
			return bfd.getBeanLocation();
		
		// Next check the bean class.
		BeanDecorator bd = (BeanDecorator) ClassDecoratorFeatureAccess.getDecoratorWithFeature(property.eClass(), BeanDecorator.class, JCMPackage.eINSTANCE.getBeanDecorator_BeanLocation());
		if (bd != null)
			return bd.getBeanLocation();
		
		// Default to Property.
		return InstanceLocation.PROPERTY_LITERAL;
	}
	
	protected void handleValue(final CommandBuilder cbld, final JCMMethod incomingMethod, final EObject value, EStructuralFeature feature, final boolean containment, final Set processed) {
		processed.add(value);
		
		// We will only walk into and handle non-IJavaInstance values that are contained.
		// This is because the purpose of VCEPreset is to find the place to put Java instances.
		// Non-java stuff needs to be anchored seperately. If this is a contained non-Java instance,
		// then we need to walk into it so that we could pick up any possible java instances that
		// are settings on it.
		
		if (!(value instanceof IJavaInstance) && !containment)
			return;  
				
		// Now walk through the children to have them also made into properties, if not already.
		// If a child is found, then get the method that the child should be in. This is only
		// done once, all children will then be in that method.
		
		// In this case, the objects need to have all of their settings membership handled before the membership
		// of value can be assigned. (This is because upon assignment the codegen walks the children to create
		// the set statements, and at that point in time they must already have their membership assigned). So we
		// will create the method first, assign membership of all settings, then assign membership of value can be
		// done. That is what mBldr is for. It contains the membership assignment (if any) of value and holds it
		// to be applied at the end.
		// Visit all of the set references. Here is the visitor we will use.
		final EReference allocationFeature = value instanceof IJavaInstance ? JavaInstantiation.getAllocationFeature((IJavaInstance) value) : null;
		class FeatureVisitor implements FeatureValueProvider.Visitor {
			public boolean hadChildren;		// During walking children, did it have children.
			public CommandBuilder mBldr;
			public JCMMethod visitMethod = incomingMethod;
			
			public Object isSet(EStructuralFeature feature, Object featureValue) {
				if (feature instanceof EReference) {
					EReference ref = (EReference) feature;
					if (ref.isChangeable()) {
						if (ref.isMany()) {
							Iterator kids = ((List) featureValue).iterator();
							while (kids.hasNext()) {
								Object kid = kids.next();
								if (!hadChildren) {
									if (!containment) {
										mBldr = new CommandBuilder();
										visitMethod = getMethod(cbld, mBldr, value, ref, visitMethod);
									}
									hadChildren = true;
								}
								if (kid != null && !processed.contains(kid))
									handleValue(cbld, visitMethod, (EObject) kid, ref, ref.isContainment(), processed);
							}
						} else {
							// Don't want to process the allocation feature. That would not have any java instances in it
							// and we don't want to force promotion of this value just for it.					
							if (ref != allocationFeature) {
								Object kid = featureValue;
								if (!hadChildren) {
									if (!containment) {
										mBldr = new CommandBuilder();
										visitMethod = getMethod(cbld, mBldr, value, ref, visitMethod);
									}
									hadChildren = true;
								}
								if (kid != null && !processed.contains(kid))
									handleValue(cbld, visitMethod, (EObject) kid, ref, ref.isContainment(), processed);
							}
						}
					}
				}
				return null;
			}
		
		};

		FeatureVisitor visitor = new FeatureVisitor();
		FeatureValueProvider.FeatureValueProviderHelper.visitSetFeatures(value, visitor);
		
		if (!visitor.hadChildren && !containment && value.eContainer() == null) {
			InstanceLocation promoteType = incomingMethod != null ? settingType(value, feature) : InstanceLocation.GLOBAL_GLOBAL_LITERAL;	// no current JCMMethod, then can only be global. 
			// If here, then we don't have any settings, so we can use the same builder for both promotion and membership.
			handleAnnotation(value, cbld);	// Need to handle annotation first.			
			switch (promoteType.getValue()) {
				case InstanceLocation.GLOBAL_GLOBAL:
					promoteGlobal(cbld, cbld, value);
					break;
				case InstanceLocation.LOCAL:
					promoteLocal(cbld, cbld, value, incomingMethod);
					break;
				case InstanceLocation.GLOBAL_LOCAL:
					promoteGlobalLocal(cbld, cbld ,value,incomingMethod);
					break;
				case InstanceLocation.PROPERTY:
					// Make sure it is a <properties> of the requested member container.
					cbld.applyAttributeSetting(incomingMethod, JCMPackage.eINSTANCE.getMemberContainer_Properties(), value);
					break;
			}				
		} else if (containment)
			handleAnnotation(value, cbld);	// Need to handle annotation before actual setting is done.
		else if (visitor.mBldr != null)
			cbld.append(visitor.mBldr.getCommand());	// We had an mbldr created, append the command, if any so now we can assign membership.
	}
	
	/*
	 * Called to handle annotation on this child. It just accumulates
	 * them. Children of children will be handled individually.
	 */
	protected void handleAnnotation(EObject child, CommandBuilder cbld) {
		Annotation annotation = domain.getAnnotationLinkagePolicy().getAnnotation(child);
		if (annotation != null && annotation.eContainmentFeature() == null) {
			cbld.append(AnnotationPolicy.getDefaultAddAnnotationsCommand(Collections.singletonList(annotation), domain));
		}
	}	
	
	
}
