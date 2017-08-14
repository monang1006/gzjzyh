package com.strongit.oa.bookmark;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.strongit.oa.bo.ToaBookMark;
import com.strongit.oa.bo.ToaBookMarkEForm;
import com.strongit.oa.bookmark.util.Option;
import com.strongit.oa.bookmark.util.Select;
import com.strongit.oa.common.eform.IEFormService;
import com.strongit.oa.common.eform.constants.Constants;
import com.strongit.oa.common.eform.model.EForm;
import com.strongit.oa.common.eform.model.EFormField;
import com.strongit.oa.eformManager.EformManagerManager;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 标签管理Action.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-7-7 下午04:52:02
 * @version  2.0.2.3
 * @classpath com.strongit.oa.bookmark.BookMarkAction
 * @comment
 * @email dengzc@strongit.com.cn
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "bookMark.action", type = ServletActionRedirectResult.class) })
public class BookMarkAction extends BaseActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6225599084490769919L;

	ToaBookMark model = new ToaBookMark();						//定义MODEL对象.
	
	Page<ToaBookMark> page = new Page<ToaBookMark>(FlexTableTag.MAX_ROWS,true);	//定义列表分页对象	
	
	List<ToaBookMark> bookMarkList ;							//标签列表
	
	@Autowired BookMarkManager manager ;						//注入服务类对象
	
	@Autowired IEFormService eformService ;						//注入电子表单服务类对象. 
	
	@Autowired EformManagerManager eformManagerManager;			//表单管理MANAGER

	List<EForm> eforms	;										//电子表单列表（只显示流程启动表单）
	
	String formId	;											//电子表单模板id
	
	List<EFormField> eformFields = new ArrayList<EFormField>();	//电子表单字段列表
	
	
	
	/**
	 * 删除标签
	 * 支持批量删除
	 */
	@Override
	public String delete() throws Exception {
		String id = model.getId();
		Assert.notNull(id, "标签id不能为空！");
		manager.delete(id.split(","));
		return RELOAD ;
	}

	/**
	 * 转到添加或修改标签页面
	 * 转到目标页面之前会调用prepareModel()方法.
	 */
	@Override
	public String input() throws Exception {
		if(model.getId() != null && !"".equals(model.getId())){
			model = manager.getBookMarkById(model.getId());
		}
		return INPUT;
	}

	/**
	 * 标签分页列表.
	 * 郑志斌  2011-01-19 修改   组织机构授权开关
	 */
	@Override
	public String list() throws Exception {
		try {
			if(model.getDesc() != null){
				model.setDesc(URLDecoder.decode(model.getDesc(),"utf-8"));
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("转码异常",e);
		}
		page = manager.getBookMarkList(page, model);
		
		eforms = eformService.getFormTemplateList(Constants.SF);                       //郑志斌  2011-01-19 修改   组织机构授权开关
		//eforms = eformManagerManager.getAllList();
		
		if(eforms != null && !eforms.isEmpty()){
			EForm firstEForm = eforms.get(0);
			if(formId == null || "".equals(formId)){
				formId = String.valueOf(firstEForm.getId());
			}
			eformFields = eformService.getFormTemplateComponents(formId);
			if(eformFields != null && !eformFields.isEmpty()){
				Select options = new Select();		//存储下拉列表选项
				Option rootOption = new Option(getText(GlobalBaseData.BOOKMARK_NOTSET));//未指定的选项
				rootOption.setChecked(true);
				options.add(rootOption); 
				List markEForms = manager.getBookMarkList(formId);
				Map<String, Object[]> mapMark = new HashMap<String, Object[]>();
				for(EFormField field : eformFields){
					if(field.getCaption()!=null && !"".equals(field.getCaption())
							&&!field.getType().equals("Strong.Form.Controls.Label")		//标签
							&&!field.getType().equals("Strong.Form.Controls.SingleAccessory")//单附件
							&&!field.getType().equals("Strong.Form.Controls.AccessoryControl")	
							&&!field.getType().equals("Strong.Form.Controls.Button")	//按钮
							&&!field.getType().equals("Strong.Form.Controls.Line")	//线条
							&&!field.getType().equals("Office")){//电子表单控件标题不为空。							   //WORD	
						Option option = new Option(field.getName(),field.getFieldname(),field.getTablename(),field.getCaption());
						options.add(option);
					}
				}
				for(int i=0;i<markEForms.size();i++){
					Object[] objs = (Object[])markEForms.get(i);
					mapMark.put(objs[0].toString(), objs);
				}
				List<ToaBookMark> bookMarkList = page.getResult();
				if(bookMarkList != null && !bookMarkList.isEmpty()){
					for(ToaBookMark bookMark : bookMarkList){
						String id = bookMark.getId();//标签id
						if(mapMark.get(id)!=null){
							Object[] objs = mapMark.get(id);
							String editId = (String)objs[2];
							if(editId == null){
								StringBuilder html = new StringBuilder(options.toString());
								html.append("&nbsp;");
								html.append("<input class=\"input_bg\" type=\"button\" onclick=\"doSave('"+id+"','"+formId+"');\"")
									.append(" value=\"")
									.append(getText(GlobalBaseData.BOOKMARK_SAVE))
									.append("\" />");
								bookMark.setRest5(html.toString());
							}else{
								Select newSelect = new Select();
								newSelect.addAll(options.getOptions());
								Option o = newSelect.get(editId);
								if(o != null){
									o.setChecked(true);
									StringBuilder html = new StringBuilder(newSelect.toString());
									html.append("&nbsp;");
									html.append("<input class=\"input_bg\" type=\"button\" onclick=\"doSave('"+id+"','"+formId+"');\"")
									.append(" value=\"")
									.append(getText(GlobalBaseData.BOOKMARK_SAVE))
									.append("\" />");
									bookMark.setRest5(html.toString());
									o.setChecked(false);//必须要加这句，否则每次都会修改引用
								}else{
									/**
									 * bug:0000050196
									 * 如果之前影射过，保存在表里，但是后来重新划了表单，以前保存的控件名字，比如edit18找不到了
									 * 那么也当做是未影射过处理，重新保存影射关联数据，修改更新之前的记录
									 */
									StringBuilder html = new StringBuilder(options.toString());
									html.append("&nbsp;");
									html.append("<input class=\"input_bg\" type=\"button\" onclick=\"doSave('"+id+"','"+formId+"');\"")
										.append(" value=\"")
										.append(getText(GlobalBaseData.BOOKMARK_SAVE))
										.append("\" />");
									bookMark.setRest5(html.toString());
								}
							}
						}else{
							StringBuilder html = new StringBuilder(options.toString());
							html.append("&nbsp;");
							html.append("<input class=\"input_bg\" type=\"button\" onclick=\"doSave('"+id+"','"+formId+"');\"")
								.append(" value=\"")
								.append(getText(GlobalBaseData.BOOKMARK_SAVE))
								.append("\" />");
							bookMark.setRest5(html.toString());
						}
						logger.info(bookMark.getRest5());
					}
				}
			}
		}
		return SUCCESS ;
	}

	@Override
	protected void prepareModel() throws Exception {
	}

	/**
	 * 校验标签名称是否存在
	 * ret：
	 * 	<p>
	 * 		<li>-2:发生异常</li>
	 * 		<li>-1:参数传递错误</li>
	 * 		<li>0:名称不存在</li>
	 * 		<li>1:名称已存在</li>
	 * 	</p>
	 * @author:邓志城
	 * @date:2010-7-7 下午07:48:12
	 */
	public void checkName() {
		String ret = "0";
		try{
			if(model.getName() == null){
				ret = "-1";
			}else{
				ToaBookMark bo = manager.getBookMarkByName(model);
				if(bo == null){//根据标签名称未查找到
					ret = "0";
				}else{
					ret = "1";
				}
			}
		}catch(Exception e){
			logger.error("校验标签名称唯一性时发生异常",e);
			ret = "-2";
		}
		this.renderText(ret);
	}

	/**
	 * 保存标签
	 * 保存之前会调用prepareModel()方法
	 */
	@Override
	public String save() throws Exception {
		if(model.getId()!=null && "".equals(model.getId())){
			model.setId(null);
		}
		model.setName(model.getName().trim());
		manager.save(model);
		return RELOAD;
	}

	/**
	 * 根据书签id在ToaBookMrkEForm中查找
	 * ,若找到则做删除操作，否则直接添加。
	 * 每次执行这个方法时先删除记录再添加.
	 * @author:邓志城
	 * @date:2010-7-8 下午02:50:10
	 * @return
	 */
	public void doSaveRelate() {
		String ret = "0";
		try{
			String bookMarkId = model.getId();//得到书签id
			String fieldName  = model.getRest1();//得到字段名
			String tableName  = model.getRest2();//得到表名称
			String editId     = model.getRest3();//控件id
			List list = manager.getBookMarkList(formId, bookMarkId);
			if(list != null && !list.isEmpty()){//在formid模板中找到此书签的映射记录,先删除
				for(int i=0;i<list.size();i++){
					Object[] objs = (Object[])list.get(i);
					String pk = objs[5].toString();
					manager.deleteRelate(pk);
				}
			}
			ToaBookMarkEForm mark = new ToaBookMarkEForm();
			mark.setBookMarkId(bookMarkId);			//书签id
			mark.setFieldName(fieldName);			//绑定的字段名
			mark.setFormId(formId);					//模板id
			mark.setRest1(editId);					//控件id
			mark.setTableName(tableName);			//表名称
			manager.saveRelate(mark);				//保存对象
		}catch(Exception e){
			logger.error("保存映射时出错。",e);
			ret = "-1";
		}
		this.renderText(ret);
	}

	/**
	* @method initAddBookMarkToWord
	* @author 申仪玲
	* @created 2011-9-22 下午09:15:00
	* @description 添加标签至WORD中。需要读出已经定义好的标签列表.
	* @return String 返回类型
	*/
	public String initAddBookMarkToWord(){
		bookMarkList = manager.getBookMarkList();
		//对bookMarkList中的标签进行排序  
		if(bookMarkList != null && !bookMarkList.isEmpty()){
			Collections.sort(bookMarkList, new Comparator<ToaBookMark>(){
				public int compare(ToaBookMark toaBookMark1, ToaBookMark toaBookMark2) {             
					  String  name1=toaBookMark1.getName();             
					  String  name2=toaBookMark2.getName();   
					  return name1.compareTo(name2);
					 }             	
			});
		}
		return "addtoword";
	}
	
	public ToaBookMark getModel() {
		return model ;
	}

	public Page<ToaBookMark> getPage() {
		return page;
	}

	public void setPage(Page<ToaBookMark> page) {
		this.page = page;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public List<EForm> getEforms() {
		return eforms;
	}

	public void setEforms(List<EForm> eforms) {
		this.eforms = eforms;
	}

	public List<EFormField> getEformFields() {
		return eformFields;
	}

	public void setEformFields(List<EFormField> eformFields) {
		this.eformFields = eformFields;
	}

	public List<ToaBookMark> getBookMarkList() {
		return bookMarkList;
	}

	public void setBookMarkList(List<ToaBookMark> bookMarkList) {
		this.bookMarkList = bookMarkList;
	}

}
