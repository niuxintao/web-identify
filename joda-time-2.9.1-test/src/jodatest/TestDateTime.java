package jodatest;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TestDateTime {

	public static void main(String[] argv) {
		
		DateTime dateTime = new DateTime(1455993437373l);
		DateTime date = dateTime.plusMonths(2147483647);
		date.getDayOfYear();

		DateTimeFormatter f = DateTimeFormat.forPattern("ZZZ");
		// Test the aliases
		f.parseDateTime("Etc/GMT+0");
		f.parseDateTime("Etc/UTC");
		f.parseDateTime("Greenwich");
		// Test the canonical id
		f.parseDateTime("Etc/GMT"); // <-- This fails
	}
}