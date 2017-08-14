package com.strongit.oa.desktop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.web.ServletCacheAdministrator;
import com.strongit.oa.bo.ToaDesktopSection;
import com.strongit.oa.bo.ToaInfopublishColumn;
import com.strongit.oa.bo.ToaUumsBaseOperationPrivil;
import com.strongit.oa.infopub.column.IColumnService;
import com.strongit.uums.optprivilmanage.BaseOptPrivilManager;
import com.strongmvc.webapp.action.BaseActionSupport;
/**
 * 对数据库中的桌面模块进行处理的类
 * @author yuhz
 * @version 1.0
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/desktopSection.action") })
public class DesktopSectionAction extends BaseActionSupport{
	
	private String blocktype;					//模块类型（对应模块权限CODE或者是栏目ID）
	
	private String blockid;						//模块ID
	
	private String blocklayout;					//模块
	
	private String wholeId;						//对应方案ID
	
	private DesktopSectionManager sectionManager;
	
	private IColumnService infoPubColumn;		
	
	private String tpl;							//模块颜色
	
	private String blockimg;					//模块图片
	
	private String blocktitle;					//模块名称
	
	private String blockrow;					//模块显示列数
	
	private String isshowcreator;				//是否显示创建者
	
	private String isshowdate;					//是否显示日期
	
	private String isClose;                     //是否可关闭模块
	
	private String blocktpl;					
	
	private String subjectlength;				//主题长度

	private String sectionFontSize;				//字体大小
	
	private String showType;					//信息发布模块显示形式
	
	private String isEdit;						//管理员后台编辑个人桌面模块
	


	@Autowired BaseOptPrivilManager optManager;

	@Autowired
	public void setInfoPubColumn(IColumnService infoPubColumn) {
		this.infoPubColumn = infoPubColumn;
	}
	
	@Autowired
	public void setSectionManager(DesktopSectionManager sectionManager) {
		this.sectionManager = sectionManager;
	}

	public void setBlocklayout(String blocklayout) {
		this.blocklayout = blocklayout;
	}

	public void setBlocktype(String blocktype) {
		this.blocktype = blocktype;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		sectionManager.delete(blockid);
		ToaDesktopSection deskSection=sectionManager.getObjById(blockid);
		flushMemory(this.getRequest(),deskSection.getToaDesktopWhole().getDesktopId());
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:17:38 PM
	 * @desc: 修改模块
	 * @return
	 * @throws Exception String
	 */
	public String updateSection() throws Exception {
		ToaDesktopSection deskSection=sectionManager.getObjById(blockid);
		deskSection.setSectionName(blocktitle);
		deskSection.setSectionRow(Integer.parseInt(blockrow));
		deskSection.setSectionCreater(isshowcreator);
		deskSection.setSectionDate(isshowdate);
		//管理员后台设置和编辑个人桌面模块
		if("edit".equals(isEdit)){			
			deskSection.setIsClose(isClose);
		}
		deskSection.setSectionWidth(Integer.parseInt(subjectlength));
		deskSection.setSectionFontSize(Integer.parseInt(sectionFontSize));
		if(showType!=null&&!"".equals(showType)&&!"null".equals(showType)){
			deskSection.setShowType(showType);
		}
		sectionManager.saveObj(deskSection);
		flushMemory(this.getRequest(),deskSection.getToaDesktopWhole().getDesktopId());
		return null;
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:17:58 PM
	 * @desc: 改变模块颜色
	 * @return
	 * @throws Exception String
	 */
	public String change() throws Exception{
		ToaDesktopSection deskSection=sectionManager.getObjById(blockid);
		deskSection.setSectionColor(tpl);
		sectionManager.saveObj(deskSection);
		flushMemory(this.getRequest(),deskSection.getToaDesktopWhole().getDesktopId());
		return null;
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:18:23 PM
	 * @desc: 修改模块图标
	 * @return
	 * @throws Exception String
	 */
	public String changeTpl() throws Exception{
		ToaDesktopSection deskSection=sectionManager.getObjById(blockid);
		deskSection.setSectionImg(blockimg);
		sectionManager.saveObj(deskSection);
		flushMemory(this.getRequest(),deskSection.getToaDesktopWhole().getDesktopId());
		return null;
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:18:37 PM
	 * @desc: 显示模块编辑页面
	 * @return
	 * @throws Exception String
	 * @modify zhanglei 2012-03-29 字体大小
	 */
	public String showEdit() throws Exception{
		ToaDesktopSection section=sectionManager.getObjById(blockid);
		Integer fontsize = section.getSectionFontSize();
		if(fontsize==null||fontsize==0){
			fontsize = 14;
		}else{
			fontsize = section.getSectionFontSize();
		}

		StringBuffer html=new StringBuffer();
		HttpServletRequest request=getRequest();
		html.append("<script>\n")
			.append("	function getCode(code){\n")
			.append("		var divobj=document.getElementById('text');\n")
			.append("		divobj.innerHTML=divobj.innerHTML.replace(/\\"+section.getSectionId()+"/g,code);\n")
			.append("	}\n")
			.append("</script>\n")
			.append("<div id='texts'>\n")
			.append("<div style=\"width:20%;float:left;\" class=\"block_editor_a\">元素标题：</div>\n")
			.append("<div class=\"block_editor_b\" style=\"padding-left:6px\" ><input type=\"text\" maxlength=\"20\" style=\"width:100px\" name=\"blocktitle_"+section.getSectionId()+"\" class=\"block_input\" id=\"blocktitle_"+section.getSectionId()+"\" onchange=\"changeDragText('"+section.getSectionId()+"')\" value=\""+section.getSectionName()+"\"></div>\n")
			.append("<div style=\"width:20%;float:left;\" class=\"block_editor_a\">显示条数：</div>\n")
			.append("<div class=\"block_editor_b\" style=\"padding-left:6px\"><input type=\"text\" maxlength=\"2\" name=\"blockrow\" style=\"width:30px\" class=\"block_input\" id=\"blockrow_"+section.getSectionId()+"\" value=\""+section.getSectionRow()+"\"></div>\n")
			.append("<div style=\"width:20%;float:left;\" class=\"block_editor_a\">内容长度：</div>\n")
			.append("<div class=\"block_editor_b\" style=\"padding-left:6px\"><input type=\"text\" maxlength=\"2\" name=\"subjectlength\" style=\"width:30px\" class=\"block_input\" id=\"subjectlength_"+section.getSectionId()+"\" value=\""+section.getSectionWidth()+"\"></div>\n")
			.append("<div style=\"width:20%;float:left;\" class=\"block_editor_a\" style=\"padding-left:6px\">字体大小：</div>\n")
			.append("<div class=\"block_editor_b\" style=\"padding-left:6px\"><input type=\"text\" maxlength=\"2\" name=\"sectionFontSize\" style=\"width:30px\" onkeyup=\"this.value=this.value.replace(/\\D/g,'')\" onafterpaste=\"this.value=this.value.replace(/\\D/g,'')\" class=\"block_input\" id=\"sectionFontSize_"+section.getSectionId()+"\" value=\""+fontsize+"\"></div>\n");
		if(isEdit != null && isEdit.equals("edit")){
			html.append("<div class=\"block_editor_a\">不可关闭：</div>\n");
			if(section.getIsClose()==null||"0".equals(section.getIsClose())){
				html.append("<div class=\"block_editor_b\"><input type=\"checkbox\" name=\"isClose"+section.getSectionId()+"\" value=\"1\" id=\"isClose_"+section.getSectionId()+"\" ></div>\n");
			}else{
				html.append("<div class=\"block_editor_b\"><input type=\"checkbox\" name=\"isClose_"+section.getSectionId()+"\" value=\"1\" id=\"isClose_"+section.getSectionId()+"\" checked=\"true\" ></div>\n");
			}		
		}
		if("00060014".equals(section.getSectionType())){	// 已办事宜显示“当前处理人”
			html.append("<div style=\"width:20%;float:left;\" class=\"block_editor_a\" >当前处理人：</div>\n");
		}else if("00060010".equals(section.getSectionType())){ // 待办事宜显示“上步处理人”
			html.append("<div style=\"width:20%;float:left;\" class=\"block_editor_a\" >上步处理人：</div>\n");
		}else{
			html.append("<div style=\"width:20%;float:left;\" class=\"block_editor_a\" >创建者：</div>\n");
		}
			
		if(section.getSectionCreater()==null||"0".equals(section.getSectionCreater())){
			html.append("<div class=\"block_editor_b\"><input type=\"checkbox\" name=\"isshowcreator_"+section.getSectionId()+"\" value=\"1\" id=\"isshowcreator_"+section.getSectionId()+"\" ></div>\n");
		}else{
			html.append("<div class=\"block_editor_b\"><input type=\"checkbox\" name=\"isshowcreator_"+section.getSectionId()+"\" value=\"1\" id=\"isshowcreator_"+section.getSectionId()+"\" checked=\"true\" ></div>\n");
		}
		if(section.getSectionType()!=null&&(section.getSectionType()).indexOf("pub-")!=-1){
			html.append("<div style=\"width:20%;float:left;\" class=\"block_editor_a\" >显示类型：</div>\n")
				.append("<div class=\"block_editor_b\"><select name=\"showType_"+section.getSectionId()+"\" id=\"showType_"+section.getSectionId()+"\" >");
			if("0".equals(section.getShowType())){
				html.append("<option value=\"0\" selected>文字列表</option>");
			}else{
				html.append("<option value=\"0\">文字列表</option>");
			}
			if("1".equals(section.getShowType())){
				html.append("<option value=\"1\" selected>图文混排</option>");
			}else{
				html.append("<option value=\"1\">图文混排</option>");
			}
			if("2".equals(section.getShowType())){
				html.append("<option value=\"2\" selected>图片动画</option>");
			}else{
				html.append("<option value=\"2\">图片动画</option>");
			}
			html.append("</select>")
			.append("</div>\n");
		}
		html.append("<div style=\"width:20%;float:left;\" class=\"block_editor_a\">显示日期：</div>\n");
		if(section.getSectionDate()==null||"0".equals(section.getSectionDate())){
			html.append("<div class=\"block_editor_b\"><input type=\"checkbox\" name=\"isshowdate_"+section.getSectionId()+"\" value=\"1\" id=\"isshowdate_"+section.getSectionId()+"\" ></div>\n");
		}else{
			html.append("<div class=\"block_editor_b\"><input type=\"checkbox\" name=\"isshowdate_"+section.getSectionId()+"\" value=\"1\" id=\"isshowdate_"+section.getSectionId()+"\" checked=\"true\" ></div>\n");
		}/*
		html.append("<div class=\"block_editor_a\">边框颜色：</div>\n")
			.append("<div id=\"colorpanel\" style=\"position: absolute;\"></div>\n")
			.append("<div class=\"block_editor_b\">\n")
			.append("	<div>\n")
			.append("		<div class=\"colorblock\" style=\"background:#FFFFFF;cursor:hand\" onclick=\"intocolor()\"></div>\n")
			.append("		<div class=\"colorblock\" style=\"background:#FFB0B0;cursor:hand\" onclick=\"switchTpl('"+section.getSectionId()+"','navarat')\"></div>\n")
			.append("		<div class=\"colorblock\" style=\"background:#FFC177;cursor:hand\" onclick=\"switchTpl('"+section.getSectionId()+"','orange')\"></div>\n")
			.append("		<div class=\"colorblock\" style=\"background:#FFED77;cursor:hand\" onclick=\"switchTpl('"+section.getSectionId()+"','yellow')\"></div>\n")
			.append("		<div class=\"colorblock\" style=\"background:#CBE084;cursor:hand\" onclick=\"switchTpl('"+section.getSectionId()+"','green')\"></div>\n")
			.append("       <div class=\"colorblock\" style=\"background:#A1D9ED;cursor:hand\" onclick=\"switchTpl('"+section.getSectionId()+"','blue')\"></div>\n")
			.append("		<div class=\"colorblock\" style=\"background:#BBBBBB;cursor:hand\" onclick=\"switchTpl('"+section.getSectionId()+"','gray')\"></div>\n")
			.append("	</div>\n")
			.append("</div>\n")
			.append("<div class=\"block_editor_a\"></div>\n")
			.append("<div class=\"block_editor_b\">\n")
			.append("	<div>\n")
			.append("		<div class=\"colorblock\" style=\"background:#B78AA9;cursor:hand\" onclick=\"switchTpl('"+section.getSectionId()+"','o_navarat')\"></div>\n")
			.append("		<div class=\"colorblock\" style=\"background:#D68C6F;cursor:hand\" onclick=\"switchTpl('"+section.getSectionId()+"','o_orange')\"></div>\n")
			.append("		<div class=\"colorblock\" style=\"background:#A9B98C;cursor:hand\" onclick=\"switchTpl('"+section.getSectionId()+"','o_yellow')\"></div>\n")
			.append("		<div class=\"colorblock\" style=\"background:#96C38A;cursor:hand\" onclick=\"switchTpl('"+section.getSectionId()+"','o_green')\"></div>\n")
			.append("		<div class=\"colorblock\" style=\"background:#579AE9;cursor:hand\" onclick=\"switchTpl('"+section.getSectionId()+"','o_blue')\"></div>\n")
			.append("		<div class=\"colorblock\" style=\"background:#8AA2B7;cursor:hand\" onclick=\"switchTpl('"+section.getSectionId()+"','o_gray')\"></div>\n")
			.append("	</div>\n")
			.append("	<input type=\"hidden\" name=\"blocktpl_"+section.getSectionId()+"\" id=\"blocktpl_"+section.getSectionId()+"\" value=\"\">\n")
			.append("</div>\n")
			.append("<div style=\"width:100%;\"><input class=\"block_button\" style=\"height:25px;\" type=\"button\" value=\"确定\" onclick=\"saveDragEditor('"+section.getSectionId()+"')\"> <input type=\"button\" style=\"height:25px;\" value=\"取消\" class=\"block_button\" onclick=\"modifyBlock('"+section.getSectionId()+"')\"></div>\n")
			.append("</div>\n");*/
		html.append("<div style=\"width:20%;float:left;\" class=\"block_editor_a\">边框颜色：</div>\n")
		.append("<div class=\"block_editor_b\" style=\"padding-left:6px\">\n")
		.append("	<div>\n")
		.append("		<div class=\"colorblock\" id=\"showColors\" style='cursor:hand;background:#"+section.getSectionColor()+"' onclick=\"intocolor('"+section.getSectionId()+"');\"></div>\n")
		.append("		<span class=\"block_editor_a\">取色</span>\n")
		.append("		<div id=\"colorpanel\" style=\"position: absolute;\"></div>\n")
		.append("	</div>\n")
		.append("</div>\n")
		.append("<input type=\"hidden\" name=\"blocktpl_"+section.getSectionId()+"\" id=\"blocktpl_"+section.getSectionId()+"\" value=\"\">\n")
		.append("<div style=\"width:100%;\"><a class=\"button\" style=\"cursor: pointer;\"  onclick=\"saveDragEditor('"+section.getSectionId()+"')\">确定    <a class=\"button\" style=\"cursor: pointer;\" onclick=\"modifyBlock('"+section.getSectionId()+"')\">取消</div>\n")
		.append("</div>\n");
			
		return renderText(html.toString());
	}
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:19:19 PM
	 * @desc: 显示对应模块编辑内容
	 * @return
	 * @throws Exception String
	 */
	public String showModel() throws Exception{
		
		ToaDesktopSection section=sectionManager.getObjById(blockid);
		ToaUumsBaseOperationPrivil privil=optManager.getPrivilInfoByPrivilSyscode(section.getSectionType());
		StringBuffer xml = new StringBuffer();
		xml.append("<channel>");
		xml.append("<item>");
		xml.append("<blockid>");
		xml.append(section.getSectionId());
		xml.append("</blockid>");
		xml.append("<blocktitle>");
		xml.append(section.getSectionName());
		xml.append("</blocktitle>");
		xml.append("<blocktpl>");
		xml.append(section.getSectionColor());
		xml.append("</blocktpl>");
		xml.append("<blockimg>");
		xml.append(section.getSectionImg());
		xml.append("</blockimg>");
		xml.append("<blockurl>");
		xml.append(section.getSectionurl());
		xml.append("</blockurl>");
		xml.append("</item>");
		xml.append("</channel>");
		return renderXML(xml.toString());//this.renderText(desktop.showDesktop());
	}

	@Override
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:19:19 PM
	 * @desc: 对新模块进行保存
	 * @return
	 * @throws Exception String
	 */
	public String save() throws Exception {
		// TODO Auto-generated method stub
		ToaDesktopSection section;
		if(blocktype.indexOf("pub-")==-1){					//功能模块
			ToaUumsBaseOperationPrivil privil=optManager.getPrivilInfoByPrivilSyscode(blocktype);
			section=sectionManager.saveObj(blocktype, privil.getPrivilName(),wholeId,privil.getPrivilAttribute());
			if(section==null){
			  return renderHtml("error");
			}
			return this.renderText(section.getSectionId()+","+privil.getPrivilAttribute());
		}else{												//信息发布模块（处理Action被固定）
			ToaInfopublishColumn col=infoPubColumn.getColumn(blocktype.substring(4, blocktype.length()));
			section=sectionManager.saveObj(blocktype, col.getClumnName(), wholeId, "/infopub/articles/articles!showDesk.action?columnId="+col.getClumnId(),"0");
			if(section==null){
				  return renderText("error");
				}
			return this.renderText(section.getSectionId()+","+"/infopub/articles/articles!showDesk.action?columnId="+col.getClumnId());
		}
		
	}
	
	/**
	 * @author:yuhz
	 * @time: 2009-9-10
	 * @desc: 刷新oscache缓存
	 * @return:
	 * @param request
	 */
	private void flushMemory(HttpServletRequest request,String key){
		ServletCacheAdministrator admin=null;
		admin=ServletCacheAdministrator.getInstance(request.getSession().getServletContext());
		if(admin!=null){
//			System.out.println(PageContext.APPLICATION_SCOPE);
			
			String aukey = admin.generateEntryKey(key, request, PageContext.APPLICATION_SCOPE,null);
			Cache cache = admin.getCache(request, PageContext.APPLICATION_SCOPE);
			cache.flushEntry(aukey);
//			admin.flushAll();
		}
	}
//	private void flushMemory(HttpServletRequest request){
//		ServletCacheAdministrator admin=null;
//		admin=ServletCacheAdministrator.getInstance(request.getSession().getServletContext());
//		if(admin!=null){
//			admin.flushAll();
//		}
//	}

	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setWholeId(String wholeId) {
		this.wholeId = wholeId;
	}

	public void setBlockid(String blockid) {
		this.blockid = blockid;
	}

	public void setTpl(String tpl) {
		this.tpl = tpl;
	}

	public void setBlockimg(String blockimg) {
		this.blockimg = blockimg;
	}

	public void setBlocktitle(String blocktitle) {
		this.blocktitle = blocktitle;
	}

	public void setBlockrow(String blockrow) {
		this.blockrow = blockrow;
	}

	public void setIsshowcreator(String isshowcreator) {
		this.isshowcreator = isshowcreator;
	}

	public void setIsshowdate(String isshowdate) {
		this.isshowdate = isshowdate;
	}

	public void setBlocktpl(String blocktpl) {
		this.blocktpl = blocktpl;
	}

	public void setSubjectlength(String subjectlength) {
		this.subjectlength = subjectlength;
	}
	
	public void setSectionFontSize(String sectionFontSize) {
		this.sectionFontSize = sectionFontSize;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public String getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
	}
	
	public String getIsClose() {
		return isClose;
	}

	public void setIsClose(String isClose) {
		this.isClose = isClose;
	}


}
