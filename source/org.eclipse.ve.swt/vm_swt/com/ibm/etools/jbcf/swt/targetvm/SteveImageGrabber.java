package com.ibm.etools.jbcf.swt.targetvm;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.internal.win32.*;

public class SteveImageGrabber {
	
		public static void main (String [] args) {
	
//		#define PRF_CHECKVISIBLE    0x00000001L
//		#define PRF_NONCLIENT       0x00000002L
//		#define PRF_CLIENT          0x00000004L
//		#define PRF_ERASEBKGND      0x00000008L
//		#define PRF_CHILDREN        0x00000010L
//		#define PRF_OWNED           0x00000020L
//		#define WM_PRINT                        0x0317
//		#define WM_PRINTCLIENT                  0x0318
	
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.open ();
	
//		Button b = new Button (shell, SWT.ARROW);
//		b.setText ("Hello");
//		b.setSize (200, 200);
//		Control control = b;
	
		StyledText text = new StyledText (shell, SWT.BORDER | SWT.V_SCROLL);
		text.setText ("Hello\nThere\nChrix");
		text.setSize (200, 200);
		Control control = text;
	
//		Table table = new Table(shell, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
//		table.setSize(200, 200);
//		table.setLinesVisible(true);
//		table.setHeaderVisible(true);
//		String[] columnTitles = {" ", "C", "!", "Description", "Resource", "In Folder", "Location" };
//		for (int i = 0; i < columnTitles.length; i++) {
//			TableColumn tableColumn = new TableColumn(table, SWT.NULL);
//			tableColumn.setText(columnTitles[i]);
//		}
//		int count = 128;
//		for (int i = 0; i < count; i++) {
//			TableItem item = new TableItem(table, SWT.NULL);
//			item.setText(0, "x");
//			item.setText(1, "y");
//			item.setText(2, "!");
//			item.setText(3, "None of this stuff behaves the way I want");
//			item.setText(4, "Almost_Everywhere");
//			item.setText(5, "Some.darn.folder man");
//			item.setText(6, "line " + i + " in Nowhere");
//		}
//		for (int i = 0; i < columnTitles.length; i++) {
//			table.getColumn(i).pack();
//		}
//		table.setSize(table.computeSize(SWT.DEFAULT, 200));
//		Control control = table;
	
		Rectangle rect = control.getBounds ();
		Image image = new Image (display, rect.width, rect.height);
		int WM_PRINT = 0x0317;
		int PRF_CHECKVISIBLE = 0x00000001;
		int PRF_NONCLIENT = 0x00000002;
		int PRF_CLIENT = 0x00000004;
		int PRF_ERASEBKGND = 0x00000008;
		int PRF_CHILDREN = 0x00000010;
		int PRF_OWNED = 0x00000020;
		int bits = PRF_CLIENT | PRF_NONCLIENT | PRF_CHILDREN | PRF_ERASEBKGND;
		GC gc = new GC (image);
		OS.SendMessage (control.handle, WM_PRINT, gc.handle, bits);
		gc.dispose ();
		gc = new GC (shell);
		gc.drawImage (image, 0, 200+50);
		gc.dispose ();
		image.dispose ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}

	}
	