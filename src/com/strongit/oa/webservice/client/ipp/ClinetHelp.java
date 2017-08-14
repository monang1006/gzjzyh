package com.strongit.oa.webservice.client.ipp;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class ClinetHelp {

	public List xmlToList(String xmlStr) throws DocumentException{
		//if(1==1){
		//	return null;	
		//}else{
		List resList=new ArrayList();
		Document document=DocumentHelper.parseText(xmlStr);//通过传入的XML字符串创建DOCUMENT对象
		Element e=document.getRootElement();//获取XML文档的根元素对象
		List elementsObjects = e.elements();//获取根元素的下一级子元素对象集合
		for(int i=0;i<elementsObjects.size();i++){
			Element objects=(Element)elementsObjects.get(i);//获取一个名为OBJECTS的对象
			List elementsObject=objects.elements();//获取末级元素对象集合
			List childList=new ArrayList();
			for(int j=0;j<elementsObject.size();j++){
				Element x=(Element)elementsObject.get(j);
				String r=x.getText();
				childList.add(r);
			}
			resList.add(childList);
		}
		return resList;
		//}
	}

}
