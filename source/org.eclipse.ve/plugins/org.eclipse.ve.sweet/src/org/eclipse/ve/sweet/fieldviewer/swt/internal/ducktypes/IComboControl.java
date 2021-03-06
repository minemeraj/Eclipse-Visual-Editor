/*
 * Copyright (C) 2005 db4objects Inc.  http://www.db4o.com
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     db4objects - Initial API and implementation
 */
package org.eclipse.ve.sweet.fieldviewer.swt.internal.ducktypes;

import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;

public interface IComboControl {
    Point getSelection();
    int getSelectionIndex();
    
    void select(int index);
    
    String getText();
    void setText(String value);
    
    void removeAll();
    
    void add(String item);
    
    void addSelectionListener(SelectionListener l);
    void removeSelectionListener(SelectionListener l);

    void addDisposeListener(DisposeListener l);
    void removeDisposeListener(DisposeListener l);
}
