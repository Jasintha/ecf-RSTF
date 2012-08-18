package org.eclipse.ecf.remoteservice.testframework.annotaion.processer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ecf.remoteservice.testframework.annotaions.Param;
import org.eclipse.ecf.remoteservice.testframework.annotaions.Result;
import org.eclipse.ecf.remoteservice.testframework.annotaions.ResultException;
import org.eclipse.ecf.remoteservice.testframework.annotaions.ServiceHost;
import org.eclipse.ecf.remoteservice.testframework.annotaions.ServiceTest;
import org.eclipse.ecf.remoteservice.testframework.annotaions.Test;
import org.eclipse.ecf.remoteservice.testframework.annotaions.TestInit;

public class AnnotationProcesser {
	
//	private String iService;
//	private String implService;
	//private String[] libraries;
	
	/**
	 * This method use to find-out the test  classes of the project also once test class is found 
	 * set the contract and implementation classes of that test class
	 * @param aClass - class type get using the reflection 
	 * @return - if given class has annotation "ServiceTest" return true else return false
	 * 
	 */
public String getTestClazzImple(Class<?> aClass){
		Annotation annotation = aClass.getAnnotation(ServiceTest.class);
        if(annotation instanceof ServiceTest){
        	ServiceTest  serviceTest = (ServiceTest) annotation;
        	/*this.setiService(serviceTest.IService());
        	this.setImplService(serviceTest.ImplService());
        	this.setLibraries(serviceTest.libraries().split(","));*/
        	Class<?> imple = serviceTest.Imple();
        	if(imple!=null){
        	return imple.getName();
        	}
        }		
		return  null;
	}
  
public String getDefineHost(Class<?> aClass){
	Annotation annotation = aClass.getAnnotation(ServiceHost.class);
    if(annotation instanceof ServiceHost){
    	ServiceHost  serviceTest = (ServiceHost) annotation;
    	return serviceTest.hostId();
    }		
	return  null;
}

public static String isTestMethod(Method method){
	    Annotation annotation = method.getAnnotation(Test.class);
	    if(annotation instanceof Test){
       	  return ((Test)annotation).Methodname();
       }		
		 return  null;
  }

public static boolean isTestInitMethod(Method method){
    Annotation annotation = method.getAnnotation(TestInit.class);
    if(annotation instanceof TestInit){
   	  return true;
   }		
	 return  false;
}
  
public static Object[] getParams(Method method){
	  Annotation[][] parameterAnnotations = method.getParameterAnnotations();
	  Class<?>[] parameterTypes = method.getParameterTypes();
      Object[] paramlist =   new Object[parameterTypes.length];
	  int i=0;
	  for(Annotation[] annotations : parameterAnnotations){
	    Class<?> parameterType = parameterTypes[i];

	    for(Annotation annotation : annotations){
	      if(annotation instanceof Param){
	    	   Param param = (Param) annotation;
	    	  addParam(paramlist, i, param, parameterType);
	    	  i++;
	      }
	    }
	  }
	 return paramlist;
  }
  
public static Map<String,Class<?>> getReturnvalue(Method method){
	 Result annotation =method.getAnnotation(Result.class);
	  Class<?> returnType = method.getReturnType();
	//  Result annotation =returnType.getAnnotation(Result.class);
	  Map<String,Class<?>> map = new HashMap<String,Class<?>>();
	  if(annotation instanceof Result){
	    	  map.put(((Result)annotation).valu(),returnType); 
	   }
	 return map;
  }

public static Class<?> getReturnException(Method method){
	  ResultException annotation =method.getAnnotation(ResultException.class);
	  if(annotation instanceof ResultException){
	    	 return ((ResultException)annotation).expected();
	   }
	 return null;
 }


private static void addParam(Object[] paramlist,int index, Param param, Class<?> boxed) {
		
		if (boxed.equals(java.lang.Boolean.class)||boxed.equals(boolean.class)) {
			    paramlist[index] = new Boolean(param.value());
			
		} else if (boxed.equals(java.lang.Byte.class)||boxed.equals(byte.class)) {
			   paramlist[index] = new Byte(param.value());

		} else if (boxed.equals(java.lang.Character.class)||boxed.equals(char.class)) {
			 paramlist[index] = new Byte(param.value());

		} else if (boxed.equals(java.lang.Double.class)||boxed.equals(double.class)) {
			 paramlist[index] = new Double(param.value());

		} else if (boxed.equals(java.lang.Float.class)||boxed.equals(float.class)) {
			 paramlist[index] = new Float(param.value());

		} else if (boxed.equals(java.lang.Integer.class)||boxed.equals(int.class)) {
			 paramlist[index] = new Integer(param.value());

		} else if (boxed.equals(java.lang.Long.class)||boxed.equals(long.class)) {
			 paramlist[index] = new Long(param.value());

		} else if (boxed.equals(java.lang.Short.class)||boxed.equals(short.class)) {
			 paramlist[index] = new Short(param.value());

		} else if(boxed.equals(java.lang.String.class)){
			paramlist[index] = new String(param.value());
		}
	}
}
