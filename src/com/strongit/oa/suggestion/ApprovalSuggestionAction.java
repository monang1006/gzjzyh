package com.strongit.oa.suggestion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaApprovalSuggestion;
import com.strongit.oa.bo.ToaSerialnumberSort;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.OALogInfo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "approvalSuggestion.action", type = ServletActionRedirectResult.class) })
public class ApprovalSuggestionAction extends BaseActionSupport {
	
	/**
	 * 分页
	 */
	private Page<ToaApprovalSuggestion> page=new Page<ToaApprovalSuggestion>(FlexTableTag.MAX_ROWS,true);
	
	private ToaApprovalSuggestion model=new ToaApprovalSuggestion();
	
	private String suggestionCode;										//审批意见ID
	
	private List<ToaApprovalSuggestion> list=new ArrayList<ToaApprovalSuggestion>();
	
	private String userId;												//当前用户ID
	
	private String suggestionContent;											//审批意见内容
	
	private Date startDate;											//开始时间
		
	private Date endDate;												//结束时间
	
	private IUserService userService;											//用户接口
	
	private IApprovalSuggestionService appSuggestionService;
	
	private String state;
	
	private String suggestionLength;

	@Override
	public String delete() throws Exception {
		String msg="";
		if(suggestionCode!=null&&!suggestionCode.equals("")){
			String []arr=suggestionCode.split(",");
			for(int i=0;i<arr.length;i++){
				appSuggestionService.delete(arr[i], new OALogInfo("删除审批意见"));
			}
			msg="success";
		}else{
			msg="error";
		}
		return renderHtml(msg);
		
	}

	@Override
	public String input() throws Exception {
		if(suggestionCode==null||"".equals(suggestionCode)){//判断ID是否为空
			model=new ToaApprovalSuggestion();
		}else{
			model=appSuggestionService.getModelById(suggestionCode, new OALogInfo("根据ID获取审批意见对象"));
			suggestionLength=String.valueOf(model.getSuggestionContent().length());
		}
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		if(userId==null||userId.equals("")){
			User user=userService.getCurrentUser();
			userId=user.getUserId();
		}
		
		page=appSuggestionService.getAppSuggestionPage(page, userId, startDate, endDate, model, new OALogInfo("分页列表搜索"));
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(suggestionCode==null||"".equals(suggestionCode)){//判断ID是否为空
			model=new ToaApprovalSuggestion();
		}else{
			model=appSuggestionService.getModelById(suggestionCode, new OALogInfo("根据ID获取审批意见对象"));
			suggestionLength=String.valueOf(model.getSuggestionContent().length());
		}
	}

	@Override
	public String save() throws Exception {
		String ret = "0";
		try {			
			User user=userService.getCurrentUser();
			if(model.getSuggestionCode()!=null&&"".equals(model.getSuggestionCode())){
				model.setSuggestionCode(null);
			}
			if(model.getSuggestionUserid()==null||!model.equals("")){
				model.setSuggestionUserid(user.getUserId());
			}
			model.setSuggestionDate(new Date());
			appSuggestionService.saveSuggestion(model, new OALogInfo("保存审批意见对象"));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户【"+userService.getCurrentUser().getUserName()+"】保存审批意见出错", e);
			ret = "-1";
		}
		
		return this.renderText(ret);
	}
	
	public String IsSuggestionContent()throws Exception{
		User user=userService.getCurrentUser();
		int count=appSuggestionService.IsHasSuggestion(user.getUserId(), suggestionContent,suggestionCode);
		if(count>0){
			return renderHtml("当前审批意见【"+suggestionContent+"】已存在，请重新输入！");
		}else {
			return renderHtml("0");
		}
	}

