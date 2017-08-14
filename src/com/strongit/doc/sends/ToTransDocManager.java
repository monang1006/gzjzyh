package com.strongit.doc.sends;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.doc.bo.TtransDoc;
import com.strongit.doc.bo.TtransDocAttach;
import com.strongit.doc.sends.util.DocType;
import com.strongit.oa.attachment.IWorkflowAttachService;
import com.strongit.oa.attachment.WorkflowAttachManager;
import com.strongit.oa.bo.ToaReportBean;
import com.strongit.oa.bo.ToaSendDocRegist;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.bo.WorkflowAttach;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.manager.AdapterBaseWorkflowManager;
import com.strongit.oa.dict.IDictService;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.PathUtil;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.workflow.bo.TwfInfoApproveinfo;
import com.strongit.workflow.workflowInterface.ITaskService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
@OALogger
public class ToTransDocManager {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	GenericDAOHibernate<TtransDoc, String> docDao ;	//DAO处理类
	
	@Autowired IUserService userService ;						//统一用户服务类.
	
	@Autowired TransDocAttachManager attachManager ;
	@Autowired
	SessionFactory sessionFactory; // 提供session
	private List<ToaSysmanageDictitem> mmdjItems ;								//秘密等级字典项
	
	private List<ToaSysmanageDictitem> jjcdItems ;								//紧急程度字典项
	@Autowired IDictService dictService ;										//字典服务类
	
	@Autowired
	protected AdapterBaseWorkflowManager adapterBaseWorkflowManager;
	private IUserService userservice;
	@Autowired protected ITaskService iTaskService;//任务操作接口类
	@Autowired
	protected IWorkflowAttachService workflowAttachManager;
	/**
	 * 注入SESSION工厂
	 * @author:邓志城
	 * @date:2010-7-7 下午04:54:30
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		docDao = new GenericDAOHibernate<TtransDoc, java.lang.String>(sessionFactory, TtransDoc.class);
	}

	public void registModel(String bussiness, String instanceId) throws ServiceException, Exception{
		if (bussiness != null && !"".equals(bussiness)) {
			String[] bussinessIds = bussiness.split(";");
			PreparedStatement psmt = null;
			ResultSet rs = null;
			User user = userService.getCurrentUser();
			Organization org = userService.getDepartmentByOrgId(user.getOrgId());
			
			String orgName = org.getOrgName();
			String orgCode = org.getOrgCode();
			mmdjItems = dictService.getItemsByValue("MMDJ");//秘密等级
			jjcdItems = dictService.getItemsByValue("JJCD");//紧急程度
			
			try {
//				con.setAutoCommit(false);
				String sql = "select * from " + bussinessIds[0]
						+ " where  " + bussinessIds[1] + "='" + bussinessIds[2]
						+ "'";
				psmt = sessionFactory.getCurrentSession().connection().prepareStatement(sql);
				rs = psmt.executeQuery();
				TtransDoc model = new TtransDoc();
				String senddocId = null;
				if(rs.next()){
					model.setIsdelete("0");
					model.setDocState("2");//分发状态
					model.setDocTitle(rs.getString("WORKFLOWTITLE"));	//公文标题		
					model.setDocCode(rs.getString("SENDDOC_CODE"));		//公文文号
					/*^^^^^^^^^^^^^^^^^*/
//					model.setDocSubmittoDepart(rs.getString("SENDDOC_SUBMITTO_DEPART"));	//主送	
					model.setDocSubmittoDepart(rs.getString("SENDDOC_RECV_ENTERPRISE"));	//主送  单位	
					model.setDocSubmittoDepart_id(rs.getString("SENDDOC_RECV_ENTERPRISE_ID"));//主送单位Id
					
//					model.setDocCcDepart(rs.getString("SENDDOC_CC_DEPART"));			//抄送
					model.setDocRemark(rs.getString("SENDDOC_REMARK"));					//附件
//					model.setDocEntryTime(rs.getDate("SENDDOC_OFFICIAL_TIME"));//成文时间
					model.setDocOfficialTime(rs.getDate("SENDDOC_OFFICIAL_TIME"));
					model.setDocEntryTime(new Date());								//
					model.setDdocEntryPeople(user.getSupOrgCode());
//					model.setDocOfficialTime(rs.getDate("SENDDOC_OFFICIAL_TIME"));
	//				model.setDocOfficialTime(new Date());							//
					
