package com.strongit.oa.bookmark.util;

import java.util.LinkedList;
import java.util.List;

public class Select { 

	List<Option> options = new LinkedList<Option>();
	
	public void add(Option option){
		options.add(option);
	}

	public void addAll(List<Option> option){
		options.addAll(option);
	}
	
	public int indexOf(Option option){
		return options.indexOf(option);
	}

	public void set(int index,Option option){
		options.set(index, option);
	}

	public Option get(int index){
		return options.get(index);
	}

	public Option get(Option option){
		for(Option o : this.getOptions()){
			if(option.getId().equals(o.getId())){
				return o;
			}
		}
		return null;
	}

	public Option get(String id){
		for(Option o : this.getOptions()){
			if(id.equals(o.getId())){
				return o;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		StringBuilder select = new StringBuilder("<select>");
		for(Option option : this.getOptions()){
			select.append(option).append("\n"); 
		}
		select.append("</select>");
		return select.toString();
	}

	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}
}
