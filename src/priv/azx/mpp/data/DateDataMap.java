package priv.azx.mpp.data;

import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import priv.azx.mpp.util.TimeConverter;

public class DateDataMap {

	public static Map<String, DateData> buildStringMap() {
		Map<String, DateData> dateDataMap = new TreeMap<String, DateData>(new Comparator<String>() {
			public int compare(String string1, String string2) {
				Date date1 = TimeConverter.stringToDate(string1);
				Date date2 = TimeConverter.stringToDate(string2);
				return date1.compareTo(date2);
			}
		});
		return dateDataMap;
	}

	public static Map<Date, DateData> buildDateMap() {
		Map<Date, DateData> dateDataMap = new TreeMap<Date, DateData>(new Comparator<Date>() {
			public int compare(Date date1, Date date2) {
				return date1.compareTo(date2);
			}
		});
		return dateDataMap;
	}
	
	public static Map<Date, DateData> buildDateCollection() {
		Map<Date, DateData> dateDataMap = new TreeMap<Date, DateData>(new Comparator<Date>() {
			public int compare(Date date1, Date date2) {
				return date1.compareTo(date2);
			}
		});
		return dateDataMap;
	}

}
