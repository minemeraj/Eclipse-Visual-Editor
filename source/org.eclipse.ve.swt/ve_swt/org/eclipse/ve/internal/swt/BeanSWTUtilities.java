package org.eclipse.ve.internal.swt;

import java.util.logging.Level;

import org.eclipse.core.runtime.*;
import org.eclipse.emf.ecore.EClassifier;

import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.ClassDescriptorDecoratorPolicy;

import org.eclipse.ve.internal.jcm.BeanDecorator;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.visual.ILayoutPolicyFactory;

public class BeanSWTUtilities {

	public static ILayoutPolicyFactory getLayoutPolicyFactoryFromLayoutManger(EClassifier layoutManagerClass, EditDomain domain) {

		if (layoutManagerClass == null)
			return new NullLayoutPolicyFactory();	// There is nothing we can check against, so we hardcode null.
		if (!(layoutManagerClass instanceof JavaClass))
			return null;	// Not a java class.

		ClassDescriptorDecoratorPolicy policy = ClassDescriptorDecoratorPolicy.getPolicy(domain);
		BeanDecorator decr = (BeanDecorator) policy.findDecorator(layoutManagerClass, BeanDecorator.class, ILayoutPolicyFactory.LAYOUT_POLICY_FACTORY_CLASSNAME_KEY);
		String layoutFactoryClassname = null;
		if (decr != null)
			layoutFactoryClassname = (String) decr.getKeyedValues().get(ILayoutPolicyFactory.LAYOUT_POLICY_FACTORY_CLASSNAME_KEY);
		if (layoutFactoryClassname != null) {
			try {
				Class factoryClass = CDEPlugin.getClassFromString(layoutFactoryClassname);
				ILayoutPolicyFactory fact = (ILayoutPolicyFactory) factoryClass.newInstance();
				CDEPlugin.setInitializationData(fact, layoutFactoryClassname, null);
				return fact;
			} catch (ClassNotFoundException e) {
				JavaVEPlugin.getPlugin().getLogger().log(new Status(IStatus.WARNING, SwtPlugin.getDefault().getDescriptor().getUniqueIdentifier(), 0, "", e), Level.WARNING); //$NON-NLS-1$
			} catch (ClassCastException e) {
				JavaVEPlugin.getPlugin().getLogger().log(new Status(IStatus.WARNING, SwtPlugin.getDefault().getDescriptor().getUniqueIdentifier(), 0, "", e), Level.WARNING); //$NON-NLS-1$
			} catch (InstantiationException e) {
				JavaVEPlugin.getPlugin().getLogger().log(new Status(IStatus.WARNING, SwtPlugin.getDefault().getDescriptor().getUniqueIdentifier(), 0, "", e), Level.WARNING); //$NON-NLS-1$
			} catch (IllegalAccessException e) {
				JavaVEPlugin.getPlugin().getLogger().log(new Status(IStatus.WARNING, SwtPlugin.getDefault().getDescriptor().getUniqueIdentifier(), 0, "", e), Level.WARNING); //$NON-NLS-1$
			} catch (CoreException e) {
				JavaVEPlugin.getPlugin().getLogger().log(new Status(IStatus.WARNING, SwtPlugin.getDefault().getDescriptor().getUniqueIdentifier(), 0, "", e), Level.WARNING); //$NON-NLS-1$
			}
		}
	
	return new UnknownLayoutPolicyFactory();
	// TODO - Need to add the unknown factory return 
	}
}
