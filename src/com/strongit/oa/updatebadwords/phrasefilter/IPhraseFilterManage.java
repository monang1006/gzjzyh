/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：词语过滤管理接口
 */

package com.strongit.oa.updatebadwords.phrasefilter;

import com.strongit.oa.bo.ToaSysmanageFiltrateModule;
import com.strongit.oa.updatebadwords.phrasemanage.IPhraseManage;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

/**
 * @author pengxq
 * @version 1.0
 */
public interface IPhraseFilterManage {

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
	public String filterPhrase(String beforeReplace, String modleId);

	/**
	 * @author：pengxq
	 * @time：2009-1-5上午09:12:53
	 * @desc: 根据模块id获取该模块过滤的开关状态
	 * @param String
	 *            modleId 模块id
	 * @return String 返回开关状态
	 */
	public String getStatus(String modleId) throws SystemException,
			ServiceException;

	/**
	 * @author：pengxq
	 * @time：2009-1-4下午09:12:53
	 * @desc: 保存过滤对象
	 * @param ToaSysmanageFiltrateModule
	 *            filterModle 过滤对象
	 * @return void
	 */
	public String save(ToaSysmanageFiltrateModule filterModle)
			throws SystemException, ServiceException;

	/**
	 * @author：pengxq
	 * @time：2009-1-4下午09:12:53
	 * @desc: 根据主键获取过滤对象
	 * @param String
	 *            id 主键
	 * @return ToaSysmanageFiltrateModule 过滤对象
	 */
	public ToaSysmanageFiltrateModule get(String id) throws SystemException,
			ServiceException;

	/**
	 * @author：pengxq
	 * @time：2009-1-4下午09:12:53
	 * @desc: 获取分页列表
	 * @param Page
	 *            page 分页对象
	 * @return Page 分页列表
	 */
	public Page getAll(Page page) throws SystemException, ServiceException;

	/**
	 * @author：pengxq
	 * @time：2009-1-4下午09:12:53
	 * @desc: 注册词语管理manger
	 * @param IPhraseManage
	 *            phraseManage 词语管理manager
	 * @return Page void
	 */
	public void setPhraseManage(IPhraseManage phraseManage);
}
