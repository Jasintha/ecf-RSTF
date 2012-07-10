package org.eclipse.ecf.remoteservice.testframework.actions;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ecf.remoteservice.testframework.annotaion.processer.AnnotationProcesser;
import org.eclipse.ecf.remoteservice.testframework.model.AnnotatedTestClass;
import org.eclipse.jdt.core.IClasspathEntry;

/**
 * This is a simple Url class loader  use to load the 
 * Interface and the implementation class 
 * 
 *
 */
public class ServiceClassLoader {

	private  URLClassLoader jdbcJarLoader;
	private  List<AnnotatedTestClass> testClassesList;
	private IClasspathEntry[] rawClasspath;

	public ServiceClassLoader(List<String> classlist, String classpath,
			AnnotationProcesser processer, IClasspathEntry[] rawClasspath)
			throws MalformedURLException, ClassNotFoundException {

		URL[] jarUrl = new URL[] { new URL("file://" + classpath + "/") };
		setJdbcJarLoader(new URLClassLoader(jarUrl, this.getClass()
				.getClassLoader()));
		setTestClassesList(getTestClassesList(getJdbcJarLoader(), classlist,
				processer));
		setRawClasspath(rawClasspath);
		loadJarServiceClasses(jarUrl);

	}

	public Class<?> getclass(String classname) throws ClassNotFoundException{
		return Class.forName(classname,true,getJdbcJarLoader());	
	}
 
	private List<AnnotatedTestClass> getTestClassesList(
			URLClassLoader jdbcJarLoader, List<String> classNameList,
			AnnotationProcesser processer){
		List<AnnotatedTestClass> classList = new ArrayList<AnnotatedTestClass>();
		if (classNameList != null) {
			for (String classname : classNameList) {
				try {
					Class<?> type = Class.forName(classname, true, jdbcJarLoader);
					if (processer.isTestClasses(type)) {
						classList.add(new AnnotatedTestClass(type, processer
								.getiService(), processer.getImplService(),
								processer.getLibraries()));
					}
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
	 * @throws MalformedURLException
	 */
	private void loadJarServiceClasses(URL[] jarUrl) throws MalformedURLException{
 
		 List<URL> urls = new ArrayList<URL>(); 
		 for (AnnotatedTestClass annotatedTestClass : testClassesList) {
			 
			 String[] libraries = annotatedTestClass.getLibraries();
		 
			 for (String lib : libraries) {
				if(lib.contains(".jar")){
					urls.add(getURL(lib));
				}
			}
		}
		 if(urls.size()>0){
			  urls.add(jarUrl[0]);
			  URL[] url = (URL[]) urls.toArray(new URL[0]);
			  setJdbcJarLoader(new URLClassLoader(url,this.getClass().getClassLoader()));
		 }
	}
 
	@SuppressWarnings("deprecation")
	private URL getURL(String path) throws MalformedURLException{
		for (IClasspathEntry iClasspathEntry : rawClasspath) {
			if(iClasspathEntry.getPath().toString().contains(path)){
				File f = new File(iClasspathEntry.getPath().toString());
				return f.toURL();
			}
		}
		return null;
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

	public void setRawClasspath(IClasspathEntry[] rawClasspath) {
		this.rawClasspath = rawClasspath;
	}

	public IClasspathEntry[] getRawClasspath() {
		return rawClasspath;
	}
	
}
