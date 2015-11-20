package maskSimulateExperiment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.inference.TTest;

import maskTool.EvaluateTuples;

import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

public class RandomAndILPExperiment {
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

	public static final int millions = 13;
	public static final int replaceTime = 14; // 每次 调用 替换， 需要的个数
	public static final int replace = 15; // 需要替换 的 数目

	// private double[] data_ilp;

	public static final String[] stringofmetric = { "avgmetric", "avgaccuate",
			"avgparent", "avgchild", "avgignore", "avgirrelevant", "metric",
			"accuate", "parent", "child", "ignore", "irrelevant", "testNum",
			"time millions", "needing replacement number",
			"trial test cases for each replacement" };

	private double[][] data;

	public RandomAndILPExperiment() {
		setup = new ExpriSetUp();
		data = new double[2][16];
	}

	public void test(int index, int algorithm) {
		int di = (algorithm == UnitSimulate.MASK_FIC ? 0 : 1);
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

		UnitSimulateMiddleVariable unit = new UnitSimulateMiddleVariable();

		List<Tuple> bench = new ArrayList<Tuple>();
		for (Integer key : setup.getBugsList().keySet()) {
			bench.addAll(setup.getBugsList().get(key));
			// System.out.println(key);
			// for(Tuple tuple : setup.getBugsList().get(key)){
			// System.out.println(tuple.toString());
			// }
		}

		unit.setBugs(setup.getBugsList());

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
				if (di == 0)
					unit.testSovler(setup.getParam(), testCase, basicRunner,
							code);
				else
					unit.testAugment(setup.getParam(), testCase, basicRunner,
							code);
				// break;
				allNum++;
				// break;
			}
			// break;

		}

		// System.out.println("test Cases");
		for (int i : new int[] { algorithm }) {
			// System.out.println(i);
			// for (TestCase testCase : this.additionalTestCases.get(i))
			if (allNum > 0)
				data[di][testNum] = unit.getAdditionalTestCases().get(i).size()
						/ (double) allNum;

			// System.out.println();

		}
		// System.out.println("replace time");
		for (int i : new int[] { algorithm }) {
			// System.out.println(i);

			// for (TestCase testCase : this.additionalTestCases.get(i))
			if (allNum > 0) {
				data[di][replace] = unit.getReplacingTimes().get(i).size()
						/ (double) allNum;

				double avRpel = 0;
				for (Integer it : unit.getReplacingTimes().get(i)) {
					avRpel += it.intValue();
				}
				avRpel /= (double) (unit.getReplacingTimes().get(i).size());
				data[di][replaceTime] = avRpel;

				double avRpel2 = 0;
				for (Long it : unit.getTimeMillions().get(i)) {
					avRpel2 += it.longValue();
				}
				avRpel2 /= (double) (unit.getTimeMillions().get(i).size());
				data[di][millions] = avRpel2;

			}

		}

		// System.out.println("evaluates -- avg");
		for (int i : new int[] { algorithm }) {

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
				data[di][avgmetric] = metric / (double) allNum;
				data[di][avgaccuate] = accuarte / (double) allNum;
				data[di][avgchild] = child / (double) allNum;
				data[di][avgparent] = parent / (double) allNum;
				data[di][avgirrlevant] = irrelevant / (double) allNum;
				data[di][avgignore] = ignore / (double) allNum;
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
		for (int i : new int[] { algorithm }) {
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
			data[di][metric] = eva.getMetric();
			data[di][accuate] = eva.getAccurateTuples().size();
			data[di][child] = eva.getChildTuples().size();
			data[di][parent] = eva.getFatherTuples().size();
			data[di][irrlevant] = eva.getRedundantTuples().size();
			data[di][ignore] = eva.getMissTuples().size();
		}
	}

	
	// p value 小于0.05 就拒绝 相等， 意味着 差异显著
	public static void showresult(int metric, RandomAndILPExperiment ex,
			double[][] datas) {

		TTest ttest = new TTest();
		Mean mean = new Mean();

		RealMatrix matrix = new Array2DRowRealMatrix(datas);

		System.out.print("ILP " + stringofmetric[metric] + " : ");
		System.out.println(ex.data[0][metric] + " ");

		System.out.print("Random " + stringofmetric[metric] + " : ");
		for (int j = 0; j < 30; j++) {
			System.out.print(datas[j][metric] + " ");
		}

		System.out.println();
		System.out.println("avg ：" + mean.evaluate(matrix.getColumn(metric)));
		System.out.println("t-test, p-value : "
				+ ttest.tTest(ex.data[0][metric], matrix.getColumn(metric)));

	}
	
	
	public void conductTest(int start, int end){
		for (int i = start; i < end; i++){
			System.out.println();
			System.out.println("the " + i + " th");

			RandomAndILPExperiment ex = new RandomAndILPExperiment();
			ex.test(i, 6);

			double[][] datas = new double[30][];
			for (int j = 0; j < 30; j++) {
				// RandomExperiment ex = new RandomExperiment();
				ex.test(i, 9);
				datas[j] = ex.data[1];
			}

			showresult(testNum, ex, datas);
			showresult(replace, ex, datas);
			showresult(replaceTime, ex, datas);
			showresult(avgmetric, ex, datas);
			showresult(avgparent, ex, datas);
			showresult(avgchild, ex, datas);
			showresult(avgignore, ex, datas);
			showresult(avgirrlevant, ex, datas);
			showresult(avgaccuate, ex, datas);
		}
	}

	public static void main(String[] args) {

		for (int i = 14; i < 15; i++) {
			System.out.println();
			System.out.println("the " + i + " th");

			RandomAndILPExperiment ex = new RandomAndILPExperiment();
			ex.test(i, 6);

			double[][] datas = new double[30][];
			for (int j = 0; j < 30; j++) {
				// RandomExperiment ex = new RandomExperiment();
				ex.test(i, 9);
				datas[j] = ex.data[1];
			}

			showresult(testNum, ex, datas);
			showresult(replace, ex, datas);
			showresult(replaceTime, ex, datas);
			showresult(avgmetric, ex, datas);
			showresult(avgparent, ex, datas);
			showresult(avgchild, ex, datas);
			showresult(avgignore, ex, datas);
			showresult(avgirrlevant, ex, datas);
			showresult(avgaccuate, ex, datas);
		}
		// ex.test(0);
		// ex.test(2);
		// // ex.test(3);
		// ex.test(4);
	}
}
