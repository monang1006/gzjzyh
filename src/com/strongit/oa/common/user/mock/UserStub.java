/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK:1.5.0_14; Struts:2.1.2; Spring:2.5.6; Hibernate:3.3.1.GA
 * Create Date: 2008-12-25
 * Autour: dengwenqiang
 * Version: V1.0
 * Description： 
 */
package com.strongit.oa.common.user.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

/**
 * 统一用户模拟类，该类用作模拟用户管理模块的接口实现 内部测试用
 * 
 * @author dengwenqiang
 * @version 1.0
 */
@Service
public class UserStub {
	private String fileName;
	private Document userTreeDocument;
	public final static String ORG_TAG = "organization";
	public final static String SUBORGS_TAG = "suborganizations";
	public final static String POSITIONS_TAG = "positions";
	public final static String POSITION_TAG = "position";
	public final static String USERS_TAG = "users";
	public final static String USER_TAG = "user";
	public final static String PROPS_TAG = "props";
	public final static String PROP_TAG = "prop";

	UserStub() {
		this.fileName = "user-tree.xml";
		String temp = this.getClass().getResource("").getPath() + fileName;
		this.loadXML(temp);
	}
	
	/**
	 * 构造函数
	 * @param filename UserStub所在目录中的文件
	 */
	public UserStub(String filename) {
		this.fileName = filename;
		String temp = this.getClass().getResource("").getPath() + filename;
		this.loadXML(temp);
	}

