package com.strongit.uums.security;

import java.io.IOException;

import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

import com.strongit.tag.web.tree.IDealTreeNode;
import com.strongit.uums.bo.TUumsBasePrivil;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongit.oa.common.user.util.SystemCodeHelper;
import com.strongit.oa.common.user.util.Const;

/**
 * 系统左侧模块树树形标签辅助类
 * @author       喻斌
 * @company      Strongit Ltd. (C) copyright
 * @date         Jan 6, 2009  7:06:49 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.uums.security.ModelDeal
 */
public class ModuleDeal extends IDealTreeNode {
	
	String abrolePrivilStr;
	
	public ModuleDeal(){
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        UserInfo user = (UserInfo)currentUser.getPrincipal();
        abrolePrivilStr = user.getAbrolePrivilStr();
	}

	@Override
	public String getNodeImg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTagNodeId() {
		return ((TUumsBasePrivil)obj).getPrivilSyscode();
		// TODO Auto-generated method stub
	}

	@Override
	public String getTagNodeName() {
		// TODO Auto-generated method stub
		return ((TUumsBasePrivil)obj).getPrivilName();
	}

	@Override
	public String getTagParentId() {
		// TODO Auto-generated method stub
		return this.getParentMethod();
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return ((TUumsBasePrivil)obj).getPrivilCode();
	}
	
	/**
	 * 根据系统编码、编码规则和默认上级编码得到上级编码
	 * @author  喻斌
	 * @date    Jan 6, 2009  6:57:15 PM
	 * @return  String 父级编码
	 */
	public String getParentMethod() {
			try {
				String parentCode = SystemCodeHelper.getFatherCode(((TUumsBasePrivil)obj).getPrivilSyscode()
						, PropertiesUtil.getCodeRule(Const.RULE_CODE_PRIVIL), "0");
				if("1".equals(((TUumsBasePrivil)obj).getRest4())){
					if(this.abrolePrivilStr.indexOf("-" + parentCode + ",") == -1){
						return "9999";
					}
				}
				return parentCode;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
	}

	@Override
	public String getClick() {
		// TODO 自动生成方法存根
		return null;
	}
}
