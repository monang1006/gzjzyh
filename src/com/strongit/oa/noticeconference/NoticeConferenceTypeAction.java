package com.strongit.oa.noticeconference;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.TOmConType;
import com.strongit.oa.noticeconference.util.StringUtil;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

public class NoticeConferenceTypeAction extends BaseActionSupport<TOmConType>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private INoticeConferenceType manager;
	private Page<TOmConType> page = new Page<TOmConType>(15,true);
	private TOmConType model = new TOmConType();
	private TOmConType search = new TOmConType();
	private String isAdd;

	 

	@Override
	public String delete() throws Exception {
		String[] ids = model.getContypeId().split(",");
		String noDelName = "";
		noDelName = manager.del(ids);
		if(noDelName.equals("nosuccess")){
			renderText("nosuccess");
		}
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Override
	public String list() throws Exception {
		String title = search.getContypeName();
		String demo = search.getContypeDemo();
		/*if(title!=null){
			 title = title.replace("%", "[%]");
		}
		if(demo!=null){
			demo = demo.replace("%", "[%]");
		}*/
		page = manager.getPage(page,title,demo);
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String save() throws Exception {
		if(StringUtil.isNotEmpty(model)&&StringUtil.isEmpty(model.getContypeId())){
		 //添加
			List<TOmConType> list = null;
			list = manager.getListByName(model.getContypeName());
			if(list.size()!=0){//判断是否存在同名类型，存在则不保存。
				renderText("nameConflict");
				return null;
			}
			model.setContypeId(null);
			manager.add(model);
		}else{//修改
			if(manager.isExit(model.getContypeId())){
				page = manager.getPage(page,model.getContypeName(),null);
				//判断是否存在除编辑类型外的同名类型，存在则不保存。
				if(page.getTotalCount()!=0&&!model.getContypeId().equals(page.getResult().get(0).getContypeId())){
					renderText("nameConflict");
					return null;
				}
				 
				manager.edit(model);
			}else{
				renderText("notExit");
				return null;
			}
		}
		return null;
	}

	public Page<TOmConType> getPage() {
		return page;
	}

	public void setPage(Page<TOmConType> page) {
		this.page = page;
	}

	public TOmConType getModel() {
		return model;
	}

	public void setModel(TOmConType model) {
		this.model = model;
	}

	public TOmConType getSearch() {
		return search;
	}

	public void setSearch(TOmConType search) {
		this.search = search;
	}

	public String getIsAdd() {
		return isAdd;
	}

	public void setIsAdd(String isAdd) {
		this.isAdd = isAdd;
	}
}
