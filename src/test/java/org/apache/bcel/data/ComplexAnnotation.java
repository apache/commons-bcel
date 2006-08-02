package org.apache.bcel.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ComplexAnnotation
{
	int ival();

	byte bval();

	char cval();

	long jval();

	double dval();

	boolean zval();

	short sval();

	float fval();
}