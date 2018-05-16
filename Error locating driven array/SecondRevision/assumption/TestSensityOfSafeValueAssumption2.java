package assumption;

import java.util.HashMap;
import java.util.Map.Entry;

import output.OutPut;

import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

import experimentData.ExperimentData;
import experiment_second.REP;
import gandi.CT_process;
import gandi.ErrorLocatingDrivenArray;
import gandi.ErrorLocatingDrivenArray_CB;
import gandi.ErrorLocatingDrivenArray_FIC;
import gandi.ErrorLocatingDrivenArray_TL;
import gandi.ErrorLocatingDrivenArray_feedback;
import grandi2.ErrorLocatingDrivenArray_feedback_MUOFOT;
import gandi.FD_CIT;
import gandi.TraditionalFGLI;

public class TestSensityOfSafeValueAssumption2 {

	public final static int ICT = 0;
	public final static int SCT = 1;
	public final static int ICT_CB = 2;
	public final static int ICT_TL = 3;
	public final static int FD = 4;

	public final static int ICT_FIC = 5;
	public final static int ICT_FB = 6;
	public final static int ICT_FB_MUOFOT = 7;

	public final static String[] StringAl = { "ist", "sct", "ICT_CB", "ICT_TL", "fd", "ICT_FIC", "ict_fb",
			"ICT_FB_MUOFOT" };

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
	
	public final static int W_CHECK = 12;
	public final static int W_IDEN = 13;
	
	public final static int ENCOUNTER_UNSAFE = 0;
	public final static int TRIGGER_UNSAFE = 1;
	public final static int IDENTIFICATIONTIMES = 2;
	
	public final static String[] unsafeStrings = {"encounter unsafe", "trigger unsafe", "identification times"};

	public final static String[] SHOW = { "num", "num_r", "num_i", "recall", "precise", "f-measure", "multi", "time",
			"time_r", "time_i", "t_cover", "all_cover", "w check", "w iden" };

	public TestSensityOfSafeValueAssumption2() {

	}

