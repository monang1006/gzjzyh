package com.strongit.oa.bo;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.strongit.oa.bo.ToaPrsnfldrFolder;

@Entity
@DiscriminatorValue("DEPARTMENT")
public class ToaDepartmentPrsnfldrFolder extends ToaPrsnfldrFolder {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 768738378704139235L;

	public ToaDepartmentPrsnfldrFolder(){
		
	}
}
