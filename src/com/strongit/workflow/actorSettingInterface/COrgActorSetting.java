/**
 * 
 */
package com.strongit.workflow.actorSettingInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.workflow.exception.WorkflowException;

/**
 * @description 类描述
 * @className COrgActorSetting
 * @company Strongit Ltd. (C) copyright
 * @author 申仪玲(shenyl)
 * @email shenyl@strongit.com.cn
 * @created 2012-4-6 下午08:01:43
 * @version 5.0
 */
@Service("cOrgActtorSetting")
public class COrgActorSetting extends AbstractActorSetting {

	@Autowired
	IUserService userService;// 注入用户管理服务类对象.

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
		super.prefix = "c";
		Map<String, Object[]> actorMap = new HashMap<String, Object[]>(0);
		if (actorInfos != null && !actorInfos.toString().equals("[null]")
				&& !actorInfos.isEmpty()) {
			for (String actorInfo : actorInfos) {
				String orgId;
				String userId;
				if (this.match(actorInfo)) {
					orgId = actorInfo.substring(1).split(",")[0];
					userId = getUupInterface().getDepartmentManager(orgId);
					if (userId != null && !"".equals(userId)) {// 这里可能是多个人员
						String[] userIds = userId.split(",");
						String uid = userIds[0];
						if (uid.length() > 32) {
							uid = uid.substring(1);
						}
						User user = userService.getUserInfoByUserId(uid);
						if (user.getUserId() != null) {
							actorMap.put(user.getUserId(), new String[] {
									user.getUserId(),
									// user.getUserName(),
									"", orgId,
									user.getUserSequence().toString() });
						}
					}
				}
			}
		}
		return actorMap;
	}

}
