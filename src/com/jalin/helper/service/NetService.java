package com.jalin.helper.service;

import java.io.*;
import java.net.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.log4j.Logger;

import com.jalin.helper.util.LogUtil;

import javax.net.ssl.*;

public class NetService {
	private static Logger log = LogUtil.getLogger(NetService.class.getName());
	public static boolean postService(String link, String object, String requestMethod) throws Exception {
		String result = "";
		boolean resultbool = false;
		try {
			trustAllHosts();
			URL url = new URL(link);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(requestMethod);
			conn.setRequestProperty("Content-Type", "application/xml");
//			conn.setRequestProperty("Content-Type", "application/json");
			String input = object;
			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(input);
			writer.flush();
			writer.close();

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				response.append(line);
			}
			br.close();
			int responseCode = conn.getResponseCode();
			result = response.toString();
			log.info("Respone Code : " + responseCode);
			log.info("Payload : ");
			log.info(result);
			conn.disconnect();
			resultbool = true;
		} catch (Exception e) {
			log.error("Error " + e.getMessage());
		}
		return resultbool;
	}

	public static void trustAllHosts(){
		try{
			TrustManager[] trustAllCerts = new TrustManager[]{
					new X509ExtendedTrustManager(){
						@Override
						public java.security.cert.X509Certificate[] getAcceptedIssuers(){return null;}
						@Override
						public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType){}
						@Override
						public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType){}
						@Override
						public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string, Socket socket) throws CertificateException{}
						@Override
						public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string, Socket socket) throws CertificateException{}
						@Override
						public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string, SSLEngine ssle) throws CertificateException{}
						@Override
						public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string, SSLEngine ssle) throws CertificateException{}
					}
			};

			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new  HostnameVerifier(){
				@Override
				public boolean verify(String hostname, SSLSession session){
					return true;
				}
			};
			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		}
		catch (Exception e){
			log.error("Error occurred",e);
		}
	}

}
