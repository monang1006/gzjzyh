package com.strongit.oa.personnel.personorg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaBaseOrg;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

@Service
@Transactional
public class PersonOrgService implements IPersonOrgService {

	private PersonOrgManager manager;
	@Autowired
	public void setManager(PersonOrgManager manager) {
		this.manager = manager;
	}
	/*
	 * 根据机构名称查询机构信息
	 * (non-Javadoc)
	 * @see com.strongit.oa.personnel.personorg.IPersonOrgService#getOrgByName(java.lang.String)
	 */
	public List<ToaBaseOrg> getOrgByName(String orgname) throws SystemException,ServiceException{
		try {
			return manager.getOrgByName(orgname);
		} catch (RuntimeException e) {
			throw new ServiceException(MessagesConst.save_error,
				    new Object[]{"根据机构名称查询出错！"});
		}
	}

}
