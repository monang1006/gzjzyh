package com.strongit.bo;

import java.util.List;

public class OperateList {
	public List addObj(List list,Channel channel){
		list.add(channel);
		return list;
	}
	
	public List delObj(List list,String objid){
		List operList=list;
		if(operList!=null){
			for(int i=0;i<operList.size();i++){
				if(objid.equals(((Channel)(operList.get(i))).getBlockid())){
					operList.remove(i);
					break;
				}
			}
		}
		return operList;
	}
	
	public Channel findObj(List list,String objid){
		List operList=list;
		if(operList!=null){
			for(int i=0;i<operList.size();i++){
				if(objid.equals(((Channel)(operList.get(i))).getBlockid())){
					//System.out.println(((Channel)(operList.get(i))).getBlockid());
					return (Channel)operList.get(i);
				}
			}
		}
		return null;		
	}
	
	public List updateObj(List list,Channel obj){
		List operList=list;
		if(operList!=null){
			for(int i=0;i<operList.size();i++){
				if((obj.getBlockid()).equals(((Channel)(operList.get(i))).getBlockid())){
					operList.set(i, obj);
					break;
				}
			}
		}
		return operList;
	}
	
}
