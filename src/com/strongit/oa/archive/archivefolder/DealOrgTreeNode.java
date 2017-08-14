/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-23
 * Autour: zhangli
 * Version: V1.0
 * Description： 字典树辅助类（用于案卷管理）
 */
package com.strongit.oa.archive.archivefolder;

import com.strongit.oa.util.TempPo;
import com.strongit.tag.web.tree.IDealTreeNode;

public class DealOrgTreeNode extends IDealTreeNode {

	public String getTagNodeId() {
		// TODO 自动生成方法存根
		return ((TempPo) obj).getId();
	}

	public String getTagNodeName() {
		// TODO 自动生成方法存根
		return ((TempPo) obj).getName();
	}

	public String getTagParentId() {
		// TODO 自动生成方法存根
		/*String syscode = ((Organization) obj).getOrgCode();
		String fcode = null;
		try {
			String orgRuleCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
			int plength = 0;
			int len = syscode.length();
			int i = 0;
			for(i=0;i<orgRuleCode.length();i++){
				plength = plength +Integer.parseInt(orgRuleCode.substring(i,i+1));
				if(plength==len)
					break;
			}
			if(i-1>=0&&i<len)
				fcode = syscode.substring(0,Integer.parseInt(orgRuleCode.substring(i-1,i)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return ((TempPo) obj).getParentId();
	}

	public String getNodeImg() {
		// TODO 自动生成方法存根
		return null;
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getClick() {
		// TODO Auto-generated method stub
		return null;
	}

}
