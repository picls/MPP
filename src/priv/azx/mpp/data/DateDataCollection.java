package priv.azx.mpp.data;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import priv.azx.mpp.util.TimeConverter;

public class DateDataCollection {

	public String code;
	public Map<Date, DateData> dateDataMap;
	public Date firstDate;
	public Date lastDate;

	public DateDataCollection(String code) {
		Map<Date, DateData> dateDataMap = null;
		try {
			dateDataMap = PersistenceService.get(code);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.code = code;
		this.dateDataMap = dateDataMap;
		if (dateDataMap != null)
			for (Date date : dateDataMap.keySet()) {
				if (firstDate == null)
					firstDate = date;
				lastDate = date;
			}
		System.out.println("DateDataCollection " + code + " has done.");
	}

	public DateDataCollection(String code, Map<Date, DateData> dateDataMap) {
		this.code = code;
		this.dateDataMap = dateDataMap;
		if (dateDataMap != null)
			for (Date date : dateDataMap.keySet()) {
				if (firstDate == null)
					firstDate = date;
				lastDate = date;
			}

	}

	public boolean isExist() {
		return (dateDataMap != null) ? true : false;
	}

	public DateData get(String date) {
		return dateDataMap.get(TimeConverter.stringToDate(date));
	}

	public DateData get(Date date) {
		return dateDataMap.get(date);
	}

	public DateData get(String date, int days) {
		List<DateData> dateDataList = gets(date, days);
		return dateDataList.get(days - 1);
	}

	public DateData get(Date date, int days) {
		List<DateData> dateDataList = gets(date, days);
		return dateDataList.get(days - 1);
	}
	
	public DateData get(DateData dateData, int days) {
		List<DateData> dateDataList = gets(dateData, days);
		return dateDataList.get(days - 1);
	}

	public List<DateData> gets(String date, int days) {
		return gets(get(date), days);
	}

	public List<DateData> gets(Date date, int days) {
		return gets(get(date), days);
	}

	public List<DateData> gets(DateData dateData, int days) {
		List<DateData> resultList = new ArrayList<DateData>();
		int step = (int) (Math.floor(days / 10000d) + Math.ceil(days / 10000d));
		int steps = 0;
		Date nextDate = TimeConverter.dateAdd(dateData.date, step);
		while (steps < Math.abs(days)) {
			DateData nextDateData = dateDataMap.get(nextDate);
			if (nextDateData != null) {
				resultList.add(nextDateData);
				steps++;
			}
			nextDate = TimeConverter.dateAdd(nextDate, step);
			if (TimeConverter.dateCompare(nextDate, lastDate) > 0)
				break;
		}
		return resultList;
	}

	public DateData getFirst() {
		return dateDataMap.get(firstDate);
	}

	public DateData getLast() {
		return dateDataMap.get(lastDate);
	}

	public DateData next(DateData dateData) {
		/*
		 * DateData nextDateData = null; Date nextDate =
		 * TimeConverter.dateAdd(dateData.date, 1); while (nextDateData == null)
		 * { nextDateData = dateDataMap.get(nextDate); nextDate =
		 * TimeConverter.dateAdd(nextDate, 1); if
		 * (TimeConverter.dateCompare(nextDate, lastDate) > 0) break; } return
		 * nextDateData;
		 */
		return move(dateData, 1);
	}

	public DateData last(DateData dateData) {
		return move(dateData, -1);
	}

	public DateData move(DateData dateData, int days) {
		/*
		 * DateData finalDateData = null; int step = (int) (Math.floor(days /
		 * 10000d) + Math.ceil(days / 10000d)); int steps = 0; Date nextDate =
		 * TimeConverter.dateAdd(dateData.date, step); while(steps < days) {
		 * DateData nextDateData = dateDataMap.get(nextDate); if(nextDateData !=
		 * null) {
		 * 
		 * } }
		 */
		List<DateData> dateDataList = gets(dateData, days);
		if (dateDataList.size() == Math.abs(days))
			return dateDataList.get(dateDataList.size() - 1);
		return null;
	}

	public boolean isOpen(String date) {
		DateData dateData = get(date);
		if (dateData != null)
			return true;
		return false;
	}

	public boolean isOpen(Date date) {
		return isOpen(TimeConverter.dateToString(date));
	}

	public boolean isOpen(DateData dateData) {
		return isOpen(dateData.date);
	}

	public boolean isHalt(String date) {
		DateData shDateData = DateDataService.SZDateDataCollection.get(date);
		DateData dateData = get(date);
		if ((shDateData != null) && (dateData == null))
			return true;
		return false;
	}

	public boolean isHalt(Date date) {
		return isHalt(TimeConverter.dateToString(date));
	}

	public boolean isHalt(DateData dateData) {
		return isHalt(dateData.date);
	}

	/*
	 * public boolean isResume(String date) { return isResume(get(date)); }
	 * 
	 * public boolean isResume(Date date) { return isResume(get(date)); }
	 * 
	 * public boolean isResume(DateData dateData) { if ((!isHalt(dateData)) &&
	 * (isHalt(last(dateData)))) return true; return false; }
	 */

	public static void main(String args[]) {
		DateDataCollection d = new DateDataCollection("sz002193");
		System.out.println(d.isOpen("20151121"));
		System.out.println(d.isHalt("20151121"));
	}

}
