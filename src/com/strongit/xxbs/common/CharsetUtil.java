package com.strongit.xxbs.common;

public class CharsetUtil
{
	/**
	 * 判断文件的编码格式
	 * @author 钟伟
	 * @date 2012-7-13
	 * @param
	 * @return
	 */
	public static String getCharset(byte[] bys)
	{
		String code = "gbk";
		if (bys[0] == -1 && bys[1] == -2 )  
            code = "UTF-16";  
		else if (bys[0] == -2 && bys[1] == -1 )  
            code = "Unicode";  
		else if(bys[0]==-17 && bys[1]==-69 && bys[2] ==-65)  
            code = "UTF-8";			
		return code;
	}
}
