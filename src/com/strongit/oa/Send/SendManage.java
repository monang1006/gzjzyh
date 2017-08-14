package com.strongit.oa.Send;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.Receive.DataSource;
import com.strongit.oa.attachment.IWorkflowAttachService;
import com.strongit.oa.bo.TabContentFileSend;
import com.strongit.oa.bo.TabContentSend;
import com.strongit.oa.bo.ToaLog;
import com.strongit.oa.bo.ToaSenddoc;
import com.strongit.oa.bo.WorkflowAttach;
import com.strongit.oa.common.service.DataBaseUtil;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.mylog.MyLogManager;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.util.StringUtil;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date May 30, 2012 10:08:38 AM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.Send.SendManage
 */
@Service
@Transactional
public class SendManage {
	private GenericDAOHibernate<Object, java.lang.String> dao;

	@Autowired
	private SendDocManager sendDocManager;

	@Autowired
	private IWorkflowAttachService workflowAttachManager;

	@Autowired
	private MyLogManager logService;

	@Autowired
	private IUserService userService;

	private static String unitName;// 所属单位

	public static Map<String, String> JJCDMap = new HashMap<String, String>();
	static {
		unitName = DataSource.getDataSoureProperties().getProperty(
				"sendunitName");
		JJCDMap.put("0", "无");
		JJCDMap.put("1", "紧急");
		JJCDMap.put("2", "紧急");
		JJCDMap.put("3", "特急");
		JJCDMap.put("4", "特急");
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		dao = new GenericDAOHibernate<Object, String>(sessionFactory,
				Object.class);
	}

