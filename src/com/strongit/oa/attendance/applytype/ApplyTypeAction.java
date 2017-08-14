package com.strongit.oa.attendance.applytype;

import java.util.Date;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaAttendType;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;
/**
 * 申请类型action
 * @author 胡丽丽
 * @date 2009-12-24
 *
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "applyType.action", type = ServletActionRedirectResult.class) })
public class ApplyTypeAction extends BaseActionSupport {
	private ApplyTypeManager manager;
	private Page<ToaAttendType> page=new Page<ToaAttendType>(FlexTableTag.MAX_ROWS,true);
	private ToaAttendType model=new ToaAttendType();
	private static final String YES="0";//是
	private static final String NO="1";	//否
	private String typeId;				//申请类型ID
	private String disLogo;				//是否搜索
	private String isEnable;			//启用，禁用
	
	public String getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}

	public String getDisLogo() {
		return disLogo;
	}

	public void setDisLogo(String disLogo) {
		this.disLogo = disLogo;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	/**
	 * 删除申请类型
	 * @author 胡丽丽
     * @date 2009-12-24
	 */
	@Override
	public String delete() throws Exception {
		String[] typeIds=typeId.split(",");
		for(int i=0;i<typeIds.length;i++){
			if(typeIds[i]!=null&&!"".equals(typeIds[i])){
			manager.deleteById(typeIds[i]);
			}
		}
		return RELOAD;
	}

	/**
	 * 初始化申请类型界面
	 * @author 胡丽丽
     * @date 2009-12-24
	 */
	@Override
	public String input() throws Exception {
		prepareModel();
		return INPUT;
	}

	/**
	 *查看申请类型
	 *@author 胡丽丽
     * @date 2009-12-24
	 * @return
	 * @throws Exception
	 */
	public String show()throws Exception{
		prepareModel();
		return "show";
	}
	/**
	 * 查看申请类型列表
	 * @author 胡丽丽
     * @date 2009-12-24
	 */
	@Override
	public String list() throws Exception {
		try {
			if(disLogo!=null&&!"".equals(disLogo)){	//是否多条件查询
				page=manager.search(page, model);
			}else{
				page=manager.getAttendTypeByIsApplyType(page, YES);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 初始化BO
	 * @author 胡丽丽
     * @date 2009-12-24
	 */
	@Override
	protected void prepareModel() throws Exception {
		if(typeId!=null&&!"".equals(typeId)){
			model=manager.getAttendTypeByID(typeId);
		}else{
			model=new ToaAttendType();
		}
	}

	/**
	 * 判断是否是系统申请类型
	 * @return
	 * @throws Exception
	 */
	public String isSystem()throws Exception{
		String sys="";
		if(typeId!=null&&!"".equals(typeId)){	//是否为空
			model=manager.getAttendTypeByID(typeId);
			sys=model.getIsSystem();
		}
		return renderHtml(sys);
	}
	
	/**
	 * 保存申请类型
	 * @author 胡丽丽
     * @date 2009-12-24
	 */
	@Override
	public String save() throws Exception {
		if(model.getTypeId()==null||"".equals(model.getTypeId())){	//是否为空
			model.setTypeId(null);
			model.setIsSystem(NO);
			model.setIsApplyType(YES);
			model.setTypeCreateDate(new Date());
		}
		manager.saveAttendType(model);
		return renderHtml("<script> window.dialogArguments.document.location.reload();window.close();</script>");
	}

	/**
	 * 是否可以补填申请单
	 * @return
	 */
	public String isCanRewriter(){
		String sys="";
		if(typeId!=null&&!"".equals(typeId)){	//是否为空
			model=manager.getAttendTypeByID(typeId);
			sys=model.getCanRewriter();
		}
		return renderHtml(sys);
	}
	
	/**
	 * 获取申请类型名称
	 * @return
	 */
	public String getTypeName(){
		model=manager.getAttendTypeByID(typeId);
		return renderHtml(model.getTypeName());
	}
	
	/**
	 * 启用或禁用
	 * @author  胡丽丽
	 * @cereatedate:2009-11-16
	 * @return
	 * @throws Exception
	 */
	public String isEnableType() throws Exception{
		if(isEnable!=null&&!"".equals(isEnable)){//是否为空
			String[] ids=typeId.split(",");
			manager.updateTypeByIsEnable(isEnable, ids);
		}
		return RELOAD;
	}
	
	public ToaAttendType getModel() {
		return model;
	}

	public Page<ToaAttendType> getPage() {
		return page;
	}

	public void setPage(Page<ToaAttendType> page) {
		this.page = page;
	}

	@Autowired
	public void setManager(ApplyTypeManager manager) {
		this.manager = manager;
	}

}
