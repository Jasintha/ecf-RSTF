package org.eclipse.ecf.remoteservice.testframework.actions;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ecf.remoteservice.testframework.annotaion.processer.AnnotationProcesser;
import org.eclipse.ecf.remoteservice.testframework.handler.ServiceRegisterHandler;
import org.eclipse.ecf.remoteservice.testframework.model.AnnotatedTestClass;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;

/**
 * This is a simple Url class loader  use to load the 
 * Interface and the implementation class 
 * 
 *
 */
public class ServiceClassLoader {

	private  URLClassLoader jdbcJarLoader;
	private  List<AnnotatedTestClass> testClassesList;
	private IProject[] wsprojects;
    private  List<URL> urls;  
    private List<String>clazzlist;
    private AnnotationProcesser processer;
	
	public ServiceClassLoader(List<String> classlist, String projectClasspath,
			AnnotationProcesser processer, IClasspathEntry[] rawClasspath,IProject[] wsprojects)
			throws MalformedURLException, ClassNotFoundException,Exception {
		    urls = new ArrayList<URL>(); 
		    urls.add(new File(projectClasspath).toURL());
		    this.clazzlist =classlist;
		    this.wsprojects =wsprojects;
		    this.processer =processer;
		setJdbcJarLoader(new URLClassLoader((URL[]) urls.toArray(new URL[0]), this.getClass()
				.getClassLoader()));
		setTestClassesList(getTestClassesList(getJdbcJarLoader(), classlist,
				processer));
		addRawClasspath(rawClasspath);
		loadJarServiceDependancies(0);

	}

	public Class<?> getclass(String classname){
		try{
			return Class.forName(classname,true,getJdbcJarLoader());	
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
 
	private List<AnnotatedTestClass> getTestClassesList(
			URLClassLoader jdbcJarLoader, List<String> classNameList,
			AnnotationProcesser processer){
		List<AnnotatedTestClass> classList = new ArrayList<AnnotatedTestClass>();
		if (classNameList != null) {
			for (String classname : classNameList) {
				try {
					try{
					Class<?> type = Class.forName(classname, true, jdbcJarLoader);
					if (processer.isTestClazz(type)) {
						classList.add(new AnnotatedTestClass(type));
					}else if(processer.isTestSuit(type)){
						ServiceRegisterHandler.setTestSuit(type.getName());
					 }
					}catch(Exception e){
						e.printStackTrace();
					}
					
					/*if (imple!=null) {
						classList.add(new AnnotatedTestClass(type,imple));
					}*/
				} catch (Throwable e) {
					 
					 /*Exception ignore*/
				}
			}
		}
		return classList;
	}
	
	/**
	 * If user given the service or imple classes as jars this methods will add those
	 * jars into class loader
	 * @param jarUrl - current class loader 
	 * @throws Exception 
	 * @throws MalformedURLException
	 */
	private void loadJarServiceDependancies(int count) throws Exception {

		try {

	      	for (AnnotatedTestClass annotatedTestClass : testClassesList) {

		
				annotatedTestClass.loadServiceImple(getJdbcJarLoader());
				String ServiceName  =annotatedTestClass.getImplService();
				URLClassLoader jdbcJarLoader2 = getJdbcJarLoader();
				Class<?> clazz = Class.forName(ServiceName, true,jdbcJarLoader2);
				Class<?>[] interfaces = clazz.getInterfaces();
				if (interfaces != null) {
					annotatedTestClass.setiService(interfaces[0].getName());
					getclass(annotatedTestClass.getiService());
				}
	      	}

			} catch (ClassNotFoundException e) {
				if (count < 0) {
					if (findClazz(e.getMessage())) {
						loadJarServiceDependancies(count++);
					}
				}
				throw e;
			} catch (NoClassDefFoundError e) {
				if (count < 3) {
					if (findClazz(e.getCause().getMessage())) {
						loadJarServiceDependancies(count++);
					}
				}
				throw e;
			}catch(TypeNotPresentException e){
				if (count < 3) {
					String message = e.getCause().getCause().getMessage();
				 
					if (findClazz(message)) {
						loadJarServiceDependancies(count++);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
	}	
	
	private boolean findClazz(String clazz)  {
		try{
		 for (IProject pr : wsprojects) {
			 if(pr.isOpen()){
				 List<String> classes = ServiceClassLoader.getClasses(pr,clazz);
				if(classes!=null){
					IJavaProject jp = JavaCore.create(pr);
					IPath outputLocation = jp.getOutputLocation();
					String projectClasspath = ResourcesPlugin.getWorkspace().getRoot()
							.getFolder(outputLocation).getLocation().toFile()
							.toString();
					File nf = new File(projectClasspath);
					urls.add(nf.toURL());
					setJdbcJarLoader(new URLClassLoader((URL[]) urls.toArray(new URL[0]), this.getClass()
							.getClassLoader()));
					setTestClassesList(getTestClassesList(getJdbcJarLoader(), clazzlist,
							processer));
					return true;
	                 	}
		     	 }
		 }	
	    	}catch(Exception e){
			/*Exception Ignored*/
		}
		 
		return false;
	} 
	
 
	/*@SuppressWarnings("deprecation")
	private URL getURL(String path) throws MalformedURLException{
		for (IClasspathEntry iClasspathEntry : rawClasspath) {
			
			if(iClasspathEntry.getPath().toString().contains(path)){
				File f = new File(iClasspathEntry.getPath().toString());
				return f.toURL();
			}
		}
		return null;
	}*/

	public static List<String> getClasses(IProject project,String classname){
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
	}

	
	
	public void setJdbcJarLoader(URLClassLoader jdbcJarLoader) {
		this.jdbcJarLoader = jdbcJarLoader;
	}

	public URLClassLoader getJdbcJarLoader() {
		return jdbcJarLoader;
	}

	public void setTestClassesList(List<AnnotatedTestClass> testClassesList) {
		this.testClassesList = testClassesList;
	}

	public List<AnnotatedTestClass> getTestClassesList() {
		return testClassesList;
	}

	public void addRawClasspath(IClasspathEntry[] rawClasspath) {
		if(rawClasspath!=null){
			
			for (IClasspathEntry iClasspathEntry : rawClasspath) {
				
				try{
					File f = new File(iClasspathEntry.getPath().toString());
	 				urls.add(f.toURL());
			 	} catch (Throwable e) {
					 
					 /*Exception ignore*/
				}
				
			}
			setJdbcJarLoader(new URLClassLoader((URL[]) urls.toArray(new URL[0]), this.getClass()
					.getClassLoader()));
		}
	}
}
