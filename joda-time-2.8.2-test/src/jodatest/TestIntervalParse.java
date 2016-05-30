package jodatest;

import org.joda.time.DateTime;
import org.joda.time.Interval;

public class TestIntervalParse {

    public static void main(String[] argv) {
    	DateTime.parse(new DateTime(Long.valueOf(Long.MAX_VALUE)).toString());
        
    	
    	
    	long days = 60;
        String   intervalS = "2015-09-15T12:00:00Z/P"+days+"D";
        Interval intervalP = Interval.parse(intervalS);

        System.out.println("intervalS="+intervalS+".");
        System.out.println("intervalP="+intervalP+".");
        System.out.println("Expected duration: "+(days*24*60*60*1000)+".");
        System.out.println("Actual   duration: "+intervalP.toDurationMillis()+".");
    }
}