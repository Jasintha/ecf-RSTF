package org.eclipse.ecf.remoteservice.testframework.annotaions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ResultException {
 
public Class<?> expected();
 
}
