package org.eclipse.jface.examples.binding.scenarios.desired;

import org.eclipse.jface.binding.BindingException;
import org.eclipse.jface.binding.ConditionalUpdatableValue;
import org.eclipse.jface.binding.DatabindingService;
import org.eclipse.jface.binding.IUpdatableValue;
import org.eclipse.jface.examples.binding.emf.EMFUpdatableTable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.examples.rcp.adventure.AdventureFactory;
import org.eclipse.ui.examples.rcp.adventure.AdventurePackage;
import org.eclipse.ui.examples.rcp.adventure.Catalog;
import org.eclipse.ui.examples.rcp.adventure.Lodging;
import org.eclipse.swt.widgets.Label;

public class SimpleTableBinding extends Composite {

	private Table table = null;

	private Button addButton = null;

	private Button removeButton = null;

	private DatabindingService dbs;

	private TableViewer tableViewer;

	private IUpdatableValue selectedLodging;

	public SimpleTableBinding(Composite parent, int style)
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
		dbs = SampleData.getSWTtoEMFDatabindingService(this);
		AdventurePackage emfPackage = AdventurePackage.eINSTANCE;

		final Catalog catalog = SampleData.CATALOG_2005;

		dbs.bindTable(dbs.createUpdatableTable(tableViewer, "contents"),
				new EMFUpdatableTable(catalog, "lodgings", new String[] {
						"name", "description" }));

		selectedLodging = dbs.createUpdatableValue(tableViewer, "selection");

		addButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						Lodging lodging = AdventureFactory.eINSTANCE.createLodging();
						lodging.setName("new lodging name");
						lodging.setDescription("new lodging description");
						catalog.getLodgings().add(
								lodging);
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

		dbs.bindValue(removeButton, "enabled", new ConditionalUpdatableValue(
				selectedLodging) {
			protected boolean compute(Object currentValue) {
				return currentValue != null;
			}
		});
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

}
