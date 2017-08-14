package com.strongit.oa.mymail.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strongit.oa.bo.ToaPersonalConfig;
import com.strongit.oa.bo.ToaSysDefaultmail;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.myinfo.MyInfoManager;
import com.strongit.oa.mymail.IMailService;
import com.strongit.oa.mymail.util.WriteMail;
import com.strongit.oa.systemmail.SystemMailManager;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

/**
 * 外部模块调用邮件接口
 * @author yuhz
 * @version v1.0
 */

@Service
public class MailService implements IMailService{
	
	/**
	 * 要考虑系统邮件和手动发送邮件所取的邮箱
	 */
	
	IUserService userService;
	
	MyInfoManager myInfoManager;
	
	SystemMailManager sysMailManager;

	@Autowired
	public void setSysMailManager(SystemMailManager sysMailManager) {
		this.sysMailManager = sysMailManager;
	}

	@Autowired
	public void setMyInfoManager(MyInfoManager myInfoManager) {
		this.myInfoManager = myInfoManager;
	}
	
	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public String autoSendMail(List<String> list, String subject, String content,String type) {
		// TODO Auto-generated method stub
		
		List<String> toList=new ArrayList<String>();			//邮件收件人列表
		
		String fromMail=null;									//发件人地址
		
		String sendServer=null;									//发送服务器
		
		String user=null;										//账户
		
		String password=null;									//密码
		
		String ssl=null;										//是否采用SSL验证
		
		String port=null;										//服务器端口
			
		//通过收件人ID获取收件人列表
		if(list!=null){
			for(int i=0;i<list.size();i++){
				String userId=list.get(i);
				ToaPersonalConfig config=myInfoManager.getConfigByUserid(userId);
				String address=null;
				if(config!=null){
					address=config.getDefaultMail();
				}
				if(address!=null&&!"".equals(address)){			//邮件地址已配置
					toList.add(address);
				}
			}
		}
		
		//根据邮件发送的类型不同找出发件人邮箱
		if("person".equals(type)){
			//发送个人邮件
			User nowUser=userService.getCurrentUser();
			ToaPersonalConfig myConfig=myInfoManager.getConfigByUserid(nowUser.getUserId());
			fromMail=myConfig.getDefaultMail();
			sendServer=myConfig.getDefaultMailSys();
			user=myConfig.getDefaultMailUser();
			password=myConfig.getDefaultMailPsw();
			ssl=myConfig.getDefaultMailSsl();
			port=myConfig.getDefaultMailPort();
		}else if("system".equals(type)){
			//发送系统邮件
			ToaSysDefaultmail sysConfig=sysMailManager.getDefaultMail();
			if(sysConfig==null){
				return "failed";
			}
			fromMail=sysConfig.getDefaultMail();
			sendServer=sysConfig.getDefaultMailSys();
			user=sysConfig.getDefaultMailUser();
			password=sysConfig.getDefaultMailPsw();
			ssl=sysConfig.getDefaultMailSsl();
			port=sysConfig.getDefaultMailPort();
		}
		
		//此模块还要根据具体的需求进行具体的判断来发送邮件
		
		if(fromMail!=null&&!"".equals(fromMail)){
			if(toList.size()!=0){						//邮件接收者不为空
				WriteMail writeMail=new WriteMail(sendServer,fromMail,user,user,password,toList,null,null,subject,content,ssl,port);
				try{
					HashMap map=writeMail.send();
			    	if("failed".equals(map.get("state"))){						
			    		//System.out.println(map.get("message"));
			    		return "failed";
			    	}else{
			    		//System.out.println("邮件发送成功");
			    		return "success";
			    	}
				}catch(Exception e){
					e.printStackTrace();
					return "failed";
				}
			}else{
				return null;
			}
		}else{
			return "failed";
		}

	}
	
	
	/**
	 * author:luosy
	 * description:判断邮件模块是否已启用
	 * modifyer:
	 * description:
	 * @return true : 启用；false ：关闭
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean isMailUseable()throws SystemException,ServiceException{  
		try{
		  return sysMailManager.isMailUseable();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
				new Object[] {"系统默认邮箱配置对象"});
		}
	}
}
