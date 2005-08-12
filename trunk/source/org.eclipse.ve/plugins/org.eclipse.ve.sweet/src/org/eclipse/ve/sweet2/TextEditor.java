package org.eclipse.ve.sweet2;

import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class TextEditor extends ContentViewer implements Editor {
	
	private Text text;
	private IValueProvider valueProvider;
	public static int DEFAULT_COMMIT_POLICY = COMMIT_FOCUS;
	private Listener updateListener;
	private int updateListenerType;
	private int commitPolicy = DEFAULT_COMMIT_POLICY;
	private IDomainProvider domainProvider;
	
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
		if(!text.isDisposed()){
			String displayValue = ((LabelProvider)getLabelProvider()).getText(valueProvider.getValue());
			text.setText(displayValue);
		}
	}

	public void setSelection(ISelection selection, boolean reveal) {

	}
	
	public void setContentProvider(IContentProvider contentProvider) {
		super.setContentProvider(contentProvider);
		valueProvider = (IValueProvider)contentProvider;
		getText().setEnabled(valueProvider.canSetValue());
		setInput(valueProvider.getValue());
	}
	
	public void setUpdatePolicy(int aCommitPolicy){
				
		if(updateListener != null){
			text.removeListener(getListenerType(commitPolicy),updateListener);
		} else {
			updateListener = new Listener(){
				public void handleEvent(Event event) {
					// Push the changes down to the model domain
					Object modelValue = getDomainProvider() == null ? 
							text.getText() : 
							getDomainProvider().getValue(text.getText());
					valueProvider.setValue(modelValue);
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

	public IDomainProvider getDomainProvider() {
		if(domainProvider == null && valueProvider.getValue() != null){
			// To to use a default domain provider based on the object type
			domainProvider = DomainProviderFactory.getDefaultDomainProvider(valueProvider.getValue().getClass());
		}
		return domainProvider;
	}
}