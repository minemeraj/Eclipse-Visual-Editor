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
 *  $RCSfile: SampleData.java,v $
 *  $Revision: 1.3 $  $Date: 2005-10-17 23:06:29 $ 
 */
package org.eclipse.jface.examples.binding.scenarios.desired;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.jface.binding.DatabindingService;
import org.eclipse.jface.binding.IUpdatableTable;
import org.eclipse.jface.binding.IUpdatableTableFactory;
import org.eclipse.jface.binding.IUpdatableValue;
import org.eclipse.jface.binding.IUpdatableValueFactory;
import org.eclipse.jface.binding.swt.SWTDatabindingService;
import org.eclipse.jface.examples.binding.emf.EMFUpdatableTable;
import org.eclipse.jface.examples.binding.emf.EMFUpdatableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.examples.rcp.adventure.Adventure;
import org.eclipse.ui.examples.rcp.adventure.AdventureFactory;
import org.eclipse.ui.examples.rcp.adventure.AdventurePackage;
import org.eclipse.ui.examples.rcp.adventure.Catalog;
import org.eclipse.ui.examples.rcp.adventure.Lodging;
 
public class SampleData {
	
	public static Adventure WINTER_HOLIDAY;
	public static Lodging FIVE_STAR_HOTEL;
	public static Lodging YOUTH_HOSTEL;
	public static Lodging CAMP_GROUND; 	
	public static Catalog CATALOG_2005;

	static{
		initializeData();
	}
	
	private static void initializeData(){
	
		AdventureFactory adventureFactory = AdventurePackage.eINSTANCE.getAdventureFactory();
		
		CATALOG_2005 = adventureFactory.createCatalog();
		
		WINTER_HOLIDAY = adventureFactory.createAdventure();
		WINTER_HOLIDAY.setDescription("Winter holiday in France");
		WINTER_HOLIDAY.setName("Ski Alps");
		WINTER_HOLIDAY.setLocation("Chamonix");
		WINTER_HOLIDAY.setPrice(4000.52d);
		
		FIVE_STAR_HOTEL = adventureFactory.createLodging();
		FIVE_STAR_HOTEL.setDescription("Deluxe palace");
		FIVE_STAR_HOTEL.setName("Flashy");
		YOUTH_HOSTEL = adventureFactory.createLodging();
		YOUTH_HOSTEL.setDescription("Youth Hostel");
		YOUTH_HOSTEL.setName("Basic");
		CAMP_GROUND = adventureFactory.createLodging();
		CAMP_GROUND.setDescription("Camp ground");
		CAMP_GROUND.setName("WetAndCold");
		
		CATALOG_2005.getLodgings().add(FIVE_STAR_HOTEL);
		CATALOG_2005.getLodgings().add(YOUTH_HOSTEL);
		CATALOG_2005.getLodgings().add(CAMP_GROUND);
		
		WINTER_HOLIDAY.setDefaultLodging(YOUTH_HOSTEL);
		
	}
	
	public static DatabindingService getSWTtoEMFDatabindingService(Control aControl){
		
		DatabindingService dbs = new SWTDatabindingService(aControl, SWT.None, SWT.FocusOut);

		IUpdatableValueFactory emfValueFactory = new IUpdatableValueFactory() {
			public IUpdatableValue createUpdatableValue(Object object,Object attribute) {
				if(attribute instanceof EStructuralFeature){
					EStructuralFeature attr = (EStructuralFeature) attribute;
					return new EMFUpdatableValue((EObject) object, attr, !attr.isChangeable());
				} else {
					EObject eObject = (EObject)object;
					EStructuralFeature attr = eObject.eClass().getEStructuralFeature((String)attribute);
					return new EMFUpdatableValue(eObject , attr, !attr.isChangeable());
				} 
			}
		};
		dbs.addUpdatableValueFactory(EObjectImpl.class, emfValueFactory);
		
		IUpdatableTableFactory emfTableFactory = new IUpdatableTableFactory(){
			public IUpdatableTable createUpdatableTable(Object object, Object attribute) {
				if(attribute instanceof String){					
					return new EMFUpdatableTable((EObject)object,(String)attribute,null);
				} else {
					EStructuralFeature attr = (EStructuralFeature) attribute;
					return new EMFUpdatableTable((EObject)object,attr.getName(),null);					
				}
			}			
		};
		
		dbs.addUpdatableTableFactory(EObjectImpl.class, emfTableFactory);
				
		return dbs;
		
	}
	
}
