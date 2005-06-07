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
package org.eclipse.ve.internal.swt.targetvm.win32;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.widgets.*;

import org.eclipse.ve.internal.swt.targetvm.IImageCapture;

public class ImageCapture implements IImageCapture{
	
	Control controlToCapture;
	
	/**
	 * Return the image of the argument.  This includes the client and non-client area
	 * If the argument includeChildren is true then child controls are included 
	 */	
	public Image getImage(Control control, boolean includeChildren){
		
		Image myImage = getImage(control);
		// Get the images of all of the children
		if(control instanceof Composite && includeChildren){
			Point clientOrigin = getClientOrigin(control);
			Composite composite = (Composite)control;
			Control[] children = composite.getChildren();
			GC myImageGC = new GC(myImage);
			for (int i = children.length; i > 0; i--) {
				// If the child is not visible then don't try and get its image
				// An example of where this would cause a problem is TabFolder where all the controls
				// for each page are children of the TabFolder, but only the visible one is being shown on the active page
				if(!children[i-1].isVisible()) continue;
				Image childImage = getImage(children[i-1],true);
				Rectangle bounds = childImage.getBounds();
				bounds.toString();
				// Paint the child image on top of our one
				// Its location is within our client area origin, so if it is at 10,10 and our client origin
				// is at 5,5 then we draw at 15,15 on our image
				Point childCorner = children[i-1].getLocation();
				childCorner.x = childCorner.x + clientOrigin.x;
				childCorner.y = childCorner.y + clientOrigin.y;
				myImageGC.drawImage(childImage,childCorner.x,childCorner.y);
				childImage.dispose();
			}
			myImageGC.dispose();
		}
		return myImage;
	}	
	/**
	 * Return the image of the argument.  This includes the client and non-client area, but does not include
	 * any child controls.  
	 * To get child control use the method getImage(Control aControl,boolean children); 
	 */
	public Image getImage(Control aControl){
		
		Rectangle rect = aControl.getBounds ();
		if (rect.width <= 0 || rect.height <= 0)
			return new Image(aControl.getDisplay(), 1, 1);	// TODO we are getting invalid rect for some reason. Figure out why later.
		Image image = new Image (aControl.getDisplay(), rect.width, rect.height);
		int WM_PRINT = 0x0317;
//		int WM_PRINTCLIENT = 0x0318;		
//		int PRF_CHECKVISIBLE = 0x00000001;
		int PRF_NONCLIENT = 0x00000002;
		int PRF_CLIENT = 0x00000004;
		int PRF_ERASEBKGND = 0x00000008;
		int PRF_CHILDREN = 0x00000010;
//		int PRF_OWNED = 0x00000020;
		int print_bits = PRF_NONCLIENT | PRF_CLIENT | PRF_ERASEBKGND;
		// This method does not print immediate children because the z-order doesn't work correctly and needs to be
		// dealt with separately, however Table's TableColumn widgets are children so much be handled differently
		boolean specialClass = aControl instanceof Table ||
		   						aControl instanceof Browser ||
		   						aControl instanceof OleFrame ||
		   						aControl instanceof CCombo;
		try {
			specialClass |= aControl instanceof Spinner;
		}
		catch (NoClassDefFoundError e) {} // might not be on 3.1 of SWT
		if(specialClass){
			print_bits = print_bits | PRF_CHILDREN;	
		}
		GC gc = new GC (image);
		
		// Need to handle cases where the GC font isn't automatically set by the control's image (e.g. CLabel)
		// see bug 98830 (https://bugs.eclipse.org/bugs/show_bug.cgi?id=98830)
		Font f = aControl.getFont();
		if (f != null)
			gc.setFont(f);

		OS.SendMessage (aControl.handle, WM_PRINT, gc.handle, print_bits);

		gc.dispose ();
		return image;
	}
	
