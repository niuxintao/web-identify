package org.jflex.test;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import JFlex.Main;
import JFlex.Out;

public class TestJFlex {
	public String[][] configurations;

	public List<String> allConfigurations;

	// public List<String> configurations;

	private OutputSet outputRecord;

	private PrintStream out;
	private PrintStream err;
	private ByteArrayOutputStream record;
	private ByteArrayOutputStream errs;

	public void redirectSystemOut() {
		PrintStream ps = new PrintStream(record);
		Out.setOutputStream(ps);
		// ps.flush();
		// System.setOut(ps);
	}

	public void redirectSystemErr() {
		PrintStream ps = new PrintStream(errs);
		System.setErr(ps);
	}

	public String getOut() {
		// Out.
		return record.toString();
	}

	public String getErr() {
		System.err.flush();
		return errs.toString();
	}

	public void getBackOut() {
		Out.setOutputStream(out);
		record.reset();
	}

	public void getBackErr() {
		System.setErr(err);
		errs.reset();
	}

	public boolean find(String patternStr, String base) {
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(base);
		return matcher.find();
	}

	public TestJFlex() {
		configurations = new String[][] { { "%public" }, { "%apiprivate" },
				{ "%cup" }, { "%caseless" }, { "%char" }, { "%line" },
				{ "%column" }, { "notunix" }, { "yyeof" },
				{ "7bit", "8bit", "16bit" }, { "%switch", "table" } };

		allConfigurations = new ArrayList<String>();
		for (String[] strs : configurations)
			for (String s : strs)
				allConfigurations.add(s);

		outputRecord = new OutputSet();

		out = System.out;
		err = System.err;
		record = new ByteArrayOutputStream();
		errs = new ByteArrayOutputStream();
	}

	public String test(int hasReturn, int normal, int[] options) {
		List<String> ops = new ArrayList<String>();
		for (int i = 0; i < options.length; i++) {
			int op = options[i];
			if (op != 0) {
				String opStr = configurations[i][op - 1];
				ops.add(opStr);
			}
		}

		String str = OutputSet.PASS;

		try {
			if (hasReturn == 0) {
				ex(ops, "Bug.jflex", "Bug_options.jflex");

			} else {
				ex(ops, "Bug_add.jflex", "Bug_add_options.jflex");
			}

			if (normal == 0) {
				ex2(ops, "State.jflex", "State_options.jflex");

			} else {
				ex2(ops, "State_normal.jflex", "State_normal_options.jflex");
			}

			this.outputRecord.pendingStr(OutputSet.PASS);

		} catch (Throwable t) {
			Writer writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			t.printStackTrace(printWriter);
			String s = writer.toString();
			this.outputRecord.pendingStr(s);
			str = s;
		}

		this.getBackOut();
		return str;
	}

	private void ex2(List<String> ops, String originalfile, String DirectFile)
			throws Throwable {
		redirectSystemOut();

		Setup setup = new Setup(originalfile, DirectFile);
		setup.removeAll(allConfigurations);
		setup.appendAll(ops);
		// JFlex.Out
		Main.generate(new String[] { DirectFile });

		String s = getOut();
		this.getBackOut();
		if (s.indexOf("Warning : Lexical states <YYINITIAL> and <STATE2> are equivalent.") >= 0) {
			throw new Exception(s);
		}

	}

	private void ex(List<String> ops, String originalfile, String DirectFile)
			throws Throwable {
		redirectSystemErr();
		try {
			Setup setup = new Setup(originalfile, DirectFile);
			setup.removeAll(allConfigurations);
			setup.appendAll(ops);
			Main.generate(new String[] { DirectFile });
		} catch (Throwable t) {
			String s = this.getErr();
			this.getBackErr();
			if (s.indexOf("java.lang.NullPointerException") >= 0) {
				throw new Exception(s);
			}
		}
		this.getBackErr();

	}

	public String test(int[] set) {
		int hasReturn = set[0];
		int normal = set[1];
		int[] options = Arrays.copyOfRange(set, 2, set.length);
		return this.test(hasReturn, normal, options);
	}

	public void testBench(int[] set, int[] part, int index, BufferedWriter bw) {
		int nextIndex = index + 1;
		for (int i = 0; i < set[index]; i++) {
			int[] partCur = new int[set.length];
			System.arraycopy(part, 0, partCur, 0, set.length);
			partCur[index] = i;
			if (nextIndex == set.length) {
				String str;
				try {
					str = test(partCur);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Writer writer = new StringWriter();
					PrintWriter printWriter = new PrintWriter(writer);
					e.printStackTrace(printWriter);
					String s = writer.toString();
					this.outputRecord.pendingStr(s);
					str = s;
				}

				try {

					for (int op : partCur)
						bw.write(op + " ");
					bw.write(":");
					bw.write(this.outputRecord.get(str) + " ");
					bw.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				testBench(set, partCur, nextIndex, bw);
			}
		}
	}

	public void showResult() {
		try {
			FileWriter fw = new FileWriter("bugInfo.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			for (int i = 0; i < this.outputRecord.getCurIndex(); i++) {
				bw.write(i + " : " + this.outputRecord.get(i));
				bw.newLine();
			}
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] argv) {
		TestJFlex tj = new TestJFlex();
		int[] set = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 4, 3 };
		int[] part = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		try {
			FileWriter fw = new FileWriter("result_of_testCase.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			try {
				tj.testBench(set, part, 0, bw);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bw.flush();
			bw.close();
			fw.close();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		tj.showResult();

		// tj.testBench(set, part, 0);
	}
}
