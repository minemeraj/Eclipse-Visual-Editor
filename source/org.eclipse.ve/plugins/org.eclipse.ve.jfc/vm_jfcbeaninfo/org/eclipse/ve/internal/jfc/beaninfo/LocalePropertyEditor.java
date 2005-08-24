/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.beaninfo;
/*
 *  $RCSfile: LocalePropertyEditor.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:38:12 $ 
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.beans.*;
import java.util.*;


public class LocalePropertyEditor extends JPanel implements ListSelectionListener, PropertyChangeListener{
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -795400473092065047L;

	private java.util.Locale fLocaleValue = null;
		
	private  LocaleLanguage[] fLocaleLanguages = null;
	private  LocaleCountry[] fLocaleCountries = null;
	private  LocaleVariant[] fLocaleVariants = null;
	
	private JLabel languageLabel = new JLabel(VisualBeanInfoMessages.getString("Locale.Language")); //$NON-NLS-1$
	private JLabel countryLabel = new JLabel(VisualBeanInfoMessages.getString("Locale.Country")); //$NON-NLS-1$
	private JLabel variantLabel = new JLabel(VisualBeanInfoMessages.getString("Locale.Variant")); //$NON-NLS-1$
	private JTextField localeField = new JTextField();
	

	private JList languageList = null;
	private JScrollPane languagePane = null;
	private JList countryList = null;
	private JScrollPane countryPane = null;
	private JList variantList = null;
	private JScrollPane variantPane = null;
	protected transient PropertyChangeSupport fPropertyChange = new PropertyChangeSupport(this);

/**
 * LocalePropertyEditor constructor comment.
 */
public LocalePropertyEditor() {
	super();
	this.initialize();
}
/**
 * LocalePropertyEditor constructor comment.
 * @param layout java.awt.LayoutManager
 */
public LocalePropertyEditor(Locale locale) {
	super();
	initialize();
	setLocaleValue( locale );
}

private void createLocaleValues(){	
	if (fLocaleLanguages != null)
		return;
		
	Locale locales[] = Locale.getAvailableLocales();
	int count = locales.length;
	
	Vector myLocales = new Vector();
	for(int i = 0; i < count; i++){
		Locale fLocale = locales[i];
	
	
		LocaleVariant fVariant = new LocaleVariant();
		fVariant.setID(fLocale.getVariant());
		fVariant.setName(fLocale.getDisplayVariant());
		
		LocaleCountry fCountry = new LocaleCountry();
		fCountry.setID(fLocale.getCountry());
		fCountry.setName(fLocale.getDisplayCountry());
		fCountry.addVariant(fVariant);
		
		boolean isDuplication = false;
		//If a same language is already in the vector, just add the country&variants
		for (int j = 0; j < myLocales.size(); j++){
			if (((LocaleLanguage)myLocales.elementAt(j)).getID() == fLocale.getLanguage()){
				((LocaleLanguage)(myLocales.elementAt(j))).addCountry(fCountry);
				isDuplication = true;
			}
		}
		
		if (isDuplication == false){
			LocaleLanguage fLanguage = new LocaleLanguage();
			fLanguage.setID(fLocale.getLanguage());
			fLanguage.setName(fLocale.getDisplayLanguage());
			fLanguage.addCountry(fCountry);
			myLocales.addElement(fLanguage);
		}
		
	}
	int fSize = myLocales.size();
	fLocaleLanguages = new LocaleLanguage[fSize];
	Enumeration enumer = myLocales.elements();
	for (int i = 0; i < fSize && enumer.hasMoreElements(); i++){
		fLocaleLanguages[i] = (LocaleLanguage)enumer.nextElement();
	}
	//need to sort fLocaleLanguages according to language names 
	sortLocaleLanguages();
}

public void sortLocaleLanguages(){
	for (int i = 0; i < fLocaleLanguages.length; i++){
		String s = fLocaleLanguages[i].getName();
		int min = i;
		if ( s != null && !s.equals(" ")){ //$NON-NLS-1$
			for (int j = i; j < fLocaleLanguages.length; j++){
				String newString = fLocaleLanguages[j].getName();
				if (newString.compareTo(s) < 0){
					min = j;
					s = fLocaleLanguages[min].getName();
				}
			}
		}
		LocaleLanguage swap = fLocaleLanguages[i];
		fLocaleLanguages[i] = fLocaleLanguages[min];
		fLocaleLanguages[min] = swap;
	}

}


