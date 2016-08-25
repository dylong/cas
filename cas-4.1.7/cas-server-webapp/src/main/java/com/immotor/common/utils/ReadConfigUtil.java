package com.immotor.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;

/**
 * @desc desc
 * @author dylong
 * @date 2016-8-16 下午4:48:34
 */
public class ReadConfigUtil
{

	private static ReadConfigUtil config = null;
	private static ResourceBundle rb = null;
	private static final String CONFIG_FILE = "config/config";

	private ReadConfigUtil() {
		rb = ResourceBundle.getBundle(CONFIG_FILE);
	}

	public static ReadConfigUtil getInstance() {
		if (null == config) {
			config = new ReadConfigUtil();
		}
		return config;
	}

	public String getValue(String key) {
		String value = "";
		try {
			value = new String(rb.getString(key).getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			value = "";
		}
		return value;
	}
}
