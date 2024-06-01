package img2braille;

public final class BitPackReadResult {
	public static final BitPackReadResult EOF = new BitPackReadResult(false, new BitPack(0), false);

	public final boolean hasValue;
	public final BitPack pack;
	public final boolean newLine;

	public BitPackReadResult(final boolean hasValue, final BitPack pack, final boolean newLine) {
		this.hasValue = hasValue;
		this.pack = pack;
		this.newLine = newLine;
	}

	public BitPackReadResult(final boolean hasValue, final BitPack pack) {
		this(hasValue, pack, false);
	}
}
