package clitest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import output.BenchExecute;

public class BenchTestCLI extends BenchExecute {
	public BenchTestCLI(){
		super();
	}
	
	public String test(int[] set) {
		CLI_Test gt = new CLI_Test();
		String s;
		try {
			s = gt.test(set);
		} catch (Throwable t) {
			// TODO Auto-generated catch block
			Writer writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			t.printStackTrace(printWriter);
			s = writer.toString();
		}
		return s;
	}
	
	/**
	 * 
	 *   argMutiple 2
	 *   optionMutiple 2
	 *   getArgOrNot 2
	 *   separteAddedOrNot 2
	 *   singleCmdOrNot 2
	 *   BasicPareseOrNot 2
	 *   addtionalSpace 2
	 *   doubleQuotes 2
	 * @param args
	 */
	
	public static void main(String[] args){
		BenchTestCLI tj = new BenchTestCLI();
		int[] param = new int[] { 2, 2, 2, 2, 2, 2, 2, 2 };
		tj.bench(param);
		tj.showResult();
	}
}
