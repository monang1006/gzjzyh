package com.strongit.oa.viewmodel.util;

import java.io.File;
import java.io.FileFilter;

/**
 * <p>Title: NoHidenFileFilter.java</p>
 * <p>Description: 文件过滤类</p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @date 	 2010-06-18
 * @version  1.0
 */
public class NoHidenFileFilter implements FileFilter{

	public boolean accept(File file) {
		// TODO Auto-generated method stub
		if(!file.isHidden()&&file.isDirectory()&&file.isDirectory()){
			return true;
		}else{
			return false;
		}
	}

}
