package com.strongit.utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import com.strongit.xxbs.common.PathUtil;


public class TimeLog
{
	public static void log(int id, String str, Date date)
	{
		String dir = PathUtil.wordPath();
		File file = new File(dir + "1.txt");
		/*try
		{
			str  = id + " : " + date.getTime() + " : " + str;
			FileUtils.writeStringToFile(file, str+"\r\n", true);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}*/
	}
}
