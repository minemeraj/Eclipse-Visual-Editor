package org.eclipse.ve.internal.java.visual;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.PreferencesUtil;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

public abstract class LayoutListMenuContributor {
		
	protected abstract EditPart getEditPart();
	
	protected abstract IJavaInstance getBean();
	
	protected abstract EStructuralFeature getLayoutSF();
	
	protected abstract IBeanProxy getLayoutBeanProxyAdapter();		
	
	protected abstract String[][] getLayoutItems();
	
	protected abstract ILayoutPolicyFactory getLayoutPolicyFactory(JavaClass layoutManagerClass);	
	
	protected abstract VisualContainerPolicy getVisualContainerPolicy();
		
	protected EditDomain getEditDomain(){
		return EditDomain.getEditDomain(getEditPart());		
	}
	
	protected IJavaInstance getNewLayoutInstance(String layoutTypeName){
		return layoutTypeName == null ? null : BeanUtilities.createJavaObject(layoutTypeName,getBean().eResource().getResourceSet(),(String)null);
	}
	
	public void fillMenuManager(MenuManager aMenuManager){
		final EditDomain editDomain = EditDomain.getEditDomain(getEditPart());				
		// Get the type of the current layout manager class to set who is selected in the list
		IBeanProxy layoutBeanProxy = getLayoutBeanProxyAdapter(); 			
		String currentLayoutManagerType = layoutBeanProxy == null ? null : layoutBeanProxy.getTypeProxy().getFormalTypeName();
		String[][] layoutItems = getLayoutItems();
		// Two arg array.  First element is type, second is array of names
		for (int i = 0; i < layoutItems[0].length; i++) {
			final String layoutDisplayName = layoutItems[1][i];
			final String layoutTypeName = layoutItems[0][i];
			Action changeLayoutAction = new Action(){
				public String getText() {
					return layoutDisplayName == null ? "null" : layoutDisplayName;
				}
				public String getToolTipText() {
					return getText();
				}
				public void run() {
					RuledCommandBuilder cbld = new RuledCommandBuilder(editDomain);								
					ILayoutPolicyFactory layoutFactory = null;
					IJavaInstance newLayout = null;					
					if(layoutTypeName == null || layoutTypeName.length() == 0){
						layoutFactory = getLayoutPolicyFactory(null);
					} else {
						// 	Change the layout manager
						newLayout = getNewLayoutInstance(layoutTypeName);					
						JavaClass layoutManagerClass = Utilities.getJavaClass(layoutTypeName,getBean().eResource().getResourceSet());
						layoutFactory = getLayoutPolicyFactory(layoutManagerClass);											
					}
					if (layoutFactory != null) {
						VisualContainerPolicy cp = getVisualContainerPolicy();
						cp.setContainer(getBean());
						ILayoutSwitcher switcher = layoutFactory.getLayoutSwitcher(cp);
						if (switcher != null) {
							cbld.append(switcher.getCommand(getLayoutSF(), newLayout));
						}
					} else {							
						// 	If no switcher apply the value
						cbld.applyAttributeSetting(getBean(),getLayoutSF(), newLayout);
					}
					editDomain.getCommandStack().execute(cbld.getCommand());
				}
			};
			// Set the checked state to match the current layout manager
			// It is either the layout name (in full) or else null
			if(currentLayoutManagerType != null && currentLayoutManagerType.equals(layoutTypeName)){
				changeLayoutAction.setChecked(true);
			} else if (currentLayoutManagerType == null && layoutTypeName.length() == 0){ // For null the returned list includes it as an empty string
				changeLayoutAction.setChecked(true);
			}
			aMenuManager.add(changeLayoutAction);						
		}
		if(aMenuManager.getItems() != null){
			aMenuManager.add(new Separator());
			// Create a separator and then a "Preferences" that opens the preference page directly to change the default layout
			aMenuManager.add(new Action(){
				public void run() {
					String prefID = getPreferencePageID();
					PreferencesUtil.createPreferenceDialogOn(Display.getCurrent().getActiveShell(), prefID, new String[]{prefID}, null).open();
				}
				public String getText() {
					return "Preferences";
				}
			});
		}
	}
	protected abstract String getPreferencePageID();

}
