package experiementFor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.inference.TTest;

import maskSimulateExperiment.BasicRunner;
import maskSimulateExperiment.DataRecord;
import maskSimulateExperiment.DistinguishRunner;
import maskSimulateExperiment.ExperiementData;
import maskSimulateExperiment.ExpriSetUp;
import maskSimulateExperiment.IgnoreRunner;
import maskSimulateExperiment.UnitSimulate;
import maskSimulateExperiment.UnitSimulateMiddleVariable;
import maskTool.EvaluateTuples;

import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

public class RandomAndTraditionalExperiment {
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
			"accuate", "parent", "child", "ignore", "irrelevant", "test Num",
			"time millions", "replace_hap", "replace_numb" };

	private double[][] data;

	public RandomAndTraditionalExperiment() {
		setup = new ExpriSetUp();
		data = new double[3][16];
	}

	public void test(int index, int algorithm) {
		int di = 0;
		if (algorithm == UnitSimulate.MASK_FIC_OLD)
			di = 0;
		else if (algorithm == UnitSimulate.DISTIN_FIC)
			di = 1;
		else if (algorithm == UnitSimulate.IGNORE_FIC)
			di = 2;
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
//				 System.out.println("testCase: " +
//				 testCase.getStringOfTest() + "error code : " + code );
				// System.out.println("distinguish");
				// System.out.println("mask");
				if (algorithm == UnitSimulate.MASK_FIC_OLD)
					unit.testAugment(setup.getParam(), testCase, basicRunner,
							code);
				else if (algorithm == UnitSimulate.DISTIN_FIC)
					unit.testTraditional(setup.getParam(), testCase,
							new DistinguishRunner(basicRunner, code), code);
				else if (algorithm == UnitSimulate.IGNORE_FIC)
					unit.testTraditional(setup.getParam(), testCase,
							new IgnoreRunner(basicRunner), code);
				// break;
				allNum++;
//				break;
			}
