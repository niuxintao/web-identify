package experiment;

import java.util.ArrayList;
import java.util.List;

import output.OutPut;

import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

import experimentData.ExperimentData;
import experimentData.GccData;
import experimentData.HsqlDBData;
import experimentData.JFlexData;
import experimentData.TcasData;
import gandi.ErrorLocatingDrivenArray;
import gandi.TraditionalFGLI;

public class SimpleExperiment {

	public SimpleExperiment() {

	}

	public double f_measue(double precise, double recall) {
		double fenzi = 2 * precise * recall;
		double fenmu = precise + recall;
		if (fenmu == 0)
			return 0;
		else
			return fenzi / fenmu;
	}

	public EDATA execute(ExperimentData data, int degree, OutPut outElda,
			OutPut outFglt) {
		data.setDegree(degree);

		ErrorLocatingDrivenArray elda = new ErrorLocatingDrivenArray(
				data.getDataCenter(), data.getCaseRunner());
		elda.run();

		TraditionalFGLI fglt = new TraditionalFGLI(data.getDataCenter(),
				data.getCaseRunner());
		fglt.run();

		// deal the results
		EDATA edata = new EDATA();

		// output to the ELDA

		// test cases
		outElda.println("testCase Num: " + elda.getOverallTestCases().size());
		for (TestCase testCase : elda.getOverallTestCases()) {
			outElda.println(testCase.getStringOfTest());
		}
		edata.numTestCases_ELDA = elda.getOverallTestCases().size();

		// MFS
		outElda.println("MFS");
		for (Tuple mfs : elda.getMFS())
			outElda.println(mfs.toString());

		// precise and recall
		List<Tuple> identified = new ArrayList<Tuple>();
		identified.addAll(elda.getMFS());

		double[] pAndR = SimilarityMFS.getPreciseAndRecall(identified,
				data.getRealMFS());
		outElda.println("precise");
		outElda.println("" + pAndR[0]);
		outElda.println("recall");
		outElda.println("" + pAndR[1]);
		outElda.println("F-measure");
		outElda.println("" + f_measue(pAndR[0], pAndR[1]));

		edata.precise_ELDA = pAndR[0];
		edata.recall_ELDA = pAndR[1];

		// accurate and accurate_oppsite
		outElda.println("accurate");
		double accurate = SimilarityMFS.getSimilarity(identified,
				data.getRealMFS());
		outElda.println("" + accurate);

		outElda.println("accurate_oppsite");
		double accurate_oppsite = SimilarityMFS.getSimilarity(
				data.getRealMFS(), identified);
		outElda.println("" + accurate_oppsite);

		edata.accurate_ELDA = accurate;
		edata.oppsite_accurate_ELDA = accurate_oppsite;

		// output to the FGLT
		outFglt.println("testCase Num: " + fglt.getOverallTestCases().size());
		for (TestCase testCase : fglt.getOverallTestCases()) {
			outFglt.println(testCase.getStringOfTest());
		}

		edata.numTestCases_FGLT = fglt.getOverallTestCases().size();

		outFglt.println("MFS");
		for (Tuple mfs : fglt.getMFS())
			outFglt.println(mfs.toString());

		List<Tuple> identified2 = new ArrayList<Tuple>();
		identified2.addAll(fglt.getMFS());
		double[] pAndR2 = SimilarityMFS.getPreciseAndRecall(identified2,
				data.getRealMFS());
		outFglt.println("precise");
		outFglt.println("" + pAndR2[0]);
		outFglt.println("recall");
		outFglt.println("" + pAndR2[1]);
		outFglt.println("F-measure");
		outFglt.println("" + f_measue(pAndR2[0], pAndR2[1]));

		edata.precise_FGLT = pAndR2[0];
		edata.recall_FGLT = pAndR2[1];

		outFglt.println("accurate");

		double accurate2 = SimilarityMFS.getSimilarity(identified2,
				data.getRealMFS());
		outFglt.println("" + accurate2);

		outFglt.println("accurate_oppsite");
		double accurate_oppsite2 = SimilarityMFS.getSimilarity(
				data.getRealMFS(), identified2);
		outFglt.println("" + accurate_oppsite2);

		edata.accurate_FGLT = accurate2;
		edata.oppsite_accurate_FGLT = accurate_oppsite2;

		return edata;
	}

