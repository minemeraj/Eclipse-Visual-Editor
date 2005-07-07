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
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.VerifyListener;

public interface ITextField {
    void setText(String value);
    String getText();
    
    void addVerifyListener(VerifyListener l);
    void removeVerifyListener(VerifyListener l);
    
    void setSelection(int begin, int end);

    void addDisposeListener(DisposeListener l);
    void removeDisposeListener(DisposeListener l);
    
    void addFocusListener(FocusListener l);
    void removeFocusListener(FocusListener l);
    void setFocus();
}
