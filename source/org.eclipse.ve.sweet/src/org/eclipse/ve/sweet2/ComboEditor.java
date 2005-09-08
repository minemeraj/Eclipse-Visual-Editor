package org.eclipse.ve.sweet2;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.*;

public final class ComboEditor extends AbstractListViewer implements StructuredEditor {

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
		super.refresh();
		// Get the value from the content consumer's binder and select the appropiate item in the list
		if(fContentConsumer != null){	
			Object objectValue = fContentConsumer.getValue();	
			boolean shouldEnableList = true;
			if (fContentConsumer instanceof ObjectContentConsumer){
				// If the object has a property then we only enable the list if the delegate isn't null
				if (fOutput instanceof IObjectDelegate && ((ObjectContentConsumer)fContentConsumer).hasProperty()){
					shouldEnableList = ((IObjectDelegate)fOutput).getValue() != null;
				}
			}
			fCombo.setEnabled(shouldEnableList);
			if(objectValue == null){
				setSelection(null);				
			} else {							
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
		if(fOutput != null){
			fContentConsumer.ouputChanged(fOutput);
			refresh();			
		}		
	}
	public IContentConsumer getContentConsumer() {
		return fContentConsumer;
	}
	public void setOutput(Object anOutput) {
		fOutput = anOutput;
		if(fContentConsumer != null){
			fContentConsumer.ouputChanged(anOutput);
			refresh();
		}
	}
	public Object getOutput() {
		return fOutput;
	}
	public void setSelectionConsumer(ISelectionConsumer aSelectionService) {
		setOutput(aSelectionService.getSource());
		setContentConsumer(aSelectionService);
	}
}
