package test;

import static org.junit.Assert.assertEquals;
import sample.MyClass;

import org.junit.Test;
public class TestMyClass {
	
	@Test
	public void mutiplicationOfZeroIntegersShouldReturnZero() {
		// MyClass is tested
		MyClass tester = new MyClass();

		// Tests
		assertEquals("10 x 0 must be 0", 0, tester.multiply(10, 0));
		assertEquals("0 x 10 must be 0", 0, tester.multiply(0, 10));
		assertEquals("0 x 0 must be 0", 0, tester.multiply(0, 0));
	}
}
