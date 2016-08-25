package com.immotor.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.Constants;

public class Utils {
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static String list2Json(List<?> list) {
		ObjectMapper om = new ObjectMapper();
		try {
			return om.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	public static <K, V> Map<K, V> json2Map(String json) {
		Map<K, V> map = newMap();
		ObjectMapper om = new ObjectMapper();
		try {
			map = om.readValue(json, new TypeReference<Map<K, V>>() {
			});
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return map;
	}
	public static <K, V> Map<K, V> object2Map(Object obj) {
		return json2Map(toJson(obj));
	}
	public static <T> T json2Object(String json, Class<T> clazz) {
		T t = null;
		ObjectMapper om = new ObjectMapper();
		try {
			t = om.readValue(json, clazz);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return t;
	}

	public static Date String2Date(String date) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sf.parse(date);
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	public static Date DateCompute(Date date, int value) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(5, value);
		return gc.getTime();
	}

	public static String DateCompute(String date, int value) {
		GregorianCalendar gc = new GregorianCalendar();

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			gc.setTime(sf.parse(date));
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}
		gc.add(5, -value);
		return sf.format(gc.getTime());
	}

	public static String toJson(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	public static JsonNode readParam(InputStream is) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readTree(is);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	public static <T> T readParam(InputStream is, Class<T> c) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(is, c);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	public static String inputStream2String(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		final byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	/**
	 * 创建HashMap实例
	 * 
	 * @return HashMap实例
	 */
	public static final <K, V> Map<K, V> newMap() {
		return new HashMap<K, V>();
	}

	/**
	 * 判断对象是否为空
	 * 
	 * @param obj
	 *            待判断对象
	 * @return boolean true-空(null/空字符串/纯空白字符),false-非空
	 */
	public static final boolean isEmpty(Object obj) {
		return obj == null || obj.toString().trim().length() == 0;
	}

	/**
	 * 判断Map是否为空(null||empty)
	 * 
	 * @param map
	 * @return boolean true时则为空，false时则有值
	 */
	public static final boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}
	/**
	 * 判断List是否为空(null||empty)
	 * 
	 * @param c
	 * @return boolean
	 */
	public static final boolean isEmpty(Collection<?> c) {
		return c == null || c.isEmpty();
	}

	/**
	 * 创建ArrayList实例
	 * 
	 * @return ArrayList实例
	 */
	public static final <E> List<E> newList() {
		return new ArrayList<E>();
	}
	
    /**
     * 创建ArrayList实例
     * @param initialCapacity 初始化容量
     * @return ArrayList实例
     */
    public static final <E> List<E> newList(int initialCapacity) {
        return new ArrayList<E>(initialCapacity);
    }
    
    /**
     * 转换为字符串
     * @param obj 待转换对象
     * @return String null-空字符串;
     */
    public static final String toString(Object obj) {
        return obj == null ? null : obj.toString();
    }
    
	/**
	 * 转为String集合
	 * 
	 * @param obj
	 *            字符串
	 * @param separtor
	 *            分隔符
	 * @return String集合
	 */
	public static final List<String> toList(Object obj, String separtor) {
		List<String> list = null;
		if (!isEmpty(obj)) {
			list = newList();
			String[] temp = String.valueOf(obj).trim().split(separtor);
			for (String str : temp) {
				if (!isEmpty(str)) {
					list.add(str);
				}
			}
		}
		return list;
	}

	/**
	 * 转为String集合(分隔符为",")
	 * 
	 * @param obj
	 *            字符串
	 * @return String集合
	 */
	public static final List<String> toList(Object obj) {
		return toList(obj, ",");
	}

	/*
	 * 删除map中的key
	 */
	public static void removeMapKeys(Map<?, ?> map, Object... keys) {
	    if(isEmpty(map)){
	        return;
	    }
		for (Object key : keys) {
			map.remove(key);
		}
	}

	/**
	 * 过滤Map中的空键或空值
	 * 
	 * @param map
	 *            Map实例
	 * @return 过滤后的Map实例
	 */
	public static final <K, V> Map<K, V> cleanMapEmptyField(Map<K, V> map) {
		if (!isEmpty(map)) {
			Iterator<K> it = map.keySet().iterator();
			while (it.hasNext()) {
				K key = it.next();
				if (isEmpty(key) || isEmpty(map.get(key))) {
					it.remove();
				}
			}
		}
		return map;
	}

	/*
	 * TODO 待优化 最短长度， 空校验 和 长度校验应该分开 输入信息验证，eg:getEmptyField(node, "passcode-6")
	 * passcode不能为空， 非空的时候长度不能超过6位
	 */
	public static String getEmptyField(HttpServletRequest request,
			String... strs) {
		String emptyResp = "";
		String lengthLimit = "";
		for (String str : strs) {
			if (str.indexOf("-") != -1) {
				String property = str.substring(0, str.indexOf("-"));
				String val = request.getParameter(property);
				if (Utils.isEmpty(val)) {
					emptyResp += property + ", ";
				} else {
					int len = Integer
							.parseInt(str.substring(str.indexOf("-") + 1));
					if (val.length() > len) {
						lengthLimit += property + " length limit " + len + ", ";
					}
				}
			} else {
				if (Utils.isEmpty(request.getParameter(str))) {
					emptyResp += str + ",";
				}
			}
		}
		return handleEmptyField(emptyResp, lengthLimit);
	}

	public static String getEmptyField(JsonNode node, String... strs) {
		String emptyResp = "";
		String lengthLimit = "";
		for (String str : strs) {
			if (str.indexOf("-") != -1) {
				String property = str.substring(0, str.indexOf("-"));
				JsonNode val = node.get(property);
				if (Utils.isEmpty(val)) {
					emptyResp += property + ", ";
				} else {
					int len = Integer
							.parseInt(str.substring(str.indexOf("-") + 1));
					if (val.asText().length() > len) {
						lengthLimit += property + " length limit " + len + ", ";
					}
				}
			} else {
				if (Utils.isEmpty(node.get(str))) {
					emptyResp += str + ",";
				}
			}
		}
		return handleEmptyField(emptyResp, lengthLimit);
	}

	private static String handleEmptyField(String emptyResp,
			String lengthLimit) {
		if (!Utils.isEmpty(emptyResp)) {
			emptyResp = emptyResp.substring(0, emptyResp.lastIndexOf(","))
					+ " not be null";
		}
		if (!Utils.isEmpty(lengthLimit)) {
			lengthLimit = lengthLimit.substring(0,
					lengthLimit.lastIndexOf(","));
		}
		return emptyResp + "  " + lengthLimit;
	}

	public static String nodeAsTest(JsonNode node) {
		return node == null ? null : node.asText();
	}
	public static Integer nodeAsInt(JsonNode node) {
		return node == null ? null : node.asInt();
	}
	public static Double nodeAsDouble(JsonNode node) {
		return node == null ? null : node.asDouble();
	}

	/**
	 * 长整型转换为日期类型
	 * 
	 * @param long
	 *            longTime 长整型时间
	 * @param String
	 *            dataFormat 时间格式
	 * @return String 长整型对应的格式的时间
	 */
	public static String long2String(long longTime) {
		Date d = new Date(longTime);
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = s.format(d);
		return str;

	}
	
	public static String long2String(long longTime, String format) {
		Date d = new Date(longTime);
		SimpleDateFormat s = new SimpleDateFormat(format);
		String str = s.format(d);
		return str;

	}
	/**
	 * 格式化Date
	 * 
	 * @param pattern
	 *            格式
	 * @param date
	 *            date对象
	 * @return String 格式化日期字符串
	 */
	public static final String Date2String(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}
	
	public static final String Date2String(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}

	/**
	 * 加减Date各字段的值
	 * 
	 * @param date
	 *            Date
	 * @param field
	 *            字段,如Calendar.DAY_OF_YEAR,Calendar.MINUTE等
	 * @param increment
	 *            增量,可为负值
	 * @return Date
	 */
	public static Date dateAdd(Date date, int field, int increment) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.add(field, increment);
		return cal.getTime();
	}
	/*
	 * 生成6位验证码
	 */
	public static String generatePassCode() {
		Random r = new Random();
		StringBuffer ps = new StringBuffer();
		for (int i = 0; i < 6; i++) {
			int num = r.nextInt(10);
			ps.append(num);
		}
		return ps.toString();
	}
	
	/*
	 * IP地址获取
	 */
	public static String getIP(HttpServletRequest request) {
		if (request.getHeader("x-forwarded-for") == null) {
			return request.getRemoteAddr();
		}
		return request.getHeader("x-forwarded-for");
	}
	
    /**
     * 判断手机的操作系统 IOS/android
     * 0：android，1：ios，2：论坛, 3：官网，4: 其他
     */
    public static Integer getDeviceType(String UA) {
        UA=UA.toUpperCase();
        if (UA == null) {
            return 4;
        }
        // IOS 判断字符串
        String iosString = "IOS";
        if (UA.indexOf(iosString) != -1) {
            return 1;
        }
        
        // Android 判断
        String androidString = "ANDROID";
        if (UA.indexOf(androidString) != -1) {
            return 0;
        }
        return 4;
    }

	/*
	 * 获取ObjectMapper 单例
	 */
	private static ObjectMapper mapper;
	public static synchronized ObjectMapper getObjectMapper() {
		if (mapper == null) {
			mapper = new ObjectMapper();
		}
		return mapper;
	}
	
	public static void main(String[] args) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("ss", "ss");
        System.out.println(Utils.isEmpty(map));
        Date date = new Date();
        
        System.out.println(Utils.dateAdd(date, Calendar.MINUTE ,10));
    }
}
