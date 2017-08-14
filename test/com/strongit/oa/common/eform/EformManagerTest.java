package com.strongit.oa.common.eform;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.strongit.oa.common.eform.service.EFormService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class EformManagerTest extends AbstractTransactionalJUnit4SpringContextTests {

	private String formId ;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired SessionFactory sessionFactory ;
	
	public Connection getConnection() {
		return sessionFactory.openSession().connection();
	}
	
	@Test public void getFormTemplateComponents() throws Exception {
		String sql = "select * from t_oa_senddoc";
		Connection con = this.getConnection();
		DatabaseMetaData meta = con.getMetaData();
		ResultSet rs = meta.getPrimaryKeys(null, null, "T_OA_SENDDOC");
		/*if(rs.next()){
				Object obj = rs.getObject(4);
				if(obj!=null){
					logger.info(obj.toString());
				}else{
					System.out.println("null"+obj);
				}
				System.out.println("********" + 4 + "----" + obj); 
		}*/
		String pk = "";
		while(rs.next()){
			pk = rs.getString("COLUMN_NAME");
			System.out.println("*****"+pk);
		}
	}
	
	@Test public void testListSet(){
		List<String> list = new ArrayList<String>();
		list.add("aaa");
		list.add("bbb");
		list.add("ccc");
		list.remove("bbb");
		list.add(0, "bbb");
		System.out.println(list);
	}
}
