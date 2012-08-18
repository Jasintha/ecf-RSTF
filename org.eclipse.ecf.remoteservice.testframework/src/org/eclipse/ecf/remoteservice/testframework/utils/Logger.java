package org.eclipse.ecf.remoteservice.testframework.utils;

import java.io.PrintStream;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

public class Logger extends ServiceTracker implements LogService{

	/** LogService interface class name */
	protected final static String clazz = "org.osgi.service.log.LogService"; //$NON-NLS-1$

	/** PrintStream to use if LogService is unavailable */
	private final PrintStream out;
 
	public Logger(BundleContext context, PrintStream out) {
		super(context, clazz, null);
		this.out = out;
	}

	public void log(int level, String message) {
		log(null, level, message, null);
	}

	public void log(int level, String message, Throwable exception) {
		log(null, level, message, exception);
	}

	public void log(ServiceReference reference, int level, String message) {
		log(reference, level, message, null);
	}

	public synchronized void log(ServiceReference reference, int level,
			String message, Throwable exception) {
		ServiceReference[] references = getServiceReferences();

		if (references != null) {
			int size = references.length;

			for (int i = 0; i < size; i++) {
				LogService service = (LogService) getService(references[i]);
				if (service != null) {
					try {
						service.log(reference, level, message, exception);
					} catch (Exception e) {
						// TODO: consider printing to System Error
					}
				}
			}
			return;
		}
	}

	public PrintStream getOut() {
		return out;
	}
}
