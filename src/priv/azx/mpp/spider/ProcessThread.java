package priv.azx.mpp.spider;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import priv.azx.mpp.data.DateData;
import priv.azx.mpp.data.PersistenceService;

public class ProcessThread implements Runnable {

	public ProcessAssigner processAssigner;
	public int index;
	public String code;

	public ProcessThread(ProcessAssigner processAssigner, int index, String code) {
		this.processAssigner = processAssigner;
		this.index = index;
		this.code = code;
	}

	public static void createMark(String code) {
		File errorFile = new File("/Users/zhixiana/Downloads/data/error/" + code + ".error");
		try {
			errorFile.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static void removeMark(String code) {
		File errorFile = new File("/Users/zhixiana/Downloads/data/error/" + code + ".error");
		errorFile.delete();
	}

	public void run() {
		System.out.println(index + " has begin");
		while (true) {
			try {
				this.code = this.processAssigner.get();
				System.out.println(index + " has got code:" + code);
			} catch (InterruptedException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			if (StringUtils.isBlank(this.code))
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			else {
				String code = this.code;
				/*if (PersistenceService.exists(code)) {
					this.code = null;
					continue;
				}*/
				
				createMark(code);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

				Map<Date, DateData> dateDataMap = new TreeMap<Date, DateData>(new Comparator<Date>() {
					public int compare(Date date1, Date date2) {
						return date1.compareTo(date2);
					}
				});
				String dateString = "20160405";

				String result = "begin";
				while (result != "") {
					System.out.println(index + ":" + dateString + "        " + new Date().toString());
					/*
					 * try { System.out.println(dateString); Thread.sleep(100);
					 * System.out.println(new Date().toString()); } catch
					 * (InterruptedException e) { // TODO Auto-generated catch
					 * block e.printStackTrace(); }
					 */

					try {
						result = SpiderUtil.send(Spider.LOCAL_URL, code, dateString);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Date lastDate = new Date();
					List<DateData> dateDataList = null;
					try {
						dateDataList = SpiderUtil.map(code, result);
					} catch (InstantiationException | IllegalAccessException | ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (dateDataList == null)
						break;
					// if (dateDataList.size() == 0)
					// break;

					for (DateData dateData : dateDataList) {
						if (!dateDataMap.containsKey(dateData.date))
							dateDataMap.put(dateData.date, dateData);
						if (dateData.date.compareTo(lastDate) < 0)
							lastDate = dateData.date;
					}
					dateString = sdf.format(lastDate);
				}

				PersistenceService.save(code, dateDataMap);

				removeMark(code);
			}
		}
	}

}
