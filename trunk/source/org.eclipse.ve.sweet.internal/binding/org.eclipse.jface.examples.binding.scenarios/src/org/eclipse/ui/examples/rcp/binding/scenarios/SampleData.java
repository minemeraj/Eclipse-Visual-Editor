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
 *  $Revision: 1.4 $  $Date: 2005-10-17 23:06:29 $ 
 */
package org.eclipse.ui.examples.rcp.binding.scenarios;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.jface.binding.*;
import org.eclipse.jface.binding.swt.SWTDatabindingService;
import org.eclipse.jface.examples.binding.emf.EMFUpdatableCollection;
import org.eclipse.jface.examples.binding.emf.EMFUpdatableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.examples.rcp.adventure.*;
 
public class SampleData {
	
	public static Adventure WINTER_HOLIDAY;
	public static Lodging FIVE_STAR_HOTEL;
	public static Lodging YOUTH_HOSTEL;
	public static Lodging CAMP_GROUND; 	
	public static Catalog CATALOG_2005;
	public static Transportation GREYHOUND_BUS;
	public static Transportation EXECUTIVE_JET;	

	static{
		initializeData();
	}
	
	public static void initializeData(){
	
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
		
		GREYHOUND_BUS = adventureFactory.createTransportation();
		GREYHOUND_BUS.setArrivalTime("14:30");
		CATALOG_2005.getTransportations().add(GREYHOUND_BUS);
		
		EXECUTIVE_JET = adventureFactory.createTransportation();
		EXECUTIVE_JET.setArrivalTime("11:10");
		CATALOG_2005.getTransportations().add(EXECUTIVE_JET);
		
	}
	
	public static DatabindingService getSWTtoEMFDatabindingService(Control aControl){
		
		DatabindingService dbs = new SWTDatabindingService(aControl, SWT.Modify, SWT.Modify);

		IUpdatableFactory emfFactory = new IUpdatableFactory(){		
			public IUpdatable createUpdatable(Object object, Object attribute) {
				EObject eObject = (EObject)object;
				EStructuralFeature attr;
				if (attribute instanceof EStructuralFeature)
					attr = (EStructuralFeature)attribute;
				else
					attr = eObject.eClass().getEStructuralFeature((String)attribute);
				if (attr.isMany()) {					
					return new EMFUpdatableCollection(eObject, attr, !attr.isChangeable());
				}
				else
					return new EMFUpdatableValue(eObject , attr, !attr.isChangeable());
					
			}
		
		};				
		dbs.addUpdatableFactory(EObjectImpl.class, emfFactory);
				
		return dbs;
		
	}
	
}
