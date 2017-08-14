package com.strongit.workflow.actorSettingInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.strongit.oa.common.user.IUserService;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.workflow.exception.WorkflowException;
import com.strongmvc.exception.SystemException;

/**
 * 选择下一步时只显示机构，不显示出人员。
 * 
 * @author 邓志城
 * @company Strongit Ltd. (C) copyright
 * @date Oct 28, 2011
 * @classpath com.strongit.workflow.actorSettingInterface.OrganizationSetting
 * @version 3.0.2
 * @email dengzc@strongit.com.cn
 * @tel 0791-8186916
 */
@Repository
public class OrganizationSetting extends AbstractActorSetting {

	@Autowired
	IUserService userService;

	/**
	 * 根据传入的处理人信息和任务实例Id解析出对应的人员信息集合
	 * 
	 * @author 喻斌
	 * @date Jun 17, 2010 2:17:12 AM
	 * @param actorInfos
	 *            -处理人信息集
	 * @param taskId
	 *            -任务实例Id，为“0”则表示流程尚未启动
	 * @return Map<String, Object[]>
	 *         人员信息集合，其中String为人员Id，Object[]数据格式为{(0)人员Id, (1)人员名称,
	 *         (2)组织机构Id,(3)人员排序号}
	 * @throws WorkflowException
	 */
	@Override
	public Map<String, Object[]> parseActors(Collection<String> actorInfos,
			String taskId) throws WorkflowException {
		super.prefix = "r";
		Map<String, Object[]> actorMap = new HashMap<String, Object[]>(0);
		if (actorInfos != null && !actorInfos.toString().equals("[null]")
				&& !actorInfos.isEmpty()) {
			// Map<String, List<User>> userMap = userService.getUserMap();
			String orgId;
			for (String actorInfo : actorInfos) {
				if (this.match(actorInfo)) {
					orgId = actorInfo.substring(1).split(",")[0];
					TUumsBaseOrg org = userService.getOrgInfoByOrgId(orgId);
					if (org == null) {
						throw new SystemException("orgId为'" + orgId
								+ "'的机构不存在！");
					}
					actorMap.put("@" + orgId, new String[] { "@" + orgId,
							org.getOrgName(), orgId,
							org.getOrgSequence().toString() });
				}
			}
		}
		return actorMap;
	}

}
