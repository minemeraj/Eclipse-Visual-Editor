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
package org.eclipse.ve.tests.cde;
/*
 *  $RCSfile: InverseMaintenanceAdapterSharedAfterPropagationTest.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:54:15 $ 
 */
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import junit.framework.TestCase;

/**
 * Tests for the Inverse Maintenance Adapter for Type (A) from InverseMaintenanceAdapter
 * comments on algorithm. Type (A) is handling shared references.
 * 
 * This goes after propagation. Makes sure it handles the notifications correctly.
 */
public class InverseMaintenanceAdapterSharedAfterPropagationTest extends TestCase {


	public InverseMaintenanceAdapterSharedAfterPropagationTest(String name) {
		super(name);
	}
	
	/**
	 * Test that adding an adapter and propagate w/o being in a resource does nothing.
	 * (A) 
	 */
	public void testAddNoResource() {
		// Use an EReference for simplicity. Create it, add an EOpposite to it and see that it doesn't propagate.
		EReference ref1 = EcoreFactory.eINSTANCE.createEReference();
		EReference ref2 = EcoreFactory.eINSTANCE.createEReference();

		InverseMaintenanceAdapter ia = new InverseMaintenanceAdapter();
		ref1.eAdapters().add(ia);
		ia.propagate();		
		
		ref1.setEOpposite(ref2);		
		
		// Now the tests.
		assertFalse(ia.isPropagated());
		assertNull(EcoreUtil.getExistingAdapter(ref2, InverseMaintenanceAdapter.ADAPTER_KEY));
	}
	
	/**
	 * Test that adding an adapter and propagate being in a resource causes propagation. Will point
	 * to one that is not contained. 
	 * (A2c)
	 */
	public void testAddResourceOtherNotContained() {
		Resource resource = new ResourceImpl();
		
		// Use an EReference for simplicity. Create it, add an EOpposite to it and see that it doesn't propagate.
		EReference ref1 = EcoreFactory.eINSTANCE.createEReference();
		EReference ref2 = EcoreFactory.eINSTANCE.createEReference();
	
		resource.getContents().add(ref1);
				
		InverseMaintenanceAdapter ia = new InverseMaintenanceAdapter();
		ref1.eAdapters().add(ia);
		ia.propagate();		
		
		ref1.setEOpposite(ref2);				
		
		// Now the tests.
		assertTrue(ia.isPropagated());
		InverseMaintenanceAdapter ia2 = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(ref2, InverseMaintenanceAdapter.ADAPTER_KEY);
		assertNotNull(ia2);
		assertFalse(ia2.isPropagated());
		assertTrue(ia2.getFirstReferencedBy(EcorePackage.eINSTANCE.getEReference_EOpposite()) == ref1);		
	}
	
	/**
	 * Test that adding an adapter and propagate being in a resource causes propagation. Will point
	 * to one that is in another resource and doesn't allow crossdoc.
	 * (A2a) 
	 */
	public void testAddResourceOtherResource() {
		Resource resource = new ResourceImpl();
		Resource resource2 = new ResourceImpl();
		
		// Use an EReference for simplicity. Create it, add an EOpposite to it and see that it doesn't propagate.
		EReference ref1 = EcoreFactory.eINSTANCE.createEReference();
		EReference ref2 = EcoreFactory.eINSTANCE.createEReference();
		
		resource.getContents().add(ref1);
		resource2.getContents().add(ref2);
				
		InverseMaintenanceAdapter ia = new InverseMaintenanceAdapter();
		ref1.eAdapters().add(ia);
		ia.propagate();		
		
		ref1.setEOpposite(ref2);		
		
		// Now the tests.
		assertTrue(ia.isPropagated());		
		assertNull(EcoreUtil.getExistingAdapter(ref2, InverseMaintenanceAdapter.ADAPTER_KEY));		
	}

	/**
	 * Test that adding an adapter and propagate being in a resource causes propagation. Will point
	 * to one that is in another resource and allows crossdoc.
	 * (A2b) 
	 */
	public void testAddResourceOtherResourceAllow() {
		Resource resource = new ResourceImpl();
		Resource resource2 = new ResourceImpl();
		
		// Use an EReference for simplicity. Create it, add an EOpposite to it and see that it doesn't propagate.
		EReference ref1 = EcoreFactory.eINSTANCE.createEReference();
		EReference ref2 = EcoreFactory.eINSTANCE.createEReference();

		resource.getContents().add(ref1);
		resource2.getContents().add(ref2);
		
		InverseMaintenanceAdapter ia = new InverseMaintenanceAdapter(true);
		ref1.eAdapters().add(ia);
		ia.propagate();
						
		ref1.setEOpposite(ref2);
		
		// Now the tests.
		assertTrue(ia.isPropagated());
		InverseMaintenanceAdapter ia2 = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(ref2, InverseMaintenanceAdapter.ADAPTER_KEY);
		assertNotNull(ia2);
		assertFalse(ia2.isPropagated());
		assertTrue(ia2.getFirstReferencedBy(EcorePackage.eINSTANCE.getEReference_EOpposite()) == ref1);		
	}

