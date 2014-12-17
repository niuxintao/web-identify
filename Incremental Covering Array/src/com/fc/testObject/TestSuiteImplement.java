package com.fc.testObject;

import java.util.ArrayList;
import java.util.List;

public class TestSuiteImplement implements TestSuite {

	private List<TestCase> testSuite;
	public TestSuiteImplement (){
		testSuite = new ArrayList<TestCase>();
	}

	@Override
	public void addTest(TestCase test) {
		// TODO Auto-generated method stub
		testSuite.add(test);
	}

	@Override
	public void deleteTest(int index) {
		// TODO Auto-generated method stub
		testSuite.remove(index);
	}

	@Override
	public TestCase getAt(int index) {
		// TODO Auto-generated method stub
		return testSuite.get(index);
	}

	@Override
	public void setOneTestCaseState(int index, int value) {
		// TODO Auto-generated method stub
		testSuite.get(index).setTestState(value);
	}

	@Override
	public int getTestCaseNum() {
		// TODO Auto-generated method stub
		return testSuite.size();
	}

	@Override
	public TestSuite getInfoTestCases(int state) {
		// TODO Auto-generated method stub
		TestSuite rs = new TestSuiteImplement();
		for(int i = 0;i < this.getTestCaseNum();i++ ){
			if(this.getAt(i).testDescription() == state){
				rs.addTest(this.getAt(i));
			}
		}
 		return rs;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		this.testSuite.clear();
	}

}
