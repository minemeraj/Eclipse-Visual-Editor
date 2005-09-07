package org.eclipse.ve.sweet2;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import org.eclipse.jface.util.Assert;
import org.eclipse.jface.util.IOpenEventListener;
import org.eclipse.jface.util.OpenStrategy;
import org.eclipse.jface.viewers.AbstractListViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Widget;

public class ListEditor extends AbstractListViewer implements StructuredEditor {
		
	private IContentConsumer fContentConsumer;
	private List fList;
	private Object fOutput;
	
	public ListEditor(Composite parent) {
        this(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
    }
	public ListEditor(Composite parent, int style) {
        this(new List(parent, style));
    }	
	public ListEditor(List list) {
	        fList = list;
	        hookControl(fList);
	}
	
	protected void hookControl(Control control) {
		super.hookControl(control);
		control.setEnabled(false);
	}

	protected void listAdd(String string, int index) {
		fList.add(string,index);
	}

	protected void listSetItem(int index, String string) {
		fList.setItem(index,string);
	}
	protected int[] listGetSelectionIndices() {
		return fList.getSelectionIndices();
	}

	protected int listGetItemCount() {
		return fList.getItemCount();
	}

	protected void listSetItems(String[] labels) {
		fList.setItems(labels);
	}

	protected void listRemoveAll() {
		fList.removeAll();
	}

	protected void listRemove(int index) {
		fList.remove(index);
	}

	protected void listSetSelection(int[] ixs) {
		fList.setSelection(ixs);
	}

	protected void listShowSelection() {
		fList.showSelection();
	}

	protected void listDeselectAll() {
		fList.deselectAll();
	}
	
	public List getList(){
		return fList;
	}

	public Control getControl() {
		return fList;
	}

	public void reveal(Object element) {
		// TODO
	}
	
	public void refresh() {
		super.refresh();
		// Get the value from the content consumer's binder and select the appropiate item in the list
		if(fContentConsumer != null){			
			Object objectValue = fContentConsumer.getValue();
			if(objectValue == null){
				setSelection(null);				
			} else {
				fList.setEnabled(true);				
				setSelection(new StructuredSelection(objectValue),true);
			}
		} else if (fOutput != null) {
			fList.setEnabled(true);
		} else {
			fList.setEnabled(false);			
		}
	}


	public void setUpdatePolicy(int updatePolicy) {
		
	}

	public void setContentConsumer(IContentConsumer contentConsumer) {
		fContentConsumer = contentConsumer;
		if(fOutput != null){
			fContentConsumer.ouputChanged(fOutput);
			refresh();			
		}
		fContentConsumer.addPropertyChangeListener(new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent event) {
				if(event.getPropertyName() == null){
					refresh();
				}
			}	
		});
	}
	
	protected void handleSelect(SelectionEvent event) {
		// Change the value in the consumer to be the newly selected value
		super.handleSelect(event);
		Object selectedObject = ((IStructuredSelection)getSelection()).getFirstElement();
		if(fContentConsumer != null){
			fContentConsumer.setValue(selectedObject);
		} else if (fOutput instanceof IObjectDelegate){			
			((IObjectDelegate)fOutput).setValue(selectedObject);
		}
	}

	public IContentConsumer getContentConsumer() {
		return fContentConsumer;
	}
	 
	public void setOutput(Object anOutput) {
		fOutput = anOutput;
		if(fContentConsumer != null){
			fContentConsumer.ouputChanged(anOutput);			
		}
		refresh();		
	}
	public Object getOutput() {
		return fOutput;
	}
	public void setSelectionService(ISelectionService aSelectionService) {
		setContentConsumer(aSelectionService);
		setOutput(aSelectionService.getSource());
	}	 
	public void setSelectionService(IObjectDelegate aSelectionService) {
		setOutput(aSelectionService);
	}
	
}
