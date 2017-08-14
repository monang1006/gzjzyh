package com.strongit.uums.organisemanage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.components.ActionMessage;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.mylog.MyLogManager;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.util.BaseDataExportInfo;
import com.strongit.oa.util.ProcessXSL;
import com.strongit.oa.util.TempPo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseAreacode;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBasePost;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.uums.security.UserInfo;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongit.oa.common.user.util.TimeKit;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "orgmanage!input.action", type = ServletActionRedirectResult.class),
			@Result(name = "bigtree", value = "/WEB-INF/jsp/tree/bigtree.jsp", type = ServletDispatcherResult.class)
		})
public class OrgmanageAction extends BaseActionSupport<TUumsBaseOrg> {
	private Page<TUumsBaseOrg> page = new Page<TUumsBaseOrg>(FlexTableTag.MAX_ROWS, true);

	private Page<TUumsBasePost> pageWholePost = new Page<TUumsBasePost>(
			FlexTableTag.MAX_ROWS, true);

	private String orgId;
    private String orgId1;
	private String postId;

	private String postName = "";

	private String codeType;

	private String orgSysCode;

	private String orgPostId = "";

	private String audittype;

	private String userName = "";
	
	private String rest2name = "";//分管领导
	
	private String rest4name = "";//部门分管厅领导

	private List<TUumsBaseOrg> orgList;

	private List<TUumsBasePost> postList;

	private List<TUumsBaseAreacode> areaCodeList;

	private List<TUumsBaseUser> userList;

	private TUumsBaseOrg model = new TUumsBaseOrg();

	private MyLogManager myLogManager;

	private String orgcode;

	private String message;
	
	//用户是否同时被还原标识
	private String userTogether;
	
	//组织机构所属区划代码名称
	private String areacodeName;
	
	//组织机构所属区划代码编码
	private String areacodeCode;
	
	//区划代码编码，用于区划代码树的延迟加载
	private String areacode;
	
	//当修改组织机构编码时，判断是否需要同时修改其下级组织机构编码
	private String needChildren;
	
	//组织机构被修改前的编码
	private String iscode;
	//需要导入的EXCEL文件
	private File upFile;
	
	private String superCode;
	
	private int topOrgcodelength;//构造组织机构树时顶级节点的编码长度
	
	private String IssuePeople; //签发领导
	
	private String editor; //责任编辑
	
