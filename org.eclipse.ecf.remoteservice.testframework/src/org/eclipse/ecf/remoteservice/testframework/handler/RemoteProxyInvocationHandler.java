package org.eclipse.ecf.remoteservice.testframework.handler;

public class RemoteProxyInvocationHandler {

	 private static Object proxy;

	 public static void setServiceProxy(Object proxy) {
		 RemoteProxyInvocationHandler.proxy = proxy;
	 }

	 public static<T> T getServiceProxy(Class<T> type) {
	 	return type.cast(proxy);
	 } 
}