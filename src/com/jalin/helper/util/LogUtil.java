package com.jalin.helper.util;

import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class LogUtil {
	private static Properties progProp = PropertiesUtil.getInstance().getProgProp();	
	public static Logger getLogger(String string) {
		Logger log = Logger.getLogger(string);
		try {

			PatternLayout patternFile = new PatternLayout();
			patternFile.setConversionPattern(progProp.getProperty("LOG_FILE_CONVERSION_PATTERN"));
			ConsoleAppender consoleAppender = new ConsoleAppender(patternFile);
			DailyRollingFileAppender dailyRollingFileAppender = new DailyRollingFileAppender(
					patternFile, progProp.getProperty("PATH_LOG_FILE")
							+ progProp.getProperty("LOG_FILE_NAME"),
							progProp.getProperty("LOG_FILE_BACKUP_PATTERN"));
			log.setLevel(Level.toLevel(progProp.getProperty("LOG_FILE_LEVEL")));
			log.addAppender(consoleAppender);
			log.addAppender(dailyRollingFileAppender);
			log.setAdditivity(false);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return log;
	}
}
