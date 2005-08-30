package org.eclipse.ve.sweet2;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.AbstractListViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;

public final class ComboEditor extends AbstractListViewer implements Editor {

    private Combo fCombo;
	private IContentConsumer fContentConsumer;
	private Object fOutput;
    
    
    public ComboEditor(Composite parent) {
        this(parent, SWT.READ_ONLY | SWT.BORDER);
    }
    public ComboEditor(Composite parent, int style) {
        this(new Combo(parent, style));
    }
    public ComboEditor(Combo list) {
        fCombo = list;
        hookControl(list);
    }
    protected void hookControl(Control control) {
    	super.hookControl(control);
    	control.setEnabled(false);
    }
    protected void listAdd(String string, int index) {
        fCombo.add(string, index);
    }
    protected void listSetItem(int index, String string) {
        fCombo.setItem(index, string);
    }
    protected int[] listGetSelectionIndices() {
        return new int[] { fCombo.getSelectionIndex() };
    }
    protected int listGetItemCount() {
        return fCombo.getItemCount();
    }
    protected void listSetItems(String[] labels) {
        fCombo.setItems(labels);
    }
    protected void listRemoveAll() {
        fCombo.removeAll();
    }
    protected void listRemove(int index) {
        fCombo.remove(index);
    }
    public Control getControl() {
        return fCombo;
    }
    public Combo getCombo() {
        return fCombo;
    }
    public void reveal(Object element) {
        return;
    }
    protected void listSetSelection(int[] ixs) {
        for (int idx = 0; idx < ixs.length; idx++) {
            fCombo.select(ixs[idx]);
        }
    }
    protected void listDeselectAll() {
        fCombo.deselectAll();
        fCombo.clearSelection();
    }
    protected void listShowSelection() {
    }
	public void setUpdatePolicy(int updatePolicy) {
	}
	protected void handleSelect(SelectionEvent event) {
		super.handleSelect(event);
		// Change the value in the consumer to be the newly selected value
		super.handleSelect(event);
		Object selectedObject = ((IStructuredSelection)getSelection()).getFirstElement();
		if(fContentConsumer != null){
			fContentConsumer.setValue(selectedObject);
		}		
	}
	public void refresh() {
		// Get the value from the content consumer's binder and select the appropiate item in the list
		if(fContentConsumer != null){			
			Object objectValue = fContentConsumer.getValue();
			if(objectValue == null){
				setSelection(null);				
			} else {
				fCombo.setEnabled(true);				
				setSelection(new StructuredSelection(objectValue),true);
			}
		} else {
			fCombo.setEnabled(false);			
		}		
	}
	public void setContentConsumer(IContentConsumer contentConsumer) {
		fContentConsumer = contentConsumer;
		fContentConsumer.addPropertyChangeListener(new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent event) {
				if(event.getPropertyName() == null){
					refresh();
				}
			}	
		});
		refresh();		
	}
	public IContentConsumer getContentConsumer() {
		return fContentConsumer;
	}
	public void setOutput(Object anOutput) {
		fOutput = anOutput;
		fContentConsumer.ouputChanged((IObjectDelegate)anOutput);		
	}
	public Object getOutput() {
		return fOutput;
	}
}
