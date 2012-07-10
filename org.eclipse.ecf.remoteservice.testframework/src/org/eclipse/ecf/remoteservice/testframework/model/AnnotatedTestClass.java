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
	
	public AnnotatedTestClass(Class<?> type,String iService,String implService,String[] libraries) {
		 this.iService = iService;
		 this.implService = implService;
		 this.testClass = type;
		 this.setLibraries(libraries);
	}
 
	/**
	 * Start the testing
	 * @param proxy - this the remote service object
	 */
	public void test(Object proxy, MessageConsoleStream userConsole) {

		userConsole.getConsole().clearConsole();
		userConsole.println(Message.TEST_BEGIN + testClass.getSimpleName());
		Method[] methods = testClass.getMethods();
		for (Method method : methods) {
			String methodname = AnnotationProcesser.isTestMethod(method);
			if (methodname != null) {
				userConsole.println(Message.TEST_SEPERATOR);
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
								}

							} else {
								if (returnType.isAssignableFrom(invoke
										.getClass())) {
									userConsole.println(Message.TEST_PASS
											+ methodname);
								}
							}

						} else {
							userConsole.println(Message.TEST_FAIL + methodname
									+ " : duto " + "Return value miss match");
						}
					} else {
						/* method not expecting any return value or exception */
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

	public void setiService(String iService) {
		this.iService = iService;
	}

	public String getiService() {
		return iService;
	}

	public void setImplService(String implService) {
		this.implService = implService;
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
