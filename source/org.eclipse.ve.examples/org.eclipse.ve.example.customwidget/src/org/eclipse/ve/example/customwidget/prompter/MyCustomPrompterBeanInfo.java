
package org.eclipse.ve.example.customwidget.prompter;

import java.beans.*;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;

public class MyCustomPrompterBeanInfo extends SimpleBeanInfo {
	
//	public BeanInfo[] getAdditionalBeanInfo() {
//		try{
//			return new BeanInfo[]{Introspector.getBeanInfo(Composite.class)}; 
//		} catch (IntrospectionException e){
//			return new BeanInfo[0];
//		}
//	}
	
	public PropertyDescriptor[] getPropertyDescriptors() {
		
		try {		
			PropertyDescriptor[] result = new PropertyDescriptor[2]; 
		
			result[0] = new PropertyDescriptor("text",MyCustomPrompter.class);
			result[1] = new PropertyDescriptor("type",MyCustomPrompter.class);
		
			result[1].setValue("enumerationValues", new Object[] {
					"Dots", new Integer(MyCustomPrompter.DOTS), "org.eclipse.ve.example.customwidget.prompter.MyCustomPrompter.DOTS",
					"More", new Integer(MyCustomPrompter.MORE), "org.eclipse.ve.example.customwidget.prompter.MyCustomPrompter.MORE",
					"Open", new Integer(MyCustomPrompter.OPEN), "org.eclipse.ve.example.customwidget.prompter.MyCustomPrompter.OPEN"							      			
			});
			
			return result;
		} catch (IntrospectionException e) {			
			e.printStackTrace();
			return null;
		}
	}
	
	public EventSetDescriptor[] getEventSetDescriptors() {
		
		try{
			MethodDescriptor addButtonSelectionMD = new MethodDescriptor(
				ButtonSelectionListener.class.getMethod("buttonSelected",
				new Class[]{SelectionEvent.class})
				);
			addButtonSelectionMD.setPreferred(true);
			addButtonSelectionMD.setValue("preferred",Boolean.TRUE);
			
			EventSetDescriptor addButtonSelectionED = new EventSetDescriptor(
					"buttonSelection",
					ButtonSelectionListener.class,
					new MethodDescriptor[] {addButtonSelectionMD},
					MyCustomPrompter.class.getMethod("addButtonSelectionListener",new Class[]{ButtonSelectionListener.class}),
					MyCustomPrompter.class.getMethod("removeButtonSelectionListener",new Class[]{ButtonSelectionListener.class})
					);

			addButtonSelectionED.setPreferred(true);
			
			return new EventSetDescriptor[] {addButtonSelectionED};
			
		} catch (Exception e){
			e.printStackTrace();
		}				
		
		return null;
		
	}
		
}
