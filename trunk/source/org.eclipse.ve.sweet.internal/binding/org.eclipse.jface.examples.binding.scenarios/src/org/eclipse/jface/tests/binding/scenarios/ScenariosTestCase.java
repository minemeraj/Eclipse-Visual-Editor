package org.eclipse.jface.tests.binding.scenarios;

import junit.framework.TestCase;

import org.eclipse.jface.binding.DatabindingContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.examples.rcp.binding.scenarios.SampleData;

/**
 * Abstract base class of the JFace binding scenario test classes.
 */

abstract public class ScenariosTestCase extends TestCase {

	private Composite composite;

	private DatabindingContext dbc;

	private Display display;

	private boolean disposeDisplay = false;

	private Shell shell;

	protected Composite getComposite() {
		return composite;
	}

	protected DatabindingContext getDbc() {
		return dbc;
	}

	public Shell getShell() {
		Shell result = BindingScenariosTestSuite.getShell();
		if (result == null) {
			display = Display.getDefault();
			if (Display.getDefault() == null) {
				display = new Display();
				disposeDisplay = true;
			}
			shell = new Shell(display, SWT.SHELL_TRIM);
			shell.setLayout(new FillLayout());
			result = shell;
		}
		result.setText(getName()); // In the case that the shell() becomes
									// visible.
		return result;
	}

	protected void spinEventLoop(int secondsToWaitWithNoEvents) {
		if (!composite.isVisible()) {
			composite.getShell().open();
		}
		while (composite.getDisplay().readAndDispatch())
			;
		try {
			Thread.sleep(secondsToWaitWithNoEvents * 1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	protected void setUp() throws Exception {
		composite = new Composite(getShell(), SWT.NONE);
		composite.setLayout(new GridLayout());
		SampleData.initializeData(); // test may manipulate the data... let
										// all start from fresh
		dbc = SampleData.getSWTtoEMFDatabindingContext(composite);
	}

	protected void tearDown() throws Exception {
		composite.dispose();
		composite = null;
		if (shell != null) {
			shell.dispose();
		} else
			dbc.dispose();
		if (display != null && disposeDisplay) {
			display.dispose();
		}
	}

}
