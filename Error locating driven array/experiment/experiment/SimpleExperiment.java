package experiment;


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
import gandi.TraditionalFGLI;

public class SimpleExperiment {

	public final static int REP = 3;

	public final static int ICT = 0;
	public final static int SCT = 1;
	public final static int ICT_CB = 2;
	public final static int ICT_TL = 3;

	public SimpleExperiment() {

	}

	public EDATA execute(int algorithm, ExperimentData data, int degree, OutPut output) {
		data.setDegree(degree);
		CT_process ct_process = null;
		if(algorithm == ICT){
		 ct_process = new ErrorLocatingDrivenArray(
				data.getDataCenter(), data.getCaseRunner());
		}else if(algorithm == SCT){
			 ct_process = new TraditionalFGLI(data.getDataCenter(),
						data.getCaseRunner());
		}
		
		ct_process.run();
		ct_process.evaluate(data.getRealMFS());
		
		
		EDATA edata = new EDATA();

		// test cases	
		edata.numIdentify = ct_process.getIdentifyCases().size();
		edata.numRegular = ct_process.getRegularCTCases().size();
		edata.numTestCases = ct_process.getOverallTestCases().size();
		
		// schemas covered
		edata.coveredSchemasNum = ct_process.getCoveredMark();
				
		edata.precise = ct_process.getPrecise();
		edata.recall = ct_process.getRecall();
		edata.f_measure =  ct_process.getF_measure();
		
		
		//multi
		edata.multipleMFS = ct_process.getMultipleMFS();
		
		
		
		//time 
		edata.allTime = ct_process.getTimeAll();
		edata.GeneratTime = ct_process.getTimeGen();
		edata.identificationTime = ct_process.getTimeIden();
		
		//mask 
		edata.t_testedCover = ct_process.getT_tested_covered();
		edata.allCover = ct_process.getCoveredMark().length;

	
		//output
		
		output.println("testCase Num: " + ct_process.getOverallTestCases().size());
		output.println("regaular Num: " + ct_process.getRegularCTCases().size());
		output.println("identify Num: " + ct_process.getIdentifyCases().size());
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
		
		return edata;


		/**		 accurate and accurate_oppsite
		outElda.println("accurate");
		double accurate = SimilarityMFS.getSimilarity(identified,
				data.getRealMFS());
		outElda.println("" + accurate);

		outElda.println("accurate_oppsite");
		double accurate_oppsite = SimilarityMFS.getSimilarity(
				data.getRealMFS(), identified);
		outElda.println("" + accurate_oppsite);

		edata.accurate_ELDA = accurate;
		edata.oppsite_accurate_ELDA = accurate_oppsite;*/
	}

	public void test(int algorithm, String subject, ExperimentData data) {
		
		String s = algorithm == ICT ? "elda" : "fglt";
		
		OutPut statistic = new OutPut(s+ "statistic for " + subject + ".txt");

		OutPut out2 = new OutPut(s + "2-way for " + subject + ".txt");
		EDATA[] data2 = new EDATA[REP];
		for (int i = 0; i < REP; i++) 
			data2[i] = execute(algorithm, data, 2, out2);
		statistic.println("2-way for " + subject);
		this.statistic(algorithm, data2, statistic);
		out2.close();
		
		
		OutPut out3 = new OutPut(s + "3-way for " + subject + ".txt");
		EDATA[] data3 = new EDATA[REP];
		for (int i = 0; i < REP; i++) 
			data3[i] = execute(algorithm, data, 3, out3);
		statistic.println("3-way for " + subject);
		this.statistic(algorithm, data3, statistic);
		out3.close();
		
		OutPut out4 = new OutPut(s + "4-way for " + subject + ".txt");
		EDATA[] data4 = new EDATA[REP];
		for (int i = 0; i < REP; i++) 
			data4[i] = execute(algorithm, data, 4, out4);
		statistic.println("4-way for " + subject);
		this.statistic(algorithm, data4, statistic);
		out4.close();		

		statistic.close();
	}

