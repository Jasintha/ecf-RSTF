package org.eclipse.ecf.remoteservice.testframework.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.eclipse.ecf.remoteservice.testframework.annotaion.processer.AnnotationProcesser;
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
	
	public AnnotatedTestClass(Class<?> type,String impleClazz) {
  
		 this.implService = impleClazz;
		 this.testClass = type;
	}
 
	public void Start() throws Exception{
		userConsole.println(Message.TEST_BEGIN + testClass.getSimpleName());
		System.out.println(Message.TEST_BEGIN);
		Method[] methods = testClass.getMethods();
		Object testClazzInstance=null;
	    if(methods!=null){
	    	boolean isInit = false;
	    	for (Method method : methods) {
				if(AnnotationProcesser.isTestInitMethod(method)){
					testClazzInstance=testClass.newInstance();
					method.invoke(testClazzInstance,null);
					isInit = true;
					break;
				}
			}
	    	if(isInit){
	    		this.test(methods,testClazzInstance);
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
	public void test(Method[] methods,Object clazzInstance) throws Exception {
		
		for (Method method : methods) {	 
			String methodname = AnnotationProcesser.isTestMethod(method);
			if (methodname != null) {
				try{
				userConsole.println(Message.TEST_SEPERATOR);
				userConsole.println("Testing Method : "+methodname);
				method.invoke(clazzInstance,null);//Test Methods do not take any params 
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
	}

	
/*	public void test(Object proxy, MessageConsoleStream userConsole) {

		userConsole.println(Message.TEST_BEGIN + testClass.getSimpleName());
		System.out.println(Message.TEST_BEGIN);
		Method[] methods = testClass.getMethods();
		for (Method method : methods) {	 
			String methodname = AnnotationProcesser.isTestMethod(method);
			if (methodname != null) {
				userConsole.println(Message.TEST_SEPERATOR);
				System.out.println(Message.TEST_SEPERATOR);
				Object[] params = AnnotationProcesser.getParams(method);
				Map<String, Class<?>> returnvalue = AnnotationProcesser
						.getReturnvalue(method);

				try {
					Method serviceMetjod = proxy.getClass().getMethod(
							methodname, method.getParameterTypes());
					Object invoke = serviceMetjod.invoke(proxy, params);
					if (returnvalue.size() > 0) {
						Class<?> returnType = returnvalue
								.get(invoke.toString());

						if (returnType != null) {

							if (returnType.isPrimitive()) {

								if (checktype(invoke, returnType)) {
									userConsole.println(Message.TEST_PASS
											+ methodname);
									System.out.println(Message.TEST_PASS
											+ methodname);
								}

							} else {
								if (returnType.isAssignableFrom(invoke
										.getClass())) {
									userConsole.println(Message.TEST_PASS
											+ methodname);
									System.out.println(Message.TEST_PASS
											+ methodname);
								}
							}

						} else {
							userConsole.println(Message.TEST_FAIL + methodname
									+ " : duto " + "Return value miss match");
						}
					} else {
						 method not expecting any return value or exception 
						userConsole.println(Message.TEST_PASS + methodname);
					}

				} catch (SecurityException e) {
					userConsole.println(Message.TEST_FAIL + methodname
							+ " : duto " + e.getMessage());
					e.printStackTrace();
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
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
					Class<?> returnException = AnnotationProcesser
							.getReturnException(method);
					if (returnException.isAssignableFrom(ExceptionUtils
							.getRootCause(e).getClass())) {
						userConsole.println(Message.TEST_PASS + methodname);
					} else {
						userConsole.println(Message.TEST_FAIL + methodname
								+ " : duto " + e.getMessage() + " : "
								+ e.getTargetException().getMessage());
						e.printStackTrace();
					}
				}
				userConsole.println(Message.TEST_SEPERATOR);
			}
		}
	}
*/

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
