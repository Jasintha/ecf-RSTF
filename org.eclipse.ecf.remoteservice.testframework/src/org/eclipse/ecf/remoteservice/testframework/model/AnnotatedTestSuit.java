package org.eclipse.ecf.remoteservice.testframework.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.ecf.remoteservice.testframework.annotaion.processer.AnnotationProcesser;

public class AnnotatedTestSuit {

	public void start(Class<?> clz) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			InstantiationException {
		Method[] methods = clz.getMethods();
		Object testClazzInstance = null;
		if (methods != null) {
			for (Method method : methods) {
				if (AnnotationProcesser.isTestSuitBefore(method)) {
					testClazzInstance = clz.newInstance();
					method.invoke(testClazzInstance, null);
					break;
				}
			}
		}
	}
	
	public void finish(Class<?> clz) throws IllegalArgumentException,
	IllegalAccessException, InvocationTargetException,
	InstantiationException {
Method[] methods = clz.getMethods();
Object testClazzInstance = null;
if (methods != null) {
	for (Method method : methods) {
		if (AnnotationProcesser.isTestSuitAftert(method)) {
			testClazzInstance = clz.newInstance();
			method.invoke(testClazzInstance, null);
			break;
		}
	}
}
}
	
}
