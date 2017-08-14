package com.strongit.oa.work.util;

import java.net.URLEncoder;

import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.TempPo;
import com.strongit.tag.web.tree.IDealTreeNode;
import com.strongmvc.exception.SystemException;

public class WorkDeal extends IDealTreeNode {
	
	@Override
	public String getClick() {
		return null;
	}

	@Override
	public String getNodeImg() {
		return null;
	}

	@Override
	public String getTagNodeId() {
		TempPo temp=(TempPo)this.obj;
		return temp.getId();
	}

	@Override
	public String getTagNodeName() {
		TempPo temp=(TempPo)this.obj;
		return temp.getName();
	}

	@Override
	public String getTagParentId() {
		TempPo temp=(TempPo)this.obj;
		return temp.getParentId();
	}

	/**
	 * 流程类型树展现在新建工作和展示待办、在办、主办列表时用到。
	 * 因为类型树是动态构建出来的。用到辅助类TempPo,其中通过类型来
	 * 标示树的展示用途。（1:展示表单；2:展示工作列表）
	 */
	@Override
	public String getUrl() {
		TempPo temp=(TempPo)this.obj;
		String type = temp.getType();
		if("form".equals(type)){
			String workflowTypeName = temp.getName();
			if(workflowTypeName == null || "".equals(workflowTypeName)){
				throw new SystemException("流程类型名称不能为空！");
			}
			try{
				workflowTypeName = URLEncoder.encode(workflowTypeName, "utf-8");
				workflowTypeName = URLEncoder.encode(workflowTypeName, "utf-8");
			}catch(Exception e){
				LogPrintStackUtil.logInfo("流程类型名字符转码出现异常！");
			}
			return "work/work!formList.action?workflowType="+getTagNodeId()+"&workflowTypeName="+workflowTypeName;
		}
		return "work/work.action?listMode="+type+"&workflowType="+getTagNodeId();
	}

}
