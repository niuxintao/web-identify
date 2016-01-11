package runGrep;

import output.BenchExecute;

public class BenchTestGrep2_6_3 extends BenchExecute {
	public BenchTestGrep2_6_3(){
		super();
	}
	
	public String test(int[] set) {
		GrepTestCase gt = new GrepTestCase();
		return gt.testBoth7600_29537();
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
		BenchTestGrep2_6_3 tj = new BenchTestGrep2_6_3();
		int[] param = new int[] { 3, 2, 2, 4, 2, 2, 2 };
		tj.bench(param);
		tj.showResult();
	}
}
