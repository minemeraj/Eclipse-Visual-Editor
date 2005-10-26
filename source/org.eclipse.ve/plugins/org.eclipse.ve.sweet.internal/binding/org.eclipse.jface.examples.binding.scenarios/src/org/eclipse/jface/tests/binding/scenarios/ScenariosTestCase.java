package org.eclipse.jface.tests.binding.scenarios;

import junit.framework.TestCase;

import org.eclipse.jface.binding.swt.SWTDatabindingContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.examples.rcp.binding.scenarios.SampleData;

/**
 * Abstract base class of the JFace binding scenario test classes.
 */

abstract public class ScenariosTestCase extends TestCase {

	private Composite composite;

	private SWTDatabindingContext dbc;

	private Display display;

	private boolean disposeDisplay = false;

	private Shell shell;

	protected Text dummyText;

	protected Composite getComposite() {
		return composite;
	}

	protected SWTDatabindingContext getDbc() {
		return dbc;
	}

	public Shell getShell() {
		if(shell!=null) {
			return shell;
		}
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
		if (!composite.isVisible() && secondsToWaitWithNoEvents > 0) {
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

	protected void setButtonSelectionWithEvents(Button button, boolean value) {
		if (button.getSelection() == value) {
			return;
		}
		pushButtonWithEvents(button);
	}

	protected void pushButtonWithEvents(Button button) {
		if (!getShell().isVisible()) {
			getShell().open();
			spinEventLoop(0);
		}
		button.forceFocus();
		spinEventLoop(0);
		AutomationUtil.performCharacterEvent(getShell().getDisplay(),
				SWT.KeyDown, ' ');
		spinEventLoop(0);
		AutomationUtil.performCharacterEvent(getShell().getDisplay(),
				SWT.KeyUp, ' ');
		spinEventLoop(0);
	}

	protected void setUp() throws Exception {
		composite = new Composite(getShell(), SWT.NONE);
		composite.setLayout(new GridLayout());
		SampleData.initializeData(); // test may manipulate the data... let
		// all start from fresh
		dbc = SampleData.getSWTtoEMFDatabindingContext(composite);
		dummyText = new Text(getComposite(), SWT.NONE);
		dummyText.setText("dummy");
	}

	protected void tearDown() throws Exception {
		getShell().setVisible(false); // same Shell may be reUsed across tests
		composite.dispose();
		composite = null;
		if (shell != null) {
			shell.close();
			shell.dispose();
		} else
			dbc.dispose();
		if (display != null && disposeDisplay) {
			display.dispose();
		}
	}

	protected void enterText(Text text, String string) {
		if (!getShell().isVisible()) {
			getShell().open();
			spinEventLoop(0);
		}
		text.forceFocus();
		spinEventLoop(0);
		text.setText(string);
		spinEventLoop(0);
		dummyText.forceFocus();
		spinEventLoop(0);
	}

}
