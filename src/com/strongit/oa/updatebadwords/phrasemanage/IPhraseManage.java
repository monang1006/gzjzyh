/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：词语管理接口
*/

package com.strongit.oa.updatebadwords.phrasemanage;

import java.util.Date;
import java.util.List;

import com.strongit.oa.bo.ToaSysmanageFiltrate;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

/**
 * @author pengxq
 * @version 1.0
 */
public interface IPhraseManage 
{
   
   /**
     * @author：pengxq
     * @time：2009-1-4下午09:09:38
     * @desc: 获取词语表中所有不良词语
     * @param
     * @return String 不良词语串
    */
   public String getBadWords() throws SystemException, ServiceException;
   
   /**
     * @author：pengxq
     * @time：2009-1-4下午09:08:29
     * @desc: 获取词语表中所有替代词语
     * @param
     * @return String 替代词语串
    */
   public String getRepWords() throws SystemException, ServiceException ;
   
   /**
     * @author：pengxq
     * @time：2009-1-4下午09:10:12
     * @desc: 获取所有分页词语列表
     * @param Page<ToaSysmanageFiltrate> page 分页对象
     * @return Page 分页词语列表
    */
   public Page<ToaSysmanageFiltrate> getAllPhrase(Page<ToaSysmanageFiltrate> page)  throws SystemException, ServiceException;
   
   /**
     * @author：pengxq
     * @time：2009-1-4下午09:11:08
     * @desc: 根据查询条件查询
     * @param Page<ToaSysmanageFiltrate> page 分页对象
     * @param String filtrateWord 不良词语
     * @param String filtrateRaplace 替代词语
     * @param Date filtrateTime 词语添加日期
     * @return Page 分页词语列表
    */
   public Page<ToaSysmanageFiltrate> searchPhrase(Page<ToaSysmanageFiltrate> page,String filtrateWord,String filtrateRaplace, Date filtrateTime) throws SystemException, ServiceException; 
   
   /**
     * @author：pengxq
     * @time：2009-1-4下午09:12:53
     * @desc: 保存词语对象
     * @param ToaSysmanageFiltrate phrase 词语对象
     * @return void
    */
   public String save(ToaSysmanageFiltrate phrase) throws SystemException, ServiceException;
   
   /**
     * @author：pengxq
     * @time：2009-1-4下午09:13:55
     * @desc: 根据主键删除对象
     * @param String id 主键
     * @return void
    */
   public void delete(String id) throws SystemException, ServiceException;
   
   /**
     * @author：pengxq
     * @time：2009-1-4下午09:14:31
     * @desc: 根据主键获取词语对象
     * @param String id 主键
     * @return ToaSysmanageFiltrate 词语对象 
    */
   public ToaSysmanageFiltrate getPhrase(String id)  throws SystemException, ServiceException;
   
   /**
	 * 
	  * @author：pengxq
	  * @time：2009-6-8上午10:17:25
	  * @desc: 
	  * @param
	  * @return
	 */
	public List getObject(String badword,String id) throws SystemException, ServiceException;
}
