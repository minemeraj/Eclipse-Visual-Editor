/*
 * Copyright (C) 2005 db4objects Inc.  http://www.db4o.com
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     db4objects - Initial API and implementatiobn
 */
package org.eclipse.ve.sweet.field.swt.internal.interfaces;

import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionListener;


/**
 * ISetSelectorField. A strict duck interface for things like list boxes or
 * groups of check boxes that allow the user to pick a bunch of choices from
 * a set of valid values
 *
 * @author djo
 */
public interface ICheckboxListField {
    int getSelectionIndex();
    void setSelection(int index);
    
    String [] getSelection();
    int [] getSelectionIndices();
    void setSelection(int[] indices);
    
    void removeAll();
    
    void add(String item);
    
    void addSelectionListener(SelectionListener l);
    void removeSelectionListener(SelectionListener l);

    void addDisposeListener(DisposeListener l);
    void removeDisposeListener(DisposeListener l);
}
