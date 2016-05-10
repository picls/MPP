package priv.azx.mpp.data;

import java.util.Date;
import java.util.Map;

import priv.azx.mpp.util.TimeConverter;

public class DateData {

	public boolean isIndex(String code) {
		if (DateDataService.indexCodes.contains(code))
			return true;
		return false;
	}

	public boolean isSH(String code) {
		if (DateDataService.SHCodes.contains(code))
			return true;
		return false;
	}

	public boolean isZX(String code) {
		if (DateDataService.ZXCodes.contains(code))
			return true;
		return false;
	}

	public boolean isCY(String code) {
		if (DateDataService.CYCodes.contains(code))
			return true;
		return false;
	}

	public boolean isSZ(String code) {
		if (DateDataService.SZCodes.contains(code))
			return true;
		return false;
	}

	public String code;
	public Date date;
	public KLine kLine;
	public Map<Integer, MA> maMap;
	public MACD macd;
	public KDJ kdj;
	public RSI rsi;

	public DateData() {

	}

	public DateData(String code, Date date) {
		this.code = code;
		this.date = date;
	}

	public DateData(String code, Date date, KLine kLine, Map<Integer, MA> maMap, MACD macd, KDJ kdj, RSI rsi) {
		this.code = code;
		this.date = date;
		this.kLine = kLine;
		this.maMap = maMap;
		this.macd = macd;
		this.kdj = kdj;
		this.rsi = rsi;
	}

	public String toString() {
		return code + "," + TimeConverter.dateToString(date);
	}

}
