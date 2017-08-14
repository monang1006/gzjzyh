package com.strongit.xxbs.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.platform.webcomponent.tree.JsonUtil;
import com.strongit.platform.webcomponent.tree.TreeNode;
import com.strongit.utils.Uuid;
import com.strongit.uums.bo.TUumsBaseRole;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Role;
import com.strongit.uums.security.UserInfo;
import com.strongit.xxbs.common.PathUtil;
import com.strongit.xxbs.common.contant.Info;
import com.strongit.xxbs.common.contant.Publish;
import com.strongit.xxbs.dto.IssueDto;
import com.strongit.xxbs.dto.JournalDto;
import com.strongit.xxbs.dto.ReleaseDto;
import com.strongit.xxbs.dto.ScoreDto;
import com.strongit.xxbs.entity.TInfoBaseColumn;
import com.strongit.xxbs.entity.TInfoBaseIssue;
import com.strongit.xxbs.entity.TInfoBaseJournal;
import com.strongit.xxbs.entity.TInfoBasePublish;
import com.strongit.xxbs.service.IIssueService;
import com.strongit.xxbs.service.IJournalService;
import com.strongit.xxbs.service.IOrgService;
import com.strongit.xxbs.service.IPublishService;
import com.strongit.xxbs.service.JdbcIssueService;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.service.ServiceLocator;
import com.strongmvc.webapp.action.BaseActionSupport;

public class ReleaseAction extends BaseActionSupport<TInfoBasePublish> {
	private static final long serialVersionUID = 1L;

	private IIssueService issueService;
	private IPublishService publishService;
	private IOrgService orgService;
	private IJournalService journalService;
	@Autowired private IUserService userService;
	private JdbcIssueService jdbcIssueService;

	private int curpage;
	private int unitpage;
	private String sidx;
	private String sord;
	private Page<TInfoBasePublish> page;
	private Page<IssueDto> pagePl;

	private String toId;
	private List<ReleaseDto> releases;
	private TInfoBaseIssue issue;

	private File upload;
	private String isPublish;
	private String selView;
	private Date relDate;
	private String orgName1;
	private String wtId;
	private String open;

	@Autowired
	public void setJdbcIssueService(JdbcIssueService jdbcIssueService) {
		this.jdbcIssueService = jdbcIssueService;
	}

	@Autowired
	public void setIssueService(IIssueService issueService) {
		this.issueService = issueService;
	}

	@Autowired
	public void setPublishService(IPublishService publishService) {
		this.publishService = publishService;
	}

	@Autowired
	public void setOrgService(IOrgService orgService) {
		this.orgService = orgService;
	}

	@Autowired
	public void setJournalService(IJournalService journalService) {
		this.journalService = journalService;
	}

	public TInfoBasePublish getModel() {
		return null;
	}

	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
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
		TInfoBaseIssue iss = issueService.getIssue(toId);
		SendDocManager manager = (SendDocManager) ServiceLocator
				.getService("sendDocManager");
		//撤消发布
		if ("0".equals(isPublish)) {
			iss.setIssIsPublish(Publish.NO_PUBLISHED);
			//删除工作流中的每日要情记录及文档
			manager.deleteYaoQingInfo(toId);
			String path = com.strongit.oa.util.PathUtil.getRootPath()
					+ "common" + File.separatorChar + "ewebeditor"
					+ File.separatorChar + "uploadfile";
			String newPath = path + File.separatorChar + toId;
			File file = new File(newPath);
			if (file.exists()) {
				file.delete();
			}
		}
		if ("1".equals(isPublish)) {
			iss.setIssIsPublish(Publish.PUBLISHED);
		}
		if ("2".equals(isPublish)) {
			iss.setIssIsPublish(Publish.PRE_PUBLISHED);
		}
		FileInputStream fIn = null;
		FileOutputStream fOut = null;
		FileOutputStream fOut1 = null;
		try {
			fIn = new FileInputStream(upload);
			byte[] bys = IOUtils.toByteArray(fIn);
			fOut = new FileOutputStream(PathUtil.wordPath() + toId + ".doc");
			IOUtils.write(bys, fOut);
			// iss.setIssContent(bys);
			issueService.saveIssue(iss);
			if (!"0".equals(isPublish)&&!"2".equals(isPublish)) {
				// 获取每日要请信息

				manager.saveYaoQingInfo(toId);
				String path = com.strongit.oa.util.PathUtil.getRootPath()
						+ "common" + File.separatorChar + "ewebeditor"
						+ File.separatorChar + "uploadfile";
				String newPath = path + File.separatorChar + toId;
				// FileInputStream fIn1 = new
				// FileInputStream(PathUtil.wordPath() +
				// toId
				// + ".doc");
				fOut1 = new FileOutputStream(newPath);
				fOut1.write(bys);
				// IOUtils.write(bys, fOut1);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			fIn.close();
			fOut.close();
			fOut1.close();
		}

		return null;
	}

