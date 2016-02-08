package expForRevision;

import java.util.HashMap;
import java.util.Map.Entry;

import output.OutPut;

import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

import experiment.REP;
import experimentData.ExperimentData;
import gandi.CT_process;
import gandi.ErrorLocatingDrivenArray;
import gandi.ErrorLocatingDrivenArray_CB;
import gandi.ErrorLocatingDrivenArray_FIC;
import gandi.ErrorLocatingDrivenArray_TL;
import gandi.ErrorLocatingDrivenArray_feedback;
import gandi.ErrorLocatingDrivenArray_feedback_MUOFOT;
import gandi.FD_CIT;
import gandi.TraditionalFGLI;

public class TestSensityOfNumbeOfMFS {


	public final static int ICT = 0;
	public final static int SCT = 1;
	public final static int ICT_CB = 2;
	public final static int ICT_TL = 3;
	public final static int FD = 4;
	
	public final static int ICT_FIC = 5;
	public final static int ICT_FB = 6;
	public final static int ICT_FB_MUOFOT = 7;

	public final static String[] StringAl = { "ist", "sct", "ICT_CB", "ICT_TL", "fd",
			"ICT_FIC", "ict_fb", "ICT_FB_MUOFOT"};

	public final static int NUM = 0;
	public final static int NUM_R = 1;
	public final static int NUM_I = 2;

	public final static int RECALL = 3;
	public final static int PRECISE = 4;
	public final static int F_MEASURE = 5;

	public final static int MULTI = 6;

	public final static int TIME = 7;
	public final static int TIME_R = 8;
	public final static int TIME_I = 9;

	public final static int T_COVER = 10;
	public final static int ALL_COVER = 11;

	public final static String[] SHOW = { "num", "num_r", "num_i", "recall", "precise", "f-measure", "multi", "time",
			"time_r", "time_i", "t_cover", "all_cover" };

	public TestSensityOfNumbeOfMFS() {

	}

	public EDATA execute(int algorithm, ExperimentData data, int degree, OutPut output) {
		data.setDegree(degree);
		CT_process ct_process = null;
		if (algorithm == ICT) {
			ct_process = new ErrorLocatingDrivenArray(data.getDataCenter(), data.getCaseRunner());
		} else if (algorithm == SCT) {
			ct_process = new TraditionalFGLI(data.getDataCenter(), data.getCaseRunner());
		} else if (algorithm == FD) {
			ct_process = new FD_CIT(data.getDataCenter(), data.getCaseRunner());
		} else if (algorithm == ICT_CB) {
			ct_process = new ErrorLocatingDrivenArray_CB(data.getDataCenter(), data.getCaseRunner());
		} else if (algorithm == ICT_TL) {
			ct_process = new ErrorLocatingDrivenArray_TL(data.getDataCenter(), data.getCaseRunner());
		}
		else if (algorithm == ICT_FIC) {
			ct_process = new ErrorLocatingDrivenArray_FIC(data.getDataCenter(), data.getCaseRunner());
		}else if (algorithm == ICT_FB) {
			ct_process = new ErrorLocatingDrivenArray_feedback(data.getDataCenter(), data.getCaseRunner());
		}else if (algorithm == ICT_FB_MUOFOT) {
			ct_process = new ErrorLocatingDrivenArray_feedback_MUOFOT(data.getDataCenter(), data.getCaseRunner());
		}


		ct_process.run();
		ct_process.evaluate(data.getRealMFS());

		EDATA edata = new EDATA();

		// test cases
		edata.numIdentify = ct_process.getIdentifyCases().size();
		edata.numRegular = ct_process.getRegularCTCases().size();
		edata.numTestCases = ct_process.getOverallTestCases().size();

		/****** schemas covered, how to describe, how to use it ***************/
		// schemas covered
		edata.coveredSchemasNum = ct_process.getCoveredNums();
		/******     ***************/

		edata.realIdentify = ct_process.getRealIdentify();

		edata.precise = ct_process.getPrecise();
		edata.recall = ct_process.getRecall();
		edata.f_measure = ct_process.getF_measure();

		// multi
		edata.multipleMFS = ct_process.getMultipleMFS();

		// time
		edata.allTime = ct_process.getTimeAll();
		edata.GeneratTime = ct_process.getTimeGen();
		edata.identificationTime = ct_process.getTimeIden();

		// mask
		edata.t_testedCover = ct_process.getT_tested_covered();
		edata.allCover = ct_process.getCoveredMark().length;

		// output

		output.println("testCase Num: " + edata.numTestCases);
		output.println("regaular Num: " + edata.numRegular);
		output.println("identify Num: " + edata.numIdentify);
		for (TestCase testCase : ct_process.getOverallTestCases()) {
			output.println(testCase.getStringOfTest());
		}
		// MFS
		output.println("MFS");
		for (Tuple mfs : ct_process.getMFS())
			output.println(mfs.toString());

		output.println("precise");
		output.println("" + edata.precise);
		output.println("recall");
		output.println("" + edata.recall);
		output.println("F-measure");
		output.println("" + edata.f_measure);

		output.println("multi");
		output.println("" + edata.multipleMFS);
		output.println("time");
		output.println("all " + edata.allTime + " iden " + edata.identificationTime + " gen " + edata.GeneratTime);

		output.println("t-cover");
		output.println("" + edata.t_testedCover);

		output.println("all-cover");
		output.println("" + edata.allCover);

		output.println("coveredNum");
		for (Entry<Integer, Integer> da : edata.coveredSchemasNum.entrySet()) {
			output.print("(" + da.getKey() + " : " + da.getValue() + ")  ");
		}
		output.println();

		output.println("real Identify");
		for (Entry<Tuple, Integer> da : edata.realIdentify.entrySet()) {
			output.print("(" + da.getKey().toString() + " : " + da.getValue() + ")  ");
		}
		output.println();
		// output.println("" + edata.);

		return edata;

		/**
		 * accurate and accurate_oppsite outElda.println("accurate"); double
		 * accurate = SimilarityMFS.getSimilarity(identified,
		 * data.getRealMFS()); outElda.println("" + accurate);
		 * 
		 * outElda.println("accurate_oppsite"); double accurate_oppsite =
		 * SimilarityMFS.getSimilarity( data.getRealMFS(), identified);
		 * outElda.println("" + accurate_oppsite);
		 * 
		 * edata.accurate_ELDA = accurate; edata.oppsite_accurate_ELDA =
		 * accurate_oppsite;
		 */
	}

