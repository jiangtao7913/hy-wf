/*
 * Copyright 2017-2018 All rights reserved.
 * Support: JinXing
 * License: 
*/
package com.hy.wf.common;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Utils - 日期处理 <br/>
 *
 * @author JinXing Team
 * @email
 * @date: 2018-4-1 下午10:58:43
 * @version 1.0
 */
public class DateUtils {
	
	/** 时间格式(yyyy-MM-dd) */
	public final static String DATE_PATTERN = "yyyy-MM-dd";
	/** 时间格式(yyyy-MM-dd HH:mm:ss) */
	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static String format(Date date, String pattern) {
        if(date != null){
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }
    
    /**
   	 * 将日期转换为当天最小的日期时间
   	 *
   	 * @param date
   	 * @return
   	 */
    public static Date toDayMin(String date) {
   		Date start = null;
   		if (StringUtils.isNotEmpty(date)) {
   			try {
   				start = org.apache.commons.lang.time.DateUtils.parseDate(date, Constant.DATE_PATTERNS);
   				start = toDayMin(start);
   			} catch (ParseException exception) {
   				exception.printStackTrace();
   			}
   		}
   		return start;
   	}
   	
   	/**
   	 * 将日期转换为当天最小的日期时间
   	 *
   	 * @param date
   	 * @return
   	 */
   	public static Date toDayMin(Date date) {
   		date = org.apache.commons.lang.time.DateUtils.setHours(date, 0);
   		date = org.apache.commons.lang.time.DateUtils.setMinutes(date, 0);
   		date = org.apache.commons.lang.time.DateUtils.setSeconds(date, 0);
   		
   		return date;
   	}
   	
   	/**
   	 * 将日期转换为当有最小的日期时间
   	 *
   	 * @param date
   	 * @return
   	 */
   	public static Date toMonthMin(String date) {
   		Date start = null;
   		if (StringUtils.isNotEmpty(date)) {
   			try {
   				start = org.apache.commons.lang.time.DateUtils.parseDate(date, Constant.DATE_PATTERNS);
   				start = toMonthMin(start);
   			} catch (ParseException exception) {
   				exception.printStackTrace();
   			}
   		}
   		return start;
   	}
   	
   	/**
   	 * 将日期转换为当月最小的日期时间
   	 *
   	 * @param date
   	 * @return
   	 */
   	public static Date toMonthMin(Date date) {
   		date = org.apache.commons.lang.time.DateUtils.setDays(date, 1);
   		date = org.apache.commons.lang.time.DateUtils.setHours(date, 0);
   		date = org.apache.commons.lang.time.DateUtils.setMinutes(date, 0);
   		date = org.apache.commons.lang.time.DateUtils.setSeconds(date, 0);
   		
   		return date;
   	}
   	
   	/**
   	 * 将日期转换为当天最大的日期时间
   	 *
   	 * @param date
   	 * @return
   	 */
   	public static Date toDayMax(String date) {
   		Date end = null;
   		if (StringUtils.isNotEmpty(date)) {
   			try {
   				end = org.apache.commons.lang.time.DateUtils.parseDate(date, Constant.DATE_PATTERNS);
   				end = toDayMax(end);
   			} catch (ParseException exception) {
   				exception.printStackTrace();
   			}
   		}
   		return end;
   	}
   	
   	/**
   	 * 将日期转换为当天最大的日期时间
   	 *
   	 * @param date
   	 * @return
   	 */
   	public static Date toDayMax(Date date) {
   		date = org.apache.commons.lang.time.DateUtils.setHours(date, 23);
   		date = org.apache.commons.lang.time.DateUtils.setMinutes(date, 59);
   		date = org.apache.commons.lang.time.DateUtils.setSeconds(date, 59);
   		
   		return date;
   	}
   	
   	/**
   	 * 将日期转换为当月最大的日期时间
   	 *
   	 * @param date
   	 * @return
   	 */
   	public static Date toMonthMax(String date) {
   		Date end = null;
   		if (StringUtils.isNotEmpty(date)) {
   			try {
   				end = org.apache.commons.lang.time.DateUtils.parseDate(date, Constant.DATE_PATTERNS);
   				end = toMonthMax(end);
   			} catch (ParseException exception) {
   				exception.printStackTrace();
   			}
   		}
   		return end;
   	}
   	
   	/**
   	 * 将日期转换为当月最大的日期时间
   	 *
   	 * @param date
   	 * @return
   	 */
   	public static Date toMonthMax(Date date) {
   		Calendar calendar = Calendar.getInstance();
   		calendar.setTime(date);
   		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
   		date = calendar.getTime();
   		date = org.apache.commons.lang.time.DateUtils.setHours(date, 23);
   		date = org.apache.commons.lang.time.DateUtils.setMinutes(date, 59);
   		date = org.apache.commons.lang.time.DateUtils.setSeconds(date, 59);
   		
   		return date;
   	}
   	
   	/**
   	 * 获取Calendar Field
   	 *
   	 * @param date
   	 * @param field
   	 * @return
   	 */
   	public static Integer getCalendarField(Date date, Integer field) {
   		Calendar calendar = Calendar.getInstance();
   		calendar.setTime(date);
   		
   		return calendar.get(field);
   	}
   	
   	/**
   	 * 获取本周的周日
   	 *
   	 * @param date
   	 * @return
   	 */
   	public static Date getSunday(Date date) {
   		Calendar calendar = Calendar.getInstance();
   		calendar.setTime(date);
   		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
   		if (dayOfWeek == 0) {
   			dayOfWeek = 7;
   		}
   		calendar.add(Calendar.DATE, -dayOfWeek + 7);
   		
   		return calendar.getTime();
   	}
   	
   	/**
   	 * 两个日期相距天数
   	 *
   	 * @param date1 小的时间
   	 * @param date2 大的时间
   	 * @return
   	 */
   	public static int distance(Date date1, Date date2) {
   		int distance = 0;
   		
   		Calendar calendar1 = Calendar.getInstance();
   		calendar1.setTime(date1);

   		Calendar calendar2 = Calendar.getInstance();
   		calendar2.setTime(date2);
   		
   		int day1 = calendar1.get(Calendar.DAY_OF_YEAR);
   		int day2 = calendar2.get(Calendar.DAY_OF_YEAR);

   		int year1 = calendar1.get(Calendar.YEAR);
   		int year2 = calendar2.get(Calendar.YEAR);
   		
   		if (year1 != year2) { // 同一年
   			for (int i = year1; i < year2; i++) {
   				if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) { // 闰年
   					distance += 366;
   				} else { // 不是闰年
   					distance += 365;
   				}
   			}
   			distance = distance + (day2 - day1);
   		} else { // 不同年
   			distance = day2 - day1;
   		}
   		
   		return distance;
   	}
   	
   	/**
   	 * 获取当前日期的前N天
   	 */
 	public static String getAheadDay(int day,Date date) {
 		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -day);
		date = calendar.getTime();
		return format(date);
 	}
 	
 	public static Date getDateAhead(int day,Date date) {
 		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -day);
		date = calendar.getTime();
		return date;
 	}

	public static Date getMonthAhead(int day,Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -day);
		date = calendar.getTime();
		return date;
	}

	public static Date getYearAhead(int day,Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, -day);
		date = calendar.getTime();
		return date;
	}

 	
 	public static String getMonth(int day,Date date) {
 		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -day);
		date = calendar.getTime();
		return format(date,"yyyy-MM");
 	}
 	
 	public static void main(String[] args) {
		System.out.println(getMonthAhead(-2,new Date()));
	}
 	
}
