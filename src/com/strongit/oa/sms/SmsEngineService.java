/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：短信猫引擎接口实现类
 */
package com.strongit.oa.sms;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strongit.oa.bo.ToaBussinessModulePara;
import com.strongit.oa.bo.ToaMessageAccount;
import com.strongit.oa.bo.ToaPersonalInfo;
import com.strongit.oa.bo.ToaSms;
import com.strongit.oa.bo.ToaSmsCommConf;
import com.strongit.oa.message.MessageManager;
import com.strongit.oa.message.MessageStateMeanManager;
import com.strongit.oa.msgaccount.MsgAccountManager;
import com.strongit.oa.myinfo.MyInfoManager;
import com.strongit.oa.reply_message.MessageReplyManager;
import com.strongit.oa.sms.engine.CommTest;
import com.strongit.oa.sms.engine.SmsModelHandler;
import com.strongit.oa.sms.engine.SmsModelHandlerByCDMA;
import com.strongit.oa.sms.gsm3040.Pdu;
import com.strongit.oa.sms.gsm3040.PduParser;
import com.strongit.oa.smsplatform.SmsPlatformManager;
import com.strongit.oa.smsplatform.util.PropertiesUtil;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;



/**
 * 短信猫引擎接口实现类
 * @Create Date: 2009-2-13
 * @author luosy
 * @version 1.0
 */
@Service
public class SmsEngineService implements IsmsEngineService{
	
	private static final long serialVersionUID = -8834810411937309724L;

	private SmsConfManager smsConfManager;
	
	private SmsManager smsManager;
	
	@Autowired private SmsModelHandler gmsModemHandler;

	@Autowired private SmsModelHandlerByCDMA cdmaModemHandler;
	
	private MessageReplyManager msgRepManager;
	
	private CommTest commTest;
	
	private MyInfoManager myInfo;
	
	private SmsPlatformManager smsPlatformManager;
	
	private MsgAccountManager msgAccountManager;
	
	private ScheduledExecutorService exeService = Executors.newScheduledThreadPool(1);
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	public void setCommTest(CommTest commTest) {
		this.commTest = commTest;
	}
	
	@Autowired
	public void setSmsPlatformManager(SmsPlatformManager smsPlatformManager) {
		this.smsPlatformManager = smsPlatformManager;
	}

	@Autowired
	public void setSmsConfManager(SmsConfManager smsConfManager) {
		this.smsConfManager = smsConfManager;
	}
	
	@Autowired
	public void setMsgRepManager(MessageReplyManager msgRepManager) {
		this.msgRepManager = msgRepManager;
	}
	
	@Autowired
	public void setMsgAccountManager(MsgAccountManager msgAccountManager) {
		this.msgAccountManager = msgAccountManager;
	}

	@Autowired
	public void setSmsManager(SmsManager smsManager) {
		this.smsManager = smsManager;
	}
	@Autowired
	public void setMyInfo(MyInfoManager myInfo) {
		this.myInfo = myInfo;
	}
	
