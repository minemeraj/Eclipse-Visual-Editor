/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: SWTLauncher.java,v $
 *  $Revision: 1.11 $  $Date: 2005-08-24 23:30:48 $ 
 */
package org.eclipse.ve.internal.java.vce.launcher.remotevm;

import java.lang.reflect.*;
import java.text.MessageFormat;
import java.util.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.List;
 

/**
 * 
 * @since 1.0.0
 */
public class SWTLauncher implements ILauncher {
		
	protected ArrayList shellList = null;
	protected Constructor controlCtor = null;
	protected int controlCtorType = NOCTOR;
	private static final int 
		NOCTOR = 0,
		DEFAULTCTOR = 1,		// shell()
		DISPLAYCTOR = 2,		// shell(display)
		PARENTCTOR = 3,			// control(parent)
		PARENTSTYLECTOR = 4;	// control(parent, style)
	
	private class ShellInfo implements Comparable {
		public Field shellField = null; 
		public Method createMethod = null;
		public String methodName = null;

		public int compareTo(Object o) {
			if (o instanceof ShellInfo) {
				return toString().compareTo(o.toString());
			} else {
				return 0;
			}
		}
		
		public String toString() {
			return shellField.getName();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.vce.launcher.remotevm.ILauncher#supportsLaunching(java.lang.Class, java.lang.Object)
	 */
	public boolean supportsLaunching(Class clazz) {
		
		if (Control.class.isAssignableFrom(clazz)) {
			// This is a control subclass, so we need a constructor so that we can put a shell around it.
			// We will search in this priority: ctor(), ctor(Display) <- these only valid for shell, ctor(parent), ctor(parent, style)
			Constructor[] ctors = clazz.getDeclaredConstructors();
			boolean isShell = Shell.class.isAssignableFrom(clazz);
			for (int i = 0; i < ctors.length; i++) {
				Class[] parms = ctors[i].getParameterTypes();
				if (parms.length == 0 && isShell) {
					controlCtor = ctors[i];
					controlCtorType = DEFAULTCTOR;
					break;	// ctor() has priority for shells.
				}
				if (parms.length == 1 && isShell && Display.class.isAssignableFrom(parms[0])) {
					controlCtor = ctors[i];
					controlCtorType = DISPLAYCTOR;
					break;	// ctor(Display) has priority for shells.
				}
				if (parms.length == 1 && Composite.class.isAssignableFrom(parms[0])) {
					controlCtor = ctors[i];
					controlCtorType = PARENTCTOR;
					break;	// ctor{parent) has priority for all others.
				}
				if (parms.length == 2 && Composite.class.isAssignableFrom(parms[0]) && parms[1] == Integer.TYPE) {
					controlCtor = ctors[i];	// ctor(parent, style)
					controlCtorType = PARENTSTYLECTOR;
				}
			}
			return controlCtorType != NOCTOR;
		}
		
		shellList = new ArrayList();
		String methodName;
		Method createMethod = null;
		
		// Look for a shell to mock up.
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			if (Shell.class.isAssignableFrom(fields[i].getType())) {
				
				// get the name of the create method: field sShell -> createSShell
				StringBuffer nameBuff = new StringBuffer(fields[i].getName());
				nameBuff.setCharAt(0, Character.toUpperCase(nameBuff.charAt(0)));
				nameBuff.insert(0, "create"); //$NON-NLS-1$
				
				methodName = nameBuff.toString();
				try {
					createMethod = clazz.getDeclaredMethod(methodName, null);
					if (createMethod != null) {
						ShellInfo info = new ShellInfo();
						info.shellField = fields[i];
						info.createMethod = createMethod;
						info.methodName = methodName;
						shellList.add(info);
					}
				} catch (NoSuchMethodException e) { 
					createMethod = null; 
				}
			}
		}
		Collections.sort(shellList);
		return (!shellList.isEmpty());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.vce.launcher.remotevm.ILauncher#launch(java.lang.Class, java.lang.Object, java.lang.String[])
	 */
	public void launch(Class clazz, String[] args) {
		Method mainMethod = null;
		try {
			mainMethod = clazz.getDeclaredMethod("main", new Class[] {String[].class}); //$NON-NLS-1$
		} catch (NoSuchMethodException e) {}
		if (mainMethod != null && Modifier.isStatic(mainMethod.getModifiers())) {
			System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Msg.BeanWithMain_INFO_"), new Object[]{clazz.getName()})); //$NON-NLS-1$
			try {
				// Static method call to the main method
				mainMethod.invoke(null, new Object[]{ args });
				System.exit(0);
			} catch (IllegalArgumentException e1) {
				System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.IllegalAccessException_ERROR_"), new Object[]{clazz.getName()})); //$NON-NLS-1$
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
		}
		
		if (controlCtorType != NOCTOR) {
			runControl(clazz);
			return;
		}
		// should already be initialized by supportsLaunching, but be safe.
		if (shellList == null) {
			if (!supportsLaunching(clazz)) {
				return;
			}
		}
		
		if (!shellList.isEmpty()) {
			try {
				// new up an instance of the java bean
				Constructor ctor = clazz.getDeclaredConstructor(null);
				// Make sure we can intantiate it in case the class it not public
				ctor.setAccessible(true);
				Object bean = ctor.newInstance(null);
				
				if (shellList.size() == 1) {
					runShell((ShellInfo)shellList.get(0), bean);
					return;
				}
				// There's more than one shell, display a prompt to the user to
				// choose which to run.
				ShellInfo info = chooseShell(shellList, bean);
				if (info != null) {
					runShell(info, bean);
				}
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
		}
	}
	
	protected Shell shell = null;
	protected Button okButton = null;
	protected Button codeButton = null;
	protected List selectionList = null;
	protected int selectedIndex = -1;
	protected ShellInfo chooseShell(final ArrayList shells, final Object bean) {
		shell = new Shell(SWT.DIALOG_TRIM);
		shell.setText(VCELauncherMessages.getString("SWTLauncher.ChooseShell.Title")); //$NON-NLS-1$
		shell.setSize(375, 275);
		shell.setLayout(new GridLayout(2, false));
		shell.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent e) {
				selectedIndex = -1;
			}
		});
		
		Label label1 = new Label(shell, SWT.NONE);
		label1.setText(VCELauncherMessages.getString("SWTLauncher.ChooseShell.SelectMessage")); //$NON-NLS-1$
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		label1.setLayoutData(gd);
		
		selectionList = new List(shell, SWT.BORDER);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		selectionList.setLayoutData(gd);
		selectionList.addSelectionListener(new SelectionAdapter() { 
			public void widgetSelected(SelectionEvent e) {
				selectedIndex = selectionList.getSelectionIndex();
				okButton.setEnabled(selectedIndex != -1);
				codeButton.setEnabled(selectedIndex != -1);
			}
		});
		
		Iterator itr = shells.iterator();
		while (itr.hasNext()) {
			selectionList.add(itr.next().toString());
		}
		
		Label label2 = new Label(shell, SWT.WRAP);
		label2.setText(MessageFormat.format(VCELauncherMessages.getString("SWTLauncher.ChooseShell.AddMainMessage"), new Object[] {bean.getClass().getName()})); //$NON-NLS-1$
		gd = new GridData();
		gd.horizontalSpan = 2;
		gd.widthHint = 350;
		label2.setLayoutData(gd);
		
		Label label3 = new Label(shell, SWT.WRAP);
		label3.setText(VCELauncherMessages.getString("SWTLauncher.ChooseShell.CodeExampleMessage")); //$NON-NLS-1$
		gd = new GridData();
		gd.horizontalSpan = 2;
		label3.setLayoutData(gd);
		
		codeButton = new Button(shell, SWT.PUSH);
		codeButton.setText(VCELauncherMessages.getString("SWTLauncher.ChooseShell.CodeButton")); //$NON-NLS-1$
		codeButton.setEnabled(false);
		codeButton.addSelectionListener(new SelectionAdapter() { 
			public void widgetSelected(SelectionEvent e) {
				launchCodeDialog((ShellInfo)shells.get(selectedIndex), shell, bean);
			}
		});
		gd = new GridData();
		gd.horizontalAlignment = GridData.BEGINNING;
		codeButton.setLayoutData(gd);
		
		Composite buttonComposite = new Composite(shell, SWT.NONE);
		gd = new GridData();
		gd.horizontalAlignment = GridData.END;
		gd.grabExcessHorizontalSpace = true;
		buttonComposite.setLayoutData(gd);
		RowLayout row = new RowLayout();
		row.pack = false;
		buttonComposite.setLayout(row);
		
		okButton = new Button(buttonComposite, SWT.PUSH);
		okButton.setText(VCELauncherMessages.getString("SWTLauncher.ChooseShell.OKButton")); //$NON-NLS-1$
		okButton.setEnabled(false);
		okButton.addSelectionListener(new SelectionAdapter() { 
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		
		Button cancelButton = new Button(buttonComposite, SWT.PUSH);
		cancelButton.setText(VCELauncherMessages.getString("SWTLauncher.ChooseShell.CancelButton")); //$NON-NLS-1$
		cancelButton.addSelectionListener(new SelectionAdapter() { 
			public void widgetSelected(SelectionEvent e) {
				selectedIndex = -1;
				shell.dispose();
			}
		});
		
		// center the dialog on the display.
		shell.setLocation(
				(Display.getDefault().getBounds().width - shell.getSize().x) / 2,
				(Display.getDefault().getBounds().height - shell.getSize().y) / 2 );
		shell.pack();
		
		runEventLoop(shell);
		
		if (selectedIndex != -1 && selectedIndex < shells.size()) {
			return (ShellInfo)shells.get(selectedIndex);
		} else {
			return null;
		}
	}
	
	protected void launchCodeDialog(ShellInfo info, Shell parent, Object bean) {
		Shell codeShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		codeShell.setText(VCELauncherMessages.getString("SWTLauncher.ExampleCode.Title")); //$NON-NLS-1$
		codeShell.setLayout(new GridLayout());
		
		Label label1 = new Label(codeShell, SWT.WRAP);
		label1.setText(MessageFormat.format(VCELauncherMessages.getString("SWTLauncher.ExampleCode.Desc"), new Object[] { info.toString() })); //$NON-NLS-1$
		
		Text codeText = new Text(codeShell, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		codeText.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		String codeBegin = "public static void main(String[] args) {\n" + //$NON-NLS-1$
					  "\torg.eclipse.swt.widgets.Display display = org.eclipse.swt.widgets.Display.getDefault();\n"; //$NON-NLS-1$
		String codeContents = 
					  "\t{0} test = new {0}();\n" + //$NON-NLS-1$
					  "\ttest.{1}();\n" + //$NON-NLS-1$
					  "\ttest.{2}.open();\n"; //$NON-NLS-1$
		String codeEnd = 
					  "\twhile (!test.sShell1.isDisposed()) {\n" + //$NON-NLS-1$
					  "\t\tif (!display.readAndDispatch()) display.sleep ();\n" + //$NON-NLS-1$
		              "\t}\n" + //$NON-NLS-1$
					  "\tdisplay.dispose();\n" +		 //$NON-NLS-1$
					  "}"; //$NON-NLS-1$
		
		String beanClassName =getSimpleName(bean.getClass().getName());
		codeText.setText(codeBegin + 
				MessageFormat.format(codeContents, new Object[] { 
						beanClassName, 
						info.createMethod.getName(), 
						info.shellField.getName() }) +
				codeEnd );
		codeText.setSelection(0, codeText.getText().length());
		
		Button okButton = new Button(codeShell, SWT.PUSH);
		okButton.setText(VCELauncherMessages.getString("SWTLauncher.ChooseShell.OKButton")); //$NON-NLS-1$
		okButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				((Button)e.widget).getShell().close();
			}
		});
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.CENTER;
		okButton.setLayoutData(gd);
		
		codeShell.pack();
		codeShell.open();
	}
	
