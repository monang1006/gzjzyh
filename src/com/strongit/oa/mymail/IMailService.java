package com.strongit.oa.mymail;

import java.util.List;

import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

public interface IMailService {
	
	/**
	 * 
	 * 其他模块调用接口
	 * @param list			接收人ID列表
	 * @param subject		邮件主题
	 * @param content		邮件内容
	 * @param type			类型（person代表手动，system系统）
	 * @return				返回调用情况信息
	 */
	public String autoSendMail(List<String>list,String subject,String content,String type);
	
	/**
	 * author:luosy
	 * description:判断邮件模块是否已启用
	 * modifyer:
	 * description:
	 * @return true : 启用；false ：关闭
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean isMailUseable()throws SystemException,ServiceException;
	
}
