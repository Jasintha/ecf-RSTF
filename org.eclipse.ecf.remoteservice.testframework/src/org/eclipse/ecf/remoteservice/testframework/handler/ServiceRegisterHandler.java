package org.eclipse.ecf.remoteservice.testframework.handler;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ecf.remoteservice.testframework.actions.ServiceActivator;
import org.eclipse.ecf.remoteservice.testframework.actions.ServiceClassLoader;
import org.eclipse.ecf.remoteservice.testframework.actions.ServiceConsumer;
import org.eclipse.ecf.remoteservice.testframework.actions.TestOutputConsole;
import org.eclipse.ecf.remoteservice.testframework.annotaion.processer.AnnotationProcesser;
import org.eclipse.ecf.remoteservice.testframework.model.AnnotatedTestClass;
import org.eclipse.ecf.remoteservice.testframework.model.AnnotatedTestSuit;
import org.eclipse.ecf.remoteservice.testframework.ui.FrameworkEditor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.MessageConsoleStream;

 

public class ServiceRegisterHandler implements IActionDelegate {
	
	private List<String> classNameList;
	private String projectClasspath;
	private MessageConsoleStream userConsole;
	private IClasspathEntry[] rawClasspath;
    private IProject[] workspIProjects;
    private String serviceContainer;
    private String ConsumerContainer;
    private String hostId;
    private static String testSuit;
    
	@Override
	public void run(IAction arg0) {
		try {
			loadTestConsole();
		    openEditor(); 

		  } catch (Exception e) {
			userConsole.println(e.getMessage());
			e.printStackTrace();
		}

	}
	
	public void openEditor() throws PartInitException {
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		IEditorDescriptor findEditor = window
				.getWorkbench()
				.getEditorRegistry()
				.findEditor(
						"org.eclipse.ecf.remoteservice.testframework.ui.editorID");
		page.openEditor(new NullEditorInput(),findEditor.getId());
		FrameworkEditor.setHandler(this);
	}
	
	public void testInit(){
		try {
			final Object lock = new Object();
			AnnotationProcesser processer = new AnnotationProcesser();
		 
			ServiceClassLoader classLoader = new ServiceClassLoader(
					classNameList, projectClasspath, processer, rawClasspath,workspIProjects);
			
			List<AnnotatedTestClass> testClassesList = classLoader
					.getTestClassesList();
			if(testSuit!=null){
				Class<?> testsuit = classLoader.getJdbcJarLoader().loadClass(testSuit);
				if(testsuit!=null){
				new AnnotatedTestSuit().start(testsuit);
				}
			}
			
			if(testClassesList.size()>0){
				
			for (AnnotatedTestClass testClass : testClassesList) {
				String serviceURL=null;
				if("local".equals(this.getHostId())){
				ServiceActivator service = startService(lock, classLoader,
						testClass);
				serviceURL = service.getserviceHost();
				}else{
					serviceURL =this.getHostId();
				}

				ServiceConsumer serviceConsumer = serviceConsumer(lock,
						classLoader, testClass, serviceURL);

					RemoteProxyInvocationHandler.setServiceProxy(serviceConsumer.getRemoteService()
									.getProxy(
											classLoader.getJdbcJarLoader(),
											new Class[] { classLoader
													.getclass(testClass
															.getiService()) }));
					testClass.setUserConsole(userConsole);
					
					URLClassLoader jdbcJarLoader = classLoader.getJdbcJarLoader();
					testClass.Start(jdbcJarLoader);

			}
		  	   userConsole.println("\n\n Test completed Successfully !!");
			}else{
				userConsole.println("Test Classes NOT found ! \n\n");
			}
			if(testSuit!=null){
				Class<?> testsuit = classLoader.getJdbcJarLoader().loadClass(testSuit);
				if(testsuit!=null){
				new AnnotatedTestSuit().finish(testsuit);
				}
			}
		}catch (Exception e) {
			userConsole.println(e.getMessage());
			e.printStackTrace();
		}

	}

