package com.javaxxz.core.toolbox.kit;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.javaxxz.core.toolbox.support.Convert;
import com.javaxxz.core.toolbox.support.FieldValidator;


public class ReKit {
	

	public final static String RE_CHINESE = "[\u4E00-\u9FFF]";
	

	public final static Pattern GROUP_VAR =  Pattern.compile("\\$(\\d+)");
	

	public final static Set<Character> RE_KEYS = CollectionKit.newHashSet(new Character[]{'$', '(', ')', '*', '+', '.', '[', ']', '?', '\\', '^', '{', '}', '|'});
	

	public final static String regExp_integer_1 = "^\\d+$";


	public final static String regExp_integer_2 = "^[0-9]*[1-9][0-9]*$";


	public final static String regExp_integer_3 = "^((-\\d+) ?(0+))$";


	public final static String regExp_integer_4 = "^-[0-9]*[1-9][0-9]*$";


	public final static String regExp_integer_5 = "^-?\\d+$";


	public final static String regExp_float_1 = "^\\d+(\\.\\d+)?$";


	public final static String regExp_float_2 = "^(([0-9]+\\.[0-9]*[1-9][0-9]*) ?([0-9]*[1-9][0-9]*\\.[0-9]+) ?([0-9]*[1-9][0-9]*))$";


	public final static String regExp_float_3 = "^((-\\d+(\\.\\d+)?) ?(0+(\\.0+)?))$";


	public final static String regExp_float_4 = "^(-(([0-9]+\\.[0-9]*[1-9][0-9]*) ?([0-9]*[1-9][0-9]*\\.[0-9]+) ?([0-9]*[1-9][0-9]*)))$";


	public final static String regExp_float_5 = "^(-?\\d+)(\\.\\d+)?$";


	public final static String regExp_letter_1 = "^[A-Za-z]+$";


	public final static String regExp_letter_2 = "^[A-Z]+$";


	public final static String regExp_letter_3 = "^[a-z]+$";


	public final static String regExp_letter_4 = "^[A-Za-z0-9]+$";


	public final static String regExp_letter_5 = "^\\w+$";


	public final static String regExp_email = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$";


	public final static String regExp_url_1 = "^[a-zA-z]+://(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(\\?\\S*)?$";


	public final static String regExp_url_2 = "[a-zA-z]+://[^\\s]*";


	public final static String regExp_chinese_1 = "[\\u4e00-\\u9fa5]";


	public final static String regExp_chinese_2 = "[^\\x00-\\xff]";


	public final static String regExp_line = "\\n[\\s ? ]*\\r";


	public final static String regExp_html_1 = "/ <(.*)>.* <\\/\\1> ? <(.*) \\/>/";


	public final static String regExp_startEndEmpty = "(^\\s*) ?(\\s*$)";


	public final static String regExp_accountNumber = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";


	public final static String regExp_telephone = "\\d{3}-\\d{8} ?\\d{4}-\\d{7}";


	public final static String regExp_qq = "[1-9][0-9]{4,}";


	public final static String regExp_postbody = "[1-9]\\d{5}(?!\\d)";


	public final static String regExp_idCard = "\\d{15} ?\\d{18}";


	public final static String regExp_ip = "\\d+\\.\\d+\\.\\d+\\.\\d+";
	
	private ReKit() {
		//阻止实例化
	}


	public static String get(String regex, String content, int groupIndex) {
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		return get(pattern, content, groupIndex);
	}
	

