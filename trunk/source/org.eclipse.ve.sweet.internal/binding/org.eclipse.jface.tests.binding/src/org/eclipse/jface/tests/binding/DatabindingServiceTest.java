package org.eclipse.jface.tests.binding;

import junit.framework.TestCase;

import org.eclipse.jface.binding.BindingException;
import org.eclipse.jface.binding.DatabindingService;
import org.eclipse.jface.binding.IConverter;
import org.eclipse.jface.binding.IUpdatableValue;
import org.eclipse.jface.binding.IValidator;
import org.eclipse.jface.binding.SettableValue;
import org.eclipse.jface.tests.binding.util.Mocks;

public class DatabindingServiceTest extends TestCase {

	DatabindingService dbs;

	IUpdatableValue updatableValueRMock;

	IValidator validatorMock;

	SettableValue settableValue1;

	SettableValue settableValue2;

	Object o1 = new Object();

	Object o2 = new Object();

	private static IConverter identityConverter = new IConverter() {

		public Class getModelType() {
			return Object.class;
		}

		public Class getTargetType() {
			return Object.class;
		}

		public Object convertModel(Object object) {
			return object;
		}

		public Object convertTarget(Object object) {
			return object;
		}
	};

	protected void setUp() throws Exception {
		super.setUp();
		dbs = new DatabindingService();
		updatableValueRMock = (IUpdatableValue) Mocks
				.createRelaxedMock(IUpdatableValue.class);
		validatorMock = (IValidator) Mocks.createMock(IValidator.class);
		settableValue1 = new SettableValue(Object.class);
		settableValue2 = new SettableValue(Object.class);
	}

	protected void tearDown() throws Exception {
		Mocks.verify(updatableValueRMock);
		Mocks.verify(validatorMock);
		super.tearDown();
	}

	public void testBindValueModel() {
		Mocks.reset(updatableValueRMock);
		updatableValueRMock.addChangeListener(null);
		updatableValueRMock.getValue();
		Mocks.startChecking(updatableValueRMock);
		dbs.bindValue(settableValue1, updatableValueRMock, identityConverter,
				validatorMock);
		Mocks.verify(updatableValueRMock);
	}

	public void testBindValueTarget() {
		updatableValueRMock.addChangeListener(null);
		updatableValueRMock.setValue(null);
		Mocks.startChecking(updatableValueRMock);
		dbs.bindValue(updatableValueRMock, settableValue2, identityConverter,
				validatorMock);
	}

	public void testBindValuePropagation() throws BindingException {
		settableValue1.setValue(o1);
		settableValue2.setValue(o2);
		dbs.bindValue(settableValue1, settableValue2);
		assertEquals(o2, settableValue1.getValue());
		settableValue1.setValue(o1);
		assertEquals(o2, settableValue2.getValue());
		settableValue1.setValueAndNotify(o1);
		assertEquals(o1, settableValue2.getValue());
		settableValue2.setValue(o2);
		assertEquals(o1, settableValue1.getValue());
		settableValue2.setValueAndNotify(o2);
		assertEquals(o2, settableValue1.getValue());
	}
}
