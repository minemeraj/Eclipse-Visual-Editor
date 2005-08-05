/**
 * 
 */
package org.eclipse.ve.sweet.controls;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

class InternalMRVRow {
	private Control row;
	private Control[] columns;
	
	public InternalMRVRow(Control row) {
		this.row = row;
		if (row instanceof Composite) {
			Composite rowComposite = (Composite) row;
			columns = rowComposite.getTabList();
		} else {
			columns = new Control[] {row};
		}
	}
	
	public Control getRowControl() {
		return row;
	}
	
	public Control getColumnControl(int i) {
		return columns[i];
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