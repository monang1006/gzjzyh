/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：年内文件接口
*/

package com.strongit.oa.archive;

import com.strongit.oa.bo.ToaArchiveTempfile;
import com.strongit.oa.util.OALogInfo;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;


/**
 * 
 * @author pengxq
 * 年内文件接口
 *
 */
public interface ITempFileService 
{
	
	 /**
	    * @author：pengxq
	    * @time：2008-12-29下午06:21:44
	    * @desc：保存年内文件
	    * @param ToaArchiveTempfile toaArchiveTempfile 年内文件对象
	    * @return void
	    */
	   public void saveTempfile(ToaArchiveTempfile toaArchiveTempfile,OALogInfo ... loginfos)throws SystemException,ServiceException;
  
}
