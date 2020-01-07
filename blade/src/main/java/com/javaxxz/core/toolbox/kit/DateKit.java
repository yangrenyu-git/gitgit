
package com.javaxxz.core.toolbox.kit;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.javaxxz.core.toolbox.Func;

public class DateKit {
	private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

	private final static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");

	private final static SimpleDateFormat sdfDays = new SimpleDateFormat("yyyyMMdd");

	private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private final static SimpleDateFormat sdfmsTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	private final static SimpleDateFormat allTime = new SimpleDateFormat("yyyyMMddHHmmss");



	public static String getYear() {
		return sdfYear.format(new Date());
	}


	public static String getYear(Date date) {
		return sdfYear.format(date);
	}


	public static String getDay() {
		return sdfDay.format(new Date());
	}


	public static String getDay(Date date) {
		return sdfDay.format(date);
	}


	public static String getDays() {
		return sdfDays.format(new Date());
	}


	public static String getDays(Date date) {
		return sdfDays.format(date);
	}


	public static String getTime() {
		return sdfTime.format(new Date());
	}
	

	public static String getMsTime() {
		return sdfmsTime.format(new Date());
	}
	

	public static String getAllTime() {
		return allTime.format(new Date());
	}


	public static String getTime(Date date) {
		return sdfTime.format(date);
	}


	public static boolean compareDate(String s, String e) {
		if (parseDate(s) == null || parseDate(e) == null) {
			return false;
		}
		return parseDate(s).getTime() >= parseDate(e).getTime();
	}


	public static Date parseDate(String date) {
		try {
			return sdfDay.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}


	public static Date parseTime(String date) {
		try {
			return sdfTime.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}


	public static Date parse(String date, String pattern) {
		DateFormat fmt = new SimpleDateFormat(pattern);
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}


	public static String format(Date date, String pattern) {
		DateFormat fmt = new SimpleDateFormat(pattern);
		return fmt.format(date);
	}


	public static Timestamp format(Date date) {
		return new java.sql.Timestamp(date.getTime());
	}


	public static boolean isValidDate(String s) {
		try {
			sdfTime.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}


	public static boolean isValidDate(String s, String pattern) {
		DateFormat fmt = new SimpleDateFormat(pattern);
		try {
			fmt.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}

	public static int getDiffYear(String startTime, String endTime) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(
					startTime).getTime()) / (1000 * 60 * 60 * 24)) / 365);
			return years;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return 0;
		}
	}


	public static long getDaySub(String beginDateStr, String endDateStr) {
		long day = 0;
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		java.util.Date beginDate = null;
		java.util.Date endDate = null;

		try {
			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
		// System.out.println("相隔的天数="+day);

		return day;
	}


	public static String getAfterDayDate(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdfd.format(date);

		return dateStr;
	}


	public static String getAfterDayWeek(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("E");
		String dateStr = sdf.format(date);

		return dateStr;
	}


	public static String buildDateValue(Object value){
		if(Func.isOracle()){
			return "to_date('"+ value +"','yyyy-mm-dd HH24:MI:SS')";
		}else{
			return Func.toStr(value);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(getDays());
		System.out.println(getAfterDayWeek("3"));
	}

}
