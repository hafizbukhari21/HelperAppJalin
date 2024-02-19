package com.jalin.helper.main;

import com.jalin.helper.service.NetService;
import org.apache.log4j.Logger;

import com.jalin.helper.service.HelperService;
import com.jalin.helper.util.LogUtil;
import java.util.*;

public class HelperMain {
	private static Logger log = LogUtil.getLogger(HelperMain.class.getName());
	public static void main(String[] args) throws Exception{
		log.info("Application Started");
        log.info("[ Run ] CommandLineRunner .. ");
        log.info("[ init ] Getting initial Data..");
	    String source;
	    String sourceDate;
		String datakey;
		int env;
	    if (args.length >= 2) {
	        source = args[0];
	        sourceDate = args[1];
			env = Integer.parseInt(args[2]);
			datakey = args[3];
	    } else {
	        // Argumen default jika tidak ada argumen yang diberikan
			log.info("Run Custom");
	        source = "2000";
	        sourceDate = "20240101";
			env =2;
			datakey = "2832014";
	    }
		
		HelperService.getDataToTransform(source, sourceDate, env, datakey);
		log.info("Application Ended");
//		String truststorePath = System.getProperty("java.home") + "/lib/security/cacerts";
//		System.out.println(truststorePat);
		/*
		String req  = "{\n" +
				"    \"name\":\"Test Lagiiii\",\n" +
				"    \"phoneNumber\":\"56788\"\n" +
				"}";
		NetService.postService("http://localhost:8080/contact/85868466-0fea-4948-a962-e2408d61c825",req,"PUT");
		*/
	}

}
