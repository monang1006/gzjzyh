package com.strongit.oa.report.query;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * 实现报表数据源接口,实现自定义填充数据.
 * 支持数据来源于JavaBean、数组和Map结构数据
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-12-17 上午11:30:30
 * @version  3.0
 * @classpath com.strongit.oa.report.query.JavaBeanDataSource
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class JavaBeanDataSource implements JRDataSource {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private int index = -1;
	
	private List data;	//报表数据
	
	public JavaBeanDataSource(List list) {
		this.data = list;
	}
	
	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;
		String name = field.getName();
		Object currentField = data.get(index);
		try {
			if(currentField instanceof Object[]){
				value = currentField;
			} else {
				value = PropertyUtils.getProperty(currentField, name);	
				if(value != null) {
					if(value instanceof Date || value instanceof Timestamp){
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						value = sdf.format((Date)value);
					} else {
						value = value.toString();
						
					}
				} else {
					value = " ";
				}
			}
		} catch (Exception e) {
			logger.error("获取报表属性时发生错误.", e);
			value = "";
		} 
		return value;
	}

	public boolean next() throws JRException {
		index++;
	    return (index < data.size());
	}

}
