package runGrep;

public class GrepTestCase {
	public static String[] options = { "-v", "-r", "-i", "\b","\\<" };

	/**
	 * should output as :
	 * 
	 * xxx
	 * 
	 * bug output as (grep 2.12)
	 * 
	 * xxx 
	 * xxxä
	 * 
	 * note that this also effect  \b, \<, \> and \w. 
	 * 
	 * @return
	 */
	public String testBug_37600() {
		String cmd = "echo -e 'xxx\nxxxä\nxxxx' |grep -w xxx";
		return CMD.execute(cmd);
	}
	
	/**
	 * for assici word there is no such error
	 * 
	 * should output Это просто текст but output nothing (grep 2.6.3)
	 * 
	 * without \< \> is also right
	 * 
	 * @return
	 */
	public String testBug_29537() {
		String cmd = "echo 'Это просто текст' | grep '\\<просто\\>'";
		return CMD.execute(cmd);
	}
	
	public String testBoth7600_29537(){
		String cmd = "echo 'Это просто текст' | grep '\\<просто\\>'";
		return CMD.execute(cmd);
	}

	
	

	/**
	 * the only matching is true true true true true the count should be 5
	 * 
	 * but it showed the 3 (which is the original line number) grep any version
	 * up-to-now (2.23)
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
	 * -B is correct "echo -e 'attention\nthis is the first\nwhile this looks like the second \nand this smells of third\nattention \nfourth' | grep  -m 1 -B 5 fourth 
"
	 * 
	 * http://savannah.gnu.org/bugs/?28588 grep 2.5.4, in fact, up to now, it is still wrong, grep 2.23
	 * 
	 * @return
	 */
	public String testBug_28588() {
		String cmd = "echo -e 'attention\nthis is the first\nwhile this looks like the second \nand this smells of third\nattention \nfourth' | grep  -m 1 -A 5 attention ";
		
//		String cmd = "echo -e 'attention\nthis is the first\nwhile this looks like the second \nand this smells of third\nattention \nfourth' | grep  -m 1 -C 5 attention ";
		return CMD.execute(cmd);
	}
	
	public String testBoth33080_28588(){
		String cmd = "echo 'Это просто текст' | grep '\\<просто\\>'";
		return CMD.execute(cmd);
	}
}
