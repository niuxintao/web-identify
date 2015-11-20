package forpaperexperiment;

import maskSimulateExperiment.Experiment_FB_OUR;
import maskSimulateExperiment.RandomAndILPExperiment;
import maskSimulateExperiment.TraditionalExperiment;


// 只有后两个实验有随机变量， 前面没有
public class Test {
//	asd
	//asdsad
	public static void main(String[] args){
		TraditionalExperiment te = new TraditionalExperiment();
		RandomAndILPExperiment re = new RandomAndILPExperiment();
		Experiment_FB_OUR ee = new Experiment_FB_OUR();
		te.conductTest(14, 15);
		re.conductTest(14, 15);
		ee.conductTest(14, 15, 2);
	}
}
