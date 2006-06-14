package org.apache.bcel;

import org.apache.bcel.classfile.DescendingVisitor;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.visitors.CounterVisitor;

public abstract class AbstractCounterVisitorTestCase extends AbstractTestCase
{
	protected abstract JavaClass getTestClass() throws ClassNotFoundException;

	private CounterVisitor visitor = null;

	public void setUp() throws ClassNotFoundException
	{
		visitor = new CounterVisitor();
		new DescendingVisitor(getTestClass(), getVisitor()).visit();
	}

	public CounterVisitor getVisitor()
	{
		if (visitor == null)
			visitor = new CounterVisitor();
		return visitor;
	}

	public void setVisitor(CounterVisitor visitor)
	{
		this.visitor = visitor;
	}
}