	public String officeStream() throws Exception {
		// TInfoBaseIssue iss = issueService.getIssue(toId);
		// byte[] bOffice = iss.getIssContent();
		FileInputStream fIn = null;
		ServletOutputStream outStream = null;
		try {
			fIn = new FileInputStream(PathUtil.wordPath() + toId + ".doc");
			byte[] bOffice = IOUtils.toByteArray(fIn);
			outStream = getResponse().getOutputStream();
			outStream.write(bOffice);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fIn.close();
			outStream.close();
		}
		return null;
	}

	public String tree() throws Exception {
		return "tree";
	}

	public String content() throws Exception {

		if (toId == null || "".equals(toId)) {
			TInfoBaseIssue issue = issueService.lastIssue();
			if (issue == null) {
				return null;
			}
			toId = issue.getIssId();
		}
		issue = issueService.getIssue(toId);
		relDate = issue.getIssTime();
		String jourId = issue.getTInfoBaseJournal().getJourId();
		TInfoBaseJournal journal = journalService.getJoural(jourId);
		if (journal.getTInfoBaseWordTemplate() != null) {
			wtId = journal.getTInfoBaseWordTemplate().getWtId();
		}
		Set<TInfoBaseColumn> allColumns = journal.getTInfoBaseColumns();

		isPublish = issue.getIssIsPublish();

		UserInfo userInfo = (UserInfo) getUserDetails();
		String orgId = userInfo.getOrgId();
		Map<String, String> orgNames = orgService.getOrgNames();

		List<TInfoBasePublish> publishs = null;
		/*
		 * if("1".equals(selView)) { publishs =
		 * publishService.findPublishsByIssId(toId, orgId); } else {
		 */
		publishs = publishService.findPublishsByIssId(toId);
		// }

		releases = new ArrayList<ReleaseDto>();
		Set<String> tempColId = new HashSet<String>();
		// for(TInfoBasePublish one : publishs)
		// {
		// ReleaseDto rel = new ReleaseDto();
		// String colId = one.getTInfoBaseColumn().getColId();
		// if(tempColId.contains(colId))
		// rel.setIsFirstColumn(false);
		// else
		// rel.setIsFirstColumn(true);
		// tempColId.add(colId);
		// rel.setColName(one.getTInfoBaseColumn().getColName());
		//
		// rel.setOrgName(orgNames.get(one.getOrgId()));
		// rel.setPubTitle(one.getPubTitle());
		// rel.setPubEditContent(one.getPubEditContent());
		// releases.add(rel);
		// }

		for (TInfoBaseColumn col : allColumns) {
			String cId = col.getColId();
			boolean isExist = false;
			tempColId.clear();
			for (TInfoBasePublish row : publishs) {
				if (cId.equals(row.getTInfoBaseColumn().getColId())) {
					isExist = true;
					ReleaseDto rel = new ReleaseDto();
					rel.setPubId(row.getPubId());
					String tColId = row.getTInfoBaseColumn().getColId();
					if (tempColId.contains(tColId))
						rel.setIsFirstColumn(false);
					else
						rel.setIsFirstColumn(true);
					tempColId.add(tColId);
					String colName = col.getColName();
					if (colName.length() <= 4) {
						String ss = "";
						for (int i = 0; i < colName.length(); i++) {
							String aa = colName.substring(i, i + 1);
							ss = ss + aa + "　";
						}
						rel.setColName(ss);
					} else {
						rel.setColName(colName);
					}
					if ((row.getPubMergeOrg() != null)
							&& (!"".equals(row.getPubMergeOrg()))) {
						String merOrg = row.getPubMergeOrg();
						String ms[] = merOrg.split(",");
						String reName = "";
						for (int i = 0; i < ms.length; i++) {
							reName = reName + orgNames.get(ms[i]) + " ";
						}
						reName = reName.substring(0, reName.length() - 1);
						rel.setOrgName("（" + reName + "）");
					} else {
						rel.setOrgName("(" + orgNames.get(row.getOrgId()) + ")");
					}
					rel.setPubTitle(row.getPubTitle());
					rel.setPubEditContent(row.getPubEditContent());
					releases.add(rel);
				}
			}
			// if(isExist == false)
			// {
			// ReleaseDto rel = new ReleaseDto();
			// rel.setIsFirstColumn(true);
			// rel.setColName(col.getColName());
			// releases.add(rel);
			// }
		}
		String flag = getRequest().getParameter("flag");
		if ("0".equals(flag)) {
			return "content";
		}

		return "content2";
	}