	public void test(int algorithm, String subject, ExperimentData data) {

		String s = StringAl[algorithm];

		OutPut statistic = new OutPut("sens/mfs/avg/" + s + "statistic for " + subject + ".txt");
		OutPut statisticDev = new OutPut("sens/mfs/dev/" + s + "statistic for " + subject + ".txt");

		OutPut out2 = new OutPut("sens/mfs/specific/" + s + "2-way for " + subject + ".txt");
		EDATA[] data2 = new EDATA[REP.REP];
		for (int i = 0; i < REP.REP; i++)
			data2[i] = execute(algorithm, data, 2, out2);
		statistic.println("2-way for " + subject);
		this.statistic(algorithm, data2, statistic, statisticDev);
		out2.close();

		// OutPut out3 = new OutPut("sens/mfs/specific/" + s + "3-way for " +
		// subject
		// + ".txt");
		// EDATA[] data3 = new EDATA[REP];
		// for (int i = 0; i < REP; i++)
		// data3[i] = execute(algorithm, data, 3, out3);
		// statistic.println("3-way for " + subject);
		// this.statistic(algorithm, data3, statistic, statisticDev);
		// out3.close();
		//
		// OutPut out4 = new OutPut("sens/mfs/specific/" + s + "4-way for " +
		// subject
		// + ".txt");
		// EDATA[] data4 = new EDATA[REP];
		// for (int i = 0; i < REP; i++)
		// data4[i] = execute(algorithm, data, 4, out4);
		// statistic.println("4-way for " + subject);
		// this.statistic(algorithm, data4, statistic, statisticDev);
		// out4.close();

		statistic.close();
		statisticDev.close();
	}

