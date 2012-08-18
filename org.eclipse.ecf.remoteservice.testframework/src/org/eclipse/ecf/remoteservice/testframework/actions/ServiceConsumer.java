package org.eclipse.ecf.remoteservice.testframework.actions;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.remoteservice.IRemoteService;
import org.eclipse.ecf.remoteservice.IRemoteServiceContainerAdapter;
import org.eclipse.ecf.remoteservice.IRemoteServiceReference;
import org.eclipse.ecf.remoteservice.testframework.Activator;
import org.eclipse.ui.console.MessageConsoleStream;
//import org.jasintha.ecf.service.cal.ICal;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class ServiceConsumer implements Runnable {

	private static BundleContext context;
	public  String serviceURL;
	private ServiceTracker containerManagerServiceTracker;
	private IContainer container;
	private String  interfaceName; //ICal.class.getName()
	private IRemoteService remoteService;
	private Object lock;
	private String containerDescription; //ecf.r_osgi.peer
	private MessageConsoleStream console;
	
 
	
	public Object getLock() {
		return lock;
	}


	public void setLock(Object lock) {
		this.lock = lock;
	}


	@Override
	public void run() {
		try{
		synchronized (lock) {
			context = Platform.getBundle(Activator.PLUGIN_ID).getBundleContext(); 
			IContainerManager containerManager = getContainerManagerService();
			container = containerManager.getContainerFactory().createContainer(containerDescription);
			IRemoteServiceContainerAdapter containerAdapter = (IRemoteServiceContainerAdapter) container
					.getAdapter(IRemoteServiceContainerAdapter.class);
			IRemoteServiceReference[] helloReferences = containerAdapter
					.getRemoteServiceReferences(IDFactory.getDefault().createID(container.getConnectNamespace(),
							serviceURL), interfaceName, null);
			remoteService = containerAdapter.getRemoteService(helloReferences[0]);
			System.out.println("Service Consumer installed");
			console.println("Service Consumer installed");
			lock.notify();
		}	
		}catch(Exception e){
		  	e.printStackTrace();
		  	console.println(e.getMessage());
			lock.notify();
		}
	}
	
	
	private IContainerManager getContainerManagerService() {
		if (containerManagerServiceTracker == null) {
	    containerManagerServiceTracker = new ServiceTracker(context, IContainerManager.class.getName(),null);
		containerManagerServiceTracker.open();
		}
		return (IContainerManager) containerManagerServiceTracker.getService();
	}
    public String getInterfaceName() {
		return interfaceName;
	}


	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}


	public IRemoteService getRemoteService() {
		return remoteService;
	}


	public void setRemoteService(IRemoteService remoteService) {
		this.remoteService = remoteService;
	}
	public String getServiceURL() {
		return serviceURL;
	}


	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}


	public void setContainerDescription(String containerDescription) {
		this.containerDescription = containerDescription;
	}


	public String getContainerDescription() {
		return containerDescription;
	}


	public void setConsole(MessageConsoleStream console) {
		this.console = console;
	}


	public MessageConsoleStream getConsole() {
		return console;
	}

}
