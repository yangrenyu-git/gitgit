package com.javaxxz.core.toolbox.kit;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;

import com.javaxxz.core.exception.ToolBoxException;
import com.javaxxz.core.toolbox.kit.StrKit;
import com.javaxxz.core.toolbox.support.DateTime;


public class DateTimeKit {

	public final static long MS = 1;

	public final static long SECOND_MS = MS * 1000;

	public final static long MINUTE_MS = SECOND_MS * 60;

	public final static long HOUR_MS = MINUTE_MS * 60;

	public final static long DAY_MS = HOUR_MS * 24;


	public final static String NORM_DATE_PATTERN = "yyyy-MM-dd";

	public final static String NORM_TIME_PATTERN = "HH:mm:ss";

	public final static String NORM_DATETIME_MINUTE_PATTERN = "yyyy-MM-dd HH:mm";

	public final static String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public final static String NORM_DATETIME_MS_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

	public final static String HTTP_DATETIME_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";


//	private final static SimpleDateFormat NORM_DATE_FORMAT = new SimpleDateFormat(NORM_DATE_PATTERN);
	private static ThreadLocal<SimpleDateFormat> NORM_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>(){
		synchronized protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(NORM_DATE_PATTERN);
		};
	};

//	private final static SimpleDateFormat NORM_TIME_FORMAT = new SimpleDateFormat(NORM_TIME_PATTERN);
	private static ThreadLocal<SimpleDateFormat> NORM_TIME_FORMAT = new ThreadLocal<SimpleDateFormat>(){
		synchronized protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(NORM_TIME_PATTERN);
		};
	};

//	private final static SimpleDateFormat NORM_DATETIME_FORMAT = new SimpleDateFormat(NORM_DATETIME_PATTERN);
	private static ThreadLocal<SimpleDateFormat> NORM_DATETIME_FORMAT = new ThreadLocal<SimpleDateFormat>(){
		synchronized protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(NORM_DATETIME_PATTERN);
		};
	};

