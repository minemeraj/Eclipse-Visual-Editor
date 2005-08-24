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
package org.eclipse.ve.internal.jfc.vm;
/*
 *  $RCSfile: TabOrderCalculator.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:38:13 $ 
 */

import java.util.List;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import javax.swing.*;
/**
 * Helper class that can calculate the tab order of components within a JFrame
 * It does this by actually telling the frame to receive focus and then
 * seeing who has focus, and then switching focus and seeing who has focus
 * This way it is working with the actual live FocusManager 
 */
public class TabOrderCalculator {
	protected List fComponentOrders;
	protected JFrame fFrame;
	protected Component fFirstComponent;
	protected List fAllFocusableComponents;
	protected FileOutputStream outputStream;
	protected String fileSeparator;
/**
 * TabOrderCalculator constructor comment.
 */
public TabOrderCalculator(JFrame aFrame) {
	fFrame = aFrame;
	try { 
		File output = new File("C:/temp/taborder/taboutputfile.txt"); //$NON-NLS-1$
		try { 
			output.delete();
		} catch ( Exception exc ) {
		}
		output.createNewFile();
		outputStream = new FileOutputStream(output);	
	} catch ( Exception exc ) {
		exc.printStackTrace();
	}
	fileSeparator = System.getProperty("line.separator");	 //$NON-NLS-1$
}
/**
 * Add all children of the container to the list of all focusable children
 */
protected void addFocusableChildrenTo(Container aContainer) {

	// See whether the container can receive focus
	if (aContainer.isFocusable() && fFrame.getContentPane() != aContainer) {
		fAllFocusableComponents.add(aContainer);
	}
	// Iterate over the children
	Component[] children = aContainer.getComponents();
	for (int i = 0; i < children.length ; i++){
		// If the child is a container get its focusable children
		if ( children[i] instanceof Container ) {
			addFocusableChildrenTo((Container)children[i]);
		}
	}
}
/**
 * Find all components that are able to receive focus
 * Walk the entire tree of components
 */
protected void calculateFocusableComponents(){

	fAllFocusableComponents = new ArrayList();
	addFocusableChildrenTo(fFrame.getContentPane());
	
	// Show all focusable children
//	Iterator iter = fAllFocusableComponents.iterator();
//	while(iter.hasNext()){
//		write("*" + iter.next());
//	}
	
}
/**
 * See whether or not any of the lists of components include the argument
 */
protected boolean componentListsInclude(Component aComponent) {

	Iterator iter = fComponentOrders.iterator();
	while ( iter.hasNext() ) {
		// The list is a list of lists
		if ( ((List)iter.next()).contains(aComponent)){
			return true;
		}
	}

	// None of the lists contained the argument.  Return false
	return false;
	
}
/**
 * Generate a component order list from the argument
 */
protected void generateComponentOrderFrom(Component aComponent){

	// See whether a Component is in any lists or not
	List componentsList = getComponentsListContaining(aComponent);
	// componentsList contains the list of components that the argument is in
	// See who comes after aComponent in the focus order
	FocusManager.getCurrentManager().focusNextComponent(aComponent);
	Component nextComponent = fFrame.getFocusOwner();
	while ( nextComponent != null ) {
		// If the next component is in any of the lists then terminate the list
		if ( componentListsInclude(nextComponent) ) {
			componentsList.add(nextComponent);
			return;
		}
		// The next component is a new one not in any existing list.  Add it to the current list
		// and remove it from the list of all components
		// Then focus on the next component and repeat
		componentsList.add(nextComponent);
		fAllFocusableComponents.remove(nextComponent);
		FocusManager.getCurrentManager().focusNextComponent(nextComponent);
		nextComponent = fFrame.getFocusOwner();
	}
}
/**
 * Return the order of the components
 */
public Object[] getComponentOrder(){

	fComponentOrders = new ArrayList();
	fFirstComponent = null;
	calculateFocusableComponents();
	// Tell the frame to receive focus
	fFrame.requestFocus();
	// Ask the focus manager to move focus to the first focussable component.
	FocusManager.getCurrentManager().focusNextComponent(fFrame.getContentPane());
	// See who has focus.  Store this as first focussable component
	Component fFirstComponent = fFrame.getFocusOwner();
	// Cycle around all of the focussable components until we return to any of the components in our current cycle
	// or to the first focussable component
	// Each time we find a component is is removed from the list of all focussable components
	generateComponentOrderFrom(fFirstComponent);
	// Keep processing all components until all of them have been put into lists
	while ( !fAllFocusableComponents.isEmpty() ) {
		generateComponentOrderFrom( (Component) fAllFocusableComponents.get(0) );
	}
	
	// The component orders is a list of lists.  Return it as arrays so that the IDE can process it more easily
	Object[] result = fComponentOrders.toArray();
	for (int i = 0; i < result.length; i++) {
		List components = (List)result[i];
		result[i] = components.toArray();
	}
	
	
	
	for (int i = 0; i < result.length; i++) {
		Object[] components = (Object[])result[i];
		for (int j = 0; j < components.length; j++) {
			write(i + "> Component(" + j + ") = " + components[j]);	 //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	try {
		outputStream.close();
	} catch ( IOException exc ) {
	}
	
	return result;

}
protected void write(String aString){
	try{
		outputStream.write(fileSeparator.getBytes());						
		outputStream.write(aString.getBytes());
	} catch ( IOException exc ) {
	}
}
/**
 * Get the list of components that contains the argument
 * There may not be one there in which case we generate a new list
 */
protected List getComponentsListContaining(Component aComponent){

	// See whether any of the fComponentOrders contain the argument
	// If so then return them
	Iterator iter = fComponentOrders.iterator();
	while ( iter.hasNext() ) {
		List components = (List)iter.next();
		if ( components.contains(aComponent) ){
			return components;

		}
	}

	// This component has not been seen before so create a new list for it and return this	
	List newList = new ArrayList();
	// Add the component to the newly create list and remove it from the set of all components
	newList.add(aComponent);
	fAllFocusableComponents.remove(aComponent);
	fComponentOrders.add(newList);
	return newList;

}
}
