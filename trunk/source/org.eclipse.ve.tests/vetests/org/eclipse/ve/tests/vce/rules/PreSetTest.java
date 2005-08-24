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
 *  $RCSfile: PreSetTest.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:54:15 $ 
 */
package org.eclipse.ve.tests.vce.rules;

import junit.framework.TestCase;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.gef.commands.Command;

import org.eclipse.ve.internal.cdm.Diagram;
import org.eclipse.ve.internal.cdm.DiagramData;

import org.eclipse.ve.internal.cde.core.AnnotationPolicy;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFAnnotationLinkagePolicy;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.jcm.*;
import org.eclipse.ve.internal.jcm.impl.KeyedInstanceLocationImpl;

import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.vce.rules.VCEPreSetCommand;
import org.eclipse.ve.internal.java.vce.rules.VCERuleRegistry;
 

/**
 * Test VCEPreset command
 * @since 1.0.0
 */
public class PreSetTest extends TestCase {
	
	public PreSetTest(String name) {
		super(name);
	}
	
	ResourceSet rset = new ResourceSetImpl();
	EditDomain domain;
	BeanSubclassComposition bc;
	EPackage vceRulesPackage;
	
	static final URI BEANCOMPOSITION_URI = URI.createURI("presettest.xmi");

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		// For these tests we need a model to be
		// defined, an editdomain, a style.
		
		// We need to be instances of IJavaObjectInstances, but we'll try fluffing them up.
		
