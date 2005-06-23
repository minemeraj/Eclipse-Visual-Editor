/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CDEDirectEditManager.java,v $
 *  $Revision: 1.4 $  $Date: 2005-06-23 16:08:42 $ 
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.properties.PropertySourceAdapter;

import org.eclipse.ve.internal.propertysheet.INeedData;
import org.eclipse.ve.internal.propertysheet.ISourced;

/**
 * Direct EditManager for CDE. It is uses the IPropertyDescriptor to get the cell editor
 * for direct edit instead of the editorType. This makes sure it goes through the proper
 * validations and such.
 * 
 * @since 1.1.0
 */
public class CDEDirectEditManager extends DirectEditManager {

	private Font scaledFont;
	private IPropertyDescriptor sfProperty;

	public CDEDirectEditManager(GraphicalEditPart source, CellEditorLocator locator, IPropertyDescriptor property) {
		super(source, null, locator);
		sfProperty = property;
	}
	
	protected DirectEditRequest createDirectEditRequest() {
		DirectEditRequest req = super.createDirectEditRequest();
		req.setDirectEditFeature(sfProperty);
		return req;
	}

	/**
	 * @see org.eclipse.gef.tools.DirectEditManager#bringDown()
	 */
	protected void bringDown() {
		// This method might be re-entered when super.bringDown() is called.
		Font disposeFont = scaledFont;
		scaledFont = null;
		super.bringDown();
		if (disposeFont != null)
			disposeFont.dispose();
	}

	protected CellEditor createCellEditorOn(Composite composite) {
		return sfProperty.createPropertyEditor(composite);
	}
	
	protected void initCellEditor() {
		Object initialValue = getPropertyValue(sfProperty);
		CellEditor cellEditor = getCellEditor();
		if (cellEditor instanceof INeedData) {
			((INeedData) cellEditor).setData(EditDomain.getEditDomain(getEditPart()));
		}
		if (cellEditor instanceof ISourced) {
			((ISourced) cellEditor).setSources(new Object[] {getEditPart().getModel()}, new IPropertySource[] {(IPropertySource) getEditPart().getAdapter(IPropertySource.class)}, new IPropertyDescriptor[] {sfProperty});
		}
		cellEditor.setValue(initialValue);
		Control editor = cellEditor.getControl();
		IFigure figure = getEditPart().getFigure();
		scaledFont = figure.getFont();
		FontData data = scaledFont.getFontData()[0];
		Dimension fontSize = new Dimension(0, data.getHeight());
		getEditPart().getFigure().translateToAbsolute(fontSize);
		data.setHeight(fontSize.height);
		scaledFont = new Font(null, data);

		editor.setFont(scaledFont);
	}

	/**
	 * Get the property value.
	 * <p>
	 * Gets the proprty value of the property specified by the property. It should be
	 * as the editable property value because it will go straight into the appropriate cell editor.
	 * 
	 * @param property
	 * @return a String value 
	 * 
	 * @since 1.1.0
	 */
	protected Object getPropertyValue(IPropertyDescriptor property) {
		// retrieve the property's value from the model
		Object value = null;
		IPropertySource ps = (IPropertySource) getEditPart().getAdapter(IPropertySource.class);
		if (PropertySourceAdapter.isPropertySet(ps, property)) {
			value = PropertySourceAdapter.getPropertyValue(ps, property);
			if (value != null) {
				if (value instanceof IPropertySource)
					value = ((IPropertySource) value).getEditableValue();
			}
		}
		return value;
	}

}
