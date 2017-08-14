/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-13
 * Autour: zhangli
 * Version: V1.0
 * Description： 字典项管理ACTION
 */
package com.strongit.oa.dict.dictItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaSysmanageDict;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.dict.dictType.DictTypeManager;
import com.strongit.oa.util.BaseDataExportInfo;
import com.strongit.oa.util.ProcessXSL;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
public class DictItemAction extends BaseActionSupport {
	/** 分页对象*/
	private Page<ToaSysmanageDictitem> page = new Page<ToaSysmanageDictitem>(FlexTableTag.MAX_ROWS,
			true);

	/** 字典项编号*/
	private String dictItemCode;

	/** 字典类编号*/
	private String dictCode;

	/** 字典项列表*/
	private List dictItemList;
	//需要导入的EXCEL文件
	private File upFile;
	

	/** 可选状态*/
	private HashMap<Integer, String> seltypemap = new HashMap<Integer, String>();

	/** 字典类名称*/
	private String dictName;

	/** 父字典项名称*/
	private String itemParentName;

	/** 字典项对象*/
	private ToaSysmanageDictitem model = new ToaSysmanageDictitem();

	/** 字典项Manager*/
	private DictItemManager manager;

	/** 字典类Manager*/
	private DictTypeManager dictmanager;

	/** 对应字典项值*/
	private String objId;

	/** 对应字典项名称*/
	private String objName;
	
	/** 是*/
	private static final String YES = "1";

	/** 否*/
	private static final String NO = "0";

	/**
	 * @roseuid 494265D302AF
	 */
	public DictItemAction() {
		seltypemap.put(new Integer(NO), "可选");
		seltypemap.put(new Integer(YES), "不可选");
	}

	/**
	 * Access method for the page property.
	 * 
	 * @return the current value of the page property
	 */
	public Page<ToaSysmanageDictitem> getPage() {
		return page;
	}

	/**
	 * Sets the value of the dictItemCode property.
	 * 
	 * @param aDictItemCode
	 *            the new value of the dictItemCode property
	 */
	public void setDictItemCode(java.lang.String aDictItemCode) {
		dictItemCode = aDictItemCode;
	}

	/**
	 * Sets the value of the dictCode property.
	 * 
	 * @param aDictCode
	 *            the new value of the dictCode property
	 */
	public void setDictCode(java.lang.String aDictCode) {
		dictCode = aDictCode;
	}

	public String getDictCode() {
		return dictCode;
	}

	/**
	 * Access method for the dictItemList property.
	 * 
	 * @return the current value of the dictItemList property
	 */
	public java.util.List getDictItemList() {
		return dictItemList;
	}

	/**
	 * Access method for the seltypemap property.
	 * 
	 * @return the current value of the seltypemap property
	 */
	public java.util.HashMap getSeltypemap() {
		return seltypemap;
	}

	/**
	 * Access method for the dictName property.
	 * 
	 * @return the current value of the dictName property
	 */
	public java.lang.String getDictName() {
		return dictName;
	}

	/**
	 * Access method for the itemParentName property.
	 * 
	 * @return the current value of the itemParentName property
	 */
	public java.lang.String getItemParentName() {
		return itemParentName;
	}

	/**
	 * Access method for the model property.
	 * 
	 * @return the current value of the model property
	 */
	public ToaSysmanageDictitem getModel() {
		return model;
	}

	/**
	 * Sets the value of the manager property.
	 * 
	 * @param aManager
	 *            the new value of the manager property
	 */
	@Autowired
	public void setManager(DictItemManager aManager) {
		manager = aManager;
	}

	@Autowired
	public void setDictmanager(DictTypeManager amanager) {
		dictmanager = amanager;
	}

	/**
	 * 获取字典项树
	 * 
	 * @return java.lang.String
	 * @roseuid 4942628F02FD
	 */
	public String tree() throws Exception{
		getRequest().setAttribute("backlocation", 
				getRequest().getContextPath()+"/dict/dictItem/dictItem!tree.action");
		dictItemList = manager.getItemsByDict(dictCode, model);
		dictName = dictmanager.getDictName(dictCode);
		return "tree";
	}

	/**
	 * 获取字典项分页列表
	 * 
	 * @return java.lang.String
	 * @roseuid 49426C9F0261
	 */
	@Override
	public String list() throws Exception{
			getRequest().setAttribute("backlocation", 
					getRequest().getContextPath()+"/fileNameRedirectAction.action?toPage=dict/dictItem/dictItem_init.jsp");		
			page = manager.getItemsByDict(dictCode, page, model);
			return SUCCESS;
	}

