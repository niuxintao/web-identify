package jodatest;

import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;

public class TestDateTime {

	public static void main(String[] argv) {

		final MutableDateTime mdt = new MutableDateTime(2011, 10, 30, 3, 0, 0,
				0, DateTimeZone.forID("Europe/Berlin"));
		System.out.println("Start date:   " + mdt + " ("
				+ mdt.toInstant().getMillis() + ")");
		mdt.addHours(-1);
		System.out.println("addHours(-1): " + mdt + " ("
				+ mdt.toInstant().getMillis() + ")");
		mdt.addHours(0);
		System.out.println("addHours(0):  " + mdt + " ("
				+ mdt.toInstant().getMillis() + ")");
		mdt.addDays(0);
		System.out.println("addDays(0):   " + mdt + " ("
				+ mdt.toInstant().getMillis() + ")"); // error

		System.out.println(DateTimeFormat.forPattern("yyyyMMdd").parseDateTime(
				"20130101"));
		// 2013-01-01T00:00:00.000+05:30 (Expected) (case 1)

		System.out.println(DateTimeFormat.forPattern("yyyyMMdd").parseDateTime(
				"+20130101"));
		// 20130-10-01T00:00:00.000+05:30 (??? Notice that month changed to 10
		// also) (case 2) error

		System.out.println(DateTimeFormat.forPattern("MMyyyydd").parseDateTime(
				"01+201301"));
		// 20130-01-01T00:00:00.000+05:30 (??? At least month is fine this time)
		// (case 3) error

		System.out.println(DateTimeFormat.forPattern("MM-yyyy-dd")
				.parseDateTime("01-+2013-01"));
		// 2013-01-01T00:00:00.000+05:30 (I expected an error, but this parsed
		// correctly) (case 4)

	}
}