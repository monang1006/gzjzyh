/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-4-25
 * Autour: hull
 * Version: V1.0
 * Description： 文档中心权限action
 */
package com.strongit.oa.archive.tempfile;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaArchiveTempfilePrivil;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.util.OALogInfo;
import com.strongmvc.webapp.action.BaseActionSupport;

public class TempPrivilAction extends BaseActionSupport {

	//权限BO
	private ToaArchiveTempfilePrivil model=new ToaArchiveTempfilePrivil();

	private TempPrivilManager tempprivilmanager;//权限manager

	private String tempfileprivilId;//文件权限ID

	private String userId;//当前用户ID

	private String orguserid;//授权用户ID

	private String userName;//授权用户名称

	private String tempfileIds;//文件ID

	private List<ToaArchiveTempfilePrivil> privilList;//权限列表

	private IUserService userService;//用户接口


	/**
	 * 删除权限
	 */
	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 截取字符串
	 * @param temp
	 * @return
	 */
	public List<String> getStoList(String temp){
		String[] shore=temp.split(",");
		List<String> list1=new ArrayList<String>();
		if(shore.length>0){
			for(int i=0;i<shore.length;i++){

				list1.add(shore[i]);

			}
		}
		return list1;
	}

	/**
	 * 设置权限初始化
	 */
	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		getRequest().setAttribute("backlocation","javascript:history.back();");	
		String id="";
		userName="";
		if(tempfileIds!=null&&getStoList(tempfileIds).size()<2){//判断是否只设置一个文件
			privilList=tempprivilmanager.getPrivilByTempfile(getStoList(tempfileIds).get(0));//根据文件查看权限
			for(ToaArchiveTempfilePrivil pr:privilList){//遍历有权限用户
				if(pr.getTempfileprivilUser()!=null&&!"".equals(pr.getTempfileprivilUser()))
				{
					id=id+pr.getTempfileprivilUser()+",";
					userName=userName+userService.getUserInfoByUserId(pr.getTempfileprivilUser()).getUserName()+",";
				}
			}
			if(id!=null&&!"".equals(id))
				id=id.substring(0,id.length()-1);
			if(userName!=null&&!"".equals(userName)){
				userName=userName.substring(0,userName.length()-1);
			}
			if(privilList!=null&&privilList.size()>0){//判断是否有权限用户
				model.setTempfileprivilDesc(privilList.get(0).getTempfileprivilDesc());
			}
		}
		userId=id;

		return "input";
	}

	/**
	 * 权限列表
	 */
	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		if(tempfileprivilId!=null){
			model=tempprivilmanager.getPrivilByID(tempfileprivilId);
		}else{
			model=new ToaArchiveTempfilePrivil();
		}

	}

	/**
	 * 保存
	 */
	@Override
	public String save() throws Exception {
		getRequest().setAttribute("backlocation","javascript:history.back();");		
		List<String> userList=getStoList(userId);
		List<String> tempfileList=getStoList(tempfileIds);
		boolean isuser=true;


		//判断是否有当前用户
		for(String puser:userList){
			if(getUserid().equals(puser)){
				isuser=false;
				break;
			}
		}
		//
		if(userList.size()==0||isuser){
			userList.add(getUserid());
		}
		for(String tempid:tempfileList){
			tempprivilmanager.save(userList, tempid,model.getTempfileprivilDesc(),new OALogInfo("保存文件查看权限"));
		}

		return renderHtml("<script> window.close(); </script>");
	}

	/***
	 * 获取当前用户名
	 * @return
	 */
	public String getUserid(){
		String userid=userService.getCurrentUser().getUserId();
		return userid;
	}
	public ToaArchiveTempfilePrivil getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	public String getTempfileprivilId() {
		return tempfileprivilId;
	}

	public void setTempfileprivilId(String tempfileprivilId) {
		this.tempfileprivilId = tempfileprivilId;
	}

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public List<ToaArchiveTempfilePrivil> getPrivilList() {
		return privilList;
	}

	public void setPrivilList(List<ToaArchiveTempfilePrivil> privilList) {
		this.privilList = privilList;
	}

	public String getTempfileIds() {
		return tempfileIds;
	}

	public void setTempfileIds(String tempfileIds) {
		this.tempfileIds = tempfileIds;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrguserid() {
		return orguserid;
	}

	public void setOrguserid(String orguserid) {
		this.orguserid = orguserid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Autowired
	public void setPrivilmanager(TempPrivilManager tempprivilmanager) {
		this.tempprivilmanager = tempprivilmanager;
	}


}
