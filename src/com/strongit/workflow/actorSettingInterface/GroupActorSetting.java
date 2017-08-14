package com.strongit.workflow.actorSettingInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.workflow.exception.WorkflowException;
import com.strongmvc.exception.SystemException;

/**
 * 用户组解析类
 * 
 * @author 邓志城
 * @company Strongit Ltd. (C) copyright
 * @date 2011-1-13 下午07:21:33
 * @version 3.0
 * @classpath com.strongit.workflow.actorSettingInterface.GroupActorSetting
 * @comment
 * @email dengzc@strongit.com.cn
 */
@Service("groupActorSetting")
public class GroupActorSetting extends AbstractActorSetting {

	@Autowired
	IUserService userService;// 注入用户管理服务类对象.

	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 根据用户组选择人员 根据传入的处理人信息和任务实例Id解析出对应的人员信息集合
	 * 
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
		super.prefix = "h";
		Map<String, Object[]> actorMap = new HashMap<String, Object[]>(0);
		try {
			if (actorInfos != null && !actorInfos.toString().equals("[null]")
					&& !actorInfos.isEmpty()) {
				for (String actorInfo : actorInfos) {
					if (this.match(actorInfo)) {
						String groupId = actorInfo.substring(1).split(",")[0]; // 得到组id
						List<User> userList = userService
								.getUsersInfoByGroupId(groupId);
						for (User user : userList) {
							actorMap.put(user.getUserId(), new String[] {
									user.getUserId(), user.getUserName(),
									user.getOrgId(),
									user.getUserSequence().toString() });
						}
					}
				}
			}
		} catch (SystemException e) {
			logger.error("解析用户组人员时发生错误", e);
			e.printStackTrace();
		}
		return actorMap;
	}

}
