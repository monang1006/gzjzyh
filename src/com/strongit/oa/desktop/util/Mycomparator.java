package com.strongit.oa.desktop.util;

import java.util.Comparator;

import com.strongit.oa.bo.ToaDesktopSection;

public class Mycomparator implements Comparator{

	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		ToaDesktopSection p1=(ToaDesktopSection)o1;
		ToaDesktopSection p2=(ToaDesktopSection)o2; 
		int flag=p1.getSectionType().compareTo(p2.getSectionType());
		if(flag==0){
			return p1.getSectionName().compareTo(p2.getSectionName());
		}else{
			return flag;
		}
		  
	}
}
