/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：词语过滤管理实现类
 */

package com.strongit.oa.updatebadwords.phrasefilter;

import java.util.List;
import java.util.StringTokenizer;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.strongit.oa.bo.ToaSysmanageFiltrateModule;
import com.strongit.oa.updatebadwords.phrasemanage.IPhraseManage;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * ����ģ��service
 * 
 * @author pengxq
 * @version 1.0
 */
@Service
@Transactional
public class PhraseFilterManage implements IPhraseFilterManage {
	private IPhraseManage phraseManage;

	private GenericDAOHibernate<ToaSysmanageFiltrateModule, java.lang.String> phraseFilterDao;

	private final String open = "0";

	private final String close = "1";

	/**
	 * @roseuid 493E2D8A029F
	 */
	public PhraseFilterManage() {

	}

	/**
	 * @param sessionFactory
	 * @roseuid 493E292F02BF
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		phraseFilterDao = new GenericDAOHibernate<ToaSysmanageFiltrateModule, java.lang.String>(
				sessionFactory, ToaSysmanageFiltrateModule.class);
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-4下午09:12:53
	 * @desc: 保存过滤对象
	 * @param ToaSysmanageFiltrateModule
	 *            filterModle 过滤对象
	 * @return void
	 */
	public String save(ToaSysmanageFiltrateModule filterModle)
			throws SystemException, ServiceException {
		String msg="";
		try {
			phraseFilterDao.save(filterModle);
		} catch (ServiceException e) {
			msg="开关设置失败！";
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "词语过滤对象" });
		}
		return msg;
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-4下午09:12:53
	 * @desc: 根据主键获取过滤对象
	 * @param String
	 *            id 主键
	 * @return ToaSysmanageFiltrateModule 过滤对象
	 */
	public ToaSysmanageFiltrateModule get(String id) throws SystemException,
			ServiceException {
		ToaSysmanageFiltrateModule obj = null;
		try {
			obj = phraseFilterDao.get(id);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "词语过滤对象" });
		}
		return obj;
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-4下午09:12:53
	 * @desc: 获取分页列表
	 * @param Page
	 *            page 分页对象
	 * @return Page 分页列表
	 */
	@Transactional(readOnly = true)
	public Page<ToaSysmanageFiltrateModule> getAll(Page page)
			throws SystemException, ServiceException {
		try {
			page = phraseFilterDao.findAll(page);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "词语过滤分页列表" });
		}
		return page;
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-4下午09:12:53
	 * @desc: 过滤信息内容
	 * @param String
	 *            beforeReplace 信息内容
	 * @param String
	 *            modleId 模块id
	 * @return String 过滤后的信息内容
	 */
	public String filterPhrase(String beforeReplace, String modleId){
		try {
			String status = null;
			if (modleId != null && !"".equals(modleId)
					&& !"null".equals(modleId)) {
				ToaSysmanageFiltrateModule modle = this.get(modleId);
				if(modle !=null){
					status = modle.getFiltrateOpenstate();					
				}
			}
			if (status != null && status.equals(open)) {
				if (beforeReplace != null && !"".equals(beforeReplace)
						&& !"null".equals(beforeReplace)) {
					String badwords = phraseManage.getBadWords();
					String replacedWords = phraseManage.getRepWords();
					if(!"".equals(badwords)){
						String[] badWordStr = badwords.split(",");
						String[] replaceWordStr = replacedWords.split(",");
						for (int i = 0; i < badWordStr.length; i++) { 
							int dot = beforeReplace.indexOf(badWordStr[i]);
							if (dot != -1) {
								beforeReplace = beforeReplace.replace(
										badWordStr[i], replaceWordStr[i]);
							}
						}
					}
				} else {
					beforeReplace = "";
				}
			}	
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return beforeReplace;
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-5上午09:12:53
	 * @desc: 根据模块id获取该模块过滤的开关状态
	 * @param String
	 *            modleId 模块id
	 * @return String 返回开关状态
	 */
	public String getStatus(String modleId) throws SystemException,
			ServiceException {
		String status = "";
		try {
			List<ToaSysmanageFiltrateModule> list = phraseFilterDao.find(
					"from ToaSysmanageFiltrateModule t where t.moduleId=?",
					modleId);
			ToaSysmanageFiltrateModule modle = list.get(0);
			status = modle.getFiltrateOpenstate();
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "词语过滤开关" });
		}
		return status;
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-5上午09:12:53
	 * @desc: 将用符号隔开的字符串，转换成字符串数组
	 * @param String
	 *            str 需处理的字符串
	 * @param String
	 *            sign 符号
	 * @return String 字符串数组
	 */
	public static String[] getStringData(String str, String sign) {
		String[] strData = null;
		try{		
			StringTokenizer st1 = new StringTokenizer(str, sign);
			strData = new String[st1.countTokens()];
			int i = 0;
			while (st1.hasMoreTokens()) {
				strData[i] = st1.nextToken().trim();
				i++;
			}		
		}catch(ServiceException e){
			e.printStackTrace();
		}	
		return strData;
	}

	@Autowired
	public void setPhraseManage(IPhraseManage phraseManage) {
		this.phraseManage = phraseManage;
	}
}
