package caseStudy;

import java.util.ArrayList;
import java.util.List;

import maskSimulateExperiment.BasicRunnerInMask;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;

public class FIC_MASK_NEWLY {
	protected BasicRunnerInMask caseRunner;
	protected TestCase testCase;
	protected int[] param;
	protected List<Tuple> bugs;
	protected TestSuite extraCases;
	protected TestTupleSuspicious testTuple;
	protected int[] otherFaults;

	private int level;

	class Pa {
		public List<Integer> CFree;
		public Tuple fixdOne;
	}

	/**
	 * @return the bugs
	 */
	public List<Tuple> getBugs() {
		return bugs;
	}

	public FIC_MASK_NEWLY(TestCase testCase, int[] param,
			BasicRunnerInMask caseRunner, int level, int[] otherFaults) {
		this.testCase = testCase;
		this.level = level;
		this.param = param;
		bugs = new ArrayList<Tuple>();
		this.caseRunner = caseRunner;
		extraCases = new TestSuiteImplement();
		testTuple = new TestTupleSuspicious(caseRunner, param, testCase, level);
		this.otherFaults = otherFaults;
	}

	public List<TestCase> getExecuted() {
		return this.testTuple.getExecuted();
	}

	public Pa LocateFixedParam(List<Integer> CFree, Tuple partBug) {

		// convert the List to a int[]
		int[] free = CovertTntegerToInt(CFree);

		// get a Candidate index
		Tuple freeTuple = new Tuple(free.length, testCase);

		freeTuple.setParamIndex(free);
		Tuple noCand = freeTuple.catComm(freeTuple, partBug);

		Tuple cand = noCand.getReverseTuple();
		int[] Ccand = cand.getParamIndex();
		List<Integer> U = new ArrayList<Integer>();
		U.addAll(CFree);

		for (int i = 0; i < Ccand.length; i++) {
			Integer para = new Integer(Ccand[i]);
			List<Integer> temp = new ArrayList<Integer>();
			temp.addAll(U);
			// accroding to sequnce

			if (temp.size() == 0) {
				temp.add(para);
			} else
				for (int j = temp.size() - 1; j >= 0; j--) {
					if (temp.get(j) < para) {
						temp.add(j + 1, para);
						break;
					} else if (temp.get(j) == para) {
						break;
					} else if (j == 0) {
						temp.add(j, para);
						break;
					}
				}
			if (testTuple(temp)) {
				Tuple fixone = new Tuple(1, testCase);
				fixone.setParamIndex(new int[] { para });
				Pa pa = new Pa();
				pa.CFree = U;
				pa.fixdOne = fixone;
				return pa;
			}
			U = temp;
		}
		Tuple fixone = new Tuple(0, testCase);
		Pa pa = new Pa();
		pa.CFree = U;
		pa.fixdOne = fixone;
		return pa;
	}

	private boolean testTuple(List<Integer> temp) {
		Tuple tuple = new Tuple(temp.size(), testCase);
		int location = 0;
		for (int i : temp) {
			tuple.set(location, i);
			location++;
		}
		tuple = tuple.getReverseTuple();

		boolean result = testTuple.testTuple(tuple, level, testCase,
				otherFaults);

		// System.out.println(tuple.toString()+": "+result);

		return result;
	}

	public int[] CovertTntegerToInt(List<Integer> CFree) {
		int[] free = new int[CFree.size()];
		for (int i = 0; i < free.length; i++) {
			free[i] = CFree.get(i);
		}
		return free;
	}

	public List<Integer> CovertTntToTnteger(int[] CFree) {
		List<Integer> free = new ArrayList<Integer>();
		for (int i = 0; i < CFree.length; i++) {
			free.add(new Integer(CFree[i]));
		}
		return free;
	}

