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
package org.eclipse.ui.examples.rcp.binding.scenarios;

import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.databinding.BindingException;
import org.eclipse.jface.databinding.IUpdatable;
import org.eclipse.jface.databinding.IUpdatableFactory2;
import org.eclipse.jface.databinding.IValidationContext;
import org.eclipse.jface.databinding.PropertyDescription;
import org.eclipse.jface.databinding.swt.SWTDatabindingContext;
import org.eclipse.jface.examples.binding.emf.EMFUpdatableCollection;
import org.eclipse.jface.examples.binding.emf.EMFUpdatableEList;
import org.eclipse.jface.examples.binding.emf.EMFUpdatableValue;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.examples.rcp.adventure.Account;
import org.eclipse.ui.examples.rcp.adventure.Adventure;
import org.eclipse.ui.examples.rcp.adventure.AdventureFactory;
import org.eclipse.ui.examples.rcp.adventure.AdventurePackage;
import org.eclipse.ui.examples.rcp.adventure.Catalog;
import org.eclipse.ui.examples.rcp.adventure.Category;
import org.eclipse.ui.examples.rcp.adventure.Lodging;
import org.eclipse.ui.examples.rcp.adventure.Transportation;

public class SampleData {

	public static Category WINTER_CATEGORY;

	public static Category SUMMER_CATEGORY;

	public static Adventure BEACH_HOLIDAY;

	public static Adventure RAFTING_HOLIDAY;

	public static Adventure WINTER_HOLIDAY;

	public static Adventure ICE_FISHING;

	public static Lodging FIVE_STAR_HOTEL;

	public static Lodging YOUTH_HOSTEL;

	public static Lodging CAMP_GROUND;

	public static Catalog CATALOG_2005;

	public static Transportation GREYHOUND_BUS;

	public static Transportation EXECUTIVE_JET;

	public static Account PRESIDENT;

	public static Account DENTIST;

	public static Account SANTA_CLAUS;

	static {
		initializeData();
	}

	public static void initializeData() {

		AdventureFactory adventureFactory = AdventurePackage.eINSTANCE
				.getAdventureFactory();

		CATALOG_2005 = adventureFactory.createCatalog();

		// Categories
		WINTER_CATEGORY = adventureFactory.createCategory();
		WINTER_CATEGORY.setName("Freeze Adventures");
		WINTER_CATEGORY.setId("100");
		CATALOG_2005.getCategories().add(WINTER_CATEGORY);

		SUMMER_CATEGORY = adventureFactory.createCategory();
		SUMMER_CATEGORY.setName("Hot Adventures");
		SUMMER_CATEGORY.setId("200");
		CATALOG_2005.getCategories().add(SUMMER_CATEGORY);

		// Adventures
		WINTER_HOLIDAY = adventureFactory.createAdventure();
		WINTER_HOLIDAY.setDescription("Winter holiday in France");
		WINTER_HOLIDAY.setName("Ski Alps");
		WINTER_HOLIDAY.setLocation("Chamonix");
		WINTER_HOLIDAY.setPrice(4000.52d);
		WINTER_HOLIDAY.setId("150");
		WINTER_CATEGORY.getAdventures().add(WINTER_HOLIDAY);

		ICE_FISHING = adventureFactory.createAdventure();
		ICE_FISHING.setDescription("Ice Fishing in Helsinki");
		ICE_FISHING.setName("Ice Fishing");
		ICE_FISHING.setLocation("Finland");
		ICE_FISHING.setPrice(375.55d);
		WINTER_CATEGORY.getAdventures().add(ICE_FISHING);

		BEACH_HOLIDAY = adventureFactory.createAdventure();
		BEACH_HOLIDAY.setDescription("Beach holiday in Spain");
		BEACH_HOLIDAY.setName("Playa");
		BEACH_HOLIDAY.setLocation("Lloret de Mar");
		BEACH_HOLIDAY.setPrice(2000.52d);
		BEACH_HOLIDAY.setId("250");
		SUMMER_CATEGORY.getAdventures().add(BEACH_HOLIDAY);

		RAFTING_HOLIDAY = adventureFactory.createAdventure();
		RAFTING_HOLIDAY
				.setDescription("White water rafting on the Ottawa river");
		RAFTING_HOLIDAY.setName("Whitewater");
		RAFTING_HOLIDAY.setLocation("Ottawa");
		RAFTING_HOLIDAY.setPrice(8000.52d);
		RAFTING_HOLIDAY.setId("270");
		SUMMER_CATEGORY.getAdventures().add(RAFTING_HOLIDAY);

		// Lodgings
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

		// Transporation
		GREYHOUND_BUS = adventureFactory.createTransportation();
		GREYHOUND_BUS.setArrivalTime("14:30");
		CATALOG_2005.getTransportations().add(GREYHOUND_BUS);
		EXECUTIVE_JET = adventureFactory.createTransportation();
		EXECUTIVE_JET.setArrivalTime("11:10");
		CATALOG_2005.getTransportations().add(EXECUTIVE_JET);

		// Accounts
		PRESIDENT = adventureFactory.createAccount();
		PRESIDENT.setFirstName("George");
		PRESIDENT.setLastName("Bush");
		PRESIDENT.setState("TX");
		PRESIDENT.setPhone("1112223333");
		PRESIDENT.setCountry("U.S.A");
		DENTIST = adventureFactory.createAccount();
		DENTIST.setFirstName("Tooth");
		DENTIST.setLastName("Fairy");
		DENTIST.setState("CA");
		DENTIST.setPhone("4543219876");
		DENTIST.setCountry("PainLand");
		SANTA_CLAUS = adventureFactory.createAccount();
		SANTA_CLAUS.setFirstName("Chris");
		SANTA_CLAUS.setLastName("Chringle");
		SANTA_CLAUS.setState("WI");
		SANTA_CLAUS.setPhone("8617429856");
		SANTA_CLAUS.setCountry("NorthPole");
		CATALOG_2005.getAccounts().add(PRESIDENT);
		CATALOG_2005.getAccounts().add(DENTIST);
		CATALOG_2005.getAccounts().add(SANTA_CLAUS);

	}

	public static SWTDatabindingContext getSWTtoEMFDatabindingContext(
			Control aControl) {

		SWTDatabindingContext dbc = new SWTDatabindingContext(null, aControl);

		IUpdatableFactory2 emfFactory = new IUpdatableFactory2() {
			public IUpdatable createUpdatable(Map properties,
					Object description, IValidationContext validationContext)
					throws BindingException {
				if (description instanceof PropertyDescription) {
					PropertyDescription propertyDescription = (PropertyDescription) description;
					if (propertyDescription.getObject() instanceof EObject) {
						EObject eObject = (EObject) propertyDescription
								.getObject();
						EStructuralFeature attr;
						if (propertyDescription.getPropertyID() instanceof EStructuralFeature)
							attr = (EStructuralFeature) propertyDescription
									.getPropertyID();
						else
							attr = eObject.eClass().getEStructuralFeature(
									(String) propertyDescription
											.getPropertyID());
						if (attr.isMany()) {
							return new EMFUpdatableCollection(eObject, attr,
									!attr.isChangeable());
						} else
							return new EMFUpdatableValue(eObject, attr, !attr
									.isChangeable());
					}
					if (propertyDescription.getObject() instanceof EList) {
						return new EMFUpdatableEList(
								(EList) propertyDescription.getObject(), true);
					}
				}
				return null;
			}

		};
		dbc.addUpdatableFactory2(emfFactory);

		return dbc;

	}

}