	/**
	 * author:luosy
	 * description: 扫描端口
	 * modifyer:
	 * description:
	 * @return
	 */
	public String CommTest() throws SystemException,ServiceException{
		try{
			ScheduledFuture future = exeService.schedule(new Callable<String>() {   
	            public String call() {
	            	String str = "";
	                try {   
	                	str = commTest.commTest();
	                } catch (Exception e) {   
	                	e.printStackTrace();
	                	LogPrintStackUtil.printInfo(logger, "端口扫描出错@commTest.commTest:"+e.getMessage());
	                }   
	                return str;
	            }
	        	},  0, TimeUnit.MILLISECONDS);
			
			return future.get().toString();
		}catch (InterruptedException e) {
			e.printStackTrace();
			LogPrintStackUtil.printInfo(logger, "端口扫描出错@InterruptedException:"+e.getMessage());
			throw new ServiceException(MessagesConst.operation_error,               
					new Object[] {"扫描进程中断 "});
		} catch (ExecutionException e) {
			e.printStackTrace();
			LogPrintStackUtil.printInfo(logger, "端口扫描出错@ExecutionException:"+e.getMessage());
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"短信猫引擎端口 "});
		}catch(ServiceException e){
			e.printStackTrace();
			LogPrintStackUtil.printInfo(logger, "端口扫描出错@ServiceException:"+e.getMessage());
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"短信猫引擎端口 "});
		}catch(Exception e){
			e.printStackTrace();
			LogPrintStackUtil.printInfo(logger, "端口扫描出错@Exception:"+e.getMessage());
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"短信猫引擎端口 "});
		}
	}
	
	/**
	 * author:luosy
	 * description: 发送单条短信
	 * modifyer:
	 * description:
	 * @param num 短信接受号码
	 * @param content	短信内容
	 * @return 发送状态
	 * @throws Exception 
	 */
	public String sendSingleSms(ToaSms sms) throws Exception,ServiceException{
		try{
			ToaSmsCommConf smsconf = smsConfManager.getSmsConf();
			if(smsconf!=null){
				String b = smsconf.getSmscomBps();
				String p = smsconf.getSmscomPort();
				String modem = smsconf.getSmscomtype();
				
				if(!"".equals(b)&&null!=b&&!"".equals(p)&&null!=p){
					final ToaSms s = sms;
					final String recvNum = sms.getSmsRecvnum();
					final String recvCon = sms.getSmsCon();
					final int baud = Integer.parseInt(b); //传输速率 bps 
					final String port = p; //端口
					final String type = modem;	//短信猫类型
					
					ScheduledFuture future = exeService.schedule(new Callable<String>() {
						public String call() {
							String str = "";
							try {   
								if(!"".equals(recvNum)&&null!=recvNum){
									if(ToaSmsCommConf.SMS_MODEM_CDMA.equals(type)){
										str = cdmaModemHandler.sendSingleSms(recvNum, recvCon, baud, port);
									}else{
										str = gmsModemHandler.sendSingleSms(recvNum, recvCon, baud, port);
									}
								}
								if(str.indexOf("OK")>0||str.indexOf("ok")>0){
									s.setSmsState(ToaSms.SMS_SEND);
									s.setSmsServerRet(ToaSms.SERVICE_RET_SUCCESS);
								}else{
									s.setSmsState(ToaSms.SMS_SEND);
									s.setSmsServerRet(ToaSms.SERVICE_RET_FAILURE);
									LogPrintStackUtil.printInfo(logger, "smssend_failed_短信发送失败:"+str);
								}
								smsManager.updateSms(s);
							} catch (Exception e) {   
								LogPrintStackUtil.printInfo(logger, "smssend_Exception_ 短信发送异常:"+e.getMessage());
								s.setSmsServerRet(ToaSms.SERVICE_RET_FAILURE);
								smsManager.updateSms(s);
//								e.printStackTrace();
								return "configError";
							}   
							return str;
						}
					},  0, TimeUnit.MILLISECONDS);
					
					return future.get().toString();
				}else{
					sms.setSmsServerRet(ToaSms.SERVICE_RET_FAILURE);
					smsManager.updateSms(sms);
					return "configError";
				}
			}else{
				LogPrintStackUtil.printInfo(logger, "no_ToaSmsCommConf__没有配置短信猫");
				sms.setSmsServerRet(ToaSms.SERVICE_RET_FAILURE);
				smsManager.updateSms(sms);
				return "configError";
			}
		}catch(IllegalArgumentException e){
			LogPrintStackUtil.printInfo(logger, "SmsEngineService_sendSingleSms_IllegalArgumentException:"+e.getMessage());
			throw new ServiceException(MessagesConst.operation_error,               
					new Object[] {"扫描进程中断 "});
		}catch (InterruptedException e) {
			LogPrintStackUtil.printInfo(logger, "SmsEngineService_sendSingleSms_InterruptedException:"+e.getMessage());
			throw new ServiceException(MessagesConst.operation_error,               
					new Object[] {"扫描进程中断 "});
		} catch (ExecutionException e) {
			LogPrintStackUtil.printInfo(logger, "SmsEngineService_sendSingleSms_ExecutionException:"+e.getMessage());
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"短信猫引擎端口 "});
		}catch(ServiceException e){
			LogPrintStackUtil.printInfo(logger, "SmsEngineService_sendSingleSms_ServiceException:"+e.getMessage());
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"短信猫引擎端口 "});
		}catch (Exception e) {   
			LogPrintStackUtil.printInfo(logger, "SmsEngineService_sendSingleSms_Exception:"+e.getMessage());
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"短信猫引擎 "});
		}   
	}
	
	
	/**
	 * author:luosy
	 * description: 读取短信
	 * modifyer:
	 * description:
	 * @throws Exception 
	 */
	public void readSIMsms() throws Exception,ServiceException{
		ToaSmsCommConf smsconf = smsConfManager.getSmsConf();
		PropertiesUtil pu = new PropertiesUtil();
		
		String moblie = pu.getProperty(PropertiesUtil.mobile);
		String[] moblieNum = moblie.split(",");
		String unicom = pu.getProperty(PropertiesUtil.unicom);
		String[] unicomNum = unicom.split(",");
		String telecom = pu.getProperty(PropertiesUtil.telecom);
		String[] telecomNum = telecom.split(",");
		
		if(smsconf!=null){
			String b = smsconf.getSmscomBps();
			String p = smsconf.getSmscomPort();
			String simNum = smsconf.getSmscomSimnum();
			String modem = smsconf.getSmscomtype();
			
			if(!"".equals(b)&&null!=b&&!"".equals(p)&&null!=p){
				final int baud = Integer.parseInt(b); //传输速率 bps 
				final String port = p; //端口
				final String simnum = simNum;
				final String moblieSN = moblieNum[0];	//移动服务号
				final String unicomSN = unicomNum[0];	//连通服务号
				final String telecomSN = telecomNum[0];	//电信服务号
				final String type = modem;				//短信猫类型
    			
				ScheduledFuture future = exeService.schedule(new Callable<String>() {   
					public String call() {
						String str = "";
						try {   
							String resp="";
							if(ToaSmsCommConf.SMS_MODEM_CDMA.equals(type)){
								List<String[]> relst = cdmaModemHandler.readSmsInSim(baud,port);
								if(null!=relst&&relst.size()>0){
									for(int k=0;k<relst.size();k++){
										String content = relst.get(k)[1];	//短信内容
										String phoneNum = relst.get(k)[0].replaceAll("\"", "");	//短信号码
										System.out.println("收到短信："+phoneNum+"-->"+content);
										
										if(phoneNum.equals(moblieSN)||phoneNum.equals(unicomSN)||phoneNum.equals(telecomSN)){
											ToaMessageAccount accMsg = new ToaMessageAccount();
											accMsg.setReplyTime(new Date());
											accMsg.setReplyContent(content);
											accMsg.setReplyNumber(phoneNum);
											accMsg.setSenderNumber(simnum);
											msgAccountManager.saveAccountMsg(accMsg);
										}
										
										if(content.length()<5){
											continue;
										}
										
										String moduleCode = content.substring(0,1);//模块ID
										ToaBussinessModulePara bmp = smsPlatformManager.getObjByCode(moduleCode);
										if(null!=bmp){			//如果找到对应模块配置
											String increaseLength =bmp.getIncreaseLength();//自增长位数
											String increaseCode = content.substring(1,Integer.parseInt(increaseLength)+1);//自增长编码
											String repCon = content.substring(Integer.parseInt(increaseLength)+1, content.length());
											Date repTime = new Date();
											String replyUser = phoneNum;
											ToaPersonalInfo perInfo = myInfo.getInfoByTelephoneNum(phoneNum);
											if(null!=perInfo){	//如果根据手机号码在系统中找的到用户
												replyUser = perInfo.getPrsnName(); //回复的用户名
											}
											
											boolean savedBefor = msgRepManager.saveReplySms(phoneNum,repTime,moduleCode,increaseCode,repCon,replyUser);
											
//					    				boolean saved =msgRepManager.isSavedsms(phoneNum,moduleCode,increaseCode);
//				    					感谢回复
											if(null!=bmp.getThankOrNot()&&!"".equals(bmp.getThankOrNot())){
												if("1".equals(bmp.getThankOrNot())){
													String thankCon = "";
													if(!savedBefor){
														thankCon = bmp.getThankContent().toString();
													}else{
														thankCon = "您已经重复回复短信或回复的短信格式不对，回复的短信只应包含数字";
													}
													ToaSms thanksSms = new ToaSms();
													thanksSms.setSmsCon(thankCon);
													thanksSms.setSmsRecvnum(phoneNum);
													//sendSingleSms(thanksSms);
													thanksSms.setSmsState(ToaSms.SMS_UNSEND);
													thanksSms.setSmsServerRet(ToaSms.SERVICE_RET_UNSEND);
													thanksSms.setSmsSendDelay(new Date());
													//smsManager.justSaveSms(thanksSms );
													
													final Timer timer = new Timer();
													final ToaSms s1 = thanksSms;
													timer.schedule(new TimerTask() {
														public void run() {
															try {
																sendSingleSms(s1);
															} catch (ServiceException e) {
																LogPrintStackUtil.printInfo(logger, "短信发送 感谢回复 异常:"+e.getMessage());
															} catch (Exception e) {
																LogPrintStackUtil.printInfo(logger, "短信发送 感谢回复 异常:"+e.getMessage());
															}
														}
													},0);
												}
											}
										}
									}
					    		}
							}else{
								resp = gmsModemHandler.readSmsInSim(baud,port);
								
								if(!"".equals(resp)&&null!=resp){
									//解析pdu
									PduParser parser = new PduParser();
									
									String str2 = resp.replaceAll("\\+CMGL:", ";");
									str2 = str2.replaceAll("OK", "");
									String[] s = str2.split(";");
									for(int i=1;i<s.length;i++){
										String[] ss = s[i].split(",");
										String sss[] = ss[3].split("\\r");
										
										String pduStr = sss[1].substring(1, sss[1].length());
										Pdu pdu = parser.parsePdu(pduStr);
										if(!"".equals(pdu.getDecodedText())&&null!=pdu.getDecodedText()){
											String content = pdu.getDecodedText().trim();
											String phoneNum = pdu.getAddress();
											
											if(phoneNum.equals(moblieSN)|phoneNum.equals(unicomSN)|phoneNum.equals(telecomSN)){
												ToaMessageAccount accMsg = new ToaMessageAccount();
												accMsg.setReplyTime(new Date());
												accMsg.setReplyContent(content);
												accMsg.setReplyNumber(phoneNum);
												accMsg.setSenderNumber(simnum);
												msgAccountManager.saveAccountMsg(accMsg);
											}
											
											if(phoneNum.length()==13&&(phoneNum.indexOf("86")==0)){
												phoneNum = phoneNum.substring(2,phoneNum.length());
											}
											
											String moduleCode = content.substring(0,1);//模块ID
											ToaBussinessModulePara bmp = smsPlatformManager.getObjByCode(moduleCode);
											if(null!=bmp){			//如果找到对应模块配置
												String increaseLength =bmp.getIncreaseLength();//自增长位数
												String increaseCode = content.substring(1,Integer.parseInt(increaseLength)+1);//自增长编码
												String repCon = content.substring(Integer.parseInt(increaseLength)+1, content.length());
												Date repTime = pdu.getSendDate();
												String replyUser = phoneNum;
												ToaPersonalInfo perInfo = myInfo.getInfoByTelephoneNum(phoneNum);
												if(null!=perInfo){	//如果根据手机号码在系统中找的到用户
													replyUser = perInfo.getPrsnName(); //回复的用户名
												}
												
												boolean savedBefor = msgRepManager.saveReplySms(phoneNum,repTime,moduleCode,increaseCode,repCon,replyUser);
												
//						    				boolean saved =msgRepManager.isSavedsms(phoneNum,moduleCode,increaseCode);
//					    					感谢回复
												if(null!=bmp.getThankOrNot()&&!"".equals(bmp.getThankOrNot())){
													if("1".equals(bmp.getThankOrNot())){
														String thankCon = "";
														if(!savedBefor){
															thankCon = bmp.getThankContent().toString();
														}else{
															thankCon = "您已经重复回复短信或回复的短信格式不对，回复的短信只应包含数字";
														}
														ToaSms thanksSms = new ToaSms();
														thanksSms.setSmsCon(thankCon);
														thanksSms.setSmsRecvnum(pdu.getAddress());
														//sendSingleSms(thanksSms);
														thanksSms.setSmsState(ToaSms.SMS_UNSEND);
														thanksSms.setSmsServerRet(ToaSms.SERVICE_RET_UNSEND);
														thanksSms.setSmsSendDelay(new Date());
														//smsManager.justSaveSms(thanksSms );
														
														final Timer timer = new Timer();
														final ToaSms s1 = thanksSms;
														timer.schedule(new TimerTask() {
															public void run() {
																try {
																	sendSingleSms(s1);
																} catch (ServiceException e) {
																	LogPrintStackUtil.printInfo(logger, "短信发送 感谢回复 异常:"+e.getMessage());
																} catch (Exception e) {
																	LogPrintStackUtil.printInfo(logger, "短信发送 感谢回复 异常:"+e.getMessage());
																}
															}
														},0);
													}
												}
												
											}
										}
									}
								}
							}
						} catch (Exception e) {   
							LogPrintStackUtil.printInfo(logger, "短信读取异常:"+e.getMessage());
						}   
						return str;
					}
				},  0, TimeUnit.MILLISECONDS);
			}
			
			
		}else{
			LogPrintStackUtil.printInfo(logger, "没有配置短信猫");
		}
		
	}

	
	/**
	 * author:luosy
	 * description: 删除短信
	 * modifyer:
	 * description:
	 * @return
	 */
	public void delSIMsms() throws Exception, SystemException,ServiceException{
		try{
			ToaSmsCommConf smsconf = smsConfManager.getSmsConf();
			if(smsconf!=null){
				String b = smsconf.getSmscomBps();
				String p = smsconf.getSmscomPort();
				String modem = smsconf.getSmscomtype();
				
				if(!"".equals(b)&&null!=b&&!"".equals(p)&&null!=p){
					final int baud = Integer.parseInt(b); //传输速率 bps 
					final String port = p; //端口
					final String type = modem;	//短信猫类型
					
					ScheduledFuture future = exeService.schedule(new Callable<String>() {   
						public String call() {
							String str = "";
							try {   
								if(ToaSmsCommConf.SMS_MODEM_CDMA.equals(type)){
									str = cdmaModemHandler.delSmsInSim(baud, port);
								}else{
									str = gmsModemHandler.delSmsInSim(baud, port);
								}
							} catch (Exception e) {   
								LogPrintStackUtil.printInfo(logger, "短信删除 异常:"+e.getMessage());
							}   
							return str;
						}
					},  0, TimeUnit.MILLISECONDS);
				}
			}else{
				LogPrintStackUtil.printInfo(logger, "没有配置短信猫");
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除 短信猫 短信"});
		}catch(Exception e){
			LogPrintStackUtil.printInfo(logger, "短信删除 异常:"+e.getMessage());
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除 短信猫 短信"});
		}
	}



	

	/**
	 * author:luosy
	 * description: 发送单条短信 根据短信猫配置的手机号码
	 * modifyer:
	 * description:
	 * @param smsNum 接受号码
	 * @param smsText 短信内容
	 * @param simNum	短信猫配置的手机号码
	 * @return 发送状态
	 * @throws Exception 
	 */
	public String sendSingleSmsBySimNum(String smsNum, String smsText,String simNum) throws Exception,ServiceException{
		try{
			ToaSmsCommConf smsconf = smsConfManager.getSmsConfBySimNum(simNum);
			if(smsconf!=null){
				String b = smsconf.getSmscomBps();
				String p = smsconf.getSmscomPort();
				String modem = smsconf.getSmscomtype();
				
				if(!"".equals(b)&&null!=b&&!"".equals(p)&&null!=p){
					final String recvNum = smsNum;
					final String recvCon = smsText;
					final int baud = Integer.parseInt(b); //传输速率 bps 
					final String port = p; //端口
					final String type = modem;	//短信猫类型
					
					ScheduledFuture future = exeService.schedule(new Callable<String>() {
						public String call() {
							String str = "";
							try {   
								if(!"".equals(recvNum)&&null!=recvNum){
									if(ToaSmsCommConf.SMS_MODEM_CDMA.equals(type)){
										str = cdmaModemHandler.sendSingleSms(recvNum, recvCon, baud, port);
									}else{
										str = gmsModemHandler.sendSingleSms(recvNum, recvCon, baud, port);
									}
								}
								if(str.indexOf("OK")>0|str.indexOf("ok")>0){
									LogPrintStackUtil.printInfo(logger, "余额查询短信发送成功:"+str);
								}else{
									LogPrintStackUtil.printInfo(logger, "短信发送失败:"+str);
								}
							} catch (Exception e) {   
								LogPrintStackUtil.printInfo(logger, "短信发送异常:"+e.getMessage());
								e.printStackTrace();
								return "configError";
							}   
							return str;
						}
					},  0, TimeUnit.MILLISECONDS);
					
					return future.get().toString();
				}else{
					LogPrintStackUtil.printInfo(logger, "短信猫配置数据（端口或比率为空）错误");
					return "configError";
				}
			}else{
				LogPrintStackUtil.printInfo(logger, "无法根据号码找到对应的短信猫");
				return "configError";
			}
		}catch(IllegalArgumentException e){
			LogPrintStackUtil.printInfo(logger, "短信发送异常:"+e.getMessage());
			throw new ServiceException(MessagesConst.operation_error,               
					new Object[] {"扫描进程中断 "});
		}catch (InterruptedException e) {
			throw new ServiceException(MessagesConst.operation_error,               
					new Object[] {"扫描进程中断 "});
		} catch (ExecutionException e) {
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"短信猫引擎端口 "});
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"短信猫引擎端口 "});
		}catch (Exception e) {   
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"短信猫引擎 "});
		}   
	}

	

	
}