package com.strongit.gzjzyh.action;

import java.util.Date;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.gzjzyh.po.TGzjzyhApplication;
import com.strongit.gzjzyh.service.IQueryApplyService;
import com.strongit.uums.util.TimeKit;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@ParentPackage("default")
public class QueryApplyAction extends BaseActionSupport {

	private TGzjzyhApplication model = new TGzjzyhApplication();

	private IQueryApplyService queryApplyService = null;

	private Page<TGzjzyhApplication> page = new Page<TGzjzyhApplication>(10,
			true);

	// 当前页数
	public Integer curpage = 0;

	// 每页显示条数
	public Integer unitpage = 20;

	private String accoutType;

	private String appFileno;

	private String appBankuser;

	private Date appStartDate;

	private Date appEndDate;

	@Autowired
	public void setQueryApplyService(IQueryApplyService queryApplyService) {
		this.queryApplyService = queryApplyService;
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return this.model;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
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
		this.page = this.queryApplyService.findQueryApplyPage(page, accoutType,
				appFileno, appBankuser, appStartDate, appEndDate);
		return SUCCESS;
	}
	
	public String showList() throws Exception {
		// TODO Auto-generated method stub
		this.page.setPageNo(this.curpage);
		this.page.setPageSize(this.unitpage);
		this.page = this.queryApplyService.findQueryApplyPage(page, accoutType,
				appFileno, appBankuser, appStartDate, appEndDate);

		int totalRecord = this.page.getTotalCount();
		// 计算总页数
		int totalPage = totalRecord % this.unitpage == 0
				? totalRecord / this.unitpage : totalRecord / this.unitpage + 1;

		//分页，翻至最后一页，删除最后一页所有记录，出现当前页大于总页数，当前页减一，并且查询出末页的记录
		if (curpage != 1 && curpage == (totalPage + 1)) {
			curpage = curpage - 1;
			this.page.setPageNo(curpage);
			this.page = this.queryApplyService.findQueryApplyPage(page, accoutType,
					appFileno, appBankuser, appStartDate, appEndDate);
		}

		// 定义返回的数据类型：json，使用了json-lib
		JSONObject jsonObj = new JSONObject();
		// 根据jqGrid对JSON的数据格式要求给jsonObj赋值
		jsonObj.put("currpage", this.curpage);// 当前页
		jsonObj.put("totalpages", totalPage);// 总页数
		jsonObj.put("totalrecords", totalRecord);// 总记录数

		List<TGzjzyhApplication> applicationList = this.page.getResult();
		// 定义rows，存放数据
		JSONArray jsonData = new JSONArray();
		if (applicationList != null && applicationList.size() > 0) {
			for (TGzjzyhApplication application : applicationList) {
				JSONObject vo = new JSONObject();
				//主键
				vo.put("appId", application.getAppId());
				//文书号
				vo.put("appFileno", application.getAppFileno());
				//查询银行
				vo.put("appBankuser", application.getAppBankuser());
				//申请时间
				vo.put("appDate", TimeKit
						.formatDate(application.getAppDate(), "long"));
				//审核时间
				vo.put("appDate", TimeKit
						.formatDate(application.getAppDate(), "long"));
				//当前状态
				vo.put("appStatus", application.getAppStatus());
				jsonData.add(vo);
			}
		}
		jsonObj.put("rows", jsonData);
		this.renderText(jsonObj.toString());
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAccoutType() {
		return accoutType;
	}

	public void setAccoutType(String accoutType) {
		this.accoutType = accoutType;
	}

	public String getAppFileno() {
		return appFileno;
	}

	public void setAppFileno(String appFileno) {
		this.appFileno = appFileno;
	}

	public String getAppBankuser() {
		return appBankuser;
	}

	public void setAppBankuser(String appBankuser) {
		this.appBankuser = appBankuser;
	}

	public Date getAppStartDate() {
		return appStartDate;
	}

	public void setAppStartDate(Date appStartDate) {
		this.appStartDate = appStartDate;
	}

	public Date getAppEndDate() {
		return appEndDate;
	}

	public void setAppEndDate(Date appEndDate) {
		this.appEndDate = appEndDate;
	}

}