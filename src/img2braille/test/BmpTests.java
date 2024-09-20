package img2braille.test;

import java.io.*;
import java.net.*;
import java.nio.file.*;

import org.junit.*;

import img2braille.*;
import img2braille.readers.*;

public class BmpTests {
	private InputStream getStream(final String resourcePath) {
		ClassLoader loader = getClass().getClassLoader();
		return loader.getResourceAsStream(resourcePath);
	}

	private byte[] getBytes(final String resourcePath) throws Exception {
		ClassLoader loader = getClass().getClassLoader();
		URI uri = loader.getResource(resourcePath).toURI();
		Path path = Paths.get(uri);
		return Files.readAllBytes(path);
	}

	private void writeToFile(final BitPackReader provider, final File file) throws Exception {
		// BufferedWriter can write in Unicode.
		final BufferedWriter output = Files.newBufferedWriter(file.toPath());
		final BitPackWriter writer = new BitPackWriter(provider);
		writer.writeTo(output);
		output.flush();
		output.close();
	}

	private void test(final String bmpFile, final String txtFile) throws Exception {
		final InputStream stream = getStream(bmpFile);
		final BitPackReader reader = new BmpBitPackReader(stream, true);
		final File file = new File(txtFile);

		if (file.exists())
			file.delete();

		writeToFile(reader, file);
		Assert.assertTrue(file.exists());

		byte[] result = Files.readAllBytes(file.toPath()); // This will get bytes from newly created file.
		byte[] expected = getBytes(txtFile); // This will get bytes from resource file with the same name.
		Assert.assertArrayEquals(expected, result);
	}

	@Test
	public void test1() throws Exception {
		test("test1.bmp", "test1.txt");
	}

	@Test
	public void test2() throws Exception {
		test("test2.bmp", "test2.txt");
	}
}
