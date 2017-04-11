package com.fc.caseRunner;

import java.util.ArrayList;
//import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

public class CaseRunnerWithBugInjectLikely implements CaseRunner {
	private List<Tuple> bugModel;
	private List<Double> likely;
	private Random randomGenerator = new Random();

	public CaseRunnerWithBugInjectLikely(List<Tuple> bugModel, List<Double> likely) {
		this.bugModel = bugModel;
		this.likely = likely;
	}
	
	public CaseRunnerWithBugInjectLikely(){
		bugModel = new ArrayList<Tuple>();
		likely = new ArrayList<Double>();
	}
	
	public void inject(Tuple bug, double likely){
		this.bugModel.add(bug);
		this.likely.add(likely);
	}
	
	public void setBugMode(List<Tuple> bugModel, List<Double> likely){
	   this.bugModel = bugModel;	
	   this.likely = likely;
	}

	@Override
	public int runTestCase(TestCase testCase) {
		// TODO Auto-generated method stub
//		double p = Math.pow(Math.E, -(unCovered - oldUncovered) / this.T);
		double exa = randomGenerator.nextDouble();
//		int result = TestCase.PASSED;
	
		for(int i = 0; i < bugModel.size(); i++){
			Tuple t = bugModel.get(i);
			double like = likely.get(i);
			if(exa < like){ // triggers fault.  
				if(testCase.containsOf(t))
					return TestCase.FAILED;
			}
		}
//		while(itr.hasNext()){
//			if(testCase.containsOf(itr.next()))
//				return TestCase.FAILED;
//		}
		return TestCase.PASSED;
	}

}
