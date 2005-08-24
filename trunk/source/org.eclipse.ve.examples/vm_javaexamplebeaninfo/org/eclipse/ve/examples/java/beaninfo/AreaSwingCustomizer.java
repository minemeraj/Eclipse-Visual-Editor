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
package org.eclipse.ve.examples.java.beaninfo;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.*;

import javax.swing.*;
import javax.swing.event.*;

import org.eclipse.ve.examples.java.vm.Area;
/**
 * This customizer updates the Area bean and signals propertyChangeSupport events
 * when it does so
 */
public class AreaSwingCustomizer extends JPanel implements Customizer {
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -5568620188128667597L;
	protected PropertyChangeSupport pcSupport = new PropertyChangeSupport(this);
	protected Area fArea;
	private JList fColorList;
	private JTextField fTxtFldBorderWidth;
	private JRadioButton fOvalCheckbox;
	private JRadioButton fNoneCheckbox;
	private JRadioButton fDiamondCheckbox;
	private static Color[] fColors = new Color[]{ 
		Color.red , Color.blue , Color.green , Color.yellow , Color.black ,
		Color.cyan , Color.orange , Color.darkGray , Color.gray, Color.lightGray , 
		Color.magenta , Color.pink , Color.white
	};
	private static String[] fColorNames= new String[] {
		"red" , "blue" , "green" , "yellow" , "black" , "cyan" , "orange" ,
		"darkGray" , "gray" , "lightGray" , "magenta" , "pink" , "white"
	};
/**
 * AreaCustomizer constructor comment.
 */
public AreaSwingCustomizer() {
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
	
	fColorList.addListSelectionListener(new ListSelectionListener(){
		public void valueChanged(ListSelectionEvent evt){
			// Get the selected color
			Color newColor = (Color) fColorList.getSelectedValue();
			changeFillColor(newColor);
			// Fire a change event
			pcSupport.firePropertyChange("fillColor",null,newColor);
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

	fTxtFldBorderWidth.addCaretListener(new CaretListener(){
		public void caretUpdate(CaretEvent anEvent){
			// Set the border width
			Integer newValue = new Integer(0);
			if ( !fTxtFldBorderWidth.getText().equals("") ) {
				newValue = new Integer(fTxtFldBorderWidth.getText());
			}
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
	JLabel label = new JLabel("Border Width:", SwingConstants.CENTER);
	GridBagConstraints labelConstraint = new GridBagConstraints();
	labelConstraint.anchor = GridBagConstraints.WEST;
	labelConstraint.insets = inset;
	add(label,labelConstraint);
	// The text field goes in the second grid column
	fTxtFldBorderWidth = new JTextField();
	GridBagConstraints textConstraint = new GridBagConstraints();
	textConstraint.gridx = 1;
	textConstraint.insets = inset;
	textConstraint.fill = GridBagConstraints.HORIZONTAL;
	add(fTxtFldBorderWidth,textConstraint);
	addTextBoxListener();
	// Create a list with the available colors 
	DefaultListModel model = new DefaultListModel();
	for( int i = 0 ; i < fColors.length ; i ++ ) {
		model.addElement(fColors[i]);
	}
	fColorList = new JList(model);
	fColorList.setCellRenderer(new ColorCellRenderer(fColorNames , fColors));
	GridBagConstraints listConstraint = new GridBagConstraints();
	listConstraint.gridy = 1;
	listConstraint.gridx = 0;
	listConstraint.gridwidth = 3;
	listConstraint.fill = GridBagConstraints.BOTH;
	listConstraint.insets = inset;
	add(fColorList,listConstraint);
	addListListener();
	// Create three checkboxes for the different types of shape
	ButtonGroup checkboxGroup = new ButtonGroup();
	// No Shape
	fNoneCheckbox = new JRadioButton("No Shape");
	checkboxGroup.add(fNoneCheckbox);
	GridBagConstraints noneCheckboxConstraint = new GridBagConstraints();
	noneCheckboxConstraint.gridy = 2;
	noneCheckboxConstraint.gridx = 0;
	noneCheckboxConstraint.anchor = GridBagConstraints.WEST;
	noneCheckboxConstraint.insets = inset;
	add(fNoneCheckbox,noneCheckboxConstraint);
	// Oval
	fOvalCheckbox = new JRadioButton("Oval");
	checkboxGroup.add(fOvalCheckbox);
	GridBagConstraints ovalCheckboxConstraint = new GridBagConstraints();
	ovalCheckboxConstraint.gridy = 2;
	ovalCheckboxConstraint.gridx = 1;
	ovalCheckboxConstraint.anchor = GridBagConstraints.WEST;
	ovalCheckboxConstraint.insets = inset;
	add(fOvalCheckbox,ovalCheckboxConstraint);
	// Diamond
	fDiamondCheckbox = new JRadioButton("Diamond");
	checkboxGroup.add(fDiamondCheckbox);
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
		fColorList.setSelectedValue(fArea.getFillColor(),true);
	}
	// Set the state checkboxes up
	switch (fArea.getShape()) {
		case Area.NO_SHAPE: {
			fNoneCheckbox.setSelected(true);
			break;	
		}
		case Area.OVAL: {
			fOvalCheckbox.setSelected(true);
			break;
		}
		case Area.DIAMOND: {
			fDiamondCheckbox.setSelected(true);
			break;
		}
	}

}
/**
 * Get the shape from the checkboxes and update the area
 */
protected void updateShape(){

	if ( fNoneCheckbox.isSelected() ) {
		changeShape(Area.NO_SHAPE);
	} else if ( fOvalCheckbox.isSelected() ) {
		changeShape(Area.OVAL);
	} else if ( fDiamondCheckbox.isSelected() ) {
		changeShape(Area.DIAMOND);
	}
}
}
