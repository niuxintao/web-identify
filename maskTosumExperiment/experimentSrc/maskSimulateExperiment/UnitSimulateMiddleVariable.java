package maskSimulateExperiment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import newMaskAlgorithms.FIC_MASK_NEWLY;
import newMaskAlgorithms.FIC_MASK_SOVLER;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class UnitSimulateMiddleVariable {

	public final static int IGNORE_FIC = 0;
	public final static int IGNORE_OFOT = 1;
	public final static int IGNORE_CTA = 2;

	public final static int DISTIN_FIC = 3;
	public final static int DISTIN_OFOT = 4;
	public final static int DISTIN_CTA = 5;

	public final static int MASK_FIC = 6;
	public HashMap<Integer, List<Integer>> getReplacingTimes() {
		return replacingTimes;
	}


	public HashMap<Integer, List<Long>> getTimeMillions() {
		return timeMillions;
	}

	public final static int MASK_OFOT = 7;
	public final static int MASK_CTA = 8;

	public final static int NUM = 9;

	public final static int MASK_FIC_OLD = 9;
	public final static int MASK_OFOT_OLD = 10;
	public final static int MASK_CTA_OLD = 11;

	private HashMap<Integer, List<Integer>> replacingTimes;
	private HashMap<Integer, List<Long>> timeMillions;


	public UnitSimulateMiddleVariable() {

		replacingTimes = new HashMap<Integer, List<Integer>>();
//		replacingNumbers = new HashMap<Integer, List<Integer[]>>();
		timeMillions = new HashMap<Integer, List<Long>>();
		// this.bugs = bugs;

		for (int i : new int[] { MASK_FIC, MASK_FIC_OLD }) {
			List<Integer> replacingT = new ArrayList<Integer>();
			List<Long> million = new ArrayList<Long>();
			replacingTimes.put(i, replacingT);
			timeMillions.put(i, million);
		}
	}


	public void testSovler(int[] param, TestCase wrongCase, BasicRunner runner,
			int code) {

		FIC_MASK_SOVLER ficmasknew = new FIC_MASK_SOVLER(wrongCase, param,
				runner, code);
		ficmasknew.FicNOP();

		this.replacingTimes.get(MASK_FIC).addAll(ficmasknew.getTimes());
		this.timeMillions.get(MASK_FIC).addAll(ficmasknew.getTimeMillions());

	}

	public void testAugment(int[] param, TestCase wrongCase,
			BasicRunner runner, int code) {

		FIC_MASK_NEWLY ficmasknew = new FIC_MASK_NEWLY(wrongCase, param,
				runner, code);
		ficmasknew.FicNOP();

		this.replacingTimes.get(MASK_FIC_OLD).addAll(
				ficmasknew.getTimes());
		this.timeMillions.get(MASK_FIC_OLD).addAll(ficmasknew.getTimeMillions());

	}

	public void testScenoria() throws Exception {
		int[] param = new int[] { 3, 3, 3, 3, 3, 3, 3 };

		int[] wrong = new int[] { 1, 1, 1, 1, 1, 1, 1 };

		TestCase wrongCase = new TestCaseImplement();
		wrongCase.setTestState(TestCase.FAILED);
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		int[] wrong2 = new int[] { 2, 2, 2, 2, 2, 2, 2 };

		TestCase wrongCase2 = new TestCaseImplement();
		wrongCase2.setTestState(TestCase.FAILED);
		((TestCaseImplement) wrongCase2).setTestCase(wrong2);

		Tuple bug1 = new Tuple(2, wrongCase);
		bug1.set(0, 0);
		bug1.set(1, 1);

		List<Tuple> bugs1 = new ArrayList<Tuple>();
		bugs1.add(bug1);

		Tuple bug2 = new Tuple(2, wrongCase2);
		bug2.set(0, 3);
		bug2.set(1, 4);

		List<Tuple> bugs2 = new ArrayList<Tuple>();
		bugs2.add(bug2);

		List<Integer> priority1 = new ArrayList<Integer>();
		priority1.add(2);
		List<Integer> priority2 = new ArrayList<Integer>();

		HashMap<Integer, List<Tuple>> bugs = new HashMap<Integer, List<Tuple>>();
		bugs.put(1, bugs1);
		bugs.put(2, bugs2);

		HashMap<Integer, List<Integer>> priority = new HashMap<Integer, List<Integer>>();

		priority.put(1, priority1);
		priority.put(2, priority2);

		BasicRunner basicRunner = new BasicRunner(priority, bugs);

		ExperiementData exData = new ExperiementData();

		exData.setParam(param);
		exData.setHigherPriority(priority);
		exData.setBugs(bugs);

		// this.setBugs(bugs);

		List<Tuple> bench = new ArrayList<Tuple>();
		for (Integer key : bugs.keySet())
			bench.addAll(bugs.get(key));


		int allNum = 0;
		for (Integer code : exData.getWrongCases().keySet()) {
			List<TestCase> wrongCases = exData.getWrongCases().get(code);
			for (TestCase testCase : wrongCases) {

				this.testSovler(param, testCase, basicRunner, code);
				this.testAugment(param, testCase, basicRunner, code);
				// break;
				allNum++;
			}
			// break;

		}

		for (int i : new int[] { MASK_FIC, MASK_FIC_OLD }) {
			System.out.println(i);
		
			if (allNum > 0){
				System.out.println(" average replacing time");
				System.out.println(this.replacingTimes.get(i).size()
						/ (double) allNum);
				
				System.out.println(" average replacing time each time");
				double avRpel = 0;
				for(Integer it : this.replacingTimes.get(i)){
					avRpel += it.intValue();
				}
				System.out.println("all each time: " + avRpel / (double) allNum);
				avRpel /= (double) (this.replacingTimes.get(i).size());
				System.out.println(avRpel);
			}

		}

		for (int i : new int[] { MASK_FIC, MASK_FIC_OLD }) {
			System.out.println(i);
			// for (TestCase testCase : this.additionalTestCases.get(i))

			if (allNum > 0){
				System.out.println(" average millions time");
//				System.out.println(this.replacingTimes.get(i).size()
//						/ (double) allNum);
//			
				double avRpel = 0;
				for(Long it : this.timeMillions.get(i)){
					avRpel += it.longValue();
				}
				avRpel /= (double) (this.timeMillions.get(i).size());
				System.out.println(avRpel);
			}


		}

//		for (int i : new int[] { MASK_FIC, MASK_FIC_OLD }) {
//			System.out.println(i);
//			
//		}

	}

	public static void main(String[] args) throws Exception {
		UnitSimulateMiddleVariable uS = new UnitSimulateMiddleVariable();
		uS.testScenoria();
	}
}
