package city.smash.union.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumberUtils {
	public static String addSeperators (Double i) {
		DecimalFormat decimalFormat = new DecimalFormat("#.0");
		decimalFormat.setGroupingUsed(true);
		decimalFormat.setGroupingSize(3);
		return decimalFormat.format(i);
	}
	public static String addSeperators (long i) {
		NumberFormat myFormat = NumberFormat.getInstance();
		myFormat.setGroupingUsed(true);
		return myFormat.format(i);
	}
}
