package org.eclipse.ve.sweet2;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import org.eclipse.jface.util.Assert;
import org.eclipse.jface.util.IOpenEventListener;
import org.eclipse.jface.util.OpenStrategy;
import org.eclipse.jface.viewers.AbstractListViewer;
import org.eclipse.jface.viewers.ILabelProvider;
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

public class ListEditor extends AbstractListViewer implements Editor {
	
    private java.util.List listMap = new ArrayList();	
	private IContentConsumer fContentConsumer;
	private List fList;
	
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
				setSelection(new StructuredSelection(objectValue),true);
			}
		}
	}


	public void setUpdatePolicy(int updatePolicy) {
		
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

	 protected Widget doFindItem(Object element) {
	        if (element != null) {
	            if (listMap.contains(element))
	                return getControl();
	        }
	        return null;
	    }	
	 
	 protected void doUpdateItem(Widget data, Object element, boolean fullMap) {
	        if (element != null) {
	            int ix = listMap.indexOf(element);
	            if (ix >= 0) {
	                ILabelProvider labelProvider = (ILabelProvider) getLabelProvider();
	                listSetItem(ix, getLabelProviderText(labelProvider,element));
	            }
	        }
	    }
	 
	 private String getLabelProviderText(ILabelProvider labelProvider, Object element){
	    	String text = labelProvider.getText(element);
	        if(text == null)
	        	return "";//$NON-NLS-1$
	        return text;
	    }	 
	 
	 public Object getElementAt(int index) {
	        if (index >= 0 && index < listMap.size())
	            return listMap.get(index);
	        return null;
	    }	
	 
	  protected int indexForElement(Object element) {
	        ViewerSorter sorter = getSorter();
	        if (sorter == null)
	            return listGetItemCount();
	        int count = listGetItemCount();
	        int min = 0, max = count - 1;
	        while (min <= max) {
	            int mid = (min + max) / 2;
	            Object data = listMap.get(mid);
	            int compare = sorter.compare(this, data, element);
	            if (compare == 0) {
	                // find first item > element
	                while (compare == 0) {
	                    ++mid;
	                    if (mid >= count) {
	                        break;
	                    }
	                    data = listMap.get(mid);
	                    compare = sorter.compare(this, data, element);
	                }
	                return mid;
	            }
	            if (compare < 0)
	                min = mid + 1;
	            else
	                max = mid - 1;
	        }
	        return min;
	    }	 
	
}
