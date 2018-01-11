package experiment_second;

import java.util.ArrayList;
import java.util.List;

import output.OutPut;

import com.fc.simulateAnnelingSeedAndConstraints.Process;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

import experimentData.ExperimentData;
import experimentData.GccData;
import experimentData.HsqlDBData;
import experimentData.JFlexData;
import experimentData.TcasData;
import experimentData.TomcatData;
import interaction.DataCenter;

public class ELA {

	// private HashSet<TestCase> regularCTCases;

	// private HashSet<TestCase> identifyCases;

	// private HashSet<Tuple> MFS;

	// private int[] coveredMark; // schemas covered condition

	// private int[] t_tested_coveredMark; // t-covered mark

	// private long timeAll = 0;

	// private long timeIden = 0;

	// private long timeGen = 0;

	// private int multipleMFS = 0;

	// private double precise = 0;

	// private double recall = 0;

	// private double f_measure = 0;

	public static int MAX = 5;

	public int maxDegree = 0;

	public ELAData identifyMFS(String subject, ExperimentData data, OutPut outEachTime,
			OutPut coveringArray) {
		
	
		
		int[][] seed = new int[][]{};
		List<Tuple> mfss = new ArrayList<Tuple>();
		
//输出基本信息
		
		System.out.println(subject);
		

		long time1 = System.currentTimeMillis();

		List<Tuple> mfs = data.getRealMFS();

		int d = mfs.size();
		int t = 0;
		for (Tuple m : mfs)
			if (m.getDegree() > t)
				t = m.getDegree();
		
		this.maxDegree = t;
		
		System.out.println("param number : " + data.getParam().length);	
		
		System.out.println("the number of mfs, i.e., t is : " + t);
		
		System.out.println("the number of mfs, i.e., d is : " + d);
		
		System.out.println("t+d is : " + (t+d));
		
		if((t+d) >= data.getParam().length){
			this.maxDegree = data.getParam().length;
			data.setDegree(data.getParam().length);
		}
		else{
			this.maxDegree = t+d;
			data.setDegree(t + d);	
		}
		
		System.out.println("covering array can to :" + this.maxDegree);
		
//		if((t+d) <= 6){

		DataCenter dataCenter = data.getDataCenter();

		com.fc.simulateAnneling.DataCenter.init(dataCenter.param, dataCenter.degree);
		
		
		Process aetg = new Process(seed, mfss);
//		aetg.addSeeds(aetg_all.coveredMark, aetg_all.unCovered);
//		aetg.addConstriants(mfss);
		aetg.process();
		
//		AETG aetg = new AETG(dataCenter);
//		aetg.process();

		for (int[] test : aetg.rsTable) {
			TestCase testCase = new TestCaseImplement(test);
			Tuple tuple = new Tuple(testCase.getLength(), testCase);
			for (int i = 0; i < tuple.getDegree(); i++)
				tuple.set(i, i);

			for (int i = 1; i <= t; i++)
				tuple.getChildTuplesByDegree(i);
		}

		outEachTime.println("num: " + aetg.rsTable.length);

		long time2 = System.currentTimeMillis();
		outEachTime.println("generatiion time: " + ((time2 - time1) / 1000));

		coveringArray.println("");
		for (int[] test : aetg.rsTable) {
			for (int i : test)
				coveringArray.print(i + " ");
			coveringArray.println("");
		}

		ELAData edata = new ELAData();
		edata.size = aetg.rsTable.length;
		edata.generationTime = ((time2 - time1) / 1000);
		
		
		//identification for one test case 
		long timeNow = System.currentTimeMillis();
		long oneTestCaseTime = 0;
		TestCase tests =  new TestCaseImplement(aetg.rsTable[0]);
		Tuple tuple = new Tuple(tests.getLength(),tests);
		
		for(int i = 2; i< this.maxDegree; i++){
			tuple.getChildTuplesByDegree(i);
		}
		
		long timeEnd = System.currentTimeMillis();
		
		oneTestCaseTime = timeEnd - timeNow;
		
		outEachTime.println("identifiication one  time: " + oneTestCaseTime);
		edata.oneTime = oneTestCaseTime;
		
		
		edata.identificationTime  =   oneTestCaseTime * edata.size;
		
		outEachTime.println("identifiication all  time: " + edata.identificationTime );
				return edata;	
//		}
//	return null;
		
	}

	public void test(String subject, ExperimentData data) {

		ELAData[] datas = new ELAData[MAX];

		OutPut outEachTime = new OutPut("ela/M's ELA statsitic for  " + subject
				+ ".txt");
		OutPut outCovering_array = new OutPut("ela/M's ELA ca for " + subject
				+ ".txt");
		for (int i = 0; i < MAX; i++) {
			datas[i] = this.identifyMFS(subject, data, outEachTime, outCovering_array);
		}

		printAverage(datas, outEachTime);

		outEachTime.close();
		outCovering_array.close();
	}

	public void printAverage(ELAData[] datas, OutPut outEachTime) {
		outEachTime.println("average");

		double num = 0;
		double numDEV = 0;

		double time = 0;
		double timeDEV = 0;
		
		
		double timeIO = 0;
		double timeIOV = 0;
		
		
		double timeIA = 0;
		double timeIAV = 0;

		for (ELAData data : datas) {
			num += data.size;
			time += data.generationTime;
			timeIO += data.oneTime;
			timeIA += data.identificationTime;
		}

		num /= datas.length;
		time /= datas.length;
		timeIO/= datas.length;
				
		timeIA /= datas.length;

		for (ELAData data : datas) {
			numDEV += (data.size - num) * (data.size - num);
			timeDEV += (data.generationTime - time)
					* (data.generationTime - time);
			
			timeIOV += (data.oneTime - timeIO) * (data.oneTime - timeIO);
			timeIAV += (data.identificationTime - timeIA)
					* (data.identificationTime - timeIA);
		}

		numDEV /= datas.length;
		timeDEV /= datas.length;
		timeIAV /= datas.length;
		timeIOV /= datas.length;

		numDEV = Math.sqrt(numDEV);
		timeDEV /= Math.sqrt(timeDEV);
		timeIAV /= Math.sqrt(timeIAV);
		timeIOV /= Math.sqrt(timeIOV);

		outEachTime.println("average  num: " + num);
		outEachTime.println("num  deviration: " + numDEV);

		outEachTime.println("average  time: " + time);
		outEachTime.println("time deviration: " + timeDEV);
		
		outEachTime.println("average  one time: " +  timeIO);
		outEachTime.println("one time deviration: " + timeIOV);
		
		outEachTime.println("average all idenit  time: " + timeIA);
		outEachTime.println("time all idenit deviration: " + timeIAV);
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

	public void testHSQLDB() {
		/********** only this two statement needs to revise */
		String subject = "HSQLDB";
		HsqlDBData data = new HsqlDBData();
		/******************************/

		this.test(subject, data);
	}

	public void testGcc() {
		/********** only this two statement needs to revise */
		String subject = "GccB";
		GccData data = new GccData();
		/******************************/

		this.test(subject, data);
	}

	public void testTomcat() {
		/********** only this two statement needs to revise */
		String subject = "Tomcat";
		TomcatData data = new TomcatData();
		/******************************/

		this.test(subject, data);
	}

	public static void main(String[] args) {
		ELA ex = new ELA();
		ex.testJFlex();
		ex.testHSQLDB();
		ex.testTomcat();
		ex.testGcc();
		ex.testTcas();
	}

}

class ELAData {
	public int size;
	public long generationTime;
	public long identificationTime;
	public long oneTime;
}
