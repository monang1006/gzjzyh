package com.strongit.uums.usermanage;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.attachment.AttachmentHelper;
import com.strongit.oa.bo.ToaPersonalInfo;
import com.strongit.oa.bo.ToaUumsBaseOperationPrivil;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Role;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.Md5;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongit.oa.common.user.util.TimeKit;
import com.strongit.oa.doctemplate.doctempItem.DocTempItemManager;
import com.strongit.oa.im.cache.Cache;
import com.strongit.oa.myinfo.MyInfoManager;
import com.strongit.oa.mylog.MyLogManager;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.util.BaseDataExportInfo;
import com.strongit.oa.util.ProcessXSL;
import com.strongit.oa.util.TempPo;
import com.strongit.oa.viewmodel.ViewModelManager;
import com.strongit.platform.webcomponent.tree.JsonUtil;
import com.strongit.platform.webcomponent.tree.TreeNode;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseGroup;
import com.strongit.uums.bo.TUumsBaseGroupUser;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBasePost;
import com.strongit.uums.bo.TUumsBasePostOrg;
import com.strongit.uums.bo.TUumsBasePostUser;
import com.strongit.uums.bo.TUumsBasePrivil;
import com.strongit.uums.bo.TUumsBaseRole;
import com.strongit.uums.bo.TUumsBaseRoleUser;
import com.strongit.uums.bo.TUumsBaseSysmanager;
import com.strongit.uums.bo.TUumsBaseSystem;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.uums.optprivilmanage.BaseOptPrivilManager;
import com.strongit.uums.security.ApplicationConfig;
import com.strongit.uums.security.UserInfo;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "usermanage.action", type = ServletActionRedirectResult.class) })
public class UsermanageAction extends BaseActionSupport<TUumsBaseUser> {

	private Page<TUumsBaseUser> page = new Page<TUumsBaseUser>(FlexTableTag.MIDDLE_ROWS, true);

	private String userId;

	private String codeType;
	//用户所属组织机构Id
	private String extOrgId;

	private String initRole = "";

	private String roleId;

	private String initPost = "";

	private String postId;

	private String initGroup = "";

	private String groupCode;

	private String initPrivil = "";

	private String privelCode;
	private String rest4;

	private String copytoid;

	private String sysmanagerid = "";

	private String managerid = "";

	private String audittype;

	private String syscode;

	private String message;

	private String loginname;

	private String logininfo;

	private Map userTypeMap = new HashMap();
	
	//用户是否启用
	private Map userActiveTypeMap = new HashMap();

	private List<TUumsBaseUser> userList;

	private List<TUumsBaseOrg> orgList;

	private List<TUumsBasePost> postList = new ArrayList<TUumsBasePost>();

	private List<TUumsBaseGroup> userGoupList = new ArrayList<TUumsBaseGroup>();

	private List<TUumsBaseRole> roleList = new ArrayList<TUumsBaseRole>();

	private List<TUumsBasePost> wholePostList = new ArrayList<TUumsBasePost>();

	private List<TUumsBasePrivil> privilList = new ArrayList<TUumsBasePrivil>();

	private List<TUumsBaseSystem> systemList;

	private List<TUumsBaseOrg> userOrgList = new ArrayList<TUumsBaseOrg>();

	private TUumsBaseUser model = new TUumsBaseUser();

	private MyLogManager myLogManager;

	@Autowired IUserService userService;
	private int topOrgcodelength;//构造组织机构树时顶级节点的编码长度
	@Autowired
	private ViewModelManager viewModelManager;//用户权限的manager
	
	private int topGroupcodelength;//构造用户组树时最顶级的用户组编码长度

	/* private String orgname; */

	/*
	 * 查询相关属性
	 */
	private String selectname;// 用户名称

	private String selectloginname;// 用户登陆名

	private String selectorg;// 所属机构

	private String isdel;// 是否删除
	
	private String isActive;//是否启用
	
	private String userAddrname  = "";//领导联系人

	private File upload;  //签名图片
	
	private String mark;
	
	private String oldOrgid;//用户原来默认机构ID
	

	private String orgNames = "";// 用户所属所有组织机构名称

	public String getOrgNames() {
		return orgNames;
	}

	public void setOrgNames(String orgNames) {
		this.orgNames = orgNames;
	}

	private String orgid = "";// 用户所属组织机构ID
	
	public String getUserAddrname() {
		return userAddrname;
	}

	public void setUserAddrname(String userAddrname) {
		this.userAddrname = userAddrname;
	}

	/**
	 * 用户密码
	 */
	private String userPassword;
	
	private String rePassword;

	/**
	 * 用户密码变更标识（0、不变；1、变更）
	 */
	private String hasPasswordEdited;
	
	private String module;
	
	//用户所属组织机构名称
	private String orgName;

	/**
	 * 公钥值
	 */
	private String pubkey;
	
	//全局设置信息
	@Autowired ApplicationConfig applicationConfig;
	
	//密码是否要md5加密
	private String md5Enable;
	
	//资源权限编号，用于ajax加载
	private String privilCode;
	//应用系统编号，用于ajax加载
	private String systemCode;
	
	//移动到新的组织机构Id
	private String moveOrgId;
	
	//组织机构编码，选择委托人延迟加载时使用
	private String orgSyscode;
	
	private String excelOrgId;// 导入用机构ID
	
	//需要导入的EXCEL文件
	private File upFile;
	
	private String currentUSBKEY;

	@Autowired MyInfoManager myInfoManager ; //个人信息管理类,加入同步用户信息功能,邓志城 2010年6月25日9:23:20
	
	private String archiveManagerId;	//档案管理员
	
	private String dataManagerId;		//分级授权管理员

	private String treeNodes;// 构造树形数据结构
	
	@Autowired BaseOptPrivilManager basePrivilManager;
	
	public File getUpFile() {
		return upFile;
	}

	public void setUpFile(File upFile) {
		this.upFile = upFile;
	}

	public String getExcelOrgId() {
		return excelOrgId;
	}

	public void setExcelOrgId(String excelOrgId) {
		this.excelOrgId = excelOrgId;
	}

	public String getMd5Enable() {
		return md5Enable;
	}

	public void setMd5Enable(String md5Enable) {
		this.md5Enable = md5Enable;
	}

	/**
	 * 构造方法
	 * 
	 */
	private UsermanageAction() {
		userTypeMap.put("0", "未删除");
		userTypeMap.put("1", "已删除");
		userActiveTypeMap.put("0", "未启用");
		userActiveTypeMap.put("1", "已启用");
	}
	
