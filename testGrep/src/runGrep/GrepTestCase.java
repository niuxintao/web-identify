package runGrep;

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
	 * but output nothing (no result)
	 * (grep 2.6.3)
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
	 * this is the test case which can trigger both the two errors 7600 and 29537
	 * 
	 * When accic word is enabled for the first grep ('\\<xxxä\\>' ), the next grep -w will not show anything so the second error will be masked.
	 * 
	
	 * so, the MFS should be (non-assic)  which have higher priority than (-w  assic ) (\< \>  assic)
	 * 
	 * parameter   -V --help   assci        -w      assci      -E   -i
 	 * 						   non-assic    <\ \>   non-assic 
	 * 					                    --
	 * 
	 * @return
	 */
	public String testBoth7600_29537() {
		String cmd = "echo -e 'xxx\nxxxä\nxxxxЭто просто текст' | grep -B 3 'Это просто текст' |grep -w xxx ";
		
		// without non-assicia 1  word "echo -e 'xxx\nxxxä\nxxxxaa' | grep 'aa' |grep -w xxx ";
		
		// second assic is as "echo -e 'xxx\nxxx\nxxxxЭто просто текст' | grep 'Это просто текст' |grep -w xxx ";
		
		return CMD.execute(cmd);
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
	 * in effct -C 5 is also wrong
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
	 * this is the test case which can trigger both the two errors 33080 and 28588
	 * 
	 * When -A 5 is enabled, the only-matching and counting is correct. in fact, it should be wrong, when only-matching and counting
	 * 
	 * so, the MFS should be (-A, 5) (-C, 5), which have higher priority than (--only matching and --count)
	 * 
	 * parameter   -V --help  -A  1  --only-matching  --count  -E  -i
	 *		      			  -C  2
	 *						  -B  3
	 * 							  5
	 * @return
	 */
	public String testBoth33080_28588() {
		String cmd = "echo -e 'attention true false \nthis is the first false true\nwhile this looks like the second false\nand this smells of third \nattention \nfourth true false true' | grep  -m 1 -A 5 attention | grep  --only-matching --count true " ;

		// String cmd = "echo -e 'attention\nthis is the first\nwhile this looks
		// like the second \nand this smells of third\nattention \nfourth' |
		// grep -m 1 -C 5 attention ";
		return CMD.execute(cmd);
	}
}
