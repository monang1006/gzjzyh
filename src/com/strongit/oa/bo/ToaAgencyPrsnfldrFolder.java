package com.strongit.oa.bo;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("AGENCY")
public class ToaAgencyPrsnfldrFolder extends ToaPrsnfldrFolder {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5812760992730241318L;

	public ToaAgencyPrsnfldrFolder(){
		
	}
}
