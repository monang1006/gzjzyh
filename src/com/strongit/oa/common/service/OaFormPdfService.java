package com.strongit.oa.common.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.compass.core.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strongit.form.pdf.FormPDFService;
import com.strongit.form.services.FormService;

/**
 * 电子表单生成pdf接口类
 * 
 * @author 钟伟
 * @version 1.0
 * @date 2014-1-17
 */
@Service
public class OaFormPdfService
{
	
	@Autowired
	private FormService  formSrv;
	
	@Autowired
	private FormPDFService  pdfSrv;

	
	/**
	 * 根据电子表单的快照生成pdf文件
	 * 
	 * @param tableName 表单绑定的表名
	 * @param pdfColName 表单绑定表中，存放快照的字段名
	 * @param pkId 表单绑定表的主键名
	 * @param pkVal 表单绑定表的主键值
	 * @return PDF输出流
	 */
	public byte[] buildPdf(String tableName, String pdfColName, String pkId, String pkVal)
	{
		Assert.hasLength(tableName, "表名不能为空");
		Assert.hasLength(pdfColName, "快照字段名不能为空");
		Assert.hasLength(pkId, "主键名不能为空");
		Assert.hasLength(pkVal, "主键值不能为空");
		
		String wheresql= pkId + "='" + pkVal + "'";

		InputStream is = formSrv.getTemplateData(tableName, pdfColName, wheresql);
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		if(is!=null && !"".equals(is)){
		  pdfSrv.outputFormPDF(os, is);
		}
		try
		{
			os.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return os.toByteArray();
	}

}
