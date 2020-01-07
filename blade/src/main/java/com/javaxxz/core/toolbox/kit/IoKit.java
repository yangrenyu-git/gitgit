package com.javaxxz.core.toolbox.kit;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Collection;

import com.javaxxz.core.exception.ToolBoxException;
import com.javaxxz.core.toolbox.support.Convert;
import com.javaxxz.core.toolbox.support.FastByteArrayOutputStream;
import com.javaxxz.core.toolbox.support.StreamProgress;


public class IoKit {


	public static final int DEFAULT_BUFFER_SIZE = 1024;

	public static final int EOF = -1;

	//-------------------------------------------------------------------------------------- Copy start

	public static long copy(Reader reader, Writer writer) throws IOException {
		return copy(reader, writer, DEFAULT_BUFFER_SIZE);
	}
	

	public static long copy(Reader reader, Writer writer, int bufferSize) throws IOException {
		return copy(reader, writer, bufferSize, null);
	}
	

	public static long copy(Reader reader, Writer writer, int bufferSize, StreamProgress streamProgress) throws IOException {
		char[] buffer = new char[bufferSize];
		long size = 0;
		int readSize;
		if(null != streamProgress){
			streamProgress.start();
		}
		while ((readSize = reader.read(buffer, 0, bufferSize)) != EOF) {
			writer.write(buffer, 0, readSize);
			size += readSize;
			writer.flush();
			if(null != streamProgress){
				streamProgress.progress(size);
			}
		}
		if(null != streamProgress){
			streamProgress.finish();
		}
		return size;
	}
	

	public static long copy(InputStream in, OutputStream out) throws IOException {
		return copy(in, out, DEFAULT_BUFFER_SIZE);
	}
	

	public static long copy(InputStream in, OutputStream out, int bufferSize) throws IOException {
		return copy(in, out, bufferSize, null);
	}
	

	public static long copy(InputStream in, OutputStream out, int bufferSize, StreamProgress streamProgress) throws IOException {
		if(null == in){
			throw new NullPointerException("InputStream is null!");
		}
		if(null == out){
			throw new NullPointerException("OutputStream is null!");
		}
		if(bufferSize <= 0){
			bufferSize = DEFAULT_BUFFER_SIZE;
		}
		
		byte[] buffer = new byte[bufferSize];
		long size = 0;
		if(null != streamProgress){
			streamProgress.start();
		}
		for (int readSize = -1; (readSize = in.read(buffer)) != EOF;) {
			out.write(buffer, 0, readSize);
			size += readSize;
			out.flush();
			if(null != streamProgress){
				streamProgress.progress(size);
			}
		}
		if(null != streamProgress){
			streamProgress.finish();
		}
		return size;
	}
	

	public static long copy(FileInputStream in, FileOutputStream out) throws IOException {
		if(null == in){
			throw new NullPointerException("FileInputStream is null!");
		}
		if(null == out){
			throw new NullPointerException("FileOutputStream is null!");
		}
		
		FileChannel inChannel = in.getChannel();
		FileChannel outChannel = out.getChannel();
		
		return inChannel.transferTo(0, inChannel.size(), outChannel);
	}
	//-------------------------------------------------------------------------------------- Copy end
	

	public static BufferedReader getReader(InputStream in, String charsetName) throws IOException{
		return getReader(in, Charset.forName(charsetName));
	}
	

	public static BufferedReader getReader(InputStream in, Charset charset) throws IOException{
		if(null == in){
			return null;
		}
		
		InputStreamReader reader = null;
		if(null == charset) {
			reader = new InputStreamReader(in);
		}else {
			reader = new InputStreamReader(in, charset);
		}
		
		return new BufferedReader(reader);
	}
	

	public static byte[] readBytes(InputStream in) throws IOException {
		final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
		copy(in, out);
		return out.toByteArray();
	}
	

	public static String read(InputStream in, String charsetName) throws IOException {
		FastByteArrayOutputStream out = read(in);
		return StrKit.isBlank(charsetName) ? out.toString() : out.toString(charsetName);
	}


	public static String read(InputStream in, Charset charset) throws IOException {
		FastByteArrayOutputStream out = read(in);
		return null == charset ? out.toString() : out.toString(charset);
	}
	

	public static FastByteArrayOutputStream read(InputStream in) throws IOException {
		final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
		copy(in, out);
		return out;
	}
	

	public static String read(Reader reader) throws IOException{
		final StringBuilder builder = StrKit.builder();
		final CharBuffer buffer = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);
		while(-1 != reader.read(buffer)){
			builder.append(buffer.flip().toString());
		}
		return builder.toString();
	}
	

	public static String read(FileChannel fileChannel, String charset) throws IOException {
		final MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size()).load();
		return StrKit.str(buffer, charset);
	}
	

	public static <T extends Collection<String>> T readLines(InputStream in, String charset, T collection) throws IOException {
		// 从返回的内容中读取所需内容
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
		String line = null;
		while ((line = reader.readLine()) != null) {
			collection.add(line);
		}

		return collection;
	}
	

	public static ByteArrayInputStream toStream(String content, String charset) {
		if(content == null) {
			return null;
		}
		
		byte[] data = null;
		try {
			data = StrKit.isBlank(charset) ? content.getBytes() : content.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new ToolBoxException(StrKit.format("Invalid charset [{}] !", charset), e);
		}
		
		return new ByteArrayInputStream(data);
	}
	

	public static void write(OutputStream out, String charset, boolean isCloseOut, Object... contents) throws IOException {
		OutputStreamWriter osw = null;
		try {
			osw = new OutputStreamWriter(out, charset);
			for (Object content : contents) {
				if(content != null) {
					osw.write(Convert.toStr(content, StrKit.EMPTY));
					osw.flush();
				}
			}
		} catch (Exception e) {
			throw new IOException("Write content to OutputStream error!", e);
		}finally {
			if(isCloseOut) {
				close(osw);
			}
		}
	}
	

	public static void echo(Object content, Object... param) {
		if(content == null) {
			System.out.println(content);
		}
		System.out.println(StrKit.format(content.toString(), param));
	}
	

	public static void close(Closeable closeable) {
		if (closeable == null) return;
		try {
			closeable.close();
		} catch (Exception e) {
		}
	}
	

	public static void close(AutoCloseable closeable) {
		if (closeable == null) return;
		try {
			closeable.close();
		} catch (Exception e) {
		}
	}
}
