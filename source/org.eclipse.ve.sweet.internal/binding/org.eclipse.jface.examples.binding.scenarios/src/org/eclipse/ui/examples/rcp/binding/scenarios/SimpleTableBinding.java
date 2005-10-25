package org.eclipse.ui.examples.rcp.binding.scenarios;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.binding.BindingException;
import org.eclipse.jface.binding.ConditionalUpdatableValue;
import org.eclipse.jface.binding.DatabindingContext;
import org.eclipse.jface.binding.IUpdatable;
import org.eclipse.jface.binding.IUpdatableFactory2;
import org.eclipse.jface.binding.IUpdatableValue;
import org.eclipse.jface.binding.TableDescription;
import org.eclipse.jface.examples.binding.emf.EMFUpdatableTable;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.examples.rcp.adventure.AdventureFactory;
import org.eclipse.ui.examples.rcp.adventure.AdventurePackage;
import org.eclipse.ui.examples.rcp.adventure.Catalog;
import org.eclipse.ui.examples.rcp.adventure.Lodging;

public class SimpleTableBinding extends Composite {

	private Table table = null;

	private Button addButton = null;

	private Button removeButton = null;

	private DatabindingContext dbc;

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
		dbc = SampleData.getSWTtoEMFDatabindingContext(this);
		AdventurePackage emfPackage = AdventurePackage.eINSTANCE;

		final Catalog catalog = SampleData.CATALOG_2005;

		dbc.addUpdatableFactory2(new IUpdatableFactory2() {
			public IUpdatable createUpdatable(Map properties, Object description) {
				if (description instanceof TableDescription) {
					TableDescription tableDescription = (TableDescription) description;
					Object object = tableDescription.getObject();
					if (object instanceof EObject) {
						return new EMFUpdatableTable((EObject) object,
								(String) tableDescription.getPropertyID(),
								(String[]) tableDescription
										.getColumnPropertyIDs());
					}
				}
				return null;
			}
		});

		dbc.bind2(tableViewer, new TableDescription(catalog, "lodgings",
				new String[] { "name", "description" }), null);

		selectedLodging = (IUpdatableValue) dbc.createUpdatable(tableViewer,
				"selection");

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

		dbc.bind(removeButton, "enabled", new ConditionalUpdatableValue(
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

	public static void main(String[] args) throws BindingException {
		Display d = new Display();
		Shell s = new Shell(d, SWT.SHELL_TRIM);
		s.setLayout(new FillLayout());
		SimpleTableBinding simpleTableBinding = new SimpleTableBinding(s,
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
