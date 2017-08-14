package com.strongit.oa.personnel.personorg;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaBaseOrg;
import com.strongit.oa.bo.ToaBasePerson;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.dict.IDictService;
import com.strongit.oa.personnel.baseperson.PersonManager;
import com.strongit.oa.personnel.structure.PersonStructureManager;
import com.strongit.oa.util.ReflectionUtils;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongmvc.webapp.action.BaseActionSupport;

public class PersonOrgAction extends BaseActionSupport {

	private PersonOrgManager manager;

	private ToaBaseOrg model = new ToaBaseOrg();

	private String orgId;

	private List<ToaBaseOrg> orgList;

	private List<ToaBasePerson> userList;

	private String personIds;

	/** 获取下一个编码manager */
	private BaseOrgManager baseorgmanager;

	private Map<String, ToaBaseOrg> orgmap = new HashMap<String, ToaBaseOrg>();

	/** 人员manager */
	private PersonManager personManager;

	/** 机构编制manager */
	private PersonStructureManager structManager;

	/** 编码规则 */
	private String codeType;

	/** 机构编码 */
	private String orgSysCode;

	/** 跳转控制 */
	private String audittype;

	/** 机构ID */
	private String moveOrgId;

	/** 字典接口 */
	private IDictService dictService;

	/** 机构性质list */
	private List<ToaSysmanageDictitem> dictlist;

	/** 已删除 */
	private static final String DEL = "1";

	/** 未删除 */
	private static final String NOTDEL = "0";

	/** 导出统一用户组织机构 */
	private IUserService userManagers;

	private List<Organization> uumsList;

	private Map<String, String> natureMap = new HashMap<String, String>();// 机构性质map

	/**
	 * 获取统一用户机构
	 * 
	 * @author 蒋国斌
	 * @date 2009-10-15 上午10:24:59
	 * @return
	 * @throws IOException
	 */
	public String baseList() throws IOException {
		uumsList = userManagers.getAllDeparments();
		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		return "baseList";
	}

	public String pubUsernames() throws Exception {
		String[] id = personIds.split(",");
		StringBuffer names = new StringBuffer();
		for (int i = 0; i < id.length; i++) {
			ToaBasePerson per = personManager.getPersonByID(id[i]);
			names.append(per.getPersonName());
			names.append(",");
		}
		// return renderHtml("<script>window.close();</script>");
		renderText(names.toString());

		return null;
	}