	public String content1() throws Exception {
		relDate = new Date();
		if (toId == null || "".equals(toId)) {
			TInfoBaseIssue issue = issueService.lastIssue();
			if (issue == null) {
				return null;
			}
			toId = issue.getIssId();
		}
		issue = issueService.getIssue(toId);

		String jourId = issue.getTInfoBaseJournal().getJourId();
		TInfoBaseJournal journal = journalService.getJoural(jourId);
		Set<TInfoBaseColumn> allColumns = journal.getTInfoBaseColumns();

		isPublish = issue.getIssIsPublish();

		UserInfo userInfo = (UserInfo) getUserDetails();
		String orgId = userInfo.getOrgId();
		String userId = userInfo.getUserId();
		Map<String, String> orgNames = orgService.getOrgNames();

		List<TInfoBasePublish> publishs = null;

		List<Role> roles = userService.getUserRoleInfosByUserId(
				userId, "1");
		String isSubmit = "1";
		if (roles.size() == 0) {
			isSubmit = "0";
			publishs = publishService.findPublishsByIssId(toId);
		}
		if (roles.size() > 0) {
			for (TUumsBaseRole one : roles) {
				if (("1".equals(selView))
						&& (Publish.ROLE_SUBMITTER.equals(one.getRoleSyscode()) || Publish.ROLE_YHGLY_ID
								.equals(one.getRoleSyscode()))) {
					publishs = publishService.findPublishsByIssId(toId, orgId);
				} else {
					isSubmit = "0";
					publishs = publishService.findPublishsByIssId(toId);
				}
			}
		}

		/*
		 * if("1".equals(selView)) { publishs =
		 * publishService.findPublishsByIssId(toId, orgId); } else { publishs =
		 * publishService.findPublishsByIssId(toId); }
		 */

		releases = new ArrayList<ReleaseDto>();
		Set<String> tempColId = new HashSet<String>();
		if (isSubmit == "1") {
			for (TInfoBasePublish one : publishs) {
				ReleaseDto rel = new ReleaseDto();
				String colId = one.getTInfoBaseColumn().getColId();
				if (tempColId.contains(colId))
					rel.setIsFirstColumn(false);
				else
					rel.setIsFirstColumn(true);
				tempColId.add(colId);
				rel.setColName(one.getTInfoBaseColumn().getColName());

				rel.setOrgName(orgNames.get(one.getOrgId()));
				rel.setPubTitle(one.getPubTitle());
				rel.setPubEditContent(one.getPubEditContent());
				orgName1 = orgNames.get(one.getOrgId());
				releases.add(rel);
			}
		} else {
			for (TInfoBaseColumn col : allColumns) {
				String cId = col.getColId();
				boolean isExist = false;
				tempColId.clear();
				for (TInfoBasePublish row : publishs) {
					if (cId.equals(row.getTInfoBaseColumn().getColId())) {
						isExist = true;
						ReleaseDto rel = new ReleaseDto();
						String tColId = row.getTInfoBaseColumn().getColId();
						if (tempColId.contains(tColId))
							rel.setIsFirstColumn(false);
						else
							rel.setIsFirstColumn(true);
						tempColId.add(tColId);
						rel.setColName(col.getColName());

						rel.setOrgName("(" + orgNames.get(row.getOrgId()) + ")");
						rel.setPubTitle(row.getPubTitle());
						rel.setPubEditContent(row.getPubEditContent());
						releases.add(rel);
					}
				}
				if (isExist == false) {
					ReleaseDto rel = new ReleaseDto();
					rel.setIsFirstColumn(true);
					rel.setColName(col.getColName());
					releases.add(rel);
				}
			}
		}
		return "content1";
	}

