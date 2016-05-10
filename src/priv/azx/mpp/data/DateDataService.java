package priv.azx.mpp.data;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DateDataService {
	
	public static final String SZIndex = "sh000001";

	public static final Map<String, DateDataCollection> indexCollections;
	public static final DateDataCollection SZDateDataCollection;

	static {
		indexCollections = new HashMap<String, DateDataCollection>();
		SZDateDataCollection = get(SZIndex);
	}
	
	public static final List<String> indexCodes;
	public static final List<String> SHCodes;
	public static final List<String> SZCodes;
	public static final List<String> ZXCodes;
	public static final List<String> CYCodes;

	static {
		indexCodes = getIndex();
		SHCodes = getSH();
		SZCodes = getSZ();
		ZXCodes = getZX();
		CYCodes = getCY();
	}

	public static List<String> getIndex() {
		List<String> codeList = new ArrayList<String>();
		codeList.add("sh000001"); // szzs
		codeList.add("sz399001"); // szcz
		codeList.add("sh000300"); // hs300
		codeList.add("sz399005"); // zxbz
		codeList.add("sz399006"); // cybz
		codeList.add("sz399101"); // zxbz
		codeList.add("sz399102"); // cybz
		codeList.add("sh000016"); // sz50
		codeList.add("sh000010"); // sz180
		codeList.add("sh000009"); // sz380
		codeList.add("sz399004"); // sz100
		codeList.add("sz399007"); // sz300
		codeList.add("sz399106"); // szzz

		return codeList;
	}

	public static List<String> getSH() {
		List<String> codeList = new ArrayList<String>();
		for (int i = 1; i <= 4000; i++) {
			String code = "sh";
			if (i < 10)
				code += "60000" + i;
			else if (i < 100)
				code += "6000" + i;
			else if (i < 1000)
				code += "600" + i;
			else
				code += "60" + i;
			codeList.add(code);
		}
		return codeList;
	}

	public static List<String> getSZ() {
		List<String> codeList = new ArrayList<String>();
		for (int i = 1; i < 2000; i++) {
			String code = "sz";
			if (i < 10)
				code += "00000" + i;
			else if (i < 100)
				code += "0000" + i;
			else if (i < 1000)
				code += "000" + i;
			else
				code += "00" + i;
			codeList.add(code);
		}
		return codeList;
	}

	public static List<String> getZX() {
		List<String> codeList = new ArrayList<String>();
		for (int i = 2001; i < 4000; i++) {
			String code = "sz";
			if (i < 10)
				code += "00000" + i;
			else if (i < 100)
				code += "0000" + i;
			else if (i < 1000)
				code += "000" + i;
			else
				code += "00" + i;
			codeList.add(code);
		}
		return codeList;
	}

	public static List<String> getCY() {
		List<String> codeList = new ArrayList<String>();
		for (int i = 1; i <= 1000; i++) {
			String code = "sz";
			if (i < 10)
				code += "30000" + i;
			else if (i < 100)
				code += "3000" + i;
			else if (i < 1000)
				code += "300" + i;
			else
				code += "30" + i;
			codeList.add(code);
		}
		return codeList;
	}

	public static DateDataCollection get(String code) {
		Map<Date, DateData> dateDataMap;
		try {
			dateDataMap = PersistenceService.get(code);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return new DateDataCollection(code, dateDataMap);
	}
	
	public static void main(String args[]) {
		DateDataCollection d = get("sz002193");
		DateData dd = d.get("20160330");
		System.out.println(d.next(dd).kLine.open);
	}

}
