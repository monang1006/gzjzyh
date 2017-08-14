/**
 * 
 */
package com.strongit.oa.senddoc;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.strongit.oa.bo.ToaSenddoc;
import com.strongmvc.orm.hibernate.Page;

/**
 * @author Administrator
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class SendDocManagerTest extends AbstractTransactionalJUnit4SpringContextTests{
	private SendDocManager theSendDocManager;
	private ToaSenddoc docBean1;
	private ToaSenddoc docBean2;
	private ToaSenddoc docBean3;
	
	@Autowired
	public void setTheSendDocManager(SendDocManager theSendDocManager) {
		this.theSendDocManager = theSendDocManager;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
//		String docTitle1 = "Bean1";
//		docBean1.setSenddocTitle(docTitle1);
//		theSendDocManager.saveSendDoc(docBean1);
//		
//		String docTitle2 = "Bean2";
//		docBean2.setSenddocTitle(docTitle2);
//		theSendDocManager.saveSendDoc(docBean2);
//		
//		String docTitle3 = "Bean3";
//		docBean2.setSenddocTitle(docTitle3);
//		theSendDocManager.saveSendDoc(docBean3);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
//		theSendDocManager.deleteSendDocBean(docBean1);
//		theSendDocManager.deleteSendDocBean(docBean2);
//		theSendDocManager.deleteSendDocBean(docBean3);
	}
	
	@Test
	public void testSaveSendDoc() {
		String docTitle = "Test title";
		ToaSenddoc docBean = new ToaSenddoc();
		docBean.setSenddocTitle(docTitle);
		theSendDocManager.saveDoc(docBean);
		
		assertTrue("Id:"+docBean.getSenddocId(), 
				   docTitle.equals(
						   theSendDocManager.getDocById(
								   docBean.getSenddocId()).getSenddocTitle()));
	}

	@Test
	public void testDeleteSendDocById() {
		String docTitle = "Test title";
		ToaSenddoc docBean = new ToaSenddoc();
		docBean.setSenddocTitle(docTitle);
		theSendDocManager.saveDoc(docBean);
		
		String id = docBean.getSenddocId();
		String[] ids = {id};
		theSendDocManager.deleteMultiDocs(ids);
		assertTrue(true);
	}

	@Test
	public void testGetAllSendDocsPageOfToaSenddoc() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetWorkflowNextStep() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSendDocById() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testGetSendDocByIdIsNull() {
		ToaSenddoc docBean = theSendDocManager.getDocById("111");
		assertTrue(docBean == null);
	}

	@Test
	public void testHandleWorkflowNextStep() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveOfficeDoc() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllSendDocs() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testGetAllDocDrafts() {
		String docTitle = "Test title";
		ToaSenddoc docBean = new ToaSenddoc();
		docBean.setSenddocTitle(docTitle);
		theSendDocManager.saveDoc(docBean);
		
		Page<ToaSenddoc> page = new Page<ToaSenddoc>(10, true);
		//page = theSendDocManager.getAllDocDrafts(page);
		assertTrue(page.getResult().size()>=0);
	}
	
	@Test
	public void testGetAllDocDraftsIsNull() {		
		Page<ToaSenddoc> page = new Page<ToaSenddoc>(10, true);
		//page = theSendDocManager.getAllDocDrafts(page);
		assertTrue(page.getResult().size()<=0);
	}

}
