package com.fc.model;

import java.util.ArrayList;
import java.util.List;

import com.fc.caseRunner.CaseRunner;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestSuite;
import com.fc.tuple.CorpTupleWithTestCase;
import com.fc.tuple.Tuple;

public class CharacterAugFeedBcak {
	public final static int VALIDATETIMES = 3;
	List<Tuple> bugs;
	List<Tuple> rights;
	List<TestCase> testCases;

	public CharacterAugFeedBcak() {
		bugs = new ArrayList<Tuple>();
		rights = new ArrayList<Tuple>();
		testCases = new ArrayList<TestCase>();
	}

	public void process(ChainAug workMachine, CaseRunner caseRunner,
			CorpTupleWithTestCase generate) {
		// character
		while (true) {
			workMachine.reset();
			character(workMachine, caseRunner);
			// validate
			List<Tuple> bugs = workMachine.getPool().getExistedBugTuples();
			boolean flag = true;
			for (Tuple bug : bugs) {
				// System.out.println("validate");
				int validate = validateFaultyTuple(bug, workMachine, generate,
						caseRunner);
				// if not right, keep all the right, deal and character again.
				if (validate == TestCase.PASSED) {
					// workMachine.getPool().getExistedBugTuples().remove(bug);
					workMachine.getPool().getExistedRightTuples().add(bug);
					flag = false;
				}
			}
			if (flag) {
				// System.out.println("break");
				break;
			}

			// add these bug
			workMachine.getPool().getExistedBugTuples().clear();
			workMachine.getPool().getExistedBugTuples()
					.add(workMachine.getPool().getRoot());
			for (int i = 0; i < generate.getTuples().size(); i++) {

				TestSuite suite = generate.getGeneratedTestCases().get(i);
				if (suite.getTestCaseNum() == VALIDATETIMES
						&& suite.getAt(VALIDATETIMES - 1).testDescription() == TestCase.FAILED)
					workMachine.getPool().getExistedBugTuples()
							.add(generate.getTuples().get(i));
			}
			generate.addIter();
		}

		bugs.addAll(workMachine.getPool().getExistedBugTuples());
		rights.addAll(workMachine.getPool().getExistedRightTuples());
		for (int i = 0; i < workMachine.getExtraCases().getTestCaseNum(); i++)
			testCases.add(workMachine.getExtraCases().getAt(i));

		// System.out.println("run import begin");
		runImportBugs(workMachine, generate, caseRunner);
		// System.out.println("run import end");

	}

	public void character(ChainAug workMachine, CaseRunner caseRunner) {
		while (true) {
			TestCase testcase = workMachine.genNextTest();
			if (testcase == null)
				break;
			// System.out.println(testcase.getStringOfTest());
			workMachine
					.setLastTestCase(caseRunner.runTestCase(testcase) == TestCase.PASSED);
		}
	}

	public int validtedTime(Tuple tuple, CorpTupleWithTestCase generate) {
		int index = generate.isTupleTested(tuple);
		if (index == -1)
			return 0;
		TestSuite suite = generate.getGeneratedTestCases().get(index);
		return suite.getTestCaseNum();
	}

	public int validateFaultyTuple(Tuple tuple, ChainAug workMachine,
			CorpTupleWithTestCase generate, CaseRunner caseRunner) {
		int result = TestCase.FAILED;
		for (int i = validtedTime(tuple, generate); i < VALIDATETIMES; i++) {
			TestCase testCase = workMachine.generateTestCaseContainTuple(tuple);
			result = caseRunner.runTestCase(testCase);
			if (result == TestCase.PASSED)
				break;
		}
		return result;
	}

	public boolean isContainBugs(TestCase testCase) {
		for (Tuple tuple : bugs) {
			if (testCase.containsOf(tuple))
				return true;
		}
		return false;
	}

	public void runImportBugs(ChainAug workMachineOld,
			CorpTupleWithTestCase generate, CaseRunner caseRunner) {
		/**
		 * for generate right tuple , but run many times which are failed
		 */
		List<TestSuite> suites = generate.getGeneratedTestCases();
		for (TestSuite suite : suites) {
			for (int i = 0; i < suite.getTestCaseNum(); i++) {
				TestCase wrongCase = suite.getAt(i);
				if (wrongCase.testDescription() == TestCase.FAILED
						&& !isContainBugs(wrongCase)) {
					TuplePool pool = new TuplePool(wrongCase, workMachineOld
							.getPool().getRightSuite());
					for (Tuple tuple : rights) {
						if (wrongCase.containsOf(tuple))
							pool.getExistedRightTuples().add(tuple);
					}
					CorpTupleWithTestCase generateNew = new CorpTupleWithTestCase(
							wrongCase, generate.getParam());
					generateNew.setCurrentBugs(bugs);
					ChainAug workMachine = new ChainAug(pool, generateNew);
					process(workMachine, caseRunner, generateNew);
				}
			}
		}
		/*
		 * for (TestSuite suite : suites) { if
		 * (suite.getAt(suite.getTestCaseNum() - 1).testDescription() ==
		 * TestCase.PASSED) { for (int i = 0; i < suite.getTestCaseNum() - 1;
		 * i++) { TestCase wrongCase = suite.getAt(i); if
		 * (!isContainBugs(wrongCase)) { System.out.println("import case: " +
		 * wrongCase.getStringOfTest()); TuplePool pool = new
		 * TuplePool(wrongCase, workMachineOld.getPool().getRightSuite()); for
		 * (Tuple tuple : rights) { if (wrongCase.containsOf(tuple))
		 * pool.getExistedRightTuples().add(tuple); } CorpTupleWithTestCase
		 * generateNew = new CorpTupleWithTestCase( wrongCase,
		 * generate.getParam()); generateNew.setCurrentBugs(bugs); ChainAug
		 * workMachine = new ChainAug(pool, generateNew); process(workMachine,
		 * caseRunner, generateNew); } } } }
		 */
	}

	public List<Tuple> getBugs() {
		return bugs;
	}

	public List<TestCase> getTestCases() {
		return testCases;
	}
}
