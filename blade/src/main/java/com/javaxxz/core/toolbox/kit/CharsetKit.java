package com.javaxxz.core.toolbox.kit;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import com.javaxxz.core.exception.ToolBoxException;
import com.javaxxz.core.toolbox.kit.StrKit;


public class CharsetKit {
	
	public static final String ISO_8859_1 = "ISO-8859-1";
	public static final String UTF_8 = "UTF-8";
	public static final String GBK = "GBK";
	
	private CharsetKit() {
		// 静态类不可实例化
	}
	

	public static String convert(String source, String srcCharset, String newCharset) {
		if(StrKit.isBlank(srcCharset)) {
			srcCharset = ISO_8859_1;
		}
		
		if(StrKit.isBlank(newCharset)) {
			srcCharset = UTF_8;
		}
		
		if (StrKit.isBlank(source) || srcCharset.equals(newCharset)) {
			return source;
		}
		try {
			return new String(source.getBytes(srcCharset), newCharset);
		} catch (UnsupportedEncodingException unex) {
			throw new ToolBoxException(unex);
		}
	}
	

	@Deprecated
	public static String str(byte[] data, String charset) {
		if(data == null) {
			return null;
		}
		
		if(StrKit.isBlank(charset)) {
			return new String(data);
		}
		
		try {
			return new String(data, charset);
		} catch (UnsupportedEncodingException e) {
			throw new ToolBoxException(e);
		}
	}
	

	public static String str(ByteBuffer data, String charset){
		if(data == null) {
			return null;
		}
		
		Charset cs;
		
		if(StrKit.isBlank(charset)) {
			cs = Charset.defaultCharset();
		}else {
			cs = Charset.forName(charset);
		}
		
		return cs.decode(data).toString();
	}
	

	public static ByteBuffer toByteBuffer(String str, String charset) {
		return ByteBuffer.wrap(StrKit.encode(str, charset));
	}
	

	public static String systemCharset() {
		String charset = System.getProperty("file.encoding");
		if(StrKit.isBlank(charset)) {
			charset = UTF_8;
		}
		return charset;
	}

}
