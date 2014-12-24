package ct;

class IJ {
	public int parameter;
	public int value;

	@Override
	public boolean equals(Object ij) {
		if (!(ij instanceof IJ))
			return false;

		if (this.parameter == ((IJ) ij).parameter
				&& this.value == ((IJ) ij).value)
			return true;
		else
			return false;
	}

	@Override
	public int hashCode() {
		String string = "";
		string += parameter;
		string += " ";
		string += value;
		return string.hashCode();

	}

}