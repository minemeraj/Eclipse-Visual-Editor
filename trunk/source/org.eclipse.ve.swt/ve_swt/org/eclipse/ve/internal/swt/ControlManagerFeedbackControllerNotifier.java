package org.eclipse.ve.internal.swt;

/**
 * Interface to use to register notifiers for callbacks. Extensions that want
 * to be called back should implement this interface and should register/deregister
 * on the {@link org.eclipse.ve.internal.swt.ControlManager.ControlManagerExtension#primSetExtensionProxy(IBeanProxy)}.
 * 
 * @since 1.1.0
 */
public interface ControlManagerFeedbackControllerNotifier {
	/**
	 * This will be called from the feedback controller when a transaction for the registered notifier comes in.
	 * 
	 * @param msgID
	 * @param parms
	 * 
	 * @since 1.1.0
	 */
	public void calledBack(int msgID, Object[] parms);
}