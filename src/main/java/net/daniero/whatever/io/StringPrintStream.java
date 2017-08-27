package net.daniero.whatever.io;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class StringPrintStream extends PrintStream {
    private ByteArrayOutputStream byteArrayOutputStream;

    public StringPrintStream() {
        this(new ByteArrayOutputStream());
    }

    private StringPrintStream(ByteArrayOutputStream byteArrayOutputStream) {
        super(byteArrayOutputStream);
        this.byteArrayOutputStream = byteArrayOutputStream;
    }

    public String getString() {
        return byteArrayOutputStream.toString();
    }
}
