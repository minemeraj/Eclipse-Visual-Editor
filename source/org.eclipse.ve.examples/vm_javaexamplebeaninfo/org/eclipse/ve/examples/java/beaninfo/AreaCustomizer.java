package org.eclipse.ve.examples.java.beaninfo;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;

import org.eclipse.ve.examples.java.vm.Area;
/**
 * This customizer updates the Area bean and signals propertyChangeSupport events
 * when it does so
 */
public class AreaCustomizer extends Panel implements Customizer {
	protected PropertyChangeSupport pcSupport = new PropertyChangeSupport(this);
	protected Area fArea;
	private java.awt.List fColorList;
	private TextField fTxtFldBorderWidth;
	private Checkbox fOvalCheckbox;
	private Checkbox fNoneCheckbox;
	private Checkbox fDiamondCheckbox;
	private static Hashtable fColors = new Hashtable(5);
	static {
		fColors.put("red",Color.red);
		fColors.put("blue",Color.blue);
		fColors.put("green",Color.green);
		fColors.put("yellow",Color.yellow);
		fColors.put("black",Color.black);
		fColors.put("cyan",Color.cyan);
		fColors.put("orange",Color.orange);
		fColors.put("darkGray",Color.darkGray);
		fColors.put("gray",Color.gray);
		fColors.put("lightGray",Color.lightGray);
		fColors.put("magenta",Color.magenta);
		fColors.put("pink",Color.pink);
		fColors.put("white",Color.white);
	}
/**
 * AreaCustomizer constructor comment.
 */
public AreaCustomizer() {
	initialize();
}
/**
 * Add a checkbox listener to know when the selection changes
 * The same listener is used for all of the check boxes because updateShape() queries the state
 */
protected void addCheckboxListeners(){

	ItemListener checkBoxListener = new ItemListener(){
		public void itemStateChanged(ItemEvent anEvent){
			updateShape();
		}
	};
	fNoneCheckbox.addItemListener(checkBoxListener);
	fOvalCheckbox.addItemListener(checkBoxListener);
	fDiamondCheckbox.addItemListener(checkBoxListener);
}
/**
 * Add a list listener to know when the selection changes
 */
protected void addListListener(){

	fColorList.addItemListener(new ItemListener(){
		public void itemStateChanged(ItemEvent anEvent){
			// Get the selected color
			Color selectedColor = (Color) fColors.get(fColorList.getSelectedItem());
			// Change the color of the object
			changeFillColor(selectedColor);
			// Fire a change event
			pcSupport.firePropertyChange("fillColor",null,selectedColor);
		}
	});

}
/**
 * Add the property change listener 
 */
public void addPropertyChangeListener(PropertyChangeListener aPCL) {
	pcSupport.addPropertyChangeListener(aPCL);
}
/**
 * Add a list listener to know when the selection changes
 */
protected void addTextBoxListener(){

	fTxtFldBorderWidth.addTextListener(new TextListener(){
		public void textValueChanged(TextEvent anEvent){
			// Set the border width
			Integer newValue = new Integer(fTxtFldBorderWidth.getText());
			changeBorderWidth(newValue.intValue());
			// Signal an event
			pcSupport.firePropertyChange("border",null,newValue);
		}
	});

}
/**
 * Change the border width directly
 */
protected void changeBorderWidth(int borderValue){

	fArea.setBorderWidth(borderValue);
	pcSupport.firePropertyChange("borderValue" , null , new Integer(borderValue));

}
/**
 * Apply then change directly
 */
protected void changeFillColor(Color aColor){

	fArea.setFillColor(aColor);
	pcSupport.firePropertyChange("fillColor" , null , aColor);

}
/**
 * Change the shape directly
 */
protected void changeShape(int shapeValue){

	fArea.setShape(shapeValue);
	pcSupport.firePropertyChange("shape" , null , new Integer(shapeValue));
	
}
protected void initialize(){
	setLayout(new GridBagLayout());
	Insets inset = new Insets(4,4,4,4);
	// Create a label and a text field to enter the border width
	Label label = new Label("Border Width:", Label.CENTER);
	GridBagConstraints labelConstraint = new GridBagConstraints();
	labelConstraint.anchor = GridBagConstraints.WEST;
	labelConstraint.insets = inset;
	add(label,labelConstraint);
	// The text field goes in the second grid column
	fTxtFldBorderWidth = new TextField();
	GridBagConstraints textConstraint = new GridBagConstraints();
	textConstraint.gridx = 1;
	textConstraint.insets = inset;
	textConstraint.fill = GridBagConstraints.HORIZONTAL;
	add(fTxtFldBorderWidth,textConstraint);
	addTextBoxListener();
	// Create a list with the available colors 
	fColorList = new java.awt.List(fColors.size(),false);
	Enumeration enum = fColors.keys();
	while ( enum.hasMoreElements() ) {
		fColorList.add( (String)enum.nextElement() );
	}
	GridBagConstraints listConstraint = new GridBagConstraints();
	listConstraint.gridy = 1;
	listConstraint.gridx = 0;
	listConstraint.gridwidth = 3;
	listConstraint.fill = GridBagConstraints.BOTH;
	listConstraint.insets = inset;
	add(fColorList,listConstraint);
	addListListener();
	// Create three checkboxes for the different types of shape
	CheckboxGroup checkboxGroup = new CheckboxGroup();
	// No Shape
	fNoneCheckbox = new Checkbox("No Shape");
	fNoneCheckbox.setCheckboxGroup(checkboxGroup);
	GridBagConstraints noneCheckboxConstraint = new GridBagConstraints();
	noneCheckboxConstraint.gridy = 2;
	noneCheckboxConstraint.gridx = 0;
	noneCheckboxConstraint.anchor = GridBagConstraints.WEST;
	noneCheckboxConstraint.insets = inset;
	add(fNoneCheckbox,noneCheckboxConstraint);
	// Oval
	fOvalCheckbox = new Checkbox("Oval");
	fOvalCheckbox.setCheckboxGroup(checkboxGroup);
	GridBagConstraints ovalCheckboxConstraint = new GridBagConstraints();
	ovalCheckboxConstraint.gridy = 2;
	ovalCheckboxConstraint.gridx = 1;
	ovalCheckboxConstraint.anchor = GridBagConstraints.WEST;
	ovalCheckboxConstraint.insets = inset;
	add(fOvalCheckbox,ovalCheckboxConstraint);
	// Diamond
	fDiamondCheckbox = new Checkbox("Diamond");
	fDiamondCheckbox.setCheckboxGroup(checkboxGroup);
	GridBagConstraints diamondCheckboxConstraint = new GridBagConstraints();
	diamondCheckboxConstraint.gridy = 2;
	diamondCheckboxConstraint.gridx = 2;
	diamondCheckboxConstraint.anchor = GridBagConstraints.WEST;
	diamondCheckboxConstraint.insets = inset;
	// Add the checkboxes to the panel and listen for when they change
	add(fDiamondCheckbox,diamondCheckboxConstraint);
	addCheckboxListeners();
}
/**
 * Remove the property change listener 
 */
public void removePropertyChangeListener(PropertyChangeListener aPCL) {
	pcSupport.removePropertyChangeListener(aPCL);
}
/**
 * Add the property change listener 
 */
public void setObject(Object anObject) {
	fArea = (Area)anObject;
	// Set the border width to be that of the bean
	fTxtFldBorderWidth.setText("" + fArea.getBorderWidth());
	// Set the color list selection to be that of the bean
	if ( fArea.getFillColor() != null ){
		Enumeration enum = fColors.keys();
		int i=0;
		while ( enum.hasMoreElements() ) {
			String colorName = (String) enum.nextElement();
			Color color = (Color) fColors.get(colorName);
			if ( color.equals(fArea.getFillColor()) ) {
				fColorList.select(i);
				break;
			}
			i++;
		}
	}
	// Set the state checkboxes up
	switch (fArea.getShape()) {
		case Area.NO_SHAPE: {
			fNoneCheckbox.setState(true);
			break;	
		}
		case Area.OVAL: {
			fOvalCheckbox.setState(true);
			break;
		}
		case Area.DIAMOND: {
			fDiamondCheckbox.setState(true);
			break;
		}
	}

}
/**
 * Get the shape from the checkboxes and update the area
 */
protected void updateShape(){

	if ( fNoneCheckbox.getState() ) {
		changeShape(Area.NO_SHAPE);
	} else if ( fOvalCheckbox.getState() ) {
		changeShape(Area.OVAL);
	} else if ( fDiamondCheckbox.getState() ) {
		changeShape(Area.DIAMOND);
	}
}
}
