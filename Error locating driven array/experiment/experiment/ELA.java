package experiment;

import java.util.List;

import output.OutPut;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

import ct.AETG;
import experimentData.ExperimentData;
import experimentData.GccData;
import experimentData.HsqlDBData;
import experimentData.JFlexData;
import experimentData.TcasData;
import experimentData.TomcatData;
import interaction.DataCenter;

public class ELA {

	public static int MAX = 10;

	public ELAData identifyMFS(ExperimentData data, OutPut outEachTime,
			OutPut coveringArray) {
		long time1 = System.currentTimeMillis();

		List<Tuple> mfs = data.getRealMFS();

		int d = mfs.size();
		int t = 0;
		for (Tuple m : mfs)
			if (m.getDegree() > t)
				t = m.getDegree();

		data.setDegree(t + d);

		DataCenter dataCenter = data.getDataCenter();

		AETG aetg = new AETG(dataCenter);
		aetg.process();

		for (int[] test : aetg.coveringArray) {
			TestCase testCase = new TestCaseImplement(test);
			Tuple tuple = new Tuple(testCase.getLength(), testCase);
			for (int i = 0; i < tuple.getDegree(); i++)
				tuple.set(i, i);

			for (int i = 1; i <= t; i++)
				tuple.getChildTuplesByDegree(i);
		}

		outEachTime.println("num: " + aetg.coveringArray.size());

		long time2 = System.currentTimeMillis();
		outEachTime.println("time: " + ((time2 - time1) / 1000));

		coveringArray.println("");
		for (int[] test : aetg.coveringArray) {
			for (int i : test)
				coveringArray.print(i + " ");
			coveringArray.println("");
		}

		ELAData edata = new ELAData();
		edata.size = aetg.coveringArray.size();
		edata.time = ((time2 - time1) / 1000);

		return edata;
	}

	public void test(String subject, ExperimentData data) {

		ELAData[] datas = new ELAData[MAX];

		OutPut outEachTime = new OutPut("M's ELA statsitic for  " + subject
				+ ".txt");
		OutPut outCovering_array = new OutPut("M's ELA ca for " + subject
				+ ".txt");
		for (int i = 0; i < MAX; i++) {
			datas[i] = this.identifyMFS(data, outEachTime, outCovering_array);
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

		for (ELAData data : datas) {
			num += data.size;
			time += data.time;
		}

		num /= datas.length;
		time /= datas.length;

		for (ELAData data : datas) {
			numDEV += (data.size - num) * (data.size - num);
			timeDEV += (data.time - time) * (data.time - time);
		}

		numDEV /= datas.length;
		timeDEV /= datas.length;

		numDEV = Math.sqrt(numDEV);
		timeDEV /= Math.sqrt(timeDEV);

		outEachTime.println("average  num: " + num);
		outEachTime.println("num  deviration: " + numDEV);

		outEachTime.println("average  time: " + time);
		outEachTime.println("time deviration: " + timeDEV);
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
//		ex.testJFlex();
//		ex.testHSQLDB();
		ex.testTomcat();
	}

}

class ELAData {
	public int size;
	public long time;
}
