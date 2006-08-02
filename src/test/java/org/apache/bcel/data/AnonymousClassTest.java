package org.apache.bcel.data;

public class AnonymousClassTest
{
	public void foo()
	{
		new Runnable()
		{
			public void run()
			{
			};
		}.run();
	}

	class X
	{
	}

	static class Y
	{
	}
}