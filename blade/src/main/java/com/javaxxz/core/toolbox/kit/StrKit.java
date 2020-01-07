package com.javaxxz.core.toolbox.kit;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class StrKit {

	public static final String SPACE = " ";
	public static final String DOT = ".";
	public static final String SLASH = "/";
	public static final String BACKSLASH = "\\";
	public static final String EMPTY = "";
	public static final String CRLF = "\r\n";
	public static final String NEWLINE = "\n";
	public static final String UNDERLINE = "_";
	public static final String COMMA = ",";

	public static final String HTML_NBSP = "&nbsp;";
	public static final String HTML_AMP = "&amp";
	public static final String HTML_QUOTE = "&quot;";
	public static final String HTML_LT = "&lt;";
	public static final String HTML_GT = "&gt;";

	public static final String EMPTY_JSON = "{}";



	public static String firstCharToLowerCase(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= 'A' && firstChar <= 'Z') {
			char[] arr = str.toCharArray();
			arr[0] += ('a' - 'A');
			return new String(arr);
		}
		return str;
	}
	

	public static String firstCharToUpperCase(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= 'a' && firstChar <= 'z') {
			char[] arr = str.toCharArray();
			arr[0] -= ('a' - 'A');
			return new String(arr);
		}
		return str;
	}

	// ------------------------------------------------------------------------ Blank

	public static boolean isBlank(String str) {
		int length;
		if ((str == null) || ((length = str.length()) == 0)) {
			return true;
		}
		for (int i = 0; i < length; i++) {
			// 只要有一个非空字符即为非空字符串
			if (false == Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}


	public static boolean notBlank(String str) {
		return false == isBlank(str);
	}


	public static boolean hasBlank(String... strs) {
		if (CollectionKit.isEmpty(strs)) {
			return true;
		}
		for (String str : strs) {
			if (isBlank(str)) {
				return true;
			}
		}
		return false;
	}


	public static boolean isAllBlank(String... strs) {
		if (CollectionKit.isEmpty(strs)) {
			return true;
		}
		for (String str : strs) {
			if (notBlank(str)) {
				return false;
			}
		}
		return true;
	}

	// ------------------------------------------------------------------------ Empty

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}


	public static boolean isNotEmpty(String str) {
		return false == isEmpty(str);
	}


	public static String nullToEmpty(String str) {
		return nullToDefault(str, EMPTY);
	}


	public static String nullToDefault(String str, String defaultStr) {
		return (str == null) ? defaultStr : str;
	}


	public static String emptyToNull(String str) {
		return isEmpty(str) ? null : str;
	}


	public static boolean hasEmpty(String... strs) {
		if (CollectionKit.isEmpty(strs)) {
			return true;
		}

		for (String str : strs) {
			if (isEmpty(str)) {
				return true;
			}
		}
		return false;
	}


	public static boolean isAllEmpty(String... strs) {
		if (CollectionKit.isEmpty(strs)) {
			return true;
		}

		for (String str : strs) {
			if (isNotEmpty(str)) {
				return false;
			}
		}
		return true;
	}

	// ------------------------------------------------------------------------ Trim

	public static String trim(String str) {
		return (null == str) ? null : trim(str, 0);
	}


	public static void trim(String[] strs) {
		if (null == strs) {
			return;
		}
		String str;
		for (int i = 0; i < strs.length; i++) {
			str = strs[i];
			if (null != str) {
				strs[i] = str.trim();
			}
		}
	}


	public static String trimStart(String str) {
		return trim(str, -1);
	}


	public static String trimEnd(String str) {
		return trim(str, 1);
	}


	public static String trim(String str, int mode) {
		if (str == null) {
			return null;
		}

		int length = str.length();
		int start = 0;
		int end = length;

		// 扫描字符串头部
		if (mode <= 0) {
			while ((start < end) && (Character.isWhitespace(str.charAt(start)))) {
				start++;
			}
		}

		// 扫描字符串尾部
		if (mode >= 0) {
			while ((start < end) && (Character.isWhitespace(str.charAt(end - 1)))) {
				end--;
			}
		}

		if ((start > 0) || (end < length)) {
			return str.substring(start, end);
		}

		return str;
	}
	

	public static boolean startWith(String str, String prefix, boolean isIgnoreCase){
		if(isIgnoreCase){
			return str.toLowerCase().startsWith(prefix.toLowerCase());
		}else{
			return str.startsWith(prefix);
		}
	}
	

	public static boolean endWith(String str, String suffix, boolean isIgnoreCase){
		if(isIgnoreCase){
			return str.toLowerCase().endsWith(suffix.toLowerCase());
		}else{
			return str.endsWith(suffix);
		}
	}
	

	public static boolean containsIgnoreCase(String str, String testStr){
		if(null == str){
			//如果被监测字符串和 
			return null == testStr;
		}
		return str.toLowerCase().contains(testStr.toLowerCase());
	}


	public static String getGeneralField(String getOrSetMethodName) {
		if (getOrSetMethodName.startsWith("get") || getOrSetMethodName.startsWith("set")) {
			return cutPreAndLowerFirst(getOrSetMethodName, 3);
		}
		return null;
	}


	public static String genSetter(String fieldName) {
		return upperFirstAndAddPre(fieldName, "set");
	}


	public static String genGetter(String fieldName) {
		return upperFirstAndAddPre(fieldName, "get");
	}


	public static String cutPreAndLowerFirst(String str, int preLength) {
		if (str == null) {
			return null;
		}
		if (str.length() > preLength) {
			char first = Character.toLowerCase(str.charAt(preLength));
			if (str.length() > preLength + 1) {
				return first + str.substring(preLength + 1);
			}
			return String.valueOf(first);
		}
		return null;
	}


	public static String upperFirstAndAddPre(String str, String preString) {
		if (str == null || preString == null) {
			return null;
		}
		return preString + upperFirst(str);
	}


	public static String upperFirst(String str) {
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}


	public static String lowerFirst(String str) {
		if(isBlank(str)){
			return str;
		}
		return Character.toLowerCase(str.charAt(0)) + str.substring(1);
	}


	public static String removePrefix(String str, String prefix) {
		if(isEmpty(str) || isEmpty(prefix)){
			return str;
		}
		
		if (str.startsWith(prefix)) {
			return str.substring(prefix.length());
		}
		return str;
	}


	public static String removePrefixIgnoreCase(String str, String prefix) {
		if(isEmpty(str) || isEmpty(prefix)){
			return str;
		}
		
		if (str.toLowerCase().startsWith(prefix.toLowerCase())) {
			return str.substring(prefix.length());
		}
		return str;
	}


	public static String removeSuffix(String str, String suffix) {
		if(isEmpty(str) || isEmpty(suffix)){
			return str;
		}
		
		if (str.endsWith(suffix)) {
			return str.substring(0, str.length() - suffix.length());
		}
		return str;
	}


	public static String removeSuffixIgnoreCase(String str, String suffix) {
		if(isEmpty(str) || isEmpty(suffix)){
			return str;
		}
		
		if (str.toLowerCase().endsWith(suffix.toLowerCase())) {
			return str.substring(0, str.length() - suffix.length());
		}
		return str;
	}
	

	public static String addPrefixIfNot(String str, String prefix){
		if(isEmpty(str) || isEmpty(prefix)){
			return str;
		}
		if(false == str.startsWith(prefix)){
			str = prefix + str;
		}
		return str;
	}
	

	public static String addSuffixIfNot(String str, String suffix){
		if(isEmpty(str) || isEmpty(suffix)){
			return str;
		}
		if(false == str.endsWith(suffix)){
			str += suffix;
		}
		return str;
	}


	public static String cleanBlank(String str) {
		if (str == null) {
			return null;
		}

		return str.replaceAll("\\s*", EMPTY);
	}


	public static List<String> split(String str, char separator) {
		return split(str, separator, 0);
	}


	public static List<String> split(String str, char separator, int limit) {
		if (str == null) {
			return null;
		}
		List<String> list = new ArrayList<String>(limit == 0 ? 16 : limit);
		if (limit == 1) {
			list.add(str);
			return list;
		}

		boolean isNotEnd = true; // 未结束切分的标志
		int strLen = str.length();
		StringBuilder sb = new StringBuilder(strLen);
		for (int i = 0; i < strLen; i++) {
			char c = str.charAt(i);
			if (isNotEnd && c == separator) {
				list.add(sb.toString());
				// 清空StringBuilder
				sb.delete(0, sb.length());

				// 当达到切分上限-1的量时，将所剩字符全部作为最后一个串
				if (limit != 0 && list.size() == limit - 1) {
					isNotEnd = false;
				}
			} else {
				sb.append(c);
			}
		}
		list.add(sb.toString());// 加入尾串
		return list;
	}


	public static String[] split(String str, String delimiter) {
		if (str == null) {
			return null;
		}
		if (str.trim().length() == 0) {
			return new String[] { str };
		}

		int dellen = delimiter.length(); // del length
		int maxparts = (str.length() / dellen) + 2; // one more for the last
		int[] positions = new int[maxparts];

		int i, j = 0;
		int count = 0;
		positions[0] = -dellen;
		while ((i = str.indexOf(delimiter, j)) != -1) {
			count++;
			positions[count] = i;
			j = i + dellen;
		}
		count++;
		positions[count] = str.length();

		String[] result = new String[count];

		for (i = 0; i < count; i++) {
			result[i] = str.substring(positions[i] + dellen, positions[i + 1]);
		}
		return result;
	}


	public static String sub(String string, int fromIndex, int toIndex) {
		int len = string.length();
		if (fromIndex < 0) {
			fromIndex = len + fromIndex;
			if(fromIndex < 0 ) { 
				fromIndex = 0;
			}
		} else if(fromIndex >= len) {
			fromIndex = len -1;
		}
		if (toIndex < 0) {
			toIndex = len + toIndex;
			if(toIndex < 0) {
				toIndex = len;
			}
		} else if(toIndex > len) {
			toIndex = len;
		}
		if (toIndex < fromIndex) {
			int tmp = fromIndex;
			fromIndex = toIndex;
			toIndex = tmp;
		}
		if (fromIndex == toIndex) {
			return EMPTY;
		}
		char[] strArray = string.toCharArray();
		char[] newStrArray = Arrays.copyOfRange(strArray, fromIndex, toIndex);
		return new String(newStrArray);
	}


	public static String subPre(String string, int toIndex) {
		return sub(string, 0, toIndex);
	}


	public static String subSuf(String string, int fromIndex) {
		if (isEmpty(string)) {
			return null;
		}
		return sub(string, fromIndex, string.length());
	}


	public static boolean isSurround(String str, String prefix, String suffix) {
		if (StrKit.isBlank(str)) {
			return false;
		}
		if (str.length() < (prefix.length() + suffix.length())) {
			return false;
		}

		return str.startsWith(prefix) && str.endsWith(suffix);
	}


	public static boolean isSurround(String str, char prefix, char suffix) {
		if (StrKit.isBlank(str)) {
			return false;
		}
		if (str.length() < 2) {
			return false;
		}

		return str.charAt(0) == prefix && str.charAt(str.length() - 1) == suffix;
	}


	public static String repeat(char c, int count) {
		char[] result = new char[count];
		for (int i = 0; i < count; i++) {
			result[i] = c;
		}
		return new String(result);
	}


	public static String repeat(String str, int count) {

		// 检查
		final int len = str.length();
		final long longSize = (long) len * (long) count;
		final int size = (int) longSize;
		if (size != longSize) {
			throw new ArrayIndexOutOfBoundsException("Required String length is too large: " + longSize);
		}

		final char[] array = new char[size];
		str.getChars(0, len, array, 0);
		int n;
		for (n = len; n < size - n; n <<= 1) {// n <<= 1相当于n *2
			System.arraycopy(array, 0, array, n, n);
		}
		System.arraycopy(array, 0, array, n, size - n);
		return new String(array);
	}


	public static boolean equals(String str1, String str2) {
		if (str1 == null) {
			return str2 == null;
		}

		return str1.equals(str2);
	}


	public static boolean equalsIgnoreCase(String str1, String str2) {
		if (str1 == null) {
			return str2 == null;
		}

		return str1.equalsIgnoreCase(str2);
	}


	public static String format(String template, Object... values) {
		if (CollectionKit.isEmpty(values) || isBlank(template)) {
			return template;
		}

		final StringBuilder sb = new StringBuilder();
		final int length = template.length();

		int valueIndex = 0;
		char currentChar;
		for (int i = 0; i < length; i++) {
			if (valueIndex >= values.length) {
				sb.append(sub(template, i, length));
				break;
			}

			currentChar = template.charAt(i);
			if (currentChar == '{') {
				final char nextChar = template.charAt(++i);
				if (nextChar == '}') {
					sb.append(values[valueIndex++]);
				} else {
					sb.append('{').append(nextChar);
				}
			} else {
				sb.append(currentChar);
			}

		}

		return sb.toString();
	}


	public static String format(String template, Map<?, ?> map) {
		if (null == map || map.isEmpty()) {
			return template;
		}

		for (Entry<?, ?> entry : map.entrySet()) {
			template = template.replace("{" + entry.getKey() + "}", entry.getValue().toString());
		}
		return template;
	}
	

	public static byte[] bytes(String str, String charset) {
		return bytes(str, isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset));
	}


	public static byte[] bytes(String str, Charset charset) {
		if (str == null) {
			return null;
		}

		if (null == charset) {
			return str.getBytes();
		}
		return str.getBytes(charset);
	}
	

	public static String str(byte[] bytes, String charset) {
		return str(bytes, isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset));
	}


	public static String str(byte[] data, Charset charset) {
		if (data == null) {
			return null;
		}

		if (null == charset) {
			return new String(data);
		}
		return new String(data, charset);
	}
	

	public static String str(ByteBuffer data, String charset){
		if(data == null) {
			return null;
		}
		
		return str(data, Charset.forName(charset));
	}
	

	public static String str(ByteBuffer data, Charset charset){
		if(null == charset) {
			charset = Charset.defaultCharset();
		}
		return charset.decode(data).toString();
	}
	

	public static ByteBuffer byteBuffer(String str, String charset) {
		return ByteBuffer.wrap(StrKit.bytes(str, charset));
	}


	public static String join(String conjunction, Object... objs) {
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (Object item : objs) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(conjunction);
			}
			sb.append(item);
		}
		return sb.toString();
	}
	

	public static String toUnderlineCase(String camelCaseStr) {
		if (camelCaseStr == null) {
			return null;
		}

		final int length = camelCaseStr.length();
		StringBuilder sb = new StringBuilder();
		char c;
		boolean isPreUpperCase = false;
		for (int i = 0; i < length; i++) {
			c = camelCaseStr.charAt(i);
			boolean isNextUpperCase = true;
			if (i < (length - 1)) {
				isNextUpperCase = Character.isUpperCase(camelCaseStr.charAt(i + 1));
			}
			if (Character.isUpperCase(c)) {
				if (!isPreUpperCase || !isNextUpperCase) {
					if (i > 0) sb.append(UNDERLINE);
				}
				isPreUpperCase = true;
			} else {
				isPreUpperCase = false;
			}
			sb.append(Character.toLowerCase(c));
		}
		return sb.toString();
	}


	public static String toCamelCase(String name) {
		if (name == null) {
			return null;
		}
		if (name.contains(UNDERLINE)) {
			name = name.toLowerCase();

			StringBuilder sb = new StringBuilder(name.length());
			boolean upperCase = false;
			for (int i = 0; i < name.length(); i++) {
				char c = name.charAt(i);

				if (c == '_') {
					upperCase = true;
				} else if (upperCase) {
					sb.append(Character.toUpperCase(c));
					upperCase = false;
				} else {
					sb.append(c);
				}
			}
			return sb.toString();
		} else
			return name;
	}


	public static String wrap(String str, String prefix, String suffix) {
		return format("{}{}{}", prefix, str, suffix);
	}


	public static boolean isWrap(String str, String prefix, String suffix) {
		return str.startsWith(prefix) && str.endsWith(suffix);
	}


	public static boolean isWrap(String str, String wrapper) {
		return isWrap(str, wrapper, wrapper);
	}


	public static boolean isWrap(String str, char wrapper) {
		return isWrap(str, wrapper, wrapper);
	}


	public static boolean isWrap(String str, char prefixChar, char suffixChar) {
		return str.charAt(0) == prefixChar && str.charAt(str.length() - 1) == suffixChar;
	}


	public static String padPre(String str, int minLength, char padChar) {
		if (str.length() >= minLength) {
			return str;
		}
		StringBuilder sb = new StringBuilder(minLength);
		for (int i = str.length(); i < minLength; i++) {
			sb.append(padChar);
		}
		sb.append(str);
		return sb.toString();
	}


	public static String padEnd(String str, int minLength, char padChar) {
		if (str.length() >= minLength) {
			return str;
		}
		StringBuilder sb = new StringBuilder(minLength);
		sb.append(str);
		for (int i = str.length(); i < minLength; i++) {
			sb.append(padChar);
		}
		return sb.toString();
	}


	public static StringBuilder builder() {
		return new StringBuilder();
	}


	public static StringBuilder builder(int capacity) {
		return new StringBuilder(capacity);
	}


	public static StringBuilder builder(String... strs) {
		final StringBuilder sb = new StringBuilder();
		for (String str : strs) {
			sb.append(str);
		}
		return sb;
	}


	public static StringReader getReader(String str) {
		return new StringReader(str);
	}


	public static StringWriter getWriter() {
		return new StringWriter();
	}


	public static byte[] encode(String str, String charset) {
		if (str == null) {
			return null;
		}

		if(isBlank(charset)) {
			return str.getBytes();
		}
		try {
			return str.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(format("Charset [{}] unsupported!", charset));
		}
	}


	public static String decode(byte[] data, String charset) {
		if (data == null) {
			return null;
		}

		if(isBlank(charset)) {
			return new String(data);
		}
		try {
			return new String(data, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(format("Charset [{}] unsupported!", charset));
		}
	}
}