	public Pa LocateFixeedParamBS(List<Integer> CFree, Tuple partBug) {
		// convert the List to a int[]
		int[] free = CovertTntegerToInt(CFree);

		// get a Candidate index
		Tuple freeTuple = new Tuple(free.length, testCase);

		freeTuple.setParamIndex(free);
		Tuple noCand = freeTuple.catComm(freeTuple, partBug);

		Tuple cand = noCand.getReverseTuple();
		int[] Ccand = cand.getParamIndex();
		List<Integer> U = new ArrayList<Integer>();
		U.addAll(CFree);

		Tuple determine = cand.catComm(cand, freeTuple);
		if (Ccand == null
				|| Ccand.length == 0
				|| (partBug.getDegree() > 0 && !this
						.testTuple(CovertTntToTnteger(determine.getParamIndex())))) {
			Tuple fixone = new Tuple(0, testCase);
			Pa pa = new Pa();
			pa.CFree = U;
			pa.fixdOne = fixone;
			return pa;
		}

		int low = 0;
		int high = Ccand.length - 1;
		int middle = (low + high) / 2;
		List<Integer> candList = this.CovertTntToTnteger(Ccand);
		while (low < high) {
			int[] lower = this.CovertTntegerToInt(candList.subList(low,
					middle + 1));
			Tuple Low = new Tuple(lower.length, testCase);
			Low.setParamIndex(lower);

			Tuple Temp = freeTuple.catComm(freeTuple, Low);
			if (this.testTuple(this.CovertTntToTnteger(Temp.getParamIndex()))) {
				high = middle;
				middle = (low + high) / 2;
			} else {
				low = middle + 1;
				middle = (low + high) / 2;
				freeTuple = Temp;
			}
		}
		Tuple fixone = new Tuple(1, testCase);
		fixone.set(0, new Integer(candList.get(low)));
		Pa pa = new Pa();
		pa.CFree = this.CovertTntToTnteger(freeTuple.getParamIndex());
		pa.fixdOne = fixone;
		return pa;
	}

	public Tuple Fic(List<Integer> CTabu) {
		Tuple partBug = new Tuple(0, testCase);
		List<Integer> CFree = new ArrayList<Integer>();
		CFree.addAll(CTabu);
		while (true) {
			Pa pa = this.LocateFixeedParamBS(CFree, partBug);
			CFree = pa.CFree;
			Tuple newRelatedPartBug = pa.fixdOne;
			if (newRelatedPartBug.getDegree() == 0) {
				break;
			}
			partBug = partBug.catComm(partBug, newRelatedPartBug);
		}
		return partBug;
	}

	public void FicNOP() {
		List<Integer> CTabu = new ArrayList<Integer>();
		while (true) {
			if (CTabu.size() > 0 && testTuple(CTabu)) {
				break;
			}

			Tuple bug = Fic(CTabu);
			if (bug.getDegree() == 0)
				break;
			this.bugs.add(bug);

			Tuple tuple = new Tuple(CTabu.size(), testCase);
			int[] tabu = CovertTntegerToInt(CTabu);
			tuple.setParamIndex(tabu);

			Tuple newCTabu = tuple.catComm(tuple, bug);
			CTabu.clear();
			CTabu.addAll(CovertTntToTnteger(newCTabu.getParamIndex()));
		}
	}

	/**
	 * @return the extraCases
	 */
	public TestSuite getExtraCases() {
		return extraCases;
	}

	public static void main(String[] args) {
		int[] wrong = new int[] { 1, 1, 1, 1, 1, 1, 1, 1 };
		TestCase wrongCase = new TestCaseImplement();
		wrongCase.setTestState(TestCase.FAILED);
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		int[] param = new int[] { 3, 3, 3, 3, 3, 3, 3, 3 };

		Tuple bugModel = new Tuple(8, wrongCase);
		bugModel.set(0, 0);
		bugModel.set(1, 1);
		bugModel.set(2, 2);
		bugModel.set(3, 3);
		bugModel.set(4, 4);
		bugModel.set(5, 5);
		bugModel.set(6, 6);
		bugModel.set(7, 7);
		// bugModel.set(1, 4);

		// Tuple bugModel2 = new Tuple(1, wrongCase);
		// bugModel2.set(0, 3);
		//
		// Tuple bugModel3 = new Tuple(1, wrongCase);
		// bugModel3.set(0, 6);

		BasicRunnerInMask caseRunner = new BasicRunnerInMask();
		caseRunner.inject(bugModel, 1);
		// ((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);
		// ((CaseRunnerWithBugInject) caseRunner).inject(bugModel3);

		int level = 1;

		FIC_MASK_NEWLY fic = new FIC_MASK_NEWLY(wrongCase, param, caseRunner,
				level, new int[] { 2 });
		fic.FicNOP();

		for (int i = 0; i < fic.getExtraCases().getTestCaseNum(); i++) {
			System.out.println(fic.getExtraCases().getAt(i).getStringOfTest());
		}
		for (Tuple bug : fic.getBugs()) {
			// if (bug.equals(bugModel1) || bug.equals(bugModel2)) {
			// System.out.println("true");
			// }
			System.out.println(bug.toString());
		}
	}
}
