package com.test.demo.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * 从.properties文件中读取、写入、更新相关测试数据
 */
public class ParseProperties {
	private Properties pro = new Properties();
	private String value = null;
	
	public ParseProperties(String propertiespath){
		this.loadProperties(propertiespath);
	}
	
	private void loadProperties(String propertiespath){
		try {
			InputStream in = new FileInputStream(propertiespath);
			InputStreamReader inr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(inr);
			pro.load(br);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取
	 * @param keyname
	 * @return
	 */
	public String getValue(String keyname){
		value = pro.getProperty(keyname).trim();
		try {
			value = new String(value.getBytes("UTF-8"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
	
	public String getTestData(String configFilePath, String keyname) {
	   // 生成输入流  
	   InputStream ins = ParseProperties.class.getResourceAsStream(configFilePath); 
	   // 生成properties对象 
	   Properties config = new Properties();  
	   try {
		   config.load(ins);
	    } catch (IOException e) {
			e.printStackTrace();
	    }
	   return String.valueOf(config.getProperty(keyname)); //转换String类型
	}
	
	
	
    /**  
    * 更新（或插入）一对properties信息(主键及其键值)  
    * 如果该主键已经存在，更新该主键的值；  
    * 如果该主键不存在，则插件一对键值。  
    * @param keyname 键名  
    * @param keyvalue 键值  
    */   
    public void writeProperties(String propertiespath, String keyname,String keyvalue) {          
        try {   
            // 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。   
            // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。   
            OutputStream fos = new FileOutputStream(propertiespath);   
            pro.setProperty(keyname, keyvalue);   
            // 以适合使用 load 方法加载到 Properties 表中的格式，   
            // 将此 Properties 表中的属性列表（键和元素对）写入输出流   
            pro.store(fos, "Update '" + keyname + "' value");   
        } catch (IOException e) {   
            System.err.println("属性文件更新错误");   
        }   
    } 

    
    /**  
    * 更新properties文件的键值对  
    * 如果该主键已经存在，更新该主键的值；  
    * 如果该主键不存在，则插件一对键值。  
    * @param keyname 键名  
    * @param keyvalue 键值  
    */   
    public void updateProperties(String profilepath, String keyname,String keyvalue) {   
        try {   
        	pro.load(new FileInputStream(profilepath));   
            // 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。   
            // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。   
            OutputStream fos = new FileOutputStream(profilepath);              
            pro.setProperty(keyname, keyvalue);   
            // 以适合使用 load 方法加载到 Properties 表中的格式，   
            // 将此 Properties 表中的属性列表（键和元素对）写入输出流   
            pro.store(fos, "Update '" + keyname + "' value");   
        } catch (IOException e) {   
            System.err.println("属性文件更新错误");   
        }   
    } 
    
    
	public static void main(String[] args){
		ParseProperties a = new ParseProperties("D:\\workspace\\AllRound_Test\\config\\url.properties");
		System.out.println(a.getValue("beike_webUrl"));
		
		a.writeProperties("D:\\workspace\\AllRound_Test\\config\\url.properties", "A", "1");
		a.updateProperties("D:\\workspace\\AllRound_Test\\config\\url.properties", "A", "2");
		
	}
}
