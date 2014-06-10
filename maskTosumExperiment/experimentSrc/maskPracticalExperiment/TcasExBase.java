package maskPracticalExperiment;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.fc.TRT.CharacterizeNAProcess;
import com.fc.TRT.PathProcess;
import com.fc.caseRunner.CaseRunner;
import com.fc.model.CTA;
import com.fc.model.FIC;
import com.fc.model.IterAIFL;
import com.fc.model.LocateGraph;
import com.fc.model.OFOT;
import com.fc.model.RI;
import com.fc.model.SpectrumBased;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.CorpTupleWithTestCase;
import com.fc.tuple.Tuple;

import driver.ChainAugProcess;
import driver.ChainProcess;
import driver.FeedBackAugProcess;
import driver.FeedBackProcess;

//������������ �� ���� �� ��ȫ�� ��������
//һ����������� ���� �㷨�����Ƿ��� �Ǹ�����������ʲô����������������ϣ�����

//������Ҫ�ı��㷨��
public class TcasExBase {
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

	private List<Integer> lowerpriority;
	private int wrongCode;
	private CaseRunner caseRunner;

	public TcasExBase(int wrongCode, List<Integer> lowerpriority) {
		this.wrongCode = wrongCode;
		this.lowerpriority = lowerpriority;
	}

	/**
	 * 
	 * @param wrongCase
	 * @param bugs
	 *            : the real bugs in the wrongCase
	 * @param result
	 * @param param
	 * @param suite
	 * @return
	 */
	public double[] expChain(TestCase wrongCase, List<Tuple> bugs,
			List<Integer> result, int[] param, TestSuite suite) {
		// System.out.println("Chain");
		CaseRunner caseRunner = getCaseRunner(result, param, lowerpriority);

		ChainProcess test = new ChainProcess(wrongCase, caseRunner, param,
				suite);
		test.testWorkFlow();
		return this.getResult(test.getWorkMachine().getPool()
				.getExistedBugTuples(), test.getWorkMachine().getExtraCases(),
				bugs);
	}

	public double[] expAUGTRT(TestCase wrongCase, List<Tuple> bugs,
			List<Integer> result, int[] param, TestSuite suite) {
		// System.out.println("TRT");

		CharacterizeNAProcess test = new CharacterizeNAProcess();
		test.testWorkFlow(wrongCase, bugs, result, param, suite, lowerpriority);
		return this.getResult(test.getBugs(), test.getAdditionalSuite(), bugs);
	}

	public double[] expTRT(TestCase wrongCase, List<Tuple> bugs,
			List<Integer> result, int[] param, TestSuite suite) {
		// System.out.println("AUGTRT");

		PathProcess test = new PathProcess();
		test.testWorkFlow(wrongCase, bugs, result, param, suite, lowerpriority);
		return this.getResult(test.getBugs(), test.getAdditionalSuite(), bugs);
	}

	public DATA expChainAugFeedBack(TestCase wrongCase, List<Tuple> bugs,
			List<Integer> result, int[] param, TestSuite suite) {
		CaseRunner caseRunner = getCaseRunner(result, param, lowerpriority);
		FeedBackAugProcess fb = new FeedBackAugProcess(wrongCase, caseRunner,
				param, suite);
		fb.testWorkFlow();
		TestSuite addsuite = new TestSuiteImplement();
		for (TestCase testCase : fb.getFb().getTestCases())
			addsuite.addTest(testCase);

		DATA data = new DATA();
		data.statis = this.getResult(fb.getFb().getBugs(), addsuite, bugs);
		data.tuples = fb.getFb().getBugs();
		return data;
	}

