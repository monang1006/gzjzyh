package com.strongit.oa.noticeconference;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.strongit.oa.bo.TOmConAttach;
import com.strongit.oa.bo.TOmConPerson;
import com.strongit.oa.bo.TOmConference;
import com.strongit.oa.bo.TOmConferenceSend;
import com.strongit.oa.bo.TOmDeptreport;
import com.strongit.oa.dict.IDictService;
import com.strongit.oa.noticeconference.util.Constants;
import com.strongit.oa.noticeconference.util.FileUtils;
import com.strongit.oa.noticeconference.util.StringUtil;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

public class ClientConferenceAction extends
		BaseActionSupport<TOmConferenceSend> {

	@Autowired
	private IConAcceptManager conferenceSendManager;
	private Page<TOmConferenceSend> page = new Page<TOmConferenceSend>(15, true);// 会议通知PAGE

	/** 选择人员列表对象 */
	private Page<Object[]> userpage = new Page<Object[]>(15, true);
	/*private Page<ToaSeatsetPerson> pageuser = new Page<ToaSeatsetPerson>(15,
			true);*/

	private String orgCode; // 组织机构编码
	private String searchuserName; // 用户名称

	private TOmConferenceSend model = new TOmConferenceSend();
    @Autowired
    IUserService userService;
	@Autowired
	private IConPersonManager personManager;
	@Autowired
	private IDictService dictService;
	/*@Autowired
	private ILeaderBookAdress leaderBookManager;
	*/
 
	@Autowired
	private IConNoticeAttachManager conticeAttachManager;
	
	private String state;
	private String conId;// 会议编号

	private String ids;// 单位下发编号

	private Date searchStime; // 搜索开始时间
	private Date searchEtime; // 搜索结束时间
	private Date searchRegendtime;

	private String[] personName;// 用户名称
	private String[] personSax;// 性别
	private String[] personNation;// 民族
	private String[] personMobile;// 手机1
	private String[] personPost;// 职务
	private String[] personPhone;// 办公电话

	private String depId;// 部门编号

	private String initdatas;

	private String orguseridback;
	private String orgusernameback;
	private String initleader;
	private TOmConAttach attach;
	private List<TOmConAttach> attachs;
	
	private String searchtitle;
	private String searchconferenceAddr;
	private String sendType;
	
	
	
	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getOrguseridback() {
		return orguseridback;
	}

	public void setOrguseridback(String orguseridback) {
		this.orguseridback = orguseridback;
	}

	
	
	
	public String getSearchtitle() {
		return searchtitle;
	}

	public void setSearchtitle(String searchtitle) {
		this.searchtitle = searchtitle;
	}

	public String getSearchconferenceAddr() {
		return searchconferenceAddr;
	}

	public void setSearchconferenceAddr(String searchconferenceAddr) {
		this.searchconferenceAddr = searchconferenceAddr;
	}

	/***************************************************************************
	 * 
	 * 方法简要描述:获取当前登录用户所在的单位
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 7, 2013
	 * @Author 万俊龙
	 * @return
	 * @version 1.0
	 * @see
	 */
	private String getCurrentOrg() {
		Assert.notNull(userService.getCurrentUser());
		return userService.getCurrentUser().getOrgId();
	}

	public String container() {
		if (StringUtil.isEmpty(state)) {
			state = "0";
		}
		return "container";
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：签收会议通知
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 7, 2013
	 * @Author 万俊龙
	 * @return
	 * @version 1.0
	 * @see
	 */
	public String signConf() {
		if (StringUtil.isNotEmpty(ids)) {
			String[] idLst = ids.split(",");
			for (String id : idLst) {
				TOmConferenceSend entity = this.conferenceSendManager
						.getModel(id);
				entity.setRecvState(Constants.CONFERENCE_SEND_RECVSTATE_SIGN);
				this.conferenceSendManager.update(entity);
			}
		}
		this.renderText("SUCCESS");
		return null;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		model = this.conferenceSendManager.getModel(this.ids);
		if(model.getTOmConference().getTsConfersort()!=null){
			sendType = model.getTOmConference().getTsConfersort().getContypeName();
		}
		if (StringUtil.isEmpty(model)) {
			this
					.renderHtml("<script>alert('由于异步操作，该对象已被删除！！');window.close();</script>");
			return null;
		}
		StringBuffer msg = new StringBuffer();
		// 获取所有的会议实体信息
		User user = userService.getCurrentUser();
		String orgId = "";
		if (StringUtil.isNotEmpty(user)) {
			// orgId=user.getOrgId();
		}
		List<TOmConPerson> list = personManager.findByDeptid(orgId);
		// str.append("<td>万钧 <input type='hidden' name='hval' id='hval'
		// value='万钧#局长#男#汉族#2345211111#number'/></td>");
		// this.getRequest().setAttribute("hval", str.toString());
		if (StringUtil.isNotEmpty(list)) {
			for (TOmConPerson entity : list) {
				msg.append("<td>");
				msg.append(entity.getPersonName());
				StringBuffer str = new StringBuffer(entity.getPersonName());
				if (StringUtil.isNotEmpty(entity.getPersonPost())) {
					str.append("#").append(entity.getPersonPost());
				} else {
					str.append("#").append(""); // 添加占位符
				}
				if (StringUtil.isNotEmpty(entity.getPersonSax())) {
					if ("0".equals(entity.getPersonSax())
							|| "男".equals(entity.getPersonSax())) {
						str.append("#").append("男");
					} else if ("1".equals(entity.getPersonSax())
							|| "女".equals(entity.getPersonSax())) {
						str.append("#").append("女");
					} else {
						str.append("#").append("未知");
					}
				}
				if (StringUtil.isNotEmpty(entity.getPersonNation())) {
					//
					String personNation = this.dictService
							.getDictItemName(entity.getPersonNation());
					if (StringUtil.isNotEmpty(personNation)) {
						str.append("#").append(personNation);
					} else {
						str.append("#").append("汉族");
					}
				}
				str.append("#");
				if (StringUtil.isNotEmpty(entity.getPersonMobile())) {
					str.append(entity.getPersonMobile());
				} else {
					str.append(""); // 添加占位符
				}

				if (StringUtil.isNotEmpty(entity.getPersonPhone())) {
					str.append("#").append(entity.getPersonPhone());
				} else {
					str.append("#").append("");// 添加占位符
				}
				msg.append("<input type='hidden' name='hval' id='hval' value='"
						+ str.toString() + "'/></td> ");
				msg.append("@");
			}
		}
		this.getRequest().setAttribute("hval", msg.toString());
		if (StringUtil.isNotEmpty(model.getSendconId())) {
			this.initdatas = initMerberInfoForView(model.getSendconId());
		}
		attachs = conticeAttachManager.getAttachsByAttachConId(model.getTOmConference().getConferenceId(), true);
		return "view";
	}
	
	//下载会议附件
	public String officeStream() throws Exception
	{
			String id = getRequest().getParameter("id");
			attach = conticeAttachManager.getConAttachByAttachId(id);
			String fileName = attach.getAttachFileName();
			File file1 = new File(FileUtils.getAttachDirectory() + File.separatorChar+attach.getAttachFilePath());
			FileInputStream filein = new FileInputStream(file1.getPath());
			InputStream fileout = new BufferedInputStream(filein);
			byte[] office = IOUtils.toByteArray(fileout);
			if(office != null)
			{
				getResponse().setHeader("content-disposition", "attachment;filename="+URLEncoder.encode(fileName, "UTF-8"));

				getResponse().setContentType("application/x-download");
				//getResponse().addHeader("Content-Disposition", "attachment;filename="
				//		+ new String(newConAttch.getAttachFileName().getBytes("gb2312"),
				//		"iso8859-1"));

				byte[] bOffice = office;
				ServletOutputStream outStream = null;
				try
				{
					outStream = getResponse().getOutputStream();
					outStream.write(bOffice);
					outStream.flush();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					//outStream.close();
				}
			}
		return null;
	}
	
	
	//删除会议附件
	public String delAttr() throws Exception
	{
			String id = getRequest().getParameter("id");
			conticeAttachManager.deleteConAttach(id);
			getResponse().setContentType("text/html");
			getResponse().getWriter().write("<script>window.opener.location.reload();</script>");
			return null;
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		if (StringUtil.isEmpty(model)) {
			model = new TOmConferenceSend();
		}
		User user = userService.getCurrentUser();
		Assert.notNull(user);
		TOmConference con = new TOmConference();
		if (!"1".equals(user.getUserIsSupManager())) {
			model.setDeptCode(user.getOrgId());
		}
		
		if (StringUtil.isNotEmpty(this.searchtitle)) {
			//searchtitle = new String(searchtitle.getBytes("iso-8859-1"), "UTF-8");  
			String title = searchtitle.replace("%", "\'\'%");
			
			con.setConferenceTitle(title);
			model.setTOmConference(con);
		}
		if (StringUtil.isNotEmpty(this.searchconferenceAddr)) {
			//searchconferenceAddr = new String(searchconferenceAddr.getBytes("iso-8859-1"), "UTF-8");
			String addr = searchconferenceAddr.replace("%", "\'\'%");
			con.setConferenceAddr(addr);
			model.setTOmConference(con);
		}
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat pim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(searchStime!=null){
			String btime = sim.format(searchStime);
			searchStime = pim.parse(btime+" 00:00:00");
		}
		if(searchEtime!=null){
			String etime = sim.format(searchEtime);
			searchEtime = pim.parse(etime+" 23:59:59");
		}
	 
		if (StringUtil.isEmpty(state) || "0".equals(state)) {
			page = this.conferenceSendManager.queryForConsend(page, model,
					searchStime, searchEtime, searchRegendtime, null);
			return "waitlist"; // 待签收会议
		}

		this.getRequest().setAttribute("stateMap",
				Constants.CONFERENCE_SEND_STATE_MAP);
		page = this.conferenceSendManager.queryForReceived(page, model,
				searchStime, searchEtime, searchRegendtime);
		return SUCCESS;
	}

	/**
	 * 
	 * 方法简要描述:会议报名
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 7, 2013
	 * @Author 万俊龙
	 * @return
	 * @version 1.0
	 * @see
	 */
	public String apply() {
		model = this.conferenceSendManager.getModel(this.ids);
		if(model.getTOmConference().getTsConfersort()!=null){
			sendType = model.getTOmConference().getTsConfersort().getContypeName();
		}
		if (StringUtil.isEmpty(model)) {
			this
					.renderHtml("<script>alert('由于异步操作，该对象已被删除！！');window.close();</script>");
			return null;
		}
		StringBuffer msg = new StringBuffer();
		// 获取所有的会议实体信息
		User user = this.userService.getCurrentUser();
		String orgId = "";
		if (StringUtil.isNotEmpty(user)) {
			// orgId=user.getOrgId();
		}
		List<TOmConPerson> list = personManager.findByDeptid(orgId);
		// str.append("<td>万钧 <input type='hidden' name='hval' id='hval'
		// value='万钧#局长#男#汉族#2345211111#number'/></td>");
		// this.getRequest().setAttribute("hval", str.toString());
		if (StringUtil.isNotEmpty(list)) {
			for (TOmConPerson entity : list) {
				msg.append("<td>");
				msg.append(entity.getPersonName());
				StringBuffer str = new StringBuffer(entity.getPersonName());
				if (StringUtil.isNotEmpty(entity.getPersonPost())) {
					str.append("#").append(entity.getPersonPost());
				} else {
					str.append("#").append(""); // 添加占位符
				}
				if (StringUtil.isNotEmpty(entity.getPersonSax())) {
					if ("0".equals(entity.getPersonSax())
							|| "男".equals(entity.getPersonSax())) {
						str.append("#").append("男");
					} else if ("1".equals(entity.getPersonSax())
							|| "女".equals(entity.getPersonSax())) {
						str.append("#").append("女");
					} else {
						str.append("#").append("未知");
					}
				}
				if (StringUtil.isNotEmpty(entity.getPersonNation())) {
					//
					String personNation = this.dictService
							.getDictItemName(entity.getPersonNation());
					if (StringUtil.isNotEmpty(personNation)) {
						str.append("#").append(personNation);
					} else {
						str.append("#").append("汉族");
					}
				}
				str.append("#");
				if (StringUtil.isNotEmpty(entity.getPersonMobile())) {
					str.append(entity.getPersonMobile());
				} else {
					str.append(""); // 添加占位符
				}

				if (StringUtil.isNotEmpty(entity.getPersonPhone())) {
					str.append("#").append(entity.getPersonPhone());
				} else {
					str.append("#").append("");// 添加占位符
				}
				msg.append("<input type='hidden' name='hval' id='hval' value='"
						+ str.toString() + "'/></td> ");
				msg.append("@");
			}
		}
		this.getRequest().setAttribute("hval", msg.toString());
		if (StringUtil.isNotEmpty(model.getSendconId())) {
			this.initdatas = initMerberInfo(model.getSendconId());
		}
		attachs = conticeAttachManager.getAttachsByAttachConId(model.getTOmConference().getConferenceId(), true);
		return "apply";
	}

	private String initMerberInfo(String sendId) {
		StringBuffer htmlStr = new StringBuffer();
		List<TOmDeptreport> personLst = this.personManager.findDeptreport(
				sendId, null);
		if (StringUtil.isNotEmpty(personLst)) {
			for (TOmDeptreport entity : personLst) {
				htmlStr.append("<tr><td>");
				htmlStr
						.append("<input type=\"text\"   id=\"txtInput11\" name=\"personName\" onkeydown=\"ShowSuggest(this);\" style=\"width: 100px\" autocomplete=\"off\" value=\""
								+ entity.getConferee().getPersonName()
								+ "\" />	");
				htmlStr
						.append(" <h6 class=\"hiddenValues\" style=\"display: none;\"></h6>");
				htmlStr.append("</td>");

				htmlStr.append("<td>");
				htmlStr
						.append("<input type=\"text\" name=\"personPost\" id=\"zw\" value=\""
								+ entity.getConferee().getPersonPost()
								+ "\" />");
				htmlStr.append("</td>");

				htmlStr.append("<td>");
				/*
				 * htmlStr .append("<input type=\"text\" name=\"personSax\"
				 * id=\"xb\" style=\"width: 50px\" value=\"" +
				 * entity.getConferee().getPersonSax() + "\" />");
				 */

				htmlStr
						.append("<select name=\"personSax\" id=\"xb\" style=\"width: 50px\" >");
				if ("男".equals(entity.getConferee().getPersonSax().toString()
						.toString())) {
					htmlStr.append("<option value=\"男\" selected>男</option>");
					htmlStr.append("<option value=\"女\" >女</option>");
				} else {
					htmlStr.append("<option value=\"男\" >男</option>");
					htmlStr.append("<option value=\"女\" selected>女</option>");
				}
				htmlStr.append("</select>");

				htmlStr.append("</td>");

				htmlStr.append("<td>");
				htmlStr
						.append("<input type=\"text\" name=\"personNation\" id=\"mz\" style=\"width: 50px\" value=\""
								+ entity.getConferee().getPersonNation()
								+ "\" />");
				htmlStr.append("</td>");

				if (StringUtil.isNotEmpty(entity.getConferee()
						.getPersonMobile())) {
					htmlStr.append("<td>");
					htmlStr
							.append("<input type=\"text\" name=\"personMobile\" id=\"tel\" value=\""
									+ entity.getConferee().getPersonMobile()
									+ "\" />");
					htmlStr.append("</td>");
				} else {
					htmlStr.append("<td>");
					htmlStr
							.append("<input type=\"text\" name=\"personMobile\" id=\"tel\"  />");
					htmlStr.append("</td>");
				}

				if (StringUtil
						.isNotEmpty(entity.getConferee().getPersonPhone())) {
					htmlStr.append("<td>");
					htmlStr
							.append("<input type=\"text\" name=\"personPhone\" id=\"mobail\" value=\""
									+ entity.getConferee().getPersonPhone()
									+ "\" />");
					htmlStr.append("</td>");
				} else {
					htmlStr.append("<td>");
					htmlStr
							.append("<input type=\"text\" name=\"personPhone\" id=\"mobail\"  />");
					htmlStr.append("</td>");
				}

				htmlStr.append("<td>");
				htmlStr
						.append("<a href=# onclick=\"deltr(this)\" style=\"color: blue;font-size: 15px\">删除</a>&nbsp;");
				htmlStr.append("</td>");

				htmlStr.append("</tr>");
			}
		}
		return htmlStr.toString();
	}

	private String initMerberInfoForView(String sendId) {
		StringBuffer htmlStr = new StringBuffer();
		List<TOmDeptreport> personLst = this.personManager.findDeptreport(
				sendId, null);
		if (StringUtil.isNotEmpty(personLst)) {
			for (TOmDeptreport entity : personLst) {
				htmlStr.append("<tr><td>");
				htmlStr.append(entity.getConferee().getPersonName());
				htmlStr.append("</td>");

				htmlStr.append("<td>");
				htmlStr.append(entity.getConferee().getPersonPost());

				htmlStr.append("</td>");

				htmlStr.append("<td>");
					htmlStr.append(entity.getConferee().getPersonMobile());
				htmlStr.append("</td>");

				/*htmlStr.append("<td>");
				if(entity.getTOmConferenceSend().getTOmConference().getConferenceUsertel()!=null){
					htmlStr.append(entity.getTOmConferenceSend().getTOmConference().getConferenceUsertel());
				}
				else{
					htmlStr.append("");
				}
				htmlStr.append("</td>");*/

				/*if (StringUtil.isNotEmpty(entity.getConferee()
						.getPersonMobile())) {
					htmlStr.append("<td>");
					htmlStr.append(entity.getConferee().getPersonMobile());
					htmlStr.append("</td>");
				} else {
					htmlStr.append("<td>");

					htmlStr.append("</td>");
				}

				if (StringUtil
						.isNotEmpty(entity.getConferee().getPersonPhone())) {
					htmlStr.append("<td>");
					htmlStr.append(entity.getConferee().getPersonPhone());
					htmlStr.append("</td>");
				} else {
					htmlStr.append("<td>");
					htmlStr.append("</td>");
				}*/

				htmlStr.append("</tr>");
			}
		}
		return htmlStr.toString();
	}

	// 保存会议报名操作
	public String saveToApply() {
		if (StringUtil.isNotEmpty(this.personName)) {
			List<TOmConPerson> list = new ArrayList();
			int i = 0;
			for (String pName : personName) {
				TOmConPerson person = new TOmConPerson();
				if (StringUtil.isNotEmpty(pName)) {
					person.setDepid(this.depId);
					person.setPersonMobile(personMobile[i]);
					person.setPersonName(pName);
					person.setPersonNation(personNation[i]);
					person.setPersonPhone(personPhone[i]);
					person.setPersonPost(personPost[i]);
					person.setPersonSax(personSax[i]);
					list.add(person);
					// this.personManager.saveToReport(ids, person);
				}
				i++;
			}
			this.personManager.saveToReport(ids, list);
		}

		return this.renderText("ok");
	}

	/***************************************************************************
	 * 
	 * 方法简要描述:单位人员上报，领导名册选择树
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 12, 2013
	 * @Author 万俊龙
	 * @return
	 * @version 1.0
	 * @see
	 */
	public String selMemberForReport() {
		if (StringUtil.isEmpty(initleader)) {
			initleader = "";
		}

		// 初始化已选领导名册
		List<TOmDeptreport> list = this.personManager.findDeptreport(
				getCurrentOrg(), "");
		if (StringUtil.isNotEmpty(list)) {
			for (TOmDeptreport entity : list) {
				initleader += entity.getConferee().getPersonId() + ",";
			}
		}

		return "reportmembertree";
	}

	/***************************************************************************
	 * 
	 * 方法简要描述:转到选择人员树形，人员列表显示界面
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 12, 2013
	 * @Author 万俊龙
	 * @return
	 * @throws Exception
	 * @version 1.0
	 * @see
	 */
	public String selectOrgPerson() throws Exception {

		return "orgTreeandpersonal";
	}

	/***************************************************************************
	 * 
	 * 方法简要描述:获取组列表,通过树标签在前台展示
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 12, 2013
	 * @Author 万俊龙
	 * @return
	 * @throws Exception
	 * @version 1.0
	 * @see
	 */
	/*public String orgtree() throws Exception {
		List<TempPo> nodeList = new ArrayList<TempPo>();
		UserInfo user = userManager.getCurrentUserInfo();
		Assert.notNull(user);
		List<ToaSeatsetDep> epList = null;
	 	 if ("1".equals(user.getUserIsSupManager())) {
			epList = this.leaderBookManager.getAllDepInfos();
		} else {
			epList = this.leaderBookManager.getSelfAndChildrenDeptsByPid(user
					.getOrgId());
		}  
		 epList =
	 this.leaderBookManager.getSelfAndChildrenDeptsByPid(user
				.getOrgId()); 
		if (StringUtil.isNotEmpty(epList)) {
			for (int i = 0; i < epList.size(); i++) {
				ToaSeatsetDep tssd = epList.get(i);
				TempPo leaderbookRoot = new TempPo();// 构造领导名称通讯录根节点
				leaderbookRoot.setId(tssd.getDepId());
				leaderbookRoot.setCodeId(tssd.getCode());
				leaderbookRoot.setName(tssd.getDepFullName());// 节点名,从资源文件中读取
			  if (!"1".equals(user.getUserIsSupManager())&&user.getOrgId().equals(tssd.getDepId())) {
			 	///if(user.getOrgId().equals(tssd.getDepId())){
					leaderbookRoot.setParentId("0");
				} else {
					leaderbookRoot.setParentId(tssd.getParentId());
				}
				leaderbookRoot.setType("bookAddress");
				nodeList.add(leaderbookRoot);
			}
		}
		getRequest().setAttribute("groupLst", nodeList);
		return "orgtree";
	}
*/
	/***************************************************************************
	 * 
	 * 方法简要描述:获取联系人列表 如果组orgCode为null,则不加载联系人
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 12, 2013
	 * @Author 万俊龙
	 * @return
	 * @throws Exception
	 * @version 1.0
	 * @see
	 */
	public String showlist() throws Exception {
		if (StringUtil.isNotEmpty(orgCode)) {
//			userpage = this.leaderBookManager.getAddressList(userpage, orgCode,
//					searchuserName);

		}
		return "showlist";
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

	public TOmConferenceSend getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	public Page<TOmConferenceSend> getPage() {
		return page;
	}

	public void setPage(Page<TOmConferenceSend> page) {
		this.page = page;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getConId() {
		return conId;
	}

	public void setConId(String conId) {
		this.conId = conId;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public Date getSearchStime() {
		return searchStime;
	}

	public void setSearchStime(Date searchStime) {
		this.searchStime = searchStime;
	}

	public Date getSearchEtime() {
		return searchEtime;
	}

	public void setSearchEtime(Date searchEtime) {
		this.searchEtime = searchEtime;
	}

	public Date getSearchRegendtime() {
		return searchRegendtime;
	}

	public void setSearchRegendtime(Date searchRegendtime) {
		this.searchRegendtime = searchRegendtime;
	}

	public void setModel(TOmConferenceSend model) {
		this.model = model;
	}

	public String[] getPersonName() {
		return personName;
	}

	public void setPersonName(String[] personName) {
		this.personName = personName;
	}

	public String[] getPersonSax() {
		return personSax;
	}

	public void setPersonSax(String[] personSax) {
		this.personSax = personSax;
	}

	public String[] getPersonNation() {
		return personNation;
	}

	public void setPersonNation(String[] personNation) {
		this.personNation = personNation;
	}

	public String[] getPersonMobile() {
		return personMobile;
	}

	public void setPersonMobile(String[] personMobile) {
		this.personMobile = personMobile;
	}

	public String[] getPersonPost() {
		return personPost;
	}

	public void setPersonPost(String[] personPost) {
		this.personPost = personPost;
	}

	public String[] getPersonPhone() {
		return personPhone;
	}

	public void setPersonPhone(String[] personPhone) {
		this.personPhone = personPhone;
	}

	public String getDepId() {
		return depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
	}

	public String getInitdatas() {
		return initdatas;
	}

	public void setInitdatas(String initdatas) {
		this.initdatas = initdatas;
	}
 

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getSearchuserName() {
		return searchuserName;
	}

	public void setSearchuserName(String searchuserName) {
		this.searchuserName = searchuserName;
	}

	public String getOrgusernameback() {
		return orgusernameback;
	}

	public void setOrgusernameback(String orgusernameback) {
		this.orgusernameback = orgusernameback;
	}

	public String getInitleader() {
		return initleader;
	}

	public void setInitleader(String initleader) {
		this.initleader = initleader;
	}

	public Page<Object[]> getUserpage() {
		return userpage;
	}

	public void setUserpage(Page<Object[]> userpage) {
		this.userpage = userpage;
	}

	public TOmConAttach getAttach() {
		return attach;
	}

	public void setAttach(TOmConAttach attach) {
		this.attach = attach;
	}

	public List<TOmConAttach> getAttachs() {
		return attachs;
	}

	public void setAttachs(List<TOmConAttach> attachs) {
		this.attachs = attachs;
	}

	
}
