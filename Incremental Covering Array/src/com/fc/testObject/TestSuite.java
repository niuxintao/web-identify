package com.fc.testObject;


public interface TestSuite {
	public void addTest(TestCase test);
	public void deleteTest(int index);
	public TestCase getAt(int index);
	public void setOneTestCaseState(int index , int value);
	public int getTestCaseNum();
	public TestSuite getInfoTestCases(int state);
	public void clear();
}
