package com.strongit.oa.common.service.parameter;

import java.util.Date;

import com.strongit.oa.common.workflow.parameter.Parameter;

/**
 * 扩展实体信息（用于向后兼容需要添加的参数）
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Apr 17, 2012 3:11:49 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.common.service.parameter.OtherParameter
 */
public class OtherParameter extends Parameter{
    /**
     * @field processTimer 流程期限值
     */
    private Date processTimer;
    /**
     * 获取流程期限值
     * 
     * @description
     * @author 严建
     * @return
     * @createTime Apr 17, 2012 3:15:30 PM
     */
    public Date getProcessTimer() {
	return processTimer;
    }

    /**设置流程期限值
     * 
     * @description
     * @author 严建
     * @param processTimer
     * @createTime Apr 17, 2012 3:15:43 PM
     */
    public void setProcessTimer(Date processTimer) {
	this.processTimer = processTimer;
    }
}
