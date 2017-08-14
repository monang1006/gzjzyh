package com.strongit.oa.systemset;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.compass.core.json.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.strongit.oa.attachment.AttachmentHelper;
import com.strongit.oa.bo.ToaBussinessModulePara;
import com.strongit.oa.bo.ToaIMEI;
import com.strongit.oa.bo.ToaSysmanageProperty;
import com.strongit.oa.bo.ToaSystemset;
import com.strongit.oa.common.eform.IEFormService;
import com.strongit.oa.common.eform.model.EForm;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.paramconfig.ConfigModule;
import com.strongit.oa.paramconfig.ParamConfigService;
import com.strongit.oa.prsnfldr.privateprsnfldr.PrsnfldrFileManager;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.prsnfldr.util.Round;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.smsplatform.SmsPlatformManager;
import com.strongit.oa.util.ListUtils;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "systemset.action", type = ServletActionRedirectResult.class) })
public class SystemsetAction extends BaseActionSupport {
	private static final long serialVersionUID = 1L;
	private List<EForm> list=new ArrayList<EForm>();
	private List<ToaSysmanageProperty> itemList=new ArrayList<ToaSysmanageProperty>();
	private List<ToaSysmanageProperty> mapItemList=new ArrayList<ToaSysmanageProperty>();
	private ToaSystemset model = new ToaSystemset();
	private SystemsetManager manager;
	private SendDocManager sendDocManager;
	@Autowired private PrsnfldrFileManager prsnfldrFileManager;// 注入文件管理对象
	private IEFormService eform;			//电子表单服务
	private static String FORMTYPE="SF";	//表单类型
	private String parentFormId;			//父流程的启动表单ID
	private String subFormId;				//子流程的启动表单ID
	private String params;
	private String dataType;				//要映射字段的数据类型
	
	private ParamConfigService pcService;
	private String baseFolderSize; 		//文件柜空间大小
	private String baseMsgSize;			//消息空间大小
	private String calendarAttSize; 		//日程附件大小
	private String notifyAttSize;			//通知公告附件大小
	private String refreshTime;             //桌面自动刷新时间间隔
	
	
	private File uploads;

	/**
	 * 手机imei安全加固参数
	 */
	private String imei_Id;
	private String userId;
	private String userNameParam;
	private String imeiCode;//imei码
	private String isOpen;//是否开启 1:开启 0:关闭
	private Page<ToaIMEI> imeiPage=new Page(15,true);
	private Map<String,String> stateMap=new HashMap<String, String>();
	private ToaIMEI imeiModel=new ToaIMEI();
	
	/**
	 * 短信模块设置信息列表
	 */
	private List<ToaBussinessModulePara> moduleList;
	private String isViewChildSetOpen;	//是否允许看到下级机构数据开关
	
	public SystemsetAction(){

	}
	/**
	 * 注入manager区域
	 * --begin--
	 */
	@Autowired
	private SmsPlatformManager platformManager;
	@Autowired IUserService userService;
	/**
	 * --end---
	 */
	@Override
	public String delete() throws Exception {
		return null;
	}

	/**
	 * 注释掉prepareModel方法,Action中的input、save方法在执行之前都会自动
	 * 先调用preparemodel方法。
	 * modify by dengzc 2011年1月20日14:01:40
	 */
	@Override
	public String input() throws Exception {
		//prepareModel();
		try {
			baseFolderSize = pcService.getConfigSize(ConfigModule.PRSNFLDR);
			baseMsgSize = pcService.getConfigSize(ConfigModule.MESSAGE);
			calendarAttSize = pcService.getConfigSize(ConfigModule.CALENDAR);
			notifyAttSize = pcService.getConfigSize(ConfigModule.NOTIFY);
			isViewChildSetOpen = String.valueOf(userService.isViewChildOrganizationEnabeld());
			
			
			byte[] buf = model.getIpadbg();
	   		if(buf != null) {
	   			InputStream is = FileUtil.ByteArray2InputStream(buf);
	   			String path = AttachmentHelper.saveFile(getRequest().getContextPath(), is, "temp.jpg");
	   			logger.info("生成附件：" + path);
	   			model.setTempFilePath(path);   			
	   		}
		} catch (RuntimeException e) {
			logger.error("加载全局设置信息出错", e);
		}
		double totalSize = prsnfldrFileManager.getTotalSize();
		int stringTotalSize = (int) (totalSize / (1024 * 1024))+1;
				// 已经用掉的空间大小totalSize/(1024*1024)+"MB";
		getRequest().setAttribute("stringTotalSize", stringTotalSize);
		return SUCCESS;
	}

