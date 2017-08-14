package com.strongit.oa.common.workflow.node;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.GWYJAttach;
import com.strongit.oa.bo.GWYJAttachs;
import com.strongit.oa.util.UUIDGenerator;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

//附件合并
@Service
@Transactional
@OALogger
public class MergerAttachmentsManager {
	
	private GenericDAOHibernate<GWYJAttach, java.lang.String> attachDao;
	private GenericDAOHibernate<GWYJAttachs, java.lang.String> attachsDao;
	@Autowired
	SessionFactory sessionFactory; // 提供session
	/**
	 * 注入SESSION工厂
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		attachDao = new GenericDAOHibernate<GWYJAttach, java.lang.String>(
				sessionFactory, GWYJAttach.class);
		attachsDao= new GenericDAOHibernate<GWYJAttachs, java.lang.String>(
				sessionFactory, GWYJAttachs.class);
	}
	public void registModel(String bussiness,String instanceId ) throws Exception {
		if (bussiness != null && !"".equals(bussiness)) {
			String[] bussinessIds = bussiness.split(";");
			String hql = "from GWYJAttach t where t.docid = ?";
			List<GWYJAttach> list = attachDao.find(hql, bussinessIds[2]);
			if(list!=null||list.size()>0){
				for(GWYJAttach t:list){
					PreparedStatement psmt = null;
					ResultSet rs = null;
					String uuid = (new UUIDGenerator().generate()).toString();
					String sql = "INSERT INTO t_docattach (DOCATTACHID,DOC_ID,PERSON_CONFIG_FLAG,PERSON_OPERATE_DATE,PERSON_OPERATER" +
							",ATTACH_NAME,ATTACH_CONTENT,ATTACH_PATH,WORKFLOWNAME,WORKFLOWSTATE,WORKFLOWAUTHOR,WORKFLOWCODE,WORKFLOWTITLE)"+ 
							"VALUES ( '"+t.getAttachId()+"','"+t.getDocid()+"','','','','"+t.getAccachName()+"','','"+t.getAttachPath()+
							"','','','','','')";
					psmt = sessionFactory.getCurrentSession().connection().prepareStatement(sql);
					rs = psmt.executeQuery();
				}
			}
		}
	}
}
