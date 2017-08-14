package com.strongit.oa.publiccontact;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.text.StrBuilder;
import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaPcCategory;
import com.strongit.oa.bo.ToaPubliccontact;
import com.strongit.oa.util.BaseDataExportInfo;
import com.strongit.oa.util.ProcessXSL;
import com.strongit.oa.util.publicContactProcessXSL;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
public class PublicContactAction extends BaseActionSupport<ToaPubliccontact> {
	private Page<ToaPubliccontact> page = new Page<ToaPubliccontact>(
			FlexTableTag.MAX_ROWS, true);
	private Page<ToaPcCategory> pccpage = new Page<ToaPcCategory>(
			FlexTableTag.MAX_ROWS, true);
	
	private ToaPubliccontact model = new ToaPubliccontact();;
	
	private ToaPcCategory pccmodel = new ToaPcCategory();
	
	Map<String,String> typeMap = new HashMap<String, String>();
	
	private String typeId;		//分类id
	private String pcId;		//人員id
	private String pccNum;		//类别排序号
	private String pccName;		//类别名称
	private String issearch;	//是否查询
	private String searchname;	//查询  姓名
	private String searchtell;	//查询  电话
	private String searchemail;	//查询  email
	
	@Autowired
	private PublicContactManage publicContactManage;

	private List<ToaPubliccontact> pcList;

	private List<ToaPcCategory> pccList;

