package com.strongit.workflow.workflowreport;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-9 下午03:01:58
 * Autour: lanlc
 * Version: V1.0
 * Description：为导出报表填充数据
 */
public class MyDataSource implements JRDataSource {
	
	public MyDataSource(List data){
		datas = data;
	}
	private List datas = null ;
	private int loop = -1 ; 
	/**
	 * author:lanlc
	 * description:
	 * modifyer:
	 */
	public Object getFieldValue(JRField field) throws JRException {		
		WorkFlowTypeDataBean temp = (WorkFlowTypeDataBean)this.datas.get(loop);
		Object rs = "" ;
		if("processId".equals(field.getName())){  
		  rs = temp.getProcessId();   
		}else if("processName".equals(field.getName())){
		  rs = temp.getProcessName();
		}else if("processTodo".equals(field.getName())){
		  rs = temp.getProcessTodo();
		}else if("processDone".equals(field.getName())){
		   rs = temp.getProcessDone();
		}
		temp = null ;  
		return rs;
	}
	
	/**
	 * author:lanlc
	 * description:
	 * modifyer:
	 */
	public boolean next() throws JRException {
		loop ++ ;
		if(loop >= datas.size()){
		  return false;
		}else{   
		  return true ;
		} 
		
	}
	
	public void finalized(){
		datas = null ;  
	}

	public List getDatas() {
		return datas;
	}

	public void setDatas(List datas) {
		this.datas = datas;
	}


}
