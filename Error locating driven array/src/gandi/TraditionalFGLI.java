package gandi;

import java.util.HashSet;

import com.fc.caseRunner.CaseRunner;
import com.fc.coveringArray.DataCenter;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

import ct.AETG;
import ct.SOFOT;

public class TraditionalFGLI {

	private CaseRunner caseRunner;
	
	private HashSet<TestCase> overallTestCases;
	
	private HashSet<Tuple> MFS; 
	

	
	public TraditionalFGLI(CaseRunner caseRunner){
		this.caseRunner = caseRunner;
		overallTestCases = new HashSet<TestCase>();
	}
	
	public void run(){
		//generate covering array
		AETG aetg = new AETG();
		aetg.process();
		for(int[] test : aetg.coveringArray){
			TestCase testCase = new TestCaseImplement(test);
			overallTestCases.add(testCase);
		}
		
		//idenitfy
		HashSet<TestCase> additional = new HashSet<TestCase> ();
		for(TestCase testCase : overallTestCases){
			if(caseRunner.runTestCase(testCase) == TestCase.FAILED){
				SOFOT ofot = new SOFOT();
				ofot.process(testCase, DataCenter.param, caseRunner);
				additional.addAll(ofot.getExecuted());
				MFS.addAll(ofot.getBugs());
//				MFS.add(ofot.)
				
			}
		}
		
		
		//merge them 
		overallTestCases.addAll(additional);
	}
}
