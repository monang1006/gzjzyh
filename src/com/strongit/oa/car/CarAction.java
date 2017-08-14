/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: 
 * modifyer:	luosy
 * Version: V1.0
 * Description：车辆信息管理
 */
package com.strongit.oa.car;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaCar;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.dict.IDictService;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * @author       吴为胜
 * @company      Strongit Ltd. (C) copyright
 * @date         2009年4月18日10:31:52
 * @version      1.0.0.0
 * @comment      车辆Action
 */

@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "car.action", type = ServletActionRedirectResult.class) })
public class CarAction extends BaseActionSupport {

	private String carId;   //车辆ID
	private Map carStstusMap = new HashMap(); //车辆状态
	private Map carTypeMap = new HashMap(); //车辆类型
	private File[] uploads;  //上传图片用
    //	查询用字段
	private String carno;   //车牌号
	private String cartype;  //车型
	private String takenumber;   //可乘人数
	private Date buydate;  //购置日期
	private Date buydate1;  //购置日期1(搜索用)
	private Date buydate2;  //购置日期2(搜索用)
	private String status;  //状态（1可用、2在用、3维修中、4停用等）
	private IDictService dictService;  //字典项类
	private List carTypeList;  //车辆类型列表
	private List carStatusList;  //车辆状态列表
	
	// 用于查看
	private String status2;  //状态 
	private String cartype2;  //车型2
	
	private String carbrand; //车辆品牌
	private String driver;  //司机
	
	private String editDelImg;  //编辑时是否删除了原有图片  "1"表示删除了原有图片

	private Page<ToaCar> page = new Page<ToaCar>(FlexTableTag.MAX_ROWS, true);
	private ToaCar model = new ToaCar();
	private CarManager carManager;

	public CarAction(){

	}
	
	@Override
	public String delete() throws Exception {
		try {
			String[] ids = carId.split(",");
			for(String id : ids) {
			   model=carManager.getCarInfo(id);
			   model.setIsdel("1");  //设置为假删除
			   carManager.saveCarInfo(model, editDelImg);
			}
			addActionMessage("删除成功");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			addActionMessage(e.getMessage());
		}
		return renderHtml("<script>  window.location='"
				+ getRequest().getContextPath()
				+ "/car/car.action';</script>");
	}

	@Override
	public String input() throws Exception {
		carTypeList = dictService.getItemsByDictValue("CARTYPE");
		carStatusList = dictService.getItemsByDictValue("CARSTATUS");
		prepareModel();
		return INPUT;
	}
	
	public String edit() throws Exception {
		carTypeList = dictService.getItemsByDictValue("CARTYPE");
		carStatusList = dictService.getItemsByDictValue("CARSTATUS");
		prepareModel();
		return "edit";
	}
	/**
	 * 查看车辆信息
	 * @return
	 * @throws Exception
	 */
	public String view() throws Exception {
		carTypeList = dictService.getItemsByDictValue("CARTYPE");
		carStatusList = dictService.getItemsByDictValue("CARSTATUS");
		prepareModel();
		for(Iterator it = carStatusList.iterator(); it.hasNext();) {
			ToaSysmanageDictitem toas = (ToaSysmanageDictitem) it.next();
			if (toas.getDictItemValue().charAt(0)==model.getStatus().charAt(0)) {
			    this.setStatus2(toas.getDictItemShortdesc());
				break;
			}
		}

		for(Iterator it = carTypeList.iterator(); it.hasNext();) {
			ToaSysmanageDictitem toas = (ToaSysmanageDictitem) it.next();
			if (toas.getDictItemValue().charAt(0)==model.getCartype().charAt(0)) {    //比较车辆类型字典项值与车辆类型值
				this.setCartype2(toas.getDictItemShortdesc());     //车辆类型字典项简称
				break;
			}
		}
		return "view";
	}

	/** 
	 * 车辆信息列表 
	 */
	@Override
	public String list() throws Exception {
		ToaSysmanageDictitem toas = null;

		//车辆类型
		carTypeList = dictService.getItemsByDictValue("CARTYPE");
		for(Iterator it = carTypeList.iterator(); it.hasNext();) {
			toas = (ToaSysmanageDictitem) it.next();
			carTypeMap.put(toas.getDictItemValue(),toas.getDictItemShortdesc());
		}
		//车辆状态
		carStatusList = dictService.getItemsByDictValue("CARSTATUS");
		for(Iterator it = carStatusList.iterator(); it.hasNext();) {
			toas = (ToaSysmanageDictitem) it.next();
			carStstusMap.put(toas.getDictItemValue(),toas.getDictItemShortdesc());
		}
		page=carManager.queryCars(page, carno, cartype,carbrand, takenumber,driver, buydate1,buydate2,status);
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if (carId != null) {
			model = carManager.getCarInfo(carId);
		} else {
			model = new ToaCar();
		}
	}

