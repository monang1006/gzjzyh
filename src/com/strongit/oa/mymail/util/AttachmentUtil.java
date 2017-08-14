package com.strongit.oa.mymail.util;

import java.io.File;

public class AttachmentUtil {
	
	public static boolean chargeFileEmpty(File[] file){
		boolean isNotEmpty=true;
		for(int i=0;i<file.length;i++){
			if(file[i].length()==0){
				isNotEmpty=false;
				break;
			}
		}
		return isNotEmpty;
	}

}
