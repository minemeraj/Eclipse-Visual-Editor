package org.eclipse.ve.sweet.timeexample.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ve.sweet.CannotSaveException;
import org.eclipse.ve.sweet.controllers.IMaster;
import org.eclipse.ve.sweet.objectviewer.IObjectViewer;
import org.eclipse.ve.sweet.objectviewer.ObjectViewerFactory;
import org.eclipse.ve.sweet.table.CompositeTable;
import org.eclipse.ve.sweet.timeexample.model.Model;


public class TimeTracker {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private CompositeTable date = null;
	private WorkdayRow workdayRow = null;
	private CompositeTable work = null;
	private WorkHeader workHeader = null;
	private WorkRow workRow = null;
	private Group group1 = null;
	
	private transient IObjectViewer personEditor;
	
	/**
	 * This method initializes compositeTable	
	 *
	 */
	private void createCompositeTable() {
		date = new CompositeTable(sShell, SWT.NONE);
		date.setMaxRowsVisible(1);
		date.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		date.setRunTime(true);
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = SWT.FILL;
		date.setLayoutData(gridData);
		createWorkdayRow();
	}

	/**
	 * This method initializes workdayRow	
	 *
	 */
	private void createWorkdayRow() {
		workdayRow = new WorkdayRow(date, SWT.NONE);
	}

	/**
	 * This method initializes compositeTable1	
	 *
	 */
	private void createCompositeTable1() {
		work = new CompositeTable(group1, SWT.NONE);
		work.setRunTime(true);
		work.setWeights(new int[] {20, 20, 60});
		createWorkHeader();
		createWorkRow();
	}

	/**
	 * This method initializes workHeader	
	 *
	 */
	private void createWorkHeader() {
		workHeader = new WorkHeader(work, SWT.NONE);
	}

	/**
	 * This method initializes workRow	
	 *
	 */
	private void createWorkRow() {
		workRow = new WorkRow(work, SWT.NONE);
	}

	/**
	 * This method initializes group1	
	 *
	 */
	private void createGroup1() {
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 5;
		fillLayout.marginWidth = 5;
		GridData gridData3 = new org.eclipse.swt.layout.GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.grabExcessVerticalSpace = true;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		group1 = new Group(sShell, SWT.NONE);
		group1.setText("Hours:");
		createCompositeTable1();
		group1.setLayout(fillLayout);
		group1.setLayoutData(gridData3);
	}

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		sShell = new Shell();
		sShell.setText("Time Tracker");
		createCompositeTable();
		createGroup1();
		sShell.setLayout(new GridLayout());
		sShell.setSize(new org.eclipse.swt.graphics.Point(535,355));
		sShell.addShellListener(new org.eclipse.swt.events.ShellAdapter() {
			public void shellClosed(org.eclipse.swt.events.ShellEvent e) {
				try {
					personEditor.commit();
				} catch (CannotSaveException e1) {
					e.doit = false;
				}
				Model.getDefault().save();
			}
		});
	}

	private void bindUI() {
		workdayRow.getDate().setData("ColumnBinding", "Date");
		workdayRow.getDescription().setData("ColumnBinding", "Description");
        personEditor = ObjectViewerFactory.edit(Model.getDefault());
		IMaster masterTable = (IMaster) personEditor.bind(date, "Workdays");
        
		workRow.getStartTime().setData("ColumnBinding", "StartTime");
		workRow.getEndTime().setData("ColumnBinding", "EndTime");
		workRow.getDescription().setData("ColumnBinding", "Description");
		IObjectViewer timeEditor = ObjectViewerFactory.construct();
		timeEditor.bind(work, "Work");
        
        masterTable.addDetailViewer(timeEditor);
	}

	/**
	 * Method main.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		TimeTracker thisClass = new TimeTracker();
		thisClass.createSShell();
		thisClass.bindUI();
		
		thisClass.sShell.open();

		while (!thisClass.sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}
