/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: VETimerTests.java,v $
 *  $Revision: 1.1 $  $Date: 2004-11-16 22:40:03 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.util.*;

/**
 * 
 * @since 1.0.2
 */
public class VETimerTests {

	/**
	 * Default TimerTests class to use when not using your own. It's a global.
	 */
	public static VETimerTests basicTest = new VETimerTests();

	public static final String CURRENT_PARENT_ID = "current parent";
	protected String currentParentId = null;


	protected static class TimerStep {
		static final String START = "Start";
		static final String STOP = "Stop";
		protected String id;
		protected String type;
		protected long currentTime;
		protected String parentId;
}

	protected boolean testOn = false;
	protected List steps;

	public synchronized boolean startStep(String id, String parentId) {
		if (!testOn)
			return true;

		TimerStep step = createTimerStep(id, parentId, TimerStep.START);
		return step != null;
	}

	protected TimerStep createTimerStep(String id, String parentId, String stepType) {
		TimerStep newStep = new TimerStep();
		newStep.parentId = parentId;
		newStep.id = id;
		newStep.type = stepType;
		newStep.currentTime = System.currentTimeMillis();
		steps.add(newStep);

		return newStep;
	}

	public synchronized boolean stopStep(String id) {
		if (!testOn)
			return true;
		TimerStep step = createTimerStep(id, null, TimerStep.STOP);
		return step != null;
	}

	public synchronized boolean startCumulativeStep(String id, String parentId) {
		if (!testOn)
			return true;

		return startStep(id, parentId);
	}

	public synchronized boolean startCumulativeStep(String id) {
		if (!testOn)
			return true;

		return startStep(id, null);
	}

	public synchronized boolean stopCumulativeStep(String id) {
		if (!testOn)
			return true;
		return stopStep(id);
	}

	/**
	 * Clear the tests so that you can restart and do some more tests.
	 * 
	 * 
	 * @since 1.0.2
	 */
	public synchronized void clearTests() {
		if (!testOn)
			return;
		steps.clear();
		currentParentId = null;
	}

	/**
	 * Turn this test on. If not turned on then all calls will quickly return with no errors. This allows the code to stay in place even when not
	 * debugging.
	 * <p>
	 * When turned off, it will clear the test.
	 * 
	 * @param on
	 * 
	 * @since 1.0.2
	 */
	public synchronized void testState(boolean on) {
		if (on == testOn)
			return;
		if (on) {
			testOn = true;
			if (steps == null)
				steps = new ArrayList();
		} else {
			testOn = false;
			steps = null;
		}
		currentParentId = null;
	}
	public synchronized void printIt() {
		if (!testOn)
			return;
		if (steps == null)
			return;
		HashMap startSteps = new HashMap(steps.size() / 2);
		TimerStep prevStep = null;
		TimerStep startStep;
		for (int i = 0; i < steps.size(); i++) {
			TimerStep step = (TimerStep) steps.get(i);
			StringBuffer strb = new StringBuffer(150);
			strb.append(step.currentTime + "\t" + step.type + "\t" + "\"" + step.id + "\"");
			if (step.type.equals(TimerStep.START)) {
				// Store the start step for later lookup when calulating the total time
				startSteps.put(step.id, step);
				strb.append("\t" + "parent(" + (step.parentId != null ? step.parentId : "<none>") + ")");
			} else {
				// This is the stop time for a step. We need to find
				// the corresponding start time and calculate the total time.
				startStep = (TimerStep) startSteps.get(step.id);
				if (startStep != null) {
					strb.append("\t" + "parent(" + (startStep.parentId != null ? startStep.parentId : "<none>") + ")");
					strb.append("\t\t\tTotal time = " + (step.currentTime - startStep.currentTime) + " ms");
				}
			}
			if (i > 0 && (step.currentTime - prevStep.currentTime) > 0)
				System.out.println(" ---  " + (step.currentTime - prevStep.currentTime) + " ms  ---");
			System.out.println(strb);
			prevStep = step;
		}
	}
}