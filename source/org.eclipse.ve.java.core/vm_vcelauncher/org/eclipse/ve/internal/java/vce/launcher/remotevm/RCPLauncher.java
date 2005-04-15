package org.eclipse.ve.internal.java.vce.launcher.remotevm;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.ViewPart;

public class RCPLauncher implements ILauncher {

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.vce.launcher.remotevm.ILauncher#supportsLaunching(java.lang.Class, java.lang.Object)
	 */
	public boolean supportsLaunching(Class clazz) {
		return ViewPart.class.isAssignableFrom(clazz);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.vce.launcher.remotevm.ILauncher#launch(java.lang.Class, java.lang.Object, java.lang.String[])
	 */
	public void launch(Class clazz, String[] args) {

		Object javaBean = null;
		try {
			// new up an instance of the java bean
			Constructor ctor = clazz.getDeclaredConstructor(null);
			// Make sure we can intantiate it in case the class it not public
			ctor.setAccessible(true);
			javaBean = ctor.newInstance(null);
		} catch (SecurityException e1) {
			System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.InvocationException_ERROR_"), new Object[]{clazz.getName()})); //$NON-NLS-1$
			e1.printStackTrace();	
			System.exit(0);	
		} catch (IllegalArgumentException e1) {
			System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.IllegalAccessException_ERROR_"), new Object[]{clazz.getName()})); //$NON-NLS-1$
			e1.printStackTrace();		
			System.exit(0);	
		} catch (NoSuchMethodException e1) {
			System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.InvocationException_ERROR_"), new Object[]{clazz.getName()})); //$NON-NLS-1$
			e1.printStackTrace();	
			System.exit(0);	
		} catch (InstantiationException e1) {
			System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.InvocationException_ERROR_"), new Object[]{clazz.getName()})); //$NON-NLS-1$
			e1.printStackTrace();	
			System.exit(0);	
		} catch (IllegalAccessException e1) {
			System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.IllegalAccessException_ERROR_"), new Object[]{clazz.getName()})); //$NON-NLS-1$
			e1.printStackTrace();		
			System.exit(0);	
		} catch (InvocationTargetException e1) {
			System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.InvocationException_ERROR_"), new Object[]{clazz.getName()})); //$NON-NLS-1$
			e1.printStackTrace();	
			System.exit(0);	
		}
		
		// Get the RCP View Tab Preferences
		int fTabPosition = 0;
		boolean fTraditionalTabs = false;
		
		try {
			fTabPosition = Integer.parseInt(System.getProperty("rcp.launcher.tabPosition")); //$NON-NLS-1$
			if( "true".equalsIgnoreCase(System.getProperty("rcp.launcher.traditionalTabs"))){ //$NON-NLS-1$
				fTraditionalTabs = true;
			}
		} catch (NumberFormatException e1){
			e1.printStackTrace();	
			System.exit(0);	
		} catch (SecurityException e1){
			e1.printStackTrace();	
			System.exit(0);			
		} catch (IllegalArgumentException e1){
			e1.printStackTrace();	
			System.exit(0);
		} catch (NullPointerException e1){
			e1.printStackTrace();
			System.exit(0);
		}
		
		ViewPart viewPart = (ViewPart) javaBean;
		ViewPartHost viewPartHost = new ViewPartHost();
		String className = viewPart.getClass().getName();
		if(className.indexOf(".") != -1){
			className = className.substring(className.lastIndexOf(".") + 1);
		}
		viewPartHost.setDetails(fTraditionalTabs, fTabPosition, clazz.getName());
		viewPartHost.addViewPart(viewPart, className);
		runEventLoop((Shell)viewPartHost.getWorkbenchShell());
		
	}
	
	protected void runEventLoop(Shell beanShell) {
		Display display = beanShell.getDisplay();
		beanShell.pack();
		beanShell.open();
		
		while (!beanShell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep ();
		}
		display.dispose();	
	}
}
