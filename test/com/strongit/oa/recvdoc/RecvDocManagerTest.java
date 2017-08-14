package com.strongit.oa.recvdoc;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.strongit.oa.bo.ToaRecvdoc;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class RecvDocManagerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRecvDocManager() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetWorkflowService() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetEformService() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetSessionFactory() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRecvDocList() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteRecvDoc() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRecvDoc() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveRecvDoc() {
		ToaRecvdoc model = new ToaRecvdoc();
		RecvDocManager manager=new RecvDocManager();
		//manager.saveRecvDoc(model);
		System.out.println(model.getRecvDocId());
		//fail("Not yet implemented");
	}

	@Test
	public void testGetForm() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFormRelativeProcess() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSecondNodeActor() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveandstartProcess() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTodoTasks() {
		fail("Not yet implemented");
	}

	@Test
	public void testHasbeenforthings() {
		fail("Not yet implemented");
	}

	@Test
	public void testSearchNextTransitions() {
		fail("Not yet implemented");
	}

	@Test
	public void testForwardProcess() {
		fail("Not yet implemented");
	}

}
