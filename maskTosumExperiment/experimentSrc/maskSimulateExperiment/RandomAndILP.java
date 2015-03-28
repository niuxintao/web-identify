package maskSimulateExperiment;

import java.util.ArrayList;
import java.util.List;


import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

public class RandomAndILP {
	private ExpriSetUp setup;

	public static final int millions = 0;
	public static final int replaceTime = 1;
	public static final int replace = 2;

	private double[] data;

	public RandomAndILP() {
		setup = new ExpriSetUp();
		data = new double[3];
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

		UnitSimulateMiddleVariable unit = new UnitSimulateMiddleVariable();

		List<Tuple> bench = new ArrayList<Tuple>();
		for (Integer key : setup.getBugsList().keySet()) {
			bench.addAll(setup.getBugsList().get(key));
		}

//		unit.setBugs(bench);

		int allNum = 0;
		for (Integer code : exData.getWrongCases().keySet()) {
			List<TestCase> wrongCases = exData.getWrongCases().get(code);
			for (TestCase testCase : wrongCases) {
				unit.testSovler(setup.getParam(), testCase, basicRunner, code);
				unit.testAugment(setup.getParam(), testCase, basicRunner, code);
				allNum++;
			}

		}

	
		for (int i : new int[] { 9 }) {
			// System.out.println(i);
			// for (TestCase testCase : this.additionalTestCases.get(i))
			if (allNum > 0){
				data[replace] = unit.getReplacingTimes().get(i).size()
						/ (double) allNum;
				
				
				double avRpel = 0;
				for(Integer it : unit.getReplacingTimes().get(i)){
					avRpel += it.intValue();
				}
				avRpel /= (double) (unit.getReplacingTimes().get(i).size());
				data[replaceTime] = avRpel;
				
				double avRpel2 = 0;
				for(Long it : unit.getTimeMillions().get(i)){
					avRpel2 += it.longValue();
				}
				avRpel2 /= (double) (unit.getTimeMillions().get(i).size());
				data[millions] = avRpel2; 
				
			}

		}

	}

	public static void main(String[] args) {
		for (int i = 0; i < 1; i++) {
			System.out.println();
			System.out.println("the " + i + " th");
			double[][] datas = new double[30][];
			for (int j = 0; j < 30; j++) {
				RandomAndILP ex = new RandomAndILP();
				ex.test(i);
				datas[j] = ex.data;
			}
			
			double avg = 0;
			System.out.print("replace :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][replace] + " ");
				avg += datas[j][replace];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			
			avg = 0;	
			System.out.print("replaceTime :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][replaceTime] + " ");
				avg += datas[j][replaceTime];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			
			avg = 0;
			System.out.print("replaceTime :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][millions] + " ");
				avg += datas[j][millions];
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
