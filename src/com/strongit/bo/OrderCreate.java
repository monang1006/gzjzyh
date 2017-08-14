package com.strongit.bo;

import java.util.ArrayList;
import java.util.List;

public class OrderCreate {
	//col_1:drag_4183,drag_4160,drag_4185,;col_2:drag_4190,drag_4201,drag_4181,;col_3:drag_4187,drag_4179,drag_4182,;
	public static String[] createOrder(String orderStr){
		String[] order=null;
		if(orderStr==null||"".equals(orderStr)){
			return null;
		}else{
			String temp[]=orderStr.split(";");
			order=new String[temp.length];
			for(int i=0;i<temp.length;i++){
				if((temp[i].indexOf(":"))==(temp[i].length()-1)){
					order[i]="null";
				}else{
					String idcore=temp[i].substring(temp[i].indexOf(":")+1, temp[i].length()-1);
					order[i]=idcore;
					//System.out.println(idcore);
				}

			}
			return order;
		}
	}
	
	public static List getColumn(List objList,String col){
		List list=new ArrayList();
		if(col==null||"null".equals(col)||"".equals(col)){
			return null;
		}else{
			String id[]=col.split(",");
			for(int i=0;i<id.length;i++){
				String idnum=id[i].substring(id[i].indexOf("_")+1,id[i].length());
				OperateList oper=new OperateList();
				Channel temp=oper.findObj(objList, idnum);
				if(temp!=null){
					list.add(temp);
				}
			}
			return list;
		}
	}
	
	public static void main(String args[]){
		OrderCreate.createOrder("col_1:drag_3,drag_2,drag_1,drag_5,drag_4,;col_2:drag_6,;");
	}

}
