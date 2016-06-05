package jodatest;

import java.util.Locale;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;

import output.OutputSet;

public class TestDateTime {
	final String[] datetimezoneId = { "Europe/Berlin", "Australia/Melbourne",
			"America/North_Dakota/Center", "Asia/Hong_Kong" };

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

	public boolean addDaysError(int dateTimeZone, boolean addDayOrNot,
			boolean addHourOrNot) {
		final MutableDateTime mdt = new MutableDateTime(2011, 10, 30, 3, 0, 0,
				0, DateTimeZone.forID(datetimezoneId[dateTimeZone]));
		// System.out.println("Start date:   " + mdt + " ("
		// + mdt.toInstant().getMillis() + ")");
		mdt.addHours(-1);
		// System.out.println("addHours(-1): " + mdt + " ("
		// + mdt.toInstant().getMillis() + ")");

		long result = mdt.toInstant().getMillis();

		if (addHourOrNot)
			mdt.addHours(0);

		if (addDayOrNot)
			mdt.addDays(0);

		return mdt.toInstant().getMillis() == result;
	}

	public boolean testMultipleError(boolean haveMinusAndPlus, int plusLocation) {

		String result = "";

		if (haveMinusAndPlus) {
			// 2013-01-01T00:00:00.000+05:30 (I expected an error, but this
			// parsed
			// correctly) (case 4)
			result = DateTimeFormat.forPattern("MM-yyyy-dd")
					.parseDateTime("01-+2013-01").toString();
		} else {
			if (plusLocation == 0)
				result = DateTimeFormat.forPattern("yyyyMMdd")
						.parseDateTime("20130101").toString();
			// 2013-01-01T00:00:00.000+05:30 (Expected) (case 1)

			if (plusLocation == 1)
				result = DateTimeFormat.forPattern("yyyyMMdd")
						.parseDateTime("+20130101").toString();
			// 20130-10-01T00:00:00.000+05:30 (??? Notice that month changed to
			// 10
			// also) (case 2) error

			if (plusLocation == 3)
				result = DateTimeFormat.forPattern("MMyyyydd")
						.parseDateTime("01+201301").toString();
			// 20130-01-01T00:00:00.000+05:30 (??? At least month is fine this
			// time)
			// (case 3) error
		}

		return result.contains("2013-01-01");
	}

	public String test(int[] set) throws Exception {
		nonsenseParamters(set[0], set[1], set[2] == 0 ? false : true,
				set[3] == 0 ? false : true);

		if (!addDaysError(set[4], set[5] == 0 ? false : true,
				set[6] == 0 ? false : true)) {
			throw new Exception("date format is not correct !");
		}

		if (!testMultipleError(set[6] == 0 ? false : true, set[7])) {
			throw new Exception("time millions is not correct!");
		}
		
		return OutputSet.PASS;
	}

	public static void main(String[] argv) {

	}
}