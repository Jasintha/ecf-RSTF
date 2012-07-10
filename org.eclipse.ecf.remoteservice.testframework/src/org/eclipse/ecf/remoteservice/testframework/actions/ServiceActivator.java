package org.eclipse.ecf.remoteservice.testframework.actions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.remoteservice.IRemoteServiceContainerAdapter;
import org.eclipse.ecf.remoteservice.IRemoteServiceID;
import org.eclipse.ecf.remoteservice.IRemoteServiceRegistration;
import org.eclipse.ecf.remoteservice.testframework.Activator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class ServiceActivator implements Runnable{

	private BundleContext context;
	private ServiceTracker containerManagerServiceTracker;
	private IContainer container;

	private Object lock;
	private String interfaceName;
	private Object classInstance;
	private IRemoteServiceRegistration registerRemoteService;
	
	@Override
	public void run() {
		 try{
			 synchronized (lock) {
				 	 System.out.println("Start Service Registration");
				     context = Platform.getBundle(Activator.PLUGIN_ID).getBundleContext(); 
				     IContainerManager containerManager = getContainerManagerService();
				     container= containerManager.getContainerFactory().createContainer("ecf.r_osgi.peer");						 
					 IRemoteServiceContainerAdapter containerAdapter = (IRemoteServiceContainerAdapter) container
					.getAdapter(IRemoteServiceContainerAdapter.class);
					setRegisterRemoteService(containerAdapter.registerRemoteService(new String[] { interfaceName }, classInstance, null));				
					System.out.println("service Sucessfully registered"); 
					lock.notify();
			}

		 }catch (Exception e) {
			 e.printStackTrace();
			 lock.notify();
		}
	}
	
	
	public String getserviceHost(){
		
		IRemoteServiceID id = registerRemoteService.getID();
	    String name = id.getName();
	    Pattern pattern = Pattern.compile("(r-osgi://)([^:^/]*)(:\\d*)?(.*)?");
	    Matcher matcher = pattern.matcher(name);
	    matcher.find();
	    String serviceURL = matcher.group(1)+"localhost"+matcher.group(3);            
	    System.out.println(serviceURL);
	    return serviceURL;
	}
	
	private IContainerManager getContainerManagerService() {
		if (containerManagerServiceTracker == null) {
			containerManagerServiceTracker = new ServiceTracker(context, IContainerManager.class.getName(),null);
			containerManagerServiceTracker.open();
			 
		}
		return (IContainerManager) containerManagerServiceTracker.getService();
	}

	public void setLock(Object lock) {
		this.lock = lock;
	}

	public Object getLock() {
		return lock;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setClassInstance(Object classInstance) {
		this.classInstance = classInstance;
	}

	public Object getClassInstance() {
		return classInstance;
	}

	public void setRegisterRemoteService(IRemoteServiceRegistration registerRemoteService) {
		this.registerRemoteService = registerRemoteService;
	}

	public IRemoteServiceRegistration getRegisterRemoteService() {
		return registerRemoteService;
	}

	public IContainer getContainer() {
		return container;
	}

	public void setContainer(IContainer container) {
		this.container = container;
	}

	

}
