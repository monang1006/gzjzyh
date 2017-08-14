//Source file: F:\\workspace\\StrongOA2.0_DEV\\src\\com\\strongit\\oa\\prsnfldr\\privateprsnfldr\\ToaPrivatePrsnfldrFolder.java

package com.strongit.oa.bo;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.strongit.oa.bo.ToaPrsnfldrFolder;

@Entity
@DiscriminatorValue("PRIVATE")
public class ToaPrivatePrsnfldrFolder extends ToaPrsnfldrFolder {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8784044027005699303L;

	
	private String isShare;

	/**
	 * @roseuid 493DDBAC0167
	 */
	public ToaPrivatePrsnfldrFolder() {

	}

	/**
	 * @roseuid 493C705B00CB
	 */
	public void ToaPrsnfldrPrivateFolder() {

	}

	@Column(name="FOLDER_ISSHARE")
	public String getIsShare() {
		return isShare;
	}

	public void setIsShare(String isShare) {
		this.isShare = isShare;
	}
}
