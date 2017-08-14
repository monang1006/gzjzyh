package com.strongit.xxbs.common.contant;

/**
 * @author 钟伟
 * @version 1.0, 2011-9-28
 */
public class Info
{
	//用于保存事件成功后，传递成功信息及关闭弹出窗口
	public static final String SUCCESS_AND_CLOSE ="<html><head><title>info</title></head><body><script type='text/javascript'>parent.window.returnValue='success';parent.window.close();</script></body></html>";

	public static final String SUCCESS  = "success";
	
	//附件过大提示
	public static final String NO_UPLOAD ="<html><head><title>info</title></head><body><script type='text/javascript'>alert('附件大小不能超过20M');parent.window.returnValue='success';parent.window.close();</script></body></html>";
}