	/**
	 * 保存字典项
	 * 
	 * @return java.lang.String
	 * @roseuid 49426C9F0280
	 */
	@Override
	public String save() throws Exception{
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		if ("".equals(model.getDictItemCode()))
			model.setDictItemCode(null);
		addActionMessage(manager.saveDictItem(model, dictCode));
		return "temp";
	}
	/**
	 * 导入EXCEL文件
	 * xush
	 * StrongOA2.0_DEV 
	 * 2010-3-15 下午05:15:08 
	 * @return
	 * @throws Exception
	 */
	public String importdata(){
		 StringBuffer msg = new StringBuffer(); 
		List<List<TUumsBaseOrg>> ooo = new ArrayList<List<TUumsBaseOrg>>(); 
		List<Integer> ttt = new ArrayList<Integer>(); 
		if(upFile!=null){
			FileInputStream fileinput=null;
			try{
				fileinput=new FileInputStream(upFile);
				//String fileType=file.substring(file.lastIndexOf("."));
				HSSFWorkbook templatebook=new HSSFWorkbook(fileinput);// 创建对Excel工作簿文件的引用 
				HSSFSheet sheet=templatebook.getSheetAt(0); // 创建对工作表的引用。
				int rows = sheet.getPhysicalNumberOfRows(); 
				long i=1;
				//获取字典类
				ToaSysmanageDict dictType=dictmanager.getDictType(dictCode);
				
				boolean cks = false;
				for (int r = 2; r < rows; r++) { 
					HSSFRow row = sheet.getRow(r); 
					 if (row != null) { 
						 int cells = row.getPhysicalNumberOfCells(); 
						 StringBuffer strvalue = new StringBuffer(); 
						 for (int c = 0; c < cells; c++) { 
							 HSSFCell cell = row.getCell((short) c);
							 
								 if (cell != null){ 
									 switch (cell.getCellType()) { 
										 case HSSFCell.CELL_TYPE_FORMULA : 
											 break; 
										 case HSSFCell.CELL_TYPE_NUMERIC: 
											 strvalue.append((long)cell.getNumericCellValue()+","); 
											 break; 
										 case HSSFCell.CELL_TYPE_STRING: 
											 strvalue.append(cell.getStringCellValue()+","); 
											 break; 
										 default: 
											 strvalue.append("null,"); 
									 } 
							}	 
						 }
						 String[] valuestr=strvalue.toString().split(",");
						 if(valuestr.length>=4){
							 ToaSysmanageDictitem dictItem=new ToaSysmanageDictitem();
							 //org.setOrgId(null);
							 dictItem.setDictItemValue(valuestr[0]);
							 dictItem.setDictItemName(valuestr[1]);
							 List list =manager.getOtherDictItemByValue(valuestr[0], dictCode,null);
							 if(list != null && list.size() > 0){
								// this.getRequest().setAttribute("msg", "导入失败：该字典项已经存在。");
								 msg.append("第"+(r+1)+"行导入失败：该字典项已经存在。");
								 msg.append("\\n" );
							}else{
								dictItem.setDictItemIsSelect( Integer.parseInt(valuestr[3]));
								dictItem.setDictItemShortdesc(valuestr[2]);
							    dictItem.setToaSysmanageDict(dictType);
							    manager.saveDictItem(dictItem, dictCode);
								
							}
				    }else{
				    	msg.append("第"+(r+1)+"行导入失败：表中项不能为空。");
				    	msg.append("\\n" );
				    }}}
					 }catch(Exception ex){
				ex.printStackTrace();
			this.getRequest().setAttribute("msg", "导入失败：格式有误，请检查。");
			return "importreturn";
			}finally{
				try {
					fileinput.close();
				} catch (IOException e) {
					if(logger.isErrorEnabled()){
						logger.error("文件关闭失败。"+e);
					}
				}
			}
		}
		this.getRequest().setAttribute("msg", msg.toString());
		return "importreturn";
	}
	
   public String moban(){
	    HttpServletResponse response=getResponse();
       
       //创建EXCEL对象
       BaseDataExportInfo export = new BaseDataExportInfo();
       String str=toUtf8String("字典项导入模板");
       export.setWorkbookFileName(str);
       export.setSheetTitle("字典项导入模板");
       export.setSheetName("字典项导入模板");
       //描述行信息
       List<String> tableHead = new ArrayList<String>();
       tableHead.add("字典项值");
		tableHead.add("字典项名称");
		tableHead.add("字典项简称");
		tableHead.add("可选状态");
		export.setTableHead(tableHead);
     //获取导出信息
       List rowList=new ArrayList();
       Map rowhigh=new HashMap();
       export.setRowList(rowList);
       export.setRowHigh(rowhigh);
       ProcessXSL xsl = new ProcessXSL();
       xsl.createWorkBookSheet(export);
       xsl.writeWorkBook(response);
       return null;
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
	/**
	 * 删除字典项
	 * 
	 * @return java.lang.String
	 * @roseuid 49426C9F029F
	 */
	@Override
	public String delete() throws Exception{
		getRequest().setAttribute("backlocation", "dict/dictItem/dictItem.action");
		addActionMessage(manager.deleteDictItem(dictItemCode));
		return "reloads";
	}

	/**
	 * 初始化获取字典项对象
	 * 
	 * @roseuid 49426C9F02CE
	 */
	@Override
	protected void prepareModel() throws Exception{
		if (dictItemCode != null) {
			model = manager.getDictItem(dictItemCode);
		} else {
			model = new ToaSysmanageDictitem();
		}
	}

	/**
	 * 初始化输入框
	 */
	@Override
	public String input() throws Exception{
		// TODO Auto-generated method stub
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		dictName = dictmanager.getDictName(dictCode);
		prepareModel();
		if (YES.equals(model.getDictItemIsSystem())) {
			addActionMessage(model.getDictItemName() + "是系统字典项，不可修改！");
			return "reloads1";
		}
		itemParentName = manager.getDictItemParentName(dictItemCode);
		return INPUT;
	}

	public File getUpFile() {
		return upFile;
	}

	public void setUpFile(File upFile) {
		this.upFile = upFile;
	}
	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}
}
