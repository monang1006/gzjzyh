package com.strongit.oa.senddoc.pojo;

public class WorkflowParameter {
	private StringBuilder customSelectItems;

	private StringBuilder customFromItems;

	private StringBuilder customQuery;

	private StringBuilder customOrderBy ;
	public StringBuilder getCustomFromItems() {
		return customFromItems;
	}

	public void setCustomFromItems(StringBuilder customFromItems) {
		this.customFromItems = customFromItems;
	}

	public StringBuilder getCustomQuery() {
		return customQuery;
	}

	public void setCustomQuery(StringBuilder customQuery) {
		this.customQuery = customQuery;
	}

	public StringBuilder getCustomSelectItems() {
		return customSelectItems;
	}

	public void setCustomSelectItems(StringBuilder customSelectItems) {
		this.customSelectItems = customSelectItems;
	}

	public StringBuilder getCustomOrderBy() {
		return customOrderBy;
	}

	public void setCustomOrderBy(StringBuilder customOrderBy) {
		this.customOrderBy = customOrderBy;
	}
}