	protected String getSimpleName(String className) {
		int index = className.lastIndexOf("."); //$NON-NLS-1$
		if (index != -1) {
			return className.substring(index + 1);
		} else {
			return className;
		}
	}
	
	protected void runControl(Class clazz) {
		controlCtor.setAccessible(true);
		try {
			switch (controlCtorType) {
				case DEFAULTCTOR:
					System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Msg.BeanWithNullConstructor_INFO_"), new Object[]{clazz.getName()})); //$NON-NLS-1$
					Shell shell = (Shell) controlCtor.newInstance(null);
					runEventLoop(shell);
					break;
					
				case DISPLAYCTOR:
					System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Msg.BeanWithShellMethod_INFO_"), new Object[]{clazz.getName(), controlCtor.toString()})); //$NON-NLS-1$
					Display display = Display.getDefault();
					shell = (Shell) controlCtor.newInstance(new Object[] {display});
					runEventLoop(shell);
					break;
					
				case PARENTCTOR:
					System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Msg.BeanWithShellMethod_INFO_"), new Object[]{clazz.getName(), controlCtor.toString()})); //$NON-NLS-1$
					Shell parentShell = createParentShell(clazz);
					Object control = controlCtor.newInstance(new Object[] {parentShell});
					if (control != null && control instanceof Control) {
						setShellSize(parentShell, (Control) control); // Set shell size based on control size
					}
					runEventLoop(parentShell);
					break;
					
				case PARENTSTYLECTOR:
					System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Msg.BeanWithShellMethod_INFO_"), new Object[]{clazz.getName(), controlCtor.toString()})); //$NON-NLS-1$
					parentShell = createParentShell(clazz);
					control = controlCtor.newInstance(new Object[] {parentShell, new Integer(SWT.NONE)});
					if (control != null && control instanceof Control) {
						setShellSize(parentShell, (Control) control); // Set shell size based on control size
					}
					runEventLoop(parentShell);
					break;
					
			}
		} catch (IllegalArgumentException e) {
			System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.IllegalAccessException_ERROR_"), new Object[]{clazz.getName()})); //$NON-NLS-1$
			e.printStackTrace();		
			System.exit(0);	
		} catch (InstantiationException e) {
			System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.InvocationException_ERROR_"), new Object[]{clazz.getName()})); //$NON-NLS-1$
			e.printStackTrace();	
			System.exit(0);	
		} catch (IllegalAccessException e) {
			System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.IllegalAccessException_ERROR_"), new Object[]{clazz.getName()})); //$NON-NLS-1$
			e.printStackTrace();		
			System.exit(0);	
		} catch (InvocationTargetException e) {
			System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.InvocationException_ERROR_"), new Object[]{clazz.getName()})); //$NON-NLS-1$
			e.printStackTrace();	
			System.exit(0);	
		}
	}

