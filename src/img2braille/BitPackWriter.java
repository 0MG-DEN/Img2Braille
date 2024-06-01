package img2braille;

import java.io.IOException;
import java.io.Writer;

public final class BitPackWriter {
	private final BitPackProvider provider;

	public BitPackWriter(final BitPackProvider provider) {
		this.provider = provider;
	}

	public void writeTo(final Writer writer) throws IOException {
		BitPackReadResult readResult;

		while ((readResult = provider.read()).hasValue) {
			char c = readResult.pack.getChar();
			writer.append(c);

			if (readResult.newLine)
				writer.write("\r\n");
		}
	}
}
