package com.strongit.oa.attendance.applyaudit;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaAttenApply;

import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional
public class ApplyauditManager {
	 private GenericDAOHibernate<ToaAttenApply, String> applyDao = null;
		
		public ApplyauditManager(){
			
		}
		
		@Autowired
		public void setSessionFactory(SessionFactory sessionFactory) {
			applyDao = new GenericDAOHibernate<ToaAttenApply, String>(
					sessionFactory, ToaAttenApply.class);	
		}

}
