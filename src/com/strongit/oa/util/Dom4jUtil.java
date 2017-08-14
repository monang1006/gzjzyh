package com.strongit.oa.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis.utils.JavaUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.strongit.oa.ipadoa.Attachment;
import com.strongit.oa.ipadoa.FormData;
import com.strongit.oa.ipadoa.Item;
import com.strongit.oa.ipadoa.Status;
import com.strongit.oa.ipadoa.model.Suggestion;
import com.strongit.oa.webservice.iphone.server.iphoneWork.iphoneWorkWebService;


/**
 * 处理WEBSERVICE交互XML的工具类
 * @author 邓志城
 *
 */
public class Dom4jUtil {

	private List<Class> basicDataType = new ArrayList<Class>();
	
	private Document document = null;
	
	private String strXML = null;
	
	public Dom4jUtil(){
		basicDataType.add(int.class);
		basicDataType.add(Integer.class);
		basicDataType.add(double.class);
		basicDataType.add(Double.class);
		basicDataType.add(float.class);
		basicDataType.add(Float.class);
		basicDataType.add(String.class);
		basicDataType.add(Date.class);
		basicDataType.add(Timestamp.class);
		basicDataType.add(byte[].class);
		basicDataType.add(Long.class);
		basicDataType.add(long.class);	
	}

	/**
	 * 从一个输入流中得到文档对象.
	 * @author:邓志城
	 * @date:2010-7-22 上午09:22:05
	 * @param is	文件输入流对象
	 * @return		文档对象
	 * @throws DocumentException
	 */
	public Document loadXML(InputStream is) throws DocumentException { 
		SAXReader reader = new SAXReader();
		document = reader.read(is);
		return document;
	}
	
