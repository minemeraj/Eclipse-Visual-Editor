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

import org.eclipse.ve.examples.java.vm.ButtonBar;

import sun.awt.OrientableFlowLayout;
/**
 * Customizer that lets the background color of the panel be changed and also
 * adds and removes buttons
 * By adding and removing buttons in the customier just using add(Component) and
 * remove(Component) on the ButtonBarOne object the VCE does not see that the components
 * have changed and cannot create the MOF objects correctly
 * This means that code generation will include the buttons, and VCE things like
 * undo, or re-creation of the bean following a re-parent or a delete will not
 * include the buttons
 */
public class ButtonBarCustomizer extends Panel implements Customizer {
	protected PropertyChangeSupport fPCSupport = new PropertyChangeSupport(this);
	protected ButtonBar fButtonBar;
	protected List fComponentList;
	protected Panel fButtonRow;
	protected Button fAddButton;
	protected Button fRemoveButton;
/**
 * ButtonBarOneCustomizer constructor comment.
 */
public ButtonBarCustomizer() {
	super();
	initialize();
}
/**
 * addPropertyChangeListener method comment.
 */
public void addPropertyChangeListener(PropertyChangeListener listener) {
	fPCSupport.addPropertyChangeListener(listener);
}
/* Get all of the components in the ButtonBar and show them in the list
 */
protected void buildListContents(){

	fComponentList.removeAll();
	for (int i = 0; i < fButtonBar.getComponentCount() ; i++){
		if ( fButtonBar.getComponent(i) instanceof Button ) {
			fComponentList.add( "Button ("+ ((Button)fButtonBar.getComponent(i)).getLabel() +")" );
		} else {
			fComponentList.add( fButtonBar.getComponent(i).toString());
		}
	}
	fRemoveButton.setEnabled(fComponentList.getSelectedItem()!=null);
	
}
/**
 * Add a button to ButtonBar
 * This is done directly by talking to the button bar one so it is not known to the VCE
 */
protected void createButton(){

	fButtonBar.add(new Button( "  " + (getButtonBarComponentCount() + 1) + " "));
	// Refresh the list showing the components of the ButtonBar
	buildListContents();
	// Signal the VCE to get it to refresh itself
	// NOTE - This will only get the VCE to change its visual appearance of the button bar
	// The VCE MOF model has not been updated so the new button is not available to be selected and changed
	// When the buttonBar is re-created following a delete and an undo or a re-parent ( by moving it to a new parent container )
	// the newly created buttons will be lost as they were not part of the VCE model.  Code generation also does not
	// know about the newly added button
	// To see the correct way to do this look at the other ButonBarCustomizer that implements IVCECustomizerPanel
	fPCSupport.firePropertyChange("components" , null , null );
	
}
/**
 * Return the number of components in the button bar
 */
protected int getButtonBarComponentCount(){
	return fButtonRow.getComponentCount();
}
/**
 * Return the selected component
 */
protected Component getSelectedComponent(){

	return fButtonBar.getComponent(fComponentList.getSelectedIndex());

}
/* We allow buttons to be added and removed by a list of the available components and an add and a remove button
 */
protected void initialize(){

	// We are in Border layout.  Add a "Center" list and a "South" button bar
	setLayout(new BorderLayout());
	fComponentList = new List();
	add(fComponentList,"Center");
	fComponentList.setSize(110,700);
	fComponentList.addItemListener(new ItemListener(){
		public void itemStateChanged(ItemEvent anEvent){
			fRemoveButton.setEnabled(fComponentList.getSelectedItem()!=null);
		}
	});
	fButtonRow = new Panel();
	fButtonRow.setLayout(new OrientableFlowLayout(OrientableFlowLayout.VERTICAL));
	add(fButtonRow,"East");
	fAddButton = new Button("Add");
	// Create an add button to add new Button objects to the ButtonBar
	fButtonRow.add(fAddButton);
	fAddButton.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent anEvent){
			createButton();
		}
	});
	fRemoveButton = new Button("Remove");
	// Create a remove button to remove the selected object from the ButtonBar
	fButtonRow.add(fRemoveButton);
	fRemoveButton.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent anEvent){
			removeComponent();
		}
	});
}
/**
 * Remove a component from the ButtonBar
 * This is done directly by talking to the button bar one so it is not known to the VCE
 */
protected void removeComponent(){

	fButtonBar.remove(getSelectedComponent());
	// Refresh the list showing the components of the ButtonBar
	buildListContents();
	// Signal the VCE to get it to refresh itself
	// NOTE - This will only get the VCE to change its visual appearance of the button bar
	// The VCE MOF model has not been updated so the new button is not available to be selected and changed
	// When the buttonBar is re-created following a delete and an undo or a re-parent ( by moving it to a new parent container )
	// the newly created buttons will be lost as they were not part of the VCE model.  Code generation also does not
	// know about the newly added button
	// To see the correct way to do this look at the other ButonBarCustomizer that implements IVCECustomizerPanel
	fPCSupport.firePropertyChange("components" , null , null );
	
}
/**
 * removePropertyChangeListener method comment.
 */
public void removePropertyChangeListener(PropertyChangeListener listener) {
	fPCSupport.removePropertyChangeListener(listener);
}
/**
 * Store the bean being customized and initialize the list
 */
public void setObject(Object bean) {
	fButtonBar = (ButtonBar)bean;
	buildListContents();
}
}