	public void statistic_realIdenti(int algorithm, EDATA[] data, OutPut out, OutPut outDev) {

		HashMap<Tuple, Integer> coverAll = new HashMap<Tuple, Integer>();

		HashMap<Tuple, Double> coverAvg = new HashMap<Tuple, Double>();

		HashMap<Tuple, Double> coverDev = new HashMap<Tuple, Double>();

		for (EDATA daa : data) {
			HashMap<Tuple, Integer> cover = daa.realIdentify;
			for (Entry<Tuple, Integer> daen : cover.entrySet()) {
				if (!coverAll.containsKey(daen.getKey())) {
					coverAll.put(daen.getKey(), daen.getValue());
				} else {
					coverAll.put(daen.getKey(), (coverAll.get(daen.getKey()).intValue() + 1));
				}
			}
		}

		for (Entry<Tuple, Integer> cen : coverAll.entrySet()) {
			coverAvg.put(cen.getKey(), cen.getValue().doubleValue() / (double) data.length);
		}

		// compute dev
		for (EDATA daa : data) {
			HashMap<Tuple, Integer> cover = daa.realIdentify;
			for (Entry<Tuple, Integer> daen : cover.entrySet()) {
				double dev = (daen.getValue().doubleValue() - coverAvg.get(daen.getKey()).doubleValue())
						* (daen.getValue().doubleValue() - coverAvg.get(daen.getKey()).doubleValue());

				if (!coverDev.containsKey(daen.getKey())) {
					coverDev.put(daen.getKey(), dev);
				} else {
					coverDev.put(daen.getKey(), (coverDev.get(daen.getKey()).doubleValue() + dev));
				}
			}
		}

		for (Entry<Tuple, Double> cen : coverDev.entrySet()) {
			coverDev.put(cen.getKey(), cen.getValue().doubleValue() / (double) data.length);
		}

		String s = StringAl[algorithm];

		out.println("average " + s + " " + "CoverNUM");

		for (Entry<Tuple, Double> cen : coverAvg.entrySet()) {
			out.print("(" + cen.getKey() + " : " + cen.getValue() + ")  ");
		}

		out.println();

		outDev.println("deviration " + s + " " + "CoverNUM");
		for (Entry<Tuple, Double> cen : coverDev.entrySet()) {
			outDev.print("(" + cen.getKey() + " : " + cen.getValue() + ")  ");
		}

		outDev.println();

	}

	public void statistic_cover(int algorithm, EDATA[] data, OutPut out, OutPut outDev) {

		HashMap<Integer, Integer> coverAll = new HashMap<Integer, Integer>();

		HashMap<Integer, Double> coverAvg = new HashMap<Integer, Double>();

		HashMap<Integer, Double> coverDev = new HashMap<Integer, Double>();

		for (EDATA daa : data) {
			HashMap<Integer, Integer> cover = daa.coveredSchemasNum;
			for (Entry<Integer, Integer> daen : cover.entrySet()) {
				if (!coverAll.containsKey(daen.getKey())) {
					coverAll.put(daen.getKey(), daen.getValue());
				} else {
					coverAll.put(daen.getKey(), (coverAll.get(daen.getKey()).intValue() + 1));
				}
			}
		}

		for (Entry<Integer, Integer> cen : coverAll.entrySet()) {
			coverAvg.put(cen.getKey(), cen.getValue().doubleValue() / (double) data.length);
		}

		// compute dev
		for (EDATA daa : data) {
			HashMap<Integer, Integer> cover = daa.coveredSchemasNum;
			for (Entry<Integer, Integer> daen : cover.entrySet()) {
				double dev = (daen.getValue().doubleValue() - coverAvg.get(daen.getKey()).doubleValue())
						* (daen.getValue().doubleValue() - coverAvg.get(daen.getKey()).doubleValue());

				if (!coverDev.containsKey(daen.getKey())) {
					coverDev.put(daen.getKey(), dev);
				} else {
					coverDev.put(daen.getKey(), (coverDev.get(daen.getKey()).doubleValue() + dev));
				}
			}
		}

		for (Entry<Integer, Double> cen : coverDev.entrySet()) {
			coverDev.put(cen.getKey(), cen.getValue().doubleValue() / (double) data.length);
		}

		String s = StringAl[algorithm];

		out.println("average " + s + " " + "CoverNUM");

		for (Entry<Integer, Double> cen : coverAvg.entrySet()) {
			out.print("(" + cen.getKey() + " : " + cen.getValue() + ")  ");
		}

		out.println();

		outDev.println("deviration " + s + " " + "CoverNUM");
		for (Entry<Integer, Double> cen : coverDev.entrySet()) {
			outDev.print("(" + cen.getKey() + " : " + cen.getValue() + ")  ");
		}

		outDev.println();

	}

