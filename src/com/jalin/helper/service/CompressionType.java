package com.jalin.helper.service;

public enum CompressionType {
  GZIP("GZ");
	  
  private String name;
  
  public static final String GZIP_STR = "GZ";
  
  CompressionType(String paramString1) {
    this.name = paramString1;
  }
  
  public static CompressionType of(String paramString) {
    if ("GZ".equals(paramString))
      return GZIP; 
    return null;
  }
  
  public String toString() {
    return this.name;
  }
}
