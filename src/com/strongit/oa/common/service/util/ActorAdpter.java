package com.strongit.oa.common.service.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;

/**
 * 适配选择人员,如果是机构则要翻译成人员方式
 * @author 		邓志城
 * @company  	Strongit Ltd. (C) copyright
 * @date   		Oct 28, 2011
 * @classpath	com.strongit.oa.common.service.util.ActorAdpter
 * @version  	3.0.2
 * @email		dengzc@strongit.com.cn
 * @tel			0791-8186916
 */
@Repository
public class ActorAdpter implements IActorAdpter {

	@Autowired
	private IUserService userService;
	
	/**
	 * 适配人员选择信息
	 * return 数据结构：402882a03248c89901324bef1e8f0042|979910,数据2........
	 *  
	 * @see com.strongit.oa.common.service.util.IActorAdpter#getActors(java.lang.String)
	 */
	public String getActors(String taskActors) {
		if(taskActors != null && taskActors.length() > 0 && taskActors.startsWith("@")) {
			String[] strTaskActorArray = taskActors.split(",");
			StringBuilder ret = new StringBuilder();
			for(String actor : strTaskActorArray) {
				String orgIdInfo = actor.substring(1);
				String[] orgIdInfos = orgIdInfo.split("\\|");
				String orgId = orgIdInfos[0];
				String nodeId = orgIdInfos[1];//得到节点id
				List<User> userList = userService.getUsersByOrgID(orgId);
				for(User u : userList) {
					ret.append(u.getUserId()).append("|").append(nodeId).append(",");
				}
			}
			if(ret.length() > 0) {
				ret.deleteCharAt(ret.length() - 1);
			}
			taskActors = ret.toString();
		}
		return taskActors;
	}
	
}
