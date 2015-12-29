package forpaperexperiment;

//import maskSimulateExperiment.Experiment_FB_OUR;
import maskSimulateExperiment.RandomAndILPExperiment;
//import maskSimulateExperiment.TraditionalExperiment;


// 只有后两个实验有随机变量， 前面没有
public class Test {
//	asd
	//asdsad
	public static void main(String[] args){
//		TraditionalExperiment te = new TraditionalExperiment();
		RandomAndILPExperiment re = new RandomAndILPExperiment();
//		Experiment_FB_OUR ee = new Experiment_FB_OUR();
//		te.conductTest(10, 15);
		re.conductTest(0, 1);
//		ee.conductTest(10, 15, 2);
//		ee.conductTest(6, 7, 3);
//		ee.conductTest(6, 7, 4);
	}
}
