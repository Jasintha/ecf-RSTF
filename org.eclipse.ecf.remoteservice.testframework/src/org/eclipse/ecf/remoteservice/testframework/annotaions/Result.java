package org.eclipse.ecf.remoteservice.testframework.annotaions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Result {
	public String valu();
}
