package org.eclipse.ve.sweet.controls.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ve.sweet.controls.InternalCompositeTable;

public class EmptyTablePlaceholder extends Canvas {

	private boolean focusControl = false;
	private InternalCompositeTable parentTable = null;
	
	private final Color RED;
	private final Color BLUE;
	
	public EmptyTablePlaceholder(Composite parent, int style) {
		super(parent, style);
		parentTable = (InternalCompositeTable) parent.getParent();
		
		parent.addControlListener(controlListener);
		
		addTraverseListener(traverseListener);
		addFocusListener(focusListener);
		addKeyListener(keyListener);
		addPaintListener(paintListener);
		addDisposeListener(disposeListener);
		
		RED = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
		BLUE = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
		
		resize();
	}

	private DisposeListener disposeListener = new DisposeListener() {
		public void widgetDisposed(DisposeEvent e) {
			removeTraverseListener(traverseListener);
			removeFocusListener(focusListener);
			removeKeyListener(keyListener);
			removePaintListener(paintListener);
			removeDisposeListener(disposeListener);
			
			getParent().removeControlListener(controlListener);
		}
	};
	
	private ControlListener controlListener = new ControlAdapter() {
		public void controlResized(ControlEvent e) {
			resize();
		}
	};
	
	private void resize() {
		Point headerSize = new Point(0, 0);
		Control header = parentTable.getHeaderControl();
		if (header != null) {
			headerSize = header.getSize();
		}
		Point parentSize = getParent().getSize();
		
		setBounds(0, headerSize.y, parentSize.x, parentSize.y - headerSize.y);
	}

	
	private String message = "Press [INS] to create a new row.";
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
		redraw();
	}

	private PaintListener paintListener = new PaintListener() {
		public void paintControl(PaintEvent e) {
			Color oldColor = e.gc.getForeground();
			int oldLineStyle = e.gc.getLineStyle();
			int oldLineWidth = e.gc.getLineWidth();
			try {
				if (focusControl) {
					e.gc.setLineStyle(SWT.LINE_DASH);
					e.gc.setLineWidth(2);
					Point parentSize = getSize();
					e.gc.drawRectangle(1, 2, parentSize.x-2, parentSize.y-3);
				}

				e.gc.setForeground(RED);
				e.gc.drawText(getMessage(), 3, 3);
			} finally {
				e.gc.setForeground(oldColor);
				e.gc.setLineStyle(oldLineStyle);
				e.gc.setLineWidth(oldLineWidth);
			}
		}
	};
	
	private FocusListener focusListener = new FocusListener() {
		public void focusGained(FocusEvent e) {
			focusControl = true;
			redraw();
		}
		public void focusLost(FocusEvent e) {
			focusControl = false;
			redraw();
		}
	};
	
	private TraverseListener traverseListener = new TraverseListener() {
		 public void keyTraversed(TraverseEvent e) {
		}
	};
	
	private KeyListener keyListener = new KeyListener() {
		public void keyPressed(KeyEvent e) {
			if (e.keyCode == SWT.INSERT)
				parentTable.keyPressed(null, e);
		}
		public void keyReleased(KeyEvent e) {
		}
	};

}
