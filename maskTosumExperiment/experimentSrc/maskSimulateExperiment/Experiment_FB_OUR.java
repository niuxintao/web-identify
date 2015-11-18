package maskSimulateExperiment;

import java.util.ArrayList;
import java.util.List;

import com.fc.tuple.Tuple;

public class Experiment_FB_OUR {
	private ExpriSetUp setup;

	//mfb  是 ylimaz原始方法，  mfc 是我的方法， mfba 是 ylimaz 方法和我的方法结合， 这里主要涉及到覆盖表
	
	public static final int mfb = 0;
	public static final int acfb = 1;
	public static final int pafb = 2;
	public static final int chfb = 3;
	public static final int igfb = 4;
	public static final int irfb = 5;
	public static final int tmfb = 6;

	public static final int mfc = 7;
	public static final int acfc = 8;
	public static final int pafc = 9;
	public static final int chfc = 10;
	public static final int igfc = 11;
	public static final int irfc = 12;
	public static final int tmfc = 13;

	public static final int mfba = 14;
	public static final int acfba = 15;
	public static final int pafba = 16;
	public static final int chfba = 17;
	public static final int igfba = 18;
	public static final int irfba = 19;

	private double[] data;

	public Experiment_FB_OUR() {
		setup = new ExpriSetUp();
		data = new double[20];
	}

	public void test(int index, int degree) {
		DataRecord record = setup.getRecords().get(index);
		setup.set(record.param, record.wrongs, record.bugs, record.faults,
				record.priority);

		List<Tuple> bench = new ArrayList<Tuple>();
		for (Integer key : setup.getBugsList().keySet()) {
			bench.addAll(setup.getBugsList().get(key));
		}

		BasicRunner basicRunner = new BasicRunner(setup.getPriorityList(),
				setup.getBugsList());
		Unit_FB_OUR fb = new Unit_FB_OUR();
		try {
			fb.testFB_OUR(bench, basicRunner, setup.getParam(), degree);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		data[tmfb] = fb.getAdditionalFB().size();
		data[tmfc] = fb.getAdditionalFIC().size();

		data[acfb] = fb.getEvaluateFB().getAccurateTuples().size();
		data[acfc] = fb.getEvaluateFIC().getAccurateTuples().size();

		data[mfb] = fb.getEvaluateFB().getMetric();
		data[mfc] = fb.getEvaluateFIC().getMetric();

		data[pafb] = fb.getEvaluateFB().getFatherTuples().size();
		data[pafc] = fb.getEvaluateFIC().getFatherTuples().size();

		data[chfb] = fb.getEvaluateFB().getChildTuples().size();
		data[chfc] = fb.getEvaluateFIC().getChildTuples().size();

		data[igfb] = fb.getEvaluateFB().getMissTuples().size();
		data[igfc] = fb.getEvaluateFIC().getMissTuples().size();

		data[irfb] = fb.getEvaluateFB().getRedundantTuples().size();
		data[irfc] = fb.getEvaluateFIC().getRedundantTuples().size();

		data[mfba] = fb.getEvaluateFB_addtional().getMetric();
		data[acfba] = fb.getEvaluateFB_addtional().getAccurateTuples().size();
		data[pafba] = fb.getEvaluateFB_addtional().getFatherTuples().size();
		data[chfba] = fb.getEvaluateFB_addtional().getChildTuples().size();
		data[igfba] = fb.getEvaluateFB_addtional().getMissTuples().size();
		data[irfba] = fb.getEvaluateFB_addtional().getRedundantTuples().size();
	}

	public static void main(String[] args) {
		int degree = 4;

		for (int i = 0; i < 15; i++) {
			System.out.println();
			System.out.println("the " + i + " th");
			double[][] datas = new double[30][];
			for (int j = 0; j < 30; j++) {
				Experiment_FB_OUR ex = new Experiment_FB_OUR();
				ex.test(i, degree);
				datas[j] = ex.data;
			}
			double avg = 0;
			System.out.print("fb num :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][tmfb] + " ");
				avg += datas[j][tmfb];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("fb metric :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][mfb] + " ");
				avg += datas[j][mfb];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("fb accuate :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][acfb] + " ");
				avg += datas[j][acfb];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("fb parent :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][pafb] + " ");
				avg += datas[j][pafb];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("fb child :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][chfb] + " ");
				avg += datas[j][chfb];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("fb ignore :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][igfb] + " ");
				avg += datas[j][igfb];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("fb irrlevant :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][irfb] + " ");
				avg += datas[j][irfb];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;

			System.out.print("fic num :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][tmfc] + " ");
				avg += datas[j][tmfc];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;

			System.out.print("fic metric :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][mfc] + " ");
				avg += datas[j][mfc];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("fic accuate :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][acfc] + " ");
				avg += datas[j][acfc];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("fic parent :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][pafc] + " ");
				avg += datas[j][pafc];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("fic child :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][chfc] + " ");
				avg += datas[j][chfc];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("fic ignore :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][igfc] + " ");
				avg += datas[j][igfc];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("fic irrlevant :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][irfc] + " ");
				avg += datas[j][irfc];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			
			

			avg = 0;
			System.out.print("fba metric :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][mfba] + " ");
				avg += datas[j][mfba];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("fba accuate :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][acfba] + " ");
				avg += datas[j][acfba];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("fba parent :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][pafba] + " ");
				avg += datas[j][pafba];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("fba child :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][chfba] + " ");
				avg += datas[j][chfba];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("fba ignore :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][igfba] + " ");
				avg += datas[j][igfba];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
			System.out.print("fba irrlevant :");
			for (int j = 0; j < 30; j++) {
				System.out.print(datas[j][irfba] + " ");
				avg += datas[j][irfba];
			}
			avg /= 30;
			System.out.println();
			System.out.println("avg : " + avg);
			avg = 0;
		}
	}
}
