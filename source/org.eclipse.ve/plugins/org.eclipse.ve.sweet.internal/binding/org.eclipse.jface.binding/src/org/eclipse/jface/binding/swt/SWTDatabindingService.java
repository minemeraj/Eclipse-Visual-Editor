package org.eclipse.jface.binding.swt;

import org.eclipse.jface.binding.DatabindingService;
import org.eclipse.jface.binding.IUpdatableTable;
import org.eclipse.jface.binding.IUpdatableTableFactory;
import org.eclipse.jface.binding.IUpdatableValue;
import org.eclipse.jface.binding.IUpdatableValueFactory;
import org.eclipse.jface.binding.viewers.AbstractListViewerUpdatableTable;
import org.eclipse.jface.binding.viewers.AbstractListViewerUpdatableValue;
import org.eclipse.jface.binding.viewers.TableViewerUpdatableTable;
import org.eclipse.jface.viewers.AbstractListViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

public class SWTDatabindingService extends DatabindingService {

	public SWTDatabindingService(Control control) {
		super();
		control.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				dispose();
			}
		});
	}

	protected void registerValueFactories() {
		super.registerValueFactories();
		addUpdatableValueFactory(Spinner.class, new IUpdatableValueFactory() {
			public IUpdatableValue createUpdatableValue(Object object,
					Object attribute) {
				return new SpinnerUpdatableValue((Spinner) object,
						(String) attribute);
			}
		});
		addUpdatableValueFactory(Text.class, new IUpdatableValueFactory() {
			public IUpdatableValue createUpdatableValue(Object object,
					Object attribute) {
				return new TextUpdatableValue((Text) object);
			}
		});
		addUpdatableValueFactory(Label.class, new IUpdatableValueFactory() {
			public IUpdatableValue createUpdatableValue(Object object,
					Object attribute) {
				return new LabelUpdatableValue((Label) object);
			}
		});
		addUpdatableValueFactory(Combo.class, new IUpdatableValueFactory() {
			public IUpdatableValue createUpdatableValue(Object object,
					Object attribute) {
				return new ComboUpdatableValue((Combo) object,
						(String) attribute);
			}
		});
		addUpdatableValueFactory(AbstractListViewer.class,
				new IUpdatableValueFactory() {
					public IUpdatableValue createUpdatableValue(Object object,
							Object attribute) {
						return new AbstractListViewerUpdatableValue(
								(AbstractListViewer) object, (String) attribute);
					}
				});
		addUpdatableTableFactory(AbstractListViewer.class,
				new IUpdatableTableFactory() {
					public IUpdatableTable createUpdatableTable(Object object,
							Object attribute) {
						return new AbstractListViewerUpdatableTable(
								(AbstractListViewer) object);
					}
				});
		addUpdatableTableFactory(TableViewer.class,
				new IUpdatableTableFactory() {
					public IUpdatableTable createUpdatableTable(Object object,
							Object attribute) {
						return new TableViewerUpdatableTable(
								(TableViewer) object);
					}
				});
		addUpdatableValueFactory(Table.class, new IUpdatableValueFactory() {
			public IUpdatableValue createUpdatableValue(Object object,
					Object attribute) {
				return new TableUpdatableValue((Table) object, "selection");
			}
		});
	}
}
