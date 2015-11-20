package maskSimulateExperiment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import maskTool.EvaluateTuples;

import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

public class TraditionalExperiment {
	private ExpriSetUp setup;

	public TraditionalExperiment() {
		setup = new ExpriSetUp();
	}

	public void test(int index) {
		System.out.println("the " + index + "th");
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

		List<Tuple> bench = new ArrayList<Tuple>();
		for (Integer key : setup.getBugsList().keySet())
			bench.addAll(setup.getBugsList().get(key));

		unit.setBugs(setup.getBugsList());

		int allNum = 0;
		for (Integer code : exData.getWrongCases().keySet()) {
			List<TestCase> wrongCases = exData.getWrongCases().get(code);
			for (TestCase testCase : wrongCases) {
				// System.out.println("testCase: " +
				// testCase.getStringOfTest());
				// System.out.println("distinguish");
				unit.testTraditional(setup.getParam(), testCase,
						new DistinguishRunner(basicRunner, code), code);
				// System.out.println("ignore");
				unit.testTraditional(setup.getParam(), testCase,
						new IgnoreRunner(basicRunner), code);

				// System.out.println("mask");
				// unit.testSovler(setup.getParam(), testCase, basicRunner,
				// code);
				// break;
				allNum++;
			}
			// break;

		}

		System.out.println("test Cases");
		for (int i : new int[] { 0, 3 }) {
			System.out.println(UnitSimulate.names[i]);
			// for (TestCase testCase : this.additionalTestCases.get(i))
			if (allNum > 0)
				System.out.println(unit.getAdditionalTestCases().get(i).size()
						/ (double) allNum);

		}

		// for (Tuple tuple : bench)
		// System.out.print(tuple.toString() + " ");
		// System.out.println();

		System.out.println("evaluates -- avg");
		for (int i : new int[] { 0, 3 }) {

			System.out.println(UnitSimulate.names[i]);
			// for (TestCase testCase : this.additionalTestCases.get(i))
			double metric = 0;
			double ignore = 0;
			double parent = 0;
			double child = 0;
			double irrelevant = 0;
			double accuarte = 0;
			for (EvaluateTuples eva : unit.getEvaluates().get(i)) {
				metric += eva.getMetric();
				ignore += eva.getMissTuples().size();
				accuarte += eva.getAccurateTuples().size();
				parent += eva.getFatherTuples().size();
				child += eva.getChildTuples().size();
				irrelevant += eva.getRedundantTuples().size();

			}
			if (allNum > 0) {
				System.out.println("metric: " + metric / (double) allNum);
				System.out.println("accuarte: " + accuarte / (double) allNum);
				System.out.println("child: " + child / (double) allNum);
				System.out.println("parent: " + parent / (double) allNum);
				System.out.println("irrelevant: " + irrelevant
						/ (double) allNum);
				System.out.println("ignore: " + ignore / (double) allNum);
			}

			// System.out.println(i);
			// HashSet<Tuple> tupl = unit.getTuples().get(i);
			// List<Tuple> tuples = new ArrayList<Tuple>();
			// tuples.addAll(tupl);
			//
			// // for (Tuple tuple : tuples)
			// // System.out.print(tuple.toString() + " ");
			// // System.out.println();
			// EvaluateTuples eva = new EvaluateTuples();
			//
			// // for(Tuple tuple : tuples){
			// // System.out.println("degree : " + tuple.getDegree());
			// // System.out.println(tuple.toString());
			// // }
			//
			// eva.evaluate(bench, tuples);
			// System.out.println(eva.getMetric());
			// System.out.println("accuate " + eva.getAccurateTuples().size());
			// System.out.println("child " + eva.getChildTuples().size());
			// System.out.println("parent " + eva.getFatherTuples().size());
			// System.out.println("reduantant " +
			// eva.getRedundantTuples().size());
			// System.out.println("ignore " + eva.getMissTuples().size());

		}

		System.out.println("evaluates -- all");
		for (int i : new int[] { 0, 3 }) {
			System.out.println(UnitSimulate.names[i]);
			HashSet<Tuple> tupl = unit.getTuples().get(i);
			List<Tuple> tuples = new ArrayList<Tuple>();
			tuples.addAll(tupl);

			// for (Tuple tuple : tuples)
			// System.out.print(tuple.toString() + " ");
			// System.out.println();
			EvaluateTuples eva = new EvaluateTuples();

			// for(Tuple tuple : tuples){
			// System.out.println("degree : " + tuple.getDegree());
			// System.out.println(tuple.toString());
			// }

			eva.evaluate(bench, tuples);
			System.out.println(eva.getMetric());
			System.out.println("accuate " + eva.getAccurateTuples().size());
			System.out.println("child " + eva.getChildTuples().size());
			System.out.println("parent " + eva.getFatherTuples().size());
			System.out.println("irrelevant " + eva.getRedundantTuples().size());
			System.out.println("ignore " + eva.getMissTuples().size());

		}
	}

	public void conductTest(int start, int end) {
		TraditionalExperiment ex = new TraditionalExperiment();
		for (int i = start; i < end; i++)
			ex.test(i);
	}

	public static void main(String[] args) {
		TraditionalExperiment ex = new TraditionalExperiment();
		for (int i = 4; i < 15; i++)
			ex.test(i);
		// ex.test(1);
		// ex.test(2);
		// // ex.test(3);
		// ex.test(4);
	}
}
