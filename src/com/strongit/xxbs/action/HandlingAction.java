package com.strongit.xxbs.action;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.jspsmart.upload.Request;
import com.opensymphony.xwork2.ActionContext;
import com.strongit.oa.util.OALogInfo;
import com.strongit.platform.webcomponent.tree.JsonUtil;
import com.strongit.platform.webcomponent.tree.TreeNode;
import com.strongit.utils.TimeLog;
import com.strongit.utils.Uuid;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.uums.security.UserInfo;
import com.strongit.oa.common.user.IUserService;
import com.strongit.xxbs.common.PathUtil;
import com.strongit.xxbs.common.contant.Info;
import com.strongit.xxbs.common.contant.Publish;
import com.strongit.xxbs.dto.CommentDto;
import com.strongit.xxbs.dto.PublishDto;
import com.strongit.xxbs.dto.PublishListDto;
import com.strongit.xxbs.dto.ReportDto;
import com.strongit.xxbs.dto.ScoreItemDto;
import com.strongit.xxbs.entity.TInfoBaseColumn;
import com.strongit.xxbs.entity.TInfoBaseComment;
import com.strongit.xxbs.entity.TInfoBaseIssue;
import com.strongit.xxbs.entity.TInfoBaseJournal;
import com.strongit.xxbs.entity.TInfoBasePS;
import com.strongit.xxbs.entity.TInfoBasePublish;
import com.strongit.xxbs.entity.TInfoBasePublishUser;
import com.strongit.xxbs.entity.TInfoBaseScore;
import com.strongit.xxbs.entity.TInfoQueryTree;
import com.strongit.xxbs.service.IColumnService;
import com.strongit.xxbs.service.IGradeService;
import com.strongit.xxbs.service.IIssueService;
import com.strongit.xxbs.service.IJournalService;
import com.strongit.xxbs.service.IOrgService;
import com.strongit.xxbs.service.IPublishService;
import com.strongit.xxbs.service.IScoreItemService;
import com.strongit.xxbs.service.ISubmitService;
import com.strongit.xxbs.service.JdbcPublishService;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.util.ThreadLocalUtils;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 
 * Project : 政务信息采编与报送系统 Copyright : Strong Digital Technology Co. Ltd. All
 * right reserved
 * 
 * @author 钟伟
 * @version 1.0, 2012-4-11 Description: 信息处理Action类
 */
public class HandlingAction extends BaseActionSupport<TInfoBasePublish> {
	private static final long serialVersionUID = 1L;

	private TInfoBasePublish model = new TInfoBasePublish();
	private ISubmitService submitService;
	private IPublishService publishService;
	private IOrgService orgService;
	@Autowired
	private IUserService userService;
	private IJournalService journalService;
	private IIssueService issueService;
	private IScoreItemService scoreItemService;
	private IGradeService gradeService;
	private JdbcPublishService jdbcPublishService;
	private IColumnService coulmnService;

	private int curpage;
	private int unitpage;
	private String sidx;
	private String sord;
	private Page<TInfoBasePublish> page;
	private Page<PublishListDto> pagePl;

	private String op;
	private String toId;
	private String qs;
	private String assigntoname;
	private Map<String, String> orgMap;
	private String dp[];

	private List<TInfoBaseJournal> journals = new ArrayList<TInfoBaseJournal>();
	private List<ScoreItemDto> scoreItems = new ArrayList<ScoreItemDto>();
	private List<TInfoBaseColumn> columns = new ArrayList<TInfoBaseColumn>();
	private List<ReportDto> reports = new ArrayList<ReportDto>();
	private List<PublishDto> list1 = new ArrayList<PublishDto>();

	private String orgName = "";
	private String userName;
	private String jourId;
	private String useStatus = Publish.ALL;
	private String[] scIds;
	private String[] checkedScIds;
	private Boolean preUse;
	private Boolean isJournalUnselected = false;
	private Boolean canCancelAdopt;

	private String publishNum;
	private String useNum;

	private String orgId;

	private TUumsBaseOrg baseOrg;
	private String IssuePeople; // 签发领导
	private String editor; // 责任编辑
	private List<TInfoBaseComment> comment;
	private Boolean isTrue = false;

	private List<TUumsBaseOrg> orgList;
	private List<TInfoBasePublish> pubList = new ArrayList<TInfoBasePublish>();
	private String userTelephone;

	@Autowired
	public void setCoulmnService(IColumnService coulmnService) {
		this.coulmnService = coulmnService;
	}

	@Autowired
	public void setPublishService(IPublishService publishService) {
		this.publishService = publishService;
	}

	@Autowired
	public void setSubmitService(ISubmitService submitService) {
		this.submitService = submitService;
	}

	@Autowired
	public void setOrgService(IOrgService orgService) {
		this.orgService = orgService;
	}

	@Autowired
	public void setJournalService(IJournalService journalService) {
		this.journalService = journalService;
	}

	@Autowired
	public void setIssueService(IIssueService issueService) {
		this.issueService = issueService;
	}

	@Autowired
	public void setScoreItemService(IScoreItemService scoreItemService) {
		this.scoreItemService = scoreItemService;
	}

	@Autowired
	public void setGradeService(IGradeService gradeService) {
		this.gradeService = gradeService;
	}

	@Autowired
	public void setJdbcPublishService(JdbcPublishService jdbcPublishService) {
		this.jdbcPublishService = jdbcPublishService;
	}