	private void loadTestConsole() {
		TestOutputConsole report = new TestOutputConsole();
	    userConsole = report.getOut();
	    try{
	    	 userConsole.print("****************");
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	   
	}

	public void setServiceContainer(String serviceContainer) {
		this.serviceContainer = serviceContainer;
	}

	public String getServiceContainer() {
		return serviceContainer;
	}

	public void setConsumerContainer(String consumerContainer) {
		ConsumerContainer = consumerContainer;
	}

	public String getConsumerContainer() {
		return ConsumerContainer;
	}

	/**
	 * @param lock
	 * @param classLoader
	 * @param testClass
	 * @param service
	 * @throws ClassNotFoundException
	 * @throws InterruptedException
	 */
  private ServiceConsumer serviceConsumer(final Object lock,
			ServiceClassLoader classLoader, AnnotatedTestClass testClass,
			String serviceURL) throws  Exception,
			InterruptedException {
		 ServiceConsumer consumer = new  ServiceConsumer();
		 consumer.setInterfaceName(classLoader.getclass(testClass.getiService()).getName());
		 consumer.setServiceURL(serviceURL);
		 consumer.setContainerDescription(getConsumerContainer());
		 consumer.setConsole(userConsole);
		 consumer.setLock(lock);
 		   
 		   Thread sct = new Thread(consumer);
 		   synchronized(lock) {
		    sct.start();
		    lock.wait();
		}
 	  return consumer;   
	}

  private ServiceActivator startService(final Object lock,
			ServiceClassLoader classLoader, AnnotatedTestClass testClass)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, InterruptedException,Exception {
	     ServiceActivator service = new ServiceActivator();
		 service.setClassInstance(classLoader.getclass(testClass.getImplService()).newInstance());
		 service.setInterfaceName(classLoader.getclass(testClass.getiService()).getName());
		 service.setContainerDescription(getServiceContainer());
		 service.setConsole(userConsole);
		 service.setLock(lock);
		   Thread sct = new Thread(service);
 		   synchronized(lock) {
		    sct.start();
		    lock.wait();
		}
		 return service;
	}

	@Override
  public void selectionChanged(IAction arg0, ISelection selection) {
 
		if (selection instanceof IStructuredSelection) {
			 try { 
			 IStructuredSelection StructuredSelection = (IStructuredSelection) selection;
			 IResource resource = (IResource) StructuredSelection.getFirstElement();
			 workspIProjects = resource.getWorkspace().getRoot().getProjects();
			 IProject project = resource.getProject();
			 project.open(new NullProgressMonitor());
			 project.build(IncrementalProjectBuilder.FULL_BUILD, new NullProgressMonitor());
			 project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
 
			 classNameList =ServiceClassLoader.getClasses(project,null);
			 System.out.println(classNameList.size());
			 IJavaProject jp = JavaCore.create(project);
			 rawClasspath = jp.getRawClasspath();
			 
					IPath outputLocation = jp.getOutputLocation();
					projectClasspath = ResourcesPlugin.getWorkspace().getRoot()
							.getFolder(outputLocation).getLocation().toFile()
							.toString();
 
			 } catch (Exception e) {
					e.printStackTrace();
				}
		}
		
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public String getHostId() {
		return hostId;
	}

	public static void setTestSuit(String testSuit) {
		ServiceRegisterHandler.testSuit = testSuit;
	}

	public static String getTestSuit() {
		return testSuit;
	}
	
/*	private static List<String> getClasses(IProject project,String classname){
		IJavaProject jp = JavaCore.create(project);
		  List<String> classesList = new ArrayList<String>();
		  boolean isclassavilable=false;
			try {
				IPackageFragment[] packageFragments = jp.getPackageFragments();
				for (IPackageFragment fragment : packageFragments) {
							ICompilationUnit[] compilationUnits = fragment.getCompilationUnits();
							for (ICompilationUnit unit : compilationUnits) {
								IType[] types = unit.getTypes();
								for (IType type : types) {
									if(classname!=null){
										if(classname.equals(type.getFullyQualifiedName())){
											isclassavilable =true;
										}
									} 
									classesList.add(type.getFullyQualifiedName());
								}
							}
					}
			} catch (Exception e) {
				 e.printStackTrace();
	    }
			 if((classname!=null)&&(!isclassavilable)){
				 classesList=null;
			 }	
	return classesList;		
	}*/
	
}

class NullEditorInput implements IEditorInput {

	public boolean exists() {
	return true;
	}

	public ImageDescriptor getImageDescriptor() {
	return ImageDescriptor.getMissingImageDescriptor();
	}

	public String getName() {
	return "Dashboard";
	}

	public IPersistableElement getPersistable() {
	return null;
	}

	public String getToolTipText() {
	return  "rosgi";
	}

	public Object getAdapter(Class adapter) {
	return null;
	}

}

