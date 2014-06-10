package maskPracticalExperiment;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import maskAlogrithms.CTA;
import maskAlogrithms.FIC_MASK;
import maskAlogrithms.OFOT_MASK;
import maskAlogrithms.SOFOT;
import maskTool.EvaluateTuples;

import com.fc.caseRunner.CaseRunner;
import com.fc.model.FIC;
import com.fc.testObject.TestCase;
//import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;

public class OFOTandFICMaskExperiement {
	// public static final int CHAIN = 0;
	public static final int AUGCHAIN = 0;
	// public static final int FEEDBACK = 2;
	public static final int AUGFEEDBACK = 1;
	public static final int FIC = 2;
	public static final int RI = 3;
	public static final int OFOT = 4;
	public static final int LG = 5;
	public static final int SP = 6;
	public static final int AIFL = 7;
	public static final int TRT = 8;
	public static final int AUGTRT = 9;
	public static final int CTA = 10;

	public static final int NUM = 11;

	// private CaseRunner caseRunner;

	public OFOTandFICMaskExperiement() {
	}

	public List<Tuple> expFIC(TestCase wrongCase, List<Integer> result,
			int[] param, List<Integer> lowerpriority) {
		// System.out.println("FIC_BS");
		CaseRunner caseRunner = getCaseRunner(result, param, lowerpriority);
		FIC fic = new FIC(wrongCase, param, caseRunner);
		fic.FicNOP();

		return fic.getBugs();
	}

	public List<Tuple> expOFOT(TestCase wrongCase, List<Integer> result,
			int[] param, List<Integer> lowerpriority) {
		// System.out.println("OFOT");
		CaseRunner caseRunner = getCaseRunner(result, param, lowerpriority);
		SOFOT ofot = new SOFOT();
		ofot.process(wrongCase, param, caseRunner);

		TestSuite suite = new TestSuiteImplement();
		for (TestCase testCase : ofot.getExecuted())
			suite.addTest(testCase);

		return ofot.getBugs();
	}

	public List<Tuple> expCTA(TestCase wrongCase, List<Integer> result,
			int[] param, List<Integer> lowerpriority) throws Exception {
		CaseRunner caseRunner = getCaseRunner(result, param, lowerpriority);
		SOFOT ofot = new SOFOT();
		ofot.process(wrongCase, param, caseRunner);

		TestSuite suite = new TestSuiteImplement();
		for (TestCase testCase : ofot.getExecuted())
			suite.addTest(testCase);

		// List<TestCase> executed = ofot.getExecuted();

		String[] classes = new String[] { "0", "1" };
		// classes[0] = "0";
		// for (int j = 1; j < classes.length; j++)
		// classes[j] = statistic.getBugCode().get(j - 1).intValue() + "";

		CTA cta = new CTA();

		String[] state = new String[suite.getTestCaseNum()];
		for (int i = 0; i < state.length; i++) {
			int runresult = caseRunner.runTestCase(suite.getAt(i)) == TestCase.PASSED ? 0
					: 1;
			state[i] = "" + runresult;
		}
		cta.process(param, classes, suite, state);

		return cta.getBugs(1);
	}

	private CaseRunner getCaseRunner(List<Integer> result, int[] parameters,
			List<Integer> lowerpriority) {
		// if (caseRunner == null) {
		CaseRunner caseRunner = new TableRunner(result, parameters,
				lowerpriority);
		// }
		return caseRunner;
	}

	public List<Tuple> expFICmask(TestCase wrongCase, List<Integer> result,
			int[] param, List<Integer> lowerpriority, int wrongCode) {
		// System.out.println("FIC_BS");
		CaseRunner caseRunner = getCaseRunnerMask(result, param, lowerpriority,
				wrongCode);
		FIC_MASK fic = new FIC_MASK(wrongCase, param, caseRunner, wrongCode);
		fic.FicNOP();

		return fic.getBugs();
	}