	/**
	 * 获取审批意见选择列表
	 * @author zhengzb
	 * @desc 
	 * 2011-3-15 下午04:46:01 
	 * @return
	 * @throws Exception
	 */
	public String select()throws Exception{
		StringBuffer strb=new StringBuffer();
		//strb.append("<option value=''>请选择以前的意见</option>");
		strb.append("<option value=''><选择常用意见></option>");
		try {
			User user=userService.getCurrentUser();
			List<ToaApprovalSuggestion> list=appSuggestionService.getAppSuggestionListByUserId(user.getUserId(), new OALogInfo("根据用户ID获取审批意见LIST"));
			
			if(list!=null){
				for(ToaApprovalSuggestion suggestion:list){
					if(suggestion.getSuggestionContent().length()>20){
						strb.append("<option value='"+suggestion.getSuggestionContent()+"' title='"+suggestion.getSuggestionContent()+"'>"+suggestion.getSuggestionContent().substring(0, 20)+"..."+"</option>");
					}else{
					  strb.append("<option value='"+suggestion.getSuggestionContent()+"' title='"+suggestion.getSuggestionContent()+"'>"+suggestion.getSuggestionContent()+"</option>");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return renderHtml(strb.toString());
	}
	
	/**
	 * 上移或下移时，相互对换两者的排序顺序。并保存
	 * @author zhengzb
	 * @desc 
	 * 2011-4-1 上午11:26:03 
	 * @return
	 */
	public String moveSeq(){
		String ret="error";
		try {
			if(suggestionCode!=null&&!suggestionCode.equals("")){
				String []idArr=suggestionCode.split(",");
				ToaApprovalSuggestion suggestion1=appSuggestionService.getModelById(idArr[0], new OALogInfo("根据ID获取审批意见对象"));
				ToaApprovalSuggestion suggestion2=appSuggestionService.getModelById(idArr[1], new OALogInfo("根据ID获取审批意见对象"));
				//Date date=suggestion1.getSuggestionDate();
				BigDecimal temp=suggestion1.getSuggestionSeq();
				
				//suggestion1.setSuggestionDate(suggestion2.getSuggestionDate());
				suggestion1.setSuggestionSeq(suggestion2.getSuggestionSeq());
				
				//suggestion2.setSuggestionDate(date);
				suggestion2.setSuggestionSeq(temp);
				appSuggestionService.saveSuggestion(suggestion1, new OALogInfo("对移动好的意见进行保存"));
				appSuggestionService.saveSuggestion(suggestion2, new OALogInfo("对移动好的意见进行保存"));
				ret="OK";	
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			renderText("error");
		}
		return this.renderText(ret);
	}
	
	/**
	 * 上移，相互对换两者的排序号，如果排序号相同则前者排序号-1。并保存
	 * @author 刘皙
	 * @desc 
	 * 2012年11月23日14:23:33 
	 * @return
	 */
	public String moveSeqUp(){
		String ret="error";
		try {
			if(suggestionCode!=null&&!suggestionCode.equals("")){
				String []idArr=suggestionCode.split(",");
				ToaApprovalSuggestion suggestion1=appSuggestionService.getModelById(idArr[0], new OALogInfo("根据ID获取审批意见对象"));
				ToaApprovalSuggestion suggestion2=appSuggestionService.getModelById(idArr[1], new OALogInfo("根据ID获取审批意见对象"));
				if(suggestion1.getSuggestionSeq().compareTo(suggestion2.getSuggestionSeq())==0){
					suggestion1.setSuggestionSeq(suggestion1.getSuggestionSeq().subtract(new BigDecimal(1)));
				}else{
					BigDecimal temp=suggestion1.getSuggestionSeq();
					suggestion1.setSuggestionSeq(suggestion2.getSuggestionSeq());
					//suggestion2.setSuggestionDate(date);
					suggestion2.setSuggestionSeq(temp);
				}
				appSuggestionService.saveSuggestion(suggestion1, new OALogInfo("对移动好的意见进行保存"));
				appSuggestionService.saveSuggestion(suggestion2, new OALogInfo("对移动好的意见进行保存"));
				ret="OK";
			}
		} catch (Exception e) {
			e.printStackTrace();
			renderText("error");
		}
		return this.renderText(ret);		
	}
	
	/**
	 * 下移，相互对换两者的排序号，如果排序号相同则前者排序号+1。并保存
	 * @author 刘皙
	 * @desc 
	 * 2012年11月23日14:23:33 
	 * @return
	 */
	public String moveSeqDown(){
		String ret="error";
		try {
			if(suggestionCode!=null&&!suggestionCode.equals("")){
				String []idArr=suggestionCode.split(",");
				ToaApprovalSuggestion suggestion1=appSuggestionService.getModelById(idArr[0], new OALogInfo("根据ID获取审批意见对象"));
				ToaApprovalSuggestion suggestion2=appSuggestionService.getModelById(idArr[1], new OALogInfo("根据ID获取审批意见对象"));
				if(suggestion1.getSuggestionSeq().compareTo(suggestion2.getSuggestionSeq())==0){
					suggestion1.setSuggestionSeq(suggestion1.getSuggestionSeq().add(new BigDecimal(1)));
				}else{
					BigDecimal temp=suggestion1.getSuggestionSeq();
					suggestion1.setSuggestionSeq(suggestion2.getSuggestionSeq());
					//suggestion2.setSuggestionDate(date);
					suggestion2.setSuggestionSeq(temp);
				}
				appSuggestionService.saveSuggestion(suggestion1, new OALogInfo("对移动好的意见进行保存"));
				appSuggestionService.saveSuggestion(suggestion2, new OALogInfo("对移动好的意见进行保存"));
				ret="OK";
			}
		} catch (Exception e) {
			e.printStackTrace();
			renderText("error");
		}
		return this.renderText(ret);		
	}
	
	public ToaApprovalSuggestion getModel() {
		// TODO 自动生成方法存根
		return model;
	}



	public List<ToaApprovalSuggestion> getList() {
		return list;
	}

	public void setList(List<ToaApprovalSuggestion> list) {
		this.list = list;
	}

	public Page<ToaApprovalSuggestion> getPage() {
		return page;
	}

	public void setPage(Page<ToaApprovalSuggestion> page) {
		this.page = page;
	}

	public String getSuggestionCode() {
		return suggestionCode;
	}

	public void setSuggestionCode(String suggestionCode) {
		this.suggestionCode = suggestionCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setModel(ToaApprovalSuggestion model) {
		this.model = model;
	}
	
	@Autowired
	public void setAppSuggestionService(
			IApprovalSuggestionService appSuggestionService) {
		this.appSuggestionService = appSuggestionService;
	}

	public Date getEndDate() {
		return endDate;
	}

	@SuppressWarnings("deprecation")
	public void setEndDate(Date endDate) {
		endDate.setHours(23);
		endDate.setMinutes(59);
		this.endDate = endDate;
//		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public String getSuggestionContent() {
		return suggestionContent;
	}

	public void setSuggestionContent(String suggestionContent) {
		this.suggestionContent = suggestionContent;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSuggestionLength() {
		return suggestionLength;
	}

	public void setSuggestionLength(String suggestionLength) {
		this.suggestionLength = suggestionLength;
	}

}
