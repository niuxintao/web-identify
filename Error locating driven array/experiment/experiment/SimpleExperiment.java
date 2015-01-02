package experiment;

import java.util.ArrayList;
import java.util.List;

import output.OutPut;

import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

import experimentData.ExperimentData;
import experimentData.HsqlDBData;
import gandi.ErrorLocatingDrivenArray;
import gandi.TraditionalFGLI;

public class SimpleExperiment {

	public SimpleExperiment() {

	}

	public EDATA execute(ExperimentData data, int degree, OutPut outElda,
			OutPut outFglt) {
		data.setDegree(degree);

		ErrorLocatingDrivenArray elda = new ErrorLocatingDrivenArray(data.getDataCenter(),
				data.getCaseRunner());
		elda.run();

		TraditionalFGLI fglt = new TraditionalFGLI(data.getDataCenter(),data.getCaseRunner());
		fglt.run();

		EDATA edata = new EDATA();

		// output to the ELDA
		outElda.println("testCase Num: " + elda.getOverallTestCases().size());
		for (TestCase testCase : elda.getOverallTestCases()) {
			outElda.println(testCase.getStringOfTest());
		}
		edata.numTestCases_ELDA = elda.getOverallTestCases().size();

		outElda.println("MFS");
		for (Tuple mfs : elda.getMFS())
			outElda.println(mfs.toString());

		outElda.println("accurate");
		List<Tuple> identified = new ArrayList<Tuple>();
		identified.addAll(elda.getMFS());
		double accurate = SimilarityMFS.getSimilarity(identified,
				data.getRealMFS());
		outElda.println("" + accurate);

		edata.accurate_ELDA = accurate;

		// output to the FGLT
		outFglt.println("testCase Num: " + fglt.getOverallTestCases().size());
		for (TestCase testCase : fglt.getOverallTestCases()) {
			outFglt.println(testCase.getStringOfTest());
		}

		edata.numTestCases_FGLT = fglt.getOverallTestCases().size();

		outFglt.println("MFS");
		for (Tuple mfs : fglt.getMFS())
			outFglt.println(mfs.toString());

		outFglt.println("accurate");
		List<Tuple> identified2 = new ArrayList<Tuple>();
		identified2.addAll(fglt.getMFS());
		double accurate2 = SimilarityMFS.getSimilarity(identified2,
				data.getRealMFS());
		outFglt.println("" + accurate2);

		edata.accurate_FGLT = accurate2;

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

	public void statistic(EDATA[] edata, OutPut out) {
		double num_ELDA = 0;
		double numDEV_ELDA = 0;

		double accurate_ELDA = 0;
		double acDEV_ELDA = 0;

		for (EDATA data : edata) {
			num_ELDA += data.numTestCases_ELDA;
			accurate_ELDA += data.accurate_ELDA;
		}

		num_ELDA /= edata.length;
		accurate_ELDA /= edata.length;

		for (EDATA data : edata) {
			numDEV_ELDA += (data.numTestCases_ELDA - num_ELDA)
					* (data.numTestCases_ELDA - num_ELDA);
			acDEV_ELDA += (data.accurate_ELDA - accurate_ELDA)
					* (data.accurate_ELDA - accurate_ELDA);
		}

		numDEV_ELDA /= edata.length;
		acDEV_ELDA /= edata.length;

		numDEV_ELDA = Math.sqrt(numDEV_ELDA);
		acDEV_ELDA = Math.sqrt(acDEV_ELDA);

		out.println("average elda num: " + num_ELDA);
		out.println("num elda deviration: " + numDEV_ELDA);
		out.println("average elda accurate: " + accurate_ELDA);
		out.println("accurate elda deviration:" + acDEV_ELDA);

		double num_FGLT = 0;
		double numDEV_FGLT = 0;

		double accurate_FGLT = 0;
		double acDEV_FGLT = 0;

		for (EDATA data : edata) {
			num_FGLT += data.numTestCases_FGLT;
			accurate_FGLT += data.accurate_FGLT;
		}

		num_FGLT /= edata.length;
		accurate_FGLT /= edata.length;

		for (EDATA data : edata) {
			numDEV_FGLT += (data.numTestCases_FGLT - num_FGLT)
					* (data.numTestCases_FGLT - num_FGLT);
			acDEV_FGLT += (data.accurate_FGLT - accurate_FGLT)
					* (data.accurate_FGLT - accurate_FGLT);
		}

		numDEV_FGLT /= edata.length;
		acDEV_FGLT /= edata.length;

		numDEV_FGLT = Math.sqrt(numDEV_FGLT);
		acDEV_FGLT = Math.sqrt(acDEV_FGLT);

		out.println("average fglt num: " + num_FGLT);
		out.println("num fglt deviration: " + numDEV_FGLT);
		out.println("average fglt accurate: " + accurate_FGLT);
		out.println("accurate fglt deviration:" + acDEV_FGLT);
	}

	public void testHSQLDB() {
		/********** only this two statement needs to revise */
		String subject = "HSQLDB";
		HsqlDBData data = new HsqlDBData();
		/******************************/

		this.test(subject, data);
	}

	public static void main(String[] args) {
		SimpleExperiment ex = new SimpleExperiment();
		ex.testHSQLDB();
	}
}

class EDATA {
	public int numTestCases_ELDA;
	public double accurate_ELDA;
	public int numTestCases_FGLT;
	public double accurate_FGLT;
}
