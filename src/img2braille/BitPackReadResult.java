package img2braille;

public final class BitPackReadResult {
	public static final BitPackReadResult EOF = new BitPackReadResult(new BitPack(0), false, false);

	public final BitPack pack;
	public final boolean newLine;
	public final boolean hasValue;

	public BitPackReadResult(final BitPack pack, final boolean newLine, final boolean hasValue) {
		this.pack = pack;
		this.newLine = newLine;
		this.hasValue = hasValue;
	}

	public BitPackReadResult(final BitPack pack, final boolean newLine) {
		this(pack, newLine, true);
	}

	public BitPackReadResult(final BitPack pack) {
		this(pack, false, true);
	}
}
