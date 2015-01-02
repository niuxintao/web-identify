package experiment;

import java.util.ArrayList;
import java.util.List;

import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

import experimentData.ExperimentData;
import experimentData.HsqlDBData;
import gandi.ErrorLocatingDrivenArray;

public class SimpleExperiment {

	public void execute(ExperimentData data, int degree) {
		// HsqlDBData data = new HsqlDBData();
		// two way
		data.setDegree(degree);
		ErrorLocatingDrivenArray elda = new ErrorLocatingDrivenArray(
				data.getCaseRunner());
		elda.run();

		System.out
				.println("testCase Num: " + elda.getOverallTestCases().size());
		for (TestCase testCase : elda.getOverallTestCases()) {
			System.out.println(testCase.getStringOfTest());
		}
		System.out.println("MFS");
		for (Tuple mfs : elda.getMFS())
			System.out.println(mfs.toString());

		System.out.println("accurate");
		List<Tuple> identified = new ArrayList<Tuple>();
		identified.addAll(elda.getMFS());
		System.out.println(SimilarityMFS.getSimilarity(identified,
				data.getRealMFS()));
	}

	
	public void testHSQLDB() {
		HsqlDBData data = new HsqlDBData();
		execute(data, 2);
	}

	public static void main(String[] args) {
		SimpleExperiment ex = new SimpleExperiment();
		ex.testHSQLDB();
	}
}
