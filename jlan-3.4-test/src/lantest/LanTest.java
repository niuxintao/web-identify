package lantest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import output.OutputSet;

public class LanTest {

	int[] startIndexs = { -1, 0, 1 };

	String[] numbers = { "81.5514", "81", "81.0" };

	public void nonsenseParamters(boolean isEmpty, boolean valueToFind,
			int startIndex) {
		boolean[] array = null;
		if (isEmpty)
			array = new boolean[0];
		else {
			array = new boolean[2];
			array[0] = true;
			array[1] = false;
		}
		if (ArrayUtils.isEmpty(array)) {
			return;
		}

		int start = startIndexs[startIndex];
		if (start < 0) {
			start = 0;
		}
		for (int i = start; i < array.length; i++) {
			if (valueToFind == array[i]) {
				return;
			}
		}
		return;
	}

	public void testTrail(int isMutiple, int num) throws Exception {

		String number = numbers[num];
		if (isMutiple == 0)
			number = number + "DD";
		else if (isMutiple == 1)
			number = number + "D";

		boolean isNumber = NumberUtils.isNumber(number); // returns false
		boolean result = false;

		try {
			NumberUtils.createNumber(number); // returns
			if (isNumber == true)
				result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (isNumber == false)
				result = true;
			else
				result = false;
		}

		if (!result) {
			throw new Exception(
					"it cause a createNumber exception, which is determiend to be a number");
		}
	}

	public void testAccurancyLoss(boolean isGreaterThanSeven, boolean hasDots) throws Exception {
		String number = "";
		if (hasDots) {
			if (isGreaterThanSeven)
				number = "193343.82";
			else
				number = "1.1";
		} else {
			if (isGreaterThanSeven)
				number = "19334382";
			else
				number = "11";
		}

		if(!number.equals(NumberUtils.createNumber(number)
				.toString()))
			throw new Exception("number created has accurancy loss");
	}

	public String test(int[] set) throws Exception {
		nonsenseParamters(set[0] == 0 ? false : true, set[1] == 0 ? false
				: true, set[2]);

		testTrail(set[3], set[4]);

		testAccurancyLoss(set[5] == 0 ? false : true, set[6] == 0 ? false
				: true);

		return OutputSet.PASS;
	}

	public static void main(String[] args) throws Exception {
		LanTest lt = new LanTest();

		lt.testAccurancyLoss(true, true);

		lt.testTrail(0, 2);
	}
}
