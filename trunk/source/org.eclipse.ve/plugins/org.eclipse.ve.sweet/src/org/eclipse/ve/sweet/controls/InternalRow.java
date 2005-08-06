/**
 * 
 */
package org.eclipse.ve.sweet.controls;

import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

class InternalRow {
	private Control row;
	private Control[] columns;
	protected InternalCompositeTable parent;
	
	public InternalRow(InternalCompositeTable parent, Control row) {
		this.parent = parent;
		this.row = row;
		if (row instanceof Composite) {
			Composite rowComposite = (Composite) row;
			columns = rowComposite.getTabList();
		} else {
			columns = new Control[] {row};
		}
		
		for (int i = 0; i < columns.length; i++) {
			addListeners(columns[i]);
		}
	}
	
	public void dispose() {
		for (int i = 0; i < columns.length; i++) {
			removeListeners(columns[i]);
		}
	}
	
	private void addListeners(Control control) {
		control.addKeyListener(keyListener);
		control.addFocusListener(focusListener);
		control.addTraverseListener(traverseListener);
	}
	
	private void removeListeners(Control control) {
		control.removeKeyListener(keyListener);
		control.removeFocusListener(focusListener);
		control.removeTraverseListener(traverseListener);
	}
	
	private KeyListener keyListener = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			parent.keyPressed(InternalRow.this, e);
		}
	};
	
	private FocusListener focusListener = new FocusAdapter() {
		public void focusLost(FocusEvent e) {
			parent.focusLost(InternalRow.this, e);
		}
		public void focusGained(FocusEvent e) {
			parent.focusGained(InternalRow.this, e);
		}
	};
	
	private TraverseListener traverseListener = new TraverseListener() {
		public void keyTraversed(TraverseEvent e) {
			parent.keyTraversed(InternalRow.this, e);
		}
	};

	public Control getRowControl() {
		return row;
	}
	
	public Control getColumnControl(int i) {
		return columns[i];
	}
	
	public int getColumnNumber(Control control) {
		for (int i = 0; i < columns.length; i++) {
			if (columns[i] == control) {
				return i;
			}
		}
		return -1;
	}
	
	public int getNumColumns() {
		return columns.length;
	}
	
	public void setVisible(boolean visible) {
		row.setVisible(visible);
	}
	
	public boolean getVisible() {
		return row.getVisible();
	}
}