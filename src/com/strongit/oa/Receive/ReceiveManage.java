package com.strongit.oa.Receive;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import com.strongit.oa.Receive.bo.DocAttach;
import com.strongit.oa.attachment.AttachmentHelper;
import com.strongit.oa.attachment.IWorkflowAttachService;
import com.strongit.oa.bo.RecvDocTrans;
import com.strongit.oa.bo.ToaLog;
import com.strongit.oa.bo.ToaRecvdoc;
import com.strongit.oa.bo.WorkflowAttach;
import com.strongit.oa.common.service.DataBaseUtil;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.mylog.MyLogManager;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.util.PathUtil;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date May 28, 2012 1:25:42 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.Receive.ReceiveManage
 */
@Service
@Transactional
public class ReceiveManage {

	private GenericDAOHibernate<RecvDocTrans, java.lang.String> recvDocTransDao;

	@Autowired
	protected IWorkflowAttachService workflowAttachManager; // 工作流附件管理

	@Autowired
	private IUserService userService;

	@Autowired
	private MyLogManager logService;

	private static String unitName;// 所属单位

	private static String recvprocessname;// 收文名称

	private static String dbName;// 数据库名称
	
	@Autowired
	private JdbcTemplate jdbcTemplate; // 提供Jdbc操作支持

	public static Map<String, String> JJCDMap = new HashMap<String, String>();

	static {
		unitName = DataSource.getDataSoureProperties().getProperty(
				"recvunitName");
		recvprocessname = DataSource.getDataSoureProperties().getProperty(
				"recvprocessname");
		dbName = DataSource.getDataSoureProperties().getProperty("dbName");
		JJCDMap.put("无", null);
		JJCDMap.put("紧急", "1");
		JJCDMap.put("紧急", "2");
		JJCDMap.put("特急", "3");
		JJCDMap.put("特急", "4");
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		recvDocTransDao = new GenericDAOHibernate<RecvDocTrans, String>(
				sessionFactory, RecvDocTrans.class);
	}