	public void test(String subject, ExperimentData data) {

		OutPut statistic = new OutPut("statistic for " + subject + ".txt");

		OutPut out2_ELDA = new OutPut("2-way ELDA for " + subject + ".txt");
		OutPut out2_FGLT = new OutPut("2-way FGLT for " + subject + ".txt");
		EDATA[] data2 = new EDATA[30];
		for (int i = 0; i < 30; i++) {
			data2[i] = execute(data, 2, out2_ELDA, out2_FGLT);
		}
		statistic.println("2-way for " + subject);
		this.statistic(data2, statistic);
		out2_ELDA.close();
		out2_FGLT.close();

		OutPut out3_ELDA = new OutPut("3-way ELDA for " + subject + ".txt");
		OutPut out3_FGLT = new OutPut("3-way FGLT for " + subject + ".txt");
		EDATA[] data3 = new EDATA[30];
		for (int i = 0; i < 30; i++) {
			data3[i] = execute(data, 3, out3_ELDA, out3_FGLT);
		}
		statistic.println("3-way for " + subject);
		this.statistic(data3, statistic);
		out3_ELDA.close();
		out3_FGLT.close();

		OutPut out4_ELDA = new OutPut("4-way ELDA for " + subject + ".txt");
		OutPut out4_FGLT = new OutPut("4-way FGLT for " + subject + ".txt");
		EDATA[] data4 = new EDATA[30];
		for (int i = 0; i < 30; i++) {
			data4[i] = execute(data, 4, out4_ELDA, out4_FGLT);
		}
		statistic.println("4-way for " + subject);
		this.statistic(data4, statistic);
		out4_ELDA.close();
		out4_FGLT.close();

		statistic.close();

	}

