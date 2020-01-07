package com.javaxxz.core.toolbox.kit;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.javaxxz.core.exception.ToolBoxException;
import com.javaxxz.core.toolbox.support.FastByteArrayOutputStream;


public class ZipKit {
	private static final Logger log = LogManager.getLogger(ZipKit.class);


	public static File zip(String srcPath) throws IOException {
		return zip(FileKit.file(srcPath));
	}


	public static File zip(File srcFile) throws IOException {
		File zipFile = FileKit.file(srcFile.getParentFile(), FileKit.mainName(srcFile) + ".zip");
		zip(srcFile, zipFile, false);
		return zipFile;
	}


	public static File zip(String srcPath, String zipPath) throws IOException {
		return zip(srcPath, zipPath, false);
	}


	public static File zip(String srcPath, String zipPath, boolean withSrcDir) throws IOException {
		File srcFile = FileKit.file(srcPath);
		File zipFile = FileKit.file(zipPath);
		zip(srcFile, zipFile, withSrcDir);
		return zipFile;
	}


	public static void zip(File srcFile, File zipFile, boolean withSrcDir) throws IOException {
		validateFile(srcFile, zipFile);

		ZipOutputStream out = null;
		try {
			out = new ZipOutputStream(new CheckedOutputStream(FileKit.getOutputStream(zipFile), new CRC32()));

			// 如果只是压缩一个文件，则需要截取该文件的父目录
			String srcRootDir = srcFile.getCanonicalPath();
			if (srcFile.isFile() || withSrcDir) {
				srcRootDir = srcFile.getParent();
			}
			// 调用递归压缩方法进行目录或文件压缩
			zip(srcRootDir, srcFile, out);
			out.flush();
		} catch (IOException e) {
			throw e;
		} finally {
			IoKit.close(out);
		}
	}


	public static File unzip(File zipFile) throws IOException {
		return unzip(zipFile, FileKit.file(zipFile.getParentFile(), FileKit.mainName(zipFile)));
	}


	public static File unzip(String zipFilePath) throws IOException {
		return unzip(FileKit.file(zipFilePath));
	}


	public static File unzip(String zipFilePath, String outFileDir) throws IOException {
		return unzip(FileKit.file(zipFilePath), FileKit.mkdir(outFileDir));
	}


	@SuppressWarnings("unchecked")
	public static File unzip(File zipFile, File outFile) throws IOException {
		final ZipFile zipFileObj = new ZipFile(zipFile);
		final Enumeration<ZipEntry> em = (Enumeration<ZipEntry>) zipFileObj.entries();
		ZipEntry zipEntry = null;
		File outItemFile = null;
		while (em.hasMoreElements()) {
			zipEntry = em.nextElement();
			outItemFile = new File(outFile, zipEntry.getName());
			log.debug("UNZIP {}", outItemFile.getPath());
			if (zipEntry.isDirectory()) {
				outItemFile.mkdirs();
			} else {
				FileKit.touch(outItemFile);
				copy(zipFileObj, zipEntry, outItemFile);
			}
		}
		IoKit.close(zipFileObj);
		return outFile;
	}
	

	public static byte[] gzip(String content, String charset) throws IOException {
		return gzip(StrKit.bytes(content, charset));
	}
	

	public static byte[] gzip(byte[] val) throws IOException {
		FastByteArrayOutputStream bos = new FastByteArrayOutputStream(val.length);
		GZIPOutputStream gos = null;
		try {
			gos = new GZIPOutputStream(bos);
			gos.write(val, 0, val.length);
			gos.finish();
			gos.flush();
			val = bos.toByteArray();
		} finally {
			IoKit.close(gos);
		}
		return val;
	}
	

	public static byte[] gzip(File file) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
		GZIPOutputStream gos = null;
		BufferedInputStream in;
		try {
			gos = new GZIPOutputStream(bos);
			in = FileKit.getInputStream(file);
			IoKit.copy(in, gos);
			return bos.toByteArray();
		} finally {
			IoKit.close(gos);
		}
	}
	

	public static String unGzip(byte[] buf, String charset) throws IOException {
		return StrKit.str(unGzip(buf), charset);
	}
	

	public static byte[] unGzip(byte[] buf) throws IOException {
		GZIPInputStream gzi = null;
		ByteArrayOutputStream bos = null;
		try {
			gzi = new GZIPInputStream(new ByteArrayInputStream(buf));
			bos = new ByteArrayOutputStream(buf.length);
			IoKit.copy(gzi, bos);
			buf = bos.toByteArray();
		} finally {
			IoKit.close(gzi);
		}
		return buf;
	}

	// ---------------------------------------------------------------------------------------------- Private method start

	private static void zip(String srcRootDir, File file, ZipOutputStream out) {
		if (file == null) {
			return;
		}

		if (file.isFile()) {// 如果是文件，则直接压缩该文件
			final String subPath = FileKit.subPath(srcRootDir, file); // 获取文件相对于压缩文件夹根目录的子路径
			log.debug("ZIP {}", subPath);
			BufferedInputStream in = null;
			try {
				out.putNextEntry(new ZipEntry(subPath));
				in = FileKit.getInputStream(file);
				IoKit.copy(in, out);
			} catch (IOException e) {
				throw new ToolBoxException(e);
			} finally {
				IoKit.close(in);
				closeEntry(out);
			}
		} else {// 如果是目录，则压缩压缩目录中的文件或子目录
			for (File childFile : file.listFiles()) {
				zip(srcRootDir, childFile, out);
			}
		}
	}


	private static void validateFile(File srcFile, File zipFile) throws ToolBoxException {
		if (false == srcFile.exists()) {
			throw new ToolBoxException(StrKit.format("File [{}] not exist!", srcFile.getAbsolutePath()));
		}

		try {
			// 压缩文件不能位于被压缩的目录内
			if (srcFile.isDirectory() && zipFile.getParent().contains(srcFile.getCanonicalPath())) {
				throw new ToolBoxException("[zipPath] must not be the child directory of [srcPath]!");
			}

			if (false == zipFile.exists()) {
				FileKit.touch(zipFile);
			}
		} catch (IOException e) {
			throw new ToolBoxException(e);
		}
	}


	private static void closeEntry(ZipOutputStream out) {
		try {
			out.closeEntry();
		} catch (IOException e) {
		}
	}


	private static void copy(ZipFile zipFile, ZipEntry zipEntry, File outItemFile) throws IOException {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = zipFile.getInputStream(zipEntry);
			out = FileKit.getOutputStream(outItemFile);
			IoKit.copy(in, out);
		} finally {
			IoKit.close(out);
			IoKit.close(in);
		}
	}
	// ---------------------------------------------------------------------------------------------- Private method end

}
