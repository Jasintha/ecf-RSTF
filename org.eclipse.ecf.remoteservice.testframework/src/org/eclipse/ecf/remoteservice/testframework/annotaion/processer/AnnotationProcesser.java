package org.eclipse.ecf.remoteservice.testframework.annotaion.processer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ecf.remoteservice.testframework.annotaions.AfterMethod;
import org.eclipse.ecf.remoteservice.testframework.annotaions.AfterSuite;
import org.eclipse.ecf.remoteservice.testframework.annotaions.BeforeMethod;
import org.eclipse.ecf.remoteservice.testframework.annotaions.BeforeSuite;
import org.eclipse.ecf.remoteservice.testframework.annotaions.ServiceTest;
import org.eclipse.ecf.remoteservice.testframework.annotaions.Test;
import org.eclipse.ecf.remoteservice.testframework.annotaions.TestEnd;
import org.eclipse.ecf.remoteservice.testframework.annotaions.TestInit;
import org.eclipse.ecf.remoteservice.testframework.annotaions.TestSuite;
import org.eclipse.ecf.remoteservice.testframework.model.AnnotatedTestClass;

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
public boolean isTestClazz(Class<?> aClass){
		Annotation annotation = aClass.getAnnotation(ServiceTest.class);
        if(annotation instanceof ServiceTest){
           return true;
        }		
		return  false;
	}

public boolean isTestSuit(Class<?> aClass){
	Annotation annotation = aClass.getAnnotation(TestSuite.class);
    if(annotation instanceof TestSuite){
       return true;
    }		
	return  false;
}

public static boolean isTestMethod(Method method){
	    Annotation annotation = method.getAnnotation(Test.class);
	    if(annotation instanceof Test){
       	  return true;
       }		
		 return  false;
  }

public static String dependTestMethod(Method method){
    Annotation annotation = method.getAnnotation(Test.class);
    if(annotation instanceof Test){
   	  Test test = (Test)annotation;
   	  return test.depend();
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

public static boolean isTestBeforeMethod(Method method){
    Annotation annotation = method.getAnnotation(BeforeMethod.class);
    if(annotation instanceof BeforeMethod){
   	  return true;
   }		
	 return  false;
}

public static boolean isTestAfterMethod(Method method){
    Annotation annotation = method.getAnnotation(AfterMethod.class);
    if(annotation instanceof AfterMethod){
   	  return true;
   }		
	 return  false;
}

public static boolean isTestEndMethod(Method method){
    Annotation annotation = method.getAnnotation(TestEnd.class);
    if(annotation instanceof TestEnd){
   	  return true;
   }		
	 return  false;
}

public static boolean isTestSuitBefore(Method method){
    Annotation annotation = method.getAnnotation(BeforeSuite.class);
    if(annotation instanceof BeforeSuite){
   	  return true;
   }		
	 return  false;
}

public static boolean isTestSuitAftert(Method method){
    Annotation annotation = method.getAnnotation(AfterSuite.class);
    if(annotation instanceof AfterSuite){
   	  return true;
   }		
	 return  false;
}

  




}
