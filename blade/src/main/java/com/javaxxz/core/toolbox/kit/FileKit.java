package com.javaxxz.core.toolbox.kit;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.javaxxz.core.exception.ToolBoxException;


public class FileKit {


	private static final char UNIX_SEPARATOR = '/';

	private static final char WINDOWS_SEPARATOR = '\\';


	public static final String CLASS_EXT = ".class";

	public static final String JAR_FILE_EXT = ".jar";

	public static final String JAR_PATH_EXT = ".jar!";

	public static final String PATH_FILE_PRE = "file:";


	public static File[] ls(String path) {
		if (path == null) {
			return null;
		}
		path = getAbsolutePath(path);

		File file = file(path);
		if (file.isDirectory()) {
			return file.listFiles();
		}
		throw new ToolBoxException(StrKit.format("Path [{}] is not directory!", path));
	}


	public static boolean isEmpty(File file) {
		if (null == file) {
			return true;
		}

		if (file.isDirectory()) {
			String[] subFiles = file.list();
			if (CollectionKit.isEmpty(subFiles)) {
				return true;
			}
		}else if(file.isFile()){
			return file.length() <= 0;
		}

		return false;
	}


	public static boolean isNotEmpty(File file) {
		return false == isEmpty(file);
	}


	public static boolean isDirEmpty(Path dirPath) {
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(dirPath)) {
			return false == dirStream.iterator().hasNext();
		} catch (IOException e) {
			throw new ToolBoxException(e);
		}
	}
	

	public static boolean isDirEmpty(File dir) {
		return isDirEmpty(dir.toPath());
	}


	public static List<File> loopFiles(File file, FileFilter fileFilter) {
		List<File> fileList = new ArrayList<File>();
		if (file == null) {
			return fileList;
		} else if (file.exists() == false) {
			return fileList;
		}

		if (file.isDirectory()) {
			for (File tmp : file.listFiles()) {
				fileList.addAll(loopFiles(tmp, fileFilter));
			}
		} else {
			if (null == fileFilter || fileFilter.accept(file)) {
				fileList.add(file);
			}
		}

		return fileList;
	}


	public static List<File> loopFiles(File file) {
		return loopFiles(file, null);
	}


	public static List<String> listFileNames(String path) {
		if (path == null) {
			return null;
		}
		path = getAbsolutePath(path);
		if (path.endsWith(String.valueOf(UNIX_SEPARATOR)) == false) {
			path = path + UNIX_SEPARATOR;
		}

		List<String> paths = new ArrayList<String>();
		int index = path.lastIndexOf(FileKit.JAR_PATH_EXT);
		try {
			if (index == -1) {
				// 普通目录路径
				File[] files = ls(path);
				for (File file : files) {
					if (file.isFile()) {
						paths.add(file.getName());
					}
				}
			} else {
				// jar文件中的路径
				index = index + FileKit.JAR_FILE_EXT.length();
				final String jarPath = path.substring(0, index);
				final String subPath = path.substring(index + 2);
				for (JarEntry entry : Collections.list(new JarFile(jarPath).entries())) {
					final String name = entry.getName();
					if (name.startsWith(subPath)) {
						String nameSuffix = StrKit.removePrefix(name, subPath);
						if (nameSuffix.contains(String.valueOf(UNIX_SEPARATOR)) == false) {
							paths.add(nameSuffix);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new ToolBoxException(StrKit.format("Can not read file path of [{}]", path), e);
		}
		return paths;
	}


	public static File file(String path) {
		if (StrKit.isBlank(path)) {
			throw new NullPointerException("File path is blank!");
		}
		return new File(getAbsolutePath(path));
	}


	public static File file(String parent, String path) {
		if (StrKit.isBlank(path)) {
			throw new NullPointerException("File path is blank!");
		}
		return new File(parent, path);
	}


	public static File file(File parent, String path) {
		if (StrKit.isBlank(path)) {
			throw new NullPointerException("File path is blank!");
		}
		return new File(parent, path);
	}


	public static File file(URI uri) {
		if (uri == null) {
			throw new NullPointerException("File uri is null!");
		}
		return new File(uri);
	}


	public static boolean exist(String path) {
		return (path == null) ? false : file(path).exists();
	}


	public static boolean exist(File file) {
		return (file == null) ? false : file.exists();
	}


	public static boolean exist(String directory, String regexp) {
		File file = new File(directory);
		if (!file.exists()) {
			return false;
		}

		String[] fileList = file.list();
		if (fileList == null) {
			return false;
		}

		for (String fileName : fileList) {
			if (fileName.matches(regexp)) {
				return true;
			}

		}
		return false;
	}


	public static Date lastModifiedTime(File file) {
		if (!exist(file)) {
			return null;
		}

		return new Date(file.lastModified());
	}


	public static Date lastModifiedTime(String path) {
		File file = new File(path);
		if (!exist(file)) {
			return null;
		}

		return new Date(file.lastModified());
	}


	public static File touch(String fullFilePath) throws IOException {
		if (fullFilePath == null) {
			return null;
		}
		return touch(file(fullFilePath));
	}


	public static File touch(File file) throws IOException {
		if (null == file) {
			return null;
		}

		if (false == file.exists()) {
			mkParentDirs(file);
			file.createNewFile();
		}
		return file;
	}


	public static File mkParentDirs(File file) {
		final File parentFile = file.getParentFile();
		if (null != parentFile && false == parentFile.exists()) {
			parentFile.mkdirs();
		}
		return parentFile;
	}


	public static File mkParentDirs(String path) {
		if (path == null) {
			return null;
		}
		return mkParentDirs(file(path));
	}


	public static boolean del(String fullFileOrDirPath) throws IOException {
		return del(file(fullFileOrDirPath));
	}


	public static boolean del(File file) throws IOException {
		if (file == null || file.exists() == false) {
			return true;
		}

		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File childFile : files) {
				boolean isOk = del(childFile);
				if (isOk == false) {
					// 删除一个出错则本次删除任务失败
					return false;
				}
			}
		}
		return file.delete();
	}


	public static File mkdir(String dirPath) {
		if (dirPath == null) {
			return null;
		}
		File dir = file(dirPath);
		if (false == dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}


	public static File createTempFile(File dir) throws IOException {
		return createTempFile("hutool", null, dir, true);
	}


	public static File createTempFile(File dir, boolean isReCreat) throws IOException {
		return createTempFile("hutool", null, dir, isReCreat);
	}


	public static File createTempFile(String prefix, String suffix, File dir, boolean isReCreat) throws IOException {
		int exceptionsCount = 0;
		while (true) {
			try {
				File file = File.createTempFile(prefix, suffix, dir).getCanonicalFile();
				if (isReCreat) {
					file.delete();
					file.createNewFile();
				}
				return file;
			} catch (IOException ioex) { // fixes java.io.WinNTFileSystem.createFileExclusively access denied
				if (++exceptionsCount >= 50) {
					throw ioex;
				}
			}
		}
	}


	public static File copy(String srcPath, String destPath, boolean isOverride) throws IOException {
		return copy(file(srcPath), file(destPath), isOverride);
	}


	public static File copy(File src, File dest, boolean isOverride) throws IOException {
		// check
		if (!src.exists()) {
			throw new FileNotFoundException("File not exist: " + src);
		}
		if (equals(src, dest)) {
			throw new IOException("Files '" + src + "' and '" + dest + "' are equal");
		}

		// 复制目录
		if (src.isDirectory()) {
			if (dest.isFile()) {
				throw new IOException(StrKit.format("Src [{}] is a directory but Dest [{}] is a file!", src.getPath(), dest.getPath()));
			}

			if (!dest.exists()) {
				dest.mkdirs();
			}
			String files[] = src.list();
			for (String file : files) {
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// 递归复制
				copy(srcFile, destFile, isOverride);
			}
			return dest;
		}

		// 检查目标
		if (dest.exists()) {
			if (dest.isDirectory()) {
				dest = new File(dest, src.getName());
			}
			if (false == isOverride) {
				// 不覆盖，直接跳过
				LogKit.debug("File already exist");
				return dest;
			}
		} else {
			touch(dest);
		}

		// do copy file
		FileInputStream input = new FileInputStream(src);
		FileOutputStream output = new FileOutputStream(dest);
		try {
			IoKit.copy(input, output);
		} finally {
			IoKit.close(output);
			IoKit.close(input);
		}

		if (src.length() != dest.length()) {
			throw new IOException("Copy file failed of '" + src + "' to '" + dest + "' due to different sizes");
		}

		return dest;
	}


	public static void move(File src, File dest, boolean isOverride) throws IOException {
		// check
		if (!src.exists()) {
			throw new FileNotFoundException("File already exist: " + src);
		}
		if (dest.exists()) {
			if (isOverride) {
				dest.delete();
			} else {
				LogKit.debug("File already exist");
			}
		}

		// 来源为文件夹，目标为文件
		if (src.isDirectory() && dest.isFile()) {
			throw new IOException(StrKit.format("Can not move directory [{}] to file [{}]", src, dest));
		}

		// 来源为文件，目标为文件夹
		if (src.isFile() && dest.isDirectory()) {
			dest = new File(dest, src.getName());
		}

		if (src.renameTo(dest) == false) {
			// 在文件系统不同的情况下，renameTo会失败，此时使用copy，然后删除原文件
			try {
				copy(src, dest, isOverride);
				src.delete();
			} catch (Exception e) {
				throw new IOException(StrKit.format("Move [{}] to [{}] failed!", src, dest), e);
			}

		}
	}


	public static String getAbsolutePath(String path, Class<?> baseClass) {
		if (path == null) {
			path = StrKit.EMPTY;
		}
		if (baseClass == null) {
			return getAbsolutePath(path);
		}
		// return baseClass.getResource(path).getPath();
		return StrKit.removePrefix(PATH_FILE_PRE, baseClass.getResource(path).getPath());
	}


	public static String getAbsolutePath(String path) {
		if (path == null) {
			path = StrKit.EMPTY;
		} else {
			path = normalize(path);

			if (path.startsWith("/") || path.matches("^[a-zA-Z]:/.*")) {
				// 给定的路径已经是绝对路径了
				return path;
			}
		}

		// 相对路径
		ClassLoader classLoader = ClassKit.getClassLoader();
		URL url = classLoader.getResource(path);
		String reultPath = url != null ? url.getPath() : ClassKit.getClassPath() + path;
		// return StrKit.removePrefix(reultPath, PATH_FILE_PRE);
		return reultPath;
	}


	public static String getAbsolutePath(File file) {
		if (file == null) {
			return null;
		}

		try {
			return file.getCanonicalPath();
		} catch (IOException e) {
			return file.getAbsolutePath();
		}
	}


	public static boolean isDirectory(String path) {
		return (path == null) ? false : file(path).isDirectory();
	}


	public static boolean isDirectory(File file) {
		return (file == null) ? false : file.isDirectory();
	}


	public static boolean isFile(String path) {
		return (path == null) ? false : file(path).isDirectory();
	}


	public static boolean isFile(File file) {
		return (file == null) ? false : file.isDirectory();
	}


	public static boolean equals(File file1, File file2) {
		try {
			file1 = file1.getCanonicalFile();
			file2 = file2.getCanonicalFile();
		} catch (IOException ignore) {
			return false;
		}
		return file1.equals(file2);
	}


	public static int indexOfLastSeparator(String filePath) {
		if (filePath == null) {
			return -1;
		}
		int lastUnixPos = filePath.lastIndexOf(UNIX_SEPARATOR);
		int lastWindowsPos = filePath.lastIndexOf(WINDOWS_SEPARATOR);
		return (lastUnixPos >= lastWindowsPos) ? lastUnixPos : lastWindowsPos;
	}


	public static boolean isModifed(File file, long lastModifyTime) {
		if (null == file || false == file.exists()) {
			return true;
		}
		return file.lastModified() != lastModifyTime;
	}


	public static String normalize(String path) {
		return path.replaceAll("[/\\\\]{1,}", "/");
	}


	public static String subPath(String rootDir, String filePath) {
		return subPath(rootDir, file(filePath));
	}


	public static String subPath(String rootDir, File file) {
		if (StrKit.isEmpty(rootDir)) {
		}

		String subPath = null;
		try {
			subPath = file.getCanonicalPath();
		} catch (IOException e) {
			throw new ToolBoxException(e);
		}

		if (StrKit.isNotEmpty(rootDir) && StrKit.isNotEmpty(subPath)) {
			rootDir = normalize(rootDir);
			subPath = normalize(subPath);

			if (subPath != null && subPath.toLowerCase().startsWith(subPath.toLowerCase())) {
				subPath = subPath.substring(rootDir.length() + 1);
			}
		}
		return subPath;
	}

	// -------------------------------------------------------------------------------------------- name start

	public static String mainName(File file) {
		if (file.isDirectory()) {
			return file.getName();
		}
		return mainName(file.getName());
	}


	public static String mainName(String fileName) {
		if (StrKit.isBlank(fileName) || false == fileName.contains(StrKit.DOT)) {
			return fileName;
		}
		return StrKit.subPre(fileName, fileName.lastIndexOf(StrKit.DOT));
	}


	public static String extName(File file) {
		if (null == file) {
			return null;
		}
		if (file.isDirectory()) {
			return null;
		}
		return extName(file.getName());
	}


	public static String extName(String fileName) {
		if (fileName == null) {
			return null;
		}
		int index = fileName.lastIndexOf(StrKit.DOT);
		if (index == -1) {
			return StrKit.EMPTY;
		} else {
			String ext = fileName.substring(index + 1);
			// 扩展名中不能包含路径相关的符号
			return (ext.contains(String.valueOf(UNIX_SEPARATOR)) || ext.contains(String.valueOf(WINDOWS_SEPARATOR))) ? StrKit.EMPTY : ext;
		}
	}
	// -------------------------------------------------------------------------------------------- name end

	// -------------------------------------------------------------------------------------------- in start

	public static BufferedInputStream getInputStream(File file) throws FileNotFoundException {
		return new BufferedInputStream(new FileInputStream(file));
	}


	public static BufferedInputStream getInputStream(String path) throws FileNotFoundException {
		return getInputStream(file(path));
	}


	public static BufferedReader getUtf8Reader(File file) throws IOException {
		return getReader(file, CharsetKit.UTF_8);
	}


	public static BufferedReader getUtf8Reader(String path) throws IOException {
		return getReader(path, CharsetKit.UTF_8);
	}


	public static BufferedReader getReader(File file, String charsetName) throws IOException {
		return IoKit.getReader(getInputStream(file), charsetName);
	}


	public static BufferedReader getReader(File file, Charset charset) throws IOException {
		return IoKit.getReader(getInputStream(file), charset);
	}


	public static BufferedReader getReader(String path, String charsetName) throws IOException {
		return getReader(file(path), charsetName);
	}


	public static BufferedReader getReader(String path, Charset charset) throws IOException {
		return getReader(file(path), charset);
	}

	// -------------------------------------------------------------------------------------------- in end


	public static byte[] readBytes(File file) throws IOException {
		// check
		if (!file.exists()) {
			throw new FileNotFoundException("File not exist: " + file);
		}
		if (!file.isFile()) {
			throw new IOException("Not a file:" + file);
		}

		long len = file.length();
		if (len >= Integer.MAX_VALUE) {
			throw new IOException("File is larger then max array size");
		}

		byte[] bytes = new byte[(int) len];
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			in.read(bytes);
		} finally {
			IoKit.close(in);
		}

		return bytes;
	}


	public static String readUtf8String(File file) throws IOException {
		return readString(file, CharsetKit.UTF_8);
	}


	public static String readUtf8String(String path) throws IOException {
		return readString(path, CharsetKit.UTF_8);
	}


	public static String readString(File file, String charsetName) throws IOException {
		return new String(readBytes(file), charsetName);
	}


	public static String readString(File file, Charset charset) throws IOException {
		return new String(readBytes(file), charset);
	}


	public static String readString(String path, String charsetName) throws IOException {
		return readString(file(path), charsetName);
	}


	public static String readString(String path, Charset charset) throws IOException {
		return readString(file(path), charset);
	}


	public static String readString(URL url, String charset) throws IOException {
		if (url == null) {
			throw new RuntimeException("Empty url provided!");
		}

		InputStream in = null;
		try {
			in = url.openStream();
			return IoKit.read(in, charset);
		} finally {
			IoKit.close(in);
		}
	}


	public static <T extends Collection<String>> T readLines(String path, String charset, T collection) throws IOException {
		return readLines(file(path), charset, collection);
	}


	public static <T extends Collection<String>> T readLines(File file, String charset, T collection) throws IOException {
		BufferedReader reader = null;
		try {
			reader = getReader(file, charset);
			String line;
			while (true) {
				line = reader.readLine();
				if (line == null) break;
				collection.add(line);
			}
			return collection;
		} finally {
			IoKit.close(reader);
		}
	}


	public static <T extends Collection<String>> T readLines(URL url, String charset, T collection) throws IOException {
		InputStream in = null;
		try {
			in = url.openStream();
			return IoKit.readLines(in, charset, collection);
		} finally {
			IoKit.close(in);
		}
	}


	public static List<String> readLines(URL url, String charset) throws IOException {
		return readLines(url, charset, new ArrayList<String>());
	}


	public static List<String> readLines(String path, String charset) throws IOException {
		return readLines(path, charset, new ArrayList<String>());
	}


	public static List<String> readLines(File file, String charset) throws IOException {
		return readLines(file, charset, new ArrayList<String>());
	}


	public static <T> T load(ReaderHandler<T> readerHandler, String path, String charset) throws IOException {
		BufferedReader reader = null;
		T result = null;
		try {
			reader = getReader(path, charset);
			result = readerHandler.handle(reader);
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			IoKit.close(reader);
		}
		return result;
	}

	// -------------------------------------------------------------------------------------------- out start

	public static BufferedOutputStream getOutputStream(File file) throws IOException {
		return new BufferedOutputStream(new FileOutputStream(touch(file)));
	}


	public static BufferedOutputStream getOutputStream(String path) throws IOException {
		return getOutputStream(touch(path));
	}


	public static BufferedWriter getWriter(String path, String charsetName, boolean isAppend) throws IOException {
		return getWriter(touch(path), Charset.forName(charsetName), isAppend);
	}


	public static BufferedWriter getWriter(String path, Charset charset, boolean isAppend) throws IOException {
		return getWriter(touch(path), charset, isAppend);
	}


	public static BufferedWriter getWriter(File file, String charsetName, boolean isAppend) throws IOException {
		return getWriter(file, Charset.forName(charsetName), isAppend);
	}


	public static BufferedWriter getWriter(File file, Charset charset, boolean isAppend) throws IOException {
		if (false == file.exists()) {
			file.createNewFile();
		}
		return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, isAppend), charset));
	}


	public static PrintWriter getPrintWriter(String path, String charset, boolean isAppend) throws IOException {
		return new PrintWriter(getWriter(path, charset, isAppend));
	}


	public static PrintWriter getPrintWriter(File file, String charset, boolean isAppend) throws IOException {
		return new PrintWriter(getWriter(file, charset, isAppend));
	}

	// -------------------------------------------------------------------------------------------- out end


	public static File writeUtf8String(String content, String path) throws IOException {
		return writeString(content, path, CharsetKit.UTF_8);
	}


	public static File writeUtf8String(String content, File file) throws IOException {
		return writeString(content, file, CharsetKit.UTF_8);
	}


	public static File writeString(String content, String path, String charset) throws IOException {
		return writeString(content, touch(path), charset);
	}


	public static File writeString(String content, File file, String charset) throws IOException {
		PrintWriter writer = null;
		try {
			writer = getPrintWriter(file, charset, false);
			writer.print(content);
			writer.flush();
		} finally {
			IoKit.close(writer);
		}
		return file;
	}


	public static File appendString(String content, String path, String charset) throws IOException {
		return appendString(content, touch(path), charset);
	}


	public static File appendString(String content, File file, String charset) throws IOException {
		PrintWriter writer = null;
		try {
			writer = getPrintWriter(file, charset, true);
			writer.print(content);
			writer.flush();
		} finally {
			IoKit.close(writer);
		}
		return file;
	}


	public static <T> void writeLines(Collection<T> list, String path, String charset) throws IOException {
		writeLines(list, path, charset, false);
	}


	public static <T> void appendLines(Collection<T> list, String path, String charset) throws IOException {
		writeLines(list, path, charset, true);
	}


	public static <T> void writeLines(Collection<T> list, String path, String charset, boolean isAppend) throws IOException {
		PrintWriter writer = null;
		try {
			writer = getPrintWriter(path, charset, isAppend);
			for (T t : list) {
				if (t != null) {
					writer.println(t.toString());
					writer.flush();
				}
			}
		} finally {
			IoKit.close(writer);
		}
	}


	public static File writeBytes(byte[] data, String path) throws IOException {
		return writeBytes(data, touch(path));
	}


	public static File writeBytes(byte[] data, File dest) throws IOException {
		return writeBytes(data, dest, 0, data.length, false);
	}


	public static File writeBytes(byte[] data, File dest, int off, int len, boolean append) throws IOException {
		if (dest.exists() == true) {
			if (dest.isFile() == false) {
				throw new IOException("Not a file: " + dest);
			}
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(dest, append);
			out.write(data, off, len);
			out.flush();
		} finally {
			IoKit.close(out);
		}
		return dest;
	}


	public static File writeFromStream(InputStream in, File dest) throws IOException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(dest);
			IoKit.copy(in, out);
		} finally {
			IoKit.close(out);
		}
		return dest;
	}


	public static File writeFromStream(InputStream in, String fullFilePath) throws IOException {
		return writeFromStream(in, touch(fullFilePath));
	}


	public static void writeToStream(File file, OutputStream out) throws IOException {
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			IoKit.copy(in, out);
		} finally {
			IoKit.close(in);
		}
	}


	public static void writeToStream(String fullFilePath, OutputStream out) throws IOException {
		writeToStream(touch(fullFilePath), out);
	}


	public static String readableFileSize(File file) {
		return readableFileSize(file.length());
	}


	public static String readableFileSize(long size) {
		if (size <= 0) return "0";
		final String[] units = new String[] { "B", "kB", "MB", "GB", "TB", "EB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	// -------------------------------------------------------------------------- Interface start

	public interface ReaderHandler<T> {
		public T handle(BufferedReader reader) throws IOException;
	}
	// -------------------------------------------------------------------------- Interface end
}
