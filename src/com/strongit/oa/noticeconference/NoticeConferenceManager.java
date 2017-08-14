package com.strongit.oa.noticeconference;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.strongit.oa.bo.TOmConAttach;
import com.strongit.oa.bo.TOmConPerson;
import com.strongit.oa.bo.TOmConType;
import com.strongit.oa.bo.TOmConference;
import com.strongit.oa.bo.TOmConferenceSend;
import com.strongit.oa.bo.ToaLog;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.mylog.MyLogManager;
import com.strongit.oa.noticeconference.util.Constants;
import com.strongit.oa.noticeconference.util.FileUtils;
import com.strongit.oa.noticeconference.util.StringUtil;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.security.UserInfo;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/*******************************************************************************
 * 会议通知管理
 * 
 * @Description: NoticeConferenceManager.java
 * @Date:Apr 1, 2013
 * @Author 万俊龙
 * @Version: V1.0
 * @Copyright: Jiang Xi Strong Co. Ltd. All right reserved.
 */

@Service
@Transactional
public class NoticeConferenceManager implements INoticeConferenceManager {
	private GenericDAOHibernate<TOmConference, java.lang.String> conNoticedao;
	private GenericDAOHibernate<TOmConType, java.lang.String> conTypedao; // 会议通知类型

	@Autowired
	private MyLogManager logService;// 日志管理

	// 统一用户服务
	@Autowired
	private IUserService userService;

	@Autowired
	private IConNoticeAttachManager conAttachManager;

