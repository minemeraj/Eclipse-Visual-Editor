package com.ibm.etools.jbcf.swt.targetvm;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class ImageGrabberTest {
	
	public static void main(String[] args) {
		
		final Display d = new Display();
		
		final Shell shell = new Shell(d);
		
		Menu menuBar = new Menu(shell,SWT.BAR);
		shell.setMenuBar(menuBar);
		MenuItem fileItem = new MenuItem(menuBar,SWT.CASCADE);
		fileItem.setText("File");
		
		final Button b = new Button(shell,SWT.PUSH);
		b.setText("Foo");
		b.setBounds(20,20,50,50);				
		
		final Group c = new Group(shell,SWT.BORDER);
		c.setText("Group");
		c.setBackground(d.getSystemColor(SWT.COLOR_GREEN));
		c.setBounds(60,40,120,80);
		
		// Create a tab folder with two pages
		final TabFolder tabFolder = new TabFolder(shell,SWT.NONE);
		tabFolder.setBounds(20,150,150,150);
		TabItem page1 = new TabItem(tabFolder,SWT.NONE);
		List list = new List(tabFolder,SWT.BORDER);
		list.add("First List Item");
		list.add("Second List Item");
		page1.setControl(list);
		page1.setText("Page 1");
		
		TabItem page2 = new TabItem(tabFolder,SWT.NONE);
		Label label1 = new Label(tabFolder,SWT.NONE);
		label1.setText("Label");
		page2.setControl(label1);
		page2.setText("Page 2");		
		
		final Table table = new Table(shell,SWT.BORDER);
		TableColumn col1 = new TableColumn(table,SWT.NONE);
		col1.setText("First");
		TableColumn col2 = new TableColumn(table,SWT.NONE);
		col2.setText("Last");
		TableItem item1 = new TableItem(table,SWT.NONE);
		item1.setText(new String[] {"Joe","Gili"});	
		TableItem item2 = new TableItem(table,SWT.NONE);
		item2.setText(new String[] {"Winchester","Mendel"});
		col1.pack();
		col2.pack();		
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(200,20,100,100);	
			
		shell.addPaintListener(new PaintListener(){
			public void paintControl(PaintEvent e) {
				e.gc.setForeground(d.getSystemColor(SWT.COLOR_RED));
				e.gc.drawLine(0,0,150,150);	
				System.out.println("Shell paint event called" + e.gc.getClipping());
			}
		});
		
		final Button childButton = new Button(c,SWT.CHECK);
		childButton.setLocation(10,20);
		childButton.setText("Child Button");
		childButton.pack();
		
		Combo combo = new Combo(c,SWT.NONE);
		combo.setLocation(10,40);
		combo.pack();

		shell.setBackground(d.getSystemColor(SWT.COLOR_BLUE));
		shell.setText("Title");
		shell.setSize(400,350);		
		shell.open();
	
		Shell targetShell = new Shell(d);
		targetShell.setLocation(500,0);
		targetShell.setLayout(new GridLayout(1,false));
		Button grabButton = new Button(targetShell,SWT.PUSH);
		grabButton.setText("Grab image");
		
		final Button includeChildrenButton = new Button(targetShell,SWT.CHECK);
		includeChildrenButton.setText("Include children");
		
 		Composite labelComposite = new Composite(targetShell,SWT.NO_REDRAW_RESIZE);
		labelComposite.setBackground(d.getSystemColor(SWT.COLOR_WHITE));
		GridData data = new GridData(GridData.FILL_BOTH);
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		data.widthHint = 450;
		data.heightHint = 450;
		labelComposite.setLayoutData(data);
		
		Browser browser = new Browser(shell, SWT.NONE) ;
		browser.setBounds(200,200, 200, 200);
		browser.setUrl("http://www.google.com");
		
		final Label l1 = new Label(labelComposite,SWT.LEFT);
		l1.setBackground(d.getSystemColor(SWT.COLOR_YELLOW));
//		final Label l2 = new Label(labelComposite,SWT.NONE);
//		final Label l3 = new Label(labelComposite,SWT.NONE);
				
		grabButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent event){
				
				ImageCapture grabber = new ImageCapture();				
				// The GC now contains the client area painted into it
				Image oldImage = l1.getImage();
				if(oldImage != null) oldImage.dispose();
				Image newImage = grabber.getImage(shell,includeChildrenButton.getSelection());
				ImageData imageData = newImage.getImageData();
				imageData.toString();
				l1.setImage(newImage);
				l1.pack();
			}
		});
		
		targetShell.pack();
		targetShell.open();		
		
		while(!shell.isDisposed()){
			if(!d.readAndDispatch()) d.sleep();
		}
		
		d.dispose();
		
	}
//	private static Point getClientOrigin(Control aControl){
//		Point displayClientOrigin = aControl.toDisplay(0,0);
//		Point locationRelativeToDisplay = aControl.getLocation();
//		if(aControl.getParent() != null){
//			Point parentClientOrigin = aControl.getParent().toDisplay(0,0);
//			locationRelativeToDisplay = new Point(
//				locationRelativeToDisplay.x + parentClientOrigin.x,
//				locationRelativeToDisplay.y + parentClientOrigin.y
//			);
//		};	
//		return new Point(displayClientOrigin.x - locationRelativeToDisplay.x,displayClientOrigin.y - locationRelativeToDisplay.y);
//	}	
}
