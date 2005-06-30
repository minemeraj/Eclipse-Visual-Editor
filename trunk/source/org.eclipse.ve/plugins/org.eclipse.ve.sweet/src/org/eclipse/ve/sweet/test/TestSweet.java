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
package org.eclipse.ve.sweet.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ve.sweet.CannotSaveException;
import org.eclipse.ve.sweet.dataeditors.IObjectEditor;
import org.eclipse.ve.sweet.dataeditors.ObjectEditorFactory;
import org.eclipse.ve.sweet.dataeditors.java.JavaObjectEditorFactory;
import org.eclipse.ve.sweet.hinthandler.HintHandler;
import org.eclipse.ve.sweet.hinthandler.IHintHandler;

public class TestSweet {

    /**
     * @param args
     */
    public static void main(String[] args) {
        new TestSweet().run();
    }
    
    private Person person = new Person("John Doe", 21);

    private void run() {
        // Standard SWT setup
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setSize(800, 600);
        
        // Make a UI for editing
        shell.setLayout(new GridLayout(2, false));
        new Label(shell, SWT.NULL).setText("Name:");
        Text name = new Text(shell, SWT.BORDER);
        name.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
        new Label(shell, SWT.NULL).setText("Age:");
        Text age = new Text(shell, SWT.BORDER);
        age.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
        
        // Edit our Person object
        ObjectEditorFactory.factory = new JavaObjectEditorFactory();
        final IObjectEditor personEditor = ObjectEditorFactory.construct(person);
        personEditor.bind(name, "Name");
        personEditor.bind(age, "Age");
        
        // Add a hint label
        final Label hint = new Label(shell, SWT.NULL);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        gd.horizontalSpan = 2;
        hint.setLayoutData(gd);
        HintHandler.setHintHandler(new IHintHandler() {
            public void setMessage(String message) {
                hint.setText(message);
            }
            public void clearMessage() {
                hint.setText("");
            }
        });
        
        // Make sure changes are committed on shell close
        shell.addShellListener(new ShellAdapter() {
            @Override
            public void shellClosed(ShellEvent e) {
                try {
                    personEditor.commit();
                } catch (CannotSaveException e1) {
                    e.doit = false;
                }
            }
        });
        
        // The usual SWT event loop
        shell.open();
        while (!shell.isDisposed()) {
            display.readAndDispatch();
        }
        display.dispose();
        
        // Print the results
        System.out.println("Name: " + person.name);
        System.out.println("Age: " + person.age);
    }

}