	public Shell createShellToCapture(Display display){
		
		Shell shell = new Shell(display);
		
		Menu menuBar = new Menu(shell,SWT.BAR);
		shell.setMenuBar(menuBar);
		MenuItem fileItem = new MenuItem(menuBar,SWT.CASCADE);
		fileItem.setText("File"); //$NON-NLS-1$
		
		final Button b = new Button(shell,SWT.PUSH);
		b.setText("Foo"); //$NON-NLS-1$
		b.setBounds(20,20,50,50);				
		
		final Group c = new Group(shell,SWT.BORDER);
		c.setText("Group"); //$NON-NLS-1$
		c.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
		c.setBounds(60,40,120,80);
		
		c.setLayout(new GridLayout(1,false));
		Button male = new Button(c,SWT.RADIO);
		male.setText("Male"); //$NON-NLS-1$
		Button female = new Button(c,SWT.RADIO);
		female.setText("Female");	 //$NON-NLS-1$
		c.layout();	
		
		// Create a tab folder with two pages
		final TabFolder tabFolder = new TabFolder(shell,SWT.NONE);
		tabFolder.setBounds(20,150,150,150);
		TabItem page1 = new TabItem(tabFolder,SWT.NONE);
		List list = new List(tabFolder,SWT.BORDER);
		list.add("First List Item"); //$NON-NLS-1$
		list.add("Second List Item"); //$NON-NLS-1$
		page1.setControl(list);
		page1.setText("Page 1"); //$NON-NLS-1$
		
		TabItem page2 = new TabItem(tabFolder,SWT.NONE);
		Label label1 = new Label(tabFolder,SWT.NONE);
		label1.setText("Label"); //$NON-NLS-1$
		page2.setControl(label1);
		page2.setText("Page 2");		 //$NON-NLS-1$
		
		final Table table = new Table(shell,SWT.BORDER);
		TableColumn col1 = new TableColumn(table,SWT.NONE);
		col1.setText("First"); //$NON-NLS-1$
		TableColumn col2 = new TableColumn(table,SWT.NONE);
		col2.setText("Last"); //$NON-NLS-1$
		TableItem item1 = new TableItem(table,SWT.NONE);
		item1.setText(new String[] {"Joe","Gili"});	 //$NON-NLS-1$ //$NON-NLS-2$
		TableItem item2 = new TableItem(table,SWT.NONE);
		item2.setText(new String[] {"Winchester","Mendel"}); //$NON-NLS-1$ //$NON-NLS-2$
		col1.pack();
		col2.pack();		
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(200,20,100,100);
		
		shell.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
		shell.setText("Title"); //$NON-NLS-1$
		shell.setSize(400,350);		
		
		shell.addPaintListener(new PaintListener(){

			public void paintControl(PaintEvent e) {
				System.out.println("PaintEvent called back clipped to " + e.gc.getClipping()); //$NON-NLS-1$
				e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_RED));
				e.gc.drawLine(0,0,100,100);
			}
		});
		
		controlToCapture = shell;		
					
		return shell;
		
	}

	public Shell createShellToShowImage(Display display){
		
		Shell targetShell = new Shell(display);
		targetShell.setLocation(500,0);
		targetShell.setLayout(new GridLayout(1,false));		
		
		Button grabButton = new Button(targetShell,SWT.PUSH);
		grabButton.setText("Grab image"); //$NON-NLS-1$
				
		Composite labelComposite = new Composite(targetShell,SWT.NO_REDRAW_RESIZE);
		labelComposite.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		GridData data = new GridData(GridData.FILL_BOTH);
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		data.widthHint = 450;
		data.heightHint = 450;
		labelComposite.setLayoutData(data);
		
		final Label l1 = new Label(labelComposite,SWT.LEFT);
		l1.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
				
		grabButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent event){
				if(l1.getImage() != null) l1.getImage().dispose();
				
				// Get the new image capture
				Image newImage = getImage(controlToCapture,true);
				
				l1.setImage(newImage);
				l1.pack();
			}
		});		
		
		targetShell.pack();
		return targetShell;
		
	}
	
	private static Point getClientOrigin(Control aControl){
		Point displayClientOrigin = aControl.toDisplay(0,0);
		Point locationRelativeToDisplay = aControl.getLocation();
		if(aControl.getParent() != null){
			Point parentClientOrigin = aControl.getParent().toDisplay(0,0);
			locationRelativeToDisplay = new Point(
				locationRelativeToDisplay.x + parentClientOrigin.x,
				locationRelativeToDisplay.y + parentClientOrigin.y
			);
		};	
		return new Point(displayClientOrigin.x - locationRelativeToDisplay.x,displayClientOrigin.y - locationRelativeToDisplay.y);
	}		

	}
	
