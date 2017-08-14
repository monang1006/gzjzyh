package com.strongit.oa.worklog;

import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.strongit.oa.bo.ToaWorkLog;
import com.strongit.oa.bo.ToaWorkLogAttach;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional
public class WorkLogAttachManager {

	private GenericDAOHibernate<ToaWorkLogAttach, java.lang.String> workLogAttachDao;

	@Autowired
	public void setSessionFactory(SessionFactory session) {
		workLogAttachDao = new GenericDAOHibernate<ToaWorkLogAttach, java.lang.String>(
				session, ToaWorkLogAttach.class);
	}

	/*
	 * 
	 * Description: 保存附件关联信息
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 23, 2010 9:29:00 PM
	 */
	@Transactional
	public String saveAttach(String attachId, ToaWorkLog toaWorkLog)
			throws SystemException, ServiceException {
		try {
			ToaWorkLogAttach attach = new ToaWorkLogAttach();
			attach.setAttachId(attachId);
			attach.setToaWorkLog(toaWorkLog);
			workLogAttachDao.save(attach);
			return attach.getWorkLogAttachId();
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "保存工作日志附件" });
		}
	}

	/*
	 * 
	 * Description: 删除日志附件关联表记录
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 24, 2010 2:14:34 PM
	 */
	public void delAttach(String attachId) throws SystemException,
			ServiceException {
		try {
			List l = workLogAttachDao.findByProperty("attachId", attachId);
			if (l.size() > 0) {
				ToaWorkLogAttach attach = (ToaWorkLogAttach) l.get(0);
				workLogAttachDao.delete(attach);
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.del_error,
					new Object[] { "删除工作日志附件" });
		}
	}
}
