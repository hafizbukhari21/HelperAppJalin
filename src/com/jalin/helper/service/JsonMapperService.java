package com.jalin.helper.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jalin.helper.util.LogUtil;
import org.apache.log4j.Logger;

public class JsonMapperService {
    private static Logger log = LogUtil.getLogger(JsonMapperService.class.getName());
	public static String getOriXMLReq(String jsonString) {
		String oriXMLVal = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            // Mengakses nilai tertentu dalam JSON
            oriXMLVal = jsonNode.get("tieredData").get("fields").get("ORIGINAL_XML_REQUEST").get("value").asText();
            log.info("Read JSON Tree success");
        } catch (Exception e) {
            log.info("Error :" + e.getMessage());
        }
		return oriXMLVal;
	}
}
