package com.strongit.oa.recvdoc;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionSupport;
import com.strongit.oa.bo.ToaRecvdoc;
import com.strongit.oa.senddoc.SendDocAction;

public class RecvDocActionTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPrepareModel() throws Exception{
		
		//fail("Not yet implemented");
	}

	@Test
	public void testRecvDocAction() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetPage() {
		//fail("Not yet implemented");
	}

	@Test
	public void testSetRecvDocId() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetRecvDocList() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetModel() {
		//fail("Not yet implemented");
	}

	@Test
	public void testSetModel() {
		//fail("Not yet implemented");
	}

	@Test
	public void testSetRecvDocManager() {
		//fail("Not yet implemented");
	}

	@Test
	public void testSetUserService() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetForm() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetFormRelativeProcess() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetSecondNodeActor() {
		//fail("Not yet implemented");
	}

	@Test
	public void testSaveandstartProcess() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetPrepareModel() throws Exception {
		
		//fail("Not yet implemented");
	}

	@Test
	public void testGetTodoTasks() {
		//fail("Not yet implemented");
	}

	@Test
	public void testHasbeenforthings() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetNodeInfo() {
		//fail("Not yet implemented");
	}

	@Test
	public void testSearchNextTransitions() {
		//fail("Not yet implemented");
	}

	@Test
	public void testForwardProcess() {
		//fail("Not yet implemented");
	}

	@Test
	public void testViewRecvDoc() {
		//fail("Not yet implemented");
	}

	@Test
	public void testList() {
		//fail("Not yet implemented");
	}

	@Test
	public void testSave() {
		//fail("Not yet implemented");
	}

	@Test
	public void testDelete() {
		//fail("Not yet implemented");
	}

	@Test
	public void testInput() throws Exception{
		SendDocAction action = new SendDocAction();
		String result = action.execute();
		assertTrue(ActionSupport.INPUT.equals(result));
		//fail("Not yet implemented");
	}

}