	public void statistic(int algorithm, EDATA[] data, OutPut out, OutPut outDev, int state) {
		double da = 0;
		double da_dev = 0;

		for (EDATA daa : data) {
			switch (state) {
			case NUM:
				da += daa.numTestCases;
				break;
			case NUM_I:
				da += daa.numIdentify;
				break;
			case NUM_R:
				da += daa.numRegular;
				break;
			case RECALL:
				da += daa.recall;
				break;
			case PRECISE:
				da += daa.precise;
				break;
			case F_MEASURE:
				da += daa.f_measure;
				break;
			case TIME:
				da += daa.allTime;
				break;
			case TIME_I:
				da += daa.identificationTime;
				break;
			case TIME_R:
				da += daa.GeneratTime;
				break;
			case MULTI:
				da += daa.multipleMFS;
				break;
			case T_COVER:
				da += daa.t_testedCover;
				break;
			case ALL_COVER:
				da += daa.allCover;
				break;

			}
		}

		da /= data.length;

		for (EDATA daa : data) {
			switch (state) {
			case NUM:
				da_dev += (daa.numTestCases - da) * (daa.numTestCases - da);
				break;
			case NUM_I:
				da_dev += (daa.numIdentify - da) * (daa.numIdentify - da);
				break;
			case NUM_R:
				da_dev += (daa.numRegular - da) * (daa.numRegular - da);
				break;
			case RECALL:
				da_dev += (daa.recall - da) * (daa.recall - da);
				break;
			case PRECISE:
				da_dev += (daa.precise - da) * (daa.precise - da);
				break;
			case F_MEASURE:
				da_dev += (daa.f_measure - da) * (daa.f_measure - da);
				break;
			case TIME:
				da_dev += (daa.allTime - da) * (daa.allTime - da);
				break;
			case TIME_I:
				da_dev += (daa.identificationTime - da) * (daa.identificationTime - da);
				break;
			case TIME_R:
				da_dev += (daa.GeneratTime - da) * (daa.GeneratTime - da);
				break;
			case MULTI:
				da_dev += (daa.multipleMFS - da) * (daa.multipleMFS - da);
				break;
			case T_COVER:
				da_dev += (daa.t_testedCover - da) * (daa.t_testedCover - da);
				break;
			case ALL_COVER:
				da_dev += (daa.allCover - da) * (daa.allCover - da);
				break;
			}

		}

		da_dev /= data.length;

		da_dev = Math.sqrt(da_dev);

		String s = StringAl[algorithm];

		out.println("average " + s + " " + SHOW[state] + " :" + da);
		out.println();
		outDev.println(SHOW[state] + " " + s + " deviration: " + da_dev);
		outDev.println();
	}

	public void statistic(int algorithm, EDATA[] edata, OutPut out, OutPut outDev) {
		out.println("###############################################");
		out.println("###############################################");

		for (int i = 0; i < SHOW.length; i++) {
			this.statistic(algorithm, edata, out, outDev, i);
		}
		statistic_cover(algorithm, edata, out, outDev);
		statistic_realIdenti(algorithm, edata, out, outDev);

		// output.println("" + edata.);
	}

	// public void statistic(EDATA[] edata_elda, EDATA[] edata_fglt, OutPut out)
	// {
	// SingleStatisitc(ICT, edata_elda, out);
	// SingleStatisitc(SCT, edata_fglt, out);
	// }

	public void testSyn(int nm, int[] param) {
		/********** only this two statement needs to revise */
		String subject = "Syn" + nm;

		DataForNumberOfMFS data = new DataForNumberOfMFS(param, nm);
		/******************************/

		testAlgorithm(subject, data, new int[] {ICT_FB_MUOFOT });
	}

	public void testAlgorithm(String subject, ExperimentData data, int[] algorithms) {
		for (int i : algorithms) {
			this.test(i, subject, data);
		}
	}

	public static void main(String[] args) {
		TestSensityOfNumbeOfMFS ex = new TestSensityOfNumbeOfMFS();
		int[] param = new int[] { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 };
		int[] num = new int[] {60, 70, 80, 90 };
		for (int nu : num){
			System.out.println("start : the number of MFS is :" + nu);
			ex.testSyn(nu, param);
		}
	}
}

class EDATA {
	public int numRegular;
	public int numIdentify;
	public int numTestCases;

	public HashMap<Integer, Integer> coveredSchemasNum;

	public HashMap<Tuple, Integer> realIdentify;

	public double precise;
	public double recall;
	public double f_measure;

	public int multipleMFS;

	public int t_testedCover;
	public int allCover;

	public long allTime;
	public long GeneratTime;
	public long identificationTime;

}