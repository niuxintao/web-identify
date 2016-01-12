package runGrep;


import output.OutputSet;

public class GrepTestCase {
	/**
	 * -V will mask every thing. --help will mask everything except -V
	 */
	public static String[] options = { "-V", "--help", "-i", "\b", "\\<" };

	/**
	 * should output as :
	 * 
	 * xxx
	 * 
	 * bug output as (grep 2.12)
	 * 
	 * xxx xxxä
	 * 
	 * note that this also effect \b, \<, \> and \w.
	 * 
	 * http://savannah.gnu.org/bugs/?37600
	 * 
	 * @return
	 */
	public String testBug_37600() {
		String cmd = "echo -e 'xxx\nxxxä\nxxxx' |grep -w xxx";
		return CMD.execute(cmd);
	}

	/**
	 * for assic word there is no such error
	 * 
	 * should output
	 * 
	 * Это просто текст
	 * 
	 * but output nothing (no result) (grep 2.6.3)
	 * 
	 * 
	 * 
	 * without \< \> is also right
	 * 
	 * http://savannah.gnu.org/bugs/?29537
	 * 
	 * @return
	 */
	public String testBug_29537() {
		String cmd = "echo 'Это просто текст' | grep '\\<просто\\>'";
		return CMD.execute(cmd);
	}

	/**
	 * 
	 * this is the test case which can trigger both the two errors 7600 and
	 * 29537
	 * 
	 * When accic word is enabled for the first grep ('\\<xxxä\\>' ), the next
	 * grep -w will not show anything so the second error will be masked.
	 * 
	 * 
	 * so, the MFS should be (non-assic) which have higher priority than (-w
	 * non-assic ) (\< \> assic)
	 * 
	 * parameter -V assci sed -w assci -E -i --color non-assic grep <\ \>
	 * non-assic --- \b --
	 * 
	 * 
	 * @return
	 */
	public String testBoth7600_29537() {
		String cmd = "echo -e 'xxx\nxxxä\nxxxxЭто просто текст' | grep -B 3 'Это просто текст' |grep -w xxx ";

		// without non-assicia 1 word "echo -e 'xxx\nxxxä\nxxxxaa' | grep 'aa'
		// |grep -w xxx ";

		// second assic is as "echo -e 'xxx\nxxx\nxxxxЭто просто текст' | grep
		// 'Это просто текст' |grep -w xxx ";

		return CMD.execute(cmd);
	}

	/**
	 * so, the MFS should be (non-assic) which have higher priority than (-w
	 * non-assic ) (\< \> assic)
	 * 
	 * parameter -V assci sed -w assci -E -i --color non-assic grep <\ \>
	 * non-assic --- \b --
	 * 
	 * 
	 * @return
	 * @throws Exception
	 */

	public String testBoth7600_29537(int[] set) throws Exception {

		String[] help = { "-V ", "--color ", "" };
		String[] firstAssic = { "", "ä" };
		String[] secondAssic = { "aa", "Это просто текст" };
		String[] sedorgrep = { "sed s/", "grep " };
		String[] E = { " -E", "" };
		String[] i = { " -i", "" };

		String s = "echo ";

		s += " -e 'xxx\nxxx";

		s += firstAssic[set[1]];

		s += "\nxxxx";

		s += "' | grep ";

		s += help[set[0]];

		s += E[set[5]];
		s += i[set[6]];
		
		if (set[3] == 0)
			s += " -w xxx ";
		else if (set[3] == 1)
			s += " '\\bxxx\\b' ";
		else if (set[3] == 2)
			s += " '\\<xxx\\>' ";
		else 
			s += " xxx ";

		String first = CMD.execute(s);

		if (!first.contains("xxx"))
			throw new Exception(first);
		
		if (first.contains("ä")) {
			throw new Exception("SHOULD NOT PRINT THE NON-ASSIC CHARACTER ä");

		}

		String cmd = "echo '";

		cmd += secondAssic[set[4]];

		cmd += "' | ";

		cmd += sedorgrep[set[2]];

		cmd += "'\\<";

		cmd += secondAssic[set[4]];

		cmd += "\\>'";
		
		if(set[2] == 0){
			cmd += "/'";
			cmd += secondAssic[set[4]];
			cmd += "'/";
		}

		String second = CMD.execute(cmd);

		if (!second.contains(secondAssic[set[4]])) {
//			if(set[2] == 0)
//				System.out.println("second " + second );
			throw new Exception("cannot " +sedorgrep[set[2]] + " word " + secondAssic[set[4]]);
		}

		return OutputSet.PASS;

	}
	
	public static void main(String[] args) {
		String s = "echo 'Это просто простой текст' | sed s/'\\<просто\\>'/'просто'/";
		System.out.println(CMD.execute(s));
	}

