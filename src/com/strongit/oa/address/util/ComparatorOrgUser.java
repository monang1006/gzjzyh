package com.strongit.oa.address.util;

import java.util.Comparator;

import com.strongit.uums.bo.TUumsBaseUser;

public class ComparatorOrgUser implements Comparator<Object>{

	public int compare(Object o1, Object o2) {
		TUumsBaseUser user0 = (TUumsBaseUser)o1;
		TUumsBaseUser user1 = (TUumsBaseUser)o2;
		if(user0.getUserSequence()!=null && user1.getUserSequence()!=null){
			return user0.getUserSequence().compareTo(user1.getUserSequence());			
		}
		return 0;
	}

}
