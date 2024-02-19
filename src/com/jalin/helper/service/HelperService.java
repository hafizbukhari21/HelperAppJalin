package com.jalin.helper.service;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.jalin.helper.util.PropertiesUtil;
import com.jalin.helper.util.DatabaseUtil;
import com.jalin.helper.util.LogUtil;

public class HelperService {
	private static Logger log = LogUtil.getLogger(HelperService.class.getName());
	private static Properties progProp = PropertiesUtil.getInstance().getProgProp();
	public static void getDataToTransform(String source, String sourceDate, int env, String datakey){
		try {
			String[] column = {"RAW_DATA"};
			int countTemp = 0;
			log.info("[ init ] Source : " + source + ", Source Date : " + sourceDate);
			log.debug("Get List Raw Data from DB...");
			ArrayList<String> listRawData = new ArrayList<>();
			int deletedData = 0;
			if(datakey.length()==0){
				//Source Date
				listRawData = DatabaseUtil.getMainQuery(progProp.getProperty("retrieveRowData").replace("$source", source).replace("$date", sourceDate), column, env);
			}
			else {
				//DataKey
				listRawData = DatabaseUtil.getMainQuery(progProp.getProperty("retrieveRowDataPerDataKey").replace("$data_key", datakey), column, env);
			}
			ArrayList<String> listJsonData = new ArrayList<String>();
			ArrayList<String> listReqVal = new ArrayList<String>();
			ArrayList<String> listFailedHit = new ArrayList<String>();
			if(listRawData.size() != 0){
				log.info("Success get list raw data, count :" + listRawData.size());
				log.info("Decompress raw data...");
				for (String string : listRawData) {
					String rawDat = DecompressionService.decompressString(string, CompressionType.GZIP, true);
					listJsonData.add(rawDat);
					log.info(rawDat);
				}
				log.info("Success decompression raw data");
				log.info("Retrieve Value from JSON & construct Request XML...");
				for (String string : listJsonData) {
					String getXMLReq = JsonMapperService.getOriXMLReq(string);
					if (!getXMLReq.isEmpty()){
						if(!getXMLReq.contains(progProp.getProperty("openingRequestChecker"))){
							getXMLReq = progProp.getProperty("openingRequestChecker") + getXMLReq;
						}
						String finalReqString = progProp.getProperty("openingRequest")
								+ getXMLReq
								+ progProp.getProperty("closingRequest");
						listReqVal.add(finalReqString);
						log.info(finalReqString);
						continue;
					}
					log.info("Bypass data, ORIGINAL_XML_REQUEST from JSON is null");
				}
				log.info("Success retrieve value & construct Request XML");
				log.info("Total List XML for HIT API count :" + listReqVal.size());
				log.info("Hit API...");
				for (String request : listReqVal) {
					String link = "";
					if (env==1) {
						log.info("Push To ENV VIT");
						link = progProp.getProperty("linkAPI");
					}
					else{
						log.info("Push To ENV Staging");
						link = progProp.getProperty("linkAPIStaging");
					}
					
					String requestMethod = progProp.getProperty("requestMethod");
					boolean isSuccess = NetService.postService(link, request, requestMethod);
					if(isSuccess){
						countTemp++;
						log.info("HIT API Success");
						if(datakey.length()==0){
							//Source Date
							deletedData = DatabaseUtil.deletedQuery(progProp.getProperty("deleteRowData").replace("$source", source).replace("$date", sourceDate), column, env);
						}
						else {
							//DataKey
							deletedData = DatabaseUtil.deletedQuery(progProp.getProperty("deleteRowDataPerDataKey").replace("$data_key", datakey), column, env);
						}
					}else{
						log.info("HIT API Failed");
						listFailedHit.add(request);
					}

				}
				
				logTheSummary(listRawData.size(), listReqVal.size(), countTemp, listFailedHit, deletedData);
			}else{
				log.info("Sorry we cannot find Raw Data with source : " + source + " and source date : " + sourceDate);
				log.info("Please try again...");
			}
		} catch (Exception e) {
			log.error("Error " + e.getMessage());
		}
	}
	
	private static void logTheSummary(int rawDataSize, int reqValSize, int countSuccessHit, ArrayList<String> listFailedHit, int deletedData){
		log.info("Hit API Is All Done, here's the summary :");
		log.info("	Success get list raw data, count : " + rawDataSize + " Raw data");
		log.info("	Raw data that have ORIGINAL_XML_REQUEST value, count : " + reqValSize+ " XML Request");
		log.info("	SUCCESS HIT API, count : " + countSuccessHit + " Hit");
		log.info("	FAILED HIT API, count : " + listFailedHit.size() + " Hit");
		log.info("	Here's List XML Request for FAILED HIT API : ");
		log.info("	Deleted Item From Source_data: "+deletedData);
		if(listFailedHit.size() == 0){
			log.info("		-->No Failed Request Data<--");
		}
		for (String request : listFailedHit) {
			log.info(request);
		}
	}
}
