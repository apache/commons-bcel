package org.apache.bcel.data;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface SimpleAnnotation
{
	int id();

	String fruit() default "bananas";
}
