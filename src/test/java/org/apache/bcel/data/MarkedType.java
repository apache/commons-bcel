package org.apache.bcel.data;

@MarkerAnnotationInvisible
@MarkerAnnotation
@SimpleAnnotation(id = 1)
public abstract class MarkedType
{
	@MarkerAnnotationInvisible
	@MarkerAnnotation
	class InnerClass
	{
	}

	@MarkerAnnotationInvisible
	@MarkerAnnotation
	int annotatedField;

	@Deprecated
	void deprecatedMthod()
	{
	}

	native void nativeMthod();

	abstract void abstractMethod();

	@MarkerAnnotationInvisible
	@MarkerAnnotation
	void annotatedMethod()
	{
	}

	void annotatedParamentrMethod(@MarkerAnnotationInvisible
	@MarkerAnnotation
	int i)
	{
	}

	void constantedMethod()
	{
		int i1 = 1;
		int i2 = 200000;
		long l1 = 1;
		long l2 = 200000;
		float f = 0.1F;
		String s = "";
	}
}
