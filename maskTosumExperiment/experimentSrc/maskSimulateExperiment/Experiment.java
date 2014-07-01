package maskSimulateExperiment;

import java.util.ArrayList;
import java.util.List;

import maskTool.EvaluateTuples;

import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

public class Experiment {
	private ExpriSetUp setup;

	public Experiment() {
		setup = new ExpriSetUp();
	}

	public void test(int index) {
		DataRecord record = setup.getRecords().get(index);
		setup.set(record.param, record.wrongs, record.bugs, record.faults,
				record.priority);
		ExperiementData exData = new ExperiementData();

		BasicRunner basicRunner = new BasicRunner(setup.getPriorityList(),
				setup.getBugsList());

		exData.setParam(setup.getParam());
		exData.setHigherPriority(setup.getPriorityList());
		exData.setBugs(setup.getBugsList());

		UnitSimulate unit = new UnitSimulate();

		int allNum = 0;
		for (Integer code : exData.getWrongCases().keySet()) {
			List<TestCase> wrongCases = exData.getWrongCases().get(code);
			for (TestCase testCase : wrongCases) {
				// System.out.println("testCase: " +
				// testCase.getStringOfTest());
				// System.out.println("distinguish");
				unit.testTraditional(setup.getParam(), testCase,
						new DistinguishRunner(basicRunner, code));
				// System.out.println("ignore");
				unit.testTraditional(setup.getParam(), testCase,
						new IgnoreRunner(basicRunner));

				// System.out.println("mask");
				unit.testSovler(setup.getParam(), testCase, basicRunner, code);
				// break;
				allNum++;
			}
			// break;

		}

		for (int i : new int[] { 0, 3, 6 }) {
			System.out.println(i);
			// for (TestCase testCase : this.additionalTestCases.get(i))
			if (allNum > 0)
				System.out.println(unit.getAdditionalTestCases().get(i).size()
						/ (double) allNum);

		}

		List<Tuple> bench = new ArrayList<Tuple>();
		for (Integer key : unit.getTuples().keySet())
			bench.addAll(unit.getTuples().get(key));

		// for(Tuple tuple : bench)
		// System.out.println(tuple.toString());

		for (int i : new int[] { 0, 3, 6 }) {
			System.out.println(i);
			List<Tuple> tuples = unit.getTuples().get(i);
			EvaluateTuples eva = new EvaluateTuples();

			// for(Tuple tuple : tuples){
			// System.out.println("degree : " + tuple.getDegree());
			// System.out.println(tuple.toString());
			// }

			eva.evaluate(bench, tuples);
			System.out.println(eva.getMetric());
		}
	}

	public static void main(String[] args) {
		Experiment ex = new Experiment();
		ex.test(0);
		ex.test(1);
		ex.test(2);
		ex.test(3);
		ex.test(4);
	}
}