	/**
	 * the only matching is true true true true true the count should be 5
	 * 
	 * the incompatitable of --only-matching and --count
	 * 
	 * but it showed the 3 (which is the original line number) grep any version
	 * up-to-now (2.23)
	 * 
	 * http://savannah.gnu.org/bugs/?33080
	 * 
	 * @return
	 */
	public String testBug_33080() {
		String cmd = "echo -e 'true true false\nfalse true\nfalse\ntrue false true' | grep  --only-matching --count true";
		return CMD.execute(cmd);
	}

	/**
	 * 
	 * -A 1,2,3 is all right, but 5 is wrong
	 * 
	 * in effect -C 5 is also wrong
	 * 
	 * -B is correct "echo -e 'attention\nthis is the first\nwhile this looks
	 * like the second \nand this smells of third\nattention \nfourth' | grep -m
	 * 1 -B 5 fourth "
	 * 
	 * http://savannah.gnu.org/bugs/?28588 grep 2.5.4, in fact, up to now, it is
	 * still wrong, grep 2.23
	 * 
	 * @return
	 */
	public String testBug_28588() {
		String cmd = "echo -e 'attention\nthis is the first\nwhile this looks like the second \nand this smells of third\nattention \nfourth' | grep  -m 1 -A 5 attention ";
		// -m -m NUM, --max-count=NUM Stop reading a file after NUM matching
		// lines.
		// -A Print NUM lines of trailing context after matching lines.
		// -B NUM, --before-context=NUM Print NUM lines of leading context
		// before matching lines.
		// -C NUM, -NUM, --context=NUM Print NUM lines of output context.

		// String cmd = "echo -e 'attention\nthis is the first\nwhile this looks
		// like the second \nand this smells of third\nattention \nfourth' |
		// grep -m 1 -C 5 attention ";
		return CMD.execute(cmd);
	}

	/**
	 * this is the test case which can trigger both the two errors 33080 and
	 * 28588
	 * 
	 * When -A 5 is enabled, the only-matching and counting is correct. in fact,
	 * it should be wrong, when only-matching and counting
	 * 
	 * so, the MFS should be (-A, 5) (-C, 5), which have higher priority than
	 * (--only matching and --count)
	 * 
	 * parameter --help -A 1 --only-matching --count -E -i --color -C 2 -- -B 3
	 * 5
	 * 
	 * @return
	 */
	public String testBoth33080_28588() {
		String cmd = "echo -e 'attention true false \nthis is the first false true\nwhile this looks like the second false\nand this smells of third \nattention \nfourth true false true' | grep  -m 1 -A 5 attention | grep  --only-matching --count true ";

		// String cmd = "echo -e 'attention\nthis is the first\nwhile this looks
		// like the second \nand this smells of third\nattention \nfourth' |
		// grep -m 1 -C 5 attention ";
		return CMD.execute(cmd);
	}

	/**
	 * parameter --help -A 1 --only-matching --count -E -i --color -C 2 -- -B 3
	 * 5
	 * 
	 * @return
	 * @throws Exception
	 */
	public String testBoth33080_28588(int[] set) throws Exception {
		String[] help = { "-V", "--color", "" };
		String[] AB = { " -A", " -C", " -B" };
		String[] third = { " 1", " 2", " 3", " 5" };

		String[] only = { " --only-matching ", "" };
		String[] count = { " --count ", "" };

		String[] E = { " -E", "" };
		String[] i = { " -i", "" };

		String s = "echo -e 'attention\nthis is the first\nwhile this looks like the second \nand this smells of third\nattention \nfourth' | grep ";

		s += help[set[0]];

		s += " -m 1 ";

		s += AB[set[1]];

		s += third[set[2]];

		s += " attention";

		String first = CMD.execute(s);

		if (!first.contains("attention"))
			throw new Exception(first);

		if (set[1] != 2) {
			String[] str1 = first.split("\n");
			if (set[2] == 0) {
				if (str1.length != 2)
					throw new Exception("NUMBER OF LINES ERROR");
			} else if (set[2] == 1) {
				if (str1.length != 3)
					throw new Exception("NUMBER OF LINES ERROR");
			} else if (set[2] == 2) {
				if (str1.length != 4)
					throw new Exception("NUMBER OF LINES ERROR");

			} else if (set[2] == 3) {
				if (str1.length != 6)
					throw new Exception("NUMBER OF LINES ERROR");
			}
		}

		String cmd = "echo -e 'true true false\nfalse true\nfalse\ntrue false true' | grep ";
		cmd += E[set[5]];
		cmd += i[set[6]];
		cmd += only[set[3]];
		cmd += count[set[4]];
		cmd += " true ";
		String second = CMD.execute(cmd);

		if (set[4] == 0) {
			if (set[3] == 0) {
				if (!second.contains("5"))
					throw new Exception("SHOULD BE 5 BUT " + second);
			} else {
				if (!second.contains("3"))
					throw new Exception("SHOULD BE 3 BUT " + second);
			}
		}


		return OutputSet.PASS;
	}



}
