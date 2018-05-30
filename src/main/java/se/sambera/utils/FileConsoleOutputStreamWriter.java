package se.sambera.utils;

import java.io.*;

/**
 * To preserve the console output.
 *
 * Example:
 *
 * new FileConsoleOutputStreamWriter("target/unRunChangeSets.txt", System.out)))
 *
 */
public class FileConsoleOutputStreamWriter extends OutputStreamWriter {
    private PrintStream stream;

    /**
     * Make it possible to write to a file and also have it displayed on the console (or other PrintStream).
     *
     * @param pathname Pathname of the file to write to
     * @param stream
     * @throws FileNotFoundException
     */
    public FileConsoleOutputStreamWriter(String pathname, PrintStream stream) throws FileNotFoundException {
        super(new FileOutputStream(pathname));
        this.stream = stream;
    }

    @Override
    public void flush() throws IOException {
        super.flush();
        stream.flush();
    }

    @Override
    public void write(int c) throws IOException {
        super.write(c);
        stream.write(c);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        super.write(str, off, len);
        stream.write(str.getBytes(), off, len);
    }

    /**
     * Closes the FileOutputStream.
     * The PrintStream is just flushed but <b>not</b> closed.
     * @see PrintStream#close()
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        super.close();
    }
}
