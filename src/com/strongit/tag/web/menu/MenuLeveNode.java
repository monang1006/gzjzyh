/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-13
 * Autour: zhangli
 * Version: V1.0
 * Description： 字典项树结构辅助类
 */
package com.strongit.tag.web.menu;

import java.io.IOException;

import com.strongit.oa.bo.ToaUumsBaseOperationPrivil;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;

public class MenuLeveNode extends ImenuNode {

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
						return id.substring(0,len-rule);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
		String code = ((ToaUumsBaseOperationPrivil) obj).getPrivilSyscode();
		String hasPriv = ((ToaUumsBaseOperationPrivil) obj).getRest4();
//		if(attr==null||attr.equals(getTagNodeId()))
//			return null;
		if(code.length()!= 8){
			return null;
		}
		if(code.equals("00020012")){		
			return "javascript:gotozbw()";//如果code为00020012既自办文，那么跳转到theme-bgt-btn.jsp中的gotozbw()，实现自办文流程表单的弹出。BY:刘皙
		}
		if(code.equals("00020016")){//角色对应的资源权限代码已在资源权限中设置
			return "javascript:gotoOrgwjcx()";//如果code为00020016既厅领导，那么跳转到theme-bgt-btn.jsp中的gotoOrgwjcx()，实现“文件监控与统计”页面的弹出。BY:刘皙
		}
		if(code.equals("00020017")){
			return "javascript:gotoDeptwjcx()";//如果code为00020017既处长，那么跳转到theme-bgt-btn.jsp中的gotoDeptwjcx()，实现“文件监控与统计”页面的弹出。BY:刘皙
		}
		if(code.equals("00020018")){
			return "javascript:gotoUserwjcx()";//如果code为00020018既普通用户，那么跳转到theme-bgt-btn.jsp中的gotoUserwjcx()，实现“文件监控与统计”页面的弹出。BY:刘皙
		}
		if("false".equals(hasPriv)){
			return "javascript:;";
		}
		
		return "javascript:gotonavigates(\'"+code+"\',\'"+name+"\')";
	}
}
