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
package org.eclipse.ve.tests.vce.rules;
/*
 *  $RCSfile: PostSetTest.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:54:15 $ 
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;

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
	 * Check that there aren't any dangling refs. I.e. we don't have a ref to something
	 * that is no longer contained within the resource. This is important because VCEPostSet is 
	 * not supposed to leave dangling refs.
	 * 
	 * @param res
	 * @throws IOException
	 * 
	 * @since 1.0.0
	 */
	protected void checkNoDangling(Resource res) throws IOException {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		res.save(bo, Collections.EMPTY_MAP); 
	}

	
	/**
	 * Test deletion when there is a reference to an
	 * object within in the same set that is to be deleted.
	 */
	public void testCrossRefInDeleteSet() throws IOException {
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
		
		checkNoDangling(testRes);
	}
	
	/**
	 * Test deletion when parent deleted that all children go too.
	 * This is a simple containment case, no interactions with others.
	 */
	public void testParentDelete() throws IOException {
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
		
		checkNoDangling(testRes);
	}

	/**
	 * Test deletion when parent deleted that all children go too.
	 * There is a cross-ref to a child outside of the parent. It should
	 * still be around afterwards.
	 * @throws IOException
	 */
	public void testParentDeleteCrossRefOutside() throws IOException {
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
		
		checkNoDangling(testRes);
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
	 * P1-c->C-d->P2
	 * 
	 * Delete P1, means C (because child) and P2 (because no other backrefs) goes.
	 * 
	 * (-c-) a child relationship
	 * (-d-) a dependency relationship
	 */
	public void testDeleteDepends1() throws IOException {
		Resource testRes = rset.getResource(URI.createURI("platform:/plugin/org.eclipse.ve.tests/resources/vcerules/testDepends1.xmi"), true);
		setupResource(testRes);
		
		CommandBuilder cbld = new CommandBuilder();
		EObject parentContainer = testRes.getEObject("parentContainer1");
		cbld.append(childRule.postDeleteChild(domain, parentContainer));
		cbld.getCommand().execute();

		assertNull(testRes.getEObject("parentContainer1"));
		assertNull(testRes.getEObject("child1"));
		assertNull(testRes.getEObject("parentContainer2"));
		
		checkNoDangling(testRes);
	}
	
	/**
	 * P1-c->C-d->P2
	 * 
	 * Delete P2, means C (because child of P1) and P1 (because C can't affect it) stay.
	 * 
	 * (-c-) a child relationship
	 * (-d-) a dependency relationship
	 */
	public void testDeleteDepends1a() throws IOException {
		Resource testRes = rset.getResource(URI.createURI("platform:/plugin/org.eclipse.ve.tests/resources/vcerules/testDepends1.xmi"), true);
		setupResource(testRes);
		
		CommandBuilder cbld = new CommandBuilder();
		EObject parentContainer = testRes.getEObject("parentContainer2");
		cbld.append(childRule.postDeleteChild(domain, parentContainer));
		cbld.getCommand().execute();

		assertNull(testRes.getEObject("parentContainer2"));
		assertNotNull(testRes.getEObject("child1"));
		assertNotNull(testRes.getEObject("parentContainer1"));
		
		checkNoDangling(testRes);
	}	

	/**
	 * P1-->C-d->P2
	 * 
	 * Delete P1, means C (because no other backrefs) and P2 (because no other backrefs) goes.
	 * 
	 * (-d-) a dependency relationship
	 */
	public void testDeleteDepends2() throws IOException {
		Resource testRes = rset.getResource(URI.createURI("platform:/plugin/org.eclipse.ve.tests/resources/vcerules/testDepends2.xmi"), true);
		setupResource(testRes);
		
		CommandBuilder cbld = new CommandBuilder();
		EObject parentContainer = testRes.getEObject("parentContainer1");
		cbld.append(childRule.postDeleteChild(domain, parentContainer));
		cbld.getCommand().execute();

		assertNull(testRes.getEObject("parentContainer1"));
		assertNull(testRes.getEObject("child1"));
		assertNull(testRes.getEObject("parentContainer2"));
		
		checkNoDangling(testRes);
	}
	
	/**
	 * P1-->C-d->P2
	 * 
	 * Delete P2, means C (because depends ref) goes and P1 (because C can't affect it) stays.
	 * 
	 * (-d-) a dependency relationship
	 */
	public void testDeleteDepends2a() throws IOException {
		Resource testRes = rset.getResource(URI.createURI("platform:/plugin/org.eclipse.ve.tests/resources/vcerules/testDepends2.xmi"), true);
		setupResource(testRes);
		
		CommandBuilder cbld = new CommandBuilder();
		EObject parentContainer = testRes.getEObject("parentContainer2");
		cbld.append(childRule.postDeleteChild(domain, parentContainer));
		cbld.getCommand().execute();

		assertNull(testRes.getEObject("parentContainer2"));
		assertNull(testRes.getEObject("child1"));
		assertNotNull(testRes.getEObject("parentContainer1"));
		
		checkNoDangling(testRes);
	}	

	/**
	 * P1-->C-d->P2
	 * 
	 * Delete C, means P1 (because not part of child chain) stays and P2 (because no other backrefs) goes.
	 * 
	 * (-d-) a dependency relationship
	 */
	public void testDeleteDepends2c() throws IOException {
		Resource testRes = rset.getResource(URI.createURI("platform:/plugin/org.eclipse.ve.tests/resources/vcerules/testDepends2.xmi"), true);
		setupResource(testRes);
		
		CommandBuilder cbld = new CommandBuilder();
		EObject child = testRes.getEObject("child1");
		cbld.append(childRule.postDeleteChild(domain, child));
		cbld.getCommand().execute();

		assertNotNull(testRes.getEObject("parentContainer1"));
		assertNull(testRes.getEObject("child1"));
		assertNull(testRes.getEObject("parentContainer2"));
		
		checkNoDangling(testRes);
	}

	/**
	 * P1-d->C-d->P2
	 * 
	 * Delete P1, means C (because no other backrefs) and P2 (because no other backrefs) goes.
	 * 
	 * (-d-) a dependency relationship
	 */
	public void testDeleteDepends3() throws IOException {
		Resource testRes = rset.getResource(URI.createURI("platform:/plugin/org.eclipse.ve.tests/resources/vcerules/testDepends3.xmi"), true);
		setupResource(testRes);
		
		CommandBuilder cbld = new CommandBuilder();
		EObject parentContainer = testRes.getEObject("parentContainer1");
		cbld.append(childRule.postDeleteChild(domain, parentContainer));
		cbld.getCommand().execute();

		assertNull(testRes.getEObject("parentContainer1"));
		assertNull(testRes.getEObject("child1"));
		assertNull(testRes.getEObject("parentContainer2"));
		
		checkNoDangling(testRes);
	}
	
	/**
	 * P1-d->C-d->P2
	 * 
	 * Delete C, means P1 (because depends on C) and P2 (because no other backrefs) goes.
	 * 
	 * (-d-) a dependency relationship
	 */
	public void testDeleteDepends3b() throws IOException {
		Resource testRes = rset.getResource(URI.createURI("platform:/plugin/org.eclipse.ve.tests/resources/vcerules/testDepends3.xmi"), true);
		setupResource(testRes);
		
		CommandBuilder cbld = new CommandBuilder();
		EObject child = testRes.getEObject("child1");
		cbld.append(childRule.postDeleteChild(domain, child));
		cbld.getCommand().execute();

		assertNull(testRes.getEObject("parentContainer1"));
		assertNull(testRes.getEObject("child1"));
		assertNull(testRes.getEObject("parentContainer2"));
		
		checkNoDangling(testRes);
	}
	
	/**
	 * P1-d->C-d->P2
	 * 
	 * Delete P2, means C (because depends) and P1 (because depends) goes.
	 * 
	 * (-d-) a dependency relationship
	 */
	public void testDeleteDepends3c() throws IOException {
		Resource testRes = rset.getResource(URI.createURI("platform:/plugin/org.eclipse.ve.tests/resources/vcerules/testDepends3.xmi"), true);
		setupResource(testRes);
		
		CommandBuilder cbld = new CommandBuilder();
		EObject parentContainer = testRes.getEObject("parentContainer2");
		cbld.append(childRule.postDeleteChild(domain, parentContainer));
		cbld.getCommand().execute();

		assertNull(testRes.getEObject("parentContainer1"));
		assertNull(testRes.getEObject("child1"));
		assertNull(testRes.getEObject("parentContainer2"));
		
		checkNoDangling(testRes);
	}	

	/**
	 * P1-c->C1-d->P2
	 *       C2-d---^
	 * 
	 * Delete P1, means C1 (because child) goes and P2 (because C2 refs) stays.
	 * 
	 * (-c-) a child relationship
	 * (-d-) a dependency relationship
	 */
	public void testDeleteDepends4() throws IOException {
		Resource testRes = rset.getResource(URI.createURI("platform:/plugin/org.eclipse.ve.tests/resources/vcerules/testDepends4.xmi"), true);
		setupResource(testRes);
		
		CommandBuilder cbld = new CommandBuilder();
		EObject parentContainer = testRes.getEObject("parentContainer1");
		cbld.append(childRule.postDeleteChild(domain, parentContainer));
		cbld.getCommand().execute();

		assertNull(testRes.getEObject("parentContainer1"));
		assertNull(testRes.getEObject("child1"));
		assertNotNull(testRes.getEObject("parentContainer2"));
		assertNotNull(testRes.getEObject("child2"));
		
		checkNoDangling(testRes);
	}

	/**
	 * P1-c->C1-d->P2
	 *       C2-d---^
	 * 
	 * Delete P2, means C1 (because child) stays and C2 (because depends on P2) goes.
	 * 
	 * (-c-) a child relationship
	 * (-d-) a dependency relationship
	 */
	public void testDeleteDepends4a() throws IOException {
		Resource testRes = rset.getResource(URI.createURI("platform:/plugin/org.eclipse.ve.tests/resources/vcerules/testDepends4.xmi"), true);
		setupResource(testRes);
		
		CommandBuilder cbld = new CommandBuilder();
		EObject parentContainer = testRes.getEObject("parentContainer2");
		cbld.append(childRule.postDeleteChild(domain, parentContainer));
		cbld.getCommand().execute();

		assertNotNull(testRes.getEObject("parentContainer1"));
		assertNotNull(testRes.getEObject("child1"));
		assertNull(testRes.getEObject("parentContainer2"));
		assertNull(testRes.getEObject("child2"));
		
		checkNoDangling(testRes);
	}
	
	/**
	 * P1-c->C1-d->P2
	 *       C2-d---^
	 * 
	 * Delete C2, means C1 (because child) stays and P2 (because backrefs) stays.
	 * 
	 * (-c-) a child relationship
	 * (-d-) a dependency relationship
	 */
	public void testDeleteDepends4b() throws IOException {
		Resource testRes = rset.getResource(URI.createURI("platform:/plugin/org.eclipse.ve.tests/resources/vcerules/testDepends4.xmi"), true);
		setupResource(testRes);
		
		CommandBuilder cbld = new CommandBuilder();
		EObject child = testRes.getEObject("child2");
		cbld.append(childRule.postDeleteChild(domain, child));
		cbld.getCommand().execute();

		assertNotNull(testRes.getEObject("parentContainer1"));
		assertNotNull(testRes.getEObject("child1"));
		assertNotNull(testRes.getEObject("parentContainer2"));
		assertNull(testRes.getEObject("child2"));
		
		checkNoDangling(testRes);
	}	

	/**
	 * P1-c->C1-d->P2
	 *       C2-d---^
	 * 
	 * Delete C1, means C1 (because child) and P2 (because C1 didn't go) and C2 (because P2 didn't go) stays.
	 * 
	 * (-c-) a child relationship
	 * (-d-) a dependency relationship
	 */
	public void testDeleteDepends4c() throws IOException {
		Resource testRes = rset.getResource(URI.createURI("platform:/plugin/org.eclipse.ve.tests/resources/vcerules/testDepends4.xmi"), true);
		setupResource(testRes);
		
		CommandBuilder cbld = new CommandBuilder();
		EObject child = testRes.getEObject("child1");
		cbld.append(childRule.postDeleteChild(domain, child));
		cbld.getCommand().execute();

		assertNotNull(testRes.getEObject("parentContainer1"));
		assertNotNull(testRes.getEObject("child1"));
		assertNotNull(testRes.getEObject("parentContainer2"));
		assertNotNull(testRes.getEObject("child2"));
		
		checkNoDangling(testRes);
	}	

}
