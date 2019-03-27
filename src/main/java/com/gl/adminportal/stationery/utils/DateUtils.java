package com.gl.adminportal.stationery.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

	public static DateFormat defaultDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");

	public static Date getCurrentDate() {
		Date date = new Date(System.currentTimeMillis());
		return date;
	}

	public static Date getCurrentDateWithTime() {
		Date date = new Date(System.currentTimeMillis());
		return date;
	}

	
	public static String convertDateToString(Date date) {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		df.format(date);
		return df.format(date);
	}

	public static Date getDateFromString(String stringDate) throws ParseException {
		
	//	String startDateString = "06/27/2007";
	    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
	    Date startDate = null;
	    try {
	        startDate = df.parse(stringDate);
	    } catch (ParseException e) {
	    	e.printStackTrace();
	    }
		return startDate;
	}

	/**
	 * This method gets date that is one month prior to today' date.
	 * 
	 * @return
	 */
	public static Date getOneMonthPriorDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		return calendar.getTime();
	}

	public static String formatDateUsing(Date date) {
		return defaultDateFormat.format(date);
	}

/*	public static void main(String[] args) {
		try {
			System.out.print(DateUtils.getDateFromString("01/01/2017"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
*/
}