	/*
	 * Get size information from the child (typically a Composite) and use this to set
	 * the Shell (parent) size.
	 */
	private void setShellSize(Shell parentShell, Control child) {
		Point point;
		Rectangle rect;
		point = child.getSize();
		rect = parentShell.computeTrim(0, 0, point.x, point.y);
		parentShell.setSize(rect.width, rect.height);
		if (child instanceof Composite) {
			((Composite) child).layout();
		}
	}
	
	protected Shell createParentShell(Class clazz) {
		Shell parentShell = new Shell(Display.getDefault());
		parentShell.setText(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.FrameTitle.LaunchComponent"), new Object[] {clazz.getName()})); //$NON-NLS-1$
		parentShell.setLayout(new FillLayout());
		return parentShell;
	}
	
	protected void runShell(ShellInfo info, Object bean) {
		info.shellField.setAccessible(true);
		info.createMethod.setAccessible(true);
		
		System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Msg.BeanWithShellMethod_INFO_"), new Object[]{bean.getClass().getName(), info.methodName})); //$NON-NLS-1$
		
		try {
			info.createMethod.invoke(bean, null);
			Shell beanShell = (Shell)info.shellField.get(bean);
			runEventLoop(beanShell);
		} catch (IllegalArgumentException e) {
			System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.IllegalAccessException_ERROR_"), new Object[]{bean.getClass().getName()})); //$NON-NLS-1$
			e.printStackTrace();		
			System.exit(0);	
		} catch (IllegalAccessException e) {
			System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.IllegalAccessException_ERROR_"), new Object[]{bean.getClass().getName()})); //$NON-NLS-1$
			e.printStackTrace();		
			System.exit(0);	
		} catch (InvocationTargetException e) {
			System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.InvocationException_ERROR_"), new Object[]{bean.getClass().getName()})); //$NON-NLS-1$
			e.printStackTrace();	
			System.exit(0);	
		}
	}
	
	protected void runEventLoop(Shell beanShell) {
		Display display = beanShell.getDisplay();
		
		// Size the shell based on the configuration settings
		String pack = System.getProperty("pack"); //$NON-NLS-1$
		if ( pack != null && pack.equals("true")){ //$NON-NLS-1$
			beanShell.pack();
		}
		
		beanShell.open();
		
		while (!beanShell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep ();
		}
		display.dispose();	
	}
}
