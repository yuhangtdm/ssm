package com.dity.ssm.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Map工具类
 */
public class MapUtil {

	/**
	 * 根据map的key进行排序
	 * 
	 * @param map
	 * @param isAsc
	 *            是否升序
	 */
	public static Map<String, Object> sort(Map<String, ? extends Object> map, final boolean isAsc) {
		Map newMap = newSortedMap(isAsc);
		newMap.putAll(map);
		return newMap;
	}

	/**
	 * 默认获取升序的map
	 */
	public static Map<String,Object> newSortedMap(){
		return newSortedMap(true);
	}

	/**
	 * 实例化一个排序的Map
	 * @param isAsc
	 * @return
	 */
	public static Map<String,Object> newSortedMap(final boolean isAsc){
		return new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				int ret = o1.compareTo(o2);
				return isAsc ? ret : -ret;
			}
		});
	}

	public static <T> T mapToObject(Map<String, Object> map, Class<T> beanClass) throws Exception {
		if (map == null)
			return null;

		T obj = beanClass.newInstance();

		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			int mod = field.getModifiers();
			if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
				continue;
			}

			field.setAccessible(true);
			field.set(obj, map.get(field.getName()));
		}

		return obj;
	}

	public static Map<String, Object> objectToMap(Object obj) throws Exception {
		if (obj == null) {
			return null;
		}

		Map<String, Object> map = MapUtil.newSortedMap(true);

		Field[] declaredFields = obj.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			field.setAccessible(true);
			map.put(field.getName(), field.get(obj));
		}

		return map;
	}

	public static String mapToStr(Map<String,Object> map) {
		if (map == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			sb.append(entry.getKey()).append("=").append(entry.getValue());
		}
		return sb.toString();
	}

	public static void removeNull(Map<String,Object> map){
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getKey()==null || entry.getValue()==null){
				map.remove(entry.getKey());
			}
		}
	}

}