	public static String get(Pattern pattern, String content, int groupIndex) {
		if(null == content){
			return null;
		}
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			return matcher.group(groupIndex);
		}
		return null;
	}
	

	public static String extractMulti(Pattern pattern, String content, String template) {
		HashSet<String> varNums = findAll(GROUP_VAR, template, 1, new HashSet<String>());
		
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			for (String var : varNums) {
				int group = Integer.parseInt(var);
				template = template.replace("$" + var, matcher.group(group));
			}
			return template;
		}
		return null;
	}
	

	public static String extractMultiAndDelPre(Pattern pattern, String[] contents, String template) {
		HashSet<String> varNums = findAll(GROUP_VAR, template, 1, new HashSet<String>());
		
		final String content = contents[0];
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			for (String var : varNums) {
				int group = Integer.parseInt(var);
				template = template.replace("$" + var, matcher.group(group));
			}
			contents[0] = StrKit.sub(content, matcher.end(), content.length());
			return template;
		}
		return null;
	}
	

	public static String extractMulti(String regex, String content, String template) {
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		return extractMulti(pattern, content, template);
	}
	

	public static String extractMultiAndDelPre(String regex, String[] contents, String template) {
		final Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		return extractMultiAndDelPre(pattern, contents, template);
	}


	public static String delFirst(String regex, String content) {
		return content.replaceFirst(regex, "");
	}


	public static String delPre(String regex, String content) {
		Matcher matcher = Pattern.compile(regex, Pattern.DOTALL).matcher(content);
		if (matcher.find()) {
			return StrKit.sub(content, matcher.end(), content.length());
		}
		return content;
	}


	public static <T extends Collection<String>> T findAll(String regex, String content, int group, T collection) {
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		return findAll(pattern, content, group, collection);
	}
	

	public static <T extends Collection<String>> T findAll(Pattern pattern, String content, int group, T collection) {
		Matcher matcher = pattern.matcher(content);
		while(matcher.find()){
			collection.add(matcher.group(group));
		}
		return collection;
	}


	public static Integer getFirstNumber(String StringWithNumber) {
		return Convert.toInt(get(FieldValidator.NUMBER, StringWithNumber, 0), null);
	}
	

	public static boolean isMatch(String regex, String content) {
		if(content == null) {
			//提供null的字符串为不匹配
			return false;
		}
		
		if(StrKit.isBlank(regex)) {
			//正则不存在则为全匹配
			return true;
		}
		
		return Pattern.matches(regex, content);
	}
	

	public static boolean isMatch(Pattern pattern, String content) {
		if(content == null || pattern == null) {
			//提供null的字符串为不匹配
			return false;
		}
		return pattern.matcher(content).matches();
	}
	

	public static String replaceAll(String content, String regex, String replacementTemplate) {
		if(StrKit.isBlank(content)){
			return content;
		}
		
		final Matcher matcher = Pattern.compile(regex, Pattern.DOTALL).matcher(content);
		matcher.reset();
		boolean result = matcher.find();
		if (result) {
			final Set<String> varNums = findAll(GROUP_VAR, replacementTemplate, 1, new HashSet<String>());
			final StringBuffer sb = new StringBuffer();
			do {
				String replacement = replacementTemplate;
				for (String var : varNums) {
					int group = Integer.parseInt(var);
					replacement = replacement.replace("$" + var, matcher.group(group));
				}
				matcher.appendReplacement(sb, escape(replacement));
				result = matcher.find();
			} while (result);
			matcher.appendTail(sb);
			return sb.toString();
		}
		return content;
	}
	

	public static String escape(String content) {
		if(StrKit.isBlank(content)){
			return content;
		}
		
		final StringBuilder builder = new StringBuilder();
		char current;
		for(int i = 0; i < content.length(); i++) {
			current = content.charAt(i);
			if(RE_KEYS.contains(current)) {
				builder.append('\\');
			}
			builder.append(current);
		}
		return builder.toString();
	}
	

	public static boolean isExist(String regex, String str) {
		Pattern pat = Pattern.compile(regex);
		Matcher mat = pat.matcher(str);
		return mat.find();
	}


	public static String replaceAll(String regex, String ment, String str,
			int flags) {
		Pattern pat = null;
		if (flags == -1) {
			pat = Pattern.compile(regex);
		} else {
			pat = Pattern.compile(regex, flags);
		}
		Matcher mat = pat.matcher(str);
		return mat.replaceAll(ment);
	}


	public static String[] getMatcherValue(String regex, String str) {
		return getMatcherValue(regex, str, Pattern.CASE_INSENSITIVE);
	}


	public static String[] getMatcherValue(String regex, String str, int flags) {
		Pattern pat = null;
		if (flags == -1) {
			pat = Pattern.compile(regex);
		} else {
			pat = Pattern.compile(regex, flags);
		}
		Matcher mat = pat.matcher(str);
		if (mat.find()) {
			String[] param = new String[mat.groupCount()];
			for (int i = 0; i < mat.groupCount(); i++) {
				param[i] = mat.group(i + 1);
			}
			return param;
		}
		return null;
	}


	public static boolean isTrue(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(str);
		return match.matches();
	}
}