private JList getLanguageList(){
	if (languageList == null){
		try{
			languageList = new JList();
			languageList.setName("LanguageList"); //$NON-NLS-1$
			DefaultListModel languageModel = new DefaultListModel();
			languageList.setModel(languageModel);
			for (int i = 0; i < fLocaleLanguages.length; i++){
				languageModel.addElement(fLocaleLanguages[i]);
			}
			languageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			String languageNames[] = new String[fLocaleLanguages.length];
			for (int i = 0; i < fLocaleLanguages.length; i++){
				languageNames[i] = fLocaleLanguages[i].getName();
			}

			languageList.setCellRenderer(new LanguageCellRenderer(languageNames));
			languageList.addListSelectionListener(this);
		}catch(Throwable ivjExc){
			handleException(ivjExc);
		}
	}

	return languageList;
}

private JScrollPane getLanguagePane(){
	if (languagePane == null){
		languagePane = new JScrollPane(getLanguageList());
		languagePane.setBackground(SystemColor.control);
	}
	return languagePane;
}

private JList getCountryList(){
	if (countryList == null){
		try{
			countryList = new JList();
			countryList.setName("CountryList"); //$NON-NLS-1$
			countryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			countryList.addListSelectionListener(this);
			//countryList.setCellRenderer(new CountryCellRenderer(countryNames));
		}catch(Throwable ivjExc){
			handleException(ivjExc);
		}
	}
	
	return countryList;
}

private JScrollPane getCountryPane(){
	if(countryPane == null){
		countryPane = new JScrollPane(getCountryList());
		countryPane.setBackground(SystemColor.control);
	}
	return countryPane;
}

private JList getVariantList(){
	if (variantList == null){
		try{
			variantList = new JList();
			variantList.setName("LanguageList"); //$NON-NLS-1$
			
			variantList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			variantList.addListSelectionListener(this);
			//variantList.setCellRenderer(new CountryCellRenderer(variantNames));
		
		}catch(Throwable ivjExc){
			handleException(ivjExc);
		}
	}
	
	return variantList;
}

private JScrollPane getVariantPane(){
	if(variantPane == null){
		variantPane = new JScrollPane(getVariantList());
	}
	return variantPane;
}


public void initialize() {
	
	createLocaleValues();
	
	setBorder( BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
	        VisualBeanInfoMessages.getString("Locale.TitleBorder"))); //$NON-NLS-1$
	setName( "LocalePropertyEditor" ); //$NON-NLS-1$
	setBackground( SystemColor.control);
	setLayout(new GridBagLayout());

	languageLabel.setBackground(SystemColor.control);
	countryLabel.setBackground(SystemColor.control);
	variantLabel.setBackground(SystemColor.control);
	
	GridBagConstraints constraintsLanguageLabel = new GridBagConstraints();
	constraintsLanguageLabel.gridx = 0; constraintsLanguageLabel.gridy = 0;
	constraintsLanguageLabel.gridwidth = 1; constraintsLanguageLabel.gridheight = 1;
	constraintsLanguageLabel.fill = GridBagConstraints.HORIZONTAL;
	constraintsLanguageLabel.insets = new Insets(2, 2, 2, 2);
	constraintsLanguageLabel.weightx = 2.0;
	constraintsLanguageLabel.weighty = 1.0;
	this.add(languageLabel, constraintsLanguageLabel);
	
	GridBagConstraints constraintsCountryLabel = new GridBagConstraints();
	constraintsCountryLabel.gridx = 1; constraintsCountryLabel.gridy = 0;
	constraintsCountryLabel.gridwidth = 1; constraintsCountryLabel.gridheight = 1;
	constraintsCountryLabel.fill = GridBagConstraints.HORIZONTAL;
	constraintsCountryLabel.insets = new Insets(2, 2, 2, 2);
	constraintsCountryLabel.weightx = 2.0;
	constraintsCountryLabel.weighty = 1.0;
	this.add(countryLabel, constraintsCountryLabel);

	GridBagConstraints constraintsVariantLabel = new GridBagConstraints();
	constraintsVariantLabel.gridx = 2; constraintsVariantLabel.gridy = 0;
	constraintsVariantLabel.gridwidth = 1; constraintsVariantLabel.gridheight = 1;
	constraintsVariantLabel.fill = GridBagConstraints.HORIZONTAL;
	constraintsVariantLabel.insets = new Insets(2, 2, 2, 2);
	constraintsVariantLabel.weightx = 2.0;
	constraintsVariantLabel.weighty = 1.0;
	this.add(variantLabel, constraintsVariantLabel);

	GridBagConstraints constraintsLanguagePane = new GridBagConstraints();
	constraintsLanguagePane.gridx = 0; constraintsLanguagePane.gridy = 1;
	constraintsLanguagePane.gridwidth = 1; constraintsLanguagePane.gridheight = 1;
	constraintsLanguagePane.fill = GridBagConstraints.BOTH;
	constraintsLanguagePane.insets = new Insets(2, 2, 2, 2);
	constraintsLanguagePane.weightx = 2.0;
	constraintsLanguagePane.weighty = 4.0;
	this.add(getLanguagePane(), constraintsLanguagePane);

	GridBagConstraints constraintsCountryPane = new GridBagConstraints();
	constraintsCountryPane.gridx = 1; constraintsCountryPane.gridy = 1;
	constraintsCountryPane.gridwidth = 1; constraintsCountryPane.gridheight = 1;
	constraintsCountryPane.fill = GridBagConstraints.BOTH;
	constraintsCountryPane.insets = new Insets(2, 2, 2, 2);
	constraintsCountryPane.weightx = 2.0;
	constraintsCountryPane.weighty = 4.0;
	this.add(getCountryPane(), constraintsCountryPane);
	
	GridBagConstraints constraintsVariantPane = new GridBagConstraints();
	constraintsVariantPane.gridx = 2; constraintsVariantPane.gridy = 1;
	constraintsVariantPane.gridwidth = 1; constraintsVariantPane.gridheight = 1;
	constraintsVariantPane.fill = GridBagConstraints.BOTH;
	constraintsVariantPane.insets = new Insets(2, 2, 2, 2);
	constraintsVariantPane.weightx = 2.0;
	constraintsVariantPane.weighty = 4.0;
	this.add(getVariantPane(), constraintsVariantPane);
	
	GridBagConstraints constraintsLocaleField = new GridBagConstraints();
	constraintsLocaleField.gridx = 0; constraintsLocaleField.gridy = 2;
	constraintsLocaleField.gridwidth = 1; constraintsLocaleField.gridheight = 1;
	constraintsLocaleField.fill = GridBagConstraints.HORIZONTAL;
	constraintsLocaleField.insets = new Insets(2, 2, 2, 2);
	constraintsLocaleField.weightx = 2.0;
	constraintsLocaleField.weighty = 1.0;
	this.add(localeField, constraintsLocaleField);
	localeField.setBackground(SystemColor.control);
	localeField.setEditable(false);

}

