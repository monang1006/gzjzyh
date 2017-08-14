/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-13
 * Autour: zhangli
 * Version: V1.0
 * Description： 字典项树结构辅助类
 */
package com.strongit.oa.theme;

import java.io.IOException;

import com.strongit.oa.bo.ToaUumsBaseOperationPrivil;
import com.strongit.tag.web.tree.IDealTreeNode;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;

public class DealLeve2TreeNode extends IDealTreeNode {

	public String getTagNodeId() {
		// TODO 自动生成方法存根
		return ((ToaUumsBaseOperationPrivil) obj).getPrivilSyscode();
	}

	public String getTagNodeName() {
		// TODO 自动生成方法存根
		return ((ToaUumsBaseOperationPrivil) obj).getPrivilName();
	}

	public String getTagParentId() {
		// TODO 自动生成方法存根
		try {
			String privilRuleCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_PRIVIL);
			int size = Integer.parseInt(privilRuleCode.substring(0,1));
			String id = getTagNodeId();
			int length = id.length();
			int len = 0;
			int rule = 0;
			if(privilRuleCode!=null){
				for(int i=0;i<privilRuleCode.length();i++){
					rule = Integer.parseInt(privilRuleCode.substring(i,i+1));
					len = len+rule;
					if(len==length&&len-rule>size){
						if(id.substring(0,len-rule).length()==8){
							return "0";
						}
						return id.substring(0,len-rule);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "0";
	}

	public String getNodeImg() {
		// TODO 自动生成方法存根
		return ((ToaUumsBaseOperationPrivil) obj).getRest1();
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getClick() {
		// TODO Auto-generated method stub
		String attr = ((ToaUumsBaseOperationPrivil) obj).getPrivilAttribute();
		String name = ((ToaUumsBaseOperationPrivil) obj).getPrivilName();
		if(attr==null||attr.equals(getTagNodeId())) {
			return null;			
		}
		if(attr.indexOf("?")!=-1) {
			attr += "&privilSyscode="+((ToaUumsBaseOperationPrivil) obj).getPrivilSyscode();
		} else {
			attr += "?privilSyscode="+((ToaUumsBaseOperationPrivil) obj).getPrivilSyscode();
		}
		if(name.equals("自办文")){//如果code为00020012既自办文，那么跳转到theme-module.jsp中的gotozbw()，实现自办文流程表单的弹出。
			return "javascript:gotozbw()";
		}
		return "gotoPrivil(\""+attr+"\",\""+name+"\")";
	}

}
