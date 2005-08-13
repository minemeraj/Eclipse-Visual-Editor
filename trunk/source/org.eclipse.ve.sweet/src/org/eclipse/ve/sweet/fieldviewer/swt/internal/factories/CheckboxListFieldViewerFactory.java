/*
 * Copyright (C) 2005 David Orme <djo@coconut-palm-software.com>
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     David Orme     - Initial API and implementation
 */
package org.eclipse.ve.sweet.fieldviewer.swt.internal.factories;

import org.eclipse.ve.sweet.fieldviewer.IFieldViewer;
import org.eclipse.ve.sweet.fieldviewer.IFieldViewerFactory;
import org.eclipse.ve.sweet.fieldviewer.swt.internal.CheckboxListFieldViewer;
import org.eclipse.ve.sweet.fieldviewer.swt.internal.ducktypes.ICheckboxListControl;
import org.eclipse.ve.sweet.objectviewer.IObjectViewer;
import org.eclipse.ve.sweet.objectviewer.IPropertyEditor;
import org.eclipse.ve.sweet.reflect.DuckType;

public class CheckboxListFieldViewerFactory implements IFieldViewerFactory {

	public IFieldViewer construct(Object control, IObjectViewer objectViewer,
			IPropertyEditor propertyEditor) {
		
		if (DuckType.instanceOf(ICheckboxListControl.class, control)) {
			return new CheckboxListFieldViewer(control, objectViewer, propertyEditor);
		}

		return null;
	}

}