	public String treeList() throws Exception {
		List<TreeNode> nodeList = new ArrayList<TreeNode>();
		List<TInfoBaseIssue> allList = issueService.getIssues();
		String uuid = Uuid.get();
		Set<JournalDto> jds = new HashSet<JournalDto>();
		SimpleDateFormat sim = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sim1 = new SimpleDateFormat("yyyy年");
		for (TInfoBaseIssue one : allList) {
			JournalDto jd = new JournalDto();
			jd.setJourId(one.getTInfoBaseJournal().getJourId());
			jd.setJourName(one.getTInfoBaseJournal().getJourName());
			jds.add(jd);

			TreeNode node = new TreeNode();
			node.setPid(uuid + one.getTInfoBaseJournal().getJourId()
					+ sim1.format(one.getIssTime()));
			node.setId(one.getIssId());
			String zt = "";
			if (Publish.PUBLISHED.equals(one.getIssIsPublish())) {
				zt = Publish.CN_PUBLISHED;
			}
			if (Publish.PRE_PUBLISHED.equals(one.getIssIsPublish())) {
				zt = Publish.CN_PRE_PUBLISHED;
			}
			if (Publish.NO_PUBLISHED.equals(one.getIssIsPublish())) {
				zt = Publish.CN_NO_PUBLISHED;
			}
			Date dd = one.getIssTime();

			String ss = sim.format(dd);
			node.setText(ss + Publish.CN_DI + one.getIssNumber()
					+ Publish.CN_QI + " " + zt);
			node.setValue(one.getIssId());
			nodeList.add(node);
		}
		for (JournalDto one : jds) {
			String mm = "";
			List<TInfoBaseIssue> issList = issueService.getIssuesByjour(one
					.getJourId());
			Set set = new TreeSet();
			for (int i = 0; i < issList.size(); i++) {
				set.add(sim1.format(issList.get(i).getIssTime()));
			}
			Iterator it = set.iterator();
			while (it.hasNext()) {
				mm = mm + it.next() + ",";
			}
			mm = mm.substring(0, mm.length() - 1);
			String mid[] = mm.split(",");
			for (int j = 0; j < mid.length; j++) {
				TreeNode node1 = new TreeNode();
				node1.setPid(one.getJourId());

				node1.setId(uuid + one.getJourId() + mid[j]);
				node1.setText(mid[j]);
				node1.setIsexpand(true);
				nodeList.add(node1);
			}
			TreeNode node = new TreeNode();
			node.setPid("-1");
			node.setId(one.getJourId());
			node.setText(one.getJourName());
			node.setValue(one.getJourId());
			node.setIsexpand(true);
			nodeList.add(node);
		}

		String jsonTreeNode = JsonUtil.fromTreeNodes(nodeList);

		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonTreeNode);
		return null;
	}

	public String showList() throws Exception {
		page = new Page<TInfoBasePublish>(unitpage, true);
		page.setPageNo(curpage);
		page.setPageSize(unitpage);
		page.setOrder(sord);
		page.setOrderBy(sidx);

		UserInfo userInfo = (UserInfo) getUserDetails();
		String orgId = userInfo.getOrgId();

		// page = publishService.findPublishs(page, orgId, submitStatus);

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		jsonObj.put("totalpages", page.getTotalPages());
		jsonObj.put("totalrecords", page.getTotalCount());

		List<TInfoBasePublish> result = page.getResult();
		JSONArray rows = new JSONArray();
		if (result != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss");
			for (TInfoBasePublish one : result) {
				JSONObject obj = new JSONObject();
				obj.put("pubId", one.getPubId());
				obj.put("pubTitle", one.getPubTitle());
				obj.put("pubInfoType", one.getPubInfoType());
				String isAppoint = (one.getTInfoBaseAppoint() == null) ? Publish.NO_APPOINT
						: Publish.APPOINTED;
				obj.put("isAppoint", isAppoint);
				obj.put("pubUseStatus", one.getPubUseStatus());
				obj.put("submitStatus", one.getPubSubmitStatus());
				obj.put("pubSubmitStatus", one.getPubSubmitStatus());
				obj.put("pubIsshare", one.getPubIsShare());
				obj.put("pubIsComment", one.getPubIsComment());
				obj.put("pubDate", sdf.format(one.getPubDate()));
				rows.add(obj);
			}
		}
		jsonObj.put("rows", rows);
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		return null;
	}

	public String showJdbcList() throws Exception {
		pagePl = new Page<IssueDto>(unitpage, true);
		pagePl.setPageNo(curpage);
		pagePl.setPageSize(unitpage);

		pagePl.setOrder(sord);
		pagePl.setOrderBy(sidx);

		UserInfo userInfo = (UserInfo) getUserDetails();
		String orgId = userInfo.getOrgId();
		pagePl = jdbcIssueService.getIssuesBysubmitter(pagePl, orgId);

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		if (pagePl.getTotalPages() == -1) {
			pagePl.setTotalCount(0);
		}
		jsonObj.put("totalpages", pagePl.getTotalPages());
		jsonObj.put("totalrecords", pagePl.getTotalCount());

		List<IssueDto> result = pagePl.getResult();
		JSONArray rows = new JSONArray();
		if (result != null) {
			for (IssueDto one : result) {
				JSONObject obj = new JSONObject();
				obj.put("issId", one.getIssId());
				obj.put("jourName", one.getJourName());
				obj.put("issNumber", one.getIssNumber());
				rows.add(obj);
			}
		}
		jsonObj.put("rows", rows);
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		return null;
	}

	public String cancelPrePublish() throws Exception {
		TInfoBaseIssue issue = issueService.getIssue(toId);
		issue.setIssIsPublish(Publish.NO_PUBLISHED);
		issue.setIssContent(new byte[0]);
		issueService.saveIssue(issue);
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(Info.SUCCESS);
		return null;
	}

	public String issueView() throws Exception {
		return "issueView";
	}

	/*
	 * setter/getter
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

	public Page<TInfoBasePublish> getPage() {
		return page;
	}

	public void setPage(Page<TInfoBasePublish> page) {
		this.page = page;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public List<ReleaseDto> getReleases() {
		return releases;
	}

	public void setReleases(List<ReleaseDto> releases) {
		this.releases = releases;
	}

	public TInfoBaseIssue getIssue() {
		return issue;
	}

	public void setIssue(TInfoBaseIssue issue) {
		this.issue = issue;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(String isPublish) {
		this.isPublish = isPublish;
	}

	public String getSelView() {
		return selView;
	}

	public void setSelView(String selView) {
		this.selView = selView;
	}

	public Date getRelDate() {
		return relDate;
	}

	public void setRelDate(Date relDate) {
		this.relDate = relDate;
	}

	public String getOrgName1() {
		return orgName1;
	}

	public void setOrgName1(String orgName1) {
		this.orgName1 = orgName1;
	}

	public String getWtId() {
		return wtId;
	}

	public void setWtId(String wtId) {
		this.wtId = wtId;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

}
