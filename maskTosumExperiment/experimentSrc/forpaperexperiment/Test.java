package forpaperexperiment;

import maskSimulateExperiment.Experiment_FB_OUR;
import maskSimulateExperiment.RandomAndILPExperiment;
import maskSimulateExperiment.TraditionalExperiment;


// ֻ�к�����ʵ������������� ǰ��û��
public class Test {
//	asd
	//asdsad
	public static void main(String[] args){
		TraditionalExperiment te = new TraditionalExperiment();
		RandomAndILPExperiment re = new RandomAndILPExperiment();
		Experiment_FB_OUR ee = new Experiment_FB_OUR();
		te.conductTest(0, 3);
		re.conductTest(0, 3);
		ee.conductTest(0, 3, 2);
		ee.conductTest(0, 3, 3);
		ee.conductTest(0, 3, 4);
	}
}
