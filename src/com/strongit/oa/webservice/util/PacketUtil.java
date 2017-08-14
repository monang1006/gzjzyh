package com.strongit.oa.webservice.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.strongit.di.exception.SystemException;
import com.strongit.di.packet.Packet;
import com.strongit.di.util.XMLParser;

/**
 * 生成财政需要的XML文件字符
 * @author 邓志城
 * @company      Strongit Ltd. (C) copyright
 * @date 2009-7-14 下午02:04:25
 * @version      2.0.2.3
 * @classpath com.strongit.oa.webservice.util.PacketUtil
 * @comment
 */
public class PacketUtil {

	/**
	 * 根据给定的字段生成XML字符
	 * @author:邓志城
	 * @date:2009-7-14 下午02:04:53
	 * @param docName 必录项，指标发文文件号
	 * @param docType 文号性质：必录项，0：上级文号/1：本级文号/2：公共文号
	 * @param docContent 指标文件内容：非必录项。（支持office文档）
	 * @param docSignatory 签发人：非必录项。
	 * @param docSigntime 签发时间：必录项。
	 * @param docRemark 备注：非必录项。
	 * @param branchId 业务处室编码：必录项
	 * @param attaName 附件名称
	 * @param attaContent 附件内容
	 * @param createIp 客户端IP地址
	 * @return XML字符
	 * @throws SystemException
	 */
	public static String getPacket(String docName,
								   String docType,
								   String docContent,
								   String docSignatory,
								   String docSigntime,
								   String docRemark,
								   String branchId,
								   String attaName,
								   String attaContent,
								   String createIp)throws SystemException{
		//创建包对象
		Packet packet = new Packet();
		//定义返回值
		String ret = "";
		//格式化时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//获取当前时间
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		String strNow = sdf.format(now);
		//设置包头【版本号、客户端IP、创建时间】
		packet.setHead("VERSION", "1.0.0");
		packet.setHead("CREATEIP", createIp);
		packet.setHead("CREATETIME", strNow);
		
		//存储数据【每个对象都是MAP键值对】
		List<Map<String, String>> rowLst = new ArrayList<Map<String, String>>();
		//存储字段值【fieldName:fieldValue】
		Map<String, String> mapRow = new HashMap<String, String>();
		//存储字段列表
		List<Map<String, String>> metaLst = new ArrayList<Map<String, String>>();
		//存储每个字段
		Map<String, String> mapDocName = new HashMap<String, String>();
		Map<String, String> mapDocType = new HashMap<String, String>();
		Map<String, String> mapDocContent = new HashMap<String, String>();
		Map<String, String> mapDocSignatory = new HashMap<String, String>();
		Map<String, String> mapDocSigntime = new HashMap<String, String>();
		Map<String, String> mapDocRemark = new HashMap<String, String>();
		Map<String, String> mapDocBranchId = new HashMap<String, String>();
		Map<String, String> mapDocAttaName = new HashMap<String, String>();
		Map<String, String> mapDocAttaContent = new HashMap<String, String>();
		
		mapDocName.put("TYPE", "STRING");
		mapDocName.put("NAME", "docName");
		mapDocName.put("LENGTH", "60");
		metaLst.add(mapDocName);
		
		mapDocType.put("TYPE", "STRING");
		mapDocType.put("NAME", "docType");
		mapDocType.put("LENGTH", "1");
		metaLst.add(mapDocType);
		
		mapDocContent.put("TYPE", "BLOB");
		mapDocContent.put("NAME", "docContent");
		metaLst.add(mapDocContent);
		
		mapDocSignatory.put("TYPE", "STRING");
		mapDocSignatory.put("NAME", "docSignatory");
		mapDocSignatory.put("LENGTH", "20");
		metaLst.add(mapDocSignatory);
		
		mapDocSigntime.put("TYPE", "DATE");
		mapDocSigntime.put("NAME", "docSigntime");
		metaLst.add(mapDocSigntime);
		
		mapDocRemark.put("TYPE", "STRING");
		mapDocRemark.put("NAME","docRemark");
		mapDocRemark.put("LENGTH", "100");
		metaLst.add(mapDocRemark);
		
		mapDocBranchId.put("TYPE", "STRING");
		mapDocBranchId.put("NAME","branchId");
		mapDocBranchId.put("LENGTH", "18");
		metaLst.add(mapDocBranchId);
		
		mapDocAttaName.put("TYPE", "STRING");
		mapDocAttaName.put("NAME","attaName");
		mapDocAttaName.put("LENGTH", "254");
		metaLst.add(mapDocAttaName);
		
		mapDocAttaContent.put("TYPE", "BLOB");
		mapDocAttaContent.put("NAME","attaContent");
		metaLst.add(mapDocAttaContent);
		
		mapRow.put("docName", docName);
		mapRow.put("docType", docType);
		mapRow.put("docContent", docContent);
		mapRow.put("docSignatory", docSignatory);
		mapRow.put("docSigntime", docSigntime);
		mapRow.put("docRemark", docRemark);
		//这里写死了业务处室编码
		mapRow.put("branchId", branchId);//202001
		mapRow.put("attaName", attaName);
		mapRow.put("attaContent ", attaContent );
		rowLst.add(mapRow);
		
		//将字段值列表和字段描述列表放入包体
		packet.setRS("QuotasFile", rowLst);
		packet.setRSMeta("QuotasFile", metaLst);
		
		//将数据包生成XML字符返回
		ret = XMLParser.generate(packet);
		//XMLParser.generateToFile(packet, "c:/packet.xml");
		return ret;
	}

	public static void p(String str){
		System.out.println(str);
	}
	
	public static void main(String[] args)throws Exception{
		p(getPacket("指标文号1", "文号性质1", "指标文件内容1 Base64String", "签发人1" , "签发时间1", "备注1" , "业务处室编码1", "附件名称1", "附件内容1 Base64String", "192.168.2.168"));
	}
}
