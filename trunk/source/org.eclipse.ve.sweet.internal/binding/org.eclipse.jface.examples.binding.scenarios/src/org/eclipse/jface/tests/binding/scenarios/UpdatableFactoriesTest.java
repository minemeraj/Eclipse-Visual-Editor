/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  Created Oct 21, 2005 by Gili Mendel
 * 
 *  $RCSfile: UpdatableFactoriesTest.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-21 19:35:15 $ 
 */
 
package org.eclipse.jface.tests.binding.scenarios;

import org.eclipse.jface.binding.*;
 

public class UpdatableFactoriesTest extends ScenariosTestCase {

	
	interface Root {};
	interface None extends Root{};
	interface Middle extends None {};
	interface StandAlone{};
	class  RootClass implements Root {};
	class NoneClass implements None{};
	class MiddleClass implements Middle{};
	class AllClass implements StandAlone, Middle, Root {};
	
	class MiddleChild extends MiddleClass {};
	
    interface TestIUpdatable extends IUpdatable {
    	public Class getType();
    }
	
	class Factory implements IUpdatableFactory{
		Class c;
		public Factory (Class c) {
			this.c = c;
		}
		public IUpdatable createUpdatable(Object object, Object attribute) {			
			return new TestIUpdatable(){
				public void dispose() {}			
				public void removeChangeListener(IChangeListener changeListener) {}			
				public void addChangeListener(IChangeListener changeListener) {}
				public Class getType() { return c; } 
			};
		}	
	}
	
	IUpdatableFactory root = new Factory(Root.class), middle = new Factory(Middle.class), sa = new Factory(StandAlone.class), factory = new Factory(Object.class);
	
			
	protected Class getFactoryType(Object src) throws BindingException {
		TestIUpdatable u = (TestIUpdatable) getDbc().createUpdatable(src, "n/a");
		return u.getType();
	}
	
	 public void test_factoryRegistration() throws BindingException {
		 
		 getDbc().addUpdatableFactory(Root.class, root);
		 getDbc().addUpdatableFactory(Middle.class, middle);
		 
		 // Direct mapping
		 assertEquals(getFactoryType(new RootClass()), Root.class);
		 assertEquals(getFactoryType(new MiddleClass()), Middle.class);
		 
		 // Inherent interface
		 assertEquals(getFactoryType(new NoneClass()), Root.class);
		 
		 // AllClass inherent interface
		 assertEquals(getFactoryType(new AllClass()), Middle.class);
		 
		 // class inheretence 
		 assertEquals(getFactoryType(new MiddleChild()), Middle.class);
		 
		 // Direct, first interface
		 getDbc().addUpdatableFactory(StandAlone.class, sa);
		 assertEquals(getFactoryType(new AllClass()), StandAlone.class);
		 
		 // Class based contribution.
		 getDbc().addUpdatableFactory(AllClass.class, factory);
		 assertEquals(getFactoryType(new AllClass()), Object.class);
		 
		 
		 
	 }
		 
	

}