/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
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
 *  $Revision: 1.23 $  $Date: 2006-05-18 23:13:49 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jem.beaninfo.common.IBaseBeanInfoConstants;
import org.eclipse.jem.internal.beaninfo.common.FeatureAttributeValue;
import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.java.JavaHelpers;

import org.eclipse.ve.internal.cdm.Annotation;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.commands.NoOpCommand;
import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.ClassDecoratorFeatureAccess;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.jcm.*;

import org.eclipse.ve.internal.java.core.BeanUtilities;

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
			final CommandBuilder cbld = new CommandBuilder();
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
				EStructuralFeature targetContainment = target.eContainmentFeature();
				m = getMethod(cbld, target, null, m);
				if (targetContainment != target.eContainmentFeature()) {
					// Containment has changed. This would be due to promotion. Because this occured outside the
					// setting of a property, we need to touch all references to the target so that codegen
					// knows its membership has changed. Codegen listens for property settings before processing,
					// not on membership changes.
					// We only need to do this on target. Membership changes of any property setting of the value
					// being set in this preset command will be handled correctly since they are property settings
					// and will be heard by codegen.
					InverseMaintenanceAdapter ai = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(target, InverseMaintenanceAdapter.ADAPTER_KEY);
					ai.visitAllReferences(new InverseMaintenanceAdapter.Visitor() {
					
						public Object visit(EStructuralFeature feature, EObject reference) {
							// Setting of member containers features are not of interest to codegen, so don't bother touching those.
							if (!(reference instanceof MemberContainer))
								cbld.applyAttributeSetting(reference, feature, target);
							return null;
						}
					
					});
				}
			}
			
			if (newValue != null)
				handleValue(cbld, m, newValue, feature, feature != null ? feature.isContainment() : false, new HashSet(10));	// If we don't have a feature, treat as not containment.
				
			if (!cbld.isEmpty())
				command = cbld.getCommand();
			else
				command = NoOpCommand.INSTANCE;	// Because of bug in CommandWrapper, we must have a command.

		}
	}

	protected JCMMethod getMethod(CommandBuilder cbld, EObject value, EStructuralFeature feature, JCMMethod m) {
		if (value.eContainer() == null) {
			// We are not contained, we need to be in a method.
			// Need to promote this appropriately. If we don't have a method
			// to go into, then we need to go to global.
			// At this point, even if setting type is Property, we must go local because we are trying to create a method for it.			
			handleAnnotation(value, cbld);	// Need to handle annotation first.
			InstanceLocation settingType = settingType(value, feature);			
			if (m == null || settingType == InstanceLocation.GLOBAL_GLOBAL_LITERAL) {
				m = promoteGlobal(cbld, value);
			} else if(settingType == InstanceLocation.GLOBAL_LOCAL_LITERAL){
				m = promoteGlobalLocal(cbld,value,m);
			} else {
				m = promoteLocal(cbld, value, m);
			}	
		} else if (value.eContainmentFeature() == JCMPackage.eINSTANCE.getMemberContainer_Properties()) {
			// We are contained by a properties, we need to be in a method.
			// Need to promote this appropriately
			// At this point, even if setting type is Property, we must go local because we are trying to create a method for it.
			InstanceLocation settingType = settingType(value, feature);			
			if (settingType == InstanceLocation.GLOBAL_GLOBAL_LITERAL)
				m = promoteGlobal(cbld, value);
			else
				m = promoteLocal(cbld, value, (JCMMethod) value.eContainer());
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
		EStructuralFeature implicits = JCMPackage.eINSTANCE.getMemberContainer_Implicits();	
		EObject v = value;
		EObject oldV = null;
		JCMMethod initMethod = null;
		for (; v != null; oldV = v, v = v.eContainer()) {
			initMethod = (JCMMethod) InverseMaintenanceAdapter.getFirstReferencedBy(v, JCMPackage.eINSTANCE.getJCMMethod_Initializes());					
			if (initMethod != null)
				return initMethod;
			EStructuralFeature cFeature = v.eContainmentFeature();
			if (cFeature == implicits) {
				// This is an implicit, so this container is the method to use.
				return (JCMMethod) v.eContainer();
			}
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
	 * Promote to global, assumption is that not already contained by a non-<properties> relationship AND not already
	 * have initializes and return set for it.
	 */
	protected JCMMethod promoteGlobal(CommandBuilder cbld, EObject member) {
		JCMMethod m = createInitMethod(cbld, member);
		cbld.applyAttributeSetting(getComposition(), JCMPackage.eINSTANCE.getMemberContainer_Members(), member);		
		return m;
	}

	/*
	 * Promote to local, assumption is that not already contained by a non-<properties> relationship AND not already
	 * have initializes and return set for it.
	 */	
	protected JCMMethod promoteLocal(CommandBuilder cbld, EObject member, JCMMethod method) {
		cbld.applyAttributeSetting(method, JCMPackage.eINSTANCE.getJCMMethod_Initializes(), member);
		cbld.applyAttributeSetting(method, JCMPackage.eINSTANCE.getMemberContainer_Members(), member);
		return method;
	}

	/*
	 * Promote to local, assumption is that not already contained by a non-<properties> relationship AND not already
	 * have initializes and return set for it.
	 */	
	protected JCMMethod promoteGlobalLocal(CommandBuilder cbld, EObject member, JCMMethod method) {
		// The initializes method is from the argument
		cbld.applyAttributeSetting(method, JCMPackage.eINSTANCE.getJCMMethod_Initializes(), member);
		// The member is added to the composition
		cbld.applyAttributeSetting(getComposition(), JCMPackage.eINSTANCE.getMemberContainer_Members(), member);
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
			try {
				InstanceLocation il = (InstanceLocation) annotation.getKeyedValues().get(BEAN_LOCATION_KEY);
				if (il != null)
					return il;
			} catch (ClassCastException e) {
				// In case incorrectly declared key type.
			}			
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
		
		// Default to Property. Except if it is the InstanceRef feature. In that case, it should be local_local if not already determined by bean type.
		// TODO Currently Codegen can't handle creating a local reference to a constructor instance ref if the 
		// init method is for the object that this constructor is for. It doesn't know how to put it into
		// the as of yet non-generated init method BEFORE the constructor stmt. So for now these will become GLOBAL_GLOBAL.
		return InstantiationPackage.eINSTANCE.getPTInstanceReference_Reference() != feature ? InstanceLocation.PROPERTY_LITERAL : InstanceLocation.GLOBAL_GLOBAL_LITERAL;
	}
	
	protected void handleValue(final CommandBuilder cbld, final JCMMethod incomingMethod, final EObject value, final EStructuralFeature feature, final boolean containment, final Set processed) {
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
		
		final boolean isImplicit = value instanceof IJavaInstance && ((IJavaInstance) value).isImplicitAllocation();
		IJavaObjectInstance implicitParent = null;
		EStructuralFeature implicitRef = null;
		if (isImplicit) {
			// We need to process implicit because parent may not yet be assigned.
			// Need to assign parent so that the implicit can get a location when it needs to.
			ImplicitAllocation alloc = (ImplicitAllocation) ((IJavaInstance) value).getAllocation();
			implicitParent = (IJavaObjectInstance) alloc.getParent();
			implicitRef = alloc.getFeature();
			if (implicitParent.eContainer() == null) {
				handleValue(cbld, incomingMethod, implicitParent, null, false, processed);
			}
		}
		
		final IJavaObjectInstance finalImplicitParent = implicitParent;
		final EStructuralFeature finalImplicitRef = implicitRef;
		
		class FeatureVisitor implements FeatureValueProvider.Visitor {
			public boolean hadChildren;		// During walking children, did it have children.
			public JCMMethod visitMethod = incomingMethod;
			
			public Object isSet(EStructuralFeature setFeature, Object featureValue) {
				if (setFeature instanceof EReference) {
					EReference ref = (EReference) setFeature;
					if (ref.isChangeable()) {
						if (ref.isMany()) {
							Iterator kids = ((List) featureValue).iterator();
							while (kids.hasNext()) {
								Object kid = kids.next();
								if (!hadChildren) {
									if (!containment) {
										if (!isImplicit) {
											visitMethod = getMethod(cbld, value, feature, visitMethod);
										} else {
											visitMethod = handleImplicitMembership(cbld, incomingMethod, (IJavaInstance) value, finalImplicitParent, finalImplicitRef);											
										}
									}
									hadChildren = true;
								}
								if (kid != null && !processed.contains(kid))
									handleValue(cbld, visitMethod, (EObject) kid, ref, ref.isContainment(), processed);
							}
						} else {
							if (ref != allocationFeature) {
								Object kid = featureValue;
								if (!hadChildren) {
									if (!containment) {
										if (!isImplicit) {
											visitMethod = getMethod(cbld, value, feature, visitMethod);
										} else {
											visitMethod = handleImplicitMembership(cbld, incomingMethod, (IJavaInstance) value, finalImplicitParent, finalImplicitRef);																						
										}
									}
									hadChildren = true;
								}
								if (kid != null && !processed.contains(kid))
									handleValue(cbld, visitMethod, (EObject) kid, ref, ref.isContainment(), processed);
							} else {
								// If allocation feature then we want to walk the allocation and handle PTInstanceRef's. These may be instances
								// that don't yet have membership assigned.
								if (featureValue instanceof ParseTreeAllocation) {
									((ParseTreeAllocation) featureValue).getExpression().accept(new ParseVisitor() {
										public boolean visit(PTInstanceReference node) {
											// We are walking into this to handle possibly not-contained parent ref. If parent ref is contained, we won't walk into it.
											if (node.getReference().eContainer() != null)
												return true;
											if (!hadChildren) {
												if (!containment) {
													if (!isImplicit) {
														visitMethod = getMethod(cbld, value, feature, visitMethod);
													} else {
														visitMethod = handleImplicitMembership(cbld, incomingMethod, (IJavaInstance) value, finalImplicitParent, finalImplicitRef);																						
													}
												}
												hadChildren = true;												
											}
											handleValue(cbld, visitMethod, node.getReference(), InstantiationPackage.eINSTANCE.getPTInstanceReference_Reference(), false, processed);
											return true;
										}
									
									});
								}
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
			if (!isImplicit) {
				InstanceLocation promoteType = incomingMethod != null ? settingType(value, feature) : InstanceLocation.GLOBAL_GLOBAL_LITERAL; // no current JCMMethod, then can only be global. 
				// If here, then we don't have any settings.
				handleAnnotation(value, cbld); // Need to handle annotation first.			
				switch (promoteType.getValue()) {
					case InstanceLocation.GLOBAL_GLOBAL:
						promoteGlobal(cbld, value);
						break;
					case InstanceLocation.LOCAL:
						promoteLocal(cbld, value, incomingMethod);
						break;
					case InstanceLocation.GLOBAL_LOCAL:
						promoteGlobalLocal(cbld, value, incomingMethod);
						break;
					case InstanceLocation.PROPERTY:
						// Make sure it is a <properties> of the requested member container.
						cbld.applyAttributeSetting(incomingMethod, JCMPackage.eINSTANCE.getMemberContainer_Properties(), value);
						break;
				} 
			} else {
				// Implicit with no children (properties) will be implicit in the method of the implicit parent.
				handleImplicitMembership(cbld, incomingMethod, (IJavaInstance) value, implicitParent, implicitRef);
			}
		} else if (containment)
			handleAnnotation(value, cbld);	// Need to handle annotation before actual setting is done.
	}

	/*
	 * Handle the implicit membership. Assign if not already assigned.
	 */
	private JCMMethod handleImplicitMembership(CommandBuilder cbld, JCMMethod incomingMethod, IJavaInstance value, IJavaObjectInstance implicitParent, EStructuralFeature parentRefToValue) {
		JCMMethod implicitMethod = null;
		if (value.eContainer() == null) {
			// TODO For now implicits will always be in implicit. Future we could have a settting that says over "n" properties means
			// move from implicit to membership.
			handleAnnotation(value, cbld); // Need to handle annotation first.
			
			// KLUDGE For now codegen really doesn't handle implicits except for those special required ones. It especially can't handle it if
			// the actual value is different than the return value (i.e. casting needed). Right now only handles the SWT "viewers" required
			// implicits. When we really get implicits working we can pull out the kludge of creating a new setting.
			if (isRequiredImplicitFeature(implicitParent.getJavaType(), parentRefToValue)) {
				// See if we have a promotion default. If we do, then use that instead of "implicits" membership.
				InstanceLocation settingType = settingType(value, parentRefToValue);			
				if (settingType == InstanceLocation.GLOBAL_GLOBAL_LITERAL) {
					implicitMethod = promoteGlobal(cbld, value);
				} else if(settingType == InstanceLocation.GLOBAL_LOCAL_LITERAL){
					implicitMethod = promoteGlobalLocal(cbld,value,incomingMethod);
				} else if (settingType == InstanceLocation.LOCAL_LITERAL) {
					implicitMethod = promoteLocal(cbld, value, incomingMethod);
				} else {
					// It wasn't asked to go any where special, so put it in implicits.
					cbld.applyAttributeSetting(implicitMethod = getMethod(cbld, implicitParent, null, incomingMethod), JCMPackage.eINSTANCE.getMemberContainer_Implicits(),
						value);
					cbld.applyAttributeSetting(implicitMethod, JCMPackage.eINSTANCE.getJCMMethod_Initializes(), value);
				}
			} else {
				// KLUDGE the real kludge is we assign to membership and change allocation to default ctor. Note this may fail but this is
				// what we always did in the past.
				cbld.cancelAttributeSetting(value, JavaInstantiation.getAllocationFeature(value));
				promoteLocal(cbld, value, implicitMethod = getMethod(cbld, implicitParent, null, incomingMethod));
			}
		} else if (value.eContainer() instanceof JCMMethod){
			implicitMethod = (JCMMethod) value.eContainer();
		} else
			implicitMethod = findInitializesMethod(value, cbld);
		return implicitMethod;
	}
	
	private boolean isRequiredImplicitFeature(JavaHelpers type, EStructuralFeature feature) {
		FeatureAttributeValue val = BeanUtilities.getSetBeanDecoratorFeatureAttributeValue(type,
				IBaseBeanInfoConstants.REQUIRED_IMPLICIT_PROPERTIES);
		if (val != null) { 
			Object fval = val.getValue();
			if (fval instanceof String)
				return ((String) fval).equals(feature.getName());
			else if (fval instanceof String[]) {
				String[] fvals = (String[]) fval;
				for (int i = 0; i < fvals.length; i++) {
					if (fvals[i].equals(feature.getName()))
						return true;
				}
				return false;
			}
		}
		return false;
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