	/**
	 * 加载XML文件
	 * 
	 * @param fileName
	 *            完整的文件路径+文件名
	 */
	private void loadXML(String fileName) {
		SAXReader reader = new SAXReader();
		try {
			userTreeDocument = reader.read(fileName);
		} catch (DocumentException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取XML文件的根节点
	 * 
	 * @param doc
	 * @return
	 */
	private Element getRootElement() {
		return userTreeDocument.getRootElement();
	}

	/**
	 * 从指定元素递归查询标签列表
	 * 
	 * @param element
	 * @param tagName
	 * @param list
	 * @return
	 */
	private List<Element> recursiveGetElementsByTagName(Element element,
			String tagName, List<Element> list) {
		for (int i = 0, size = element.nodeCount(); i < size; i++) {
			Node node = element.node(i);
			if (node instanceof Element) {
				if (node.getName().equals(tagName)) {
					list.add((Element) node);
				}
				list = recursiveGetElementsByTagName((Element) node, tagName,
						list);
			}
		}

		return list;
	}

	/**
	 * 转换元素为Map
	 * 
	 * @param element
	 * @return Map<String, String> 
	 */
	private Map<String, String> elementPropsToMap(Element element) {
		List props = element.element(UserStub.PROPS_TAG).elements();
		Map<String, String> map = new HashMap<String, String>();
		for (Iterator i = props.iterator(); i.hasNext();) {
			Element prop = (Element) i.next();
			map.put(prop.attributeValue("key"), prop.getTextTrim());
		}
		
		return map;
	}
	
	/**
	 * 获取指定元素的props->prop元素的值
	 * @param element
	 * @param prop
	 * @return
	 */
	private String getElementPropertyValue(Element element, String prop) {
		// 如果元素中包含props标签节点，获取指定的属性值；否则直接跳出
		Iterator iterator = element.elementIterator(UserStub.PROPS_TAG);
		if (!iterator.hasNext()) {
			return null;
		}
		
		//获取指定的属性值
		Element props = element.element(UserStub.PROPS_TAG);
		String propValue = null;
		for (Iterator i = props.elements().iterator(); i.hasNext();) {
			Element propElement = (Element) i.next();
			String propKey = propElement.attributeValue("key");
			if (propKey.equals(prop)){
				propValue = propElement.getTextTrim();
				break;
			}
		}
		
		return propValue;
	}
	
	/**
	 * 根据标签名获取元素列表
	 * 
	 * @param tagName
	 */
	private List<Element> getElementsByTagName(String tagName) {
		Element root = this.getRootElement();
		List<Element> list = new ArrayList<Element>();
		if (root.getName().equals(tagName)) {
			list.add(root);
		}

		// 递归查找元素
		list = recursiveGetElementsByTagName(root, tagName, list);

		return list;
	}

	/**
	 * 根据标签,标签属性,标签属性值获取元素
	 * 
	 * @param tagName 标签名
	 * @param prop 属性名
	 * @param propValue 属性值
	 */
	private Element getElementByTagProperty(String tagName, String prop,
			String propValue) {
		List<Element> list = getElementsByTagName(tagName);
		Element ret = null;
		for (Iterator<Element> iterator = list.iterator(); iterator.hasNext();) {
			Element element = iterator.next();
			// 获取指定元素的值
			String value = getElementPropertyValue(element, prop);
			if (value == null) {
				break;
			}

			if (propValue.equals(value)) {
				ret = element;
				break;
			}
		}

		return ret;
	}
	
	/**
	 * 获取部门基本信息
	 * @param orgElement
	 * @return String[] 部门信息数组
	 * 记录形式：<br>
	 * [部门ID,上级部门ID,部门名称]
	 */
	private String[] getDepartmentProperties(Element orgElement) {
		//获取部门信息
		String[] organization = new String[3];
		Map<String, String> map = elementPropsToMap(orgElement);
		organization[0] = map.get("organization.id");
		organization[2] = map.get("organization.name");
		
		//获取上级部门ID
		Element parentOrg = orgElement.getParent().getParent();
		if (parentOrg == null) {
			organization[1] = "0";
		} else {
			organization[1] = getElementPropertyValue(parentOrg, "organization.id");
		}
		
		return organization;
	}
	
	/**
	 * 获取岗位基本信息
	 * @param positionElement
	 * @return String[] 岗位信息数组
	 * 记录形式：<br>
	 * [岗位ID,岗位名]
	 */
	private String[] getPositionProperties(Element positionElement) {
		//获取岗位信息
		String[] position = new String[2];
		Map<String, String> map = elementPropsToMap(positionElement);
		position[0] = map.get("position.id");
		position[1] = map.get("position.name");
		
		return position;
	}
	
	/**
	 * 获取用户基本信息
	 * @param userElement
	 * @return String[] 用户信息数组
	 * 记录形式：<br>
	 * [用户ID,用户登录名,用户名,密码,邮箱]
	 */
	private String[] getUserProperties(Element userElement) {
		//获取用户信息
		String[] user = new String[5];
		Map<String, String> map = elementPropsToMap(userElement);
		user[0] = map.get("user.id");
		user[1] = map.get("user.loginname");
		user[2] = map.get("user.name");		
		user[3] = map.get("user.password");
		user[4] = map.get("user.mail");
		
		return user;
	}
	
	/**
	 * 获取部门下的所有岗位
	 * @param orgElement
	 * @return List<String[]>
	 * 记录形式：<br>
	 * ArrayList{String[][岗位ID1,岗位名1],String[][岗位ID2,岗位名2]}]
	 */
	private List<String[]> getDepartmentPositions(Element orgElement) {
		List<String[]> list = new ArrayList<String[]>();
		List positionElements = orgElement.element(UserStub.POSITIONS_TAG).elements();		
		for (Iterator i = positionElements.iterator(); i.hasNext();) {
			Element positionElement = (Element)i.next();
			String[] position = getPositionProperties(positionElement);
			list.add(position);
		}
		
		return 	list;
	}
	
	/**
	 * 获取部门下的所有用户
	 * @param orgElement
	 * @return List<String[]> 
	 * 记录形式：<br>
	 * ArrayList{String[][用户ID1,用户名1],String[][用户ID2,用户名2]}]
	 */
	private List<String[]> getDepartmentUsers(Element orgElement) {
		List<String[]> list = new ArrayList<String[]>();
		
		//遍历岗位列表
		List positionElements = orgElement.element(UserStub.POSITIONS_TAG).elements();		
		for (Iterator i = positionElements.iterator(); i.hasNext();) {
			Element positionElement = (Element)i.next();
			
			//获取岗位下的用户
			List<String[]> userList = getPositionUsers(positionElement);			
			list.addAll(userList);			
		}
		
		return 	list;
	}
	
	/**
	 * 获取岗位下的所有用户
	 * @param positionElement
	 * @return List<String[]>
	 * 记录组织形式：<br>
	 * ArrayList{String[][用户ID1,用户名1],String[][用户ID2,用户名2]}
	 */
	private List<String[]> getPositionUsers(Element positionElement) {
		List<String[]> list = new ArrayList<String[]>();
		
		//遍历用户列表
		List userElements = positionElement.element(UserStub.USERS_TAG).elements();
		for (Iterator i = userElements.iterator(); i.hasNext();) {
			Element userElement = (Element)i.next();
			String[] user = getUserProperties(userElement);
			list.add(user);
		}
		
		return list;
	}
	
	/**
	 * 获取当前用户信息
	 * 
	 * @return String[] 用户信息 数组结构说明：<br>
	 *         [用户ID,用户登录名,用户名,密码,邮箱]
	 */
	public String[] getCurrentUser() {
		// 返回第一个用户的信息
		Element element = this.getElementByTagProperty(UserStub.USER_TAG,
				"user.id", "01001001");
		if (element == null) {
			return null;
		}
		String[] user = getUserProperties(element);
		
		return user;
	}

	/**
	 * 根据登录名获取用户信息
	 * 
	 * @param loginName
	 *            用户登录名
	 * @return String[] 用户信息 数组结构说明：<br>
	 *         [用户ID,用户登录名,用户名,密码,邮箱]
	 */
	public String[] getUserInfoByLoginName(String loginName) {
		Element element = this.getElementByTagProperty(UserStub.USER_TAG,
				"user.loginname", loginName);
		String[] user = getUserProperties(element);
		
		return user;
	}

	/**
	 * 根据用户ID获取用户信息
	 * 
	 * @param userId
	 *            用户ID
	 * @return String[] 用户信息 数组结构说明：<br>
	 *         [用户ID,用户登录名,用户名,密码,邮箱]
	 */
	public String[] getUserInfoByUserId(String userId) {
		Element element = this.getElementByTagProperty(UserStub.USER_TAG,
				"user.id", userId);
		String[] user = getUserProperties(element);
		
		return user;
	}

	/**
	 * 获取指定用户的用户名
	 * 
	 * @param userId
	 * @return String 用户名
	 */
	public String getUserNameByUserId(String userId) {
		Element element = this.getElementByTagProperty(UserStub.USER_TAG,
				"user.id", userId);
		String[] user = getUserProperties(element);
		return user[2];
	}
	
	/**
	 * 获取指定用户的岗位
	 * 
	 * @param userId 用户ID
	 * @return List 岗位信息集合 记录组织形式：<br>
	 *         ArrayList{String[][岗位ID1,岗位名1],String[][岗位ID2,岗位名2]}
	 */
	public List<String[]> getUserPositionsByUserId(String userId) {
		Element user = this.getElementByTagProperty(UserStub.USER_TAG,
				"user.id", userId);

		// 获取岗位元素
		Element position = user.getParent().getParent();		
		
		List<String[]> list = new ArrayList<String[]>();
		String[] positions = getPositionProperties(position);
		list.add(positions);

		return list;
	}
	
	/**
	 * 获取指定用户的岗位以及部门
	 * 
	 * @param userId 用户ID
	 * @return List 岗位信息集合 记录组织形式：<br>
	 *         ArrayList{String[][部门ID,岗位ID1,岗位名1],String[][部门ID,岗位ID2,岗位名2]}
	 */
	public List<String[]> getUserDepartmentAndPositionsByUserId(String userId) {
		Element user = this.getElementByTagProperty(UserStub.USER_TAG,
				"user.id", userId);

		// 获取岗位元素
		Element position = user.getParent().getParent();	
		
		//获取用户所在部门
		String[] department = getUserDepartmentByUserId(userId);
		
		List<String[]> list = new ArrayList<String[]>();
		String[] ret = new String[3];
		
		String[] positions = getPositionProperties(position);
		
		ret[0] = department[0]; //部门ID
		ret[1] = positions[0]; //岗位ID
		ret[2] = positions[1]; //岗位名称
		    
		
		list.add(ret);

		return list;
	}
	
	/**
	 * 获取指定用户的组织机构信息
	 * 
	 * @param userId
	 * @return String[] 组织机构信息数组 数组结构说明：<br>
	 *         [组织机构ID,组织机构名称]
	 */
	public String[] getUserDepartmentByUserId(String userId) {
		Element user = this.getElementByTagProperty(UserStub.USER_TAG,
				"user.id", userId);
		// 获取组织机构，user->users->position->positions>orgnaization
		Element organization = user.getParent().getParent().getParent()
				.getParent();
		
		String[] organizations = new String[2];
		Map<String, String> map = elementPropsToMap(organization);
		organizations[0] = map.get("organization.id");
		organizations[1] = map.get("organization.name");

		return organizations;
	}
	
	/**
	 * 根据岗位、部门获取用户列表
	 * 
	 * @param postId 岗位ID
	 * @param orgId 部门ID
	 * @return List<String[]> 用户信息集合
	 * 记录组织形式：<br>
	 * ArrayList{String[][用户ID1,用户名1],String[][用户ID2,用户名2]}
	 */
	public List<String[]> getUsersByPostitionAndDepartment(String postId,
			String orgId) {
		List<String[]> list = new ArrayList<String[]>();
		
		//获取部门
		Element org = this.getElementByTagProperty(UserStub.ORG_TAG,
				"organization.id", orgId);
		
		//获取部门下所有岗位
		List positionList = org.element(UserStub.POSITIONS_TAG).elements();
		for(Iterator i = positionList.iterator(); i.hasNext();) {
			Element positionElement = (Element)i.next();
			String positionId = getElementPropertyValue(positionElement, "position.id");
			if (positionId.equals(postId)) {
				list = getPositionUsers(positionElement);
				break;
			}
		}
		
		return list;
	}
	
	/**
	 * 获取指定部门的负责人ID
	 * @param orgId
	 * @return String 负责人ID
	 */
	public String getDepartmentManagerByOrgId(String orgId) {
		Element org = this.getElementByTagProperty(UserStub.ORG_TAG,
				"organization.id", orgId);
		
		//取部门下第一个用户为部门负责人
		List<String[]> userList = getDepartmentUsers(org);
		String[] user = (String[])userList.get(0);
		
		return user[0];
	}

	/**
	 * 获取指定部门的上级部门ID
	 * 
	 * @param orgId
	 * @return String 上级部门ID
	 */
	public String getParentDepartmentByOrgId(String orgId) {
		Element org = this.getElementByTagProperty(UserStub.ORG_TAG,
				"organization.id", orgId);
		//获取上级部门元素organization->suborganizations->organization
		Element parentOrg = org.getParent().getParent();
		
		//如果没有上级部门，直接返回本级机构
		if (parentOrg == null) {
			parentOrg = org;
		}

		return getElementPropertyValue(parentOrg, "organization.id");
	}

	/**
	 * 获取指定用户所在部门的负责人
	 * 
	 * @param userId 用户ID
	 * @return String 负责人ID
	 */
	public String getDepartmentManagerByUserId(String userId) {
		String[] org = getUserDepartmentByUserId(userId);
		String managerId = getDepartmentManagerByOrgId(org[0]);		
		return managerId;
	}

	/**
	 * 获取指定用户所在部门的上级部门负责人
	 * @param userId 用户ID
	 * @return String 负责人ID
	 */
	public String getParentDepartmentManagerByUserId(String userId) {
		//获取用户所在部门信息
		String[] org = getUserDepartmentByUserId(userId);
		
		//获取上级部门ID
		String parentOrgId = getParentDepartmentByOrgId(org[0]);
		
		//获取上级部门负责人
		String managerId = getDepartmentManagerByOrgId(parentOrgId);
		
		return managerId;
	}
	
	/**
	 * 获取所有部门以及部门下的部门
	 * 
	 * @return List 返回记录集合<br>
	 *         记录组织形式：<br>
	 *         ArrayList{<br>
	 *         String[][部门ID,上级部门ID,部门名称],//记录1<br>
	 *         String[][部门ID,上级部门ID,部门名称],//记录2<br> }
	 */
	public List<String[]> getAllDeparments() {
		List<String[]> list = new ArrayList<String[]>();
		
		List<Element> orgElements = getElementsByTagName(UserStub.ORG_TAG);
		for (Iterator i = orgElements.iterator(); i.hasNext();) {
			Element orgElement = (Element)i.next();
			//获取部门信息
			String[] organization = getDepartmentProperties(orgElement);			
			
			list.add(organization);
		}
		
		return list;
	}
	
	/**
	 * 获取所有部门以及部门下的用户
	 * 
	 * @return List 返回记录集合<br>
	 *         记录组织形式：<br>
	 *         ArrayList{<br>
	 *         Object[][部门ID,上级部门ID,部门名称,
	 *         ArrayList{String[][用户ID,用户名],String[][用户ID,用户名]}],//记录1<br>
	 *         Object[][部门ID,上级部门ID,部门名称,
	 *         ArrayList{String[][用户ID,用户名],String[][用户ID,用户名]}],//记录2<br> }
	 */
	public List<Object[]> getAllDeparmentsUsers() {
		List<Object[]> list = new ArrayList<Object[]>();
		List<Element> orgElements = getElementsByTagName(UserStub.ORG_TAG);
		for (Iterator i = orgElements.iterator(); i.hasNext();) {
			Element orgElement = (Element)i.next();
			Object[] object = new Object[4];
			
			//获取部门信息
			String[] organization = getDepartmentProperties(orgElement);
			object[0] = organization[0];
			object[1] = organization[1];
			object[2] = organization[2];
			
			//获取部门下的用户信息
			List<String[]> users = getDepartmentUsers(orgElement);
			object[3] = users;
			
			list.add(object);
		}
		
		return list;
	}

	/**
	 * 获取所有部门以及部门下的岗位
	 * 
	 * @return List 返回记录集合<br>
	 *         记录组织形式：<br>
	 *         ArrayList{<br>
	 *         Object[][部门ID,上级部门ID,部门名称,
	 *         ArrayList{String[][岗位ID,岗位名],String[][岗位ID,岗位名]}],//记录1<br>
	 *         Object[][部门ID,上级部门ID,部门名称,
	 *         ArrayList{String[][岗位ID,岗位名],String[][岗位ID,岗位名]}],//记录2<br> }
	 */
	public List<Object[]> getAllDeparmentsPositions() {
		List<Object[]> list = new ArrayList<Object[]>();
		
		//获取所有部门节点
		List<Element> orgElements = getElementsByTagName(UserStub.ORG_TAG);
		for (Iterator i = orgElements.iterator(); i.hasNext();) {
			Element orgElement = (Element)i.next();
			Object[] object = new Object[4];
			
			//获取部门信息
			String[] organization = getDepartmentProperties(orgElement);
			object[0] = organization[0];
			object[1] = organization[1];
			object[2] = organization[2];
			
			//获取部门下的岗位信息
			List<String[]> positionList = getDepartmentPositions(orgElement);
			object[3] = positionList;
			
			list.add(object);
		}
		
		return list;
	}
}