	public TInfoBasePublish getModel() {
		return model;
	}

	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
	}

	@Override
	public String save() throws Exception {
		UserInfo userInfo = (UserInfo) getUserDetails();
		String pubId = model.getPubId();
		TInfoBasePublish publish = publishService.getPublish(pubId);
		publish.setPubTitle(model.getPubTitle());
		publish.setPubInfoType(model.getPubInfoType());
		publish.setPubSigner(model.getPubSigner());
		publish.setPubEditor(model.getPubEditor());
		publish.setPubEditContent(model.getPubEditContent());
		publish.setPubUseStatus(model.getPubUseStatus());
		publish.setPubUseScore(model.getPubUseScore());
		publish.setPubRemarkScore(model.getPubRemarkScore());
		// 是否网上发布
		if (model.getIsOA() != null) {
			publish.setIsOA(model.getIsOA());
		}
		// publish.setPubIsInstruction(model.getPubIsInstruction());
		publish.setPubInstructionContent(model.getPubInstructionContent());
		publish.setPubInstructor(model.getPubInstructor());
		publish.setPubComment(model.getPubComment());
		// 评论内容是否为空
		if ((model.getPubComment() != null)
				&& (!"".equals(model.getPubComment()))) {
			publish.setPubIsComment(Publish.COMMENTED);
		}

		publish.setPubAdoptOrgId(userInfo.getOrgId());
		publish.setPubAdoptUserId(userInfo.getUserId());
		if (model.getTInfoBaseIssue() != null) {
			publish.setTInfoBaseIssue(model.getTInfoBaseIssue());
		}
		if (model.getTInfoBaseColumn() != null) {
			publish.setTInfoBaseColumn(model.getTInfoBaseColumn());
		}
		if (model.getTInfoBaseIssue() != null) {
			String issId = model.getTInfoBaseIssue().getIssId();
			TInfoBaseIssue issue = issueService.getIssue(issId);
			if (issue.getTInfoBaseJournal().getJourCode() != null) {
				int issCode = issue.getTInfoBaseJournal().getJourCode();
				// 期刊得分
				publish.setPubCode(issCode);
			}

		}
		if (model.getTInfoBaseColumn() != null) {
			String columnId = model.getTInfoBaseColumn().getColId();
			TInfoBaseColumn column = coulmnService.getColumn(columnId);
			// 插入栏目文章排序
			if (model.getPubSort() == null) {
				String colId = column.getColId();
				String issId = model.getTInfoBaseIssue().getIssId();
				List list = publishService.getPublishOrdersort(colId, issId);
				if (list.get(0) != null) {
					publish.setPubSort(Integer.valueOf(list.get(0).toString()) + 10);
				} else {
					publish.setPubSort(1);
				}
			}
			if (column.getColCode() != null) {
				int colCode = column.getColCode();

				// 栏目得分
				publish.setPubAdoptCode(colCode);
			}
		}

		/*
		 * if(cname.equals("领导批示")||cname.equals("领导活动")||cname.equals("重要发文")){
		 * publish.setPubCode(3); } else{ publish.setPubCode(1); }
		 */
		publish.setPubSubmitDate(new Date());
		// 是否合并
		String uuid = Uuid.get();
		String mergeOrgId = getRequest().getParameter("mergeOrgId");
		if (mergeOrgId != null) {
			String ms[] = toId.split(",");
			String msd[] = mergeOrgId.split(",");
			// 相同单位的部门合并，去除重名
			Set set = new TreeSet();
			String merName = "";
			for (int i = 0; i < msd.length; i++) {
				set.add(msd[i]);
			}
			Iterator it = set.iterator();
			while (it.hasNext()) {
				merName = merName + it.next() + ",";
			}
			merName = merName.substring(0, merName.length() - 1);
			msd = merName.split(",");
			String ss = "," + publish.getOrgId() + ",";
			for (int i = 0; i < ms.length; i++) {
				// 在循环中去除主文章
				if (!ms[i].equals(pubId)) {
					TInfoBasePublish pub = publishService.getPublish(ms[i]);

					pub.setPubTitle(model.getPubTitle());
					pub.setPubMergeFlag(uuid);
					pub.setPubIsMerge("0");
					pub.setTInfoBaseIssue(model.getTInfoBaseIssue());
					pub.setTInfoBaseColumn(model.getTInfoBaseColumn());
					if (model.getTInfoBaseIssue() != null) {
						String issId = model.getTInfoBaseIssue().getIssId();
						TInfoBaseIssue issue = issueService.getIssue(issId);
						if (issue.getTInfoBaseJournal().getJourCode() != null) {
							int issCode = issue.getTInfoBaseJournal()
									.getJourCode();
							if (ss.indexOf("," + pub.getOrgId() + ",") == -1) {
								pub.setPubCode(issCode);
							}
						}
					}
					if (model.getTInfoBaseColumn() != null) {
						String columnId = model.getTInfoBaseColumn().getColId();
						TInfoBaseColumn column = coulmnService
								.getColumn(columnId);
						// 插入栏目文章排序
						if (model.getPubSort() == null) {
							String colId = column.getColId();
							String issId = model.getTInfoBaseIssue().getIssId();
							List list = publishService.getPublishOrdersort(
									colId, issId);
							if (list.get(0) != null) {
								pub.setPubSort(Integer.valueOf(list.get(0)
										.toString()) + 10);
							} else {
								pub.setPubSort(1);
							}
						}
						if (column.getColCode() != null) {
							int colCode = column.getColCode();
							if (ss.indexOf("," + pub.getOrgId() + ",") == -1) {
								pub.setPubAdoptCode(colCode);
								ss = ss + "," + pub.getOrgId() + ",";
							}
						}
					}
					pub.setPubSubmitDate(new Date());
					pub.setPubUseStatus(Publish.USED);
					String orgName = getRequest().getParameter("orgName");
					pub.setPubMergeName(orgName);
					// 更新副文章
					publishService.updatePublish(pub);
				}
			}
		}
		if (publish.getPubIsMerge() == null)
			publish.setPubIsMerge("1");

		if ((model.getPubMergeOrg() != null)
				&& (!"".equals(model.getPubMergeOrg()))) {
			publish.setPubMergeOrg(model.getPubMergeOrg());
		} else {
			publish.setPubMergeOrg(mergeOrgId);
			String orgName = getRequest().getParameter("orgName");
			publish.setPubMergeName(orgName);
		}
		if (publish.getPubMergeFlag() == null)
			publish.setPubMergeFlag(uuid);
		publishService.savePublish(publish, scIds, new OALogInfo(
				"信息采编-信息处理-『采用』：" + publish.getPubTitle()));

		String userId = userInfo.getUserId();
		TInfoBasePublishUser pubUser = publishService.getPU(pubId, userId);
		if (pubUser == null) {
			publishService.savePU(pubId, userId);
		}
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(Info.SUCCESS_AND_CLOSE);
		return null;
	}

	public String tree() throws Exception {
		return "tree";
	}

	public String content() throws Exception {
		orgList = orgService.getOrgNames1();
		return "content";
	}

	public String showTree() throws Exception {
		List<TreeNode> nodeList = new ArrayList<TreeNode>();
		List<TInfoQueryTree> allList = submitService.getQueryTree();

		TreeNode node;
		for (TInfoQueryTree row : allList) {
			node = new TreeNode();
			node.setPid(row.getPid());

			node.setId(row.getId());
			node.setText(row.getName());
			node.setValue(row.getValue());

			node.setComplete(true);
			node.setShowcheck(false);
			node.setIsexpand(true);
			nodeList.add(node);
		}

		String jsonTreeNode = JsonUtil.fromTreeNodes(nodeList);

		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonTreeNode);
		return null;
	}

	public String test() throws Exception {
		return "test";
	}

	public String showList() throws Exception {
		// Random r = new Random(new Date().getTime());
		// int logId = r.nextInt();
		// TimeLog.log(logId, "Action-start", new Date());
		page = new Page<TInfoBasePublish>(unitpage, true);
		page.setPageNo(curpage);
		page.setPageSize(unitpage);

		page.setOrder(sord);
		page.setOrderBy(sidx);

		UserInfo userInfo = (UserInfo) getUserDetails();
		if (getRequest().getParameter("isSearch") != null
				&& getRequest().getParameter("isSearch").equals("true")) {
			// page.setPageNo(1);
			Map<String, String> search = new HashMap<String, String>();
			String pubTitle = getRequest().getParameter("pubTitle");
			pubTitle = pubTitle.replace("%", "\'\'%");
			String startDate = getRequest().getParameter("startDate");
			String endDate = getRequest().getParameter("endDate");
			String orgId = getRequest().getParameter("orgId");
			search.put("pubTitle", pubTitle);
			search.put("startDate", startDate);
			search.put("endDate", endDate);
			search.put("useStatus", useStatus);
			search.put("orgName", orgId);
			page = publishService.findPublishs1(page, orgId, search, true);
		} else {
			if (qs == null || "".equals(qs)) {
				page = publishService.getPublishs1(page, Publish.ALL,
						Publish.SUBMITTED, useStatus);
			} else {
				userInfo = (UserInfo) getUserDetails();
				ThreadLocalUtils.put("userInfo", userInfo);
				String userId = userInfo.getUserId();
				if ("readR".equals(qs)) {
					List<TInfoBasePublishUser> puList = publishService
							.getPUList(userId);
					String pubIds[] = new String[puList.size()];
					for (int i = 0; i < puList.size(); i++) {
						pubIds[i] = puList.get(i).getPubId();
					}
					page = publishService.getPublishsByuser(page, Publish.ALL,
							Publish.SUBMITTED, useStatus, pubIds);
				} else if ("readU".equals(qs)) {
					page = publishService.findPublishsIsUread(page, curpage,
							unitpage);

				} else {
					page = submitService.findSubmittedByQs(page, qs);
				}
			}
		}

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		if (page.getTotalPages() == -1) {
			page.setTotalCount(0);
		}
		jsonObj.put("totalpages", page.getTotalPages());
		jsonObj.put("totalrecords", page.getTotalCount());

		List<TInfoBasePublish> result = page.getResult();
		JSONArray rows = new JSONArray();
		if (result != null) {
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Map<String, String> orgNames = orgService.getOrgNames();
			Map<String, String> isRead = new HashMap<String, String>();

			for (TInfoBasePublish one : result) {
				Boolean isRead1 = publishService.isRead(one.getPubId(),
						userInfo.getUserId());
				String read = null;
				if (isRead1) {
					read = "1";
				} else {
					read = "0";
				}

				isRead.put(one.getPubTitle(), read);
				JSONObject obj = new JSONObject();
				obj.put("pubId", one.getPubId());
				obj.put("pubTitle", one.getPubTitle());
				obj.put("pubInfoType", one.getPubInfoType());
				// obj.put("pubIsAppoint", one.getPubIsAppoint());
				obj.put("pubUseStatus", one.getPubUseStatus());
				obj.put("pubIsShare", one.getPubIsShare());
				obj.put("onlyDate", sdfDate.format(one.getPubDate()));
				obj.put("pubDate", sdfTime.format(one.getPubDate()));
				// obj.put("orgName", orgNames.get(one.getOrgId()));
				obj.put("pubIsInstruction", one.getPubIsInstruction());
				obj.put("pubIsRead", read);
				obj.put("pubIsComment", one.getPubIsComment());
				String mm = "";
				if ((one.getPubMergeOrg() != null)
						&& (!"".equals(one.getPubMergeOrg()))) {

					obj.put("orgName", one.getPubMergeName());
				} else {
					obj.put("orgName", one.getOrgName());
				}
				if ((!"".equals(one.getPubMergeOrg()))
						&& (one.getPubMergeOrg() != null)) {
					obj.put("pubMergeOrg", 1);
				} else {
					obj.put("pubMergeOrg", 0);
				}
				// obj.put("issName", one.getTInfoBaseIssue().getIssNumber());
				// if(Publish.USED.equals(one.getPubUseStatus()))
				// {
				// Map<String, String> jourNames =
				// journalService.getJourIdNames();
				// obj.put("journalName",
				// jourNames.get(one.getTInfoBaseIssue().getTInfoBaseJournal().getJourId()));
				// }
				rows.add(obj);
			}
		}
		jsonObj.put("rows", rows);
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		// TimeLog.log(logId, "Action-end", new Date());
		return null;
	}

	public String showListByjdbc() throws Exception {
		// Random r = new Random(new Date().getTime());
		// int logId = r.nextInt();
		// TimeLog.log(logId, "Action-start", new Date());
		pagePl = new Page<PublishListDto>(unitpage, true);
		pagePl.setPageNo(curpage);
		pagePl.setPageSize(unitpage);

		pagePl.setOrder(sord);
		pagePl.setOrderBy(sidx);

		UserInfo userInfo = (UserInfo) getUserDetails();
		if (getRequest().getParameter("isSearch") != null
				&& getRequest().getParameter("isSearch").equals("true")) {
			// pagePl.setPageNo(1);
			// curpage = 1;
			Map<String, String> search = new HashMap<String, String>();
			String pubTitle = getRequest().getParameter("pubTitle");
			pubTitle = pubTitle.replace("%", "\'\'%");
			String startDate = getRequest().getParameter("startDate");
			String endDate = getRequest().getParameter("endDate");
			String orgId = getRequest().getParameter("orgId");
			search.put("pubTitle", pubTitle);
			search.put("startDate", startDate);
			search.put("endDate", endDate);
			search.put("useStatus", useStatus);
			pagePl = jdbcPublishService.getPublishs(pagePl, orgId,
					Publish.SUBMITTED, useStatus, search);
		} else {
			if (qs == null || "".equals(qs)) {
				// TimeLog.log(logId, "Db-start", new Date());
				pagePl = jdbcPublishService.getPublishs(pagePl, Publish.ALL,
						Publish.SUBMITTED, useStatus, null);
				// TimeLog.log(logId, "Db-end", new Date());
			}
		}

		JSONObject jsonObj = new JSONObject();
		if (pagePl.getTotalPages() == -1) {
			pagePl.setTotalCount(0);
		}
		jsonObj.put("curpage", pagePl.getPageNo());
		jsonObj.put("totalpages", pagePl.getTotalPages());
		jsonObj.put("totalrecords", pagePl.getTotalCount());

		List<PublishListDto> result = pagePl.getResult();
		JSONArray rows = new JSONArray();
		if (result != null) {
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Map<String, String> orgNames = orgService.getOrgNames();
			// Map<String, String> orgNames = jdbcPublishService.getOrgNames();
			Map<String, String> isRead = new HashMap<String, String>();

			for (PublishListDto one : result) {
				Boolean isRead1 = publishService.isRead(one.getPubId(),
						userInfo.getUserId());
				String read = null;
				if (isRead1) {
					read = "1";
				} else {
					read = "0";
				}

				isRead.put(one.getPubTitle(), read);
				JSONObject obj = new JSONObject();
				obj.put("pubId", one.getPubId());
				obj.put("pubTitle", one.getPubTitle());
				obj.put("pubIsInstruction", one.getPubIsInstruction());
				// obj.put("pubIsAppoint", one.getPubIsAppoint());
				obj.put("pubUseStatus", one.getPubUseStatus());
				obj.put("pubIsShare", one.getPubIsShare());
				obj.put("onlyDate", sdfDate.format(one.getPubDate()));
				obj.put("pubDate", sdfTime.format(one.getPubDate()));
				String mm = "";
				if ((one.getPubMergeOrg() != null)
						&& (!"".equals(one.getPubMergeOrg()))) {

					obj.put("orgName", one.getPubMergeName());
				} else {
					obj.put("orgName", one.getOrgName());
				}
				obj.put("pubIsRead", read);
				obj.put("pubIsComment", one.getPubIsComment());
				if ((!"".equals(one.getPubMergeOrg()))
						&& (one.getPubMergeOrg() != null)) {
					obj.put("pubMergeOrg", 1);
				} else {
					obj.put("pubMergeOrg", 0);
				}

				// obj.put("issName", one.getTInfoBaseIssue().getIssNumber());
				// if(Publish.USED.equals(one.getPubUseStatus()))
				// {
				// Map<String, String> jourNames =
				// journalService.getJourIdNames();
				// obj.put("journalName",
				// jourNames.get(one.getTInfoBaseIssue().getTInfoBaseJournal().getJourId()));
				// }
				rows.add(obj);
			}
		}
		jsonObj.put("rows", rows);
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		// TimeLog.log(logId, "Action-end", new Date());
		return null;
	}

	public String viewShare() throws Exception {
		model = publishService.getPublish(toId);
		return "viewShare";
	}

	public String saveShare() throws Exception {
		String share = model.getPubIsShare();
		publishService.setShare(toId, share);
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(Info.SUCCESS_AND_CLOSE);
		return null;
	}

	public String view() throws Exception {
		model = publishService.getPublish(toId);
		pubview();
		getRequest().setAttribute("orgName", orgName);
		return "view";
	}

	// 公用的VIEW方法
	public void pubview() throws Exception {
		String mergeOrgId = model.getPubMergeOrg();
		getRequest().setAttribute("mergeOrgId", mergeOrgId);
		String oldtitle = model.getPubRawTitle();
		oldtitle = oldtitle.replaceAll("\"", "\\“");
		model.setPubRawTitle(oldtitle);
		String title = model.getPubTitle();
		title = title.replaceAll("\"", "\\“");
		model.setPubTitle(title);
		if ((Publish.USED.equals(model.getPubUseStatus()))
				&& ("1".equals(model.getTInfoBaseIssue().getIssIsPublish()))) {
			journals = journalService.getJourals();
		} else {
			journals = journalService.getJouralsByNoPublished();
		}

		if (model.getPubUseScore() == null) {
			String scoreUse = gradeService.get("score.use");
			model.setPubUseScore(new BigDecimal(scoreUse));
		}
		if (model.getPubRemarkScore() == null) {
			String scoreInstruction = gradeService.get("score.instruction");
			model.setPubRemarkScore(new BigDecimal(scoreInstruction));
		}

		if (Publish.PRE_USED.equals(model.getPubUseStatus())) {
			preUse = Boolean.TRUE;
		}

		TUumsBaseUser user = userService.getUserInfoByUserId(model
				.getPubPublisherId());
		userName = user.getUserName();
		TUumsBaseOrg org = userService.getOrgInfoByOrgId(model.getOrgId());
		orgName = org.getOrgName();
		if ((org != null) && (org.getRest1() != null)
				&& (!"".equals(org.getRest1()))) {
			String rs1 = org.getRest3();
			if ((rs1 != null) && (!"".equals(rs1))) {
				String a[] = rs1.split(",");
				IssuePeople = a[0].trim();
				editor = a[1].trim();
			}
		}

		if (model.getTInfoBaseIssue() == null) {
			isJournalUnselected = true;
			// jourId = journals.get(0).getJourId();
		} else {
			String issId = model.getTInfoBaseIssue().getIssId();
			TInfoBaseIssue issue = issueService.getIssue(issId);
			jourId = issue.getTInfoBaseJournal().getJourId();
			if (Publish.NO_PUBLISHED.equals(issue.getIssIsPublish())
					&& Publish.USED.equals(model.getPubUseStatus())) {
				canCancelAdopt = true;
			}

		}
		// 是否查看合并
		if (model.getPubMergeFlag() != null) {

			TInfoBasePublish pub1 = publishService.getPublishBymergeFlag(model
					.getPubMergeFlag());
			String merge = pub1.getPubMergeOrg();
			if (merge != null) {
				orgName = "";
				String ms[] = merge.split(",");
				for (int i = 0; i < ms.length; i++) {
					TUumsBaseOrg org1 = userService.getOrgInfoByOrgId(ms[i]);
					orgName = orgName + org1.getOrgName() + ",";
				}
				orgName = orgName.substring(0, orgName.length() - 1);
				String merName = "";
				String msname[] = orgName.split(",");
				// 去除相同单位名称
				Set set = new TreeSet();
				for (int i = 0; i < msname.length; i++) {
					set.add(msname[i]);
				}
				Iterator it = set.iterator();
				while (it.hasNext()) {
					merName = merName + it.next() + ",";
				}
				merName = merName.substring(0, merName.length() - 1);
				orgName = merName;
				canCancelAdopt = false;
			}
		}
		if (jourId != null) {
			TInfoBaseJournal journal = journalService.getJoural(jourId);
			columns.addAll(journal.getTInfoBaseColumns());
		}

		if (model.getTInfoBasePSs() != null) {
			Set<String> cScIds = new HashSet<String>();
			for (TInfoBasePS one : model.getTInfoBasePSs()) {
				cScIds.add(one.getScId());
			}
			checkedScIds = cScIds.toArray(new String[cScIds.size()]);
		}

		List<TInfoBaseScore> sis = scoreItemService.getScoreItems();
		scoreItems = new ArrayList<ScoreItemDto>();
		for (TInfoBaseScore one : sis) {
			ScoreItemDto si = new ScoreItemDto();
			String fName = one.getScName();
			fName += " (" + one.getScScore().toString() + ")";
			String state = one.getScState();
			if (state.equals(Publish.SC_YS)) {
				si.setScId(one.getScId());
				si.setScNameNumber(fName);
				scoreItems.add(si);
			}
		}
		String pubId = model.getPubId();
		UserInfo userInfo = (UserInfo) getUserDetails();
		String userId = userInfo.getUserId();
		// 标记为已读
		TInfoBasePublishUser pubUser = publishService.getPU(pubId, userId);
		if (pubUser == null) {
			publishService.savePU(pubId, userId);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		// 评论内容
		List<CommentDto> comm = new ArrayList<CommentDto>();
		comment = publishService.findComment(toId);
		for (int i = 0; i < comment.size(); i++) {
			String userId1 = comment.get(i).getComUserId();
			TUumsBaseUser user1 = userService.getUserInfoByUserId(userId1);
			String username = user1.getUserName();
			CommentDto com = new CommentDto();
			com.setCommentName(username);
			String date = sdf.format(comment.get(i).getComDate());
			com.setCommentDate(date);

			com.setCommentInfo(comment.get(i).getComInfo());
			com.setCommentId(comment.get(i).getComId());
			comm.add(com);
			TInfoBasePublish pub = publishService.getPublish(toId);
			String puberId = pub.getPubPublisherId();
			if (puberId.equals(userId)) {
				isTrue = true;
			}
		}

		ActionContext.getContext().put("comm", comm);
	}

	public String viewComment() throws Exception {
		model = publishService.getPublish(toId);
		return "viewComment";
	}

	public String saveComment() throws Exception {
		publishService.saveComment(toId, model.getPubComment());
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(Info.SUCCESS_AND_CLOSE);
		return null;
	}

	public String viewRemark() throws Exception {
		model = publishService.getPublish(toId);
		return "viewRemark";
	}

	public String isOA() throws Exception {
		model = publishService.getPublish(toId);
		return "isOA";
	}

	public String saveOA() throws Exception {
		TInfoBasePublish publish = publishService.getPublish(toId);
		publish.setIsOA(model.getIsOA());
		publishService.savePublish(publish);
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(Info.SUCCESS_AND_CLOSE);
		return null;
	}

	public String saveRemark() throws Exception {
		TInfoBasePublish publish = publishService.getPublish(toId);

		// publish.setPubRemarkScore(model.getPubRemarkScore());
		// 批示后的要情分数为9分
		if (Publish.INSTRUCTION.equals(model.getPubIsInstruction())) {
			publish.setPubCode(9);
		} else {
			int issCode = publish.getTInfoBaseIssue().getTInfoBaseJournal()
					.getJourCode();
			// 期刊得分
			publish.setPubCode(issCode);
		}
		publish.setPubIsInstruction(model.getPubIsInstruction());
		publish.setPubInstructionContent(model.getPubInstructionContent());
		publish.setPubInstructor(model.getPubInstructor());
		publishService.savePublish(publish);
		if (Publish.INSTRUCTION.equals(publish.getPubIsInstruction())) {
			String merId = publish.getPubMergeOrg();
			if (merId != null) {
				String flag = publish.getPubMergeFlag();
				List<TInfoBasePublish> pubList = publishService
						.getPublishBymerge(flag);

				for (int i = 0; i < pubList.size(); i++) {
					TInfoBasePublish pub = pubList.get(i);
					pub.setPubIsInstruction(Publish.INSTRUCTION);
					pub.setPubCode(9);
					publishService.updatePublish(pub);
				}
			}
		} else {
			String merId = publish.getPubMergeOrg();
			if (merId != null) {
				String flag = publish.getPubMergeFlag();
				List<TInfoBasePublish> pubList = publishService
						.getPublishBymerge(flag);
				for (int i = 0; i < pubList.size(); i++) {
					TInfoBasePublish pub = pubList.get(i);
					pub.setPubIsInstruction(Publish.NO_INSTRUCTION);
					int issCode = publish.getTInfoBaseIssue()
							.getTInfoBaseJournal().getJourCode();
					pub.setPubCode(issCode);
					publishService.updatePublish(pub);
				}
			}
		}
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(Info.SUCCESS_AND_CLOSE);
		return null;
	}

	public String usedReport() throws Exception {
		UserInfo userInfo = (UserInfo) getUserDetails();
		String orgId = userInfo.getOrgId();
		// publishNum = submitService.getPublishNum(toId, orgId);
		// toId是每天的时间
		List<TInfoBasePublish> lists = submitService.findUsedByDate1(toId,
				orgId);
		Map<String, String> orgNames = orgService.getOrgNames();
		Set<String> tempOrgId = new HashSet<String>();
		for (TInfoBasePublish one : lists) {
			ReportDto rd = new ReportDto();
			String oId = one.getOrgId();
			if (tempOrgId.contains(oId))
				rd.setIsFirst(false);
			else
				rd.setIsFirst(true);
			tempOrgId.add(oId);

			rd.setOrgName(orgNames.get(oId));
			rd.setPubTitle(one.getPubTitle());
			rd.setUseScore(one.getPubUseScore().toString());
			if (one.getPubIsInstruction().toString()
					.equals(Publish.INSTRUCTION)) {
				rd.setRemarkScore(one.getPubRemarkScore().toString());
			}
			reports.add(rd);
		}
		return "usedReport";
	}

	// public String usedList() throws Exception
	// {
	// //toId是date
	// JSONObject jsonObj = new JSONObject();
	// UserInfo userInfo = (UserInfo) getUserDetails();
	// String orgId = userInfo.getOrgId();
	//
	//
	// List<TInfoBasePublish> result = submitService.findUsedByDate(toId,
	// orgId);
	//
	// jsonObj.put("curpage", curpage);
	// jsonObj.put("totalpages", 1);
	// jsonObj.put("totalrecords", result.size());
	//
	// useNum = String.valueOf(result.size());
	//
	// //?
	// //Map<String, TInfoBaseAdoption> rets = adoptionService.getAdos();
	//
	// JSONArray rows = new JSONArray();
	// if(result != null)
	// {
	// Map<String, String> orgNames = orgService.getOrgNames();
	// for(TInfoBasePublish one : result)
	// {
	// JSONObject obj = new JSONObject();
	// obj.put("pubId", one.getPubId());
	// obj.put("pubTitle", one.getPubTitle());
	// obj.put("orgName", orgNames.get(one.getOrgId()));
	// //obj.put("scoreUse", rets.get(one.getPubId()).getAdoUseScore());
	// //obj.put("scoreInstruction",
	// rets.get(one.getPubId()).getAdoInstructionScore());
	// obj.put("pubDate", one.getPubDate().toString());
	// rows.add(obj);
	// }
	// }
	// jsonObj.put("rows", rows);
	// getResponse().setContentType("application/json");
	// getResponse().getWriter().write(jsonObj.toString());
	// return null;
	// }

	public String preUse() throws Exception {
		Boolean preUse = true;
		String flag = getRequest().getParameter("flag");
		if ("0".equals(flag)) {
			preUse = false;
		}
		publishService.markPreUse(toId, preUse);
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(Info.SUCCESS);
		return null;
	}

	public String columns() throws Exception {
		Set<TInfoBaseColumn> cols = new HashSet<TInfoBaseColumn>();
		JSONArray colArray = new JSONArray();

		TInfoBaseJournal journal = journalService.getJoural(toId);
		cols = journal.getTInfoBaseColumns();
		if (cols != null) {
			for (TInfoBaseColumn one : cols) {
				JSONObject obj = new JSONObject();
				obj.put("colId", one.getColId());
				obj.put("colName", one.getColName());
				colArray.add(obj);
			}
		}
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(colArray.toString());
		return null;
	}

	public String issues() throws Exception {
		// Set<TInfoBaseIssue> issues = new HashSet<TInfoBaseIssue>();
		JSONArray colArray = new JSONArray();

		TInfoBaseJournal journal = journalService.getJoural(toId);
		List<TInfoBaseIssue> issues = issueService
				.getNotPublishIssueByJour(journal.getJourId());
		// issues = journal.getTInfoBaseIssues();
		if (issues != null) {
			for (TInfoBaseIssue one : issues) {
				JSONObject obj = new JSONObject();
				obj.put("issId", one.getIssId());
				obj.put("issNumber", one.getIssNumber());
				colArray.add(obj);
			}
		}
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(colArray.toString());
		return null;
	}

	public String comment() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<CommentDto> comm = new ArrayList<CommentDto>();
		comment = publishService.findComment(toId);
		for (int i = 0; i < comment.size(); i++) {
			String userId = comment.get(i).getComUserId();
			UserInfo userInfo = (UserInfo) getUserDetails();
			String userId1 = userInfo.getUserId();
			String true1 = "";
			TUumsBaseUser user = userService.getUserInfoByUserId(userId);
			String username = user.getUserName();
			CommentDto com = new CommentDto();
			com.setCommentName(username);
			String date = sdf.format(comment.get(i).getComDate());
			com.setCommentDate(date);
			if (userId.equals(userId1)) {
				true1 = "1";
			} else {
				true1 = "0";
			}
			com.setCommentIstrue(true1);
			com.setCommentInfo(comment.get(i).getComInfo());
			com.setCommentId(comment.get(i).getComId());
			comm.add(com);
		}
		ActionContext.getContext().put("comm", comm);
		TInfoBasePublish publish = publishService.getPublish(toId);
		String userId = publish.getPubAdoptUserId();
		if (userId != null) {
			String userName = userService.getUserInfoByUserId(userId)
					.getUserName();
			String info = publish.getPubComment();
			String date = sdf.format(publish.getPubSubmitDate());
			dp = new String[3];
			dp[0] = userName;
			dp[1] = date;
			dp[2] = info;
			getRequest().setAttribute("dp", dp);
		}
		return "comment";
	}

	public String saveComment1() throws Exception {
		String pubId = getRequest().getParameter("pubId");
		String comment = getRequest().getParameter("comment");
		comment = java.net.URLDecoder.decode(comment, "UTF-8");
		UserInfo userInfo = (UserInfo) getUserDetails();
		String userId = userInfo.getUserId();
		TInfoBaseComment com = new TInfoBaseComment();
		Date date = new Date();
		com.setComDate(date);
		com.setComInfo(comment);
		com.setComPublishId(pubId);
		com.setComUserId(userId);
		publishService.saveComment(com);
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(Info.SUCCESS);
		return null;
	}

	public String deleteComment() throws Exception {
		TInfoBaseComment com = publishService.findComment1(toId);
		String pubId = com.getComPublishId();
		com.setComId(toId);
		publishService.delectComment(com);
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(SUCCESS);
		return null;
	}

	public String isYcaiyong(String toId) throws Exception {
		TInfoBasePublish pub = publishService.getPublish(toId);
		String state = pub.getPubSubmitStatus();
		String flag = "";
		if (state.equals(Publish.PRE_USED)) {
			flag = "true";
		}
		return flag;
	}

	public String print() throws Exception {
		return "print";
	}

	public String print1() throws Exception {
		String ids[] = toId.split(",");
		Map<String, String> orgNames = orgService.getOrgNames();
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < ids.length; i++) {

			TInfoBasePublish pub = publishService.getPublish(ids[i]);
			PublishDto pubDto = new PublishDto();
			if (pub.getPubMergeOrg() != null) {
				pubDto.setOrgName(pub.getPubMergeName());
			} else {
				pubDto.setOrgName(pub.getOrgName());
			}
			pubDto.setPubTitle(pub.getPubTitle());
			pubDto.setPubDate(sim.format(pub.getPubDate()));
			list1.add(pubDto);
		}
		return "print2";
	}

	public String merge() throws Exception {
		String ms[] = toId.split(",");
		String mergeOrgName = "";
		String mergeOrgId = "";
		for (int i = 0; i < ms.length; i++) {
			TInfoBasePublish pu = publishService.getPublish(ms[i]);
			pubList.add(pu);
			TUumsBaseOrg org = userService.getOrgInfoByOrgId(pu.getOrgId());
			mergeOrgName = mergeOrgName + org.getOrgName() + ",";
			mergeOrgId = mergeOrgId + org.getOrgId() + ",";
		}
		String merName = "";
		String msname[] = mergeOrgName.split(",");
		Set set = new TreeSet();
		for (int i = 0; i < msname.length; i++) {
			set.add(msname[i]);
		}
		Iterator it = set.iterator();
		while (it.hasNext()) {
			merName = merName + it.next() + ",";
		}
		merName = merName.substring(0, merName.length() - 1);
		mergeOrgId = mergeOrgId.substring(0, mergeOrgId.length() - 1);
		getRequest().setAttribute("mergeOrgName", merName);
		getRequest().setAttribute("mergeOrgId", mergeOrgId);
		model = publishService.getPublish(ms[0]);
		String oldtitle = model.getPubRawTitle();
		oldtitle = oldtitle.replaceAll("\"", "\\“");
		model.setPubRawTitle(oldtitle);
		String title = model.getPubTitle();
		title = title.replaceAll("\"", "\\“");
		model.setPubTitle(title);
		if (Publish.USED.equals(model.getPubUseStatus())) {
			journals = journalService.getJourals();
		} else {
			journals = journalService.getJouralsByNoPublished();
		}

		if (model.getPubUseScore() == null) {
			String scoreUse = gradeService.get("score.use");
			model.setPubUseScore(new BigDecimal(scoreUse));
		}
		if (model.getPubRemarkScore() == null) {
			String scoreInstruction = gradeService.get("score.instruction");
			model.setPubRemarkScore(new BigDecimal(scoreInstruction));
		}

		if (Publish.PRE_USED.equals(model.getPubUseStatus())) {
			preUse = Boolean.TRUE;
		}
		UserInfo userInfo = (UserInfo) getUserDetails();
		TUumsBaseUser user = userService.getUserInfoByUserId(userInfo
				.getUserId());
		userName = user.getUserName();
		TUumsBaseOrg org = userService.getOrgInfoByOrgId(userInfo.getOrgId());
		String rs1 = org.getRest3();
		if ((rs1 != null) && (!"".equals(rs1))) {
			String a[] = rs1.split(",");
			IssuePeople = a[0].trim();
			editor = a[1].trim();
		}
		orgName = org.getOrgName();
		if (model.getTInfoBaseIssue() == null) {
			isJournalUnselected = true;
			// jourId = journals.get(0).getJourId();
		} else {
			String issId = model.getTInfoBaseIssue().getIssId();
			TInfoBaseIssue issue = issueService.getIssue(issId);
			jourId = issue.getTInfoBaseJournal().getJourId();
			if (Publish.NO_PUBLISHED.equals(issue.getIssIsPublish())
					&& Publish.USED.equals(model.getPubUseStatus())) {
				canCancelAdopt = true;
			}

		}

		if (jourId != null) {
			TInfoBaseJournal journal = journalService.getJoural(jourId);
			columns.addAll(journal.getTInfoBaseColumns());
		}

		if (model.getTInfoBasePSs() != null) {
			Set<String> cScIds = new HashSet<String>();
			for (TInfoBasePS one : model.getTInfoBasePSs()) {
				cScIds.add(one.getScId());
			}
			checkedScIds = cScIds.toArray(new String[cScIds.size()]);
		}

		List<TInfoBaseScore> sis = scoreItemService.getScoreItems();
		scoreItems = new ArrayList<ScoreItemDto>();
		for (TInfoBaseScore one : sis) {
			ScoreItemDto si = new ScoreItemDto();
			String fName = one.getScName();
			fName += " (" + one.getScScore().toString() + ")";
			String state = one.getScState();
			if (state.equals(Publish.SC_YS)) {
				si.setScId(one.getScId());
				si.setScNameNumber(fName);
				scoreItems.add(si);
			}
		}
		String pubId = model.getPubId();
		String userId = userInfo.getUserId();

		TInfoBasePublishUser pubUser = publishService.getPU(pubId, userId);
		if (pubUser == null) {
			publishService.savePU(pubId, userId);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<CommentDto> comm = new ArrayList<CommentDto>();
		comment = publishService.findComment(toId);
		for (int i = 0; i < comment.size(); i++) {
			String userId1 = comment.get(i).getComUserId();
			TUumsBaseUser user1 = userService.getUserInfoByUserId(userId1);
			String username = user1.getUserName();
			CommentDto com = new CommentDto();
			com.setCommentName(username);
			String date = sdf.format(comment.get(i).getComDate());
			com.setCommentDate(date);

			com.setCommentInfo(comment.get(i).getComInfo());
			com.setCommentId(comment.get(i).getComId());
			comm.add(com);
			TInfoBasePublish pub = publishService.getPublish(toId);
			String puberId = pub.getPubPublisherId();
			if (puberId.equals(userId)) {
				isTrue = true;
			}
		}
		ActionContext.getContext().put("comm", comm);
		return "merge";
	}

	// 解除合并
	public String closemerge() throws Exception {
		model = publishService.getPublish(toId);
		String flag = "success";
		String mid = model.getPubMergeOrg();
		if (mid == null) {
			flag = "nosuccess1";
			getResponse().setContentType("text/html");
			getResponse().getWriter().write(flag);
			return null;
		}
		if (model.getTInfoBaseIssue() != null) {
			String isPub = model.getTInfoBaseIssue().getIssIsPublish();
			if ("1".equals(isPub)) {
				flag = "nosuccess";
				getResponse().setContentType("text/html");
				getResponse().getWriter().write(flag);
				return null;
			}
		}
		String pubFlag = model.getPubMergeFlag();
		if (pubFlag != null) {
			pubList = publishService.getPublishBymerge(pubFlag);
			for (int i = 0; i < pubList.size(); i++) {
				TInfoBasePublish pub = pubList.get(i);
				pub.setPubIsMerge(null);
				pub.setPubMergeFlag(null);
				pub.setPubMergeOrg(null);
				pub.setPubMergeName(null);
				pub.setTInfoBaseColumn(null);
				pub.setTInfoBaseIssue(null);
				pub.setPubUseStatus(Publish.PRE_USED);
				// pub.setPubSubmitDate(null);
				pub.setPubCode(null);
				pub.setPubAdoptCode(null);
				pub.setPubTitle(pub.getPubRawTitle());
				pub.setPubEditContent(pub.getPubRawContent());
				publishService.updatePublish(pub);
			}
		}
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(flag);
		return null;
	}

	// 是否采用
	public String isHand() throws Exception {
		String flag = "";
		String ms[] = toId.split(",");
		for (int i = 0; i < ms.length; i++) {
			model = publishService.getPublish(ms[i]);
			if (model.getPubUseStatus().equals("1")) {
				flag = "nosuccess";
				break;
			}
			// if((i<ms.length)&&(model.getOrgId().equals(publishService.getPublish(ms[i+1]).getOrgId()))){
			// flag="nosuccess1";
			// break;
			// }
		}
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(flag);
		return null;
	}

	// 是否采用
	public String isPublish() throws Exception {
		String flag = "";
		model = publishService.getPublish(toId);
		if ((model.getTInfoBaseIssue() == null)) {
			flag = "nosuccess";
		} else if (!model.getTInfoBaseIssue().getIssIsPublish().equals("1")) {
			flag = "nosuccess";
		}
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(flag);
		return null;
	}

	public String viewmerge() throws Exception {
		return "viewmerge";
	}
	
	
	/**
	 * 查看合并信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String viewIsMerge() throws Exception {
		model = publishService.getPublish(toId);

		return "viewIsMerge";
	}

	public String mergeOrg() throws Exception {

		page = new Page<TInfoBasePublish>(unitpage, true);
		page.setPageNo(curpage);
		page.setPageSize(unitpage);

		page.setOrder(sord);
		page.setOrderBy(sidx);
		String flag = getRequest().getParameter("flag");
		TInfoBasePublish pub = publishService.getPublish(flag);
		page = publishService.getPublishBymerge1(page, pub.getPubMergeFlag());
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		if (page.getTotalPages() == -1) {
			page.setTotalCount(0);
		}
		jsonObj.put("totalpages", page.getTotalPages());
		jsonObj.put("totalrecords", page.getTotalCount());

		List<TInfoBasePublish> result = page.getResult();
		JSONArray rows = new JSONArray();
		if (result != null) {
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Map<String, String> orgNames = orgService.getOrgNames();
			Map<String, String> isRead = new HashMap<String, String>();

			for (TInfoBasePublish one : result) {

				JSONObject obj = new JSONObject();
				obj.put("pubId", one.getPubId());
				obj.put("pubTitle", one.getPubRawTitle());
				obj.put("pubDate", sdfTime.format(one.getPubDate()));
				obj.put("orgName", orgNames.get(one.getOrgId()));
				rows.add(obj);
			}
		}
		jsonObj.put("rows", rows);
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		return null;
	}

	public String cancelAdopt() throws Exception {
		model = publishService.getPublish(toId);
		model.setPubUseStatus(Publish.PRE_USED);
		pubList = publishService.getPublishBymerge(model.getPubMergeFlag());
		for (int i = 0; i < pubList.size(); i++) {
			TInfoBasePublish pub = pubList.get(i);
			pub.setPubIsMerge(null);
			pub.setPubMergeFlag(null);
			pub.setPubMergeOrg(null);
			pub.setPubMergeName(null);
			pub.setTInfoBaseColumn(null);
			pub.setTInfoBaseIssue(null);
			pub.setPubUseStatus(Publish.PRE_USED);
			pub.setPubSubmitDate(null);
			pub.setPubCode(null);
			pub.setPubAdoptCode(null);
			pub.setPubTitle(pub.getPubRawTitle());
			pub.setPubEditContent(pub.getPubRawContent());
			publishService.updatePublish(pub);
		}
		if (pubList.size() == 0) {
			publishService.updatePublish(model);
		}
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(Info.SUCCESS);
		return null;
	}

	// 期刊中上移文章序号
	public String upPublish() throws Exception {
		model = publishService.getPublish(toId);
		List list = publishService.getPublishup(model.getPubSort(), model
				.getTInfoBaseColumn().getColId(), model.getTInfoBaseIssue()
				.getIssId());
		String flag = "";
		if (list.size() > 0) {
			TInfoBasePublish publish = publishService.getPublish(list.get(0)
					.toString());
			int sort1 = model.getPubSort();
			int sort2 = publish.getPubSort();
			publish.setPubSort(sort1);
			model.setPubSort(sort2);
			publishService.updatePublish(publish);
			publishService.updatePublish(model);
			flag = "success";
		} else {
			flag = "nosuccess";
		}
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(flag);
		return null;
	}

	// 期刊中下移文章序号
	public String downPublish() throws Exception {
		model = publishService.getPublish(toId);
		List list = publishService.getPublishdown(model.getPubSort(), model
				.getTInfoBaseColumn().getColId(), model.getTInfoBaseIssue()
				.getIssId());
		String flag = "";
		if (list.size() > 0) {
			TInfoBasePublish publish = publishService.getPublish(list.get(0)
					.toString());
			int sort1 = model.getPubSort();
			int sort2 = publish.getPubSort();
			publish.setPubSort(sort1);
			model.setPubSort(sort2);
			publishService.updatePublish(publish);
			publishService.updatePublish(model);
			flag = "success";
		} else {
			flag = "nosuccess";
		}
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(flag);
		return null;
	}

	// 补录
	public String entry() throws Exception {
		UserInfo userInfo = (UserInfo) getUserDetails();
		userTelephone = userInfo.getUserTel();
		String orgId = userInfo.getOrgId();
		TUumsBaseOrg org = userService.getOrgInfoByOrgId(orgId);
		String rest1 = org.getRest3();
		if (rest1 != null) {
			String ss[] = rest1.split(",");
			IssuePeople = ss[0];
			editor = ss[1];
		}
		journals = journalService.getJouralsByNoPublished();
		return "entry";
	}

	// 补录
	public String saveEntry() throws Exception {
		String mergeOrgId = getRequest().getParameter("mergeOrgId");
		UserInfo userInfo = (UserInfo) getUserDetails();
		String mid[] = mergeOrgId.split(",");
		String uuid = Uuid.get();
		Map<String, String> orgNames = orgService.getOrgNames();
		for (int i = 0; i < mid.length; i++) {
			TInfoBasePublish publish = new TInfoBasePublish();
			publish.setPubSubmitStatus(Publish.SUBMITTED);
			publish.setPubEditContent(model.getPubEditContent());
			if (model.getPubUseStatus() != null) {
				publish.setPubUseStatus(model.getPubUseStatus());
			} else {
				publish.setPubUseStatus(Publish.USED);
			}
			// }
			publish.setPubRawTitle(model.getPubTitle());
			publish.setPubRawContent(model.getPubEditContent());
			publish.setPubPublisherId(userInfo.getUserId());
			publish.setPubDate(new Date());
			publish.setOrgId(mid[i]);
			publish.setOrgName(orgNames.get(mid[i]));
			publish.setPubTitle(model.getPubTitle());
			publish.setPubInfoType(model.getPubInfoType());
			publish.setPubSigner(model.getPubSigner());
			publish.setPubEditor(model.getPubEditor());
			// publish.setPubEditContent(model.getPubEditContent());
			publish.setPubUseScore(model.getPubUseScore());
			publish.setPubRemarkScore(model.getPubRemarkScore());
			publish.setPubIsInstruction(model.getPubIsInstruction());
			publish.setPubInstructionContent(model.getPubInstructionContent());
			publish.setPubInstructor(model.getPubInstructor());
			publish.setPubComment(model.getPubComment());
			if ((model.getPubComment() != null)
					&& (!"".equals(model.getPubComment()))) {
				publish.setPubIsComment(Publish.COMMENTED);
			} else {
				publish.setPubIsComment(Publish.NO_COMMENTED);
			}
			publish.setPubIsShare(Publish.NO_SHARED);
			publish.setPubIsMailInfo(Publish.NO_MAIL_INFO);

			publish.setPubIsText(Publish.TEXT_CONTENT);
			publish.setPubIsInstruction(Publish.NO_INSTRUCTION);

			publish.setPubAdoptOrgId(userInfo.getOrgId());
			publish.setPubAdoptUserId(userInfo.getUserId());
			if (model.getTInfoBaseIssue() != null) {
				publish.setTInfoBaseIssue(model.getTInfoBaseIssue());
			}
			if (model.getTInfoBaseColumn() != null) {
				publish.setTInfoBaseColumn(model.getTInfoBaseColumn());
			}
			if (model.getTInfoBaseIssue() != null) {
				String issId = model.getTInfoBaseIssue().getIssId();
				TInfoBaseIssue issue = issueService.getIssue(issId);
				if (issue.getTInfoBaseJournal().getJourCode() != null) {
					int issCode = issue.getTInfoBaseJournal().getJourCode();
					publish.setPubCode(issCode);
				}

			}
			if (model.getTInfoBaseColumn() != null) {
				String columnId = model.getTInfoBaseColumn().getColId();
				TInfoBaseColumn column = coulmnService.getColumn(columnId);
				// 插入栏目文章排序
				if (model.getPubSort() == null) {
					String colId = column.getColId();
					String issId = model.getTInfoBaseIssue().getIssId();
					List list = publishService
							.getPublishOrdersort(colId, issId);
					if (list.get(0) != null) {
						publish.setPubSort(Integer.valueOf(list.get(0)
								.toString()) + 10);
					} else {
						publish.setPubSort(1);
					}
				}
				if (column.getColCode() != null) {
					int colCode = column.getColCode();
					publish.setPubAdoptCode(colCode);
				}
			}
			publish.setPubSubmitDate(new Date());
			publishService.savePublish(publish, scIds);
			String userId = userInfo.getUserId();
			// 标记为已读
			TInfoBasePublishUser pubUser = publishService.getPU(
					publish.getPubId(), userId);
			if (pubUser == null) {
				publishService.savePU(publish.getPubId(), userId);
			}
			if (mid.length > 1) {
				if (publish.getOrgId().equals(mid[0])) {
					if (publish.getPubIsMerge() == null)
						publish.setPubIsMerge("1");
					publish.setPubMergeOrg(mergeOrgId);
					if (publish.getPubMergeFlag() == null)
						publish.setPubMergeFlag(uuid);
					String orgName = getRequest().getParameter("orgName");
					publish.setPubMergeName(orgName);
					publishService.updatePublish(publish);
				} else {
					publish.setPubMergeFlag(uuid);
					publish.setPubIsMerge("0");
					String orgName = getRequest().getParameter("orgName");
					publish.setPubMergeName(orgName);
					publishService.updatePublish(publish);
				}
			}
		}
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(Info.SUCCESS_AND_CLOSE);
		return null;
	}

	public String listadd() throws Exception {
		journals = journalService.getJouralsByNoPublished();
		return "listadd";
	}

	public String savelist() throws Exception {
		String ids[] = toId.split(",");
		for (int i = 0; i < ids.length; i++) {
			UserInfo userInfo = (UserInfo) getUserDetails();
			TInfoBasePublish publish = publishService.getPublish(ids[i]);

			publish.setPubAdoptOrgId(userInfo.getOrgId());
			publish.setPubAdoptUserId(userInfo.getUserId());
			if (model.getTInfoBaseIssue() != null) {
				publish.setTInfoBaseIssue(model.getTInfoBaseIssue());
			}
			if (model.getTInfoBaseColumn() != null) {
				publish.setTInfoBaseColumn(model.getTInfoBaseColumn());
			}
			if (model.getTInfoBaseIssue() != null) {
				String issId = model.getTInfoBaseIssue().getIssId();
				TInfoBaseIssue issue = issueService.getIssue(issId);
				if (issue.getTInfoBaseJournal().getJourCode() != null) {
					int issCode = issue.getTInfoBaseJournal().getJourCode();
					publish.setPubCode(issCode);
				}

			}
			if (model.getTInfoBaseColumn() != null) {
				String columnId = model.getTInfoBaseColumn().getColId();
				TInfoBaseColumn column = coulmnService.getColumn(columnId);
				// 插入栏目文章排序
				if (model.getPubSort() == null) {
					String colId = column.getColId();
					String issId = model.getTInfoBaseIssue().getIssId();
					List list = publishService
							.getPublishOrdersort(colId, issId);
					if (list.get(0) != null) {
						publish.setPubSort(Integer.valueOf(list.get(0)
								.toString()) + 10);
					} else {
						publish.setPubSort(1);
					}
				}
				if (column.getColCode() != null) {
					int colCode = column.getColCode();
					publish.setPubAdoptCode(colCode);
				}
			}
			publish.setPubUseStatus(Publish.USED);
			publish.setPubSubmitDate(new Date());
			publishService.updatePublish(publish);
		}
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(Info.SUCCESS_AND_CLOSE);
		return null;
	}

	public String isSubmit() throws Exception {
		String ids[] = toId.split(",");
		String flag = "success";
		for (int i = 0; i < ids.length; i++) {
			model = publishService.getPublish(ids[i]);
			if (model.getPubUseStatus().equals(Publish.USED)) {
				flag = "nosuccess";
				break;
			}
		}
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(flag);
		return null;
	}

	public String preView() throws Exception {
		TInfoBasePublish pub = publishService.getPublish(toId);
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String date1 = sim.format(pub.getPubDate());
		String preId = publishService.getPrePublish(date1);
		if (preId != null) {
			model = publishService.getPublish(preId);
		} else {
			model = publishService.getPublish(toId);
		}
		// if(preId!=null){
		pubview();
		// }
		getRequest().setAttribute("orgName", orgName);
		return "view";
	}

	public String nextView() throws Exception {
		TInfoBasePublish pub = publishService.getPublish(toId);
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String date1 = sim.format(pub.getPubDate());
		String nextId = publishService.getNextPublish(date1);
		if (nextId != null) {
			model = publishService.getPublish(nextId);
		} else {
			model = publishService.getPublish(toId);
		}
		// if(nextId!=null){
		pubview();
		// }
		getRequest().setAttribute("orgName", orgName);
		return "view";
	}

	public String isNext() throws Exception {
		TInfoBasePublish pub = publishService.getPublish(toId);
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String date1 = sim.format(pub.getPubDate());
		String preId = publishService.getNextPublish(date1);
		if (preId == null) {
			getResponse().setContentType("text/html");
			getResponse().getWriter().write("success");
		}
		return null;
	}

	public String isPre() throws Exception {
		String id = getRequest().getParameter("id");
		TInfoBasePublish pub = publishService.getPublish(id);
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String date1 = sim.format(pub.getPubDate());
		String preId = publishService.getPrePublish(date1);
		if (preId == null) {
			getResponse().setContentType("text/html");
			getResponse().getWriter().write("success");
		}
		return null;
	}

	/**
	 * 查看批示信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String viewInstruction() throws Exception {
		model = publishService.getPublish(toId);
		return "viewInstruction";
	}

	public String putall() throws Exception {
		Map<String, String> orgNames = orgService.getOrgNames();
		/*
		 * List pub = publishService.getPublishId();
		 * 
		 * for(int i=0;i<pub.size();i++){ TInfoBasePublish p =
		 * publishService.getPublish(pub.get(i).toString()); String orgId =
		 * p.getOrgId(); String orgName = orgNames.get(orgId);
		 * p.setOrgName(orgName); publishService.updatePublish(p); }
		 */

		List pub1 = publishService.getMerPublishId();
		for (int i = 0; i < pub1.size(); i++) {
			TInfoBasePublish p = publishService.getPublish(pub1.get(i)
					.toString());
			String merorgId = p.getPubMergeOrg();
			String mid[] = merorgId.split(",");
			String mm = "";
			for (int j = 0; j < mid.length; j++) {
				String orgName = orgNames.get(mid[j]);
				mm = mm + orgName + ",";
			}
			mm = mm.substring(0, mm.length() - 1);
			p.setPubMergeName(mm);
			publishService.updatePublish(p);
		}
		getResponse().setContentType("text/html");
		getResponse().getWriter().write("success");
		return null;
	}

	/*
	 * 以下是getter/setter
	 */

	public int getCurpage() {
		return curpage;
	}

	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}

	public int getUnitpage() {
		return unitpage;
	}

	public void setUnitpage(int unitpage) {
		this.unitpage = unitpage;
	}

	public Page<TInfoBasePublish> getPage() {
		return page;
	}

	public void setPage(Page<TInfoBasePublish> page) {
		this.page = page;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public String getQs() {
		return qs;
	}

	public void setQs(String qs) {
		this.qs = qs;
	}

	public String getSidx() {
		return sidx;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	public String getSord() {
		return sord;
	}

	public void setSord(String sord) {
		this.sord = sord;
	}

	public String getAssigntoname() {
		return assigntoname;
	}

	public void setAssigntoname(String assigntoname) {
		this.assigntoname = assigntoname;
	}

	public Map<String, String> getOrgMap() {
		return orgMap;
	}

	public void setOrgMap(Map<String, String> orgMap) {
		this.orgMap = orgMap;
	}

	public List<TInfoBaseJournal> getJournals() {
		return journals;
	}

	public void setJournals(List<TInfoBaseJournal> journals) {
		this.journals = journals;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getJourId() {
		return jourId;
	}

	public void setJourId(String jourId) {
		this.jourId = jourId;
	}

	public String getPublishNum() {
		return publishNum;
	}

	public void setPublishNum(String publishNum) {
		this.publishNum = publishNum;
	}

	public String getUseNum() {
		return useNum;
	}

	public void setUseNum(String useNum) {
		this.useNum = useNum;
	}

	public String getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(String useStatus) {
		this.useStatus = useStatus;
	}

	public List<ScoreItemDto> getScoreItems() {
		return scoreItems;
	}

	public void setScoreItems(List<ScoreItemDto> scoreItems) {
		this.scoreItems = scoreItems;
	}

	public String[] getScIds() {
		return scIds;
	}

	public void setScIds(String[] scIds) {
		this.scIds = scIds;
	}

	public String[] getCheckedScIds() {
		return checkedScIds;
	}

	public void setCheckedScIds(String[] checkedScIds) {
		this.checkedScIds = checkedScIds;
	}

	public Boolean getPreUse() {
		return preUse;
	}

	public void setPreUse(Boolean preUse) {
		this.preUse = preUse;
	}

	public List<TInfoBaseColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<TInfoBaseColumn> columns) {
		this.columns = columns;
	}

	public List<ReportDto> getReports() {
		return reports;
	}

	public void setReports(List<ReportDto> reports) {
		this.reports = reports;
	}

	public Boolean getIsJournalUnselected() {
		return isJournalUnselected;
	}

	public void setIsJournalUnselected(Boolean isJournalUnselected) {
		this.isJournalUnselected = isJournalUnselected;
	}

	public Boolean getCanCancelAdopt() {
		return canCancelAdopt;
	}

	public void setCanCancelAdopt(Boolean canCancelAdopt) {
		this.canCancelAdopt = canCancelAdopt;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public TUumsBaseOrg getBaseOrg() {
		return baseOrg;
	}

	public void setBaseOrg(TUumsBaseOrg baseOrg) {
		this.baseOrg = baseOrg;
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

	public Boolean getIsTrue() {
		return isTrue;
	}

	public void setIsTrue(Boolean isTrue) {
		this.isTrue = isTrue;
	}

	public List<TUumsBaseOrg> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<TUumsBaseOrg> orgList) {
		this.orgList = orgList;
	}

	public String[] getDp() {
		return dp;
	}

	public void setDp(String[] dp) {
		this.dp = dp;
	}

	public List<TInfoBasePublish> getPubList() {
		return pubList;
	}

	public void setPubList(List<TInfoBasePublish> pubList) {
		this.pubList = pubList;
	}

	public List<PublishDto> getList1() {
		return list1;
	}

	public void setList1(List<PublishDto> list1) {
		this.list1 = list1;
	}

	public String getUserTelephone() {
		return userTelephone;
	}

	public void setUserTelephone(String userTelephone) {
		this.userTelephone = userTelephone;
	}

}
