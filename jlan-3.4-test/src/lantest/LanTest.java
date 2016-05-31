package lantest;

import org.apache.commons.lang3.math.NumberUtils;

public class LanTest {

//	public void testIsNumber(boolean isDig) {
//		String a = "";
//		if (isDig)
//			a = "81.5514";
//		else
//			a = "81.5514";
//		System.out.println(NumberUtils.isNumber(a));
//	}

	public void testTrail(int isMutiple) {

		String number = "";
		if (isMutiple == 0)
			number = "81.5514DD";
		else if (isMutiple == 1)
			number = "81.5514D";
		else
			number = "81.5514";

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

		System.out.println(result);
	}

	public void testAccurancyLoss(boolean isGreaterThanSeven) {
		String number = "";
		if (isGreaterThanSeven)
			number = "193343.82";
		else
			number = "1.1";

		System.out.println(number.equals(NumberUtils.createNumber(number)
				.toString()));
	}

	public static void main(String[] args) {
		LanTest lt = new LanTest();

		lt.testAccurancyLoss(true);

		lt.testTrail(2);
	}
}