	@Override
	public String list() throws Exception {
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		this.model=this.manager.getSystemset();
		if(null==this.model)
		{
			ToaSystemset systemset = new ToaSystemset(null,"0","0",0,16,"false","0","0","0","0","0",12,12,12,"0","0","0",30);
			this.model = systemset;
			this.manager.addSystemset(systemset);
		}
	}
	/**
	 * 获取office的控制
	 * @author niwy
	 * @date  2014年12月17日14:00:22
	 * @return
	 * @throws Exception
	 */
	public String getOfficeNew() throws Exception{
		SystemsetManager sysmanager = new SystemsetManager();
		String officeNew=sysmanager.getOfficeSet();
		return officeNew;
	}
	/*
	 * Description:数据映射
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Feb 25, 2010 11:21:29 AM
	 */
	public String mapdata() throws Exception{
		list=eform.getFormTemplateList(FORMTYPE);
		if(parentFormId==null||"".equals(parentFormId)){	//父流程表单ID为空
			if(list!=null&&list.size()>0){	
				parentFormId=String.valueOf(list.get(0).getId());
			}
		}
		if(subFormId==null||"".equals(subFormId)){	//子流程表单ID为空
			if(list!=null&&list.size()>0){	
				subFormId=String.valueOf(list.get(0).getId());
			}
		}
		if(parentFormId!=null&&!"".equals(parentFormId)){	//父流程表单ID不为空
			String tableName=sendDocManager.getTNByFormId(parentFormId);//根据表单ID获取业务表
			String subTableName=sendDocManager.getTNByFormId(subFormId);//根据表单ID获取业务表
			//itemList=manager.getCreatedItemsAndMapItemsByValue(tableName, subTableName);
			if(tableName!=null&&!"".equals(tableName))
				itemList=manager.getFormFiledsAndMapItemsByValue(tableName, subTableName, parentFormId);
		}
		return "mapdata";
	}
	
	/*
	 * Description:字段映射
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Feb 25, 2010 2:53:24 PM
	 */
	public String changeMap() throws Exception{
		//String[] subTableName=sendDocManager.getTNByFormId(subFormId);//根据表单ID获取表名
		//mapItemList=itemManager.getAllCreatedItemsByValueAndDataType(subTableName,dataType);
		mapItemList=manager.getSubFormFileds(subFormId,dataType);
		return "changemap";
	}
	
	/*
	 * Description:保存映射关系
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Feb 26, 2010 9:31:46 AM
	 */
	public String saveMapping() throws Exception{
		manager.saveMapping(params,parentFormId,subFormId);
		return null;
	}
	
