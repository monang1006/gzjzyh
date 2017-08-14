package com.strongit.oa.attendance.holiday;

import java.util.Date;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;
import com.strongit.oa.bo.ToaAttendHoliday;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;
/**
 * 法定假日action
 * @author 胡丽丽
 * @date 2009-12-24
 *
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "holiday.action", type = ServletActionRedirectResult.class) })
public class HolidayAction extends BaseActionSupport {
	private static final long serialVersionUID = 1L;
	private Page<ToaAttendHoliday> page=new Page<ToaAttendHoliday>(FlexTableTag.MAX_ROWS,true);
	private HolidayManager manager;
	private ToaAttendHoliday model=new ToaAttendHoliday();
	private static final String NO="1";	//禁用
	private String holidayId;			//假日ID
	private String disLogo;				//搜索
	private String isEnable;			//是否启用
	private Date beginTime;				//开始时间
	private Date endTime;				//结束时间
	
	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

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

	public Page<ToaAttendHoliday> getPage() {
		return page;
	}

	public void setPage(Page<ToaAttendHoliday> page) {
		this.page = page;
	}

	public String getHolidayId() {
		return holidayId;
	}

	public void setHolidayId(String holidayId) {
		this.holidayId = holidayId;
	}

	@Autowired
	public void setManager(HolidayManager manager) {
		this.manager = manager;
	}
	
	public ToaAttendHoliday getModel() {
		return model;
	}

	/**
	 * 删除法定假日
	 * @author 胡丽丽
	 * @date:2009-12-24
	 */
	@Override
	public String delete() throws Exception {
		try {
			if(holidayId!=null&&!"".equals(holidayId)){//是否为空
				String[] ids=holidayId.split(",");
				for(int i=0;i<holidayId.length();i++){
					if(ids[i]!=null&&!"".equals(ids[i])){
						manager.delete(ids[i]);
					}
				}
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return RELOAD;
	}

	/**
	 * 初始化法定假日界面
	 * @author 胡丽丽
	 * @date:2009-12-24
	 */
	@Override
	public String input() throws Exception {
		prepareModel();
		return INPUT;
	}

	/**
	 * 查看法定假日列表
	 * @author 胡丽丽
	 * @date:2009-12-24
	 */
	@Override
	public String list() throws Exception {
		try {
			if(disLogo!=null&&!"".equals(disLogo)){//是否多条件查询
				page=manager.search(page,model);
			}else{
				page=manager.getAllHoliday(page);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 初始化BO
	 * @author 胡丽丽
	 * @date:2009-12-24
	 */
	@Override
	protected void prepareModel() throws Exception {
		if(holidayId!=null&&!"".equals(holidayId)){//是否为空
			model=manager.getHolidayByID(holidayId);
		}else{
			model=new ToaAttendHoliday();
		}

	}

	/**
	 * 保存法定假日
	 * @author 胡丽丽
	 * @date:2009-12-24
	 */
	@Override
	public String save() throws Exception {
		try {
			if("".equals(model.getHolidayId())){	//是否为空字符串
				model.setHolidayId(null);
			}
			if(model.getIsEnable()==null||"".equals(model.getIsEnable())){		//是否启用
				model.setIsEnable(NO);
			}
			if(model.getIsWholeDay()==null||"".equals(model.getIsWholeDay())){	//是否全天
				model.setIsWholeDay("0");
			}
			int temp=this.isholiday();
			if(temp==0){	//不存在相同的法定假日
				manager.save(model);
				return renderHtml("<script> window.dialogArguments.document.location.reload();window.close();</script>");
			}else if(temp==1){	
				return renderHtml("<script>alert('该时间段已经设置为法定假日，目前为禁用状态，启用即可！');window.close();</script>");
			}else if(temp==2){
				return renderHtml("<script>alert('该时间段已经设置为法定假日，目前已经失效，修改失效日期即可！');window.close();</script>");
			}else if(temp==3){
				return renderHtml("<script>alert('该时间段已经设置为法定假日，目前为启用状态！');window.close();</script>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return renderHtml("<script> window.dialogArguments.document.location.reload();window.close();</script>");
	}

	/**
	 * 查看法定假日
	 * @author 胡丽丽
	 * @date:2009-12-24
	 * @return
	 * @throws Exception
	 */
	public String show()throws Exception{
		prepareModel();
		return "show";
	}
	
	/**
	 * 禁止或启用法定假日
	 * @author 胡丽丽
	 * @date:2009-12-24
	 * @return
	 * @throws Exception
	 */
	public String isEnableType() throws Exception{
		String[] ids=holidayId.split(",");
		manager.isEnableType(isEnable, ids);
		return RELOAD;
	}
	
	/**
	 * 是否已经存在该法定假日
	 * @author 胡丽丽
	 * @date:2009-12-01
	 * @return
	 */
	public int isholiday(){
		try {
			int i=manager.getHolidayByTime(model.getHolidayId(),model.getHolidayStime(), model.getHolidayEtime()); //获取某个时间段是否已经设置了法定假日
			return i;
		}catch (SystemException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return 4;
	}
}