	/**
	 * 导入统一用户机构与人员到人事中
	 * 
	 * @author 蒋国斌
	 * @date 2009-10-15 上午10:25:20
	 * @return
	 */
	public String saveUumsOrgs() throws Exception {
		

		uumsList = userManagers.getAllDeparments();// 得到统一用户那边所有的机构及部门
		List ls = manager.getOrgsByUums();// 得到人事这边已经从统一用户那导入过来的机构及部门
		Map<String, ToaBaseOrg> map = new HashMap<String, ToaBaseOrg>();// 相应的ORGID对应相应的机构
		String umcode="";
		for (Iterator ft = ls.iterator(); ft.hasNext();) {
			ToaBaseOrg tg = (ToaBaseOrg) ft.next();
			umcode=tg.getOrgSyscode().substring(0,3);
			map.put(tg.getRest(), tg);
		}// 将统一用户机构ID与人事机构衔接一一对应（人事这边的REST字段存着统一用户的机构ID）

		List<String> ids = ReflectionUtils.fetchElementPropertyToList(ls,
				"rest");// 将所有的REST提取出来放在一个LIST 便于比较
		
		if (ls != null && ls.size() != 0) {// 已经导入过一次机构
			for (Iterator it = uumsList.iterator(); it.hasNext();) {
				Organization fog = (Organization) it.next();
				
				if (ids.contains(fog.getOrgId())) {// 比较统一用户的机构ID是否已经存在人事这边，存在就将更新人事机构
					ToaBaseOrg tg = map.get(fog.getOrgId());
					tg.setOrgName(fog.getOrgName());
					tg.setOrgCode(fog.getOrgCode());	
					tg.setRest(fog.getOrgId());
					tg.setOrgAddr(fog.getOrgAddr());
					SimpleDateFormat dateformat1 = new SimpleDateFormat(
							"yyyy-MM-dd");
					String aa = dateformat1.format(new Date());
					tg.setOrgCreatedate(aa);
					tg.setOrgDescription(fog.getOrgDescription());
					tg.setOrgFax(fog.getOrgFax());
					tg.setOrgIsdel(fog.getOrgIsdel());
					tg.setOrgNature(fog.getOrgNature());
					tg.setOrgTel(fog.getOrgTel());
					tg.setOrgZip(fog.getOrgZip());
					manager.updateOrg(tg); // 存在的情况下更新人事机构
				} else {// 不存在将保存为新机构
					
					ToaBaseOrg bg = new ToaBaseOrg();
					bg.setOrgid(null);
					bg.setOrgSyscode(umcode+fog.getOrgSyscode());
					bg.setOrgName(fog.getOrgName());
					bg.setOrgCode(fog.getOrgCode());
					bg.setRest(fog.getOrgId());
					bg.setOrgAddr(fog.getOrgAddr());
					SimpleDateFormat dateformat1 = new SimpleDateFormat(
							"yyyy-MM-dd");
					String aa = dateformat1.format(new Date());
					bg.setOrgCreatedate(aa);
					bg.setOrgDescription(fog.getOrgDescription());
					bg.setOrgFax(fog.getOrgFax());
					bg.setOrgIsdel(fog.getOrgIsdel());
					bg.setOrgNature(fog.getOrgNature());
					bg.setOrgTel(fog.getOrgTel());
					bg.setOrgZip(fog.getOrgZip());
					bg.setLeadNumbe("20");
					bg.setNoLeadNumber("20");
					manager.save(bg);// 不存在的情况下新添一个机构
				}

			}
			this.addUumsPersonList();// 导入机构下的人员
		} else {// 第一次导入的情况
			String oaId = manager.getNextOrgCode(null);// 预留给统一用户机构的父级编码
			ToaBaseOrg borg = new ToaBaseOrg();// 添一个顶级节点作为区分
			borg.setOrgid(null);
			borg.setOrgSyscode(oaId);
			borg.setOrgName("统一用户机构");
			borg.setOrgCode(oaId);
			SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd");
			String aa = dateformat1.format(new Date());
			borg.setOrgCreatedate(aa);
			borg.setOrgNature("1");
			borg.setOrgIsdel("0");
			borg.setLeadNumbe("20");
			borg.setNoLeadNumber("20");
			borg.setRest(oaId);
			try {
				manager.save(borg);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			this.addFinaOrgList(oaId, uumsList); // 导入统一用户机构及人员
		}

		return renderHtml("<script> window.dialogArguments.parent.organiseTree.location='"
				+ getRequest().getContextPath()
				+ "/personnel/personorg/personOrg!tree.action';window.dialogArguments.parent.organiseInfo.location='"
				+ getRequest().getContextPath()
				+ "/personnel/personorg/personOrg!addchild.action?orgSysCode='; window.close();</script>");

	}

	/**
	 * 新添统一用户那边的机构到人事
	 * 
	 * @author 蒋国斌
	 * @date 2009-11-30 上午08:56:05
	 * @param oaId
	 *            顶级节点编码
	 * @param uumsList
	 *            统一用户那边的机构LIST
	 */
	public void addFinaOrgList(String oaId, List uumsList) {

		for (Iterator it = uumsList.iterator(); it.hasNext();) {
			Organization fog = (Organization) it.next();
			ToaBaseOrg org = new ToaBaseOrg();
			org.setOrgid(null);
			org.setOrgSyscode(oaId + fog.getOrgSyscode());
			org.setOrgName(fog.getOrgName());
			org.setOrgCode(fog.getOrgCode());
			org.setRest(fog.getOrgId());
			org.setOrgAddr(fog.getOrgAddr());
			SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd");
			String aa = dateformat1.format(new Date());
			org.setOrgCreatedate(aa);
			org.setOrgDescription(fog.getOrgDescription());
			org.setOrgFax(fog.getOrgFax());
			org.setOrgIsdel(fog.getOrgIsdel());
			org.setOrgNature(fog.getOrgNature());
			org.setOrgTel(fog.getOrgTel());
			org.setOrgZip(fog.getOrgZip());
			org.setLeadNumbe("20");
			org.setNoLeadNumber("20");
			manager.save(org);
		}
		this.addUumsPersonList();// 导入机构下的人员
	}

	/**
	 * 将统一用户那边人员添到人事中对应的机构下
	 * 
	 * @author 蒋国斌
	 * @date 2009-11-30 上午08:58:13
	 */
	public void addUumsPersonList() {
		List<ToaBaseOrg> uums = manager.getOrgsByUums();// 得到已经导入过来的人事机构
		Map<String, List<User>> userMap = userManagers.getUserMap();// 用来存统一用户机构ID对应下所有的人员

		if (uums != null && !uums.isEmpty()) {
			for (ToaBaseOrg baseOrg : uums) {
				List<ToaBasePerson> bplist = personManager
						.getUumsPersons(baseOrg.getRest());// 根据统一用户那边的机构ID得到其在人事已经导入过来的人员
				List<User> users = userMap.get(baseOrg.getRest());// 根据统一用户那边的机构ID得到统一用户下所有的人员

				List<String> ids = ReflectionUtils.fetchElementPropertyToList(
						bplist, "uums_person_id");// 将统一用户那边的导入过来的人员ID放在一个LIST
													// 便于比较
				Map<String, ToaBasePerson> map = new HashMap<String, ToaBasePerson>();// 存放统一用户人员ID对应人事人员
				for (Iterator ft = bplist.iterator(); ft.hasNext();) {
					ToaBasePerson bp = (ToaBasePerson) ft.next();
					map.put(bp.getUums_person_id(), bp);
				}

				if (bplist == null || bplist.isEmpty()) {// 第一次导入人员的情况将所有人员直接保存在相应的人事机构下
					if (users != null) {
						for (User user : users) {

							if (user.getOrgId().equals(baseOrg.getRest())) {// 根据机构ID存入相应人员
								ToaBasePerson sbp = new ToaBasePerson();
								sbp.setPersonid(null);
								sbp.setBaseOrg(baseOrg);
								sbp.setPersonIsdel(user.getUserIsdel());
								sbp.setPersonName(user.getUserName());
								sbp.setUums_person_id(user.getUserId());
								sbp.setPersonPersonKind("4028822723fece180123fed551ae0008");
								sbp.setPersonSax("4028822723fa40580123fa76c50c0003");
								personManager.save(sbp);
							}

						}
					}
				} else {// 已经导入过一次的情况 需要逐一比较ID
					if (users != null) {
						for (User user : users) {
							if (ids.contains(user.getUserId())) { // exist统一用户那边在人事已经存在的人员情况
																	// 直接更新
								ToaBasePerson bp = map.get(user.getUserId());
								bp.setPersonIsdel(user.getUserIsdel());
								bp.setPersonName(user.getUserName());
								bp.setUums_person_id(user.getUserId());
								personManager.updatePerson(bp);
							} else { // 不存在的情况 直接对号入座 添加到相应的机构下
								if (user.getOrgId().equals(baseOrg.getRest())) {
									ToaBasePerson p = new ToaBasePerson();
									p.setPersonid(null);
									p.setBaseOrg(baseOrg);
									p.setPersonIsdel(user.getUserIsdel());
									p.setPersonName(user.getUserName());
									p.setUums_person_id(user.getUserId());
									p.setPersonPersonKind("4028822723fece180123fed551ae0008");
									p.setPersonSax("4028822723fa40580123fa76c50c0003");
									personManager.save(p);
								}
							}
						}
					}

				}

			}
		}

	}

	public String adduser() throws IOException {
		orgList = manager.getOrgsByIsdel(NOTDEL);

		userList = personManager.getPersonsByIsdel(NOTDEL);

		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		return "adduser";
	}

	public Map<String, String> getNatureMap() {
		return natureMap;
	}

	public void setNatureMap(Map<String, String> natureMap) {
		this.natureMap = natureMap;
	}

	public List<ToaSysmanageDictitem> getDictlist() {

		return dictlist;
	}

	public void setDictlist(List<ToaSysmanageDictitem> dictlist) {
		this.dictlist = dictlist;
	}

	@Autowired
	public void setDictService(IDictService dictService) {
		this.dictService = dictService;
	}

	public String getAudittype() {
		return audittype;
	}

	public void setAudittype(String audittype) {
		this.audittype = audittype;
	}

	public String getOrgSysCode() {
		return orgSysCode;
	}

	public void setOrgSysCode(String orgSysCode) {
		this.orgSysCode = orgSysCode;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	@Autowired
	public void setBaseorgmanager(BaseOrgManager baseorgmanager) {
		this.baseorgmanager = baseorgmanager;
	}

	/**
	 * 根据ID删除机构
	 * 
	 * @author 胡丽丽
	 * @date 2009-10-16 12:00
	 */
	@Override
	public String delete() throws Exception {
		manager.deleteByID(orgId);
		return renderHtml("<script>" + "window.location='"
				+ getRequest().getContextPath()
				+ "/personnel/personorg/personOrg!addchild.action'; "
				+ " parent.organiseTree.location='"
				+ getRequest().getContextPath()
				+ "/personnel/personorg/personOrg!tree.action'; </script>");
	}

	/**
	 * 还原机构
	 * @return
	 * @throws Exception
	 */
	public String restore()throws Exception{
		manager.restoreById(orgId);
		return renderHtml("<script>" + "window.location='"
				+ getRequest().getContextPath()
				+ "/personnel/personorg/personOrg!addchild.action'; "
				+ " parent.organiseTree.location='"
				+ getRequest().getContextPath()
				+ "/personnel/personorg/personOrg!tree.action'; </script>");
	}
	/**
	 * 编辑初始化页面
	 * 
	 * @author 胡丽丽
	 * @date 2009-10-16 12:00
	 */
	@Override
	public String input() throws Exception {
		prepareModel();
		dictlist = dictService.getItemsByDictValue("JIGOU");// 获取密级
		if ("get".equals(audittype)) {
			this.nativelist();
			model.setOrgNatureName(natureMap.get(model.getOrgNature()));
			return "show";
		} else if ("merge".equals(audittype)) {
			return "newinput";
		}
		return "input";
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * BO初始化
	 * 
	 * @author 胡丽丽
	 * @date 2009-10-16 12:00
	 */
	@Override
	protected void prepareModel() throws Exception {
		if (orgId != null && !"".equals(orgId)) {
			model = manager.getOrgByID(orgId);
		} else {
			model = new ToaBaseOrg();
		}

	}

	/**
	 * 保存机构
	 * 
	 * @author 胡丽丽
	 * @date 2009-10-16 12:00
	 */
	@Override
	public String save() throws Exception {
		if ("".equals(orgId)) {
			model.setOrgid(null);
			//pengxq于20110105新增
			User user=userManagers.getCurrentUser();
			TUumsBaseOrg org=userManagers.getSupOrgByUserIdByHa(user.getUserId());
			model.setUserOrgid(org.getOrgId());
			model.setUserOrgcode(org.getOrgSyscode());
		} else {
			model.setOrgid(orgId);
		}
		if (model.getOrgIsdel() == null || "".equals(model.getOrgIsdel())) {
			model.setOrgIsdel("0");
		}
		if (moveOrgId == null || moveOrgId == "") {
			manager.save(model);
			addActionMessage("保存成功");
			return renderHtml("<script>" + "window.location='"
					+ getRequest().getContextPath()
					+ "/personnel/personorg/personOrg!addchild.action'; "
					+ " parent.organiseTree.location='"
					+ getRequest().getContextPath()
					+ "/personnel/personorg/personOrg!tree.action'; </script>");
		} else {
			// manager.save(model);
			// String[] orgs=moveOrgId.split(",");
			// ToaBaseOrg baseorg=null;
			// for(int i=0;i<orgs.length;i++){
			// baseorg=manager.getOrgByID(orgs[i]);
			// String code = manager.getNextOrgCode(nextCode);
			// baseorg.setOrgSyscode(orgSyscode)
			// orgList=manager.getOrgByParent(org.getOrgSyscode());
			// for(ToaBaseOrg org:orgList){//修改子机构的编码
			// String code = manager.getNextOrgCode(nextCode);
			// org.setOrgSyscode(code);
			// manager.save(org);
			// }
			// }
			return null;
		}
	}

	/**
	 * 机构树
	 * 
	 * @author 胡丽丽
	 * @date 2009-10-16 12:00
	 * @return
	 * @throws Exception
	 */
	public String tree() throws Exception {

		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		if ("structure".equals(audittype)) {
			orgList = manager.getOrgsByIsdel(NOTDEL);
			return "structure";
		} else if ("person".equals(audittype)) {
			orgList = manager.getOrgsByIsdel(NOTDEL);
			return "person";
		} else if("query".equals(audittype)){
			orgList = manager.getOrgsByIsdel(NOTDEL);
			return "query";
		}else if ("statistic".equals(audittype)) {
			orgList = manager.getOrgsByIsdel(NOTDEL);
			return "statistic";
		} else {
			orgList = manager.getAllOrg();// 获取所有机构
			return "tree";
		}
	}

	/**
	 * 获取机构列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String choiceOrg() throws Exception {

		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		if ("structure".equals(audittype)) {
			orgList = manager.getOrgsByIsdel(NOTDEL);
			return "structure";
		} else if ("person".equals(audittype)) {
			orgList = manager.getOrgsByIsdel(NOTDEL);
			return "person";
		} else if ("statistic".equals(audittype)) {
			orgList = manager.getOrgsByIsdel(NOTDEL);
			return "statistic";
		} else {

			orgList = manager.getAllOrg();// 获取所有机构
			return "choiceOrg";
		}
	}

	/**
	 * 选择机构树
	 * 
	 * @author 胡丽丽
	 * @date 2009-10-16 12:00
	 * @return
	 * @throws Exception
	 */
	public String radiotree() throws Exception {
		prepareModel();
		orgList = manager.getOrgsByIsdelnoum(NOTDEL, model.getOrgSyscode());
		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		return "radiotree";
	}

	/**
	 * 移动机构
	 * 
	 * @author 胡丽丽
	 * @date 2009-10-16 12:00
	 * @return
	 * @throws Exception
	 */
	public String tomove() throws Exception {
		prepareModel();
		// 根据机构ID获取机构信息
		ToaBaseOrg baseorg = manager.getOrgByID(moveOrgId);
		orgSysCode = baseorg.getOrgSyscode();// 获取移动到的机构编码
		String nextCode = manager.getNextOrgCode(orgSysCode);
		// 查询机构下的子机构
		orgList = manager.getOrgByParent(model.getOrgSyscode());
		Map<String, ToaBaseOrg> orgs = new HashMap<String, ToaBaseOrg>();
		for (ToaBaseOrg org : orgList) {
			orgs.put(org.getOrgSyscode(), org);
		}
		// 原机构CODE
		String upcode = model.getOrgSyscode();
		model.setOrgSyscode(nextCode);
		manager.save(model);
		orgmap.put(model.getOrgid(), model);
		List<ToaBaseOrg> list = new ArrayList<ToaBaseOrg>();
		String code = "";// 移动后子机构编制
		do {
			// 判断是否存在未修改的机构
			if (list.size() > 0) {
				orgList = list;
			}
			list = new ArrayList<ToaBaseOrg>();
			for (ToaBaseOrg org : orgList) {// 修改子机构的编码
				code = "";
				if (org.getOrgSyscode().length() > upcode.length() + 4) {
					String syscode = manager.getParentOrgSyscode(org
							.getOrgSyscode());
					orgId = orgs.get(syscode).getOrgid();
					// 判断是否已经移动
					if (orgmap.get(orgId) != null) {
						code = manager.getNextOrgCode(orgmap.get(orgId)
								.getOrgSyscode());
					} else {
						list.add(org);
						continue;
					}
				} else {
					code = manager.getNextOrgCode(nextCode);
				}
				// 是否有新编码
				if (!"".equals(code)) {
					org.setOrgSyscode(code);
					manager.save(org);
					orgmap.put(org.getOrgid(), org);
				}
			}
		} while (list.size() > 0);

		return renderHtml("<script> window.parent.document.location.reload();</script>");

	}

	/**
	 * 机构选择树
	 * 
	 * @author 胡丽丽
	 * @date 2009-10-16 12:00
	 * @return
	 * @throws Exception
	 */
	public String selectTree() throws Exception {
		prepareModel();
		orgList = manager.getOrgsByIsdelnoum(NOTDEL, model.getOrgSyscode());
		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		return "selecttree";
	}

	/**
	 * 合并新的机构
	 * 
	 * @author 胡丽丽
	 * @date 2009-10-16 12:00
	 * @return
	 * @throws Exception
	 */
	public String merge() throws Exception {
		String mes = "";// 消息
		orgSysCode = model.getOrgSyscode();
		if (model.getOrgid() == null || "".equals(model.getOrgid())) {
			model.setOrgid(orgId);
		}
		manager.save(model);
		String[] ids = moveOrgId.split(",");// 分割要合并的所有机构ID
		for (int i = 0; i < ids.length; i++) {// 合并机构
			if (orgmap.get(ids[i]) == null) {// 判断机构是否为空
				mes = mes + this.getorgByParent(ids[i]);
			}
		}
		if (!"".equals(mes)) {// 判断是否全部合并
			mes = mes + "合并失败，请先掉转这些机构下人员！";
			return renderHtml("<script> alert('" + mes
					+ "'); window.close();</script>");
		}
		return renderHtml("<script> window.close()</script>");
	}

	/**
	 * 修改机构父子关系
	 * 
	 * @author 胡丽丽
	 * @date 2009-10-16 12:00
	 * @param id
	 * @return
	 */
	private String getorgByParent(String id) {
		String message = "";// 存放机构名称
		// 判断该机构是否已经做了合并处理
		if (orgmap.get(id) == null) {
			ToaBaseOrg org = manager.getOrgByID(id);
			// 获取机构编码
			String code = org.getOrgSyscode();
			// 查看机构下是否存在人员
			List<ToaBasePerson> userlist = personManager.getPersonByOrg(id);
			// 获取所有子机构
			List<ToaBaseOrg> orglist = manager.getOrgByParent(org
					.getOrgSyscode());
			// 判断机构下人员是否为空
			if (userlist != null && userlist.size() > 0) {
				message = org.getOrgName() + " ";
				for (ToaBaseOrg or : orglist) {
					orgmap.put(or.getOrgid(), or);
				}
				return message;
			} else {
				// 删除该机构
				manager.delete(id);
				orgmap.put(org.getOrgid(), org);
				Map<String, String> map = new HashMap<String, String>();
				for (ToaBaseOrg or : orglist) {
					map.put(or.getOrgSyscode(), or.getOrgid());
				}
				List<ToaBaseOrg> list = new ArrayList<ToaBaseOrg>();
				String nextCode = "";
				// 修改机构下子机构的父子关系
				do {
					if (list.size() > 0) {
						orglist = list;
					}
					list = new ArrayList<ToaBaseOrg>();
					for (ToaBaseOrg or : orglist) {
						nextCode = "";
						if (or.getOrgSyscode().length() > code.length() + 4) {
							String syscode = manager.getParentOrgSyscode(or
									.getOrgSyscode());
							orgId = map.get(syscode);
							ToaBaseOrg orgobj = orgmap.get(orgId);
							if (orgobj != null) {
								nextCode = manager.getNextOrgCode(orgobj
										.getOrgSyscode());
							} else {
								list.add(or);
								continue;
							}
						} else {
							nextCode = manager.getNextOrgCode(model
									.getOrgSyscode());
						}
						if (!"".equals(nextCode))
							;
						or.setOrgSyscode(nextCode);
						manager.save(or);
						orgmap.put(or.getOrgid(), or);
					}
				} while (list.size() > 0);
			}
		}
		return "";
	}

	/**
	 * 获取增加组织机构时设置下一个系统编码
	 * 
	 * @author 胡丽丽
	 * @date 2009-10-16 12:00
	 * @return
	 * @throws IOException
	 */
	public String addchild() throws Exception {
		this.prepareModel();
		dictlist = dictService.getItemsByDictValue("JIGOU");// 获取密级
		if (!Const.IS_YES.equals(model.getOrgIsdel())) {
			String nextCode = manager.getNextOrgCode(orgSysCode);
			// 组织机构已达到编码的最底层
			if (nextCode.equals(orgSysCode) && orgId != null
					&& !"".equals(orgId)) {
				return this
						.renderHtml("<script>alert('组织机构已达最底层，无法添加！');location='"
								+ this.getRequest().getContextPath()
								+ "/organisemanage/orgmanage!input.action?orgId="
								+ orgId + "&audittype=get'</script>");
			}
			model = new ToaBaseOrg();
			model.setOrgSyscode(nextCode);
			return INPUT;
		} else {
			return this
					.renderHtml("<script>alert('该组织结构已经被删除，无法进行该操作！');location='"
							+ this.getRequest().getContextPath()
							+ "/personnel/personorg/personOrg!input.action?orgId="
							+ orgId + "&audittype=get'</script>");
		}
	}

	/**
	 * 判断父级元素是否被设置删除属性
	 * 
	 * @author 胡丽丽
	 * @date 2009-10-16 12:00
	 * @param orgSyscode
	 * @return
	 * @throws Exception
	 */
	public String checkFatherIsdel() throws Exception {
		ToaBaseOrg orgInfo = manager.getParentOrgByOrgSyscode(orgSysCode);
		if (orgInfo != null) {
			if (Const.IS_YES.equals(orgInfo.getOrgIsdel())) {
				return this.renderText("true");
			}
		}
		return this.renderText("false");
	}

	/**
	 * 获取机构的所有父机构，直到顶级机构
	 * 
	 * @author 胡丽丽
	 * @date 2009-10-16 12:00
	 * @return
	 * @throws Exception
	 */
	public String parentOrgs() throws Exception {
		this.prepareModel();

		ToaBaseOrg org = manager
				.getParentOrgByOrgSyscode(model.getOrgSyscode());
		String orgids = org.getOrgid();
		String id = model.getOrgid();
		while (!org.getOrgid().equals(id)) {
			id = org.getOrgid();
			org = manager.getParentOrgByOrgSyscode(org.getOrgSyscode());
			orgids = orgids + "," + org.getOrgid();
		}
		return renderHtml(orgids);
	}

	/**
	 * 获取机构性质
	 * 
	 * @author 胡丽丽
	 * @date 2009-10-16 12:00
	 */
	private void nativelist() {
		dictlist = dictService.getItemsByDictValue("JIGOU");// 编制类型
		for (ToaSysmanageDictitem dictitem : dictlist) {
			natureMap.put(dictitem.getDictItemCode(), dictitem
					.getDictItemName());
		}
	}

	public ToaBaseOrg getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public List<ToaBaseOrg> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<ToaBaseOrg> orgList) {
		this.orgList = orgList;
	}

	@Autowired
	public void setManager(PersonOrgManager manager) {
		this.manager = manager;
	}

	public String getMoveOrgId() {
		return moveOrgId;
	}

	public void setMoveOrgId(String moveOrgId) {
		this.moveOrgId = moveOrgId;
	}

	public IUserService getUserManagers() {
		return userManagers;
	}

	@Autowired
	public void setUserManagers(IUserService userManagers) {
		this.userManagers = userManagers;
	}

	public List<Organization> getUumsList() {
		return uumsList;
	}

	public void setUumsList(List<Organization> uumsList) {
		this.uumsList = uumsList;
	}

	@Autowired
	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

	@Autowired
	public void setStructManager(PersonStructureManager structManager) {
		this.structManager = structManager;
	}

	public List<ToaBasePerson> getUserList() {
		return userList;
	}

	public void setUserList(List<ToaBasePerson> userList) {
		this.userList = userList;
	}

	public String getPersonIds() {
		return personIds;
	}

	public void setPersonIds(String personIds) {
		this.personIds = personIds;
	}


}
