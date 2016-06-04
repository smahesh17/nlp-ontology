package org.research.util;

import java.util.HashMap;
import java.util.Map;

public class Logger {

	static Map<String, Logger> map = new HashMap<String, Logger>();

	java.util.logging.Logger delegatedLogger;

	public Logger(String name) {
		delegatedLogger = java.util.logging.Logger.getLogger(name);
	}

	public static Logger getLogger(Class klass) {
		Logger logger = map.get(klass.getName());
		if (logger == null) {
			logger = new Logger(klass.getName());
			map.put(klass.getName(), logger);
		}
		return logger;
	}

	public void info(String message) {
		delegatedLogger.info(message);
	}

	public void severe(String msg) {
		delegatedLogger.severe(msg);
	}

	public java.util.logging.Logger getActualLogger() {
		return delegatedLogger;
	}

}
