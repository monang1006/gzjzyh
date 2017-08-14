package com.strongit.oa.viewmodel;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaForamula;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * <p>Title: InterModelAction.java</p>
 * <p>Description: 界面模型Action</p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @date 	 2010-06-03
 * @version  1.0
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/viewModel.action") })
public class ViewModelAction extends BaseActionSupport{

	private static final long serialVersionUID = 6477913858315213637L;
	
	private static final Logger log  = Logger.getLogger(ViewModelAction.class); 
	
	@Autowired private ViewModelManager viewModelManager;					//视图模型Manager
	
	@Autowired private ViewModelPageManager viewModelPageManager;
	
	private ToaForamula model = new ToaForamula();
	
	private Page<ToaForamula> page = new Page<ToaForamula>(FlexTableTag.MAX_ROWS,true);		//分页对象
	
	private String id;														//页面传入ID值
	
	private static final String menu = "1";
	
	private static final String work = "2";
	
	private static final String top = "3";

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		if(id!=null){
			if(viewModelManager.chargeDefault(id)){
				return this.renderText("has");
			}else{
				if(viewModelManager.deleteByIds(id)){
					return this.renderText("true");
				}else{
					return this.renderText("false");
				}
			}
		}else{
			return this.renderText("false");
		}
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		model.setForamulaDefault("0");
		return "input";
	}
	
	public String edit() throws Exception{
		model = viewModelManager.getObjById(id);
		return "input";
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		page = viewModelManager.getPageList(page);
		return this.SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		if("".equals(model.getForamulaId())||"null".equals(model.getForamulaId())){
			model.setForamulaId(null);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
			model.setCreateTime(sdf.format(new Date()));
		}
		model.setForamulaDate(new Date());
	}

	@Override
	public String save() throws Exception {
		prepareModel();
		if(!"".equals(model.getForamulaDec())&&null!=model.getForamulaDec()){
			model.setForamulaDec(java.net.URLDecoder.decode(model
					.getForamulaDec(), "utf-8"));
		}
		if(viewModelManager.saveObj(model)){
			return this.renderText("true");
		}else{
			return this.renderText("false");
		}
	}
	
	public ToaForamula getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	public Page<ToaForamula> getPage() {
		return page;
	}

	public void setPage(Page<ToaForamula> page) {
		this.page = page;
	}

	public void setModel(ToaForamula model) {
		this.model = model;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
