package org.eclipse.ve.sweet2;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

public class TextEditor extends ContentViewer implements Editor {
	
	private Text text;
	private IElementContentProvider valueProvider;
	private IContentConsumer fContentConsumer;
	public static int DEFAULT_COMMIT_POLICY = COMMIT_FOCUS;
	private Listener updateListener;
	private int updateListenerType;
	private int commitPolicy = DEFAULT_COMMIT_POLICY;
	private ILabelConsumer fLabelConsumer;
	private boolean isSettingValue = false;
	private String lastSetValue;
	private IObjectDelegate fBinder;
	private Object fOutput;
	
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
		} else if (input instanceof IObjectDelegate){
			final IObjectDelegate binder = (IObjectDelegate) input;
			text.setEnabled(binder.getValue() != null);
			// Listen for when the value in the binder changes
			binder.addPropertyChangeListener(new PropertyChangeListener(){
				public void propertyChange(PropertyChangeEvent event) {
					text.setEnabled(binder.getValue() != null);
				}
			});
			if(fContentConsumer != null){
				fContentConsumer.ouputChanged(binder);
			}
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
	
	public void update() {
		if(!isSettingValue){
			isSettingValue = true;
			// Push the changes down to the model domain
			if (fContentConsumer != null){
				Object value = fLabelConsumer == null ?
						text.getText() :
						fLabelConsumer.getObject(text.getText());
				if (!value.equals(fContentConsumer.getValue()))
				    fContentConsumer.setValue(value);
			}
			isSettingValue = false;						
		}							
	}
	
	public void setUpdatePolicy(int aCommitPolicy){
				
		if(updateListener != null){
			text.removeListener(getListenerType(commitPolicy),updateListener);
		} else {
			updateListener = new Listener(){
				public void handleEvent(Event event) {
					update();
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

	public void setContentConsumer(IContentConsumer contentConsumer) {
		fContentConsumer = contentConsumer;
		if(fOutput != null){
			fContentConsumer.ouputChanged(fOutput);
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
	}

	public Object getOutput() {
		return fOutput;
	}

	public ILabelConsumer getLabelConsumer() {
		return fLabelConsumer;
	}

	public void setLabelConsumer(ILabelConsumer labelConsumer) {
		fLabelConsumer = labelConsumer;
	}
}