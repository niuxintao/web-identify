package lantest;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.math.NumberUtils;

public class DiffBuilderAppendsNonEqualObjects {

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

	public boolean testCreateNumber(boolean negativeOrNot, boolean zeroOrNot) {
		String a = "";
		if (negativeOrNot) {
			a += "-";
		}
		if (zeroOrNot)
			a += "0.0";
		else
			a += "1.0";

		Number result = NumberUtils.createNumber(a);
		if (negativeOrNot) {
			if (result.toString().contains("-"))
				return true;
			else
				return false;
		} else {
			if (result.toString().contains("-"))
				return false;
			else
				return true;
		}
	}

	public void testappendError(boolean DefaultDiffBuilderOrNot) {
		Integer sharedChildId = 1;
		Parent parent1 = new Parent();
		parent1.setChild(new Child(sharedChildId));
		Parent parent2 = new Parent();
		parent2.setChild(new Child(sharedChildId));

		Validate.isTrue(parent1.getChild().equals(parent2.getChild()));

		DiffBuilder diffBuilder = null;

		if (DefaultDiffBuilderOrNot)
			diffBuilder = new DiffBuilder(parent1, parent2,
					(ToStringStyle) null);
		else
			diffBuilder = new DiffBuilder2(parent1, parent2,
					(ToStringStyle) null);
		diffBuilder.append("value", parent1.getChild(), parent2.getChild());

		DiffResult diffResult = diffBuilder.build();

		Validate.isTrue(
				diffResult.getNumberOfDiffs() == 0,
				"This fails because did append(String, Object, object) did an \"l == r\" check but did not do an \"l.equals(r)\" check after determining the inputs were not arrays.");
	}

	public static void main(String[] args) {
		DiffBuilderAppendsNonEqualObjects db = new DiffBuilderAppendsNonEqualObjects();
		db.testappendError(false);
		System.out.println(db.testCreateNumber(true, true));

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