		domain = new EditDomain(null);
		domain.setAnnotationLinkagePolicy(new EMFAnnotationLinkagePolicy());
		domain.setRuleRegistry(new VCERuleRegistry());
		Resource rules = rset.getResource(PostSetTest.VCERULES_ECORE, true);
		vceRulesPackage = (EPackage) rules.getContents().get(0);
		vceRulesPackage.setEFactoryInstance(new JavaObjectFactory());	// Force one that fluffs up IJavaObjectInstances.
		Resource r = rset.createResource(BEANCOMPOSITION_URI);
		r.getContents().add(bc = JCMFactory.eINSTANCE.createBeanSubclassComposition());
		setupResource(r);
	}
	
	/*
	 * Setup the resource with common requirements.
	 */
	private void setupResource(Resource res) {
		DiagramData dd = (DiagramData) res.getContents().get(0);
		domain.setDiagramData(dd);
		InverseMaintenanceAdapter ia = new InverseMaintenanceAdapter() {
			/**
			 * @see org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter#subShouldPropagate(EReference, Object)
			 */
			protected boolean subShouldPropagate(EReference ref, Object newValue) {
				return !(newValue instanceof Diagram); // On the base BeanComposition, don't propagate into Diagram references.
			}
		};
		dd.eAdapters().add(ia);
		ia.propagate();		
	}	
	
	/**
	 * Test setting for no location
	 */
	public void testNoLocation() {
		// Test setting for no location. This should put it in GLOBAL_GLOBAL.
		// Should be GLOBAL_GLOBAL because not a child of anything. Can't have local if not a child.
		
		RuledCommandBuilder cbld = new RuledCommandBuilder(domain);
		cbld.setPropertyRule(false);
		EObject comp = vceRulesPackage.getEFactoryInstance().create((EClass)vceRulesPackage.getEClassifier("NOLocation"));
		cbld.applyAttributeSetting(bc, JCMPackage.eINSTANCE.getBeanComposition_Components(), comp);
		cbld.getCommand().execute();

		assertFalse("Child not added.", bc.getComponents().isEmpty());
		assertFalse("Child not global.", bc.getMembers().isEmpty());
		
		EObject initMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertNotNull("No init method", initMethod);

		EObject returnMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Return());
		assertNotNull("No return method", returnMethod);

	}
	
	/**
	 * Test setting for global-global location
	 */
	public void testGlobalGlobalLocation() {
		// Test setting for no location. This should put it in GLOBAL_GLOBAL.
		// Should be GLOBAL_GLOBAL because not a child of anything. Can't have local if not a child.
		
		RuledCommandBuilder cbld = new RuledCommandBuilder(domain);
		cbld.setPropertyRule(false);
		EObject comp = vceRulesPackage.getEFactoryInstance().create((EClass)vceRulesPackage.getEClassifier("GLOBAL_GLOBALLocation"));
		cbld.applyAttributeSetting(bc, JCMPackage.eINSTANCE.getBeanComposition_Components(), comp);
		cbld.getCommand().execute();

		assertFalse("Child not added.", bc.getComponents().isEmpty());
		assertFalse("Child not global.", bc.getMembers().isEmpty());
		
		EObject initMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertNotNull("No init method", initMethod);

		EObject returnMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Return());
		assertNotNull("No return method", returnMethod);

	}	

	/**
	 * Test setting for global-local location
	 */
	public void testGlobalLocalLocation() {
		// Test setting for no location. This should put it in GLOBAL_LOCAL.
		// Should be GLOBAL_GLOBAL because not a child of anything. Can't have local if not a child.
		
		RuledCommandBuilder cbld = new RuledCommandBuilder(domain);
		cbld.setPropertyRule(false);
		EObject comp = vceRulesPackage.getEFactoryInstance().create((EClass)vceRulesPackage.getEClassifier("GLOBAL_LOCALLocation"));
		cbld.applyAttributeSetting(bc, JCMPackage.eINSTANCE.getBeanComposition_Components(), comp);
		cbld.getCommand().execute();

		assertFalse("Child not added.", bc.getComponents().isEmpty());
		assertFalse("Child not global.", bc.getMembers().isEmpty());
		
		EObject initMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertNotNull("No init method", initMethod);

		EObject returnMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Return());
		assertNotNull("No return method", returnMethod);

	}	

	/**
	 * Test setting for local location
	 */
	public void testLocalLocation() {
		// Test setting for no location. This should put it in LOCAL.
		// Should be GLOBAL_GLOBAL because not a child of anything. Can't have local if not a child.
		
		RuledCommandBuilder cbld = new RuledCommandBuilder(domain);
		cbld.setPropertyRule(false);
		EObject comp = vceRulesPackage.getEFactoryInstance().create((EClass)vceRulesPackage.getEClassifier("LOCALLocation"));
		cbld.applyAttributeSetting(bc, JCMPackage.eINSTANCE.getBeanComposition_Components(), comp);
		cbld.getCommand().execute();

		assertFalse("Child not added.", bc.getComponents().isEmpty());
		assertFalse("Child not global.", bc.getMembers().isEmpty());
		
		EObject initMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertNotNull("No init method", initMethod);

		EObject returnMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Return());
		assertNotNull("No return method", returnMethod);

	}	

	/**
	 * Test setting for property location
	 */
	public void testPropertyLocation() {
		// Test setting for no location. This should put it in Property.
		// Should be GLOBAL_GLOBAL because not a child of anything. Can't have property if not a child.
		
		RuledCommandBuilder cbld = new RuledCommandBuilder(domain);
		cbld.setPropertyRule(false);
		EObject comp = vceRulesPackage.getEFactoryInstance().create((EClass)vceRulesPackage.getEClassifier("GLOBAL_LOCALLocation"));
		cbld.applyAttributeSetting(bc, JCMPackage.eINSTANCE.getBeanComposition_Components(), comp);
		cbld.getCommand().execute();

		assertFalse("Child not added.", bc.getComponents().isEmpty());
		assertFalse("Child not global.", bc.getMembers().isEmpty());
		
		EObject initMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertNotNull("No init method", initMethod);

		EObject returnMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Return());
		assertNotNull("No return method", returnMethod);

	}
	
	protected EObject setupParent(String parentClassName) {

		// Create a GLOBAL_GLOBAL as a parent to handle children.
		
		RuledCommandBuilder cbld = new RuledCommandBuilder(domain);
		cbld.setPropertyRule(false);
		EObject parent = vceRulesPackage.getEFactoryInstance().create((EClass)vceRulesPackage.getEClassifier(parentClassName));
		cbld.applyAttributeSetting(bc, JCMPackage.eINSTANCE.getBeanComposition_Components(), parent);
		cbld.getCommand().execute();
		
		return parent;
	}

	/**
	 * Test setting for no location as a child
	 */
	public void testNoLocationChild() {
		// Test setting for no location. This should put it in property.
		
		EObject parent = setupParent("NOLocation");
		RuledCommandBuilder cbld = new RuledCommandBuilder(domain);
		cbld.setPropertyRule(false);
		EObject comp = vceRulesPackage.getEFactoryInstance().create((EClass)vceRulesPackage.getEClassifier("NOLocation"));
		cbld.applyAttributeSetting(parent, parent.eClass().getEStructuralFeature("childComponent"), comp);
		cbld.getCommand().execute();

		assertFalse("Child should not be global.", bc.getMembers().contains(comp));

		JCMMethod parentInitMethod = (JCMMethod) InverseMaintenanceAdapter.getFirstReferencedBy(parent, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertFalse("Child should not be in parent init method membership.", parentInitMethod.getMembers().contains(comp));
		assertTrue("Child not in parent init method property containment.", parentInitMethod.getProperties().contains(comp));		

		EObject initMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertNull("Should not have init method", initMethod);

		EObject returnMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Return());
		assertNull("Should not have return method", returnMethod);

	}

	/**
	 * Test setting for global-global as a child
	 */
	public void testGlobalGlobalChild() {
		// This should put it in GLOBAL_GLOBAL.
		
		EObject parent = setupParent("NOLocation");
		RuledCommandBuilder cbld = new RuledCommandBuilder(domain);
		cbld.setPropertyRule(false);
		EObject comp = vceRulesPackage.getEFactoryInstance().create((EClass)vceRulesPackage.getEClassifier("GLOBAL_GLOBALLocation"));
		cbld.applyAttributeSetting(parent, parent.eClass().getEStructuralFeature("childComponent"), comp);
		cbld.getCommand().execute();

		assertFalse("Child not added.", bc.getComponents().isEmpty());
		assertFalse("Child not global.", bc.getMembers().isEmpty());
		
		EObject initMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertNotNull("No init method", initMethod);

		EObject returnMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Return());
		assertNotNull("No return method", returnMethod);

	}

	/**
	 * Test setting for global-local as a child
	 */
	public void testGlobalLocalChild() {
		// This should put it in GLOBAL_LOCAL.
		
		EObject parent = setupParent("NOLocation");
		RuledCommandBuilder cbld = new RuledCommandBuilder(domain);
		cbld.setPropertyRule(false);
		EObject comp = vceRulesPackage.getEFactoryInstance().create((EClass)vceRulesPackage.getEClassifier("GLOBAL_LOCALLocation"));
		cbld.applyAttributeSetting(parent, parent.eClass().getEStructuralFeature("childComponent"), comp);
		cbld.getCommand().execute();

		assertTrue("Child not global.", bc.getMembers().contains(comp));

		EObject parentInitMethod = InverseMaintenanceAdapter.getFirstReferencedBy(parent, JCMPackage.eINSTANCE.getJCMMethod_Initializes());

		EObject initMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertSame("Not parent init method", parentInitMethod, initMethod);

		EObject returnMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Return());
		assertNull("Should not have return method", returnMethod);

	}

	/**
	 * Test setting for local as a child
	 */
	public void testLocalChild() {
		// This should put it in LOCAL.
		
		EObject parent = setupParent("NOLocation");
		RuledCommandBuilder cbld = new RuledCommandBuilder(domain);
		cbld.setPropertyRule(false);
		EObject comp = vceRulesPackage.getEFactoryInstance().create((EClass)vceRulesPackage.getEClassifier("LOCALLocation"));
		cbld.applyAttributeSetting(parent, parent.eClass().getEStructuralFeature("childComponent"), comp);
		cbld.getCommand().execute();

		assertFalse("Child should not be global.", bc.getMembers().contains(comp));

		JCMMethod parentInitMethod = (JCMMethod) InverseMaintenanceAdapter.getFirstReferencedBy(parent, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertTrue("Child not found in parent init method membership.", parentInitMethod.getMembers().contains(comp));

		EObject initMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertSame("Not parent init method", parentInitMethod, initMethod);

		EObject returnMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Return());
		assertNull("Should not have return method", returnMethod);

	}
	
	/**
	 * Test setting for property as a child
	 */
	public void testPropertyChild() {
		// This should put it in property.
		
		EObject parent = setupParent("NOLocation");
		RuledCommandBuilder cbld = new RuledCommandBuilder(domain);
		cbld.setPropertyRule(false);
		EObject comp = vceRulesPackage.getEFactoryInstance().create((EClass)vceRulesPackage.getEClassifier("PropertyLocation"));
		cbld.applyAttributeSetting(parent, parent.eClass().getEStructuralFeature("childComponent"), comp);
		cbld.getCommand().execute();

		assertFalse("Child should not be global.", bc.getMembers().contains(comp));

		JCMMethod parentInitMethod = (JCMMethod) InverseMaintenanceAdapter.getFirstReferencedBy(parent, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertFalse("Child should not be in parent init method membership.", parentInitMethod.getMembers().contains(comp));
		assertTrue("Child not in parent init method property containment.", parentInitMethod.getProperties().contains(comp));		

		EObject initMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertNull("Should not have init method", initMethod);

		EObject returnMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Return());
		assertNull("Should not have return method", returnMethod);

	}	

	/**
	 * Test setting for global-global as a child due to feature.
	 */
	public void testGlobalGlobalChildFeature() {
		// This should put it in GLOBAL_GLOBAL.
		
		EObject parent = setupParent("GLOBAL_GLOBALLocation");
		RuledCommandBuilder cbld = new RuledCommandBuilder(domain);
		cbld.setPropertyRule(false);
		EObject comp = vceRulesPackage.getEFactoryInstance().create((EClass)vceRulesPackage.getEClassifier("NOLocation"));
		cbld.applyAttributeSetting(parent, parent.eClass().getEStructuralFeature("childComponent"), comp);
		cbld.getCommand().execute();

		assertFalse("Child not added.", bc.getComponents().isEmpty());
		assertFalse("Child not global.", bc.getMembers().isEmpty());
		
		EObject initMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertNotNull("No init method", initMethod);

		EObject returnMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Return());
		assertNotNull("No return method", returnMethod);

	}

	/**
	 * Test setting for global-local as a child with feature
	 */
	public void testGlobalLocalChildFeature() {
		// This should put it in GLOBAL_LOCAL.
		
		EObject parent = setupParent("GLOBAL_LOCALLocation");
		RuledCommandBuilder cbld = new RuledCommandBuilder(domain);
		cbld.setPropertyRule(false);
		EObject comp = vceRulesPackage.getEFactoryInstance().create((EClass)vceRulesPackage.getEClassifier("NOLocation"));
		cbld.applyAttributeSetting(parent, parent.eClass().getEStructuralFeature("childComponent"), comp);
		cbld.getCommand().execute();

		assertTrue("Child not global.", bc.getMembers().contains(comp));

		EObject parentInitMethod = InverseMaintenanceAdapter.getFirstReferencedBy(parent, JCMPackage.eINSTANCE.getJCMMethod_Initializes());

		EObject initMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertSame("Not parent init method", parentInitMethod, initMethod);

		EObject returnMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Return());
		assertNull("Should not have return method", returnMethod);

	}

	/**
	 * Test setting for local as a child with feature
	 */
	public void testLocalChildFeature() {
		// This should put it in LOCAL.
		
		EObject parent = setupParent("LOCALLocation");
		RuledCommandBuilder cbld = new RuledCommandBuilder(domain);
		cbld.setPropertyRule(false);
		EObject comp = vceRulesPackage.getEFactoryInstance().create((EClass)vceRulesPackage.getEClassifier("NOLocation"));
		cbld.applyAttributeSetting(parent, parent.eClass().getEStructuralFeature("childComponent"), comp);
		cbld.getCommand().execute();

		assertFalse("Child should not be global.", bc.getMembers().contains(comp));

		JCMMethod parentInitMethod = (JCMMethod) InverseMaintenanceAdapter.getFirstReferencedBy(parent, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertTrue("Child not found in parent init method membership.", parentInitMethod.getMembers().contains(comp));

		EObject initMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertSame("Not parent init method", parentInitMethod, initMethod);

		EObject returnMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Return());
		assertNull("Should not have return method", returnMethod);

	}

	/**
	 * Test setting for property as a child feature
	 */
	public void testPropertyChildFeature() {
		// This should put it in property.
		
		EObject parent = setupParent("PropertyLocation");
		RuledCommandBuilder cbld = new RuledCommandBuilder(domain);
		cbld.setPropertyRule(false);
		EObject comp = vceRulesPackage.getEFactoryInstance().create((EClass)vceRulesPackage.getEClassifier("NOLocation"));
		cbld.applyAttributeSetting(parent, parent.eClass().getEStructuralFeature("childComponent"), comp);
		cbld.getCommand().execute();

		assertFalse("Child should not be global.", bc.getMembers().contains(comp));

		JCMMethod parentInitMethod = (JCMMethod) InverseMaintenanceAdapter.getFirstReferencedBy(parent, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertFalse("Child should not be in parent init method membership.", parentInitMethod.getMembers().contains(comp));
		assertTrue("Child not in parent init method property containment.", parentInitMethod.getProperties().contains(comp));		

		EObject initMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertNull("Should not have init method", initMethod);

		EObject returnMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Return());
		assertNull("Should not have return method", returnMethod);

	}	

	/**
	 * Test setting for global-global as a child due to annotation.
	 */
	public void testGlobalGlobalChildAnnotation() {
		// This should put it in GLOBAL_GLOBAL.
		
		EObject parent = setupParent("NOLocation");
		RuledCommandBuilder cbld = new RuledCommandBuilder(domain);
		cbld.setPropertyRule(false);
		EObject comp = vceRulesPackage.getEFactoryInstance().create((EClass)vceRulesPackage.getEClassifier("NOLocation"));
		// We need to create an annotation for this poofed up location
		KeyedInstanceLocationImpl kl = (KeyedInstanceLocationImpl) JCMFactory.eINSTANCE.create(JCMPackage.eINSTANCE.getKeyedInstanceLocation());
		kl.setKey(VCEPreSetCommand.BEAN_LOCATION_KEY);
		kl.setTypedValue(InstanceLocation.GLOBAL_GLOBAL_LITERAL);
		Command cmd = AnnotationPolicy.applyAnnotationSetting(comp, kl, domain);
		cbld.append(cmd);
		
		cbld.applyAttributeSetting(parent, parent.eClass().getEStructuralFeature("childComponent"), comp);
		cbld.getCommand().execute();

		assertFalse("Child not added.", bc.getComponents().isEmpty());
		assertFalse("Child not global.", bc.getMembers().isEmpty());
		
		EObject initMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertNotNull("No init method", initMethod);

		EObject returnMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Return());
		assertNotNull("No return method", returnMethod);

	}

	/**
	 * Test setting for global-local as a child with annotation
	 */
	public void testGlobalLocalChildAnnotation() {
		// This should put it in GLOBAL_LOCAL.
		
		EObject parent = setupParent("NOLocation");
		RuledCommandBuilder cbld = new RuledCommandBuilder(domain);
		cbld.setPropertyRule(false);
		EObject comp = vceRulesPackage.getEFactoryInstance().create((EClass)vceRulesPackage.getEClassifier("NOLocation"));
		// We need to create an annotation for this poofed up location
		KeyedInstanceLocationImpl kl = (KeyedInstanceLocationImpl) JCMFactory.eINSTANCE.create(JCMPackage.eINSTANCE.getKeyedInstanceLocation());
		kl.setKey(VCEPreSetCommand.BEAN_LOCATION_KEY);
		kl.setTypedValue(InstanceLocation.GLOBAL_LOCAL_LITERAL);
		Command cmd = AnnotationPolicy.applyAnnotationSetting(comp, kl, domain);
		cbld.append(cmd);
		
		cbld.applyAttributeSetting(parent, parent.eClass().getEStructuralFeature("childComponent"), comp);
		cbld.getCommand().execute();

		assertTrue("Child not global.", bc.getMembers().contains(comp));

		EObject parentInitMethod = InverseMaintenanceAdapter.getFirstReferencedBy(parent, JCMPackage.eINSTANCE.getJCMMethod_Initializes());

		EObject initMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertSame("Not parent init method", parentInitMethod, initMethod);

		EObject returnMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Return());
		assertNull("Should not have return method", returnMethod);

	}

	/**
	 * Test setting for local as a child with annotation
	 */
	public void testLocalChildAnnotation() {
		// This should put it in LOCAL.
		
		EObject parent = setupParent("NOLocation");
		RuledCommandBuilder cbld = new RuledCommandBuilder(domain);
		cbld.setPropertyRule(false);
		EObject comp = vceRulesPackage.getEFactoryInstance().create((EClass)vceRulesPackage.getEClassifier("NOLocation"));
		// We need to create an annotation for this poofed up location
		KeyedInstanceLocationImpl kl = (KeyedInstanceLocationImpl) JCMFactory.eINSTANCE.create(JCMPackage.eINSTANCE.getKeyedInstanceLocation());
		kl.setKey(VCEPreSetCommand.BEAN_LOCATION_KEY);
		kl.setTypedValue(InstanceLocation.LOCAL_LITERAL);
		Command cmd = AnnotationPolicy.applyAnnotationSetting(comp, kl, domain);
		cbld.append(cmd);
		
		cbld.applyAttributeSetting(parent, parent.eClass().getEStructuralFeature("childComponent"), comp);
		cbld.getCommand().execute();

		assertFalse("Child should not be global.", bc.getMembers().contains(comp));

		JCMMethod parentInitMethod = (JCMMethod) InverseMaintenanceAdapter.getFirstReferencedBy(parent, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertTrue("Child not found in parent init method membership.", parentInitMethod.getMembers().contains(comp));

		EObject initMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertSame("Not parent init method", parentInitMethod, initMethod);

		EObject returnMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Return());
		assertNull("Should not have return method", returnMethod);

	}

	/**
	 * Test setting for property as a child with annotation
	 */
	public void testPropertyChildAnnotation() {
		// This should put it in property.
		
		EObject parent = setupParent("NOLocation");
		RuledCommandBuilder cbld = new RuledCommandBuilder(domain);
		cbld.setPropertyRule(false);
		EObject comp = vceRulesPackage.getEFactoryInstance().create((EClass)vceRulesPackage.getEClassifier("NOLocation"));
		// We need to create an annotation for this poofed up location
		KeyedInstanceLocationImpl kl = (KeyedInstanceLocationImpl) JCMFactory.eINSTANCE.create(JCMPackage.eINSTANCE.getKeyedInstanceLocation());
		kl.setKey(VCEPreSetCommand.BEAN_LOCATION_KEY);
		kl.setTypedValue(InstanceLocation.PROPERTY_LITERAL);
		Command cmd = AnnotationPolicy.applyAnnotationSetting(comp, kl, domain);
		cbld.append(cmd);
		
		cbld.applyAttributeSetting(parent, parent.eClass().getEStructuralFeature("childComponent"), comp);
		cbld.getCommand().execute();

		assertFalse("Child should not be global.", bc.getMembers().contains(comp));

		JCMMethod parentInitMethod = (JCMMethod) InverseMaintenanceAdapter.getFirstReferencedBy(parent, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertFalse("Child should not be in parent init method membership.", parentInitMethod.getMembers().contains(comp));
		assertTrue("Child not in parent init method property containment.", parentInitMethod.getProperties().contains(comp));		

		EObject initMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertNull("Should not have init method", initMethod);

		EObject returnMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Return());
		assertNull("Should not have return method", returnMethod);

	}	

	/**
	 * Test setting for no return method
	 */
	public void testNoReturn() {
		// Test setting for no return method indicated on bean decorator. Only applies for GLOBAL-GLOBAL
		// objects because anything else is LOCAL/PROPERTY and so have no return method by default.
		
		RuledCommandBuilder cbld = new RuledCommandBuilder(domain);
		cbld.setPropertyRule(false);
		EObject comp = vceRulesPackage.getEFactoryInstance().create((EClass)vceRulesPackage.getEClassifier("NOReturn"));
		cbld.applyAttributeSetting(bc, JCMPackage.eINSTANCE.getBeanComposition_Components(), comp);
		cbld.getCommand().execute();

		assertFalse("Child not added.", bc.getComponents().isEmpty());
		assertFalse("Child not global.", bc.getMembers().isEmpty());
		
		EObject initMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		assertNotNull("No init method", initMethod);

		EObject returnMethod = InverseMaintenanceAdapter.getFirstReferencedBy(comp, JCMPackage.eINSTANCE.getJCMMethod_Return());
		assertNull("Should not have a return method", returnMethod);

	}	
	
}
