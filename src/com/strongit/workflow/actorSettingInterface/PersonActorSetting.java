/**
 * 人员选择配置信息人员信息解析类
 * @author 喻斌
 * @company Strongit Ltd. (c) copyright
 * @date Jun 17, 2010 2:33:13 AM
 * @version 1.0
 * @classpath com.strongit.workflow.actorSettingInterface.PersonActorSetting
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

@Service
public class PersonActorSetting extends AbstractActorSetting {

	// protected String prefix = "u";

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
		Map<String, Object[]> actorMap = new HashMap<String, Object[]>(0);
		super.prefix = "u";
		if (actorInfos != null && !actorInfos.toString().equals("[null]")
				&& !actorInfos.isEmpty()) {
			User users;
			String[] st;
			String userId;
			Map<String, User> userMap = null;
			/*
			 * if(actorInfos.size() > 5){//大小超过5时执行Map查找 List<User> userLst =
			 * userService.getAllUserInfo(); userMap = new HashMap<String,
			 * User>(); for(User user : userLst){ userMap.put(user.getUserId(),
			 * user); } if(!userLst.isEmpty()){ userLst.clear(); } }
			 */
			for (String actorInfo : actorInfos) {
				if (this.match(actorInfo)) {
					st = actorInfo.substring(1).split(",")[0].split("\\$");
					userId = st[0];
					if (userMap != null) {
						User user = userMap.get(userId);
						if (user != null) {
							actorMap.put(user.getUserId(), new String[] {
									user.getUserId(), user.getUserName(),
									user.getOrgId(),
									user.getUserSequence().toString() });
						}
					} else {
						users = userService.getUserInfoByUserId(userId);
						if (users != null) {
							if ("1".equals(users.getUserIsactive())
									&& "0".equals(users.getUserIsdel())) {// 启动并且是未删除的人员
								actorMap.put(users.getUserId(), new String[] {
										users.getUserId(), users.getUserName(),
										users.getOrgId(),
										users.getUserSequence().toString() });
							}
						}
					}
				}
			}
			if (userMap != null && !userMap.isEmpty()) {
				userMap.clear();
			}
		}
		return actorMap;
	}
}
