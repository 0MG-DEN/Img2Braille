package img2braille.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import img2braille.*;
import img2braille.providers.*;

public final class Test {
	public static void main(String[] args) throws IOException {
		final BitPackProvider provider1 = new BmpBitPackProvider("test1.bmp");
		writeToFile(provider1, "test1.txt");

		final BitPackProvider provider2 = new BmpBitPackProvider("test2.bmp");
		writeToFile(provider2, "test2.txt");
	}

	private static void writeToFile(final BitPackProvider provider, final String filePath) throws IOException {
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
