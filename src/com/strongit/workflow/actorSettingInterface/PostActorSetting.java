/**
 * 人员选择配置信息岗位信息解析类
 * @author 喻斌
 * @company Strongit Ltd. (c) copyright
 * @date Jun 17, 2010 2:32:15 AM
 * @version 1.0
 * @classpath com.strongit.workflow.actorSettingInterface.PostActorSetting
 */
package com.strongit.workflow.actorSettingInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.oa.common.user.IUserService;
import com.strongit.workflow.exception.WorkflowException;

@Service
public class PostActorSetting extends AbstractActorSetting {

	@Autowired
	private IUserService userService;

	// protected String prefix = "p";

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
		super.prefix = "p";
		Map<String, Object[]> actorMap = new HashMap<String, Object[]>(0);
		if (actorInfos != null && !actorInfos.toString().equals("[null]")
				&& !actorInfos.isEmpty()) {
			String orgId;
			String[] st;
			String postId;
			for (String actorInfo : actorInfos) {
				if (this.match(actorInfo)) {
					st = actorInfo.substring(1).split(",")[0].split("\\$");
					postId = st[0];
					orgId = st[1];
					List<TUumsBaseUser> users = userService
							.getUserInfosByPostIdAndOrgId(postId, orgId, "1",
									"0");
					if (users != null && !users.isEmpty()) {
						for (TUumsBaseUser user : users) {
							actorMap.put(user.getUserId(), new String[] {
									user.getUserId(), user.getUserName(),
									orgId, user.getUserSequence().toString() });
						}
					}
				}
			}
		}
		return actorMap;
	}

}
