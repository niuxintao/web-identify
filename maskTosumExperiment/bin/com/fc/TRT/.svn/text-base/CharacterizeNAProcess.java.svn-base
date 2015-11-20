package com.fc.TRT;

import java.util.ArrayList;
import java.util.List;


import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;

public class CharacterizeNAProcess {
	protected CaseRunner caseRunner;
	protected CorpTupleWithTestCase generate;
	protected TreeStruct tree;
	protected TestSuite additionalSuite;
	protected List<Tuple> bugs;
	
	public void testWorkFlow(TestCase wrongCase, List<Tuple> bugs, int[] param, TestSuite rightSuite){
		caseRunner = new CaseRunnerWithBugInject();
		for (Tuple bug : bugs)
			((CaseRunnerWithBugInject) caseRunner).inject(bug);

		tree = new TreeStruct(wrongCase, rightSuite);
		tree.constructTree();
		tree.init();

		generate = new CorpTupleWithTestCase(wrongCase, param);
		
		PathNA workMachine = new PathNA(tree,caseRunner,generate);	
		workMachine.process();	
		this.bugs = tree.getBugModes();
		additionalSuite = workMachine.getExtraCases();
		
	}
	
	public TestSuite getAdditionalSuite() {
		return additionalSuite;
	}

	public List<Tuple> getBugs() {
		return bugs;
	}

	public static void main(String[] args){
		CharacterizeNAProcess na = new CharacterizeNAProcess();
        int[] wrong = new int[] { 1, 1, 1, 1, 1, 1, 1, 1,1,1,1,1,1,1,1};
		
		int[] wrong2 = new int[] { 2, 2, 2, 2, 2, 2,2, 2,2,2,2,2,2,2,2};
		
		int[] pass = new int[] { 0, 0, 0, 0, 0, 0, 0, 0,0,0,0,0,0,0,0};
		TestCase rightCase = new TestCaseImplement();
		((TestCaseImplement) rightCase).setTestCase(pass);
		TestCase wrongCase = new TestCaseImplement();
		((TestCaseImplement) wrongCase).setTestCase(wrong);
		
		TestCase wrongCase2 = new TestCaseImplement();
		((TestCaseImplement) wrongCase2).setTestCase(wrong2);

		TestSuite rightSuite = new TestSuiteImplement();
		rightSuite.addTest(rightCase);
	

		int[] param = new int[] { 3, 3, 3, 3,3,3,3,3,3,3,3,3,3,3,3};
		
		
		TestSuite wrongSuite = new TestSuiteImplement();
		wrongSuite.addTest(wrongCase);
		
		Tuple bugModel = new Tuple(3, wrongCase);
		bugModel.set(0, 1);
		bugModel.set(1, 2);
		bugModel.set(2, 4);
		
		Tuple bugModel2 = new Tuple(3, wrongCase2);
		bugModel2.set(0, 12);
		bugModel2.set(1, 13);
		bugModel2.set(2, 14);
		

		CaseRunner caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel);
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);
		List<Tuple> bugs = new ArrayList<Tuple>();
		bugs.add(bugModel);
		bugs.add(bugModel2);

		na.testWorkFlow(wrongCase,bugs,param,rightSuite);
		
		for(Tuple bug : na.bugs)
			System.out.println(bug.toString());
		
		System.out.println("all:" + na.additionalSuite.getTestCaseNum());
		for (int i = 0; i < na.additionalSuite.getTestCaseNum(); i++) {
			System.out.println(na.additionalSuite.getAt(i).getStringOfTest());
		}
	}
}
