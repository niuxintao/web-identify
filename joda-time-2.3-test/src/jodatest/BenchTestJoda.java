package jodatest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import output.BenchExecute;

public class BenchTestJoda extends BenchExecute {
	public BenchTestJoda(){
		super();
	}
	
	public String test(int[] set) {
		TestDateTime gt = new TestDateTime();
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
	 *   locale 3
	 *   local_date 2
	 *   addYear 2
	 *   addMonth 2
	 *   dateTimeZone 4
	 *   addDayOrNot 2
	 *   addHourOrNot 2
	 *   haveMinusAndPlus 2
	 *   plusLocation 3
	 * 
	 * 
	 * @param args
	 */
	
	public static void main(String[] args){
		BenchTestJoda tj = new BenchTestJoda();
		int[] param = new int[] { 3, 2, 2, 2, 4, 2, 2, 2, 3 };
		tj.bench(param);
		tj.showResult();
	}
}
