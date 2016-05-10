package priv.azx.mpp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeConverter {

	public static String dateToString(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		return simpleDateFormat.format(date);
	}

	public static Date stringToDate(String string) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		try {
			return (Date) simpleDateFormat.parse(string);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static Date dateAdd(String string, int days) {
		return dateAdd(stringToDate(string), days);
	}

	public static Date dateAdd(Date date, int days) {
		long time = date.getTime();
		time = time + 1000l * 60 * 60 * 24 * days;
		return new Date(time);
	}

	private static int intCompare(int a, int b) {
		if (a > b)
			return 1;
		if (a < b)
			return -1;
		return 0;
	}

	@SuppressWarnings("deprecation")
	public static int dateCompare(Date d1, Date d2) {
		int yearCompare = intCompare(d1.getYear(), d2.getYear());
		if (yearCompare != 0)
			return yearCompare;

		int monthCompare = intCompare(d1.getMonth(), d2.getMonth());
		if (monthCompare != 0)
			return monthCompare;

		int dateCompare = intCompare(d1.getDate(), d2.getDate());
		return dateCompare;
	}

}
