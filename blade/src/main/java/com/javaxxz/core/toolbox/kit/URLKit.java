package com.javaxxz.core.toolbox.kit;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.javaxxz.core.exception.ToolBoxException;


public class URLKit {
	

	public static URL url(String url){
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			throw new ToolBoxException(e.getMessage(), e);
		}
	}


	public static URL getURL(String pathBaseClassLoader) {
		return ClassKit.getClassLoader().getResource(pathBaseClassLoader);
	}


	public static URL getURL(String path, Class<?> clazz) {
		return clazz.getResource(path);
	}


	public static URL getURL(File file) {
		try {
			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			throw new ToolBoxException("Error occured when get URL!", e);
		}
	}
	

	public static URL[] getURLs(File... files) {
		final URL[] urls = new URL[files.length];
		try {
			for(int i = 0; i < files.length; i++){
				urls[i] = files[i].toURI().toURL();
			}
		} catch (MalformedURLException e) {
			throw new ToolBoxException("Error occured when get URL!", e);
		}
		
		return urls;
	}
	

	public static String formatUrl(String url) {
		if (StrKit.isBlank(url)){
			return null;
		}
		if (url.startsWith("http://") || url.startsWith("https://")){
			return url;
		}
		return "http://" + url;
	}


	public static String complateUrl(String baseUrl, String relativePath) {
		baseUrl = formatUrl(baseUrl);
		if (StrKit.isBlank(baseUrl)) {
			return null;
		}

		try {
			final URL absoluteUrl = new URL(baseUrl);
			final URL parseUrl = new URL(absoluteUrl, relativePath);
			return parseUrl.toString();
		} catch (MalformedURLException e) {
			throw new ToolBoxException(e);
		}
	}
	

	public static String encode(String url, String charset) {
		try {
			return URLEncoder.encode(url, charset);
		} catch (UnsupportedEncodingException e) {
			throw new ToolBoxException(e);
		}
	}
	

	public static String decode(String url, String charset) {
		try {
			return URLDecoder.decode(url, charset);
		} catch (UnsupportedEncodingException e) {
			throw new ToolBoxException(e);
		}
	}
	

	public static String getPath(String uriStr){
		URI uri = null;
		try {
			uri = new URI(uriStr);
		} catch (URISyntaxException e) {
			throw new ToolBoxException(e);
		}
		
		return uri == null ? null : uri.getPath();
	}
}
