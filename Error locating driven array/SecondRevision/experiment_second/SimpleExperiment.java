package experiment_second;

import java.util.HashMap;
import java.util.Map.Entry;

import output.OutPut;

import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

import experimentData.ExperimentData;
import experimentData.GccData;
import experimentData.HsqlDBData;
import experimentData.JFlexData;
import experimentData.TcasData;
import experimentData.TomcatData;
import gandi.CT_process;
import gandi.ErrorLocatingDrivenArray;
import gandi.ErrorLocatingDrivenArray_CB;
import gandi.ErrorLocatingDrivenArray_TL;
import grandi2.ErrorLocatingDrivenArray_feedback_MUOFOT;
import gandi.FD_CIT;
import gandi.TraditionalFGLI;

public class SimpleExperiment {

	public final static int ICT = 0;
	public final static int SCT = 1;
	public final static int ICT_CB = 2;
	public final static int ICT_TL = 3;
	public final static int FD = 4;

	public final static int ICT_FIC = 5;
	public final static int ICT_FB = 6;
	public final static int ICT_FB_MUOFOT = 7;

	public final static String[] StringAl = { "ist", "sct", "ICT_CB", "ICT_TL",
			"fd", "ICT_FIC", "ict_fb", "ICT_FB_MUOFOT" };

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

	public final static String[] SHOW = { "num", "num_r", "num_i", "recall",
			"precise", "f-measure", "multi", "time", "time_r", "time_i",
			"t_cover", "all_cover" };

	public final static int multi_recall = 0;
	public final static int multi_precise = 1;
	public final static int multi_f_measure = 2;
	public final static int multi_found = 3;
	public final static int multi_found_percent = 4;
	public final static int multi_helped_later = 5;
	public final static int multi_hleped_later_percent = 6;

	public final static String[] SHOWMULTI = { "multi_recall", "multi_precise",
			"multi_f_measure", "multi_found", "multi_found_percent",
			"multi_helped_later", "multi_helped_later_pecent" };

	public final static int negtive_checked = 0;
	public final static int negtive_improved = 1;
	public final static int postive_checked = 2;
	public final static int postive_imporved = 3;
	public final static int feedback_times  = 4;
	
	public final static String[] SHOWFEEDBACK = { "negative_checked",
			"negative_imporved", "postive_checked", "postive_imporved",
			"feedback_times" };

	public SimpleExperiment() {

	}

