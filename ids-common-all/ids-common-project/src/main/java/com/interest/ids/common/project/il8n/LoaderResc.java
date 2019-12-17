package com.interest.ids.common.project.il8n;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;

import org.springframework.core.io.Resource;



public class LoaderResc {

	private Properties properties;
	/**
	 * 资源路径
	 */
	private Resource resource;
	private Locale locale;

	public LoaderResc(Resource rs, Locale locale) throws IOException {
		this.resource = rs;
		this.locale = locale;
		if (resource != null) {
			properties = new Properties();
			properties.load(resource.getInputStream());
		}
	}

	public String getString(String key) {
		String ret = (String) properties.get(key);
		if (ret == null)
			throw new RuntimeException(key);
		if (Locale.CHINA.equals(locale)) {
			ret = SmartEncoder.transformString(ret, SmartEncoder.GB2312);
		}
		return ret;
	}

	public String getString(String key, String[] params) {
		return getString(key, (Object[]) params);
	}

	public String getString(String key, Object[] params) {
		String ret = getString(key);
		if (params != null && params.length > 0) {
			ret = MessageFormat.format(ret, params);
			ret = String.format(ret, params);
		}
		return ret;
	}

	public Locale getLocale() {
		// TODO Auto-generated method stub
		return this.locale;
	}
}
