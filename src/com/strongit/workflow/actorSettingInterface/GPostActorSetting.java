package com.strongit.workflow.actorSettingInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.workflow.exception.WorkflowException;

/**
 * 全局岗位人员选择解析类.
 * 
 * @author 邓志城
 * @company Strongit Ltd. (C) copyright
 * @date 2010-7-15 下午04:53:05
 * @version 2.0.2.3
 * @classpath com.strongit.workflow.actorSettingInterface.GPostActorSetting
 * @comment
 * @email dengzc@strongit.com.cn
 */
@Service("gPostActorSetting")
public class GPostActorSetting extends AbstractActorSetting {

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
		super.prefix = "g";
		Map<String, Object[]> actorMap = new HashMap<String, Object[]>(0);
		if (actorInfos != null && !actorInfos.toString().equals("[null]")
				&& !actorInfos.isEmpty()) {
			for (String actorInfo : actorInfos) {
				if (this.match(actorInfo)) {
					String postId = actorInfo.substring(1).split(",")[0]; // 得到岗位id
					List<User> userList = userService
							.getUsersByPositionId(postId);// 得到岗位下的人员列表
					for (User user : userList) {
						actorMap.put(user.getUserId(), new String[] {
								user.getUserId(), user.getUserName(),
								user.getOrgId(),
								user.getUserSequence().toString() });
					}
				}
			}
		}
		return actorMap;
	}

}