	/**
	 * @field serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		publicContactManage.delContacts(pcId);
		return null;
	}

	@Override
	public String input() throws Exception {
		if(pcId!=null&&!"".equals(pcId)){
			model = publicContactManage.get(pcId);
			getRequest().setAttribute("addoredit", "1");
		}else{
			getRequest().setAttribute("addoredit", "0");
		}
		pccpage.setPageNo(1);
		pccpage.setPageNoBak(1);
		pccpage.setPageSize(10000);
		pccpage.setPageSizeBak(10000);
		pccpage = publicContactManage.getContactsType(pccpage);
		for(ToaPcCategory t :pccpage.getResult()){
			typeMap.put(t.getPccId(), t.getPccName());
		}
		//解决第一次进入时typeid为空
		if(typeId==null||"".equals(typeId)){
			typeId = model.getTOaPcCategory().getPccId();
		}
		pccList = pccpage.getResult();
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		if(typeId!=null&&!"".equals(typeId)){
			if(typeId.indexOf(",")!=-1){
				typeId = typeId.substring(0, typeId.indexOf(","));
			}
			pccmodel  = publicContactManage.getType(typeId);
			//是否查询
			if(issearch==null||"".equals(issearch)){
				page = publicContactManage.getContactsBytype(page, typeId);
			}else{
				page = publicContactManage.getContactsBycondition(page, typeId, searchname, searchtell, searchemail);
			}
		}else{//第一次进入默认显示类型中的第一条记录的所有人员
			/*pccpage = publicContactManage.getContactsType(pccpage);
			if(pccpage!=null&&pccpage.getTotalCount()>0){
				typeId = pccpage.getResult().get(0).getPccId();
				pccmodel  = publicContactManage.getType(typeId);
				page = publicContactManage.getContactsBytype(page, typeId);
			}*/
			page = publicContactManage.getContactsBycondition(page, typeId, searchname, searchtell, searchemail);
			pccmodel.setPccName("全部联系人");
		}
		return SUCCESS;
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		ToaPcCategory ttt = publicContactManage.getType(typeId);
		model.setTOaPcCategory(ttt);
		if(pcId==null || "".equals(pcId)){
			model.setPcId(null);
		}else{
			model.setPcId(pcId);
		}
		publicContactManage.save(model);
		return renderHtml("<script type=\"text/javascript\">window.returnValue=true;window.close();</script>");
	}

	public String tree() {
		pccpage.setPageNo(1);
		pccpage.setPageNoBak(1);
		pccpage.setPageSize(10000);
		pccpage.setPageSizeBak(10000);
		pccpage = publicContactManage.getContactsType(pccpage);
		pccList = pccpage.getResult();
		List<ToaPcCategory> showList = new ArrayList<ToaPcCategory>();
		StringBuffer html = new StringBuffer();
		if(null!=pccList){
			for(ToaPcCategory t : pccList){
				/*ToaPcCategory m = new ToaPcCategory();
				m.setPccId(t.getPccId());
				if(t.getPccName().length()>8){
					m.setPccName(t.getPccName().substring(0,8)+"...");
				}else{
					m.setPccName(t.getPccName());
				}
				//临时存储
				m.setPccOther(t.getPccName());
				showList.add(m);*/
				String name = "";
				if(t.getPccName().length()>7){
					name = t.getPccName().substring(0,7)+"...";
				}else{
					name = t.getPccName();
				}
				html.append("<li id=\""+t.getPccId()+"\" title=\""+t.getPccName()+"\">")
					.append(" 	<span  >"+name+"</span><label>&nbsp;(<font>" + publicContactManage.getCounts(t.getPccId()) + "</font>)"+ "</label>")
					.append("</li>");
			}
		}
//		getRequest().setAttribute("showList", showList);
		getRequest().setAttribute("showList", html);
		return "tree";
	}
	public String typeManage(){
		pccpage.setPageNo(page.getPageNo());
		pccpage.setPageNoBak(page.getPageNoBak());
		pccpage.setPageSize(page.getPageSize());
		pccpage.setPageSizeBak(page.getPageSizeBak());
		pccpage = publicContactManage.getContactsType(pccpage);
		return "typeManage";
	}
	public String addType(){
		getRequest().setAttribute("addoredit", "0");
		pccmodel.setPccNum(publicContactManage.getMaxNumType());
		return "inputType";
	}
	public String editType(){
		pccmodel = publicContactManage.getType(typeId);
		getRequest().setAttribute("addoredit", "1");
		return "inputType";
	}
	public String saveType(){
		publicContactManage.saveType(pccmodel);
		return renderHtml("<script type=\"text/javascript\">window.returnValue=true;window.close();</script>");
	}
	public void delType(){
		publicContactManage.delType(typeId);
	}
	public String isRepeat() throws UnsupportedEncodingException{
		//true  表示有重复
		
		boolean t = publicContactManage.isRepeat(pccNum);
		boolean m = publicContactManage.isRepeatName(pccName);
		if(!t&&!m){
			return renderText("true");
		}else if(t){
			if(m){
				return renderText("all");
			}else{
				return renderText("numFalse");
			}
		}else{
			if(t){
				return renderText("all");
			}else{
				return renderText("nameFalse");
			}
		}
	}
	public String showTree(){
		pccpage.setPageNo(1);
		pccpage.setPageNoBak(1);
		pccpage.setPageSize(10000);
		pccpage.setPageSizeBak(10000);
		pccpage = publicContactManage.getContactsType(pccpage);
		pccList = pccpage.getResult();
		List<ToaPcCategory> showList = new ArrayList<ToaPcCategory>();
		StringBuffer html = new StringBuffer();
		if(null!=pccList){
			for(ToaPcCategory t : pccList){
				/*ToaPcCategory m = new ToaPcCategory();
				m.setPccId(t.getPccId());
				if(t.getPccName().length()>8){
					m.setPccName(t.getPccName().substring(0,8)+"...");
				}else{
					m.setPccName(t.getPccName());
				}
				//临时存储
				m.setPccOther(t.getPccName());
				showList.add(m);*/
				String name = "";
				if(t.getPccName().length()>7){
					name = t.getPccName().substring(0,7)+"...";
				}else{
					name = t.getPccName();
				}
				html.append("<li id=\""+t.getPccId()+"\" title=\""+t.getPccName()+"\">")
					.append(" 	<span  >"+name+"</span><label>&nbsp;(<font>" + publicContactManage.getCounts(t.getPccId()) + "</font>)"+ "</label>")
					.append("</li>");
			}
		}
//		getRequest().setAttribute("showList", showList);
		getRequest().setAttribute("showList", html);
		return "showTree";
	}
	public String showList() throws Exception {
		if(typeId!=null&&!"".equals(typeId)){
			if(typeId.indexOf(",")!=-1){
				typeId = typeId.substring(0, typeId.indexOf(","));
			}
			pccmodel  = publicContactManage.getType(typeId);
			//是否查询
			if(issearch==null||"".equals(issearch)){
				page = publicContactManage.getContactsBytype(page, typeId);
			}else{
				page = publicContactManage.getContactsBycondition(page, typeId, searchname, searchtell, searchemail);
			}
		}else{//第一次进入默认显示类型中的第一条记录的所有人员
			/*pccpage = publicContactManage.getContactsType(pccpage);
			if(pccpage!=null&&pccpage.getTotalCount()>0){
				typeId = pccpage.getResult().get(0).getPccId();
				pccmodel  = publicContactManage.getType(typeId);
				page = publicContactManage.getContactsBytype(page, typeId);
			}*/
			page = publicContactManage.getContactsBytype(page, typeId);
			pccmodel.setPccName("全部联系人");
		}
		return "showList";
	}
	public String exportContacts() throws Exception {
		if(pcId!=null&&!"".equals(pcId)){
			pcList = publicContactManage.findToaPubliccontactByIds(pcId);
		}
	    if(pcList==null||pcList.size()==0){
	    	page.setPageNo(1);
	    	page.setPageNoBak(1);
			page.setPageSize(10000);
			page.setPageSizeBak(10000);
	    	page = publicContactManage.getContactsBytype(page, typeId);
	    	pcList = page.getResult();
	    }
	    if(pcList!=null&&pcList.size()>0){
	    	this.exportfils(pcList);
	    }else{
	    	getResponse().getWriter().write("<script type='text/javascript'>alert('此类别下无数据可导入到Excel。');</script>");
	    }
		return null;
	}
	/**
	 * 导出组织机构信息
	 * @author Administrator蒋国斌
	 * StrongOA2.0_DEV 
	 * 2010-3-16 下午02:58:08 
	 * @param orderlist
	 */
	@SuppressWarnings("unchecked")
	private void exportfils(List<ToaPubliccontact> pcList){
			try{
				HttpServletResponse response=getResponse();
				//创建EXCEL对象
				BaseDataExportInfo export = new BaseDataExportInfo();
				String str=toUtf8String("公共联系人");
				export.setWorkbookFileName(str);
				export.setSheetTitle("公共联系人");
				export.setSheetName("公共联系人");
				//描述行信息
				List<String> tableHead = new ArrayList<String>();
				tableHead.add("姓名");
				tableHead.add("电话");
				tableHead.add("手机号码");
				tableHead.add("Email");
				tableHead.add("职务");
				tableHead.add("类别");
				export.setTableHead(tableHead);
				//获取导出信息
			    List rowList=new ArrayList();
			    Map rowhigh=new HashMap();
			    int rownum=0;
			    for (ToaPubliccontact tpbcc : pcList) {
					Vector cols=new Vector();
					cols.add(tpbcc.getPcName());//姓名
					cols.add(tpbcc.getPcTell());//电话
					cols.add(tpbcc.getPcPhone());//手机号码
					cols.add(tpbcc.getPcEmail());//Email
					cols.add(tpbcc.getPcPost());//职务
					cols.add(tpbcc.getTOaPcCategory().getPccName());//类别
					rowList.add(cols);
					rownum++;
				}
				export.setRowList(rowList);
				export.setRowHigh(rowhigh);
				publicContactProcessXSL xsl = new publicContactProcessXSL();
			    xsl.createWorkBookSheet(export);
			    xsl.writeWorkBook(response);
			}catch(Exception e){
	            e.printStackTrace();
	        }
	}
	 /**
     *  把字符串转成utf8编码，保证中文文件名不会乱码
      * @param s
      * @return
      */
     public static String toUtf8String(String s){
         StringBuffer sb = new StringBuffer();
         for (int i=0;i<s.length();i++){
            char c = s.charAt(i);
            if (c >= 0 && c <= 255){sb.append(c);}
           else{
               byte[] b;
               try { 
            	   b = Character.toString(c).getBytes("utf-8");
        	   }
               catch (Exception ex) {
                    System.out.println(ex);
                    b = new byte[0];
               }
               for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0) k += 256;
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }
	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub

	}
	
	public ToaPubliccontact getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	public Page<ToaPubliccontact> getPage() {
		return page;
	}

	public void setPage(Page<ToaPubliccontact> page) {
		this.page = page;
	}

	public List<ToaPubliccontact> getPcList() {
		return pcList;
	}

	public void setPcList(List<ToaPubliccontact> pcList) {
		this.pcList = pcList;
	}

	public List<ToaPcCategory> getPccList() {
		return pccList;
	}

	public void setPccList(List<ToaPcCategory> pccList) {
		this.pccList = pccList;
	}

	public ToaPcCategory getPccmodel() {
		return pccmodel;
	}

	public void setPccmodel(ToaPcCategory pccmodel) {
		this.pccmodel = pccmodel;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public Page<ToaPcCategory> getPccpage() {
		return pccpage;
	}

	public void setPccpage(Page<ToaPcCategory> pccpage) {
		this.pccpage = pccpage;
	}

	public String getPcId() {
		return pcId;
	}

	public void setPcId(String pcId) {
		this.pcId = pcId;
	}

	public String getSearchname() {
		return searchname;
	}

	public void setSearchname(String searchname) {
		this.searchname = searchname;
	}

	public String getSearchemail() {
		return searchemail;
	}

	public void setSearchemail(String searchemail) {
		this.searchemail = searchemail;
	}

	public String getIssearch() {
		return issearch;
	}

	public void setIssearch(String issearch) {
		this.issearch = issearch;
	}

	public String getSearchtell() {
		return searchtell;
	}

	public void setSearchtell(String searchtell) {
		this.searchtell = searchtell;
	}

	public String getPccNum() {
		return pccNum;
	}

	public void setPccNum(String pccNum) {
		this.pccNum = pccNum;
	}

	public Map<String, String> getTypeMap() {
		return typeMap;
	}

	public void setTypeMap(Map<String, String> typeMap) {
		this.typeMap = typeMap;
	}

	public String getPccName() {
		return pccName;
	}

	public void setPccName(String pccName) {
		this.pccName = pccName;
	}
}
