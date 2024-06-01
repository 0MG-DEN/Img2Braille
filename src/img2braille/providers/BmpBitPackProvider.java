package img2braille.providers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Stack;

import img2braille.*;

/**
 * This class handles 1-bit color depth BMP images and implements interface to
 * write image's equivalent as text using Braille symbols.
 * 
 * @see img2braille.BitPackProvider
 */
public class BmpBitPackProvider implements BitPackProvider {
	private final Stack<BitPackReadResult> stack;

	public BmpBitPackProvider(final InputStream stream, final boolean closeStream) throws IOException {
		this.stack = new Stack<BitPackReadResult>();
		this.init(stream);

		if (closeStream)
			stream.close();
	}

	public BmpBitPackProvider(final InputStream stream) throws IOException {
		this(stream, false);
	}

	public BmpBitPackProvider(final File file) throws IOException {
		this(new FileInputStream(file), true);
	}

	public BmpBitPackProvider(final String filePath) throws IOException {
		this(new File(filePath));
	}

	private void init(final InputStream stream) throws IOException {
		skip(stream, 10); // Skip to array pointer.
		int arrayStart = readInt(stream);

		skip(stream, 4); // Skip to image parameters.
		int width = readInt(stream);
		int height = readInt(stream);

		// BMPs are written in 4 byte blocks (32 pixels for 1 bit depth).
		// Ceiling of width divided by 32 and multiplied
		// by 4 to get total number of bytes in a row.
		int rowTotalBytes = ceil(width, 32) * 4;

		byte[] emptyRow = new byte[rowTotalBytes];

		// Ceiling of width divided by 8 to get number of used bytes in a row.
		int rowUsedBytes = ceil(width, 8);

		// Bytes are stored as is so need to reverse them using the buffer
		// stack below to put them in the result stack in correct order.
		Stack<BitPackReadResult> rowStack = new Stack<BitPackReadResult>();

		skip(stream, arrayStart - 26); // Skip to array.
		for (int currentRow = 1; currentRow <= height;) {
			byte[] part1, part2, part3;

			// Lines are stored in reversed order.
			part3 = readPart(stream, rowTotalBytes);
			part2 = ++currentRow <= height ? readPart(stream, rowTotalBytes) : emptyRow;
			part1 = ++currentRow <= height ? readPart(stream, rowTotalBytes) : emptyRow;
			++currentRow;

			// Each byte consists of 8 bits which we divide into 4 pairs
			// on each line. Then we add bits of all first items in pairs
			// to the shift and after that bits of all second items.
			for (int currentByte = 0; currentByte < rowUsedBytes; ++currentByte) {
				for (int currentPair = 0; currentPair < 4; ++currentPair) {
					byte shift = 0;

					// 1.......; ..1.....; ....1...; ......1.;
					int mask1 = 1 << (7 - currentPair * 2);
					shift |= (part1[currentByte] & mask1) > 0 ? 0x01 : 0;
					shift |= (part2[currentByte] & mask1) > 0 ? 0x02 : 0;
					shift |= (part3[currentByte] & mask1) > 0 ? 0x04 : 0;

					// .1......; ...1....; .....1..; .......1;
					int mask2 = mask1 >> 1;
					shift |= (part1[currentByte] & mask2) > 0 ? 0x08 : 0;
					shift |= (part2[currentByte] & mask2) > 0 ? 0x10 : 0;
					shift |= (part3[currentByte] & mask2) > 0 ? 0x20 : 0;

					// New line if we've read last bits of last byte in a row.
					boolean newLine = (currentPair == 3) && (currentByte == rowUsedBytes - 1);
					rowStack.push(new BitPackReadResult(true, new BitPack(shift), newLine));
				}
			}

			while (!rowStack.empty())
				stack.push(rowStack.pop());
		}

		stack.trimToSize();
	}

	private static int ceil(final int n, final int d) {
		return (n / d) + (n % d > 0 ? 1 : 0);
	}

	private static void skip(final InputStream stream, final int length) throws IOException {
		stream.skip(length);
	}

	private static int readInt(final InputStream stream) throws IOException {
		int result = 0;
		result |= stream.read();
		result |= stream.read() << 0x08;
		result |= stream.read() << 0x10;
		result |= stream.read() << 0x18;
		return result;
	}

	private static byte[] readPart(final InputStream stream, final int length) throws IOException {
		byte[] result = new byte[length];
		for (int i = 0; i < length; ++i)
			result[i] = (byte) stream.read();
		return result;
	}

	public BitPackReadResult read() {
		if (stack.empty())
			return BitPackReadResult.EOF;
		return stack.pop();
	}
}
