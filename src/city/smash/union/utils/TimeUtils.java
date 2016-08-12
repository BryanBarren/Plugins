package city.smash.union.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

public class TimeUtils {
	
	public static String milisToDateString(Long milis) {
		SimpleDateFormat dateFormat = new SimpleDateFormat ("MM/dd/yyyy");
		return dateFormat.format(new Date(milis));
	}
	
	public static String milisToDurationString(Long milis) {
		milis = Math.abs(milis);
		
		if (milis != 0) {
			milis += 60*1000;
		}
		
		double multi;
		
		multi = (24 * 60 * 60 * 1000);
		long day = (long)Math.floor(milis / multi);
		milis -= (long)(day * multi);
		
		multi = (60 * 60 * 1000);
		long hour = (long)Math.floor(milis / multi);
		milis -= (long)(hour * multi);
		
		multi = (60 * 1000);
		long min = (long)Math.floor(milis / multi);
		milis -= (long)(min * multi);
		
		String dayString;
		if (day == 1) {
			dayString = " day ";
		} else {
			dayString = " days ";
		}
		
		String hourString;
		if (hour == 1) {
			hourString = " hour ";
		} else {
			hourString = " hours ";
		}
		
		String minString;
		if (min == 1) {
			minString = " minute ";
		} else {
			minString = " minutes ";
		}
		String result;
		if (day == 0) {
			if (hour == 0) {
				result = min + minString;
			} else {
				if (min == 0) {
					result = + hour + hourString;
				} else {
					result = + hour + hourString + "and " + min + minString;
				}
			}
		} else {
			result = day + dayString.substring(0, dayString.length()-1) + ", " + hour + hourString + "and " + min + minString;
		}
		return StringUtils.trim(result);
	}
	
	public static long getStartOfHour() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(System.currentTimeMillis()));
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	public static long getStartOfDay() {
		return getStartOfDay(TimeZone.getDefault());
	}
	public static long getStartOfDay(TimeZone tz) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(tz);
		calendar.setTime(new Date(System.currentTimeMillis()));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}
	public static long getStartOfWeek() {
		return getStartOfWeek(TimeZone.getDefault());
	}
	public static long getStartOfWeek(TimeZone tz) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(tz);
		calendar.setTime(new Date(System.currentTimeMillis()));
		calendar.set(Calendar.DAY_OF_WEEK, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}
	public static long getStartOfMonth() {
		return getStartOfMonth(TimeZone.getDefault());
	}
	public static long getStartOfMonth(TimeZone tz) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(tz);
		calendar.setTime(new Date(System.currentTimeMillis()));
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}
	public static long getStartOfNextMonth() {
		return getStartOfNextMonth(TimeZone.getDefault());
	}
	public static long getStartOfNextMonth(TimeZone tz) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(tz);
		calendar.setTime(new Date(System.currentTimeMillis()));
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}
}
