package img2braille;

public final class BitPack {
	private final byte bits;

	public BitPack(final byte b) {
		this.bits = (byte) (b & 63);
	}

	public BitPack(final short s) {
		this.bits = (byte) (s & 63);
	}

	public BitPack(final int i) {
		this.bits = (byte) (i & 63);
	}

	public char getChar() {
		return bits > 0 ? (char) ('\u2800' + bits) : ' ';
	}
}
