/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-4-25
 * Autour: hull
 * Version: V1.0
 * Description： 知识类型action
 */
package com.strongit.oa.knowledge.mykmsort;

import java.net.URLDecoder;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaKnowledgeMykm;
import com.strongit.oa.bo.ToaKnowledgeMykmSort;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.knowledge.mykm.MykmManage;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/knowledge/mykm/mykmSort.action", type = ServletActionRedirectResult.class) })
public class MykmSortAction extends BaseActionSupport {

	private Page<ToaKnowledgeMykmSort> page = new Page<ToaKnowledgeMykmSort>(FlexTableTag.MAX_ROWS, true);

	private ToaKnowledgeMykmSort model=new ToaKnowledgeMykmSort();//类型BO

	private String mykmSortId;//类型ID

	private MykmSortManage sortManage;//类型manager

	private List<ToaKnowledgeMykmSort> sortList;//类型列表

	private IUserService userservice;//用户接口

	private String mykmSortName;//类型名称

	private MykmManage mykmManager;//知识收藏Manager




	/**
	 * 删除一个类型
	 * 
	 * @return java.lang.String
	 * @throws Exception
	 */
	@Override
	public String delete() throws Exception {
		Page<ToaKnowledgeMykm> mykmlist=new Page<ToaKnowledgeMykm>();
		String userid=userservice.getCurrentUser().getUserId();//获取当前用户
		Page<ToaKnowledgeMykm> mykmpage=mykmManager.getMykmsBySort(mykmlist,userid, mykmSortId);
		if(mykmpage.getResult()!=null&&mykmpage.getResult().size()>0)
		{
			 StringBuffer returnhtml = new StringBuffer(
				"<script> var scriptroot = '")
				.append(getRequest().getContextPath())
				.append("'</script>")
				.append("<SCRIPT src='")
				.append(getRequest().getContextPath())
				.append("/common/js/commontab/workservice.js'>")
				.append("</SCRIPT>")
				.append("<SCRIPT src='")
				.append(getRequest().getContextPath())
				.append("/common/js/commontab/service.js'>")
				.append("</SCRIPT>")
				.append("<script>alert('请先删除分类下的信息！'); window.location='")
				.append(getRequest().getContextPath())
				.append("/knowledge/mykmsort/mykmSort!tree.action';</script>");
		    return  renderHtml(returnhtml.toString());

		}else{
			sortManage.deleteMykmSort(mykmSortId);
		}

		//return tree();
		return renderHtml("<script> window.location='"+ getRequest().getContextPath()+"/knowledge/mykmsort/mykmSort!tree.action';</script>");
	}

	/**
	 * 添加和修改类型初始化
	 * 
	 * @return java.lang.String 
	 * @throws Exception
	 */
	@Override
	public String input() throws Exception {

		prepareModel();
		return INPUT;
	}

	/**
	 * 现实类型列表
	 * @return java.lang.String 
	 * @throws Exception
	 */
	@Override
	public String list() throws Exception {
		String userid=userservice.getCurrentUser().getUserId();//获取当前用户
		if(mykmSortName==null || mykmSortName.equals("")){//判断类型名称是否为空

			page=sortManage.getMykmSortByUser(page,userid);//根据用户查询知识类型
		}else{
			page=sortManage.getMykmSortByName(page,mykmSortName, userid);//根据用户和类型名称查询知识类型
		}
		return SUCCESS;
	}

	/**
	 * 类型显示树
	 * @return
	 * @throws Exception
	 */
	public String tree() throws Exception{

		String userid=userservice.getCurrentUser().getUserId();
		sortList=sortManage.getMykmSortListByUser(userid);

		return "tree";
	}

	@Override
	protected void prepareModel() throws Exception {

		if(mykmSortId==null||mykmSortId.equals("")){
			model=new ToaKnowledgeMykmSort();
			String userid=userservice.getCurrentUser().getUserId();
			model.setMykmSortUser(userid);
		}else{
			model=sortManage.getMykmSortByID(mykmSortId);
		}
	}

	/**
	 * 保存一个知识类型
	 * 
	 * @return java.lang.String 
	 * @throws Exception
	 */
	@Override
	public String save() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");

		if("".equals(model.getMykmSortId()))//判断类型ID是否为空
		{
			model.setMykmSortId(null);
		}
		model.setMykmSortName(URLDecoder.decode(model.getMykmSortName(), "utf-8"));
		model.setMykmSortDesc(URLDecoder.decode(model.getMykmSortDesc(), "utf-8"));
		sortManage.saveMykmSort(model);
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		obj.put("id", model.getMykmSortId());
		obj.put("name", model.getMykmSortName());
		array.add(obj);
		return renderHtml(array.toString());
	}

	/**
	 * 修改知识类型
	 * @return
	 * @throws Exception
	 */
	public String edit() throws Exception{
		sortManage.saveMykmSort(model);
		return tree();
	}

	public ToaKnowledgeMykmSort getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	public List<ToaKnowledgeMykmSort> getSortList() {
		return sortList;
	}

	public void setSortList(List<ToaKnowledgeMykmSort> sortList) {
		this.sortList = sortList;
	}

	@Autowired
	public void setSortManage(MykmSortManage sortManage) {
		this.sortManage = sortManage;
	}

	public String getMykmSortId() {
		return mykmSortId;
	}

	public void setMykmSortId(String mykmSortId) {
		this.mykmSortId = mykmSortId;
	}

	public String getMykmSortName() {
		return mykmSortName;
	}

	public void setMykmSortName(String mykmSortName) {
		this.mykmSortName = mykmSortName;
	}


	@Autowired
	public void setUserservice(IUserService userservice) {
		this.userservice = userservice;
	}

	public Page<ToaKnowledgeMykmSort> getPage() {
		return page;
	}

	public void setPage(Page<ToaKnowledgeMykmSort> page) {
		this.page = page;
	}


	@Autowired
	public void setMykmManager(MykmManage mykmManager) {
		this.mykmManager = mykmManager;
	}

}
