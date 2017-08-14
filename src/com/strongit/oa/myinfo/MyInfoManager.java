/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-02-09
 * Autour: pengxq
 * Version: V1.0
 * Description：个人信息业务实现类
*/

package com.strongit.oa.myinfo;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.or.RendererMap;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaPersonalConfig;
import com.strongit.oa.bo.ToaPersonalInfo;
import com.strongit.oa.bo.ToaPrsnfldrFile;
import com.strongit.oa.bo.ToaSyssingleload;
import com.strongit.oa.mymail.util.StringUtil;
import com.strongit.oa.mymail.util.WriteMail;
import com.strongit.oa.util.MessagesConst;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.oa.common.user.IUserService;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * @author pengxq
 * @version 1.0
 */
@Service
@Transactional
public class MyInfoManager 
{
   
   private GenericDAOHibernate<ToaPersonalInfo, java.lang.String> myInfoDao;
   private GenericDAOHibernate<ToaSyssingleload, java.lang.String> sysTemDao;
   private InfoConfigManager configManager;
   Logger logger = LoggerFactory.getLogger(this.getClass());
   @Autowired
   IUserService userService;
	
   /**
    * @roseuid 494857FE02EE
    */
   public MyInfoManager() 
   {
	  
   }
   
   /**
    * @param sessionFactory
    * @roseuid 494854950242
    */
   @Autowired
   public void setSessionFactory(SessionFactory sessionFactory) 
   {
	   myInfoDao=new GenericDAOHibernate<ToaPersonalInfo, java.lang.String>(sessionFactory,ToaPersonalInfo.class);
	   sysTemDao=new GenericDAOHibernate<ToaSyssingleload, java.lang.String>(sessionFactory,ToaSyssingleload.class);
   
   }
  
   

   
   /**
    * 得到用户的签名列表
    * @param userIds
    * @return
    */
   public Map<String,byte[]> getUserSign(List<String> userIds) {
	   if(userIds != null && !userIds.isEmpty()) {
		   Map<String, byte[]> map = new HashMap<String, byte[]>();
		   StringBuilder ids = new StringBuilder();
		   for(String id : userIds) {
			   ids.append("'").append(id).append("'").append(",");
		   }
		   ids.deleteCharAt(ids.length() - 1);
		   String hql = "select t.userId ,t.sign from ToaPersonalInfo t where t.userId in("+ids+")";
		   List<Object[]> list = myInfoDao.find(hql);
		   if(list != null && !list.isEmpty()) {
			   for(Object[] objs : list) {
				   map.put(objs[0].toString(), (byte[])objs[1]);
			   }
		   }
		   return map;
	   }
	   return null;
   }
   
   
   
   
   /**
    * @author:yuhz
    * @time:2009-3-30
    * @desc:根据用户手机号码获取用户对象
    */
   
   public ToaPersonalInfo getInfoByTelephoneNum(String num) throws SystemException,ServiceException{
	   ToaPersonalInfo personInfo=null;
	   try{
		   if(num.length()==13){
			   num = num.substring(2,num.length());
		   }else if(num.length()==14){
			   num = num.substring(3,num.length());
		   }else if(num.length()==15){
			   num = num.substring(4,num.length());
		   }
		   String sql="from ToaPersonalInfo as t where t.prsnMobile1 =?";
		   List<ToaPersonalInfo> list=myInfoDao.find(sql,num);
		   if(list.size()>0){
			   return list.get(0);
		   }
	   }catch(ServiceException e){
		   e.printStackTrace();
		   throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"个人信息对象"});
	   }
	   return personInfo;
   }
   
   /**
    * 
     * @author：pengxq
     * @time：2009-2-10上午09:01:26
     * @desc: 根据userid获取用户个人信息
     * @param String userid 用户id
     * @return 获取个人信息
    */
   public ToaPersonalInfo getInfoByUserid(String userid)throws SystemException,ServiceException{
	   ToaPersonalInfo personInfo=null;
	   try{
		   String sql="from ToaPersonalInfo t where t.userId = ?";
		   List<ToaPersonalInfo> list=myInfoDao.find(sql, userid);
		   if(list.size()>0){
			   personInfo=list.get(0);
		   }
	   }catch(ServiceException e){
		   throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"个人信息对象"});
	   }
	   return personInfo;
   }
   
   
   
   /**
    * 
     * @author：pengxq
     * @time：2009-2-10上午09:01:26
     * @desc: 根据USER_ID获取第三方系统信息
     * @param String userid 用户id
     * @return 获取个人信息
    */
   public List<ToaSyssingleload> getInfoByUSER_ID(String USER_ID)throws SystemException,ServiceException{
	   ToaSyssingleload personInfo=null;
	   List<ToaSyssingleload> list= new ArrayList<ToaSyssingleload>();
	   try{
		   String sql="from ToaSyssingleload t where t.USER_ID = ?";
		   list=myInfoDao.find(sql, USER_ID);
		  
	   }catch(ServiceException e){
		   throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"当前用户信息对象"});
	   }
	   return list;
   }
   
   
   /**
    * 
     * @author：GD
     * @time：2014-3-25上午09:01:26
     * @desc: 根据USER_ID获取个人信息中的签名图片
     * @param String userid 用户id
     * @return 置空个人信息里的签名图片
    */
