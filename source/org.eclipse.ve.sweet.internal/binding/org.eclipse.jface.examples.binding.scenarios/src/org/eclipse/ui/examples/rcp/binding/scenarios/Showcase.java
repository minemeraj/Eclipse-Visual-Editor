package org.eclipse.ui.examples.rcp.binding.scenarios;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.databinding.BindingException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

public class Showcase extends Composite {

	public Showcase(Composite parent, int style) throws BindingException {
		super(parent, style);
		initialize();
	}

	private void initialize() throws BindingException {
		GridLayout gridLayout = new GridLayout(2, true);
		gridLayout.horizontalSpacing = 20;
		gridLayout.verticalSpacing = 20;
		this.setLayout(gridLayout);

//		createGroup(ComboBinding.class);
		createGroup(LabelBinding.class);
		createGroup(ListBinding.class);
		// TODO createGroup(NumberBinding.class);
		createGroup(SimpleListBinding.class);
		createGroup(SimpleTableBinding2.class);
		createGroup(TextBinding.class);
		createGroup(Custom_DerivedAggregateProperty.class);
	}

	private void createGroup(Class clazz) {
		Group group = new Group(this, SWT.NONE);
		group.setText(clazz.getName());
		group.setLayout(new FillLayout());
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		group.setLayoutData(gridData);
		Constructor constructor;
		try {
			constructor = clazz.getConstructor(new Class[] { Composite.class,
					int.class });
			constructor
					.newInstance(new Object[] { group, new Integer(SWT.NONE) });
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws BindingException {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(800, 600);
		shell.setLayout(new FillLayout());
		shell.setText("JFace Binding Showcase");
		new Showcase(shell, SWT.NONE);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

}