//	private final static SimpleDateFormat HTTP_DATETIME_FORMAT = new SimpleDateFormat(HTTP_DATETIME_PATTERN, Locale.US);
	private static ThreadLocal<SimpleDateFormat> HTTP_DATETIME_FORMAT = new ThreadLocal<SimpleDateFormat>(){
		synchronized protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(HTTP_DATETIME_PATTERN, Locale.US);
		};
	};


	public static String now() {
		return formatDateTime(new DateTime());
	}


	public static String today() {
		return formatDate(new DateTime());
	}
	

	public static int thisMonth() {
		return month(date());
	}
	

	public static int thisYear() {
		return year(date());
	}
	

	public static DateTime date() {
		return new DateTime();
	}


	public static DateTime date(long date) {
		return new DateTime(date);
	}


	public static Calendar toCalendar(Date date){
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	

	public static int month(Date date){
		return toCalendar(date).get(Calendar.MONTH) + 1;
	}
	

	public static int year(Date date){
		return toCalendar(date).get(Calendar.YEAR);
	}
	

	public static int season(Date date){
		return toCalendar(date).get(Calendar.MONTH) / 3 +1;
	}
	

	public static String yearAndSeason(Date date) {
		return yearAndSeason(toCalendar(date));
	}


	public static LinkedHashSet<String> yearAndSeasons(Date startDate, Date endDate) {
		final LinkedHashSet<String> seasons = new LinkedHashSet<String>();
		if(startDate == null || endDate == null) {
			return seasons;
		}


		final Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		while(true) {
			//如果开始时间超出结束时间，让结束时间为开始时间，处理完后结束循环
			if(startDate.after(endDate)) {
				startDate = endDate;
			}

			seasons.add(yearAndSeason(cal));

			if(startDate.equals(endDate)) {
				break;
			}

			cal.add(Calendar.MONTH, 3);
			startDate = cal.getTime();
		}

		return seasons;
	}
	// ------------------------------------ Format start ----------------------------------------------

	public static String format(Date date, String format){
		return new SimpleDateFormat(format).format(date);
	}


	public static String formatDateTime(Date date) {
		return NORM_DATETIME_FORMAT.get().format(date);
	}


	public static String formatDate(Date date) {
		return NORM_DATE_FORMAT.get().format(date);
	}


	public static String formatHttpDate(Date date) {
		return HTTP_DATETIME_FORMAT.get().format(date);
	}
	// ------------------------------------ Format end ----------------------------------------------

	// ------------------------------------ Parse start ----------------------------------------------


	public static DateTime parse(String dateStr, SimpleDateFormat simpleDateFormat) {
		try {
			return new DateTime(simpleDateFormat.parse(dateStr));
		} catch (Exception e) {
			throw new ToolBoxException(StrKit.format("Parse [{}] with format [{}] error!", dateStr, simpleDateFormat.toPattern()), e);
		}
	}


	public static DateTime parse(String dateString, String format){
		return parse(dateString, new SimpleDateFormat(format));
	}


	public static DateTime parseDateTime(String dateString){
		return parse(dateString, NORM_DATETIME_FORMAT.get());
	}


	public static DateTime parseDate(String dateString){
		return parse(dateString, NORM_DATE_FORMAT.get());
	}


	public static DateTime parseTime(String timeString){
		return parse(timeString, NORM_TIME_FORMAT.get());
	}


	public static DateTime parse(String dateStr) {
		if(null == dateStr) {
			return null;
		}
		dateStr = dateStr.trim();
		int length = dateStr.length();
		try {
			if(length == NORM_DATETIME_PATTERN.length()) {
				return parseDateTime(dateStr);
			}else if(length == NORM_DATE_PATTERN.length()) {
				return parseDate(dateStr);
			}else if(length == NORM_TIME_PATTERN.length()){
				return parseTime(dateStr);
			}else if(length == NORM_DATETIME_MINUTE_PATTERN.length()){
				return parse(dateStr, NORM_DATETIME_MINUTE_PATTERN);
			}else if(length >= NORM_DATETIME_MS_PATTERN.length() -2){
				return parse(dateStr, NORM_DATETIME_MS_PATTERN);
			}
		}catch(Exception e) {
			throw new ToolBoxException(StrKit.format("Parse [{}] with format normal error!", dateStr));
		}

		//没有更多匹配的时间格式
		throw new ToolBoxException(StrKit.format(" [{}] format is not fit for date pattern!", dateStr));
	}
	// ------------------------------------ Parse end ----------------------------------------------

	// ------------------------------------ Offset start ----------------------------------------------

	public static DateTime getBeginTimeOfDay(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new DateTime(calendar.getTime());
	}


	public static DateTime getEndTimeOfDay(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return new DateTime(calendar.getTime());
	}


	public static DateTime yesterday() {
		return offsiteDay(new DateTime(), -1);
	}


	public static DateTime lastWeek() {
		return offsiteWeek(new DateTime(), -1);
	}


	public static DateTime lastMouth() {
		return offsiteMonth(new DateTime(), -1);
	}


	public static DateTime offsiteDay(Date date, int offsite) {
		return offsiteDate(date, Calendar.DAY_OF_YEAR, offsite);
	}


	public static DateTime offsiteWeek(Date date, int offsite) {
		return offsiteDate(date, Calendar.WEEK_OF_YEAR, offsite);
	}


	public static DateTime offsiteMonth(Date date, int offsite) {
		return offsiteDate(date, Calendar.MONTH, offsite);
	}


	public static DateTime offsiteDate(Date date, int calendarField, int offsite){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(calendarField, offsite);
		return new DateTime(cal.getTime());
	}
	// ------------------------------------ Offset end ----------------------------------------------


	public static long diff(Date subtrahend, Date minuend, long diffField){
	  long diff = minuend.getTime() - subtrahend.getTime();
	  return diff/diffField;
	}


	public static long spendNt(long preTime) {
		return System.nanoTime() - preTime;
	}


	public static long spendMs(long preTime) {
		return System.currentTimeMillis() - preTime;
	}


	public static int toIntSecond(Date date) {
		return Integer.parseInt(DateTimeKit.format(date, "yyMMddHHmm"));
	}


	public static int weekCount(Date start, Date end) {
		final Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(start);
		final Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(end);

		final int startWeekofYear = startCalendar.get(Calendar.WEEK_OF_YEAR);
		final int endWeekofYear = endCalendar.get(Calendar.WEEK_OF_YEAR);

		int count = endWeekofYear - startWeekofYear + 1;

		if(Calendar.SUNDAY != startCalendar.get(Calendar.DAY_OF_WEEK)) {
			count --;
		}

		return count;
	}

	//------------------------------------------------------------------------ Private method start

	private static String yearAndSeason(Calendar cal) {
		return new StringBuilder().append(cal.get(Calendar.YEAR)).append(cal.get(Calendar.MONTH) / 3 + 1).toString();
	}
	//------------------------------------------------------------------------ Private method end
}
