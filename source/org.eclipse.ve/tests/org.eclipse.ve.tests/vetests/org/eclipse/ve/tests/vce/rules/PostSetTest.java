package org.eclipse.ve.tests.vce.rules;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: PostSetTest.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:38:46 $ 
 */

import junit.framework.TestCase;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.emf.EMFAnnotationLinkagePolicy;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.ve.internal.cdm.Diagram;
import org.eclipse.ve.internal.cdm.DiagramData;
import org.eclipse.ve.internal.java.vce.rules.VCEChildRule;
import org.eclipse.ve.internal.jcm.JCMPackage;
import org.eclipse.ve.internal.jcm.BeanSubclassComposition;
import org.eclipse.ve.internal.jcm.JCMMethod;

/**
 * @author Rich Kulp
 *
 * Test the VCEPostSet Command.
 */
public class PostSetTest extends TestCase {

	public PostSetTest(String name) {
		super(name);
	}

	ResourceSet rset = new ResourceSetImpl();
	EditDomain domain;
	VCEChildRule childRule = new VCEChildRule();
	
	static final URI VCERULES_ECORE = URI.createURI("platform:/plugin/org.eclipse.ve.tests/resources/vcerules/vcerules.ecore");
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		// For these tests we need a model to be
		// defined, an editdomain, a style.
		
		// We could use straight Java Core Model, but
		// that requires a java project to be created.
		// Easiest is to just load a simple EMF
		// model to do the tests. 
		
		domain = new EditDomain(null);
		domain.setAnnotationLinkagePolicy(new EMFAnnotationLinkagePolicy());
	}

	
	/**
	 * Test deletion when there is a reference to an
	 * object within in the same set that is to be deleted.
	 */
	public void testCrossRefInDeleteSet() {
		Resource testRes = rset.getResource(URI.createURI("platform:/plugin/org.eclipse.ve.tests/resources/vcerules/testCrossRefInDelete.xmi"), true);
		setupResource(testRes);
		
		// Get the ParentContainer, which is //@members.0, create a command builder, and remove it from components
		// as a child policy and the post set command and then execute.
		CommandBuilder cbld = new CommandBuilder();
		BeanSubclassComposition comp = (BeanSubclassComposition) (EObject) testRes.getContents().get(0);
		EObject parentContainer = testRes.getEObject("parentContainer");
		cbld.cancelAttributeSetting(comp, JCMPackage.eINSTANCE.getBeanComposition_Components(), parentContainer);
		cbld.append(childRule.postDeleteChild(domain, parentContainer));
		cbld.getCommand().execute();

		assertTrue("Not all members removed.", comp.getMembers().isEmpty());		
		assertTrue("Not all components removed.", comp.getComponents().isEmpty());		
		assertTrue("Not all initialize methods removed.", comp.getMethods().isEmpty());
	}
	
	/**
	 * Test deletion when parent deleted that all children go too.
	 * This is a simple containment case, no interactions with others.
	 */
	public void testParentDelete() {
		Resource testRes = rset.getResource(URI.createURI("platform:/plugin/org.eclipse.ve.tests/resources/vcerules/testParentDelete.xmi"), true);
		setupResource(testRes);
		
		// Get the ParentContainer, which is //@members.0, create a command builder, and remove it from components
		// as a child policy and the post set command and then execute.
		CommandBuilder cbld = new CommandBuilder();
		BeanSubclassComposition comp = (BeanSubclassComposition) (EObject) testRes.getContents().get(0);
		EObject parentContainer = testRes.getEObject("parentContainer");
		cbld.cancelAttributeSetting(comp, JCMPackage.eINSTANCE.getBeanComposition_Components(), parentContainer);
		cbld.append(childRule.postDeleteChild(domain, parentContainer));
		cbld.getCommand().execute();

		assertTrue("Not all members removed.", comp.getMembers().isEmpty());		
		assertTrue("Not all components removed.", comp.getComponents().isEmpty());		
		assertTrue("Not all initialize methods removed.", comp.getMethods().isEmpty());
	}

	/**
	 * Test deletion when parent deleted that all children go too.
	 * There is a cross-ref to a child outside of the parent. It should
	 * still be around afterwards.
	 */
	public void testParentDeleteCrossRefOutside() {
		Resource testRes = rset.getResource(URI.createURI("platform:/plugin/org.eclipse.ve.tests/resources/vcerules/testParentDeleteCrossRefOutside.xmi"), true);
		setupResource(testRes);
		
		// Get the ParentContainer, create a command builder, and remove it from components
		// as a child policy and the post set command and then execute.
		CommandBuilder cbld = new CommandBuilder();
		BeanSubclassComposition comp = (BeanSubclassComposition) (EObject) testRes.getContents().get(0);
		EObject parentContainer = testRes.getEObject("parentContainer");
		cbld.cancelAttributeSetting(comp, JCMPackage.eINSTANCE.getBeanComposition_Components(), parentContainer);
		cbld.append(childRule.postDeleteChild(domain, parentContainer));
		cbld.getCommand().execute();

		assertNull("ParentContainer still around.", testRes.getEObject("parentContainer"));
		assertNull("Child1 still around.", testRes.getEObject("child1"));
		assertNull("Child2 still around.", testRes.getEObject("child2"));
		assertNotNull("ParentContainer1 gone.", testRes.getEObject("parentContainer1"));
		EObject child3 = testRes.getEObject("child3");
		assertNotNull("Child3 gone.", child3);		
		assertTrue("Wrong number of components removed.", comp.getComponents().size() == 1);
		assertTrue("ParentContainer1 removed from components.", comp.getComponents().get(0) == testRes.getEObject("parentContainer1"));
		assertTrue("Wrong number of members removed.", comp.getMembers().size() == 2);
		assertTrue("ParentContainer1 members missing.", comp.getMembers().get(0) == testRes.getEObject("parentContainer1"));
		assertTrue("Child3 members missing.", comp.getMembers().get(1) == testRes.getEObject("child3"));		
		assertTrue("Wrong number of initialize methods removed.", comp.getMethods().size() == 2);
		assertTrue("ParentContainer1 initializer missing.", ((JCMMethod) comp.getMethods().get(0)).getReturn() == testRes.getEObject("parentContainer1"));
		assertTrue("Child3 initializer missing.", ((JCMMethod) comp.getMethods().get(1)).getReturn() == testRes.getEObject("child3"));
		
		EStructuralFeature crossRef = child3.eClass().getEStructuralFeature("crossRef");
		assertNull("crossRef from Child1 not removed.", InverseMaintenanceAdapter.getFirstReferencedBy(child3, (EReference) crossRef));
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

}
