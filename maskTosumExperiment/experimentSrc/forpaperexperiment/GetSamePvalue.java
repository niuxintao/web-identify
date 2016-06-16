package forpaperexperiment;

import java.util.ArrayList;
import java.util.List;

//import org.apache.commons.math3.stat.inference.OneWayAnova;
import org.apache.commons.math3.stat.inference.TTest;

public class GetSamePvalue {
	public static void main(String[] str){
//		OneWayAnova anova = new OneWayAnova();
		TTest ttest = new TTest();
		double[] a = {1, 1 ,1 ,1 ,1 ,1, 1};
		double b = 0;
		double c = 1;
		double[] d = {1, 1 ,1 ,1 ,1 ,1, 1};
		double[] e = {0, 0 ,0 ,0 ,0 ,0, 0};
		
		List<double[]> ad = new ArrayList<double[]> ();
		ad.add(a);ad.add(d);
		
		List<double[]> ae = new ArrayList<double[]> ();
		ae.add(a);ae.add(e);
		
		System.out.println("b, a : " + ttest.tTest(b, a) + " "+ ttest.tTest(b, a, 0.05));
		System.out.println("c, a : " + ttest.tTest(c, a) + " "+ ttest.tTest(c, a, 0.05));
		System.out.println("d, a : " + ttest.tTest(d, a) + " "+ ttest.tTest(d, a, 0.05));
		System.out.println("e, a : " + ttest.tTest(e, a) + " "+ ttest.tTest(e, a, 0.05));
		
//		System.out.println("d, a : " + anova.anovaFValue(ad) + " "+ anova.anovaTest(ad, 0.05));
		
//		System.out.println("e, a : " + anova.anovaFValue(ae) + " "+ anova.anovaTest(ae, 0.05));
	}
}
