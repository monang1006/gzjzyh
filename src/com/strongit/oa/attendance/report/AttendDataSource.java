package com.strongit.oa.attendance.report;

import java.util.List;

import com.strongit.oa.attendance.register.MyRecord;
import com.strongit.oa.bo.ToaReportBean;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class AttendDataSource implements JRDataSource {

	/**
     *测试数据，实际项目中是动态获取，也不一定是数组，可以是其它的数据类型.
     */
    private List<MyRecord> data=null;
    
    private int index = -1;
    
    public AttendDataSource(List<MyRecord> data){	
    	this.data=data; 
    }
    /**
     *实现了JRDataSource中的方法．判断是否还有下一个．
     */
    public boolean next() throws JRException
    {
       index++;
       return (index < data.size());
    }
    /**
     *实现了JRDataSource中的方法．
     *@paramfield是对应报表中的要填充的字段的名称．
     */
    public Object getFieldValue(JRField field) throws JRException
    {
       Object value = null;
       List<Object> list=data.get(index).getWorkList();
       String fieldName = field.getName();
       if(fieldName!=null&&"attendTime".equals(fieldName)){
    	   value = data.get(index).getAttendTime();
       }else if(fieldName!=null&&"userName".equals(fieldName)){
    	   value = data.get(index).getUserName();
       }else if(fieldName!=null&&"attendLaterTime".equals(fieldName)){
    	   value = data.get(index).getAttendLaterTime();
       }else if(fieldName!=null&&"attendEarlyTime".equals(fieldName)){
    	   value = data.get(index).getAttendEarlyTime();
       }else if(fieldName!=null&&"absenceHours".equals(fieldName)){
    	   value = data.get(index).getAbsenceHours();
       }else if(fieldName!=null&&"attendDesc".equals(fieldName)){
    	   if(data.get(index).getAttendDesc()!=null&&!"".equals(data.get(index).getAttendDesc())){
    	   value = data.get(index).getAttendDesc();
    	   }else{
    		   value="  ";
    	   }
       }else if(fieldName!=null&&"shouldAttendHours".equals(fieldName)){
    	   value = data.get(index).getShouldAttendHours();
       }else if(fieldName!=null&&"jiaBanHours".equals(fieldName)){
    	   value = data.get(index).getJiaBanHours();
       }
       else {
    	   for(int i=0;i<list.size();i++){
    		   if(fieldName!=null&&fieldName.equals("test"+(i+1)+"")){
    			   value=list.get(i);
    		   }
    	   }
       }
       if(value==null||"".equals(value)){
    	   value="  ";
       }
       return value;
    }

}
