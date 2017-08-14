package com.strongit.oa.bgt.adapterimp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.strongit.oa.adapter.WorkflowAdapter;
import com.strongit.oa.common.workflow.comparator.util.SortConst;
import com.strongit.oa.common.workflow.model.TaskBean;
import com.strongit.oa.component.jdbc.JdbcSplitService;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongmvc.orm.hibernate.Page;

public class WorkflowAdapterImpl implements WorkflowAdapter {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SendDocManager manager;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private JdbcSplitService jdbcSplitService;

	public Page loadProcessedData(Page page, String userId, TaskBean model,
			String tableName, String formColumns) {
		StringBuilder sqlQuery = new StringBuilder("select ");
		sqlQuery.append(formColumns).append(
				" from JBPM_PROCESSINSTANCE pi,T_JBPM_BUSINESS tjb ");
		if (tableName != null && tableName.length() > 0) {
			sqlQuery.append(",").append(tableName).append(" oatable ");
		}
		sqlQuery
				.append(" where pi.BUSINESS_ID = tjb.BUSINESS_ID and pi.business_id is not null ");
		if (tableName != null && tableName.length() > 0) {
			String pkFieldName = manager.getPrimaryKeyName(tableName);
			sqlQuery.append(" and pi.BUSINESS_ID = '").append(tableName)
					.append(";");
			sqlQuery.append(pkFieldName).append(";' || ").append("oatable.")
					.append(pkFieldName);
		}
		if (model.getWorkflowType() != null
				&& model.getWorkflowType().length() > 0) {
			sqlQuery.append(" and pi.TYPE_ID_ in(" + model.getWorkflowType()
					+ ")");
		}
		if (model.getExcludeWorkflowType() != null
				&& model.getExcludeWorkflowType().length() > 0) {
			sqlQuery.append(" and pi.TYPE_ID_ not in("
					+ model.getExcludeWorkflowType() + ")");
		}
		StringBuilder signSql = new StringBuilder();
		signSql
				.append(" and pi.ISSUSPENDED_ = 0 and exists(select ti.ID_ from JBPM_TASKINSTANCE ti,T_WF_BASE_NODESETTINGPLUGIN tnplugin where "
						+ " ti.ISOPEN_ = 0 and ti.NODE_ID_=tnplugin.nsp_nodeid and ti.END_ is not null and ti.ISCANCELLED_ = 0"
						+ " and ti.ACTORID_='"
						+ userId
						+ "'"
						+ " and ti.PROCINST_ = pi.ID_ and tnplugin.NSP_PLUGINCLOBVALUE not like 1"
						+ " and tnplugin.NSP_PLUGINNAME='plugins_chkModifySuggestion')");
		signSql
				.append(
						" and not exists(select ti.ID_ from JBPM_TASKINSTANCE ti where ti.ISOPEN_ = 1 ")
				.append("and ti.ACTORID_='").append(userId).append(
						"' and ti.PROCINST_ = pi.ID_) ");
		StringBuilder notsignSql = new StringBuilder();
		notsignSql
				.append(
						" and exists(select ti.ID_ from JBPM_TASKINSTANCE ti where ti.ISCANCELLED_ = 0 ")
				.append("and ti.ACTORID_='").append(userId).append(
						"' and ti.PROCINST_ = pi.ID_) ");

		StringBuilder yjzxSql = new StringBuilder(
				" and jbpmbusiness.BUSINESS_TYPE=0");

		StringBuilder querySign = new StringBuilder().append(sqlQuery).append(
				signSql);
		StringBuilder queryYjzx = new StringBuilder().append(sqlQuery).append(
				yjzxSql).append(notsignSql);
		querySign.append(" union ").append(queryYjzx);
		if(page.isAutoCount()) {
			String countSql = "select count(*) from (" + querySign.toString() + ")";
			logger.info(countSql);
			int count = jdbcTemplate.queryForInt(countSql);
			page.setTotalCount(count);			
		}
		if (SortConst.SORT_TYPE_TASKENDDATE_ASC.equals(model.getSortType())) {
			querySign.append(" order by START_ asc");
		} else if (SortConst.SORT_TYPE_PROCESSSTARTDATE_ASC.equals(model.getSortType())) {
			querySign.append(" order by START_ asc");
		} else if (SortConst.SORT_TYPE_PROCESSSTARTDATE_DESC.equals(model.getSortType())) {
			querySign.append(" order by START_ desc");
		} else {
			querySign.append(" order by START_ desc");
		}
		page.setAutoCount(false);
		return jdbcSplitService.excuteSqlForPage(page, querySign.toString(),null, null);
	}

}
