package priv.azx.mpp.spider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import priv.azx.mpp.data.DateData;
import priv.azx.mpp.data.KDJ;
import priv.azx.mpp.data.KLine;
import priv.azx.mpp.data.MA;
import priv.azx.mpp.data.MACD;
import priv.azx.mpp.data.RSI;

public class SpiderUtil {
	
	public static StringBuilder assembleParameter(StringBuilder stringBuilder, String key, String value) {
		stringBuilder.append(key).append("=").append(value).append("&");
		return stringBuilder;
	}

	public static String assembleParameters(String code, String dateString) {
		StringBuilder parameter = new StringBuilder();
		parameter.append("?");
		assembleParameter(parameter, "from", "pc");
		assembleParameter(parameter, "os_ver", "1");
		assembleParameter(parameter, "cuid", "xxx");
		assembleParameter(parameter, "vv", "100");
		assembleParameter(parameter, "format", "json");
		assembleParameter(parameter, "stock_code", code);
		assembleParameter(parameter, "step", "3");
		assembleParameter(parameter, "start", dateString);
		assembleParameter(parameter, "count", "320");
		assembleParameter(parameter, "fq_type", "front");
		assembleParameter(parameter, "timestamp", new Date().getTime() + "");

		return parameter.toString();
	}

	public static void assembleURL(HttpURLConnection httpURLConnection) {
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setDoInput(true);
		httpURLConnection.setUseCaches(true);
		httpURLConnection.setInstanceFollowRedirects(true);
		httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		httpURLConnection.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0");
		// httpURLConnection.setRequestProperty("Accept-Encoding", "gzip,
		// deflate, sdch");
		httpURLConnection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
		httpURLConnection.setRequestProperty("Cache-Control", "max-age=0");
		httpURLConnection.setRequestProperty("Connection", "keep-alive");
		httpURLConnection.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36");
		// httpURLConnection.setRequestProperty(key, value);
	}

	public static String send(String localUrl, String code, String dateString) throws IOException {
		String parameter = assembleParameters(code, dateString);

		// System.out.println("send begin");
		System.out.println(localUrl + parameter.toString());

		int tryTime = 0;
		while (tryTime <= 5) {
			try {
				tryTime++;
				URL url = new URL(localUrl + parameter.toString());
				URLConnection urlConnection = url.openConnection();
				HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
				assembleURL(httpURLConnection);

				if (httpURLConnection.getResponseCode() != 200)
					throw new IOException();
				// InputStreamReader errorStream = new
				// InputStreamReader(httpURLConnection.getErrorStream());
				InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());

				/*
				 * BufferedReader errorReader = new BufferedReader(errorStream);
				 * String errorLine = ""; while ((errorLine =
				 * errorReader.readLine()) != null)
				 * System.out.println(errorLine);
				 */

				BufferedReader reader = new BufferedReader(inputStreamReader);
				StringBuilder result = new StringBuilder();
				String tempLine = "";
				while ((tempLine = reader.readLine()) != null)
					result.append(tempLine);

				httpURLConnection.disconnect();

				return result.toString();
			} catch (IOException e) {
				if (tryTime <= 5)
					;
				else
					throw e;
			}
		}

		throw new IOException();

	}

	public static List<DateData> map(String code, String jsonString)
			throws ParseException, InstantiationException, IllegalAccessException {
		List<DateData> resultList = new ArrayList<DateData>();

		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		String errorMsgString = jsonObject.getString("errorMsg");
		if (errorMsgString.equals("SUCCESS")) {
			if (!jsonObject.containsKey("mashData")) {
				return null;
			}
			JSONArray mashData = jsonObject.getJSONArray("mashData");
			for (int i = 0; i < mashData.size(); i++) {
				JSONObject dateData = mashData.getJSONObject(i);
				DateData dateDataObject = new DateData();

				String dateString = dateData.getString("date");
				JSONObject ma5 = dateData.getJSONObject("ma5");
				JSONObject ma10 = dateData.getJSONObject("ma10");
				JSONObject ma20 = dateData.getJSONObject("ma20");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				Map<Integer, MA> maMap = new HashMap<Integer, MA>();
				maMap.put(5, (MA) map(ma5, MA.class));
				maMap.put(10, (MA) map(ma10, MA.class));
				maMap.put(20, (MA) map(ma20, MA.class));

				dateDataObject.code = code;
				dateDataObject.date = sdf.parse(dateString);
				dateDataObject.kLine = (KLine) map(dateData.getJSONObject("kline"), KLine.class);
				dateDataObject.maMap = maMap;
				dateDataObject.macd = (MACD) map(dateData.getJSONObject("macd"), MACD.class);
				dateDataObject.kdj = (KDJ) map(dateData.getJSONObject("kdj"), KDJ.class);
				dateDataObject.rsi = (RSI) map(dateData.getJSONObject("rsi"), RSI.class);

				resultList.add(dateDataObject);
			}
		}

		return resultList;
	}
	
	@SuppressWarnings("rawtypes")
	public static Object map(JSONObject jsonObject, Class objectClass)
			throws InstantiationException, IllegalAccessException {
		Object object = objectClass.newInstance();
		Field[] fields = objectClass.getDeclaredFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			Class fieldClass = field.getType();
			//String fieldClassName = field.getType().toString();

			if (jsonObject.containsKey(fieldName))
				if (fieldClass.isArray()) {
					//Class componentClass = fieldClass.getComponentType();

				} else {
					// if (fieldName.equals("netChangeRatio"))
					// System.out.println(jsonObject.getString("netChangeRatio"));
					if (fieldClass.equals(String.class))
						set(object, field, jsonObject.getString(fieldName));
					if ((fieldClass.equals(int.class)) || (fieldClass.equals(Integer.class)))
						set(object, field, jsonObject.getInt(fieldName));
					if ((fieldClass.equals(long.class)) || (fieldClass.equals(Long.class)))
						set(object, field, jsonObject.getLong(fieldName));
					if ((fieldClass.equals(double.class)) || (fieldClass.equals(Double.class))) {
						if (jsonObject.getString(fieldName).equals("INF"))
							set(object, field, 0);
						else
							set(object, field, jsonObject.getDouble(fieldName));
					}
					if ((fieldClass.equals(float.class)) || (fieldClass.equals(Float.class)))
						set(object, field, jsonObject.getDouble(fieldName));

				}

		}
		return object;
	}

	private static void set(Object object, Field field, Object value)
			throws IllegalArgumentException, IllegalAccessException {
		if (value != null)
			field.set(object, value);
	}

}
