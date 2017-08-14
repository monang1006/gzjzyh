package com.strongit.workflow.workflowDesign.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.strongit.oa.util.TempPo;

/**
 * 构造流程设计选择人员树形结构
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2011-6-14 下午04:12:02
 * @version  3.0
 * @classpath com.strongit.workflow.workflowDesign.util.GenerateSelectTree
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class GenerateSelectTree {

	/**
	 * 构造人员选择树形结构
	 * @author:邓志城
	 * @date:2011-6-14 下午03:55:14
	 * @param orgList
	 * * 	<p>信息列表数据结构：<br>
	 * 		<p>Object[]{组织机构Id, 父级组织机构Id, 组织机构名称, 组织机构下人员信息}<br>
	 * 		<p>最顶级组织机构父级Id为”0“<br>
	 * 		<p>人员信息数据机构：<br>
	 * 		<p>userList<String[]{人员Id, 人员名称}>
	 * @return List<TempPo>
	 */
	@SuppressWarnings("unchecked")
	public static List<TempPo> generateOrgUserTree(List<Object[]> orgList,String flag) {
		List<TempPo> trees = new LinkedList<TempPo>();
		if(orgList != null && !orgList.isEmpty()) {
			Set<String> orgIds = new HashSet<String>();
			for(Object[] objs : orgList) {
				orgIds.add(objs[0].toString());
			}
			for(Object[] objs : orgList) {
				String parentId = (String)objs[1];
				TempPo tree = new TempPo();
				tree.setId(new StringBuilder("g").append(objs[0].toString()).toString());
				if(parentId != null && !orgIds.contains(parentId)) {
					parentId = "0";
				}
				if("0".equals(parentId)) {
					tree.setParentId(new StringBuilder(parentId).toString());
				} else {
					tree.setParentId(new StringBuilder("g").append(parentId).toString());					
				}
				tree.setName(objs[2].toString());
				//tree.setType("o");
				tree.setCodeId("o");
				trees.add(tree);
				List<String[]> userList = (List<String[]>)objs[3];
				if(userList != null && !userList.isEmpty()) {
					for(String[] user : userList) {
						TempPo u = new TempPo();
						if("pos".equals(flag.trim())){
							u.setId(new StringBuilder("p").append(user[0]).append("$").append(objs[0].toString()).toString());
							u.setName(user[1]);
							u.setParentId(tree.getId());
							//u.setType("u");
							u.setCodeId("p");
						}else{
						    u.setId(new StringBuilder("u").append(user[0]).append("$").append(objs[0].toString()).toString());
						    u.setName(user[1]);
						    u.setParentId(tree.getId());
						    //u.setType("u");
						    u.setCodeId("u");
						}
						trees.add(u);
					}
				}
			}
		}
		return trees;
	}
}
