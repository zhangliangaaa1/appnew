package com.yili.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigLoader extends Properties {

	private static final long serialVersionUID = 1L;

	private static final String cfgFile = "WebAPP.properties";

	private static ConfigLoader instance;

	private static Map<String, ConfigLoader> configs = null;

	public static ConfigLoader getInstance() throws IOException {
		if (configs == null){
			configs = new HashMap<String, ConfigLoader>();
		}
		
		if (configs.get(cfgFile) == null) {
			makeInstance(cfgFile);
		} 
		return instance;
	}

	private static synchronized void makeInstance(String path) throws IOException {
		if (instance == null) {
			instance = new ConfigLoader(path);
			configs.put(path, instance);
		}
	}

	private ConfigLoader(String path) throws IOException {
		load(ConfigLoader.class.getClassLoader().getResourceAsStream(path));
	}
	
}
