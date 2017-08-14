package com.strongit.oa.common.service;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VoFormDataBean {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private List<File> lstDeleteFormAttachment;
	
	private String businessId;
	
	private InputStream isReturnFormData;
	
	private String instanceId;

	public VoFormDataBean() {
		
	}
	
	public VoFormDataBean(String businessId,InputStream isReturnFormData,List<File> lstDeleteFormAttachment) {
		this.businessId = businessId;
		this.isReturnFormData = isReturnFormData;
		this.lstDeleteFormAttachment = lstDeleteFormAttachment;
	}
	
	public void deleteFile() {
		//在事务外删除被已删除了的表单数据所关联的表单附件文件。
        if (lstDeleteFormAttachment != null && lstDeleteFormAttachment.size() > 0) {
            for (Iterator<File> iter = lstDeleteFormAttachment.iterator(); iter.hasNext(); ) {
                File formAttachmentFile = iter.next();
                if (formAttachmentFile.exists()) {
                    try {
                        formAttachmentFile.delete();
                        logger.info("删除表单附件文件：" + formAttachmentFile.getName());
                    } catch (Exception ex) {
                        logger.error("删除表单附件文件时产生异常，" + ex.getMessage(), ex);
                    }
                }
            }
        }
	}
	
	public void setLstDeleteFormAttachment(List<File> lstDeleteFormAttachment) {
		this.lstDeleteFormAttachment = lstDeleteFormAttachment;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public InputStream getIsReturnFormData() {
		return isReturnFormData;
	}

	public void setIsReturnFormData(InputStream isReturnFormData) {
		this.isReturnFormData = isReturnFormData;
	}

	public String getBusinessId() {
		return businessId;
	}

	public List<File> getLstDeleteFormAttachment() {
		return lstDeleteFormAttachment;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
}
