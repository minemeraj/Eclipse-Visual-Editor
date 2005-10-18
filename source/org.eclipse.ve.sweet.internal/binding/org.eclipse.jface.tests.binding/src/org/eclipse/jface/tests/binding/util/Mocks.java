/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jface.tests.binding.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.AssertionFailedError;

public class Mocks {

	public static interface EqualityComparator {
		public boolean equals(Object o1, Object o2);
	}

	private static EqualityComparator defaultEqualityComparator = new EqualityComparator() {
		public boolean equals(Object o1, Object o2) {
			return o1 == null ? o2 == null : o1.equals(o2);
		}
	};

	private static EqualityComparator indifferentEqualityComparator = new EqualityComparator() {
		public boolean equals(Object o1, Object o2) {
			return true;
		}
	};

	private static interface Mock {
		public MockInvocationHandler getMockInvocationHandler();
	}

	private static Method getMockInvocationHandlerMethod;

	private static Method equalsMethod;

	static {
		try {
			getMockInvocationHandlerMethod = Mock.class.getMethod(
					"getMockInvocationHandler", new Class[0]);
			equalsMethod = Object.class.getMethod("equals",
					new Class[] { Object.class });
		} catch (Exception e) {
			// ignore, will lead to NullPointerExceptions later on
		}
	}

	private static final class MockInvocationHandler implements
			InvocationHandler {

		private class MethodCall {
			private final Method method;

			private final Object[] args;

			public MethodCall(Method method, Object[] args) {
				this.method = method;
				this.args = args;
			}

			public boolean equals(Object obj) {
				if (!(obj instanceof MethodCall)) {
					return false;
				}
				MethodCall other = (MethodCall) obj;
				if (other.method != method
						|| (other.args == null && args != null)
						|| (other.args != null && args == null)
						|| (args != null && other.args.length != args.length)) {
					return false;
				}
				if (args != null) {
					for (int i = 0; i < args.length; i++) {
						if (!equalityComparator.equals(args[i], other.args[i])) {
							return false;
						}
					}
				}
				return true;
			}
		}

		List previousCallHistory = null;

		List currentCallHistory = new ArrayList();

		private final boolean ordered;

		private final EqualityComparator equalityComparator;

		public MockInvocationHandler(boolean ordered,
				EqualityComparator equalityComparator) {
			this.ordered = ordered;
			this.equalityComparator = equalityComparator;
		}

		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			if (getMockInvocationHandlerMethod.equals(method)) {
				return this;
			}
			if (equalsMethod.equals(method)) {
				return new Boolean(proxy == args[0]);
			}
			MethodCall methodCall = new MethodCall(method, args);
			if (previousCallHistory != null) {
				// we are in replay mode
				if (!previousCallHistory.contains(methodCall)) {
					throw new AssertionFailedError("unexpected method call: "
							+ method.getName());
				}
				if (ordered) {
					if (previousCallHistory.size() <= currentCallHistory.size()) {
						throw new AssertionFailedError("extra method call: "
								+ method.getName());
					}
					MethodCall previousCall = (MethodCall) previousCallHistory
							.get(currentCallHistory.size());
					if (!methodCall.equals(previousCall)) {
						throw new AssertionFailedError(
								"different method call (expected:"
										+ previousCall.method.getName()
										+ ", actual:" + method.getName() + ")");
					}
				}
			}
			currentCallHistory.add(methodCall);
			Class returnType = method.getReturnType();
			if (returnType.isPrimitive() && void.class != returnType) {
				return returnType.newInstance();
			}
			return null;
		}

		public void replay() {
			previousCallHistory = currentCallHistory;
			currentCallHistory = new ArrayList();
		}

		public void verify() {
			if (previousCallHistory == null && currentCallHistory.size() == 0) {
				// mock object was not used at all
				return;
			}
			if (ordered) {
				int numMissingCalls = previousCallHistory.size()
						- currentCallHistory.size();
				if (numMissingCalls > 0) {
					throw new AssertionFailedError("missing method calls ("
							+ numMissingCalls + ")");
				}
				for (int i = 0; i < previousCallHistory.size(); i++) {
					if (!previousCallHistory.get(i).equals(
							currentCallHistory.get(i))) {
						throw new AssertionFailedError(
								"method call did not match (" + i + " of "
										+ currentCallHistory.size() + ")");
					}
				}
			} else {
				for (Iterator it = previousCallHistory.iterator(); it.hasNext();) {
					MethodCall methodCall = (MethodCall) it.next();
					if (!currentCallHistory.contains(methodCall)) {
						throw new AssertionFailedError("missing method call:"
								+ methodCall.method.getName());
					}
				}
			}
		}

		public void reset() {
			previousCallHistory = null;
			currentCallHistory = new ArrayList();
		}
	}

	/**
	 * Creates a mock object that neither looks at the order of method calls nor
	 * at the arguments.
	 * 
	 * @param interfaceType
	 * @return a mock object that checks for the order of method invocations but
	 *         not for equality of method arguments
	 */
	public static Object createRelaxedMock(Class interfaceType) {
		return createMock(interfaceType, false, indifferentEqualityComparator);
	}

	/**
	 * Creates a mock object that does not look at the arguments, but checks
	 * that the order of calls is as expected.
	 * 
	 * @param interfaceType
	 * @return a mock object that checks for the order of method invocations but
	 *         not for equality of method arguments
	 */
	public static Object createOrderedMock(Class interfaceType) {
		return createMock(interfaceType, true, indifferentEqualityComparator);
	}

	/**
	 * creates a fussy mock object
	 * 
	 * @param interfaceType
	 * @return a mock object that checks for the order of method invocations and
	 *         for equality of method arguments
	 */
	public static Object createMock(Class interfaceType) {
		return createMock(interfaceType, true, defaultEqualityComparator);
	}

	/**
	 * creates a fussy mock object with a comparator
	 * 
	 * @param interfaceType
	 * @return a mock object that checks for the order of method invocations and
	 *         uses the given comparator to compare method arguments
	 */
	public static Object createMock(Class interfaceType,
			EqualityComparator equalityComparator) {
		return createMock(interfaceType, true, equalityComparator);
	}

	private static Object createMock(Class interfaceType, boolean ordered,
			EqualityComparator equalityComparator) {
		if (!interfaceType.isInterface()) {
			throw new IllegalArgumentException();
		}
		MockInvocationHandler mockInvocationHandler = new MockInvocationHandler(
				ordered, equalityComparator);
		Object newProxyInstance = Proxy.newProxyInstance(interfaceType
				.getClassLoader(), new Class[] { interfaceType, Mock.class },
				mockInvocationHandler);
		return newProxyInstance;
	}

	public static void startChecking(Object mock) {
		getMockInvocationHandler(mock).replay();
	}

	public static void verify(Object mock) {
		getMockInvocationHandler(mock).verify();
	}

	public static void reset(Object mock) {
		getMockInvocationHandler(mock).reset();
	}

	private static MockInvocationHandler getMockInvocationHandler(Object mock) {
		return ((Mock) mock).getMockInvocationHandler();
	}
}
