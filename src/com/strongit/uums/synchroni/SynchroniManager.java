package com.strongit.uums.synchroni;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.di.exception.BaseException;
import com.strongit.di.packet.Packet;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.webservice.client.finance.synchronizes.SynchClient;
import com.strongit.uums.bo.TUumsBaseAreacode;

import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;

import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.exception.ServiceException;

import java.io.IOException;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.strongit.di.util.XMLParser;

@Service
@Transactional
public class SynchroniManager extends BaseManager {

	@Autowired IUserService userService;	

	/**
	 * 构造方法。
	 */
	public SynchroniManager() {
	}
	
	
	public TUumsBaseAreacode getArea(String areaId) {
		return userService.getAreaInfoByAreaCode(areaId);
	}

	/**
	 * 解析组织机构xml文件生成List对象
	 * 
	 * @author 蒋国斌
	 * @date 2009-7-14 下午05:01:01
	 * @param strPacket
	 *            XML文件
	 * @return
	 * @throws IOException 
	 * @throws ServiceException 
	 * @throws DAOException 
	 * @throws SystemException 
	 */
	public List parseOrgXml(String sessionId, String data){
		String strPacket = SynchClient.getOrgangesDate(sessionId, data);
		Packet pa = new Packet();
		try {
			pa = XMLParser.parser(strPacket);
		} catch (com.strongit.di.exception.SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List organ = pa.getRS("Branchs");
		List finaOrg = new ArrayList();
		for (Iterator it = organ.iterator(); it.hasNext();) {
			Map row = (Map) it.next();
			StringBuffer str = new StringBuffer();
			for (Iterator iter = row.keySet().iterator(); iter.hasNext();) {
				String field = (String) iter.next();
				String vals = (String) row.get(field);
				str.append(vals);
				str.append(",");
			}
			str.append(":");

			String[] arr = str.toString().split(":");
			for (int i = 0; i < arr.length; i++) {
				FinaBaseOrg org = new FinaBaseOrg();
				String[] ar = arr[i].split(",");
				org.setParlorOrgId(ar[0]);
				org.setParlorId(ar[1]);
				org.setParlorName(ar[2]);
				finaOrg.add(org);
			}
		}
		
		return finaOrg;
	}

	/**
	 * 解析用户密码xml文件生成String对象
	 *@author 蒋国斌
	 *@date 2009-7-24 下午03:47:24 
	 * @param sessionId
	 * @param data
	 * @return
	 */
public String parseUserPasswordXml(String sessionId, String data){
	  String xmlData="";
      String strPacket=""; 
	if(data!=null && data!=""){
			try {
				xmlData = SynchClient.getPacketbyUser(data,"192.168.2.185");//将参数解析成XML格式数据
			} catch (com.strongit.di.exception.SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			   strPacket=SynchClient.getUserPassword(sessionId, xmlData);
	        }
	        else{
	        	strPacket=SynchClient.getUsersDate(sessionId,data);
	        }

			Packet pa = new Packet();
			try {
				pa = XMLParser.parser(strPacket);
			} catch (com.strongit.di.exception.SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String pwd=pa.getVar("userPassword");
	    return pwd;
}
	/**
	 * 解析机构用户xml文件生成List对象
	 * 
	 * @author 蒋国斌
	 * @date 2009-7-20 上午09:19:57
	 * @param sessionId
	 * @param data
	 * @return
	 * @throws BaseException 
	 */

	public List parseFinaUserXml(String sessionId, String data) { 
        String xmlData="";
        String strPacket="";
        if(data!=null && data!=""){
		try {
			xmlData = SynchClient.getPacketbyOrg(data,"192.168.2.185");
		} catch (com.strongit.di.exception.SystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		   strPacket=SynchClient.getUsersDate(sessionId, xmlData);
        }
        else{
        	strPacket=SynchClient.getUsersDate(sessionId,data);
        }

		Packet pa = new Packet();
		try {
			pa = XMLParser.parser(strPacket);
		} catch (com.strongit.di.exception.SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List organ = pa.getRS("Users");
		List finaUser = new ArrayList();
		for (Iterator it = organ.iterator(); it.hasNext();) {
			Map row = (Map) it.next();
			StringBuffer str = new StringBuffer();
			for (Iterator iter = row.keySet().iterator(); iter.hasNext();) {
				String field = (String) iter.next();
				String vals = (String) row.get(field);
				str.append(vals);
				str.append(",");
			}
			str.append(":");

			String[] arr = str.toString().split(":");
			for (int i = 0; i < arr.length; i++) {
				FinanceUser ur = new FinanceUser();
				String[] ar = arr[i].split(",");
				ur.setUserName(ar[0]);
				ur.setUserType(ar[1]);
				ur.setUserId(ar[3]);
				ur.setIsCompanyAdmin(ar[6]);
				ur.setUserPwd(ar[7]);
				ur.setEnable(ar[15]);
				finaUser.add(ur);
			}
		}
		return finaUser;
	}
/**
 * 得到已经同步过的所有财政机构
 *@author 蒋国斌
 *@date 2009-7-22 上午10:17:31 
 * @param orgCode
 * @throws DAOException
 * @throws SystemException
 * @throws ServiceException
 */

	@Transactional(readOnly = false)
	public List getOrgsbyCode() throws DAOException,
			SystemException, ServiceException {
	      return userService.getOrgsbyCode();
	}
	
	/**
	 * 保存同步机构
	 *@author 蒋国斌
	 *@date 2009-7-27 上午08:50:13 
	 * @param finacode
	 */
	public void addFinaOrgList(String oaId,List finacode){
	    Long secode = userService.getNextOrgSequence();
		for (Iterator it = finacode.iterator(); it.hasNext();) {
			FinaBaseOrg fog = (FinaBaseOrg) it.next();
			TUumsBaseOrg org = new TUumsBaseOrg();
			org.setOrgId(null);
			org.setOrgSyscode(oaId + fog.getParlorId());
			org.setOrgName(fog.getParlorName());
			org.setOrgCode(oaId + fog.getParlorId());
			org.setRest1(fog.getParlorId());
			org.setOrgAreaCode("5FE216F2B5F5BF19E040007F01005E09");
			org.setOrgGrade("1");
			org.setOrgNature("1");
			org.setOrgIsdel("0");
			org.setOrgSequence(secode);
			userService.saveOrgInfo(org);
			//secode = String.valueOf(Integer.valueOf(secode) + 1);
			secode=secode+1;
		}
	}

	/**
	 * 机构同步
	 * 
	 * @author 蒋国斌
	 * @date 2009-7-20 上午10:14:02
	 * @param code
	 * @param codeRule
	 * @param finacode
	 */
	public void setSynchronizeforOrg(List finacode) {
		String oaId =userService.getNextOrgCode(null);// 预留给财政机构的父级编码
		List<TUumsBaseOrg> bo=userService.getOrgsbyCode();
		if(bo.size()==0 || bo==null){
		Long secode = userService.getNextOrgSequence();
		TUumsBaseOrg borg = new TUumsBaseOrg();
		borg.setOrgId(null);
		borg.setOrgSyscode(oaId);
		borg.setOrgName("江西财政机构");
		borg.setOrgCode(oaId);
		borg.setOrgAreaCode("5FE216F2B5F5BF19E040007F01005E09");
		borg.setOrgGrade("1");
		borg.setOrgNature("1");
		borg.setOrgIsdel("0");
		borg.setRest1("0000");
		borg.setOrgSequence(secode);
		try {
			userService.saveOrgInfo(borg);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.addFinaOrgList(oaId,finacode);
		
		}
		else{
		    List<FinaBaseOrg> synoNew=new ArrayList();
		    boolean foo;
				for (Iterator fit = finacode.iterator(); fit.hasNext();) {
					FinaBaseOrg fog = (FinaBaseOrg) fit.next();
					foo=true;
					for (Iterator it = bo.iterator(); it.hasNext();) {
						TUumsBaseOrg org = (TUumsBaseOrg) it.next();
						if(fog.getParlorId().equals(org.getRest1())){ 
							org.setOrgName(fog.getParlorName());
						     userService.saveOrgInfo(org);
							 it.remove();
							foo=false;
							break;
						  }
						
				        }
					if(foo){
						synoNew.add(fog);
					 }
		         }
				this.addFinaOrgList(oaId,synoNew);
				if(bo.size()>0){
				for (Iterator it = bo.iterator(); it.hasNext();) {
					TUumsBaseOrg org = (TUumsBaseOrg) it.next();
					if(!org.getOrgSyscode().equals(oaId)){
						userService.deleteOrgs(org.getOrgId());
						}
					
				}
				}
				
				
		}

	}
	
	
	/**
	 * 保存同步机构下的所有用户
	 *@author 蒋国斌
	 *@date 2009-7-27 上午08:50:13 
	 * @param finacode
	 */
	public void addFinaUserList(List users,TUumsBaseOrg org){
		Long secCode=userService.getNextUserSequence();
		for (Iterator it = users.iterator(); it.hasNext();) {
			FinanceUser fu=(FinanceUser)it.next();
			TUumsBaseUser tu=new TUumsBaseUser();
			tu.setOrgId(org.getOrgId());
			tu.setUserId(null);
			tu.setUserName(fu.getUserName());
			tu.setUserLoginname(fu.getUserId());
			tu.setUserIsSupManager(fu.getIsCompanyAdmin());
			tu.setUserPassword(SynchClient.convUpperLower(fu.getUserPwd()));
			tu.setUserSequence(secCode);
			tu.setUserSyscode(fu.getUserId());
			tu.setUserIsactive(fu.getEnable());
			userService.saveUserInfo(tu);
			secCode = secCode+1;
		}
		
		
	}
	
	/**
	 * 用户同步
	 *@author 蒋国斌
	 *@date 2009-7-22 上午10:20:09 
	 * @param users
	 * @param sysCode
	 */
	public void setSynchronizeforUser(List users,String rest1) {
		TUumsBaseOrg org =userService.getOrgInfoByRest1(rest1, "");
		List ou=userService.getUserInfoByOrgId(org.getOrgId(),"","");
		if(ou.size()==0 || ou==null){
		  this.addFinaUserList(users,org);
		}
		else{
		    List<FinanceUser> synoNew=new ArrayList();
		    boolean foo;
				for (Iterator fit = users.iterator(); fit.hasNext();) {
					FinanceUser fu = (FinanceUser) fit.next();
					foo=true;
					for (Iterator it = ou.iterator(); it.hasNext();) {
						TUumsBaseUser tu = (TUumsBaseUser) it.next();
						if(fu.getUserId().equals(tu.getUserSyscode())){ 
							tu.setOrgId(org.getOrgId());
							tu.setUserName(fu.getUserName());
							tu.setUserLoginname(fu.getUserId());
							tu.setUserIsSupManager(fu.getIsCompanyAdmin());
							tu.setUserPassword(SynchClient.convUpperLower(fu.getUserPwd()));
							tu.setUserSyscode(fu.getUserId());
							tu.setUserIsactive(fu.getEnable());
							userService.saveUserInfo(tu);
							 it.remove();
							foo=false;
							break;
						  }
						
				        }
					if(foo){
						synoNew.add(fu);
					 }
		         }
				this.addFinaUserList(synoNew, org);
				if(ou.size()>0){
				for (Iterator it = ou.iterator(); it.hasNext();) {
					TUumsBaseUser tu = (TUumsBaseUser) it.next();
					userService.deleteUser(tu.getUserId());
				}
				}		
		}
		
	}
	
}