package com.strongit.oa.senddoc.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.axis.encoding.Base64;

import com.strongmvc.exception.SystemException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Json格式和Map结构数据转换
 * @author 		邓志城
 * @company  	Strongit Ltd. (C) copyright
 * @date   		Feb 21, 2012
 * @classpath	com.strongit.oa.senddoc.util.JsonUtil
 * @version  	5.0
 * @email		dengzc@strongit.com.cn
 * @tel			0791-8186916
 */
public class JsonUtil {

	/*public static void main(String...strings) throws Exception {
		String str = "test数据";
		str = Base64.encode(str.getBytes("utf-8"));
		System.out.println(str);
		str = new String(Base64.decode(str),"utf-8");
		System.out.println(str);
	}*/
	
	/**
	 * 将Map结构的意见内容转换成Json格式字符串.
	 * @param data
	 * 	Map<控件名称,List<String[意见,处理人id,处理人名称,处理时间,第一个委派/指派人id,委托人姓名,记录id,所属机构名称,最后委托人名称(作为实际处理时间使用)>>
	 * @return
	 */
	public static String generateApproveToJson(Map<String, List<String[]>> data) {
		JSONArray map = new JSONArray();
		if(data != null) {
			Set<Entry<String, List<String[]>>> entries = data.entrySet();
			for(Entry<String, List<String[]>> entry : entries) {
				String controlName = entry.getKey();
				JSONObject json = new JSONObject();
				json.put(controlName, JSONArray.fromObject(entry.getValue()).toString());
				map.add(json);
			}
		}
		return map.toString();
	}

	public static String generateApproveToJsonBase64(Map<String, List<String[]>> data) throws SystemException {
		String json = generateApproveToJson(data);
		try {
			json = Base64.encode(json.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			throw new SystemException(e);
		}
		return json;
	}

	public static Map<String, List<String[]>> generatejsonToApproveBase64(String data) throws SystemException {
		try {
			data = new String(Base64.decode(data),"utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new SystemException(e);
		}
		return generateJsonToApprove(data);
	}
	
	/**
	 * 将Json格式字符串转换成Map结构意见内容
	 * @param data		Json格式
	 * @return
	 * Map<控件名称,List<String[意见,处理人id,处理人名称,处理时间,第一个委派/指派人id,委托人姓名,记录id,所属机构名称,最后委托人名称(作为实际处理时间使用)>>
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, List<String[]>> generateJsonToApprove(String data) {
		JSONArray array = JSONArray.fromObject(data);
		Map<String, List<String[]>> map = new HashMap<String, List<String[]>>();
		if(array != null && !array.isEmpty()) {
			for(int i=0;i<array.size();i++) {
				JSONObject json = array.getJSONObject(i);
				Set keys = json.keySet();
				for(Iterator it = keys.iterator(); it.hasNext();) {
					String key = it.next().toString();
					String strArray = json.getString(key);
					JSONArray objArray = JSONArray.fromObject(strArray);
					List<String[]> values = new ArrayList<String[]>();
					for(int j=0;j<objArray.size();j++) {
						if(objArray.get(j) instanceof JSONArray){
							JSONArray obj = (JSONArray)objArray.get(j);
							String[] strs = new String[obj.size()];
							for(int k=0;k<obj.size();k++) {
								String o = obj.getString(k);
								strs[k] = o;
							}
							values.add(strs);
						}else{
							System.out.println();
						}
					}
					map.put(key, values);
				}
			}
		}
		return map;
	}
}
