package com.ibm.jve.sample.internal.vm;
import org.eclipse.swt.widgets.Composite;

import com.ibm.jve.sample.core.UIContainer;

public class ConcreteUIContainer extends UIContainer {

	private Composite parent;

	protected void create() {
		parent = createBaseContainer(1, false);
		Composite c1 = createComposite(parent, 1);
	}
	
}