	public EDATA execute(int algorithm, ExperimentData data, int degree, OutPut output) {
		data.setDegree(degree);
		CT_process ct_process = null;
		if (algorithm == ICT) {
			ct_process = new ErrorLocatingDrivenArray(data.getDataCenter(), data.getCaseRunner());
			((ErrorLocatingDrivenArray)ct_process).setActualMFS(data.getRealMFS());
//			((DataForSafeValueAssumption)data).getUnSafeMFS();
		} else if (algorithm == SCT) {
			ct_process = new TraditionalFGLI(data.getDataCenter(), data.getCaseRunner());
			((TraditionalFGLI)ct_process).setActualMFS(data.getRealMFS());
		} else if (algorithm == FD) {
			ct_process = new FD_CIT(data.getDataCenter(), data.getCaseRunner());
		} else if (algorithm == ICT_CB) {
			ct_process = new ErrorLocatingDrivenArray_CB(data.getDataCenter(), data.getCaseRunner());
		} else if (algorithm == ICT_TL) {
			ct_process = new ErrorLocatingDrivenArray_TL(data.getDataCenter(), data.getCaseRunner());
		} else if (algorithm == ICT_FIC) {
			ct_process = new ErrorLocatingDrivenArray_FIC(data.getDataCenter(), data.getCaseRunner());
		} else if (algorithm == ICT_FB) {
			ct_process = new ErrorLocatingDrivenArray_feedback(data.getDataCenter(), data.getCaseRunner());
		} else if (algorithm == ICT_FB_MUOFOT) {
			ct_process = new ErrorLocatingDrivenArray_feedback_MUOFOT(data.getDataCenter(), data.getCaseRunner());
			((ErrorLocatingDrivenArray)ct_process).setActualMFS(data.getRealMFS());
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
		
		
		if (algorithm == ICT) {
			edata.encounterUnsafe = ((ErrorLocatingDrivenArray) ct_process).getEncounterUnsafe();
			edata.triggerFailureUnsafe =  ((ErrorLocatingDrivenArray) ct_process).getEncounterUnsafe();
			edata.identificationTimes =  ((ErrorLocatingDrivenArray) ct_process).getIdentificationTimes();
		} else if (algorithm == ICT_FB_MUOFOT){
			edata.encounterUnsafe = ((ErrorLocatingDrivenArray_feedback_MUOFOT) ct_process).getEncounterUnsafe();
			edata.triggerFailureUnsafe =  ((ErrorLocatingDrivenArray_feedback_MUOFOT) ct_process).getEncounterUnsafe();
			edata.identificationTimes =  ((ErrorLocatingDrivenArray_feedback_MUOFOT) ct_process).getIdentificationTimes();
	
		} else if(algorithm  ==  SCT){
			edata.encounterUnsafe = ((TraditionalFGLI) ct_process).getEncounterUnsafe();
			edata.triggerFailureUnsafe =  ((TraditionalFGLI) ct_process).getEncounterUnsafe();
			edata.identificationTimes =  ((TraditionalFGLI) ct_process).getIdentificationTimes();

		}
		
		
		

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
		
		if(algorithm == ICT_FB_MUOFOT){
		edata.wrongChecked = ((ErrorLocatingDrivenArray_feedback_MUOFOT)ct_process).wrongNumberChecked;
		edata.wrongIdentifyed =(int) (ct_process.getMFS().size() - (((ErrorLocatingDrivenArray_feedback_MUOFOT)ct_process).getPrecise() * ct_process.getMFS().size()));
		output.println("wrong Checked");
		output.println("" + edata.wrongChecked);
		output.println("wrong identified");
		output.println("" + edata.wrongIdentifyed);
		}
		
		
		if (algorithm == ICT || algorithm == SCT || algorithm == ICT_FB_MUOFOT) {
			output.println("encounter unsafe");
			output.println("" + edata.encounterUnsafe);
			output.println("trigger unsafe");
			output.println("" + edata.triggerFailureUnsafe);
			output.println("identification times");
			output.println("" + edata.identificationTimes);
		}


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

		OutPut statistic = new OutPut("sens/safe/avg/" + s + "statistic for " + subject + ".txt");
		OutPut statisticDev = new OutPut("sens/safe/dev/" + s + "statistic for " + subject + ".txt");

		OutPut out2 = new OutPut("sens/safe/specific/" + s + "2-way for " + subject + ".txt");
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
	
	public void statistic_unsafe(int algorithm, EDATA[] data, OutPut out, OutPut outDev){
		double avgEncounter = 0;
		double avgTrigger = 0;
		double avgIdenTimes = 0;
		
		
		double  devEncounter = 0;
		double devTrigger = 0;
		double devIdenTimes = 0;
		
		for (EDATA daa : data) {
			avgEncounter += daa.encounterUnsafe;
			avgTrigger += daa.triggerFailureUnsafe;
			avgIdenTimes  += daa.identificationTimes;
		}
		avgEncounter/= data.length;
		avgTrigger/=  data.length;
		avgIdenTimes/= data.length;
		
		
		for (EDATA daa : data) {
			devEncounter += (daa.encounterUnsafe - avgEncounter) * (daa.encounterUnsafe - avgEncounter);
			devTrigger += (daa.triggerFailureUnsafe - avgTrigger) * (daa.triggerFailureUnsafe - avgTrigger);
			devIdenTimes  += (daa.identificationTimes - avgIdenTimes) * (daa.identificationTimes - avgIdenTimes);
		}
		devEncounter/=data.length;
		devTrigger/=data.length;
		devIdenTimes/=data.length;
		
		
		String s = StringAl[algorithm];
		
		out.println("average " + s + " encounter unsafe values " + " :" + avgEncounter);
		out.println();
		out.println("average " + s + " trigger unsafe values " + " :" + avgTrigger);
		out.println();
		out.println("average " + s + " identification times " + " :" + avgIdenTimes);
		out.println();
		
		outDev.println("deviration " + s + " encounter unsafe values " + " :" + devEncounter);
		outDev.println();
		outDev.println("deviration " + s + " trigger unsafe values " + " :" + devTrigger);
		outDev.println();
		outDev.println("deviration " + s + " identification times " + " :" + devIdenTimes);
		outDev.println();
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
			case W_CHECK:
				da += daa.wrongChecked;
				break;
			case W_IDEN:
				da += daa.wrongIdentifyed;
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
			case W_CHECK:
				da_dev += (daa.wrongChecked - da) *(daa.wrongChecked - da);
				break;
			case W_IDEN:
				da_dev +=(daa.wrongIdentifyed - da) *(daa.wrongIdentifyed - da);
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
		
		
		if(algorithm == ICT_FB_MUOFOT){
		this.statistic(algorithm, edata, out, outDev, W_CHECK);
		this.statistic(algorithm, edata, out, outDev, W_IDEN);
		this.statistic_unsafe(algorithm, edata, out, outDev);
		}
		if(algorithm == ICT || algorithm == SCT ){
			this.statistic_unsafe(algorithm, edata, out, outDev);
		}
		// output.println("" + edata.);
	}

	// public void statistic(EDATA[] edata_elda, EDATA[] edata_fglt, OutPut out)
	// {
	// SingleStatisitc(ICT, edata_elda, out);
	// SingleStatisitc(SCT, edata_fglt, out);
	// }

	public void testSyn(int param_length, int id) {
		/********** only this two statement needs to revise */
		String subject = "Syn" + param_length+ "-" + id;

		DataForSafeValueAssumption data = new DataForSafeValueAssumption(param_length, id);
		/******************************/
		
		System.out.println("MFS number is  : " + (data.getRealMFS().size()));
		testAlgorithm(subject, data, REP.ALG);
	}

	public void testAlgorithm(String subject, ExperimentData data, int[] algorithms) {
		for (int i : algorithms) {
			this.test(i, subject, data);
		}
	}

	public static void main(String[] args) {
		TestSensityOfSafeValueAssumption2 ex = new TestSensityOfSafeValueAssumption2();
		// 
		
//		List<List<Tuple>> = DataForSafeValueAssumption data = new DataForSafeValueAssumption(param_length, degree);
		// 
	//	int[] num = new int[] { 8,10,16,  20, 30, 40,  50,60, 70, 80, 90 };
		int[] num = new int[] {8,10,12, 16,  20, 25, 30, 35, 40,  50,60, 70, 80, 90 };
		for (int id  : new int[]{2, 7}) {
//			System.out.println("start : the number of MFS is :" + id);
			int param_length = num[id];
			ex.testSyn(param_length, id);
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
	
	public int wrongChecked;
	public int wrongIdentifyed;
	
	public double encounterUnsafe;
	public double triggerFailureUnsafe; 
	public double identificationTimes;

}