package com.strongit.oa.senddoc.query.bo;

import java.util.List;

import com.strongit.oa.util.NodeNameConst;


/**
 * 
 * @author       严建
 * @company      Strongit Ltd. (C) copyright
 * @date         Apr 26, 2012 3:37:58 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.oa.senddoc.query.bo.NodeNameQuery
 */
public class NodeNameQuery {
	private String processTypeId;

	private String nodeName;

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getProcessTypeId() {
		return processTypeId;
	}

	public void setProcessTypeId(String processTypeId) {
		this.processTypeId = processTypeId;
	}

	@Deprecated
	public String getSQLString() {
		StringBuilder str = new StringBuilder();
//		str
//				.append(" exists (")
//				.append(" select NODENAMEQUERYTI.id_ ")
//				.append(" from ")
//				.append(" JBPM_PROCESSINSTANCE NODENAMEQUERYPI, ")
//				.append(" JBPM_TASKINSTANCE    NODENAMEQUERYTI, ")
//				.append(" T_JBPM_BUSINESS      NODENAMEQUERYJBPMBUSINESS ")
//				.append(" where ")
//				.append(" 1 = 1 ")
//				.append(" and NODENAMEQUERYTI.ISOPEN_ = 1 ")
//				.append(" and NODENAMEQUERYTI.PROCINST_ = NODENAMEQUERYPI.ID_ ")
//				.append(" and NODENAMEQUERYPI.ISSUSPENDED_ = '0' ")
//				.append(" and NODENAMEQUERYPI.business_id is not null ")
//				.append(
//						" AND NODENAMEQUERYPI.BUSINESS_ID = NODENAMEQUERYJBPMBUSINESS.BUSINESS_ID ")
//				.append(
//						processTypeIdIsNull() ? " and NODENAMEQUERYPI.type_id_ in ("
//								+ this.processTypeId + ") "
//								: " ")
//				.append(
//						nodeNameIsNull() ? " and NODENAMEQUERYTI.node_name_ like '"
//								+ this.nodeName + "' "
//								: " ").append(
//						" and @processInstanceId = NODENAMEQUERYTI.procinst_ ")
//				.append(") ");
		return str.toString();
	}

