package lantest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import output.BenchExecute;

public class BenchTestLan extends BenchExecute {
	public BenchTestLan(){
		super();
	}
	
	public String test(int[] set) {
		LanTest gt = new LanTest();
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
	 *   isEmpty 2
	 *   valueToFind 2
	 *   startIndex 3
	 *   DefaultDiffBuilderOrNot 2
	 *   hasChild 2
	 *   childID 3
	 *   negativeOrNot 2
	 *   zeroOrNot 2
	 *   
	 * @param args
	 */
	
	public static void main(String[] args){
		BenchTestLan tj = new BenchTestLan();
		int[] param = new int[] { 2, 2, 3, 2, 2, 3, 2, 2 };
		tj.bench(param);
		tj.showResult();
	}
}
