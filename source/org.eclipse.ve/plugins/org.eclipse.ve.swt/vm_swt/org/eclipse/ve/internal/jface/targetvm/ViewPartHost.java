/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ViewPartHost.java,v $
 *  $Revision: 1.11 $  $Date: 2005-06-06 00:52:37 $ 
 */
package org.eclipse.ve.internal.jface.targetvm;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.WorkbenchPart;

import org.eclipse.ve.internal.swt.targetvm.PreventShellCloseMinimizeListener;

public class ViewPartHost {

	static Shell shell;
	static Map viewPartToParentComposite = new HashMap(1);
	private static int fx;
	private static int fy;
	private static boolean fTraditionalTabs;	// Style for whether tabs are square or rounded
	public static final int MIN_X = 300;
	public static final int MIN_Y = 175;
	private static int fTabPosition; // Location of the tab position (top or bottom)
	private static Image image;
	public static final String VIEW_PART = "VIEW_PART";
	public static final String EDITOR_PART = "EDITOR_PART";	

public static Composite[] addViewPart(WorkbenchPart aWorkbenchPart, String aTitle, String iconLocation){

	Composite parent = new Composite(getWorkbenchShell(),SWT.NONE);
	parent.setLayout(new FillLayout());
	
    CTabFolder folder = new CTabFolder(parent, SWT.BORDER);
    folder.setUnselectedCloseVisible(false);
    folder.setMaximizeVisible(true);
    folder.setMinimizeVisible(true);
	
	// Having simple and traditional tabs is not available on all target platforms so use reflection
	// folder.setSimple(fTraditionalTabs);
	try{
		Method setSimpleMethod = folder.getClass().getMethod("setSimple",new Class[] {Boolean.TYPE});
		setSimpleMethod.invoke(folder,new Object[]{fTraditionalTabs ? Boolean.TRUE : Boolean.FALSE});
	} catch (Exception e){	
	}
	folder.setTabPosition(fTabPosition);

    ViewForm viewForm = new ViewForm(folder, SWT.NONE);
    viewForm.marginHeight = 0;
    viewForm.marginWidth = 0;
    viewForm.verticalSpacing = 0;
    viewForm.setBorderVisible(false);

    CTabItem item = new CTabItem(folder, SWT.CLOSE);
    item.setText(aTitle); //$NON-NLS-1$
    Composite viewPartArgument = new Composite(viewForm, SWT.NONE){
		public Point computeSize(int wHint,int hHint, boolean changed){
			Point preferredSize = super.computeSize(wHint,hHint, changed);
			Point result = new Point(
					preferredSize.x > MIN_X ? preferredSize.x : MIN_X,
					preferredSize.y > MIN_Y ? preferredSize.y : MIN_Y
					);
			return result;
		}		
    };
	viewPartArgument.setLayout(new FillLayout(SWT.HORIZONTAL));
	viewForm.setContent(viewPartArgument);
    folder.setSelection(item);
	// Load the icon disposing the old one if required
	if(iconLocation != null){
		// If no icon location is read from the plugin.xml the ViewPartProxyAdapter determinesd the type of WorkbenchPart
		// and passes this in as the literals VIEW_PART or EDITOR_PART
		if(VIEW_PART.equals(iconLocation)){
			image = new Image(null,ViewPartHost.class.getResourceAsStream("rcp_app.gif"));
		} else if (EDITOR_PART.equals(iconLocation)){
			image = new Image(null,ViewPartHost.class.getResourceAsStream("rcp_editor.gif"));			
		} else {
			try{
				image = new Image(null,iconLocation);
			} catch (Exception e){
			}
		}
		item.setImage(image);
		item.addDisposeListener(new DisposeListener(){
			public void widgetDisposed(DisposeEvent e) {
				if(image != null && !image.isDisposed()){
					image.dispose();
				}
			}			
		});
	}
	item.setControl(viewForm);
	// Record a map entry of the workbenchPart against the Composite argument and the CTabFolder that represents its trim
	viewPartToParentComposite.put(aWorkbenchPart,new Composite[] {viewPartArgument, folder});
	aWorkbenchPart.createPartControl(viewPartArgument);
	// Need to set the layout data to null because some workbench parts set the children's
	// layout data to other than FillLayoutData.
	Control [] children = viewPartArgument.getChildren();
	for (int i = 0; i < children.length; i++) {
		children[i].setLayoutData(null);
	}
	getWorkbenchShell().layout(true);
	return new Composite[] {folder,viewPartArgument};
		
}

public static void removeViewPart(WorkbenchPart aWorkbenchPart){

	// Dispose the CTabFolder that represents the outermost composite which will clean up all of the children
	((Composite[])viewPartToParentComposite.get(aWorkbenchPart))[1].dispose();
	viewPartToParentComposite.remove(aWorkbenchPart);	
	
}

public static void layoutViewPart(WorkbenchPart aWorkbenchPart){
	
	Composite c = ((Composite[])viewPartToParentComposite.get(aWorkbenchPart))[0];
	c.pack();
	getWorkbenchShell().layout(true);
	
}

public static void main(String[] args) {
	
	TestViewPartTest testViewPart = new TestViewPartTest(); 
	addViewPart(testViewPart,JFaceTargetVMMessages.getString("ViewPartHost.ViewPart.Name"),"VIEW_PART"); //$NON-NLS-1$
	layoutViewPart(testViewPart);
	while(!shell.isDisposed()){
		if(!shell.getDisplay().readAndDispatch())shell.getDisplay().sleep();
	}
	
}

public static void setDetails(int x, int y, boolean traditionalTabs, int tabLocation){
	fx = x;
	fy = y;
	fTraditionalTabs = traditionalTabs;
	fTabPosition = tabLocation;
	if(shell != null){
		shell.setLocation(x,y);
	}
}

protected static Composite getWorkbenchShell(){
	
	if(shell == null){
		Shell dialogParent = new Shell();
		shell = new Shell(dialogParent,SWT.SHELL_TRIM);
		shell.addShellListener(new PreventShellCloseMinimizeListener());
		shell.setBounds(fx,fy,200,200);
		shell.setText(JFaceTargetVMMessages.getString("ViewPartHost.Shell.Text")); //$NON-NLS-1$
		shell.setLayout(new RowLayout());
		shell.open();
	}
	
	return shell;
}
}

