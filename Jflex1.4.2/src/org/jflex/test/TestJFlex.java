package org.jflex.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import JFlex.Main;

public class TestJFlex {
	public String[][] configurations;
	private OutputSet outputRecord;

	public List<String> allConfigurations;

	public TestJFlex() {
		// String[] a = new String[] { "%public", "%apiprivate", "%cup",
		// "%caseless", "%char", "%line", "%column" };

		configurations = new String[][] { { "%public" }, { "%apiprivate" },
				{ "%cup" }, { "%caseless" }, { "%char" }, { "%line" },
				{ "%column" }, { "notunix" }, { "yyeof" },
				{ "7bit", "8bit", "16bit" }, { "%switch", "table" } };

		allConfigurations = new ArrayList<String>();
		for (String[] strs : configurations)
			for (String s : strs)
				allConfigurations.add(s);

		outputRecord = new OutputSet();
	}

	public String test(int lookAhead, int standalone, int type, int[] options) {
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
			if (lookAhead != 1) {
				Setup setup = new Setup("Bug2_remove.jflex",
						"Bug2_remove_options.jflex");
				setup.removeAll(allConfigurations);
				setup.appendAll(ops);

				Main.generate(new String[] { "Bug2_remove_options.jflex" });
			} else {
				Setup setup = new Setup("Bug2.jflex", "Bug2_options.jflex");
				setup.removeAll(allConfigurations);
				setup.appendAll(ops);
				Main.generate(new String[] { "Bug2_options.jflex" });
			}

			if (type == 1) {
				Setup setup = new Setup("testF.jflex", "testF_options.jflex");
				setup.removeAll(allConfigurations);
				setup.appendAll(ops);

				Main.generate(new String[] { "testF_options.jflex" });
			} else {
				Setup setup = new Setup("remove_type.jflex",
						"remove_type_options.jflex");
				setup.removeAll(allConfigurations);
				setup.appendAll(ops);

				Main.generate(new String[] { "remove_type_options.jflex" });
			}

			if (standalone == 0) {
				Setup setupFind = new Setup("Scanner.java", "");
				if (setupFind.find("Foo"))
					throw new Exception("unexpected return type: Foo!");
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

		return str;
	}

	public String test(int[] set) {
		int lookAhead = set[0];
		int standalone = set[1];
		int type = set[2];
		int[] options = Arrays.copyOfRange(set, 3, set.length);
		return this.test(lookAhead, standalone, type, options);
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
		int[] set = new int[] { 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 4, 3 };
		int[] part = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
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
