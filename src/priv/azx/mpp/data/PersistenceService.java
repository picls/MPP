package priv.azx.mpp.data;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import priv.azx.mpp.util.ExcelUtility;
import priv.azx.mpp.util.TimeConverter;

public class PersistenceService {

	public static final String[] titles = { "code", "date", "open", "high", "low", "close", "volume", "amount",
			"netChangeRatio", "MA5volume", "MA5avgPrice", "MA10volume", "MA10avgPrice", "MA20volume", "MA20avgPrice",
			"MACDdiff", "MACDdea", "MACDmacd", "KDJk", "KDJd", "KDJj", "RSIrsi1", "RSIrsi2", "RSIrsi3" };
	public static final List<String> titleList;

	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	static {
		titleList = new ArrayList<String>();
		for (String s : titles)
			titleList.add(s);
	}

	public static File getFile(String code) {
		return new File("/Users/zhixiana/Downloads/data/" + code + ".xls");
	}

	public static String getFileName(String code) {
		return "/Users/zhixiana/Downloads/data/" + code + ".xls";
	}

	public static boolean exists(String code) {
		File file = getFile(code);
		if (file.exists())
			return true;
		else
			return false;
	}

	public static void save(String code, Map<Date, DateData> dateDataMap) {
		File file = getFile(code);
		List<List<String>> contentList = new ArrayList<List<String>>();

		contentList = new ArrayList<List<String>>();
		for (DateData dateData : dateDataMap.values()) {
			List<String> tempList = new ArrayList<String>();
			tempList.add(code);
			tempList.add(sdf.format(dateData.date));
			tempList.add(dateData.kLine.open + "");
			tempList.add(dateData.kLine.high + "");
			tempList.add(dateData.kLine.low + "");
			tempList.add(dateData.kLine.close + "");
			tempList.add(dateData.kLine.volume + "");
			tempList.add(dateData.kLine.amount + "");
			tempList.add(dateData.kLine.netChangeRatio + "");
			tempList.add(dateData.maMap.get(5).volume + "");
			tempList.add(dateData.maMap.get(5).avgPrice + "");
			tempList.add(dateData.maMap.get(10).volume + "");
			tempList.add(dateData.maMap.get(10).avgPrice + "");
			tempList.add(dateData.maMap.get(20).volume + "");
			tempList.add(dateData.maMap.get(20).avgPrice + "");
			tempList.add(dateData.macd.diff + "");
			tempList.add(dateData.macd.macd + "");
			tempList.add(dateData.macd.dea + "");
			tempList.add(dateData.kdj.k + "");
			tempList.add(dateData.kdj.d + "");
			tempList.add(dateData.kdj.j + "");
			tempList.add(dateData.rsi.rsi1 + "");
			tempList.add(dateData.rsi.rsi2 + "");
			tempList.add(dateData.rsi.rsi3 + "");

			contentList.add(tempList);
		}

		if (contentList.size() != 0)
			ExcelUtility.generateExcelFile(file, titleList, contentList);

	}

	private static Map<Integer, MA> getMaMap(Map<String, String> contentMap) {
		Map<Integer, MA> maMap = new HashMap<Integer, MA>();

		MA ma5 = new MA(5, Long.parseLong(contentMap.get("ma5volume")),
				Double.parseDouble(contentMap.get("ma5avgprice")));
		MA ma10 = new MA(10, Long.parseLong(contentMap.get("ma10volume")),
				Double.parseDouble(contentMap.get("ma10avgprice")));
		MA ma20 = new MA(20, Long.parseLong(contentMap.get("ma20volume")),
				Double.parseDouble(contentMap.get("ma20avgprice")));

		maMap.put(5, ma5);
		maMap.put(10, ma10);
		maMap.put(20, ma20);

		return maMap;
	}

	public static Map<Date, DateData> get(String code) throws ParseException {
		Map<Date, DateData> dateDataMap = DateDataMap.buildDateMap();

		File file = getFile(code);
		if (!file.exists())
			return null;
		List<Map<String, String>> contentMapList = ExcelUtility.getFileContentMap(file);
		for (Map<String, String> contentMap : contentMapList) {
			Date date = sdf.parse(contentMap.get("date"));

			KLine kLine = (KLine) ModelOperation.assemble(KLine.class, contentMap, false);
			Map<Integer, MA> maMap = getMaMap(contentMap);
			MACD macd = (MACD) ModelOperation.assemble(MACD.class, contentMap, true);
			KDJ kdj = (KDJ) ModelOperation.assemble(KDJ.class, contentMap, true);
			RSI rsi = (RSI) ModelOperation.assemble(RSI.class, contentMap, true);

			DateData dateData = new DateData(code, date, kLine, maMap, macd, kdj, rsi);
			dateDataMap.put(date, dateData);
		}

		return dateDataMap;

	}

	public static Map<String, DateData> getWithString(String code) {
		Map<String, DateData> dateDataMap = DateDataMap.buildStringMap();

		File file = getFile(code);
		List<Map<String, String>> contentMapList = ExcelUtility.getFileContentMap(file);
		for (Map<String, String> contentMap : contentMapList) {
			String date = contentMap.get("date");

			KLine kLine = (KLine) ModelOperation.assemble(KLine.class, contentMap, false);
			Map<Integer, MA> maMap = getMaMap(contentMap);
			MACD macd = (MACD) ModelOperation.assemble(MACD.class, contentMap, true);
			KDJ kdj = (KDJ) ModelOperation.assemble(KDJ.class, contentMap, true);
			RSI rsi = (RSI) ModelOperation.assemble(RSI.class, contentMap, true);

			DateData dateData = new DateData(code, TimeConverter.stringToDate(date), kLine, maMap, macd, kdj, rsi);
			dateDataMap.put(date, dateData);
		}

		return dateDataMap;
	}

}
