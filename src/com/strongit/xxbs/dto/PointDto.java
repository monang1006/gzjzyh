package com.strongit.xxbs.dto;

import java.math.BigDecimal;
import java.math.BigInteger;


public class PointDto implements Comparable<PointDto>
{
	private String ORGID = "";
	private String SUBDATE = "";
	private BigDecimal SCORE= BigDecimal.valueOf(0) ;
	private String ORGNAME= "";
	private String CODE = "";
	private Integer COUNT;
	public String getOrgid() {
		return ORGID;
	}
	public void setOrgid(String orgid) {
		this.ORGID = orgid;
	}
	public String getSubdate() {
		return SUBDATE;
	}
	public void setSubdate(String subdate) {
		this.SUBDATE = subdate;
	}
	public BigDecimal getScore() {
		return SCORE;
	}
	public void setScore(BigDecimal score) {
		this.SCORE = score;
	}
	public String getOrgname() {
		return ORGNAME;
	}
	public void setOrgname(String orgname) {
		this.ORGNAME = orgname;
	}
	public String getCode() {
		return CODE;
	}
	public void setCode(String code) {
		this.CODE = code;
	}
	public Integer getCOUNT() {
		return COUNT;
	}
	public void setCOUNT(Integer cOUNT) {
		COUNT = cOUNT;
	}
	@Override
	public int hashCode()
	{
		return 7*ORGID.hashCode() + 11*SUBDATE.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj) return true;
		
		if(obj == null) return false;
		
		if(!(obj instanceof PointDto)) return false;
		
		PointDto oObj = (PointDto) obj;
		return ORGID.equals(oObj.ORGID)&&(SUBDATE.equals(oObj.SUBDATE));
	}
	
	@Override
	public String toString() {
		return ORGNAME.toString()+":"+SCORE.toString();
	}
	public int compareTo(PointDto o) {
		return -SCORE.compareTo(o.SCORE);
	}

	
	
	
	
}
