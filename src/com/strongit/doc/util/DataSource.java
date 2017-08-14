package com.strongit.doc.util;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import com.strongit.oa.bo.ToaReportBean;
/**
 * 
 * @company  Strongit
 * @author 	 彭小青
 * @date 	 Jan 28, 2010 3:15:23 PM
 * @version  2.0.4
 * @comment
 */
public class DataSource implements JRDataSource{
	/**
     *测试数据，实际项目中是动态获取，也不一定是数组，可以是其它的数据类型.
     */
    private List<ToaReportBean> data=null;
    
    private int index = -1;
    
    public DataSource(List<ToaReportBean> data){	
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
       
       String fieldName = field.getName();
       if ("text1".equals(fieldName)){
           value = data.get(index).getText1();
       }else if ("text2".equals(fieldName)){
           value = data.get(index).getText2();
       }else if ("text3".equals(fieldName)){
           value = data.get(index).getText3();
       }else if ("text4".equals(fieldName)){
           value = data.get(index).getText4();
       }else if ("text5".equals(fieldName)){
           value = data.get(index).getText5();
       }else if ("text6".equals(fieldName)){
           value = data.get(index).getText6();
       }else if ("text7".equals(fieldName)){
           value = data.get(index).getText7();
       }else if ("text8".equals(fieldName)){
           value = data.get(index).getText8();
       }else if ("text9".equals(fieldName)){
           value = data.get(index).getText9();
       }else if ("text10".equals(fieldName)){
           value = data.get(index).getText10();
       }else if ("text11".equals(fieldName)){
           value = data.get(index).getText11();
       }else if ("text12".equals(fieldName)){
           value = data.get(index).getText12();
       }else if ("text13".equals(fieldName)){
           value = data.get(index).getText13();
       }else if ("text14".equals(fieldName)){
           value = data.get(index).getText14();
       }else if ("text15".equals(fieldName)){
           value = data.get(index).getText15();
       }else if ("text16".equals(fieldName)){
           value = data.get(index).getText16();
       }else if ("text17".equals(fieldName)){
           value = data.get(index).getText17();
       }else if ("text18".equals(fieldName)){
           value = data.get(index).getText18();
       }else if ("text19".equals(fieldName)){
           value = data.get(index).getText19();
       }else if ("text20".equals(fieldName)){
           value = data.get(index).getText20();
       }else if ("text21".equals(fieldName)){
           value = data.get(index).getText21();
       }else if ("text22".equals(fieldName)){
           value = data.get(index).getText22();
       }else if ("text23".equals(fieldName)){
           value = data.get(index).getText23();
       }else if ("text24".equals(fieldName)){
           value = data.get(index).getText24();
       }else if ("text25".equals(fieldName)){
           value = data.get(index).getText25();
       }else if ("text26".equals(fieldName)){
           value = data.get(index).getText26();
       }else if ("text27".equals(fieldName)){
           value = data.get(index).getText27();
       }else if ("text28".equals(fieldName)){
           value = data.get(index).getText28();
       }else if ("text29".equals(fieldName)){
           value = data.get(index).getText29();
       }else if ("text30".equals(fieldName)){
           value = data.get(index).getText30();
       } else if ("text31".equals(fieldName)){
           value = data.get(index).getText31();
       } else if ("text32".equals(fieldName)){
           value = data.get(index).getText32();
       } else if ("text33".equals(fieldName)){
           value = data.get(index).getText33();
       } else if ("text34".equals(fieldName)){
           value = data.get(index).getText34();
       } else if ("text35".equals(fieldName)){
           value = data.get(index).getText35();
       } else if ("text36".equals(fieldName)){
           value = data.get(index).getText36();
       } else if ("text37".equals(fieldName)){
           value = data.get(index).getText37();
       } else if ("text38".equals(fieldName)){
           value = data.get(index).getText38();
       } else if ("text39".equals(fieldName)){
           value = data.get(index).getText39();
       } else if ("text40".equals(fieldName)){
           value = data.get(index).getText40();
       } else if ("text41".equals(fieldName)){
           value = data.get(index).getText41();
       } else if ("text42".equals(fieldName)){
           value = data.get(index).getText42();
       } else if ("text43".equals(fieldName)){
           value = data.get(index).getText43();
       } else if ("text44".equals(fieldName)){
           value = data.get(index).getText44();
       } else if ("text45".equals(fieldName)){
           value = data.get(index).getText45();
       }     
       return value;
    }
}
