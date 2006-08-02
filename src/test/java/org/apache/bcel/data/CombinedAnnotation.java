package org.apache.bcel.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CombinedAnnotation
{
	public SimpleAnnotation[] value();
}