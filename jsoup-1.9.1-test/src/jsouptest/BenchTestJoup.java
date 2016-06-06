package jsouptest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import output.BenchExecute;

public class BenchTestJoup extends BenchExecute {
	public BenchTestJoup(){
		super();
	}
	
	public String test(int[] set) {
		Jstest gt = new Jstest();
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
	 *   selectOrNot 2
	 *   ParseOrNot 2
	 *   CheckOrNot 2
	 *   CharSet 6
	 *   xmlOrHmtl 2
	 *   divOrTitle 2
	 *   unbanlancedOrNot 2
	 * @param args
	 */
	
	public static void main(String[] args){
		BenchTestJoup tj = new BenchTestJoup();
		int[] param = new int[] { 2, 2, 2, 6, 2, 2, 2 };
		tj.bench(param);
		tj.showResult();
	}
}
