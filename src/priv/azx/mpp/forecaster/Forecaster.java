package priv.azx.mpp.forecaster;

import static priv.azx.mpp.forecaster.FeatureAbstractor.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import priv.azx.mpp.data.DateData;
import priv.azx.mpp.data.DateDataCollection;
import priv.azx.mpp.data.DateDataService;
import priv.azx.mpp.data.PersistenceService;
import priv.azx.mpp.util.TimeConverter;

public class Forecaster {

	public static void getFeature() {

	}

	public static double judgeFeature(DateData d1, DateData d2, DateData d3, DateData d4) {
		if (isRiseStop(d1))
			// if ((isRiseBetween(d2, 3, 5)) && (isOpenLow(d1, d2, 1)) &&
			// (isUpShadowLonger(d1, d2, 2)))
			if ((isRiseHigher(d2, 3)) && (!isRiseStop(d2)) && (isUpShadowLonger(d1, d2, 2)) && (isOpenLow(d1, d2)))
				// if ((isOpenLow(d2, d3, 2)) && (isFallBetween(d3, 3, 5)))
				if ((isOpenLow(d2, d3)) && (isFallLower(d3, 3)) && (!isFallStop(d3)))
				return d4.kLine.netChangeRatio;

		return -100d;
	}

	public static double judgeFeature1(DateData d1, DateData d2, DateData d3, DateData d4) {
		if (isRiseStop(d1))
			if (isRiseStop(d2))
				return 1d;
		return -100d;

	}

	public static double judgeFeature2() {

		return 0d;
	}

	public static boolean judgeFeature(List<DateData> dateDataList1, List<DateData> dateDataList2) {
		if (dateDataList1.size() != dateDataList2.size())
			return false;
		boolean isSameFeature = true;
		for (int i = 1; i < dateDataList1.size(); i++) {
			double similarity = getSimilarity(getDayFeature(dateDataList1.get(i), dateDataList1.get(i - 1)),
					getDayFeature(dateDataList2.get(i), dateDataList2.get(i - 1)));
			if (similarity > 0.15) {
				isSameFeature = false;
				break;
			}
		}
		return isSameFeature;
	}

	public static void main(String args[]) throws ParseException {
		DateDataCollection ddc = new DateDataCollection("sz002004");
		DateData dd0 = ddc.get(TimeConverter.stringToDate("20121213"));
		List<DateData> ddList = new ArrayList<DateData>();
		ddList.add(dd0);
		ddList.addAll(ddc.gets(dd0, 5));

		List<String> codes = new ArrayList<String>();
		// codes.addAll(DateData.getSH());
		// codes.addAll(DateData.getSZ());
		codes.addAll(DateDataService.getZX());

		double totalResult = 0;
		int times = 0;
		int[] results = new int[8];
		for (int i = 0; i < 8; i++)
			results[i] = 0;
		List<Double> resultList = new ArrayList<Double>();
		for (String code : codes) {
			DateDataCollection dateDataCollection = new DateDataCollection(code);
			if (!dateDataCollection.isExist())
				continue;
			DateData dateData0 = dateDataCollection.getFirst();

			while (dateData0 != null) {
				List<DateData> dateDataList = new ArrayList<DateData>();
				dateDataList.add(dateData0);
				dateDataList.addAll(dateDataCollection.gets(dateData0, 5));

				if (judgeFeature(ddList, dateDataList)) {
					List<DateData> resultDateDataList = dateDataCollection.gets(dateDataList.get(5), 5);
					System.out.println(code + "    " + TimeConverter.dateToString(dateDataList.get(1).date));
					double netChangeResult = 0d;
					for (DateData d : resultDateDataList)
						netChangeResult += d.kLine.netChangeRatio;
					times += 1;
					totalResult += netChangeResult;

					resultList.add(netChangeResult);
					if (netChangeResult < -10)
						results[0] += 1;
					if ((netChangeResult < -5) && (netChangeResult > -10))
						results[1] += 1;
					if ((netChangeResult < -2) && (netChangeResult > -5))
						results[2] += 1;
					if ((netChangeResult < 0) && (netChangeResult > -2))
						results[3] += 1;
					if ((netChangeResult < 2) && (netChangeResult > 0))
						results[4] += 1;
					if ((netChangeResult < 5) && (netChangeResult > 2))
						results[5] += 1;
					if ((netChangeResult < 10) && (netChangeResult > 5))
						results[6] += 1;
					if (netChangeResult > 10)
						results[7] += 1;
				}

				dateData0 = dateDataCollection.next(dateData0);
			}

			/*
			 * while (d != null) { double similarity =
			 * getSimilarity(getDayFeature(dd, ydd), getDayFeature(d, yd)); if
			 * (similarity < 0.1) System.out.println(code + "    " +
			 * TimeConverter.dateToString(d.date)); yd = d; d =
			 * dateDataCollection.next(d); }
			 */

			/*
			 * System.out.println(code); DateData d1 = null; DateData d2 = null;
			 * DateData d3 = null; DateData d4 = null; for (DateData dateData :
			 * dateDataMap.values()) { d1 = d2; d2 = d3; d3 = d4; d4 = dateData;
			 * if (d1 != null) { double result = judgeFeature(d1, d2, d3, d4);
			 * if (result != -100) System.out.println(code + ":" +
			 * TimeConverter.dateToString(d1.date)); } }
			 */
		}

		System.out.println(totalResult / times);
		System.out.println(resultList);
		for (int i = 0; i < 8; i++)
			System.out.println(results[i]);

		System.exit(0);

		/*
		 * File file = new File("/Users/zhixiana/Downloads/data/"); File[] files
		 * = file.listFiles(); for (File tempFile : files) { if
		 * ((!tempFile.isDirectory()) && (tempFile.getName().contains("xls"))) {
		 * System.out.println(tempFile.getName()); //if
		 * (tempFile.getName().equals("sh600181.xls")) {
		 * ExcelUtility.xxx(tempFile, true, PersistenceService.titleList); //}
		 * 
		 * 
		 * System.out.println(tempFile.getName()); List<Map<String, String>>
		 * contentMapList = ExcelUtility.getFileContentMap(tempFile);
		 * List<String> titleList = PersistenceService.titleList;
		 * List<List<String>> contentList = new ArrayList<List<String>>(); for
		 * (Map<String, String> contentMap : contentMapList) { List<String>
		 * content = new ArrayList<String>(); for (String title : titleList) {
		 * content.add(contentMap.get(title.toLowerCase().trim())); }
		 * content.remove(0); contentList.add(content); } tempFile.delete();
		 * ExcelUtility.generateExcelFile(tempFile, titleList, contentList);
		 * 
		 * // Map<Date, DateData> map = PersistenceService.get("sh600001"); //
		 * tempFile.delete(); // File f = new
		 * File("/Users/zhixiana/Downloads/data/aaa.xls"); //
		 * tempFile.renameTo(f); // PersistenceService.save("sh000001", map);
		 * 
		 * } } System.exit(0);
		 * 
		 */

		String code = "sz002193";

		Map<Date, DateData> dateDataMap = PersistenceService.get(code);
		DateData d1 = dateDataMap.get(TimeConverter.stringToDate("20160303"));
		DateData d2 = dateDataMap.get(TimeConverter.stringToDate("20160304"));

		System.out.println(getUpShadowPCT(d1, d2));
		System.out.println(getUpShadow(d2));
		System.out.print(FeatureAbstractor.isUpShadowLonger(d1, d2, 5));
		// System.out.println(isRiseHigher(d, 4));

	}

}