	@Override
	public String save() throws Exception {
		if ("".equals(model.getCarId())) {
			model.setCarId(null);
		} 
	
		if (uploads!=null){    //图片不为空
		FileInputStream fis = null;
		fis = new FileInputStream(uploads[0]);
		byte[] buf = new byte[(int)uploads[0].length()];
		fis.read(buf);
		model.setImg(buf);
		}else{
			model.setImg(null);
		}
	    model.setIsdel("0");  //设置为不删除
		carManager.saveCarInfo(model,editDelImg);
		addActionMessage("保存成功");
		return renderHtml("<script>  window.dialogArguments.location='" + getRequest().getContextPath() + "/car/car.action'; window.close();</script>");
	}
	/**
	 * 判断车辆有没有图片
	 * {1:有图片;0:无图片}
	 * @return
	 */
	public String judgeImg(){
		ToaCar c = carManager.getCarInfo(carId);
		String message=null;
		if (c.getImg()!=null){
			message="1";	//有图片
		}else{
			message="0";   //无图片
		}
		StringBuilder str = new StringBuilder();
		ToaSysmanageDictitem toas = null;
		carStatusList = dictService.getItemsByDictValue("CARSTATUS");
		for(Iterator it = carStatusList.iterator(); it.hasNext();) {
			toas = (ToaSysmanageDictitem) it.next();
			str.append(toas.getDictItemValue()).append(":'")
				.append(toas.getDictItemShortdesc()).append("',");
		}
		this.renderText(message+"&{"+str.toString().substring(0,str.length()-1)+"}");
		return null;
		
	}
	
	/**
	 * author:luosy
	 * description:是否有重复的车牌
	 * modifyer:
	 * description:
	 * @return
	 */
	public String isExistCarno(){
		try {
			if(!"".equals(carno)&&null!=carno){
				carno = java.net.URLDecoder.decode(carno, "utf-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		if(carManager.isExistCar(carno,carId)){
			return renderText("exist");
		}else{
			return renderText("succ");
		}
		
	}
	
	/**
	 * 显示具体车辆图片
	 * @return
	 */
	public String viewImg(){
		HttpServletResponse response = super.getResponse();
		ToaCar c = carManager.getCarInfo(carId);
		response.setContentType("application/x-download");         
		response.setHeader("Cache-Control","no-store");
		response.setHeader("Pragrma","no-cache");
		response.setDateHeader("Expires",0);
		if(null!=c&&c.getImg()!=null){
			response.reset();
			OutputStream output = null;
			try{
				response.setContentType("application/x-download");         
				response.setHeader("Cache-Control","no-store");
				response.setHeader("Pragrma","no-cache");
				response.setDateHeader("Expires",0);
				response.addHeader("Content-Disposition", "attachment;filename=" +
				         new String(c.getCarno().getBytes("gb2312"),"iso8859-1"));
				
			    output = response.getOutputStream();
			    
			    output.write(c.getImg());
			    output.flush();
			} catch(Exception e) {
				if(logger.isErrorEnabled()){
					logger.error(e.getMessage(),e);
				}
			} finally {		 
			    if(output != null){
			      try {
						output.close();
					} catch (IOException e) {
						if(logger.isErrorEnabled()){
							logger.error(e.getMessage(),e);
						}
					}
			      output = null;
			    }
			}
		}
		return null;
	}
	/**
	 * 显示车辆信息树
	 * @return
	 */
	public String selecttree(){
		List<ToaCar> cars=carManager.getCars();
		getRequest().setAttribute("carlist", cars);
		return "selecttree";
	}
	
	public ToaCar getModel() {
		// TODO Auto-generated method stub
		return this.model;
	}

	public Page<ToaCar> getPage() {
		return page;
	}

	public void setPage(Page<ToaCar> page) {
		this.page = page;
	}

	public CarManager getCarManager() {
		return carManager;
	}
	@Autowired
	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	public void setModel(ToaCar model) {
		this.model = model;
	}

	public Date getBuydate() {
		return buydate;
	}

	public void setBuydate(Date buydate) {
		this.buydate = buydate;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getCarno() {
		return carno;
	}

	public void setCarno(String carno) {
		this.carno = carno;
	}

	public String getCartype() {
		return cartype;
	}

	public void setCartype(String cartype) {
		this.cartype = cartype;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTakenumber() {
		return takenumber;
	}

	public void setTakenumber(String takenumber) {
		this.takenumber = takenumber;
	}

	public Date getBuydate1() {
		return buydate1;
	}

	public void setBuydate1(Date buydate1) {
		this.buydate1 = buydate1;
	}

	public Date getBuydate2() {
		return buydate2;
	}

	public void setBuydate2(Date buydate2) {
		this.buydate2 = buydate2;
	}

	public Map getCarStstusMap() {
		return carStstusMap;
	}

	public void setCarStstusMap(Map carStstusMap) {
		this.carStstusMap = carStstusMap;
	}

	public Map getCarTypeMap() {
		return carTypeMap;
	}

	public void setCarTypeMap(Map carTypeMap) {
		this.carTypeMap = carTypeMap;
	}

	public List getCarStatusList() {
		return carStatusList;
	}

	public List getCarTypeList() {
		return carTypeList;
	}

	@Autowired
	public void setDictService(IDictService dictService) {
		this.dictService = dictService;
	}

	public File[] getUploads() {
		return uploads;
	}

	public void setUploads(File[] uploads) {
		this.uploads = uploads;
	}

	public String getCartype2() {
		return cartype2;
	}

	public void setCartype2(String cartype2) {
		this.cartype2 = cartype2;
	}

	public String getStatus2() {
		return status2;
	}

	public void setStatus2(String status2) {
		this.status2 = status2;
	}

	public String getCarbrand() {
		return carbrand;
	}

	public void setCarbrand(String carbrand) {
		this.carbrand = carbrand;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getEditDelImg() {
		return editDelImg;
	}

	public void setEditDelImg(String editDelImg) {
		this.editDelImg = editDelImg;
	}

}