	public DATA expAugChain(TestCase wrongCase, List<Tuple> bugs,
			List<Integer> result, int[] param, TestSuite suite) {
		// System.out.println("ChainAug");
		CaseRunner caseRunner = getCaseRunner(result, param, lowerpriority);

		ChainAugProcess test = new ChainAugProcess(wrongCase, caseRunner,
				param, suite);
		test.testWorkFlow();
		// for (Tuple tuple : bugs)
		// System.out.println(tuple.toString());
		// System.out.println();
		// for (Tuple tuple : test.getWorkMachine().getPool()
		// .getExistedBugTuples())
		// System.out.println(tuple.toString());
		//
		// System.out.println();
		DATA data = new DATA();
		data.statis = this.getResult(test.getWorkMachine().getPool()
				.getExistedBugTuples(), test.getWorkMachine().getExtraCases(),
				bugs);
		data.tuples = test.getWorkMachine().getPool().getExistedBugTuples();
		return data;
	}

	public double[] expChainFeedBack(TestCase wrongCase, List<Tuple> bugs,
			List<Integer> result, int[] param, TestSuite suite) {
		// System.out.println("FeedBack");
		CaseRunner caseRunner = getCaseRunner(result, param, lowerpriority);
		FeedBackProcess fb = new FeedBackProcess(wrongCase, caseRunner, param,
				suite);
		fb.testWorkFlow();
		TestSuite addsuite = new TestSuiteImplement();
		for (TestCase testCase : fb.getFb().getTestCases())
			addsuite.addTest(testCase);
		return this.getResult(fb.getFb().getBugs(), addsuite, bugs);
	}

	public DATA expFIC(TestCase wrongCase, List<Tuple> bugs,
			List<Integer> result, int[] param) {
		// System.out.println("FIC_BS");
		CaseRunner caseRunner = getCaseRunner(result, param, lowerpriority);
		FIC fic = new FIC(wrongCase, param, caseRunner);
		fic.FicNOP();

		DATA data = new DATA();
		data.statis = getResult(fic.getBugs(), fic.getExtraCases(), bugs);
		data.tuples = fic.getBugs();
		return data;
	}

	public DATA expRI(TestCase wrongCase, List<Tuple> bugs,
			List<Integer> result, int[] param) {
		// System.out.println("RI");
		CaseRunner caseRunner = getCaseRunner(result, param, lowerpriority);

		CorpTupleWithTestCase generate = new CorpTupleWithTestCase(wrongCase,
				param);

		RI ri = new RI(generate, caseRunner);
		List<Tuple> tupleg = ri.process(wrongCase);

		DATA data = new DATA();
		data.statis = getResult(tupleg, ri.getAddtionalTestSuite(), bugs);
		data.tuples = tupleg;
		return data;
	}

	public DATA expOFOT(TestCase wrongCase, List<Tuple> bugs,
			List<Integer> result, int[] param) {
		// System.out.println("OFOT");
		CaseRunner caseRunner = getCaseRunner(result, param, lowerpriority);
		OFOT ofot = new OFOT();
		ofot.process(wrongCase, param, caseRunner);

		TestSuite suite = new TestSuiteImplement();
		for (TestCase testCase : ofot.getExecuted().keySet())
			suite.addTest(testCase);

		DATA data = new DATA();
		data.statis = getResult(ofot.getBugs(), suite, bugs);
		data.tuples = ofot.getBugs();
		return data;

	}

	public double[] expIterAIFL(TestCase wrongCase, List<Tuple> bugs,
			List<Integer> result, int[] param) {
		// System.out.println("IterAIFL");
		CorpTupleWithTestCase generate = new CorpTupleWithTestCase(wrongCase,
				param);
		CaseRunner caseRunner = getCaseRunner(result, param, lowerpriority);
		IterAIFL ifl = new IterAIFL(generate, caseRunner);
		ifl.process(wrongCase);
		return getResult(ifl.getBugs(), ifl.getSuite(), bugs);
	}

	public DATA expLocateGraph(TestCase wrongCase, List<Tuple> bugs,
			List<Integer> result, int[] param, TestCase rightCase) {
		// System.out.println("LocateGraph");

		CaseRunner caseRunner = getCaseRunner(result, param, lowerpriority);
		LocateGraph lg = new LocateGraph(caseRunner);
		Tuple tuple = new Tuple(0, wrongCase);
		tuple = tuple.getReverseTuple();

		List<Tuple> faidu = lg.locateErrorsInTest(rightCase, wrongCase, tuple);

		DATA data = new DATA();
		data.statis = this.getResult(faidu, lg.getAddtionalTestSuite(), bugs);
		data.tuples = faidu;
		return data;
	}

