package com.strongit.oa.importAddress;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.strongit.oa.address.AddressGroupManager;
import com.strongit.oa.address.AddressManager;
import com.strongit.oa.bo.ToaAddress;
import com.strongit.oa.bo.ToaAddressGroup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class DataCopyToDB {
	
	AddressManager addressManager ;
	AddressGroupManager agm ;
	@Autowired
	public void setAgm(AddressGroupManager agm) {
		this.agm = agm;
	}
	@Autowired
	public void setAddressManager(AddressManager addressManager) {
		this.addressManager = addressManager;
	}
	
	
	/**
	 * author:luosy
	 * description:将数据，导入到OA数据库
	 * modifyer:
	 * description:
	 */
	@Test
	public void dateCopy2DB() {
		File file = new File("e:/个人通讯录信息.txt");
		//File file = new File("e:/1.txt");
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String inputLine;
		try {
			while ((inputLine = in.readLine()) != null) {
//				inputLine = inputLine.replaceAll(",,", ",");
				inputLine = formatString(inputLine);
				
				//添加组
				String[] inputItems = inputLine.split(",");
				String userId = inputItems[2].split(":")[1];
				String groupName = inputItems[3].split(":")[1];
				ToaAddressGroup group = this.addGroup(userId, groupName);
				if(null==group){ break;}
				
				//添加人
				String persons = inputItems[4].split(":")[1];
				String[] person = persons.split("@");
				for(String personName:person){
					this.addAddress(personName, userId, group);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * author:luosy
	 * description:取出多余的","(即包含",,"的情况)
	 * modifyer:
	 * description:
	 * @param str
	 * @return
	 */
	public String formatString(String str){
		if(str.indexOf(",,")>0){
			str = str.replaceAll(",,", ",");
			return formatString(str);
		}else{
			return str;
		}
	}
	
	/**
	 * author:luosy
	 * description: 添加到联系人
	 * modifyer:
	 * description:
	 * @param name	联系人姓名
	 * @param userId	用户ID
	 * @param group 联系人所在分组
	 */
	public void addAddress(String name,String userId,ToaAddressGroup group){
		ToaAddress addr = null;
		try {
			if(addressManager.isNotSameNameExist(name, userId,group.getAddrGroupName())){
				addr = new ToaAddress();
				addr.setToaAddressGroup(group);
				addr.setUserId(userId);
				addr.setName(name);
				addressManager.save(addr);
			}else{
				System.out.println("用户"+name+"已存在！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("用户"+name+"添加出错！");
		}
	} 
	
	/**
	 * author:luosy
	 * description:添加联系人分组
	 * modifyer:
	 * description:
	 * @param userId 用户ID
	 * @param groupName 分组名称
	 * @return
	 */
	public ToaAddressGroup addGroup(String userId,String groupName) {
		ToaAddressGroup group = null;
		try {
			group = agm.getGroupByGroupName(groupName,userId);
			if(null==group){
				group = new ToaAddressGroup();
				group.setAddrGroupName(groupName);
				group.setUserId(userId);
				agm.addGroup(group);
				//System.out.println(group.getAddrGroupId());
			}else{
				System.out.println("用户组"+groupName+"已存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("用户组"+groupName+"添加出错");
		}
		return group;
	}

}