	@Autowired
	private IConAcceptManager conAcceptManager;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		conNoticedao = new GenericDAOHibernate<TOmConference, String>(
				sessionFactory, TOmConference.class);
		conTypedao = new GenericDAOHibernate<TOmConType, String>(
				sessionFactory, TOmConType.class);
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：删除草稿状态的会议通知，并将所属的会议附件及下派单位级联删除
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 3, 2013
	 * @Author 万俊龙
	 * @param conNoticeId
	 * @version 1.0
	 * @see
	 */
	public void deleteConNotice(String conNoticeId) {
		TOmConference entity = this.getConNoticeByConId(conNoticeId);
		if (StringUtil.isNotEmpty(entity)) {
			// 判断会议类型状态，如果为草稿状态，会议通知可以删除。否则，不能删除。
			if (Constants.CONFERENCE_STATE_DRAFT.equals(entity
					.getConferenceState())) {
				try {
					// 删除会议通知下的所有附件信息
					this.conAttachManager.deleteConAttchByAttachConId(entity
							.getConferenceId());
					// 删除会议通知下的所有下派单位
					this.conAcceptManager.deleteByConId(entity
							.getConferenceId());
					// 删除会议通知本身信息
					this.conNoticedao.delete(entity);

					ToaLog log = new ToaLog();
					try {
						InetAddress inet = InetAddress.getLocalHost();
						log.setOpeIp(inet.getHostAddress()); // 操作者IP地址
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					log.setOpeUser(userService.getCurrentUser().getUserName()); // 操作姓名
					log.setLogState("1"); // 日志状态
					log.setOpeTime(new Date()); // 操作时间
					log.setLogInfo("{删除}会议通知[id=" + entity.getConferenceId()
							+ "]");// 日志信息
					logService.saveObj(log);
				} catch (ServiceException ex) {
					// TODO: handle ServiceException
					throw ex;
				} catch (DAOException ex) {
					// TODO: handle DAOException
					throw ex;
				} catch (SystemException ex) {
					// TODO: handle SystemException
					throw ex;
				} catch (Exception ex) {
					// TODO: handle Exception
					throw new SystemException(ex);
				}
			}
		}
	}

	/***************************************************************************
	 * 
	 * 方法简要描述:分页查询通知会议草稿
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 1, 2013
	 * @Author 万俊龙
	 * @param page
	 * @param entity
	 * @param beginTime
	 * @param endTime
	 * @param regendtime
	 * @return
	 * @version 1.0
	 * @throws ParseException 
	 * @see
	 */
	public Page<TOmConference> queryDraftList(Page<TOmConference> page,
			TOmConference entity, Date beginTime, Date endTime, Date regendtime) throws ParseException {
		if (StringUtil.isEmpty(entity)) {
			entity = new TOmConference();
		}
		
		entity.setConferenceState(Constants.CONFERENCE_STATE_DRAFT);
		return queryDraft(page, entity, beginTime, endTime, regendtime);
	}
	
	/***
	 * 
	* 方法简要描述：已办结会议通知
	*	 备注：只要会议通知下其中之一的单位被接收报名处理，该会议通知状态为已办结状态。
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 18, 2013
	* @Author 万俊龙
	* @param page
	* @param entity
	* @param beginTime
	* @param endTime
	* @param regendtime
	* @return
	* @version	1.0
	 * @throws ParseException 
	* @see
	 */
	public  Page<TOmConference> queryHandleList(Page<TOmConference> page,
			TOmConference entity, Date beginTime, Date endTime, Date regendtime) throws ParseException {
		if (StringUtil.isEmpty(entity)) {
			entity = new TOmConference();
		}
		entity.setConferenceState(Constants.CONFERENCE_STATE_END);
		return query(page, entity, beginTime, endTime, regendtime);
	}
	/***************************************************************************
	 * 
	 * 方法简要描述：分页查询已发通知会议
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 1, 2013
	 * @Author 万俊龙
	 * @param page
	 * @param entity
	 * @param beginTime
	 * @param endTime
	 * @param regendtime
	 * @return
	 * @version 1.0
	 * @throws ParseException 
	 * @see
	 */
	public Page<TOmConference> queryIssiedList(Page<TOmConference> page,
			TOmConference entity, Date beginTime, Date endTime, Date regendtime) throws ParseException {

		if (StringUtil.isEmpty(entity)) {
			entity = new TOmConference();
		}
		entity.setConferenceState(Constants.CONFERENCE_STATE_ISSIED);
		return query(page, entity, beginTime, endTime, regendtime);

	}
	
	
	
	/***************************************************************************
	 * 
	 * 方法简要描述:分页查询通知会议草稿
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 1, 2013
	 * @Author 万俊龙
	 * @param page
	 * @param entity
	 * @param beginTime
	 * @param endTime
	 * @param regendtime
	 * @return
	 * @version 1.0
	 * @throws ParseException 
	 * @see
	 */
	private Page<TOmConference> queryDraft(Page<TOmConference> page,
			TOmConference entity, Date beginTime, Date endTime, Date regendtime) throws ParseException {
		List paramLst = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("from TOmConference t where 1=1 ");
		if (StringUtil.isNotEmpty(entity)) {
			// 会议名称
			if (StringUtil.isNotEmpty(entity.getConferenceTitle())) {
				sql.append(" and t.conferenceTitle like ? ");
				paramLst.add("%" + entity.getConferenceTitle() + "%");
			}

			// 会议类型
			if (StringUtil.isNotEmpty(entity.getTsConfersort())
					&& StringUtil.isNotEmpty(entity.getTsConfersort()
							.getContypeId())) {
				sql.append(" and t.tsConfersort.confersortId = ? ");
				paramLst.add(entity.getTsConfersort().getContypeId());
			}
			if (StringUtil.isNotEmpty(entity.getConferenceState())) {
				sql.append(" and t.conferenceState = ? ");
				paramLst.add(entity.getConferenceState());
			}

			if (StringUtil.isNotEmpty(entity.getConferenceAddr())) {
				sql.append(" and t.conferenceAddr like ? ");
				paramLst.add("%" + entity.getConferenceAddr() + "%");
			}

			if (StringUtil.isNotEmpty(entity.getConferenceHost())) {
				sql.append(" and t.conferenceHost = ? ");
				paramLst.add(entity.getConferenceHost());
			}

			// 承办单位
			if (StringUtil.isNotEmpty(entity.getConferenceUndertaker())) {
				sql.append(" and t.conferenceUndertaker = ? ");
				paramLst.add(entity.getConferenceUndertaker());
			}
			//发起会议的用户
			if (StringUtil.isNotEmpty(entity.getConferenceRest1())) {
				User user = userService.getCurrentUser();
				sql.append(" and t.conferenceRest1 = ? ");
				paramLst.add(user.getOrgId());
			}
			if (StringUtil.isNotEmpty(entity.getConferenceRest2())) {
				sql.append(" and t.conferenceRest2 = ? ");
				paramLst.add(entity.getConferenceRest2());
			}
			
			
			if (StringUtil.isNotEmpty(entity.getConferenceRest3())) {
				sql.append(" and t.conferenceRest3 = ? ");
				paramLst.add(entity.getConferenceRest3());
			}

		}

		if (StringUtil.isNotEmpty(entity.getConferenceFromId())) {
			sql.append(" and t.conferenceFromId = ? ");
			paramLst.add(entity.getConferenceFromId());
		}

		if (StringUtil.isNotEmpty(beginTime)) {
			// sql.append(" and to_char(t.conferenceStime,'yyyy-MM-dd') = ?");
			sql.append(" and t.conferenceStime >= ?");
			paramLst.add(beginTime);
		}
		if (StringUtil.isNotEmpty(endTime)) {
			sql.append(" and t.conferenceEtime <= ?");
			paramLst.add(endTime);
		}
		

		if (StringUtil.isNotEmpty(regendtime)) {
			SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat pim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String btime = sim.format(regendtime);
			beginTime = pim.parse(btime+" 00:00:00");
			String etime = sim.format(regendtime);
			endTime = pim.parse(etime+" 23:59:59");
			
			sql.append(" and t.conferenceRegendtime >= ? ");
			paramLst.add(beginTime);
			sql.append(" and t.conferenceRegendtime <= ? ");
			paramLst.add(endTime);
		}
		sql.append(" order by conferenceStime desc");

		return this.conNoticedao.find(page, sql.toString(), paramLst.toArray());
	}
	

	/***************************************************************************
	 * 
	 * 方法简要描述:分页查询通知会议
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 1, 2013
	 * @Author 万俊龙
	 * @param page
	 * @param entity
	 * @param beginTime
	 * @param endTime
	 * @param regendtime
	 * @return
	 * @version 1.0
	 * @throws ParseException 
	 * @see
	 */
	private Page<TOmConference> query(Page<TOmConference> page,
			TOmConference entity, Date beginTime, Date endTime, Date regendtime) throws ParseException {
		List paramLst = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("from TOmConference t where 1=1 ");
		if (StringUtil.isNotEmpty(entity)) {
			// 会议名称
			if (StringUtil.isNotEmpty(entity.getConferenceTitle())) {
				sql.append(" and t.conferenceTitle like ? ");
				paramLst.add("%" + entity.getConferenceTitle() + "%");
			}

			// 会议类型
			if (StringUtil.isNotEmpty(entity.getTsConfersort())
					&& StringUtil.isNotEmpty(entity.getTsConfersort()
							.getContypeId())) {
				sql.append(" and t.tsConfersort.confersortId = ? ");
				paramLst.add(entity.getTsConfersort().getContypeId());
			}
			if (StringUtil.isNotEmpty(entity.getConferenceState())) {
				sql.append(" and t.conferenceState = ? ");
				paramLst.add(entity.getConferenceState());
			}

			if (StringUtil.isNotEmpty(entity.getConferenceAddr())) {
				sql.append(" and t.conferenceAddr like ? ");
				paramLst.add("%" + entity.getConferenceAddr() + "%");
			}

			if (StringUtil.isNotEmpty(entity.getConferenceHost())) {
				sql.append(" and t.conferenceHost = ? ");
				paramLst.add(entity.getConferenceHost());
			}

			// 承办单位
			if (StringUtil.isNotEmpty(entity.getConferenceUndertaker())) {
				sql.append(" and t.conferenceUndertaker = ? ");
				paramLst.add(entity.getConferenceUndertaker());
			}
			//发起会议的用户
			if (StringUtil.isNotEmpty(entity.getConferenceRest1())) {
				User user = userService.getCurrentUser();
				sql.append(" and t.conferenceRest1 = ? ");
				paramLst.add(user.getOrgId());
			}
			if (StringUtil.isNotEmpty(entity.getConferenceRest2())) {
				sql.append(" and t.conferenceRest2 = ? ");
				paramLst.add(entity.getConferenceRest2());
			}
			
			
			if (StringUtil.isNotEmpty(entity.getConferenceRest3())) {
				sql.append(" and t.conferenceRest3 = ? ");
				paramLst.add(entity.getConferenceRest3());
			}

		}

		if (StringUtil.isNotEmpty(entity.getConferenceFromId())) {
			sql.append(" and t.conferenceFromId = ? ");
			paramLst.add(entity.getConferenceFromId());
		}

		if (StringUtil.isNotEmpty(beginTime)) {
			// sql.append(" and to_char(t.conferenceStime,'yyyy-MM-dd') = ?");
			sql.append(" and t.conferenceRegendtime >= ?");
			paramLst.add(beginTime);
		}
		if (StringUtil.isNotEmpty(endTime)) {
			sql.append(" and t.conferenceRegendtime <= ?");
			paramLst.add(endTime);
		}
		

		if (StringUtil.isNotEmpty(regendtime)) {
			SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat pim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String btime = sim.format(regendtime);
			beginTime = pim.parse(btime+" 00:00:00");
			String etime = sim.format(regendtime);
			endTime = pim.parse(etime+" 23:59:59");
			
			sql.append(" and t.conferenceRegendtime >= ? ");
			paramLst.add(beginTime);
			sql.append(" and t.conferenceRegendtime <= ? ");
			paramLst.add(endTime);
		}
		sql.append(" order by conferenceStime desc");

		return this.conNoticedao.find(page, sql.toString(), paramLst.toArray());
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：根据附件编号，获取附件实体对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 3, 2013
	 * @Author 万俊龙
	 * @param attachId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public TOmConAttach getConAttachByAttachId(String attachId) {
		Assert.notNull(attachId);
		TOmConAttach entity = this.conAttachManager
				.getConAttachByAttachId(attachId);
		return entity;
	}

	/***************************************************************************
	 * 
	 * 方法简要描述:构造附件展现html浮层，在前台页面需设定 function deldbobj()方法
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 3, 2013
	 * @Author 万俊龙
	 * @param request
	 * @param conNoticeId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public String getConAttachHtmlByConId(HttpServletRequest request,
			String conNoticeId) {
		StringBuffer contentsHtml = new StringBuffer();
		List<TOmConAttach> list = this.conAttachManager
				.getAttachsByAttachConId(conNoticeId, false);
		if (StringUtil.isNotEmpty(list)) {
			for (TOmConAttach attach : list) {
				// 使用该预留字段标识附件是否为正文[0:不是|1:是]
				String type = attach.getAttachRest1();
				StringBuilder temp = new StringBuilder();
				temp.append("<div id=div" + attach.getAttachId()
						+ " attachtype=" + type + ">");
				temp.append("[<a onclick=\"deldbobj('" + attach.getAttachId()
						+ "');\"");
				temp.append("href=\"#\">删除</a>]");

				temp.append("<a ");
				temp
						.append("href=\""
								+ request.getContextPath()
								+ "/noticeconference/noticeConference!downloadConAttach.action?conAttachId="
								+ attach.getAttachId()
								+ "\"><font color='red'>");
				temp.append(attach.getAttachFileName());
				temp.append("</font></a>");
				temp.append("</div>");
				temp.append("<input value=" + attach.getAttachId() + " id="
						+ attach.getAttachId() + " type=\"hidden\" ");
				temp.append(" name=\"dbobj\" />");
				contentsHtml.append(temp.toString());
			}
			return contentsHtml.toString();
		}
		return null;

	}

	/***************************************************************************
	 * 
	 * 方法简要描述：根据会议编号Id，获取所有下发单位主键信息 数据格式：【13333333,12222】
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 3, 2013
	 * @Author 万俊龙
	 * @param conNoticeId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public String getAcceptOrgIdsByConid(String conNoticeId,
			StringBuffer deptNames) {

		List<TOmConferenceSend> acceptLst = this.conAcceptManager
				.findByconNoticeId(conNoticeId);
		if (StringUtil.isNotEmpty(acceptLst)) {
			StringBuffer ids = new StringBuffer();
			for (TOmConferenceSend entity : acceptLst) {

				ids.append(entity.getDeptCode()).append(",");
				deptNames.append(entity.getDeptName()).append(";");

			}
			return ids.toString();
		}
		return null;
	}
	
	/***************************************************************************
	 * 
	 * 方法简要描述：根据会议编号Id，获取所有下发单位主键信息 数据格式：【13333333,12222】
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 3, 2013
	 * @Author 万俊龙
	 * @param conNoticeId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public String getAcceptOrgIdsByConid(String conNoticeId) {

		List<TOmConferenceSend> acceptLst = this.conAcceptManager
				.findByconNoticeId(conNoticeId);
		if (StringUtil.isNotEmpty(acceptLst)) {
			StringBuffer ids = new StringBuffer();
			for (TOmConferenceSend entity : acceptLst) {
				ids.append(entity.getDeptCode()).append(",");				 
			}
			return ids.toString();
		}
		return null;
	}
	
	

	/***************************************************************************
	 * 
	 * 方法简要描述:保存会议通知对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 2, 2013
	 * @Author 万俊龙
	 * @param model
	 * @param attachsFileName
	 * @param attachs
	 * @version 1.0
	 * @see
	 */
	public void saveConferInfo(TOmConference model, String depIds,
			String[] attachsFileName, File[] attachs) {
		try {
			saveConNotice(model);// 保存会议通知
			// 保存下发单位信息
			if (StringUtil.isNotEmpty(depIds)) {
				this.conAcceptManager
						.save(model.getConferenceId(), depIds, "0");
			}
			// 保存附件内容信息
			InetAddress inet = InetAddress.getLocalHost();
			if (attachsFileName != null && attachsFileName.length > 0) {
				FileInputStream fis = null;
				for (int i = 0; i < attachsFileName.length; i++) {
					try {
						TOmConAttach attachEntity = new TOmConAttach();
						attachEntity.setAttachFileData(new Date());// 设置上传时间
						attachEntity.setAttachRest1("0");// 附件类型为附件信息附件
						attachEntity.setConferenceId(model.getConferenceId());// 设置会议通知id
						fis = new FileInputStream(attachs[i]);
						attachEntity.setAttachContent(FileUtils
								.inputstream2ByteArray(fis));// 设置附件内容
						attachEntity.setAttachFileName(attachsFileName[i]);// 设置附件名称
						String[] attachFileNameInfo = attachsFileName[i]
								.split("\\.");
						attachEntity
								.setAttachFileType(attachFileNameInfo[attachFileNameInfo.length - 1]);
						attachEntity.setFileServer(inet.getHostAddress());
						conAttachManager.saveConAttach(attachEntity);// 保存附件
					} catch (Exception e) {
						throw new SystemException(e);
						// TODO: handle exception
					} finally {
						if (fis != null) {
							try {
								fis.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			// 删除用户要去除的附件信息
			String[] delAttachId = ServletActionContext.getRequest()
					.getParameterValues("dbobj");
			if (delAttachId != null && delAttachId.length > 0) {
				for (String id : delAttachId) {
					if (id.endsWith(";")) {
						// 需要删除的附件
						conAttachManager.deleteConAttach(id.substring(0, id
								.length() - 1));
					}
				}
			}
		} catch (ServiceException ex) {
			// TODO: handle ServiceException
			throw ex;
		} catch (DAOException ex) {
			// TODO: handle DAOException
			throw ex;
		} catch (SystemException ex) {
			// TODO: handle SystemException
			throw ex;
		} catch (Exception ex) {
			// TODO: handle Exception
			throw new SystemException(ex);
		}
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：保存会议通知对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 1, 2013
	 * @Author 万俊龙
	 * @param entity
	 * @throws Exception
	 * @version 1.0
	 * @see
	 */
	public void saveConNotice(TOmConference entity) throws Exception {
		Assert.notNull(entity);
		// entity.setConferenceId(null);
		try {
			if (StringUtil.isEmpty(entity.getConferenceId())) {
				entity.setConferenceId(null);
				entity.setConEntryTime(new Date());
				this.conNoticedao.save(entity);
			} else {
				this.conNoticedao.update(entity);
			}

			ToaLog log = new ToaLog();
			try {
				InetAddress inet = InetAddress.getLocalHost();
				log.setOpeIp(inet.getHostAddress()); // 操作者IP地址
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			log.setOpeUser(userService.getCurrentUser().getUserName()); // 操作姓名
			log.setLogState("1"); // 日志状态
			log.setOpeTime(new Date()); // 操作时间
			log.setLogInfo("{新增}会议通知[id=" + entity.getConferenceId() + "]");// 日志信息
			logService.saveObj(log);

		} catch (Exception e) {
			throw e;
		}

	}

	/***************************************************************************
	 * 
	 * 方法简要描述：根据会议通知主键，获取会议通知实体对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 3, 2013
	 * @Author 万俊龙
	 * @param conNoticeId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public TOmConference getConNoticeByConId(String conNoticeId) {
		Assert.notNull(conNoticeId);
		return this.conNoticedao.get(conNoticeId);
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：保存会议类型; 保存会议类型之前，判断会员类型表中是否存在相同名称的会议类型，如果存在，不在保存。
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 1, 2013
	 * @Author 万俊龙
	 * @param entity
	 * @version 1.0
	 * @see
	 */
	public void saveConType(TOmConType entity) {
		Assert.notNull(entity);
		entity.setContypeId(null);
		if (!hasConType(entity.getContypeName())) {
			this.conTypedao.save(entity);
		}
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：根据会议类型名称，查找会议类型对象。
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 1, 2013
	 * @Author 万俊龙
	 * @param name
	 * @return
	 * @version 1.0
	 * @see
	 */
	public TOmConType getContypeByName(String name) {
		Assert.notNull(name);
		StringBuffer sql = new StringBuffer();
		sql.append("from TOmConType t where t.confersortName = ? ");
		List<TOmConType> lst = this.conTypedao.find(sql.toString(),
				new Object[] { name });
		if (StringUtil.isNotEmpty(lst)) {
			return lst.get(0);
		}
		return null;
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：判断是否存在指定名称的会议类型; <br>
	 * false:表示不存在 true:表示存在
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 1, 2013
	 * @Author 万俊龙
	 * @param name
	 * @return
	 * @version 1.0
	 * @see
	 */
	public boolean hasConType(String name) {
		TOmConType entity = this.getContypeByName(name);
		if (StringUtil.isEmpty(entity)) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * 方法简要描述:获取所有的会议通知类型。
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 1, 2013
	 * @Author 万俊龙
	 * @return
	 * @version 1.0
	 * @see
	 */
	public List<TOmConType> findAll() {
		return this.conTypedao.findAll();
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：根据类型主键，获取类型实体对象。
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 2, 2013
	 * @Author 万俊龙
	 * @param sortId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public TOmConType getConferSortById(String sortId) {
		Assert.notNull(sortId);
		return this.conTypedao.get(sortId);
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：判断会议通知是否存在下发对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 7, 2013
	 * @Author 万俊龙
	 * @param conId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public boolean hasConferenceSend(String conId) {
		List<TOmConferenceSend> list = this.conAcceptManager
				.findByconNoticeId(conId);
		if (StringUtil.isNotEmpty(list)) {
			return true;
		}
		return false;
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：下发会议通知
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 7, 2013
	 * @Author 万俊龙
	 * @version 1.0
	 * @see
	 */
	public void sendConference(String conId) {
		Assert.notNull(conId);
		// 会议下发时间是否需要考虑 会议召开时间及会议截至时间
		// 会议下发时间如果在截至时间内下发
		// 会议下发时间如果在会议召开期间下发，则下发无效

		// 更新会议下发表
	/*	List<TOmConferenceSend> list = this.conAcceptManager
				.findByconNoticeId(conId);
		if (StringUtil.isNotEmpty(list)) {
			for (TOmConferenceSend conSend : list) {
				conSend
						.setRecvState(Constants.CONFERENCE_SEND_RECVSTATE_DEFAULT);
				this.conAcceptManager.save(conSend);
			}
		}*/
		updateConferenceState(conId);
	}
	
	/****
	 * 
	* 方法简要描述:保存会议下派单位
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 9, 2013
	* @Author 万俊龙
	* @param conId
	* @param deptIds
	* @version	1.0
	* @see
	 */
	public void saveForConsend(String conId,String deptIds){
		this.conAcceptManager.save(conId, deptIds);
	}
	
	/***
	 * 
	* 方法简要描述：查找会议下下派的单位
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 9, 2013
	* @Author 万俊龙
	* @param conId
	* @return
	* @version	1.0
	* @see
	 */
	public List<TOmConferenceSend> findConferenceSendListByConId(String conId){
		List<TOmConferenceSend> list= this.conAcceptManager.findByconNoticeId(conId);
	 
		
		
		return list;
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：更新会议通知状态
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 7, 2013
	 * @Author 万俊龙
	 * @version 1.0
	 * @see
	 */
	public void updateConferenceState(String conId) {
		Assert.notNull(conId);
		TOmConference entity = this.conNoticedao.get(conId);
		if (StringUtil.isNotEmpty(entity)) {
			entity.setConferenceState(Constants.CONFERENCE_STATE_ISSIED);
			this.conNoticedao.update(entity);
		}
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：得到所有单位及其下人员信息列表
	 * 
	 * @return List<Object[]> 信息列表<br>
	 *         <p>
	 *         信息列表数据结构：<br>
	 *         <p>
	 *         Object[]{组织机构Id, 父级组织机构Id, 组织机构名称, 组织机构下人员信息}<br>
	 *         <p>
	 *         最顶级组织机构父级Id为”0“<br>
	 *         <p>
	 *         人员信息数据机构：<br>
	 *         <p>
	 *         userList<String[]{人员Id, 人员名称}> 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * @Date:Apr 7, 2013
	 * @Author 万俊龙
	 * @return
	 * @version 1.0
	 * @see
	 */
	public List<Object[]> getAllOrgUserList() {
		String parentOrgId;
		List<Object[]> returnList = new ArrayList<Object[]>();
		List<String[]> orgUserList;

		List<TUumsBaseOrg> orgList = userService.getSelfAndChildOrgsByOrgSyscodeByHa("001999", "0", "8a928a703bb11098013bb6756e9a004c");
		List<TOmConPerson> userList = null;
		try {
			// 下发组织机构人员信息 来源：统一用户 or 参会成员 ？？？？？？？？？？？
			// userList =this.userService.getAllUserInfos();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (orgList != null && !orgList.isEmpty()) {
			for (TUumsBaseOrg org : orgList) {
				orgUserList = new ArrayList<String[]>();
				if (StringUtil.isNotEmpty(orgUserList)) {
					for (int i = 0; i < userList.size();) {
						TOmConPerson user = userList.get(i);
						if (org.getOrgId().equals(user.getDepid())) {
							orgUserList.add(new String[] { user.getPersonId(),
									user.getPersonName() });
							userList.remove(i);
							continue;
						}
						i++;
					}
				}

				/**
				 * 如果此组织机构已是最顶级，则其父级Id为”0“
				 */

				parentOrgId = org.getOrgParentId();
				if (parentOrgId == null || "".equals(parentOrgId)) {
					parentOrgId = "0";
				}
				returnList.add(new Object[] { org.getOrgId(), parentOrgId,
						org.getOrgName(), orgUserList });
			}
		}
		return returnList;
	}
	
 

}