public void getInfoByUSERID1(String USER_ID)throws SystemException,ServiceException{
	   try{
		 
		   String sql="update T_OA_PERSONAL_INFO  set SIGN=null where USER_ID='"+USER_ID+"'";
		   myInfoDao.executeJdbcUpdate(sql);
	   }catch(ServiceException e){
		   throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"当前用户信息对象"});
	   }
   }
   
   
   /**
    * 保存系统信息
    * 
    * 
    */
   @SuppressWarnings("unchecked")
   public String saveSystem(ToaSyssingleload system) throws ServiceException {
	   sysTemDao.save(system);
	   return "ok";
   }
   
   
   /**
	 * author:dengzc
	 * description:删除系统信息
	 * modifyer:
	 * description:
	 * @param file
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteSys(String SYS_ID) throws SystemException,ServiceException{
		try {
			//SYS_ID attachSYS_ID = this.getInfoByUSER_ID(SYS_ID);
			if( SYS_ID != null && !"".equals(SYS_ID)){
				//attachSYS_ID.delete();
				ToaSyssingleload sys = sysTemDao.get(SYS_ID);
				if (sys != null) {
					sysTemDao.delete(sys);
					logger.info("删除文件" + sys);
				}else{
					logger.error("文件 不存在。");
				}
			}else{
				logger.error("文件不存在。");
			}
			
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"文件"});
		}
	}
   

   /**
    * 获取个人信息表信息,存储在HASHMAP中
    * @return
    * @throws ServiceException
    */
   @SuppressWarnings("unchecked")
   public Map getPersonalInfo() throws ServiceException {
	   Map userInfo = new HashMap();
	   try{
		   String sql="from ToaPersonalInfo t ";
		   List<ToaPersonalInfo> list = myInfoDao.find(sql);
		   if(list!=null && list.size()>0){
			   for(ToaPersonalInfo person : list){
				   userInfo.put(person.getUserId(), person);
			   }
		   }
	   }catch(ServiceException e){
		   throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"个人信息对象"});
	   }
	   return userInfo;
   }
   
   
   
   
   
   
   
   /**
    * 
     * @author：pengxq
     * @time：2009-2-10上午09:13:46
     * @desc: 保存个人信息
     * @param ToaPersonalInfo obj 个人信息对象
     * @return void
    */
   public String savePrsnInfo(ToaPersonalInfo obj)throws SystemException,ServiceException{
	   String message="";
	   try{
		   if (obj.getPrsnId() == null) {
			   myInfoDao.save(obj);
		   } else {
			   ToaPersonalInfo entry = myInfoDao.get(obj.getPrsnId());
			   if(entry.getSign() != null) {
				   obj.setSign(entry.getSign());
			   }
			   myInfoDao.evict(entry);
			   myInfoDao.save(obj);
			   TUumsBaseUser uumsUser = userService.getBaseUserByUserId(obj.getUserId());
			   boolean isChanged = false;
			   if(obj.getPrsnName() != null && !uumsUser.getUserName().equals(obj.getPrsnName())) {
				   isChanged = true;
			   }
			   
			   uumsUser.setUserName(obj.getPrsnName());
			   /* mobify by dengzc 2010年6月25日9:45:26
			   String mobile = obj.getPrsnMobile1();   by  tj bug  0000050330*/
			   String mobile = "";
			   if(obj.getPrsnMobile1()!=null&&!"".equals(obj.getPrsnMobile1())){
				   mobile = obj.getPrsnMobile1(); 
			   }else{
				   mobile = obj.getPrsnMobile2();
			   }
			   String mail = obj.getPrsnMail1();
			   String tel = obj.getPrsnTel1();
			   if(mobile != null && !mobile.equals(uumsUser.getRest2())) {
				   isChanged = true;
			   }
			   if(mail != null && !mail.equals(uumsUser.getUserEmail())) {
				   isChanged = true;
			   }
			   if(tel != null && !tel.equals(uumsUser.getUserTel())) {
				   isChanged = true;
			   }
			   if(mobile == null || "".equals(mobile)){
				   mobile = obj.getPrsnMail2();
			   }
			   if(mail == null || "".equals(mail)){
				   mail = obj.getPrsnMail2();
			   }
			   if(isChanged) {
				   uumsUser.setRest2(mobile);
				   uumsUser.setUserEmail(mail);
				   uumsUser.setUserTel(tel);
				   myInfoDao.getSession().saveOrUpdate(uumsUser);
			   }
			   // end
		   }
		   
	   }catch(ServiceException e){
		   message="个人信息保存失败!";
		   throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"个人信息对象"});
	   }
	   return message;
   }

   /**
    * 保存或更新个人信息对象.
    * @author:邓志城
    * @date:2010-6-25 上午09:34:33
    * @param obj	个人信息对象
    * @return	操作结果
    * @throws SystemException
    * @throws ServiceException
    */
   public boolean saveObj(ToaPersonalInfo obj) throws SystemException,ServiceException{
	   try{
		   myInfoDao.save(obj);
		   return true;
	   }catch(Exception e){
		   return false;
	   }
   }
   
   /**
    * 
     * @author：pengxq
     * @time：2009-2-11上午09:16:32
     * @desc: 根据用户id获取该用户的个人配置对象
     * @param String userid 用户id
     * @return ToaPersonalConfig 个人配置对象
    */
   public ToaPersonalConfig getConfigByUserid(String userid)throws SystemException,ServiceException{
	   ToaPersonalConfig config=null;
	   try{
		   List<ToaPersonalConfig> list=null;
		   ToaPersonalInfo personInfo=this.getInfoByUserid(userid);
		   if(personInfo!=null){
			   list=configManager.getConfigByInfoid(personInfo.getPrsnId());
			   if(list.size()>0){
				   config=list.get(0); 
			   }
		   }
	   }catch(ServiceException e){
		   throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"个人配置对象"});
	   }
	   return config;
   }
   
   /**
    * 
     * @author：pengxq
     * @time：2009-2-12下午03:51:22
     * @desc: 个人邮箱发送邮件
     * @param String receivers 收件人员
     * @param ToaSysDefaultmail obj 个人邮箱配置信息 
     * @return String 提示信息
    */
   public String sendMail(String receivers,ToaPersonalConfig obj)throws SystemException,ServiceException{
	   String msg="发送失败！！";
	   List<String> toList=null;
	   try{
		   if(obj!=null){
	  			String sendServer=obj.getDefaultMailSys();	//获得smtp服务器
	  			String fromEmail=obj.getDefaultMail();
	  			String port=obj.getDefaultMailPort();			//获得smtp服务器端口
	  			String ssl=obj.getDefaultMailSsl();			//是否需要ssl加密
	  			String username=obj.getDefaultMailUser();		//用户名称
	  			String password=obj.getDefaultMailPsw();	
	  			toList=StringUtil.stringToList(receivers, ",");	//收件人员列表	
	  			WriteMail writeMail=new WriteMail(sendServer,fromEmail,username,username,password,toList,null,null,"系统默认邮箱测试","测试成功",ssl,port);
				HashMap map=writeMail.send();
	  			msg ="发送成功！！";
	  		}
	   }catch(ServiceException e){
		   throw new ServiceException(MessagesConst.send_error,               
					new Object[] {"个人邮件"});
	   }catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	   return msg;
   }
   	
   	@Autowired
	public void setConfigManager(InfoConfigManager configManager) {
		this.configManager = configManager;
	}
}
