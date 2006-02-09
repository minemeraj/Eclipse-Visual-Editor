package org.eclipse.ve.internal.forms;

import org.eclipse.jem.internal.instantiation.*;

import org.eclipse.ve.internal.java.core.CopyContributor;
import org.eclipse.ve.internal.java.core.EnsureFactoryCommand;


public class FormToolkitCopyContributor implements CopyContributor {

	public void contributeToCopy(PTMethodInvocation factoryMethodCall) {

		// The receiver of the method invocation is a PTInstanceReference that points to a form toolkit
		// Swop this out for a PTName of {factory:org.eclipse.ui.forms.widgets.FormToolkit} which can be used for the clipboard
		PTName toolkitName = InstantiationFactory.eINSTANCE.createPTName(EnsureFactoryCommand.FACTORY_PREFIX_FLAG+"org.eclipse.ui.forms.widgets.FormToolkit}");
		factoryMethodCall.setReceiver(toolkitName);
		// The first argument of the factory call is always the parent composite.  This needs repacing too
		PTName parentCompositeName = InstantiationFactory.eINSTANCE.createPTName("{parentComposite}");
		factoryMethodCall.getArguments().set(0, parentCompositeName);
	}
}
