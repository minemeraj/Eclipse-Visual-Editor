package org.eclipse.ve.internal.java.codegen.editorpart;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.texteditor.IUpdate;
import org.eclipse.ui.texteditor.TextEditorAction;

public class ResourceWrapperAction implements IAction, IUpdate {
	
	private TextEditorAction javaEditorAction;
	public boolean isJavaEditorActive; 
	
	public ResourceWrapperAction(TextEditorAction aJavaEditorAction) {
		javaEditorAction = aJavaEditorAction;
	}
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		javaEditorAction.addPropertyChangeListener(listener);
	}
	public int getAccelerator() {
		return javaEditorAction.getAccelerator();
	}
	public String getActionDefinitionId() {
		return javaEditorAction.getActionDefinitionId();
	}
	public String getDescription() {
		return javaEditorAction.getDescription();
	}
	public ImageDescriptor getDisabledImageDescriptor() {
		return javaEditorAction.getDisabledImageDescriptor();
	}
	public HelpListener getHelpListener() {
		return javaEditorAction.getHelpListener();
	}
	public ImageDescriptor getHoverImageDescriptor() {
		return javaEditorAction.getHoverImageDescriptor();
	}
	public String getId() {
		return javaEditorAction.getId();
	}
	public ImageDescriptor getImageDescriptor() {
		return javaEditorAction.getImageDescriptor();
	}
	public IMenuCreator getMenuCreator() {
		return javaEditorAction.getMenuCreator();
	}
	public int getStyle() {
		return javaEditorAction.getStyle();
	}
	public String getText() {
		return javaEditorAction.getText();
	}
	public String getToolTipText() {
		return javaEditorAction.getToolTipText();
	}
	public boolean isChecked() {
		return javaEditorAction.isChecked();
	}
	public boolean isEnabled() {
		return javaEditorAction.isEnabled();
	}
	public boolean isHandled() {
		return javaEditorAction.isHandled();
	}
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		javaEditorAction.removePropertyChangeListener(listener);
	}
	public void run() {
		if(isJavaEditorActive){
			javaEditorAction.run();
		}
	}
	public void runWithEvent(Event event) {
		if(isJavaEditorActive){
			javaEditorAction.runWithEvent(event);
		}
		
	}
	public void setActionDefinitionId(String id) {
		javaEditorAction.setActionDefinitionId(id);
	}
	public void setChecked(boolean checked) {
		javaEditorAction.setChecked(checked);
	}
	public void setDescription(String text) {	
		javaEditorAction.setDescription(text);
	}
	public void setDisabledImageDescriptor(ImageDescriptor newImage) {
		javaEditorAction.setDisabledImageDescriptor(newImage);
	}
	public void setEnabled(boolean enabled) {
		javaEditorAction.setEnabled(enabled);
	}
	public void setHelpListener(HelpListener listener) {
		javaEditorAction.setHelpListener(listener);
	}
	public void setHoverImageDescriptor(ImageDescriptor newImage) {	
		javaEditorAction.setHoverImageDescriptor(newImage);
	}
	public void setId(String id) {
		javaEditorAction.setId(id);
	}
	public void setImageDescriptor(ImageDescriptor newImage) {
		javaEditorAction.setImageDescriptor(newImage);
	}
	public void setMenuCreator(IMenuCreator creator) {
		javaEditorAction.setMenuCreator(creator);
	}
	public void setText(String text) {
		javaEditorAction.setText(text);
	}
	public void setToolTipText(String text) {
		javaEditorAction.setToolTipText(text);
	}
	public void setAccelerator(int keycode) {
		javaEditorAction.setAccelerator(keycode);
	}
	public void update() {
		javaEditorAction.update();
	}
}