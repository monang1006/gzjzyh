/**
 * 人员选择配置信息机构信息解析类
 * @author 喻斌
 * @company Strongit Ltd. (c) copyright
 * @date Jun 17, 2010 2:30:03 AM
 * @version 1.0
 * @classpath com.strongit.workflow.actorSettingInterface.OrgActorSetting
 */
package com.strongit.workflow.actorSettingInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strongit.oa.common.user.IUserService;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.workflow.exception.WorkflowException;

@Service
public class OrgActorSetting extends AbstractActorSetting {

	// protected String prefix = "o";

	@Autowired
	IUserService userService;// 增加用户服务对象.解决查询性能问题。

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
		super.prefix = "o";
		Map<String, Object[]> actorMap = new HashMap<String, Object[]>(0);
		if (actorInfos != null && !actorInfos.toString().equals("[null]")
				&& !actorInfos.isEmpty()) {
			// Map<String, List<User>> userMap = userService.getUserMap();
			String orgId;
			for (String actorInfo : actorInfos) {
				if (this.match(actorInfo)) {
					orgId = actorInfo.substring(1).split(",")[0];
					// users = this.getUupInterface().getUserInfoByOrgId(orgId);
					List<TUumsBaseUser> users = userService
							.getUserListByOrgId(orgId);
					// List<User> users = userMap.get(orgId);
					if (users != null && !users.isEmpty()) {
						for (TUumsBaseUser user : users) {
							actorMap.put(user.getUserId(), new String[] {
									user.getUserId(), user.getUserName(),
									orgId, user.getUserSequence().toString() });
						}
					} else {
						actorMap.put("$" + orgId, new String[] { null, null,
								orgId, Long.MAX_VALUE + "" });
					}
				}
			}
		}
		return actorMap;
	}

}