	// a covering array may make it better
	public DATA expSpectrumBased(TestCase wrongCase, List<Tuple> bugs,
			List<Integer> result, int[] param, int degree) {
		// System.out.println("SpectrumBased");

		CaseRunner caseRunner = getCaseRunner(result, param, lowerpriority);

		SpectrumBased sp = new SpectrumBased(caseRunner);
		TestSuite suite = new TestSuiteImplement();
		wrongCase.setTestState(TestCase.FAILED);
		suite.addTest(wrongCase);

		// setting 4
		sp.process(suite, param, degree);

		DATA data = new DATA();
		data.statis = this.getResult(sp.getFailreIndcuing(),
				sp.getAddtionalSuite(), bugs);
		data.tuples = sp.getFailreIndcuing();
		return data;
	}

	// hasn't think
	public void expLocateGraphBinary() {

	}

	// need a covering array
	public DATA expCTA(TestCase wrongCase, List<Tuple> bugs,
			List<Integer> result, int[] param) throws Exception {
		// System.out.println("Classified tree analysis");
		CaseRunner caseRunner = getCaseRunner(result, param, lowerpriority);
		// for (int i = 0; i < suite.getTestCaseNum(); i++)
		// suite.getAt(i).setTestState(caseRunner.runTestCase(suite.getAt(i)));
		// String[] classes = { "pass", "fail" };
		// String[] state = new String[suite.getTestCaseNum()];
		// for (int i = 0; i < suite.getTestCaseNum(); i++)
		// state[i] = (suite.getAt(i).testDescription() == TestCase.PASSED ?
		// "pass"
		// : "fail");
		// cta.process(param, classes, suite, state);

		TestSuite suite = new TestSuiteImplement();
		suite.addTest(wrongCase);
		CTA cta = new CTA();
		cta.process(suite, param, caseRunner);

		TestSuite add = new TestSuiteImplement();
		for (TestCase testCase : cta.getExecuted().keySet())
			add.addTest(testCase);
		double[] processResult = this.getResult(cta.getBugs(), add, bugs);
		processResult[0] -= 1;

		DATA data = new DATA();
		data.statis = processResult;
		data.tuples = cta.getBugs();
		return data;
	}

	private CaseRunner getCaseRunner(List<Integer> result, int[] parameters,
			List<Integer> lowerpriority) {
		if (caseRunner == null) {
			caseRunner = new TableRunner(result, parameters, lowerpriority);
		}
		return caseRunner;
	}

	public double[] getRecallAndPrecise(List<Tuple> identified,
			List<Tuple> realBugs) {
		double recall = 0;
		double precise = 0;
		for (Tuple iden : identified) {
			if (realBugs.contains(iden)) {
				recall++;
				precise++;
			}
		}
		recall = recall / (double) realBugs.size();
		precise = precise / (double) identified.size();

		double[] result = new double[2];
		result[0] = recall;
		result[1] = precise;
		return result;
	}

	public double[] getResult(List<Tuple> bugs, TestSuite suite,
			List<Tuple> realBugs) {
		double[] result = new double[3];
		result[0] = suite.getTestCaseNum();
		double[] info = this.getRecallAndPrecise(bugs, realBugs);
		result[1] = info[0];
		result[2] = info[1];

		return result;
	}

	public List<Tuple> getBugsFromWrongCase(TestCase wrongCase,
			List<Tuple> tuples) {
		List<Tuple> results = new ArrayList<Tuple>();
		for (Tuple tuple : tuples)
			if (wrongCase.containsOf(tuple))
				results.add(tuple);
		return results;
	}