	/**
	 * 导入用户EXCEL文件
	 * @author Administrator蒋国斌
	 * StrongOA2.0_DEV 
	 * 2010-3-16 下午04:44:03 
	 * @return
	 */
	public String importdata(){
		 StringBuffer msg = new StringBuffer(); 
		if(upFile!=null){
			FileInputStream fileinput=null;
			try{
				fileinput=new FileInputStream(upFile);
				HSSFWorkbook templatebook=new HSSFWorkbook(fileinput);// 创建对Excel工作簿文件的引用 
				HSSFSheet sheet=templatebook.getSheetAt(0); // 创建对工作表的引用。
				int rows = sheet.getPhysicalNumberOfRows(); 
				int i=1;
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
										 //strvalue.append("0,"); 
										 strvalue.append(" ,"); 
									 } 
								 }
							 }	 
						 }
						
						 String[] valuestr=strvalue.toString().split(",");
						 if(valuestr.length>=3){
							 if(valuestr[1].trim().equals("")||valuestr[2].trim().equals("")||valuestr[4].trim().equals("")){
								 msg.append("第"+(r+1)+"行导入失败：表中的用户名、登录账号、所属机构存在空值。");
								 msg.append("\\n" );
							 }else{
								 TUumsBaseUser user=new TUumsBaseUser();
								user.setUserId(null);
								user.setOrgId(excelOrgId);
								user.setOrgIds(","+excelOrgId+",");
//								user.setUserSyscode(valuestr[0]);
								//用户编号 自动生成  
								SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
							    user.setUserSyscode(sdf.format(new Date())+(int)(Math.random()*900000));
								user.setUserName(valuestr[1]);
								user.setUserLoginname(valuestr[2]);
								if(valuestr[3]!=null&&!"".equals(valuestr[3])&&valuestr[3].length()==32 ){
									user.setUserPassword(valuestr[3]);
								}else if(valuestr[3]!=null&&!"".equals(valuestr[3])){
									Md5 md5=new Md5();
								String pass=md5.getMD5ofStr(valuestr[3]);
								user.setUserPassword(pass);
							}else{
								//为空 则设置成默认密码 1
								//为空时，对1进行md5加密然后导入
								Md5 md5=new Md5();
								String pass=md5.getMD5ofStr("1");
								user.setUserPassword(pass);
							}
							//user.setUserTel(valuestr[4]);
							

							user.setUserTel(valuestr.length >= 6 ? valuestr[5] : "");//电话
							user.setRest2(valuestr.length >= 7 ? valuestr[6] : "");//手机号码
							user.setUserEmail(valuestr.length >= 8 ? valuestr[7] : "");//Email
							
							user.setUserSequence(new Long(i+1));
							user.setUserIsactive("1");
							user.setUserIsdel("0");
//							userService.saveUser(user);
							userService.saveUserByOldOrgIds(user,"");
							//luosy coyp from 处理同步到个人信息中；added by dengzc 2010年5月18日10:56:21
							ToaPersonalInfo myInfo = myInfoManager.getInfoByUserid(user.getUserId());	
							if(myInfo == null){//个人信息部存在
								myInfo = new ToaPersonalInfo();
								myInfo.setUserId(user.getUserId());
							}
							myInfo.setPrsnName(user.getUserName());//姓名
							myInfo.setPrsnMobile1(user.getRest2());//手机号码
							myInfo.setPrsnMail1(user.getUserEmail());//email
							myInfo.setPrsnTel1(user.getUserTel());//电话
							myInfoManager.saveObj(myInfo);
							//end
							
						   }
						 }
						  i=i+1;
				
					 }
				}
			}
			
			catch(Exception ex){
				ex.printStackTrace();
				msg.append("导入失败：格式有误或者数据已经存在，请检查。");
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
		this.getRequest().setAttribute("excelOrgId",excelOrgId);
		this.getRequest().setAttribute("msg", msg.toString());
		return "importreturn";
	}
	
	/**
	 * 导出机构用户
	 * @author Administrator蒋国斌
	 * StrongOA2.0_DEV 
	 * 2010-3-23 上午09:56:10 
	 * @return
	 * @throws Exception
	 */
  public String exportExcels() throws Exception {
	 
	 if(excelOrgId!=null && excelOrgId!=""){
		 TUumsBaseOrg org=userService.getOrgInfoByOrgId(excelOrgId);
		 /**
		 if(org.getOrgSyscode().length()==3){
			 userList=userManager.getAllUserInfos();
		 }else
		  userList=userManager.queryOrgUsers(excelOrgId);
	
	**/
//	 userList=userService.getUsersOfOrgAndChildByOrgcodeByHa(org.getOrgSyscode(), "1", "0", userService.getCurrentUserInfo().getOrgId());
	 userList=userService.getUserInfoByOrgId(excelOrgId, "1", "0");
	 }
		this.exportfils(userList);
		return null;
	}
  
  private void exportfils(List<TUumsBaseUser> orglist){
		try{
			
			HttpServletResponse response=getResponse();
			
			//创建EXCEL对象
			BaseDataExportInfo export = new BaseDataExportInfo();
			String str=toUtf8String("用户信息");
			export.setWorkbookFileName(str);
			export.setSheetTitle("用户信息");
			export.setSheetName("用户信息");
			//描述行信息
			List<String> tableHead = new ArrayList<String>();
			tableHead.add("用户编号");
			tableHead.add("用户名");
			tableHead.add("登录帐号");
			tableHead.add("登录密码");
			tableHead.add("所属机构");
			tableHead.add("电话");
			tableHead.add("手机号码");
			tableHead.add("Email");
			export.setTableHead(tableHead);
			
			
			//获取导出信息
		    List rowList=new ArrayList();
		    Map rowhigh=new HashMap();
		    int rownum=0;
		    String nowdate=TimeKit.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		    for (TUumsBaseUser us : orglist) {
				Vector cols=new Vector();
				cols.add(us.getUserSyscode());//用户编号
				cols.add(us.getUserName());//用户名称
				
				cols.add(us.getUserLoginname());//登陆帐号
				//Md5 md5=new Md5();
				//String pass=md5.getMD5ofStr(org.getUserPassword());
//				cols.add(us.getUserPassword());//登陆密码
				cols.add("");//默认为空
				cols.add(userService.getOrgInfoByOrgId(us.getOrgId()).getOrgName());//所属机构

				cols.add(us.getUserTel());//电话
				cols.add(us.getRest2());//手机号码
				cols.add(us.getUserEmail());//Email
				
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
             try { b = Character.toString(c).getBytes("utf-8");}
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
	public String delete() throws Exception {
		try {
			String userNames = "";
			 
			if ((userId != null) && (!("".equals(userId)))){
				String[] userIds = userId.split(",");
				for (int i = 0; i < userIds.length; i++) {
//					if("".equals(userNames)){
						model =userService.getUumsUserInfoByUserId(userIds[i]);
						String[] orgidsss = model.getOrgIds().split(",");
						int tempInt=0;
						String tempString = "";
						TUumsBaseOrg org = userService.getOrgInfoByOrgId(extOrgId);
						if(org!=null){
							for(int k=0;k<orgidsss.length;k++){
								if(orgidsss[k]!=null&&!"".equals(orgidsss[k])){
									if(org.getOrgId().equals(orgidsss[k])){
										model.setOrgIds(model.getOrgIds().replace(orgidsss[k], ""));
										model.setSupOrgCode(model.getSupOrgCode().replaceFirst(org.getSupOrgCode(), ""));
										tempString =tempString + "," + orgidsss[k];
										
									}
									tempInt++;
								}
							}
						}
						if(tempInt<=1){
							model.setOrgIds(model.getOrgIds()+tempString);
							userNames = model.getUserName();
							long time=new Date().getTime();
							model.setUserLoginname(model.getUserLoginname()+"_del"+time);
							model.setUserSyscode(model.getUserSyscode() + "_del"+time);
							model.setUserIsdel(Const.IS_YES);
							userService.saveUserForDel(model);
						}else{
//							userService.saveUser(model);
							if(userId.indexOf(",")==-1){
								userId="";
							}else{
								userId=userId.replaceAll(model.getUserId(), " ");
							}
							//默认机构展现
							String[] orgidssss = model.getOrgIds().split(",");
							if(model.getOrgId().indexOf(model.getOrgIds())==-1){
								for(int k=0;k<orgidssss.length;k++){
									if(orgidsss[k]!=null&&!"".equals(orgidssss[k])){
										model.setOrgId(orgidssss[k]);
										break;
									}
								}
							}
							userService.saveUserForDel(model);
						}
					
//					}else{
//						model.setUserLoginname(model.getUserLoginname()+"_del");
//						model.setUserSyscode(model.getUserSyscode() + "_del");
//						model.setUserIsdel("1");
//						userNames += "," + model.getUserName();
//					}
				}
			}

		
//		userManager.deleteUsers(userId);
			try {
				String[] userIds = userId.split(",");
				for (int i = 0; i < userIds.length; i++) {
					Cache.getService().delUser(userIds[i]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			addActionMessage("删除成功");
			//添加删除操作的日志信息
			String ip = getRequest().getRemoteAddr();
			String logInfo = "";
			logInfo = "删除了用户:" + userNames;
			this.myLogManager.addLog(logInfo, ip);
			return renderHtml("true");
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			addActionMessage(e.getMessage());
			return renderHtml("false");
		}
		// return RELOAD;
/*		return renderHtml("<script>location='"
				+ getRequest().getContextPath()
				+ "/usermanage/usermanage.action?extOrgId=" + extOrgId
				+ "'; </script>");*/
		
	}

	public String reduction() throws Exception {
		String returnValue = "";
		try {
			returnValue = userService.reductUser(userId);
			addActionMessage(returnValue);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			addActionMessage(e.getMessage());
		}
		// return RELOAD;
		return renderHtml("<script> alert('" + returnValue + "'); location='"
				+ getRequest().getContextPath()
				+ "/usermanage/usermanage.action?extOrgId=" + extOrgId
				+ "'; </script>");
	}

	@Override
	public String input() throws Exception {
//		prepareModel();
		//获取md5加密设置
		if(applicationConfig.isMd5Enable()){
			this.md5Enable = "1";
		}else{
			this.md5Enable = "0";
		}
		
		String isSupman=userService.getCurrentUserInfo().getUserIsSupManager();
		this.getRequest().setAttribute("isSupman", isSupman);
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		if (extOrgId != null && extOrgId.trim().length() > 0) {
			if(userService.isSystemDataManager(userService.getCurrentUser().getUserId())) {
				page = userService.queryOrgUsers(page, extOrgId, selectname,
						selectloginname, "0", isActive);				
			}else{
				if((selectname != null && !"".equals(selectname))||(selectloginname != null && !"".equals(selectloginname))||(isActive != null && !"".equals(isActive))){
					page = userService.queryOrgUsers(page, extOrgId, selectname,
							selectloginname, "0", isActive);
				}else{
					String sysCode=userService.getOrgInfoByOrgId(extOrgId).getOrgSyscode();
//					page=userService.getUsersOfOrgAndChildByOrgcodeForPageByHa(page,sysCode,"","0",userService.getCurrentUser().getOrgId());
					
					page = userService.queryOrgUsersByHa(page, extOrgId, selectname, selectloginname, "0", isActive, userService.getCurrentUserInfo().getOrgId());				
					
				}
			}
		} else {
			if(userService.isSystemDataManager(userService.getCurrentUser().getUserId())) {
				page = userService.queryUsers(page, selectname,selectloginname, selectorg, "0", isActive);
			}else{
				page=userService.queryUsersByHa(page, selectname, selectloginname, selectorg, "0", isActive, userService.getCurrentUserInfo().getOrgId());				
			}
		}
//		List<TUumsBaseUser> list = page.getResult();
//		if(list!=null){
//			for(TUumsBaseUser role:list){
//				userId = role.getUserId();
//				postList = userService.getPostInfoByUserId(userId);
//				StringBuilder userPost = new StringBuilder("");
//				for(TUumsBasePost post:postList){
//					if(userPost.length()!=0){
//						userPost.append("、");
//					}
//					userPost.append(post.getPostName());
//				}
//				//role.setRest4(userPost.toString());
//			}
//		}
		return SUCCESS;
	}
	
	/*
	 * 
	 * Description:判断USBKEY是否已经使用
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 16, 2010 5:08:04 PM
	 */
	public String isUsbkeyExist() throws Exception {
		List<TUumsBaseUser> list=userService.getAllUserInfos();
		TUumsBaseUser user;
		String usbkey="";
		String msg="";
		for(int i=0;i<list.size();i++){
			user=list.get(i);
			if(userId.equals(user.getUserId())){
				continue;
			}else{
				usbkey=user.getUserUsbkey();
				if(usbkey!=null&&!"".equals(usbkey)&&usbkey.equals(currentUSBKEY)){
					msg=user.getUserName()+"已经使用该USBKEY,请先清除空"+user.getUserName()+"的USBKEY再设置！";
					break;
				}
			}
		}
		this.renderText(msg);
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		try {
			if (userId != null && !"".equals(userId) && !"null".equals(userId)) {
				model = userService.getUumsUserInfoByUserId(userId);
				List<TUumsBaseOrg> orglist = userService.getOrgInfosByUserId(userId);
//				TUumsBaseOrg org = userService.getOrgInfoByOrgId(model.getOrgId());
//				if(org != null){
//					orgName = org.getOrgName();
//				}
				if (orglist != null && orglist.size() > 0) {
					for (TUumsBaseOrg uumsBaseOrg : orglist) {
						if(uumsBaseOrg!=null){
							orgid += "," + uumsBaseOrg.getOrgId();
						}
					}
				}
				if (orgid != null && !"".equals(orgid)) {
					orgid = orgid.substring(1);
				}
				String[] orgs = orgid.split(",");
				for (int i = 0; i < orgs.length; i++) {
					TUumsBaseOrg org = userService.getOrgInfoByOrgId(orgs[i]);
					if (org != null) {
						orgNames += "," + org.getOrgName();
					}
					userOrgList.add(org);
				}
				if (orgNames != null && !"".equals(orgNames)) {
					orgNames = orgNames.substring(1);
				}
				
				if(model!=null){
					//显示签名图
					ToaPersonalInfo myInfo = myInfoManager.getInfoByUserid(model.getUserId());	
					
					if(myInfo!=null){
						byte[] buf = myInfo.getSign();
						if(buf != null) {
							InputStream is = FileUtil.ByteArray2InputStream(buf);
							String path = AttachmentHelper.saveFile(getRequest().getContextPath(), is, "temp.jpg");
							logger.info("生成附件：" + path);
							getRequest().setAttribute("tempFilePath", path);
						}
					}
				}
				
				//为了保证Ldap和数据库的一致性，将人员保存与人员管理员设置操作分离开
				/*if ("get".equals(audittype)) {
					Set sysmanager = model.getBaseSysmanagers();
					sysmanagerid = "";
					managerid = "";
					for (Iterator<TUumsBaseSysmanager> it = sysmanager
							.iterator(); it.hasNext();) {
						TUumsBaseSysmanager syaman = it.next();
						if (Const.USER_TYPE_SYSTEM.equals(syaman
								.getSmUserType())) {
							sysmanagerid += ","
									+ syaman.getBaseSystem().getSysId();
						}
						if (Const.USER_TYPE_MANAGER.equals(syaman
								.getSmUserType())) {
							managerid += ","
									+ syaman.getBaseSystem().getSysId();
						}
					}
					if (sysmanagerid.length() > 0) {
						sysmanagerid = sysmanagerid.substring(1);
					}
					if (managerid.length() > 0) {
						managerid = managerid.substring(1);
					}
				}*/
			} else {
				model = new TUumsBaseUser();
				model.setUserSequence(userService.getNextUserSequenceCode());
				if (extOrgId != null && !"".equals(extOrgId)) {
					TUumsBaseOrg org = userService.getOrgInfoByOrgId(extOrgId);
					userOrgList.add(org);
					if(org != null){
//						orgName = org.getOrgName();
						orgid = org.getOrgId();
						orgNames = org.getOrgName();
						
					}
					model.setOrgId(extOrgId);
				}
				// model.setUserSyscode(userManager.getNextUserCode());
			}
			
			if(model.getUserAddr()!= null && !model.getUserAddr().equals("")){//UserAddr对应领导联系人的用户ID
				String id = model.getUserAddr();
				id = id.substring(1);
				userAddrname = userService.getUserNameByUserId(id);
						
			}
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public String delPic(){
		ToaPersonalInfo myInfo = myInfoManager.getInfoByUserid(userId);
		myInfo.setSign(null);
		myInfoManager.saveObj(myInfo);
		return renderText("true");
	}
	@Override
	public String save() throws Exception {
		byte[] buf = null;
		if(upload != null) {//签名
			FileInputStream fis = null;
   			try {
   				//判断图片分辨率是否符合
   				Image srcImage = null;
   				srcImage = ImageIO.read(upload); 
   				int srcWidth = srcImage.getWidth(null);		//原图片宽度   
   				int srcHeight = srcImage.getHeight(null);	//原图片高度 
   				if(srcWidth!=120||srcHeight!=90){
   					getRequest().setAttribute("wrongInfo", true);
   					return input();
   				}
   				File file = File.createTempFile("test", ".jpg");// 创建临时文件
   				String tempFile = file.getPath();
   				logger.info(tempFile);
   				DocTempItemManager.makeSmallImage(upload, tempFile);
				fis = new FileInputStream(file);
				buf = FileUtil.inputstream2ByteArray(fis);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}finally {
				if(fis != null) {
					fis.close();
				}
			}
		}
		
		//添加用户还是编辑用户的标志
		String flag = "edit";		
		if ("".equals(model.getUserId())){
			flag = "add";
			model.setUserId(null);			
		}
		//注入组织机构Id
		//if(extOrgId != null && !"".equals(extOrgId)){
		//	model.setOrgId(extOrgId);
		//}
		/**
		 * 对变更密码重新赋值
		 */
		/*
		 * if("1".equals(hasPasswordEdited)){ Encrypt encrypt = new Encrypt();
		 * model.setUserPassword(encrypt.encrypt(userPassword)); }
		 */
		/**
		 * 获取用户公钥值
		 */
		if (this.pubkey != null && !"".equals(this.pubkey)) {
			model.setUserPubkey(pubkey);
		}
		//将人员保存和人员管理员设置分离开
		/*if (sysmanagerid != null && !"".equals(sysmanagerid)) {
			String[] sysmid = sysmanagerid.split(",");
			for (int i = 0; i < sysmid.length; i++) {
				str += sysmid[i] + "," + Const.USER_TYPE_SYSTEM + ";";
			}
		}
		if (managerid != null && !"".equals(managerid)) {
			String[] smid = managerid.split(",");
			for (int i = 0; i < smid.length; i++) {
				str += smid[i] + "," + Const.USER_TYPE_MANAGER + ";";
			}
		}*/
		
		//add
		UserInfo userInfo = userService.getCurrentUserInfo();
		List<TUumsBaseOrg> orglist = userService.getOrgInfosByUserIdByHa(userId,
				userInfo.getOrgId());
		String newOrgIds = "";
		String myOldorgids="";
		Set<String> orgSet = new HashSet<String>();
		String oldOrgIds = model.getOrgIds();
		List<TUumsBaseOrg> orls = new ArrayList<TUumsBaseOrg>();
		String roleids="";
		String groupIds="";
		if (null!=model.getUserId() && !"".equals(model.getUserId())){
		List<TUumsBaseRoleUser> roelst=userService.getUserToRoleInfosByUserIdByHa(model.getUserId(), userInfo.getOrgId());
		List<TUumsBaseGroupUser> grouplst=userService.getUserToGroupInfosByUserId(model.getUserId(), userInfo.getOrgId());
		if(roelst!=null && roelst.size()>0){//处理在同一机构下变换不同部门应该保留之前所拥有的角色信息
			for (TUumsBaseRoleUser roleUser : roelst) {
				 roleids+=","+roleUser.getBaseRole().getRoleId()+"&"+model.getOrgId();
				}
		}
		if(grouplst!=null && grouplst.size()>0){//处理在同一机构下变换不同部门应该保留之前所拥有的用户组信息
			for (TUumsBaseGroupUser groupUser :grouplst ) {
				groupIds+=","+groupUser.getBaseGroup().getGroupId()+"&"+model.getOrgId();
				}
		}
		}
		if (oldOrgIds != null && !"".equals(oldOrgIds)) {
			String[] oldOrgId = oldOrgIds.substring(1, oldOrgIds.length() - 1)
					.split(",");
			myOldorgids=oldOrgIds.substring(1, oldOrgIds.length() - 1);
			for (int i = 0; i < oldOrgId.length; i++) {
				TUumsBaseOrg neworg = userService.getOrgInfoByOrgId(oldOrgId[i]);
				if(neworg!=null){
					orls.add(neworg);
				}
			}
		}
		if(orglist!=null){
			for (TUumsBaseOrg uumsBaseOrg : orglist) {
				orls.remove(uumsBaseOrg);
			}
		}
		String oids = "";
		if (orls.size() > 0) {
			for (TUumsBaseOrg uumsBaseOrg2 : orls) {
				oids += "," + uumsBaseOrg2.getOrgId();
			}
		}
		if (!"".equals(oids)) {
			oldOrgIds = oids + ",";
		} else {
			oldOrgIds = "";
		}

		if (oldOrgIds != null && !"".equals(oldOrgIds)) {

			String[] oldOrgId = oldOrgIds.substring(1, oldOrgIds.length() - 1)
					.split(",");
			for (int i = 0; i < oldOrgId.length; i++) {
				orgSet.add(oldOrgId[i]);
			}

		}
		if (orgid != null && !"".equals(orgid)) {
			String[] orgids = orgid.split(",");
			for (int i = 0; i < orgids.length; i++) {
				orgSet.add(orgids[i]);
			}
		}
		for (String string : orgSet) {
			newOrgIds += "," + string;
		}
		if (!"".equals(newOrgIds)) {
			model.setOrgIds(newOrgIds + ",");
		}
		
		//end
		try {
			//用户中只保存组织机构Id，不再保存组织机构实体类
			/*//重新注入组织机构，不能在页面上直接采用model.baseOrg.orgId形式
			if(!model.getBaseOrg().getOrgId().equals(orgId)){
				String oldOrgId = model.getBaseOrg().getOrgId();
				model.setBaseOrg(orgManager.getOrg(orgId));
				this.orgId = oldOrgId;
			}*/
			//userManager.saveUser(model, str);
			//userService.saveUser(model);
			userService.saveUserByOldOrgIds(model, myOldorgids);//调用统一用户最新的保存用户接口 jianggb 修改于2014-03-05
			if(!oldOrgid.equals(model.getOrgId())){
				userService.saveUserRoles(model.getUserId(),roleids.substring(1,roleids.length()));
				userService.saveUserGroups(model.getUserId(), groupIds.substring(1,groupIds.length()));
			  }
			
			
			//处理同步到个人信息中；added by dengzc 2010年5月18日10:56:21
			ToaPersonalInfo myInfo = myInfoManager.getInfoByUserid(model.getUserId());	
			if(myInfo == null){//个人信息部存在
				myInfo = new ToaPersonalInfo();
				myInfo.setUserId(model.getUserId());
			}
			myInfo.setPrsnName(model.getUserName());//姓名
			myInfo.setPrsnMobile1(model.getRest2());//手机号码
			myInfo.setPrsnMail1(model.getUserEmail());//email
			myInfo.setPrsnTel1(model.getUserTel());//电话
			if(null!=buf && buf.length>0){
				myInfo.setSign(buf);
			}
			myInfoManager.saveObj(myInfo);
			//end
			
			//同步到即时通讯中
			User userModel = new User();
			userModel.setUserId(model.getUserId());
			userModel.setUserPassword(model.getUserPassword());
			userModel.setUserName(model.getUserName());
			userModel.setRest2(model.getRest2());
			userModel.setUserLoginname(model.getUserLoginname());
			userModel.setUserDescription(model.getUserDescription());
			userModel.setOrgId(model.getOrgId());
			userModel.setOrgName(orgName);
			userModel.setUserSequence(model.getUserSequence());
			userModel.setUserTel(model.getUserTel());//单位电话
			userModel.setUserEmail(model.getUserEmail());//Email
			try {
//				即时通讯保存用户
//				Cache.getService().updateUserInfo(userModel);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//---------- End ------------------
			
			//更新邮件、论坛中的密码等信息
//			SyncBbsMailUserpsw syncUser = new SyncBbsMailUserpsw();
//			syncUser.setUserPswByUrl(userModel.getUserLoginname(), rePassword, SyncBbsMailUserpsw.BBSURL);
//			syncUser.setUserPswByUrl(userModel.getUserLoginname(), rePassword, SyncBbsMailUserpsw.MAILURL);
			// ---------- End ---------------
			
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		addActionMessage("保存成功");
		
		//添加日志信息
		String orgName = userService.getOrgInfoByOrgId(model.getOrgId()).getOrgName();
		String ip = getRequest().getRemoteAddr();
		String logInfo = "";
		if(flag.equals("add")){
			logInfo = "添加了用户" + model.getUserName()+"（"+ orgName + "）";			
		}else{
			logInfo = "编辑了用户" + model.getUserName()+"（"+ orgName + "）";			
		}
		
		this.myLogManager.addLog(logInfo, ip);
		
		//orgId = model.getBaseOrg().getOrgId();
		return "init";
		// return renderHtml("<script> alert('保存成功');
		// window.dialogArguments.location='"+getRequest().getContextPath()+"/usermanage/usermanage.action';
		// window.close();</script>");
	}
	
	/**
	 * 保存应用系统管理员设置信息
	 * @author 喻斌
	 * @date Jun 17, 2009 4:09:59 PM
	 * @return
	 * @throws Exception
	 */
	public String savaSystemManagers() throws Exception {
		String str = "";
		//将人员保存和人员管理员设置分离开
		if (sysmanagerid != null && !"".equals(sysmanagerid)) {
			String[] sysmid = sysmanagerid.split(",");
			for (int i = 0; i < sysmid.length; i++) {
				str += sysmid[i] + "," + Const.USER_TYPE_SYSTEM ;
			}
		}
		if (managerid != null && !"".equals(managerid)) {
			String[] smid = managerid.split(",");
			for (int i = 0; i < smid.length; i++) {
				str += smid[i] + "," + Const.USER_TYPE_MANAGER + "";
			}
		}
		if (dataManagerId != null && !"".equals(dataManagerId)) {
			String[] smid = dataManagerId.split(",");
			for (int i = 0; i < smid.length; i++) {
				str += smid[i] + "," + IUserService.GradeAuthorizationUser + "";
			}
		}
		try {
			if(!"".equals(userId)&&null!=userId){
				String orgId = userService.getUserInfoByUserId(userId).getOrgId();
				userService.saveSystemAdminInfo(userId, str,orgId);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		addActionMessage("保存成功");
		//orgId = model.getBaseOrg().getOrgId();
		return "init";
		// return renderHtml("<script> alert('保存成功');
		// window.dialogArguments.location='"+getRequest().getContextPath()+"/usermanage/usermanage.action';
		// window.close();</script>");
	}

	/**
	 * 获取组织机构列表
	 * 
	 * @return
	 */
	public String ogrlist() {
		if (extOrgId != null && extOrgId.trim().length() > 0) {
			//page = userManager.queryOrgUsers(page, extOrgId);
			page = userService.queryOrgUsersByHa(page, extOrgId, userService.getCurrentUserInfo().getOrgId());
		} else {
			//page = userManager.queryUsers(page, "%", "%");
			page = userService.queryUsersByHa(page, "%", "%",userService.getCurrentUserInfo().getOrgId());
		}
		return SUCCESS;
	}

	/**
	 * 获取岗位信息
	 * 
	 * @return
	 */
	public String postInfo() {
		postList = userService.getPostInfoByUserId(userId);
		return "postinfo";
	}

	/**
	 * 获取角色信息
	 * 
	 * @return
	 */
	public String roleInfo() {
		roleList = userService.getUumsBaseRoleInfosByUserId(userId, Const.IS_YES);
		return "roleinfo";
	}

	/**
	 * 获取用户组信息
	 * 
	 * @return
	 */
	public String groupInfo() {
		userGoupList = userService.getGroupInfosByUserId(userId, Const.IS_YES);
		return "groupinfo";
	}

	/**
	 * 改变用户组织机构时，获取组织机构列表
	 * 
	 * @return
	 * @throws IOException
	 */
	public String movetree() throws IOException {
		//增加分级授权功能
		UserInfo userInfo = userService.getCurrentUserInfo();
		orgList = userService.queryOrgsByHa(Const.IS_NO, userInfo.getOrgId());
		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		//topOrgcodelength = userInfo.getSupOrgCode().length();
		topOrgcodelength = userService.findTopOrgCodeLength();
		return "movetree";
	}


	/**
	 * 得到父级代码。
	 * 
	 * @param codeRule -
	 *            编码规则
	 * @param code -
	 *            代码
	 * @return String - 父级代码,如果为最顶级则返回""
	 * @throws SystemException
	 * @throws DAOException
	 * @throws ServiceException
	 */
	private String getParentCode(String code, String codeRule)
			throws Exception {
		String parentCode = null;
		if (code != null && codeRule != null) {
			// 校验代码编码规则
			// 分析编码规则
			List<Integer> lstCodeRule = new ArrayList<Integer>();
			for (int i = 0; i < codeRule.length(); i++) {
				String str = codeRule.substring(i, i + 1);
				lstCodeRule.add(new Integer(str));
			}
			int codeLength = code.length();
			int count = 0;
			for (Iterator<Integer> iter = lstCodeRule.iterator(); iter
					.hasNext();) {
				Integer len = iter.next();
				count += len.intValue();
				if (count == codeLength) {
					count -= len.intValue();
					parentCode = code.substring(0, count);
				}
			}
		}
		return parentCode;
	}
	
	/**
	 * 获取组织机构树
	 * 
	 * @return
	 * @throws IOException
	 */
	public String tree() throws IOException {
	//	orgList = .queryOrgs(Const.IS_NO);
		TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(userService.getCurrentUserInfo().getUserId());
		if(userService.isSystemDataManager(userService.getCurrentUserInfo().getUserId())) {
			orgList = userService.getAllOrgInfos();	
		} else {
			//增加分级授权功能
			UserInfo userInfo = userService.getCurrentUserInfo();
			orgList = userService.queryOrgsByHa(Const.IS_NO, userInfo.getOrgId());
		}
		syscode=org.getSupOrgCode();
		topOrgcodelength = userService.findTopOrgCodeLength();
		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		return "tree";
	}

	/**
	 * 获取组织机构树
	 * 增加一人多岗之前的选择org
	 * @return
	 * @throws IOException
	 */
	public String getOrgTree() throws IOException {
		if(userService.isSystemDataManager(userService.getCurrentUserInfo().getUserId())) {
			orgList = userService.queryOrgs(Const.IS_NO);
		} else {
			//增加分级授权功能
			UserInfo userInfo = userService.getCurrentUserInfo();
			orgList = userService.queryOrgsByHa(Const.IS_NO, userInfo.getOrgId());
		}
		topOrgcodelength = userService.findTopOrgCodeLength();
		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		return "checktree";
	}
	
	/**
	 * author:luosy 2014-1-18
	 * description:增加一人多岗之后的选择org
	 * modifyer:
	 * description:
	 * @return
	 * @throws IOException
	 */
	public String orgMoreTree() throws Exception {
		// 增加分级授权功能
		System.out.println(orgid);
		if (!"".equals(orgid)) {
			orgid = "," + orgid + ",";
		}
		UserInfo userInfo = userService.getCurrentUserInfo();
		orgList = userService.queryOrgsByHa(Const.IS_NO, userInfo.getOrgId());
		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		topOrgcodelength = userService.findTopOrgCodeLength();
		
		// 得到预选中org数据		start	modify by  luosy 2014-01-21
		if(null!=userId && !"".equals(userId)){
			//initPost = userService.getBaseUserByUserId(userId).getOrgIds();
			//页面新选中的机构 也在机构选中页面勾选展现
			initPost = orgid;
		}
		// 得到预选中org数据		end
		
		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		List<TreeNode> nodeLst = new ArrayList<TreeNode>(0);
		for(int i=0;i<orgList.size();i++){
			String parentId = orgList.get(i).getOrgParentId();
			TreeNode node = new TreeNode();
			String isorg = orgList.get(i).getIsOrg();
//			node.getAttributes().put("isorg", isorg);
//			node.getAttributes().put("orgSyscode",orgList.get(i).getOrgSyscode());
			//TODO ：@lsy这里要加上分级授权开关判断
			if(parentId==null || "".equals(parentId) || (userInfo.getOrgId().equals(orgList.get(i).getOrgId())&& Const.IS_YES.equals("1"))){
				node.setId(orgList.get(i).getOrgId());
				node.setPid("-1");
				node.setIsexpand(true);
				node.setShowcheck(true);
				node.setComplete(true);
				node.setText(orgList.get(i).getOrgName());
				node.setValue("system");
				nodeLst.add(node);
			}else{
				node.setId(orgList.get(i).getOrgId());
				node.setPid(parentId);
				node.setIsexpand(true);
				node.setShowcheck(true);
				node.setComplete(true);
				node.setText(orgList.get(i).getOrgName());
				node.setValue("privil");
				nodeLst.add(node);
			}
		}
		String treeNodes = "";
		//构造树的json数据
		treeNodes = JsonUtil.fromTreeNodes(nodeLst);//JsonMapper.buildNormalBinder().toJson(nodeLst);
//		this.getResponse().setContentType("application/json");
//		this.getResponse().getWriter().write(treeNodes);
		getRequest().setAttribute("treeNodes", treeNodes);

		
		return "checkmore";
	}

	public String getOrgTree1() throws Exception {
		//增加分级授权功能
		UserInfo userInfo = userService.getCurrentUserInfo();
		List<TreeNode> nodeLst = new ArrayList<TreeNode>(0);
		String treeNodes = "";
		//orgList = orgManager.queryOrgsByHa(Const.IS_NO, userInfo.getOrgId());
		if(userInfo.getIsAdmin()||userInfo.getIsSubmit()){
			orgList = userService.getOrgsByOrgSyscode("001006013", "0");
			topOrgcodelength = 9;
		}
		else{
			orgList = userService.getSelfAndChildOrgsByOrgSyscodeByHa("001999", "0", "8a928a703bb11098013bb6756e9a004c");
		topOrgcodelength = 6;
		}
		String codeRule = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		if(orgList != null && !orgList.isEmpty()){
			TreeNode node;
			String parentCode;
			for(TUumsBaseOrg org : orgList){
				node = new TreeNode();
				node.setId(org.getOrgSyscode());
				parentCode = this.getParentCode(org.getOrgSyscode(), codeRule);
				node.setPid(org.getOrgSyscode().length() == topOrgcodelength?"-1":parentCode);
				node.setComplete(true);
				node.setIsexpand(org.getOrgSyscode().length() == topOrgcodelength?true:false);
				node.setShowcheck(false);
				node.setText(org.getOrgName());
				node.setValue(org.getOrgId());
				nodeLst.add(node);
			}
		}
		treeNodes = JsonUtil.fromTreeNodes(nodeLst);
		this.getResponse().setContentType("application/json");
		this.getResponse().getWriter().write(treeNodes);
		
		return null;
	}
	
	/**
	 * 获取组织机构树
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getOrgTree2() throws Exception {
		//增加分级授权功能
		UserInfo userInfo = userService.getCurrentUserInfo();
		List<TreeNode> nodeLst = new ArrayList<TreeNode>(0);
		String treeNodes = "";
		orgList = userService.queryOrgsByHa(Const.IS_NO, userInfo.getOrgId());
		topOrgcodelength = userService.findTopOrgCodeLength();
		String codeRule = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		if(orgList != null && !orgList.isEmpty()){
			TreeNode node;
			String parentCode;
			for(TUumsBaseOrg org : orgList){
				node = new TreeNode();
				node.setId(org.getOrgSyscode());
				parentCode = this.getParentCode(org.getOrgSyscode(), codeRule);
				node.setPid(org.getOrgSyscode().length() == topOrgcodelength?"-1":parentCode);
				node.setComplete(true);
				node.setIsexpand(org.getOrgSyscode().length() == topOrgcodelength?true:false);
				node.setShowcheck(true);
				node.setText(org.getOrgName());
				node.setValue(org.getOrgId());
				nodeLst.add(node);
			}
		}
		treeNodes = JsonUtil.fromTreeNodes(nodeLst);
		this.getResponse().setContentType("application/json");
		this.getResponse().getWriter().write(treeNodes);
		
		return null;
	}
	/**
	 * 改变用户组织机构
	 * 
	 * @return
	 */
	public String moveuser() {
	//	userManager.changeUsersOrg(userId, this.moveOrgId);
		model=userService.getBaseUserByUserId(userId);
		TUumsBaseUser userModel = new TUumsBaseUser();
		//userModel.setUserId(model.getUserId());
		userModel.setUserPassword(model.getUserPassword());
		userModel.setUserName(model.getUserName());
		userModel.setRest2(model.getRest2());
		userModel.setUserLoginname(model.getUserLoginname());
		userModel.setUserDescription(model.getUserDescription());
		userModel.setRest3(model.getRest3());
		userModel.setRest4(model.getRest4());
		userModel.setUserAddr(model.getUserAddr());
		userModel.setUserEmail(model.getUserEmail());
		userModel.setUserIsactive(model.getUserIsactive());
		userModel.setUserTel(model.getUserTel());
		userModel.setUserSyscode(model.getUserSyscode());
		userModel.setUserIsSupManager(model.getUserIsSupManager());
		userModel.setUserIsactive(model.getUserIsactive());
		userModel.setRest1(model.getRest1());
		userModel.setUserSequence(model.getUserSequence());
		userModel.setOrgId(this.moveOrgId);
		userModel.setOrgIds(","+this.moveOrgId+",");
		long time=new Date().getTime();
		model.setUserLoginname((model.getUserLoginname()+"*"+time));
		model.setUserSyscode((model.getUserSyscode()+"*"+time));
		model.setUserIsdel("1");
		userService.saveUserForDel(model);
//		userService.deleteUsers(userId);
		
		userService.saveUser(userModel);
		//移动用户后，将用户的首页权限为默认
		String pageName = "";
		pageName = viewModelManager.getDefaultModel(model.getUserId());//得到用户的首页权限
		if(!"".equals(pageName)  || pageName!=null){
			viewModelManager.deleteByUserName(model.getUserName());//删除用户的首页权限信息
		}
		addActionMessage("用户成功移动至新机构下");
				
		//添加日志信息
		//旧部门名称
		String orgName1 = userService.getOrgInfoByOrgId(model.getOrgId()).getOrgName();	
		//新部门名称
		String orgName2 = userService.getOrgInfoByOrgId(userModel.getOrgId()).getOrgName();
		String ip = getRequest().getRemoteAddr();		
		String logInfo = "";
		logInfo = "用户" + userModel.getUserName()+"从（" + orgName1 + "）移动到（" + orgName2 + "）";			
		this.myLogManager.addLog(logInfo, ip);
		
		return "init";
		// return renderHtml("<script> alert('用户移动成功');
		// window.dialogArguments.location='"+getRequest().getContextPath()+"/usermanage/usermanage.action?orgId='"+orgId+";
		// window.close();</script>");
	}

	/**
	 * 获取用户角色
	 * 
	 * @return
	 */
	public String getUserRole() {
		model = userService.getUserInfoByUserId(userId);
		if (Const.IS_YES.equals(model.getUserIsdel())) {
			addActionMessage("用户已经删除不能操作");
			return "noset";
		}
		List<Role> userRoleSet = userService.getUserRoleInfosByUserId( userId, Const.IS_YES);
		for (Iterator<Role> it = userRoleSet.iterator(); it
				.hasNext();) {
			TUumsBaseRole role = it.next();
			initRole += "," + role.getRoleId();
		}
		if (initRole.length() > 0) {
			initRole = initRole.substring(1);
		}
		// roleList = roleManager.getRoles();
		//roleList = roleManager.queryRoles("1");
		roleList = userService.queryRolesByHa("1",userService.getCurrentUserInfo().getOrgId());
		return "setrole";
	}

	/**
	 * 设置用户的角色
	 * 
	 * @return
	 */
	public String setUserRole() {
		userService.saveUserRoles(userId, roleId);
		addActionMessage("设置用户角色成功");
		return "init";
	}
	/**
	 * 构造岗位树
	 * @author 喻斌
	 * @date 2012-4-18 上午03:51:05
	 * @return
	 * @throws Exception
	 */
	private void buildPostTree(List<TUumsBaseOrg> orgLst,
			List<TUumsBasePostOrg> lst) throws Exception {
		List<TreeNode> nodeLst = new ArrayList<TreeNode>(0);
		if (orgLst != null && !orgLst.isEmpty()) {
			for (TUumsBaseOrg org : orgLst) {
				TreeNode node = new TreeNode();
				node.setId(org.getOrgId());
				node.setPid("-1");
				node.setShowcheck(true);
				node.setComplete(true);
				node.setIsexpand(true);
				node.setText(org.getOrgName());
				node.setValue("org");
				nodeLst.add(node);
			}
		}

		if (lst != null && !lst.isEmpty()) {
			for (TUumsBasePostOrg po : lst) {
				TreeNode node = new TreeNode();
				node.setId(po.getBasePost().getPostId() + "&" + po.getOrgId());
				node.setPid(po.getOrgId());
				node.setShowcheck(true);
				node.setComplete(true);
				node.setIsexpand(true);
				node.setText(po.getBasePost().getPostName());
				node.setValue("post");
				nodeLst.add(node);
			}
		}
//		this.treeNodes = JsonMapper.buildNormalBinder().toJson(nodeLst);
		treeNodes = JsonUtil.fromTreeNodes(nodeLst);
	}

	/**
	 * 获取用户的岗位
	 * 
	 * @return
	 */
	public String getUserPost() throws Exception{
		model = userService.getUserInfoByUserId(userId);
		if (Const.IS_YES.equals(model.getUserIsdel())) {
			addActionMessage("用户已经删除不能操作");
			return "noset";
		}
		// 2012-04-18 yubin：支持人员对应多组织机构的功能
		// List<TUumsBasePost> userPostSet =
		// postManager.getPostInfoByUserId(userId);
		UserInfo userInfo = userService.getCurrentUserInfo();
		List<TUumsBasePostUser> userPostSet = userService
				.getPostInfoByUserIdByHa(userId, userInfo.getOrgId());
		if (userPostSet != null && userPostSet.size() > 0) {
			for (Iterator<TUumsBasePostUser> it = userPostSet.iterator(); it
					.hasNext();) {
				TUumsBasePostUser post = it.next();
				// 2012-04-18 yubin：支持人员对应多组织机构的功能
				initPost += "," + post.getBasePost().getPostId() + "&"
						+ post.getOrgId();
			}
			initPost += ",";
		}
		// 2012-04-18 yubin：支持人员对应多组织机构的功能
		List<TUumsBaseOrg> orgLst = userService.getOrgInfosByUserIdByHa(
				userId, userInfo.getOrgId());
		if (orgLst != null && !orgLst.isEmpty()) {
			List<String> orgIdLst = new ArrayList<String>(0);
			for (TUumsBaseOrg org : orgLst) {
				orgIdLst.add(org.getOrgId());
			}
			List<TUumsBasePostOrg> lst = userService
					.getPostInfoByOrgIds(orgIdLst);
			this.buildPostTree(orgLst, lst);
		}

		return "setpost";
	}

	public String getTreeNodes() {
		return treeNodes;
	}

	public void setTreeNodes(String treeNodes) {
		this.treeNodes = treeNodes;
	}

	/**
	 * 设置用户的岗位
	 * 
	 * @return
	 */
	public String setUserPost() {
		userService.saveUserPosts(userId, postId);
		addActionMessage("设置用户岗位成功");
		return "init";
	}

	/**
	 * 获取用户组
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getUserGroup() throws IOException {
		model = userService.getUserInfoByUserId(userId);
		if (Const.IS_YES.equals(model.getUserIsdel())) {
			addActionMessage("用户已经删除不能操作");
			return "noset";
		}
		List<TUumsBaseGroup> userPostSet = userService.getGroupInfosByUserId(
				userId, Const.IS_YES);
		if (userPostSet.size() > 0) {
			for (Iterator<TUumsBaseGroup> it = userPostSet.iterator(); it
					.hasNext();) {
				TUumsBaseGroup group = it.next();
				initGroup += "," + group.getGroupId();
			}
			if (initGroup.length() > 0) {
				initGroup = initGroup.substring(1);
			}
		}
		// userGoupList = groupManager.getGroups();
		//采用分级授权方式
		UserInfo userInfo = userService.getCurrentUserInfo();
		userGoupList = userService.queryGroupsByHa(Const.IS_YES, userInfo.getOrgId());
		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_GROUP);
		int minLength = Integer.parseInt(codeType.substring(0,1));//最短编码
		topGroupcodelength = Integer.MAX_VALUE;//目前的最短编码，默认为最大值
		if(userGoupList != null && !userGoupList.isEmpty()){
			for(TUumsBaseGroup group : userGoupList){
				if(topGroupcodelength > group.getGroupSyscode().length()){
					topGroupcodelength = group.getGroupSyscode().length();
				}
				if(topGroupcodelength == minLength){//达到最短编码则跳出循环
					break;
				}
				
			}
		}

		return "setgroup";
	}

	/**
	 * 设置用户组
	 * 
	 * @return
	 */
	public String setUserGroup() {
		userService.saveUserGroups(userId, groupCode);
		addActionMessage("设置用户组成功");
		return "init";
	}

	/**
	 * 获取用户权限
	 * 
	 * @return
	 * @throws IOException
	 * replaced by setUserPrivil()
	 */
	@Deprecated 
	public String getPrivil() throws IOException {
		model = userService.getUserInfoByUserId(userId);
		if (Const.IS_YES.equals(model.getUserIsdel())) {
			addActionMessage("用户已经删除不能操作");
			return "noset";
		}
		List<TUumsBasePrivil> userPrivilsSet = userService.getUserPrivilsByUserId(
				userId, Const.IS_YES);
		if (userPrivilsSet.size() > 0) {
			for (Iterator<TUumsBasePrivil> it = userPrivilsSet.iterator(); it
					.hasNext();) {
				TUumsBasePrivil privil = it.next();
				initPrivil += "," + privil.getPrivilSyscode();
			}
			if (initPrivil.length() > 0) {
				initPrivil = initPrivil.substring(1);
			}
		}
		// privilList = privilManager.getPrivils();
		// systemList = systemManager.getCurrentUserSystems();
		UserInfo user = userService.getCurrentUserInfo();
		privilList = userService.getAdminPrivilInfosByUserId(user.getUserId(), Const.IS_YES, Const.IS_YES,
				Const.IS_YES);
		systemList = userService.getCurrentUserSystems(Const.IS_YES);
		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_PRIVIL);
		return "setprivil";
	}

	/**
	 * 设置用户权限
	 * @author:邓志城
	 * @date:2011-1-7 下午03:22:42
	 * @return
	 * @throws Exception
	 */
	public String setUserPrivil() throws Exception {
		try{
			List<TUumsBasePrivil> lst = userService.getUserPrivilsByUserId(userId, Const.IS_YES);
			StringBuilder ids = new StringBuilder();
			if(lst != null && !lst.isEmpty()) {
				for(TUumsBasePrivil privil : lst) {
					ids.append(privil.getPrivilSyscode()).append(",");
				}
			}
			if(ids.length() > 0) {
				ids.deleteCharAt(ids.length() - 1);
			}
			getRequest().setAttribute("id", ids.toString());
			getRequest().setAttribute("title", "设置资源权限");
			//得到当前用户资源权限
			List<TempPo> data = new LinkedList<TempPo>();
			String ruleCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_PRIVIL);
			systemList = userService.getCurrentUserSystems("1");
			if(!systemList.isEmpty()) {
				TUumsBaseSystem system = systemList.get(0);
				TempPo root = new TempPo();
				root.setId("1");
				root.setName(system.getSysName());
				root.setType("system");
				root.setParentId("0");
				data.add(root);
				String parentId = "1";
				List<ToaUumsBaseOperationPrivil> privilList = basePrivilManager.getCurrentUserPrivilLst(true);
				for(ToaUumsBaseOperationPrivil privil : privilList) {
					TempPo po = new TempPo();
					po.setId(privil.getPrivilSyscode());
					po.setName(privil.getPrivilName());
					po.setType(privil.getPrivilType());
					String code = privil.getPrivilSyscode();
					String parentCode = userService.findFatherCode(code, ruleCode, "0");
					if("0".equals(parentCode)) {
						parentId = "1";
					} else {
						parentId = parentCode;
					}
					po.setParentId(parentId);
					data.add(po);
				}
			} else {
				getRequest().setAttribute("title", "设置权限操作仅限于管理员身份");
			}
			getRequest().setAttribute("data", data);
		}catch(Exception e) {
			logger.error("设置用户资源时发生异常", e);
			throw e;
		}
		return "priviltree";
	}
	
	/**
	 * 设置用户权限
	 * 
	 * @return
	 * replaced by saveUserPrivil()
	 */
	@Deprecated
	public String setPrivil() {
		//userManager.saveUserPrivils(userId, privelCode);
		if(privelCode != null && !"".equals(privelCode)){
			String[] privilCodes = privelCode.split("\\|");
			//解析所有子级资源权限
			if(privilCodes.length > 1 && privilCodes[1] != null && !"".equals(privilCodes[1])){
				String[] parseSubPrivilCodes = privilCodes[1].split(",");
				for(String parseSubPrivilCode : parseSubPrivilCodes){
					List<TUumsBasePrivil> subPrivilCodeLst = userService.getPrivilInfosByPrivilCode(parseSubPrivilCode + "%", Const.IS_YES);
					for(int i = 0; i < subPrivilCodeLst.size(); i++){
						privilCodes[0] = privilCodes[0] + "," + subPrivilCodeLst.get(i).getPrivilSyscode();
					}
				}
			}
			//若用户没有选择任何权限
			if(privilCodes.length == 0){
				privilCodes = new String[]{null};
			}else if(privilCodes[0].startsWith(",")){
				privilCodes[0] = privilCodes[0].substring(1);
			}
			userService.saveUserPrivilsByPrivilCodes(userId, privilCodes[0]);
		}
		addActionMessage("设置权限成功");
		return "init";
	}

	/**
	 * 为用户设置资源权限
	 * @author:邓志城
	 * @date:2011-1-7 下午05:11:18
	 * @return
	 * @throws Exception
	 */
	public String saveUserPrivil() throws Exception {
		try{
			userService.saveUserPrivilsByPrivilCodes(userId, privilCode);
			addActionMessage("设置权限成功");
			return "init";
		}catch(Exception e) {
			logger.error("设置用户权限时发生错误", e);
			throw e;
		}
	}
	
	/**
	 * 获取用户分页信息
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getCopyPage() throws IOException {
		model = userService.getUserInfoByUserId(userId);
		if (Const.IS_YES.equals(model.getUserIsdel())) {
			addActionMessage("用户已经删除不能操作");
			return "noset";
		}
		// page = userManager.queryUsers(page);
		//orgList = orgManager.getOrgs(Const.IS_NO);
		//userList = userManager.getUsers(Const.IS_NO, Const.IS_YES);
		//采用分级授权方式
		//int i = userInfo.getSupOrgCode().length();
		int i = userService.findTopOrgCodeLength();
		String random = "%";
		//orgList = orgManager.getOrgsByOrgSyscode(random, Const.IS_NO);
		orgList=userService.getOrgsByOrgSyscodeByHa(random, Const.IS_NO, userService.getCurrentUserInfo().getOrgId());
		for(int j=0; j<orgList.size();){
			if(orgList.get(j).getOrgSyscode().length() != i){
				orgList.remove(j);
			}else{
				j++;
			}
		}
		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		return "copyprivil";
	}

	/**
	 * 复制用户权限
	 * 
	 * @return
	 */
	public String setCopy() {
		//修改为延迟加载的方式，copytoid为人员Id,人员Id,...|组织机构code,组织机构code,...
		if(copytoid != null && !"".equals(copytoid)){
			String[] orgCodes = copytoid.split("\\|");
			//解析所有人员Id
			if(orgCodes.length > 1 && orgCodes[1] != null && !"".equals(orgCodes[1])){
				String[] parseUserCodes = orgCodes[1].split(",");
				for(String parseUserCode : parseUserCodes){
					//List<TUumsBaseUser> userIdLst = userManager.getUsersOfOrgAndChildByOrgcode(parseUserCode, Const.IS_YES, Const.IS_NO);
					
				//TUumsBaseOrg org=userManager.getSupOrgByUserIdByHa(userManager.getCurrentUserInfo().getUserId());
				//syscode=org.getOrgSyscode();
					
				//String haid=org.getOrgId();
			List<TUumsBaseUser> userIdLst = userService.getUsersOfOrgAndChildByOrgcodeByHa(parseUserCode, Const.IS_YES, Const.IS_NO,userService.getCurrentUserInfo().getOrgId());

					for(int i = 0; i < userIdLst.size(); i++){
						orgCodes[0] = orgCodes[0] + "," + userIdLst.get(i).getUserId();
					}
				}
			}
			if(orgCodes[0].startsWith(",")){
				orgCodes[0] = orgCodes[0].substring(1);
			}
			userService.copyUserPrivils(userId, orgCodes[0]);
			addActionMessage("复制用户权限成功");
		}
		mark = "1";
		return "init";
	}

	/**
	 * 获取用户类型页面
	 * 
	 * @return
	 */
	public String getUserType() {
		systemList = userService.getAllSystems();
		return "usertype";
	}

	/**
	 * 比较编码唯一性
	 * 
	 * @author 蒋国斌
	 * @date 2009-2-25 下午02:17:49
	 * @return String
	 * @throws Exception
	 */
	public String compareCode() throws Exception {
		boolean flag = userService.compareUserCode(syscode);
		if (flag == true) {
			message = "111";
		} else
			message = "000";
		return this.renderText(message);
	}

	/**
	 * 检查用户登录名是否已使用
	 * @author 喻斌
	 * @date Jun 17, 2009 5:50:17 PM
	 * @return
	 * @throws Exception
	 */
	public String checkLoginName() throws Exception {
		boolean flag = userService.compareUserName(loginname);
		if (flag == true) {
			logininfo = "111";
		} else
			logininfo = "000";
		return this.renderText(logininfo);
	}
	
	
	/**
	 * author:luosy
	 * description: 初始化用户名密码和usbkey为password
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String initUsbKeyAndPsw() throws Exception {
		
		List<TUumsBaseUser> allUser =  userService.getAllUserInfos();
		Md5 md5 = new Md5();
		for(int i=0;i<allUser.size();i++){
			TUumsBaseUser u = allUser.get(i);
			String psw = "1_"+u.getUserUsbkey();
			String desPwd = md5.getMD5ofStr(psw);
			u.setUserPassword(desPwd);
			userService.saveUser(u);
		}
		
		return this.renderText("设置成功，所有用户密码设置为1。");
	}
	
	/**
	 * 初始化人员的应用系统管理员信息
	 * @author 喻斌
	 * @date Jun 26, 2009 11:43:50 AM
	 * @return
	 * @throws Exception
	 */
	public String initSystemAdminInfo() throws Exception {
		//为了保证Ldap和数据库的一致性，将人员保存与人员管理员设置操作分离开
		//this.prepareModel();
		if (model.getUserId() != null && !"".equals(model.getUserId())) {
			List<TUumsBaseSysmanager> sysmanager = userService.getSystemAdminInfoByUserId(model.getUserId());
			sysmanagerid = "";
			managerid = "";
			dataManagerId = "";
			for (Iterator<TUumsBaseSysmanager> it = sysmanager
					.iterator(); it.hasNext();) {
				TUumsBaseSysmanager syaman = it.next();
				if (Const.USER_TYPE_SYSTEM.equals(syaman
						.getSmUserType())) {
					sysmanagerid += ","
							+ syaman.getBaseSystem().getSysId();
				}
				if (Const.USER_TYPE_MANAGER.equals(syaman
						.getSmUserType())) {
					managerid += ","
							+ syaman.getBaseSystem().getSysId();
				}
				if (IUserService.GradeAuthorizationUser.equals(syaman
						.getSmUserType())) {
					dataManagerId += ","
							+ syaman.getBaseSystem().getSysId();
				}
			}
			if (sysmanagerid.length() > 0) {
				sysmanagerid = sysmanagerid.substring(1);
			}
			if (managerid.length() > 0) {
				managerid = managerid.substring(1);
			}
			if (dataManagerId.length() > 0) {
				dataManagerId = dataManagerId.substring(1);
			}
		}
		systemList = userService.querySystems(Const.IS_YES);
		return "usertype";
	}
	
	/**
	 * 得到指定系统下的第一级资源或指定资源code的下一级资源信息，用于ajax延迟加载
	 * @author 喻斌
	 * @date Jul 10, 2009 1:15:47 PM
	 * @return
	 * @throws Exception
	 */
	public String lazyLoadPrivil() throws Exception {
		UserInfo user = userService.getCurrentUserInfo();
		StringBuffer code = new StringBuffer("");
		//查找应用系统下的第一级资源权限
		if(this.systemCode != null && !"".equals(this.systemCode)){
			int codeLength = userService.findChildCodeLength(Const.RULE_CODE_PRIVIL, "");
			for(int i=0; i<codeLength; i++){
				code.append("_");
			}
			privilList = userService.getAdminPrivilInfosByUserAndPrivilAndSystem(user.getUserId(), code.toString(), systemCode, Const.IS_YES, Const.IS_YES, Const.IS_YES);
		//查找资源权限下的下一级资源权限
		}else if(this.privilCode != null && !"".equals(this.privilCode)){
			code.append(this.privilCode);
			int codeLength = userService.findChildCodeLength(Const.RULE_CODE_PRIVIL, privilCode);
			int iteratorIndex = codeLength - this.privilCode.length();
			//如果已经达到最大编码
			if(iteratorIndex == 0){
				iteratorIndex = 1;
			}
			for(int i=0; i<iteratorIndex; i++){
				code.append("_");
			}
			privilList = userService.getAdminPrivilInfosByUserAndPrivil(user.getUserId(), code.toString(), Const.IS_YES, Const.IS_YES, Const.IS_YES);
		}
		String jasonStr = new String("[");
		if(privilList != null && !privilList.isEmpty()){
			StringBuffer bufStr = new StringBuffer("");
			for(TUumsBasePrivil privil : privilList){
				bufStr.append(",{id:'")
						.append(privil.getPrivilId())
						.append("',name:'")
						.append(privil.getPrivilName())
						.append("',code:'")
						.append(privil.getPrivilSyscode())
						.append("',system:'")
						.append(privil.getBasePrivilType().getBaseSystem().getSysSyscode())
						.append("'}");
			}
			jasonStr = jasonStr + bufStr.substring(1);
		}
		jasonStr = jasonStr + "]";
		this.renderText(jasonStr);
		return null;
	}

	/**
	 * 设置超级管理员
	 * 
	 * @return
	 */
	public String setSuperAdmin() {

		return RELOAD;
	}
	
	//子级组织机构和人员树延迟加载（延迟加载，ajax调用）
	public String lazyLoadUserAndChildOrgs() throws Exception {
		TUumsBaseOrg orgInfo = userService.getOrgInfoBySyscode(orgSyscode);
		userList = userService.getUserInfoByOrgId(orgInfo.getOrgId(), Const.IS_YES, Const.IS_NO);
		int i = userService.findChildCodeLength(Const.RULE_CODE_ORG, orgInfo.getOrgSyscode());
		if(i == orgInfo.getOrgSyscode().length()){
			orgList = new ArrayList<TUumsBaseOrg>();
		}else{
			//orgList = orgManager.getOrgsByOrgSyscode(orgInfo.getOrgSyscode() + "%", Const.IS_NO);
			orgList = userService.getOrgsByOrgSyscodeByHa(orgInfo.getOrgSyscode() + "%", Const.IS_NO, userService.getCurrentUserInfo().getOrgId());
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
				//在资源复制选择人员的页面内，人员会显示在多个机构下  解决bug 0000049269
				//通过orgsyscode 比对是否是默认机构下的人员  不是则不添加到树
				TUumsBaseOrg tempOrgInfo = userService.getOrgInfoByOrgId(user.getOrgId());
				if(tempOrgInfo.getOrgSyscode().equals(orgSyscode)){
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
	 * 獲取用戶Id
	 * @return
	 */
	public String getUserId(){
		String userId=null;
		User user=new User();
		try {
			user=userService.getCurrentUser();
			userId=user.getUserId();
			return renderText(userId);
		} catch (Exception e) {
			return renderText("-1");//-1表示当前系统获取不到“当前处理人”
		}
		
	}
	/**
	 * 通过领导联系人的id获取领导的id
	 * @return
	 * @throws SQLException
	 */
	public String isLeaderRelation() throws SQLException{
		String ret="";
		if (userId != null && !("".equals(userId))){
			String[] userIds = userId.split(",");
			for (int i = 0; i < userIds.length; i++) {
			String userName=userService.getUserNameByUserId(userIds[i]);
			String userLeaderName=userService.getLeaderByuserId(userIds[i]);
			if(userLeaderName!=null && !"".equals(userLeaderName)){
	          ret=ret+userName+",";
			 }
			}
		}
	     return renderText(ret);
	}
	
	
	public String checkCardId()
    throws Exception
  {
    boolean flag = userService.compareRest4(rest4, userId);
    if (flag)
      this.logininfo = "111";
    else
      this.logininfo = "000";
    return renderText(this.logininfo);
  }

	public TUumsBaseUser getModel() {
		return model;
	}

	public List<TUumsBaseUser> getUserList() {
		return userList;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<TUumsBaseOrg> getOrgList() {
		return orgList;
	}

	public void setExtOrgId(String extOrgId) {
		this.extOrgId = extOrgId;
	}

	public String getCodeType() {
		return codeType;
	}

	public String getExtOrgId() {
		return extOrgId;
	}

	public List<TUumsBaseRole> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<TUumsBaseRole> roleList) {
		this.roleList = roleList;
	}

	public List<TUumsBasePost> getPostList() {
		return postList;
	}

	public void setPostList(List<TUumsBasePost> postList) {
		this.postList = postList;
	}

	public List<TUumsBaseGroup> getUserGoupList() {
		return userGoupList;
	}

	public void setUserGoupList(List<TUumsBaseGroup> userGoupList) {
		this.userGoupList = userGoupList;
	}

	public String getInitRole() {
		return initRole;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getInitPost() {
		return initPost;
	}

	public void setInitPost(String initPost) {
		this.initPost = initPost;
	}

	public List<TUumsBasePost> getWholePostList() {
		return wholePostList;
	}

	public String getInitGroup() {
		return initGroup;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public List<TUumsBasePrivil> getPrivilList() {
		return privilList;
	}

	public void setPrivilList(List<TUumsBasePrivil> privilList) {
		this.privilList = privilList;
	}

	public MyLogManager getMyLogManager() {
		return myLogManager;
	}
	@Autowired
	public void setMyLogManager(MyLogManager myLogManager) {
		this.myLogManager = myLogManager;
	}

	public String getCopytoid() {
		return copytoid;
	}

	public void setCopytoid(String copytoid) {
		this.copytoid = copytoid;
	}

	public String getPrivelCode() {
		return privelCode;
	}

	public void setPrivelCode(String privelCode) {
		this.privelCode = privelCode;
	}

	public String getInitPrivil() {
		return initPrivil;
	}

	public void setInitPrivil(String initPrivil) {
		this.initPrivil = initPrivil;
	}

	public Map getUserTypeMap() {
		return userTypeMap;
	}

	public List<TUumsBaseSystem> getSystemList() {
		return systemList;
	}

	public String getManagerid() {
		return managerid;
	}

	public String getSysmanagerid() {
		return sysmanagerid;
	}

	public void setSysmanagerid(String sysmanagerid) {
		this.sysmanagerid = sysmanagerid;
	}

	public void setManagerid(String managerid) {
		this.managerid = managerid;
	}

	public String getAudittype() {
		return audittype;
	}

	public void setAudittype(String audittype) {
		this.audittype = audittype;
	}

	public Page<TUumsBaseUser> getPage() {
		return page;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSyscode() {
		return syscode;
	}

	public void setSyscode(String syscode) {
		this.syscode = syscode;
	}

	public String getLogininfo() {
		return logininfo;
	}

	public void setLogininfo(String logininfo) {
		this.logininfo = logininfo;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getPubkey() {
		return pubkey;
	}

	public void setPubkey(String pubkey) {
		this.pubkey = pubkey;
	}

	public String getHasPasswordEdited() {
		return hasPasswordEdited;
	}

	public void setHasPasswordEdited(String hasPasswordEdited) {
		this.hasPasswordEdited = hasPasswordEdited;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getSelectname() {
		return selectname;
	}

	public void setSelectname(String selectname) {
		try {
			selectname = URLDecoder.decode(selectname, "utf-8");
			selectname = URLDecoder.decode(selectname, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("流程名称转码异常！");
		}
		this.selectname = selectname;
	}

	public String getSelectloginname() {
		return selectloginname;
	}

	public void setSelectloginname(String selectloginname) {
		try {
			selectloginname = URLDecoder.decode(selectloginname, "utf-8");
			selectloginname = URLDecoder.decode(selectloginname, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("流程名称转码异常！");
		}
		this.selectloginname = selectloginname;
	}

	public String getSelectorg() {
		return selectorg;
	}

	public void setSelectorg(String selectorg) {
		this.selectorg = selectorg;
	}

	public String getIsdel() {
		return isdel;
	}

	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}
	/*
	 * public String getOrgname() { return orgname; } public void
	 * setOrgname(String orgname) { this.orgname
	 * =orgManager.getOrg(orgId).getOrgName() ; }
	 */

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getPrivilCode() {
		return privilCode;
	}

	public void setPrivilCode(String privilCode) {
		this.privilCode = privilCode;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Map getUserActiveTypeMap() {
		return userActiveTypeMap;
	}

	public void setUserActiveTypeMap(Map userActiveTypeMap) {
		this.userActiveTypeMap = userActiveTypeMap;
	}

	public String getMoveOrgId() {
		return moveOrgId;
	}

	public void setMoveOrgId(String moveOrgId) {
		this.moveOrgId = moveOrgId;
	}

	/**
	 * @return the orgSyscode
	 */
	public String getOrgSyscode() {
		return orgSyscode;
	}

	/**
	 * @param orgSyscode the orgSyscode to set
	 */
	public void setOrgSyscode(String orgSyscode) {
		this.orgSyscode = orgSyscode;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getCurrentUSBKEY() {
		return currentUSBKEY;
	}

	public void setCurrentUSBKEY(String currentUSBKEY) {
		this.currentUSBKEY = currentUSBKEY;
	}

	public String getArchiveManagerId() {
		return archiveManagerId;
	}

	public void setArchiveManagerId(String archiveManagerId) {
		this.archiveManagerId = archiveManagerId;
	}

	public String getDataManagerId() {
		return dataManagerId;
	}

	public void setDataManagerId(String dataManagerId) {
		this.dataManagerId = dataManagerId;
	}
	
	/**
	 * @return the topOrgcodelength
	 */
	public int getTopOrgcodelength() {
		return topOrgcodelength;
	}

	/**
	 * @param topOrgcodelength the topOrgcodelength to set
	 */
	public void setTopOrgcodelength(int topOrgcodelength) {
		this.topOrgcodelength = topOrgcodelength;
	}

	/**
	 * @return the topGroupcodelength
	 */
	public int getTopGroupcodelength() {
		return topGroupcodelength;
	}

	/**
	 * @param topGroupcodelength the topGroupcodelength to set
	 */
	public void setTopGroupcodelength(int topGroupcodelength) {
		this.topGroupcodelength = topGroupcodelength;
	}

	public String getRePassword() {
		return rePassword;
	}

	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public List<TUumsBaseOrg> getUserOrgList() {
		return userOrgList;
	}

	public void setUserOrgList(List<TUumsBaseOrg> userOrgList) {
		this.userOrgList = userOrgList;
	}
	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getOldOrgid() {
		return oldOrgid;
	}

	public void setOldOrgid(String oldOrgid) {
		this.oldOrgid = oldOrgid;
	}

	public String getRest4() {
		return rest4;
	}

	public void setRest4(String rest4) {
		this.rest4 = rest4;
	}
}