package org.eclipse.ve.sweet2;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class TextEditor extends ContentViewer implements Editor {
	
	private Text text;
	private IElementContentProvider valueProvider;
	private IContentConsumer fContentConsumer;
	public static int DEFAULT_COMMIT_POLICY = COMMIT_FOCUS;
	private Listener updateListener;
	private int updateListenerType;
	private int commitPolicy = DEFAULT_COMMIT_POLICY;
	private IDomainProvider domainProvider;
	private boolean isSettingValue = false;
	private String lastSetValue;
	private IObjectBinder fBinder;
	
	public TextEditor(Text aText){
		text = aText;
		setDefaults();
		hookControl(aText);
	}
	
	public TextEditor(Composite parent, int styles){
		text = new Text(parent,styles);
		hookControl(text);
		setDefaults();
	}
	
	private void setDefaults(){
		text.setEnabled(false);
		setUpdatePolicy(DEFAULT_COMMIT_POLICY);		
	}
	
	public Text getText(){
		return text;
	}

	public Control getControl() {
		return text;
	}

	public ISelection getSelection() {
		return null;
	}

	public void refresh() {
		if(valueProvider == null) return;
		if(!text.isDisposed() && !isSettingValue){
			isSettingValue = true;
			String newValue = "";
			if(getLabelProvider() != null){
				newValue = ((LabelProvider)getLabelProvider()).getText(valueProvider.getElement(getInput()));
			} else {
				newValue = (String) valueProvider.getElement(getInput());
			}
			text.setText(newValue);	
			isSettingValue = false;			
		}
	}

	protected void inputChanged(Object input, Object oldInput) {
		if(input == null){
			text.setEnabled(false);
		} else if (input instanceof IObjectBinder){
			final IObjectBinder binder = (IObjectBinder) input;
			text.setEnabled(binder.getValue() != null);
			// Listen for when the value in the binder changes
			binder.addPropertyChangeListener(new PropertyChangeListener(){
				public void propertyChange(PropertyChangeEvent event) {
					text.setEnabled(binder.getValue() != null);
				}
			});
		} else {
			text.setEnabled(true);
		}
		refresh();
	}
	
	public void setSelection(ISelection selection, boolean reveal) {

	}
	
	public void setContentProvider(IContentProvider contentProvider) {
		Assert.isTrue(contentProvider instanceof IElementContentProvider);
		valueProvider = (IElementContentProvider) contentProvider;
		super.setContentProvider(contentProvider);
	}
	
	public void setUpdatePolicy(int aCommitPolicy){
				
		if(updateListener != null){
			text.removeListener(getListenerType(commitPolicy),updateListener);
		} else {
			updateListener = new Listener(){
				public void handleEvent(Event event) {
					if(!isSettingValue){
						isSettingValue = true;
						// Push the changes down to the model domain
						if (fContentConsumer != null){
							fContentConsumer.setValue(text.getText());
						}
						isSettingValue = false;						
					}
				}				
			};
		}
		
		commitPolicy = aCommitPolicy;
		text.addListener(getListenerType(commitPolicy),updateListener);
		
	}
	private int getListenerType(int aCommitPolicy){
		switch (aCommitPolicy) {
		case COMMIT_MODIFY:
			return SWT.Modify;
		case COMMIT_FOCUS:
			return SWT.FocusOut;
		case COMMIT_EXPLICIT:
			return SWT.None;
		default:
			return DEFAULT_COMMIT_POLICY;
		}		
	}

	public void setDomainProvider(IDomainProvider aDomainProvider) {	
		domainProvider = aDomainProvider;
	}

	public void setContentConsumer(IContentConsumer contentConsumer) {
		fContentConsumer = contentConsumer;
	}

	public IContentConsumer getContentConsumer() {
		return fContentConsumer;
	}
}