	public void statistic(int algorithm, EDATA[] edata, OutPut out) {
		out.println("###############################################");
		out.println("###############################################");
		double num = 0;
		double numDEV = 0;

		double num_r = 0;
		double num_rDEV = 0;

		double num_i = 0;
		double num_iDEV = 0;

		double pre = 0;
		double preDEV = 0;

		double recall = 0;
		double recallDev = 0;


		double f_mea = 0;
		double f_dev = 0;


		for (EDATA data : edata) {
				num += data.numTestCases;
				num_r += data.numRegular;
				num_i += data.numIdentify;
				pre += data.precise;
				recall += data.recall;
				f_mea += data.f_measure;
		}

		num /= edata.length;
		num_r /= edata.length;
		num_i /= edata.length;
		pre /= edata.length;
		recall /= edata.length;
		f_mea /= edata.length;

		for (EDATA data : edata) {
				numDEV += (data.numTestCases - num)
						* (data.numTestCases - num);
				num_rDEV += (data.numRegular - num)
						* (data.numRegular - num);
				num_iDEV += (data.numIdentify - num)
						* (data.numIdentify - num);
				preDEV += (data.precise - pre) * (data.precise - pre);
				recallDev += (data.recall - recall)
						* (data.recall - pre);
				f_dev += (data.f_measure - f_mea) * (data.f_measure  - f_mea);

		}

		numDEV /= edata.length;
		num_rDEV /= edata.length;
		num_iDEV /= edata.length;
		preDEV /= edata.length;
		recallDev /= edata.length;
		f_dev /= edata.length;

		numDEV = Math.sqrt(numDEV);
		num_rDEV = Math.sqrt(num_rDEV);
		num_iDEV = Math.sqrt(num_iDEV);
		preDEV /= Math.sqrt(preDEV);
		recallDev /= Math.sqrt(recallDev);
		f_dev /= Math.sqrt(f_dev);

		String s = algorithm == ICT ? "elda" : "fglt";

		out.println("average " + s + " num: " + num);
		out.println("num " + s + " deviration: " + numDEV);
		out.println();

		out.println("average " + s + " num_r: " + num_r);
		out.println("num_r " + s + " deviration: " + num_rDEV);
		out.println();

		out.println("average " + s + " num_i: " + num_i);
		out.println("num_i " + s + " deviration: " + num_iDEV);
		out.println();

		out.println("average " + s + " precise: " + pre);
		out.println("precise " + s + " deviration: " + preDEV);
		out.println();

		out.println("average " + s + " recall: " + recall);
		out.println("recall " + s + " deviration: " + recallDev);
		out.println();

		out.println("average " + s + " f-measure: " + f_mea);
		out.println("f-measure " + s + " deviration: " + f_dev);
		out.println();

	}

//	public void statistic(EDATA[] edata_elda, EDATA[] edata_fglt, OutPut out) {
//		SingleStatisitc(ICT, edata_elda, out);
//		SingleStatisitc(SCT, edata_fglt, out);
//	}

	public void testHSQLDB() {
		/********** only this two statement needs to revise */
		String subject = "HSQLDB";
		HsqlDBData data = new HsqlDBData();
		/******************************/

		this.test(ICT, subject, data);
		this.test(SCT, subject, data);
	}

	public void testJFlex() {
		/********** only this two statement needs to revise */
		String subject = "JFlex";
		JFlexData data = new JFlexData();
		/******************************/

		this.test(ICT, subject, data);
		this.test(SCT, subject, data);
	}

	public void testTcas() {
		/********** only this two statement needs to revise */
		String subject = "Tcas";
		TcasData data = new TcasData();
		/******************************/

		this.test(ICT, subject, data);
		this.test(SCT, subject, data);
	}

	public void testGcc() {
		/********** only this two statement needs to revise */
		String subject = "Gcc";
		GccData data = new GccData();
		/******************************/

		this.test(ICT, subject, data);
		this.test(SCT, subject, data);
	}

	public void testTomcat() {
		/********** only this two statement needs to revise */
		String subject = "Tomcat";
		TomcatData data = new TomcatData();
		/******************************/

		this.test(ICT, subject, data);
		this.test(SCT, subject, data);
	}

	public static void main(String[] args) {
		SimpleExperiment ex = new SimpleExperiment();
		ex.testTcas();
		ex.testJFlex();
		ex.testGcc();
		ex.testHSQLDB();
	}
}

class EDATA {
	public int numRegular;
	public int numIdentify;
	public int numTestCases;

	public int[] coveredSchemasNum;

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