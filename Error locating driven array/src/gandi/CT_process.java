package gandi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

public interface CT_process {

	public abstract int[] getCoveredMark();

	public abstract HashSet<TestCase> getOverallTestCases();

	public abstract HashSet<Tuple> getMFS();

	public abstract void run();

	public abstract int[] getT_tested_coveredMark();

	public abstract void evaluate(List<Tuple> actualMFS);

	public abstract long getTimeAll();

	public abstract long getTimeIden();

	public abstract long getTimeGen();

	public abstract int getMultipleMFS();

	public abstract double getPrecise();

	public abstract double getRecall();

	public abstract double getF_measure();

	public abstract int getT_tested_covered();

	public abstract HashSet<TestCase> getRegularCTCases();

	public abstract HashSet<TestCase> getIdentifyCases();

	HashMap<Integer, Integer> getCoveredNums();

	HashMap<Tuple, Integer> getRealIdentify();

}