package img2braille;

import java.io.IOException;
import java.io.Writer;

public final class BitPackWriter {
	private final BitPackReader reader;

	public BitPackWriter(final BitPackReader reader) {
		this.reader = reader;
	}

	public void writeTo(final Writer writer) throws IOException {
		BitPackReadResult readResult;

		while ((readResult = reader.read()).hasValue) {
			char c = readResult.pack.getChar();
			writer.append(c);

			if (readResult.newLine)
				writer.write("\r\n");
		}
	}
}
