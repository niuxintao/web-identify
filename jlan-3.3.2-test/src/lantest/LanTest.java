package lantest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.math.NumberUtils;

import output.OutputSet;

public class LanTest {

	int[] startIndexs = { -1, 0, 1 };

	int[] childID = { 0, 1, 2 };

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

	private static class Child {

		private Integer id;

		public Child(final Integer id) {
			super();
			this.id = id;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			Child other = (Child) obj;
			if (id == null) {
				if (other.id != null) {
					return false;
				}
			} else if (!id.equals(other.id)) {
				return false;
			}
			return true;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}

	}

	private static class Parent {

		private Child child;

		public Child getChild() {
			return child;
		}

		public void setChild(Child child) {
			this.child = child;
		}

	}

	public void testCreateNumber(boolean negativeOrNot, boolean zeroOrNot)
			throws Exception {
		String a = "";
		if (negativeOrNot) {
			a += "-";
		}
		if (zeroOrNot)
			a += "0.0";
		else
			a += "1.0";

		boolean judge = false;

		Number result = NumberUtils.createNumber(a);
		if (negativeOrNot) {
			if (result.toString().contains("-"))
				judge = true;
		} else {
			if (!result.toString().contains("-"))
				judge = true;
		}

		if (!judge) {
			throw new Exception("the mark of negative and postive is error");
		}
	}

	public void testappendError(boolean DefaultDiffBuilderOrNot,
			boolean hasChild, int id) throws Exception {
		Integer sharedChildId = childID[id];
		Parent parent1 = new Parent();
		Parent parent2 = new Parent();

		if (hasChild) {
			parent1.setChild(new Child(sharedChildId));

			parent2.setChild(new Child(sharedChildId));
		}

		// Validate.isTrue(parent1.getChild().equals(parent2.getChild()));

		DiffBuilder diffBuilder = null;

		if (DefaultDiffBuilderOrNot)
			diffBuilder = new DiffBuilder(parent1, parent2,
					(ToStringStyle) null);
		else
			diffBuilder = new DiffBuilder2(parent1, parent2,
					(ToStringStyle) null);
		diffBuilder.append("value", parent1.getChild(), parent2.getChild());

		DiffResult diffResult = diffBuilder.build();

		if (diffResult.getNumberOfDiffs() != 0)
			throw new Exception(
					"This fails because did append(String, Object, object) did an \"l == r\" check but did not do an \"l.equals(r)\" check after determining the inputs were not arrays.");
	}

	public String test(int[] set) throws Exception {
		nonsenseParamters(set[0] == 0 ? false : true, set[1] == 0 ? false
				: true, set[2]);

		testappendError(set[3] == 0 ? false : true, set[4] == 0 ? false : true,
				set[5]);

		testCreateNumber(set[6] == 0 ? false : true, set[7] == 0 ? false : true);

		return OutputSet.PASS;
	}

	public static void main(String[] args) throws Exception {
		LanTest db = new LanTest();
		db.testappendError(true, true, 1);
		db.testCreateNumber(true, true);

	}

	private static class DiffBuilder2 extends
			org.apache.commons.lang3.builder.DiffBuilder {

		public DiffBuilder2(Object lhs, Object rhs, ToStringStyle style) {
			super(lhs, rhs, style);
		}

		/**
		 * <p>
		 * Test if two {@code Objects}s are equal.
		 * </p>
		 * 
		 * @param fieldName
		 *            the field name
		 * @param lhs
		 *            the left hand {@code Object}
		 * @param rhs
		 *            the right hand {@code Object}
		 * @return this
		 */
		@SuppressWarnings("deprecation")
		@Override
		public org.apache.commons.lang3.builder.DiffBuilder append(
				final String fieldName, final Object lhs, final Object rhs) {
			if (ObjectUtils.equals(lhs, rhs)) {
				return this;
			}

			return super.append(fieldName, lhs, rhs);
		}

	}

}