	@Override
	public String save() throws Exception {
		if(model.getRefreshTime()== null || "".equals(model.getRefreshTime())){
			model.setRefreshTime(0);
		}
        Image srcImage = null;   
        BufferedImage tagImage = null;   
        FileOutputStream fileOutputStream = null;   
        JPEGImageEncoder encoder = null;   

		if(uploads != null) {//背景图片
   			FileInputStream fis = null;
   			try {
   				File file = File.createTempFile("test", ".jpg");// 创建临时文件
   				String tempFile = file.getPath();
   				logger.info(tempFile);
// 			    DocTempItemManager.makeSmallImage(uploads, tempFile);

   				//绘制图像
   	            srcImage = ImageIO.read(uploads);   
   	            tagImage = new BufferedImage(srcImage.getWidth(null),srcImage.getHeight(null),BufferedImage.TYPE_INT_RGB); //生成缩略图   
   	            tagImage.getGraphics().drawImage(srcImage,0,0,srcImage.getWidth(null),srcImage.getHeight(null),null);   

	   	        fileOutputStream = new FileOutputStream(tempFile); //临时图片文件的的输出流  
	            encoder = JPEGCodec.createJPEGEncoder(fileOutputStream);   
	            encoder.encode(tagImage);//解析后，临时图片绘制成功
	            fileOutputStream.close();   
	            fileOutputStream = null;   
				fis = new FileInputStream(file);
				byte[] buf = FileUtil.inputstream2ByteArray(fis);
				
				model.setIpadBgUpdate(new Date());
				model.setIpadbg(buf);
 				//file.delete();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}finally {
				if(fis != null) {
					fis.close();
				}
			}
   		}
		
		
		manager.editSystemset(model);
		
		pcService.save(null, baseFolderSize, ConfigModule.PRSNFLDR);
		pcService.save(null, baseMsgSize, ConfigModule.MESSAGE);
		pcService.save(null, calendarAttSize, ConfigModule.CALENDAR);
		pcService.save(null, notifyAttSize, ConfigModule.NOTIFY);
		
		//userService.setViewChildOrganizationEnabled(Boolean.parseBoolean(isViewChildSetOpen));//修改开关设置
		double totalSize = prsnfldrFileManager.getTotalSize();
		int stringTotalSize = (int) (totalSize / (1024 * 1024))+1;
				// 已经用掉的空间大小totalSize/(1024*1024)+"MB";
		getRequest().setAttribute("stringTotalSize", stringTotalSize);
	//	return SUCCESS;
		return renderText("ok");
	}
	/**
	 * 向ios推送的消息模块设置页面
	 * @description
	 *
	 * @author  hecj
	 * @date    Jan 11, 2014 2:13:27 PM
	 * @param   
	 * @return  String
	 * @throws IOException 
	 * @throws
	 */
	public String pushSetting(){
		return "pushSetting";
	}
	/**
	 * 保存推送设置
	 * @description
	 *
	 * @author  hecj
	 * @date    Jan 13, 2014 7:54:14 PM
	 * @param   
	 * @return  void
	 * @throws
	 */
	public String saveSetting(){
		String code=getRequest().getParameter("pushModuleNo");
		String check=getRequest().getParameter("checkedFlag");
		JSONArray array=new JSONArray();
		JSONObject json=new JSONObject();
		String[] settingCode=code.split(";");
		String[] checkArray=check.split(";");
		if(settingCode!=null&&settingCode.length>0){
			JSONObject obj=null;
			for(int m=0;m<settingCode.length;m++){
				obj=new JSONObject();
				obj.put("mCode",settingCode[m]);
				obj.put("mStatus",checkArray[m]==null?"off":checkArray[m]);
				array.add(obj);
			}
		}
		if(array!=null&&array.size()>0){
			model=manager.getSystemset();
			json.put("rows", array);
			model.setPushSetting(json.toString());
			manager.editSystemset(model);
			return renderText("OK"); 
		}else{
			return renderText("ERROR"); 
		}
	}
	/**
	 * 通过json读取页面设置
	 * @description
	 *
	 * @author  hecj
	 * @date    Jan 11, 2014 2:48:31 PM
	 * @param   
	 * @return  String
	 * @throws IOException 
	 * @throws
	 */
	public String pushSettingToJson() throws IOException{
		JSONArray jsonArr=new JSONArray();
		JSONArray saveArr=new JSONArray();
		JSONObject json=null;
		JSONObject rows=new JSONObject();
		String rtn=null;
		this.model=this.manager.getSystemset();
		if(model!=null){
			moduleList=platformManager.getAllObj();
			HashMap<String,String> nameMap=new HashMap<String, String>();
			if(moduleList!=null&&moduleList.size()>0){
				Iterator<ToaBussinessModulePara> it=moduleList.iterator();
				ToaBussinessModulePara para=null;
				while(it.hasNext()){
					para=it.next();
					nameMap.put(para.getBussinessModuleCode(), para.getBussinessModuleName());
				}
			}
			if(model.getPushSetting()==null||model.getPushSetting().length()==0){
				/**
				 * 如果为空则读取webservice.properties的默认设置
				 */
				Properties prop= PropertiesLoaderUtils.loadProperties(new ClassPathResource("webserviceaddress.properties"));
				String  mlist= prop.getProperty("ios_pushnotify_module_list");
				/**
				 * 如果中文逗号就转换
				 */
				mlist=mlist.replaceAll("，", ",");
				/**
				 * 将webserviceAddress.properties
				 * 的初始化参数保存成json格式，默认推送消息模块都是关闭的 0:关闭 1:打开
				 */
				if(mlist!=null&&mlist.indexOf(",")!=-1){
					String[] arr=mlist.split(",");
					for(String code:arr){
						if(nameMap.get(code)!=null){
							json=new JSONObject();
							json.put("mName",nameMap.get(code));
							json.put("mCode", code);
							json.put("mStatus", "0");
							jsonArr.add(json);
						}
					}
				}
				rows.put("rows",jsonArr);
				rtn=rows.toString();
				
			}else{
				rtn=model.getPushSetting();
				JSONObject expJson=JSONObject.fromObject(rtn);
				JSONArray newArr=new JSONArray();
				JSONObject newJson=new JSONObject();
				if(expJson!=null){
					JSONArray expArr=(JSONArray)expJson.get("rows");
					for(int m=0;m<expArr.size();m++){
						JSONObject obj=(JSONObject)expArr.get(m);
						obj.put("mName",nameMap.get(obj.get("mCode")));
						newArr.add(obj);
					}
					newJson.put("rows", newArr);
					rtn=newJson.toString();
				}
			}
		}
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(rtn);
		return null;
	}
	/**
	 * 手机安全加固列表
	 * @description
	 *
	 * @author  hecj
	 * @date    Jan 15, 2014 2:56:48 PM
	 * @param   
	 * @return  String
	 * @throws
	 */
	public String imeiSettingList(){
		List<ToaIMEI> tmpList=manager.findIMEIList(userId,userNameParam,imeiCode, isOpen);
		List<ToaIMEI> imeiList=new ArrayList<ToaIMEI>();
		List<User> allUserList= userService.getAllUserInfo();
		HashMap<String,String> uMap=new HashMap<String, String>();
		Iterator it=allUserList.iterator();
		User user=null;
		while(it.hasNext()){
			user=(User)it.next();
			uMap.put(user.getUserId(), user.getUserName());
		}
		/**
		 * 不与统一用户表耦合，所以这里做个处理，根据用户id获取用户名
		 */
		if(tmpList!=null){
			for(ToaIMEI imei:tmpList){
				imei.setUserName(uMap.get(imei.getUserId()));
				imeiList.add(imei);
			}
		}
		stateMap.put("1", "开启");
		stateMap.put("0", "关闭");
		ListUtils.splitList2Page(imeiPage, imeiList);
		return "imeiSettingList";
	}
	/**
	 * 新增编辑页面
	 * @description
	 *
	 * @author  hecj
	 * @date    Jan 18, 2014 11:55:02 AM
	 * @param   
	 * @return  String
	 * @throws
	 */
	public String imeiInput(){
		if(imei_Id!=null){
			imeiModel=manager.getIMEIbyId(imei_Id);
		}
		return "imeiInput";
	}
	/**
	 * 保存imei手机加固记录
	 * @description
	 *
	 * @author  hecj
	 * @date    Jan 18, 2014 11:57:13 AM
	 * @param   
	 * @return  void
	 * @throws
	 */
	public void saveIMEI(){
		if(imeiModel.getIsOpen()==null){
			imeiModel.setIsOpen("0");
		}
		manager.saveIMEI(imeiModel);
	}
	/**
	 * 删除iemi记录
	 * @description
	 *
	 * @author  hecj
	 * @date    Jan 20, 2014 7:06:46 PM
	 * @param   
	 * @return  void
	 * @throws
	 */
	public void deleteIMEI(){
		manager.deleteIMEI(imei_Id);
	}
	public Object getModel() {
		return model;
	}
	
