package org.eclipse.ecf.remoteservice.testframework.handler;

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
import org.eclipse.ecf.remoteservice.testframework.actions.TestConsole;
import org.eclipse.ecf.remoteservice.testframework.annotaion.processer.AnnotationProcesser;
import org.eclipse.ecf.remoteservice.testframework.model.AnnotatedTestClass;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.console.MessageConsoleStream;
 

public class ServiceRegisterHandler implements IActionDelegate {
	
	private List<String> classNameList;
	private String projectClasspath;
	private MessageConsoleStream userConsole;
	private IClasspathEntry[] rawClasspath;

	@Override
	public void run(IAction arg0) {
		try {
			final Object lock = new Object();
			loadTestConsole();
			AnnotationProcesser processer = new AnnotationProcesser();
			ServiceClassLoader classLoader = new ServiceClassLoader(
					classNameList, projectClasspath, processer,rawClasspath);
			List<AnnotatedTestClass> testClassesList = classLoader
					.getTestClassesList();
			if(testClassesList.size()>0){
			for (AnnotatedTestClass testClass : testClassesList) {

				ServiceActivator service = startService(lock, classLoader,
						testClass);

				ServiceConsumer serviceConsumer = serviceConsumer(lock,
						classLoader, testClass, service,
						processer.getDefineHost(testClass.getType()));

				testClass.test(serviceConsumer.getRemoteService().getProxy(),
						userConsole);

			}
		  	   userConsole.println("\n\n Test completed Successfully !!");
			}else{
				userConsole.println("Test Classes NOT found ! \n\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void loadTestConsole() {
		TestConsole report = new TestConsole();
	    userConsole = report.getOut();
	    try{
	    	 userConsole.print("****************");
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	   
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
			ServiceActivator service,String hostId) throws ClassNotFoundException,
			InterruptedException {
		 ServiceConsumer consumer = new  ServiceConsumer();
		 consumer.setInterfaceName(classLoader.getclass(testClass.getiService()).getName());
		 if(hostId!=null){
			 consumer.setServiceURL(hostId);
		 }else{
			 consumer.setServiceURL(service.getserviceHost());
		 }
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
			ClassNotFoundException, InterruptedException {
	     ServiceActivator service = new ServiceActivator();
		 service.setClassInstance(classLoader.getclass(testClass.getImplService()).newInstance());
		 service.setInterfaceName(classLoader.getclass(testClass.getiService()).getName());
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
			 IProject project = resource.getProject();
			 project.open(new NullProgressMonitor());
			 project.build(IncrementalProjectBuilder.FULL_BUILD, new NullProgressMonitor());
			 project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
 
			 classNameList =this.getClasses(project);
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
	
	private List<String> getClasses(IProject project){
		IJavaProject jp = JavaCore.create(project);
		  List<String> classesList = new ArrayList<String>();
			try {
				IPackageFragment[] packageFragments = jp.getPackageFragments();
				for (IPackageFragment fragment : packageFragments) {
							ICompilationUnit[] compilationUnits = fragment.getCompilationUnits();
							for (ICompilationUnit unit : compilationUnits) {
								IType[] types = unit.getTypes();
								for (IType type : types) {
									classesList.add(type.getFullyQualifiedName());
								}
							}
					}
			} catch (Exception e) {
				 e.printStackTrace();
	    }
	return classesList;		
	}
	
}