					model.setDocprintSend(rs.getDate("SENDDOC_PRINT_TIME"));		//印发日期
					model.setDocContent((rs.getBytes(("SENDDOC_CONTENT"))));		//正文
					model.setDocKeywords(rs.getString("SENDDOC_KEYWORDS"));			//主送主题词
					model.setDocIssueDepartSigned("南昌市人民政府");							//录入人
					model.setRest3(org.getSupOrgCode());										//
					//jjcd
					for(Iterator it = jjcdItems.iterator(); it.hasNext();){
						ToaSysmanageDictitem toas = (ToaSysmanageDictitem) it.next();
						if("".equals(rs.getString("SENDDOC_EMERGENCY"))||null==rs.getString("SENDDOC_EMERGENCY")){
							model.setDocEmergency("");
							break;
						}
						if(rs.getString("SENDDOC_EMERGENCY").equals(toas.getDictItemValue())){
							model.setDocEmergency(toas.getDictItemName());
							break;
						}
					}
					//mmdj
//					for(Iterator it = mmdjItems.iterator(); it.hasNext();){
//						ToaSysmanageDictitem toas = (ToaSysmanageDictitem) it.next();
//						if(rs.getString("SENDDOC_SECRET_LVL").equals(toas.getDictItemName()));
//						model.setDocSecretLvl(toas.getDictItemValue());
//					}
//					model.setDocSecretLvl(rs.getString("SENDDOC_SECRET_LVL"));
					model.setDocSecretLvl("普通");
					senddocId = rs.getString("SENDDOC_ID");
				}
				docDao.save(model);
				
				//附件保存
				Set<TtransDocAttach> set = new HashSet<TtransDocAttach>();
				List<WorkflowAttach> workflowAttach = workflowAttachManager.getWorkflowAttachsByDocId(senddocId);
				if(workflowAttach!=null||workflowAttach.size()>0){
					for(WorkflowAttach w:workflowAttach){
						TtransDocAttach t = new TtransDocAttach();
						t.setTtransDoc(model);
						t.setAttachFileName(w.getAttachName());
						/**
						 * 屏蔽草稿文件到公文传输
						 */
						if(w.getAttachName().equals("草稿.doc"))
						{
							continue;
						}
						String rootPath = PathUtil.getRootPath();//得到工程根路径
						 File file = new File(rootPath+"transdoc"+File.separatorChar);
						  //判断文件夹是否存在,如果不存在则创建文件夹
						  if (!file.exists()) {
						   file.mkdir();
						  }
						  //取得公文附件
						  String path = rootPath+"WEB-INF"+File.separatorChar+"classes"+File.separatorChar+"attachments"+File.separatorChar;
						  File fs = new File(path+w.getAttachPath());
						  //创建要拷贝到的公文传输附件
						  String ext = w.getAttachName().substring(w.getAttachName().lastIndexOf(".")+1);
						  File fr = new File(rootPath+"transdoc"+File.separatorChar+w.getAttachPath()+"."+ext);
						  if(!fr.exists()){
							  fr.createNewFile();
						  }
						  //拷贝一份
						  FileUtils.copyFile(fs, fr);
						  
						t.setAttachFilePath("transdoc"+File.separatorChar+w.getAttachPath()+"."+ext);
						t.setAttachFileData(new Date());
						t.setFileServer("0.0.0.0");
//						String ext = w.getAttachName().substring(w.getAttachName().lastIndexOf(".")+1);
						t.setAttachFileType(ext);
						t.setIs(new ByteArrayInputStream(w.getAttachContent()));
						attachManager.save(t);
						set.add(t);
					}
					model.setObj(set);
				}
			} catch (SQLException e) {
//				try {
//					con.rollback();
//				} catch (SQLException e1) {
//					e1.printStackTrace();
//				}
				e.printStackTrace();
			}finally{
				try{
					if(rs != null){
						rs.close();
					}
					if (psmt != null) {
						psmt.close();
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
	}
}