	public List<Tuple> expOFOTmask(TestCase wrongCase, List<Integer> result,
			int[] param, List<Integer> lowerpriority, int wrongCode) {
		// System.out.println("OFOT");
		CaseRunner caseRunner = getCaseRunnerMask(result, param, lowerpriority,
				wrongCode);
		OFOT_MASK ofot = new OFOT_MASK();
		ofot.process(wrongCase, param, caseRunner);

		// TestSuite suite = new TestSuiteImplement();
		// for (TestCase testCase : ofot.getExecuted())
		// suite.addTest(testCase);

		return ofot.getBugs();
	}

	public List<Tuple> expCTAMask(TestCase wrongCase, List<Integer> result,
			int[] param, List<Integer> lowerpriority, int wrongCode)
			throws Exception {

		CaseRunner caseRunner = getCaseRunnerMask(result, param, lowerpriority,
				wrongCode);
		OFOT_MASK ofot = new OFOT_MASK();
		ofot.process(wrongCase, param, caseRunner);

		TestSuite suite = new TestSuiteImplement();
		for (TestCase testCase : ofot.getExecuted())
			suite.addTest(testCase);

		// List<TestCase> executed = ofot.getExecuted();

		String[] classes = new String[] { "0", "1" };
		// classes[0] = "0";
		// for (int j = 1; j < classes.length; j++)
		// classes[j] = statistic.getBugCode().get(j - 1).intValue() + "";

		CTA cta = new CTA();

		String[] state = new String[suite.getTestCaseNum()];
		for (int i = 0; i < state.length; i++) {
			int runresult = caseRunner.runTestCase(suite.getAt(i)) == TestCase.FAILED ? 1
					: 0;
			state[i] = "" + runresult;
//			System.out.println(runresult);
		}
	
		cta.process(param, classes, suite, state);
		
		List<Tuple> tuples = cta.getBugs(1);
//		for(Tuple t : tuple)
//			System.out.println(t.toString());

//		return tuples;
		return tuples;
	}

	private CaseRunner getCaseRunnerMask(List<Integer> result,
			int[] parameters, List<Integer> lowerpriority, int wrongCode) {
		TableRunnerMask caseRunner = new TableRunnerMask(result, parameters,
				lowerpriority, wrongCode);
		return caseRunner;
	}

	public String toStringTuple(Tuple tuple) {
		int len = tuple.getCaseLen();
		int[] data = new int[len];
		for (int i = 0; i < len; i++) {
			data[i] = -1;
		}
		for (int i = 0; i < tuple.getDegree(); i++) {
			data[tuple.getParamIndex()[i]] = tuple.getParamValue()[i];
		}

		String rs = "";
		for (int i = 0; i < len; i++) {
			if (data[i] == -1)
				rs += "-";
			else
				rs += data[i];
			if (i != len - 1)
				rs += " ";
		}
		return rs;
	}

