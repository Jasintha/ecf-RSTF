package org.eclipse.ecf.remoteservice.testframework.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.eclipse.ecf.remoteservice.testframework.annotaion.processer.AnnotationProcesser;
import org.eclipse.ecf.remoteservice.testframework.annotaions.ServiceTest;
import org.eclipse.ecf.remoteservice.testframework.utils.Message;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * This a dynamic representation  of the test class
 * @author jasintha
 *
 */
public class AnnotatedTestClass {
	
	private String iService;
	private String implService;
	private Class<?> testClass;
	private String[] libraries;
	private MessageConsoleStream userConsole;
	private Method before;
	private Method after;
	private Method end;
	private List<TestMethod> methodsList;
 
	
	public AnnotatedTestClass(Class<?> type) {
		 this.testClass = type;
	}
	
	public void loadServiceImple(URLClassLoader jdbcJarLoader)throws Exception{
		Class<?> loadClazz = jdbcJarLoader.loadClass(testClass.getName());
		Annotation annotation = loadClazz.getAnnotation(ServiceTest.class);
	    if(annotation instanceof ServiceTest){
	    	ServiceTest  serviceTest = (ServiceTest) annotation;
	    	Class<?> imple = serviceTest.Imple();
	    	  if(imple!=null){
	     	     setImplService(imple.getName());
	    	 }
	    }	
	}
 
	public void Start(URLClassLoader jdbcJarLoader) throws Exception{
		
		Class<?> loadClazz = jdbcJarLoader.loadClass(testClass.getName());
		userConsole.println(Message.TEST_BEGIN + testClass.getSimpleName());
		System.out.println(Message.TEST_BEGIN);
		Method[] methods = loadClazz.getMethods();
		
		Object testClazzInstance=null;
	    if(methods!=null){
	    	boolean isInit = false;
	    	methodsList = new ArrayList<TestMethod>();
	    	for (Method method : methods) {
				if(AnnotationProcesser.isTestInitMethod(method)&&(!isInit)){
					testClazzInstance=loadClazz.newInstance();
					method.invoke(testClazzInstance,null);
					isInit = true;
				}else if(AnnotationProcesser.isTestBeforeMethod(method)&&(before==null)){
					this.before =method;
				}else if(AnnotationProcesser.isTestAfterMethod(method)&&(after==null)){
					this.after = method;
				}else if(AnnotationProcesser.isTestEndMethod(method)&&(after==null)){
					this.end = method;
				}else if(AnnotationProcesser.isTestMethod(method)){
				  methodsList.add(new TestMethod(method));
				}
			}
	    	if(isInit){
	    		this.test(testClazzInstance);
	    	}
	    }else{
	    	userConsole.println("Coudn't find any methods in the "+testClass.getSimpleName());
	    }
	}
	/**
	 * Start the testing
	 * @param proxy - this the remote service object
	 * @throws InstantiationException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public void test(Object clazzInstance) throws Exception {
		for (TestMethod testMethod : methodsList) {
			 if(!testMethod.isInvoked()){
			 invokeMethod(clazzInstance, testMethod.getMethod());
			 }
		}
		if(end!=null){end.invoke(clazzInstance, null);}
	}

	private void invokeMethod(Object clazzInstance, Method method) {
		String methodname=null;
		if (AnnotationProcesser.isTestMethod(method)) {
			try{
				String dependOn = AnnotationProcesser.dependTestMethod(method);
				if ((dependOn!=null)&&(!dependOn.equals("?method"))){
					Method depenMethod = findMethod(dependOn);
					if(depenMethod!=null){
						invokeMethod(clazzInstance, depenMethod);
					}
				}
			userConsole.println(Message.TEST_SEPERATOR);
			methodname = method.getName();
			userConsole.println("Testing Method : "+methodname);
			if(before!=null){before.invoke(clazzInstance,null);}
			method.invoke(clazzInstance,null);
			if(after!=null){after.invoke(clazzInstance,null);}
			userConsole.println(Message.TEST_SEPERATOR);
			}catch (SecurityException e) {
				userConsole.println(Message.TEST_FAIL + methodname
						+ " : duto " + e.getMessage());
				e.printStackTrace();
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				userConsole.println(Message.TEST_FAIL + methodname
						+ " : duto " + e.getMessage());
				e.printStackTrace();
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				userConsole.println(Message.TEST_FAIL + methodname
						+ " : duto " + e.getMessage());
				e.printStackTrace();
				e.printStackTrace();
			} catch (InvocationTargetException e) {
 
					userConsole.println(Message.TEST_FAIL + methodname
							+ " : duto " + e.getMessage() + " : "
							+ ExceptionUtils.getRootCause(e).getMessage());
					e.printStackTrace();
			}
		}
	}

	
	public Method findMethod(String methodName){
		for (TestMethod testMethod : methodsList) {
			   Method temp = testMethod.getMethod();
			  if(methodName.equals(testMethod.getMethod().getName())){
				  testMethod.setInvoked(true);
				  return temp;
			  }
		}
	    return null;
	}

	public void setiService(String iService) {
		this.iService = iService;
	}

	public String getiService() {
		return iService;
	}

	public void setImplService(String implService) {
		this.implService = implService;
	}

	public void setUserConsole(MessageConsoleStream userConsole) {
		this.userConsole = userConsole;
	}

	public MessageConsoleStream getUserConsole() {
		return userConsole;
	}

	public String getImplService() {
		return implService;
	}

	public void setType(Class<?> type) {
		this.testClass = type;
	}

	public Class<?> getType() {
		return testClass;
	}

	public void setLibraries(String[] libraries) {
		this.libraries = libraries;
	}

	public String[] getLibraries() {
		return libraries;
	}

	private boolean checktype(Object invoke, Class<?> boxed) {

		if (boxed.equals(boolean.class)) {
			return invoke instanceof Boolean;

		} else if (boxed.equals(byte.class)) {
			return invoke instanceof Byte;

		} else if (boxed.equals(char.class)) {
			return invoke instanceof Character;

		} else if (boxed.equals(double.class)) {
			return invoke instanceof Double;

		} else if (boxed.equals(float.class)) {
			return invoke instanceof Float;

		} else if (boxed.equals(int.class)) {
			return invoke instanceof Integer;

		} else if (boxed.equals(long.class)) {
			return invoke instanceof Long;

		} else if (boxed.equals(short.class)) {
			return invoke instanceof Short;
		}

		return false;
	}

}
