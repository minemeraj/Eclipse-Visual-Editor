package org.eclipse.ve.sweet2.testing;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

public class TestSweet_EditPerson {

	private Shell sShell = null;

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		sShell = new Shell();
		sShell.setText("Shell");
		sShell.setSize(new Point(300, 200));
	}

}
