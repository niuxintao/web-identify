package com.fc.caseRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fc.testObject.TestCase;
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
	public int runTestCase(TestCase testCase) {
		// TODO Auto-generated method stub
		Iterator<Tuple> itr = bugModel.iterator();
		while(itr.hasNext()){
			if(testCase.containsOf(itr.next()))
				return TestCase.FAILED;
		}
		return TestCase.PASSED;
	}

}
