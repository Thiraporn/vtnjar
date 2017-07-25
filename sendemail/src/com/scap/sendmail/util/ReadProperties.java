package com.scap.sendmail.util; 

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap; 
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ReadProperties {
	
   /*public static void main(String[] args){
		 
	    String propertiesName = "db.properties";
		//String propertiesName = "conf/db.properties";
		ReadProperties prop = new ReadProperties();
	    ArrayList<HashMap<String,String>>  propData = prop.getDataObjReadPropertiesFile(propertiesName);
        System.out.println("propData "+propData);
 
		 
		 
	 
	 
		//"hospital.properties"
		//"db.properties"
		//"log4j.properties"
	} */
	
	public Map<String, String>  getDataReadPropertiesFile(String propertyFileName) {

		Properties prop = new Properties();
		InputStream input = null; 
		HashMap<String, String> map = new HashMap<String, String>();
		try {

			String filename = propertyFileName;
			input = getClass().getClassLoader().getResourceAsStream(filename);

			// not found file
			if (input == null) {
				return map;
			}

			// found file
			prop.load(input);

			// Java and Enumeration
			// เป็น Interface ที่ใช้ในการอ้างถึงสมาชิกดึงข้อมูลจาก Collection
			// ประเภท Set ต่าง ๆ โดยมี method สำคัญ ๆ คือ hasMoreElements และ
			// nextElement
			Enumeration<?> e = prop.propertyNames();

			while (e.hasMoreElements()) {
				//map = new HashMap<String, String>();
				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				 
			    map.put(key,value); 
			}
		
			 

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return map;

	}
	public  ArrayList<HashMap<String,String>> getDataObjReadPropertiesFile(String propertyFileName) {
		Map<String, String>  propData =  getDataReadPropertiesFile(propertyFileName); 
		ArrayList<HashMap<String,String>> dataList = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = null;
        
        
        
        Set<String> keys = propData.keySet();
        for(String key: keys){
        	map = new HashMap<String, String>(); 
        	String value = propData.get(key);
        	 
        	String  vaKey = value.split(":")[0];
			String  vaValue = value.split(":")[1];
			map.put("CODE", vaKey);
        	map.put("NAME", vaValue);
        	dataList.add(map);
            //System.out.println("CODE   "+vaKey+" NAME "+ vaValue);
        } 
       return dataList;
		
	}

}