	public String getCustomFromItem() {
		StringBuilder str = new StringBuilder();
		StringBuilder selectItem = new StringBuilder();
		StringBuilder formItem = new StringBuilder();
		StringBuilder whereItem = new StringBuilder();
		selectItem.append(" NODENAMEQUERYPI.ID_ as NODENAMEQUERYPID, ")
			.append(" NODENAMEQUERYTI.NODE_NAME_ as NODENAMEQUERYNODENAME  ");	
		formItem.append(" JBPM_PROCESSINSTANCE NODENAMEQUERYPI, ")
			.append(" JBPM_TASKINSTANCE    NODENAMEQUERYTI ");
		whereItem.append(" 1=1 ")
			.append(" and NODENAMEQUERYTI.PROCINST_ = NODENAMEQUERYPI.ID_ ")
			.append(
						nodeNameIsNull() ? (this.nodeName
								.equals(NodeNameConst.BAN_JIE) ? " and NODENAMEQUERYPI.END_ is not null "
								: " and NODENAMEQUERYPI.END_ is  null ")
								: " ");
		StringBuilder maxwhereItem = new StringBuilder(" 1=1 AND NODENAMEQUERYTI.ISCANCELLED_ = 0");
		StringBuilder maxformItem = new StringBuilder(" JBPM_TASKINSTANCE    NODENAMEQUERYTI ");
		StringBuilder maxselectItem = new StringBuilder()
						.append(" max(NODENAMEQUERYTI.ID_) ");
		StringBuilder maxgroupByItem = new StringBuilder()
						.append("  NODENAMEQUERYTI.procinst_");
		StringBuilder maxstr = new StringBuilder()
                        		.append(" select  ").append(maxselectItem)
                        		.append(" from ").append(maxformItem)
                        		.append(" where ").append(maxwhereItem)
                        		.append(" group by  ").append(maxgroupByItem);
		whereItem.append(" and NODENAMEQUERYTI.id_ in ( ")
				
				.append(maxstr)
				.append(" ) ")
				.append(
					nodeNameIsNull() && !this.nodeName.equals(NodeNameConst.BAN_JIE)?initNodeNameQuery():" ");
		str.append("  (")
			.append(" select  ").append(selectItem)
			.append(" from ").append(formItem)
			.append(" where ").append(whereItem)
			.append(") ")
			.append(" NODENAMEQUERY");
			
//		str
//				.append("  (")
//				.append(" select  ")
//				.append(
//						"  distinct NODENAMEQUERYPI.ID_ as NODENAMEQUERYPID,  NODENAMEQUERYTI.NODE_NAME_ as NODENAMEQUERYNODENAME ")
//				.append(" from ")
//				.append(" JBPM_PROCESSINSTANCE NODENAMEQUERYPI, ")
//				.append(" JBPM_TASKINSTANCE    NODENAMEQUERYTI, ")
//				.append(" T_JBPM_BUSINESS      NODENAMEQUERYJBPMBUSINESS ")
//				.append(" where ")
//				.append(" 1 = 1 ")
////				.append(" and NODENAMEQUERYTI.ISOPEN_ = 1 ")
//				.append(" and NODENAMEQUERYTI.PROCINST_ = NODENAMEQUERYPI.ID_ ")
//				.append(" and NODENAMEQUERYPI.ISSUSPENDED_ = '0' ")
//				.append(" and NODENAMEQUERYPI.business_id is not null ")
//				.append(
//						" AND NODENAMEQUERYPI.BUSINESS_ID = NODENAMEQUERYJBPMBUSINESS.BUSINESS_ID ")
//				.append(
//						processTypeIdIsNull() ? " and NODENAMEQUERYPI.type_id_ in ("
//								+ this.processTypeId + ") "
//								: " ")
//				.append(
//						nodeNameIsNull() ? " and NODENAMEQUERYTI.node_name_ like '"
//								+ this.nodeName + "' "
//								: " ").append(
//						" and NODENAMEQUERYPI.ID_ = NODENAMEQUERYTI.procinst_ ")
//				.append(") ")
//				.append(" NODENAMEQUERY");
		return str.toString();
	}
	
	public String getCustomSelectItem() {
		StringBuilder str = new StringBuilder();
		str.append(" NODENAMEQUERY.NODENAMEQUERYNODENAME ");
		return str.toString();
	}
	/**
	 * 初始化节点名称查询
	 * 
	 * @author 严建
	 * @return
	 * @createTime Jul 2, 2012 11:09:14 AM
	 */
	public String initNodeNameQuery(){
		List<String> list = NodeNameConst.getNodeName(this.nodeName);
		if(list == null || list.isEmpty()){
			return " ";
		}else{
			StringBuilder query = new StringBuilder(" and (");
			for (int i = 0; i < list.size(); i++) {
				if(i>0){
					query.append(" or ");
				}
				String str = list.get(i);
				query.append("  NODENAMEQUERYTI.node_name_ ='"+str+"' ");
			}
			query.append(" ) ");
			return query.toString();
		}
	}
	public String getCustomQuery() {
		StringBuilder str = new StringBuilder();
		str.append(" @processInstanceId=NODENAMEQUERY.NODENAMEQUERYPID ");
		return str.toString();
	}

	protected boolean nodeNameIsNull() {
		if (this.nodeName != null && !"".equals(this.nodeName)) {
			return true;
		} else {
			return false;
		}
	}

	protected boolean processTypeIdIsNull() {
		if (this.processTypeId != null && !"".equals(this.processTypeId)) {
			return true;
		} else {
			return false;
		}
	}
}