public void actionPerformed(ActionEvent e){
}

public String getLocaleInitializationString() {
	return "new java.util.Locale(\""+ fLocaleValue.getLanguage() +"\", \""+ fLocaleValue.getCountry() +"\", \""+ fLocaleValue.getVariant() +"\")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
}


public Locale getLocaleValue() {
	return fLocaleValue;
}

public void setLocaleValue(Locale locale) {
	fLocaleValue = locale;
	
	//select the language and generate country list dynamically
	if (fLocaleLanguages != null){
		for (int languageIndex = 0; languageIndex < fLocaleLanguages.length; languageIndex++){
			if (fLocaleLanguages[languageIndex].getName() == fLocaleValue.getDisplayLanguage()){
				getLanguageList().setSelectedIndex(languageIndex);
				//getLanguageList().setVisibleRowCount(16);
				//getLanguageList().ensureIndexIsVisible(languageIndex);
				//getLanguageList().setSelectedValue(fLocaleLanguages[languageIndex], true);
				//the selection handling here will mess up the fLocaleValue and setLocaleField 
				//is called and reset to something different from the method argument locale
			}
		}
	}
	
	//select country and generate variant list dynamically
	if (fLocaleCountries != null ){
		for (int countryIndex = 0; countryIndex < fLocaleCountries.length; countryIndex++){
			String s = fLocaleCountries[countryIndex].getName();
			if (s !=null && !s.equals(" ") && s.equals(locale.getDisplayCountry())){ //$NON-NLS-1$
				getCountryList().setSelectedIndex(countryIndex);
				getCountryList().ensureIndexIsVisible(countryIndex);
			}
		}
	}
	
	//select variant if there is any for fLocaleValue
	if (fLocaleVariants != null){
		for (int variantIndex = 0; variantIndex < fLocaleVariants.length; variantIndex++){
			String s = fLocaleVariants[variantIndex].getName();
			if (s != null && !s.equals(" ") && s.equals(locale.getDisplayVariant())){ //$NON-NLS-1$
				getVariantList().setSelectedIndex(variantIndex);
				getVariantList().ensureIndexIsVisible(variantIndex);
			}
		}
	}
	
	//have to set the field after all selecting and also with the argument locale instead of
	//fLocaleValue, since in the selection handling the fLocaleValue get changed/messed up
	//so far leave it like this, debug later
	setLocaleField(locale);

}

public void setLocaleField(Locale locale){
	localeField.setText(locale.toString());
}


public void firePropertyChange(String propertyName, Object oldValue, Object newValue){
	if (fPropertyChange == null){
		fPropertyChange = new PropertyChangeSupport(this);
	}
	fPropertyChange.firePropertyChange(propertyName, oldValue, newValue);
}