	public void SingleStatisitc(int mark, EDATA[] edata, OutPut out) {
		double num = 0;
		double numDEV = 0;

		double pre = 0;
		double preDEV = 0;

		double recall = 0;
		double recallDev = 0;

		double accurate = 0;
		double acDEV = 0;

		double oppiste_accurate = 0;
		double oppisteacDEV = 0;

		double f_mea = 0;
		double f_dev = 0;
		double[] f_meas = new double[30];

		int i = 0;

		for (EDATA data : edata) {
			if (mark == 0) {
				num += data.numTestCases_ELDA;
				pre += data.precise_ELDA;
				recall += data.recall_ELDA;
				accurate += data.accurate_ELDA;
				oppiste_accurate += data.accurate_ELDA;
				f_meas[i] = f_measue(data.precise_ELDA, data.recall_ELDA);
				f_mea += f_meas[i];
			} else {
				num += data.numTestCases_FGLT;
				pre += data.precise_FGLT;
				recall += data.recall_FGLT;
				accurate += data.accurate_FGLT;
				oppiste_accurate += data.accurate_FGLT;
				f_meas[i] = f_measue(data.precise_FGLT, data.recall_FGLT);
				f_mea += f_meas[i];
			}
			i++;
		}

		num /= edata.length;
		pre /= edata.length;
		recall /= edata.length;
		accurate /= edata.length;
		oppiste_accurate /= edata.length;
		f_mea /= edata.length;

		i = 0;
		for (EDATA data : edata) {
			if (mark == 0) {
				numDEV += (data.numTestCases_ELDA - num)
						* (data.numTestCases_ELDA - num);
				preDEV += (data.precise_ELDA - pre) * (data.precise_ELDA - pre);
				recallDev += (data.recall_ELDA - recall)
						* (data.recall_ELDA - pre);
				acDEV += (data.accurate_ELDA - accurate)
						* (data.accurate_ELDA - accurate);
				oppisteacDEV += (data.oppsite_accurate_ELDA - oppiste_accurate)
						* (data.oppsite_accurate_ELDA - oppiste_accurate);
				f_dev += (f_meas[i] - f_mea) * (f_meas[i] - f_mea);
			} else {
				numDEV += (data.numTestCases_FGLT - num)
						* (data.numTestCases_FGLT - num);
				preDEV += (data.precise_FGLT - pre) * (data.precise_FGLT - pre);
				recallDev += (data.recall_FGLT - recall)
						* (data.recall_FGLT - pre);
				acDEV += (data.accurate_FGLT - accurate)
						* (data.accurate_FGLT - accurate);
				oppisteacDEV += (data.oppsite_accurate_FGLT - oppiste_accurate)
						* (data.oppsite_accurate_FGLT - oppiste_accurate);
				f_dev += (f_meas[i] - f_mea) * (f_meas[i] - f_mea);
			}
			i++;
		}

		numDEV /= edata.length;
		preDEV /= edata.length;
		recallDev /= edata.length;
		acDEV /= edata.length;
		oppisteacDEV /= edata.length;
		f_dev /= edata.length;

		numDEV = Math.sqrt(numDEV);
		preDEV /= Math.sqrt(preDEV);
		recallDev /= Math.sqrt(recallDev);
		acDEV = Math.sqrt(acDEV);
		oppisteacDEV = Math.sqrt(oppisteacDEV);
		f_dev /=  Math.sqrt(f_dev);

		String s = mark == 0 ? "elda" : "fglt";

		out.println("average " + s + " num: " + num);
		out.println("num " + s + " deviration: " + numDEV);

		out.println("average " + s + " precise: " + pre);
		out.println("precise " + s + " deviration: " + preDEV);

		out.println("average " + s + " recall: " + recall);
		out.println("recall " + s + " deviration: " + recallDev);
		
		out.println("average " + s + " f-measure: " + f_mea);
		out.println("f-measure " + s + " deviration: " + f_dev);

		out.println("average " + s + " accurate: " + accurate);
		out.println("accurate " + s + " deviration:" + acDEV);

		out.println("average " + s + " oppiste_accurate: " + oppiste_accurate);
		out.println("oppiste_accurate " + s + " deviration: " + oppisteacDEV);
	}

	public void statistic(EDATA[] edata, OutPut out) {
		SingleStatisitc(0, edata, out);
		SingleStatisitc(1, edata, out);
	}

	public void testHSQLDB() {
		/********** only this two statement needs to revise */
		String subject = "HSQLDB";
		HsqlDBData data = new HsqlDBData();
		/******************************/

		this.test(subject, data);
	}
	
	public void testJFlex() {
		/********** only this two statement needs to revise */
		String subject = "JFlex";
		JFlexData data = new JFlexData();
		/******************************/

		this.test(subject, data);
	}
	
	public void testTcas() {
		/********** only this two statement needs to revise */
		String subject = "Tcas";
		TcasData data = new TcasData();
		/******************************/

		this.test(subject, data);
	}
	
	
	public void testGcc() {
		/********** only this two statement needs to revise */
		String subject = "Gcc";
		GccData data = new GccData();
		/******************************/

		this.test(subject, data);
	}

	public static void main(String[] args) {
		SimpleExperiment ex = new SimpleExperiment();
		ex.testGcc();
	}
}

class EDATA {
	public int numTestCases_ELDA;
	public double accurate_ELDA;
	public int numTestCases_FGLT;
	public double accurate_FGLT;

	public double precise_ELDA;
	public double recall_ELDA;
	public double precise_FGLT;
	public double recall_FGLT;

	public double oppsite_accurate_ELDA;
	public double oppsite_accurate_FGLT;
}
