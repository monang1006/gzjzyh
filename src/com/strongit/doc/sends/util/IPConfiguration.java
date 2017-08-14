package com.strongit.doc.sends.util;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



/**
 * 读取方正OA接口同步数据配置文件类
 * @author 李骏
 * @since    2012.12.20
 * @version 1.0
 *
 */
public class IPConfiguration {
        private Properties properties;
        /*
         * 单例模式
         */
        private final static IPConfiguration cfg = new IPConfiguration();
        /**
         * 构造函数
         * 
         */
        public IPConfiguration() {
                read();
        }
        /**
         * 私有的读取方法
         * 读取配置文件内容
         */
        private void read()
        {
        	properties = new Properties();
            InputStream is = null;
            try {
                    is = getClass().getResourceAsStream("/IP.properties");
                    properties.load(is);
            } catch (Exception exception) {
                    System.out.println("Can't read the properties file. ");
            } finally {
                    try {
                            if (is != null)
                                    is.close();
                    } catch (IOException exception) {
                            // ignored
                    }
            }
        }
        /**
         * 写入配置文件
         */
        public void write()
        {
        	
            try {
           
            	properties.store(new FileOutputStream(this.getClass().getClassLoader().getResource("").getPath()+"/UIMConfig.properties"), "/UIMConfig.properties");
            	
            	
               } catch (IOException e) {
                e.printStackTrace();
               }

        }

        /**
         * Use singleton pattern, only return one instance of Configuration.
         * @return Configuration Configuration
         */
        public static IPConfiguration getInstance() {
                return cfg;
        }

        /**
         * Retun a value for certain key.
         * @param key a certain key define in properties file.
         * @return value String
         */
        public String getValue(String key) {
                return properties.getProperty(key);
        }
        public void setValue(String key,String value) {
             properties.setProperty(key, value);
        }
}