	/**
	 * 保存发文
	 * 
	 * @description
	 * @author 严建
	 * @param bean
	 * @createTime Jun 1, 2012 11:41:29 AM
	 */
	private void saveTabContentSendInOASystem(TabContentSend bean)
			throws ServiceException, DAOException, SystemException {
		try {
			dao.save(bean);

			ToaLog log = new ToaLog();
			try {
				InetAddress inet = InetAddress.getLocalHost();
				log.setOpeIp(inet.getHostAddress()); // 操作者IP地址
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			log.setOpeUser(userService
					.getUserNameByUserId(IUserService.SYSTEM_ACCOUNT)); // 操作姓名
			log.setLogState("1"); // 日志状态
			log.setOpeTime(new Date()); // 操作时间
			log.setLogInfo("【OA系统】：保存公文传输发文存根数据成功,id=" + bean.getId());// 日志信息
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

	/**
	 * 保存发文数据到公文传输系统
	 * 
	 * @description
	 * @author 严建
	 * @param bean
	 * @createTime Jun 1, 2012 11:47:54 AM
	 */
	public void saveTabContentSendInTranSystem(TabContentSend bean)
			throws ServiceException, DAOException, SystemException {
		Connection Conn = null;
		PreparedStatement ps = null;
		try {
			Conn = getConnecton();
			String sql = "INSERT INTO TAB_CONTENT_SEND (ID, CONTENT,FLAG,createDate,lockstatus,unitName) VALUES (?,?,?,?,?,?)";
			ps = Conn.prepareStatement(sql);
			ps.setObject(1, bean.getId());
			ps.setObject(2, bean.getContent());
			ps.setInt(3, Integer.parseInt(bean.getFlag()));
			ps.setDate(4, new java.sql.Date(bean.getCreateDate().getTime()));
			ps.setObject(5, bean.getLockstatus());
			ps.setObject(6, bean.getUnitName());
			ps.execute();

			ToaLog log = new ToaLog();
			try {
				InetAddress inet = InetAddress.getLocalHost();
				log.setOpeIp(inet.getHostAddress()); // 操作者IP地址
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			log.setOpeUser(userService
					.getUserNameByUserId(IUserService.SYSTEM_ACCOUNT)); // 操作姓名
			log.setLogState("1"); // 日志状态
			log.setOpeTime(new Date()); // 操作时间
			log.setLogInfo("【公文传输系统】：备份公文传输发文存根数据成功,id=" + bean.getId());// 日志信息
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
			ex.printStackTrace();
			throw new SystemException(ex);
		} finally {
			DataBaseUtil.closePreparedStatement(ps);
			DataBaseUtil.closeConnecton(Conn);
		}
	}

	/**
	 * 保存发文附件
	 * 
	 * @description
	 * @author 严建
	 * @param bean
	 * @createTime Jun 1, 2012 11:42:27 AM
	 */
	private void saveTabContentFileSendInOASystem(TabContentFileSend bean)
			throws ServiceException, DAOException, SystemException {
		try {
			dao.save(bean);

			ToaLog log = new ToaLog();
			try {
				InetAddress inet = InetAddress.getLocalHost();
				log.setOpeIp(inet.getHostAddress()); // 操作者IP地址
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			log.setOpeUser(userService
					.getUserNameByUserId(IUserService.SYSTEM_ACCOUNT)); // 操作姓名
			log.setLogState("1"); // 日志状态
			log.setOpeTime(new Date()); // 操作时间
			log.setLogInfo("【OA系统】：保存公文传输发文附件存根数据成功,发文id=" + bean.getId()
					+ "|附件id=" + bean.getFileId() + "|附件类型="
					+ (bean.getFileState().equals("1") ? "附件" : "正文"));// 日志信息
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

	/**
	 * 保存发文附件到公文传输系统
	 * 
	 * @description
	 * @author 严建
	 * @param bean
	 * @createTime Jun 1, 2012 11:48:50 AM
	 */
	private void saveTabContentFileSendInTranSystem(TabContentFileSend bean)
			throws ServiceException, DAOException, SystemException {
		Connection Conn = null;
		PreparedStatement ps = null;
		try {
			Conn = getConnecton();
			String sql = "INSERT INTO TAB_CONTENT_FILE_SEND (ID, FILEID,FILENAME,FILECONTENT,FILESTATE) VALUES (?,?,?,?,?)";
			ps = Conn.prepareStatement(sql);
			ps.setObject(1, bean.getId());
			ps.setObject(2, bean.getFileId());
			ps.setObject(3, bean.getFileName());
			ps.setObject(4, bean.getFileContent());
			ps.setObject(5, bean.getFileState());
			ps.execute();

			ToaLog log = new ToaLog();
			try {
				InetAddress inet = InetAddress.getLocalHost();
				log.setOpeIp(inet.getHostAddress()); // 操作者IP地址
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			log.setOpeUser(userService
					.getUserNameByUserId(IUserService.SYSTEM_ACCOUNT)); // 操作姓名
			log.setLogState("1"); // 日志状态
			log.setOpeTime(new Date()); // 操作时间
			log.setLogInfo("【公文传输系统】：备份公文传输发文附件存根数据成功,发文id=" + bean.getId()
					+ "|附件id=" + bean.getFileId() + "|附件类型="
					+ (bean.getFileState().equals("1") ? "附件" : "正文"));// 日志信息
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
		} finally {
			DataBaseUtil.closePreparedStatement(ps);
			DataBaseUtil.closeConnecton(Conn);
		}
	}

	/**
	 * 获取中间表数据库的连接
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @createTime May 30, 2012 10:56:27 AM
	 */
	private static Connection getConnecton() throws ServiceException,
			DAOException, SystemException {
		try {
			return DataSource.getConnecton();
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

	/**
	 * 锁定指定的发文数据
	 * 
	 * @description
	 * @author 严建
	 * @param docId
	 *            return 锁定成功返回true;否则返回false
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * @createTime Jun 1, 2012 4:10:27 PM
	 */
	public synchronized boolean lock(String docId) throws ServiceException,
			DAOException, SystemException {
		boolean result = false;
		try {
			result = isSend(docId);
			if (result) {
				String sql = "UPDATE T_OA_SENDDOC SET FLAG = '1' WHERE SENDDOC_ID='"
						+ docId + "'";
				sendDocManager.getJdbcTemplate().execute(sql);
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
		return result;
	}

	/**
	 * 如果发生异常，解锁该数据
	 * 
	 * @description
	 * @author 严建
	 * @param docId
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * @createTime Jun 2, 2012 4:07:04 PM
	 */
	public void unLockIfException(String docId) throws ServiceException,
			DAOException, SystemException {
		try {
			String sql = "UPDATE T_OA_SENDDOC SET FLAG = '0' WHERE SENDDOC_ID='"
					+ docId + "'";
			sendDocManager.getJdbcTemplate().execute(sql);
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

	/**
	 * 
	 * 根据发文主键值判断该文是否能够进行发送
	 * 
	 * @description
	 * @author 严建
	 * @param docId
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * @createTime Jun 1, 2012 4:03:53 PM
	 */
	private boolean isSend(String docId) throws ServiceException, DAOException,
			SystemException {
		boolean result = false;
		try {
			String countsql = "SELECT count(SENDDOC_ID) AS TOTAL FROM T_OA_SENDDOC where SENDDOC_ID = '"
					+ docId + "' AND WORKFLOWSTATE='2' AND FLAG='0'";
			Map map = sendDocManager.queryForMap(countsql);
			if (!(map.get("TOTAL") + "").equals("0")) {
				result = true;
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
		return result;
	}

	/**
	 * 保存发文所有的数据到中间表
	 * 
	 * @description
	 * @author 严建
	 * @param id
	 * @createTime May 30, 2012 10:08:35 AM
	 */
	public void saveSendAllInfo(TabContentSend bean) throws ServiceException,
			DAOException, SystemException {
		try {
			/** 将发文数据在oa系统中保留存根 */
			List<TabContentFileSend> attchs = this.getAttachsBySendDocId(bean
					.getSenddocId());// 获取oa中发文的附件信息
			bean.setAttachs(attchs);// 获取附件数据
			TabContentSend tabcontentsendbean = getTabContentSendBySendDocId(bean);// 发文数据
			saveTabContentSendInOASystem(tabcontentsendbean);// 保存发文数据到OA系统中
			TabContentFileSend mainContent = bean.getMainContent();// 发文正文
			if (mainContent != null) {
				mainContent.setFileState("0");// 设置为正文
				mainContent.setId(tabcontentsendbean.getId());// 设置外键
				saveTabContentFileSendInOASystem(mainContent);// 保存正文
			}
			if (attchs != null && !attchs.isEmpty()) {
				for (TabContentFileSend attch : attchs) {
					attch.setId(tabcontentsendbean.getId());
					saveTabContentFileSendInOASystem(attch);// 保存附件
				}
			}

			/** 将oa系统中发文数据的保留存根备份到公文传输系统中 */
			saveTabContentSendInTranSystem(tabcontentsendbean);// 备份发文数据
			if (mainContent != null) {// 备份发文正文数据
				saveTabContentFileSendInTranSystem(mainContent);
			}
			if (attchs != null && !attchs.isEmpty()) {// 备份发文附件数据
				for (TabContentFileSend attch : attchs) {
					saveTabContentFileSendInTranSystem(attch);
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

	/**
	 * 根据表T_OA_SENDDOC主键值获取对应的信息
	 * 
	 * @description
	 * @author 严建
	 * @param id
	 *            表T_OA_SENDDOC主键值
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * @createTime Jun 1, 2012 12:51:26 PM
	 */
	public TabContentSend getTabContentSendBySendDocId(TabContentSend bean)
			throws ServiceException, DAOException, SystemException {
		TabContentSend sendbean = new TabContentSend();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			// 标题,流水号,主题词,密级,紧急程度,发文号,创建人,发文时间,主送单位,抄送单位,签发人,收文时间
			String sql = "SELECT WORKFLOWTITLE,WORKFLOWCODE,SENDDOC_KEYWORDS,SENDDOC_SECRET_LVL,PERSON_CONFIG_FLAG"
					+ ",SENDDOC_CODE,WORKFLOWAUTHOR,SENDDOC_OFFICIAL_TIME,SENDDOC_SUBMITTO_DEPART,SENDDOC_CC_DEPART"
					+ ",SENDDOC_ISSUER,SENDDOC_RECV_TIME FROM T_OA_SENDDOC where SENDDOC_ID = '"
					+ bean.getSenddocId() + "'";
			Map map = sendDocManager.queryForMap(sql);
			/*
			 * <?xml version="1.0" encoding="GBK"?> <xmls> <xml> <docid
			 * des="编号">1</docid> <doctitle des="标题">11111111111111111111111111</doctitle>
			 * <docNoPre des="发文字号">123123123123</docNoPre> <yearNo
			 * des="年号">2012</yearNo> <waterNo des="流水号">1231</waterNo>
			 * <subject des="主题词"></subject> <creator des="创建人">user11</creator>
			 * <createdate des="创建时间">2012-05-29 16:10:52.127</createdate>
			 * <createUnitId des="创建单位">ceshixitong</createUnitId> <secret
			 * des="密级"></secret> <emergency des="紧急程度">紧急</emergency> <sendto
			 * des="主送单位"></sendto> <cc des="抄送单位"></cc> <publishperson
			 * des="签发人"></publishperson> <sendPerson des="发送人">user11</sendPerson>
			 * <sendTime des="发送时间">2012-05-29 16:18:51.937</sendTime>
			 * <DaochuUser des="导出人">cs1</DaochuUser> <DaochuTime
			 * des="导出时间">2012-05-29</DaochuTime> <files> <file /> </files>
			 * <files> <file fileName="测试申请表－2012-05-28－Strong_OA5.0.gd"
			 * filestate="2"> 测试申请表－2012-05-28－Strong_OA5.0.gd </file> </files>
			 * </xml> </xmls>
			 */
			Document document = DocumentHelper.createDocument();
			document.setXMLEncoding("gb2312");
			Element rootElement = document.addElement("xmls");
			Element xmlElement = rootElement.addElement("xml");
			Element e = null;
			// <docid des="编号">1</docid>
			e = xmlElement.addElement("docid");
			e.addAttribute("des", "编号");
			e.addText("1");
			// <doctitle des="标题">标题</doctitle>
			e = xmlElement.addElement("doctitle");
			e.addAttribute("des", "标题");
			String WORKFLOWTITLE = StringUtil.castString(map
					.get("WORKFLOWTITLE"));
			if (WORKFLOWTITLE != null) {
				e.addText(WORKFLOWTITLE);
			} else {
				e.addText("");
			}
			// <docNoPre des = "发文字号">测发</docNoPre>
			e = xmlElement.addElement("docNoPre");
			e.addAttribute("des", "发文文号");
			String SENDDOC_CODE = StringUtil
					.castString(map.get("SENDDOC_CODE"));
			if (SENDDOC_CODE != null) {
//				e.addText(SENDDOC_CODE);
				e.addText("");
			} else {
				e.addText("");
			}
			// <yearNo des = "年号">2010</ yearNo>
			e = xmlElement.addElement("yearNo ");
			e.addAttribute("des", "年号");
			e.addText(java.util.Calendar.getInstance().get(
					java.util.Calendar.YEAR)
					+ "");
			// <waterNo des = "流水号">1</waterNo>
			e = xmlElement.addElement("waterNo");
			e.addAttribute("des", "流水号");
			String WORKFLOWCODE = StringUtil
					.castString(map.get("WORKFLOWCODE"));
			if (WORKFLOWCODE != null) {
//				e.addText(WORKFLOWCODE);
				e.addText("");
			} else {
				e.addText("");
			}
			// <subject des="主题词">主题词</subject>
			e = xmlElement.addElement("subject");
			e.addAttribute("des", "主题词");
			String SENDDOC_KEYWORDS = StringUtil.castString(map
					.get("SENDDOC_KEYWORDS"));
			if (SENDDOC_KEYWORDS != null) {
				e.addText("");
//				e.addText(SENDDOC_KEYWORDS);
			} else {
				e.addText("");
			}
			// <creator des="创建人">张三</creator>
			e = xmlElement.addElement("creator");
			e.addAttribute("des", "创建人");
			String WORKFLOWAUTHOR = StringUtil.castString(map
					.get("WORKFLOWAUTHOR"));
			if (WORKFLOWAUTHOR != null) {
				e.addText("");
//				e.addText(userService.getUserNameByUserId(WORKFLOWAUTHOR));
			} else {
				e.addText("");
			}
			// <createdate des="创建时间">2010-1-5 9:42:38</createdate>
			e = xmlElement.addElement("createdate");
			e.addAttribute("des", "创建时间");
			java.sql.Timestamp SENDDOC_OFFICIAL_TIME = (java.sql.Timestamp) map
					.get("SENDDOC_OFFICIAL_TIME");
			if (SENDDOC_OFFICIAL_TIME != null) {
//				e.addText(sdf.format(new java.util.Date(SENDDOC_OFFICIAL_TIME.getTime())));
				e.addText("");
			} else {
				e.addText("");
			}
			// <createUnitId des="创建单位">北京市文物局</createUnitId><!—单位名称-->
			e = xmlElement.addElement("createUnitId");
			e.addAttribute("des", "创建单位");
			e.addText(unitName);
			// <secret des="密级">机密</secret>
			e = xmlElement.addElement("secret");
			e.addAttribute("des", "密级");
			String SENDDOC_SECRET_LVL = StringUtil.castString(map
					.get("SENDDOC_SECRET_LVL"));
			if (SENDDOC_SECRET_LVL != null) {
				e.addText(SENDDOC_SECRET_LVL);
			} else {
				e.addText("");
			}
			// <emergency des="紧急程度">急件</emergency>
			e = xmlElement.addElement("emergency");
			e.addAttribute("des", "紧急程度");
			String PERSON_CONFIG_FLAG = JJCDMap.get(StringUtil.castString(map
					.get("PERSON_CONFIG_FLAG") == null ? "0" : map
					.get("PERSON_CONFIG_FLAG")));
			if (PERSON_CONFIG_FLAG != null) {
				e.addText(PERSON_CONFIG_FLAG);
			} else {
				e.addText("");
			}
			// <sendto des="主送单位">主送单位名称</sendto>
			e = xmlElement.addElement("sendto");
			e.addAttribute("des", "主送单位");
			String SENDDOC_SUBMITTO_DEPART = StringUtil.castString(map
					.get("SENDDOC_SUBMITTO_DEPART"));
			if (SENDDOC_SUBMITTO_DEPART != null) {
				e.addText("");
//				e.addText(SENDDOC_SUBMITTO_DEPART);
			} else {
				e.addText("");
			}
			// <cc des="抄送单位">抄送单位</cc>
			e = xmlElement.addElement("cc");
			e.addAttribute("des", "抄送单位");
			String SENDDOC_CC_DEPART = StringUtil.castString(map
					.get("SENDDOC_CC_DEPART"));
			if (SENDDOC_CC_DEPART != null) {
				e.addText("");
//				e.addText(SENDDOC_CC_DEPART);
			} else {
				e.addText("");
			}
			// <publishperson des="签发人">李四</publishperson>
			e = xmlElement.addElement("publishperson");
			e.addAttribute("des", "签发人");
			String SENDDOC_ISSUER = StringUtil.castString(map
					.get("SENDDOC_ISSUER"));
			if (SENDDOC_ISSUER != null) {
				e.addText("");
//				e.addText(SENDDOC_ISSUER);
			} else {
				e.addText("");
			}
			// <sendPerson des="发送人">李四</sendPerson>
			e = xmlElement.addElement("sendPerson");
			e.addAttribute("des", "发送人");
			if (SENDDOC_ISSUER != null) {
				e.addText("");
//				e.addText(SENDDOC_ISSUER);
			} else {
				e.addText("");
			}
			// <sendTime des="发送时间">2010-1-5 9:42:38</sendTime>
			e = xmlElement.addElement("sendTime");
			e.addAttribute("des", "发送时间");
			if (SENDDOC_OFFICIAL_TIME != null) {
				e.addText("");
//				e.addText(sdf.format(new java.util.Date(SENDDOC_OFFICIAL_TIME.getTime())));
			} else {
				e.addText("");
			}
			// OA导出人
			e = xmlElement.addElement("OADaochuUser");
			e.addAttribute("des", "OA导出人");
			e.addText(userService.getCurrentUser().getUserName());
			// OA导出日期
			e = xmlElement.addElement("OADaochuTime ");
			e.addAttribute("des", "OA导出日期");
			e.addText(sdf.format(new Date()));
			// 设置正文信息
			Element mainfilesElement = xmlElement.addElement("files");
			Element mainfileElement = mainfilesElement.addElement("file");
			if (bean.getMainContent() != null) {
				TabContentFileSend mainContent = bean.getMainContent();
				mainfileElement.addAttribute("fileName", mainContent
						.getFileName());
				mainfileElement.addAttribute("filestate", "0");
				mainfileElement.addText(mainContent.getFileName());
			}
			// 设置附件信息
			if (bean.getAttachs() != null && !bean.getAttachs().isEmpty()) {
				List<TabContentFileSend> attachs = bean.getAttachs();
				for (TabContentFileSend attch : attachs) {
					Element attachsfilesElement = xmlElement
							.addElement("files");
					Element attachfilesElement = attachsfilesElement
							.addElement("file");
					attachfilesElement.addAttribute("fileName", attch
							.getFileName());
					attachfilesElement.addAttribute("filestate", "1");
					attachfilesElement.addText(attch.getFileName());
				}
			} else {
				Element attachsfilesElement = xmlElement.addElement("files");
				attachsfilesElement.addElement("file");
			}

			sendbean.setContent(document.asXML());// 设置发文内容
			sendbean.setCreateDate(new Date());// 设置发文创建时间
			sendbean.setFlag("0"); // 设置为未取走
			sendbean.setLockstatus("0");// 设置为未锁定
			sendbean.setUnitName(unitName);// 设置单位名称
			return sendbean;
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

	/**
	 * 根据oa的发文id获取该文的附件信息
	 * 
	 * @description
	 * @author 严建
	 * @param id
	 * @return
	 * @createTime May 30, 2012 10:13:31 AM
	 */
	public List<TabContentFileSend> getAttachsBySendDocId(String id)
			throws ServiceException, DAOException, SystemException {
		List<TabContentFileSend> attchs = null;
		try {
			List<WorkflowAttach> workflowattachs = workflowAttachManager
					.getWorkflowAttachsByDocId(id);
			if (workflowattachs != null && !workflowattachs.isEmpty()) {
				attchs = new LinkedList<TabContentFileSend>();
				for (WorkflowAttach workflowattach : workflowattachs) {
					TabContentFileSend attach = new TabContentFileSend();
					attach.setFileName(workflowattach.getAttachName());// 设置附件名称
					attach.setFileContent(workflowattach.getAttachContent());// 设置附件内容
					attach.setFileState("1");// 为附件
					attchs.add(attach);
				}
			}
			return attchs;
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

	/**
	 * 查询可以进行数据传输的收文数据
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * @createTime Jun 1, 2012 4:39:53 PM
	 */
	public Page<ToaSenddoc> query(Page<ToaSenddoc> page, ToaSenddoc model)
			throws ServiceException, DAOException, SystemException {
		try {
			initPageTotalCount(page, model);
			initPageResult(page, model);
			return page;
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

	/**
	 * 设置page记录总数
	 * 
	 * @description
	 * @author 严建
	 * @param page
	 * @param dbType
	 * @param dbPath
	 * @param dbName
	 * @param dbUser
	 * @param dbPass
	 * @param dbChar
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 * @createTime May 24, 2012 8:40:19 PM
	 */
	public void initPageTotalCount(Page<ToaSenddoc> page, ToaSenddoc model)
			throws DAOException, ServiceException, SystemException {
		Connection Conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Conn = sendDocManager.getCurrentConnection();
			// 算总记录数据
			String countsql = "SELECT count(SENDDOC_ID) AS TOTAL FROM T_OA_SENDDOC where WORKFLOWSTATE='2' AND FLAG='0'";
			ps = Conn.prepareStatement(countsql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();
			rs.last();
			int total = rs.getInt(1);
			page.setTotalCount(total);
		} catch (DAOException ex) {
			ex.printStackTrace();
			throw ex;
		} catch (ServiceException ex) {
			ex.printStackTrace();
			throw ex;
		} catch (SystemException ex) {
			ex.printStackTrace();
			throw ex;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SystemException(ex);
		} finally {
			DataBaseUtil.closeResultSet(rs);
			DataBaseUtil.closePreparedStatement(ps);
			// DataBaseUtil.closeConnecton(Conn);//hibnate控制连接，不需要关闭
		}
	}

	/**
	 * 初始化page的result
	 * 
	 * @description
	 * @author 严建
	 * @param page
	 * @param dbType
	 * @param dbPath
	 * @param dbName
	 * @param dbUser
	 * @param dbPass
	 * @param dbChar
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 * @createTime May 24, 2012 8:42:49 PM
	 */
	public void initPageResult(Page<ToaSenddoc> page, ToaSenddoc model)
			throws DAOException, ServiceException, SystemException {
		Connection Conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			List<ToaSenddoc> lis = new ArrayList<ToaSenddoc>();
			if (page.getTotalCount() > 0) {
				Conn = sendDocManager.getCurrentConnection();
				// 算总记录数据
				// 发文id,标题,流水号,主题词,密级,紧急程度,收文时间,流程名称
				String sql = "SELECT SENDDOC_ID,WORKFLOWTITLE,WORKFLOWCODE,SENDDOC_KEYWORDS,SENDDOC_SECRET_LVL,PERSON_CONFIG_FLAG,SENDDOC_RECV_TIME,WORKFLOWNAME  FROM T_OA_SENDDOC where WORKFLOWSTATE='2' AND FLAG='0'";
				ps = Conn.prepareStatement(sql,
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				ps.setMaxRows(page.getPageNo() * page.getPageSize());
				rs = ps.executeQuery();
				if (rs != null) {
					int pageSize = page.getPageSize();
					rs.last();// 定位到最后一行
					rs.absolute((page.getPageNo() - 1) * pageSize + 1);// 定位到当前页的第一项
					int i = 0;
					ToaSenddoc toasenddoc = null;
					while (i < pageSize && !rs.isAfterLast()) {
						toasenddoc = new ToaSenddoc();
						toasenddoc.setSenddocId(rs.getString("SENDDOC_ID")
								+ "#" + rs.getString("WORKFLOWNAME"));
						toasenddoc.setWORKFLOWTITLE(rs
								.getString("WORKFLOWTITLE") == null ? "" : rs
								.getString("WORKFLOWTITLE"));
						toasenddoc
								.setWORKFLOWCODE(rs.getString("WORKFLOWCODE") == null ? ""
										: rs.getString("WORKFLOWCODE"));
						toasenddoc.setSenddocKeywords(rs
								.getString("SENDDOC_KEYWORDS") == null ? ""
								: rs.getString("SENDDOC_KEYWORDS"));
						toasenddoc.setSenddocSecretLvl(rs
								.getString("SENDDOC_SECRET_LVL") == null ? ""
								: rs.getString("SENDDOC_SECRET_LVL"));
						toasenddoc.setSenddocRecvTime(((Date) rs
								.getObject("SENDDOC_RECV_TIME")));
						lis.add(toasenddoc);
						i++;
						rs.next();
					}
					page.setResult(lis);
				}
			}
		} catch (DAOException ex) {
			ex.printStackTrace();
			throw ex;
		} catch (ServiceException ex) {
			ex.printStackTrace();
			throw ex;
		} catch (SystemException ex) {
			ex.printStackTrace();
			throw ex;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SystemException(ex);
		} finally {
			DataBaseUtil.closeResultSet(rs);
			DataBaseUtil.closePreparedStatement(ps);
			//			DataBaseUtil.closeConnecton(Conn);//hibnate控制连接，不需要关闭
		}
	}
}
