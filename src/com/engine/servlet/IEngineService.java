package com.engine.servlet;

import java.util.Map;

/**
 * 
 * Project : StrongIPP
 * Copyright : Strong Digital Technology Co. Ltd.
 * All right reserved
 * @author 曹钦
 * @version 5.0, 2011-8-30
 * Description: 前台事件响应接口
 */

public interface IEngineService{
	
	public String getParse(Map paras);
	public String postParse();
	
}
