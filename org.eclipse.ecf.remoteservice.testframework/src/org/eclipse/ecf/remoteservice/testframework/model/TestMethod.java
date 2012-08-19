package org.eclipse.ecf.remoteservice.testframework.model;

import java.lang.reflect.Method;

public class TestMethod {
	
 private boolean isInvoked;
 private Method method;
 private String methodGroup;
 
 public TestMethod(Method method) {
	 this.method = method;
	 isInvoked=false;
}
 
public void setInvoked(boolean isInvoked) {
	this.isInvoked = isInvoked;
}

public boolean isInvoked() {
	return isInvoked;
}

public void setMethod(Method method) {
	this.method = method;
}

public Method getMethod() {
	return method;
}

public void setMethodGroup(String methodGroup) {
	this.methodGroup = methodGroup;
}

public String getMethodGroup() {
	return methodGroup;
}

}
