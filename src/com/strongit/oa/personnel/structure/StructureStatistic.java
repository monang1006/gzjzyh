package com.strongit.oa.personnel.structure;

import com.strongit.oa.bo.ToaBaseOrg;
/**
 * 编制情况统计辅助类
 * @author 胡丽丽
 * date:2009-10-19 16:45:00
 *
 */
public class StructureStatistic {
	/** 机构信息*/
	private ToaBaseOrg toabaseorg;
	/** 应有人数*/
	private int properPerson;
	/** 实际人数*/
    private int realityPerson;
    /** 超编人数*/
    private int outPerson;
	public ToaBaseOrg getToabaseorg() {
		return toabaseorg;
	}
	public void setToabaseorg(ToaBaseOrg toabaseorg) {
		this.toabaseorg = toabaseorg;
	}
	public int getProperPerson() {
		return properPerson;
	}
	public void setProperPerson(int properPerson) {
		this.properPerson = properPerson;
	}
	public int getRealityPerson() {
		return realityPerson;
	}
	public void setRealityPerson(int realityPerson) {
		this.realityPerson = realityPerson;
	}
	public int getOutPerson() {
		return outPerson;
	}
	public void setOutPerson(int outPerson) {
		this.outPerson = outPerson;
	}
}