	@Autowired IUserService userService;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getOrgcode() {
		return orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}
	
	 
	/**
	 * 导入EXCEL文件
	 * @author Administrator蒋国斌
	 * StrongOA2.0_DEV 
	 * 2010-3-15 下午05:15:08 
	 * @return
	 * @throws Exception
	 */
	public String importdata(){
		/**用户存放导入数据
		 *  同一机构编码长度的  放在一个list里面
		 *  之后进行整理后保存
		 */
		List<List<TUumsBaseOrg>> ooo = new ArrayList<List<TUumsBaseOrg>>(); 
		/**	用户存放导入数据中构编码长度
		 *  同一长度的  放在一个list里面
		 *  为后面整理之用
		 */
		List<Integer> ttt = new ArrayList<Integer>(); 
		
		String orgId=getRequest().getParameter("orgId");
		if(orgId==null || "".equals(orgId)){
			this.getRequest().setAttribute("msg", "导入失败：请先从左边树中选择要导入机构的位置。");
			return "importreturn";
		}
		if(upFile!=null){
			FileInputStream fileinput=null;
			try{
				fileinput=new FileInputStream(upFile);
				//String fileType=file.substring(file.lastIndexOf("."));
				HSSFWorkbook templatebook=new HSSFWorkbook(fileinput);// 创建对Excel工作簿文件的引用 
				HSSFSheet sheet=templatebook.getSheetAt(0); // 创建对工作表的引用。
				int rows = sheet.getPhysicalNumberOfRows(); 
				long i=1;
				//获取父节点对象
				TUumsBaseOrg parentOrg=userService.getOrgInfoByOrgId(orgId);
				//获取父节点下的编码
				String nextCode = "";
				//boolean idDel = false;
				//if(parentOrg==null||"".equals(parentOrg)||"001".equals(parentOrg.getOrgSyscode())){
				//nextCode = "001";
				//	idDel = true;
				//}else{
					nextCode = userService.getNextOrgCode(parentOrg.getOrgSyscode());
				//}
				boolean cks = false;
				for (int r = 2; r < rows; r++) { 
					HSSFRow row = sheet.getRow(r); 
					 if (row != null) { 
						 int cells = row.getPhysicalNumberOfCells(); 
						 StringBuffer strvalue = new StringBuffer(); 
						 for (int c = 0; c < cells; c++) { 
							 HSSFCell cell = row.getCell((short) c);
							 if(c==8){
								 strvalue.append(TimeKit.formatDate(cell.getDateCellValue(), "yyyy-MM-dd"));
							 }else{
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
						 }
						 String[] valuestr=strvalue.toString().split(",");
						 if(valuestr.length>=2){
						  i=parentOrg.getOrgSequence()+(long)Integer.parseInt(nextCode.substring(nextCode.length()-3));
						  TUumsBaseOrg org=new TUumsBaseOrg();
						     org.setIsOrg("0");
							 //org.setOrgId(null);
							 org.setOrgSyscode(valuestr[0]);
							 if("001".equals(valuestr[0])){
								 cks = true;
							 }
							 if("".equals(valuestr[0])||valuestr[0] ==null||"null".equals(valuestr[0])||!isNum(valuestr[0])){
								 this.getRequest().setAttribute("msg", "导入失败：机构编号格式有误或者数据为空，请检查。");
								 return "importreturn";
							 }
							 if(valuestr[0].length()>15){
								 this.getRequest().setAttribute("msg", "导入失败：机构编号长度过长，只能导入5级层级机构的人员，请检查。");
								 return "importreturn";
							 }
							 org.setOrgCode(valuestr[0]);
							 org.setOrgName(valuestr[1]);
							 if(valuestr.length>5){
								 System.out.println(valuestr[5]);
								 if(!"".equals(valuestr[5])&&valuestr[5] !=null&&!"null".equals(valuestr[5])&&isNum(valuestr[5])){
									 org.setOrgSequence(Long.valueOf(valuestr[5]));
								 }else{
									 org.setOrgSequence(new Long(i));
								 }
							 }else{
								 org.setOrgSequence(new Long(i));
							 }
							 if(valuestr.length>4){
								 if("部门".equals(valuestr[4])){
								     org.setIsOrg("0");
								 }else{
								     org.setIsOrg("1");
								 }
							 }
							 if(valuestr.length>=3){
								 if(valuestr.length>3){
									 if(!"null".equals(valuestr[3])&&!"".equals(valuestr[3])){
										 org.setOrgAddr(valuestr[3]);
									 }else{
										 org.setOrgAddr("");  
									 }
								 }
								 if(!"null".equals(valuestr[2])&&!"".equals(valuestr[2])){
								   org.setOrgTel(valuestr[2]);
								 }else{
								     org.setOrgTel("");
								 }
							 }
							 org.setOrgIsdel("0");
							 int l=0;
							 if(ttt!=null&&ttt.size()>0){
								 for(int k=0;k<ttt.size();k++){
									 int mmm = valuestr[0].length();
									 if(ttt.get(k)==mmm){
										ooo.get(k).add(org); 
									 }else{
										 l++;
									 }
								 }
								 if(l==ttt.size()){
									 ttt.add(valuestr[0].length());
									 List<TUumsBaseOrg> lstst = new ArrayList<TUumsBaseOrg>();
									 lstst.add(org);
									 ooo.add(ttt.size()-1,lstst);
								 }
							 }else{
								 ttt.add(0,valuestr[0].length());
								 List<TUumsBaseOrg> lstst = new ArrayList<TUumsBaseOrg>();
								 lstst.add(org);
								 ooo.add(0,lstst);
							 }
//							 manager.saveOrg(org);
						   }
					 }
				}
				if(ooo.size()==0||ooo==null){
					this.getRequest().setAttribute("msg", "导入失败：格式有误，请检查。");
					return "importreturn";
				}
				//if(idDel){
					//最高级导入  删除原有记录  物理删除
				//	userService.delOrgByCode(nextCode);
				//}
				if("001".equals(nextCode)){
					if(!cks){
						nextCode = userService.getNextOrgCode("001");
					}
				}
				for(int m = 0;m<ttt.size();m++){
					for(int n = m+1;n<ttt.size();n++){
						if(m>n){
							int temp = m;
							ttt.set(m, ttt.get(n));
							ttt.set(n, temp);
							
							List<TUumsBaseOrg> list = ooo.get(m); 
							ooo.set(m, ooo.get(n));
							ooo.set(n, list);
						}
					}
				}
				StringBuffer sb = new StringBuffer();
				for(int j =0;j<ooo.size()-1;j++){
					int kk = 0;
					for(TUumsBaseOrg mmm:ooo.get(j)){
						String code = mmm.getOrgCode();
						if(j==0){
							mmm.setOrgSyscode(nextCode(nextCode,kk++));
						}
						String yyy = userService.getNextOrgCode(mmm.getOrgSyscode());
						int fc=0;
						for(int k=0;k<ooo.get(j+1).size();k++){
							if(code.equals(ooo.get(j+1).get(k).getOrgSyscode().substring(0, 
									(code.length())))){
								if("1".equals(mmm.getIsOrg())){
									ooo.get(j+1).get(k).setOrgParentId(mmm.getOrgId());
									ooo.get(j+1).get(k).setOrgSyscode(nextCode(yyy,fc));
									fc++;
								}else{//部门机构下数据不导入
//									sb.append(ooo.get(j+1).get(k).getOrgName()+"；");
//									ooo.get(j+1).remove(k);
//									k=-1;
									ooo.get(j).get(k).setIsOrg("1");
									ooo.get(j+1).get(k).setOrgParentId(mmm.getOrgId());
									ooo.get(j+1).get(k).setOrgSyscode(nextCode(yyy,fc));
									fc++;
								}
							}
						}
					}
				}
				if(ooo.size()==1){
					int mm = 0;
					for(TUumsBaseOrg mmm:ooo.get(0)){
						mmm.setOrgSyscode(nextCode(nextCode,mm));
						mm++;
					}
				}
				for(List<TUumsBaseOrg> list:ooo){
					for(TUumsBaseOrg tjtj : list){
						//001最高级  不保存
						if("001".equals(tjtj.getOrgSyscode())){
						}else{
							tjtj.setOrgCode(tjtj.getOrgSyscode());
							userService.saveOrg(tjtj);
						}
					}
				}
				//日志信息
				String ip = getRequest().getRemoteAddr();
				String logInfo = "";			
				logInfo = "导入了组织机构";					
				this.myLogManager.addLog(logInfo, ip);
				if(sb!=null&&sb.length()>0){
					this.getRequest().setAttribute("smsg",sb.toString()+"上级为部门没有导入。");
				}
			}
			catch(Exception ex){
				ex.printStackTrace();
			this.getRequest().setAttribute("msg", "导入失败：格式有误或者数据已经存在，请检查。");
			    //return null;
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
		return "importreturn";
	}
	
	public String nextCode(String value,int tjtj){
		if(value!=null&&!"".equals(value)){
			if(value.length()>=6){
				String str = value.substring(value.length()-6,value.length());
				int t = Integer.parseInt(str)+tjtj;
				String sss = String.valueOf(t);
				String mmm = sss;
				for(int i=0;i<str.length()-sss.length();i++){
					mmm = "0"+mmm;
				}
				return value.substring(0,value.length()-6)+mmm;
			}else{
				String str = value.substring(value.length()-3,value.length());
				int t = Integer.parseInt(str)+tjtj;
				String sss = String.valueOf(t);
				String mmm = sss;
				for(int i=0;i<str.length()-sss.length();i++){
					mmm = "0"+mmm;
				}
				return value.substring(0,value.length()-3)+mmm;
			}
		}
		return "";
	}
	public boolean isNum(String value){
		for(int i=value.length();--i>=0;){
	      int chr=value.charAt(i);
	      if(chr<48 || chr>57)
	         return false;
		}
		return true;
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
     
	public String moban(){
	    HttpServletResponse response=getResponse();
        
        //创建EXCEL对象
        BaseDataExportInfo export = new BaseDataExportInfo();
        String str=toUtf8String("组织机构导入模板");
        export.setWorkbookFileName(str);
        export.setSheetTitle("组织机构导入模板");
        export.setSheetName("组织机构导入模板");
        //描述行信息
        List<String> tableHead = new ArrayList<String>();
        tableHead.add("机构编号");
		tableHead.add("机构名称");
		tableHead.add("机构电话");
		tableHead.add("机构地址");
		tableHead.add("机构类型");
		tableHead.add("排序序号");
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
	 * 修改bug 0000050958
	 * author  taoji
	 * @return 
	 * @throws IOException
	 * @date 2014-3-24 下午08:19:39
	 */
	public String downMoban() throws IOException{
		HttpServletResponse response = super.getResponse();
		response.reset();
		response.setContentType("application/x-download"); // windows
		OutputStream output = null;
		HttpServletRequest req = getRequest();
		
		String filePath = getRequest().getRealPath("")+File.separator+"orgTemp.xls";
		 byte[] buffer = null;  
		File file = new File(filePath);  
        FileInputStream fis = new FileInputStream(file); 
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
        byte[] b = new byte[1000];  
        int n;  
        while ((n = fis.read(b)) != -1) {  
            bos.write(b, 0, n);  
        }  
        fis.close();  
        bos.close();  
        buffer = bos.toByteArray();
		try {
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String("组织机构信息.xls".getBytes("gb2312"), "iso8859-1"));
			output = response.getOutputStream();
			output.write(buffer);
			output.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					if (logger.isErrorEnabled()) {
						logger.error(e.getMessage(), e);
					}
				}
				output = null;
			}
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
	private void exportfils(List<TUumsBaseOrg> orglist){
			try{
				HttpServletResponse response=getResponse();
				//创建EXCEL对象
				BaseDataExportInfo export = new BaseDataExportInfo();
				String str=toUtf8String("组织机构信息");
				export.setWorkbookFileName(str);
				export.setSheetTitle("组织机构信息");
				export.setSheetName("组织机构信息");
				//描述行信息
				List<String> tableHead = new ArrayList<String>();
				tableHead.add("机构编号");
				tableHead.add("机构名称");
				tableHead.add("机构电话");
				tableHead.add("机构地址");
				tableHead.add("机构类型");
				tableHead.add("排序序号");
				export.setTableHead(tableHead);
				//获取导出信息
			    List rowList=new ArrayList();
			    Map rowhigh=new HashMap();
			    int rownum=0;
			    String nowdate=TimeKit.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
			    for (TUumsBaseOrg org : orglist) {
					Vector cols=new Vector();
					cols.add(org.getOrgSyscode());//机构编号
					cols.add(org.getOrgName());//机构名称
					cols.add(org.getOrgTel());//单位电话
					cols.add(org.getOrgAddr());//单位地址
//					cols.add(org.getIsOrg());//机构类型
					if("0".equals(org.getIsOrg())){
						cols.add("部门");
					 }else{
						cols.add("机构");
					 }
					System.out.println(String.valueOf(org.getOrgSequence()));
					cols.add(String.valueOf(org.getOrgSequence()));//排序序号
					String ordercodeStr="";
					rowList.add(cols);
					rownum++;
				}
				export.setRowList(rowList);
				export.setRowHigh(rowhigh);
				ProcessXSL xsl = new ProcessXSL();
			    xsl.createWorkBookSheet(export);
			    xsl.writeWorkBook(response);
			}catch(Exception e){
	            e.printStackTrace();
	        }
	}
	
/**
 * 导出数据
 * @author Administrator蒋国斌
 * StrongOA2.0_DEV 
 * 2010-3-16 下午03:17:17 
 * @return
 * @throws Exception
 */
	public String exportExcels() throws Exception {
		//List<TUumsBaseOrg> orglist=manager.getAllOrgInfos();
//	    List<TUumsBaseOrg> orglist=manager.queryOrgs("0");
	    List<TUumsBaseOrg> orglist=userService.getOrg(orgSysCode);
	    if(orglist==null||orglist.size()==0){
	    	orglist=userService.getOrg("001");
	    }
	    //tj  废了 但不想删
	    /*List<TUumsBaseOrg> orglist=oMmanager.xiha();
	    List<List<TUumsBaseOrg>> ooo = new ArrayList<List<TUumsBaseOrg>>();
	    List<Object[]> list=oMmanager.xiha1();
	    for(int i=0;i<list.size();i++){
	    	List<TUumsBaseOrg> listsss = oMmanager.getXiha1(list.get(i)[0].toString());
	    	if(listsss!=null&&listsss.size()>0){
	    		for(int j=0;j<listsss.size();j++){
		    		for(int k=j+1;k<listsss.size();k++){
			    		if(listsss.get(j).getOrgSequence()>listsss.get(k).getOrgSequence()){
			    			TUumsBaseOrg tempsss = listsss.get(j); 
			    			listsss.set(j, listsss.get(k));
			    			listsss.set(k, tempsss);
			    		}
			    	}
		    	}
		    	ooo.add(i, listsss);
	    	}
	    }
	    List<TUumsBaseOrg> temp = new ArrayList<TUumsBaseOrg>();
	    temp.addAll(xiha(ooo,0,list.size(),"001"));*/
	    if(orglist!=null&&orglist.size()>0){
	    	this.exportfils(orglist);
	    }else{
	    	return renderHtml("<script language=\"javascript\">alert(\"请先点击要导出的部门或机构。\")</script>");
	    }
		return null;
	}
	
	/**
	 * 
	 * author  taoji
	 * @param temp1
	 * @param ooo
	 * @param length1
	 * @param length
	 * @param orgSyscode
	 * @return
	 * @date 2013-12-4 下午02:48:35
	 */
	public List<TUumsBaseOrg> xiha(List<List<TUumsBaseOrg>> ooo,int length1,int length,String orgSyscode){
		List<TUumsBaseOrg> temp = new ArrayList<TUumsBaseOrg>();
		for(TUumsBaseOrg mmm : ooo.get(length1)){
			if(mmm.getOrgSyscode().length()==3||
					mmm.getOrgSyscode().substring(0, orgSyscode.length()).equals(orgSyscode)){
				temp.add(mmm);
				if(length1<length-1){
					List<TUumsBaseOrg> xiha = xiha(ooo, length1+1, length,mmm.getOrgSyscode());
					temp.addAll(xiha);
				}
			}
		}
		return temp;
	}
	
	/**
	 * 删除组织机构
	 */
	@Override
	public String delete() throws Exception {
		try {
			TUumsBaseOrg org = userService.getOrgInfoByOrgId(orgId);
			if("001".equals(org.getOrgCode())){

				addActionMessage("不能删除顶级机构");
			}else{
				userService.deleteOrgs(orgId);
				addActionMessage("删除成功");
				//日志信息
				String ip = getRequest().getRemoteAddr();
				String logInfo = "";			
				logInfo = "删除了组织机构:" + org.getOrgName();						
				this.myLogManager.addLog(logInfo, ip);
			}
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			addActionMessage(e.getMessage());
		}
		//在左侧树形列表里面操作
		if ("1".equals(audittype)) {
			return renderHtml("<script>window.location='"
					+ getRequest().getContextPath()
					+ "/organisemanage/orgmanage!tree.action';"
							+ "parent.organiseInfo.location='"
							+ getRequest().getContextPath()
							+ "/fileNameRedirectAction.action?toPage=/organisemanage/orgmanage-container.jsp?orgId="
							+orgId
							+ "'; </script>");
		}
		//在右侧主界面里面操作
		return renderHtml("<script>window.location='"
				+ getRequest().getContextPath()
				+ "/organisemanage/orgmanage!input.action?orgId="
							+ orgId + "'; </script>");
	}

	/**
	 * 获取组织机构
	 */
	@Override
	public String input() throws Exception {
		//prepareModel();
		String rest3 = model.getRest3();
		if((rest3!=null)&&(!"".equals(rest3))){
		String[] r3 = rest3.split(",");
		IssuePeople = r3[0].trim();
		editor = r3[1].trim();
		}
		return INPUT;
	}

	/**
	 * 获取组织机构page页
	 */
	@Override
	public String list() throws Exception {
		page = userService.queryOrgs(page, "2");
		return SUCCESS;
	}

	/**
	 * 获取组织机构
	 */
	@Override
	protected void prepareModel() throws Exception {
		try {
			if (orgId != null && !"".equals(orgId) && !"null".equals(orgId)) {
				model = userService.getOrgInfoByOrgId(orgId);
				if (!"".equals(model.getOrgManager())
						&& model.getOrgManager() != null) {
					/*TUumsBaseUser user = userManager.getUserInfoByUserId(model.getOrgManager());
					if(user != null){
						userName = user.getUserName();
					}*/
					//改为支持多个负责人的情况
					String id = model.getOrgManager();
					String[] ids = id.split(",");
					StringBuilder names = new StringBuilder();
					if(ids.length <= 10){
						for(String uid : ids) {
							if(uid != null) {
								uid = uid.substring(1);
								String f=userService.getUserInfoByUserId(uid).getUserIsdel();
								if(!f.equals("1"))//判断负责人是否为删除状态
								{
									String name = userService.getUserNameByUserId(uid);
									names.append(name).append(",");
								}
							}
						}
						if(names.length() > 0) {
							names.deleteCharAt(names.length() - 1);
						}
						userName = names.toString();
					} else {
						List<User> userList = userService.getAllUserInfo();
						Map<String, String> userMap = new LinkedHashMap<String, String>();
						for(User u : userList) {
							userMap.put(u.getUserId(), u.getUserName());
						}
						for(String uid : ids) {
							if(uid != null) {
								uid = uid.substring(1);
								String name = userMap.get(uid);
								names.append(name).append(",");
							}
						}
						if(names.length() > 0) {
							names.deleteCharAt(names.length() - 1);
						}
						userName = names.toString();
						userMap.clear();
						userList.clear();
					}
					//----------END-------------
				}
				
				if(model.getRest2() != null && !model.getRest2().equals("")){//Rest2对应分管领导的用户ID
					String id = model.getRest2();
					String[] ids = id.split(",");
					StringBuilder names = new StringBuilder();
					if(ids.length <= 10){
						for(String uid : ids) {
							if(uid != null) {
								uid = uid.substring(1);
								String f=userService.getUserInfoByUserId(uid).getUserIsdel();
								if(!f.equals("1"))//判断分管领导是否为删除状态
								{
									String name = userService.getUserNameByUserId(uid);
									names.append(name).append(",");
								}
							}
						}
						if(names.length() > 0) {
							names.deleteCharAt(names.length() - 1);
						}
						rest2name = names.toString();
					} else {
						List<User> userList = userService.getAllUserInfo();
						Map<String, String> userMap = new LinkedHashMap<String, String>();
						for(User u : userList) {
							userMap.put(u.getUserId(), u.getUserName());
						}
						for(String uid : ids) {
							if(uid != null) {
								uid = uid.substring(1);
								
								String name = userMap.get(uid);
								names.append(name).append(",");
								
							}
						}
						if(names.length() > 0) {
							names.deleteCharAt(names.length() - 1);
						}
						rest2name = names.toString();
						userMap.clear();
						userList.clear();
					}
				}
				if(model.getRest4() != null && !model.getRest4().equals("")){//Rest4对应部门分管厅领导的用户ID
					String id = model.getRest4();
					String[] ids = id.split(",");
					StringBuilder names = new StringBuilder();
					if(ids.length <= 10){
						for(String uid : ids) {
							if(uid != null) {
								uid = uid.substring(1);
								String f=userService.getUserInfoByUserId(uid).getUserIsdel();
								if(!f.equals("1"))//判断分管厅领导的用户是否为删除状态
								{
									String name = userService.getUserNameByUserId(uid);
									names.append(name).append(",");
								}
							}
						}
						if(names.length() > 0) {
							names.deleteCharAt(names.length() - 1);
						}
						rest4name = names.toString();
					} else {
						List<User> userList = userService.getAllUserInfo();
						Map<String, String> userMap = new LinkedHashMap<String, String>();
						for(User u : userList) {
							userMap.put(u.getUserId(), u.getUserName());
						}
						for(String uid : ids) {
							if(uid != null) {
								uid = uid.substring(1);
								String name = userMap.get(uid);
								names.append(name).append(",");
							}
						}
						if(names.length() > 0) {
							names.deleteCharAt(names.length() - 1);
						}
						rest4name = names.toString();
						userMap.clear();
						userList.clear();
					}
				}
				if(model.getOrgAreaCode() != null && !"".equals(model.getOrgAreaCode())){
					TUumsBaseAreacode areaCode = userService.getAreaInfoByAreaCode(model.getOrgAreaCode());
					areacodeName = areaCode.getAcName();
				}
				//为了保证Ldap与数据库的事务问题，将组织机构与岗位关联的动作分开处理
				if ("get".equals(audittype)) {
					List<TUumsBasePost> postLst = userService.getPostInfoByOrgId(orgId);
					postName = "";
					orgPostId = "";
					if (postLst.size() > 0) {
						for (Iterator it = postLst.iterator(); it.hasNext();) {
							TUumsBasePost o = (TUumsBasePost) it.next();
							orgPostId += ',' + o.getPostId();
							postName += ',' + o.getPostName();
						}
					}
					if (orgPostId.length() > 0) {
						orgPostId = orgPostId.substring(1);
					}
					if (postName.length() > 0) {
						postName = postName.substring(1);
					}
				}
			} else {
				model = new TUumsBaseOrg();
				Long nextSequence = userService.getNextOrgSequenceCode();
				model.setOrgSequence(nextSequence);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 保存组织机构（将组织机构与岗位关联操作分离出去）
	 */
	@Override
	public String save() throws Exception {
		String flag = "edit";
		if ("".equals(model.getOrgId())){
			flag = "add";
			model.setOrgId(null);			
		}
		if("1".equals(this.needChildren)&&!"".equals(model.getOrgId())&&null!=model.getOrgId()){
			TUumsBaseOrg oldorg = userService.getOrgInfoByOrgId(model.getOrgId());
			String isOrg = "";
			if(oldorg.getIsOrg().equals(model.getIsOrg())){
			}else{
				//机构变部门
				if(Const.IS_YES.equals(oldorg.getIsOrg()) && Const.IS_NO.equals(model.getIsOrg())){
					isOrg = "1";
				//部门变机构
				}else if(Const.IS_NO.equals(oldorg.getIsOrg()) && Const.IS_YES.equals(model.getIsOrg())){
					isOrg = "0";
				}
			}
			userService.saveOrgAndChildren(model, iscode,isOrg);
		}else{
			userService.saveOrg(model);
		}
		//添加日志信息
		String ip = getRequest().getRemoteAddr();
		String logInfo = "";
		if(flag.equals("add")){
			logInfo = "添加了组织机构：“" + model.getOrgName()+"”";			
		}else{
			logInfo = "编辑了组织机构：“" +  model.getOrgName()+"”";			
		}
		this.myLogManager.addLog(logInfo, ip);
		addActionMessage("reload");
		return INPUT;
//		return renderHtml("<script>" + "window.location='"
//				+ getRequest().getContextPath()
//				+ "/organisemanage/orgmanage!addchild.action'; "
//				+ " parent.parent.organiseTree.location='"
//				+ getRequest().getContextPath()
//				+ "/organisemanage/orgmanage!tree.action'; </script>");
	}
	
	/**
	 * 初始化组织机构关联的岗位信息
	 * @author 喻斌
	 * @date Jun 22, 2009 4:54:53 PM
	 * @return
	 * @throws Exception
	 */
	public String initSelectOrgPost() throws Exception {
		prepareModel();
		return "selectpost";
	}
	
	/**
	 * 保存与组织机构关联的岗位信息
	 * @author 喻斌
	 * @date Jun 17, 2009 3:51:12 PM
	 * @return
	 * @throws Exception
	 */
	public String saveOrgPost() throws Exception {
		if ("".equals(model.getOrgId()))
			model.setOrgId(null);
		userService.saveOrgPost(orgId, orgPostId);
		addActionMessage("保存成功");
		return "windowclose";
	}
	
	/**
	 * 获取全局岗位page页
	 * 
	 * @return
	 * @throws Exception
	 */
	public String setPost() throws Exception {
		pageWholePost = userService.queryPosts(pageWholePost);
		return "setpost";
	}

	/**
	 * 获取全局岗位列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getWholePostList() throws Exception {
		postList = userService.getAllPostInfos();
		return "posttree";
	}

	/**
	 * 获取机构下已经有的岗位 postId 已有岗位ID=id1,id2,id3.... postName
	 * 已有岗位name=name1,name2,name3.....
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getPost() throws Exception {
		postList = userService.getPostInfosByPostIds(postId);
		postId = "";
		postName = "";
		for (int i = 0; i < postList.size(); i++) {
			TUumsBasePost wholepost = postList.get(i);
			postId += "," + wholepost.getPostId();
			postName += "," + wholepost.getPostName();
		}
		if (postId.length() > 0) {
			postId = postId.substring(1);
		}
		if (postName.length() > 0) {
			postName = postName.substring(1);
		}
		return "initpost";
	}

	/**
	 * 获取组织机构树
	 * 
	 * @return
	 * @throws IOException
	 */
	public String tree() throws IOException {
		UserInfo userInfo = userService.getCurrentUserInfo();
		String userId = userInfo.getUserId();
		TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(userService.getCurrentUserInfo().getUserId());
		if(userService.isSystemDataManager(userId)) {
			orgList = userService.getAllOrgInfos();			
		} else {
			orgList = userService.getAllOrgInfosByHa(userInfo.getOrgId());
		}
		superCode=org.getSupOrgCode();
		topOrgcodelength = userService.findTopOrgCodeLength();
		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		return "tree";
	}

	/**
	 * 获取增加组织机构时设置下一个系统编码
	 * 
	 * @return
	 * @throws IOException
	 */
	public String addchild() throws Exception {
		this.prepareModel();
		if (!Const.IS_YES.equals(model.getOrgIsdel())) {
			//采用分级授权方式
			if(orgSysCode == null || "".equals(orgSysCode) || orgId == null || "".equals(orgId)){
				orgSysCode = userService.getCurrentUserInfo().getSupOrgCode();
				
				//统一用户升级修改 modify by luosy 2014-01-18
				orgSysCode = orgSysCode.replaceAll(",", "");
			}
			String nextCode = userService.getNextOrgCode(orgSysCode);
			//组织机构已达到编码的最底层
			if(nextCode.equals(orgSysCode) && orgId != null && !"".equals(orgId)){
				return this
				.renderHtml("<script>alert('组织机构已达最底层，无法添加。');location='"
						+ this.getRequest().getContextPath()
						+ "/organisemanage/orgmanage!input.action?orgId="
						+ orgId + "&audittype=get'</script>");
			}
			Long nextSequence = userService.getNextOrgSequenceCode();
			model = new TUumsBaseOrg();
			model.setOrgSyscode(nextCode);
			model.setOrgSequence(nextSequence);
			model.setIsOrg("");	 //郑志斌添加 2010-12-20 添加新的组织机构时，机构类型默认为""
			model.setOrgManager(null); //申仪玲添加 2011-11-22 添加新的组织机构时，机构负责人默认为null
			return INPUT;
		} else {
			return this
					.renderHtml("<script>alert('该组织结构已经被删除，无法进行该操作。');location='"
							+ this.getRequest().getContextPath()
							+ "/organisemanage/orgmanage!input.action?orgId="
							+ orgId + "&audittype=get'</script>");
		}
		
	}

	/**
	 * 获取区划代码列表
	 * 
	 * @return
	 * @throws IOException
	 */
	public String selectArea() throws IOException {
		areaCodeList = userService.getAllAreaCodes();
		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_AREA);
		return "areatree";
	}
	
	/**
	 * 得到子级区划代码节点的jason信息，用于ajax延迟加载
	 * @author 喻斌
	 * @date Jul 10, 2009 1:15:47 PM
	 * @return
	 * @throws Exception
	 */
	public String lazyLoadArea() throws Exception {
		List<TUumsBaseAreacode> areaLst = userService.getChildAreaCodes(areacode);
		String jasonStr = new String("[");
		if(areaLst != null && !areaLst.isEmpty()){
			StringBuffer bufStr = new StringBuffer("");
			for(TUumsBaseAreacode areacode : areaLst){
				bufStr.append(",{id:'")
						.append(areacode.getAcId())
						.append("',name:'")
						.append(areacode.getAcName())
						.append("',code:'")
						.append(areacode.getAcCode())
						.append("'}");
			}
			jasonStr = jasonStr + bufStr.substring(1);
		}
		jasonStr = jasonStr + "]";
		this.renderText(jasonStr);
		return null;
	}

	public String selectUser() throws IOException {
		String userId = userService.getCurrentUserInfo().getUserId();
		if(userService.isSystemDataManager(userId)) {
			int i = userService.findChildCodeLength(Const.RULE_CODE_ORG, null);
			String random = "%";
			orgList = userService.getOrgsByOrgSyscode(random, Const.IS_NO);
			for(int j=0; j<orgList.size();){
				if(orgList.get(j).getOrgSyscode().length() != i){
					orgList.remove(j);
				}else{
					j++;
				}
			}
		} else {
			String random = "%";
			TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(userService.getCurrentUserInfo().getUserId());
			String codes=org.getOrgSyscode();
			orgList = userService.getOrgsByOrgSyscodeByHa(random, Const.IS_NO,userService.getCurrentUserInfo().getOrgId());
			for(int j=0; j<orgList.size();){
				if(!orgList.get(j).getOrgSyscode().equals(codes)){
					orgList.remove(j);
				}else{
					j++;
				}
			}			
		}
		return "usertree";
	}

	/**
	 * 选择机构负责人,支持多选.
	 * @author:邓志城
	 * @date:2011-1-6 下午04:16:31
	 * @return
	 */
	public String chooseOrgManager() {
		List<TempPo> nodes = new LinkedList<TempPo>();
		try {
			List<Organization> orgList = userService.getAllDeparments();//得到机构列表
			Map<String,List<User>> userMap = userService.getUserMap();
			List<Organization> newList = new ArrayList<Organization>(orgList);
			Collections.sort(newList, new Comparator<Organization>(){
				public int compare(Organization o1, Organization o2) {
					String code1 = o1.getOrgSyscode();
					String code2 = o2.getOrgSyscode();
					if(code1 != null && code2 != null) {
						return code1.length() - code2.length();
					}
					return 0;
				}
			});
			String root = null;
			if(!newList.isEmpty()) {
				root = newList.get(0).getOrgSyscode();
			}
			for(int i=0;i<orgList.size();i++) {
				Organization organization = orgList.get(i);
				TempPo po = new TempPo();
				po.setId("o"+organization.getOrgId());
				po.setName(organization.getOrgName());
				String parentId = organization.getOrgParentId();
				po.setType("o");
				/*if(i == 0){//机构列表根据编码排序，第一个一定是根节点
					parentId = "0";
				} else {
					parentId = "o" + parentId;
				}*/
				if(organization.getOrgSyscode().equals(root)) {
					parentId = "0";
				} else {
					parentId = parentId == null ? "0" : "o" + parentId;
				}
				po.setParentId(parentId);
				nodes.add(po);
				//添加人员
				List<User> userList = userMap.get(organization.getOrgId());
				if(userList != null) {
					for(User u : userList) {
						TempPo upo = new TempPo();
						upo.setId("u"+u.getUserId());
						upo.setName(u.getUserName());
						upo.setParentId(po.getId());
						upo.setType("u");
						nodes.add(upo);	
					}
				}
			}
			userMap.clear();
			orgList.clear();
		}catch (Exception e) {
			logger.error("选择机构负责人发生异常", e);
		} 
		getRequest().setAttribute("data", nodes);
		getRequest().setAttribute("id", getRequest().getParameter("id"));
		return "bigtree";
	}
	
	/**
	 * 组织机构上移
	 * 
	 * @author 喻斌
	 * @date Feb 11, 2009 9:49:47 AM
	 * @return
	 * @throws Exception
	 */
	public String moveUp() throws Exception {
		try {
			prepareModel();
			//采用分级授权方式
			UserInfo userInfo = userService.getCurrentUserInfo();
			String mes = userService
					.moveOrgPositionInTreeByHa(model, Const.MOVE_UP_IN_TREE, userInfo.getOrgId());
			if(mes.equals("该节点已在最上层，无法上移")){
				mes = "该节点已在最上层，无法上移。";
			}
			if (!"".equals(mes)) {
				this.addActionMessage(mes);
				this.message = mes;
			}
			return "move";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 组织机构下移
	 * 
	 * @author 喻斌
	 * @date Feb 11, 2009 9:50:00 AM
	 * @return
	 * @throws Exception
	 */
	public String moveDown() throws Exception {
		try {
			prepareModel();
			//采用分级授权方式
			UserInfo userInfo = userService.getCurrentUserInfo();
			String mes = userService.moveOrgPositionInTreeByHa(model,
					Const.MOVE_DOWN_IN_TREE, userInfo.getOrgId());
			if(mes.equals("该节点已在最下层，无法下移")){
				mes = "该节点已在最下层，无法下移。";
			}
			if (!"".equals(mes)) {
				this.addActionMessage(mes);
				this.message = mes;
			}
			return "move";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 检验编码合法性
	 * 
	 * @author 蒋国斌
	 * @date 2009-2-25 下午02:17:49
	 * @return String
	 * @throws Exception
	 */
	public String compareCode() throws Exception {
		message = "111";
		boolean iserr=userService.checkCode(orgcode);
		TUumsBaseOrg org=userService.getParentDepartmentBySyscode(orgcode);
		if(iserr){
			boolean flag=userService.compareOrgCode(orgcode);
			if(flag){
				boolean isParentExist = userService.checkOrgParentIsExist(orgcode, orgId);
				if(!isParentExist){
					message = "222";// 该编码的父级不存在
				}else if(org!=null && org.getIsOrg().equals("0")){
					message = "444";// 父级编码是部门，也不能添加机构或部门
				}
			}else{
				message = "000";// 编码不唯一
			}
		}else{
			message = "333";// 编码不符合规则
		}
		
		
		return this.renderText(message);
	}

	/**
	 * 还原已删除的组织机构
	 * 
	 * @author 喻斌
	 * @date Mar 13, 2009 1:53:25 PM
	 * @return
	 * @throws Exception
	 */
	public String reduction() throws Exception {
		String returnMessage = "还原成功。";
		try {
			TUumsBaseOrg fatherOrg = userService.getParentOrgByOrgId(orgId);
			if(!orgId.equals(fatherOrg.getOrgId()) && Const.IS_YES.equals(fatherOrg.getOrgIsdel())){
				returnMessage = "该组织机构的父级机构已被删除，无法还原。请改变该组织机构的父级机构再进行操作。";
			}else {
				userService.reductOrg(orgId, userTogether);
				addActionMessage("还原成功");
				//日志信息
				String orgName = userService.getOrgInfoByOrgId(orgId).getOrgName();
				String ip = getRequest().getRemoteAddr();
				String logInfo = "";			
				logInfo = "还原了组织机构:" + orgName;						
				this.myLogManager.addLog(logInfo, ip);
			}
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			addActionMessage(e.getMessage());
		}
		// return RELOAD;
		if ("1".equals(audittype)) {
			return renderHtml("<script> alert('" + returnMessage + "'); window.location='"
					+ getRequest().getContextPath()
					+ "/organisemanage/orgmanage!tree.action';"
						+ "parent.organiseInfo.location='"
						+ getRequest().getContextPath()
						+ "/fileNameRedirectAction.action?toPage=/organisemanage/orgmanage-container.jsp?orgId="
						+orgId
						+ "'; </script>");
		}
		return renderHtml("<script> alert('" + returnMessage + "'); window.location='"
				+ getRequest().getContextPath()
				+ "/organisemanage/orgmanage!input.action?orgId="
							+ orgId + "'; </script>");
	}
	
	/**
	 * 判断父级元素是否被设置删除属性
	 * @author 喻斌
	 * @date Apr 3, 2009 2:18:20 PM
	 * @param orgSyscode
	 * @return
	 * @throws Exception
	 */
	public String checkFatherIsdel() throws Exception {
		TUumsBaseOrg orgInfo = userService.getParentOrgByOrgSyscode(orgSysCode);
		if(orgInfo != null){
			if(Const.IS_YES.equals(orgInfo.getOrgIsdel())){
				return this.renderText("true");
			}
		}
		return this.renderText("false");
	}
	
	/**
	 * 在添加机构时判断当前所要添加机构的节点是：“部门”，“机构”
	 * @author zhengzb
	 * @desc 
	 * 2010-12-29 下午03:11:25 
	 * @return
	 * @throws Exception
	 */
	public String isHasOrg()throws Exception{
		TUumsBaseOrg orgInfo=userService.getOrgInfoByOrgId(orgId);
		String message="false";
		if(orgInfo!=null){
			if( orgInfo.getIsOrg()!=null&&!orgInfo.getIsOrg().equals("")&&orgInfo.getIsOrg().equals("1")){
				message="true";
			}
		}
		return this.renderText(message);
	}
	
	
	//初始化选择负责人（延迟加载，ajax调用）
	public String lazyLoadUserAndChildOrgs() throws Exception {
		TUumsBaseOrg orgInfo = userService.getOrgInfoBySyscode(orgSysCode);
		userList = userService.getUserInfoByOrgId(orgInfo.getOrgId(), Const.IS_YES, Const.IS_NO);
		int i = userService.findChildCodeLength(Const.RULE_CODE_ORG, orgInfo.getOrgSyscode());
		if(i == orgInfo.getOrgSyscode().length()){
			orgList = new ArrayList<TUumsBaseOrg>();
		}else{
			if(userService.isSystemDataManager(userService.getCurrentUserInfo().getUserId())) {
				orgList = userService.getOrgsByOrgSyscode(orgInfo.getOrgSyscode() + "%", Const.IS_NO);
			} else {
				orgList = userService.getOrgsByOrgSyscodeByHa(orgInfo.getOrgSyscode() + "%",Const.IS_NO, userService.getCurrentUserInfo().getOrgId());
			}
			for(int j=0; j<orgList.size();){
				if(orgList.get(j).getOrgSyscode().length() != i){
					orgList.remove(j);
				}else{
					j++;
				}
			}
		}
		String jasonStr = new String("[");
		if(orgList != null && !orgList.isEmpty()){
			StringBuffer bufStr = new StringBuffer("");
			for(TUumsBaseOrg org : orgList){
				bufStr.append(",{id:'")
						.append(org.getOrgId())
						.append("',name:'")
						.append(org.getOrgName())
						.append("',code:'")
						.append(org.getOrgSyscode())
						.append("',type:'")
						.append("org")
						.append("'}");
			}
			jasonStr = jasonStr + bufStr.substring(1);
		}
		if(userList != null && !userList.isEmpty()){
			StringBuffer bufStr = new StringBuffer("");
			for(TUumsBaseUser user : userList){
				bufStr.append(",{id:'")
						.append(user.getUserId())
						.append("',name:'")
						.append(user.getUserName())
						.append("',code:'")
						.append(user.getUserSyscode())
						.append("',type:'")
						.append("user")
						.append("'}");
			}
			if("[".equals(jasonStr)){
				jasonStr = jasonStr + bufStr.substring(1);
			}else{
				jasonStr = jasonStr + bufStr;
			}
		}
		jasonStr = jasonStr + "]";
		this.renderText(jasonStr);
		return null;
	}
	
	/**
	 * 根据组织机构编码判断父级组织机构是否是机构对象，若为最顶级则默认返回true（分级授权使用，ajax方式调用）
	 * @author 喻斌
	 * @date Nov 11, 2009 4:32:04 PM
	 * @return
	 * @throws Exception
	 */
	public String checkParentOrgIsOrg() throws Exception {
		TUumsBaseOrg parentOrg = userService.getParentOrgByOrgSyscode(orgSysCode);
		if(parentOrg == null){//最顶级节点,默认可以为机构
			this.renderText("true");
		}else{
			this.renderText("1".equals(parentOrg.getIsOrg()) ? "true" : "false");
		}
		return null;
	}
	
	/**
	 * 根据组织机构编码判断自己组织机构中是否包含机构对象（分级授权使用，ajax方式调用）
	 * @author 喻斌
	 * @date Nov 11, 2009 5:02:17 PM
	 * @return
	 * @throws Exception
	 */
	public String checkChildOrgIsOrg() throws Exception {
		TUumsBaseOrg selfOrg = userService.getOrgInfoBySyscode(orgSysCode);
		List<TUumsBaseOrg> childOrgLst = userService.getSelfAndChildOrgsByOrgsyscodeAndType(orgSysCode, Const.IS_YES, "2");
		if(childOrgLst != null && Const.IS_YES.equals(selfOrg.getIsOrg())
				&& childOrgLst.size() > 2){//本级为机构对象且返回值中包含1个以上的机构对象
			this.renderText("true");
		}else if(childOrgLst != null && Const.IS_NO.equals(selfOrg.getIsOrg())
				&& !childOrgLst.isEmpty()){//本级为部门对象且返回值中包含0个以上的机构对象
			this.renderText("true");
		}else{
			this.renderText("false");
		}
		return null;
	}
	
	/**
	 * 判断当前组织机构是否是顶级组织机构（约定顶级组织机构必须是机构对象，分级授权使用，ajax方式调用）
	 * @author 喻斌
	 * @date Nov 12, 2009 9:13:56 AM
	 * @return
	 * @throws Exception
	 */
	public String checkOrgIsTopOrg() throws Exception {
		//得到组织机构编码规则
		String codeRule = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		String parentOrgSyscode = userService.getParentCode(codeRule, orgSysCode);
		if(parentOrgSyscode != null && !"".equals(parentOrgSyscode)){
			this.renderText("false");
		}else{
			this.renderText("true");
		}
		return null;
	}

	public TUumsBaseOrg getModel() {
		return model;
	}

	public Page<TUumsBaseOrg> getPage() {
		return page;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public List<TUumsBaseOrg> getOrgList() {
		return orgList;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public List<TUumsBasePost> getPostList() {
		return postList;
	}

	public Page<TUumsBasePost> getPageWholePost() {
		return pageWholePost;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public void setOrgSysCode(String orgSysCode) {
		this.orgSysCode = orgSysCode;
	}

	public List<TUumsBaseAreacode> getAreaCodeList() {
		return areaCodeList;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getPostName() {
		return postName;
	}

	public String getOrgPostId() {
		return orgPostId;
	}

	public void setOrgPostId(String orgPostId) {
		this.orgPostId = orgPostId;
	}

	public String getAudittype() {
		return audittype;
	}

	public void setAudittype(String audittype) {
		this.audittype = audittype;
	}

	public List<TUumsBaseUser> getUserList() {
		return userList;
	}

	public void setUserList(List<TUumsBaseUser> userList) {
		this.userList = userList;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public MyLogManager getMyLogManager() {
		return myLogManager;
	}
	@Autowired
	public void setMyLogManager(MyLogManager myLogManager) {
		this.myLogManager = myLogManager;
	}

	public String getUserTogether() {
		return userTogether;
	}

	public void setUserTogether(String userTogether) {
		this.userTogether = userTogether;
	}

	public String getAreacodeCode() {
		return areacodeCode;
	}

	public void setAreacodeCode(String areacodeCode) {
		this.areacodeCode = areacodeCode;
	}

	public String getAreacodeName() {
		return areacodeName;
	}

	public void setAreacodeName(String areacodeName) {
		this.areacodeName = areacodeName;
	}

	public String getAreacode() {
		return areacode;
	}

	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}

	public String getIscode() {
		return iscode;
	}

	public void setIscode(String iscode) {
		this.iscode = iscode;
	}

	public String getNeedChildren() {
		return needChildren;
	}

	public void setNeedChildren(String needChildren) {
		this.needChildren = needChildren;
	}

	public File getUpFile() {
		return upFile;
	}

	public void setUpFile(File upFile) {
		this.upFile = upFile;
	}

	public String getSuperCode() {
		return superCode;
	}

	public void setSuperCode(String superCode) {
		this.superCode = superCode;
	}
	
	/**
	 * @return the supOrgCode
	 */
	public int getTopOrgcodelength() {
		return topOrgcodelength;
	}

	/**
	 * @param supOrgCode the supOrgCode to set
	 */
	public void setTopOrgcodelength(int topOrgcodelength) {
		this.topOrgcodelength = topOrgcodelength;
	}

	public String getRest2name() {
		return rest2name;
	}

	public void setRest2name(String rest2name) {
		this.rest2name = rest2name;
	}

	public String getRest4name() {
		return rest4name;
	}

	public void setRest4name(String rest4name) {
		this.rest4name = rest4name;
	}

	public String getIssuePeople() {
		return IssuePeople;
	}

	public void setIssuePeople(String issuePeople) {
		IssuePeople = issuePeople;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

    public String getOrgId1() {
        return orgId1;
    }

    public void setOrgId1(String orgId1) {
        this.orgId1 = orgId1;
    }
}