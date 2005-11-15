package org.eclipse.ui.examples.rcp.binding.scenarios;

import org.eclipse.jface.databinding.*;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.examples.rcp.adventure.*;

public class SimpleTableBinding2 extends Composite {

	private Table table = null;

	private Button addButton = null;

	private Button removeButton = null;

	private IDataBindingContext dbc;

	private TableViewer tableViewer;

	private IUpdatableValue selectedLodging;

	public SimpleTableBinding2(Composite parent, int style)
			throws BindingException {
		super(parent, style);
		initialize();
	}

	private void initialize() throws BindingException {
		this.setBounds(new org.eclipse.swt.graphics.Rectangle(0, 0, 400, 200));
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		createTable();
		this.setLayout(gridLayout);
		addButton = new Button(this, SWT.NONE);
		addButton.setText("Add Row");
		removeButton = new Button(this, SWT.NONE);
		removeButton.setText("Remove Selected Row");
		bind();
	}

	private void bind() throws BindingException {
		dbc = SampleData.getSWTtoEMFDatabindingContext(this);

		final Catalog catalog = SampleData.CATALOG_2005;

		TableViewerDescription tableViewerDescription = new TableViewerDescription(
				tableViewer);
		tableViewerDescription.addColumn("name");
		tableViewerDescription.addColumn("description");
		dbc.bind(tableViewerDescription, new PropertyDescription(catalog,
				"lodgings"), null);

		selectedLodging = (IUpdatableValue) dbc.createUpdatable(new PropertyDescription(tableViewer,
				ViewersProperties.SINGLE_SELECTION));

		addButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						Lodging lodging = AdventureFactory.eINSTANCE
								.createLodging();
						lodging.setName("new lodging name");
						lodging.setDescription("new lodging description");
						catalog.getLodgings().add(lodging);
					}
				});

		removeButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						Lodging lodging = (Lodging) selectedLodging.getValue();
						if (lodging != null) {
							catalog.getLodgings().remove(lodging);
						}
					}
				});

		dbc.bind(dbc.createUpdatable(new PropertyDescription(removeButton, "enabled")), 
				new ConditionalUpdatableValue(
				selectedLodging) {
			protected boolean compute(Object currentValue) {
				return currentValue != null;
			}
		}, null);
	}

	/**
	 * This method initializes table
	 * 
	 */
	private void createTable() {
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessVerticalSpace = true;
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		TableColumn tableColumn0 = new TableColumn(table, SWT.NONE);
		tableColumn0.setWidth(100);
		tableColumn0.setText("Name");
		TableColumn tableColumn1 = new TableColumn(table, SWT.NONE);
		tableColumn1.setWidth(200);
		tableColumn1.setText("Description");
		tableViewer = new TableViewer(table);
	}

	public static void main(String[] args) throws BindingException {
		Display d = new Display();
		Shell s = new Shell(d, SWT.SHELL_TRIM);
		s.setLayout(new FillLayout());
		SimpleTableBinding2 simpleTableBinding = new SimpleTableBinding2(s,
				SWT.NONE);
		s.pack();
		s.open();
		while (!s.isDisposed()) {
			if (!d.readAndDispatch()) {
				d.sleep();
			}
		}
	}

}