	/**
	 * Test that adding an adapter and propagate being in a resource causes propagation. Will point
	 * to one that is in another resource and allows crossdoc but already has adapter.
	 * (A1 with A2b conditions) 
	 */
	public void testAddResourceOtherResourceAllowWithAdapter() {
		Resource resource = new ResourceImpl();
		Resource resource2 = new ResourceImpl();
		
		// Use an EReference for simplicity. Create it, add an EOpposite to it and see that it doesn't propagate.
		EReference ref1 = EcoreFactory.eINSTANCE.createEReference();
		EReference ref2 = EcoreFactory.eINSTANCE.createEReference();

		resource.getContents().add(ref1);
		resource2.getContents().add(ref2);
		
		InverseMaintenanceAdapter ia = new InverseMaintenanceAdapter(true);
		ref1.eAdapters().add(ia);
		ia.propagate();
		
		InverseMaintenanceAdapter ia2 = new InverseMaintenanceAdapter(true);
		ref2.eAdapters().add(ia2);
				
		ref1.setEOpposite(ref2);
		
		// Now the tests.
		assertTrue(ia.isPropagated());
		assertFalse(ia2.isPropagated());		
		assertTrue(ia2.getFirstReferencedBy(EcorePackage.eINSTANCE.getEReference_EOpposite()) == ref1);		
	}


	/**
	 * Test that adding an adapter and propagate being in a resource causes propagation. Will point
	 * to one that is in same resource.
	 * (A2d) 
	 */
	public void testAddResourceSameResourceOther() {
		Resource resource = new ResourceImpl();
		
		// Use an EReference for simplicity. Create it, add an EOpposite to it and see that it doesn't propagate.
		EReference ref1 = EcoreFactory.eINSTANCE.createEReference();
		EReference ref2 = EcoreFactory.eINSTANCE.createEReference();

		resource.getContents().add(ref1);
		resource.getContents().add(ref2);
		InverseMaintenanceAdapter ia = new InverseMaintenanceAdapter();
		ref1.eAdapters().add(ia);
		ia.propagate();
		
		ref1.setEOpposite(ref2);
		
		// Now the tests.
		assertTrue(ia.isPropagated());
		InverseMaintenanceAdapter ia2 = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(ref2, InverseMaintenanceAdapter.ADAPTER_KEY);
		assertNotNull(ia2);
		assertFalse(ia2.isPropagated());
		assertTrue(ia2.getFirstReferencedBy(EcorePackage.eINSTANCE.getEReference_EOpposite()) == ref1);		
	}
	/**
	 * Test that adding an adapter and propagate being in a resource causes propagation. Will point
	 * to one that is not contained but has an adapter. 
	 * (A1 with A2c conditions)
	 */
	public void testAddResourceOtherNotContainedWithAdapter() {
		Resource resource = new ResourceImpl();
		
		// Use an EReference for simplicity. Create it, add an EOpposite to it and see that it doesn't propagate.
		EReference ref1 = EcoreFactory.eINSTANCE.createEReference();
		EReference ref2 = EcoreFactory.eINSTANCE.createEReference();
		
		resource.getContents().add(ref1);
				
		InverseMaintenanceAdapter ia = new InverseMaintenanceAdapter();
		ref1.eAdapters().add(ia);
		ia.propagate();
				
		InverseMaintenanceAdapter ia2 = new InverseMaintenanceAdapter();
		ref2.eAdapters().add(ia2);
				
		ref1.setEOpposite(ref2);

		// Now the tests.
		assertTrue(ia2.getFirstReferencedBy(EcorePackage.eINSTANCE.getEReference_EOpposite()) == ref1);		
	}
	
	/**
	 * Test that adding an adapter and propagate being in a resource causes propagation. Will point
	 * to one that is in another resource and doesn't allow crossdoc, but already has an adapter. 
	 * (A1 with A2A conditions) 
	 */
	public void testAddResourceOtherResourceWithAdapter() {
		Resource resource = new ResourceImpl();
		Resource resource2 = new ResourceImpl();
		
		// Use an EReference for simplicity. Create it, add an EOpposite to it and see that it doesn't propagate.
		EReference ref1 = EcoreFactory.eINSTANCE.createEReference();
		EReference ref2 = EcoreFactory.eINSTANCE.createEReference();
		
		resource.getContents().add(ref1);
		resource2.getContents().add(ref2);
		
		InverseMaintenanceAdapter ia = new InverseMaintenanceAdapter();
		ref1.eAdapters().add(ia);
		ia.propagate();
		
		InverseMaintenanceAdapter ia2 = new InverseMaintenanceAdapter();
		ref2.eAdapters().add(ia2);		
		
		ref1.setEOpposite(ref2);
		
		// Now the tests.
		assertTrue(ia.isPropagated());
		assertFalse(ia2.isPropagated());		
		assertTrue(ia2.getFirstReferencedBy(EcorePackage.eINSTANCE.getEReference_EOpposite()) == ref1);				
	}

	/**
	 * Test that adding an adapter and propagate being in a resource causes propagation. Will point
	 * to one that is in same resource.
	 * (A1 with A2d conditions) 
	 */
	public void testAddResourceSameResourceOtherWithAdapter() {
		Resource resource = new ResourceImpl();
		
		// Use an EReference for simplicity. Create it, add an EOpposite to it and see that it doesn't propagate.
		EReference ref1 = EcoreFactory.eINSTANCE.createEReference();
		EReference ref2 = EcoreFactory.eINSTANCE.createEReference();
		
		resource.getContents().add(ref1);
		resource.getContents().add(ref2);
		
		InverseMaintenanceAdapter ia = new InverseMaintenanceAdapter();
		ref1.eAdapters().add(ia);
		ia.propagate();
		
		InverseMaintenanceAdapter ia2 = new InverseMaintenanceAdapter();
		ref2.eAdapters().add(ia2);

		ref1.setEOpposite(ref2);
		
		// Now the tests.
		assertTrue(ia.isPropagated());
		assertFalse(ia2.isPropagated());		
		assertTrue(ia2.getFirstReferencedBy(EcorePackage.eINSTANCE.getEReference_EOpposite()) == ref1);		
	}
}
