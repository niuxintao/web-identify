package com.fc.testObject;

import com.fc.tuple.Tuple;

/**
 * the interface identify the test case
 * 
 * @author Xintao Niu
 *
 */
public interface TestCase {
	public final static int UNTESTED = 0;
	public final static int PASSED = 1;
	public final static int FAILED = -1;
	public int getAt(int index);
	public void set(int index, int value);
	public int testDescription();
	public void setTestState(int state);
	public int getLength();
	public String getStringOfTest();
	public boolean containsOf(Tuple tuple);
	public TestCase copy();
}