	public EDATA execute(int algorithm, ExperimentData data, int degree,
			OutPut output) {
		data.setDegree(degree);
		CT_process ct_process = null;
		if (algorithm == ICT) {
			ct_process = new ErrorLocatingDrivenArray(data.getDataCenter(),
					data.getCaseRunner());
			((ErrorLocatingDrivenArray)ct_process).setActualMFS(data.getRealMFS());
		} else if (algorithm == SCT) {
			ct_process = new TraditionalFGLI(data.getDataCenter(),
					data.getCaseRunner());
			((TraditionalFGLI)ct_process).setActualMFS(data.getRealMFS());
		} else if (algorithm == FD) {
			ct_process = new FD_CIT(data.getDataCenter(), data.getCaseRunner());
		} else if (algorithm == ICT_CB) {
			ct_process = new ErrorLocatingDrivenArray_CB(data.getDataCenter(),
					data.getCaseRunner());
		} else if (algorithm == ICT_TL) {
			ct_process = new ErrorLocatingDrivenArray_TL(data.getDataCenter(),
					data.getCaseRunner());
		} else if (algorithm == ICT_FB_MUOFOT) {
			ct_process = new ErrorLocatingDrivenArray_feedback_MUOFOT(
					data.getDataCenter(), data.getCaseRunner());
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

		// muliple

		if (algorithm == ICT) {
			edata.multip_f_measure = ((ErrorLocatingDrivenArray) ct_process)
					.getMultip_f_measure();
			edata.multip_precise = ((ErrorLocatingDrivenArray) ct_process)
					.getMultip_precise();
			edata.multip_recall = ((ErrorLocatingDrivenArray) ct_process)
					.getMultip_recall();
			edata.multipe_found = ((ErrorLocatingDrivenArray) ct_process)
					.getMultipe_found();
			edata.multipe_found_percent = ((ErrorLocatingDrivenArray) ct_process)
					.getMultipe_found_percent();
			edata.helpedInTheNextRun = ((ErrorLocatingDrivenArray) ct_process)
					.getHelpedInTheNextRun();
			edata.helpedInTheNextRun_percen = ((ErrorLocatingDrivenArray) ct_process)
					.getHelpedInTheNextRun_percen();

		} else if (algorithm == SCT) {
			edata.multip_f_measure = ((TraditionalFGLI) ct_process)
					.getMultip_f_measure();
			edata.multip_precise = ((TraditionalFGLI) ct_process)
					.getMultip_precise();
			edata.multip_recall = ((TraditionalFGLI) ct_process)
					.getMultip_recall();
			edata.multipe_found = ((TraditionalFGLI) ct_process)
					.getMultipe_found();
			edata.multipe_found_percent = ((TraditionalFGLI) ct_process)
					.getMultipe_found_percent();
			edata.helpedInTheNextRun = ((TraditionalFGLI) ct_process)
					.getHelpedInTheNextRun();
			edata.helpedInTheNextRun_percen = ((TraditionalFGLI) ct_process)
					.getHelpedInTheNextRun_percen();

		} else if (algorithm == ICT_FB_MUOFOT) {
			edata.multip_f_measure = ((ErrorLocatingDrivenArray_feedback_MUOFOT) ct_process)
					.getMultip_f_measure();
			edata.multip_precise = ((ErrorLocatingDrivenArray_feedback_MUOFOT) ct_process)
					.getMultip_precise();
			edata.multip_recall = ((ErrorLocatingDrivenArray_feedback_MUOFOT) ct_process)
					.getMultip_recall();
			edata.multipe_found = ((ErrorLocatingDrivenArray_feedback_MUOFOT) ct_process)
					.getMultipe_found();
			edata.multipe_found_percent = ((ErrorLocatingDrivenArray_feedback_MUOFOT) ct_process)
					.getMultipe_found_percent();
			edata.helpedInTheNextRun = ((ErrorLocatingDrivenArray_feedback_MUOFOT) ct_process)
					.getHelpedInTheNextRun();
			edata.helpedInTheNextRun_percen = ((ErrorLocatingDrivenArray_feedback_MUOFOT) ct_process)
					.getHelpedInTheNextRun_percen();
			edata.negativeChecked = ((ErrorLocatingDrivenArray_feedback_MUOFOT) ct_process).negativeChecked;
			edata.negtiveImporved = ((ErrorLocatingDrivenArray_feedback_MUOFOT) ct_process).negtiveImporved;
			edata.postiveChecked = ((ErrorLocatingDrivenArray_feedback_MUOFOT) ct_process).postiveChecked;
			edata.postiveImporved = ((ErrorLocatingDrivenArray_feedback_MUOFOT) ct_process).postiveImporved;
			edata.feedBackStartTimes = ((ErrorLocatingDrivenArray_feedback_MUOFOT) ct_process).feedBackStartTimes;
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
		output.println("all " + edata.allTime + " iden "
				+ edata.identificationTime + " gen " + edata.GeneratTime);

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
			output.print("(" + da.getKey().toString() + " : " + da.getValue()
					+ ")  ");
		}
		output.println();
		// output.println("" + edata.);

		if (algorithm == ICT || algorithm == SCT || algorithm == ICT_FB_MUOFOT) {
			output.println("multiple f measure");
			output.println("" + edata.multip_f_measure);
			output.println("multiple f recall");
			output.println("" + edata.multip_recall);
			output.println("multiple f precise");
			output.println("" + edata.multip_precise);

			output.println("multiple f found");
			output.println("" + edata.multipe_found);
			output.println("multiple f found percent");
			output.println("" + edata.multipe_found_percent);
			output.println("multipe helpedInTheNextRun");
			output.println("" + edata.helpedInTheNextRun);
			output.println("multipe helpedInTheNextRun percent");
			output.println("" + edata.helpedInTheNextRun_percen);

			if (algorithm == ICT_FB_MUOFOT) {
				output.println("negative checked");
				output.println("" + edata.negativeChecked);
				output.println("negtive impoved");
				output.println("" + edata.negtiveImporved);
				output.println("postive checked");
				output.println("" + edata.postiveChecked);
				output.println("postive impoved");
				output.println("" + edata.postiveImporved);
				output.println("feedback start times");
				output.println("" + edata.feedBackStartTimes);
			}
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

		System.out.println("two way start :  ");
		OutPut statistic = new OutPut("avg/" + s + "statistic for " + subject
				+ ".txt");
		OutPut statisticDev = new OutPut("dev/" + s + "statistic for "
				+ subject + ".txt");

		OutPut out2 = new OutPut("specific/" + s + "2-way for " + subject
				+ ".txt");
		EDATA[] data2 = new EDATA[REP.REP];
		for (int i = 0; i < REP.REP; i++)
			data2[i] = execute(algorithm, data, 2, out2);
		statistic.println("2-way for " + subject);
		this.statistic(algorithm, data2, statistic, statisticDev);
		out2.close();

		System.out.println("three way start :  ");
		OutPut out3 = new OutPut("specific/" + s + "3-way for " + subject
				+ ".txt");
		EDATA[] data3 = new EDATA[REP.REP];
		for (int i = 0; i < REP.REP; i++)
			data3[i] = execute(algorithm, data, 3, out3);
		statistic.println("3-way for " + subject);
		this.statistic(algorithm, data3, statistic, statisticDev);
		out3.close();

		System.out.println("four way start :  ");
		OutPut out4 = new OutPut("specific/" + s + "4-way for " + subject
				+ ".txt");
		EDATA[] data4 = new EDATA[REP.REP];
		for (int i = 0; i < REP.REP; i++)
			data4[i] = execute(algorithm, data, 4, out4);
		statistic.println("4-way for " + subject);
		this.statistic(algorithm, data4, statistic, statisticDev);
		out4.close();

		statistic.close();
		statisticDev.close();
	}

	public void statistic_realIdenti(int algorithm, EDATA[] data, OutPut out,
			OutPut outDev) {

		HashMap<Tuple, Integer> coverAll = new HashMap<Tuple, Integer>();

		HashMap<Tuple, Double> coverAvg = new HashMap<Tuple, Double>();

		HashMap<Tuple, Double> coverDev = new HashMap<Tuple, Double>();

		for (EDATA daa : data) {
			HashMap<Tuple, Integer> cover = daa.realIdentify;
			for (Entry<Tuple, Integer> daen : cover.entrySet()) {
				if (!coverAll.containsKey(daen.getKey())) {
					coverAll.put(daen.getKey(), daen.getValue());
				} else {
					coverAll.put(daen.getKey(), (coverAll.get(daen.getKey())
							.intValue() + 1));
				}
			}
		}

		for (Entry<Tuple, Integer> cen : coverAll.entrySet()) {
			coverAvg.put(cen.getKey(), cen.getValue().doubleValue()
					/ (double) data.length);
		}

		// compute dev
		for (EDATA daa : data) {
			HashMap<Tuple, Integer> cover = daa.realIdentify;
			for (Entry<Tuple, Integer> daen : cover.entrySet()) {
				double dev = (daen.getValue().doubleValue() - coverAvg.get(
						daen.getKey()).doubleValue())
						* (daen.getValue().doubleValue() - coverAvg.get(
								daen.getKey()).doubleValue());

				if (!coverDev.containsKey(daen.getKey())) {
					coverDev.put(daen.getKey(), dev);
				} else {
					coverDev.put(daen.getKey(), (coverDev.get(daen.getKey())
							.doubleValue() + dev));
				}
			}
		}

		for (Entry<Tuple, Double> cen : coverDev.entrySet()) {
			coverDev.put(cen.getKey(), cen.getValue().doubleValue()
					/ (double) data.length);
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

	public void statistic_cover(int algorithm, EDATA[] data, OutPut out,
			OutPut outDev) {

		HashMap<Integer, Integer> coverAll = new HashMap<Integer, Integer>();

		HashMap<Integer, Double> coverAvg = new HashMap<Integer, Double>();

		HashMap<Integer, Double> coverDev = new HashMap<Integer, Double>();

		for (EDATA daa : data) {
			HashMap<Integer, Integer> cover = daa.coveredSchemasNum;
			for (Entry<Integer, Integer> daen : cover.entrySet()) {
				if (!coverAll.containsKey(daen.getKey())) {
					coverAll.put(daen.getKey(), daen.getValue());
				} else {
					coverAll.put(daen.getKey(), (coverAll.get(daen.getKey())
							.intValue() + daen.getValue()));
				}
			}
		}

		for (Entry<Integer, Integer> cen : coverAll.entrySet()) {
			coverAvg.put(cen.getKey(), cen.getValue().doubleValue()
					/ (double) data.length);
		}

		// compute dev
		for (EDATA daa : data) {
			HashMap<Integer, Integer> cover = daa.coveredSchemasNum;
			for (Entry<Integer, Integer> daen : cover.entrySet()) {
				double dev = (daen.getValue().doubleValue() - coverAvg.get(
						daen.getKey()).doubleValue())
						* (daen.getValue().doubleValue() - coverAvg.get(
								daen.getKey()).doubleValue());

				if (!coverDev.containsKey(daen.getKey())) {
					coverDev.put(daen.getKey(), dev);
				} else {
					coverDev.put(daen.getKey(), (coverDev.get(daen.getKey())
							.doubleValue() + dev));
				}
			}
		}

		for (Entry<Integer, Double> cen : coverDev.entrySet()) {
			coverDev.put(cen.getKey(), cen.getValue().doubleValue()
					/ (double) data.length);
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

	public void statistic(int algorithm, EDATA[] data, OutPut out,
			OutPut outDev, int state) {
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
				da_dev += (daa.identificationTime - da)
						* (daa.identificationTime - da);
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

	public void statistic_multiple(int algorithm, EDATA[] data, OutPut out,
			OutPut outDev, int state) {
		double da = 0;
		double da_dev = 0;

		for (EDATA daa : data) {
			switch (state) {
			case multi_recall:
				da += daa.multip_recall;
				break;
			case multi_precise:
				da += daa.multip_precise;
				break;
			case multi_f_measure:
				da += daa.multip_f_measure;
				break;
			case multi_found:
				da += daa.multipe_found;
				break;
			case multi_found_percent:
				da += daa.multipe_found_percent;
				break;
			case multi_helped_later:
				da += daa.helpedInTheNextRun;
				break;
			case multi_hleped_later_percent:
				da += daa.helpedInTheNextRun_percen;
				break;
			}
		}

		da /= data.length;

		for (EDATA daa : data) {
			switch (state) {
			case multi_recall:
				da_dev += (daa.multip_recall - da) * (daa.multip_recall - da);
				break;
			case multi_precise:
				da_dev += (daa.multip_precise - da) * (daa.multip_precise - da);
				break;
			case multi_f_measure:
				da_dev += (daa.multip_f_measure - da) * (daa.multip_f_measure - da);
				break;
			case multi_found:
				da_dev += (daa.multipe_found - da) * (daa.multipe_found - da);
				break;
			case multi_found_percent:
				da_dev += (daa.multipe_found_percent - da) * (daa.multipe_found_percent - da);
				break;
			case multi_helped_later:
				da_dev += (daa.helpedInTheNextRun - da) * (daa.helpedInTheNextRun - da);
				break;
			case multi_hleped_later_percent:
				da_dev += (daa.helpedInTheNextRun_percen - da) * (daa.helpedInTheNextRun_percen - da);
				break;
			}

		}

		da_dev /= data.length;

		da_dev = Math.sqrt(da_dev);

		String s = StringAl[algorithm];

		out.println("average " + s + " " + SHOWMULTI[state] + " :" + da);
		out.println();
		outDev.println(SHOWMULTI[state] + " " + s + " deviration: " + da_dev);
		outDev.println();
	}

	public void statistic_feedbackImproved(int algorithm, EDATA[] data,
			OutPut out, OutPut outDev, int state) {
		double da = 0;
		double da_dev = 0;

		for (EDATA daa : data) {
			switch (state) {
			case negtive_checked:
				da += daa.negativeChecked;
				break;
			case negtive_improved:
				da += daa.negtiveImporved;
				break;
			case postive_checked:
				da += daa.postiveChecked;
				break;
			case postive_imporved:
				da += daa.postiveImporved;
				break;
			case feedback_times:
				da += daa.feedBackStartTimes;
				break;
			}
		}

		da /= data.length;

		for (EDATA daa : data) {
			switch (state) {
			case negtive_checked:
				da_dev += (daa.negativeChecked - da) * (daa.negativeChecked - da);
				break;
			case negtive_improved:
				da_dev += (daa.negtiveImporved - da) * (daa.negtiveImporved - da);
				break;
			case postive_checked:
				da_dev += (daa.postiveChecked - da) * (daa.postiveChecked - da);
				break;
			case postive_imporved:
				da_dev += (daa.postiveImporved - da) * (daa.postiveImporved - da);
				break;
			case feedback_times:
				da_dev += (daa.feedBackStartTimes - da) * (daa.feedBackStartTimes - da);
				break;
			}

		}

		da_dev /= data.length;

		da_dev = Math.sqrt(da_dev);

		String s = StringAl[algorithm];

		out.println("average " + s + " " + SHOWFEEDBACK[state] + " :" + da);
		out.println();
		outDev.println(SHOWFEEDBACK[state] + " " + s + " deviration: " + da_dev);
		outDev.println();
	}

	public void statistic(int algorithm, EDATA[] edata, OutPut out,
			OutPut outDev) {
		out.println("###############################################");
		out.println("###############################################");

		for (int i = 0; i < SHOW.length; i++) {
			this.statistic(algorithm, edata, out, outDev, i);
		}
		

		statistic_cover(algorithm, edata, out, outDev);
		statistic_realIdenti(algorithm, edata, out, outDev);
		
		
		for(int i = 0; i < SHOWMULTI.length; i++){
			this.statistic_multiple(algorithm, edata, out, outDev, i);
		}
		
		for(int i = 0; i <  SHOWFEEDBACK.length; i++){
			this.statistic_feedbackImproved(algorithm, edata, out, outDev, i);
		}
		

		// output.println("" + edata.);
	}

	// public void statistic(EDATA[] edata_elda, EDATA[] edata_fglt, OutPut out)
	// {
	// SingleStatisitc(ICT, edata_elda, out);
	// SingleStatisitc(SCT, edata_fglt, out);
	// }

	public void testHSQLDB() {
		/********** only this two statement needs to revise */
		String subject = "HSQLDB";
		HsqlDBData data = new HsqlDBData();
		/******************************/

		testAlgorithm(subject, data, REP.ALG);
	}

	public void testJFlex() {
		/********** only this two statement needs to revise */
		String subject = "JFlex";
		JFlexData data = new JFlexData();
		/******************************/

		testAlgorithm(subject, data, REP.ALG);
	}

	public void testTcas() {
		/********** only this two statement needs to revise */
		String subject = "Tcas";
		TcasData data = new TcasData();
		/******************************/

		testAlgorithm(subject, data, REP.ALG);
	}

	public void testGcc() {
		/********** only this two statement needs to revise */
		String subject = "Gcc";
		GccData data = new GccData();
		/******************************/

		testAlgorithm(subject, data, REP.ALG);
	}

	public void testTomcat() {
		/********** only this two statement needs to revise */
		String subject = "Tomcat";
		TomcatData data = new TomcatData();
		/******************************/

		testAlgorithm(subject, data, REP.ALG);
	}

	public void testAlgorithm(String subject, ExperimentData data,
			int[] algorithms) {
		for (int i : algorithms) {
			this.test(i, subject, data);
		}
	}

	public static void main(String[] args) {
		SimpleExperiment ex = new SimpleExperiment();
		ex.testJFlex();
		ex.testGcc();
		ex.testHSQLDB();
		ex.testTomcat();
		ex.testTcas();
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

	public double multip_precise = 0;

	public double multip_recall = 0;

	public double multip_f_measure = 0;

	public double multipe_found = 0;

	public double multipe_found_percent = 0;

	public double helpedInTheNextRun = 0;

	public double helpedInTheNextRun_percen = 0;

	public double negtiveImporved = 0;

	// 正确的， 确改错了
	public double postiveImporved = 0;

	// 错误的的 检查到 错误
	public double postiveChecked = 0;

	// 正确的的 检查到 错误
	public double negativeChecked = 0;

	// 启动的次数
	public double feedBackStartTimes = 0;

}