package experiment;

import java.util.ArrayList;
import java.util.List;

import output.OutPut;

import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

import experimentData.ExperimentData;
import experimentData.HsqlDBData;
import gandi.ErrorLocatingDrivenArray;

public class SimpleExperiment {

	public SimpleExperiment() {

	}

	public EDATA execute(ExperimentData data, int degree, OutPut out) {
		data.setDegree(degree);
		ErrorLocatingDrivenArray elda = new ErrorLocatingDrivenArray(
				data.getCaseRunner());
		elda.run();

		EDATA edata = new EDATA();

		out.println("testCase Num: " + elda.getOverallTestCases().size());
		for (TestCase testCase : elda.getOverallTestCases()) {
			out.println(testCase.getStringOfTest());
		}

		edata.numTestCases = elda.getOverallTestCases().size();

		out.println("MFS");
		for (Tuple mfs : elda.getMFS())
			out.println(mfs.toString());

		out.println("accurate");
		List<Tuple> identified = new ArrayList<Tuple>();
		identified.addAll(elda.getMFS());
		double accurate = SimilarityMFS.getSimilarity(identified,
				data.getRealMFS());
		out.println("" + accurate);

		edata.accurate = accurate;

		return edata;
	}

	public void testHSQLDB() {
		HsqlDBData data = new HsqlDBData();
		OutPut statistic = new OutPut("statistic for HSQLDB.txt");

		OutPut out2 = new OutPut("2-way for HSQLDB.txt");
		EDATA[] data2 = new EDATA[30];
		for (int i = 0; i < 30; i++) {
			data2[i] = execute(data, 2, out2);
		}
		
		this.statistic(data2, statistic);

		OutPut out3 = new OutPut("3-way for HSQLDB.txt");
		EDATA[] data3 = new EDATA[30];
		for (int i = 0; i < 30; i++) {
			data3[i] = execute(data, 3, out3);
		}
		this.statistic(data3, statistic);

		OutPut out4 = new OutPut("4-way for HSQLDB.txt");
		EDATA[] data4 = new EDATA[30];
		for (int i = 0; i < 30; i++) {
			data4[i] = execute(data, 4, out4);
		}
		this.statistic(data4, statistic);
	}

	public void statistic(EDATA[] edata,  OutPut out) {
		double num = 0;
		double numDEV = 0;

		double accurate = 0;
		double acDEV = 0;

		for (EDATA data : edata) {
			num += data.numTestCases;
			accurate += data.accurate;
		}

		num /= edata.length;
		accurate /= edata.length;

		for (EDATA data : edata) {
			numDEV += (data.numTestCases - num) * (data.numTestCases - num);
			acDEV += (data.accurate - accurate) * (data.accurate - accurate);
		}

		numDEV /= edata.length;
		acDEV /= edata.length;

		numDEV = Math.sqrt(numDEV);
		acDEV = Math.sqrt(acDEV);
		
		
		out.println("average num: " + num );
		out.println("num deviration: " + numDEV );
		out.println("average accurate: " + accurate );
		out.println("accurate deviration:" + acDEV);
	}

	public static void main(String[] args) {
		SimpleExperiment ex = new SimpleExperiment();
		ex.testHSQLDB();
	}
}

class EDATA {
	public int numTestCases;
	public double accurate;
}
