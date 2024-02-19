package com.jalin.helper.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


public class MinifyXML {
	private static Properties progProp = PropertiesUtil.getInstance().getProgProp();
	public static String getMinifyXML(String xml) throws Exception {
	    // Create a TransformerFactory
	    TransformerFactory factory = TransformerFactory.newInstance();
	    
	    // Create a Transformer
	    Transformer transformer = factory.newTransformer();
	    
	    // Use the "identity" transformation to remove whitespace
	    transformer.setOutputProperty("omit-xml-declaration", "yes");
	    transformer.setOutputProperty("indent", "no");
	    
	    String completeRequest = progProp.getProperty("openingRequest") + xml + progProp.getProperty("closingRequest");
	    
	    // Create input and output streams
	    StringReader reader = new StringReader(completeRequest);
	    StringWriter writer = new StringWriter();
	    
	    // Transform the XML
	    transformer.transform(new StreamSource(reader), new StreamResult(writer));
	    
	    // Get the minified XML from the writer
	    String minifiedXML = writer.toString();
	    
	    // Close the streams
	    reader.close();
	    writer.close();
	    
	    return minifiedXML;
	}

}
