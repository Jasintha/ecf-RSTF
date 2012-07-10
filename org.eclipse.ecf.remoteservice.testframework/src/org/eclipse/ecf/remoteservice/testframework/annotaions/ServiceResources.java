package org.eclipse.ecf.remoteservice.testframework.annotaions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)		
/**
 * libraries get the external libraries which need to run the test
 */
public @interface ServiceResources {

	public String[] libraries (); 
}
