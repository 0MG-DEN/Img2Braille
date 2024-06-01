package img2braille.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import img2braille.*;
import img2braille.readers.*;

public final class Test {
	public static void main(String[] args) throws IOException {
		final BitPackReader reader1 = new BmpBitPackReader("test1.bmp");
		writeToFile(reader1, "test1.txt");

		final BitPackReader reader2 = new BmpBitPackReader("test2.bmp");
		writeToFile(reader2, "test2.txt");
	}

	private static void writeToFile(final BitPackReader provider, final String filePath) throws IOException {
		final File file = new File(filePath);

		if (file.exists())
			file.delete();

		// BufferedWriter can write in Unicode.
		final BufferedWriter output = Files.newBufferedWriter(file.toPath());
		final BitPackWriter writer = new BitPackWriter(provider);
		writer.writeTo(output);
		output.flush();
		output.close();
	}
}
