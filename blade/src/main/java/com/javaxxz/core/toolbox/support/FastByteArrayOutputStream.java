package com.javaxxz.core.toolbox.support;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;


public class FastByteArrayOutputStream extends OutputStream {

	private final FastByteBuffer buffer;

	public FastByteArrayOutputStream() {
		this(1024);
	}

	public FastByteArrayOutputStream(int size) {
		buffer = new FastByteBuffer(size);
	}

	@Override
	public void write(byte[] b, int off, int len) {
		buffer.append(b, off, len);
	}

	@Override
	public void write(int b) {
		buffer.append((byte) b);
	}

	public int size() {
		return buffer.size();
	}


	@Override
	public void close() {
		// nop
	}

	public void reset() {
		buffer.reset();
	}

	public void writeTo(OutputStream out) throws IOException {
		int index = buffer.index();
		for (int i = 0; i < index; i++) {
			byte[] buf = buffer.array(i);
			out.write(buf);
		}
		out.write(buffer.array(index), 0, buffer.offset());
	}

	public byte[] toByteArray() {
		return buffer.toArray();
	}

	@Override
	public String toString() {
		return new String(toByteArray());
	}

	public String toString(String enc) throws UnsupportedEncodingException {
		return new String(toByteArray(), enc);
	}
	
	public String toString(Charset charset) {
		return new String(toByteArray(), charset);
	}

}
