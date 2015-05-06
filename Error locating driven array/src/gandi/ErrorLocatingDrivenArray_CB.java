package gandi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import locatConstaint.FIC_Constraints;
import locatConstaint.LocateGraph_Constriants;
import locatConstaint.SOFOT_Constriants;
import location.CTA;
import interaction.DataCenter;

import com.fc.caseRunner.CaseRunner;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;

import ct.AETG_Constraints;
//import com.fc.coveringArray.CoveringManage;
public class ErrorLocatingDrivenArray_CB extends ErrorLocatingDrivenArray {
	
	
	private SOFOT_Constriants ofot;
	

	public ErrorLocatingDrivenArray_CB(DataCenter dataCenter,
			CaseRunner caseRunner) {
		super(dataCenter, caseRunner);
		// TODO Auto-generated constructor stub
	}
	
	public List<Tuple> getMFS(AETG_Constraints ac, TestCase testCase) {
		return this.Combine(ac, testCase);
	}
	
	public List<Tuple> ficMFS(AETG_Constraints ac, TestCase testCase){

		FIC_Constraints fic = new FIC_Constraints(testCase, dataCenter.param, caseRunner,
				ac);
		fic.FicNOP();
		
		this.setCoverageAndIdentify(fic.getExecuted(), ac);
		
		return fic.getBugs();
	}
	
	public List<Tuple> lgMFS(AETG_Constraints ac, TestCase testCase){

		LocateGraph_Constriants lg = new LocateGraph_Constriants(caseRunner);

		Tuple tuple = new Tuple(0, testCase);
		tuple = tuple.getReverseTuple();
	

		List<Tuple> result  = lg.locateErrorsInTest(ac, testCase, tuple);
		
		List<TestCase> additional = new ArrayList<TestCase> ();
		for(int i = 0; i < lg.getAddtionalTestSuite().getTestCaseNum(); i++)
			additional.add(lg.getAddtionalTestSuite().getAt(i));
		
		this.setCoverageAndIdentify(additional, ac);
		
		return result;
		
	}
	
	public List<Tuple> cta (){

		TestSuite suite = new TestSuiteImplement();
		for (TestCase testCase : ofot.getExecuted())
			suite.addTest(testCase);

		// List<TestCase> executed = ofot.getExecuted();

		String[] classes = new String[] { "pass", "fail" };
		// classes[0] = "0";
		// for (int j = 1; j < classes.length; j++)
		// classes[j] = statistic.getBugCode().get(j - 1).intValue() + "";

		CTA cta = new CTA();

		String[] state = new String[suite.getTestCaseNum()];
		for (int i = 0; i < state.length; i++) {
			String runresult = caseRunner.runTestCase(suite.getAt(i)) == TestCase.FAILED ? "fail"
					: "pass";
			state[i] =  runresult;
//			System.out.println(runresult);
		}
	
		try {
			cta.process(dataCenter.param, classes, suite, state);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Tuple> tuples = cta.getBugs();
		
		return tuples;
	}
	

	public List<Tuple> ofotMFS(AETG_Constraints ac, TestCase testCase) {
		 ofot = new SOFOT_Constriants(dataCenter,
				testCase, ac);
		// sc.process(testCase, DataCenter.param, caseRunner);

		while (!ofot.isEnd()) {
			TestCase nextTestCase = ofot.generateNext();
			identifyCases.add(nextTestCase);
			overallTestCases.add(nextTestCase);
			// System.out.println("ofot" +
			// nextTestCase.getStringOfTest());

			int[] next = new int[nextTestCase.getLength()];
			for (int i = 0; i < next.length; i++) {
				next[i] = nextTestCase.getAt(i);
			}
			if (caseRunner.runTestCase(nextTestCase) == TestCase.PASSED) {
				ac.unCovered = cm.setCover(ac.unCovered,
						ac.coveredMark, next);
				nextTestCase.setTestState(TestCase.PASSED);
			} else
				nextTestCase.setTestState(TestCase.FAILED);
		}

		ofot.analysis();
		
		List<Tuple> mfs = ofot.getBugs();
		
		return mfs;
	}
	
	
	public void setCoverageAndIdentify(List<TestCase> generated, AETG_Constraints ac){
		identifyCases.addAll(generated);
		overallTestCases.addAll(generated);
		for(TestCase testCase : generated){
			if (caseRunner.runTestCase(testCase) == TestCase.PASSED) {
				int[] next = new int[testCase.getLength()];
				for (int i = 0; i < next.length; i++) {
					next[i] = testCase.getAt(i);
				}
				ac.unCovered = cm.setCover(ac.unCovered,
						ac.coveredMark, next);
			}
		}
	}
	
	public  List<Tuple> Combine(AETG_Constraints ac, TestCase testCase) {
		List<Tuple> bug1 = this.ofotMFS(ac, testCase);
//		List<Tuple> bug2 = this.lgMFS(ac, testCase);
//		List<Tuple> bug3 = this.lgMFS(ac, testCase);
		
		List<Tuple> bug4 = this.cta();
		
		HashSet<Tuple> rr = new HashSet<Tuple> ();
		HashSet<Tuple> rr2 = new HashSet<Tuple> ();
		for(Tuple t : bug1){
			rr.add(t);
		}
//		for(Tuple t : bug2){
//			if(rr.contains(t)){
//				rr2.add(t);
//			}else{
//				rr.add(t);
//			}
//		}
//		for(Tuple t : bug3){
//			if(rr.contains(t)){
//				rr2.add(t);
//			}else{
//				rr.add(t);
//			}
//		}
		for(Tuple t : bug4){
		if(rr.contains(t)){
			rr2.add(t);
		}else{
			rr.add(t);
		}
	}
		List<Tuple> result = new ArrayList<Tuple> ();
		for(Tuple t : rr2)
			result.add(t);
		
		return result;
		
	}
	

}