public void valueChanged(ListSelectionEvent e){
	String selectedLanguageID = ""; //$NON-NLS-1$
	String selectedCountryID = ""; //$NON-NLS-1$
	String selectedVariantID = ""; //$NON-NLS-1$
	int selectedLanguageIndex;
	int selectedCountryIndex;
	int selectedVariantIndex;
	
	//reset country and variant list model every time to clean up the old values
	
	
	if (e.getSource() == getLanguageList()){
		selectedLanguageIndex = getLanguageList().getSelectedIndex();
		if (selectedLanguageIndex >= 0){
			LocaleLanguage selectedLanguage = fLocaleLanguages[selectedLanguageIndex];
		
			selectedLanguageID = selectedLanguage.getID();
		
			//***somewhere here the country list for English is messed up and they 
			//have a null country in the list
			//and the fLocaleValue get messed up too and the default en_US becomes
			//just en_, debug this soon
			
			//show the coutries for this selected language in countryList
			fLocaleCountries = selectedLanguage.getCountries();
			int countryCount = fLocaleCountries.length;
			String[] countryNames = new String[countryCount];
	
			DefaultListModel countryModel = new DefaultListModel();
			getCountryList().setModel(countryModel);
			for (int i = 0; i < countryCount; i++){
				if (fLocaleCountries[i].getName() != null && !(fLocaleCountries[i].getName().equals(" "))){ //$NON-NLS-1$
					countryNames[i] = fLocaleCountries[i].getName();
					countryModel.addElement(countryNames[i]);
				}
			}
			getCountryList().setCellRenderer(new CountryCellRenderer(countryNames));
			getVariantList().setModel(new DefaultListModel());
		}
	}
	
	if (e.getSource() == getCountryList()){
		//user must first choose a language before choosing a country, so 
		//there must be a language chosen already
		selectedLanguageIndex = getLanguageList().getSelectedIndex();
		if (selectedLanguageIndex >= 0){
			LocaleLanguage selectedLanguage = fLocaleLanguages[selectedLanguageIndex];
			selectedLanguageID = selectedLanguage.getID();
		}
		
		selectedCountryIndex = getCountryList().getSelectedIndex();
		if (selectedCountryIndex >= 0){
			LocaleCountry selectedCountry = fLocaleCountries[selectedCountryIndex];
			selectedCountryID = selectedCountry.getID();
	
			//show the variants for this selected country in variantList
			fLocaleVariants = selectedCountry.getVariants();
			int variantCount = fLocaleVariants.length;
			String[] variantNames = new String[variantCount];
		
			DefaultListModel variantModel = new DefaultListModel();
			getVariantList().setModel(variantModel);
			
			for (int i = 0; i < variantCount; i++){
				if (fLocaleVariants[i].getName() != null && !(fLocaleVariants[i].getName().equals(" "))){ //$NON-NLS-1$
				variantNames[i] = fLocaleVariants[i].getName();
				variantModel.addElement(variantNames[i]);
				}
			}
			getVariantList().setCellRenderer(new VariantCellRenderer(variantNames));
		}
	}
	
	if (e.getSource() == getVariantList()){
		//user must have chosen a language and a country already before choosing any variant
		selectedLanguageIndex = getLanguageList().getSelectedIndex();
		if (selectedLanguageIndex >= 0){
			LocaleLanguage selectedLanguage = fLocaleLanguages[selectedLanguageIndex];
			selectedLanguageID = selectedLanguage.getID();
		}
		
		selectedCountryIndex = getCountryList().getSelectedIndex();
		if (selectedCountryIndex >= 0){
			LocaleCountry selectedCountry = fLocaleCountries[selectedCountryIndex];
			selectedCountryID = selectedCountry.getID();
		}

		selectedVariantIndex = getVariantList().getSelectedIndex();
		if (selectedVariantIndex >= 0){
			LocaleVariant selectedVariant = fLocaleVariants[selectedVariantIndex];
			selectedVariantID = selectedVariant.getID();
		}
	}
	fLocaleValue = new Locale(selectedLanguageID, selectedCountryID, selectedVariantID);
	setLocaleField(fLocaleValue);
	firePropertyChange("localeValue", null, getLocaleValue());  //$NON-NLS-1$
}

public void propertyChange(PropertyChangeEvent e) {
	if (e.getPropertyName().equals("localeValue")) { //$NON-NLS-1$
		setLocaleField(fLocaleValue);
	}
		
}

public synchronized void addPropertyChangeListener(PropertyChangeListener listener){
	fPropertyChange.addPropertyChangeListener(listener);
}


public synchronized void removePropertyChangeListener(PropertyChangeListener listener){
	fPropertyChange.removePropertyChangeListener(listener);
}

private void handleException(Throwable exc){
}

public Dimension getPreferredSize(){
	return new Dimension(450,250);
}

}
