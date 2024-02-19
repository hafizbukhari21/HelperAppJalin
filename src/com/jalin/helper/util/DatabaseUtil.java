package com.jalin.helper.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;

public class DatabaseUtil {
	private static Properties properties = PropertiesUtil.getInstance().getDbProp();
	private static Properties progProp = PropertiesUtil.getInstance().getProgProp();
	private static Logger log = LogUtil.getLogger(DatabaseUtil.class.getName());
	
	private static Connection con(int env) {

		Connection con = null;
		try {
			if(env == 1 ){// VIT
				log.info("RUn as VIT");
				Class.forName(properties.getProperty("driver"));
				con = DriverManager.getConnection(properties.getProperty("url"),
				properties.getProperty("user"),
				properties.getProperty("password"));
			}
			else if (env==2 ) {// Staging OR selain 1 
				log.info("Run As Staging");
				Class.forName(properties.getProperty("driver"));
				con = DriverManager.getConnection(properties.getProperty("urlStaging"),
				properties.getProperty("user"),
				properties.getProperty("password"));
			}
			else{
				return null ;
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 		
		return con;
	}
	
	public static ArrayList<String> getMainQuery(String query, String[] model, int env) {
		Connection con = con(env);
	    Statement stmt = null;
	    ArrayList<String> data = new ArrayList<String>();
	    try {
	    	stmt = con.createStatement();
			  ResultSet rset = stmt.executeQuery(query);
			  while (rset.next()) {		
				  String record = "";
				  for(int i = 0; i < model.length ; i++) {
					  String x  = rset.getString(model[i]) ;
					  if(i == model.length - 1) {
						  record = record+x;
					  }else {
						  record = record+x+ progProp.getProperty("delimiter");
					  }
				  }
				  data.add(record);
		      }
			  
		} catch (Exception e) {
			log.error("Error " + e.getMessage());
//			e.printStackTrace();
			// TODO: handle exception
		}finally{
	        try {
	            stmt.close();
	            con.close();
	        } catch (SQLException f) {
	            f.printStackTrace();
	        }
	    }
		return data;
	}

	public static int deletedQuery(String query, String[] model, int env) {
		Connection con = con(env);
	    Statement stmt = null;
		int rset=0;
	    
	    try {
	    	stmt = con.createStatement();
			rset = stmt.executeUpdate(query);
		} catch (Exception e) {
			log.error("Error " + e.getMessage());
//			e.printStackTrace();
			// TODO: handle exception
		}finally{
	        try {
	            stmt.close();
	            con.close();
	        } catch (SQLException f) {
	            f.printStackTrace();
	        }
	    }
		return rset;
	}
	
	


	
	public static String selectData(String query, String model, int env) {
		Connection con = con(env);
	    Statement stmt = null;
	    String data = "";
	    try {
	    	stmt = con.createStatement();
			  ResultSet rset = stmt.executeQuery(query);
			  while (rset.next()) {
				  data = rset.getString(model);
		      }			  
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally{
	        try {
	            stmt.close();
	            con.close();
	        } catch (SQLException f) {
	            f.printStackTrace();
	        }
	    }
		return data;
	}
	
	public static int executeUpdate(String queryInsertUpdate, int env) throws SQLException {
		Connection con = con(env);
	    PreparedStatement stmt = null;
	    int id = 0;
//	    try {
			  stmt = con.prepareStatement(queryInsertUpdate);
			  stmt.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
			// TODO: handle exception
//		}finally{
	        try {
	            stmt.close();
	            con.close();
	        } catch (SQLException f) {
	            f.printStackTrace();
	        }
//	    }
		return id;
	}
}