	public void test(int[] param, TestCase wrongCase, TestCase rightCase,
			List<Integer> result, List<Tuple> bugs) {

		TestSuite rightSuite = new TestSuiteImplement();
		rightSuite.addTest(rightCase);

		this.expChain(wrongCase, bugs, result, param, rightSuite);
		this.expChainFeedBack(wrongCase, bugs, result, param, rightSuite);
		this.expAugChain(wrongCase, bugs, result, param, rightSuite);
		this.expChainAugFeedBack(wrongCase, bugs, result, param, rightSuite);
		this.expFIC(wrongCase, bugs, result, param);
		this.expRI(wrongCase, bugs, result, param);
		this.expOFOT(wrongCase, bugs, result, param);
		this.expIterAIFL(wrongCase, bugs, result, param);
		this.expSpectrumBased(wrongCase, bugs, result, param, 2);
		this.expLocateGraph(wrongCase, bugs, result, param, rightCase);
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

	public void testAvgOfOneInput(int[] param, List<TestCase> wrongCases,
			TestCase rightCase, List<Integer> result, List<Tuple> bugs) {

		String path = "./alogrithm/revAl" + wrongCode + ".txt";

		TestSuite rightSuite = new TestSuiteImplement();
		rightSuite.addTest(rightCase);

		double[][] avg = new double[NUM][3];

		String partPath = "./alogrithm/" + wrongCode + "/";
		try {
			BufferedWriter outAUGCHAIN = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(partPath
							+ "AUGCHAIN.txt")));

			BufferedWriter outAUGFEEDBACK = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(partPath
							+ "AUGFEEDBACK.txt")));
			BufferedWriter outFIC = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(partPath + "FIC.txt")));
			BufferedWriter outRI = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(partPath + "RI.txt")));
			BufferedWriter outOFOT = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(partPath + "OFOT.txt")));
			BufferedWriter outSP = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(partPath + "SP.txt")));
			BufferedWriter outLG = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(partPath + "LG.txt")));
			BufferedWriter outCTA = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(partPath + "CTA.txt")));

			BufferedWriter outRealBug = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(partPath
							+ "BUG.txt")));

			int i = 0;
			for (TestCase wrongCase : wrongCases) {
				i++;
				System.out.println("the " + i + "time");
				// System.out.println(wrongCase.getStringOfTest());
				// System.out.println("chain");
				List<Tuple> specificBugs = this.getBugsFromWrongCase(wrongCase,
						bugs);
				for (Tuple bug : specificBugs)
					outRealBug.write(toStringTuple(bug) + ";");
				outRealBug.newLine();

				DATA ac = this.expAugChain(wrongCase, specificBugs, result,
						param, rightSuite);
				for (Tuple tuple : ac.tuples)
					outAUGCHAIN.write(toStringTuple(tuple) + ";");
				outAUGCHAIN.newLine();
				add(avg[AUGCHAIN], ac.statis);

				// System.out.println("feedBack");
				DATA af = this.expChainAugFeedBack(wrongCase, specificBugs,
						result, param, rightSuite);
				for (Tuple tuple : af.tuples)
					outAUGFEEDBACK.write(toStringTuple(tuple) + ";");
				outAUGFEEDBACK.newLine();
				add(avg[AUGFEEDBACK], af.statis);

				// System.out.println("fic");
				DATA fic = this.expFIC(wrongCase, specificBugs, result, param);
				for (Tuple tuple : fic.tuples)
					outFIC.write(toStringTuple(tuple) + ";");
				outFIC.newLine();
				add(avg[FIC], fic.statis);

				DATA ri = this.expRI(wrongCase, specificBugs, result, param);
				for (Tuple tuple : ri.tuples)
					outRI.write(toStringTuple(tuple) + ";");
				outRI.newLine();
				add(avg[RI], ri.statis);

				DATA ofot = this
						.expOFOT(wrongCase, specificBugs, result, param);
				for (Tuple tuple : ofot.tuples)
					outOFOT.write(toStringTuple(tuple) + ";");
				outOFOT.newLine();
				add(avg[OFOT], ofot.statis);
				// add(avg[AIFL], this.expIterAIFL(wrongCase, specificBugs,
				// result,
				// param));
				//
				// DATA sp = this.expSpectrumBased(wrongCase, specificBugs,
				// result, param, 4);
				// for (Tuple tuple : sp.tuples)
				// outSP.write(toStringTuple(tuple) + ";");
				// outSP.newLine();
				// add(avg[SP], sp.statis);

				DATA lg = this.expLocateGraph(wrongCase, specificBugs, result,
						param, rightCase);
				for (Tuple tuple : lg.tuples)
					outLG.write(toStringTuple(tuple) + ";");
				outLG.newLine();
				add(avg[LG], lg.statis);

				// add(avg[TRT], this.expTRT(wrongCase, specificBugs, result,
				// param,
				// rightSuite));
				// add(avg[AUGTRT], this.expAUGTRT(wrongCase, specificBugs,
				// result,
				// param, rightSuite));
				try {
					wrongCase.setTestState(TestCase.FAILED);
					DATA cta = this.expCTA(wrongCase, specificBugs, result,
							param);
					for (Tuple tuple : cta.tuples) {
						outCTA.write(toStringTuple(tuple) + ";");
					}
					outCTA.newLine();
					add(avg[CTA], cta.statis);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			outAUGCHAIN.close();
			outAUGFEEDBACK.close();
			outFIC.close();
			outRI.close();
			outOFOT.close();
			outSP.close();
			outLG.close();
			outCTA.close();
			outRealBug.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int i = 0; i < NUM; i++)
			getAvg(avg[i], wrongCases.size());

		this.outputResult(avg, path);

	}

	public void outputResult(double[][] avg, String path) {
		BufferedWriter out = null;

		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(path)));

			outPutAlo(avg, out, "AUGCHAIN", AUGCHAIN);
			outPutAlo(avg, out, "AUGFEEDBACK", AUGFEEDBACK);
			outPutAlo(avg, out, "FIC", FIC);
			outPutAlo(avg, out, "RI", RI);
			outPutAlo(avg, out, "OFOT", OFOT);
			outPutAlo(avg, out, "LG", LG);
			outPutAlo(avg, out, "SP", SP);
			outPutAlo(avg, out, "AIFL", AIFL);
			outPutAlo(avg, out, "TRT", TRT);
			outPutAlo(avg, out, "AUGTRT", AUGTRT);
			outPutAlo(avg, out, "CTA", CTA);

			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void outPutAlo(double[][] avg, BufferedWriter out, String aloName,
			int alo) throws IOException {
		out.write(aloName);
		out.newLine();
		out.write("cases:");
		out.newLine();
		out.write("" + avg[alo][0]);
		out.newLine();
		out.write("recall:");
		out.newLine();
		out.write("" + avg[alo][1]);
		out.newLine();
		out.write("precise:");
		out.newLine();
		out.write("" + avg[alo][2]);
		out.newLine();
	}

	public void add(double[] a, double[] b) {
		a[0] += b[0];
		a[1] += b[1];
		a[2] += b[2];
	}

	public void getAvg(double[] a, int num) {
		a[0] /= num;
		a[1] /= num;
		a[2] /= num;
	}

	public static void main(String[] args) {

		// int[] param = new int[] { 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 2, 2, 3,
		// 2,
		// 2 };
		Statistics statistic = new Statistics();

		int wrongCode = 2;
		List<Integer> lowerpriority = new ArrayList<Integer>();
		lowerpriority.add(1);
		// lowerpriority.add(2);

		statistic.readTestCases("./result.txt");
		statistic.readResults(wrongCode);

		ReadInput in = new ReadInput();
		in.readBugs("./bug_ot" + wrongCode + ".txt");

		List<Tuple> bugs = in.getBugs();

		TcasExBase ta = new TcasExBase(wrongCode, lowerpriority);

		TestCase rightCase = statistic.getRightCases().get(0);

		ta.testAvgOfOneInput(statistic.getParam(), statistic.getWrongCases(),
				rightCase, statistic.getResult(), bugs);
	}
}

class DATA {
	public List<Tuple> tuples;
	public double[] statis;
}
