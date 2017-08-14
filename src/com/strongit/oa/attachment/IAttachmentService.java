/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK:1.5.0_14; Struts:2.1.2; Spring:2.5.6; Hibernate:3.3.1.GA
 * Create Date: 2008-12-25
 * Autour: dengwenqiang
 * Version: V1.0
 * Description： 
 */
package com.strongit.oa.attachment;

import java.util.Date;
import java.util.List;

import com.strongit.oa.bo.ToaAttachment;

/**
 * 附件接口
 * @author dengwenqiang
 * @version 1.0
 */
public interface IAttachmentService {
	/**
	 * 保存附件
	 * @param attachName 附件名称
	 * @param attachCon 附件内容(二进制)
	 * @param attachTime 添加时间
	 * @param attachType 文件类型，即文件扩展名(如：zip、doc等)
	 * @param isPrivacy 是否是私人文件, "0"-公共文件;"1"-私人文件(邮件、内部消息等与个人相关的附件都属于私人文件)
	 * @param attachRemark 附注
	 * @param userId 用户ID
	 * @return java.lang.String 附件ID
	 */
	public String saveAttachment(String attachName, byte[] attachCon,
			Date attachTime, String attachType, String isPrivacy,
			String attachRemark, String userId);
	
	/**
	 * 获取附件
	 * @param attachId 附件ID
	 * @return ToaAttachment 附件对象
	 */
	public ToaAttachment getAttachmentById(String attachId);
	
	/**
	 * 删除附件
	 * @param attachId 附件ID 
	 */
	public void deleteAttachment(String attachId);
	
	
	/*
	 * 
	 * Description:删除附件记录
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 24, 2010 4:41:39 PM
	 */
	public void deleteAttachment(List<ToaAttachment> list);
}