	/**
	 * 获取中间表数据库的连接
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @createTime May 30, 2012 10:56:27 AM
	 */
	private static Connection getConnecton() {
		return DataSource.getConnecton();
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
	public void initPageTotalCount(Page<ToaRecvdoc> page, ToaRecvdoc model)
			throws DAOException, ServiceException, SystemException {
		Connection Conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuilder datediff = new StringBuilder();
			if (model != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date = model.getRecvdocOfficialTime();
				if (date != null) {
					String dateString = sdf.format(date);
					datediff.append(" and datediff(day,createDate,'"
							+ dateString + "') = 0 ");
				}
			}
			// 查询数据
			// 获取数据源//数据库类型,数据库地址,数据库名,用户名,密码,字符集
			Conn = getConnecton();
			// 算总记录数据
			String unitNameSql = "";
			if (unitName != null && !"".equals(unitName)) {
				unitNameSql = " and unitName = '" + unitName + "' ";
			}
			String sql = "select count(id) from Tab_Content_Recive  where flag = 0 "
					+ unitNameSql + datediff.toString();
			ps = Conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
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
			DataBaseUtil.closeConnecton(Conn);
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
	public void initPageResult(Page<ToaRecvdoc> page, ToaRecvdoc model)
			throws DAOException, ServiceException, SystemException {
		Connection Conn1 = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		try {
			if (page.getTotalCount() > 0) {
				StringBuilder datediff = new StringBuilder();
				if (model != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date date = model.getRecvdocOfficialTime();
					if (date != null) {
						String dateString = sdf.format(date);
						datediff.append(" and datediff(day,createDate,'"
								+ dateString + "') = 0 ");
					}
				}
				String content = null;
				List<ToaRecvdoc> lis = new ArrayList<ToaRecvdoc>();
				Conn1 = getConnecton();
				String unitNameSql = "";
				if (unitName != null && !"".equals(unitName)) {
					unitNameSql = " and unitName = '" + unitName + "' ";
				}
				String sql1 = "select id, content,createDate from Tab_Content_Recive where flag = 0 "
						+ unitNameSql + datediff.toString();
				ps1 = Conn1.prepareStatement(sql1,
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				ps1.setMaxRows(page.getPageNo() * page.getPageSize());
				rs1 = ps1.executeQuery();
				if (rs1 != null) {
					int pageSize = page.getPageSize();
					rs1.last();// 定位到最后一行
					rs1.absolute((page.getPageNo() - 1) * pageSize + 1);// 定位到当前页的第一项
					int i = 0;
					ToaRecvdoc toaRecvdoc = null;
					while (i < pageSize && !rs1.isAfterLast()) {
						toaRecvdoc = new ToaRecvdoc();
						String recvDocId = String.valueOf(rs1.getInt("id"));
						content = rs1.getString("content");
						toaRecvdoc.setRecvDocId(recvDocId);
						// 创建时间
						Date date = (Date) rs1.getObject("createDate");
						toaRecvdoc.setRecvdocOfficialTime(date);
						if (!"".equals(content) || content != null) {
							StringReader sr = new StringReader(content);
							InputSource is = new InputSource(sr);
							DocumentBuilderFactory factory = DocumentBuilderFactory
									.newInstance();
							DocumentBuilder builder = factory
									.newDocumentBuilder();
							Document doc = builder.parse(is);
							// 标题
							NodeList doctitle = doc
									.getElementsByTagName("doctitle");
							Element e = (Element) doctitle.item(0);
							Node t = e.getFirstChild();
							if (t != null) {
								toaRecvdoc.setRecvdocTitle(t.getNodeValue());
							}

							// 文号
							NodeList docNoPre = doc
									.getElementsByTagName("docNoPre");
							Element e1 = (Element) docNoPre.item(0);
							Node t1 = e1.getFirstChild();
							if (t1 != null) {
								toaRecvdoc.setRecvdocCode(t1.getNodeValue());
							}
							// 缓急
							NodeList emergency = doc
									.getElementsByTagName("emergency");
							Element e2 = (Element) emergency.item(0);
							Node t2 = e2.getFirstChild();
							if (t2 != null) {
								toaRecvdoc.setRecvdocEmergency(t2
										.getNodeValue());
							}

							// 发送时间
							// SimpleDateFormat sdf = new SimpleDateFormat(
							// "yyyy-MM-dd hh:mm:ss");
							// NodeList sendTime = doc
							// .getElementsByTagName("sendTime");
							// Element e3 = (Element) sendTime.item(0);
							// Node t3 = e3.getFirstChild();
							// if (t3 != null) {
							// Date date = sdf.parse(t3.getNodeValue());
							// toaRecvdoc.setRecvdocOfficialTime(date);
							// }

							// 发文单位
							NodeList sendto = doc
									.getElementsByTagName("sendto");
							Element e4 = (Element) sendto.item(0);
							Node t4 = e4.getFirstChild();
							if (t4 != null) {
								toaRecvdoc.setRecvdocSubmittoDepart(t4
										.getNodeValue());
							}

						}
						lis.add(toaRecvdoc);
						i++;
						rs1.next();
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
			DataBaseUtil.closeResultSet(rs1);
			DataBaseUtil.closePreparedStatement(ps1);
			DataBaseUtil.closeConnecton(Conn1);
		}
	}

	/**
	 * 根据附件文件id获取附件名称和附件内容
	 * 
	 * @description
	 * @author 严建
	 * @param fileId
	 * @return
	 * @createTime May 25, 2012 9:47:44 AM
	 */
	public DocAttach getAttachByFileId(String fileId) {
		DocAttach docattach = null;
		Connection Conn1 = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		try {

			Conn1 = getConnecton();
			String sql1 = "select  fileName,fileContent from tab_content_file_recive where id = "
					+ fileId;
			ps1 = Conn1.prepareStatement(sql1,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs1 = ps1.executeQuery();
			rs1.last();
			int count = rs1.getRow();
			if (count > 0) {
				rs1.absolute(1);
				docattach = new DocAttach();

				String fileName = rs1.getString("fileName");
				docattach.setFileName(fileName);

				Object fileContentTemp = rs1.getObject("fileContent");
				if (fileContentTemp instanceof byte[]) {
					docattach.setFileContent((byte[]) fileContentTemp);
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
			DataBaseUtil.closeResultSet(rs1);
			DataBaseUtil.closePreparedStatement(ps1);
			DataBaseUtil.closeConnecton(Conn1);
		}
		return docattach;
	}

	/**
	 * 获取附件列表
	 * 
	 * @description
	 * @author 严建
	 * @param docid
	 *            书生提供的中间表中收文表的主键id
	 * @return
	 * @createTime May 25, 2012 11:17:56 AM
	 */
	public List<DocAttach> getAttachListByDocId(String docid) {
		List<DocAttach> lis1 = new ArrayList<DocAttach>();
		Connection Conn1 = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		try {

			Conn1 = getConnecton();
			String sql1 = "select  fileName,fileContent from tab_content_file_recive where fileId in ("
					+ docid + ")";
			ps1 = Conn1.prepareStatement(sql1,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs1 = ps1.executeQuery();

			if (rs1 != null) {
				DocAttach docattach = null;
				while (rs1.next()) {
					docattach = new DocAttach();

					String fileName = rs1.getString("fileName");// 设置附件名称
					docattach.setFileName(fileName);

					Object fileContentTemp = rs1.getObject("fileContent");// 设置附件内容
					if (fileContentTemp instanceof byte[]) {
						docattach.setFileContent((byte[]) fileContentTemp);
					}
					lis1.add(docattach);
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
			DataBaseUtil.closeResultSet(rs1);
			DataBaseUtil.closePreparedStatement(ps1);
			DataBaseUtil.closeConnecton(Conn1);
		}
		return lis1;
	}

	/**
	 * 根据公文id获取附件列表,包括文件id，文件名称及文件状态符
	 * 
	 * @description
	 * @author 严建
	 * @param docid
	 * @return
	 * @createTime May 25, 2012 9:20:29 AM
	 */
	public List<DocAttach> getSimpleAttachListByDocId(String docid) {
		List<DocAttach> lis1 = new ArrayList<DocAttach>();
		Connection Conn1 = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		try {

			Conn1 = getConnecton();
			String sql1 = "select id, fileName,fileState from tab_content_file_recive where fileId in ("
					+ docid + ")";
			ps1 = Conn1.prepareStatement(sql1,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs1 = ps1.executeQuery();

			if (rs1 != null) {
				DocAttach docattach = null;
				while (rs1.next()) {
					docattach = new DocAttach();
					String fileId = String.valueOf(rs1.getInt("id"));
					docattach.setFileId(Long.parseLong(fileId));

					String fileName = rs1.getString("fileName");
					docattach.setFileName(fileName);

					String fileState = rs1.getString("fileState");
					docattach.setFileState(fileState);
					lis1.add(docattach);
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
			DataBaseUtil.closeResultSet(rs1);
			DataBaseUtil.closePreparedStatement(ps1);
			DataBaseUtil.closeConnecton(Conn1);
		}
		return lis1;
	}

	/**
	 * Description: 获取数据
	 */
	public Page query(Page<ToaRecvdoc> page, ToaRecvdoc model)
			throws DAOException, ServiceException, SystemException {
		try {
			// 获取数据源//数据库类型,数据库地址,数据库名,用户名,密码,字符集
			initPageTotalCount(page, model);
			// 查询数据
			// 获取数据源//数据库类型,数据库地址,数据库名,用户名,密码,字符集
			initPageResult(page, model);
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
		}
		return page;
	}

	/**
	 * 保存收文数据到OA数据库
	 * 
	 * @description
	 * @author 严建
	 * @param recvDocId
	 *            收文id
	 * @return 返回保存到oa中该业务数据
	 * @createTime May 24, 2012 11:33:37 AM
	 */
	private RecvDocTrans saveRecvDocData(String recvDocId) {
		RecvDocTrans recvdoctrans = null;
		Connection Conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Conn = getConnecton();
			// 算总记录数据
			String sql = "select Tab_Content_Recive.content from Tab_Content_Recive where Tab_Content_Recive.id in ("
					+ recvDocId + ")";
			ps = Conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();
			rs.last();
			int count = rs.getRow();
			if (count > 0) {
				rs.absolute(1);
				String content = rs.getString("content");
				recvdoctrans = new RecvDocTrans();
				if (!"".equals(content) || content != null) {
					StringReader sr = new StringReader(content);
					InputSource is = new InputSource(sr);
					DocumentBuilderFactory factory = DocumentBuilderFactory
							.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document doc = builder.parse(is);
					NodeList nodelist = null;
					Element e = null;
					Node t = null;
					// 标题
					nodelist = doc.getElementsByTagName("doctitle");
					e = (Element) nodelist.item(0);
					t = e.getFirstChild();
					if (t != null) {
						recvdoctrans.setWorkflowtitle(t.getNodeValue());
					}

					// 流水号
					nodelist = doc.getElementsByTagName("waterNo");
					e = (Element) nodelist.item(0);
					t = e.getFirstChild();
					if (t != null) {
						recvdoctrans.setWorkflowcode(t.getNodeValue());
					}

					// 主题词
					nodelist = doc.getElementsByTagName("subject");
					e = (Element) nodelist.item(0);
					t = e.getFirstChild();
					if (t != null) {
						recvdoctrans.setKeywords(t.getNodeValue());
					}

					// 密级
					nodelist = doc.getElementsByTagName("secret");
					e = (Element) nodelist.item(0);
					t = e.getFirstChild();
					if (t != null) {
						recvdoctrans.setSecret_lvl(t.getNodeValue());
					}

					// 缓急
					nodelist = doc.getElementsByTagName("emergency");
					e = (Element) nodelist.item(0);
					t = e.getFirstChild();
					if (t != null) {
						recvdoctrans.setPerson_config_flag(JJCDMap.get(t
								.getNodeValue()));
					}
					// 发文号
					nodelist = doc.getElementsByTagName("docNoPre");
					e = (Element) nodelist.item(0);
					t = e.getFirstChild();
					if (t != null) {
						recvdoctrans.setDoc_number(t.getNodeValue());
					}

					// 设置文的作者
					recvdoctrans.setWorkflowauthor(userService.getCurrentUser()
							.getUserId());
					// 设置文对应的流程名称
					recvdoctrans.setWorkflowname(recvprocessname);
				}
				recvDocTransDao.save(recvdoctrans);

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
				log
						.setLogInfo("接受公文《" + recvdoctrans.getWorkflowname()
								+ "》成功");// 日志信息
				logService.saveObj(log);
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
			DataBaseUtil.closeConnecton(Conn);
		}
		return recvdoctrans;
	}

	/**
	 * 将书生中间表中指定id的数据标识为签收
	 * 
	 * @description
	 * @author 严建
	 * @param docId
	 * @createTime May 25, 2012 1:05:37 PM
	 */
	private void recved(String docId) {
		Connection Conn = null;
		PreparedStatement ps = null;
		try {
			Conn = getConnecton();
			String sql2 = "update [" + dbName
					+ "].[dbo].[tab_content_recive] set flag = 1 "
					+ " where id in (" + docId + ")";
			ps = Conn.prepareStatement(sql2, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.executeUpdate();
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
			DataBaseUtil.closePreparedStatement(ps);
			DataBaseUtil.closeConnecton(Conn);
		}

	}

	/**
	 * 保存附件到OA数据库
	 * 
	 * @description
	 * @author 严建
	 * @param docid
	 *            书生提供的中间表中收文表的主键id
	 * @param recvdoctrans
	 *            oa收文表
	 * @throws Exception 
	 * @createTime May 24, 2012 11:36:13 AM
	 */
	private void saveRecvDocAttach(String docid, RecvDocTrans recvdoctrans) throws Exception {
		try {
			List<DocAttach> list = getAttachListByDocId(docid);
			
			for (DocAttach docattach : list) {
				WorkflowAttach model = new WorkflowAttach();
				model.setAttachContent(docattach.getFileContent());
				model.setAttachName(docattach.getFileName());
				model.setDocId(recvdoctrans.getOarecvdocid());
				workflowAttachManager.saveWorkflowAttach(model);

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
				log.setLogInfo("接受附件《" + recvdoctrans.getWorkflowtitle() + "——"
						+ docattach.getFileName() + "》成功");// 日志信息
				logService.saveObj(log);
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
		}
	}

	/**
	 * Description: 保存数据
	 */
	public boolean saveDoc(String ids) throws DAOException, ServiceException,
			SystemException {
		boolean flag = true;
		try {
			String[] idarr = ids.split(",");
			for (String id : idarr) {
				RecvDocTrans recvdoctrans = saveRecvDocData(id);// 保存收文
				saveRecvDocAttach(id, recvdoctrans);// 保存附件
				
				try {
					List<DocAttach> list = getAttachListByDocId(id);
					DocAttach doccontent = list.get(0);
					WorkflowAttach modelcontent = new WorkflowAttach();
					modelcontent.setAttachName(doccontent.getFileName());
					modelcontent.setAttachContent(doccontent.getFileContent());
					String rootPath = PathUtil.getRootPath();// 得到工程根路径
					String uploadDir = "pdfFile";
					String dir = rootPath + uploadDir;
					File file = new File(dir);
					if (!file.exists()) {
						file.mkdir();
					}
					String attachName = modelcontent.getAttachName();
					String ext = attachName.substring(attachName.lastIndexOf(".") + 1);
					Long nowTime = System.currentTimeMillis();
					Random random = new Random();
					String randomNum = random.nextInt(9) + "" + random.nextInt(9) + ""
							+ random.nextInt(9) + "" + random.nextInt(9) + ""
							+ random.nextInt(9);
					String fileName = nowTime + randomNum+"." + ext;  // 通过当前格林威治时间和5位随机数参数新的文件名
					byte[] buf = modelcontent.getAttachContent();
					if (buf != null) {
						InputStream is = FileUtil.ByteArray2InputStream(buf);
						String path = AttachmentHelper.transformFile("pdfFile", is, fileName);
						System.out.println("生成附件：" + path);
					}
					recvdoctrans.setAdobe_pdf_name(fileName);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
		} catch (DAOException ex) {
			flag = false;
			ex.printStackTrace();
			throw ex;
		} catch (ServiceException ex) {
			flag = false;
			ex.printStackTrace();
			throw ex;
		} catch (SystemException ex) {
			flag = false;
			ex.printStackTrace();
			throw ex;
		} catch (Exception ex) {
			flag = false;
			ex.printStackTrace();
			throw new SystemException(ex);
		}
		return flag;
	}
	
	/**
	 * 更新已取出的收文状态
	 * 
	 * @description
	 * @author 严建
	 * @param ids
	 * @createTime May 30, 2012 1:51:57 PM
	 */
	public void recvedids(String ids) {
		try {
			String[] idarr = ids.split(",");
			for (String id : idarr) {
				recved(id);// 标识该文已取走
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
		}
	}
}
