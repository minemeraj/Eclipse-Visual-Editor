package org.eclipse.ve.sweet2;

import org.eclipse.jface.viewers.AbstractListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public final class ComboEditor extends AbstractListViewer {

    private Combo combo;
    public ComboEditor(Composite parent) {
        this(parent, SWT.READ_ONLY | SWT.BORDER);
    }
    public ComboEditor(Composite parent, int style) {
        this(new Combo(parent, style));
    }
    public ComboEditor(Combo list) {
        this.combo = list;
        hookControl(list);
    }
    protected void hookControl(Control control) {
    	super.hookControl(control);
    	control.setEnabled(false);
    }
    protected void listAdd(String string, int index) {
        combo.add(string, index);
    }
    protected void listSetItem(int index, String string) {
        combo.setItem(index, string);
    }
    protected int[] listGetSelectionIndices() {
        return new int[] { combo.getSelectionIndex() };
    }
    protected int listGetItemCount() {
        return combo.getItemCount();
    }
    protected void listSetItems(String[] labels) {
        combo.setItems(labels);
    }
    protected void listRemoveAll() {
        combo.removeAll();
    }
    protected void listRemove(int index) {
        combo.remove(index);
    }
    public Control getControl() {
        return combo;
    }
    public Combo getCombo() {
        return combo;
    }
    public void reveal(Object element) {
        return;
    }
    protected void listSetSelection(int[] ixs) {
        for (int idx = 0; idx < ixs.length; idx++) {
            combo.select(ixs[idx]);
        }
    }
    protected void listDeselectAll() {
        combo.deselectAll();
        combo.clearSelection();
    }
    protected void listShowSelection() {
    }
}
