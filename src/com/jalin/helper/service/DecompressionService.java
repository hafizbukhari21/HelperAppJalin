package com.jalin.helper.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

import com.jalin.helper.util.StringToBinaryInputStreamUtil;

public class DecompressionService {
	
	public static String decompressString(String paramString, CompressionType paramCompressionType, boolean paramBoolean) throws Exception {
	    StringBuilder stringBuilder = new StringBuilder();
	    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(paramString.getBytes(StandardCharsets.ISO_8859_1));
	    if (paramBoolean)
	      byteArrayInputStream.skip(paramCompressionType.toString().length()); 
	    StringToBinaryInputStreamUtil stringToBinaryInputStream = new StringToBinaryInputStreamUtil(byteArrayInputStream);
	    try {
	      InputStream inputStream = getInputStream((InputStream)stringToBinaryInputStream, paramCompressionType);
	      try {
	        byte[] arrayOfByte = new byte[1024];
	        int i;
	        while ((i = inputStream.read(arrayOfByte)) != -1)
	          stringBuilder.append(new String(arrayOfByte, 0, i, StandardCharsets.UTF_8)); 
	        if (inputStream != null)
	          inputStream.close(); 
	      } catch (Throwable throwable) {
	        if (inputStream != null)
	          try {
	            inputStream.close();
	          } catch (Throwable throwable1) {
	            throwable.addSuppressed(throwable1);
	          }  
	        throw throwable;
	      } 
	      stringToBinaryInputStream.close();
	    } catch (Throwable throwable) {
	      try {
	        stringToBinaryInputStream.close();
	      } catch (Throwable throwable1) {
	        throwable.addSuppressed(throwable1);
	      } 
	      throw throwable;
	    } 
	    return stringBuilder.toString();
	  }
	
	public static CompressionType getCompressionType(String paramString) {
	    CompressionType compressionType = null;
	    if (paramString.startsWith("GZ"))
	      compressionType = CompressionType.GZIP; 
	    return compressionType;
	}
	
	public static InputStream getInputStream(InputStream paramInputStream, CompressionType paramCompressionType) throws Exception {
	    GZIPInputStream gZIPInputStream = null;
	    if (paramCompressionType == CompressionType.GZIP) {
	      gZIPInputStream = new GZIPInputStream(paramInputStream);
	    } else {
	      throw new IllegalArgumentException("Unknown type: " + paramCompressionType);
	    } 
	    return gZIPInputStream;
	}
}