//			break;

		}

		// System.out.println("test Cases");
		for (int i : new int[] { algorithm }) {
			// System.out.println(i);
			// for (TestCase testCase : this.additionalTestCases.get(i))
			if (allNum > 0)
				data[di][testNum] = unit.getAdditionalTestCases().get(i).size()
						/ (double) allNum;

			// if (i == algorithm)
			// System.out.println(unit.getAdditionalTestCases().get(i).size()
			// / (double) allNum);

		}
		// System.out.println("replace time");
		if (unit.getReplacingTimes().get(algorithm) != null)
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
	public static void storeResult(DATA result_temp, int metric,
			RandomAndTraditionalExperiment ex, double[][] datas) {

		TTest ttest = new TTest();
		Mean mean = new Mean();

		RealMatrix matrix = new Array2DRowRealMatrix(datas);

		double[] result = result_temp.getValues(metric);

		// System.out.print("Distin " + stringofmetric[metric] + " : ");
		result[1] = ex.data[1][metric];

		// System.out.print("Ignore " + stringofmetric[metric] + " : ");
		result[2] = ex.data[2][metric];

		// System.out.print("Random " + stringofmetric[metric] + " : ");
		// for (int j = 0; j < 30; j++) {
		// System.out.print(datas[j][metric] + " ");
		// }

		result[0] = mean.evaluate(matrix.getColumn(metric));
		// System.out.println();
		// System.out.println("avg : " + );
		result[3] = ttest.tTest(ex.data[1][metric], matrix.getColumn(metric));
		result[4] = ttest.tTest(ex.data[2][metric], matrix.getColumn(metric));

	}

	public void conductTest(int start, int end) {
		DATA[] results = new DATA[end - start];
		for (int i = start; i < end; i++) {
			// System.out.println();
			// System.out.println("the " + i + " th");

			RandomAndTraditionalExperiment ex = new RandomAndTraditionalExperiment();
			ex.test(i, UnitSimulate.DISTIN_FIC);
			ex.test(i, UnitSimulate.IGNORE_FIC);

			double[][] datas = new double[30][stringofmetric.length];
			for (int j = 0; j < 30; j++) {
				// RandomAndILPExperiment ex2 = new RandomAndILPExperiment();
				ex.test(i, UnitSimulate.MASK_FIC_OLD);
				for (int k = 0; k < ex.data[0].length; k++)
					datas[j][k] = ex.data[0][k];
				// System.out.println("-----------------------------------------");
			}

			DATA result_temp = new DATA();

			storeResult(result_temp, testNum, ex, datas);
			storeResult(result_temp, replace, ex, datas);
			storeResult(result_temp, replaceTime, ex, datas);
			storeResult(result_temp, avgmetric, ex, datas);
			storeResult(result_temp, avgparent, ex, datas);
			storeResult(result_temp, avgchild, ex, datas);
			storeResult(result_temp, avgignore, ex, datas);
			storeResult(result_temp, avgirrlevant, ex, datas);
			storeResult(result_temp, avgaccuate, ex, datas);
			storeResult(result_temp, millions, ex, datas);

			results[i - start] = result_temp;
		}

		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$");

		int[] metricss = { avgmetric, avgaccuate, avgparent, avgchild,
				avgignore, avgirrlevant, testNum, replace, replaceTime };
		System.out.print("[");
		for (int met : metricss) {
			if (met == replace)
				break;
			show(results, met);
		}
		System.out.println("]");

		showPaper1(results, metricss);

		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$");

		showPaper2(results, metricss);
	}

	public void show(DATA[] result, int metric) {
		System.out.print("[");
		for (int i = 0; i < 3; i++) {
			System.out.print("[");
			for (DATA d : result) {
				double[] metr = d.getValues(metric);
				System.out.print(metr[i] + ", ");
			}
			System.out.print("], ");
		}
		System.out.println("],");
	}

	public void showPaper1(DATA[] result, int[] metrics) {
		for (int metric : metrics)
			System.out.print(stringofmetric[metric] + "\t");
		System.out.println();

		for (DATA d : result) {
			for (int metric : metrics) {
				for (int i = 1; i < 3; i++) {
					double[] metr = d.getValues(metric);
					System.out.print(metr[i] + ", ");
				}
				System.out.print("\t");
			}
			System.out.println();
		}
	}

	public void showPaper2(DATA[] result, int[] metrics) {
		for (int metric : metrics)
			System.out.print(stringofmetric[metric] + "\t");
		System.out.println();

		for (DATA d : result) {
			for (int metric : metrics) {
				for (int i : new int[] { 0, 3, 4 }) {
					double[] metr = d.getValues(metric);
					System.out.print(metr[i] + ", ");
				}
				System.out.print("\t");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {

		RandomAndTraditionalExperiment ex = new RandomAndTraditionalExperiment();

		ex.conductTest(0, ex.setup.getRecords().size());
	}
}

/**
 * 
 * 0 for random 1 for distin 2 for ignore 3 for p-value between random and
 * distin 4 for p-value between random and ignore
 * 
 * 
 * @author xintao
 *
 */
class DATA {
	double[][] DA = new double[10][5];

	public double[] getValues(int index) {
		if (index == RandomAndTraditionalExperiment.testNum)
			return DA[0];
		else if (index == RandomAndTraditionalExperiment.replace)
			return DA[1];
		else if (index == RandomAndTraditionalExperiment.replaceTime)
			return DA[2];
		else if (index == RandomAndTraditionalExperiment.avgaccuate)
			return DA[3];
		else if (index == RandomAndTraditionalExperiment.avgchild)
			return DA[4];
		else if (index == RandomAndTraditionalExperiment.avgparent)
			return DA[5];
		else if (index == RandomAndTraditionalExperiment.avgignore)
			return DA[6];
		else if (index == RandomAndTraditionalExperiment.avgirrlevant)
			return DA[7];
		else if (index == RandomAndTraditionalExperiment.avgmetric)
			return DA[8];
		else
			return DA[9];

	}

	public double[] getValues2(int metric) {
		// TODO Auto-generated method stub
		return DA[metric];
	}
}
