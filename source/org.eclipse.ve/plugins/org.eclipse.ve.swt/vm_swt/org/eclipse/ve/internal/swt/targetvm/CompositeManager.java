package org.eclipse.ve.internal.swt.targetvm;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class CompositeManager {

public static void ensureChildLayoutDataCorrect(Composite aComposite, String layoutDataClassName) throws ClassNotFoundException {

	Class layoutDataClass = null;
	if(layoutDataClassName == null){
		layoutDataClass = Class.forName(layoutDataClassName);	
	}

	// Iterate over the children and make sure that the composite's children's layout Data
	// are either null or match the argument
	Control[] children = aComposite.getChildren();
	for (int i = 0; i < children.length; i++) {
		Object childLayoutData = children[i].getLayoutData();
		if(childLayoutData == null) continue;  // If the layoutData is null just continue
		if(layoutDataClass == null){		   // If there is no layoutData class then set it to null
			children[i].setLayoutData(null);
			continue;
		}
		if(layoutDataClass.isAssignableFrom(childLayoutData.getClass()))continue; // If the layoutData isn't null and is OK continue
		children[i].setLayoutData(null);	   // Null out the layoutData
	}
}
}
