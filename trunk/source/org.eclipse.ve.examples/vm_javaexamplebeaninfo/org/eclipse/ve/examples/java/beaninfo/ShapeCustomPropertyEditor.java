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

public class ShapeCustomPropertyEditor extends AbstractShapeCustomPropertyEditor {

	protected ShapeJCustomEditor fCustomEditor;

public Component getCustomEditor(){
	fCustomEditor = new ShapeJCustomEditor(fShapeIndex);
	return fCustomEditor;
}
/**
 * Ask the custom editor for the value
 */
public Object getValue(){
	return fCustomEditor != null ? new Integer(fCustomEditor.getShape()) : null;
}

}