	@Autowired
	public void setManager(SystemsetManager manager) {
		this.manager = manager;
	}

	@Autowired
	public void setEform(IEFormService eform) {
		this.eform = eform;
	}

	public List<EForm> getList() {
		return list;
	}

	public String getParentFormId() {
		return parentFormId;
	}

	public void setParentFormId(String parentFormId) {
		this.parentFormId = parentFormId;
	}

	public String getSubFormId() {
		return subFormId;
	}

	public void setSubFormId(String subFormId) {
		this.subFormId = subFormId;
	}

	@Autowired
	public void setSendDocManager(SendDocManager sendDocManager) {
		this.sendDocManager = sendDocManager;
	}

	public List<ToaSysmanageProperty> getItemList() {
		return itemList;
	}
	
	public List<ToaSysmanageProperty> getMapItemList() {
		return mapItemList;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public String getDataType() {
		return dataType;
	}

	public String getBaseFolderSize() {
		return baseFolderSize;
	}

	public void setBaseFolderSize(String baseFolderSize) {
		this.baseFolderSize = baseFolderSize;
	}

	public String getBaseMsgSize() {
		return baseMsgSize;
	}

	public void setBaseMsgSize(String baseMsgSize) {
		this.baseMsgSize = baseMsgSize;
	}

	@Autowired
	public void setPcService(ParamConfigService pcService) {
		this.pcService = pcService;
	}

	public String getIsViewChildSetOpen() {
		return isViewChildSetOpen;
	}

	public void setIsViewChildSetOpen(String isViewChildSetOpen) {
		this.isViewChildSetOpen = isViewChildSetOpen;
	}

	public String getCalendarAttSize() {
		return calendarAttSize;
	}

	public void setCalendarAttSize(String calendarAttSize) {
		this.calendarAttSize = calendarAttSize;
	}

	public String getNotifyAttSize() {
		return notifyAttSize;
	}

	public void setNotifyAttSize(String notifyAttSize) {
		this.notifyAttSize = notifyAttSize;
	}
	
	public String getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(String refreshTime) {
		this.refreshTime = refreshTime;
	}

	public void setUploads(File uploads) {
		this.uploads = uploads;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getImeiCode() {
		return imeiCode;
	}

	public void setImeiCode(String imeiCode) {
		this.imeiCode = imeiCode;
	}

	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}

	public Page<ToaIMEI> getImeiPage() {
		return imeiPage;
	}

	public void setImeiPage(Page<ToaIMEI> imeiPage) {
		this.imeiPage = imeiPage;
	}

	public ToaIMEI getImeiModel() {
		return imeiModel;
	}

	public void setImeiModel(ToaIMEI imeiModel) {
		this.imeiModel = imeiModel;
	}

	public Map<String, String> getStateMap() {
		return stateMap;
	}

	public void setStateMap(Map<String, String> stateMap) {
		this.stateMap = stateMap;
	}

	public String getImei_Id() {
		return imei_Id;
	}

	public void setImei_Id(String imei_Id) {
		this.imei_Id = imei_Id;
	}

	public String getUserNameParam() {
		return userNameParam;
	}

	public void setUserNameParam(String userNameParam) {
		this.userNameParam = userNameParam;
	}
}