	/**
	 * 加载XML文档
	 * @author:邓志城
	 * @date:2009-7-8 下午05:20:05
	 * @param strXML XML字符
	 */
	public Document loadXML(String strXML){
		SAXReader reader = new SAXReader();
		try {
			document = reader.read(new StringReader(strXML));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}
	
	/**
	 * 获取指定的bean所属基本类型
	 * @author:邓志城
	 * @date:2009-7-6 下午05:30:00
	 * @param bean
	 * @return
	 */
	private String getTypeOfBean(Object bean){
		for(Class cls:basicDataType){
			if(bean.getClass().equals(cls)){
				return cls.getSimpleName().toLowerCase();
			}
		}
		return bean.getClass().getName();
	}
	
	/**
	 * 创建根节点
	 * @author:邓志城
	 * @date:2009-7-6 下午04:12:58
	 * @param document 文档对象
	 * @return rootElement 根节点
	 */
	private Element createRootElement(Document document){
		Element rootElement = document.addElement("service-response");
		return rootElement;
	}

	/**
	 * 创建状态节点
	 * @author:邓志城
	 * @date:2009-7-6 下午04:16:34
	 * @param rootElement 根节点
	 * @param status 返回状态【1：成功；0：失败】
	 * @return statusElement 状态节点
	 */
	private Element createStatusElement(Element rootElement,String status){
		Element statusElement = rootElement.addElement("status");
		statusElement.addText(status);
		return statusElement;
	}
	
	/**
	 * 在property节点下创建Item节点
	 * @author:汤世凤
	 * @date:2012-2-15 下午03:09:25
	 * @param propertyElement
	 * @param itemType
	 * @param itemKey
	 * @param itemValue
	 * @return
	 */
	private Element createItemElement(Element propertyElement,String itemType,String itemKey,String itemValue){
		Element itemElement = propertyElement.addElement("item");
		itemElement.addAttribute("type", itemType);
		itemElement.addAttribute("key", itemKey);
		itemElement.addAttribute("value", itemValue);
		return itemElement;
	}
	

	


	/**
	 * 创建失败描述节点
	 * @author:邓志城
	 * @date:2009-7-6 下午04:21:17
	 * @param rootElement 根节点
	 * @param failReason 失败描述信息
	 * @return 失败描述节点
	 */
	private Element createFailReasonElement(Element rootElement,String failReason){
		Element failReasonElement = rootElement.addElement("fail-reason");
		if(failReason!=null && !"".equals(failReasonElement)){ 
			failReasonElement.addText(failReason);
		}
		return failReasonElement;
	}

	private Element createRowElement(Element dataElement) {
		Element rowElement = dataElement.addElement("row");
		return rowElement;
	}
	
	private Element createElement(Element el , String name) {
		return el.addElement(name);
	}
	
	/**
	 * 创建data节点
	 * @author:邓志城
	 * @date:2009-7-6 下午04:41:32
	 * @param rootElement
	 * @param responseData
	 * @return
	 */
	private Element createDataElement(Element rootElement){
		return createDataElement(rootElement, null,null);
	}

	/**
	 * 创建data节点
	 * @author:邓志城
	 * @date:2009-7-6 下午04:41:32
	 * @param rootElement
	 * @param responseData
	 * @return
	 */
	private Element createDataElement(Element rootElement,String count,String totalPages){
		Element dataElement = rootElement.addElement("data");
		if(count != null) {
			dataElement.addAttribute("totalCount", count);			
		}
		if(totalPages != null) {
			dataElement.addAttribute("totalPages", totalPages);
		}
		return dataElement;
	}
	
	/**
	 * 创建list节点
	 * @author:邓志城
	 * @date:2009-7-6 下午04:40:32
	 * @param dataElement data节点
	 * @return list节点
	 */
	private Element createListElement(Element dataElement){
		Element listElement = dataElement.addElement("list");
		return listElement;
	}

	/**
	 * 创建bean节点
	 * @author:邓志城
	 * @date:2009-7-6 下午04:39:31
	 * @param listElement list节点
	 * @return bean节点
	 */
	private Element createBeanElement(Element listElement){
		Element beanElement = listElement.addElement("bean");
		return beanElement;
	}

	/**
	 * 创建property节点【含属性】
	 * 用户List中对象时标准JavaBean
	 * @author:邓志城
	 * @date:2009-7-6 下午04:38:50
	 * @param beanElement bean节点
	 * @return property节点
	 */
	private Element createPropertyElement(Element beanElement,String name){
		Element propertyElement = beanElement.addElement("property");
		propertyElement.addAttribute("name",name);
		return propertyElement;
	}

	/**
	 * 创建property节点【不含属性】
	 * 用户List中对象不是标准JavaBean情况
	 * @author:邓志城
	 * @date:2009-7-6 下午04:38:50
	 * @param beanElement bean节点
	 * @return property节点
	 */
	private Element createPropertyElement(Element beanElement){
		Element propertyElement = beanElement.addElement("property");
		return propertyElement;
	}
	
	/**
	 * 在property节点下创建Item节点
	 * @author:邓志城
	 * @date:2009-7-6 下午04:35:57
	 * @param propertyElement property节点
	 * @param itemType 元素类型【string，date，int...】
	 * @param itemValue 元素的value值
	 * @return item节点
	 */
	private Element createItemElement(Element propertyElement,String itemType,String itemValue){
		Element itemElement = propertyElement.addElement("item");
		itemElement.addAttribute("type", itemType);
		itemElement.addAttribute("value", itemValue);
		return itemElement;
	}

	/**
	 * 获取根节点
	 * @author:邓志城
	 * @date:2009-7-8 下午05:18:07
	 * @return
	 */
	private Element getRootElement(){
		return document.getRootElement();
	}

	/**
	 * 递归查找名为tagName的元素
	 * @author:邓志城
	 * @date:2009-6-26 下午03:01:09
	 * @param el 需要递归的元素
	 * @param tagName el下的节点
	 * @param elLst 返回el的所有节点名为tagName的节点
	 * @return
	 */
	private List<Element> recursiveElement(Element el,String tagName,List<Element> elLst){
		List lst = el.elements();
		for(int i=0;i<lst.size();i++){
			Element tempEl = (Element)lst.get(i);
			if(tempEl.getName().equals(tagName)){
				elLst.add(tempEl);
			}
			recursiveElement(tempEl, tagName, elLst);
		}
		return elLst;
	}
	
	/**
	 * 根据节点名称获取文档中所有名为tagName的元素
	 * @author:邓志城
	 * @date:2009-6-26 下午03:07:48
	 * @param tagName
	 * @return
	 */
	public List<Element> getElementsByTagNames(String tagName){
		List<Element> elLst = new ArrayList<Element>();
		Element rootElement = getRootElement();
		elLst = recursiveElement(rootElement, tagName, elLst);
		return elLst;
	}
	
	/**
	 * 创建含list的节点XML文档,若responseData不存在，则直接创建一个data空节点
	 * list中的对象必须是标准的javabean对象
	 * @author:邓志城
	 * @date:2009-7-6 下午05:10:35
	 * @param responseData list元素列表
	 * @param status 返回状态
	 * @param failReason 异常描述信息
	 * @return XML文档描述信息
	 */
	public String createListResponseData(List responseData,String status,String failReason){
		//创建文档对象
		Document document = DocumentHelper.createDocument();
		//创建根节点
		Element rootElement = createRootElement(document);
		//创建返回状态节点
		createStatusElement(rootElement, status);
		//创建失败信息节点
		createFailReasonElement(rootElement, failReason);
		//创建data节点
		Element dataElement = createDataElement(rootElement);
		if("1".equals(status) && responseData!=null && responseData.size()>0){
			//创建data节点
			Element listElement = createListElement(dataElement);
			for(int i=0;i<responseData.size();i++){
				Object bean = responseData.get(i);
				//创建bean节点
				Element beanElement = createBeanElement(listElement);
				//如果List中的对象时数组
				if(bean instanceof Object[]){
					Object[] objs = (Object[])bean;
					for(int j=0;j<objs.length;j++){
						Element propertyElement = createPropertyElement(beanElement);
						createItemElement(propertyElement, getTypeOfBean(objs[j]), String.valueOf(objs[j]));  
					}
				}else{//不是数组而是对象的情况
					Field[] fields = bean.getClass().getDeclaredFields();
					AccessibleObject.setAccessible(fields, true);
					for(Field field:fields){
						if(!Modifier.isStatic(field.getModifiers())&&!Modifier.isFinal(field.getModifiers())){
							String fieldName = field.getName();
							Object fieldValue = null;
							String fieldType = field.getType().getSimpleName().toLowerCase();
							try {
								fieldValue = field.get(bean);
							} catch (IllegalArgumentException e) {
								fieldValue = "获取"+fieldName+"值异常:"+e.getMessage();
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								fieldValue = "获取"+fieldName+"值异常:"+e.getMessage();
								e.printStackTrace();
							}
							if(!fieldName.equals("serialVersionUID")&&fieldName.indexOf("$")==-1&&fieldValue!=null){
								Element propertyElement = createPropertyElement(beanElement,fieldName);
								createItemElement(propertyElement, fieldType, String.valueOf(fieldValue));
							}							
						}
					}					
				}
			}
		}
		//saveXMLToFile("c:\\webservice.xml", document);
		return document.asXML();
	}

	/**
	 * 创建只有一个bean的XML文档
	 * @author:邓志城
	 * @date:2009-7-6 下午05:48:00
	 * @param responseData
	 * @param status
	 * @param failReason
	 * @return
	 */
	public String createBeanResponse(List responseData,String status,String failReason){
		//创建文档对象
		Document document = DocumentHelper.createDocument();
		//创建根节点
		Element rootElement = createRootElement(document);
		//创建返回状态节点
		createStatusElement(rootElement, status);
		//创建失败信息节点
		createFailReasonElement(rootElement, failReason);
		//创建data节点
		Element dataElement = createDataElement(rootElement);
		//创建data节点
		Element listElement = createListElement(dataElement);
		if("1".equals(status)){//调用成功时
			//创建bean节点
			Element beanElement = createBeanElement(listElement);
			for(int i=0;i<responseData.size();i++){
				Object bean = responseData.get(i);
				Element propertyElement = createPropertyElement(beanElement);
				createItemElement(propertyElement, getTypeOfBean(bean), String.valueOf(bean));
			}			
		}
		return document.asXML();
	}
	
	/**
	 * 返回操作结果
	 * @author:邓志城
	 * @date:2009-7-6 下午05:16:31
	 * @param status
	 * @param failReason
	 * @param itemType 操作结果类型
	 * @param itemValue 操作结果
	 * @return
	 */
	public String createItemResponseData(String status,String failReason,String itemType,String itemValue){
		//创建文档对象
		Document document = DocumentHelper.createDocument();
		//创建根节点
		Element rootElement = createRootElement(document);
		//创建返回状态节点
		createStatusElement(rootElement, status);
		//创建失败信息节点
		createFailReasonElement(rootElement, failReason);
		//创建data节点
		Element dataElement = createDataElement(rootElement);
		if("1".equals(status)){
			createItemElement(dataElement, itemType, itemValue);			
		}
		return document.asXML();
	}
	
	/**
	 * 返回XML格式数据
	 * @param status			状态码 0：失败；1：成功
	 * @param failReason		异常信息
	 * @param items				返回的数据列
	 * @return					XML格式字符数据
	 */
	public String createItemsResponseData(String status,String failReason,List<String[]> items, int elementNum,String totalCount,String totalPages) {
		//创建文档对象
		Document document = DocumentHelper.createDocument();
		//创建根节点
		Element rootElement = createRootElement(document);
		//创建返回状态节点
		createStatusElement(rootElement, status);
		//创建失败信息节点
		createFailReasonElement(rootElement, failReason);
		//创建data节点
		Element dataElement = createDataElement(rootElement,totalCount,totalPages);
		if("1".equals(status)){
			if(items != null && !items.isEmpty()) {
				Element rowElement = null;
				for(int i=0;i<items.size();i++) {
					String[] objs = items.get(i);
					if(i % elementNum == 0) {
						rowElement = createRowElement(dataElement);
					}
					createItemElement(rowElement, objs[0], objs[1]);
					//createItemElement(dataElement, objs[0], objs[1]);
				}
			}
		}
		return document.asXML();
	}

	//返回公文业务数据和附件数据
	public String createItemsResponseData(String status,String failReason,List<String[]> items,List<String[]> attachs,List<Object[]> suggestions) {
//		创建文档对象
		Document document = DocumentHelper.createDocument();
		//创建根节点
		Element rootElement = createRootElement(document);
		//创建返回状态节点
		createStatusElement(rootElement, status);
		//创建失败信息节点
		createFailReasonElement(rootElement, failReason);
		//创建data节点
		Element dataElement = createDataElement(rootElement);
		if("1".equals(status)) {
			Element rowElement = this.createElement(dataElement, "form");
			if(items != null && !items.isEmpty()) {
				for(String[] item : items) {
					createItemElement(rowElement, item[0], item[1]);
				}
			}
			rowElement = this.createElement(dataElement, "attachments");
			if(attachs != null && !attachs.isEmpty()) {
				for(String[] item : attachs) {
					Element attach = this.createElement(rowElement, "attachment");
					createItemElement(attach, item[0], item[1]);
					createItemElement(attach, item[2], item[3]);
				}
			}
			rowElement = this.createElement(dataElement, "suggestions");
			if(suggestions != null && !suggestions.isEmpty()) {
				for(Object[] item : suggestions) {
					Element suggestion = this.createElement(rowElement, "suggestion");
					createItemElement(suggestion, "date", item[7].toString());//任务处理时间
					createItemElement(suggestion, "string", item[10] == null ? "" : item[10].toString());//处理人部门名称
					createItemElement(suggestion, "string", item[6] == null ? "" : item[6].toString());//处理人名称
					createItemElement(suggestion, "string", item[4] == null ? "" : item[4].toString());//意见内容
				}
			}
		}
		return document.asXML();
	}
	
	/**
	 * 将XML文档输出至文件系统
	 * @author:邓志城
	 * @date:2009-7-7 下午02:44:41
	 * @param fileName 文件名【文件完成路径和文件名称】
	 * @param document XML文档对象
	 */
	public void saveXMLToFile(String fileName,Document document){
		try{
			File file = new File(fileName);
			if(!file.exists()){
				file.createNewFile();
			}
			//美化输出格式
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			XMLWriter writer = new XMLWriter(new FileOutputStream(file),format);
			writer.write(document);
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 是否是基本类型
	 * @author:邓志城
	 * @date:2009-7-7 上午09:27:49
	 * @param clazz
	 * @return
	 */
	private boolean isSimpleType(Class clazz) {    
        return clazz.isPrimitive() || String.class.isAssignableFrom(clazz) || Number.class.isAssignableFrom(clazz)    
                || java.util.Date.class.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz)    
                || Character.class.isAssignableFrom(clazz);    
    }

	/**
	 * 
	 * @author:邓志城
	 * @date:2010-11-9 上午09:55:52
	 * @param name
	 * @return
	 */
	public String getAttribute(String xPath,String name) {
		if(document == null) {
			return null;
		}
		List list = document.selectNodes(xPath);//document.selectNodes("eform/dataset/table");
		if(list != null && !list.isEmpty()) {
			Element element = (Element)list.get(0);
			return element.attributeValue(name);
		}
		return null;
	}
	
	public static void main(String...strings) throws DocumentException{
		Dom4jUtil dom = new Dom4jUtil();
		Document document = dom.loadXML(Dom4jUtil.class.getClassLoader().getResourceAsStream("ldapQuery.xml"));
		List queryElements = document.getRootElement().selectNodes("query");
		Map queryMap = new HashMap(0);
		for (int i = 0; i < queryElements.size(); i++){
			Element queryElement = (Element)queryElements.get(i);
			
			String query = queryElement.getText();
			query = query.replaceAll("\\t", " ");
			query = query.replaceAll("\\n", " ");
			query = query.replaceAll("\\s+=\\s+", "=");
			query = query.trim();
			queryMap.put(queryElement.attributeValue("name"), query);
		}
		System.out.println(queryMap);
	}

	/**
	* @method createItemsResponseData
	* @author 申仪玲
	* @created 2011-11-9 下午09:14:03
	* @description 描述
	* @return String 返回类型
	*/
	
	public String createItemsResponseData(String status, String failReason, String itemType, String itemValue, String totalPage) {
		//创建文档对象
		Document document = DocumentHelper.createDocument();
		//创建根节点
		Element rootElement = createRootElement(document);
		//创建返回状态节点
		createStatusElement(rootElement, status);
		//创建失败信息节点
		createFailReasonElement(rootElement, failReason);
		//创建data节点
		Element dataElement = createDataElement(rootElement, null,totalPage);
		if("1".equals(status)){
			createItemElement(dataElement, itemType, itemValue);			
		}
		return document.asXML();
	}
	
	/**
	 * 创建su节点
	 * @author:邓志城
	 * @date:2009-7-6 下午04:41:32
	 * @param rootElement
	 * @param responseData
	 * @return
	 */
	private Element createSuggestionElement(Element rootElement,String elementName){
		Element dataElement = rootElement.addElement(elementName);
		return dataElement;
	}
	
	public String createItemsUsedSuggestionData(String status,
			String failReason, List<String> suggestionList) {
		// 创建文档对象
		Document document = DocumentHelper.createDocument();
		// 创建根节点
		Element rootElement = createRootElement(document);
		// 创建返回状态节点
		createStatusElement(rootElement, status);
		// 创建失败信息节点
		createFailReasonElement(rootElement, failReason);
		// 创建suggestion节点
		Element suggestionElement = rootElement.addElement("suggestion");
	 
		if ("1".equals(status)) {
			for (String list : suggestionList)
				createItemElement(suggestionElement, "String", list);
		}
		return document.asXML();
	}
	
	public String createItemsTransitionsData(String status,
			String failReason, List<String[]> transitions) {
		// 创建文档对象
		Document document = DocumentHelper.createDocument();
		// 创建根节点
		Element rootElement = createRootElement(document);
		 // 创建返回状态节点
		createStatusElement(rootElement, status);
		// 创建失败信息节点
		createFailReasonElement(rootElement, failReason);
		// 创建transitions节点
		Element transitionsElement = rootElement.addElement("transitions");
		Element transitionElement;
		
		if ("1".equals(status)) {
			for(int i=0;i<transitions.size();i++) {
				 transitionElement = transitionsElement.addElement("transition");
				String[] objs = transitions.get(i);
				for(int m=0;m<objs.length;m++){
					createItemElement(transitionElement, "String", objs[m]);
					//createItemElement(transitionElement, "String", objs[1]);
					//createItemElement(transitionElement, "String", objs[2]);
					//createItemElement(transitionElement, "String", objs[3]);
					//createItemElement(transitionElement, "String", objs[4]);
				}
			 }
		}
		return document.asXML();
	}
	public String createItemsTransitionsAndActorData(String status,
			String failReason, List<String[]> transitions,String taskId) {
		// 创建文档对象
		Document document = DocumentHelper.createDocument();
		// 创建根节点
		Element rootElement = createRootElement(document);
		 // 创建返回状态节点
		createStatusElement(rootElement, status);
		// 创建失败信息节点
		createFailReasonElement(rootElement, failReason);
		// 创建transitions节点
		Element transitionsElement = rootElement.addElement("transitions");
		Element transitionElement;
		
		 iphoneWorkWebService workWebService = new iphoneWorkWebService();//获取数据接口
		if ("1".equals(status)) {
			for(int i=0;i<transitions.size();i++) {
				 transitionElement = transitionsElement.addElement("transition");
				String[] objs = transitions.get(i);
				List<String[]> taskActors = new ArrayList<String[]>();
				String nodeId = "";
				String transitionId = "";
				if(objs[5].equals("1")){
					nodeId = objs[3];
					transitionId = objs[0];
//					if(nodeId!=null&&transitionId!=null){
//					taskActors = workWebService.getActor(nodeId, taskId, transitionId); 
//					}
					for(int m=0;m<objs.length;m++){
						createItemElement(transitionElement, "String", objs[m]);
					}
					if(taskActors.size()==1){
					createItemElement(transitionElement, "String", taskActors.get(0)[0]);
					createItemElement(transitionElement, "String", taskActors.get(0)[1]);
					createItemElement(transitionElement, "String", taskActors.get(0)[2]);
					}
				}else{
				for(int m=0;m<objs.length;m++){
					createItemElement(transitionElement, "String", objs[m]);
					//createItemElement(transitionElement, "String", objs[1]);
					//createItemElement(transitionElement, "String", objs[2]);
					//createItemElement(transitionElement, "String", objs[3]);
					//createItemElement(transitionElement, "String", objs[4]);
				}
				}
			 }
		}
		return document.asXML();
	}
	public String createItemsTaskActorsData(String status,
			String failReason, List taskActorsList) {
		// 创建文档对象
		Document document = DocumentHelper.createDocument();
		// 创建根节点
		Element rootElement = createRootElement(document);
		// 创建返回状态节点
		createStatusElement(rootElement, status);
		// 创建失败信息节点
		createFailReasonElement(rootElement, failReason);
		// 创建transitions节点
		Element transitionsElement = rootElement.addElement("taskactors");
		Element taskactorElement;
		if ("1".equals(status)) {
			if(!taskActorsList.isEmpty()){
				for(int i=0;i<taskActorsList.size();i++) {
					taskactorElement = transitionsElement.addElement("taskactor");
					String[] objs = (String[])taskActorsList.get(i);
					for(int m=0;m<objs.length;m++){
						createItemElement(taskactorElement, "String", objs[m]);
					}
					//createItemElement(taskactorElement, "String", objs[1]);
					//createItemElement(taskactorElement, "String", objs[2]);
	 			}
			}
		}
		return document.asXML();
	}
	
	
	public String createItemsTaskActorsData(String status,
			String failReason, List taskActorsList,String multFlag) {
		// 创建文档对象
		Document document = DocumentHelper.createDocument();
		// 创建根节点
		Element rootElement = createRootElement(document);
		// 创建返回状态节点
		createStatusElement(rootElement, status);
		// 创建失败信息节点
		createFailReasonElement(rootElement, failReason);
		// 创建transitions节点
		Element multSelElement=rootElement.addElement("multselect");
		createItemElement(multSelElement, "String", multFlag);
		
		Element transitionsElement = rootElement.addElement("taskactors");
		Element taskactorElement;
		if ("1".equals(status)) {
			if(!taskActorsList.isEmpty()){
				for(int i=0;i<taskActorsList.size();i++) {
					taskactorElement = transitionsElement.addElement("taskactor");
					String[] objs = (String[])taskActorsList.get(i);
					for(int m=0;m<objs.length;m++){
						createItemElement(taskactorElement, "String", objs[m]);
					}
					//createItemElement(taskactorElement, "String", objs[1]);
					//createItemElement(taskactorElement, "String", objs[2]);
	 			}
			}
		}
		return document.asXML();
	}
	
	/**
	 * 读取邮件详细信息
	 * 返回XML格式数据
	 * @author yanghwf
	 * @param status
	 * @param failReason
	 * @param items
	 * @param attachs
	 * @return
	 */
	public String createXmlMessageDetail(String status, String failReason,
			List<String[]> items, List<String[]> attachs) {
		// 创建文档对象
		Document document = DocumentHelper.createDocument();
		// 创建根节点
		Element rootElement = createRootElement(document);
		// 创建返回状态节点
		createStatusElement(rootElement, status);
		// 创建失败信息节点
		createFailReasonElement(rootElement, failReason);
		// 创建data节点
		Element dataElement = createDataElement(rootElement);
		if ("1".equals(status)) {
			Element rowElement = this.createElement(dataElement, "form");
			if (items != null && !items.isEmpty()) {
				for (String[] item : items) {
					createItemElement(rowElement, item[0], item[1]);
				}
			}
			rowElement = this.createElement(dataElement, "attachments");
			if (attachs != null && !attachs.isEmpty()) {
				
				//Element attach = this.createElement(rowElement, "attachment");
				Element attach = null;
				for(int i=0;i<attachs.size();i++) {
					String[] objs = attachs.get(i);
					if(i % 2 == 0) {
						attach = this.createElement(rowElement, "attachment");
					}				
					createItemElement(attach, objs[0], objs[1]);
				}
				
			}
			
		}
		return document.asXML();
	}
	
	/**
	 * 读取邮件详细信息
	 * 返回XML格式数据
	 * @author yanghwf
	 * @param status
	 * @param failReason
	 * @param items
	 * @param attachs
	 * len
	 * 从第几个参数之后需要重新建立一个attachment标签
	 * @return
	 */
	public String createXmlMessageDetail(String status, String failReason,
			List<String[]> items, List<String[]> attachs,int len) {
		// 创建文档对象
		Document document = DocumentHelper.createDocument();
		// 创建根节点
		Element rootElement = createRootElement(document);
		// 创建返回状态节点
		createStatusElement(rootElement, status);
		// 创建失败信息节点
		createFailReasonElement(rootElement, failReason);
		// 创建data节点
		Element dataElement = createDataElement(rootElement);
		if ("1".equals(status)) {
			Element rowElement = this.createElement(dataElement, "form");
			if (items != null && !items.isEmpty()) {
				for (String[] item : items) {
					createItemElement(rowElement, item[0], item[1]);
				}
			}
			rowElement = this.createElement(dataElement, "attachments");
			if (attachs != null && !attachs.isEmpty()) {
				
				//Element attach = this.createElement(rowElement, "attachment");
				Element attach = null;
				for(int i=0;i<attachs.size();i++) {
					String[] objs = attachs.get(i);
					if(i % len == 0) {
						attach = this.createElement(rowElement, "attachment");
					}				
					createItemElement(attach, objs[0], objs[1]);
				}
				
			}
			
		}
		return document.asXML();
	}

	/**
	@Description: TODO() 
	@author penghj    
	@date Mar 2, 2012 2:43:38 PM 
	@param formData
	@return
	 */
	public  String GenerateXmlFormData(FormData formData) {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		Status status = formData.getStatus();//操作是否成功的标记
		String code = status.getCode();//操作返回编码 1：成功；0：失败
		String failReason=status.getFailReason();
		Document document = DocumentHelper.createDocument();
		//创建根节点
		Element rootElement = createRootElement(document);
		//创建返回状态节点
		createStatusElement(rootElement, status.getCode());
		//创建失败信息节点
		createFailReasonElement(rootElement, failReason);
		//创建data节点
		Element dataElement = createDataElement(rootElement);
		
		try {
			if(code.equals("1")){
				//表单信息
				Element formElement = this.createElement(dataElement, "form");

				List<String[]> form=new ArrayList<String[]>();
				List<Item> formItemList=formData.getForm().getItems();
				for(Item item:formItemList){
					createItemElement(formElement, item.getType(), item.getKey(),item.getValue());
				}
				
				//有无正文
				Element hasContentElement = dataElement.addElement("hasContent");
				boolean hasContent = formData.getHasContent();
				if(hasContent){
					hasContentElement.addText("1");
				}else{
					hasContentElement.addText("0");
				}
				
				//附件信息
				Element attachmentsElement = this.createElement(dataElement, "attachments");

				List<String[]> attchments=new ArrayList<String[]>();
				List<Attachment> attchmentList=formData.getAttachments().getAtachments();
				for(Attachment attachment:attchmentList){
					Element attachmentElement = this.createElement(attachmentsElement, "attachment");
					for(Item attachmentItem:attachment.getItems()){
						createItemElement(attachmentElement,attachmentItem.getType(), attachmentItem.getValue());

						String[] propertys=new String[2];
						propertys[0]=attachmentItem.getType();
						propertys[1]=attachmentItem.getValue();
 					}
				}
				
				//办理记录
				Element suggestionsElement = this.createElement(dataElement, "suggestions");
				List<Object[]> suggestions = new ArrayList<Object[]>();
				List<Suggestion> suggestionList = formData.getSuggestions().getSuggestions();
				if (suggestionList != null && !suggestionList.isEmpty()) {
					for (Suggestion suggestion : suggestionList) {
						Element suggestionElement = this.createElement(suggestionsElement, "suggestion");
						for (Item suggestionItem : suggestion.getItems()) {
							createItemElement(suggestionElement,suggestionItem.getType(), suggestionItem.getValue());
						}
					}
				}
				//显示按钮
				Element opElement = this.createElement(dataElement, "opButton");
				if(formData.getOperateButton() != null){
				    List<Item> opList = formData.getOperateButton().getItems();
					if (opList != null && !opList.isEmpty()) {
						for (Item op : opList) {
							createItemElement(opElement,op.getType(), op.getValue());
						}
					}
				}
				//迁移线
				Element transElement = this.createElement(dataElement, "transition");
				List<Item> transList = formData.getTransition().getItems();
				if (transList != null && !transList.isEmpty()) {
					for (Item trans : transList) {
						createItemElement(transElement,trans.getType(), trans.getValue());
					}
				}
				//显示取回按钮
				Element qhElement = this.createElement(dataElement, "taskButton");
				if(formData.getTasktion() != null){
				    List<Item> qhList = formData.getTasktion().getItems();
					if (qhList != null && !qhList.isEmpty()) {
						for (Item qh : qhList) {
							createItemElement(qhElement,qh.getType(), qh.getValue());
						}
					}
				}
				
				return document.asXML();

 			}
		} catch (Exception ex) {
			ret = dom.createItemResponseData(code, "获取表单明细信息发生异常:"+JavaUtils.stackToString(ex), null, null);
		}
		return ret;
	}
	public String createXmlOrgInfo(String status, String failReason, List<String[]> orgs) {
		// 创建文档对象
		Document document = DocumentHelper.createDocument();
		// 创建根节点
		Element rootElement = createRootElement(document);
		// 创建返回状态节点
		createStatusElement(rootElement, status);
		// 创建失败信息节点
		createFailReasonElement(rootElement, failReason);
		// 创建data节点
		Element dataElement = createDataElement(rootElement);
		if ("1".equals(status)) {
			Element organizationsElement = this.createElement(dataElement, "organizations");
			if (orgs != null && !orgs.isEmpty()) {
				for (String[] org : orgs) {
					Element organizationElement = this.createElement(organizationsElement, "organization");
					for (int i = 0; i < org.length; i++) {
						createItemElement(organizationElement, "String", org[i]);
					} 

				}
			}
		}
		return document.asXML();
	}
	
	public String createXmlUsers(String status, String failReason, List<String[]> users) {
		// 创建文档对象
		Document document = DocumentHelper.createDocument();
		// 创建根节点
		Element rootElement = createRootElement(document);
		// 创建返回状态节点
		createStatusElement(rootElement, status);
		// 创建失败信息节点
		createFailReasonElement(rootElement, failReason);
		// 创建data节点
		Element dataElement = createDataElement(rootElement);
		if ("1".equals(status)) {
			Element organizationsElement = this.createElement(dataElement, "users");
			if (users != null && !users.isEmpty()) {
				for (String[] user : users) {
					Element organizationElement = this.createElement(organizationsElement, "user");
					for (int i = 0; i < user.length; i++) {
						createItemElement(organizationElement, "String", user[i]);
					} 

				}
			}
		}
		return document.asXML();
	}
	
	
	public String createXmlOrgInfo(String status, String failReason, String[] orgInfo) {
		// 创建文档对象
		Document document = DocumentHelper.createDocument();
		// 创建根节点
		Element rootElement = createRootElement(document);
		// 创建返回状态节点
		createStatusElement(rootElement, status);
		// 创建失败信息节点
		createFailReasonElement(rootElement, failReason);
		// 创建data节点
		Element dataElement = createDataElement(rootElement);
		if ("1".equals(status)) {		
					Element organizationElement = this.createElement(dataElement, "organization");
					for (int i = 0; i < orgInfo.length; i++) {
						createItemElement(organizationElement, "String", orgInfo[i]);
					} 
					

				
		}
		return document.asXML();
	}
	
	public String createXmlUserInfo(String status, String failReason, String[] userInfo) {
		// 创建文档对象
		Document document = DocumentHelper.createDocument();
		// 创建根节点
		Element rootElement = createRootElement(document);
		// 创建返回状态节点
		createStatusElement(rootElement, status);
		// 创建失败信息节点
		createFailReasonElement(rootElement, failReason);
		// 创建data节点
		Element dataElement = createDataElement(rootElement);
		if ("1".equals(status)) {
					Element userElement = this.createElement(dataElement, "user");
					for (int i = 0; i < userInfo.length; i++) {
						createItemElement(userElement, "String", userInfo[i]);
					} 
		}
		return document.asXML();
	}
	
	/**
	 * @param status
	 * @param failReason
	 * @param data
	 * @return
	 */
	public String createXmldata(String status, String failReason, String[] data) {
		// 创建文档对象
		Document document = DocumentHelper.createDocument();
		// 创建根节点
		Element rootElement = createRootElement(document);
		// 创建返回状态节点
		createStatusElement(rootElement, status);
		// 创建失败信息节点
		createFailReasonElement(rootElement, failReason);
		// 创建data节点
		Element dataElement = createDataElement(rootElement);
		if ("1".equals(status)) {
					for (int i = 0; i < data.length; i++) {
						createItemElement(dataElement, "String", data[i]);
					} 
		}
		return document.asXML();
	}
	
	
	public String createXmlMessage(String status, String failReason,String[] result) {
		// 创建文档对象
		Document document = DocumentHelper.createDocument();
		// 创建根节点
		Element rootElement = createRootElement(document);
		// 创建返回状态节点
		createStatusElement(rootElement, status);
		// 创建失败信息节点
		createFailReasonElement(rootElement, failReason);
		// 创建data节点
		Element dataElement = createDataElement(rootElement);
		if ("1".equals(status)) {
			 createItemElement(dataElement, result[0], result[1]);				
		}
		return document.asXML();
	}

	/**
	* @method createXmlUnreads
	* @author 申仪玲(shenyl)
	* @created 2012-3-30 下午05:17:30
	* @description 用户待办文件个数和未读邮件条数
	* @return String 返回XML格式数据
	*/
	public String createXmlUnreads(String status, String failReason,
			List<String[]> items) {
		// 创建文档对象
		Document document = DocumentHelper.createDocument();
		// 创建根节点
		Element rootElement = createRootElement(document);
		// 创建返回状态节点
		createStatusElement(rootElement, status);
		// 创建失败信息节点
		createFailReasonElement(rootElement, failReason);
		// 创建data节点
		Element dataElement = createDataElement(rootElement);
		if ("1".equals(status)) {
			if (items != null && !items.isEmpty()) {
				for (String[] item : items) {
					createItemElement(dataElement, item[0], item[1]);
				}
			}
			
		}
		return document.asXML();
	}
	
	
	
}
