package priv.azx.mpp.data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ModelOperation {

	public static Object assemble(@SuppressWarnings("rawtypes") Class objectClass, Map<String, String> map,
			boolean addName) {
		Object object = null;
		try {
			object = objectClass.newInstance();
			Field[] fields = objectClass.getDeclaredFields();
			for (Field field : fields) {
				String fieldName = field.getName();
				if(addName)
					fieldName = objectClass.getSimpleName() + fieldName;
				fieldName = fieldName.toLowerCase().trim();
				@SuppressWarnings("rawtypes")
				Class fieldClass = field.getType();
				
				if (!map.containsKey(fieldName))
					continue;
				String valueString = map.get(fieldName);
				 
				if ((fieldClass.equals(double.class)) || (fieldClass.equals(Double.class))) {
					double value = Double.parseDouble(valueString);
					field.set(object, value);
				}
				if ((fieldClass.equals(long.class)) || (fieldClass.equals(Long.class))) {
					long value = Long.parseLong(valueString);
					field.set(object, value);
				}
				if ((fieldClass.equals(int.class)) || (fieldClass.equals(Integer.class))) {
					int value = Integer.parseInt(valueString);
					field.set(object, value);
				}
			}

		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return object;
	}

	public static Map<String, String> map(Object object) {
		Map<String, String> map = new HashMap<String, String>();

		try {
			Field[] fields = object.getClass().getDeclaredFields();
			for (Field field : fields) {
				String fieldName = field.getName();
				Object o = field.get(object);
				map.put(fieldName, o.toString());
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return map;
	}

	public static void map(Object object, Map<String, String> map) {
		try {
			Field[] fields = object.getClass().getDeclaredFields();
			for (Field field : fields) {
				String fieldName = field.getName();
				Object o = field.get(object);
				map.put(fieldName, o.toString());
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
