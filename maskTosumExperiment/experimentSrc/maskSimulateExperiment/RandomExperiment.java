package maskSimulateExperiment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import maskTool.EvaluateTuples;

import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

public class RandomExperiment {
	private ExpriSetUp setup;

	public static final int avgmetric = 0;
	public static final int avgaccuate = 1;
	public static final int avgparent = 2;
	public static final int avgchild = 3;
	public static final int avgignore = 4;
	public static final int avgirrlevant = 5;
	public static final int metric = 6;
	public static final int accuate = 7;
	public static final int parent = 8;
	public static final int child = 9;
	public static final int ignore = 10;
	public static final int irrlevant = 11;
	public static final int testNum = 12;

	private double[] data;

	public RandomExperiment() {
		setup = new ExpriSetUp();
		data = new double[13];
	}

	public void test(int index) {
		// System.out.println("the " + index + "th");
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
		for (Integer key : setup.getBugsList().keySet()) {
			bench.addAll(setup.getBugsList().get(key));
			// System.out.println(key);
			// for(Tuple tuple : setup.getBugsList().get(key)){
			// System.out.println(tuple.toString());
			// }
		}

		unit.setBugs(bench);

		// for (Tuple tuple : bench)
		// System.out.print(tuple.toString() + " ");
		// System.out.println();

		int allNum = 0;
		for (Integer code : exData.getWrongCases().keySet()) {
			List<TestCase> wrongCases = exData.getWrongCases().get(code);
			for (TestCase testCase : wrongCases) {
				// System.out.println("testCase: " +
				// testCase.getStringOfTest());
				// System.out.println("distinguish");
				// System.out.println("mask");
				unit.testAugment(setup.getParam(), testCase, basicRunner, code);
				// break;
				allNum++;
				// break;
			}
			// break;

		}

		// System.out.println("test Cases");
		for (int i : new int[] { 9 }) {
			// System.out.println(i);
			// for (TestCase testCase : this.additionalTestCases.get(i))
			if (allNum > 0)
				data[testNum] = unit.getAdditionalTestCases().get(i).size()
						/ (double) allNum;

			// System.out.println();

		}

		// System.out.println("evaluates -- avg");
		for (int i : new int[] { 9 }) {

			// System.out.println(i);
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
				data[avgmetric] = metric / (double) allNum;
				data[avgaccuate] = accuarte / (double) allNum;
				data[avgchild] = child / (double) allNum;
				data[avgparent] = parent / (double) allNum;
				data[avgirrlevant] = irrelevant / (double) allNum;
				data[avgignore] = ignore / (double) allNum;
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

		// System.out.println("evaluates -- all");
		for (int i : new int[] { 9 }) {
			// System.out.println(i);
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
			data[metric] = eva.getMetric();
			data[accuate] = eva.getAccurateTuples().size();
			data[child] = eva.getChildTuples().size();
			data[parent] = eva.getFatherTuples().size();
			data[irrlevant] = eva.getRedundantTuples().size();
			data[ignore] = eva.getMissTuples().size();
		}
	}

	public static void main(String[] args) {
		for (int i = 0; i < 15; i++) {
			System.out.println();
			System.out.println("the " + i + " th");
			double[][] datas = new double[30][];
			for (int j = 0; j < 30; j++) {
				RandomExperiment ex = new RandomExperiment();
				ex.test(i);
				datas[j] = ex.data;
			}
			double avg = 0;
			System.out.print("testNum :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][testNum] + " ");
				avg += datas[j][testNum];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("avgmetric :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][avgmetric] + " ");
				avg += datas[j][avgmetric];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("avgaccuate :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][avgaccuate] + " ");
				avg += datas[j][avgaccuate];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("avgparent :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][avgparent] + " ");
				avg += datas[j][avgparent];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("avgchild :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][avgchild] + " ");
				avg += datas[j][avgchild];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("avgignore :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][avgignore] + " ");
				avg += datas[j][avgignore];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("avgirrlevant :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][avgirrlevant] + " ");
				avg += datas[j][avgirrlevant];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("metric :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][metric] + " ");
				avg += datas[j][metric];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("accuate :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][accuate] + " ");
				avg += datas[j][accuate];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("parent :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][parent] + " ");
				avg += datas[j][parent];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("avgchild :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][child] + " ");
				avg += datas[j][child];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("avgignore :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][ignore] + " ");
				avg += datas[j][ignore];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("avgirrlevant :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][irrlevant] + " ");
				avg += datas[j][irrlevant];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
		}
		// ex.test(0);
		// ex.test(2);
		// // ex.test(3);
		// ex.test(4);
	}
}
