package org.eclipse.ecf.remoteservice.testframework.annotaions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)		

public @interface ServiceTest {
	/*public String IService();//service Interface Name, or path
	public String ImplService();//Service Imple name or classs 
	public String libraries();*/
	
	public Class<?> Imple();
}
