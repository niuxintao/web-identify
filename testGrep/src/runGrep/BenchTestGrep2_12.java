package runGrep;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import output.BenchExecute;

public class BenchTestGrep2_12 extends BenchExecute {
	public BenchTestGrep2_12(){
		super();
	}
	
	public String test(int[] set) {
		GrepTestCase gt = new GrepTestCase();
		String s;
		try {
			s = gt.testBoth7600_29537(set);
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
	 * so, the MFS should be (non-assic)  which have higher priority than (-w  non-assic ) (\< \>  assic)
	 * 
	 * parameter   -V         assci       sed    -w      assci       -E -i  
	 * 			  --color  	  non-assic   grep   <\ \>   non-assic
	 * 			  ---							 \b
	 * 										      --
	 * 					                         
	 * 
	 * @return
	 */
	
	public static void main(String[] args){
		BenchTestGrep2_12 tj = new BenchTestGrep2_12();
		int[] param = new int[] { 3, 2, 2, 4, 2, 2, 2 };
		tj.bench(param);
		tj.showResult();
	}
}
