package com.jalin.helper.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
	private Properties dbProp = new Properties();
	private Properties progProp = new Properties();
	
	public Properties getProgProp() {
		return progProp;
	}

	public void setProgProp(Properties progProp) {
		this.progProp = progProp;
	}

	public Properties getDbProp() {
		return dbProp;
	}

	public void setDbProp(Properties dbProp) {
		this.dbProp = dbProp;
	}
	
	private static PropertiesUtil instance;
	public static synchronized PropertiesUtil getInstance(){
        if(instance == null)
            instance = new PropertiesUtil();
        return instance;
	}
	
	private PropertiesUtil() {
        loadProperties();
    }
	
	private void loadProperties() {
		try {
			dbProp.load(new FileInputStream("prop/db.properties"));
			progProp.load(new FileInputStream("prop/program.properties"));
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
		
	}
}


