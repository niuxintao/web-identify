package experiment_second;

import java.util.ArrayList;
import java.util.List;

//import output.OutPut;

import com.fc.caseRunner.CaseRunner;
//import com.fc.simulateAnnelingSeedAndConstraints.Process;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

import ct.AETG_Constraints;
import experimentData.ExperimentData;
import experimentData.GccData;
import experimentData.HsqlDBData;
import experimentData.JFlexData;
import experimentData.TcasData;
import experimentData.TomcatData;
import interaction.CoveringManage;
import interaction.DataCenter;

public class ComputeT_coverage_ELA {

	public int computeT_cover(DataCenter dataCenter, CaseRunner caseRunner,
			List<Tuple> actualMFS, List<TestCase> overallTestCases) {
		// computingTcove
		int t_tested_covered = 0;
		AETG_Constraints ac = new AETG_Constraints(dataCenter);
		CoveringManage cm = new CoveringManage(dataCenter);

		for (TestCase testCase : overallTestCases) {
			if (caseRunner.runTestCase(testCase) == TestCase.PASSED) {
				int[] test = new int[testCase.getLength()];
				for (int i = 0; i < test.length; i++) {
					test[i] = testCase.getAt(i);
				}
				ac.unCovered = cm.setCover(ac.unCovered, ac.coveredMark, test);
			}
		}

		List<Tuple> mfss = new ArrayList<Tuple>();
		for (Tuple mfs : actualMFS) {
			mfss.add(mfs);
		}
		ac.addConstriants(mfss);

		t_tested_covered = dataCenter.coveringArrayNum - ac.unCovered;
		// t_tested_coveredMark = ac.coveredMark;
		return t_tested_covered;
	}

	public List<TestCase> getTestCases(int[][] table) {
		List<TestCase> overallTestCases = new ArrayList<TestCase>();
		for (int[] t : table) {
			TestCase testCase = new TestCaseImplement(t);
			overallTestCases.add(testCase);
		}
		return overallTestCases;
	}

	public int[][][] read(String path) {
		ReadInput read = new ReadInput();
		read.read(path);
		return read.getResult();
	}

	public int testTway(int degree, int[][] table, ExperimentData data) {
		data.setDegree(degree);
		List<TestCase> overallTestCases = this.getTestCases(table);
		int t_tuple = this.computeT_cover(data.getDataCenter(),
				data.getCaseRunner(), data.getRealMFS(), overallTestCases);
		System.out.println(t_tuple + " ");
		return t_tuple;
	}
	
	public void testTway_ALLTables(int degree, int[][][] tables, ExperimentData data){
		double tall = 0;
		for(int[][] table : tables){
			int t_tuple = this.testTway(degree, table, data);
			tall += t_tuple;
		}
		tall /= (double)tables.length;
		System.out.println("avg : " + tall  + "pecent: " + (tall/data.getDataCenter().coveringArrayNum ));	
	}

	public void test(String subject, ExperimentData data, String path) {
		int[][][] tables = this.read(path);
		
		for(int degree = 2; degree < 5; degree++){
			System.out.println("degree: " + degree);
			this.testTway_ALLTables(degree, tables, data);
		}
		
	}
	
	public void testJFlex() {
		/********** only this two statement needs to revise */
		String subject = "JFlex";
		System.out.println(subject);
		JFlexData data = new JFlexData();
		/******************************/
		String path = "./ela/M's ELA ca for JFlex.txt";
		this.test(subject, data, path);
	}

	public void testTcas() {
		/********** only this two statement needs to revise */
		String subject = "Tcas";
		System.out.println(subject);
		TcasData data = new TcasData();
		/******************************/
		String path = "./ela/M's ELA ca for Tcas.txt";
		this.test(subject, data, path);
	}

	public void testHSQLDB() {
		/********** only this two statement needs to revise */
		String subject = "HSQLDB";
		System.out.println(subject);
		HsqlDBData data = new HsqlDBData();
		/******************************/
		String path = "./ela/M's ELA ca for HSQLDB.txt";
		this.test(subject, data, path);
	}

	public void testGcc() {
		/********** only this two statement needs to revise */
		String subject = "GccB";
		System.out.println(subject);
		GccData data = new GccData();
		/******************************/
		String path = "./ela/M's ELA ca for GccB.txt";
		this.test(subject, data, path);
	}

	public void testTomcat() {
		/********** only this two statement needs to revise */
		String subject = "Tomcat";
		System.out.println(subject);
		TomcatData data = new TomcatData();
		/******************************/
		String path = "./ela/M's ELA ca for Tomcat.txt";
		this.test(subject, data, path);
	}

	public static void main(String[] args) {
		ComputeT_coverage_ELA ex = new ComputeT_coverage_ELA();
//		ex.testJFlex();
		ex.testHSQLDB();
		ex.testTomcat();
		ex.testGcc();
//		ex.testTcas();
	}

}