	public void runIngoreApproach(String flag, Statistics statistic,
			List<Tuple> bugs) {

		List<Integer> wrongCodes = statistic.getBugCode();

		HashSet<Tuple> ofotTuples = new HashSet<Tuple>();
		HashSet<Tuple> ficTuples = new HashSet<Tuple>();
		HashSet<Tuple> ctaTuples = new HashSet<Tuple>();

		for (int wrongCode : wrongCodes) {

			List<Integer> lowerpriority = new ArrayList<Integer>();

			for (int code : wrongCodes)
				if (wrongCode != code)
					lowerpriority.add(code);

			statistic.readResults(wrongCode);
			List<TestCase> wrongCases = statistic.getWrongCases();
			TestCase rightCase = statistic.getRightCases().get(0);
			List<Integer> result = statistic.getResult();
			int[] param = statistic.getParam();

			TestSuite rightSuite = new TestSuiteImplement();
			rightSuite.addTest(rightCase);

			HashSet<Tuple> ofotTuplesTemp = new HashSet<Tuple>();
			HashSet<Tuple> ficTuplesTemp = new HashSet<Tuple>();
			HashSet<Tuple> ctaTuplesTemp = new HashSet<Tuple>();

			String partPath = "./alogrithm/" + "bug" + "/";

			try {

				BufferedWriter outFIC = new BufferedWriter(
						new OutputStreamWriter(
								new FileOutputStream(partPath + "FIC" + "_"
										+ flag + "_" + wrongCode + ".txt")));
				BufferedWriter outOFOT = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(partPath
								+ "OFOT" + "_" + flag + "_" + wrongCode
								+ ".txt")));

				BufferedWriter outCTA = new BufferedWriter(
						new OutputStreamWriter(
								new FileOutputStream(partPath + "CTA" + "_"
										+ flag + "_" + wrongCode + ".txt")));

				int i = 0;
				for (TestCase wrongCase : wrongCases) {
					i++;
					System.out.println("the " + i + "time");

					// System.out.println("fic");
					List<Tuple> fic = this.expFIC(wrongCase, result, param,
							lowerpriority);
					ficTuplesTemp.addAll(fic);

					List<Tuple> ofot = this.expOFOT(wrongCase, result, param,
							lowerpriority);
					ofotTuplesTemp.addAll(ofot);

					List<Tuple> cta = this.expCTA(wrongCase, result, param,
							lowerpriority);
					ctaTuplesTemp.addAll(cta);

				}

				for (Tuple tuple : ficTuplesTemp) {
					outFIC.write(toStringTuple(tuple) + ";");
					outFIC.newLine();
				}

				for (Tuple tuple : ofotTuplesTemp) {
					outOFOT.write(toStringTuple(tuple) + ";");
					outOFOT.newLine();
				}

				for (Tuple tuple : ctaTuplesTemp) {
					outCTA.write(toStringTuple(tuple) + ";");
					outCTA.newLine();
				}

				ofotTuples.addAll(ofotTuplesTemp);
				ficTuples.addAll(ficTuplesTemp);
				ctaTuples.addAll(ctaTuplesTemp);

				outFIC.close();
				outOFOT.close();
				outCTA.close();

				// outCTA.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		try {
			String path = "./alogrithm/revAl" + "_" + flag + ".txt";
			BufferedWriter outStatis = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(path)));

			EvaluateTuples eva = new EvaluateTuples();
			List<Tuple> ficTupleList = new ArrayList<Tuple>();
			for (Tuple tuple : ficTuples)
				ficTupleList.add(tuple);
			eva.evaluate(bugs, ficTupleList);
			outPutAlo(outStatis, "fic", eva.getAccurateTuples().size(), eva
					.getFatherTuples().size(), eva.getChildTuples().size(), eva
					.getMissTuples().size(), eva.getRedundantTuples().size());

			List<Tuple> ofotTupleList = new ArrayList<Tuple>();
			for (Tuple tuple : ofotTuples)
				ofotTupleList.add(tuple);
			eva.evaluate(bugs, ofotTupleList);
			outPutAlo(outStatis, "ofot", eva.getAccurateTuples().size(), eva
					.getFatherTuples().size(), eva.getChildTuples().size(), eva
					.getMissTuples().size(), eva.getRedundantTuples().size());

			List<Tuple> ctaTupleList = new ArrayList<Tuple>();
			for (Tuple tuple : ctaTuples)
				ctaTupleList.add(tuple);
			eva.evaluate(bugs, ctaTupleList);
			outPutAlo(outStatis, "cta", eva.getAccurateTuples().size(), eva
					.getFatherTuples().size(), eva.getChildTuples().size(), eva
					.getMissTuples().size(), eva.getRedundantTuples().size());

			outStatis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void runOurApproach(String flag, Statistics statistic,
			List<Tuple> bugs) {

		List<Integer> wrongCodes = statistic.getBugCode();

		HashSet<Tuple> ofotTuples = new HashSet<Tuple>();
		HashSet<Tuple> ficTuples = new HashSet<Tuple>();
		HashSet<Tuple> ctaTuples = new HashSet<Tuple>();

		for (int wrongCode : wrongCodes) {

			List<Integer> lowerpriority = new ArrayList<Integer>();

			// for (int code : wrongCodes)
			// if (wrongCode != code)
			// lowerpriority.add(code);

			statistic.readResults(wrongCode);
			List<TestCase> wrongCases = statistic.getWrongCases();
			TestCase rightCase = statistic.getRightCases().get(0);
			List<Integer> result = statistic.getResult();
			int[] param = statistic.getParam();

			TestSuite rightSuite = new TestSuiteImplement();
			rightSuite.addTest(rightCase);

			HashSet<Tuple> ofotTuplesTemp = new HashSet<Tuple>();
			HashSet<Tuple> ficTuplesTemp = new HashSet<Tuple>();
//			HashSet<Tuple> ctaTuplesTemp = new HashSet<Tuple>();

			String partPath = "./alogrithm/" + "bug" + "/";

			try {

				BufferedWriter outFIC = new BufferedWriter(
						new OutputStreamWriter(
								new FileOutputStream(partPath + "FIC" + "_"
										+ flag + "_" + wrongCode + ".txt")));
				BufferedWriter outOFOT = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(partPath
								+ "OFOT" + "_" + flag + "_" + wrongCode
								+ ".txt")));
//				BufferedWriter outCTA = new BufferedWriter(
//						new OutputStreamWriter(
//								new FileOutputStream(partPath + "CTA" + "_"
//										+ flag + "_" + wrongCode + ".txt")));

				int i = 0;
				for (TestCase wrongCase : wrongCases) {
					i++;
					System.out.println("the " + i + "time");

					// System.out.println("fic");
					List<Tuple> fic = this.expFICmask(wrongCase, result, param,
							lowerpriority, wrongCode);
					ficTuplesTemp.addAll(fic);

					List<Tuple> ofot = this.expOFOTmask(wrongCase, result,
							param, lowerpriority, wrongCode);
					ofotTuplesTemp.addAll(ofot);
//
//					List<Tuple> cta = this.expCTAMask(wrongCase, result, param,
//							lowerpriority, wrongCode);
//					ctaTuplesTemp.addAll(cta);
					
//					for(Tuple t: cta){
//						System.out.println(t.toString());
//					}
				}

				for (Tuple tuple : ficTuplesTemp) {
					outFIC.write(toStringTuple(tuple) + ";");
					outFIC.newLine();
				}

				for (Tuple tuple : ofotTuplesTemp) {
					outOFOT.write(toStringTuple(tuple) + ";");
					outOFOT.newLine();
				}
//				for (Tuple tuple : ctaTuplesTemp) {
//					outCTA.write(toStringTuple(tuple) + ";");
//					outCTA.newLine();
//				}

				ofotTuples.addAll(ofotTuplesTemp);
				ficTuples.addAll(ficTuplesTemp);
//				ctaTuples.addAll(ctaTuplesTemp);

				// System.out.println("cta:"+ctaTuples.size());

				outFIC.close();
				outOFOT.close();
//				outCTA.close();

				// outCTA.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
//
//		try {
//			String path = "./alogrithm/revAl" + "_" + flag + ".txt";
//			BufferedWriter outStatis = new BufferedWriter(
//					new OutputStreamWriter(new FileOutputStream(path)));
//
//			EvaluateTuples eva = new EvaluateTuples();
//			List<Tuple> ficTupleList = new ArrayList<Tuple>();
//			for (Tuple tuple : ficTuples)
//				ficTupleList.add(tuple);
//			eva.evaluate(bugs, ficTupleList);
//			outPutAlo(outStatis, "fic", eva.getAccurateTuples().size(), eva
//					.getFatherTuples().size(), eva.getChildTuples().size(), eva
//					.getMissTuples().size(), eva.getRedundantTuples().size());
//
//			List<Tuple> ofotTupleList = new ArrayList<Tuple>();
//			for (Tuple tuple : ofotTuples)
//				ofotTupleList.add(tuple);
//			eva.evaluate(bugs, ofotTupleList);
//			outPutAlo(outStatis, "ofot", eva.getAccurateTuples().size(), eva
//					.getFatherTuples().size(), eva.getChildTuples().size(), eva
//					.getMissTuples().size(), eva.getRedundantTuples().size());
//
//			List<Tuple> ctaTupleList = new ArrayList<Tuple>();
//			for (Tuple tuple : ctaTuples)
//				ctaTupleList.add(tuple);
//			eva.evaluate(bugs, ctaTupleList);
//			outPutAlo(outStatis, "cta", eva.getAccurateTuples().size(), eva
//					.getFatherTuples().size(), eva.getChildTuples().size(), eva
//					.getMissTuples().size(), eva.getRedundantTuples().size());
//
//			outStatis.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		for (int wrongCode : wrongCodes) {

			List<Integer> lowerpriority = new ArrayList<Integer>();

			// for (int code : wrongCodes)
			// if (wrongCode != code)
			// lowerpriority.add(code);

			statistic.readResults(wrongCode);
			List<TestCase> wrongCases = statistic.getWrongCases();
			TestCase rightCase = statistic.getRightCases().get(0);
			List<Integer> result = statistic.getResult();
			int[] param = statistic.getParam();

			TestSuite rightSuite = new TestSuiteImplement();
			rightSuite.addTest(rightCase);

//			HashSet<Tuple> ofotTuplesTemp = new HashSet<Tuple>();
//			HashSet<Tuple> ficTuplesTemp = new HashSet<Tuple>();
			HashSet<Tuple> ctaTuplesTemp = new HashSet<Tuple>();

			String partPath = "./alogrithm/" + "bug" + "/";

			try {

//				BufferedWriter outFIC = new BufferedWriter(
//						new OutputStreamWriter(
//								new FileOutputStream(partPath + "FIC" + "_"
//										+ flag + "_" + wrongCode + ".txt")));
//				BufferedWriter outOFOT = new BufferedWriter(
//						new OutputStreamWriter(new FileOutputStream(partPath
//								+ "OFOT" + "_" + flag + "_" + wrongCode
//								+ ".txt")));
				BufferedWriter outCTA = new BufferedWriter(
						new OutputStreamWriter(
								new FileOutputStream(partPath + "CTA" + "_"
										+ flag + "_" + wrongCode + ".txt")));

				int i = 0;
				for (TestCase wrongCase : wrongCases) {
					i++;
					System.out.println("the " + i + "time");

					// System.out.println("fic");
//					List<Tuple> fic = this.expFICmask(wrongCase, result, param,
//							lowerpriority, wrongCode);
//					ficTuplesTemp.addAll(fic);
//
//					List<Tuple> ofot = this.expOFOTmask(wrongCase, result,
//							param, lowerpriority, wrongCode);
//					ofotTuplesTemp.addAll(ofot);

					List<Tuple> cta = this.expCTAMask(wrongCase, result, param,
							lowerpriority, wrongCode);
					ctaTuplesTemp.addAll(cta);
					
//					for(Tuple t: cta){
//						System.out.println(t.toString());
//					}
				}

//				for (Tuple tuple : ficTuplesTemp) {
//					outFIC.write(toStringTuple(tuple) + ";");
//					outFIC.newLine();
//				}
//
//				for (Tuple tuple : ofotTuplesTemp) {
//					outOFOT.write(toStringTuple(tuple) + ";");
//					outOFOT.newLine();
//				}
				for (Tuple tuple : ctaTuplesTemp) {
					outCTA.write(toStringTuple(tuple) + ";");
					outCTA.newLine();
				}

//				ofotTuples.addAll(ofotTuplesTemp);
//				ficTuples.addAll(ficTuplesTemp);
				ctaTuples.addAll(ctaTuplesTemp);

				// System.out.println("cta:"+ctaTuples.size());

//				outFIC.close();
//				outOFOT.close();
				outCTA.close();

				// outCTA.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		try {
			String path = "./alogrithm/revAl" + "_" + flag + ".txt";
			BufferedWriter outStatis = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(path)));

			EvaluateTuples eva = new EvaluateTuples();
			List<Tuple> ficTupleList = new ArrayList<Tuple>();
			for (Tuple tuple : ficTuples)
				ficTupleList.add(tuple);
			eva.evaluate(bugs, ficTupleList);
			outPutAlo(outStatis, "fic", eva.getAccurateTuples().size(), eva
					.getFatherTuples().size(), eva.getChildTuples().size(), eva
					.getMissTuples().size(), eva.getRedundantTuples().size());

			List<Tuple> ofotTupleList = new ArrayList<Tuple>();
			for (Tuple tuple : ofotTuples)
				ofotTupleList.add(tuple);
			eva.evaluate(bugs, ofotTupleList);
			outPutAlo(outStatis, "ofot", eva.getAccurateTuples().size(), eva
					.getFatherTuples().size(), eva.getChildTuples().size(), eva
					.getMissTuples().size(), eva.getRedundantTuples().size());

			List<Tuple> ctaTupleList = new ArrayList<Tuple>();
			for (Tuple tuple : ctaTuples)
				ctaTupleList.add(tuple);
			eva.evaluate(bugs, ctaTupleList);
			outPutAlo(outStatis, "cta", eva.getAccurateTuples().size(), eva
					.getFatherTuples().size(), eva.getChildTuples().size(), eva
					.getMissTuples().size(), eva.getRedundantTuples().size());

			outStatis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void runFromTraditionalApproach(String flag, Statistics statistic,
			List<Tuple> bugs) {

		String path = "./alogrithm/revAl" + "_" + flag + ".txt";

		statistic.readResults();
		int[] param = statistic.getParam();
		List<TestCase> wrongCases = statistic.getWrongCases();
		TestCase rightCase = statistic.getRightCases().get(0);
		List<Integer> result = statistic.getResult();

		TestSuite rightSuite = new TestSuiteImplement();
		rightSuite.addTest(rightCase);

		HashSet<Tuple> ofotTuples = new HashSet<Tuple>();
		HashSet<Tuple> ficTuples = new HashSet<Tuple>();
		HashSet<Tuple> ctaTuples = new HashSet<Tuple>();

		String partPath = "./alogrithm/" + "bug" + "/";
		List<Integer> lowerpriority = new ArrayList<Integer>();

		try {

			BufferedWriter outFIC = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(partPath
							+ "FIC" + "_" + flag + ".txt")));
			BufferedWriter outOFOT = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(partPath + "OFOT" + "_" + flag
							+ ".txt")));
			BufferedWriter outCTA = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(partPath
							+ "CTA" + "_" + flag + ".txt")));

			BufferedWriter outStatis = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(path)));

			int i = 0;
			for (TestCase wrongCase : wrongCases) {
				i++;
				System.out.println("the " + i + "time");

				List<Tuple> fic = this.expFIC(wrongCase, result, param,
						lowerpriority);
				ficTuples.addAll(fic);

				List<Tuple> ofot = this.expOFOT(wrongCase, result, param,
						lowerpriority);
				ofotTuples.addAll(ofot);

				List<Tuple> cta = this.expCTA(wrongCase, result, param,
						lowerpriority);
				if (cta != null)
					ctaTuples.addAll(cta);
			}

			for (Tuple tuple : ficTuples) {
				outFIC.write(toStringTuple(tuple) + ";");
				outFIC.newLine();
			}

			for (Tuple tuple : ofotTuples) {
				outOFOT.write(toStringTuple(tuple) + ";");
				outOFOT.newLine();
			}

			for (Tuple tuple : ctaTuples) {
				outCTA.write(toStringTuple(tuple) + ";");
				outCTA.newLine();
			}

			outFIC.close();
			outOFOT.close();
			outCTA.close();

			EvaluateTuples eva = new EvaluateTuples();
			List<Tuple> ficTupleList = new ArrayList<Tuple>();
			for (Tuple tuple : ficTuples)
				ficTupleList.add(tuple);
			eva.evaluate(bugs, ficTupleList);
			outPutAlo(outStatis, "fic", eva.getAccurateTuples().size(), eva
					.getFatherTuples().size(), eva.getChildTuples().size(), eva
					.getMissTuples().size(), eva.getRedundantTuples().size());

			List<Tuple> ofotTupleList = new ArrayList<Tuple>();
			for (Tuple tuple : ofotTuples)
				ofotTupleList.add(tuple);
			eva.evaluate(bugs, ofotTupleList);
			outPutAlo(outStatis, "ofot", eva.getAccurateTuples().size(), eva
					.getFatherTuples().size(), eva.getChildTuples().size(), eva
					.getMissTuples().size(), eva.getRedundantTuples().size());

			List<Tuple> ctaTupleList = new ArrayList<Tuple>();
			for (Tuple tuple : ctaTuples)
				ctaTupleList.add(tuple);
			eva.evaluate(bugs, ctaTupleList);
			outPutAlo(outStatis, "cta", eva.getAccurateTuples().size(), eva
					.getFatherTuples().size(), eva.getChildTuples().size(), eva
					.getMissTuples().size(), eva.getRedundantTuples().size());

			outStatis.close();

			// outCTA.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void outPutAlo(BufferedWriter out, String aloName, int accurate,
			int father, int child, int miss, int reduntant) throws IOException {
		out.write(aloName);
		out.newLine();
		out.write("accuarte :" + accurate);
		out.newLine();
		out.write("father :" + father);
		out.newLine();
		out.write("child :" + child);
		out.newLine();
		out.write("miss :" + miss);
		out.newLine();
		out.write("reduntant :" + reduntant);
		out.newLine();
		out.newLine();
	}

	// public void outPutAlo(BufferedWriter out, String aloName, int numDiff,
	// double similarD) throws IOException {
	// out.write(aloName);
	// out.newLine();
	// out.write("NumDiff :" + numDiff);
	// out.newLine();
	// out.write("similar :" + similarD);
	// out.newLine();
	// out.newLine();
	// }

	public double similar(Tuple A, Tuple B) {
		int Aindex = 0;
		int Bindex = 0;
		double result = 0;

		// A change to B
		while (Aindex < A.getDegree() && Bindex < B.getDegree()) {
			if (A.getParamIndex()[Aindex] > B.getParamIndex()[Bindex]) {
				// A large than B , so now is B need to increase the index to
				// have a chance to meet A
				Bindex++;
			} else if (A.getParamIndex()[Aindex] < B.getParamIndex()[Bindex]) {
				// A smaller than B, so now is A need to increase the index to
				// have a chance to meet B
				Aindex++;
			} else if (A.getParamIndex()[Aindex] == B.getParamIndex()[Bindex]) {
				if (A.getParamValue()[Aindex] == B.getParamValue()[Bindex])// equal
																			// element
					result++;
				Aindex++;
				Bindex++;
			}
		}

		int maxDegree = A.getDegree() > B.getDegree() ? A.getDegree() : B
				.getDegree();

		return result / ((double) maxDegree);

	}

	public double similar(List<Tuple> setA, HashSet<Tuple> setB) {
		double similarNum = 0;
		for (Tuple A : setA) {
			double max = 0;
			for (Tuple B : setB) {
				double temp = similar(A, B);
				if (temp > max)
					max = temp;
			}
			similarNum += max;
		}
		return similarNum / ((double) setA.size());
	}

	public static void test(String ID) {
		Statistics statistic = new Statistics();
		statistic.readTestCases("./resultNew.txt");
		statistic.readBugCodeAndLowePriority("./FaultLevel.txt");

		List<Integer> wrongCodes = statistic.getBugCode();

		List<Tuple> accurateBugs = new ArrayList<Tuple>();

		for (int wrongCode : wrongCodes) {
			ReadInput in = new ReadInput();
			in.readBugs("./bug_ot" + wrongCode + ".txt");
			List<Tuple> bugs = in.getBugs();
			accurateBugs.addAll(bugs);
		}

		if (ID == "ignore") {
			OFOTandFICMaskExperiement ta = new OFOTandFICMaskExperiement();
			ta.runIngoreApproach(ID, statistic, accurateBugs);
		} else if (ID == "one") {
			OFOTandFICMaskExperiement ta = new OFOTandFICMaskExperiement();
			ta.runFromTraditionalApproach(ID, statistic, accurateBugs);
		} else if (ID == "our") {
			OFOTandFICMaskExperiement ta = new OFOTandFICMaskExperiement();
			ta.runOurApproach(ID, statistic, accurateBugs);
		}

		// statistic.readTestCases("./result.txt");
	}

	public static void main(String[] args) {
		OFOTandFICMaskExperiement.test("our");
		OFOTandFICMaskExperiement.test("ignore");
		OFOTandFICMaskExperiement.test("one");
	

	}
}
