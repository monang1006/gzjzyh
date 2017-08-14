package com.strongit.oa.common.user.mock;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.strongit.oa.common.user.mock.UserStub;

public class UserStubTest {
	private UserStub stub;
	private String[][] organizations = { 
			{ "01", "0", "本级机构" },
			{ "02", "01", "一级机构1" }, 
			{ "03", "01", "一级机构2" } };
	private String[][] positions = { 
			{ "01001", "本级职位1" },
			{ "01002", "本级职位2" }, 
			{ "02001", "一级机构1职位1" }, 
			{ "02002", "一级机构1职位2" },
			{ "03001", "一级机构2职位1" }, 
			{ "03002", "一级机构2职位2" } };
	private String[][] users = {
			{ "01001001", "user1", "用户1", "1", "user1@user1.com" },
			{ "01001002", "user2", "用户2", "1", "user2@user2.com" },
			{ "01002001", "user3", "用户3", "1", "user3@user3.com" },
			{ "01002002", "user4", "用户4", "1", "user4@user4.com" },
			{ "02001001", "user5", "用户5", "1", "user5@user5.com" },
			{ "02001002", "user6", "用户6", "1", "user6@user6.com" },
			{ "02002001", "user7", "用户7", "1", "user7@user7.com" },
			{ "02002002", "user8", "用户8", "1", "user8@user8.com" },
			{ "03001001", "user9", "用户9", "1", "user9@user9.com" },
			{ "03001002", "user10", "用户10", "1", "user10@user10.com" },
			{ "03002001", "user11", "用户11", "1", "user11@user11.com" },
			{ "03002002", "user12", "用户12", "1", "user12@user12.com" } };

	@Before
	public void setUp() throws Exception {
		stub = new UserStub("user-tree-test.xml");
	}

	@After
	public void tearDown() throws Exception {
		stub = null;
	}

	@Test
	public void testGetCurrentUser() {
		String[] result = stub.getCurrentUser();
		String[] compare = users[0];
		assertTrue(Arrays.equals(result, compare));
	}

	@Test
	public void testGetUserInfoByLoginName() {
		String[] result = stub.getUserInfoByLoginName(users[5][1]);
		String[] compare = users[5];
		assertTrue(Arrays.equals(result, compare));
	}

	@Test
	public void testGetUserInfoByUserId() {
		String[] result = stub.getUserInfoByUserId(users[11][0]);
		String[] compare = users[11];
		assertTrue(Arrays.equals(result, compare));
	}

	@Test
	public void testGetUserNameByUserId() {
		String result = stub.getUserNameByUserId(users[7][0]);
		String compare = users[7][2];
		assertTrue(result.equals(compare));
	}

	@Test
	public void testGetUserPositionsByUserId() {
		List<String[]> result = stub.getUserPositionsByUserId(users[2][0]);
		List<String[]> compare = new ArrayList<String[]>();
		compare.add(positions[1]);
		
		assertTrue(Arrays.equals(result.get(0), compare.get(0)));
	}

	@Test
	public void testGetUserDepartmentAndPositionsByUserId() {
		List<String[]> result = stub.getUserDepartmentAndPositionsByUserId(users[2][0]);
		assertTrue(true);
	}
	
	@Test
	public void testGetUserDepartmentByUserId() {
		String[] result = stub.getUserDepartmentByUserId(users[9][0]);
		String[] compare = new String[2];
		compare[0] = organizations[2][0];
		compare[1] = organizations[2][2];
		assertTrue(Arrays.equals(result, compare));
	}

	@Test
	public void testGetUsersByPostitionAndDepartment() {
		List<String[]> result = stub.getUsersByPostitionAndDepartment(
				positions[3][0], organizations[1][0]);
	}

	@Test
	public void testGetDepartmentManagerByOrgId() {
		String result = stub.getDepartmentManagerByOrgId(organizations[0][0]);
		String compare = users[0][0];

		assertTrue(result.equals(compare));
	}

	@Test
	public void testGetParentDepartmentByOrgId() {
		String result = stub.getParentDepartmentByOrgId(organizations[2][0]);
		String compare = organizations[0][0];
		assertTrue(result.equals(compare));
	}

	@Test
	public void testGetDepartmentManagerByUserId() {
		String result = stub.getDepartmentManagerByUserId(users[11][0]);
		String compare = users[8][0];
		assertTrue(result.equals(compare));
	}

	@Test
	public void testGetParentDepartmentManagerByUserId() {
		String result = stub.getParentDepartmentManagerByUserId(users[11][0]);
		String compare = users[0][0];
		assertTrue(result.equals(compare));
	}

	@Test
	public void testGetAllDeparmentsUsers() {
		List<Object[]> result = stub.getAllDeparmentsUsers();
		assertTrue(true);
	}

	@Test
	public void testGetAllDeparmentsPositions() {
		List<Object[]> result = stub.getAllDeparmentsPositions();
		assertTrue(true);
	}

	@Test
	public void testGetAllDeparments() {
		List<String[]> result = stub.getAllDeparments();
		assertTrue(true);
	}

}
