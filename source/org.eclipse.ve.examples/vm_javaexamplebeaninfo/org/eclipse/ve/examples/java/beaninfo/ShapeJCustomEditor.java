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

import javax.swing.*;

public class ShapeJCustomEditor extends JPanel {
	
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = 4988719615706081667L;
	protected int fShape;
	protected JList fList;
	
public ShapeJCustomEditor(int aShape){

	fShape = aShape;
	// Show the user a list of the available shapes with the current shape
	// selected	
	DefaultListModel model = new DefaultListModel();	
	fList = new JList(model);
	fList.setSize(100,40);
	for ( int i=0; i<ShapeHelper.fShapeNames.length ; i++){
		model.add(i,ShapeHelper.fShapeNames[i]);
	}
	fList.setSelectedIndex(fShape);
	JScrollPane scrollPane = new JScrollPane(fList);
	add(scrollPane);		
}
/**
 * The shape is the selection index in the list
 */
public int getShape(){
	return fList.getSelectedIndex();
}
}