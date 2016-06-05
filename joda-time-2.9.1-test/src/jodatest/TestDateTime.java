package jodatest;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import output.OutputSet;

public class TestDateTime {
	
	
	final long[] startTime = {0, 5000, 1455993437373l};
	
	final int[] addTime = {0, 5000, 2147483647};
	
	final String[] format = {"Etc/GMT+0", "Etc/UTC","Greenwich","Etc/GMT" };
	
//	final String[] pattern = {"Z", "MMMM, yyyy", "ZZZ"};
	
	
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
	
	public void testArrayIndexOutError(int start, int added){
		DateTime dateTime = new DateTime(startTime[start]);
		DateTime date = dateTime.plusMonths(addTime[added]);
		date.getDayOfYear();
	}
	
	public void testFormat(int fom){

		DateTimeFormatter f = DateTimeFormat.forPattern("ZZZ");
		// Test the aliases
		f.parseDateTime(format[fom]);
	}
	
	
	public String test(int[] set) throws Exception {
		nonsenseParamters(set[0], set[1], set[2] == 0 ? false : true,
				set[3] == 0 ? false : true);

		testArrayIndexOutError(set[4], set[5]);

		testFormat(set[6]);

		return OutputSet.PASS;
	}
	

	public static void main(String[] argv) {
		TestDateTime td = new TestDateTime();
		td.testFormat(1);

	}
}