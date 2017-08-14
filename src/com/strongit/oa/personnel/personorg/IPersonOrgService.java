package com.strongit.oa.personnel.personorg;

import java.util.List;

import com.strongit.oa.bo.ToaBaseOrg;

public interface IPersonOrgService {

	/**
	 * 根据机构名称查询
	 * @param orgname
	 * @return
	 */
   List<ToaBaseOrg>	getOrgByName(String orgname);
}
