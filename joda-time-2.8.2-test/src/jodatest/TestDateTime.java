package jodatest;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import output.OutputSet;

public class TestDateTime {

	final int[] startMonth = { 1, 5, 6, 7, 9 };

	final int[] longDay = { 1, 2, 60 };

	final Locale[] locales = { Locale.CHINA, Locale.US, Locale.GERMAN };

	final LocalDate[] dateOfBirth = { LocalDate.parse("1990-2-12"),
			LocalDate.parse("1990-3-12") };

	public void nonsenseParamters(int locale, int date, boolean addYearOrNot,
			boolean addMonthOrNot) {
		Locale local = locales[locale];
		LocalDate dateTime = dateOfBirth[date];
		if (addYearOrNot)
			dateTime.plusYears(1);
		if (addMonthOrNot)
			dateTime.plusMonths(1);
		dateTime.monthOfYear().getAsText(local);
	}

	public void testParse(boolean LongOrNot, boolean MaxOrNot) throws Exception {

		if (LongOrNot) {
			if (MaxOrNot)
				DateTime.parse(new DateTime(Long.valueOf(Long.MAX_VALUE))
						.toString());
			else
				DateTime.parse(new DateTime(Long.valueOf(Long
						.valueOf("1000000"))).toString());
		} else
			DateTime.parse(new DateTime(Integer.MAX_VALUE).toString());
	}

	public void testInterval(int start, int ld) throws Exception {
		String intervalS = "2015-0" + startMonth[start] + "-15T12:00:00Z/P"
				+ longDay[ld] + "D";
		Interval intervalP = Interval.parse(intervalS);

		// System.out.println("intervalS=" + intervalS + ".");
		// System.out.println("intervalP=" + intervalP + ".");
		if (longDay[ld] * 24 * 60 * 60 * 1000 != intervalP.toDurationMillis())
			throw new Exception("Time duration is not as expected !");

	}

	public String test(int[] set) throws Exception {
		nonsenseParamters(set[0], set[1], set[2] == 0 ? false : true,
				set[3] == 0 ? false : true);

		testParse(set[4] == 0 ? false : true, set[5] == 0 ? false : true);

		testInterval(set[6], set[7]);

		return OutputSet.PASS;
	}

	public static void main(String[] argv) {
		TestDateTime td = new TestDateTime();
		try {
			td.testInterval(0, 1);
			td.testParse(false, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}