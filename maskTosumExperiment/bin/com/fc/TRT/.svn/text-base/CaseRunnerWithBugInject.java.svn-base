package com.fc.TRT;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestSuite;
import com.fc.tuple.Tuple;

public class CaseRunnerWithBugInject implements CaseRunner {
	private List<Tuple> bugModel;

	public CaseRunnerWithBugInject(List<Tuple> bugModel) {
		this.bugModel = bugModel;
	}
	
	public CaseRunnerWithBugInject(){
		bugModel = new ArrayList<Tuple>();
	}
	
	public void inject(Tuple bug){
		this.bugModel.add(bug);
	}
	
	public void setBugMode(List<Tuple> bugModel){
	   this.bugModel = bugModel;	
	}

	@Override
	public void runTestCase(TestCase testCase) {
		// TODO Auto-generated method stub
		Iterator<Tuple> itr = bugModel.iterator();
		while(itr.hasNext()){
			if(testCase.containsOf(itr.next()))
				testCase.setTestState(TestCase.FAILED);
		}
		if(testCase.testDescription() == TestCase.UNTESTED){
			testCase.setTestState(TestCase.PASSED);
		}
	}

	@Override
	public void runTestSuite(TestSuite suite) {
		// TODO Auto-generated method stub
		for(int i = 0; i < suite.getTestCaseNum(); i++){
			this.runTestCase(suite.getAt(i));
		}
	}

	@Override
	public TestSuite getRunnedTestSuite() {
		// TODO Auto-generated method stub
		return null;
	}

}
